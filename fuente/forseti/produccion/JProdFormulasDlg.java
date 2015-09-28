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

import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.JForsetiApl;
import forseti.sets.JProdFormulasSetDetprodV2;
import forseti.sets.JProdFormulasSetProcV2;
import forseti.sets.JProdFormulasSetV2;

@SuppressWarnings("serial")
public class JProdFormulasDlg extends JForsetiApl
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

      String prod_formulas_dlg = "";
      request.setAttribute("prod_formulas_dlg",prod_formulas_dlg);

      String mensaje = ""; short idmensaje = -1;
  
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
  
        if(request.getParameter("proceso").equals("AGREGAR_FORMULA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("PROD_FORMULAS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS_AGREGAR","PFRM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            HttpSession ses = request.getSession(true);
            JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
            if(rec == null)
            {
              rec = new JProdFormulasSes();
              ses.setAttribute("prod_formulas_dlg", rec);
            }
            else
              rec.resetear();

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);
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
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametrosPartida(request, response))
       					  AgregarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    EditarPartida(request, response);
       			  }
       			  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
 	   			  return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametrosPartidaDet(request, response))
       					  AgregarPartidaDet(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartidaDet(request, response))
    	   			    EditarPartidaDet(request, response);
       			  }
       			  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartidaDet(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
 	   			  return;
	   		  }
	   	  }
	
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_FORMULA"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("PROD_FORMULAS"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS","PFRM||||",mensaje);
                irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {

                HttpSession ses = request.getSession(true);
                JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
                if(rec == null)
                {
                  rec = new JProdFormulasSes();
                  ses.setAttribute("prod_formulas_dlg", rec);
                }
                else
                  rec.resetear();

                // aqui debe llenar la formula
                JProdFormulasSetV2 SetMod = new JProdFormulasSetV2(request);
                JProdFormulasSetProcV2 SetPrc = new JProdFormulasSetProcV2(request);
                SetMod.m_Where = "ID_Formula = '" + p(request.getParameter("ID")) + "'";
                SetMod.Open();
                SetPrc.m_Where = "ID_Formula = '" + p(request.getParameter("ID")) + "'";
                SetPrc.m_OrderBy = "ID_Proceso ASC";
                SetPrc.Open();
                
        			
                rec.setID_Formula(SetMod.getAbsRow(0).getID_Formula());
            	rec.setDescripcion(SetMod.getAbsRow(0).getDescripcion());
            	rec.setID_Prod(SetMod.getAbsRow(0).getClave());
            	rec.setFormula(SetMod.getAbsRow(0).getFormula());
            	rec.setRendimiento(SetMod.getAbsRow(0).getCantidad());
            	rec.setMasMenos(SetMod.getAbsRow(0).getMasMenos());
            	rec.setUnidad(SetMod.getAbsRow(0).getUnidad());
            	rec.setCantLotes(SetMod.getAbsRow(0).getUnidadUnica());
            	rec.setID_Proceso(SetMod.getAbsRow(0).getID_Formula());
        		
            	for (int p = 0; p < SetPrc.getNumRows(); p++)
            	{
               		
                    JProdFormulasSesPartProc sesproc = rec.agregaPartida(SetPrc.getAbsRow(p).getNombre(), SetPrc.getAbsRow(p).getTiempo(), SetPrc.getAbsRow(p).getCantidad(), SetPrc.getAbsRow(p).getMasMenos(), (SetPrc.getAbsRow(p).getID_SubProd() == null ? "" : SetPrc.getAbsRow(p).getID_SubProd()), (SetPrc.getAbsRow(p).getDescripcion() == null ? "" : SetPrc.getAbsRow(p).getDescripcion()), (SetPrc.getAbsRow(p).getUnidad() == null ? "" : SetPrc.getAbsRow(p).getUnidad()), SetPrc.getAbsRow(p).getPorcentaje(), null, null);
                    
                    JProdFormulasSetDetprodV2 SetDet = new JProdFormulasSetDetprodV2(request);
                    SetDet.m_Where = "ID_Proceso = '" + SetPrc.getAbsRow(p).getID_Proceso() + "'";
                    SetDet.Open();

                    for(int j = 0; j< SetDet.getNumRows(); j++)
                    {
                    	rec.agregaPartidaDet(sesproc, SetDet.getAbsRow(j).getCantidad(), JUtil.redondear(SetDet.getAbsRow(j).getCantidad() * SetMod.getAbsRow(0).getCantidad(),3), 
            							SetDet.getAbsRow(j).getMasMenos(), JUtil.redondear(SetDet.getAbsRow(j).getMasMenos() * SetMod.getAbsRow(0).getCantidad(),3),
          																	SetDet.getAbsRow(j).getID_Prod(), SetDet.getAbsRow(j).getDescripcion(), SetDet.getAbsRow(j).getUnidad(),
          																	SetDet.getAbsRow(j).getCP(), SetDet.getAbsRow(j).getUC(), JUtil.redondear(SetDet.getAbsRow(j).getCP() * SetDet.getAbsRow(j).getCantidad(),4), JUtil.redondear(SetDet.getAbsRow(j).getUC() * SetDet.getAbsRow(j).getCantidad(),4), SetDet.getAbsRow(j).getPrincipal());
                    }
            	}
            	
            	rec.establecerResultados();
            	            	           
            	RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"PROD_FORMULAS", "PFRM|" + request.getParameter("ID") + "|||","");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_FORMULA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("PROD_FORMULAS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS_CAMBIAR","PFRM||||",mensaje);
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

                  HttpSession ses = request.getSession(true);
                  JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
                  if(rec == null)
                  {
                    rec = new JProdFormulasSes();
                    ses.setAttribute("prod_formulas_dlg", rec);
                  }
                  else
                    rec.resetear();

               // aqui debe llenar la formula
                  JProdFormulasSetV2 SetMod = new JProdFormulasSetV2(request);
                  JProdFormulasSetProcV2 SetPrc = new JProdFormulasSetProcV2(request);
                  SetMod.m_Where = "ID_Formula = '" + p(request.getParameter("ID")) + "'";
                  SetMod.Open();
                  SetPrc.m_Where = "ID_Formula = '" + p(request.getParameter("ID")) + "'";
                  SetPrc.m_OrderBy = "ID_Proceso ASC";
                  SetPrc.Open();
                  
                  rec.setID_Formula(SetMod.getAbsRow(0).getID_Formula());
                  rec.setDescripcion(SetMod.getAbsRow(0).getDescripcion());
                  rec.setID_Prod(SetMod.getAbsRow(0).getClave());
                  rec.setFormula(SetMod.getAbsRow(0).getFormula());
                  rec.setRendimiento(SetMod.getAbsRow(0).getCantidad());
                  rec.setMasMenos(SetMod.getAbsRow(0).getMasMenos());
                  rec.setUnidad(SetMod.getAbsRow(0).getUnidad());
                  rec.setCantLotes(SetMod.getAbsRow(0).getUnidadUnica());
                  rec.setID_Proceso(SetMod.getAbsRow(0).getID_Formula());
          		
                  for (int p = 0; p < SetPrc.getNumRows(); p++)
                  {
                 		
                	  JProdFormulasSesPartProc sesproc = rec.agregaPartida(SetPrc.getAbsRow(p).getNombre(), SetPrc.getAbsRow(p).getTiempo(), SetPrc.getAbsRow(p).getCantidad(), SetPrc.getAbsRow(p).getMasMenos(), (SetPrc.getAbsRow(p).getID_SubProd() == null ? "" : SetPrc.getAbsRow(p).getID_SubProd()), (SetPrc.getAbsRow(p).getDescripcion() == null ? "" : SetPrc.getAbsRow(p).getDescripcion()), (SetPrc.getAbsRow(p).getUnidad() == null ? "" : SetPrc.getAbsRow(p).getUnidad()), SetPrc.getAbsRow(p).getPorcentaje(), null, null);
                      
                      JProdFormulasSetDetprodV2 SetDet = new JProdFormulasSetDetprodV2(request);
                      SetDet.m_Where = "ID_Proceso = '" + SetPrc.getAbsRow(p).getID_Proceso() + "'";
                      SetDet.Open();

                      for(int j = 0; j< SetDet.getNumRows(); j++)
                      {
                      	rec.agregaPartidaDet(sesproc, SetDet.getAbsRow(j).getCantidad(), JUtil.redondear(SetDet.getAbsRow(j).getCantidad() * SetMod.getAbsRow(0).getCantidad(),3), 
              							SetDet.getAbsRow(j).getMasMenos(), JUtil.redondear(SetDet.getAbsRow(j).getMasMenos() * SetMod.getAbsRow(0).getCantidad(),3),
            																	SetDet.getAbsRow(j).getID_Prod(), SetDet.getAbsRow(j).getDescripcion(), SetDet.getAbsRow(j).getUnidad(),
            																	SetDet.getAbsRow(j).getCP(), SetDet.getAbsRow(j).getUC(), JUtil.redondear(SetDet.getAbsRow(j).getCP() * SetDet.getAbsRow(j).getCantidad(),4), JUtil.redondear(SetDet.getAbsRow(j).getUC() * SetDet.getAbsRow(j).getCantidad(),4), SetDet.getAbsRow(j).getPrincipal());
                      }
                  }

              	  rec.establecerResultados();
              	              	           
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);
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
       				  if(VerificarParametros(request, response))
       				  {
       					  Cambiar(request, response);
       					  return;
       				  }
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametrosPartida(request, response))
       					  AgregarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartida(request, response))
    	   			    EditarPartida(request, response);
       			  }
       			  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_PROC"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartida(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
 	   			  return;
	   		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				  if(VerificarParametrosPartidaDet(request, response))
       					  AgregarPartidaDet(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				if(VerificarParametrosPartidaDet(request, response))
    	   			    EditarPartidaDet(request, response);
       			  }
       			  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
       			  return;
	   		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_DET"))
	   		  {
       			  if(AgregarCabecero(request,response) == -1)
       			  {
       				BorrarPartidaDet(request, response);
       			  }
	       		  
       			  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
 	   			  return;
	   		  }
	   	  }
	
        }  
        else if(request.getParameter("proceso").equals("EDITAR_FORMULA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("PROD_FORMULAS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS_CAMBIAR","PFRM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO para cambiar `por primera vez
          {
        	  HttpSession ses = request.getSession(true);
              JProdProduccionSes prod = (JProdProduccionSes)ses.getAttribute("prod_produccion_dlg");
              JProdFormulasSes rec = null;
              
              JProdFormulasSetV2 set = new JProdFormulasSetV2(request);
              set.m_Where = "ID_Formula = '" + prod.getPartida(Integer.parseInt(request.getParameter("idpartida"))).getID_Formula() + "'";
              set.Open();
              
              if(set.getAbsRow(0).getUnidadUnica())
              {
            	  idmensaje = 1; mensaje += "PRECAUCION: No se pueden editar las fórmulas simples<br>";
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return; 
              }
              
              rec = prod.getPartida(Integer.parseInt(request.getParameter("idpartida"))).getPartidaFormula();
              ses.setAttribute("prod_formulas_dlg", rec);
                      	  
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);
              return;
         	            
          }
          else
          {
	       	  // Solicitud de envio a procesar
        	  if(request.getParameter("subproceso").equals("AGR_PART_PROC"))
	   		  {
        		  if(VerificarParametrosPartida(request, response))
        			  AgregarPartida(request, response);
       		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_PROC"))
	   		  {
       			  if(VerificarParametrosPartida(request, response))
       				  EditarPartida(request, response);
       		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_PROC"))
	   		  {
       			  BorrarPartida(request, response);
       		  }
        	  else if(request.getParameter("subproceso").equals("AGR_PART_DET"))
	   		  {
        		  if(VerificarParametrosPartidaDet(request, response))
        			  AgregarPartidaDet(request, response);
       		  }
	   		  else if(request.getParameter("subproceso").equals("EDIT_PART_DET"))
	   		  {
       			  if(VerificarParametrosPartidaDet(request, response))
       				  EditarPartidaDet(request, response);
       		  }
	   		  else if(request.getParameter("subproceso").equals("BORR_PART_DET"))
	   		  {
       			  BorrarPartidaDet(request, response);
       		  }
        	  
        	  irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);  
        	  return;
	   	  }
	
        } 
        else if(request.getParameter("proceso").equals("APLICAR_FORMULA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("PROD_FORMULAS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "PROD_FORMULAS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"PROD_FORMULAS_CAMBIAR","PFRM||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }
          
          
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if(valoresParam.length == 1)
            {
            	Aplicar(request, response);
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

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("nombre") != null && request.getParameter("tiempo") != null &&
    			!request.getParameter("nombre").equals("") && !request.getParameter("tiempo").equals(""))
    	{
    		return true;
    	}
    	else
    	{
    		idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parámetros de nombre y tiempo del proceso<br>";
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }

    public boolean VerificarParametrosPartidaDet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      String p = request.getParameter("idpartidaproc");
      // Verificacion
      if(request.getParameter("cantidad" + p) != null && request.getParameter("mmcantidad" + p) != null && request.getParameter("idprod_part" + p) != null && request.getParameter("principal" + p) != null &&
         !request.getParameter("cantidad" + p).equals("") && !request.getParameter("mmcantidad" +p).equals("") && !request.getParameter("idprod_part" + p).equals("") && !request.getParameter("principal" + p).equals(""))
      {
    	  return true;
      }
      else
      {
          idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parámetros de cantidad, diferencias, clave del producto y pp <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
        HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
         
        if(rec.getID_Prod().equals("") || rec.getFormula().equals("") || rec.getRendimiento() <= 0.0 || rec.getMasMenos() < 0.0)
		{
 	        idmensaje = 1; mensaje.append("PRECAUCION: La clave, fórmula, cantidad o diferencias están mal <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
		}
        
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append("PRECAUCION: La fórmula no contiene procesos <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
        else
        {
        	for(int p = 0; p < rec.getPartidas().size(); p++)
        	{
        		if(rec.getPartida(p).getPartidas().size() == 0)
        		{
        			idmensaje = 1; mensaje.append("PRECAUCION: No pueden haber procesos vacíos<br>");
          	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
           	        return false;
        		}
        	}
        }
          
             
        return true;
	
    }

    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
              
       	HttpSession ses = request.getSession(true);
       	JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
 
       	idmensaje = rec.agregaCabecero(request, mensaje);
       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

       	return idmensaje;
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

    	HttpSession ses = request.getSession(true);
    	JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
    	
    	int tiempo = Integer.parseInt(request.getParameter("tiempo"));
    	        
    	idmensaje = rec.agregaPartida(request, p(request.getParameter("nombre")), tiempo, mensaje);

    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	     
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

    	HttpSession ses = request.getSession(true);
    	JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");

    	int tiempo = Integer.parseInt(request.getParameter("tiempo"));
        
    	idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartidaproc")), request, p(request.getParameter("nombre")), tiempo, mensaje);
    	
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	     
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

    	HttpSession ses = request.getSession(true);
    	JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");

    	idmensaje = rec.borraPartida(request.getParameter("proceso"), Integer.parseInt(request.getParameter("idpartidaproc")), mensaje);

    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    	     
    }

    public void AgregarPartidaDet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
        String p = request.getParameter("idpartidaproc");
        float cantidad = Float.parseFloat(request.getParameter("cantidad" + p));
        float masmenos = Float.parseFloat(request.getParameter("mmcantidad" + p));
        boolean principal = request.getParameter("principal" + p).equals("0") ? false : true;
        int idpartidaproc = Integer.parseInt(p);
        idmensaje = rec.agregaPartidaDet(request, idpartidaproc, cantidad, masmenos, request.getParameter("idprod_part" + p), principal, mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
     
    }

    public void EditarPartidaDet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");
        String p = request.getParameter("idpartidaproc");
        float cantidad = Float.parseFloat(request.getParameter("cantidad" + p));
        float masmenos = Float.parseFloat(request.getParameter("mmcantidad" + p));
        boolean principal = request.getParameter("principal" + p).equals("0") ? false : true;
        int idpartidaproc = Integer.parseInt(p);
        idmensaje = rec.editaPartidaDet(idpartidaproc,Integer.parseInt(request.getParameter("idpartida")), request, cantidad, masmenos, request.getParameter("idprod_part" + p), principal, mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
     
    }

    public void BorrarPartidaDet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");

        idmensaje = rec.borraPartidaDet(request.getParameter("proceso"), Integer.parseInt(request.getParameter("idpartidaproc")), Integer.parseInt(request.getParameter("idpartida")), mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
     
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");

        String str = "select * from sp_prod_formulas_cambiar('" + rec.getID_Formula() + "','" + p(rec.getID_Prod()) + "','" + p(rec.getFormula()) + "','" + rec.getRendimiento() + "','" + ((rec.getCantLotes()) ? "1" : "0" ) + "','" + rec.getMasMenos() + "') as (err integer, res varchar, clave integer)";
       
        String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_PRODUCCION_FORMULAS_PROCESOS (\n";
        tbl += " ID_Proceso int NOT NULL , \n";
        tbl += " Nombre varchar(255) NOT NULL , \n";
        tbl += " Tiempo smallint NOT NULL , \n";
        tbl += " ID_SubProd varchar(20) ,\n";
        tbl += " Porcentaje numeric(8,6) , \n";
        tbl += " Cantidad numeric(12, 6) ,\n";
        tbl += " MasMenos numeric(12, 6) \n";
        tbl += ");\n\n";
        tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_PRODUCCION_FORMULAS_DETPROD (\n";
        tbl += " ID_Proceso int NOT NULL , \n";
        tbl += " ID_Prod varchar(20) NOT NULL ,\n";
        tbl += " Cantidad numeric(12, 6) NOT NULL ,\n";
        tbl += " MasMenos numeric(12, 6) NOT NULL ,\n";
        tbl += " Principal bit NOT NULL \n";
        tbl += ");\n\n";

        for(int p = 0; p < rec.getPartidas().size(); p++)
        {
        	String id_subprod = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + p(rec.getPartida(p).getID_Prod()) + "'");
        	String porcentaje = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getPorcentaje()) + "'");
        	String cantidad = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getCantidad()) + "'");
        	String masmenos = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getMasMenos()) + "'");
        	
        	tbl += "insert into _TMP_PRODUCCION_FORMULAS_PROCESOS\n";
        	tbl += "values('" + p + "','" + p(rec.getPartida(p).getNombre()) + "','" + rec.getPartida(p).getTiempo() + "'," + id_subprod + "," + porcentaje + "," + cantidad + "," + masmenos + ");\n";
        	for(int i = 0; i < rec.getPartida(p).getPartidas().size(); i++)
        	{
        		tbl += "insert into _TMP_PRODUCCION_FORMULAS_DETPROD\n";
        		tbl += "values('" + p + "','" + p(rec.getPartida(p).getPartida(i).getID_Prod()) + "','" + rec.getPartida(p).getPartida(i).getCantidadXU() + "','" + rec.getPartida(p).getPartida(i).getMasMenosXU() +  "','" + (rec.getPartida(p).getPartida(i).getPrincipal() ? "1" : "0") + "');\n";
           	}
        }
        JRetFuncBas rfb = new JRetFuncBas();
      			
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_PRODUCCION_FORMULAS_DETPROD; DROP TABLE _TMP_PRODUCCION_FORMULAS_PROCESOS", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_FORMULAS_CAMBIAR", "PFRM|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);

    }    
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JProdFormulasSes rec = (JProdFormulasSes)ses.getAttribute("prod_formulas_dlg");

        String str = "select * from sp_prod_formulas_agregar('" + p(rec.getID_Prod()) + "','" + p(rec.getFormula()) + "','" + rec.getRendimiento() + "','" + (( rec.getCantLotes()) ? "1" : "0" ) + "','" + rec.getMasMenos() + "') as (err integer, res varchar, clave integer)";
       
        String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_PRODUCCION_FORMULAS_PROCESOS (\n";
        tbl += " ID_Proceso int NOT NULL , \n";
        tbl += " Nombre varchar(255) NOT NULL , \n";
        tbl += " Tiempo smallint NOT NULL , \n";
        tbl += " ID_SubProd varchar(20) ,\n";
        tbl += " Porcentaje numeric(8,6) , \n";
        tbl += " Cantidad numeric(12, 6) ,\n";
        tbl += " MasMenos numeric(12, 6) \n";
        tbl += ");\n\n";
        tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_PRODUCCION_FORMULAS_DETPROD (\n";
        tbl += " ID_Proceso int NOT NULL , \n";
        tbl += " ID_Prod varchar(20) NOT NULL ,\n";
        tbl += " Cantidad numeric(12, 6) NOT NULL ,\n";
        tbl += " MasMenos numeric(12, 6) NOT NULL ,\n";
        tbl += " Principal bit NOT NULL \n";
        tbl += ");\n\n";

        for(int p = 0; p < rec.getPartidas().size(); p++)
        {
        	String id_subprod = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + rec.getPartida(p).getID_Prod() + "'");
        	String porcentaje = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getPorcentaje()) + "'");
        	String cantidad = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getCantidad()) + "'");
        	String masmenos = (rec.getPartida(p).getID_Prod().equals("") ? "null" : "'" + Float.toString(rec.getPartida(p).getMasMenos()) + "'");
        	
        	tbl += "insert into _TMP_PRODUCCION_FORMULAS_PROCESOS\n";
        	tbl += "values(" + p + ",'" + p(rec.getPartida(p).getNombre()) + "','" + rec.getPartida(p).getTiempo() + "'," + id_subprod + "," + porcentaje + "," + cantidad + "," + masmenos + ");\n";
        	for(int i = 0; i < rec.getPartida(p).getPartidas().size(); i++)
        	{
        		tbl += "insert into _TMP_PRODUCCION_FORMULAS_DETPROD\n";
        		tbl += "values('" + p + "','" + p(rec.getPartida(p).getPartida(i).getID_Prod()) + "','" + rec.getPartida(p).getPartida(i).getCantidadXU() + "','" + rec.getPartida(p).getPartida(i).getMasMenosXU() +  "','" + (rec.getPartida(p).getPartida(i).getPrincipal() ? "1" : "0") + "');\n";
           	}
        }
        JRetFuncBas rfb = new JRetFuncBas();
      			
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_PRODUCCION_FORMULAS_DETPROD; DROP TABLE _TMP_PRODUCCION_FORMULAS_PROCESOS", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_FORMULAS_AGREGAR", "PFRM|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/produccion/prod_formulas_dlg.jsp", request, response);

			
    }
      
    public void Aplicar(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
    	String str = "select * from sp_prod_formulas_principal('" + p(request.getParameter("ID")) + "') as (err integer, res varchar, clave integer)";
	      
    	JRetFuncBas rfb = new JRetFuncBas();
			
        doCallStoredProcedure(request, response, str, rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "PROD_FORMULAS_CAMBIAR", "PFRM|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

				
	} 
    
}
