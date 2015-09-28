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
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.*;


@SuppressWarnings("serial")
public class JVenPoliticasDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      //request.setAttribute("fsi_modulo",request.getRequestURI());
      super.doPost(request,response);

      String ven_pol_dlg = "";
      request.setAttribute("ven_pol_dlg",ven_pol_dlg);

      String mensaje = ""; short idmensaje = -1;
    
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
        if(request.getParameter("proceso").equals("PRECIOS_CLIENTE"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_CLIENTES"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_CLIENTES");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_CLIENTES", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            {
            	CambiarPreciosCliente(request, response);
            	return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
            	if(VerificarParametrosPartida(request, response))
            		AgregarPartida(request, response);
            	
            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
            	return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
            {
            	if(VerificarParametrosPartida(request, response))
            		EditarPartida(request, response);
            	
            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
            	return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
            	BorrarPartida(request, response);
            	
            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
            	return;
            }
            else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO `por primera vez
            {            
	            if(request.getParameter("ID") != null)
	            {
	            	String[] valoresParam = request.getParameterValues("ID");
	            	if(valoresParam.length == 1)
	            	{
	            		if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("CLIENTES"))
	            		{
	            			HttpSession ses = request.getSession(true);
	            			JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
	            			if(rec == null)
	            			{
	            				rec = new JVenPoliticasSes();
	            				ses.setAttribute("ven_pol_dlg", rec);
	            			}
	            			else
	            				rec.resetear();
	            				                
	            			// Llena la politica
	            			JClientClientSetV2 set = new JClientClientSetV2(request);
	            			set.m_Where = "ID_Tipo = 'CL' and Clave = '" + p(request.getParameter("ID")) + "'";
			        		set.Open();
			                JInvServVsClient SetMod = new JInvServVsClient(request);
				            SetMod.m_Where = "ID_Client = '" + p(request.getParameter("ID")) + "'";
				            SetMod.Open();
				            rec.setParametros(set.getAbsRow(0).getClave(), set.getAbsRow(0).getNumero(), set.getAbsRow(0).getID_EntidadVenta(), set.getAbsRow(0).getEntidad(), set.getAbsRow(0).getNombre());
				            	
				            for(int i = 0; i< SetMod.getNumRows(); i++)
				            {
				            	 rec.agregaPartida(SetMod.getAbsRow(i).getUnidad(), SetMod.getAbsRow(i).getID_Prod(), SetMod.getAbsRow(i).getDescripcion(),
				            			SetMod.getAbsRow(i).getPrecio(), SetMod.getAbsRow(i).getMoneda(), SetMod.getAbsRow(i).getNombreMoneda());
				            }
		           	
			            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
			                irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
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
        }  
        else if(request.getParameter("proceso").equals("DESCUENTOS_CLIENTE"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_CLIENTES"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_CLIENTES");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_CLIENTES", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            {
            	CambiarDescuentosCliente(request, response);
            	return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
            	if(VerificarParametrosPartidaDesc(request, response))
            		AgregarPartidaDesc(request, response);
            	
            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
                return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
            {
            	if(VerificarParametrosPartidaDesc(request, response))
            		EditarPartidaDesc(request, response);

            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
                return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
            	BorrarPartidaDesc(request, response);
            	
            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
                return;
            }
            else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO `por primera vez
            {            
	            if(request.getParameter("ID") != null)
	            {
	            	String[] valoresParam = request.getParameterValues("ID");
	            	if(valoresParam.length == 1)
	            	{
	            		if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("CLIENTES"))
	            		{
	            			HttpSession ses = request.getSession(true);
	            			JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
	            			if(rec == null)
	            			{
	            				rec = new JVenPoliticasSes();
	            				ses.setAttribute("ven_pol_dlg", rec);
	            			}
	            			else
	            				rec.resetear();
		                
	            			// Llena la politica
	            			JClientClientSetV2 set = new JClientClientSetV2(request);
	            			set.m_Where = "ID_Tipo = 'CL' and Clave = '" + p(request.getParameter("ID")) + "'";
	            			set.Open();
	            			JVentasCliVsDesc SetMod = new JVentasCliVsDesc(request);
	            			SetMod.m_Where = "ID_Client = '" + p(request.getParameter("ID")) + "'";
	            			SetMod.Open();
	            			rec.setParametros(set.getAbsRow(0).getClave(), set.getAbsRow(0).getNumero(), set.getAbsRow(0).getID_EntidadVenta(), set.getAbsRow(0).getEntidad(), set.getAbsRow(0).getNombre());
			            	
	            			for(int i = 0; i< SetMod.getNumRows(); i++)
	            			{
	            				rec.agregaPartidaOtr(SetMod.getAbsRow(i).getUnidad(), SetMod.getAbsRow(i).getID_Prod(), SetMod.getAbsRow(i).getNombre(),
			            			SetMod.getAbsRow(i).getDescuento(), SetMod.getAbsRow(i).getDescuento2(), SetMod.getAbsRow(i).getDescuento3(), SetMod.getAbsRow(i).getDescuento4(), SetMod.getAbsRow(i).getDescuento5());
	            			}
	           	
	            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            			irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
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
        }  
        else if(request.getParameter("proceso").equals("PRECIOS_PROD"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_PRODUCTOS"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_PRODUCTOS");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("PRODUCTOS"))
            {
            	if(request.getParameter("subproceso") == null)
            	{
            		HttpSession ses = request.getSession(true);
            		JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
            		if(rec == null)
            		{
            			rec = new JVenPoliticasSes();
            			ses.setAttribute("ven_pol_dlg", rec);
            		}
            		else
            			rec.resetear();
            		                
	                // Llena la politica
            		JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
            		set.m_Where = getSesion(request).getSesion("VEN_POL").generarWhere();
            		set.m_OrderBy = getSesion(request).getSesion("VEN_POL").generarOrderBy();
            		set.Open();
            				
            		for(int i = 0; i< set.getNumRows(); i++)
		            {
		            	 rec.agregaObjeto(set.getAbsRow(i).getID_Tipo(), set.getAbsRow(i).getClave(), set.getAbsRow(i).getDescripcion(), set.getAbsRow(i).getLinea(), 
		            			 set.getAbsRow(i).getUnidad(), set.getAbsRow(i).getStatus(), set.getAbsRow(i).getP1(), set.getAbsRow(i).getP2(), set.getAbsRow(i).getP3(), set.getAbsRow(i).getP4(), set.getAbsRow(i).getP5(), 
		            			 set.getAbsRow(i).getPMin(), set.getAbsRow(i).getPMax(), set.getAbsRow(i).getPW(), set.getAbsRow(i).getPOW(), set.getAbsRow(i).getPComp(), set.getAbsRow(i).getID_Moneda());
		            }
           	
	            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
	                return;
            	}
            	else
                {

      	       	  	// Solicitud de envio a procesar
      	       	  	if(request.getParameter("subproceso").equals("ENVIAR"))
      	       	  	{
      	       	  		if(VerificarParametrosPreciosProd(request, response))
      	       	  		{
      	       	  			CambiarPreciosProd(request, response);
      	       	  			return;
      	       	  		}
      	       	  		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
      	       	  		return;
      	       	  	}
                }
            }
        }        	 
        else if(request.getParameter("proceso").equals("CANTIDADES_PROD"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_PRODUCTOS"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_PRODUCTOS");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("PRODUCTOS"))
            {

            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	{
            		if(VerificarParametrosCantidadesProd(request,response))
            		{
            			CambiarCantidadesProd(request, response);
            			return;
            		}
            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
  	       	  		return;
            	}
            	else
            	{
    	            if(request.getParameter("ID") != null)
    	            {
    	            	String[] valoresParam = request.getParameterValues("ID");
    	            	if(valoresParam.length == 1)
    	            	{
    	            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response); 
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
            }
        }   
        else if(request.getParameter("proceso").equals("AUMENTOS_PROD"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_PRODUCTOS"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_PRODUCTOS");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("PRODUCTOS"))
            {
            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	{
            		if(VerificarParametrosAumentosProd(request,response))
            		{
            			if(request.getParameter("procfin").equals("CAMBIO"))
            			{
            				AumentosProd(request, response);
            				return;
            			}
            			else
            			{
            				request.setAttribute("AUMENTO", "AUMENTO");
         	            	idmensaje = 2; mensaje += "¿ Son estos los aumentos que necesitas aplicar ?. Para aplicarlos definitivamente, selecciona el botón: Aplica defínitivamente los cambios<br>";
        	            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	            	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
        	            	return;
            			}
            		}
            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
	            	return;
            	}
            	else
            	{
    	            if(request.getParameter("ID") != null)
    	            {
    	            	String[] valoresParam = request.getParameterValues("ID");
    	            	if(valoresParam.length == 1)
    	            	{
    	            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response); 
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
            }
        }      
        else if(request.getParameter("proceso").equals("POLITICAS_ENTIDAD"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_POL_ENTIDAD"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_POL_ENTIDAD");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_POL_ENTIDAD", "VPOL" + "||||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
            
            if(getSesion(request).getSesion("VEN_POL").getEspecial().equals("ENTIDADES"))
            {
            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            	{
            		if(VerificarParametrosPoliticasEntidad(request,response))
            		{
            			CambiarPoliticasEntidad(request, response);
            			return;
            		}
            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response); 
            		return;
            	}
            	else
            	{
    	            if(request.getParameter("ID") != null)
    	            {
    	            	String[] valoresParam = request.getParameterValues("ID");
    	            	if(valoresParam.length == 1)
    	            	{
    	            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            		irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response); 
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
 
    public boolean VerificarParametrosAumentosProd(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	   short idmensaje = -1; String mensaje = "";
		
	   // Ahora verifica las existencias
	   if(request.getParameter("idprod") != null && request.getParameter("idlinea") != null && request.getParameter("prodlin") != null && 
			   request.getParameter("FSI_DEC") != null && request.getParameter("FSI_P1") != null &&
			   request.getParameter("FSI_P2") != null && request.getParameter("FSI_P3") != null &&
			   request.getParameter("FSI_P4") != null && request.getParameter("FSI_P5") != null && request.getParameter("FSI_PMIN") != null &&
			   request.getParameter("FSI_PMAX") != null && 
			!request.getParameter("idprod").equals("") && !request.getParameter("idlinea").equals("") && !request.getParameter("prodlin").equals("") && 
			!request.getParameter("FSI_DEC").equals("") && !request.getParameter("FSI_P1").equals("") &&
			!request.getParameter("FSI_P2").equals("") && !request.getParameter("FSI_P3").equals("") &&
			!request.getParameter("FSI_P4").equals("") && !request.getParameter("FSI_P5").equals("") && !request.getParameter("FSI_PMIN").equals("") &&
			!request.getParameter("FSI_PMAX").equals("") )
	    
	   {
		   return true;
	   }
	   else
	   {
		   idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parametros de clave de producto o linea, y de todos los porcentajes <br>";
		   getSesion(request).setID_Mensaje(idmensaje, mensaje);
		   return false;
	   }
	
	}
    
    public void AumentosProd(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
	          
	   String str = "select * from  sp_politicas_invserv_cant_aumento_precios('CAMBIO','" + 
	    	(request.getParameter("prodlin").equals("PROD") ? p(request.getParameter("idprod")) :  p(request.getParameter("idlinea")) ) + "', '" + 
	      	p(request.getParameter("prodlin")) + "','" + p(request.getParameter("FSI_DEC")) + "','" + p(request.getParameter("FSI_P1")) + "','" + p(request.getParameter("FSI_P2")) + "','" + 
	      	p(request.getParameter("FSI_P3")) + "','" + p(request.getParameter("FSI_P4")) + "','" + 
	      	p(request.getParameter("FSI_P5")) + "','" + p(request.getParameter("FSI_PMIN")) + "','" + p(request.getParameter("FSI_PMAX")) + "') as (err integer, res varchar, clave varchar)";
	      
       JRetFuncBas rfb = new JRetFuncBas();
		
       doCallStoredProcedure(request, response, str, rfb);
  
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
       irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
     
	}
    
    public boolean VerificarParametrosCantidadesProd(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       short idmensaje = -1; String mensaje = "";
		
       // Ahora verifica las existencias
       if(request.getParameter("idprod") != null && request.getParameter("idlinea") != null && request.getParameter("prodlin") != null && request.getParameter("FSI_DESDE_P1") != null &&
   		   request.getParameter("FSI_DESDE_P2") != null && request.getParameter("FSI_DESDE_P3") != null &&
   		   request.getParameter("FSI_DESDE_P4") != null && request.getParameter("FSI_DESDE_P5") != null && request.getParameter("FSI_HASTA_P1") != null &&
   		   request.getParameter("FSI_HASTA_P2") != null && request.getParameter("FSI_HASTA_P3") != null &&
   		   request.getParameter("FSI_HASTA_P4") != null && request.getParameter("FSI_HASTA_P5") != null &&
   		   !request.getParameter("idprod").equals("") && !request.getParameter("idlinea").equals("") && !request.getParameter("prodlin").equals("") && !request.getParameter("FSI_DESDE_P1").equals("") &&
		   !request.getParameter("FSI_DESDE_P2").equals("") && !request.getParameter("FSI_DESDE_P3").equals("") &&
		   !request.getParameter("FSI_DESDE_P4").equals("") && !request.getParameter("FSI_DESDE_P5").equals("") && !request.getParameter("FSI_HASTA_P1").equals("") &&
		   !request.getParameter("FSI_HASTA_P2").equals("") && !request.getParameter("FSI_HASTA_P3").equals("") &&
		   !request.getParameter("FSI_HASTA_P4").equals("") && !request.getParameter("FSI_HASTA_P5").equals("") )
        
       {
    	   return true;
       }
       else
       {
    	   idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parametros de clave de producto o linea, y de todas las cantidades <br>";
    	   getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	   return false;
       }
	
    }

    public void CambiarCantidadesProd(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
            
        String str = "select * from sp_politicas_invserv_cant_vs_precio_agregar_cambiar('" + 
        	(request.getParameter("prodlin").equals("PROD") ? p(request.getParameter("idprod")) : p(request.getParameter("idlinea")) ) + "','" + 
        	p(request.getParameter("prodlin")) + "','" + p(request.getParameter("FSI_DESDE_P1")) + "','" + p(request.getParameter("FSI_HASTA_P1")) + "','" + 
        	p(request.getParameter("FSI_DESDE_P2")) + "','" + p(request.getParameter("FSI_HASTA_P2")) + "','" + 
        	p(request.getParameter("FSI_DESDE_P3")) + "','" + p(request.getParameter("FSI_HASTA_P3")) + "','" + 
        	p(request.getParameter("FSI_DESDE_P4")) + "','" + p(request.getParameter("FSI_HASTA_P4")) + "','" + 
        	p(request.getParameter("FSI_DESDE_P5")) + "','" + p(request.getParameter("FSI_HASTA_P5")) + "') as (err integer, res varchar, clave varchar)";
        
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);

    }   

    public boolean VerificarParametrosPoliticasEntidad(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
	
        JVentasEntidadesSetIdsV2 ent = new JVentasEntidadesSetIdsV2(request,getSesion(request).getID_Usuario(),request.getParameter("ID"));
    	ent.Open();
 
    	if(ent.getNumRows() < 1)
    	{
    		idmensaje = 3; mensaje = "ERROR: No tienes acceso a esta entidad de venta";
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    	// Ahora verifica las existencias
    	JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
    	set.m_Where = getSesion(request).getSesion("VEN_POL").generarWhere();
    	set.m_OrderBy = getSesion(request).getSesion("VEN_POL").generarOrderBy();
    	set.Open();
    	String clave = "";
    	boolean flag = true;
    	for(int i = 0; i< set.getNumRows(); i++)
    	{
    		clave = set.getAbsRow(i).getClave();
    		try 
    		{
    			float P1,P2,P3,P4,P5;
    			String SP1 = request.getParameter("FSI_P1_" + clave);
    			String SP2 = request.getParameter("FSI_P2_" + clave);
    			String SP3 = request.getParameter("FSI_P3_" + clave);
    			String SP4 = request.getParameter("FSI_P4_" + clave);
    			String SP5 = request.getParameter("FSI_P5_" + clave);
               	 
    			if(SP1 == null || SP1.equals(""))
    				P1 = 0F;
    			else
    				P1 = Float.parseFloat(SP1);
            		
    			if(SP2 == null || SP2.equals(""))
    				P2 = 0F;
    			else
    				P2 = Float.parseFloat(SP2);
               		
    			if(SP3 == null || SP3.equals(""))
    				P3 = 0F;
    			else
    				P3 = Float.parseFloat(SP3);
              		
    			if(SP4 == null || SP4.equals(""))
    				P4 = 0F;
    			else
    				P4 = Float.parseFloat(SP4);
 
    			if(SP5 == null || SP5.equals(""))
    				P5 = 0F;
    			else
    				P5 = Float.parseFloat(SP5);
 
    			if( P1 > 100.0 || P2 >100.0 || P3 >100.0 || P4 > 100.0 || P5 > 100.0 )
    			{
    				flag = false;
    				break;
    			}
    		}
    		catch(NumberFormatException e) 
    		{
    			flag = false;
    			break;
    		}
    	}
    	if(!flag)
    	{
    		idmensaje = 1; mensaje = "PRECAUCION: Alguno de los descuentos no está correcto. Si no deseas asignarle descuento, dejalo en cero. <br>Clave: " + clave;
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    	return true;
    }
 
    public void CambiarPoliticasEntidad(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String tbl;
    	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_VENTAS_POLITICAS_ENT_DESC (\n";
    	tbl += "ID_Prod varchar(20) NOT NULL , \n";
    	tbl += "P1 numeric(9, 6) NOT NULL , \n";
    	tbl += "P2 numeric(9, 6) NOT NULL , \n";
    	tbl += "P3 numeric(9, 6) NOT NULL , \n";
    	tbl += "P4 numeric(9, 6) NOT NULL , \n";
    	tbl += "P5 numeric(9, 6) NOT NULL , \n";
    	tbl += "Aplicacion smallint NOT NULL \n";
    	tbl += ");\n";
      
    	JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
    	set.m_Where = getSesion(request).getSesion("VEN_POL").generarWhere();
    	set.m_OrderBy = getSesion(request).getSesion("VEN_POL").generarOrderBy(); 
    	set.Open();

    	for(int i = 0; i< set.getNumRows(); i++)
    	{
    		String clave = set.getAbsRow(i).getClave();
        	
    		float P1,P2,P3,P4,P5;
    		String SP1 = request.getParameter("FSI_P1_" + clave);
    		String SP2 = request.getParameter("FSI_P2_" + clave);
    		String SP3 = request.getParameter("FSI_P3_" + clave);
    		String SP4 = request.getParameter("FSI_P4_" + clave);
    		String SP5 = request.getParameter("FSI_P5_" + clave);
    	  
    		if(SP1 == null || SP1.equals(""))
    			P1 = 0F;
    		else
    			P1 = Float.parseFloat(SP1);
      		
    		if(SP2 == null || SP2.equals(""))
    			P2 = 0F;
    		else
    			P2 = Float.parseFloat(SP2);
         		
    		if(SP3 == null || SP3.equals(""))
    			P3 = 0F;
    		else
    			P3 = Float.parseFloat(SP3);
        		
    		if(SP4 == null || SP4.equals(""))
    			P4 = 0F;
    		else
    			P4 = Float.parseFloat(SP4);

    		if(SP5 == null || SP5.equals(""))
    			P5 = 0F;
    		else
    			P5 = Float.parseFloat(SP5);

    		tbl += "INSERT INTO _TMP_VENTAS_POLITICAS_ENT_DESC \n";
    		tbl += "VALUES('" + p(set.getAbsRow(i).getClave()) + "','" + P1 + "','" + P2 + "','" + P3 + "','" + P4 + "','" + P5 + "','"
    					+ p(request.getParameter("FSI_APL_" + set.getAbsRow(i).getClave())) + "');\n";
    		
    	}
      
    	String str = "select * from sp_ventas_politicas_ent_desc('" + p(request.getParameter("ID")) + "') as (err integer, res varchar, clave smallint)";

    	JRetFuncBas rfb = new JRetFuncBas();
	
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_VENTAS_POLITICAS_ENT_DESC", rfb);

    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_ENTIDAD", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
      
  	}    
    
    public boolean VerificarParametrosPreciosProd(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
		set.m_Where = getSesion(request).getSesion("VEN_POL").generarWhere();
		set.m_OrderBy = getSesion(request).getSesion("VEN_POL").generarOrderBy();
		set.Open();
		String clave = "";
		boolean flag = true;
		for(int i = 0; i< set.getNumRows(); i++)
		{
    	   clave = set.getAbsRow(i).getClave();
    	   try 
    	   {
    		   float P1 = Float.parseFloat(request.getParameter("FSI_P1_" + set.getAbsRow(i).getClave()));
    		   float P2 = Float.parseFloat(request.getParameter("FSI_P2_" + set.getAbsRow(i).getClave()));
    		   float P3 = Float.parseFloat(request.getParameter("FSI_P3_" + set.getAbsRow(i).getClave()));
    		   float P4 = Float.parseFloat(request.getParameter("FSI_P4_" + set.getAbsRow(i).getClave()));
    		   float P5 = Float.parseFloat(request.getParameter("FSI_P5_" + set.getAbsRow(i).getClave()));
    		   float PMin = Float.parseFloat(request.getParameter("FSI_PMIN_" + set.getAbsRow(i).getClave()));
    		   float PMax = Float.parseFloat(request.getParameter("FSI_PMAX_" + set.getAbsRow(i).getClave()));
            		  
    		   if( P1 < 0.0 || P2 < 0.0 || P3 < 0.0 || P4 < 0.0 || P5 < 0.0 || PMin < 0.0 || PMax < 0.0 )
    		   {
    			   flag = false;
    			   break;
    		   }
    	   }
    	   catch(NumberFormatException e) 
    	   {
    		   flag = false;
    		   break;
    	   }
       }
       if(!flag)
       {
           idmensaje = 1;
           mensaje = "PRECAUCION: La alguno de los precios del producto " + clave + " no está correcta ó es menor que cero. <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
       }
       return true;
    }

    public void CambiarPreciosProd(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_PRECIOS (\n";
        tbl += "ID_Prod varchar(20) NOT NULL , \n";
        tbl += "P1 numeric(9, 2) NOT NULL , \n";
        tbl += "P2 numeric(9, 2) NOT NULL , \n";
        tbl += "P3 numeric(9, 2) NOT NULL , \n";
        tbl += "P4 numeric(9, 2) NOT NULL , \n";
        tbl += "P5 numeric(9, 2) NOT NULL , \n";
        tbl += "PMin numeric(9, 2) NOT NULL , \n";
        tbl += "PMax numeric(9, 2) NOT NULL \n";
        tbl += ");\n";
        
        JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
		set.m_Where = getSesion(request).getSesion("VEN_POL").generarWhere();
		set.m_OrderBy = getSesion(request).getSesion("VEN_POL").generarOrderBy();
		set.Open();
		
        for(int i = 0; i< set.getNumRows(); i++)
        {
            tbl += "INSERT INTO _TMP_INVSERV_PRECIOS \n";
            tbl += "VALUES('" + set.getAbsRow(i).getClave() + "','" 
            + p(request.getParameter("FSI_P1_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_P2_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_P3_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_P4_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_P5_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_PMIN_" + set.getAbsRow(i).getClave())) + "','"
            + p(request.getParameter("FSI_PMAX_" + set.getAbsRow(i).getClave())) + "'); \n";
        }
        
        String str = "select * from sp_invserv_cambio_precios('', '0', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00') as (err integer, res varchar, clave varchar)";
               //doDebugSQL(request,response,tbl + "<br>" + str);
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_PRECIOS", rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_PRODUCTOS", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
    }
    
 
  public boolean VerificarParametrosPartidaDesc(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  short idmensaje = -1; String mensaje = "";
	  // Verificacion
	  if(request.getParameter("idprod") != null && 
			  !request.getParameter("idprod").equals(""))
	  {
		 return true;
	  }
	  else
	  {
		  idmensaje = 1; mensaje = "PRECAUCION: Se debe enviar la clave del producto <br>";
		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
		  return false;
	  }
  }
    
  public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  short idmensaje = -1; String mensaje = "";
	  // Verificacion
	  if(request.getParameter("idprod") != null && request.getParameter("idmoneda") != null &&
			  request.getParameter("precio")  != null &&
			  !request.getParameter("idprod").equals("") && !request.getParameter("idmoneda").equals("") &&
			  !request.getParameter("precio").equals(""))
	  {
		  return true;
	  }
	  else
	  {
		  idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parametros de clave de producto, clave de moneda, y precio <br>";
		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
		  return false;
	  }
  }

  	public void AgregarPartidaDesc(HttpServletRequest request, HttpServletResponse response)
  		throws ServletException, IOException
	{
  		short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
  		HttpSession ses = request.getSession(true);
  		JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
	
  		Float descuento, descuento2, descuento3, descuento4, descuento5;
  		//float desc, desc2, desc3, desc4, desc5;
  		
  		if(request.getParameter("tipoorigen") == null || request.getParameter("tipoorigen").equals("porcentaje"))
  		{
  			try{ descuento = Float.parseFloat(request.getParameter("descuento")); } catch (NumberFormatException e) { descuento = 0F; }
  			try{ descuento2 = Float.parseFloat(request.getParameter("descuento2")); } catch (NumberFormatException e) { descuento2 = 0F; }
  			try{ descuento3 = Float.parseFloat(request.getParameter("descuento3")); } catch (NumberFormatException e) { descuento3 = 0F; }
  			try{ descuento4 = Float.parseFloat(request.getParameter("descuento4")); } catch (NumberFormatException e) { descuento4 = 0F; }
  			try{ descuento5 = Float.parseFloat(request.getParameter("descuento5")); } catch (NumberFormatException e) { descuento5 = 0F; }
  		}
  		else
  		{
  			JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
  			ent.m_Where = "ID_EntidadVenta = '" + rec.getID_EntidadVenta() + "'";
  			ent.Open();
  			JInvServInvSetMasV2 inv = new JInvServInvSetMasV2(request);
  			inv.m_Where = "Clave = '" + p(request.getParameter("idprod")) + "'";
  			inv.Open();
		  
  			boolean desglose = ent.getAbsRow(0).getDesgloseMOSTR();
  			float iva = ent.getAbsRow(0).getIVA();
  			boolean apliva = inv.getAbsRow(0).getIVA();
		  
  			if(desglose && apliva && iva != 0.0)
  			{
  				float ivapor = (iva / 100) + 1;
  				try{	descuento = JUtil.redondear(  (((Float.parseFloat(request.getParameter("descuento")) / (inv.getAbsRow(0).getPrecio()  / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento = 0F; }
  				try{	descuento2 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento2")) / (inv.getAbsRow(0).getPrecio2() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento2 = 0F; }
  				try{	descuento3 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento3")) / (inv.getAbsRow(0).getPrecio3() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento3 = 0F; }
  				try{	descuento4 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento4")) / (inv.getAbsRow(0).getPrecio4() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento4 = 0F; }
  				try{	descuento5 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento5")) / (inv.getAbsRow(0).getPrecio5() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento5 = 0F; }
  			}
  			else
  			{
  				try{	descuento = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento")) / inv.getAbsRow(0).getPrecio()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento = 0F; }
  				try{	descuento2 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento2")) / inv.getAbsRow(0).getPrecio2()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento2 = 0F; }
  				try{	descuento3 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento3")) / inv.getAbsRow(0).getPrecio3()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento3 = 0F; }
  				try{	descuento4 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento4")) / inv.getAbsRow(0).getPrecio4()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento4 = 0F; }
  				try{	descuento5 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento5")) / inv.getAbsRow(0).getPrecio5()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento5 = 0F; }
  			}
  			
  			
  		}
  		
  		if(descuento >= 100F || descuento.isInfinite() || descuento.isNaN())
  			descuento = 0F;
		if(descuento2 >= 100F || descuento2.isInfinite() || descuento2.isNaN())
			descuento2 = 0F;
		if(descuento3 >= 100F || descuento3.isInfinite() || descuento3.isNaN())
			descuento3 = 0F;
		if(descuento4 >= 100F || descuento4.isInfinite() || descuento4.isNaN())
			descuento4 = 0F;
		if(descuento5 >= 100F || descuento5.isInfinite() || descuento5.isNaN())
			descuento5 = 0F;
  		
		idmensaje = rec.agregaPartidaOtr(request, request.getParameter("idprod"), descuento, descuento2, descuento3, descuento4, descuento5, mensaje);
	
  		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
  		
	}
  
  
  public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

	  HttpSession ses = request.getSession(true);
	  JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");

	  float precio = Float.parseFloat(request.getParameter("precio"));
	  byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));

	  idmensaje = rec.agregaPartida(request, request.getParameter("idprod"), precio, idmoneda, mensaje);

	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
  
  }

  public void EditarPartidaDesc(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
  {
	  short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	  HttpSession ses = request.getSession(true);
	  JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
	
	  Float descuento, descuento2, descuento3, descuento4, descuento5;
	  
	  if(request.getParameter("tipoorigen") == null || request.getParameter("tipoorigen").equals("porcentaje"))
	  {
		  try{ descuento = Float.parseFloat(request.getParameter("descuento")); } catch (NumberFormatException e) { descuento = 0F; }
		  try{ descuento2 = Float.parseFloat(request.getParameter("descuento2")); } catch (NumberFormatException e) { descuento2 = 0F; }
		  try{ descuento3 = Float.parseFloat(request.getParameter("descuento3")); } catch (NumberFormatException e) { descuento3 = 0F; }
		  try{ descuento4 = Float.parseFloat(request.getParameter("descuento4")); } catch (NumberFormatException e) { descuento4 = 0F; }
		  try{ descuento5 = Float.parseFloat(request.getParameter("descuento5")); } catch (NumberFormatException e) { descuento5 = 0F; }
	  }
	  else
	  {
		  JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		  ent.m_Where = "ID_EntidadVenta = '" + rec.getID_EntidadVenta() + "'";
		  ent.Open();
		  JInvServInvSetMasV2 inv = new JInvServInvSetMasV2(request);
		  inv.m_Where = "Clave = '" + p(request.getParameter("idprod")) + "'";
		  inv.Open();
		  
		  boolean desglose = ent.getAbsRow(0).getDesgloseMOSTR();
		  float iva = ent.getAbsRow(0).getIVA();
		  boolean apliva = inv.getAbsRow(0).getIVA();
		  
		  if(desglose && apliva && iva != 0.0)
		  {
			  float ivapor = (iva / 100) + 1;
			  try{	descuento = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento")) / (inv.getAbsRow(0).getPrecio() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento = 0F; }
			  try{	descuento2 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento2")) / (inv.getAbsRow(0).getPrecio2() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento2 = 0F; }
			  try{	descuento3 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento3")) / (inv.getAbsRow(0).getPrecio3() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento3 = 0F; }
			  try{	descuento4 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento4")) / (inv.getAbsRow(0).getPrecio4() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento4 = 0F; }
			  try{	descuento5 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento5")) / (inv.getAbsRow(0).getPrecio5() / ivapor)) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento5 = 0F; }
		  }
		  else
		  {
			  try{	descuento = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento")) / inv.getAbsRow(0).getPrecio()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento = 0F; }
			  try{	descuento2 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento2")) / inv.getAbsRow(0).getPrecio2()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento2 = 0F; }
			  try{	descuento3 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento3")) / inv.getAbsRow(0).getPrecio3()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento3 = 0F; }
			  try{	descuento4 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento4")) / inv.getAbsRow(0).getPrecio4()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento4 = 0F; }
			  try{	descuento5 = JUtil.redondear( (((Float.parseFloat(request.getParameter("descuento5")) / inv.getAbsRow(0).getPrecio5()) * 100) - 100) * -1, 6); } catch (NumberFormatException e) { descuento5 = 0F; }
		  }
	  
	  }
	  
	  if(descuento >= 100F || descuento.isInfinite() || descuento.isNaN())
		  descuento = 0F;
	  if(descuento2 >= 100F || descuento2.isInfinite() || descuento2.isNaN())
		  descuento2 = 0F;
	  if(descuento3 >= 100F || descuento3.isInfinite() || descuento3.isNaN())
		  descuento3 = 0F;
	  if(descuento4 >= 100F || descuento4.isInfinite() || descuento4.isNaN())
		  descuento4 = 0F;
	  if(descuento5 >= 100F || descuento5.isInfinite() || descuento5.isNaN())
		  descuento5 = 0F;
		
	  idmensaje = rec.editaPartidaOtr(Integer.parseInt(request.getParameter("idpartida")), request, request.getParameter("idprod"), descuento, descuento2, descuento3, descuento4, descuento5, mensaje);
	
	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
  }
  
  
  public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

	  HttpSession ses = request.getSession(true);
	  JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");

	  float precio = Float.parseFloat(request.getParameter("precio"));
	  byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));

	  idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, request.getParameter("idprod"), precio, idmoneda, mensaje);

	  getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
  }

  public void BorrarPartidaDesc(HttpServletRequest request, HttpServletResponse response)
  	throws ServletException, IOException
  {
	 short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

	 HttpSession ses = request.getSession(true);
	 JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");

	 rec.borraPartidaOtr(Integer.parseInt(request.getParameter("idpartida")));

	 getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

  }
  
  public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	 short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

	 HttpSession ses = request.getSession(true);
	 JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");

	 rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

	 getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

  }

  public void CambiarDescuentosCliente(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");
	
	  String tbl;
	  tbl = "CREATE TABLE _TMP_CLIENT_VS_DESCUENTOS (\n";
	  tbl += "ID_Client int NOT NULL ,\n";
	  tbl += "ID_Prod varchar(20) NOT NULL ,\n";
	  tbl += "Descuento numeric(9,6) NOT NULL ,\n";
	  tbl += "Descuento2 numeric(9,6) NOT NULL ,\n";
	  tbl += "Descuento3 numeric(9,6) NOT NULL ,\n";
	  tbl += "Descuento4 numeric(9,6) NOT NULL ,\n";
	  tbl += "Descuento5 numeric(9,6) NOT NULL \n";
	  tbl += ");\n";
	
	  for(int i = 0; i < rec.getOtros().size(); i++)
	  {
	     tbl += "INSERT INTO _TMP_CLIENT_VS_DESCUENTOS\n";
	     tbl += "VALUES('" + rec.getClaveClient() + "','" + p(rec.getOtro(i).getID_Prod()) + "','" +
	         rec.getOtro(i).getDescuento() + "','" + rec.getOtro(i).getDescuento2() + "','" + rec.getOtro(i).getDescuento3() + "','" + rec.getOtro(i).getDescuento4() + "','" + rec.getOtro(i).getDescuento5() + "');\n";
	  }
	
	  String str = "select * from sp_invserv_inventarios_clientes_vs_descuentos('" + rec.getClaveClient() + "') as (err integer, res varchar, clave integer)";
	 
	  JRetFuncBas rfb = new JRetFuncBas();
		
      doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_CLIENT_VS_DESCUENTOS", rfb);
 
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_CLIENTES", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);

  }
  
  public void CambiarPreciosCliente(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  JVenPoliticasSes rec = (JVenPoliticasSes)ses.getAttribute("ven_pol_dlg");

	  String tbl;
	  tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_CLIENT_VS_INVENTARIO (\n";
	  tbl += "ID_Client int NOT NULL ,\n";
	  tbl += "ID_Prod varchar(20) NOT NULL ,\n";
	  tbl += "Precio numeric(19,4) NOT NULL ,\n";
	  tbl += "Moneda smallint NOT NULL\n";
	  tbl += "); \n";
	
	  for(int i = 0; i < rec.getPartidas().size(); i++)
	  {
		  tbl += "INSERT INTO _TMP_CLIENT_VS_INVENTARIO\n";
		  tbl += "VALUES('" + rec.getClaveClient() + "','" + p(rec.getPartida(i).getID_Prod()) + "','" +
	           rec.getPartida(i).getPrecio() + "','" + rec.getPartida(i).getID_Moneda() + "');\n";
	  }

	  String str = "select * from sp_invserv_cambio_precios('','" + rec.getClaveClient() + "', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00') as (err integer, res varchar, clave varchar)";
	  
	  JRetFuncBas rfb = new JRetFuncBas();
		
      doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_CLIENT_VS_INVENTARIO", rfb);
 
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_POL_CLIENTES", "VPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/ventas/ven_pol_dlg.jsp", request, response);
    
  }
     
}
