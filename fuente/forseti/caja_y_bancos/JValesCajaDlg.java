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
package forseti.caja_y_bancos;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
//import forseti.sets.JBancosIdsSet;
import forseti.sets.JBancosSetIdsV2;
import forseti.sets.JCajasValesSetV2;
import forseti.sets.JCajasVsGenGastoSet;
import forseti.JUtil;
import forseti.sets.JPublicInvServGasCatalogSetV2;

@SuppressWarnings("serial")
public class JValesCajaDlg extends JForsetiApl
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

      String vales_caja_dlg = "";
      request.setAttribute("vales_caja_dlg",vales_caja_dlg);

      String mensaje = ""; short idmensaje = -1;
      
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  // revisa por las entidades
    	  JBancosSetIdsV2 setids = new JBancosSetIdsV2(request,getSesion(request).getID_Usuario(),"1",getSesion(request).getSesion("BANCAJ_VALES").getEspecial());
          //setids.m_Where = "ID_Usuario = '" + getSesion(request).getID_Usuario() + "' and Tipo = 1 and ID = " + getSesion(request).getSesion("BANCAJ_VALES").getEspecial(); 
          setids.Open();  
      	
          if(setids.getNumRows() < 1)
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES","VCAJ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }  
      	
          // Revisa por intento de intrusion (Salto de permiso de entidad)
          if(!request.getParameter("proceso").equals("AGREGAR_VALE") && request.getParameter("ID") != null)
          {
        	JCajasValesSetV2 set = new JCajasValesSetV2(request);
        	set.m_Where = "ID_Clave = " + setids.getAbsRow(0).getID() + " and ID_Vale = " + request.getParameter("ID");
          	set.Open();
          	if(set.getNumRows() < 1)
          	{
          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES");
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"BANCAJ_VALES","VCAJ|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "|" + setids.getAbsRow(0).getID() + "||",mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
          	}
          }
          
          if(request.getParameter("proceso").equals("AGREGAR_VALE"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("BANCAJ_VALES_AGREGAR"))
              {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_AGREGAR");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_AGREGAR","VCAJ||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
              }

        	  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
        		  if(VerificarParametros(request, response))
        		  {
        			  AgregarCambiar(request, response, "AGREGAR");
        			  return;
        		  }
        		  
        		  irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg.jsp", request, response);
        		  return;
              }
        	  else
        	  {
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg.jsp", request, response);
        		  return;
          	  }
	
          }
          else if(request.getParameter("proceso").equals("ELIMINAR_VALE"))
          {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("BANCAJ_VALES_ELIMINAR"))
            {
          	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_ELIMINAR");
          	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_ELIMINAR","VCAJ||||",mensaje);
          	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	  return;
            }
            
            // Solicitud de envio a procesar
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  JCajasValesSetV2 set = new JCajasValesSetV2(request);
            	  set.m_Where = "ID_Vale = " + request.getParameter("ID");
            	  set.Open();
            	
            	  // Revisa si existe aun el vale
            	  if(set.getNumRows() == 0)
            	  {
                	  idmensaje = 3; mensaje += JUtil.Msj("CEF", "BANCAJ_VALES", "DLG", "MSJ-PROCERR", 1); //"ERROR: Este vale no existe o ya se habia eliminado previamente";
                	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                	  return;
            	  }
            	  
            	  Eliminar(request,response);
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
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }

          }
          else if(request.getParameter("proceso").equals("CAMBIAR_VALE"))
          {
        	  if(!getSesion(request).getPermiso("BANCAJ_VALES_CAMBIAR"))
              {
            	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_CAMBIAR");
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_CAMBIAR","VCAJ||||",mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
              
        	  if(request.getParameter("ID") != null)
        	  {
        		  String[] valoresParam = request.getParameterValues("ID");
        		  if(valoresParam.length == 1)
        		  {
        			  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
                      {
                        // Verificacion
                        if(VerificarParametros(request, response))
                        {
                          AgregarCambiar(request, response, "CAMBIAR");
                          return;
                        }
                        irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg.jsp", request, response);
                        return;
                      }
                      else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
                      {
                        getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg.jsp", request, response);
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
        		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }
        }
      	else if(request.getParameter("proceso").equals("TRASPASAR_VALE"))
      	{
        	if(!getSesion(request).getPermiso("BANCAJ_VALES_TRASPASAR"))
            {
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_TRASPASAR");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_TRASPASAR","VCAJ||||",mensaje);
          	  	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	  	return;
            }

            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
	       	{
            	if(VerificarParametrosTrasp(request, response))
            	{
            		Traspasar(request, response);
            		return;
            	}
            	
            	irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_trasp.jsp", request, response);
      	  		return;
            }
      	  	else
      	  	{
      	  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      	  		irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_trasp.jsp", request, response);
      	  		return;
      	  	}
      	}
        else if(request.getParameter("proceso").equals("PROTEGER_CAJA"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("BANCAJ_VALES_GENPROC"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_GENPROC");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_GENPROC","VCAJ||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        	
        	Proteger(request,response);
    		return;
        }
        else if(request.getParameter("proceso").equals("GENERAR_GASTO"))
        {
        	// Revisa si tiene permisos
        	if(!getSesion(request).getPermiso("BANCAJ_VALES_GENPROC"))
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES_GENPROC");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES_GENPROC","VCAJ||||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        	   
        	// Solicitud de envio a procesar
        	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        	{
        		if(VerificarParametrosGenGasto(request, response))
        		{
        			GenerarGasto(request, response);
        			return;
        		}
        		  
        		irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_gengas.jsp", request, response);
        		return;
        		  
        	}
        	else // Como el subproceso no es ENVIAR abre la ventana del proceso de Cierre `por primera vez
        	{
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_gengas.jsp", request, response);
        		return;
        	}
        	
        }
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("BANCAJ_VALES"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_VALES");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_VALES","VCAJ||||",mensaje);
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
                  String SQLCab = "select * from view_cajas_vales_impcab where ID_Vale = " + request.getParameter("ID");
                  String SQLDet = "select * FROM view_cont_polizas_impdet where ID = -1"; //Solo para complementar y que no falle la impresion del cabecero
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
                   request.setAttribute("impresion", "CEFValesCajaDlg");
                   request.setAttribute("tipo_imp", "BANCAJ_VALES");
                   request.setAttribute("formato_default", null);
                   
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
    	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
      }

    }

    public boolean VerificarParametrosGenGasto(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
        if(request.getParameter("fecha") != null && request.getParameter("identidad") != null && request.getParameter("referencia") != null && 
        	!request.getParameter("fecha").equals("") && !request.getParameter("identidad").equals("") )
        {
        	if(request.getParameter("identidad").equals("ENTIDAD"))
        	{
        		idmensaje = 1; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR2",3); //PRECAUCION: Debes seleccionar la entidad de gasto para la generación de vales.
    	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	        return false;
        	}
            
        	JCajasVsGenGastoSet set = new JCajasVsGenGastoSet(request);
        	set.m_Where = "ID_EntidadCompra = " + request.getParameter("identidad") + " and Clave = " + JUtil.getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + " and Enlazado = 1";
        	set.Open();
        	
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR2",4); //PRECAUCION: Debes seleccionar la entidad de gasto para la generación de vales.
    	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	        return false;
        	}
        	
        	return true;
        }
        else
        {
            idmensaje = 3; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO",1); //"ERROR: Alguno de los parametros necesarios es Nulo. Verifícalos e intenta de nuevo";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
        }	    
	    
	}    

    public boolean VerificarParametrosTrasp(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("bancaj") == null || request.getParameter("bancaj").equals("")
	               || request.getParameter("bancaj").equals("CAJAS"))
	    {
	        idmensaje = 3; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR",2); //"ERROR: Debe especificarse la caja de destino para este traspaso de vales<br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	    else // verifica si estan bien las cajas
	    {
	    	if(request.getParameter("FSI_F") == null && request.getParameter("FSI_A") == null &&
	    			request.getParameter("FSI_G") == null && request.getParameter("FSI_C") == null &&
	    			request.getParameter("FSI_T") == null)
	    	{
		        idmensaje = 3; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR",3);  //"ERROR: Debes seleccionar por lo menos un tipo de vales a enviar<br>";
		        getSesion(request).setID_Mensaje(idmensaje, mensaje);
		        return false;
	    	}
	    	
	        String bancaj = request.getParameter("bancaj").substring(8);
	        if(request.getParameter("bancaj").indexOf("FSI_CAJ_") != -1 &&
	           bancaj.equals(getSesion(request).getSesion("BANCAJ_VALES").getEspecial()) )
	        {
	            idmensaje = 3; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR",4); //"ERROR: No se puede aplicar el traspaso de vales a si mismo <br>";
	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
	            return false;
	        }
	    }
	    
	    return true;
	}    
    
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        if(request.getParameter("idgasto") != null && request.getParameter("total") != null && request.getParameter("concepto") != null &&
        	request.getParameter("fecha") != null && request.getParameter("idtipo") != null && 
        	!request.getParameter("idgasto").equals("") && !request.getParameter("total").equals("") &&	!request.getParameter("concepto").equals("") && 
        	!request.getParameter("fecha").equals("") && !request.getParameter("idtipo").equals(""))
        {
           	JPublicInvServGasCatalogSetV2 set = new JPublicInvServGasCatalogSetV2(request);
        	set.m_Where = "Clave = '" + request.getParameter("idgasto") + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{           		
        		idmensaje = 1; mensaje = JUtil.Msj("CEF","BANCAJ_VALES","DLG","MSJ-PROCERR",5); //"ERROR: No existe el gasto especificado.";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		return false;
        	}
        }
        else
        {
            idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"); //"ERROR: Alguno de los parametros necesarios es Nulo. Verifícalos e intenta de nuevo";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
        }

                     
        return true;
	
    }

    public void Traspasar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
    	int FSI_F = ( (request.getParameter("FSI_F") == null) ? 0 : 1 );
    	int FSI_A = ( (request.getParameter("FSI_A") == null) ? 0 : 1 );
    	int FSI_G = ( (request.getParameter("FSI_G") == null) ? 0 : 1 );
    	int FSI_C = ( (request.getParameter("FSI_C") == null) ? 0 : 1 );
    	int FSI_T = ( (request.getParameter("FSI_T") == null) ? 0 : 1 );

    	String bancaj = request.getParameter("bancaj").substring(8);		
    	String str = "select * from sp_cajas_vales_traspasar('" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "','" + p(bancaj) + 
    		"','" + FSI_F + "','" + FSI_A + "','" + FSI_G + "','" + FSI_C + "','" + FSI_T + "' ) as ( err integer, res varchar, clave smallint )";
   
    	JRetFuncBas rfb = new JRetFuncBas();
 		
    	doCallStoredProcedure(request, response, str, rfb);
   
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_VALES_TRASPASAR", "VCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_trasp.jsp", request, response);
    }
    
    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
    	float ftotal = Float.parseFloat(request.getParameter("total"));
    	String str = "select * from ";
		String proc = ( proceso.equals("AGREGAR") ) ? "sp_cajas_vales_agregar('" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "'" : "sp_cajas_vales_cambiar('" + p(request.getParameter("ID")) + "'";
        		
		str += proc + ",'" + p(request.getParameter("idtipo")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("idgasto")) + "','" + 
			p(request.getParameter("concepto")) + "','" + ftotal + "') as ( err integer, res varchar, clave integer)";
		
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
  
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_VALES_" + proceso, "VCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg.jsp", request, response);
   
    }

       
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String str = "select * from sp_cajas_vales_eliminar('" + p(request.getParameter("ID")) + "') as ( err integer, res varchar, clave integer)";
		
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
  
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_VALES_ELIMINAR", "VCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);
 
	
	}
 
    public void Proteger(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	//String str = " EXEC  sp_cajas_vales_proteccion " + getSesion(request).getSesionValesCaja().getEspecial();
    	String str = "select * from sp_cajas_vales_proteccion ('" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "') as ( err integer, res varchar, clave smallint)";
		
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
  
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_VALES_GENPROC", "VCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	
	}    
 
    public void GenerarGasto(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	
    	String str = "select * from sp_cajas_vales_generargasto('1','" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "','" + p(request.getParameter("identidad")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("referencia")) + "') as ( err integer, res varchar, clave smallint)";
    	
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
  
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_VALES_GENPROC", "VCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_VALES").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_y_bancos/vales_caja_dlg_gengas.jsp", request, response);
	
	}    
    
}
