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
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmFormatosSet;
import forseti.sets.JFormatosDetSet;


@SuppressWarnings("serial")
public class JAdmFormatosDlg extends JForsetiApl
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

      String adm_formatos_dlg = "";
      request.setAttribute("adm_formatos_dlg",adm_formatos_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
 
        if(request.getParameter("proceso").equals("AGREGAR_FORMATO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_FORMATOS_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_FORMATOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_FORMATOS_AGREGAR","AFMT||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            

        	if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
        	{
        		HttpSession ses = request.getSession(true);
        		JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
        		if(rec == null)
        		{
        			rec = new JAdmFormatosSes(request);
        			ses.setAttribute("adm_formatos_dlg", rec);
        		}
        		else
        			rec.resetear(request);
                  
                       
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);
        		return;
        	}
        	else
        	{
        		// Solicitud de envio a procesar
        		if(request.getParameter("subproceso").equals("ENVIAR"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				Actualizar("agregar", request, response);
        				return;
        			}
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("CAMBIO_TIPO"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        				CambioTipo(request, response);
        			
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("CAMBIO_ETQCAB"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        				CambioEtiqueta("Cab", request, response);
        			
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
        			return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("CAMBIO_ETQDET"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        				CambioEtiqueta("Det", request, response);
        			
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("AGR_PART"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				if(VerificarParametrosPartida(request, response))
        					AgregarPartida(request, response);
        			}
        		    irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("AGR_PART_DET"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				if(VerificarParametrosPartidaDet(request, response))
        					AgregarPartidaDet(request, response);
        			}
	        		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  	{
        			if(AgregarCabecero(request,response) == -1)
       			  	{
        				BorrarPartida("Cab", request, response);
       			  	}
	       		  	irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
	       		  	return;
	   		  	}
        		else if(request.getParameter("subproceso").equals("BORR_PART_DET"))
	   		  	{
        			if(AgregarCabecero(request,response) == -1)
       			  	{
        				BorrarPartida("Det", request, response);
       			  	}
	       		  	irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
	       		  	return;
	   		  	}
        		else if(request.getParameter("subproceso").equals("PRE_EDIT_PART"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				PreEditarPartida("Cab", request, response);
        			}
  	       		    irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
  	       		    return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("PRE_EDIT_PART_DET"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				PreEditarPartida("Det", request, response);
        			}
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("EDIT_PART"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				if(VerificarParametrosPartida(request, response))
        					EditarPartida(request, response);
        			}
        			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
        		else if(request.getParameter("subproceso").equals("EDIT_PART_DET"))
	       	  	{
        			if(AgregarCabecero(request,response) == -1)
        			{
        				if(VerificarParametrosPartidaDet(request, response))
        					EditarPartidaDet(request, response);
        			}
        		    irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
	       	  		return;
	       	  	}
   
	   	  }
	
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_FORMATO"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_FORMATOS"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_FORMATOS");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_FORMATOS","AFMT||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("id") != null)
            {
            	String[] valoresParam = request.getParameterValues("id");
            	if(valoresParam.length == 1)
            	{
            		HttpSession ses = request.getSession(true);
            		JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
            		if(rec == null)
            		{
            			rec = new JAdmFormatosSes(request);
            			ses.setAttribute("adm_formatos_dlg", rec);
            		}
            		else
            			rec.resetear(request);
     
            		JAdmFormatosSet SetMod = new JAdmFormatosSet(request);
            		SetMod.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "'";
            		SetMod.Open();
            		JFormatosDetSet SetSis = new JFormatosDetSet(request);
            		SetSis.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and Formato = 'S'";
            		SetSis.m_OrderBy = "ID_Part asc";
            		SetSis.Open();
            		JFormatosDetSet SetCab = new JFormatosDetSet(request);
            		SetCab.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and ( Formato = 'C' or Formato = 'F' )";
            		SetCab.m_OrderBy = "ID_Part asc";
            		SetCab.Open();
            		JFormatosDetSet SetDet = new JFormatosDetSet(request);
            		SetDet.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and Formato = 'D'";
            		SetDet.m_OrderBy = "ID_Part asc";
            		SetDet.Open();
	            	
            		rec.setID_Formato(SetMod.getAbsRow(0).getID_Formato());
            		rec.setDescripcion(SetMod.getAbsRow(0).getDescripcion());
            		rec.setTipo(SetMod.getAbsRow(0).getTipo());
                  
            		for(int i = 0; i< SetSis.getNumRows(); i++)
            		{
            			if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPTIT"))
            				rec.estEncabezado("Titulo", SetSis.getAbsRow(i).getValor());
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPETQ"))
            				rec.estEncabezado("Etiqueta", SetSis.getAbsRow(i).getValor());
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPCAB"))
            				rec.estEncabezado("Cabecero", SetSis.getAbsRow(i).getValor());
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPDET"))
            				rec.estEncabezado("Detalle", SetSis.getAbsRow(i).getValor());
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_CAB"))
            				rec.setCab(SetSis.getAbsRow(i).getAlto());
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_DET"))
            			{
            				rec.setDetAlt(SetSis.getAbsRow(i).getAlto());
            				rec.setDetIni(SetSis.getAbsRow(i).getYPos());
            				rec.setDetNum(Short.parseShort(SetSis.getAbsRow(i).getValor()));
            			}
            			else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_VENTANA"))
            			{
            				rec.setVentanaHW((int)SetSis.getAbsRow(i).getAncho());
            				rec.setVentanaVW((int)SetSis.getAbsRow(i).getAlto());
            			}
                               	                  	  
            		}
            		for(int i = 0; i< SetCab.getNumRows(); i++)
            		{
            			rec.agregaPartida("Cab_NAP", SetCab.getAbsRow(i).getEtiqueta(), SetCab.getAbsRow(i).getValor(), 
            				SetCab.getAbsRow(i).getXPos(), SetCab.getAbsRow(i).getYPos(), SetCab.getAbsRow(i).getAncho(), SetCab.getAbsRow(i).getAlto(), 
	            				SetCab.getAbsRow(i).getAlinHor(), SetCab.getAbsRow(i).getAlinVer(), SetCab.getAbsRow(i).getFormato().equals("F"));
            		}
            		for(int i = 0; i< SetDet.getNumRows(); i++)
            		{
            			rec.agregaPartida("Det_NAP", SetDet.getAbsRow(i).getEtiqueta(), SetDet.getAbsRow(i).getValor(), 
            				SetDet.getAbsRow(i).getXPos(), SetDet.getAbsRow(i).getYPos(), SetDet.getAbsRow(i).getAncho(), SetDet.getAbsRow(i).getAlto(), 
	            				SetDet.getAbsRow(i).getAlinHor(), SetDet.getAbsRow(i).getAlinVer(), false);
            		}
 
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_FORMATO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ADM_FORMATOS_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_FORMATOS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_FORMATOS_AGREGAR","AFMT||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
            	if(request.getParameter("id") != null)
                {
            		String[] valoresParam = request.getParameterValues("id");
            		if(valoresParam.length == 1)
            		{
                	  
            			HttpSession ses = request.getSession(true);
            			JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
            			if(rec == null)
            			{
            				rec = new JAdmFormatosSes(request);
            				ses.setAttribute("adm_formatos_dlg", rec);
            			}
            			else
            				rec.resetear(request);
         
                    
            			JAdmFormatosSet SetMod = new JAdmFormatosSet(request);
            			SetMod.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "'";
            			SetMod.Open();
                      
            			rec.setID_Formato(SetMod.getAbsRow(0).getID_Formato());
            			rec.setDescripcion(SetMod.getAbsRow(0).getDescripcion());
            			rec.setTipo(SetMod.getAbsRow(0).getTipo());
            			rec.RecargarVista(request);
                      
            			JFormatosDetSet SetSis = new JFormatosDetSet(request);
            			SetSis.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and Formato = 'S'";
            			SetSis.m_OrderBy = "ID_Part asc";
            			SetSis.Open();
            			JFormatosDetSet SetCab = new JFormatosDetSet(request);
            			SetCab.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and ( Formato = 'C' or Formato = 'F' )";
            			SetCab.m_OrderBy = "ID_Part asc";
            			SetCab.Open();
            			JFormatosDetSet SetDet = new JFormatosDetSet(request);
            			SetDet.m_Where = "ID_Formato = '" + p(request.getParameter("id")) + "' and Formato = 'D'";
            			SetDet.m_OrderBy = "ID_Part asc";
            			SetDet.Open();
    	            	
            			for(int i = 0; i< SetSis.getNumRows(); i++)
            			{
            				if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPTIT"))
            					rec.estEncabezado("Titulo", SetSis.getAbsRow(i).getValor());
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPETQ"))
            					rec.estEncabezado("Etiqueta", SetSis.getAbsRow(i).getValor());
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPCAB"))
            					rec.estEncabezado("Cabecero", SetSis.getAbsRow(i).getValor());
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_IMPDET"))
            					rec.estEncabezado("Detalle", SetSis.getAbsRow(i).getValor());
                    	  
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_CAB"))
            					rec.setCab(SetSis.getAbsRow(i).getAlto());
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_DET"))
            				{
            					rec.setDetAlt(SetSis.getAbsRow(i).getAlto());
            					rec.setDetIni(SetSis.getAbsRow(i).getYPos());
            					rec.setDetNum(Short.parseShort(SetSis.getAbsRow(i).getValor()));
            				}
            				else if(SetSis.getAbsRow(i).getEtiqueta().equals("FSI_VENTANA"))
            				{
            					rec.setVentanaHW((int)SetSis.getAbsRow(i).getAncho());
            					rec.setVentanaVW((int)SetSis.getAbsRow(i).getAlto());
            				}
                     	          	
            			}
            			for(int i = 0; i< SetCab.getNumRows(); i++)
            			{
            				rec.agregaPartida("Cab_NAP", SetCab.getAbsRow(i).getEtiqueta(), SetCab.getAbsRow(i).getValor(), 
    	            			SetCab.getAbsRow(i).getXPos(), SetCab.getAbsRow(i).getYPos(), SetCab.getAbsRow(i).getAncho(), SetCab.getAbsRow(i).getAlto(), 
    	            				SetCab.getAbsRow(i).getAlinHor(), SetCab.getAbsRow(i).getAlinVer(), SetCab.getAbsRow(i).getFormato().equals("F"));
            			}
            			for(int i = 0; i< SetDet.getNumRows(); i++)
            			{
            				rec.agregaPartida("Det_NAP", SetDet.getAbsRow(i).getEtiqueta(), SetDet.getAbsRow(i).getValor(), 
    	            			SetDet.getAbsRow(i).getXPos(), SetDet.getAbsRow(i).getYPos(), SetDet.getAbsRow(i).getAncho(), SetDet.getAbsRow(i).getAlto(), 
    	            				SetDet.getAbsRow(i).getAlinHor(), SetDet.getAbsRow(i).getAlinVer(), false);
            			}
     
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);
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
  	       	  	// Solicitud de envio a procesar
          	  	if(request.getParameter("subproceso").equals("ENVIAR"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			Actualizar("cambiar", request, response);
          	  			return;
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("CAMBIO_TIPO"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  			CambioTipo(request, response);
          	  			
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("CAMBIO_ETQCAB"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  			CambioEtiqueta("Cab", request, response);
          	  			
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("CAMBIO_ETQDET"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  			CambioEtiqueta("Det", request, response);
          	  			
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				AgregarPartida(request, response);
          	  		}
          		    irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          		    return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("AGR_PART_DET"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartidaDet(request, response))
          	  				AgregarPartidaDet(request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("BORR_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			BorrarPartida("Cab", request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
   	   			  	return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("BORR_PART_DET"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			BorrarPartida("Det", request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
   	   			  	return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("PRE_EDIT_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			PreEditarPartida("Cab", request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("PRE_EDIT_PART_DET"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			PreEditarPartida("Det", request, response);
          	  		}
  	       		    irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);  
  	       		    return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("EDIT_PART"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartida(request, response))
          	  				EditarPartida(request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
          	  	}
          	  	else if(request.getParameter("subproceso").equals("EDIT_PART_DET"))
          	  	{
          	  		if(AgregarCabecero(request,response) == -1)
          	  		{
          	  			if(VerificarParametrosPartidaDet(request, response))
          	  				EditarPartidaDet(request, response);
          	  		}
          	  		irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response); 
          	  		return;
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
  
    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
    	HttpSession ses = request.getSession(true);
    	JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
		
    	float x = Float.parseFloat(request.getParameter("pos_x"));
    	float y = Float.parseFloat(request.getParameter("pos_y"));
    	float alt = Float.parseFloat(request.getParameter("alt"));
    	float anc = Float.parseFloat(request.getParameter("anc"));
    	boolean fin = ( request.getParameter("fin") == null ) ? false : true;
		  
    	idmensaje = rec.editaPartida("Cab", Integer.parseInt(request.getParameter("FMTID")), request.getParameter("etiqueta"), request.getParameter("valor"), x, y, anc, alt, request.getParameter("hor"), request.getParameter("ver"), fin);
		
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	}
	
	public void EditarPartidaDet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
		HttpSession ses = request.getSession(true);
		JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
	
		float x = Float.parseFloat(request.getParameter("pos_x_det"));
		float y = Float.parseFloat(request.getParameter("pos_y_det"));
		float alt = Float.parseFloat(request.getParameter("alt_det"));
		float anc = Float.parseFloat(request.getParameter("anc_det"));
	  
		idmensaje = rec.editaPartida("Det", Integer.parseInt(request.getParameter("FMTID")), request.getParameter("etiqueta_det"), request.getParameter("valor_det"), x, y, anc, alt, request.getParameter("hor_det"), request.getParameter("ver_det"), false);
	
		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
	}
      
    public void BorrarPartida(String tipo, HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	HttpSession ses = request.getSession(true);
    	JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");

    	rec.borraPartida(tipo, Integer.parseInt(request.getParameter("idpartida")));
	
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	
	}

    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
    	HttpSession ses = request.getSession(true);
    	JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
	
    	float x = Float.parseFloat(request.getParameter("pos_x"));
    	float y = Float.parseFloat(request.getParameter("pos_y"));
    	float alt = Float.parseFloat(request.getParameter("alt"));
    	float anc = Float.parseFloat(request.getParameter("anc"));
    	boolean fin = ( request.getParameter("fin") == null ) ? false : true;
	  
    	idmensaje = rec.agregaPartida("Cab", request.getParameter("etiqueta"), request.getParameter("valor"), x, y, anc, alt, request.getParameter("hor"), request.getParameter("ver"), fin);
	
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}
    
    public void AgregarPartidaDet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
    	HttpSession ses = request.getSession(true);
    	JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
	
    	float x = Float.parseFloat(request.getParameter("pos_x_det"));
    	float y = Float.parseFloat(request.getParameter("pos_y_det"));
    	float alt = Float.parseFloat(request.getParameter("alt_det"));
    	float anc = Float.parseFloat(request.getParameter("anc_det"));
	  
    	idmensaje = rec.agregaPartida("Det", request.getParameter("etiqueta_det"), request.getParameter("valor_det"), x, y, anc, alt, request.getParameter("hor_det"), request.getParameter("ver_det"), false);
	
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}

    public void PreEditarPartida(String tipo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
    	HttpSession ses = request.getSession(true);
    	JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
	
    	rec.preEditaPartida(tipo, Integer.parseInt(request.getParameter("FMTID")));
	
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	
	}

   
    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("etiqueta") != null && request.getParameter("valor") != null &&
	       request.getParameter("pos_x") != null && request.getParameter("pos_y") != null && 
	       request.getParameter("alt") != null && request.getParameter("anc") != null &&
	       request.getParameter("hor") != null && request.getParameter("ver") != null &&
	       !request.getParameter("etiqueta").equals("") && !request.getParameter("valor").equals("") &&
	       !request.getParameter("pos_x").equals("") && !request.getParameter("pos_y").equals("") && 
	       !request.getParameter("alt").equals("") && !request.getParameter("anc").equals("") &&
	       !request.getParameter("hor").equals("") && !request.getParameter("ver").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO",2);
	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  		return false;
	    }
	}
    
    public boolean VerificarParametrosPartidaDet(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("etiqueta_det") != null && request.getParameter("valor_det") != null &&
	       request.getParameter("pos_x_det") != null && request.getParameter("pos_y_det") != null && 
	       request.getParameter("alt_det") != null && request.getParameter("anc_det") != null &&
	       request.getParameter("hor_det") != null && request.getParameter("ver_det") != null &&
	       !request.getParameter("etiqueta_det").equals("") && !request.getParameter("valor_det").equals("") &&
	       !request.getParameter("pos_x_det").equals("") && !request.getParameter("pos_y_det").equals("") && 
	       !request.getParameter("alt_det").equals("") && !request.getParameter("anc_det").equals("") &&
	       !request.getParameter("hor_det").equals("") && !request.getParameter("ver_det").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	  		return false;
	    }
	}
    
    public short CambioTipo(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
      	HttpSession ses = request.getSession(true);
        JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
         
       	idmensaje = rec.cambioTipo(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public short CambioEtiqueta(String tipo, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
      	HttpSession ses = request.getSession(true);
        JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
         
       	idmensaje = rec.cambioEtiqueta(tipo, request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
 
    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
      	HttpSession ses = request.getSession(true);
        JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
         
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public void Actualizar(String proc, HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
        HttpSession ses = request.getSession(true);
        JAdmFormatosSes rec = (JAdmFormatosSes)ses.getAttribute("adm_formatos_dlg");
        
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_FORMATOS_DET (\n";
        tbl += "    ID_Part smallint NOT NULL ,\n";
        tbl += "    Etiqueta varchar(20) NOT NULL ,\n";
        tbl += "    Valor varchar(254) NOT NULL ,\n";
        tbl += "    XPos numeric(5, 2) NULL ,\n";
        tbl += "   	YPos numeric(5, 2) NULL ,\n";
        tbl += " 	Ancho numeric(5, 2) NULL ,\n";
        tbl += "	Alto numeric(5, 2) NULL ,\n";
        tbl += "	Formato char(1) NULL ,\n";
        tbl += "	FGColor char(6) NULL ,\n";
        tbl += "    AlinHor varchar(20) NULL ,\n";
        tbl += " 	AlinVer varchar(20) NULL \n";
        tbl += "); \n\n";

        int part = 1;
        
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_CAB','',null,null,null,'" + rec.getCab() + "','S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_VENTANA','',null,null,'" + rec.getVentanaHW() + "','" + rec.getVentanaVW() + "','S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_DET','" + rec.getDetNum() + "',null,'" + rec.getDetIni() + "',null,'" + rec.getDetAlt() + "','S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_IMPTIT','" + p(rec.obtEncabezado("Titulo")) + "',null,null,null,null,'S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_IMPETQ','" + p(rec.obtEncabezado("Etiqueta")) + "',null,null,null,null,'S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_IMPCAB','" + p(rec.obtEncabezado("Cabecero")) + "',null,null,null,null,'S','000000',null,null);\n";
        tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','FSI_IMPDET','" + p(rec.obtEncabezado("Detalle")) + "',null,null,null,null,'S','000000',null,null);\n";
        
        for(int i = 0; i < rec.numPartidas(); i++)
		{
            tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','" + p(rec.getPartida(i).getEtiqueta()) + "','" + p(rec.getPartida(i).getValor()) + 
            	"','" + rec.getPartida(i).getX() + "','" + rec.getPartida(i).getY() + "','" + rec.getPartida(i).getAnc() + "','" + rec.getPartida(i).getAlt() + 
            	"','" + ((rec.getPartida(i).getFin()) ? "F" : "C") + "','000000','" + p(rec.getPartida(i).getHor()) + "','" + p(rec.getPartida(i).getVer()) + "');\n";
        }

        for(int i = 0; i < rec.numObjetos(); i++)
		{
            tbl += "\n\ninsert into _TMP_FORMATOS_DET\nvalues('" + (part++) + "','" + p(rec.getObjeto(i).getEtiqueta()) + "','" + p(rec.getObjeto(i).getValor()) + 
            	"','" + rec.getObjeto(i).getX() + "','" + rec.getObjeto(i).getY() + "','" + rec.getObjeto(i).getAnc() + "','" + rec.getObjeto(i).getAlt() + 
            	"','D','000000','" + p(rec.getObjeto(i).getHor()) + "','" + p(rec.getObjeto(i).getVer()) + "');\n";
        }

    	String str = "select * from sp_formatos_" + proc + "('" + p(rec.getID_Formato()) + "','" + p(rec.getDescripcion()) + "','" + p(rec.getTipo()) + "') as ( err integer, res varchar, clave varchar );";
        
        JRetFuncBas rfb = new JRetFuncBas();
    	
		//doDebugSQL(request, response, tbl + "\n\n\n\n" + str);
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_FORMATOS_DET ", rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADM_FORMATOS_AGREGAR","AFMT|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/administracion/adm_formatos_dlg.jsp", request, response);
   	
    }   
    
}
