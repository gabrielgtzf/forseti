/*
    Forseti, El ERP Gratuito para PyMEs
    Copyright (C) 2015 Gabriel Gutiérrez Fuentes.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package forseti.crm;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JCRMContactsSet;
import forseti.sets.JNotasBlocksIdsSet;
import forseti.sets.JVendedoresSet;

@SuppressWarnings("serial")
public class JCRMPersonasDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String crm_personas_dlg = "";
      request.setAttribute("crm_personas_dlg",crm_personas_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
    	JNotasBlocksIdsSet setids = new JNotasBlocksIdsSet(request,usuario,JUtil.Elm(getSesion(request).getSesion("CRM_PERSONAS").getEspecial(),1));
        setids.Open();
    	  
    	if(setids.getNumRows() < 1)
    	{
    		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "CRM_PERSONAS", "CRMP||||",mensaje);
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		return;
    	}
          
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_PERSONA") && request.getParameter("id") != null)
        {
        	JCRMContactsSet set = new JCRMContactsSet(request);
          	set.m_Where = "gu_workarea = '" + setids.getAbsRow(0).getGU_Workarea() + "' and gu_contact = '" + p(request.getParameter("id")) + "'";
          	set.Open();
          	if(set.getNumRows() < 1)
          	{
          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS");
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getGU_Workarea() + "||",mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
          	}
        }
          
        if(request.getParameter("proceso").equals("AGREGAR_PERSONA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CRM_PERSONAS_GESTIONAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS_GESTIONAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_PERSONAS_GESTIONAR","CRMP||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametros(request, response))
        	  {
        		  Gestionar(request, response, "AGREGAR");
        	   	  return;
        	  }
        	  
        	  irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
        	  return;
          }
            
          
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_PERSONA"))
        {
        	 if(!getSesion(request).getPermiso("CRM_PERSONAS"))
             {
                 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP||||",mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
             }

        	 // Solicitud de envio a procesar
        	 if(request.getParameter("id") != null)
        	 {
        		 String[] valoresParam = request.getParameterValues("id");
        		 if(valoresParam.length == 1)
        		 {
        			 RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CRM_PERSONAS","CRMP|" + p(request.getParameter("id")) + "|" + JUtil.Elm(getSesion(request).getSesion("CRM_PERSONAS").getEspecial(),2) + "||","");
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
                     return;
        		 }
        		 else
        		 {
        			 idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                     return;
        		 }
        	 }
        	 else
        	 {
        		 idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
        	 }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR_PERSONA"))
        {
        	if(!getSesion(request).getPermiso("CRM_PERSONAS_GESTIONAR"))
            {
                idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_PERSONAS_GESTIONAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_PERSONAS_GESTIONAR","CRMP||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }

        	// Solicitud de envio a procesar
        	if(request.getParameter("id") != null)
        	{
        		String[] valoresParam = request.getParameterValues("id");
        		if(valoresParam.length == 1)
        		{
        			if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        			{
        				// Verificacion
        				if(VerificarParametros(request, response))
        				{
        					Gestionar(request, response, "CAMBIAR");
        					return;
        				}
        	        	  
        				irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
                        return;
        			}
        		}
        		else
        		{
        			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
        		}
          }
          else
          {
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else
        {
        	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
      }

    }
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("tx_name") != null && !request.getParameter("tx_name").equals("") &&
    		  request.getParameter("id_vendedor") != null && !request.getParameter("id_vendedor").equals("") &&
    		  request.getParameter("tx_surname") != null && !request.getParameter("tx_surname").equals("") &&
    	      request.getParameter("tx_email") != null && !request.getParameter("tx_email").equals("") )
      {
    	  if(!request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
    	  {
    		  idmensaje = 1; mensaje = "PRECAUCION: El Correo electónico esta mal, puede que contenga caracteres no validos";
    		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    		  return false;
    	  }
    	  
    	  //Verifica el vendedor
          JVendedoresSet setven  = new JVendedoresSet(request);
          setven.m_Where = "ID_Vendedor = '" + p(request.getParameter("id_vendedor")) + "'";
          setven.Open();
          if(setven.getNumRows() == 0)
          {
            idmensaje = 3;
            mensaje = JUtil.Msj("CEF", "VEN_CLIENT","DLG","MSJ-PROCERR",2); //"ERROR: El vendedor especificado no existe <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
          }
          
    	  return true;
      }
      else
      {
        idmensaje = 1; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"); //"PRECAUCION: Alguno de los parametros necesarios es Nulo <br>";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        return false;
      }
    }

    public void Gestionar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
      String de_title = (request.getParameter("de_title").equals("FSI_TITLE") ? "null" : "'" + p(request.getParameter("de_title")) + "'");
      String tp_passport = (request.getParameter("tp_passport").equals("FSI_PASSPORT") ? "null" : "'" + p(request.getParameter("tp_passport")) + "'");
      String id_nationality = (request.getParameter("id_nationality").equals("FSI_NATIONALITY") ? "null" : "'" + p(request.getParameter("id_nationality")) + "'");
      String gu_writer = getSesion(request).getUsuarioCRM().gu_user.equals("cef-su") ? "null" : "'" + getSesion(request).getUsuarioCRM().gu_user + "'";
      String id_country = (request.getParameter("id_country").equals("FSI_COUNTRY") ? "'mx '" : "'" + p(request.getParameter("id_country")) + "'");
      String str = "select * from sp_crm_personas_gestionar(" + ( proceso.equals("AGREGAR") ? "null" : "'" + p(request.getParameter("id")) + "'") + ",'"
    		+ p(JUtil.Elm(getSesion(request).getSesion("CRM_PERSONAS").getEspecial(),2)) + "',null," 
    		+ p2(request.getParameter("bo_restricted"),"int",false,"0") + ","
    		+ (request.getParameter("bo_private") == null ? "'0'" : "'1'") + ","
    		+ p2(request.getParameter("nu_notes"),"int",false,"0") + ","
    		+ p2(request.getParameter("nu_attachs"),"int",false,"0") + ","
    		+ p2(request.getParameter("bo_change_pwd"),"int",false,"1") + ","
    		+ p2(request.getParameter("tx_nickname"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_pwd"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_challenge"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_reply"),"str",true,"") + ","
    		+ p2(request.getParameter("dt_pwd_expires"),"date",true,"") + ","
    		+ p2(request.getParameter("dt_modified"),"date",true,"") + ","
    		+ gu_writer + ","
    		+ p2(request.getParameter("gu_company"),"str",true,"") + ","
    		+ p2(request.getParameter("id_batch"),"str",true,"") + ","
    		+ p2(request.getParameter("id_status"),"str",false,"") + ","
    		+ p2(request.getParameter("id_ref"),"str",true,"") + ","
    		+ p2(request.getParameter("id_fare"),"str",true,"") + ","
    		+ p2(request.getParameter("id_bpartner"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_name"),"str",false,"") + ","
    		+ p2(request.getParameter("tx_surname"),"str",false,"") + ","
    		+ de_title + ","
    		+ p2(request.getParameter("id_gender"),"str",false,"") + ","
    		+ p2(request.getParameter("dt_birth"),"date",true,"") + ","
    		+ p2(request.getParameter("ny_age"),"int",true,"") + ","
    		+ id_nationality + ","
    		+ p2(request.getParameter("sn_passport"),"str",true,"") + ","
    		+ tp_passport + ","
    		+ p2(request.getParameter("sn_drivelic"),"str",true,"") + ","
    		+ p2(request.getParameter("dt_drivelic"),"date",true,"") + ","
    		+ p2(request.getParameter("tx_dept"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_division"),"str",true,"") + ","
    		+ p2(request.getParameter("gu_geozone"),"str",true,"") + ","
    		+ p2(request.getParameter("id_vendedor"),"int",true,"0") + ","
    		+ p2(request.getParameter("tx_comments"),"str",true,"") + ","
    		+ p2(request.getParameter("url_linkedin"),"str",true,"") + ","
    		+ p2(request.getParameter("url_facebook"),"str",true,"") + ","
    		+ p2(request.getParameter("url_twitter"),"str",true,"") + "," +
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
    		p2(request.getParameter("tp_street"),"str",true,"") + "," +
    		p2(request.getParameter("nm_street"),"str",true,"") + "," +
    		p2(request.getParameter("nu_street"),"str",true,"") + "," +
    		p2(request.getParameter("tx_addr1"),"str",true,"") + "," +
    		p2(request.getParameter("tx_addr2"),"str",true,"") + "," +
    		id_country + "," + 
    		//nm_country character varying(50),
    		//id_state character varying(16),
    		p2(request.getParameter("nm_state"),"str",true,"") + "," +
    		p2(request.getParameter("mn_city"),"str",true,"") + "," +
    		p2(request.getParameter("zipcode"),"str",true,"") + "," +
    		p2(request.getParameter("work_phone"),"str",true,"") + "," +
    		p2(request.getParameter("direct_phone"),"str",true,"") + "," +
    		p2(request.getParameter("home_phone"),"str",true,"") + "," +
    		p2(request.getParameter("mov_phone"),"str",true,"") + "," +
    		p2(request.getParameter("fax_phone"),"str",true,"") + "," +
    		p2(request.getParameter("other_phone"),"str",true,"") + "," +
    		//po_box character varying(50),
    		p2(request.getParameter("tx_email"),"str",false,"") + 
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
  	
	  doCallStoredProcedure(request, response, str, rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CRM_PERSONAS_GESTIONAR", "CRMP|" + rfb.getClaveret() + "|" + JUtil.Elm(getSesion(request).getSesion("CRM_PERSONAS").getEspecial(),2) + "||",rfb.getRes());
      irApag("/forsetiweb/crm/crm_personas_dlg.jsp", request, response);
		
    }

}
