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
package forseti.admon;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JProcessSet;

@SuppressWarnings("serial")
public class JAdmPeriodosDlg extends JForsetiApl
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

      String adm_periodos_dlg = "";
      request.setAttribute("adm_periodos_dlg",adm_periodos_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  if(request.getParameter("proceso").equals("AGREGAR_PERIODO"))
    	  {
    		  // Revisa si tiene permisos
    		  if(!getSesion(request).getPermiso("ADM_PERIODOS_AGREGAR"))
    		  {
    			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_PERIODOS_AGREGAR");
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_PERIODOS_AGREGAR","APRD||||",mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
    		  }

    		  JProcessSet setPer = new JProcessSet(request);
              setPer.setSQL("select count(*) as count from TBL_CONT_CATALOGO_PERIODOS");
              setPer.Open();

              if(Integer.parseInt(setPer.getAbsRow(0).getSTS("Col1")) < 1)
              {
            	  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	  {
            		  Agregar(request, response, true);
            		  return;
            	  }
            	  else
            	  {
            		  irApag("/forsetiweb/administracion/adm_periodos_dlg.jsp", request, response);
        			  return;
            	  }
              }
              else
              {
            	  Agregar(request, response, false);
            	  return;
              }
    		  
          }
    	  else if(request.getParameter("proceso").equals("CERRAR_PERIODO"))
    	  {
    		  // Revisa si tiene permisos
    		  if(!getSesion(request).getPermiso("ADM_PERIODOS_AGREGAR"))
    		  {
    			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_PERIODOS_AGREGAR");
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_PERIODOS_AGREGAR","APRD||||",mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
    		  }

    		  // Solicitud de envio a procesar
    		  if(request.getParameter("id") != null)
    		  {
    			  String[] valoresParam = request.getParameterValues("id");
    			  if(valoresParam.length == 1)
    			  {
    				  	Cerrar(request, response);
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
    			  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
    		  }
        }
        else
        {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
      }

    }

    public void Cerrar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String ano = JUtil.Elm(request.getParameter("id"), 1);
    	String mes = JUtil.Elm(request.getParameter("id"), 2);
		
    	String str = "select * from  sp_cont_catalogo_cerrar_per('" + p(mes) + "','" + p(ano) + "') as ( err integer, res varchar, clave varchar ) ";
        JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_PERIODOS_AGREGAR", "APRD|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response, boolean mesano)
      throws ServletException, IOException
    {
    	String str;
    	if(mesano)
    		str = "select * from  sp_cont_catalogo_crear_sig_per('" + p(request.getParameter("mes")) + "','" + p(request.getParameter("ano")) + "') as ( err integer, res varchar, clave varchar ) ";
    	else
    		str = "select * from  sp_cont_catalogo_crear_sig_per(null,null) as ( err integer, res varchar, clave varchar ) ";
    		
    	//doDebugSQL(request, response, str);
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_PERIODOS_AGREGAR", "APRD|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		
    }



}
