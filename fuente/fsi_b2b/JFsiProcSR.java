package fsi_b2b;

import nl.captcha.Captcha;
//import nl.captcha.audio.AudioCaptcha;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.lang.mutable.MutableInt;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import forseti.JBDRegistradasSet;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JCRMCompaniesSet;
import forseti.sets.JCRMContactsSet;
import forseti.sets.JNotasBlocksIdsSet;

@SuppressWarnings("serial")
public class JFsiProcSR extends HttpServlet 
{
	private static String m_BD_Eje = "";
	private static String m_GU_workarea = ""; 
	private static boolean m_bEje = false;
	
	@Override
	public void init() 
	{
		//Checa si existe Empresa EJE y CRM EJE de esta empresa
		//System.out.println("INIT");
		JAdmVariablesSet eje = new JAdmVariablesSet(null);
		eje.ConCat(true);
		eje.m_Where = "ID_Variable = 'BD-EJE'";
		eje.Open();
		JBDRegistradasSet bd = new JBDRegistradasSet(null);
		bd.ConCat(true);
		bd.m_Where = "Nombre = '" + eje.getAbsRow(0).getVAlfanumerico() + "'";
		bd.Open();
		if(bd.getNumRows() > 0)
		{
			//System.out.println("BD OK:" + eje.getAbsRow(0).getVAlfanumerico());
			JAdmVariablesSet crm = new JAdmVariablesSet(null);
			crm.ConCat(true);
			crm.m_Where = "ID_Variable = 'CRM-EJE'";
			crm.Open();
			JNotasBlocksIdsSet blk = new JNotasBlocksIdsSet(null,"cef-su",Integer.toString(crm.getAbsRow(0).getVEntero())); 
			blk.setBD(eje.getAbsRow(0).getVAlfanumerico());
			blk.ConCat(3);
			blk.Open();
			if(blk.getNumRows() > 0)
			{
				//System.out.println("CRM OK");
				m_bEje = true;
				m_GU_workarea = blk.getAbsRow(0).getGU_Workarea();
				m_BD_Eje = eje.getAbsRow(0).getVAlfanumerico();
			}
		}
		
	}	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException 
	{
		if(!m_bEje)
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><body><h1>Lo sentimos, este servidor no contiene una base de datos eje para el CRM de seguimiento</h1></body></html>");
			return;
		}
		
		MutableInt idmensaje = new MutableInt(-1); StringBuffer mensaje = new StringBuffer("");
		RequestDispatcher despachador;
		if(request.getParameter("proceso").equals("AGREGAR_EMPRESA"))
			despachador = getServletContext().getRequestDispatcher("/forsetiprocsr/AgregarEmpresa.jsp");
		else if(request.getParameter("proceso").equals("RENTAR_INSTANCIA") ||
					request.getParameter("proceso").equals("RENTAR_ESPACIO") ||
						request.getParameter("proceso").equals("SOLICITAR_SOPORTE"))
			despachador = getServletContext().getRequestDispatcher("/forsetiprocsr/CRMOptSR.jsp");
		else
		{ 
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			JUtil.RDP("SAF","FORSETI_ADMIN","AL", "", "SYS_PROC_SR", "????||||", JUtil.q(JUtil.depurarParametros(request)));
			out.println("<html><body><h1>El Servidor ha registrado sus requerimientos... Parece ser que no es válido, procederemos a investigar el motivo y obtendrá una respuesta a la brevedad</h1></body></html>");
			return;
		}
			
		HttpSession session = request.getSession(true);
	    	
		Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
		request.setCharacterEncoding("UTF-8");
		String answer = request.getParameter("answer");
		if(!captcha.isCorrect(answer)) 
		{
			idmensaje.setValue(3); mensaje.append("ERROR: El texto verificador es incorrecto, no corresponde con la imagen... Vuelve a intentarlo"); 
			session.removeAttribute(Captcha.NAME);
			request.setAttribute("idmensaje", idmensaje);
			request.setAttribute("mensaje", mensaje);
			despachador.forward(request,response);
			return;
		} 
		
