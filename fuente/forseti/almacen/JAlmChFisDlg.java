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
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JAlmacenesBodegasSet;
import forseti.sets.JAlmacenesCHFISSet;
import forseti.sets.JAlmChFisDetSet;

@SuppressWarnings("serial")
public class JAlmChFisDlg extends JForsetiApl
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

      String alm_chfis_dlg = "";
      request.setAttribute("alm_chfis_dlg",alm_chfis_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
        JAlmacenesMovimSetIdsV2 setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_CHFIS").getEspecial(),"P");
        setids.Open();
        
        if(setids.getNumRows() < 1)
        {
        	 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS");
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "ALM_CHFIS", "CHFI" + "||||",mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
        }
        
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_CHFIS") && request.getParameter("ID") != null)
        {
        	JAlmacenesCHFISSet set = new JAlmacenesCHFISSet(request);
        	set.m_Where = "ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' and ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_CHFIS","CHFI|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_CHFIS"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ALM_CHFIS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_AGREGAR","CHFI||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  HttpSession ses = request.getSession(true);
              JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
              if (rec == null) 
              {
                rec = new JAlmChFisSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());
                ses.setAttribute("alm_chfis_dlg", rec);
              }
              else
                rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());

              JAdmVariablesSet var = new JAdmVariablesSet(request);
              var.m_Where = "ID_Variable = 'CHFIS_RET'";
              var.Open();
              
              JAlmacenesBodegasSet setex = new JAlmacenesBodegasSet(request);
              String sql = "select * from view_invserv_bodegas_modulo('" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "','" + var.getAbsRow(0).getVEntero() + "') as ( clave varchar, descripcion varchar, id_bodega smallint, bodega varchar, existencia numeric, unidad varchar, stockmin numeric, stockmax numeric, status char)";
              setex.setSQL(sql);
              setex.Open();

              for (int i = 0; i < setex.getNumRows(); i++)
                rec.agregar(setex.getAbsRow(i).getClave(), 
                		setex.getAbsRow(i).getDescripcion(), setex.getAbsRow(i).getExistencia(), 0.0F, setex.getAbsRow(i).getUnidad(), 
                			((setex.getAbsRow(i).getStockMin() == -1 && setex.getAbsRow(i).getStockMax() == -1) ? false : true) );


              getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
        	  return;
          }
          else
          {

	       	  // Solicitud de envio a procesar
	       	  if(request.getParameter("subproceso").equals("ENVIAR"))
	       	  {

       			  if(VerificarParametros(request, response))
       			  {
       				  Agregar(request, response);
       				  return;
       			  }
       			  
       			  irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
       			  return;
	   		  }
	       	 	   	  
	      }
	
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_CHFIS"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ALM_CHFIS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS","CHFI||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          if(request.getParameter("ID") != null)
          {
             
        	String[] valoresParam = request.getParameterValues("ID");
        	if(valoresParam.length == 1)
            {
        		HttpSession ses = request.getSession(true);
        		JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
        		if (rec == null) 
        		{
        			rec = new JAlmChFisSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());
        			ses.setAttribute("alm_chfis_dlg", rec);
        		}
        		else
        			rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());
	
        		JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
        		SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
        		SetMod.Open();
                	
        		rec.setFecha(SetMod.getAbsRow(0).getFecha());
                	
        		JAlmChFisDetSet setex = new JAlmChFisDetSet(request);
        		setex.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
        		setex.m_OrderBy = "ID_Prod ASC";
        		setex.Open();
        		
        		for (int i = 0; i < setex.getNumRows(); i++)
        			rec.agregar(setex.getAbsRow(i).getID_Prod(), 
        					setex.getAbsRow(i).getDescripcion(), setex.getAbsRow(i).getCantidad(), setex.getAbsRow(i).getDiff(), setex.getAbsRow(i).getUnidad(), 
        					((setex.getAbsRow(i).getStockMin() == -1 && setex.getAbsRow(i).getStockMax() == -1) ? false : true) );

        		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_CHFIS","CHFI|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||","");
                
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
                return;	
            }
        	else
        	{
        		idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite consultar un chequeo a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
             
          }
          else
          {
        	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere consultar <br>";
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
        	
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_CHFIS"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ALM_CHFIS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_CAMBIAR","CHFI||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          if(request.getParameter("subproceso") == null) 
          {
             
        	 if(request.getParameter("ID") != null)
             {
             
        		String[] valoresParam = request.getParameterValues("ID");
                if(valoresParam.length == 1)
                {
                	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
    	          	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
    	          	SetMod.Open();
    	      	  
    	        	if(SetMod.getAbsRow(0).getCerrado() || SetMod.getAbsRow(0).getGenerado())
    	        	{
    	                idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR",1);//"ERROR: No se puede cambiar el chequeo porque ya esta cerrado o generado <br>";
    	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    	                return;
    	            }
    	        	
                	HttpSession ses = request.getSession(true);
                	JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
                	if (rec == null) 
                	{
                		rec = new JAlmChFisSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());
	                    ses.setAttribute("alm_chfis_dlg", rec);
                	}
                	else
                		rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNombre());
	
                	
                	rec.setFecha(SetMod.getAbsRow(0).getFecha());
                	
                	JAlmChFisDetSet setex = new JAlmChFisDetSet(request);
                	setex.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
                	setex.m_OrderBy = "ID_Prod ASC";
                	setex.Open();
	
                	for (int i = 0; i < setex.getNumRows(); i++)
                		rec.agregar(setex.getAbsRow(i).getID_Prod(), 
	                    		setex.getAbsRow(i).getDescripcion(), setex.getAbsRow(i).getCantidad(), setex.getAbsRow(i).getDiff(), setex.getAbsRow(i).getUnidad(), 
	                    			((setex.getAbsRow(i).getStockMin() == -1 && setex.getAbsRow(i).getStockMax() == -1) ? false : true) );

                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
                	return;
                }
                else
                {
                	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite cambiar un chequeo a la vez <br>";
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                	return;
                }
             
             }
             else
             {
                 idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere cambiar <br>";
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

       			  if(VerificarParametros(request, response))
       			  {
       				  Cambiar(request, response);
       				  return;
       			  }
       			  
       			  irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
       			  return;
	   		  }
      	 	   	  
	      }
	
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_CHFIS"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("ALM_CHFIS_CANCELAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_CANCELAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_CANCELAR","CHFI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            		
              	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
              	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
              	SetMod.Open();
          	  
            	if(SetMod.getAbsRow(0).getGenerado())
            	{
                    idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR",2);//"ERROR: No se puede eliminar el chequeo porque ya esta generado, debes cancelarlo <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            	else
            	{
            		Eliminar(request, response);
            		return;
            	}
              }
              else
              {
                idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite eliminar un chequeo a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere eliminar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        }
        else if(request.getParameter("proceso").equals("CANCELAR_CHFIS"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("ALM_CHFIS_CANCELAR"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_CANCELAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_CANCELAR","CHFI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            		
              	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
              	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
              	SetMod.Open();
          	  
            	if(!SetMod.getAbsRow(0).getGenerado())
            	{
                    idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR",3);//"ERROR: No se puede cancelar el chequeo porque aun no está generado. Si ya no lo necesitas, elimínalo <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            	else
            	{
            		Cancelar(request, response);
            		return;
            	}
              }
              else
              {
                idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite cancelar un chequeo a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere eliminar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        } 
        else if(request.getParameter("proceso").equals("CALCULAR_CHFIS"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_CHFIS_AGREGAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_AGREGAR","CHFI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            		
              	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
              	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
              	SetMod.Open();
          	  
            	if(SetMod.getAbsRow(0).getGenerado())
            	{
                    idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR",4);//"ERROR: No se puede calcular el chequeo porque ya está generado <br>";
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
                }
            	else
            	{
            		Calcular(request, response);
            		return;
            	}
              }
              else
              {
                idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite calcular un chequeo a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere calcular <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
           
        } 
        else if(request.getParameter("proceso").equals("CERRAR_CHFIS"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_CHFIS_CAMBIAR"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_CAMBIAR");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_CAMBIAR","CHFI||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;                	
        	}

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
	         	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
	          	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
	          	SetMod.Open();
	      	  
	        	if(SetMod.getAbsRow(0).getGenerado())
	        	{
	                idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR",5);//"ERROR: No se puede cerrar o abrir el chequeo porque ya está generado <br>";
	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                return;
	            }
	        	else
	        	{
	        		Cerrar(request, response);
	        		return;
	        	}
              }
              else
              {
                idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite cerrar un chequeo a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere cerrar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
         	  
        }
        else if(request.getParameter("proceso").equals("GENERAR_CHFIS"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_CHFIS_GENPROC"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_GENPROC");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_GENPROC","CHFI||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;      
        	}

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
	         	JAlmacenesCHFISSet SetMod = new JAlmacenesCHFISSet(request);
	          	SetMod.m_Where = "ID_CHFIS = '" + p(request.getParameter("ID")) + "'";
	          	SetMod.Open();
	      	  
	        	if(!SetMod.getAbsRow(0).getCerrado() || SetMod.getAbsRow(0).getGenerado())
	        	{
	                idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_CHFIS", "DLG", "MSJ-PROCERR2",1);//"ERROR: No se puede generar el chequeo porque aun no esta cerrado, o ya esta generado <br>";
	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	                return;
	            }
	        	else
	        	{
	        		Generar(request, response);
	        		return;
	        	}
              }
              else
              {
                idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2);//"PRECAUCION: Solo se permite generar un chequeo a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
              }
            }
            else
            {
               idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1);//" ERROR: Se debe enviar el identificador del chequeo que se quiere generar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
               return;
            }
        	  
        	
        }
        else if(request.getParameter("proceso").equals("RASTREAR_CHFIS"))
        {
        	if(!getSesion(request).getPermiso("ALM_CHFIS_CONSULTAR"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS_CONSULTAR");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS_CONSULTAR","CHFI||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
      	  	
            if(request.getParameter("ID") != null)
            {
            	String[] valoresParam = request.getParameterValues("ID");
            	if (valoresParam.length == 1)
            	{
              	    JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("ALM_CHFIS").generarTitulo(JUtil.Msj("CEF","ALM_CHFIS","VISTA","CONSULTAR_CHFIS",3)),
                  								"CHFI",request.getParameter("ID"));
              	    String rastreo_imp = "true";
              	    request.setAttribute("rastreo_imp", rastreo_imp);
              	    // Ahora pone los atributos para el jsp
              	    request.setAttribute("rastreo", rastreo);
              	    RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_CHFIS_CONSULTAR","CHFI|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||","");
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
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ALM_CHFIS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_CHFIS");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_CHFIS","CHFI||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
        
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if (valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("IMPRESION"))
              {
                StringBuffer bsmensaje = new StringBuffer(254);
                String SQLCab = "select * from view_invserv_chfis_impcab where ID_CHFIS = " + request.getParameter("ID");
                String SQLDet = "select * from view_invserv_chfis_impdet where ID_CHFIS = " + request.getParameter("ID") + " order by ID_Prod ASC";
                  
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
            	  request.setAttribute("impresion", "CEFAlmChFisDlg");
            	  request.setAttribute("tipo_imp", "ALM_CHFIS");
            	  
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

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        if(request.getParameter("fecha") != null && !request.getParameter("fecha").equals(""))
        {
        	HttpSession ses = request.getSession(true);
            JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
            rec.setFecha(JUtil.estFecha(request.getParameter("fecha")));
            
            // Ahora verifica las existencias
            
            String clave = "";
            boolean flag = true;
            for(int i = 0; i< rec.numPartidas(); i++)
            {
            	clave = rec.getPartida(i).getID_Prod();
            	if(request.getParameter("FSI_CANT_" + clave) == null)
            		continue;
            	
            	try 
            	{
            		float cantidad = Float.parseFloat(request.getParameter("FSI_CANT_" + clave));
            		
            		if( cantidad < 0.0 )
            		{
            			flag = false;
            			break;
            		}
            		
            		rec.getPartida(i).setCantidad(cantidad);
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
                mensaje = "PRECAUCION: La cantidad del producto " + clave + " no está correcta ó es menor que cero. <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                return false;
            }
     
        	return true;
        }
        else
        {
            idmensaje = 3; mensaje = "ERROR: Se necesita la fecha para este chequeo físico ";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
        }
	
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
         
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_EXISTENCIAS (\n";
        tbl += "ID_Prod varchar(20) NOT NULL , \n";
        tbl += "Existencia decimal(9, 3) NOT NULL \n";
        tbl += ");  \n";
        
        for(int i = 0; i< rec.numPartidas(); i++)
        {
            tbl += "INSERT INTO _TMP_INVSERV_EXISTENCIAS \n";
            tbl += "VALUES('" + p(rec.getPartida(i).getID_Prod()) + "','" + p(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod())) + "' ); \n";
        }
        
        String str = "select * from sp_invserv_alm_chfis_agregar('" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "') as ( err integer, res varchar, clave integer );";
     
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_EXISTENCIAS ", rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_AGREGAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
        
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JAlmChFisSes rec = (JAlmChFisSes) ses.getAttribute("alm_chfis_dlg");
         
        String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_EXISTENCIAS (\n";
        tbl += "ID_Prod varchar(20) NOT NULL , \n";
        tbl += "Existencia decimal(9, 3) NOT NULL \n";
        tbl += ");  \n";
        
        for(int i = 0; i< rec.numPartidas(); i++)
        {
            tbl += "INSERT INTO _TMP_INVSERV_EXISTENCIAS \n";
            tbl += "VALUES('" + p(rec.getPartida(i).getID_Prod()) + "','" + p(request.getParameter("FSI_CANT_" + rec.getPartida(i).getID_Prod())) + "' ); \n";
        }
        
        String str = "select * from sp_invserv_alm_chfis_cambiar('" + p(request.getParameter("ID")) + "','" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "') as ( err integer, res varchar, clave integer );";
     
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_EXISTENCIAS ", rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_CAMBIAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/almacen/alm_chfis_dlg.jsp", request, response);
              
    }
    
    public void Calcular(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String str = "select * from sp_invserv_alm_chfis_diferencias('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
  
    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_AGREGAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        
	
	}
    
    public void Cancelar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	String str = "select * from sp_invserv_alm_chfis_cancelar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
    	  
    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_CANCELAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    
	
	}
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	String str = "select * from sp_invserv_alm_chfis_eliminar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
  	  
    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_CANCELAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	}
 
    public void Cerrar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	
    	String str = "select * from sp_invserv_alm_chfis_cerrar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
    	  
    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_CAMBIAR", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	}    
 
    public void Generar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String str = "select * from sp_invserv_alm_chfis_generar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
    	  
    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_CHFIS_GENPROC", "CHFI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_CHFIS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	
	}    
    
}
