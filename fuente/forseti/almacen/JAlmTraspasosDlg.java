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
package forseti.almacen;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.JBajarArchivo;
import forseti.JForsetiApl;
//import forseti.JForsetiCFD;
//import forseti.JForsetiCFDEntidad;
import forseti.JForsetiCFD;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;

import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JAlmacenesMovBodSetV2;
import forseti.sets.JAlmacenesMovBodSetDetallesV2;
import forseti.sets.JAlmacenesMovReqSet;
import forseti.sets.JPublicBodegasCatSetV2;

@SuppressWarnings("serial")
public class JAlmTraspasosDlg extends JForsetiApl
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

      String alm_traspasos_dlg = "";
      request.setAttribute("alm_traspasos_dlg",alm_traspasos_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
        JAlmacenesMovimSetIdsV2 setids;
        if(request.getParameter("tipomov").equals("TRASPASOS"))
        	setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_TRASPASOS").getEspecial(),"P");
        else //(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
          	setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial(),"P");
        setids.Open();
        
        if(setids.getNumRows() < 1)
        {
        	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", (request.getParameter("tipomov").equals("TRASPASOS") ? "ALM_TRASPASOS" : "ALM_REQUERIMIENTOS"));
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),(request.getParameter("tipomov").equals("TRASPASOS") ? "ALM_TRASPASOS" : "ALM_REQUERIMIENTOS"),(request.getParameter("tipomov").equals("TRASPASOS") ? "TALM" : "RALM") + "||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        } 
        
     // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(request.getParameter("tipomov").equals("TRASPASOS"))
        {   
        	if(!request.getParameter("proceso").equals("AGREGAR_TRASPASO") && request.getParameter("ID") != null)
            {
        		JAlmacenesMovBodSetV2 set = new JAlmacenesMovBodSetV2(request);
        		set.m_Where = "ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' and ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	set.Open();
            	if(set.getNumRows() < 1)
            	{
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS");
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
	  	  	
        }
        else
        {    
        	if(!request.getParameter("proceso").equals("AGREGAR_TRASPASO") && request.getParameter("ID") != null)
            {
        		JAlmacenesMovReqSet set = new JAlmacenesMovReqSet(request);
        		set.m_Where = "(ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' or ID_BodegaDEST = '" + setids.getAbsRow(0).getID_Bodega() + "') and ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	set.Open();
            	if(set.getNumRows() < 1)
            	{
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS");
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS","RALM|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
        		  	  	
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_TRASPASO"))
        {
        	// Revisa si tiene permisos
            if(request.getParameter("tipomov").equals("TRASPASOS"))
            {	
            	if(!getSesion(request).getPermiso("ALM_TRASPASOS_AGREGAR"))
            	{
          		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS_AGREGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS_AGREGAR","TALM||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
            	}
            }
            else // Requerimientos
            {
          	  	if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS_AGREGAR"))
                {
          	  		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS_AGREGAR");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS_AGREGAR","RALM||||",mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            }
                     
          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  	HttpSession ses = request.getSession(true);
	            JAlmTraspasosSes rec;
	            
	            if(request.getParameter("tipomov").equals("TRASPASOS"))
	            {	
	            	rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
	            	if(rec == null)
	            	{
	            		rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            		ses.setAttribute("alm_traspasos_dlg", rec);
	            	}
	            	else
	                   	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	                	
	            }
	            else 
	            {	
	            	rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
	            	if(rec == null)
	            	{
	            		rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            		ses.setAttribute("alm_requerimientos_dlg", rec);
	            	}
	            	else
	                	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            }
	            
	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
	            return;
          }
          else
          {
	       	  // Solicitud de envio a procesar
	       	  if(request.getParameter("subproceso").equals("ENVIAR"))
	       	  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametros(request, response))
       				  {
       					  if(request.getParameter("tipomov").equals("TRASPASOS"))
       					  {
       						  HttpSession ses = request.getSession(true);
       						  JAlmTraspasosSes rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       						  
       						  StringBuffer bfmensaje = new StringBuffer();
       						  idmensaje = rec.VerificacionesFinales(request, bfmensaje);
       						  getSesion(request).setID_Mensaje(idmensaje, bfmensaje.toString());
	       			                
       						  if(idmensaje != -1)
       						  {
       							  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
       							  return;
       						  }
       						  
       						  Agregar(request, response, "TRASPASOS", setids);
       						  return;
       					  }
       					  else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
       					  {
       						  Agregar(request, response, "REQUERIMIENTOS", setids);
       						  return;
       					  }
       					  
       				  }
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
       			  return;
	   		  }
	       	  else if(request.getParameter("subproceso").equals("AGR_BODEGA"))
	       	  {
	       		  AgregarCabecero(request,response);
	       	      irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
	       	      return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    AgregarPartida(request, response);
       			  }
	       		 
       			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    EditarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
 	   			  return;
	   		  }	   	  
	      }
	
        }
        //////////////////////////////////////////////////////////////////
        else if(request.getParameter("proceso").equals("CAMBIAR_TRASPASO"))
        {
            // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS_CAMBIAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS_CAMBIAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS_CAMBIAR","RALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
        	else
        		return;
        	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
                  String[] valoresParam = request.getParameterValues("ID");
                  if(valoresParam.length == 1)
                  {
                	  	JAlmacenesMovReqSet SetMod = new JAlmacenesMovReqSet(request);
      	            	SetMod.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
      	            	SetMod.Open();
      	            	
      	            	if(SetMod.getAbsRow(0).getID_BodegaDEST() == setids.getAbsRow(0).getID_Bodega())
      	            	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR",1);//"PRECAUCION: Este requerimiento solo se puede cambiar desde la bodega de origen <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;

      	            	}
      	            	
      	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR",2);//"PRECAUCION: Este requerimiento ya esta cancelado, no se puede cambiar <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		      	      	} 
		
		      	      	if(SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR",3);//"PRECAUCION: Este requerimiento ya tiene un traspaso asociado, no se puede cambiar <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		      	      	} 

		      	      	HttpSession ses = request.getSession(true);
		      	      	JAlmTraspasosSes rec;
		      	      	if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
		      	      	{
		      	      		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
		      	      		if(rec == null)
		      	      		{
	            			  	rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
	            			  	ses.setAttribute("alm_requerimientos_dlg", rec);
		      	      		}
		      	      		else
	            			  	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());

		      	      		// Llena la consulta
	                        JAlmacenesMovReqSet SetCab = new JAlmacenesMovReqSet(request);
	                        SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
	                    	SetCab.Open();
	              	      	
	                    	JPublicBodegasCatSetV2 bod = new JPublicBodegasCatSetV2(request);
	                    	bod.m_Where = "ID_Bodega = '" + SetCab.getAbsRow(0).getID_BodegaDEST() + "'";
	                    	bod.Open();
	                    	
	                    	rec.setID_BodegaDest((byte)SetCab.getAbsRow(0).getID_BodegaDEST());
	                    	rec.setNumero(SetCab.getAbsRow(0).getRequerimiento());
	                    	rec.setFecha(SetCab.getAbsRow(0).getFecha());
	                    	rec.setRef(SetCab.getAbsRow(0).getReferencia());
	                    	rec.setBodegaDest_Descripcion(SetCab.getAbsRow(0).getBodegaDEST());
	                    	rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
	                    	rec.setAuditarAlmDEST(bod.getAbsRow(0).getAuditarAlm());
	                    	rec.setManejoStocks(bod.getAbsRow(0).getManejoStocks());
	                    
	                    	JAlmacenesMovBodSetDetallesV2 SetDet = new JAlmacenesMovBodSetDetallesV2(request, request.getParameter("tipomov"));
	                    	SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
	                    	SetDet.m_OrderBy = "Partida ASC";
	                    	SetDet.Open();
	                	
	                    	for(int i = 0; i< SetDet.getNumRows(); i++)
	                    	{
	                    		rec.agregaPartida(request, SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion());
	                    	}
	                    
	                    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                    	irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
	                    	return;
		      	      	}
                  }
                  else
                  {
                    idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite cambiar un requerimiento a la vez <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                  }
                }
                else
                {
                   idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del requerimiento que se quiere cambiar <br>";
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
     				  if(VerificarParametros(request, response))
     					  Cambiar(request, response, "REQUERIMIENTOS");
     				  return;
    			  }
	       		
          		  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
          		  return;
         			  
  	   		  }
          	  else if(request.getParameter("subproceso").equals("AGR_BODEGA"))
	       	  {
	       		  AgregarCabecero(request,response);
	       	      irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
	       	      return;
	       	  }
          	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				if(VerificarParametrosPartida(request, response))
  	   			    	AgregarPartida(request, response);
     			  }
	       		  
     			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
     			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				if(VerificarParametrosPartida(request, response))
  	   			    	EditarPartida(request, response);
     			  }
	       		  
     			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
     			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				BorrarPartida(request, response);
     			  }
	       		  
     			  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);  
	   			  return;
	   		  }	  
  	   	  }
           
        } 
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if(request.getParameter("proceso").equals("TRASPASAR_TRASPASO"))
        {
        	 // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS_TRASPASAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS_TRASPASAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS_TRASPASAR","RALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
        	else
        		return;
 
        	
            if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
            	if(request.getParameter("ID") != null)
                {
                  String[] valoresParam = request.getParameterValues("ID");
                  if(valoresParam.length == 1)
                  {
	                	           		
                		JAlmacenesMovReqSet SetMod = new JAlmacenesMovReqSet(request);
      	            	SetMod.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
      	            	SetMod.Open();
      	            	
      	            	if(SetMod.getAbsRow(0).getID_BodegaDEST() != setids.getAbsRow(0).getID_Bodega())
      	            	{
		      	      		idmensaje = 1;
		                    mensaje += "PRECAUCION: Este requerimiento solo se puede traspasar desde la bodega de destino <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;

      	            	}
      	            	
      	            	if(SetMod.getAbsRow(0).getStatus().equals("C"))
		      	      	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR",4);//"PRECAUCION: Este requerimiento ya esta cancelado, no se puede traspasar <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		      	      	} 
		
		      	      	if(SetMod.getAbsRow(0).getStatus().equals("N"))
		      	      	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR",5);//"PRECAUCION: Este requerimiento ya tiene un traspaso asociado, no se puede volver a traspasar <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		      	      	} 

		      	      	HttpSession ses = request.getSession(true);
		      	      	JAlmTraspasosSes rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
		      	      	if(rec == null)
		      	      	{
		      	      		rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
                            ses.setAttribute("alm_requerimientos_dlg", rec);
		      	      	}
		      	      	else
		      	      		rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
                     
		      	      	// Llena la consulta
		      	      	JAlmacenesMovReqSet SetCab = new JAlmacenesMovReqSet(request);
		      	      	SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
		      	      	SetCab.Open();
              	      	
                    	JPublicBodegasCatSetV2 bod = new JPublicBodegasCatSetV2(request);
                    	bod.m_Where = "ID_Bodega = '" + SetCab.getAbsRow(0).getID_Bodega() + "'";
                    	bod.Open();
                    	
                    	rec.setID_BodegaDest((byte)SetCab.getAbsRow(0).getID_Bodega());
                    	rec.setFecha(SetCab.getAbsRow(0).getFecha());
                    	rec.setRef(SetCab.getAbsRow(0).getReferencia());
                    	rec.setBodegaDest_Descripcion(SetCab.getAbsRow(0).getBodega());
                    	rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
                    	rec.setAuditarAlmDEST(bod.getAbsRow(0).getAuditarAlm());
                    	rec.setManejoStocks(bod.getAbsRow(0).getManejoStocks());
                    	rec.setTraspasoNum(setids.getAbsRow(0).getSalida());
                    	JAlmacenesMovBodSetDetallesV2 SetDet = new JAlmacenesMovBodSetDetallesV2(request, request.getParameter("tipomov"));
                      	SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                    	SetDet.m_OrderBy = "Partida ASC";
                    	SetDet.Open();
                    	
                    	for(int i = 0; i< SetDet.getNumRows(); i++)
                    	{
                    		rec.agregaPartida(request, SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion());
                    	}
                                  
                    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    	irApag("/forsetiweb/almacen/alm_traspasos_dlg_generar.jsp", request, response);
                    	return;
                  }
                  else
                  {
                    idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite generar el traspaso de un requerimiento a la vez <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                  }
                }
                else
                {
                   idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del requerimiento que se quiere traspasar <br>";
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
                  if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
         		  {
  	       			if(request.getParameter("fecha") == null || request.getParameter("fecha").equals(""))
  	       			{
  	       				idmensaje = 1; mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR2",1);//"PRECAUCION: Se debe enviar la fecha del traspaso <br>";
  	       				getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	       				irApag("/forsetiweb/ventas/alm_traspasos_dlg_generar.jsp", request, response);
  	       				return;
  	       			}
  	       			else
         			{
         			  if(VerificarParametros(request, response))
         			  {
         				 StringBuffer sbmensaje = new StringBuffer();
         				 HttpSession ses = request.getSession(true);
         				 JAlmTraspasosSes rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
         				 
         				 idmensaje = rec.VerificacionesFinales(request, sbmensaje);
         				     
         				 if(idmensaje != -1)
         				 {
         					getSesion(request).setID_Mensaje(idmensaje, sbmensaje.toString());
             	            irApag("/forsetiweb/almacen/alm_traspasos_dlg_generar.jsp", request, response);
         					return;
         				 }
         				 
         				 AgregarDesde(request, response, request.getParameter("ID"), setids);
         				 return;
         			  }
         			}
         		 }
       		  }
  	   	  	}
           
        }           
        else if(request.getParameter("proceso").equals("CONSULTAR_TRASPASO"))
        {
        	// Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("TRASPASOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_TRASPASOS"))
                {
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM||||",mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            }
            else // Requerimientos
            {
            	if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS"))
                {
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS","RALM||||",mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            }
        	
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	            	  
                  HttpSession ses = request.getSession(true);
                  JAlmTraspasosSes rec;
                  if(request.getParameter("tipomov").equals("TRASPASOS"))
            	  {
            		  rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
            		  if(rec == null)
            		  {
            			 	rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
                            ses.setAttribute("alm_traspasos_dlg", rec);
            		  }
            		  else
            			  	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getSalida(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
                      
            	  }
            	  else //REQUERIMIENTOS
            	  {
            		  rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
            		  if(rec == null)
            		  {
            			  	rec = new JAlmTraspasosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());
            			  	ses.setAttribute("alm_requerimientos_dlg", rec);
            		  }
            		  else
            			  	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getRequerimiento(), setids.getAbsRow(0).getAuditarAlm(), setids.getAbsRow(0).getManejoStocks());

            	  }
                
                  if(request.getParameter("tipomov").equals("TRASPASOS"))
                  {
                      // Llena la consulta
                      JAlmacenesMovBodSetV2 SetCab = new JAlmacenesMovBodSetV2(request);
                      SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                  	  SetCab.Open();
                  	
	                  rec.setID_BodegaDest((byte)SetCab.getAbsRow(0).getID_BodegaDEST()); 
	                  rec.setNumero(SetCab.getAbsRow(0).getSalida());
	                  rec.setFecha(SetCab.getAbsRow(0).getFecha());
	                  rec.setRef(SetCab.getAbsRow(0).getReferencia());
	                  rec.setBodegaDest_Descripcion(SetCab.getAbsRow(0).getBodegaDEST());
	                  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
	                  
	                  JAlmacenesMovBodSetDetallesV2 SetDet = new JAlmacenesMovBodSetDetallesV2(request, request.getParameter("tipomov"));
	                  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
	                  SetDet.m_OrderBy = "Partida ASC";
	              	  SetDet.Open();
	              	
	                  for(int i = 0; i< SetDet.getNumRows(); i++)
	              	  {
	              		rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion());
	              	  }
	                  
	        		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_TRASPASOS").getEspecial() + "||","");
	                  	                  
                  }
                  else //if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
                  {
                      // Llena la consulta
                      JAlmacenesMovReqSet SetCab = new JAlmacenesMovReqSet(request);
                      SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                  	  SetCab.Open();
                  	  JPublicBodegasCatSetV2 bod = new JPublicBodegasCatSetV2(request);
                  	  bod.m_Where = "ID_Bodega = '" + SetCab.getAbsRow(0).getID_BodegaDEST() + "'";
                  	  bod.Open();
                  	  
	                  rec.setID_BodegaDest((byte)SetCab.getAbsRow(0).getID_BodegaDEST()); 
	                  rec.setNumero(SetCab.getAbsRow(0).getRequerimiento());
	                  rec.setFecha(SetCab.getAbsRow(0).getFecha());
	                  rec.setRef(SetCab.getAbsRow(0).getReferencia());
	                  rec.setBodegaDest_Descripcion(SetCab.getAbsRow(0).getBodegaDEST());
	                  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
	                  rec.setAuditarAlmDEST(bod.getAbsRow(0).getAuditarAlm());
                  	  rec.setManejoStocks(bod.getAbsRow(0).getManejoStocks());
                  	  JAlmacenesMovBodSetDetallesV2 SetDet = new JAlmacenesMovBodSetDetallesV2(request, request.getParameter("tipomov"));
                  	  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                  	  SetDet.m_OrderBy = "Partida ASC";
                	  SetDet.Open();
                	
                	  for(int i = 0; i< SetDet.getNumRows(); i++)
                	  {
                		  rec.agregaPartida(SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion());
                	  }
                	  
                	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS","RALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial() + "||","");
	                  
                  }
                            	
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
                  return;
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite consultar un traspaso ó requerimiento a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso ó requerimiento que se quiere consultar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        }
        else if(request.getParameter("proceso").equals("SELLAR_TRASPASO"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_TRASPASOS_AGREGAR"))
        	{
      		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS_AGREGAR","TALM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
        	}
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(setids.getAbsRow(0).getCFD().equals("00"))
            	  {
            		  idmensaje = 1;
        			  mensaje += "PRECAUCION: Esta entidad no est&aacute; establecida como CFD. No se puedes sellar el traspaso <br>";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			  return;
              	  }
            	  
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
                  {
            		  JAlmacenesMovBodSetV2 SetMod = new JAlmacenesMovBodSetV2(request);
            		  SetMod.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  
            		  if(SetMod.getAbsRow(0).getTFD() == 3 || SetMod.getAbsRow(0).getStatus().equals("C"))
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este traspaso ya est&aacute; sellado o cancelado<br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		   
            		  StringBuffer sb_mensaje = new StringBuffer(254);
            		  idmensaje = generarCFDI(request, response, "TRASPASOS", Integer.parseInt(request.getParameter("ID")), null, setids, SetMod.getAbsRow(0).getTFD(), sb_mensaje);
            		  mensaje = sb_mensaje.toString();
            		  
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
            		  return;
                  }
            	  
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite sellar un traspaso a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso que se quiere sellar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
           
        }
        else if(request.getParameter("proceso").equals("XML_TRASPASO"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_TRASPASOS"))
        	{
      		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
        	}
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
                  {
            		  JAlmacenesMovBodSetV2 SetMod = new JAlmacenesMovBodSetV2(request);
            		  SetMod.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este traspaso no est&aacute; sellado completamente, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/TFDs/SIGN_TRS-" + request.getParameter("ID") + ".xml";
            		  String destino = "TRS-" + SetMod.getAbsRow(0).getID_Bodega() + "-" + SetMod.getAbsRow(0).getSalida() + ".xml";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "El traspaso se bajo satisfactoriamente";
            		  
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
            		  return;
                  } 	  
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite bajar un traspaso a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso que se quiere bajar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        }
        else if(request.getParameter("proceso").equals("PDF_TRASPASO"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_TRASPASOS_AGREGAR"))
        	{
      		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
        	}
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
                  {
            		  JAlmacenesMovBodSetV2 SetMod = new JAlmacenesMovBodSetV2(request);
            		  SetMod.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetMod.Open();
	            	
            		  if(SetMod.getAbsRow(0).getTFD() != 3)
            		  {
            			  idmensaje = 1;
            			  mensaje += "PRECAUCION: Este traspaso no est&aacute; sellado completamente, no hay nada que bajar <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		  
            		  String nombre = "/usr/local/forseti/emp/" + getSesion(request).getBDCompania() + "/PDFs/TRS-" + request.getParameter("ID") + ".pdf";
            		  String destino = "TRS-" + SetMod.getAbsRow(0).getID_Bodega() + "-" + SetMod.getAbsRow(0).getSalida() + ".pdf";
            		  JBajarArchivo fd = new JBajarArchivo();
            		  
            		  fd.doDownload(response, getServletConfig().getServletContext(), nombre, destino);
            		  
            		  idmensaje = 0;
            		  mensaje = "El traspaso se bajo satisfactoriamente";
            		  
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response); 
            		  return;
                  } 	  
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite bajar un traspaso a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso que se quiere bajar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        }
        else if(request.getParameter("proceso").equals("CANCELAR_TRASPASO"))
        {
            // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("TRASPASOS"))
            {	
          	  	if(!getSesion(request).getPermiso("ALM_TRASPASOS_CANCELAR"))
                {
          	  		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS_CANCELAR");
          	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	  		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS_CANCELAR","TALM||||",mensaje);
          	  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	  		return;
                }
            }
            else // Requerimientos
            {
          	  	if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS_CANCELAR"))
                {
          	  		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS");
          	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	  		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS","RALM||||",mensaje);
          	  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	  		return;
                }
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
            	  {
		            	JAlmacenesMovBodSetV2 SetCab = new JAlmacenesMovBodSetV2(request);
		                SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
		            	SetCab.Open();
		            		
		            	if(SetCab.getAbsRow(0).getStatus().equals("C"))
		            	{
		                    idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_TRASPASOS","DLG","MSJ-PROCERR",1);//PRECAUCION: Este traspaso ya esta cancelado <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		              	} 
		            	else if(setids.getAbsRow(0).getAuditarAlm() && !SetCab.getAbsRow(0).getStatus().equals("R"))
		            	{
		                    idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_TRASPASOS","DLG","MSJ-PROCERR",2);//"PRECAUCION: Este traspaso necesita estar revertido desde el módulo de movimientos al almacén para poder cancelarlo<br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		            		return;
		            	} 
		                else
		                {
		                	StringBuffer sb_mensaje = new StringBuffer();
		            		int idms = cancelarCFDI(request, response, "TRASPASOS", Integer.parseInt(request.getParameter("ID")), SetCab.getAbsRow(0).getTFD(), sb_mensaje);
		            		if(idms == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfd
		            		{
		            			idmensaje = 3; mensaje += sb_mensaje.toString();
		            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
		            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		            			return;
		            		}
		            		else
		            		{
		            			CancelarTraspaso(request, response, "TRASPASOS");
				                return;
		            		}
		            		
		                   
		                }
            	  }
            	  else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
            	  {
		            	JAlmacenesMovReqSet SetCab = new JAlmacenesMovReqSet(request);
		                SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
		            	SetCab.Open();
		            	
      	            	if(SetCab.getAbsRow(0).getID_BodegaDEST() == setids.getAbsRow(0).getID_Bodega())
      	            	{
		      	      		idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR2",2);//"PRECAUCION: Este requerimiento solo se puede cancelar desde la bodega de origen <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;

      	            	}
      	            	
		            	if(SetCab.getAbsRow(0).getStatus().equals("C"))
		            	{
		                    idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR2",3);//PRECAUCION: Este requerimiento ya esta cancelado <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		                    return;
		              	} 
		            	else if(SetCab.getAbsRow(0).getStatus().equals("N"))
		            	{
		                    idmensaje = 1;
		                    mensaje += JUtil.Msj("CEF","ALM_REQUERIMIENTOS","DLG","MSJ-PROCERR2",4);//"PRECAUCION: Este requerimiento ya tiene un traspaso asociado, no se puede cancelar. Primero debes cancelar el traspaso para poder cancelar el requerimiento <br>";
		                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
		                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
		            		return;
		            	} 
		                else
		                {
		                   CancelarTraspaso(request, response, "REQUERIMIENTOS");
		                   return;
		                }
            	  }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite cancelar un traspaso ó requerimiento a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso ó requerimiento que se quiere cancelar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
               
            }
           
        }  
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
          // Revisa si tiene permisos
          if(request.getParameter("tipomov").equals("TRASPASOS"))
          {	
        	  if(!getSesion(request).getPermiso("ALM_TRASPASOS"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS","TALM||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }
          else // Requerimientos
          {
        	  if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS","RALM||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }
   
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if (valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("IMPRESION"))
              {
                StringBuffer bsmensaje = new StringBuffer(254);
                String SQLCab = "select * from ";
                String SQLDet = "select * from ";
                if(request.getParameter("tipomov").equals("TRASPASOS"))
                {
                	SQLCab += "view_invserv_traspasos_impcab where ID_Movimiento = ";
                	SQLDet += "view_invserv_traspasos_impdet where ID_Movimiento = ";
                }
                else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
                {
                	SQLCab += "view_invserv_requerimientos_impcab where ID_Movimiento = ";
                	SQLDet += "view_invserv_requerimientos_impdet where ID_Movimiento = ";
                }
                SQLCab += request.getParameter("ID");
                SQLDet += request.getParameter("ID") + " order by partida asc";
                
                idmensaje = Imprimir(SQLCab, SQLDet, request.getParameter("idformato"), bsmensaje, request, response);

                if (idmensaje != -1)
                {
                	getSesion(request).setID_Mensaje(idmensaje, bsmensaje.toString());
                	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                	return;
                }
              }
              else // significa que debe llamar a la ventana de formatos de impresion
              {
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
            	  {
            		  request.setAttribute("impresion", "CEFAlmTraspasosDlg");
            		  request.setAttribute("tipo_imp", "ALM_TRASPASOS");
            		  request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Traspasos());
            	  }
            	  else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
            	  {
            		  request.setAttribute("impresion", "CEFAlmTraspasosDlg");
            		  request.setAttribute("tipo_imp", "ALM_REQUERIMIENTOS");
            		  request.setAttribute("formato_default", null);
            	  }
            	  
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/impresion_dlg.jsp", request, response);
                  return;
              }
            }
            else
            {
               idmensaje = 1;
               mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite imprimir un traspaso ó requerimiento a la vez <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del traspaso ó requerimiento que se quiere imprimir<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
          }
        } 
        else if(request.getParameter("proceso").equals("RASTREAR_TRASPASO"))
        {
        	if(request.getParameter("tipomov").equals("TRASPASOS"))
      	  	{
        		if(!getSesion(request).getPermiso("ALM_TRASPASOS_CONSULTAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_TRASPASOS_CONSULTAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_TRASPASOS_CONSULTAR","TALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
      	  	}
        	else
        	{
        		if(!getSesion(request).getPermiso("ALM_REQUERIMIENTOS_CONSULTAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_REQUERIMIENTOS_CONSULTAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS_CONSULTAR","RALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
        	}

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
              	
            	  if(request.getParameter("tipomov").equals("TRASPASOS"))
            	  {
            		  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("ALM_TRASPASOS").generarTitulo(JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","CONSULTAR_TRASPASO",3)),
                  								"TALM",request.getParameter("ID"));
            		  String rastreo_imp = "true";
            		  request.setAttribute("rastreo_imp", rastreo_imp);
            		  // Ahora pone los atributos para el jsp
            		  request.setAttribute("rastreo", rastreo);
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_TRASPASOS_CONSULTAR","TALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_TRASPASOS").getEspecial() + "||","");
            		  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
            		  return;
            	  }
            	  else // requerimientos
            	  {
            		  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("ALM_REQUERIMIENTOS").generarTitulo(JUtil.Msj("CEF","ALM_REQUERIMIENTOS","VISTA","CONSULTAR_TRASPASO",3)),
								"RALM",request.getParameter("ID"));
            		  String rastreo_imp = "true";
            		  request.setAttribute("rastreo_imp", rastreo_imp);
            		  // Ahora pone los atributos para el jsp
            		  request.setAttribute("rastreo", rastreo);
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_REQUERIMIENTOS_CONSULTAR","RALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial() + "||","");
            		  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
            		  return;
            	  }
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
          idmensaje = 1;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);//"PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);//"ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
         return;
      }

    }

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("cantidad") != null && request.getParameter("idprod") != null &&
         !request.getParameter("cantidad").equals("") && !request.getParameter("idprod").equals("") )
      {
        return true;
      }
      else
      {
          idmensaje = 1; mensaje = JUtil.Msj("CEF","ALM_TRASPASOS","DLG","MSJ-PROCERR",3);//"PRECAUCION: Por lo menos se deben enviar los parámetros de cantidad y clave del producto <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
       	JAlmTraspasosSes rec;
       	if(request.getParameter("tipomov").equals("TRASPASOS"))
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       	else
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
     
        if(rec.getID_BodegaDest() == 0)
        {
 	        idmensaje = 3; mensaje.append(JUtil.Msj("CEF","ALM_TRASPASOS","DLG","MSJ-PROCERR",4));//"ERROR: La clave de la bodega de destino no se ha especificado ( No puede ser cero ) <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
       	
        }
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append(JUtil.Msj("GLB","GLB","DLG","CERO-PART",2));
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        /*
        if(request.getParameter("tipomov").equals("TRASPASOS"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
                
        	if(idmensaje != -1)
        	{
        		irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
        		return false;
        	}
        }
        if(request.getParameter("tipomov").equals("REQUERIMIENTOS") && request.getParameter("proceso").equals("TRASPASAR"))
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
                
        	if(idmensaje != -1)
        	{
        		irApag("/forsetiweb/almacen/alm_traspasos_dlg_generar.jsp", request, response);
        		return false;
        	}
        }
        */
        return true;
	
    }

    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
        
       	HttpSession ses = request.getSession(true);
       	JAlmTraspasosSes rec;
       	if(request.getParameter("tipomov").equals("TRASPASOS"))
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       	else
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
       	
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
        HttpSession ses = request.getSession(true);
        JAlmTraspasosSes rec; 
        
        if(request.getParameter("tipomov").equals("TRASPASOS"))
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       	else
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
       
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod"), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
        HttpSession ses = request.getSession(true);
        JAlmTraspasosSes rec;
        
        if(request.getParameter("tipomov").equals("TRASPASOS"))
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       	else
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
       
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JAlmTraspasosSes rec;
        
        if(request.getParameter("tipomov").equals("TRASPASOS"))
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       	else
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
       
        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       
    }

    public void AgregarDesde(HttpServletRequest request, HttpServletResponse response, String traspaso, JAlmacenesMovimSetIdsV2 set)
    	throws ServletException, IOException
    {
    	
	      HttpSession ses = request.getSession(true);
	      JAlmTraspasosSes rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
	
	      String tbl =	"CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET (\n";
	      tbl +=		"	ID_Prod varchar (20) NOT NULL ,\n";
	      tbl +=		"	Partida smallint NOT NULL ,\n";
	      tbl +=		"	Cantidad numeric(9, 3) NOT NULL \n";
	      tbl +=		"); \n";
	  	
	      for(int i = 0; i < rec.getPartidas().size(); i++)
	      {
	      	tbl += "\ninsert into _TMP_INVSERV_ALMACEN_BOD_MOV_DET\n";
	      	tbl += "values('" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "');";
	      }
	      
	      String str = "select * from sp_invserv_alm_movs_bod_agregar(" +
        	"'" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + rec.getID_Bodega() + "','" + rec.getID_BodegaDest() + "'," +
            "'" + p(rec.getConcepto()) + "','" + p(rec.getRef()) + "','" + p(traspaso) + "') as ( err integer, res varchar, clave integer );";
	      JRetFuncBas rfb = new JRetFuncBas();
	    	
	      doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET ", rfb);
	      
	      String mensaje = rfb.getRes();
	      if(rfb.getIdmensaje() == 0)
	      {
	    	  StringBuffer sb_mensaje = new StringBuffer(254);
	    	  generarCFDI(request, response, "TRASPASOS", Integer.parseInt(rfb.getClaveret()), null, set, (byte)0, sb_mensaje);
	    	  mensaje += "<br>" + sb_mensaje.toString();
	    	  getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
	      }
	      
	      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_REQUERIMIENTOS_TRASPASAR", "RALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial() + "||",mensaje);
	      irApag("/forsetiweb/almacen/alm_traspasos_dlg_generar.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response, String tipomov, JAlmacenesMovimSetIdsV2 set)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JAlmTraspasosSes rec;
        String proceso, proc, modulo;
        if(request.getParameter("tipomov").equals("TRASPASOS"))
        {
       		rec = (JAlmTraspasosSes)ses.getAttribute("alm_traspasos_dlg");
       		proceso = "ALM_TRASPASOS_AGREGAR";
       		proc = "TALM";
       		modulo = "ALM_TRASPASOS";
        }
        else
        {
        	rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");
        	proceso = "ALM_REQUERIMIENTOS_AGREGAR";
       		proc = "RALM";
       		modulo = "ALM_REQUERIMIENTOS";
        }
      
        String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET (\n";
    	tbl +=			"ID_Prod varchar(20) NOT NULL ,\n";
    	tbl +=			"Partida smallint NOT NULL ,\n";
    	tbl +=			"Cantidad numeric(9, 3) NOT NULL \n";
    	tbl +=		");\n";

    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
        	tbl += "\ninsert into _TMP_INVSERV_ALMACEN_BOD_MOV_DET\n";
        	tbl += "values('" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "');";
        }

        String str = "select * from  ";
        if(tipomov.equals("TRASPASOS"))
        {
        	str += "sp_invserv_alm_movs_bod_agregar(" +
        	"'" + JUtil.obtFechaSQL(rec.getFecha()) + "','" + rec.getID_Bodega() + "','" + rec.getID_BodegaDest() + "'," +
            "'" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "',null)";
        }
        else if(tipomov.equals("REQUERIMIENTOS"))
        {
        	str += "sp_invserv_alm_movs_req_agregar(" +
        	"'" + JUtil.obtFechaSQL(rec.getFecha()) + "','" + rec.getID_Bodega() + "','" + rec.getID_BodegaDest() + "'," +
            "'" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "')";
        }	
        str += " as ( err integer, res varchar, clave integer );";
        
        JRetFuncBas rfb = new JRetFuncBas();
    			
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET ", rfb);
        
        String mensaje = rfb.getRes();
		if(rfb.getIdmensaje() == 0 && tipomov.equals("TRASPASOS"))
        {
			StringBuffer sb_mensaje = new StringBuffer(254);
			generarCFDI(request, response, "TRASPASOS", Integer.parseInt(rfb.getClaveret()), null, set, (byte)0, sb_mensaje);
			mensaje += "<br>" + sb_mensaje.toString();
			getSesion(request).setID_Mensaje((short)rfb.getIdmensaje(), mensaje);
		}
          
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, proc + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(modulo).getEspecial() + "||",mensaje);
        irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
        
    }
 
    public void Cambiar(HttpServletRequest request, HttpServletResponse response, String tipomov)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
    	JAlmTraspasosSes rec = (JAlmTraspasosSes)ses.getAttribute("alm_requerimientos_dlg");

    	String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET (\n";
    	tbl +=		 "	ID_Prod varchar(20) NOT NULL ,\n";
    	tbl +=		 "	Partida smallint NOT NULL ,\n";
    	tbl +=		 "	Cantidad numeric(9, 3) NOT NULL \n";
    	tbl +=		 "); \n";
  	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
    	{
    		tbl += "\ninsert into _TMP_INVSERV_ALMACEN_BOD_MOV_DET\n";
    		tbl += "values('" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "');";
    	}

    	String str = "select * from sp_invserv_alm_movs_req_cambiar('" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + rec.getID_Bodega() + "','" + rec.getID_BodegaDest() + "'" +
    		",'" + rec.getNumero() + "','" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "') as ( err integer, res varchar, clave integer );";
      
    	JRetFuncBas rfb = new JRetFuncBas();
		
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_BOD_MOV_DET ", rfb);
   
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_REQUERIMIENTOS_CAMBIAR", "RALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/almacen/alm_traspasos_dlg.jsp", request, response);
      
    }
    
    public void CancelarTraspaso(HttpServletRequest request, HttpServletResponse response, String tipomov)
    	throws ServletException, IOException
    {
    	 String proceso, proc, modulo;
         if(request.getParameter("tipomov").equals("TRASPASOS"))
         {
        	 proceso = "ALM_TRASPASOS_CANCELAR";
        	 proc = "TALM";
        	 modulo = "ALM_TRASPASOS";
         }
         else
         {
         	proceso = "ALM_REQUERIMIENTOS_CANCELAR";
         	proc = "RALM";
         	modulo = "ALM_REQUERIMIENTOS";
         }
       
         String str = "select * from ";
         if(tipomov.equals("TRASPASOS"))
    		str += "sp_invserv_alm_movs_bod_cancelar('" + p(request.getParameter("ID"));
         else if(tipomov.equals("REQUERIMIENTOS"))
    		str += "sp_invserv_alm_movs_req_cancelar('" + p(request.getParameter("ID"));
         str +=  "') as ( err integer, res varchar, clave integer );";
      
         JRetFuncBas rfb = new JRetFuncBas();
		
         doCallStoredProcedure(request, response, str, rfb);
   
         RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, proc + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(modulo).getEspecial() + "||",rfb.getRes());
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      
	     
	  }
  
    
}