		if(request.getParameter("polprv") == null)
		{
			idmensaje.setValue(3); mensaje.append("Para poder registrarte, debes aceptar nuestra política de privacidad"); 
			session.removeAttribute(Captcha.NAME);
			request.setAttribute("idmensaje", idmensaje);
			request.setAttribute("mensaje", mensaje);
			despachador.forward(request,response);
			return;
		}
					
		if(request.getParameter("proceso").equals("AGREGAR_EMPRESA"))
		{
			if(VerificarParametrosEmpresa(request, response, idmensaje, mensaje))
	        {
				if(AgregarEmpresa(request, idmensaje, mensaje))
	            	GestionarOportunidad(request, response, "3", "Instancia o Espacio. Empresa de prueba creada", "'Este usuario ha registrado la base de datos " + JUtil.p(request.getParameter("nombre")) + "'",mensaje);
	            else
	               	session.removeAttribute(Captcha.NAME);
	        }
			else
				session.removeAttribute(Captcha.NAME);
		}
		else if(request.getParameter("proceso").equals("RENTAR_INSTANCIA"))
		{
			if(VerificarParametrosOportunidad(request, response, idmensaje, mensaje))
			{
				String tipo = request.getParameter("tipo");
				if(GestionarOportunidad(request, response, (tipo.equals("INFO") ? "1" : "3"), 
						(tipo.equals("INFO") ? "Mas Información sobre Renta de Instancia " : "Apartado de Instancia " + JUtil.p(tipo)), null, mensaje))
				{
					idmensaje.setValue(0);
					mensaje.append("Tu solicitud de Renta de Instancia ha sido registrada.");
				}
				else
					idmensaje.setValue(3);
			}
			else
				session.removeAttribute(Captcha.NAME);           
		}
		else if(request.getParameter("proceso").equals("RENTAR_ESPACIO"))
		{
			if(VerificarParametrosOportunidad(request, response, idmensaje, mensaje))
			{
				String tipo = request.getParameter("tipo");
				if(GestionarOportunidad(request, response, (tipo.equals("INFO") ? "1" : "3"), 
						(tipo.equals("INFO") ? "Mas Información sobre Renta de Espacio " : "Apartado de Espacio"), null, mensaje))
				{
					idmensaje.setValue(0);
					mensaje.append("Tu solicitud de Renta de Espacio ha sido registrada.");
				}
				else
					idmensaje.setValue(3);
			}
			else
				session.removeAttribute(Captcha.NAME);           
		}
		else if(request.getParameter("proceso").equals("SOLICITAR_SOPORTE"))
		{
			if(VerificarParametrosOportunidad(request, response, idmensaje, mensaje))
			{
				String tipo = request.getParameter("tipo");
				if(GestionarOportunidad(request, response, "2", JUtil.p(tipo), null, mensaje))
				{
					idmensaje.setValue(0);
					mensaje.append("Tu solicitud de Soporte ha sido registrada.");
				}
				else
					idmensaje.setValue(3);
			}
			else
				session.removeAttribute(Captcha.NAME);           
		}
		else
		{
			
		} 
		request.setAttribute("idmensaje", idmensaje);
		request.setAttribute("mensaje", mensaje);
		despachador.forward(request,response);
		return;
	}

	public boolean VerificarParametrosContacto(HttpServletRequest request, HttpServletResponse response, MutableInt idmensaje, StringBuffer mensaje)
		      throws ServletException, IOException
	{
		// Verificacion
		if(request.getParameter("tx_name") != null && !request.getParameter("tx_name").equals("") &&
				request.getParameter("tx_surname") != null && !request.getParameter("tx_surname").equals("") &&
				request.getParameter("tx_email") != null && !request.getParameter("tx_email").equals("") )
		{
			if(!request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			{
				idmensaje.setValue(3); mensaje.append("ERROR: El Correo electónico esta mal, puede que contenga caracteres no validos");
				return false;
			}
		    	  
			return true;
		}
		else
		{
			idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
			return false;
		}
	}

	public boolean VerificarParametrosEmpresa(HttpServletRequest request, HttpServletResponse response, MutableInt idmensaje, StringBuffer mensaje)
		      throws ServletException, IOException
	{
		// Verificacion
		if(request.getParameter("nombre") != null && request.getParameter("nm_legal") != null
			&& request.getParameter("direccion") != null  && request.getParameter("poblacion") != null
			&& request.getParameter("cp") != null  && request.getParameter("tx_email") != null && !request.getParameter("tx_email").equals("")
			&& request.getParameter("web") != null
			&& request.getParameter("password") != null  && request.getParameter("confpwd") != null &&
			!request.getParameter("nombre").equals("") && !request.getParameter("nm_legal").equals("") &&
			!request.getParameter("password").equals("") && !request.getParameter("confpwd").equals("") &&
			// Contacto
			request.getParameter("tx_name") != null && !request.getParameter("tx_name").equals("") &&
		  	request.getParameter("tx_surname") != null && !request.getParameter("tx_surname").equals(""))
		{
			if(!request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
	    	{
	    		  idmensaje.setValue(3); mensaje.append("ERROR: El Correo electónico esta mal, puede que contenga caracteres no validos");
	    		  return false;
	    	}
			
			if(!request.getParameter("nombre").matches("[A-Z]{4,20}"))
			{
				idmensaje.setValue(3); mensaje.append(JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR3",2));//"ERROR: El nombre de la base de datos debe de constar de entre 4 y 20 caracteres de la A a la Z mayusculas";
				return false;  
			}
			
			if(request.getParameter("nm_legal").length() < 3 || request.getParameter("nm_legal").length() > 254)
			{
				idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO") + ": " + JUtil.Msj("GLB","GLB","GLB","COMPANIA")); 
				return false; 
			}
				
			if(request.getParameter("password").length() < 3 || request.getParameter("password").length() > 30)
			{
				idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO") + ": " + JUtil.Msj("GLB","GLB","GLB","PASSWORD")); 
				return false; 
			}
			if(request.getParameter("confpwd").length() < 3 || request.getParameter("confpwd").length() > 30)
			{
				idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO") + ": " + JUtil.Msj("GLB","GLB","GLB","PASSWORD",2)); 
				return false; 
			}		
			
			if(!request.getParameter("password").equals(request.getParameter("confpwd")))
			{
				idmensaje.setValue(3); mensaje.append(JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 1));
				return false;
			}
			return true;
		}
		else
		{
			idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
			return false;
		}

	}

	public boolean VerificarParametrosOportunidad(HttpServletRequest request, HttpServletResponse response, MutableInt idmensaje, StringBuffer mensaje)
    	      throws ServletException, IOException
    {
		// Verificacion
		if(	request.getParameter("tx_name") != null && request.getParameter("tx_surname") != null && 
			request.getParameter("tx_email") != null && request.getParameter("work_phone") != null && 
			request.getParameter("nm_legal") != null && 
			!request.getParameter("tx_name").equals("") && !request.getParameter("tx_surname").equals("") && 
			!request.getParameter("tx_email").equals("") && !request.getParameter("work_phone").equals("") &&
			!request.getParameter("nm_legal").equals(""))
		{	
			if(!request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
	    	{
	    		  idmensaje.setValue(3); mensaje.append("ERROR: El Correo electónico esta mal, puede que contenga caracteres no validos");
	    		  return false;
	    	}
			    	  
			return true;
		}
		else
		{
			idmensaje.setValue(3); mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO")); //"PRECAUCION: Alguno de los parametros necesarios es Nulo <br>";
			return false;
		}
    }
	
  public boolean GestionarOportunidad(HttpServletRequest request, HttpServletResponse response, String lv_interest, String tl_oportunity, String tx_note, StringBuffer mensaje)
  	      throws ServletException, IOException
  {
	JCRMContactsSet per = new JCRMContactsSet(null);
	per.ConCat(3);
	per.setBD(m_BD_Eje);
	per.m_Where = "tx_name = '" + JUtil.p(request.getParameter("tx_name")) + "' and tx_surname = '" + JUtil.p(request.getParameter("tx_surname")) + "' and gu_workarea = '" + m_GU_workarea + "'";
	per.Open();
	
	String gu_contact = (per.getNumRows() > 0 ? "'" + per.getAbsRow(0).getGU_contact() + "'" : "null");
	String tx_name = (per.getNumRows() > 0 ? "null" : "'" + JUtil.p(request.getParameter("tx_name")) + "'");
	String tx_surname = (per.getNumRows() > 0 ? "null" : "'" + JUtil.p(request.getParameter("tx_surname")) + "'");
	String tx_contact = (per.getNumRows() > 0 ? "'" + JUtil.p(request.getParameter("tx_name")) + " " + JUtil.p(request.getParameter("tx_surname")) + "'" : "null");
	
	JCRMCompaniesSet com = new JCRMCompaniesSet(null);
	com.ConCat(3);
	com.setBD(m_BD_Eje);
	com.m_Where = "nm_legal = '" + JUtil.p(request.getParameter("nm_legal")) + "' and gu_workarea = '" + m_GU_workarea + "'";
	com.Open();
	
	String gu_company = (com.getNumRows() > 0 ? "'" + com.getAbsRow(0).getGU_company() + "'" : "null");
	String nm_legal = (com.getNumRows() > 0 ? "null" : "'" + JUtil.p(request.getParameter("nm_legal")) + "'");
	String tx_company = (com.getNumRows() > 0 ? "'" + JUtil.p(request.getParameter("nm_legal")) + "'" : "null");
	
	
  	String id_objetive = (request.getParameter("id_objetive") == null || request.getParameter("id_objetive").equals("FSI_OBJETIVE") ? "null" : "'" + JUtil.p(request.getParameter("id_objetive")) + "'");
  	String tp_origin = (request.getParameter("tp_origin") == null || request.getParameter("tp_origin").equals("FSI_ORIGIN") ? "null" : "'" + JUtil.p(request.getParameter("tp_origin")) + "'");
  	String tx_cause = "null"; //(request.getParameter("tx_cause").equals("FSI_CAUSE") ? "null" : "'" + p(request.getParameter("tx_cause")) + "'");
  	String gu_campaign = "null"; //(request.getParameter("gu_campaign").equals("FSI_CAMPAIGN") ? "null" : "'" + p(request.getParameter("gu_campaign")) + "'");
  	String id_nationality = (request.getParameter("id_nationality") == null || request.getParameter("id_nationality").equals("FSI_NATIONALITY") ? "null" : "'" + JUtil.p(request.getParameter("id_nationality")) + "'");
  	String id_country = (request.getParameter("id_country") == null || request.getParameter("id_country").equals("FSI_COUNTRY") ? "'mx '" : "'" + JUtil.p(request.getParameter("id_country")) + "'");
  	String gu_writer = "null"; //getSesion(request).getUsuarioCRM().gu_user.equals("cef-su") ? "null" : "'" + getSesion(request).getUsuarioCRM().gu_user + "'";
  	if(tx_note == null) { tx_note = (request.getParameter("tx_note") == null || request.getParameter("tx_note").equals("") ? "null" : "'" + JUtil.p(request.getParameter("tx_note")) + "'"); }
  	String str = "select * from sp_crm_oportunidades_gestionar(null,"
  			+ gu_writer + ",'"
  			+ JUtil.p(m_GU_workarea) + "'," 
  			+ (request.getParameter("bo_private") == null ? "'0'" : "'1'") + ",null,"
  			+ JUtil.p2(request.getParameter("dt_modified"),"date",true,"") + ","
  			+ JUtil.p2(request.getParameter("dt_next_action"),"date",true,"") + ","
  			+ JUtil.p2(request.getParameter("dt_last_call"),"date",true,"") + ","
  			+ "'" + lv_interest + "'," //JUtil.p2(request.getParameter("lv_interest"),"int",true,"") + ","
  			+ JUtil.p2(request.getParameter("nu_oportunities"),"int",false,"1") + ","
  			+ gu_campaign + ","
  			+ gu_company + ","  //JUtil.p2(request.getParameter("gu_company"),"str",true,"") + ","
  			+ gu_contact + ","  //JUtil.p2(request.getParameter("gu_contact"),"str",true,"") + ","
  			+ tx_company + ","  //JUtil.p2(request.getParameter("tx_company"),"str",true,"") + ","
  			+ tx_contact + "," //JUtil.p2(request.getParameter("tx_contact"),"str",true,"") + ","
  			+ "'" + tl_oportunity + "'," //JUtil.p2(request.getParameter("tl_oportunity"),"str",false,"") + ","
  			+ JUtil.p2(request.getParameter("tp_oportunity"),"str",true,"") + ","
  			+ tp_origin +","
  			+ "'0.0'," //JUtil.p2(request.getParameter("im_revenue"),"double",true,"") + ","
  			+ "'0.0'," //JUtil.p2(request.getParameter("im_cost"),"double",true,"") + ","
  			+ "'Nueva'," //JUtil.p2(request.getParameter("id_status"),"str",false,"") + ","
  			+ id_objetive + ","
  			+ JUtil.p2(request.getParameter("id_message"),"str",true,"") + ","
  			+ tx_cause + ","
  			+ tx_note + "," //JUtil.p2(request.getParameter("tx_note"),"str",true,"") + ","
  			//Ahora los datos de contacto altenativos
  			+ nm_legal + "," //JUtil.p2(request.getParameter("nm_legal"),"str",true,"") + ","
  			+ tx_name + "," //JUtil.p2(request.getParameter("tx_name"),"str",true,"") + ","
  			+ tx_surname + "," //JUtil.p2(request.getParameter("tx_surname"),"str",true,"") + ","
  			+ JUtil.p2(request.getParameter("id_gender"),"str",true,"") + ","
  			+ id_nationality + "," +
  			//Ahora la direccion
  			//gu_address character(32) NOT NULL,
  			//ix_address integer NOT NULL,
  			//gu_workarea character(32) NOT NULL,
  			//dt_created timestamp without time zone DEFAULT now(),
  			//bo_active smallint DEFAULT 1,
  			//dt_modified timestamp without time zone,
  			gu_writer + "," + //user character(32),
  			//tp_location character varying(16),
  			//nm_company character varying(70),
  			JUtil.p2(request.getParameter("tp_street"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("nm_street"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("nu_street"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("tx_addr1"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("tx_addr2"),"str",true,"") + "," +
  			id_country + "," + 
  			//nm_country character varying(50),
  			//id_state character varying(16),
  			JUtil.p2(request.getParameter("nm_state"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("mn_city"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("zipcode"),"str",true,"") + "," +
  			//JUtil.p2(request.getParameter("work_phone"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("direct_phone"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("work_phone"),"str",true,"") + "," + //JUtil.p2(request.getParameter("home_phone"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("mov_phone"),"str",true,"") + "," +
  			//JUtil.p2(request.getParameter("fax_phone"),"str",true,"") + "," +
  			JUtil.p2(request.getParameter("other_phone"),"str",true,"") + "," +
  			//po_box character varying(50),
  			JUtil.p2(request.getParameter("tx_email"),"str",false,"") + 
  			//tx_email_alt character varying(100),
  			//url_addr character varying(254),
  			//coord_x double precision,
  			//coord_y double precision,
  			//contact_person character varying(100),
  			//tx_salutation character varying(16),
  			//tx_dept character varying(70),
  			//id_ref character varying(50),
  			//tx_remarks character varying(254),
  			") as (err integer, res varchar, clave bpchar) ";
  	    
  		//System.out.println(str);
	    //JUtil.doDebugSQL(request, response, str);
  	    //return false;
  	     
  		JRetFuncBas rfb = new JRetFuncBas();
  	  	
  		JUtil.doCallStoredProcedure(request, response, m_BD_Eje, str, rfb);
  	    
  		JUtil.RDP("REF",m_BD_Eje,(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),"ref:su:" + request.getRemoteAddr(),"CRM_OPORTUNIDADES_GESTIONAR", "CRMO|" + rfb.getClaveret() + "|" + m_GU_workarea + "||",rfb.getRes());
	   	
		if(rfb.getIdmensaje() == 0)
			return true;
		else
		{
			mensaje.append(rfb.getRes());
			return false;
		}
	  	    
  	}

	public boolean GestionarPersona(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		
		String de_title = "null"; //(request.getParameter("de_title").equals("FSI_TITLE") ? "null" : "'" + p(request.getParameter("de_title")) + "'");
		String tp_passport = "null"; //(request.getParameter("tp_passport").equals("FSI_PASSPORT") ? "null" : "'" + p(request.getParameter("tp_passport")) + "'");
		String id_nationality = "null"; //(request.getParameter("id_nationality").equals("FSI_NATIONALITY") ? "null" : "'" + p(request.getParameter("id_nationality")) + "'");
		String gu_writer = "null"; //getSesion(request).getUsuarioCRM().gu_user.equals("cef-su") ? "null" : "'" + getSesion(request).getUsuarioCRM().gu_user + "'";
		String id_country = "'mx '"; //(request.getParameter("id_country").equals("FSI_COUNTRY") ? "'mx '" : "'" + p(request.getParameter("id_country")) + "'");
		String str = "select * from sp_crm_personas_gestionar(null,'"
				+ JUtil.p(m_GU_workarea) + "',null," 
				+ JUtil.p2(request.getParameter("bo_restricted"),"int",false,"0") + ","
				+ (request.getParameter("bo_private") == null ? "'0'" : "'1'") + ","
				+ JUtil.p2(request.getParameter("nu_notes"),"int",false,"0") + ","
				+ JUtil.p2(request.getParameter("nu_attachs"),"int",false,"0") + ","
				+ JUtil.p2(request.getParameter("bo_change_pwd"),"int",false,"1") + ","
				+ JUtil.p2(request.getParameter("tx_nickname"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_pwd"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_challenge"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_reply"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("dt_pwd_expires"),"date",true,"") + ","
				+ JUtil.p2(request.getParameter("dt_modified"),"date",true,"") + ","
				+ gu_writer + ","
				+ JUtil.p2(request.getParameter("gu_company"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("id_batch"),"str",true,"") + ","
				+ "'Activo'," // + JUtil.p2(request.getParameter("id_status"),"str",false,"") + ","
				+ JUtil.p2(request.getParameter("id_ref"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("id_fare"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("id_bpartner"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_name"),"str",false,"") + ","
				+ JUtil.p2(request.getParameter("tx_surname"),"str",false,"") + ","
				+ de_title + ","
				+ JUtil.p2(request.getParameter("id_gender"),"str",false,"") + ","
				+ JUtil.p2(request.getParameter("dt_birth"),"date",true,"") + ","
				+ JUtil.p2(request.getParameter("ny_age"),"int",true,"") + ","
				+ id_nationality + ","
				+ JUtil.p2(request.getParameter("sn_passport"),"str",true,"") + ","
				+ tp_passport + ","
				+ JUtil.p2(request.getParameter("sn_drivelic"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("dt_drivelic"),"date",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_dept"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("tx_division"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("gu_geozone"),"str",true,"") + ","
				+ "'0'," //JUtil.p2(request.getParameter("id_vendedor"),"int",true,"0") + ","
				+ JUtil.p2(request.getParameter("tx_comments"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("url_linkedin"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("url_facebook"),"str",true,"") + ","
				+ JUtil.p2(request.getParameter("url_twitter"),"str",true,"") + "," +
				//Ahora la direccion
				//gu_address character(32) NOT NULL,
				//ix_address integer NOT NULL,
				//gu_workarea character(32) NOT NULL,
				//dt_created timestamp without time zone DEFAULT now(),
				//bo_active smallint DEFAULT 1,
				//dt_modified timestamp without time zone,
				gu_writer + "," + //user character(32),
				//tp_location character varying(16),
				//nm_company character varying(70),
				JUtil.p2(request.getParameter("tp_street"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("nm_street"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("nu_street"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("tx_addr1"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("tx_addr2"),"str",true,"") + "," +
				id_country + "," + 
				//nm_country character varying(50),
				//id_state character varying(16),
				JUtil.p2(request.getParameter("nm_state"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("mn_city"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("zipcode"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("work_phone"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("direct_phone"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("home_phone"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("mov_phone"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("fax_phone"),"str",true,"") + "," +
				JUtil.p2(request.getParameter("other_phone"),"str",true,"") + "," +
				//po_box character varying(50),
				JUtil.p2(request.getParameter("mail"),"str",false,"") + 
				//tx_email_alt character varying(100),
				//url_addr character varying(254),
				//coord_x double precision,
				//coord_y double precision,
				//contact_person character varying(100),
				//tx_salutation character varying(16),
				//tx_dept character varying(70),
				//id_ref character varying(50),
				//tx_remarks character varying(254),
				
				") as (err integer, res varchar, clave bpchar) ";
		    		
		//doDebugSQL(request, response, str);
		     
		JRetFuncBas rfb = new JRetFuncBas();
		
		JUtil.doCallStoredProcedure(request, response, m_BD_Eje, str, rfb);
		
		JUtil.RDP("REF",m_BD_Eje,(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),"ref-su:" + request.getRemoteAddr(),"CRM_PERSONAS_GESTIONAR","CRMP|" + rfb.getClaveret() + "|" + m_GU_workarea + "||",rfb.getRes());
     	
		if(rfb.getIdmensaje() == 0)
			return true;
		else
			return false;
		
	}
	
	public boolean AgregarEmpresa(HttpServletRequest request, MutableInt idmensaje, StringBuffer mensaje) 
			throws ServletException, IOException 
	{
		StringBuffer PURL = new StringBuffer(); StringBuffer SERV = new StringBuffer(); 
		StringBuffer USER = new StringBuffer(); StringBuffer PASS = new StringBuffer();
		
		if(!cargarInfoSrv(PURL, SERV, USER, PASS, idmensaje, mensaje))
			return false;
				
		try 
		{
			
			//Genera la peticion de agregado
			///////////////////////////////////////////////////////////////////////////////////////////////////
			String urlParameters = 	"SERVER=" + URLEncoder.encode(SERV.toString(),"ISO-8859-1") +
			    		"&USER=" + URLEncoder.encode(USER.toString(),"ISO-8859-1") +
			    		"&PASSWORD=" + URLEncoder.encode(PASS.toString(),"ISO-8859-1") +
			    		"&nombre=" + URLEncoder.encode(request.getParameter("nombre"),"ISO-8859-1") +
			    		"&compania=" + URLEncoder.encode(request.getParameter("nm_legal"),"ISO-8859-1") +
			    		"&direccion=" + URLEncoder.encode(request.getParameter("direccion"),"ISO-8859-1") +
			    		"&poblacion=" + URLEncoder.encode(request.getParameter("poblacion"),"ISO-8859-1") +
			    		"&cp=" + URLEncoder.encode(request.getParameter("cp"),"ISO-8859-1") +
			    		"&mail=" + URLEncoder.encode(request.getParameter("tx_email"),"ISO-8859-1") +
			    		"&web=" + URLEncoder.encode(request.getParameter("web"),"ISO-8859-1") +
			    		"&password=" + URLEncoder.encode(request.getParameter("password"),"ISO-8859-1") +
			    		"&confpwd=" + URLEncoder.encode(request.getParameter("confpwd"),"ISO-8859-1");
					
			String urlString = "https://" + PURL + "/servlet/REFProcSRAgrEmp";
			//System.out.println(urlString);
			//System.out.println(urlParameters);
			URL url = new URL(urlString);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setReadTimeout(300000);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			conn.setRequestProperty("Content-Language", "es-MX");  
			//conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			//Send request
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush ();
			wr.close ();
					
			conn.connect();
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				//System.out.println("Conectado a revidor SRV");
				// Recibe la respuesta del servidor, esta debe estar en formato xml
				SAXBuilder builder = new SAXBuilder();
				Document document = (Document)builder.build(conn.getInputStream());
				Element Respuesta = document.getRootElement();
					
				if(Respuesta.getAttribute("CodError").getValue().equals("0"))
				{
					idmensaje.setValue(0);
					mensaje.append(Respuesta.getAttribute("Msj").getValue());
				}
				else
				{
					idmensaje.setValue(3);
					mensaje.append("Codigo de Error JFsiProcSRAgrEmp: " + Respuesta.getAttribute("CodError").getValue() + "<br>" + Respuesta.getAttribute("Msj").getValue());
				}	
				conn.disconnect();
			}
			else // La conexion no tuvo exito
			{
				idmensaje.setValue(3);
				System.out.println("No se establecio la conexión");
				mensaje.append("ERROR: Al intentar conectarse al servidor. Revisa los datos del servidor intermediario de Agragado de Empresas en el archivo de configuración");
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////
			  
		}
		catch (JDOMException e) 
		{
			e.printStackTrace();
			idmensaje.setValue(3);
			mensaje.append("ERROR DE JDOMException AL AGREGAR EMPRESA: " + e.getMessage() + "<br>");
		}
		catch (SocketTimeoutException e)
		{
			e.printStackTrace();
			idmensaje.setValue(1);
			mensaje.append("La conexión se cerro porque la creación de la empresa en estos momentos está tardando demasiado. Esto no significa que haya fallado. Puedes intentar conectarte en 5 minutos mas, según las instrucciones abajo descritas<br>");
		}
		
		if(idmensaje.intValue() == 3)
			return false;
		else
			return true;
		
	}
	
	protected boolean cargarInfoSrv(StringBuffer PURL, StringBuffer SERV, StringBuffer USER, StringBuffer PASS, MutableInt idmensaje, StringBuffer mensaje) 
			throws ServletException
	{
		try
		{
			FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_srv");
			BufferedReader buff = new BufferedReader(file);
			boolean eof = false;
			while(!eof)
			{
				String line = buff.readLine();
				if(line == null)
					eof = true;
				else
				{
					try
					{
						StringTokenizer st = new StringTokenizer(line,"=");
						String key         = st.nextToken();
						String value       = st.nextToken();
						
						if(key.equals("SERV"))
							SERV.append(value);
						if(key.equals("PURL"))
							PURL.append(value);
						else if(key.equals("PASS"))
							PASS.append(value);
						else if(key.equals("USER"))
							USER.append(value);
												
					}
					catch(NoSuchElementException e)
					{
						idmensaje.setValue(3);
						mensaje.append("ERROR: La información del servidor parece estar mal configurada");
						return false;
					}
				}
				
			}
			buff.close();
			
		}
		catch (FileNotFoundException e1)
		{
			idmensaje.setValue(3);
			mensaje.append("Error de archivos SRV: " + e1.getMessage());
			return false;
		}
		catch (IOException e1) 
		{
			idmensaje.setValue(3);
			mensaje.append("Error de Entrada/Salida SRV: " + e1.getMessage());
			return false;
		}
		
		return true;
	}	
}