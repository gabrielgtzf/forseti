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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JAlmacenesMovimSetV2;
import forseti.sets.JAlmacenesMovimSetDetallesV2;
import forseti.sets.JPublicInvServInvCatalogSetV2;

@SuppressWarnings("serial")
public class JAlmUtensiliosDlg extends JForsetiApl
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

      String alm_utensilios_dlg = "";
      request.setAttribute("alm_utensilios_dlg",alm_utensilios_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	//revisa por las entidades
        JAlmacenesMovimSetIdsV2 setids;
        setids = new JAlmacenesMovimSetIdsV2(request,usuario,getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial(),"G");
        setids.Open();  
    	
        if(setids.getNumRows() < 1)
        {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        } 
     
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if((!request.getParameter("proceso").equals("ENTRADA_UTENSILIOS") && 
        		!request.getParameter("proceso").equals("SALIDA_UTENSILIOS")) && request.getParameter("ID") != null)
        {
        	JAlmacenesMovimSetV2 set = new JAlmacenesMovimSetV2(request);
        	set.m_Where = "ID_Bodega = '" + setids.getAbsRow(0).getID_Bodega() + "' and ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID_Bodega() + "||",mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
        }
	  	  	
        if(request.getParameter("proceso").equals("ENTRADA_UTENSILIOS") || request.getParameter("proceso").equals("SALIDA_UTENSILIOS"))
        {
          // Revisa si tiene permisos
          if(request.getParameter("proceso").equals("ENTRADA_UTENSILIOS"))
          {
        	  if(!getSesion(request).getPermiso("ALM_UTENSILIOS_ENTRADA"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS_ENTRADA");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS_ENTRADA","UALM||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }
          else // es salida
          {
        	  if(!getSesion(request).getPermiso("ALM_UTENSILIOS_SALIDA"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS_SALIDA");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS_SALIDA","UALM||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
          }

          if(request.getParameter("subproceso") == null) // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  if(getSesion(request).getSesion("ALM_UTENSILIOS").getVista().equals("MOVIMIENTOS"))
        	  {
	            HttpSession ses = request.getSession(true);
	            JAlmMovimientosSes rec;
	            
	            rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
	            if(rec == null)
	            {
	            	rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            	rec.setID_Clave((short)(request.getParameter("proceso").equals("ENTRADA_UTENSILIOS") ? -1 : -2));
	            	ses.setAttribute("alm_utensilios_dlg", rec);
	            }
	            else
	            {
	            	rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
	            	rec.setID_Clave((short)(request.getParameter("proceso").equals("ENTRADA_UTENSILIOS") ? -1 : -2));
	            }   	
	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
	            return;
        	  }
        	  else
        	  {
        		  if(request.getParameter("ID") == null)
        		  {
        			  idmensaje = 3; mensaje += JUtil.Msj("CEF", "ALM_UTENSILIOS", "DLG", "MSJ-PROCERR", 1); //ERROR: En salidas de utensilios rápidas, es necesario seleccionar de la lista el producto que se va a sacar del almacén.
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                      irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                      return;
        		  }
        		
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/almacen/alm_utensilios_dlgsal.jsp", request, response);
        		  return; 
        	  }
          }
          else
          {
	       	  // Solicitud de envio a procesar
        	  if(getSesion(request).getSesion("ALM_UTENSILIOS").getVista().equals("MOVIMIENTOS"))
        	  {
		       	  if(request.getParameter("subproceso").equals("ENVIAR"))
		       	  {
		       		  if(AgregarCabecero(request,response) == -1)
	     			  {
		       			  if(VerificarParametros(request, response))
		       			  {
		       				  Agregar(request, response, (request.getParameter("proceso").equals("ENTRADA_UTENSILIOS") ? "ENTRADA" : "SALIDA"));
		       				  return;
		       			  }
	     			  }
		       		  
	       			  irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);  
	       			  return;
		   		  }
	        	  else if(request.getParameter("subproceso").equals("AGR_PART"))
	        	  {
	        		  if(AgregarCabecero(request,response) == -1)
	     			  {
	        			  if(VerificarParametrosPartida(request, response))
	        				  AgregarPartida(request, response);
	     			  }
	        		  
	       			  irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);  
	       			  return;
		   		  }
		   		  else if(request.getParameter("subproceso").equals("EDIT_PART"))
		   		  {
		   			  if(AgregarCabecero(request,response) == -1)
		   			  {
		   				  if(VerificarParametrosPartida(request, response))
		   					  EditarPartida(request, response);
		   			  }
		   			  
	       			  irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);  
	       			  return;
		   		  }
		   		  else if(request.getParameter("subproceso").equals("BORR_PART"))
		   		  {
		   			  if(AgregarCabecero(request,response) == -1)
		   			  {
		   				  BorrarPartida(request, response);
		   			  }
		   			  
	       			  irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);  
	 	   			  return;
		   		  }	
        	  }
        	  else // Es vista de existencias
        	  {
        		  if(request.getParameter("subproceso").equals("ENVIAR"))
		       	  {
		       		  if(VerificarParametrosSal(request, response))
		       		  {
		       			  AgregarSal(request, response);
		       			  return;
		       		  }
	     			  	       		  
	       			  irApag("/forsetiweb/almacen/alm_utensilios_dlgsal.jsp", request, response);  
	       			  return;
		   		  }
        	  }
	      }
	
        }
        //////////////////
        //////////////////
        else if(request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO"))
        {
            // Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("ALM_UTENSILIOS"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
                        
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  HttpSession ses = request.getSession(true);
            	  JAlmMovimientosSes rec;
            
            	  rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
            	  if(rec == null)
            	  {
            		  rec = new JAlmMovimientosSes(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
            		  ses.setAttribute("alm_utensilios_dlg", rec);
            	  }
            	  else
            		  rec.resetear(setids.getAbsRow(0).getID_Bodega(), setids.getAbsRow(0).getNumero(), setids.getAbsRow(0).getAuditarAlm(), (byte)1 );
                   
            	  // Llena la consulta
            	  JAlmacenesMovimSetV2 SetCab = new JAlmacenesMovimSetV2(request);
            	  JAlmacenesMovimSetDetallesV2 SetDet = new JAlmacenesMovimSetDetallesV2(request);
            	  SetCab.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	  SetDet.m_Where = "ID_Movimiento = '" + p(request.getParameter("ID")) + "'";
            	  SetDet.m_OrderBy = "ID_Costo ASC";
            	  SetCab.Open();
            	  SetDet.Open();
            	  	  
            	  rec.setID_Clave((short)-1); 
            	  rec.setNumero(SetCab.getAbsRow(0).getNum());
            	  rec.setFecha(SetCab.getAbsRow(0).getFecha());
            	  rec.setRef(SetCab.getAbsRow(0).getReferencia());
            	  rec.setClave_Descripcion(SetCab.getAbsRow(0).getDescripcion());
            	  rec.setConcepto(SetCab.getAbsRow(0).getConcepto());
            	  rec.setTipoMov((byte)1);
            	  rec.setRecalcularCosto(false);
            	  
            	  for(int i = 0; i< SetDet.getNumRows(); i++)
            	  {
            		  rec.agregaPartida( (SetDet.getAbsRow(i).getEntrada() - SetDet.getAbsRow(i).getSalida() ), SetDet.getAbsRow(i).getUnidad(), SetDet.getAbsRow(i).getID_Prod(), SetDet.getAbsRow(i).getDescripcion(), SetDet.getAbsRow(i).getUC());
            	  }
            		  
            	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "||","");
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
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
        	if(!getSesion(request).getPermiso("ALM_UTENSILIOS_CANCELAR"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS_CANCELAR");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS_CANCELAR","UALM||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
                                    
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
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
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
        	if(!getSesion(request).getPermiso("ALM_UTENSILIOS"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ALM_UTENSILIOS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ALM_UTENSILIOS","UALM||||",mensaje);
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
        				String SQLCab, SQLDet;
                
        				SQLCab = "select * FROM view_invserv_almacen_movim_impcab where ID_Movimiento = " + request.getParameter("ID");
        				SQLDet = "select * FROM view_invserv_almacen_movim_impdet where ID_Movimiento = " + request.getParameter("ID") + " order by ID_Costo asc";
                                
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
        				request.setAttribute("impresion", "CEFAlmUtensiliosDlg");
        				request.setAttribute("tipo_imp", "ALM_UTENSILIOS");
        				request.setAttribute("formato_default", setids.getAbsRow(0).getFmt_Movimientos());
                 
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
      if(request.getParameter("cantidad") != null && request.getParameter("idprod") != null &&
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
    	JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
       	
        if(rec.getPartidas().size() == 0)
        {
 	        idmensaje = 1; mensaje.append(JUtil.Msj("GLB","GLB","DLG","CERO-PART",2));//"PRECAUCION: El movimiento no contiene partidas <br>");
  	        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   	        return false;
        }
            
        return true;
	
    }

    public boolean VerificarParametrosSal(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("cantidad") != null && request.getParameter("ID") != null && request.getParameter("concepto") != null &&
    			!request.getParameter("cantidad").equals("") && !request.getParameter("ID").equals("") )
    	{
    		JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
    		set.m_Where = "Clave = '" + JUtil.p(request.getParameter("ID")) + "' and ID_Tipo = 'G' and Status = 'V' and NoSeVende = '1'";
  		  	set.Open();
  			
  		  	if( set.getNumRows() < 1 )
  		  	{
  		  		idmensaje = 3; mensaje = JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",5);//"ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio");
  		  		return false;
  		  	}
  		  
    		return true;
    	}
    	else
    	{
    		idmensaje = 1; mensaje = JUtil.Msj("CEF", "ALM_MOVIM", "DLG", "MSJ-PROCERR2",4); //"PRECAUCION: Por lo menos se deben enviar los parámetros de cantidad y clave del producto <br>";
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
	}
    
    public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	              
    	HttpSession ses = request.getSession(true);
    	JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
    	       	
    	idmensaje = rec.agregaCabecero(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
    }
    
    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
    	JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
       	
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.agregaPartida(request, cantidad, request.getParameter("idprod"), "", mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
 
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
       	
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
 
        idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, cantidad, request.getParameter("idprod"), "", mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
  
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
       	
        rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        //irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
 
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response, String tipomov)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JAlmMovimientosSes rec = (JAlmMovimientosSes)ses.getAttribute("alm_utensilios_dlg");
        String proceso, tipo;
    	
        if(tipomov.equals("ENTRADA"))
        {
        	tipo = "-1";
        	proceso = "ALM_UTENSILIOS_ENTRADA";
        }
        else
        {
        	tipo = "-2";
        	proceso = "ALM_UTENSILIOS_SALIDA";
        }
          
        String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (\n";
        tbl += "ID_Bodega smallint NOT NULL ,\n";
        tbl += "ID_Prod varchar(20) NOT NULL ,\n";
        tbl += "Partida smallint NOT NULL ,\n";
        tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
        tbl += "Costo numeric(19,4) NULL \n";
        tbl += "); \n";
        
        for(int i = 0; i < rec.getPartidas().size(); i++)
        {
        	tbl += "\ninsert into _TMP_INVSERV_ALMACEN_MOVIM_DET\n";
        	tbl += "values('" + rec.getID_Bodega() + "','" + p(rec.getPartida(i).getID_Prod()) + "','" + (i+1) + "','" + rec.getPartida(i).getCantidad() + "','0.0'); ";
        }
        
        String str = "select * from sp_invserv_alm_utensilios_agregar('" + JUtil.obtFechaSQL(rec.getFecha()) + "','" + rec.getID_Bodega() + "','G','" + tipo + "','" +
        	p(rec.getConcepto()) + "','" + p(rec.getRef())+ "','" + tipomov + "','', null, null) as ( err integer, res varchar, clave integer );";
        
        JRetFuncBas rfb = new JRetFuncBas();
    		
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET ", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), proceso, "UALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/almacen/alm_utensilios_dlg.jsp", request, response);
        
    }
    
    public void CancelarMovimiento(HttpServletRequest request, HttpServletResponse response, String tipomov)
    	throws ServletException, IOException
    {
    	String str = "select * from sp_invserv_alm_utensilios_cancelar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer );";
  
   	 	JRetFuncBas rfb = new JRetFuncBas();
	
   	 	doCallStoredProcedure(request, response, str, rfb);

   	 	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_UTENSILIOS_CANCELAR", "UALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "||",rfb.getRes());
   	 	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	}
    
    public void AgregarSal(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	float cantidad = Float.parseFloat(request.getParameter("cantidad"));
    	Calendar fecha = GregorianCalendar.getInstance();
    	          
    	String tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET (\n";
    	tbl += "ID_Bodega smallint NOT NULL ,\n";
    	tbl += "ID_Prod varchar(20) NOT NULL ,\n";
    	tbl += "Partida smallint NOT NULL ,\n";
    	tbl += "Cantidad numeric(9, 3) NOT NULL ,\n";
    	tbl += "Costo numeric(19,4) NULL \n";
    	tbl += "); \n";
    	        
    	tbl += "\ninsert into _TMP_INVSERV_ALMACEN_MOVIM_DET\n";
    	tbl += "values('" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "','" + p(request.getParameter("ID")) + "','1','" + cantidad + "','0.0'); ";
    	        
    	String str = "select * from sp_invserv_alm_utensilios_agregar('" + JUtil.obtFechaSQL(fecha) + "','" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "','G','-2','" +
    	        	p(request.getParameter("concepto")) + "','','SALIDA','', null, null) as ( err integer, res varchar, clave integer );";
    	        
    	JRetFuncBas rfb = new JRetFuncBas();
    	    		
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_ALMACEN_MOVIM_DET ", rfb);
    	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ALM_UTENSILIOS_SALIDA", "UALM|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("ALM_UTENSILIOS").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/almacen/alm_utensilios_dlgsal.jsp", request, response);
    	        
    }
}
