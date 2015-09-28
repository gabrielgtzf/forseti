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
package forseti.contabilidad;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JPublicContCatalogSetV2;

@SuppressWarnings("serial")
public class JContaPolizasCierreDlg extends JForsetiApl
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

      String conta_polizas_cierre_dlg = "";
      request.setAttribute("conta_polcierre_dlg",conta_polizas_cierre_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("GENERAR_POLCIERRE"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("CONT_POLCIERRE_GENERAR"))
            {
            	 idmensaje = 3; mensaje +=  MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE_GENERAR");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE_GENERAR","POLC||||",mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
            }

            Generar(request, response);
            return;
        }
        else if(request.getParameter("proceso").equals("CANCELAR_POLCIERRE"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("CONT_POLCIERRE_CANCELAR"))
            {
            	idmensaje = 3; mensaje +=  MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE_CANCELAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CANCELAR","POLC||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }

            Cancelar(request, response);
            return;
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_POLCIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLCIERRE_ELIMINAR"))
          {
        	  idmensaje = 3; mensaje +=  MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE_ELIMINAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE_ELIMINAR","POLC||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              Eliminar(request, response);
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
        else if(request.getParameter("proceso").equals("AGREGAR_POLCIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLCIERRE_CREAR"))
          {
        	  idmensaje = 3; mensaje +=  MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE_CREAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CREAR","POLC||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
            {
              Agregar(request, response);
              return;
            }
            irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_POLCIERRE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_POLCIERRE_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje +=  MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CAMBIAR","POLC||||",mensaje);
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
                	Cambiar(request, response);
                	return;
                }
                irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);
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

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("id") != null && !request.getParameter("id").equals("") &&
    			request.getParameter("debe") != null && !request.getParameter("debe").equals("") &&
                request.getParameter("haber") != null && !request.getParameter("haber").equals("") )
    	{
    		JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    		set.m_Where = "Numero = '" + p(JUtil.obtCuentas(request.getParameter("id"),(byte)19)) + "'";
    		set.Open();
    		if (set.getNumRows() > 0)
    		{
    			if (set.getAbsRow(0).getAcum() == true)
    			{
    				idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_POLIZAS", "SES", "MSJ-PROCERR", 1);
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		return false;
    			}
    		}
    		else
    		{
    			idmensaje = 3; mensaje = JUtil.Msj("CEF", "CONT_POLIZAS", "SES", "MSJ-PROCERR", 3); //"ERROR: La cuenta especificada no existe <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		return false;
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
 
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    String str = "select * from sp_cont_polizas_cierre_eliminar('" + p(getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "','" + p(JUtil.obtCuentas(request.getParameter("id"),(byte)19)) + "') as (err integer, res varchar, clave bpchar)";
	    JRetFuncBas rfb = new JRetFuncBas();
    	
	    doCallStoredProcedure(request, response, str, rfb);
	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLCIERRE_ELIMINAR","POLC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLCIERRE").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      
	}
    
    public void Cancelar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    String str = "select * from sp_cont_polizas_cierre_cancelar('" + p(getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "') as (err integer, res varchar, clave smallint)";
	    JRetFuncBas rfb = new JRetFuncBas();
    	
	    doCallStoredProcedure(request, response, str, rfb);
	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CANCELAR","POLC|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	}
    
    public void Generar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    String str = "select * from sp_cont_polizas_cierre_generar('" + p(getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "') as (err integer, res varchar, clave smallint)";
	    JRetFuncBas rfb = new JRetFuncBas();
    	
	    doCallStoredProcedure(request, response, str, rfb);
	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLCIERRE_GENERAR","POLC|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	}
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
                Double.parseDouble(request.getParameter("debe")) : 0;
        double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
                Double.parseDouble(request.getParameter("haber")) : 0;
                       	
    	String str = "select * from sp_cont_polizas_cierre_cambiar('" + p(getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "','" + 
    			p(JUtil.obtCuentas(request.getParameter("id"),(byte)19)) + "','" + debe + "','" + haber + "') as (err integer, res varchar, clave bpchar)";
	    JRetFuncBas rfb = new JRetFuncBas();
    	
	    doCallStoredProcedure(request, response, str, rfb);
	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CAMBIAR","POLC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLCIERRE").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
                Double.parseDouble(request.getParameter("debe")) : 0;
        double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
                Double.parseDouble(request.getParameter("haber")) : 0;
                       	
    	String str = "select * from sp_cont_polizas_cierre_agregar('" + p(getSesion(request).getSesion("CONT_POLCIERRE").getEspecial()) + "','" + 
    			p(JUtil.obtCuentas(request.getParameter("id"),(byte)19)) + "','" + debe + "','" + haber + "') as (err integer, res varchar, clave bpchar)";
	    JRetFuncBas rfb = new JRetFuncBas();
    	
	    doCallStoredProcedure(request, response, str, rfb);
	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_POLCIERRE_CREAR","POLC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("CONT_POLCIERRE").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_polcierre_dlg.jsp", request, response);

    }

}
