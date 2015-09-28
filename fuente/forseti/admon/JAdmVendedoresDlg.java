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

public class JAdmVendedoresDlg extends JForsetiApl
{
 	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String adm_vendedores_dlg = "";
      request.setAttribute("adm_vendedores_dlg",adm_vendedores_dlg);
      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_VENDEDOR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_VENDEDORES_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VENDEDORES_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VENDEDORES_AGREGAR","AVEN||||",mensaje);
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
            irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
            return;

          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_VENDEDOR"))
        {
        	if(!getSesion(request).getPermiso("ADM_VENDEDORES_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VENDEDORES_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VENDEDORES_AGREGAR","AVEN||||",mensaje);
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
        				irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
        	            return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_VENDEDOR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_VENDEDORES_ELIMINAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_VENDEDORES_ELIMINAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_VENDEDORES_ELIMINAR","AVEN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	if(request.getParameter("id").equals("0"))
		  	  	{
  					idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_VENDEDORES","DLG","MSJ-PROCERR"); //PRECAUCION: No se puede eliminar o cambiar el vendedor de sistema cero
  					getSesion(request).setID_Mensaje(idmensaje, mensaje);
  					irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  					return;
		  	  	}
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
      if(request.getParameter("idvendedor") != null && request.getParameter("nombre") != null
              && request.getParameter("comision") != null  && 
              !request.getParameter("idvendedor").equals("") && !request.getParameter("nombre").equals("")
              && !request.getParameter("comision").equals("") )
      {
    	  int idvendedor = Integer.parseInt(request.getParameter("idvendedor"));
          if(idvendedor < 0)
  	  	  {
              idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_VENDEDORES","DLG","MSJ-PROCERR");//"PRECAUCION: El vendedor debe tener una clave mayor a cero";
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
  	
    	String str = "SELECT * FROM sp_vendedores_cambiar('" + p(request.getParameter("idvendedor")) + "','" + p(request.getParameter("nombre")) + "','" + p(request.getParameter("comision")) + "','" + p(request.getParameter("status")) +  
  				"') as ( err integer, res varchar, clave smallint ) ";
 
		doCallStoredProcedure(request, response, str, rfb);

		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_VENDEDORES_AGREGAR","AVEN|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
   
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
	
  		String str = "SELECT * FROM sp_vendedores_agregar('" + p(request.getParameter("idvendedor")) + "','" + p(request.getParameter("nombre")) + "','" + p(request.getParameter("comision")) + "','" + p(request.getParameter("status")) +  
  				"') as ( err integer, res varchar, clave smallint ) ";
 
  		doCallStoredProcedure(request, response, str, rfb);
  
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_VENDEDORES_AGREGAR","AVEN|" + rfb.getClaveret() + "|||",rfb.getRes());
  		irApag("/forsetiweb/administracion/adm_vendedores_dlg.jsp", request, response);
      
    }

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
  		String str = "SELECT * FROM sp_vendedores_eliminar('" + p(request.getParameter("id")) + "') as ( err integer, res varchar, clave smallint ) ";
 
  		doCallStoredProcedure(request, response, str, rfb);
  
  		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_VENDEDORES_ELIMINAR","AVEN|" + rfb.getClaveret() + "|||",rfb.getRes());
  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
   
    }

}
