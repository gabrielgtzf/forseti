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
import forseti.sets.JCRMOportunitiesSet;
import forseti.sets.JNotasBlocksIdsSet;

@SuppressWarnings("serial")
public class JCRMOportunidadesDlg extends JForsetiApl
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

      String crm_oportunidades_dlg = "";
      request.setAttribute("crm_oportunidades_dlg",crm_oportunidades_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
    	JNotasBlocksIdsSet setids = new JNotasBlocksIdsSet(request,usuario,JUtil.Elm(getSesion(request).getSesion("CRM_OPORTUNIDADES").getEspecial(),1));
        setids.Open();
    	  
    	if(setids.getNumRows() < 1)
    	{
    		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_OPORTUNIDADES");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "CRM_OPORTUNIDADES", "CRMO||||",mensaje);
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		return;
    	}
          
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD") && request.getParameter("id") != null)
        {
        	JCRMOportunitiesSet set = new JCRMOportunitiesSet(request);
          	set.m_Where = "gu_workarea = '" + setids.getAbsRow(0).getGU_Workarea() + "' and gu_oportunity = '" + p(request.getParameter("id")) + "'";
          	set.Open();
          	if(set.getNumRows() < 1)
          	{
          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_OPORTUNIDADES");
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"CRM_OPORTUNIDADES","CRMO|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getGU_Workarea() + "||",mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
          	}
        }
          
        if(request.getParameter("proceso").equals("AGREGAR_OPORTUNIDAD"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CRM_OPORTUNIDADES_GESTIONAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_OPORTUNIDADES_GESTIONAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_OPORTUNIDADES_GESTIONAR","CRMO||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametros(request, response, "AGREGAR"))
        	  {
        		  Gestionar(request, response, "AGREGAR");
        	   	  return;
        	  }
        	  
        	  irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
        	  return;
          }
            
          
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_OPORTUNIDAD"))
        {
        	 if(!getSesion(request).getPermiso("CRM_OPORTUNIDADES"))
             {
                 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_OPORTUNIDADES");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_OPORTUNIDADES","CRMO||||",mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
             }

        	 // Solicitud de envio a procesar
        	 if(request.getParameter("id") != null)
        	 {
        		 String[] valoresParam = request.getParameterValues("id");
        		 if(valoresParam.length == 1)
        		 {
        			 RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CRM_OPORTUNIDADES","CRMO|" + p(request.getParameter("id")) + "|" + JUtil.Elm(getSesion(request).getSesion("CRM_OPORTUNIDADES").getEspecial(),2) + "||","");
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_OPORTUNIDAD"))
        {
        	if(!getSesion(request).getPermiso("CRM_OPORTUNIDADES_GESTIONAR"))
            {
                idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CRM_OPORTUNIDADES_GESTIONAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CRM_OPORTUNIDADES_GESTIONAR","CRMO||||",mensaje);
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
        				if(VerificarParametros(request, response, "AGREGAR"))
        				{
        					Gestionar(request, response, "CAMBIAR");
        					return;
        				}
        	        	  
        				irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
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
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("tl_oportunity") != null && !request.getParameter("tl_oportunity").equals("") &&
    		  request.getParameter("im_revenue") != null && !request.getParameter("im_revenue").equals("") &&
    		  request.getParameter("im_cost") != null && !request.getParameter("im_cost").equals("")  )
      {
    	  if(proceso.equals("AGREGAR"))
    	  {
	    	  if(request.getParameter("gu_contact") == null || request.getParameter("gu_contact").equals(""))
	    	  {
	    		  if(request.getParameter("tx_name") == null || request.getParameter("tx_surname") == null || request.getParameter("tx_email") == null || 
	    				  request.getParameter("tx_name").equals("") || request.getParameter("tx_surname").equals("") || 
	    				  request.getParameter("tx_email").equals("") || !request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
	        	  {
	        		  idmensaje = 1; mensaje = "PRECAUCION: El Nombre, Apellidos o Correo electónico estan mal, puede que esten en blanco o contengan caracteres no validos";
	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        		  return false;
	        	  }  
	    	  }
	    	  
	    	  if((request.getParameter("gu_company") == null || request.getParameter("gu_company").equals("")) 
	    			  && (request.getParameter("nm_legal") != null && !request.getParameter("nm_legal").equals("")))
	    	  {
	    		  if(request.getParameter("tx_email") == null || request.getParameter("tx_email").equals("") || !request.getParameter("tx_email").matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
	        	  {
	        		  idmensaje = 1; mensaje = "PRECAUCION: El Correo electónico esta mal, puede que este en blanco o contenga caracteres no válidos";
	        		  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        		  return false;
	        	  }  
	    	  }
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
      String id_objetive = (request.getParameter("id_objetive").equals("FSI_OBJETIVE") ? "null" : "'" + p(request.getParameter("id_objetive")) + "'");
      String tp_origin = (request.getParameter("tp_origin").equals("FSI_ORIGIN") ? "null" : "'" + p(request.getParameter("tp_origin")) + "'");
      String tx_cause = (request.getParameter("tx_cause").equals("FSI_CAUSE") ? "null" : "'" + p(request.getParameter("tx_cause")) + "'");
      String gu_campaign = (request.getParameter("gu_campaign").equals("FSI_CAMPAIGN") ? "null" : "'" + p(request.getParameter("gu_campaign")) + "'");
      String id_nationality = (request.getParameter("id_nationality") == null || request.getParameter("id_nationality").equals("FSI_NATIONALITY") ? "null" : "'" + p(request.getParameter("id_nationality")) + "'");
      String id_country = (request.getParameter("id_country") == null || request.getParameter("id_country").equals("FSI_COUNTRY") ? "'mx '" : "'" + p(request.getParameter("id_country")) + "'");
      String gu_writer = getSesion(request).getUsuarioCRM().gu_user.equals("cef-su") ? "null" : "'" + getSesion(request).getUsuarioCRM().gu_user + "'";
      
      String str = "select * from sp_crm_oportunidades_gestionar(" + ( proceso.equals("AGREGAR") ? "null" : "'" + p(request.getParameter("id")) + "'") + ","
      		+ gu_writer + ",'"
      		+ p(JUtil.Elm(getSesion(request).getSesion("CRM_OPORTUNIDADES").getEspecial(),2)) + "'," 
    		+ (request.getParameter("bo_private") == null ? "'0'" : "'1'") + ",null,"
    		+ p2(request.getParameter("dt_modified"),"date",true,"") + ","
    		+ p2(request.getParameter("dt_next_action"),"date",true,"") + ","
    		+ p2(request.getParameter("dt_last_call"),"date",true,"") + ","
    		+ p2(request.getParameter("lv_interest"),"int",true,"") + ","
    		+ p2(request.getParameter("nu_oportunities"),"int",false,"1") + ","
    		+ gu_campaign + ","
    		+ p2(request.getParameter("gu_company"),"str",true,"") + ","
    		+ p2(request.getParameter("gu_contact"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_company"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_contact"),"str",true,"") + ","
    		+ p2(request.getParameter("tl_oportunity"),"str",false,"") + ","
    		+ p2(request.getParameter("tp_oportunity"),"str",true,"") + ","
    		+ tp_origin +","
    		+ p2(request.getParameter("im_revenue"),"double",true,"") + ","
    		+ p2(request.getParameter("im_cost"),"double",true,"") + ","
    		+ p2(request.getParameter("id_status"),"str",false,"") + ","
    		+ id_objetive + ","
    		+ p2(request.getParameter("id_message"),"str",true,"") + ","
    		+ tx_cause + ","
    		+ p2(request.getParameter("tx_note"),"str",true,"") + ","
    		//Ahora los datos de contacto altenativos
    		+ p2(request.getParameter("nm_legal"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_name"),"str",true,"") + ","
    		+ p2(request.getParameter("tx_surname"),"str",true,"") + ","
    		+ p2(request.getParameter("id_gender"),"str",true,"") + ","
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
    		//p2(request.getParameter("work_phone"),"str",true,"") + "," +
    		p2(request.getParameter("direct_phone"),"str",true,"") + "," +
    		p2(request.getParameter("home_phone"),"str",true,"") + "," +
    		p2(request.getParameter("mov_phone"),"str",true,"") + "," +
    		//p2(request.getParameter("fax_phone"),"str",true,"") + "," +
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
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "CRM_OPORTUNIDADES_GESTIONAR", "CRMO|" + rfb.getClaveret() + "|" + JUtil.Elm(getSesion(request).getSesion("CRM_OPORTUNIDADES").getEspecial(),2) + "||",rfb.getRes());
      irApag("/forsetiweb/crm/crm_oportunidades_dlg.jsp", request, response);
		
    }

}
