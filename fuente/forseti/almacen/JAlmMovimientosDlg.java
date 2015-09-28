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

import forseti.JForsetiApl;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmInvservCostosConceptosSet;
import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JAlmacenesMovimSetV2;
import forseti.sets.JAlmacenesMovimSetDetallesV2;
import forseti.sets.JAlmacenesMovimPlantSet;
import forseti.sets.JPublicInvServConceptosCatSetV2;

@SuppressWarnings("serial")
public class JAlmMovimientosDlg extends JForsetiApl
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

      String alm_movim_dlg = "";
      request.setAttribute("alm_movim_dlg",alm_movim_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	//revisa por las entidades
        JAlmacenesMovimSetIdsV2 setids;
        if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
        	setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_MOVIM").getEspecial(),"P");
        else //(request.getParameter("tipomov").equals("PLANTILLAS"))
          	setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_MOVPLANT").getEspecial(),"P");
        setids.Open();  
    	
        if(setids.getNumRows() < 1)
        {
        	
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", (request.getParameter("tipomov").equals("MOVIMIENTOS") ? "ALM_MOVIM" : "ALM_MOVPLANT"));
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),(request.getParameter("tipomov").equals("MOVIMIENTOS") ? "ALM_MOVIM" : "ALM_MOVPLANT"),(request.getParameter("tipomov").equals("MOVIMIENTOS") ? "MALM" : "PALM") + "||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        } 
     
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
        {   
        	if(!request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO") && request.getParameter("ID") != null)
            {
        		JAlmacenesMovimSetV2 set = new JAlmacenesMovimSetV2(request);
        		set.m_Where = "ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' and ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	set.Open();
            	if(set.getNumRows() < 1)
            	{
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM");
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_MOVIM","MALM|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
	  	  	
        }
        else
        {    
        	if(!request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO") && request.getParameter("ID") != null)
            {
        		JAlmacenesMovimPlantSet set = new JAlmacenesMovimPlantSet(request);
        		set.m_Where = "ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' and ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
            	set.Open();
            	if(set.getNumRows() < 1)
            	{
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT");
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_MOVPLANT","PALM|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
        		  	  	
        }
        
  	  	if(request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
          {	
        	if(!getSesion(request).getPermiso("ALM_MOVIM_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM_AGREGAR","MALM||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
          }
          else // Plantillas
          {
        	  if(!getSesion(request).getPermiso("ALM_MOVPLANT_AGREGAR"))
              {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT_AGREGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_AGREGAR","PALM||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
          }
          

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
	            HttpSession ses = request.getSession(true);
	            JAlmMovimientosSes rec;
	            
	            if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
	            {	
	            	rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
	            	if(rec == null)
	            	{
	            		rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            		ses.setAttribute("alm_movim_dlg", rec);
	            	}
	            	else
	                   	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	                	
	            }
	            else //if(request.getParameter("tipomov").equals("PLANTILLAS"))
	            {	
	            	rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
	            	if(rec == null)
	            	{
	            		rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            		ses.setAttribute("alm_movplant_dlg", rec);
	            	}
	            	else
	                	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            }
	            
	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
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
       					  if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       						  Agregar(request, response, "MOVIMIENTOS");
       					  else if(request.getParameter("tipomov").equals("PLANTILLAS"))
       						  Agregar(request, response, "PLANTILLAS");
       					  
       					  return;
       				  }
      				  	
      			  }
	       		 
       			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
       			  return;
	   		  }
	       	  else if(request.getParameter("subproceso").equals("AGR_CONCEPTO"))
	       	  {
	       		  AgregarCabecero(request,response);
	       	      irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
	       	      return;
	       	  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    AgregarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    EditarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
 	   			  return;
	   		  }	   	  
	      }
	
        }
        //////////////////
        else if(request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO"))
        {
        	// Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("PLANTILLAS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVPLANT_CAMBIAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT_CAMBIAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_CAMBIAR","PALM||||",mensaje);
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
                	  
      
                	  JAlmacenesMovimPlantSet SetMod = new JAlmacenesMovimPlantSet(request);
                	  SetMod.m_Where = "ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
                	  SetMod.Open();
      	            	
                	  if(!SetMod.getAbsRow(0).getStatus().equals("G"))
                	  {
                		  idmensaje = 1;
                		  mensaje += JUtil.Msj("CEF", "ALM_MOVPLANT", "DLG", "MSJ-PROCERR",1); //"PRECAUCION: Esta plantilla tiene status diferente de Guardada, no se puede cambiar <br>";
                		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                		  return;
                	  } 
		            	  	  
                	  HttpSession ses = request.getSession(true);
                	  JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
                	  if(rec == null)
                	  {
                		  rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
                		  ses.setAttribute("alm_movplant_dlg", rec);
                	  }
                	  else
                		  rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
                    
                	  JAlmacenesMovimPlantSet SetCab = new JAlmacenesMovimPlantSet(request);
                	  JAlmacenesMovimSetDetallesV2 SetDet = new JAlmacenesMovimSetDetallesV2(request,"PLANTILLAS");
                	  SetCab.m_Where = "ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
                	  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                	  SetDet.m_OrderBy = "ID_Costo ASC";
                	  SetCab.Open();
                	  SetDet.Open();
                	  JPublicInvServConceptosCatSetV2 SetCon = new JPublicInvServConceptosCatSetV2(request);
                	  SetCon.m_Where = "ID_Concepto = '" + SetCab.getAbsRow(0).getID_Concepto() + "'";
                	  SetCon.Open();
                    	
                	  rec.setID_Clave(SetCab.getAbsRow(0).getID_Concepto()); 
                	  rec.setNumero(SetCab.getAbsRow(0).getNum());
                	  rec.setFecha(SetCab.getAbsRow(0).getFecha());
                	  rec.setRef(SetCab.getAbsRow(0).getReferencia());
                	  rec.setClave_Descripcion(SetCab.getAbsRow(0).getDescripcion());
                	  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
                	  rec.setTipoMov((SetCon.getAbsRow(0).getTipo().equals("ENT") ? (byte)1 : (byte)2));
                	  rec.setRecalcularCosto(SetCon.getAbsRow(0).getRecalcularCosto());
                    	
                	  for(int i = 0; i< SetDet.getNumRows(); i++)
                	  {
                		  rec.agregaPartida( (SetCon.getAbsRow(0).getTipo().equals("ENT") ? SetDet.getAbsRow(i).getEntrada() : SetDet.getAbsRow(i).getSalida()), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getUC());
                	  }
                	  
                	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
                	  return;
                  }
                  else
                  {
                	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                      getSesion(request).setID_Mensaje(idmensaje, mensaje);
                      irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                      return;
                  }
              }
              else
              {
            	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
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
     					  Cambiar(request, response, "PLANTILLAS");
     				  return;
     			  }
	       		  
          		  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
          		  return;
         			  
  	   		  }
          	  else if(request.getParameter("subproceso").equals("AGR_CONCEPTO"))
	       	  {
	       		  AgregarCabecero(request,response);
	       	      irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
	       	      return;
	       	  }
          	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				if(VerificarParametrosPartida(request, response))
  	   			    	AgregarPartida(request, response);
     			  }
	       		
     			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
     			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				if(VerificarParametrosPartida(request, response))
  	   			    	EditarPartida(request, response);
     			  }
	       		  
     			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
     			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  {
     			  if(AgregarCabecero(request,response) == -1)
     			  {
     				BorrarPartida(request, response);
     			  }
	       		  
     			  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);  
	   			  return;
	   		  }	  
  	   	  }
           
        } 
        //////////////////
        else if(request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO"))
        {
            // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVIM"))
                {
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM","MALM||||",mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            }
            else // Plantillas
            {
            	if(!getSesion(request).getPermiso("ALM_MOVPLANT"))
                {
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT","PALM||||",mensaje);
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
            	  JAlmMovimientosSes rec;
                
            	  if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            	  {
            		  rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
            		  if(rec == null)
            		  {
            			  rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
            			  ses.setAttribute("alm_movim_dlg", rec);
            		  }
            		  else
            			  rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
                   
            	  }
            	  else //if(request.getParameter("tipomov").equals("PLANTILLAS"))
            	  {
            		  rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
            		  if(rec == null)
            		  {
            			  rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
            			  ses.setAttribute("alm_movplant_dlg", rec);
            		  }
            		  else
            			  rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getPlantilla(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
                
            	  }
                 	
            	  // Llena la consulta
            	  if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            	  {
            		  JAlmacenesMovimSetV2 SetCab = new JAlmacenesMovimSetV2(request);
            		  JAlmacenesMovimSetDetallesV2 SetDet = new JAlmacenesMovimSetDetallesV2(request);
            		  SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetDet.m_OrderBy = "ID_Costo ASC";
            		  SetCab.Open();
            		  SetDet.Open();
            		  JAdmInvservCostosConceptosSet SetCon = new JAdmInvservCostosConceptosSet(request);
            		  SetCon.m_Where = "ID_Concepto = '" + SetCab.getAbsRow(0).getID_Concepto() + "'";
                	  SetCon.Open();
                	  
            		  rec.setID_Clave(SetCab.getAbsRow(0).getID_Concepto()); 
            		  rec.setNumero(SetCab.getAbsRow(0).getNum());
            		  rec.setFecha(SetCab.getAbsRow(0).getFecha());
            		  rec.setRef(SetCab.getAbsRow(0).getReferencia());
            		  rec.setClave_Descripcion(SetCab.getAbsRow(0).getDescripcion());
            		  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
            		  rec.setTipoMov((SetCon.getAbsRow(0).getTipo().equals("ENT") ? (byte)1 : (byte)2));
                	  rec.setRecalcularCosto(SetCon.getAbsRow(0).getRecalcularCosto());
                    
            		  for(int i = 0; i< SetDet.getNumRows(); i++)
            		  {
            			  rec.agregaPartida( (SetDet.getAbsRow(i).getEntrada() - SetDet.getAbsRow(i).getSalida() ), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getUC());
            		  }
            		  
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_MOVIM","MALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_MOVIM").getEspecial() + "||","");
                      
            	  }
            	  else //if(request.getParameter("tipomov").equals("PLANTILLAS"))
            	  {
            		  JAlmacenesMovimPlantSet SetCab = new JAlmacenesMovimPlantSet(request);
                	  JAlmacenesMovimSetDetallesV2 SetDet = new JAlmacenesMovimSetDetallesV2(request,"PLANTILLAS");
                	  SetCab.m_Where = "ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
                	  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
                	  SetDet.m_OrderBy = "ID_Costo ASC";
                	  SetCab.Open();
                	  SetDet.Open();
                	  JPublicInvServConceptosCatSetV2 SetCon = new JPublicInvServConceptosCatSetV2(request);
                	  SetCon.m_Where = "ID_Concepto = '" + SetCab.getAbsRow(0).getID_Concepto() + "'";
                	  SetCon.Open();
                    	
                	  rec.setID_Clave(SetCab.getAbsRow(0).getID_Concepto()); 
                	  rec.setNumero(SetCab.getAbsRow(0).getNum());
                	  rec.setFecha(SetCab.getAbsRow(0).getFecha());
                	  rec.setRef(SetCab.getAbsRow(0).getReferencia());
                	  rec.setClave_Descripcion(SetCab.getAbsRow(0).getDescripcion());
                	  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
                	  rec.setTipoMov((SetCon.getAbsRow(0).getTipo().equals("ENT") ? (byte)1 : (byte)2));
                	  rec.setRecalcularCosto(SetCon.getAbsRow(0).getRecalcularCosto());
                    	
                	  for(int i = 0; i< SetDet.getNumRows(); i++)
                	  {
                		  rec.agregaPartida( (SetCon.getAbsRow(0).getTipo().equals("ENT") ? SetDet.getAbsRow(i).getEntrada() : SetDet.getAbsRow(i).getSalida()), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getUC());
                	  }
                	              		  
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_MOVPLANT","PALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_MOVPLANT").getEspecial() + "||","");
                      
            	  }
            	 
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
            	  return;
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
           
        }
        else if(request.getParameter("proceso").equals("CANCELAR_MOVIMIENTO"))
        {
            // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVIM_CANCELAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM_CANCELAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM_CANCELAR","MALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
            else // Plantillas
            {
            	if(!getSesion(request).getPermiso("ALM_MOVPLANT_CANCELAR"))
                {
            		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT_CANCELAR");
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_CANCELAR","MALM||||",mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
                }
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            	  {
            		  JAlmacenesMovimSetV2 SetCab = new JAlmacenesMovimSetV2(request);
            		  SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            		  SetCab.Open();
            
            		  if(SetCab.getAbsRow(0).getStatus().equals("C"))
            		  {
            			  idmensaje = 1;
            			  mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR",1); //"PRECAUCION: Este movimiento ya esta cancelado <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		  else if(!SetCab.getAbsRow(0).getRef().equals(""))
            		  {
            			  idmensaje = 1;
            			  mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR",2); //"PRECAUCION: Este movimiento es externo a este módulo y no se puede cancelar. Utiliza el módulo que lo creó para cancelarlo <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		  else
            		  {
            			  CancelarMovimiento(request, response, "MOVIMIENTOS");
            			  return;
            		  }
            	  }
            	  else if(request.getParameter("tipomov").equals("PLANTILLAS"))
            	  {
            		  JAlmacenesMovimPlantSet SetCab = new JAlmacenesMovimPlantSet(request);
            		  SetCab.m_Where = "ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
            		  SetCab.Open();
            
            		  if(SetCab.getAbsRow(0).getStatus().equals("C"))
            		  {
            			  idmensaje = 1;
            			  mensaje += JUtil.Msj("CEF", "ALM_MOVPLANT", "DLG", "MSJ-PROCERR",2); //"PRECAUCION: Esta plantilla ya esta cancelada <br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  } 
            		  else if(setids.getAbsRow(0).getAuditarAlm() && !SetCab.getAbsRow(0).getStatus().equals("R"))
            		  {
            			  idmensaje = 1;
            			  mensaje += JUtil.Msj("CEF", "ALM_MOVPLANT", "DLG", "MSJ-PROCERR",3); //"PRECAUCION: Esta plantilla necesita estar revertida desde el módulo de movimientos al almacén para poder cancelarla<br>";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			  return;
            		  }
            		  else
            		  {
            			  CancelarMovimiento(request, response, "PLANTILLAS");
            			  return;
            		  }
            	  }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
           
        }  
        else if(request.getParameter("proceso").equals("AUDITAR_MOVIMIENTO"))
        {
        	// Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVIM_AUDITAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM_AUDITAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM_AUDITAR","MALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
        	else
        		return;
           	
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	          		
            	JAlmacenesMovimSetV2 SetCab = new JAlmacenesMovimSetV2(request);
                SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	SetCab.Open();
            
            	if(	!setids.getAbsRow(0).getAuditarAlm() ||
						SetCab.getAbsRow(0).getRef().equals("") )
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR",3);//"PRECAUCION: Este movimiento es directo, o la bodega no es aduditable <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
 	
            	}
            	else if( SetCab.getAbsRow(0).getStatus().equals("C") || 
            			SetCab.getAbsRow(0).getStatus().equals("R") )
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR",4);//"PRECAUCION: Este movimiento esta cancelado o revertido. No se puede Auditar <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
             		
            	}
            	else if( SetCab.getAbsRow(0).getStatus().equals("U"))
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR",5);//"PRECAUCION: Este movimiento ya esta auditado. No se puede volver a Auditar <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
             		
            	}
                else
                {
                   AuditarMovimiento(request, response);
                   return;
                }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
           
        }        
        else if(request.getParameter("proceso").equals("REVERTIR_MOVIMIENTO"))
        {
        	// Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVIM_AUDITAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM_AUDITAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM_AUDITAR","MALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
        	else
        		return;
    
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	
            	JAlmacenesMovimSetV2 SetCab = new JAlmacenesMovimSetV2(request);
                SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	SetCab.Open();
            
            	if(	!setids.getAbsRow(0).getAuditarAlm() ||
						SetCab.getAbsRow(0).getRef().equals("") )
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",1);//"PRECAUCION: Este movimiento es directo, o la bodega no es reversible <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
 	
            	}
            	else if( SetCab.getAbsRow(0).getStatus().equals("C") || 
            			SetCab.getAbsRow(0).getStatus().equals("R") )
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",2);//"PRECAUCION: Este movimiento ya esta cancelado o revertido. No se puede volver a revertir <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
             		
            	}
            	else if( !SetCab.getAbsRow(0).getStatus().equals("U"))
            	{
                    idmensaje = 1;
                    mensaje += JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",3);//"PRECAUCION: Este movimiento no esta auditado aún. Para poder revertirlo, primero debes auditarlo <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
             		
            	}
                else
                {
                   AuditarMovimiento(request, response);
                   return;
                }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
           
        }  
        else if(request.getParameter("proceso").equals("GENERAR_MOVIMIENTO"))
        {
            // Revisa si tiene permisos
        	if(request.getParameter("tipomov").equals("PLANTILLAS"))
            {	
        		if(!getSesion(request).getPermiso("ALM_MOVPLANT_GENERAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT_GENERAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_GENERAR","PALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
            }
        	else
        		return;
           	
            if(request.getParameter("ID") != null)
            {
            	String[] valoresParam = request.getParameterValues("ID");
            	if(valoresParam.length == 1)
            	{
            		JAlmacenesMovimPlantSet SetMod = new JAlmacenesMovimPlantSet(request);
            		SetMod.m_Where = "ID_MovimPlant = '" + p(request.getParameter("ID")) + "'";
            		SetMod.Open();
      	            	
            		if(SetMod.getAbsRow(0).getStatus().equals("C"))
            		{
            			idmensaje = 1;
            			mensaje += JUtil.Msj("CEF", "ALM_MOVPLANT", "DLG", "MSJ-PROCERR",4);//PRECAUCION: Esta plantilla ya esta cancelada, no se puede aplicar <br>";
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			return;
            		} 
		
            		if(SetMod.getAbsRow(0).getStatus().equals("N") || SetMod.getAbsRow(0).getStatus().equals("E") )
            		{
            			idmensaje = 1;
            			mensaje += JUtil.Msj("CEF", "ALM_MOVPLANT", "DLG", "MSJ-PROCERR",5); //"PRECAUCION: Esta plantilla ya tiene un movimientoo asociado, no se puede volver a aplicar <br>";
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            			return;
            		} 

                      
                	Generar(request, response);                    
                    return;
                	
                  }
                  else
                  {
                	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite proceso de un registro a la vez
                      getSesion(request).setID_Mensaje(idmensaje, mensaje);
                      irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                      return;
                  }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del registro que se quiere quiere aplicar este proceso
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }              
            		
           
        }
        else if(request.getParameter("proceso").equals("RASTREAR_MOVIMIENTO"))
        {
        	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
      	  	{
        		if(!getSesion(request).getPermiso("ALM_MOVIM_CONSULTAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM_CONSULTAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM_CONSULTAR","MALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
      	  	}
        	else
        	{
        		if(!getSesion(request).getPermiso("ALM_MOVPLANT_CONSULTAR"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT_CONSULTAR");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_CONSULTAR","PALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
        	}

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
              	
            	  if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
            	  {
            		  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("ALM_MOVIM").generarTitulo(JUtil.Msj("CEF","ALM_MOVIM","VISTA","CONSULTAR_MOVIMIENTO",3)),
                  								"MALM",request.getParameter("ID"));
            		  String rastreo_imp = "true";
            		  request.setAttribute("rastreo_imp", rastreo_imp);
            		  // Ahora pone los atributos para el jsp
            		  request.setAttribute("rastreo", rastreo);
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_MOVIM_CONSULTAR","MALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_MOVIM").getEspecial() + "||","");
            		  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
            		  return;
            	  }
            	  else // Plantillas
            	  {
            		  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("ALM_MOVPLANT").generarTitulo(JUtil.Msj("CEF","ALM_MOVPLANT","VISTA","CONSULTAR_MOVIMIENTO",3)),
								"PALM",request.getParameter("ID"));
            		  String rastreo_imp = "true";
            		  request.setAttribute("rastreo_imp", rastreo_imp);
            		  // Ahora pone los atributos para el jsp
            		  request.setAttribute("rastreo", rastreo);
            		  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_MOVPLANT_CONSULTAR","PALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_MOVPLANT").getEspecial() + "||","");
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
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
          if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
          {
        		if(!getSesion(request).getPermiso("ALM_MOVIM"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVIM");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVIM","MALM||||",mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		} 
          }
          else
          {
        		if(!getSesion(request).getPermiso("ALM_MOVPLANT"))
        		{
        			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_MOVPLANT");
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_MOVPLANT","PALM||||",mensaje);
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
                String SQLCab, SQLDet;
                if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
                {
                	SQLCab = "select * FROM view_invserv_almacen_movim_impcab where ID_Movimiento = " + request.getParameter("ID");
                	SQLDet = "select * FROM view_invserv_almacen_movim_impdet where ID_Movimiento = " + request.getParameter("ID") + " order by ID_Costo asc";
                }
                else
                {
                	SQLCab = "select * FROM view_invserv_plantillas_impcab where ID_Plantilla = " + request.getParameter("ID");
                	SQLDet = "select * FROM view_invserv_plantillas_impdet where ID_Plantilla = " + request.getParameter("ID") + " order by Partida asc";
                }	
                
                idmensaje = Imprimir(SQLCab, SQLDet, 
                                     request.getParameter("idformato"), bsmensaje,
                                     request, response);

                if (idmensaje != -1)
                {
                  getSesion(request).setID_Mensaje(idmensaje, bsmensaje.toString());
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
                }
              }
              else // significa que debe llamar a la ventana de formatos de impresion
              {
            	 if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
                 {
            	 	 request.setAttribute("impresion", "CEFAlmMovimientosDlg");
            	 	 request.setAttribute("tipo_imp", "ALM_MOVIM");
            	 	 request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Movimientos());
                 }
            	 else
                 {
            	 	 request.setAttribute("impresion", "CEFAlmMovimientosDlg");
            	 	 request.setAttribute("tipo_imp", "ALM_MOVPLANT");
            	 	 request.setAttribute("formato_default", null);
                 }
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 irApag("/forsetiweb/impresion_dlg.jsp", request, response);
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
    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //PRECAUCION: Solo se permite proceso de un registro a la vez
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
      if(request.getParameter("cantidad") != null && request.getParameter("idprod") != null && request.getParameter("costo") != null &&
         !request.getParameter("cantidad").equals("") && !request.getParameter("idprod").equals("") )
      {
    	  return true;
      }
      else
      {
          idmensaje = 1; mensaje = JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",4); //"PRECAUCION: Por lo menos se deben enviar los parámetros de cantidad y clave del producto <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
    	JAlmMovimientosSes rec;
       	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       	else
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
       
        if(rec.getID_Clave() == 0)
        {
 	        idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",5));//"ERROR: La clave del movimiento no se ha especificado ( No puede ser cero ) <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
       	
        }
        
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append(JUtil.Msj("GLB","GLB","DLG","CERO-PART",2));//"PRECAUCION: El movimiento no contiene partidas <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
            
        if(rec.getTipoMov() == 2) // 2 es SALIDA
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
            getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

            if(idmensaje != -1)
            	return false;
            
        }
             
        return true;
	
    }

    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JAlmMovimientosSes rec;
       	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       	else
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
       	
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
    	JAlmMovimientosSes rec;
       	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       	else
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
       	
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod"), request.getParameter("costo"), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
 
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec;
        if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       	else
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
      
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), request.getParameter("costo"), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
  
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec;
        if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       	else
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
      
        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
 
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response, String tipomov)
    	throws ServletException, IOException
    {
    	 HttpSession ses = request.getSession(true);
         JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
                
         String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (\n";
         tbl +=	"ID_Prod varchar(20) NOT NULL ,\n";
         tbl +=	"Partida smallint NOT NULL ,\n";
         tbl +=	"Entrada numeric(9, 3) NOT NULL ,\n";
         tbl +=	"Salida numeric(9, 3) NOT NULL ,\n";
         tbl += 	"Costo numeric(19,4) NULL \n";
         tbl +=	"); \n";
        
         for(int i = 0; i < rec.getPartidas().size(); i++)
         {
        	 tbl += "\ninsert into _TMP_INVSERV_ALMACEN_MOVIM_DET\n";
        	 tbl += "values('" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + (rec.getTipoMov() == 1 ? rec.getPartida(i).getCantidad() : "0") + "','" + (rec.getTipoMov() == 1 ? "0" : rec.getPartida(i).getCantidad() ) + "','" + rec.getPartida(i).getCosto() + "'); ";
         }
    
         /* @ID_EntidadCompra smallint, @Numero int, @ID_Proveedor int, @Fecha smalldatetime, @Referencia varchar(20), 
     		/status/ @Moneda tinyint, @TC money, /FechaRecep/ @Condicion tinyint, @Obs varchar(255), @Importe money, @Descuento money, @SubTotal money, 
     	 * @IVA money, @Total money, @ID_FormaPago smallint, @ID_BanCaj smallint, @RefPago varchar(25),
 		@ID_Bodega smallint */
         String str = "select * from sp_invserv_alm_movs_plant_cambiar('" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + rec.getID_Bodega() + "','" + rec.getNumero() + "','G','" +
      		rec.getID_Clave() + "','" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "') as ( err integer, res varchar, clave integer );";
         
         JRetFuncBas rfb = new JRetFuncBas();
   		
         doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET ", rfb);
       
         RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_MOVPLANT_CAMBIAR", "PALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_MOVPLANT").getEspecial() + "||",rfb.getRes());
         irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
  
  	}    
    
    public void Generar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
    	String str = "select * from sp_invserv_alm_movs_plant_aplicar( '" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
         
        JRetFuncBas rfb = new JRetFuncBas();
   		
        doCallStoredProcedure(request, response, str, rfb);
       
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_MOVPLANT_GENERAR", "PALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_MOVPLANT").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  
	}    
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response, String tipomov)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec;
        String proceso, proc, modulo;
        if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
        {
       		rec = (JAlmMovimientosSes)ses.getAttribute("alm_movim_dlg");
       		proceso = "ALM_MOVIM_AGREGAR";
       		proc = "MALM";
       		modulo = "ALM_MOVIM";
        }
        else
        {
        	rec = (JAlmMovimientosSes)ses.getAttribute("alm_movplant_dlg");
        	proceso = "ALM_MOVPLANT_AGREGAR";
       		proc = "PALM";
       		modulo = "ALM_MOVPLANT";
        }
      
        String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (\n";
        if(tipomov.equals("MOVIMIENTOS"))
        {
           	tbl += "ID_Bodega smallint NOT NULL ,\n";
        	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
        	tbl += "Partida smallint NOT NULL ,\n";
        	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
        	tbl += "Costo numeric(19,4) NULL \n";
        	tbl += "); \n";
        }
        else if(tipomov.equals("PLANTILLAS"))
        {
        	tbl +=	"ID_Prod varchar(20) NOT NULL ,\n";
        	tbl +=	"Partida smallint NOT NULL ,\n";
        	tbl +=	"Entrada numeric(9, 3) NOT NULL ,\n";
        	tbl +=	"Salida numeric(9, 3) NOT NULL ,\n";
        	tbl += 	"Costo numeric(19,4) NULL \n";
        	tbl +=	"); \n";
        }
        
        if(tipomov.equals("MOVIMIENTOS"))
        {
        	for(int i = 0; i < rec.getPartidas().size(); i++)
        	{
        		tbl += "\ninsert into _TMP_INVSERV_ALMACEN_MOVIM_DET\n";
        		tbl += "values('" + rec.getID_Bodega() + "','" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + rec.getPartida(i).getCosto() + "'); ";
        	}
        }
        else if(tipomov.equals("PLANTILLAS"))
        {
        	for(int i = 0; i < rec.getPartidas().size(); i++)
        	{
        		tbl += "\ninsert into _TMP_INVSERV_ALMACEN_MOVIM_DET\n";
        		tbl += "values('" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + (rec.getTipoMov() == 1 ? rec.getPartida(i).getCantidad() : "0") + "','" + (rec.getTipoMov() == 1 ? "0" : rec.getPartida(i).getCantidad() ) + "','" + rec.getPartida(i).getCosto() + "'); ";
        	}
        }
    	/* @ID_EntidadCompra smallint, @Numero int, @ID_Proveedor int, @Fecha smalldatetime, @Referencia varchar(20), 
    	/status/ @Moneda tinyint, @TC money, /FechaRecep/ @Condicion tinyint, @Obs varchar(255), @Importe money, @Descuento money, @SubTotal money, 
    	 * @IVA money, @Total money, @ID_FormaPago smallint, @ID_BanCaj smallint, @RefPago varchar(25),
		@ID_Bodega smallint */
        String str = "select * from  ";
        if(tipomov.equals("MOVIMIENTOS"))
        {
        	str += "sp_invserv_alm_movs_agregar('" + JUtil.obtFechaSQL(rec.getFecha()) + "','" + rec.getID_Bodega() + "','U','" +
        	rec.getID_Clave() + "','" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "','" + rec.getTipoMov() + "','', null, null)";
        }
        else if(tipomov.equals("PLANTILLAS"))
        {
        	str += "sp_invserv_alm_movs_plant_agregar('" + JUtil.obtFechaSQL(rec.getFecha()) + "','" + rec.getID_Bodega() + "','G','" +
        	rec.getID_Clave() + "','" + p(rec.getConcepto()) + "','" + p(rec.getRef())+ "')";
        }
        str += " as ( err integer, res varchar, clave integer );";
        
        JRetFuncBas rfb = new JRetFuncBas();
    	
		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET ", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, proc + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(modulo).getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/almacen/alm_movimientos_dlg.jsp", request, response);
        
    }
    
    public void AuditarMovimiento(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	
    	String str = "select * from sp_invserv_alm_movs_auditar( '" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
    	//doDebugSQL(request, response, str);
    	JRetFuncBas rfb = new JRetFuncBas();
   		
        doCallStoredProcedure(request, response, str, rfb);
       
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_MOVIM_AUDITAR", "MALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_MOVIM").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	}
    
    public void CancelarMovimiento(HttpServletRequest request, HttpServletResponse response, String tipomov)
    	throws ServletException, IOException
    {
    	String proceso, proc, modulo;
   	 	if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
   	 	{
   	 		proceso = "ALM_MOVIM_CANCELAR";
   	 		proc = "MALM";
   	 		modulo = "ALM_MOVIM";
   	 	}
   	 	else
   	 	{
   	 		proceso = "ALM_MOVPLANT_CANCELAR";
   	 		proc = "PALM";
   	 		modulo = "ALM_MOVPLANT";
   	 	}
   
   	 	String str = "select * from ";
   	 	if(tipomov.equals("MOVIMIENTOS"))
   	 		str += "sp_invserv_alm_movs_cancelar('" + p(request.getParameter("ID"));
   	 	else
    	 	str += "sp_invserv_alm_movs_plant_cancelar('" + p(request.getParameter("ID"));
     
   	 	str +=  "') as ( err integer, res varchar, clave integer );";
  
   	 	JRetFuncBas rfb = new JRetFuncBas();
	
   	 	doCallStoredProcedure(request, response, str, rfb);

   	 	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, proc + "|" + rfb.getClaveret() + "|" + getSesion(request).getSesion(modulo).getEspecial() + "||",rfb.getRes());
   	 	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	}
}
