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
package forseti.produccion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import forseti.JForsetiApl;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JProdProdSetV2;
import forseti.sets.JProdProdSetDetV2;
import forseti.sets.JProdProdSetProcV2;
import forseti.sets.JProdProdSetDetprodV2;
import forseti.sets.JProdEntidadesSetIdsV2;

@SuppressWarnings("serial")
public class JProdProduccionDlg extends JForsetiApl
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

      String prod_produccion_dlg = "";
      request.setAttribute("prod_produccion_dlg",prod_produccion_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();
  
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
      	// revisa por las entidades
        JProdEntidadesSetIdsV2 setids = new JProdEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion("PROD_PRODUCCION").getEspecial());
        setids.Open();
    
      	if(setids.getNumRows() < 1)
        {
      		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION","PPRD||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }
  
      	// Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_PRODUCCION") && request.getParameter("ID") != null)
        {
        	JProdProdSetV2 set = new JProdProdSetV2(request);
        	set.m_Where = "ID_Entidad = '" + setids.getAbsRow(0).getID_Entidad() + "' and ID_Reporte = '" + p(request.getParameter("ID")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"PROD_PRODUCCION","PPRD|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Entidad() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_PRODUCCION"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("PROD_PRODUCCION_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION_AGREGAR","PPRD||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            HttpSession ses = request.getSession(true);
            JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
            if(rec == null)
            {
              rec = new JProdProduccionSes(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);
              ses.setAttribute("prod_produccion_dlg", rec);
            }
            else
              rec.resetear(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);
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
      				  	  Agregar(request, response);
      				  	  return;
       				  }
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);  
       			  return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_FORM"))
	       	  {
	       		  AgregarCabecero(request,response);
	       	      irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);
	       	      return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametrosPartida(request, response))
       					  AgregarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  BorrarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);  
 	   			  return;
	   		  }
	   	  }
	
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_PRODUCCION"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("PROD_PRODUCCION"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION","PPRD||||",mensaje);
                irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {

                HttpSession ses = request.getSession(true);
                JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
                if(rec == null)
                {
                  rec = new JProdProduccionSes(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);
                  ses.setAttribute("prod_produccion_dlg", rec);
                }
                else
                  rec.resetear(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);

                // aqui debe llenar el reporte
            	//////////////////////////////////////////////////////////////
            	JProdProdSetV2 SetMod = new JProdProdSetV2(request);
            	SetMod.m_Where = "ID_Reporte = '" + p(request.getParameter("ID")) + "'";
            	SetMod.Open();
            	JProdProdSetDetV2 SetDet = new JProdProdSetDetV2(request);
            	SetDet.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "'";
            	SetDet.m_OrderBy = "Partida ASC";
            	SetDet.Open();
            	 	 
            	rec.setReporteNum(SetMod.getAbsRow(0).getNumero());
            	rec.setFecha(SetMod.getAbsRow(0).getFecha());
            	rec.setConcepto(SetMod.getAbsRow(0).getConcepto());
            	rec.setID_BodegaMP((byte)SetMod.getAbsRow(0).getID_BodegaMP());
            	rec.setID_BodegaPT((byte)SetMod.getAbsRow(0).getID_BodegaPT());
            	rec.setBodegaMP(SetMod.getAbsRow(0).getBodega_MP());
            	rec.setBodegaPT(SetMod.getAbsRow(0).getBodega_PT());
            	rec.setObs(SetMod.getAbsRow(0).getObs());
            	rec.setDirecta(SetMod.getAbsRow(0).getDirecta());
           	 
            	for(int i = 0; i< SetDet.getNumRows(); i++)
            	{
            		JProdProdSetProcV2 SetProc = new JProdProdSetProcV2(request);
            		SetProc.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "' AND Partida = '" + (i+1) + "'";
            		SetProc.m_OrderBy = "ID_Proceso ASC";
            		SetProc.Open();
              		
            		rec.agregaPartida(SetDet.getAbsRow(i).getClave(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getID_Formula(), SetDet.getAbsRow(i).getFormula(), SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getLote(), false, SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getMasMenos(), 0, 0, SetDet.getAbsRow(i).getTerminada(), SetDet.getAbsRow(i).getFecha());
           	 	
            		for(int j = 0; j< SetProc.getNumRows(); j++)
            		{
            			JProdProdSetDetprodV2 SetDetprod = new JProdProdSetDetprodV2(request);
            			SetDetprod.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "' AND Partida = '" + (i+1) + "' AND ID_Proceso = '" + (j+1) + "'";
            			SetDetprod.m_OrderBy = "Secuencia ASC";
            			SetDetprod.Open();
                   	
            			JProdFormulasSesPartProc proc =
            			rec.getPartida(i).getPartidaFormula().agregaPartida(SetProc.getAbsRow(j).getNombre(), 0, 
           					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getCantidad()), 
           					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getMasMenos()),
           					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getClave()),
           					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getDescripcion()),
           					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getUnidad()), 
           					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getPorcentaje()), 
           					SetProc.getAbsRow(j).getFecha(), SetProc.getAbsRow(j).getFechaSP());
           			
            			for(int k = 0; k < SetDetprod.getNumRows(); k++)
            			{
            				rec.getPartida(i).getPartidaFormula().agregaPartidaDet(proc, 
           						0F, SetDetprod.getAbsRow(k).getCantidad(), 0F, SetDetprod.getAbsRow(k).getMasMenos(), SetDetprod.getAbsRow(k).getID_Prod(), SetDetprod.getAbsRow(k).getDescripcion(), SetDetprod.getAbsRow(k).getUnidad(), 0, 0, 0, 0, false);
     				
            			}
            		}
            	}
            	
            	RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"PROD_PRODUCCION", "PPRD|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "||","");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("APLICAR_PRODUCCION"))
        {
        	if(!getSesion(request).getPermiso("PROD_PRODUCCION_AGREGAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION_AGREGAR","PPRD||||",mensaje);
                irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
                return;
            }
            
        	if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO para cambiar `por primera vez
            {
        		if(request.getParameter("ID") != null)
	            {
	              String[] valoresParam = request.getParameterValues("ID");
	              if(valoresParam.length == 1)
	              {
	                 JProdProdSetV2 SetMod = new JProdProdSetV2(request);
	                 SetMod.m_Where = "ID_Reporte = '" + p(request.getParameter("ID")) + "'";
	                 SetMod.Open();
	                 ///////////////////////////
	             	 if(SetMod.getAbsRow(0).getCDA() || SetMod.getAbsRow(0).getStatus().equals("C") || SetMod.getAbsRow(0).getStatus().equals("R"))
	            	 {
	                     idmensaje = 1;
	                     mensaje = "PRECAUCION: Este reporte está cerrado, revertido o cancelado. No se puede aplicar ningun proceso<br>";
	                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                     irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                     return;
	              	 } 
	             	 
	             	 HttpSession ses = request.getSession(true);
	             	 JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
	             	 if(rec == null)
	             	 {
	             	 	 rec = new JProdProduccionSes(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);
	             	 	 ses.setAttribute("prod_produccion_dlg", rec);
	             	 }
	             	 else
	             	 	 rec.resetear(request, getSesion(request).getSesion("PROD_PRODUCCION").getEspecial(), usuario);
	             	 
	             	 // LLena el reporte
	             	 JProdProdSetDetV2 SetDet = new JProdProdSetDetV2(request);
	             	 SetDet.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "'";
	             	 SetDet.m_OrderBy = "Partida ASC";
	             	 SetDet.Open();
	             	 	 
	             	 rec.setReporteNum(SetMod.getAbsRow(0).getNumero());
	             	 rec.setFecha(SetMod.getAbsRow(0).getFecha());
	             	 rec.setConcepto(SetMod.getAbsRow(0).getConcepto());
	             	 rec.setID_BodegaMP((byte)SetMod.getAbsRow(0).getID_BodegaMP());
	             	 rec.setID_BodegaPT((byte)SetMod.getAbsRow(0).getID_BodegaPT());
	             	 rec.setBodegaMP(SetMod.getAbsRow(0).getBodega_MP());
	             	 rec.setBodegaPT(SetMod.getAbsRow(0).getBodega_PT());
	             	 rec.setObs(SetMod.getAbsRow(0).getObs());
	             	 rec.setDirecta(SetMod.getAbsRow(0).getDirecta());
	            	 
	             	 for(int i = 0; i< SetDet.getNumRows(); i++)
	             	 {
	             		 JProdProdSetProcV2 SetProc = new JProdProdSetProcV2(request);
	             		 SetProc.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "' AND Partida = '" + (i+1) + "'";
	             		 SetProc.m_OrderBy = "ID_Proceso ASC";
	             		 SetProc.Open();
	               		
	             		 rec.agregaPartida(SetDet.getAbsRow(i).getClave(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getID_Formula(), SetDet.getAbsRow(i).getFormula(), SetDet.getAbsRow(i).getCantidad(), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getLote(), false, SetDet.getAbsRow(i).getObs(), SetDet.getAbsRow(i).getMasMenos(), SetDet.getAbsRow(i).getNumProc(), SetDet.getAbsRow(i).getActualProc(), SetDet.getAbsRow(i).getTerminada(), SetDet.getAbsRow(i).getFecha());
	            	 	
	             		 for(int j = 0; j< SetProc.getNumRows(); j++)
	             		 {
	             			 JProdProdSetDetprodV2 SetDetprod = new JProdProdSetDetprodV2(request);
	             			 SetDetprod.m_Where = "ID_Reporte = '" + SetMod.getAbsRow(0).getID_Reporte() + "' AND Partida = '" + (i+1) + "' AND ID_Proceso = '" + (j+1) + "'";
	             			 SetDetprod.m_OrderBy = "Secuencia ASC";
	             			 SetDetprod.Open();
	                    	
	             			 JProdFormulasSesPartProc proc =
	             			 rec.getPartida(i).getPartidaFormula().agregaPartida(SetProc.getAbsRow(j).getNombre(), 0, 
	            					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getCantidad()), 
	            					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getMasMenos()),
	            					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getClave()),
	            					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getDescripcion()),
	            					(SetProc.getAbsRow(j).getClave() == null ? "" : SetProc.getAbsRow(j).getUnidad()), 
	            					(SetProc.getAbsRow(j).getClave() == null ? 0F : SetProc.getAbsRow(j).getPorcentaje()),
	            					SetProc.getAbsRow(j).getFecha(), SetProc.getAbsRow(j).getFechaSP());
	            			
	             			 for(int k = 0; k < SetDetprod.getNumRows(); k++)
	             			 {
	            				rec.getPartida(i).getPartidaFormula().agregaPartidaDet(proc, 
	            						0F, SetDetprod.getAbsRow(k).getCantidad(), 0F, SetDetprod.getAbsRow(k).getMasMenos(), SetDetprod.getAbsRow(k).getID_Prod(), SetDetprod.getAbsRow(k).getDescripcion(), SetDetprod.getAbsRow(k).getUnidad(), 0, 0, 0, 0, false);
	      				
	             			 }
	             		 }
	             	 }
	             	 
	             	 getSesion(request).setID_Mensaje(idmensaje, mensaje);
	             	 irApag("/forsetiweb/produccion/prod_produccion_dlg_aplproc.jsp", request, response);
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
          	  		if(VerificarParametrosApl(request, response))
          	  		{
          	  			AgregarApl(request, response);
          	  			return;
          	  		}
          	  		  	       		  
          	  		irApag("/forsetiweb/produccion/prod_produccion_dlg_aplproc.jsp", request, response);  
          	  		return;
          	  	}
        	}
        } 
        else if(request.getParameter("proceso").equals("RASTREAR_MOVIMIENTO"))
        {
        	if(!getSesion(request).getPermiso("PROD_PRODUCCION_CONSULTAR"))
            {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION_CONSULTAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION_CONSULTAR","PPRD||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
            }

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
              	
                  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("PROD_PRODUCCION").generarTitulo(JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","CONSULTAR_PRODUCCION",3)),
                  								"PPRD",request.getParameter("ID"));
                  String rastreo_imp = "true";
                  request.setAttribute("rastreo_imp", rastreo_imp);
                  // Ahora pone los atributos para el jsp
                  request.setAttribute("rastreo", rastreo);
                  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"PROD_PRODUCCION_CONSULTAR","PPRD|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "||","");
                  irApag("/forsetiweb/rastreo_imp.jsp", request, response); 
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
        else if(request.getParameter("proceso").equals("CANCELAR_PRODUCCION"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("PROD_PRODUCCION_CANCELAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_PRODUCCION_CANCELAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_PRODUCCION_CANCELAR","PPRD||||",mensaje);
                irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
                 JProdProdSetV2 SetMod = new JProdProdSetV2(request);
                 SetMod.m_Where = "ID_Reporte = '" + p(request.getParameter("ID")) + "'";
                 SetMod.Open();
                 ///////////////////////////
             	 if(SetMod.getAbsRow(0).getStatus().equals("C"))
            	 {
                     idmensaje = 1;
                     mensaje = "PRECAUCION: Este reporte ya está cancelado <br>";
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                     return;
              	 } 
            	 else if( setids.getAbsRow(0).getAuditarAlm() && !SetMod.getAbsRow(0).getStatus().equals("R") )
            	 {
            		 idmensaje = 1;
                     mensaje = "PRECAUCION: Este reporte necesita estar revertido desde el módulo del almacén para poder cancelarlo";
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                     return;
            	 } 
            	 else
                 {
            		 CancelarReporte(request, response);
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

    public boolean VerificarParametrosApl(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
    	HttpSession ses = request.getSession(true);
    	JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
    	 
    	// Verificacion
    	if(request.getParameter("tipo_part").equals("PT"))
    	{
    		int i = Integer.parseInt(request.getParameter("id_form")) -1;
    		float cantidad;
    		try { cantidad = Float.parseFloat(request.getParameter("cantidad")); } catch(NumberFormatException e) { cantidad = 0F; }
    		if( (cantidad < rec.getPartida(i).getCantidad() && (rec.getPartida(i).getCantidad() - cantidad) > rec.getPartida(i).getMasMenos()) ||
    			 (cantidad > rec.getPartida(i).getCantidad() && (cantidad - rec.getPartida(i).getCantidad()) > rec.getPartida(i).getMasMenos())	)
    		{
    		      idmensaje = 3; mensaje.append("ERROR: No se puede cambiar la cantidad porque la formula o proceso no lo permite, o porque se sale de las diferencias permitidas.<br>");
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	          return false;
    		}
    		
    	}
    	else if(request.getParameter("tipo_part").equals("SP"))
    	{
    		int i = Integer.parseInt(request.getParameter("id_form")) -1;
    		int j = Integer.parseInt(request.getParameter("id_proc")) -1;
    		float cantidad;
    		try { cantidad = Float.parseFloat(request.getParameter("cantidad")); } catch(NumberFormatException e) { cantidad = 0F; }
    		if( (cantidad < rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() && (rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() - cantidad) > rec.getPartida(i).getPartidaFormula().getPartida(j).getMasMenos()) ||
    			 (cantidad > rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() && (cantidad - rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad()) > rec.getPartida(i).getPartidaFormula().getPartida(j).getMasMenos())	)
    		{
    		      idmensaje = 3; mensaje.append("ERROR: No se puede cambiar la cantidad porque la formula o proceso no lo permite, o porque se sale de las diferencias permitidas.<br>");
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	          return false;
    		}
    		
    		if(JUtil.VerificaStocks(request, mensaje, rec.getID_BodegaMP(), rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod(), rec.getManejoStocks(), rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad()) != -1)
			{
    			idmensaje = 3;
				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
				return false;
			}
    	}
    	else if(request.getParameter("tipo_part").equals("MP"))
    	{
    		int i = Integer.parseInt(request.getParameter("id_form")) -1;
    		int j = Integer.parseInt(request.getParameter("id_proc")) -1;
    		for(int d = 0; d < rec.getPartida(i).getPartidaFormula().getPartida(j).numPartidas(); d++)
    		{
    			float cantidad;
    			try { cantidad = Float.parseFloat(request.getParameter("cantidad_" + (d+1))); } catch(NumberFormatException e) { cantidad = 0F; }
    			if( (cantidad < rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() && (rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() - cantidad) > rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getMasMenos()) ||
    					(cantidad > rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() && (cantidad - rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad()) > rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getMasMenos())	)
    			{
    				idmensaje = 3; mensaje.append("ERROR: No se puede cambiar la cantidad porque la formula o proceso no lo permite, o porque se sale de las diferencias permitidas.<br>");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    				return false;
    			}
    			
    			if(JUtil.VerificaExistencias(request, mensaje, rec.getID_BodegaMP(), rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getID_Prod(), rec.getAuditarAlm(), cantidad) != -1)
  			  	{
    				idmensaje = 3;
    				getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    				return false;
  			  	}
    		}
    	}
    	return true;
    	
    }

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("idprod_part") != null && request.getParameter("formula") != null &&
    	  request.getParameter("cantidad") != null && request.getParameter("lote") != null && request.getParameter("obs_partida") != null &&
    	  !request.getParameter("idprod_part").equals("") && !request.getParameter("formula").equals("") &&
    	  !request.getParameter("cantidad").equals(""))
      {
    	  return true;
      }
      else
      {
          idmensaje = 1; mensaje = "PRECAUCION: Por lo menos se deben enviar los parámetros de clave del producto, fórmula y cantidad <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
        JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
         
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: El reporte no contiene formulas y procesos a producir <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        
        if(rec.getDirecta())
        {
        	idmensaje = rec.VerificacionesFinales(request, mensaje);
        	if(idmensaje != -1)
        	{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
        }
        
        return true;
	
    }

    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");

        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
        long idformula = Long.parseLong(request.getParameter("formula"));
        
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod_part"), idformula, mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");

        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   
    }
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");

        int numProc = 0;
        
	    String tbl;
	    tbl = "CREATE LOCAL TEMPORARY TABLE _tmp_produccion_reportes_det (\n";
	    tbl += "partida smallint NOT NULL,\n";
	    tbl += "cantidad numeric(9,3) NOT NULL,\n";
	    tbl += "loteref character varying(255),\n";
	    tbl += "id_prod character varying(20) NOT NULL,\n";
	    tbl += "id_formula integer,\n";
	    tbl += "obs character varying(80),\n";
	    tbl += "masmenos numeric(9,3) NOT NULL\n";
	    tbl += ");\n\n";
	      
        tbl += "CREATE LOCAL TEMPORARY TABLE _tmp_produccion_reportes_procesos (\n";
        tbl += "partida smallint NOT NULL,\n";
        tbl += "id_proceso smallint NOT NULL,\n";
        tbl += "nombre character varying(255) NOT NULL,\n";
        tbl += "tiempo varchar(10) NOT NULL,\n";
        tbl += "id_subprod character varying(20),\n";
        tbl += "porcentaje numeric(8,6),\n";
        tbl += "cantidad numeric(9,3),\n";
        tbl += "masmenos numeric(9,3)\n";
        tbl += ");\n\n";
          
    	tbl += "CREATE LOCAL TEMPORARY TABLE _tmp_produccion_reportes_detprod (\n";
    	tbl += "partida smallint NOT NULL,\n";
    	tbl += "id_proceso smallint NOT NULL,\n";
    	tbl += "id_prod character varying(20) NOT NULL,\n";
    	tbl += "secuencia smallint NOT NULL,\n";
    	tbl += "cantidad numeric(9,3) NOT NULL,\n";
    	tbl += "masmenos numeric(9,3) NOT NULL\n";
    	tbl += ");\n\n";
        
    	for(int i = 0; i < rec.numPartidas(); i++)
		{
        	tbl += "insert into _tmp_produccion_reportes_det\n";
        	tbl += "values('" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','" + p(rec.getPartida(i).getLote()) + "','" + p(rec.getPartida(i).getID_Prod()) + "','" + rec.getPartida(i).getID_Formula() + "','" + p(rec.getPartida(i).getObs()) + "','" + rec.getPartida(i).getMasMenos() + "');\n\n";
        	
        	for(int j = 0; j < rec.getPartida(i).getPartidaFormula().numPartidas(); j++)
			{
        		numProc++;
        		
        		tbl += "insert into _tmp_produccion_reportes_procesos\n";
                tbl += "values('" + (i+1) + "','" + (j+1) + "','" + p(rec.getPartida(i).getPartidaFormula().getPartida(j).getNombre()) + "','" + Integer.toString(rec.getPartida(i).getPartidaFormula().getPartida(j).getTiempo()) + " day'," +
                		(rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod().equals("") ? "null" : "'" + p(rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod()) + "'") + "," +
                		(rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod().equals("") ? "null" : "'" + rec.getPartida(i).getPartidaFormula().getPartida(j).getPorcentaje() + "'") + "," +
                		(rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod().equals("") ? "null" : "'" + rec.getPartida(i).getPartidaFormula().getPartida(j).getCantidad() + "'") + "," +
                		(rec.getPartida(i).getPartidaFormula().getPartida(j).getID_Prod().equals("") ? "null" : "'" + rec.getPartida(i).getPartidaFormula().getPartida(j).getMasMenos() + "'") + ");\n\n";

                for(int d = 0; d < rec.getPartida(i).getPartidaFormula().getPartida(j).numPartidas(); d++)
				{
        			tbl += "insert into _tmp_produccion_reportes_detprod\n";
    				tbl += "values('" + (i+1) + "','" + (j+1) + "','" + p(rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getID_Prod()) + "','" + (d+1) + "','" + 
    						rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getCantidad() + "','" + rec.getPartida(i).getPartidaFormula().getPartida(j).getPartida(d).getMasMenos() + "');\n\n";
    	
				}
			}
		}

    	String str = "select * from sp_prod_reportes_agregar('" + rec.getID_Entidad() + "','" + rec.getReporteNum() + "','" + p(JUtil.obtFechaSQL(rec.getFecha())) + "','" + p(rec.getConcepto()) + 
    		"','" + rec.getID_BodegaMP() + "','" + rec.getID_BodegaPT() + "','" + p(rec.getObs()) + "','" + (request.getParameter("directa") != null ? "1" : "0") + "','" + numProc +"') as (err integer, res varchar, clave integer)";
		
    	//doDebugSQL(request, response, tbl + "\n\n" + str);
    
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, tbl, str, 
				"DROP TABLE _tmp_produccion_reportes_detprod; DROP TABLE _tmp_produccion_reportes_procesos; DROP TABLE _tmp_produccion_reportes_det;", rfb);
		            
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_PRODUCCION_AGREGAR", "PPRD|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "||",rfb.getRes());
		irApag("/forsetiweb/produccion/prod_produccion_dlg.jsp", request, response);
        		
    }
    
    public void CancelarReporte(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	String str = "select * from sp_prod_reportes_cancelar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "') as (err integer, res varchar, clave integer)";
   
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);
		            
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_PRODUCCION_CANCELAR", "PPRD|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "||",rfb.getRes());
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	}
    
    public void AgregarApl(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
    	JProdProduccionSes rec = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
    	
    	String str = "", tbl = "";
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		   	  	        
    	// Verificacion
    	if(request.getParameter("tipo_part").equals("PT"))
    	{
    		float cantidad = Float.parseFloat(request.getParameter("cantidad"));
    		str = "select * from sp_prod_reportes_aplicar('" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "','" + rec.getID_BodegaMP() + "','" + rec.getID_BodegaPT() + "','PT','" + p(request.getParameter("ID")) + "','" + p(request.getParameter("id_form")) + "',null,'" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + JUtil.redondear(cantidad, 3) + "') as (err integer, res varchar, clave integer);";
    		doCallStoredProcedure(request, response, str, rfb);
       	}
    	else if(request.getParameter("tipo_part").equals("SP"))
    	{
    		float cantidad = Float.parseFloat(request.getParameter("cantidad"));
    		str = "select * from sp_prod_reportes_aplicar('" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "','" + rec.getID_BodegaMP() + "','" + rec.getID_BodegaPT() + "','SP','" + p(request.getParameter("ID")) + "','" + p(request.getParameter("id_form")) + "','" + p(request.getParameter("id_proc")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + JUtil.redondear(cantidad, 3) + "') as (err integer, res varchar, clave integer);";
    		doCallStoredProcedure(request, response, str, rfb);
       	}
    	else if(request.getParameter("tipo_part").equals("MP"))
    	{
    		tbl += "CREATE LOCAL TEMPORARY TABLE _tmp_produccion_reportes_detprod (\n";
        	tbl += "secuencia smallint NOT NULL,\n";
        	tbl += "cantidad numeric(9,3) NOT NULL\n";
        	tbl += ");\n\n";
        	
        	int i = Integer.parseInt(request.getParameter("id_form")) -1;
    		int j = Integer.parseInt(request.getParameter("id_proc")) -1;
    		
    		for(int d = 0; d < rec.getPartida(i).getPartidaFormula().getPartida(j).numPartidas(); d++)
    		{
    			float cantidad = Float.parseFloat(request.getParameter("cantidad_" + (d+1)));
    			tbl += "INSERT INTO _tmp_produccion_reportes_detprod\n";
    			tbl += "VALUES('" + (d+1) + "','" + JUtil.redondear(cantidad, 3) + "');\n\n";
    		}
    		
    		str = "select * from sp_prod_reportes_aplicar('" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "','" + rec.getID_BodegaMP() + "','" + rec.getID_BodegaPT() + "','MP','" + p(request.getParameter("ID")) + "','" + p(request.getParameter("id_form")) + "','" + p(request.getParameter("id_proc")) + "','" + p(JUtil.obtFechaSQL( rec.getPartida(i).getPartidaFormula().getPartida(j).getFecha() )) + "',null) as (err integer, res varchar, clave integer);";
    		doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _tmp_produccion_reportes_detprod;", rfb);
     	}
    	
   		//doDebugSQL(request, response, tbl + "\n\n" + str);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_PRODUCCION_AGREGAR", "PPRD|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("PROD_PRODUCCION").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/produccion/prod_produccion_dlg_aplproc.jsp", request, response);
    	        		
    }
}
