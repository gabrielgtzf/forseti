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
import forseti.JUtil;
import forseti.sets.*;


@SuppressWarnings("serial")
public class JCierresCajaDlg extends JForsetiApl
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

      String cierres_caja_dlg = "";
      request.setAttribute("cierres_caja_dlg",cierres_caja_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  // revisa por las entidades
    	  JBancosSetIdsV2 setids = new JBancosSetIdsV2(request,getSesion(request).getID_Usuario(),"1",getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial());
          //setids.m_Where = "ID_Usuario = '" + getSesion(request).getID_Usuario() + "' and Tipo = 1 and ID = " + getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial(); 
          setids.Open();  
          
          if(setids.getNumRows() < 1)
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_CIERRES");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES","CCAJ||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          } 
          
          // Revisa por intento de intrusion (Salto de permiso de entidad)
          if(!request.getParameter("proceso").equals("AGREGAR_CIERRE") && request.getParameter("ID") != null)
          {
        	JVentasCierresSet set = new JVentasCierresSet(request);
        	set.m_Where = "ID_Entidad = " + setids.getAbsRow(0).getID() + " and ID_Cierre = " + request.getParameter("ID");
          	set.Open();
          	if(set.getNumRows() < 1)
          	{
          		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_CIERRES");
          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES","CCAJ|" + getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() + "|" + setids.getAbsRow(0).getID() + "||",mensaje);
          		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          		return;
          	}
          }
          
          if(request.getParameter("proceso").equals("AGREGAR_CIERRE"))
          {
        	  // Revisa si tiene permisos
        	  if(!getSesion(request).getPermiso("BANCAJ_CIERRES_AGREGAR"))
        	  {
        		  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_CIERRES_AGREGAR");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES_AGREGAR","CCAJ||||",mensaje);
        		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		  return;
        	  }

            
        	  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
        	  {
        		  if(request.getParameter("traspasar").equals("CAMBIO"))
        		  {
        			  CierreZ(request, response);
        			  return;
        		  }
            	  else
        		  {
            			request.setAttribute("CIERREZ", "CIERREZ");
     	           		idmensaje = 2; mensaje += JUtil.Msj("CEF", "BANCAJ_CIERRES", "DLG", "ETQ", 2); //"¿ Esta correcta la cuenta en este cierre ?. Para aplicar definitivamente el cierre, selecciona el botón: Aplica defínitivamente el cierre<br>";
     	           		getSesion(request).setID_Mensaje(idmensaje, mensaje);
     	           		irApag("/forsetiweb/caja_y_bancos/cierres_caja_dlg.jsp", request, response);
     	           		return;
        		  }
        	  }
        	  else
        	  {
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/caja_y_bancos/cierres_caja_dlg.jsp", request, response);
        		  return;
        	  }
        }
    	else if(request.getParameter("proceso").equals("CONSULTAR_CIERRE"))
        {
    		// Revisa si tiene permisos
    		if(!getSesion(request).getPermiso("BANCAJ_CIERRES"))
      	  	{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_CIERRES");
      		  	getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		  	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES","CCAJ||||",mensaje);
      		  	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		  	return;
      	  	}
            
            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
            	  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES","CCAJ|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() + "||","");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_y_bancos/cierres_caja_dlg.jsp", request, response);
            	  return;
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar un cierre a la vez <br>";
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return; 
            	  
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //" ERROR: Se debe enviar el identificador del cierre que se quiere consultar <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
           
        }
    	else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("BANCAJ_CIERRES"))
            {
            	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_CIERRES");
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_CIERRES","CCAJ||||",mensaje);
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
                  String SQLCab = "select * from view_cajas_cierres_impcab where ID_Cierre = " + request.getParameter("ID");
                  String SQLDet = "select * FROM view_cajas_cierres_impdet where ID_Cierre = " + request.getParameter("ID") + " order by Partida asc";
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
                   request.setAttribute("impresion", "CEFCierresCajaDlg");
                   request.setAttribute("tipo_imp", "BANCAJ_CIERRES");
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
        	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
      }

    }

    public void CierreZ(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
    	JBDSSet setbd = new JBDSSet(request);
    	setbd.ConCat(true);
		setbd.m_Where = "Nombre = 'FSIBD_" + getSesion(request).getBDCompania() + "'";
		setbd.Open();
		String beneficiario = p(setbd.getAbsRow(0).getCompania());
		String rfc = p(JUtil.fco(JUtil.frfc(setbd.getAbsRow(0).getRFC())));
		  	
		String str = "select * from sp_cajas_cierrez ('" + getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() + "','1','" + p(request.getParameter("obs")) + "','" + beneficiario + "','" + rfc + "') as ( err integer, res varchar, clave smallint)";
		
        JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
  
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_CIERRES_AGREGAR", "CCAJ|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_CIERRES").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_y_bancos/cierres_caja_dlg.jsp", request, response);
	  
	}    
      
   
    
}
