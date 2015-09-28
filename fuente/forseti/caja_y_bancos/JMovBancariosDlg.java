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
import javax.servlet.http.HttpSession;
import forseti.JForsetiApl;
import forseti.JRastreo;
import forseti.JRetFuncBas;
import forseti.JUtil;
//import forseti.sets.JBDSSet;
import forseti.sets.JBDSSet;
import forseti.sets.JBancosSetDetalleV2;
import forseti.sets.JBancosSetIdsV2;
import forseti.sets.JBancosSetMovsV2;
import forseti.sets.JPublicBancosCuentasSetV2;
import forseti.sets.JSatBancosSet;

@SuppressWarnings("serial")
public class JMovBancariosDlg extends JForsetiApl
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

      String mov_bancaj_dlg = "BANCOS";
      request.setAttribute("mov_bancaj_dlg",mov_bancaj_dlg);

      String mensaje = ""; short idmensaje = -1;
      
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	JBancosSetIdsV2 setids = new JBancosSetIdsV2(request,getSesion(request).getID_Usuario(),"0",getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial());
        setids.Open();  
    	
        if(setids.getNumRows() < 1)
        {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }  
    	  
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_DEPOSITO") && !request.getParameter("proceso").equals("AGREGAR_RETIRO") && !request.getParameter("proceso").equals("TRASPASAR_FONDO") && request.getParameter("ID") != null)
        {
        	JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
        	set.m_Where = "Tipo = '0' and Clave = '" + setids.getAbsRow(0).getID() + "' and ID = '" + p(request.getParameter("ID")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN|" + request.getParameter("ID") + "|" + setids.getAbsRow(0).getID() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_DEPOSITO") || request.getParameter("proceso").equals("AGREGAR_RETIRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS_AGREGAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_AGREGAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_AGREGAR","MBAN||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(AgregarCabecero(request,response) == -1)
        	  {
        		  if(VerificarParametros(request, response))
        		  {
        			  Agregar(request, response);
        			  return;
        		  }
        	  }
        	  
              irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
        	  if(AgregarCabecero(request,response) == -1)
        	  {
        		  if(VerificarParametrosPartida(request, response))
        		  	  AgregarPartida(request, response);
        	  }
        	  
        	  irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
        	  if(AgregarCabecero(request,response) == -1)
        	  {
        		 if(VerificarParametrosPartida(request, response))
        			 EditarPartida(request, response);
        	  }
        	  
        	  irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
        	  if(AgregarCabecero(request,response) == -1)
        	  {
        		  BorrarPartida(request, response);
        	  }
        	  
        	  irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            HttpSession ses = request.getSession(true);
            JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
            if(pol == null)
            {
              pol = new JMovBancariosSes(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", false, (request.getParameter("proceso").equals("AGREGAR_DEPOSITO") ? "deposito" : "retiro"));
       	      ses.setAttribute("mov_bancarios_dlg", pol);
            }
            else
              pol.resetear(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", false, (request.getParameter("proceso").equals("AGREGAR_DEPOSITO") ? "deposito" : "retiro"));

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
            return;
            
          }
        }
        else if(request.getParameter("proceso").equals("TRASPASAR_FONDO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS_TRASPASAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_TRASPASAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_TRASPASAR","MBAN||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(AgregarCabecero(request,response) == -1)
        	  {
        		  if(VerificarParametrosTrasp(request, response))
        		  {
            		  AgregarTrasp(request, response);
        			  return;
        		  }
        	  }
        	  
              irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg_trsp.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR
          {
            HttpSession ses = request.getSession(true);
            JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
            if(pol == null)
            {
              pol = new JMovBancariosSes(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", true, "retiro");
       	      ses.setAttribute("mov_bancarios_dlg", pol);
            }
            else
              pol.resetear(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", true, "retiro");

            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg_trsp.jsp", request, response);
            return;
            
          }
        }
        else if(request.getParameter("proceso").equals("RASTREAR_MOVIMIENTO"))
        {
        	if(!getSesion(request).getPermiso("BANCAJ_BANCOS_CONSULTAR"))
            {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_CONSULTAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_CONSULTAR","MBAN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
            }

            if(request.getParameter("ID") != null)
            {
              String[] valoresParam = request.getParameterValues("ID");
              if (valoresParam.length == 1)
              {
              	
                  JRastreo rastreo = new JRastreo(	request, getSesion(request).getSesion("BANCAJ_BANCOS").generarTitulo(JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","CONSULTAR_MOVIMIENTO",3)),
                  								"MBAN",request.getParameter("ID"));
                  String rastreo_imp = "true";
                  request.setAttribute("rastreo_imp", rastreo_imp);
                  // Ahora pone los atributos para el jsp
                  request.setAttribute("rastreo", rastreo);
                  RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_CONSULTAR","MBAN|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||","");
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
        else if(request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if(valoresParam.length == 1)
            {
            	 // Llena el movimiento
                JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
                set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                set.Open();
                JBancosSetDetalleV2 set2 = new JBancosSetDetalleV2(request);
                set2.m_OrderBy = "Part ASC";
                set2.m_Where = "ID_Pol = '" + set.getAbsRow(0).getPol_ID() + "'";
                set2.Open();	
             
                
                
                HttpSession ses = request.getSession(true);
                JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
                if(pol == null)
                {
                	pol = new JMovBancariosSes(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", false, "");
                	ses.setAttribute("mov_bancarios_dlg", pol);
                }
                else
                	pol.resetear(request, getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial(), getSesion(request).getID_Usuario(), "BANCOS", false, "");

                pol.setFecha(set.getAbsRow(0).getFecha());
                pol.setID_Moneda(set.getAbsRow(0).getID_Moneda());
                pol.setTC(set.getAbsRow(0).getTC());
                pol.setRef(set.getAbsRow(0).getReferencia());
                pol.setBeneficiario(set.getAbsRow(0).getBeneficiario());
                pol.setRFC(set.getAbsRow(0).getRFC());
                pol.setNum(set.getAbsRow(0).getNum());
                pol.setStatus(set.getAbsRow(0).getEstatus());
                pol.setConcepto(set.getAbsRow(0).getConcepto());
                pol.setTipoMov(set.getAbsRow(0).getTipoMov());
                pol.setMetPagoPol(set.getAbsRow(0).getID_SatMetodosPago());
                pol.setDepChq(set.getAbsRow(0).getDoc());
                pol.setCuentaBanco(set.getAbsRow(0).getCuentaBanco());
                pol.setID_SatBanco(set.getAbsRow(0).getID_SatBanco());
                pol.setBancoExt(set.getAbsRow(0).getBancoExt());
                if(set.getAbsRow(0).getTipoMov().equals("DEP"))
                	pol.setTipoPrimario("deposito");
                else
                	pol.setTipoPrimario("retiro");
              
                for(int i = 0; i < set2.getNumRows(); i++)
                {
                	pol.agregaPartida( request, set2.getAbsRow(i).getCC(), set2.getAbsRow(i).getNombre(), set2.getAbsRow(i).getConcepto(),
                                   set2.getAbsRow(i).getParcial(), set2.getAbsRow(i).getMoneda(),
                                   set2.getAbsRow(i).getTC(), set2.getAbsRow(i).getDebe(), set2.getAbsRow(i).getHaber() );
                }
                pol.setCantidad((set.getAbsRow(0).getRetiro() == 0.0 ? set.getAbsRow(0).getDeposito() : set.getAbsRow(0).getRetiro()));
              
                RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN|" + request.getParameter("ID") + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||","");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CANCELAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS_CANCELAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_CANCELAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_CANCELAR","MBAN||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }
          
          // Solicitud de envio a procesar
          if(request.getParameter("ID") != null)
          {
            String[] valoresParam = request.getParameterValues("ID");
            if(valoresParam.length == 1)
            {
              JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
              set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
              set.Open();
              
              if(!set.getAbsRow(0).getRef().equals("") || set.getAbsRow(0).getEstatus().equals("C"))
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR2", 4); //"PRECAUCION: No se puede cancelar un movimiento ya cancelado o externo <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
              else if(set.getAbsRow(0).getMC())
              {
            	  idmensaje = 3; mensaje += JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR2", 5); //"ERROR: No se puede cancelar un movimiento conciliado <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	  return;
              }
              else
              {
            	  Cancelar(request,response);
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
        else if(request.getParameter("proceso").equals("APLICAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS_GENPROC"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_GENPROC");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_GENPROC","MBAN||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }

          if(request.getParameter("ID") != null)
		  {
        	  String[] valoresParam = request.getParameterValues("ID");
              if(valoresParam.length == 1)
              {
                JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
                set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                set.Open();
                
                if(set.getAbsRow(0).getEstatus().equals("C"))
                {
              	  idmensaje = 1; mensaje += JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 1); //"PRECAUCION: No se puede aplicar / desaplicar un movimiento cancelado<br>";
              	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              	  return;
                }
                else
                {
              	  GenerarProc(request,response,"APLICAR");
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
        else if(request.getParameter("proceso").equals("PROTEGER_MES"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS_GENPROC"))
          {
          	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS_GENPROC");
          	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS_GENPROC","MBAN||||",mensaje);
          	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          	  return;
          }
          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(request.getParameter("mes") != null && request.getParameter("ano") != null &&
        			  !request.getParameter("mes").equals("") && !request.getParameter("ano").equals(""))
        	  {
        		  GenerarProc(request, response, "CERRAR");
        		  return;
        	  }
        	  else
        	  {
        		  idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg_proc.jsp", request, response);
        		  return;
        	  }
          }
          else // Como el subproceso no es ENVIAR abre la ventana del proceso de Cierre `por primera vez
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg_proc.jsp", request, response);
        	  return;
          }
        
        }
        else if(request.getParameter("proceso").equals("IMPRIMIR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("BANCAJ_BANCOS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "BANCAJ_BANCOS");
          	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"BANCAJ_BANCOS","MBAN||||",mensaje);
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
                JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
                set.m_Where = "ID = '" + p(request.getParameter("ID")) + "'";
                set.Open();

                StringBuffer bsmensaje = new StringBuffer(254);
                String SQLCab = "select * FROM view_bancos_movimientos_impcab  where ID = " + request.getParameter("ID");
                String SQLDet = "select * FROM view_cont_polizas_impdet where ID = " + set.getAbsRow(0).getPol_ID() + " order by Part asc";
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
                 request.setAttribute("impresion", "CEFMovBancariosDlg");
                 request.setAttribute("tipo_imp", "BANCAJ_BANCOS");
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

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("cuenta") != null &&
         request.getParameter("concepto_part") != null && 
         !request.getParameter("cuenta").equals("") && 
         !request.getParameter("concepto_part").equals("") )
      {
        if((request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ||
           (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) )
        {
        	return true;
        }
        else // error
        {
        	idmensaje = 1; mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 1); //"PRECAUCION: Se debe especificar si es un retiro o un abono <br>";
        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	return false;
        }
      }
      else
      {
          idmensaje = 1; mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 2); //"PRECAUCION: Se deben enviar los parametros de cuenta, clave de moneda, concepto de partida, y tipo de cambio <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }

    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");

      double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
          Float.parseFloat(request.getParameter("debe")) : 0F;
      double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
          Float.parseFloat(request.getParameter("haber")) : 0F;
     
      double tc = (request.getParameter("tc") == null) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
      byte idmoneda = (request.getParameter("idmoneda") == null) ? 1 : Byte.parseByte(request.getParameter("idmoneda"));

      idmensaje = pol.agregaPartida(request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), pt(request.getParameter("concepto_part")),
                        idmoneda, tc, debe, haber, mensaje);

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
     
    }

    public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");

      double debe = (request.getParameter("debe") != null && !request.getParameter("debe").equals("")) ?
          Float.parseFloat(request.getParameter("debe")) : 0F;
      double haber = (request.getParameter("haber") != null && !request.getParameter("haber").equals("")) ?
          Float.parseFloat(request.getParameter("haber")) : 0F;
      double tc = (request.getParameter("tc") == null) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
      byte idmoneda = (request.getParameter("idmoneda") == null) ? 1 : Byte.parseByte(request.getParameter("idmoneda"));

      idmensaje = pol.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), pt(request.getParameter("concepto_part")),
                        idmoneda, tc, debe, haber, mensaje);

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
      
    }

    public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);

      HttpSession ses = request.getSession(true);
      JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");

      pol.borraPartida(request, Integer.parseInt(request.getParameter("idpartida")));

      getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
     
    }

    public boolean VerificarParametrosTrasp(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	
    	// Verificacion
	  	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
		HttpSession ses = request.getSession(true);
		JMovBancariosSes rec = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
	 
		JSatBancosSet setBan = new JSatBancosSet(request);
	    setBan.m_Where = "Clave = '" + p(rec.getID_SatBanco()) + "'";
	    setBan.Open();
		
		if(setBan.getNumRows() < 1)
		{
			idmensaje = 3; mensaje.append("ERROR: El banco para el SAT no es válido<br>");
   		 	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   		 	return false;
   	 	}
		else 
		{
			if(!setBan.getAbsRow(0).getClave().equals("000") && !rec.getBancoExt().equals(""))
			{
				idmensaje = 1; mensaje.append("PRECAUCION: No se debe establecer el banco extranjero porque ya se ha seleccionado un banco nacional<br>");
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       		 	return false;
			}
		}
    	
    	if(!rec.getRFC().equals(""))
    	{
    		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
    		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos");
    			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    			return false;
    		}
    	}
    	
    	if(rec.getMetPagoPol().equals("02") /*cheque*/ && (rec.getBeneficiario().equals("") || rec.getDepChq().equals("")) )
        {
       		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 3)); //"ERROR: Debes proporcionar el número y beneficiario para el cheque <br>");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    		return false;
        }
        
        if(rec.getCantidad() == 0.0)
        {
          idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 4));// "ERROR: Debe especificarse la cantidad del traspaso");
          getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
          return false;
        }
        
        String bancaj = request.getParameter("bancaj").substring(8);
        if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1 &&
        		bancaj.equals(getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial()) )
        {
        	idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 5)); //ERROR: No se puede aplicar el traspaso a si mismo <br>");
        	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        	return false;
        }
        
        if(rec.getID_Moneda() != 1 && rec.getID_MonedaDEST() != 1 && rec.getID_Moneda() != rec.getID_MonedaDEST()) // la cuenta de origen y de destino son ambas moneda extranjera pero diferentes.
        {
            idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR2", 1)); //"ERROR: La cuenta de origen y de destino son ambas moneda extranjera pero diferentes entre si. Solo se puede aplicar un traspaso cuando ambas iguales o por lo menos una cuenta esta en moneda nacional. <br>");
            getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
            return false;
        }
        
        if((rec.getID_MonedaDEST() != 1 || rec.getID_Moneda() != 1) && (rec.getTC() == 1.0 || rec.getTC() == 0.0))
        {
        	idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR2", 2)); //"ERROR: Como la cuenta de origen o de destino son en moneda extranjera, el tipo de cambio se debe establecer. <br>");
            getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
            return false;
        }
        
        if(rec.getMetPagoPol().equals("03") && rec.getTipo() == 1)
        {
        	idmensaje = 1; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 1)); //PRECAUCION: No se puede agregar un depósito o retiro de caja por un método de transferencia.
    		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    		return false;
        }
        
        if(!rec.getFijo() && rec.getFijoDEST()) //si el origen es banco contable y el destino es banco por fuera o caja por fuera
        {
        	if(rec.getBeneficiario().equals("") || rec.getRFC().equals(""))
        	{
        		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 4)); //ERROR: Debes proporcionar para la entidad contable de origen o de destino, el beneficiario y RFC, para soporte de contabilidad electrónica, ya que el traspaso, involucra una entidad no contable hacia una contable o viceversa.
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
	       	        	
        	if(rec.getMetPagoPol().equals("03") && (rec.getCuentaBanco().equals("") || (rec.getID_SatBanco().equals("000") && rec.getBancoExt().equals(""))))
        	{
        		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 3)); //"ERROR: Debes proporcionar para la entidad contable de origen o de destino, la cuenta, banco y cheque (si es necesario), para soporte de contabilidad electrónica, ya que el traspaso, involucra una entidad no contable hacia una contable o viceversa.
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
        }
        else if(rec.getFijo() && !rec.getFijoDEST()) // Si el origen es un banco fijo y el destino es un banco o caja 
        {
        	if(rec.getBeneficiario().equals("") || rec.getRFC().equals(""))
        	{
        		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 4)); //ERROR: Debes proporcionar para la entidad contable de origen o de destino, el beneficiario y RFC, para soporte de contabilidad electrónica, ya que el traspaso, involucra una entidad no contable hacia una contable o viceversa.
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
	       	        	
        	if((rec.getMetPagoPol().equals("03") || rec.getMetPagoPol().equals("02")) && (rec.getCuentaBanco().equals("") || (rec.getID_SatBanco().equals("000") && rec.getBancoExt().equals(""))))
            {
        		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 3)); //"ERROR: Debes proporcionar cuenta y banco para soporte de contabilidad electrónica, si es un depósito, cuenta y banco del cheque u origen de la transferencia, si es retiro, cuenta y banco ERROR: Debes proporcionar para la entidad contable de origen o de destino, la cuenta, banco y cheque (si es necesario), para soporte de contabilidad electrónica, ya que el traspaso, involucra una entidad no contable hacia una contable o viceversa.
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
    	JMovBancariosSes rec = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
     
	   	idmensaje = rec.agregaCabecero(request, mensaje);
	   	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	   	return idmensaje;
	}
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
		
    	HttpSession ses = request.getSession(true);
    	JMovBancariosSes rec = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");
     
    	JSatBancosSet setBan = new JSatBancosSet(request);
	    setBan.m_Where = "Clave = '" + p(rec.getID_SatBanco()) + "'";
	    setBan.Open();
		
		if(setBan.getNumRows() < 1)
		{
			idmensaje = 3; mensaje.append("ERROR: El banco para el SAT no es válido<br>");
   		 	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
   		 	return false;
   	 	}
		else 
		{
			if(!setBan.getAbsRow(0).getClave().equals("000") && !rec.getBancoExt().equals(""))
			{
				idmensaje = 1; mensaje.append("PRECAUCION: No se debe establecer el banco extranjero porque ya se ha seleccionado un banco nacional<br>");
       		 	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
       		 	return false;
			}
		}
    	
    	if(!rec.getRFC().equals(""))
    	{
    		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
    		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
    		{
    			idmensaje = 1; mensaje.append("PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos");
    			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    			return false;
    		}
    	}
    	
    	if(rec.getMetPagoPol().equals("02") /*cheque*/ && (rec.getBeneficiario().equals("") || rec.getDepChq().equals("")) )
        {
    		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 3)); //"ERROR: Debes proporcionar el número y beneficiario para el cheque <br>");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    		return false;
        }
        
        if(!rec.getFijo())
        {
        	if(rec.getBeneficiario().equals("") || rec.getRFC().equals(""))
            {
              idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 5)); //"ERROR: Debes proporcionar el beneficiario y RFC para soporte de contabilidad electrónica, si es un depósito, nuestra empresa como beneficiario y el RFC del que paga, y si es un retiro, el beneficiario y RFC, ambos del que recibe. 
              getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
              return false;
            }
        	
        	if((rec.getMetPagoPol().equals("03") || (rec.getTipoPrimario().equals("deposito") && rec.getMetPagoPol().equals("02"))) && (rec.getCuentaBanco().equals("") || (rec.getID_SatBanco().equals("000") && rec.getBancoExt().equals(""))))
            {
              idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 2)); //"ERROR: Debes proporcionar cuenta y banco para soporte de contabilidad electrónica, si es un depósito, cuenta y banco del cheque u origen de la transferencia, si es retiro, cuenta y banco de destino de la transferencia.
              getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
              return false;
            }
        	
        	if(rec.getPartidas().size() < 1)
        	{
        		idmensaje = 3; mensaje.append(JUtil.Msj("GLB", "GLB", "DLG", "CERO-PART", 2)); //"ERROR: no existen partidas del movimiento");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
        	
        	double cantidad;
        
        	if(rec.getTipoMov().equals("deposito")) // de lo contrario, siempre es retiro ya sea con cheque u otros
        		cantidad = rec.getSumHaber() - rec.getSumDebe();
        	else
        		cantidad = rec.getSumDebe() - rec.getSumHaber();
   
        	if(rec.getCantidad() != cantidad)
        	{
        		idmensaje = 3; mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR2", 3)); //"ERROR: La cantidad del movimiento no coincide con los cargos y abonos de la póliza");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        		return false;
        	}
        }
               
        return true;
    }
    
    public void GenerarProc(HttpServletRequest request, HttpServletResponse response, String proc)
      throws ServletException, IOException
    {
    	String str = "select * from ";
    	if(proc.equals("APLICAR"))
    		str += "sp_bancos_movs_aplicar( '" + p(request.getParameter("ID")) + "' ) ";
    	else
    		str += "sp_bancos_movs_cierremes( '0','" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() +
    																			"','" + p(request.getParameter("mes")) + "','" + p(request.getParameter("ano")) + "' ) ";
    	str += "as ( err integer, res varchar, clave varchar)";

    	JRetFuncBas rfb = new JRetFuncBas();
 		
        doCallStoredProcedure(request, response, str, rfb);
      
        RDP("CEF", getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_BANCOS_GENPROC", "MBAN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public void Cancelar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String str = "select * from  sp_bancos_movs_cancelar( '" + p(request.getParameter("ID")) + "' ) as ( err integer, res varchar, clave integer)";
 
    	JRetFuncBas rfb = new JRetFuncBas();
 		
        doCallStoredProcedure(request, response, str, rfb);
      
        RDP("CEF", getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")), getSesion(request).getID_Usuario(), "BANCAJ_BANCOS_CANCELAR", "MBAN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public void AgregarTrasp(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        HttpSession ses = request.getSession(true);
        JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");

        JPublicBancosCuentasSetV2 set = new JPublicBancosCuentasSetV2(request);
        String bancaj = request.getParameter("bancaj").substring(8);
        if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1) // es hacia banco
          set.m_Where = "Tipo = '0' and Clave = '" + p(bancaj) + "'";
        else
          set.m_Where = "Tipo = '1' and Clave = '" + p(bancaj) + "'";

        set.Open();
         
        double tc = 1.0;
        if(set.getAbsRow(0).getID_Moneda() != 1 || pol.getID_Moneda() != 1)
         	tc = pol.getTC(); 
      
        String str, tipomov;
        double retiro = pol.getCantidad();
        
        if(pol.getMetPagoPol().equals("02")/*cheque*/)
        	tipomov = "CHQ";
        else
        	tipomov = "RET";
      	 
        String rfc, beneficiario;
        if(!pol.getFijo() && !pol.getFijoDEST()) // Si el origen es un banco contable y el destino es un banco o caja contable tambien 
        {
        	JBDSSet setbd = new JBDSSet(request);
        	setbd.ConCat(true);
  		  	setbd.m_Where = "Nombre = 'FSIBD_" + getSesion(request).getBDCompania() + "'";
  		  	setbd.Open();
  		  	beneficiario = p(setbd.getAbsRow(0).getCompania());
  		  	rfc = p(JUtil.fco(JUtil.frfc(setbd.getAbsRow(0).getRFC())));
        }
        else
        {
        	beneficiario = p(pol.getBeneficiario());
        	rfc = p(JUtil.fco(JUtil.frfc(pol.getRFC())));
        }
        
        str = "select * from  sp_bancos_trans_agregar( '0','" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "','" + p(JUtil.obtFechaSQL(pol.getFecha())) + "','" + p(pol.getConcepto()) +
            "','" + beneficiario + "','" + retiro + "','" + tipomov + "','" +
            ( pol.getStatus().equals("T") ? "T" : "G" ) + "','" + pol.getID_Moneda() + "','" + tc + "','','" + p(pol.getRef()) + "','" + set.getAbsRow(0).getTipo() + "','" + set.getAbsRow(0).getClave() + "','" +
            p(pol.getID_SatBanco()) + "','" + rfc + "','" + p(pol.getMetPagoPol()) + "','" + p(pol.getBancoExt()) + "','" + p(pol.getCuentaBanco()) + "','" + p(pol.getDepChq()) + "') as ( err integer, res varchar, clave integer)";
               
        JRetFuncBas rfb = new JRetFuncBas();
 		
        doCallStoredProcedure(request, response, str, rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "BANCAJ_BANCOS_TRASPASAR", "MBAN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg_trsp.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JMovBancariosSes pol = (JMovBancariosSes)ses.getAttribute("mov_bancarios_dlg");

      String tbl;
      tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE (\n";
      tbl += "Part smallint NOT NULL ,\n";
      tbl += "Cuenta char(19) NOT NULL ,\n";
      tbl += "Concepto varchar(80) NOT NULL ,\n";
      tbl += "Parcial numeric(19,4) NOT NULL ,\n";
      tbl += "Moneda smallint NOT NULL ,\n";
      tbl += "TC numeric(19,4) NOT NULL ,\n";
      tbl += "Cantidad numeric(19,4) NOT NULL \n";
      tbl += "); \n";
           
      byte idmoneda = pol.getID_Moneda();
      double tc = (pol.getID_Moneda() == 1) ? 1.0F : pol.getTC();

      String str, tipomov;
      double deposito, retiro;
      
      if(pol.getTipoPrimario().equals("retiro") && pol.getMetPagoPol().equals("02")/*cheque*/)
      {
    	  tipomov = "CHQ";
    	  deposito = 0F;
    	  retiro = pol.getCantidad();
      } 
      else if(pol.getTipoPrimario().equals("deposito"))
      {
    	  if(!pol.getMetPagoPol().equals("02")/*cheque*/)
    	  	  tipomov = "DEP";
    	  else
    		  tipomov = "DCH";
    	  deposito = pol.getCantidad();
    	  retiro = 0F;
      } 
      else
      {
    	  tipomov = "RET";
    	  deposito = 0F;
    	  retiro = pol.getCantidad();
      }
      
      str = "select * from  sp_bancos_movs_agregar( '0','" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "','" + p(JUtil.obtFechaSQL(pol.getFecha())) + "','" + p(pol.getConcepto()) +
          "','" + p(pol.getBeneficiario()) + "','" + deposito + "','" + retiro + "','" + tipomov + "','" +
          ( pol.getStatus().equals("T") ? "T" : "G" ) + "','" + idmoneda + "','" + tc + "','','" + p(pol.getRef()) + "',null,null,'" + 
          p(pol.getID_SatBanco()) + "','" + p(JUtil.fco(JUtil.frfc(pol.getRFC()))) + "','" + p(pol.getMetPagoPol()) + "','" + p(pol.getBancoExt()) + "','" + p(pol.getCuentaBanco()) + "','" + p(pol.getDepChq()) + "') as ( err integer, res varchar, clave integer)";
      															        
      for(int i = 0; i < pol.getPartidas().size(); i++)
      {
    	  double cantpart;
    	  if(pol.getTipoMov().equals("cheque"))
    		  cantpart = JUtil.redondear( (pol.getPartida(i).getDebe() - pol.getPartida(i).getHaber()),2);
    	  else if(pol.getTipoMov().equals("deposito"))
    		  cantpart = JUtil.redondear( (pol.getPartida(i).getHaber() - pol.getPartida(i).getDebe()),2);
    	  else
    		  cantpart = JUtil.redondear( (pol.getPartida(i).getDebe() - pol.getPartida(i).getHaber()),2);
    	  
    	  tbl += "insert into _TMP_BANCOS_MOVIMIENTOS_DETALLE\n";
    	  tbl += "VALUES( '" + (i+1) + "','" + p(JUtil.obtCuentas(pol.getPartida(i).getCuenta(),(byte)19)) + "','" +
               p(pol.getPartida(i).getConcepto()) + "','" + pol.getPartida(i).getParcial() + "','" + pol.getPartida(i).getID_Moneda() +
               "','" + pol.getPartida(i).getTC() + "','" + cantpart + "');\n";
      }

      //doDebugSQL(request, response, (tbl + "\n\n\n" + str));
  	
      JRetFuncBas rfb = new JRetFuncBas();
    	
		
      doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_BANCOS_MOVIMIENTOS_DETALLE;", rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "BANCAJ_BANCOS_AGREGAR", "MBAN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "||",rfb.getRes());
      irApag("/forsetiweb/caja_y_bancos/mov_bancaj_dlg.jsp", request, response);
		
    }

}
