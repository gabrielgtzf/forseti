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
package forseti.ventas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forseti.JForsetiApl;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JClientCXCSetV2;
import forseti.sets.JClientClientMasSetV2;
import forseti.sets.JPublicCXCConeSetV2;
import forseti.sets.JVentasEntidadesSetIdsV2;

@SuppressWarnings("serial")
public class JVenCXCDlg extends JForsetiApl
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

      String ven_cxc_dlg = "";
      request.setAttribute("ven_cxc_dlg",ven_cxc_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  // revisa por las entidades
          JVentasEntidadesSetIdsV2 setids = new JVentasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion("VEN_CXC").getEspecial());
          setids.Open();
          
          if(setids.getNumRows() < 1)
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_CXC", "VCXC" + "||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
          
          // Revisa por intento de intrusion (Salto de permiso de entidad)
          if(request.getParameter("id") != null)
          {
	          	JClientCXCSetV2 set = new JClientCXCSetV2(request);
	          	set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and Clave = '" + p(request.getParameter("id")) + "'";
	          	set.Open();
	          	if(set.getNumRows() < 1)
	          	{
	          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC");
	          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"VEN_CXC","VCXC|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getID_Entidad() + "||",mensaje);
	          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	          		return;
	          	}
          }
                               	  
          if(request.getParameter("proceso").equals("SALDAR_CXC"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC_SALDAR"))
        	  {
        		 	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_SALDAR");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_SALDAR","VCXC||||",mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
              }

        	  // Solicitud de envio a procesar
        	  if(request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if(valoresParam.length == 1)
        		  {
        			  JClientCXCSetV2 set = new JClientCXCSetV2(request);
        			  set.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        			  set.Open();

        			  if(!set.getAbsRow(0).getID_TipoCP().equals("ALT"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",1);//"PRECAUCION: Este registro no representa una cuenta Debes seleccionar una cuenta común para saldarla <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  else if(set.getAbsRow(0).getStatus().equals("C"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: No se puede saldar una cuenta ya cancelada <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  
        			  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        			  {
        				  if(VerificarParametrosPago(request, response))
        				  {
        					  SaldarCXC(request, response);
        					  return;
        				  }
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
        				  return;
        			  }
        			  else // Como el subproceso no es ENVIAR, abre la ventana por primera vez
        			  {
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
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
        		  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
        	  }
          }
          else if(request.getParameter("proceso").equals("PAGAR_CXC"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC_PAGAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_PAGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_PAGAR","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
        	  }
          
        	  // Solicitud de envio a procesar
        	  if(request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if(valoresParam.length == 1)
        		  {
        			  JClientCXCSetV2 set = new JClientCXCSetV2(request);
        			  set.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        			  set.Open();

        			  if(!set.getAbsRow(0).getID_TipoCP().equals("ALT"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",1);//"PRECAUCION: Esta cuenta No representa una cuenta por cobrar. Debes seleccionar una cuenta común para liquidarla <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  else if(set.getAbsRow(0).getStatus().equals("C"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: No se puede liquidar una cuenta ya cancelada <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }

        			  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        			  {
        				  request.setAttribute("fsipg_tipo","ventas");
        				  request.setAttribute("fsipg_proc","deposito");
        				  request.setAttribute("fsipg_ident",getSesion(request).getSesion("VEN_CXC").getEspecial());
        				  if(VerificarParametrosPago(request, response) && VerificarPago(request, response))
        				  {
        					  AltaPago(request, response);
        					  return;
        				  }
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
        				  return;
        			  }
        			  else
        			  {
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
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
        		  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
        	  }
          }
          else if(request.getParameter("proceso").equals("APLICAR_ANTICIPO"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC_PAGAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_PAGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_PAGAR","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
        	  }
          
        	  // Solicitud de envio a procesar
        	  if(request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if(valoresParam.length == 1)
        		  {
        			  JClientCXCSetV2 set = new JClientCXCSetV2(request);
        			  set.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        			  set.Open();

        			  if(!set.getAbsRow(0).getID_TipoCP().equals("ALT"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",1);//"PRECAUCION: Esta cuenta No representa una cuenta por cobrar. Debes seleccionar una cuenta común para liquidarla <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  else if(set.getAbsRow(0).getStatus().equals("C"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: No se puede aplicar anticipo a una cuenta ya cancelada <br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }

        			  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        			  {
        				  if(VerificarParametrosPago(request, response))
        				  {
        					  AplicarAnticipoCXC(request, response);
        					  return;
        				  }
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
        				  return;
        			  }
        			  else // Como el subproceso no es ENVIAR, abre la ventana por primera vez
        			  {
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
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
        		  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
        	  }
          }
          else if(request.getParameter("proceso").equals("DEVOLVER_ANTICIPO"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC_CANCELAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_CANCELAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_CANCELAR","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
        	  }
          
        	  // Solicitud de envio a procesar
        	  if (request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if (valoresParam.length == 1)
        		  {
        			  JClientCXCSetV2 set = new JClientCXCSetV2(request);
        			  set.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        			  set.Open();

        			  if(!set.getAbsRow(0).getID_TipoCP().equals("ANT"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",3);//"PRECAUCION: Este registro no es un anticipo, Debes xxxxxx";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  else if(set.getAbsRow(0).getStatus().equals("C"))
        			  {
        				  idmensaje = 1; mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: Este registro ya esta cancelado. No se le puede aplicar el proceso";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  
        			  if (request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        			  {
        				  request.setAttribute("fsipg_tipo","ventas");
        				  request.setAttribute("fsipg_proc","retiro");
        				  request.setAttribute("fsipg_ident",getSesion(request).getSesion("VEN_CXC").getEspecial());
        				  if(VerificarParametrosPago(request, response) && VerificarPago(request, response))
        				  {	  
        					  AnticipoDevolucion(request, response);
        					  return;
        				  }
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
        				  return;
        			  }
        			  else
        			  { 
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
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
        		  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
        	  }
          }
          else if(request.getParameter("proceso").equals("CONSULTAR_CUENTA"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
        	  }
          
        	  // Solicitud de envio a procesar
        	  if (request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if (valoresParam.length == 1)
        		  {
        			  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"VEN_CXC","VCXC|" + request.getParameter("id") + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||","");
                      getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiweb/ventas/ven_cxc_dlg_cons.jsp", request, response);
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
          else if(request.getParameter("proceso").equals("CANCELAR"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("VEN_CXC_CANCELAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_CANCELAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_CANCELAR","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
        	  }
          
        	  // Solicitud de envio a procesar
        	  if (request.getParameter("id") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("id");
        		  if (valoresParam.length == 1)
        		  {
        			  JClientCXCSetV2 set = new JClientCXCSetV2(request);
        			  set.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        			  set.Open();

        			  if(set.getAbsRow(0).getStatus().equals("C"))
        			  {
        				  idmensaje = 1;
        				  mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: No se puede cancelar un registro de cuenta ya cancelad<br>";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  
        			  if(!set.getAbsRow(0).getRef().equals(""))
        			  {
        				  idmensaje = 1;
        				  mensaje += JUtil.Msj("CEF", "VEN_CXC", "DLG", "MSJ-PROCERR",4);//"PRECAUCION: No se puede cancelar un registro de cuenta por cobrar o de anticipo, generado externamente. Debes cancelar el registro que lo creó ";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        				  return;
        			  }
        			  
        			  CancelarCuenta(request, response);
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
          else if(request.getParameter("proceso").equals("RASTREAR_MOVIMIENTO"))
          {
          	// Revisa si tiene permisos
          	if(!getSesion(request).getPermiso("VEN_CXC_CONSULTAR"))
              {
          		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_CONSULTAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CXC_CONSULTAR","VCXC||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
          	
              if(request.getParameter("id") != null)
              {
            	  String[] valoresParam = request.getParameterValues("id");
            	  if (valoresParam.length == 1)
            	  {
            		  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("VEN_CXC").generarTitulo(JUtil.Msj("CEF","VEN_CXC","VISTA","CONSULTAR_CUENTA",3)),
                    								"VCXC",request.getParameter("id"));
            		  String rastreo_imp = "true";
            		  request.setAttribute("rastreo_imp", rastreo_imp);
            		  // Ahora pone los atributos para el jsp
            		  request.setAttribute("rastreo", rastreo);
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"VEN_CXC_CONSULTAR","VCXC|" + request.getParameter("id") + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||","");
            		  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
            		  return;
                	
            	  }
            	  else
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
              else
              {
            	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador de la póliza que se quiere consultar<br>";
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

    public boolean VerificarParametrosPago(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if( request.getParameter("clave") != null && request.getParameter("fecha") != null && request.getParameter("concepto") != null &&
          request.getParameter("tc") != null && request.getParameter("cantidad") != null && request.getParameter("idmoneda") != null &&
          !request.getParameter("clave").equals("") && !request.getParameter("fecha").equals("") &&
          !request.getParameter("tc").equals("") && !request.getParameter("cantidad").equals("") && !request.getParameter("idmoneda").equals("") )
      {
        // Verifica que el cliente de la cuenta realmente pertenece a esta entidad
        // Primero sustrae la clave de la cuenta, luego al cliente para obtener su entidad
        JClientCXCSetV2 smod = new JClientCXCSetV2(request);
        smod.m_Where = "Clave = '" + p(request.getParameter("id")) + "'";
        smod.Open();
        // Verifica que el cliente de la cuenta realmente pertenece a esta entidad
        JClientClientMasSetV2 pr = new JClientClientMasSetV2(request);
        pr.m_Where = "ID_Clave = '" + smod.getAbsRow(0).getID_ClaveClient() + "'";
        pr.Open();
        if(pr.getAbsRow(0).getID_EntidadVenta() != Integer.parseInt(getSesion(request).getSesion("VEN_CXC").getEspecial()) )
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "VEN_CXC","DLG","MSJ-PROCERR",5); //ERROR: El cliente no pertenece a la entidad mandada. <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        // Por ultimo verifica la cantidad el tc y el id concepto
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
        float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
        if (cantidad == 0.00 || tc <= 0.0000)
        {
           idmensaje = 3;
           mensaje = JUtil.Msj("CEF", "VEN_CXC","DLG","MSJ-PROCERR2",1); //"ERROR: La cantidad para este registro de cuenta por cobrar no puede ser cero, ni el tipo de cambio menor que cero <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
        }

        // solo verifica el id concepto para saldo
        if(request.getParameter("proceso").equals("SALDAR_CXC"))
        {
        	JPublicCXCConeSetV2 con = new JPublicCXCConeSetV2(request);
        	con.m_Where = "ID_Concepto = '" + p(request.getParameter("clave")) + "' and Tipo = 'SAL'";
        	con.Open();
        	if(con.getNumRows() < 1)
        	{
        		idmensaje = 3;
        		mensaje = JUtil.Msj("CEF","ALM_MOVIM","SES","MSJ-PROCERR",2); //ERROR: No existe el concepto del saldo<br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		return false;
        	}
        }

        // solo verifica el del anticipo anticipo
        if(request.getParameter("proceso").equals("APLICAR_ANTICIPO"))
        {
        	JClientCXCSetV2 ant = new JClientCXCSetV2(request);
        	ant.m_Where = "Clave = '" + p(request.getParameter("clave")) + "' and ID_ClaveClient = '" + smod.getAbsRow(0).getID_ClaveClient() + "' and ID_TipoClient = 'CL' and ID_Entidad = '" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "' and ID_TipoCP = 'ANT'";
        	ant.Open();

        	if(ant.getNumRows() < 1)
        	{
        		idmensaje = 3;
        		mensaje = JUtil.Msj("CEF", "VEN_CXC","DLG","MSJ-PROCERR2",2); //"ERROR: No existe el anticipo especificado<br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		return false;
        	}
        }

        return true;
      }
      else
      {
    	  idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public void CancelarPago(HttpServletRequest request, HttpServletResponse response, String fecha, String forma_pago, String id_mov, String id_pol)
      throws ServletException, IOException
    {
    	//String str = "EXEC  sp_client_cxc_pago_cancelar " + request.getParameter("id") + "," + request.getParameter("idpago") + ",'" + fecha + "','" + forma_pago + "'," + id_mov + "," + id_pol;
        irApag("/forsetiweb/ventas/ven_cxc_dlg_cons.jsp", request, response);
    }

    public void CancelarCuenta(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String str = "select * from sp_client_cxc_cancelar('" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave integer );";
        
    	JRetFuncBas rfb = new JRetFuncBas();
 		
   		doCallStoredProcedure(request, response, str, rfb);
  
   		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_CANCELAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||",rfb.getRes());
   		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
     
    }


    public void AltaPago(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);
    	
    	String str = "select * from sp_client_cxc_pagar('" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "','" + p(request.getParameter("id")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p((String)request.getAttribute("fsipg_ref")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + p((String)request.getAttribute("fsipg_forma")) + "','" + p((String)request.getAttribute("fsipg_id_bancaj")) + "','" + total + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("id")) + "','0','100', true, null, null, null, '" +
    			p((String)request.getAttribute("fsipg_tipomov")) + "','" +
    			p((String)request.getAttribute("fsipg_id_satbanco")) + "','" +
    			p((String)request.getAttribute("fsipg_metpagopol")) + "','" +
    			p((String)request.getAttribute("fsipg_bancoext")) + "','" +
    			p((String)request.getAttribute("fsipg_cuentabanco")) + "','" +
    			p((String)request.getAttribute("fsipg_depchq")) + "') as ( err integer, res varchar, clave integer );";
       	
       	JRetFuncBas rfb = new JRetFuncBas();
		
  		doCallStoredProcedure(request, response, str, rfb);
 
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_PAGAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||",rfb.getRes());
  		irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);
	  
    }

    public void AnticipoDevolucion(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);
    	
    	String str = "select * from sp_client_cxc_devanticipo('" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "','" + p(request.getParameter("id")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p((String)request.getAttribute("fsipg_ref")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + p((String)request.getAttribute("fsipg_forma")) + "','" + p((String)request.getAttribute("fsipg_id_bancaj")) + "','" + total + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("id")) + "','0','102','" + 
    			p((String)request.getAttribute("fsipg_tipomov")) + "','" +
    			p((String)request.getAttribute("fsipg_id_satbanco")) + "','" +
    			p((String)request.getAttribute("fsipg_metpagopol")) + "','" +
    			p((String)request.getAttribute("fsipg_bancoext")) + "','" +
    			p((String)request.getAttribute("fsipg_cuentabanco")) + "','" +
    			p((String)request.getAttribute("fsipg_depchq")) + "') as ( err integer, res varchar, clave integer );";
    	//doDebugSQL(request, response, str);
       	JRetFuncBas rfb = new JRetFuncBas();
		
  		doCallStoredProcedure(request, response, str, rfb);
 
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_CANCELAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||",rfb.getRes());
  		irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);

    }

    public void AplicarAnticipoCXC(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

    	String str = "select * from sp_client_cxc_aplanticipo('" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "','" + p(request.getParameter("id")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + total + "','" + p(request.getParameter("clave")) + "') as ( err integer, res varchar, clave integer );";
  	
     	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);

		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_PAGAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||",rfb.getRes());
		irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);

    }

    public void SaldarCXC(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

    	String str = "select * from sp_client_cxc_pagar('" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "','" + p(request.getParameter("id")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','0','0','" + total + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("id")) + "','1','" + p(request.getParameter("clave")) + "', true, null, null, null, " +
    			"null, null, null, null, null, null) as ( err integer, res varchar, clave integer );";
     	
     	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);

		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_SALDAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CXC").getEspecial() + "||",rfb.getRes());
		irApag("/forsetiweb/ventas/ven_cxc_dlg_pagos.jsp", request, response);


    }

}
