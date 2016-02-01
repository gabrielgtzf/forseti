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
package fsi_admin.admon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.JAccesoBD;
import forseti.JBDRegistradasSet;
import forseti.JFsiScript;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.JZipUnZipUtil;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JBDSSet;
import forseti.sets.JReportesBind2Set;
import forseti.sets.JReportesBind3Set;
import forseti.sets.JReportesBindFSet;
import forseti.sets.JReportesSet;
import fsi_admin.JFsiForsetiApl;
import fsi_admin.JRepGenSes;

public class JAdmBDDlg extends JFsiForsetiApl
{
    private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);
 
      String adm_bd_dlg = "";
      request.setAttribute("adm_bd_dlg",adm_bd_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_EMPRESA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_CREAR","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response, AGREGAR))
            {
            	if(!JUtil.getFsiTareas().getActualizando())
            		Agregar(request, response);
            	else
            	{
            		idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5); //"PRECAUCION: El proceso de agregado no se puede llevar a cabo porque se esta actualizando el servidor actualmente";
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	}
            }
            irApag("/forsetiadmin/administracion/adm_bd_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/adm_bd_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("RESTAURAR_EMPRESA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_CREAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_CREAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_CREAR","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null)
          {
        	  if(request.getParameter("subproceso").equals("ENVIAR"))
        	  {
        		  // Verificacion
        		  if(VerificarParametrosRest(request, response))
        		  {	  
        			  if(!JUtil.getFsiTareas().getActualizando())
        				  Restaurar(request, response);
        			  else
        			  {
        				  idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5); //"PRECAUCION: El proceso de restauracion no se puede llevar a cabo porque se esta actualizando el servidor actualmente";
        				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  }
        		  }
		   	  }
        	  else if(request.getParameter("subproceso").equals("MOSTRAR_BDS"))
        	  {
        		  JAdmVariablesSet var = new JAdmVariablesSet(null);
        		  var.ConCat(true);
        		  var.m_Where = "ID_Variable = 'RESPALDOS'";
        		  var.Open();
        		  
        		  if(var.getAbsRow(0).getVAlfanumerico().equals("NC"))
        		  {
        			  idmensaje = 1; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",1); //"PRECAUCION: La variable RESPALDOS (ruta de respaldos) no se ha configurado aun... No se puede generar el proceso";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	      }
        		  else if( !request.getParameter("basedatos").equals("NC") && !request.getParameter("bdperdida").equals(""))
        		  {
        			  idmensaje = 1; mensaje = "PRECAUCION: Solo se puede restaurar una base de datos existente (copia) o una perdida (restauración), pero no ambas";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  }
        		  else
        		  {
        			  mensaje = ""; idmensaje = -1;
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  
        			  String ruta_resp = var.getAbsRow(0).getVAlfanumerico();
        			  request.setAttribute("ruta_resp", ruta_resp);
        		  }
        	  }
        	  
        	  irApag("/forsetiadmin/administracion/adm_bd_dlg_rest.jsp", request, response);
    		  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/adm_bd_dlg_rest.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_PROPIEDADES"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_CAMBIAR","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              JBDRegistradasSet set = new JBDRegistradasSet(null);
          	  set.ConCat(true);
          	  set.m_Where = "ID_BD = '" + p(request.getParameter("id")) + "'";
          	  set.Open();
          	  if(!set.getAbsRow(0).getSU().equals("3")) // La base de datos esta corrupta, se debe eliminar
          	  {
          		  idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",4);
          		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          		  return; 
          	  }
          	  
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
            	  if(VerificarParametros(request, response, CAMBIAR))
            	  {
            		  if(!JUtil.getFsiTareas().getActualizando())
            			  Cambiar(request, response);
            		  else
            		  {
            			  idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5);//"PRECAUCION: El proceso de cambios no se puede llevar a cabo porque se esta actualizando el servidor actualmente";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  }
            	  }
            	  irApag("/forsetiadmin/administracion/adm_bd_dlg.jsp", request, response);
            	  return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiadmin/administracion/adm_bd_dlg.jsp", request, response);
            	  return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite cambiar un concepto a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del concepto que se quiere cambiar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_EMPRESA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_ELIMINAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_ELIMINAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_ELIMINAR","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JBDRegistradasSet set = new JBDRegistradasSet(null);
            	set.ConCat(true);
            	set.m_Where = "ID_BD = '" + p(request.getParameter("id")) + "'";
            	set.Open();
            	if(set.getNumRows() < 1) 
            	{
            		idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",2);//"PRECAUCION: Ya no existe la base de datos que se quiere elminar";
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            		return; 
            	}
            	
            	if(!JUtil.getFsiTareas().getActualizando())
            		Eliminar(request, response);
            	else
            	{
            		idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5);//"PRECAUCION: El proceso de eliminacion de base de datos no se puede llevar a cabo porque se esta acutalizando el servidor actualmente";
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	}
            	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	return;
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite eliminar una empresa a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	return;
            }
          }
          else
          {
             idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador de la empresa que se quiere eliminar<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("GENERAR_REPORTE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_GENREP"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_GENREP");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_GENREP","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          String bd = "FORSETI_ADMIN";
          if(request.getParameter("id") != null)
          {
        	  String[] valoresParam = request.getParameterValues("id");
        	  if(valoresParam.length > 1)
        	  {
        		  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite uno a la vez <br>";
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        		  return;
        	  }
        	  bd = request.getParameter("id");
          }
            	
          if(!bd.equals("FORSETI_ADMIN"))
          {
              JBDRegistradasSet set = new JBDRegistradasSet(request);
          	  set.ConCat(true);
          	  set.m_Where = "ID_BD = '" + p(request.getParameter("id")) + "'";
          	  set.Open();
          	  if(!set.getAbsRow(0).getSU().equals("3")) // La base de datos esta corrupta, se debe eliminar
          	  {
          		  idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",4);
          		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
          		  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          		  return; 
          	  }
          	  bd = set.getAbsRow(0).getNombre();
          }
          
          if(request.getParameter("subproceso") == null)
          {
        	  HttpSession ses = request.getSession(true);
        	  JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
        	  if(rec == null)
        	  {
        		  rec = new JRepGenSes(request, bd);
        		  ses.setAttribute("rep_gen_dlg", rec);
        	  }
        	  else
        		  rec.resetear(request, bd);
                	  
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso").equals("PROCESO")) 
          {
        	  HttpSession ses = request.getSession(true);
        	  JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
        	  
        	  if(request.getParameter("idreportplnt") != null)
        	  {
        		  int repid = Integer.parseInt(request.getParameter("idreportplnt"));
        		  int repidinp;
        		  try { repidinp = Integer.parseInt(request.getParameter("idreport")); } catch(NumberFormatException e) { repidinp = 0; }
        		  
        		  if(repidinp == 0 && repid < 10000)  
        		  {
        			  idmensaje = 1; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",3); //"PRECAUCION: No se puede cambiar un reporte de sistema. Si deseas usar este reporte como plantilla para uno nuevo, debes ingresar el id del nuevo reporte, y que este sea mayor a 10000";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
                	  return; 
        		  }
        		  
        		  if(repidinp != 0 && repidinp < 10000)  
        		  {
        			  idmensaje = 1; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",4);//"PRECAUCION: No se puede agregar un reporte con un id menor a 10000, porque estos estan reservados para reportes del sistema. Cambia el id y vuelve a intentarlo";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
                	  return; 
        		  }
        		  
        		  if(repidinp != 0)
        		  {
        			  JReportesSet rep = new JReportesSet(request);
        			  rep.ConCat(3);
        			  rep.setBD(rec.getBD());
        			  rep.m_Where = "ID_Report = " + repidinp;
        			  rep.Open();
        			  if(rep.getNumRows() > 0)
        			  {
        				  idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",5); //"ERROR: El reporte que se intenta agregar ya existe. Selecciona otro número de reporte";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
                    	  return;  
        			  }
        		  }
        		  
        		  rec.setID_ReportPlnt(repid);
        		  rec.setID_Report( (repidinp == 0 ? repid : repidinp) );
        		  
            	  //Carga el reporte
            	  JReportesSet SetMod = new JReportesSet(request);
                  SetMod.m_Where = "ID_Report = " + repid;
                  SetMod.ConCat(3);
                  SetMod.setBD(rec.getBDP());
                  SetMod.Open();
                  rec.setTipo(SetMod.getAbsRow(0).getTipo());
                  rec.setSubTipo(SetMod.getAbsRow(0).getSubTipo());
                  rec.setClave(SetMod.getAbsRow(0).getClave());
                  rec.setGraficar(SetMod.getAbsRow(0).getGraficar());
                  rec.setDescripcion(SetMod.getAbsRow(0).getDescription());
                  rec.setHW(SetMod.getAbsRow(0).getHW());
                  rec.setVW(SetMod.getAbsRow(0).getVW());
                  rec.estEncabezado("Titulo", SetMod.getAbsRow(0).getTitulo());
                  rec.estEncabezado("EncL1", SetMod.getAbsRow(0).getEncL1());
                  rec.estEncabezado("EncL2", SetMod.getAbsRow(0).getEncL2());
                  rec.estEncabezado("EncL3", SetMod.getAbsRow(0).getEncL3());
                  rec.estEncabezado("L1", SetMod.getAbsRow(0).getL1());
                  rec.estEncabezado("L2", SetMod.getAbsRow(0).getL2());
                  rec.estEncabezado("L3", SetMod.getAbsRow(0).getL3());
                  rec.estEncabezado("CL1", SetMod.getAbsRow(0).getCL1());
                  rec.estEncabezado("CL2", SetMod.getAbsRow(0).getCL2());
                  rec.estEncabezado("CL3", SetMod.getAbsRow(0).getCL3());
 
                  JReportesBind2Set SetB2 = new JReportesBind2Set(request);
                  SetB2.m_Where = "ID_Report = " + repid;
                  SetB2.ConCat(3);
                  SetB2.setBD(rec.getBDP());
                  SetB2.Open();
                  for(int i = 0; i < SetB2.getNumRows(); i++)
                  {
                	  if(SetB2.getAbsRow(i).getID_Sentence() == 1 && SetB2.getAbsRow(i).getID_IsCompute() == 0) 
                	  {
                		  rec.setTabPrintPntL1(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setSCL1(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusL1("PRO");
                	  }
                	  else if(SetB2.getAbsRow(i).getID_Sentence() == 1 && SetB2.getAbsRow(i).getID_IsCompute() == 1) 
                	  {
                		  rec.setTabPrintPntCL1(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setCSCL1(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusCL1("PRO");
                	  }
                	  else if(SetB2.getAbsRow(i).getID_Sentence() == 2 && SetB2.getAbsRow(i).getID_IsCompute() == 0) 
                	  {
                		  rec.setTabPrintPntL2(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setSCL2(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusL2("PRO");
                	  }
                	  else if(SetB2.getAbsRow(i).getID_Sentence() == 2 && SetB2.getAbsRow(i).getID_IsCompute() == 1) 
                	  {
                		  rec.setTabPrintPntCL2(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setCSCL2(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusCL2("PRO");
                	  }
                	  else if(SetB2.getAbsRow(i).getID_Sentence() == 3 && SetB2.getAbsRow(i).getID_IsCompute() == 0) 
                	  {
                		  rec.setTabPrintPntL3(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setSCL3(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusL3("PRO");
                	  }
                	  else if(SetB2.getAbsRow(i).getID_Sentence() == 3 && SetB2.getAbsRow(i).getID_IsCompute() == 1) 
                	  {
                		  rec.setTabPrintPntCL3(SetB2.getAbsRow(i).getTabPrintPnt());
                		  rec.setCSCL3(SetB2.getAbsRow(i).getSelect_Clause());
                		  rec.setStatusCL3("PRO");
                	  }
                  }
  	            
                  JReportesBind3Set SetB3 = new JReportesBind3Set(request);
                  SetB3.m_Where = "ID_Report = " + repid;
                  SetB3.m_OrderBy = "ID_Sentence ASC, ID_IsCompute ASC, ID_Column ASC";
                  SetB3.ConCat(3);
                  SetB3.setBD(rec.getBDP());
                  SetB3.Open();
  	            	
                  for(int i = 0; i< SetB3.getNumRows(); i++)
                  {
                	  String L = "";
                	  if(SetB3.getAbsRow(i).getID_Sentence() == 1 && SetB3.getAbsRow(i).getID_IsCompute() == 0) 
                		  L = "L1";
                	  else if(SetB3.getAbsRow(i).getID_Sentence() == 1 && SetB3.getAbsRow(i).getID_IsCompute() == 1) 
                		  L = "CL1";
                	  else if(SetB3.getAbsRow(i).getID_Sentence() == 2 && SetB3.getAbsRow(i).getID_IsCompute() == 0) 
                		  L = "L2";
                	  else if(SetB3.getAbsRow(i).getID_Sentence() == 2 && SetB3.getAbsRow(i).getID_IsCompute() == 1) 
                		  L = "CL2";
                	  else if(SetB3.getAbsRow(i).getID_Sentence() == 3 && SetB3.getAbsRow(i).getID_IsCompute() == 0) 
                		  L = "L3";
                	  else if(SetB3.getAbsRow(i).getID_Sentence() == 3 && SetB3.getAbsRow(i).getID_IsCompute() == 1) 
                		  L = "CL3";
   	            	
                	  rec.agregaColumna(L,SetB3.getAbsRow(i).getColName(), SetB3.getAbsRow(i).getBindDataType(), !SetB3.getAbsRow(i).getWillShow(), SetB3.getAbsRow(i).getFormat(), SetB3.getAbsRow(i).getAncho(), SetB3.getAbsRow(i).getAlinHor());
	            		 
                  }
  	             
                  JReportesBindFSet SetBF = new JReportesBindFSet(request);
                  SetBF.m_Where = "ID_Report = " + repid;
                  SetBF.m_OrderBy = "ID_Column ASC";
                  SetBF.ConCat(3);
                  SetBF.setBD(rec.getBDP());
                  SetBF.Open();
  	             
                  for(int i = 0; i< SetBF.getNumRows(); i++)
                  {
                	  int idcatalogo;
                	  try { idcatalogo = Integer.parseInt(SetBF.getAbsRow(i).getSelect_Clause()); } catch(NumberFormatException e) { idcatalogo = 0; }
 	            	 
                	  rec.agregaFiltro(SetBF.getAbsRow(i).getInstructions(), SetBF.getAbsRow(i).getIsRange(), SetBF.getAbsRow(i).getPriDataName(), SetBF.getAbsRow(i).getPriDefault(), SetBF.getAbsRow(i).getSecDataName(), SetBF.getAbsRow(i).getSecDefault(), SetBF.getAbsRow(i).getBindDataType(), SetBF.getAbsRow(i).getFromCatalog(), idcatalogo);
                  }
            	  // Fin de carga del reporte
        	  }
        	  else
        	  {
        		  int repidinp;
        		  try { repidinp = Integer.parseInt(request.getParameter("idreport")); } catch(NumberFormatException e) { repidinp = 0; }
        		   
        		  if(repidinp < 10000)  
        		  {
        			  idmensaje = 1; mensaje += "PRECAUCION: No se puede agregar un reporte con un id menor a 10000, porque estos estan reservados para reportes del sistema. Cambia el id y vuelve a intentarlo";
        			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
                	  return; 
        		  }
        		  else
        		  {
        			  JReportesSet rep = new JReportesSet(request);
        			  rep.ConCat(3);
        			  rep.setBD(rec.getBD());
        			  rep.m_Where = "ID_Report = " + repidinp;
        			  rep.Open();
        			  if(rep.getNumRows() > 0)
        			  {
        				  idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR2",5); //"ERROR: El reporte que se intenta agregar ya existe. Selecciona otro número de reporte";
            			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            			  irApag("/forsetiadmin/administracion/adm_bd_dlg_prerepgen.jsp", request, response);
                    	  return;  
        			  }
        		  }
        		  
        		  rec.setID_Report(repidinp);
        		  rec.setID_ReportPlnt(0);
        		 
        	  }
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
        	  return;
          }
          else
          {
        	  // Solicitud de envio a procesar
        	  if(request.getParameter("subproceso").equals("ENVIAR"))
        	  {
        		  if(AgregarCabecero(request,response) == -1)
        		  {
        			  Actualizar(request, response);
        			  return;
        		  }
        		  else
        		  {
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
        			  return;
        		  }
        	  }
        	  else if(request.getParameter("subproceso").equals("PROBARL1"))
        	  {
        		  if(AgregarCabecero(request,response) == -1)
        		  {
        			  Probar(request, response, "L1");
        			  return;
        		  }
        		  else
        		  {
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
        			  return;
        		  }
        	  }
        	  else if(request.getParameter("subproceso").equals("PROBARL2"))
        	  {
        		  if(AgregarCabecero(request,response) == -1)
        		  {
        			  Probar(request, response, "L2");
        			  return;
        		  }
        		  else
        		  {
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
        			  return;
        		  }
        	  }
        	  else if(request.getParameter("subproceso").equals("PROBARL3"))
        	  {
        		  if(AgregarCabecero(request,response) == -1)
        		  {
        			  Probar(request, response, "L3");
        			  return;
        		  }
        		  else
        		  {
        			  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);  
        			  return;
        		  }
        	  }
        	  /*else if(request.getParameter("subproceso").equals("CAMBIO_BASE"))
        	  {
        		  CambioBase(request, response);
        		  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response); 
        		  return;
        	  }*/
        	  else if(request.getParameter("subproceso").equals("CAMBIO_TABLA"))
        	  {
        		  CambioTabla(request, response);
        		  irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response); 
        		  return;
        	  }
          }
           
        }
        else if(request.getParameter("proceso").equals("RESPALDAR_EMPRESA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_PROPIEDADES"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_PROPIEDADES");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	JBDSSet set = new JBDSSet(null);
            	set.ConCat(true);
            	set.m_Where = "ID_BD = '" + p(request.getParameter("id")) + "'";
            	set.Open();
            	
            	if(!set.getAbsRow(0).getSU().equals("3")) // La base de datos esta corrupta, se debe eliminar
            	{
            		idmensaje = 3; mensaje += JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",4);
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            		return; 
            	}
            	
            	if(!JUtil.getFsiTareas().getActualizando())
                {
            		StringBuffer sb_mensaje = new StringBuffer();
            		Calendar fecha = GregorianCalendar.getInstance();
        			idmensaje = JUtil.getFsiTareas().respaldarEmpresa(set, 0, fecha, sb_mensaje, null); //Servidor(fecha, sb_mensaje);  
        			RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",q(sb_mensaje.toString()));
        	    	getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
                    irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
                    return;
                }
                else
                {
                	idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5);// "PRECAUCION: El proceso de respaldo no se puede llevar a cabo porque se esta aplicando actualmente";
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
                    return;
                }
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite eliminar una empresa a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	return;
            }
          }
          else
          {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
          	getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          	return;
          }
        }
        else if(request.getParameter("proceso").equals("CONFIGURAR_PROPIEDADES"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_PROPIEDADES"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_PROPIEDADES");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametrosPropServ(request, response))
            {
            	if(!JUtil.getFsiTareas().getActualizando())
            	{
            		ActualizarPropiedades(request, response);
            		return;
            	}
            	else
            	{
            		idmensaje = 2; mensaje = "PRECAUCION: El proceso de actualizacion de propiedades no se puede llevar a cabo porque se esta actualizando el servidor actualmente";
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	}
            }

            irApag("/forsetiadmin/administracion/adm_bd_dlg_propserv.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/adm_bd_dlg_propserv.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("ACTUALIZAR_SERVIDOR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_BD_PROPIEDADES"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_BD_PROPIEDADES");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",mensaje);
        	  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        	  return;
          }

          if(!JUtil.getFsiTareas().getActualizando())
          {
        	  StringBuffer sb_mensaje = new StringBuffer();
        	  idmensaje = JUtil.getFsiTareas().actualizarServidor(sb_mensaje);
        	  RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",q(sb_mensaje.toString()));
        	  getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
          }
          else
          {
        	  idmensaje = 2; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5);//"PRECAUCION: El proceso de actualizacion no se puede llevar a cabo porque se esta aplicando actualmente";
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
          }
          
          
        }
        else
        {
          idmensaje = 3;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
         return;
      }

    }
    
    public boolean VerificarParametrosPropServ(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("tomcat") != null && 
    		request.getParameter("jvm") != null && 
    		request.getParameter("servidor") != null && 
    		request.getParameter("actualizar") != null && 
    		request.getParameter("postgresql") != null && 
    		request.getParameter("fsi_pass") != null && 
    		request.getParameter("respaldos") != null && 
    		request.getParameter("auto_hora") != null && 
    	    request.getParameter("pac_purl") != null && 
    		request.getParameter("pac_serv") != null && 
    		request.getParameter("pac_user") != null && 
    		request.getParameter("pac_pass") != null && 
    	    request.getParameter("edi_purl") != null && 
    		request.getParameter("edi_user") != null && 
    		request.getParameter("edi_pass") != null && 
    	    request.getParameter("smtp_host") != null && 
    		request.getParameter("smtp_port") != null && 
    		request.getParameter("smtp_user") != null && 
    		request.getParameter("smtp_pass") != null &&
    		request.getParameter("s3_bukt") != null && 
    	    request.getParameter("s3_user") != null && 
    	    request.getParameter("s3_pass") != null &&
    	    		
    		!request.getParameter("tomcat").equals("") && 
    	    !request.getParameter("jvm").equals("") && 
    	    !request.getParameter("servidor").equals("") && 
    	    !request.getParameter("actualizar").equals("") && 
    	    !request.getParameter("respaldos").equals("") && 
    	    !request.getParameter("pac_purl").equals("") && 
    	    !request.getParameter("pac_serv").equals("") && 
    	    !request.getParameter("pac_user").equals("") && 
    	    !request.getParameter("edi_purl").equals("") && 
    	    !request.getParameter("edi_user").equals("") && 
    	    !request.getParameter("smtp_host").equals("") && 
    	    !request.getParameter("smtp_port").equals("") && 
    	    !request.getParameter("smtp_user").equals("") &&
    	    !request.getParameter("s3_bukt").equals("") && 
    	    !request.getParameter("s3_user").equals(""))
    	{
    		if(!request.getParameter("pac_serv").matches("\\w{36}"))
    	    {
    			idmensaje = 1; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR3",1);//"PRECAUCION: El ID de servidor debe de constar de 36 caracteres alfanuméricos exáctamente, sin caracteres especiales";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    	    }
    		return true;
    	}
    	else
    	{
    		idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}

    }
    
    public void ActualizarPropiedades(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	    	
    	String str = "SELECT * FROM sp_adm_bd_propserv('" +
    			p(request.getParameter("tomcat")) + "','" + 
        		p(request.getParameter("jvm")) + "','" + 
        		p(request.getParameter("servidor")) + "','" + 
        		p(request.getParameter("actualizar")) + "','" + 
        		p(request.getParameter("postgresql")) + "','" + 
        		q(request.getParameter("fsi_pass")) + "','" + 
        		p(request.getParameter("respaldos")) + "','" + 
        		JUtil.obtFechaHoraSQL("1/ene/1970 " + p(request.getParameter("auto_hora"))) + "','" + 
        		(request.getParameter("auto_act") == null ? "0" : "1") + "','" + 
        		(request.getParameter("auto_resp") == null ? "0" : "1") + "','" + 
        		(request.getParameter("auto_slds") == null ? "0" : "1") + "','" + 
        	    p(request.getParameter("pac_purl")) + "','" + 
        		p(request.getParameter("pac_serv")) + "','" + 
        		p(request.getParameter("pac_user")) + "','" + 
        		q(request.getParameter("pac_pass")) + "','" + 
        	    p(request.getParameter("edi_purl")) + "','" + 
        		p(request.getParameter("edi_user")) + "','" + 
        		q(request.getParameter("edi_pass")) + "','" + 
        	    p(request.getParameter("smtp_host")) + "','" + 
        		p(request.getParameter("smtp_port")) + "','" + 
        		p(request.getParameter("smtp_user")) + "','" + 
        		q(request.getParameter("smtp_pass")) + "','" +
        		p(request.getParameter("s3_bukt")) + "','" + 
        		p(request.getParameter("s3_user")) + "','" + 
        		q(request.getParameter("s3_pass")) + "') as ( err integer, res varchar, clave varchar ) "; 

    	doCallStoredProcedure(request, response, str, rfb);
    	
    	//Ya estan en las variables, ahora guarda los archivos
    	if(rfb.getIdmensaje() == 0)
    	{
    		if(!q(request.getParameter("fsi_pass")).equals(""))
    		{
    			String fsi_pass =	
        				"CREATE OR REPLACE FUNCTION getfsipass()\n" +
        				"RETURNS varchar AS\n" +
        				"$BODY$\n" +  
    					"BEGIN\n" +
    					"	return '" + q(request.getParameter("fsi_pass")) + "';\n" +
    					"END\n" +
    					"$BODY$\n" +
    					"  LANGUAGE 'plpgsql';\n" +
        				"ALTER FUNCTION getfsipass()\n" +
        				"OWNER TO forseti;";
    			try
    	      	{
    				String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
    				
    				String driver = "org.postgresql.Driver";
    	          	Class.forName(driver).newInstance();
    	      		String url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
    	           	Connection conn = DriverManager.getConnection(url);
    	           	Statement s = conn.createStatement();
    	      		s.execute(fsi_pass);
    	            s.close();
    	      		conn.close();
    	      	}
    			catch(Throwable e)
    	      	{
    	      		e.printStackTrace(System.out);
    	      	}
       		}
    		String auto =
    				"HORA=" + p(request.getParameter("auto_hora")) + "\n" +
    				"ACT=" + (request.getParameter("auto_act") == null ? "0" : "1") + "\n" + 
            		"SLDS=" + (request.getParameter("auto_slds") == null ? "0" : "1") + "\n" +
            		"RESP=" + (request.getParameter("auto_resp") == null ? "0" : "1") + "\n" + 
            		"POSTGRESQL=" + p(request.getParameter("postgresql")) + "\n" + 
            		"RESPALDOS=" + p(request.getParameter("respaldos")) + "\n" + 
    				"ACTUALIZAR=" + p(request.getParameter("actualizar")) + "\n" + 
    	    		"TOMCAT=" + p(request.getParameter("tomcat"));
            FileWriter fauto = null;
            try 
            {
                File file = new File("/usr/local/forseti/bin/.forseti_auto");
                fauto = new FileWriter(file);
                fauto.write(auto);
                fauto.close();
            } 
            catch (IOException ex) 
            {
            	ex.printStackTrace(System.out);
            	fauto.close();
            }
    		String pac =
    				"PURL=" + p(request.getParameter("pac_purl")) + "\n" +
    				"SERV=" + p(request.getParameter("pac_serv")) + "\n" +
    				"USER=" + p(request.getParameter("pac_user")) + "\n";
    		if(!q(request.getParameter("pac_pass")).equals(""))
    			pac += "PASS=" + q(request.getParameter("pac_pass"));
    		else
    		{
    			JAdmVariablesSet var = new JAdmVariablesSet(null);
    			var.ConCat(true);
    			var.m_Where = "ID_Variable = 'PAC_PASS'";
    			var.Open();
    			pac += "PASS=" + var.getAbsRow(0).getVAlfanumerico();
    		}
    		FileWriter fpac = null;
            try 
            {
                File file = new File("/usr/local/forseti/bin/.forseti_pac");
                fpac = new FileWriter(file);
                fpac.write(pac);
                fpac.close();
            } 
            catch (IOException ex) 
            {
            	ex.printStackTrace(System.out);
            	fpac.close();
            }
            
    		String edi =
    				"PURL=" + p(request.getParameter("edi_purl")) + "\n" +
    				"USER=" + p(request.getParameter("edi_user")) + "\n";
    		if(!q(request.getParameter("edi_pass")).equals(""))
    			edi += "PASS=" + q(request.getParameter("edi_pass"));
    		else
    		{
    			JAdmVariablesSet var = new JAdmVariablesSet(null);
    			var.ConCat(true);
    			var.m_Where = "ID_Variable = 'EDI_PASS'";
    			var.Open();
    			edi += "PASS=" + var.getAbsRow(0).getVAlfanumerico();
    		}
    		FileWriter fedi = null;
            try 
            {
                File file = new File("/usr/local/forseti/pac/.forseti_pac");
                fedi = new FileWriter(file);
                fedi.write(edi);
                fedi.close();
            } 
            catch (IOException ex) 
            {
            	ex.printStackTrace(System.out);
            	fedi.close();
            }
    		
    		String smtp =
    				"HOST=" + p(request.getParameter("smtp_host")) + "\n" +
    				"PORT=" + p(request.getParameter("smtp_port")) + "\n" +
    				"USER=" + p(request.getParameter("smtp_user")) + "\n";
    		if(!q(request.getParameter("smtp_pass")).equals(""))
    			smtp += "PASS=" + q(request.getParameter("smtp_pass"));
    		else
    		{
    			JAdmVariablesSet var = new JAdmVariablesSet(null);
    			var.ConCat(true);
    			var.m_Where = "ID_Variable = 'SMTP_PASS'";
    			var.Open();
    			smtp += "PASS=" + var.getAbsRow(0).getVAlfanumerico();
    		}
    		FileWriter fsmtp = null;
            try 
            {
                File file = new File("/usr/local/forseti/bin/.forseti_smtp");
                fsmtp = new FileWriter(file);
                fsmtp.write(smtp);
                fsmtp.close();
            } 
            catch (IOException ex) 
            {
            	ex.printStackTrace(System.out);
            	fsmtp.close();
            }
           
            String s3 =
    				"BUKT=" + p(request.getParameter("s3_bukt")) + "\n" +
    				"USER=" + p(request.getParameter("s3_user")) + "\n";
    		if(!q(request.getParameter("s3_pass")).equals(""))
    			s3 += "PASS=" + q(request.getParameter("s3_pass"));
    		else
    		{
    			JAdmVariablesSet var = new JAdmVariablesSet(null);
    			var.ConCat(true);
    			var.m_Where = "ID_Variable = 'S3_PASS'";
    			var.Open();
    			s3 += "PASS=" + var.getAbsRow(0).getVAlfanumerico();
    		}
    		FileWriter fs3 = null;
            try 
            {
                File file = new File("/usr/local/forseti/bin/.forseti_awss3");
                fs3 = new FileWriter(file);
                fs3.write(s3);
                fs3.close();
            } 
            catch (IOException ex) 
            {
            	ex.printStackTrace(System.out);
            	fs3.close();
            }
    	}
    	
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD||||",rfb.getRes());
    	irApag("/forsetiadmin/administracion/adm_bd_dlg_propserv.jsp", request, response);
    	
    }
    
    public boolean VerificarParametrosRest(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	short idmensaje = -1; String mensaje = "";
    	// Verificacion
    	if(request.getParameter("nombre") != null && request.getParameter("basedatos") != null && request.getParameter("respaldo") != null
    		&& request.getParameter("password") != null  && request.getParameter("confpwd") != null &&
    	    !request.getParameter("nombre").equals("") && !request.getParameter("basedatos").equals("") && !request.getParameter("respaldo").equals("") &&
    	    !request.getParameter("password").equals("") && !request.getParameter("confpwd").equals(""))
    	{
    		if(!request.getParameter("nombre").matches("[A-Z]{4,20}"))
    		{
    			idmensaje = 3; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR3",2);//"ERROR: El nombre de la base de datos debe de constar de entre 4 y 20 caracteres de la A a la Z mayusculas";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;  
    		}
    		
    		JBDRegistradasSet set = new JBDRegistradasSet(null);
    		set.m_Where = "Nombre ~~* 'FSIBD_" + p(request.getParameter("nombre")) + "'";
    		set.ConCat(true);
    		set.Open();
    		if(set.getNumRows() > 0)
    		{
    			idmensaje = 3; mensaje = JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 2);
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;  
    		}
    	
    		if(!request.getParameter("password").equals(request.getParameter("confpwd")))
    		{
    			idmensaje = 3; mensaje = JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 1);
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}
    		return true;
    	}
    	else
    	{
    		idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response, short proc)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("nombre") != null && request.getParameter("compania") != null
          && request.getParameter("direccion") != null  && request.getParameter("poblacion") != null
          && request.getParameter("cp") != null  && request.getParameter("mail") != null 
          && request.getParameter("web") != null
          && request.getParameter("password") != null  && request.getParameter("confpwd") != null &&
          !request.getParameter("nombre").equals("") && !request.getParameter("compania").equals("") &&
          !request.getParameter("password").equals("") && !request.getParameter("confpwd").equals(""))
      {
    	  if(proc == AGREGAR)
    	  {
    		  if(!request.getParameter("nombre").matches("[A-Z]{4,20}"))
    		  {
    			  idmensaje = 3; mensaje = JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR3",2);//"ERROR: El nombre de la base de datos debe de constar de entre 4 y 20 caracteres de la A a la Z mayusculas";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;  
    		  }
    		  
    		  JBDRegistradasSet set = new JBDRegistradasSet(null);
    		  set.m_Where = "Nombre ~~* 'FSIBD_" + p(request.getParameter("nombre")) + "'";
    		  set.ConCat(true);
    		  set.Open();
    		  if(set.getNumRows() > 0)
    		  {
    			  idmensaje = 3; mensaje = JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 2);
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  return false;  
    		  }
    	  }
    	  
    	  if(!request.getParameter("password").equals(request.getParameter("confpwd")))
    	  {
    		  idmensaje = 3; mensaje = JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 1);
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
    	  return true;
      }
      else
      {
          idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }

    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_adm_bd_cambiar( '" + p(request.getParameter("id")) + "','FSIBD_" + p(request.getParameter("nombre")) + "','" + p(request.getParameter("nombre")).toLowerCase() + "','" + q(request.getParameter("password")) + "','" + p(request.getParameter("compania")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("poblacion")) + "','" +
			p(request.getParameter("cp")) + "','" + p(request.getParameter("mail")) + "','" + p(request.getParameter("web")) + "') as ( err integer, res varchar, clave integer ) "; 

    	doCallStoredProcedure(request, response, str, rfb);
    	
    	// Ahora actualiza 
    	Connection conn = null;
       	Statement s = null;
       	try
    	{
			String driver = "org.postgresql.Driver";
          	Class.forName(driver).newInstance();
      		// Apartir de aqui es crítico el proceso Genera el usuario
      		String url = "jdbc:postgresql://" + JUtil.getADDR() + ":" + JUtil.getPORT() + "/postgres?user=forseti&password=" + JUtil.getPASS();
      		conn = DriverManager.getConnection(url);
           	s = conn.createStatement();
           	String sql = 
           		"ALTER ROLE " + p(request.getParameter("nombre")).toLowerCase() +  " \n" +
           		"ENCRYPTED PASSWORD '" + q(request.getParameter("password")) + "';";
      		s.execute(sql);
    	}
      	catch(Throwable e)
        {
    		e.printStackTrace(System.out);
    		rfb.setIdMensaje(3);
    		rfb.setRes(p(e.getMessage()));
        }
    	finally
    	{
        	try { s.close(); } catch (Exception e) { }
            try { conn.close(); } catch (Exception e) { }
    	}	
    	
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_CAMBIAR","ADBD|FSIBD_" + p(request.getParameter("nombre")) +  "|||",rfb.getRes());
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String mensaje = ""; short idmensaje = -1;
    	String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
    	//GENERA LA BASE DE DATOS MANUALMENTE. (NO A TRAVES DE PROCEDIMIENTO ALMACENADO)
    	Calendar fecha = GregorianCalendar.getInstance(); 
		String path = "/usr/local/forseti/log/CREAR-FSIBD_" + request.getParameter("nombre") + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
		FileWriter filewri		= new FileWriter(path, true);
		PrintWriter pw			= new PrintWriter(filewri);
		Connection conn = null;
       	Statement s = null;
       	
		try
    	{
			pw.println("----------------------------------------------------------------------------");
			pw.println("             " + "AGREGAR EMPRESA: " + p(request.getParameter("nombre")) + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.println("----------------------------------------------------------------------------");
			pw.flush();
			
			String driver = "org.postgresql.Driver";
          	Class.forName(driver).newInstance();
      		// Apartir de aqui es crítico el proceso Genera el usuario
      		String url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
      		conn = DriverManager.getConnection(url);
           	s = conn.createStatement();
           	String sql = 
           		"CREATE ROLE " + p(request.getParameter("nombre")).toLowerCase() +  " LOGIN\n" +
           		"ENCRYPTED PASSWORD '" + q(request.getParameter("password")) + "'\n" +
           		"NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE NOREPLICATION;\n" +
           		"GRANT " + p(request.getParameter("nombre")).toLowerCase() +  " TO forseti;";
      		s.execute(sql);
      		pw.println("Creado el usuario dueño de la base de datos de la empresa...");
			pw.flush();
			// Genera la base de datos del usuario
            sql = 
            	"CREATE DATABASE \"FSIBD_" + p(request.getParameter("nombre")) + "\"\n" +
            	"	WITH OWNER = " + p(request.getParameter("nombre")).toLowerCase() + "\n" +
            	"	TEMPLATE = template0\n" +
            	"	ENCODING = 'UTF8'\n" +
            	"	LC_COLLATE = 'es_MX.UTF-8'\n" +
            	"	LC_CTYPE = 'es_MX.UTF-8'\n" +
            	"	CONNECTION LIMIT = -1;";
     		s.execute(sql);
            s.close();
      		conn.close();
      		pw.println("Creada la base de datos de la empresa...");
			pw.flush();
			// Inserta registro nuevo de BD
      		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
           	conn = DriverManager.getConnection(url);
           	s = conn.createStatement();
      		sql = "INSERT INTO tbl_bd(id_bd, nombre, usuario, \"password\", fechaalta, compania, direccion, poblacion, cp, mail, web, su)\n";
      		sql += "VALUES (default, 'FSIBD_" + p(request.getParameter("nombre")) + "','" + p(request.getParameter("nombre")).toLowerCase() + "','" + q(request.getParameter("password")) + "','" + JUtil.obtFechaSQL(Calendar.getInstance()) + "','" + p(request.getParameter("compania")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("poblacion")) + "','" + p(request.getParameter("cp")) + "','" + p(request.getParameter("mail")) + "','" + p(request.getParameter("web")) + "','0');\n";
      		s.execute(sql);
            s.close();
      		conn.close();
      		pw.println("Insertado registro de base de datos en FORSETI_ADMIN... SU = 0");
      		pw.println("--------------------- AHORA LA ESTRUCTURA DE LA BASE DE DATOS: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss") + " ---------------------");
			pw.flush();
			// Se conecta a la base de datos principal creada para generar su estructura
      		// Esta depende del archivo .forseti_inibd que es el que tiene la estructura de todas las BD
           	url = "jdbc:postgresql://" + addr + ":" + port + "/FSIBD_" + p(request.getParameter("nombre")) + "?user=" + p(request.getParameter("nombre")).toLowerCase() + "&password=" + q(request.getParameter("password"));
           	conn = DriverManager.getConnection(url);
           	s = conn.createStatement();
      		// Ahora toma del archivo de inicio
      		// bin/.forseti_inibd que contiene la estructura de inicio de cada base de datos
            sql = "";
            FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_inibd");
            BufferedReader buff     = new BufferedReader(file);
            boolean eof             = false;
            String strbas = "";
            Vector<String> actbd_sql = new Vector<String>();
    		
			while(!eof)
			{
				String line = buff.readLine();
				if(line == null)
					eof = true;
				else
				{
					if(line.indexOf("--@FIN_BLOQUE") == -1)
						strbas += line + "\n";
					else
					{
						actbd_sql.addElement(strbas);
						strbas = "";
					}
	            }
			}
			buff.close();
			file.close();
			buff = null;
            file = null;
            
			// Ahora, ejecuta la larga consulta......
            for(int j = 0; j < actbd_sql.size(); j++)
			{
				sql = actbd_sql.get(j);
				pw.println(sql);
				pw.flush();
				s.executeUpdate(sql);
			}
			            
			pw.println("------------------------------------------------------------------------");
			pw.println("Fin de estructura de base de datos. Ahora los mensajes del sistema. " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.flush();
            // Ahora toma del archivo de lenguaje segun sea el caso (Hasta ahora solo permite Español (es) En proximas actualizaciones será multiidioma)
      		sql = "";
            file = new FileReader("/usr/local/forseti/bin/.forseti_es");
            buff = new BufferedReader(file);
            eof  = false;
            while(!eof)
            {
                String line = buff.readLine();
                if(line == null)
                	eof = true;
                else
                {
                	if(line.equals("__INIT"))
                	{
                		String alc = "", mod = "", sub = "", elm = "", msj1 = "", msj2 = "", msj3 = "", msj4 = "", msj5 = "";
                		for(int i = 1; i <= 9; i++)
                		{
                			line = buff.readLine();
                			switch(i)
                			{
                			case 1: msj1 = "'" + line + "'";
                			break;
                			case 2: msj2 = (line.equals("null") ? "null" : "'" + line + "'");
                			break;
                			case 3: msj3 = (line.equals("null") ? "null" : "'" + line + "'");
                			break;
                			case 4: msj4 = (line.equals("null") ? "null" : "'" + line + "'");
                			break;
                			case 5: msj5 = (line.equals("null") ? "null" : "'" + line + "'");
                			break;
                			case 6: alc = "'" + line + "'";
                			break;
                			case 7: mod = "'" + line + "'";
                			break;
                			case 8: sub = "'" + line + "'";
                			break;
                			case 9: elm = "'" + line + "'";
                			break;
                			}
                		} 
                		String sqllang = "INSERT INTO tbl_msj\nVALUES(";
                		sqllang += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ")";
                		//Aqui genera el registro
                		pw.println(sqllang);
             			pw.flush();
                        s.execute(sqllang);
                	}
                }
            }
            buff.close();
            file.close();
            buff = null;
            file = null;
            
            s.close();
      		conn.close();
      		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
           	conn = DriverManager.getConnection(url);
           	s = conn.createStatement();
      		sql = "UPDATE tbl_bd\n";
      		sql += "SET su = '3'\n";
      		sql += "WHERE nombre = 'FSIBD_" + p(request.getParameter("nombre")) + "';\n";
      		pw.println(sql);
 			pw.flush();
            s.execute(sql);
            s.close();
      		conn.close();
      		//Termina el proceso critico.
      		pw.println("-------------------- FIN DE MENSAJES DE SISTEMA " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss") + " --------------------");
 			pw.println("Creando Archivos emp...");
 			pw.flush();
            File dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")));
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/CE");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/cont");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/nom");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/nom/TFDs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/nom/PDFs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/ven");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/ven/TFDs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/ven/PDFs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/certs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/smtp");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/comp");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/comp/TFDs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/comp/PDFs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/comp/OTRs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/CFDs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/TFDs");
      		dir.mkdir();
      		dir = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/PDFs");
      		dir.mkdir();
      		// ahora los archivos de smtp
      		String msg =
      			"Nota de credito XML y PDF enviado por forseti\n" +
	      		"text/html\n" +
	      		"<html>\n" +
	      		"  <head>\n" +
	      		"    <title>Nota de crédito</title>\n" +
	      		"  </head>\n" +
	      		"  <body>\n" +
	      		"  <h1>Nota de credito</h1>\n" +
	      		"  Una Nota de credito a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
	      		"  </body>\n" +
	      		"</html>";
      		File f = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/smtp/DSV-FORSETI.msg");
      		FileWriter fw = new FileWriter(f);
            fw.write(msg);
            fw.close();
            msg =
            	"Factura XML y PDF enviado por forseti\n" +
	            "text/html\n" +
	            "<html>\n" +
	            "  <head>\n" +
	            "    <title>Factura</title>\n" +
	            "  </head>\n" +
	            "<body>\n" +
	            " <h1>Factura</h1>\n" +
	            " Una factura a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
	            "</body>\n" +
	            "</html>";
            f = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/smtp/FAC-FORSETI.msg");
      		fw = new FileWriter(f);
            fw.write(msg);
            fw.close();
            msg =
            	"Recibo de Nomina XML y PDF enviado por forseti\n" +
            	"text/html\n" +
            	"<html>\n" +
            	"  <head>\n" +
            	"    <title>Recibo de Nomina</title>\n" +
            	"  </head>\n" +
            	"  <body>\n" +
            	"  <h1>Recibo de nomina</h1>\n" +
            	"  [[:nombre:]], tu recibo de nomina te ha sido enviado por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
            	"  </body>\n" +
            	"</html>";
            f = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/smtp/NOM-FORSETI.msg");
      		fw = new FileWriter(f);
            fw.write(msg);
            fw.close();
            msg = 
            	"Remision (traslado) XML y PDF enviado por forseti\n" +
            	"text/html\n" +
            	"<html>\n" +
            	"  <head>\n" +
            	"    <title>Remision (traslado)</title>\n" +
            	"  </head>\n" +
            	"  <body>\n" +
            	"  <h1>Remision (traslado)</h1>\n" +
            	"  Una Remision (traslado) a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.<br>\n" +
            	"  </body>\n" +
            	"</html>";
            f = new File("/usr/local/forseti/emp/" + p(request.getParameter("nombre")) + "/smtp/REM-FORSETI.msg");
      		fw = new FileWriter(f);
            fw.write(msg);
            fw.close();
         
            idmensaje = 0;
      		mensaje = JUtil.Msj("SAF","ADMIN_BD", "DLG", "MSJ-PROCOK");
      		//////////////////////////////////////////
            
      	}
		catch(Throwable e)
      	{
			e.printStackTrace(pw);
      		idmensaje = 3;
      		mensaje = JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "<br>" + e.getMessage();
      	}
		finally
		{
      		try { s.close(); } catch (Exception e) { }
      	    try { conn.close(); } catch (Exception e) { }
		}
		
		
		pw.println("----------------------------- FIN DE LA CREACION ----------------------------------");
		pw.flush();
		pw.close();
		
      	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_CREAR","ADBD|FSIBD_" + p(request.getParameter("nombre")) + "|||",mensaje);
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        return;
    }
 
    public void Restaurar(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String mensaje = "", rdpmensaje = ""; short idmensaje = 0;
    	String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
    	JAdmVariablesSet var = new JAdmVariablesSet(null);
		var.ConCat(true);
		var.m_Where = "ID_Variable = 'RESPALDOS'";
		var.Open();
		JAdmVariablesSet varver = new JAdmVariablesSet(null);
		varver.ConCat(true);
		varver.m_Where = "ID_Variable = 'VERSION'";
		varver.Open();
		//Extrae el nombre de la empresa respaldada
		int index = request.getParameter("respaldo").indexOf('-');
		int indexzip = request.getParameter("respaldo").indexOf('.');
		String bdresp = request.getParameter("respaldo").substring(6, index);
		String nomdir = request.getParameter("respaldo").substring(0, indexzip);
		//Genera el directorio del respaldo para extraer ahi
		File newdir = new File(var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir);
		newdir.mkdir();
		//Descomprime el respaldo en el nuevo directorio
		JZipUnZipUtil fzip = new JZipUnZipUtil();
		fzip.unZipDir(var.getAbsRow(0).getVAlfanumerico() + "/" + request.getParameter("respaldo"), var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir);
		
		String urlver = var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir + "/forseti.version";
		String urldump = var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir + "/" + nomdir + ".dump";
		String urlconf = var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir + "/" + nomdir + ".conf";
		String urlemp = var.getAbsRow(0).getVAlfanumerico() + "/" + nomdir + "/" + bdresp + "/";
    	System.out.println("DUMP: " + urldump + "\nCONF: " + urlconf + "\nEMP: " + urlemp);
    	Calendar fecha = GregorianCalendar.getInstance(); 
		String path = "/usr/local/forseti/log/REST-FSIBD_" + request.getParameter("nombre") + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
		FileWriter filewri		= new FileWriter(path, true);
		PrintWriter pw			= new PrintWriter(filewri);

		try
    	{
			pw.println("----------------------------------------------------------------------------");
			pw.println("             " + "RESTAURANDO LA BASE DE DATOS Y ARCHIVOS EMP: " + request.getParameter("nombre") + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.println("----------------------------------------------------------------------------");
			pw.flush();
			
			//Revisa si la versión es la misma que la de este servidor
			FileReader ver = new FileReader(urlver);
            BufferedReader buff = new BufferedReader(ver);
            boolean eof = false;
            while(!eof)
            {
                String line = buff.readLine();
                if(line == null)
                	eof = true;
                else //Aqui verifica que la linea del archivo forseti.version sea igual a la variable de versión de este servidor forseti
                {
                	if(!varver.getAbsRow(0).getVAlfanumerico().equals(line))
                	{
                		String 
                		e = 	"ERROR: No se puede restaurar esta empresa porque el respaldo pertenece a una versión diferente a la del servidor\n";
                		e +=	"Version del Servidor: " + varver.getAbsRow(0).getVAlfanumerico() + " Versión del respaldo de la empresa: " + line + "\n";
                		e += 	"Si es indispensable la restauración, deberás primero instalar el servidor con la versión en la que se respaldó la empresa y posteriormente restaurarla";
                		pw.println(e);
            			pw.flush();
            			buff.close();
            			throw new Throwable(e);
                	}
                }
            }
            buff.close();
			///////////////////////////////
			
			String ERROR = "";
			JFsiScript sc = new JFsiScript();
			String CONTENT = "rsync -av --stats " + urlemp + " /usr/local/forseti/emp/" + request.getParameter("nombre");
			System.out.println(CONTENT);
			sc.setContent(CONTENT);
			sc.executeCommand();
			ERROR += sc.getError();
			if(!ERROR.equals(""))
			{
				//System.out.println(ERROR);
				idmensaje = 3; 
				mensaje = "ERROR al restaurar desde RSYNC: " + ERROR;
				pw.println("ERROR al restaurar desde RSYNC: " + ERROR);
				pw.flush();
			}
			else
			{
				mensaje = "La restauracion de los archivos se genero con exito en: /usr/local/forseti/emp/" + request.getParameter("nombre");
				pw.println("La restauracion de los archivos se genero con éxito en: /usr/local/forseti/emp/" + request.getParameter("nombre"));
				pw.flush();
			}
	
			pw.println("FINALIZANDO RESTAURACION DE ARCHIVOS EMP: " + request.getParameter("nombre") + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.flush();
						
			if(idmensaje == 0)
			{
				//ahora genera el registro a partir del .conf
				String insert = "INSERT INTO tbl_bd(id_bd, nombre, usuario, password";
				String values = "VALUES (default, 'FSIBD_" + p(request.getParameter("nombre")) + "','" + p(request.getParameter("nombre")).toLowerCase() + "','" + q(request.getParameter("password")) + "'"; 
				FileReader file         = new FileReader(urlconf);
				buff     = new BufferedReader(file);
				eof             = false;
				while(!eof)
				{
					String line = buff.readLine();
					if(line == null)
						eof = true;
					else
					{
						try
						{
							StringTokenizer st = new StringTokenizer(line,"|");
							String key         = st.nextToken();
							String value       = st.nextToken();
							if(key.equals("id_bd") || key.equals("nombre") || key.equals("usuario") || key.equals("password"))
								continue;
							insert += ", " + key;
							values += ",'" + p(value) + "'"; 
						}
						catch(NoSuchElementException e)
						{
							//System.out.println(e.getMessage());
							continue;
						}
					}
				}
				buff.close();
				file.close();
				buff = null;
				file = null;
				String sql = insert + ")\n" + values + ");\n";
				String driver = "org.postgresql.Driver";
				Class.forName(driver).newInstance();
	    		// Apartir de aqui es crítico el proceso
	    		// Inserta registro nuevo de BD - SU 0
	    		String url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
	    		Connection conn = DriverManager.getConnection(url);
	    		Statement s = conn.createStatement();
	    		System.out.println("INSERTANDO EN TBL_BD SU 0...\n" + sql);
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		pw.println("FINALIZANDO EL REGISTRO DE BASE DE DATOS: FSIBD_" + request.getParameter("nombre") + " EN FORSETI_ADMIN " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
				
	    		// Genera el usuario
	    		url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
	    		conn = DriverManager.getConnection(url);
	    		s = conn.createStatement();
	    		sql =	"CREATE ROLE " + p(request.getParameter("nombre")).toLowerCase() + " \n" +
	    				"LOGIN ENCRYPTED PASSWORD '" + q(request.getParameter("password")) + "' \n" +
	    				"NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE NOREPLICATION;\n" +
	    				"GRANT " + p(request.getParameter("nombre")).toLowerCase() +  " TO forseti;";
	    	    
	    		System.out.println("CREANDO EL USUARIO PGSQL DUENO DE ESTA BASE DE DATOS...");
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		// Actualiza registro de BD: Usuario PGSQL OK - SU 1
	    		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
	    		conn = DriverManager.getConnection(url);
	    		s = conn.createStatement();
	    		sql = "UPDATE tbl_bd\n";
	    		sql += "SET su = '1'\n";
	    		sql += "WHERE nombre = 'FSIBD_" + p(request.getParameter("nombre")) + "';\n";
	    		System.out.println("ACTUALIZANDO TBL_BD SU 1...");
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		pw.println("FINALIZANDO CREACION DE USUARIO PRINCIPAL DE ESTA BASE DE DATOS: " + request.getParameter("nombre") + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
				
	    		// Genera la base de datos del usuario
	    		url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
	    		conn = DriverManager.getConnection(url);
	    		s = conn.createStatement();
	    		sql =
	    		"CREATE DATABASE \"FSIBD_" + p(request.getParameter("nombre")) + "\"\n" +
            	"	WITH OWNER = " + p(request.getParameter("nombre")).toLowerCase() + "\n" +
            	"	TEMPLATE = template0\n" +
            	"	ENCODING = 'UTF8'\n" +
            	"	LC_COLLATE = 'es_MX.UTF-8'\n" +
            	"	LC_CTYPE = 'es_MX.UTF-8'\n" +
            	"	CONNECTION LIMIT = -1;";
	    		System.out.println("CREANDO LA BASE DE DATOS PRINCIPAL DEL USUARIO...");
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		// Actualiza registro de BD: Empresa generada vacia OK - SU 2
	    		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
	    		conn = DriverManager.getConnection(url);
	    		s = conn.createStatement();
	    		sql = "UPDATE tbl_bd\n";
	    		sql += "SET su = '2'\n";
	    		sql += "WHERE nombre = 'FSIBD_" + p(request.getParameter("nombre")) + "';\n";
	    		System.out.println("ACTUALIZANDO TBL_BD SU 2...");
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		pw.println("FINALIZANDO CREACION DE LA BASE DE DATOS: FSIBD_" + request.getParameter("nombre") + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.println("Generando el proceso de restauracion, a continuación checa las alertas y errores que pudiera tener este respaldo...");
	    		pw.flush();
				
	    		// Se conecta a la base de datos principal creada para generar desde su respaldo
	    		ERROR = "";
				CONTENT = "PGPASSWORD=" + q(request.getParameter("password")) + " psql --username=" + p(request.getParameter("nombre")).toLowerCase() + " --host=" + addr + " --port=" + port + " --dbname=FSIBD_" + p(request.getParameter("nombre")) + " --file=" + urldump;
				System.out.println(CONTENT);
				sc.setVerbose(true);
				sc.setContent(CONTENT);
				sc.executeCommand();
				ERROR += sc.getError();
				
				// Actualiza registro de BD: ESTRUCTURA DE LA EMPRESA COMPLETADA OK - SU 3
	    		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
	    		conn = DriverManager.getConnection(url);
	    		s = conn.createStatement();
	    		sql = "UPDATE tbl_bd\n";
	    		sql += "SET su = '3'\n";
	    		sql += "WHERE nombre = 'FSIBD_" + p(request.getParameter("nombre")) + "';\n";
	    		System.out.println("ACTUALIZANDO TBL_BD SU 3...");
	    		s.execute(sql);
	    		s.close();
	    		conn.close();
	    		//Termina el proceso critico.
	    	    
	    		if(!ERROR.equals(""))
				{
					idmensaje = 1;
					mensaje = "La restauracion de la base de datos arrojo avisos de error o de precaucion:<br>" + ERROR;
					rdpmensaje = "La restauracion de la base de datos arrojo avisos de error o de precaucion, checalos en los archivos de registro";
					pw.println("La restauracion de la base de datos arrojo los siguientes avisos de error o de precaucion:\n" + ERROR);
					pw.flush();
				}
	    		else
	    		{
	    			idmensaje = 0;
	    			mensaje = JUtil.Msj("SAF","ADMIN_BD", "DLG", "MSJ-PROCOK");
	    			rdpmensaje = mensaje;
	    		}
			}
			
		}
    	catch(Throwable e)
    	{
    		idmensaje = 3;
    		mensaje = JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "<br>" + e.toString();
    		rdpmensaje = JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "<br>" + e.getMessage();
    		pw.println(JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "\n" + e.toString());
			pw.flush();
    	}
		
		pw.println("BORRANDO ARCHIVOS TEMPORALES DE RESTAURACION...");
		pw.flush();
		
		File[] currList;
		Stack<File> stack = new Stack<File>();
		stack.push(newdir);
		while (!stack.isEmpty()) 
		{
			if (stack.lastElement().isDirectory()) 
			{
				currList = stack.lastElement().listFiles();
				if (currList.length > 0) 
				{
					for (File curr: currList) 
					{
						stack.push(curr);
					}
				} 
				else 
				{
					stack.pop().delete();
				}
			} 
			else 
			{
				stack.pop().delete();
			}
		} 
		pw.println("----------------------------- FIN DE LA RESTAURACION ----------------------------------");
		pw.flush();
		pw.close();
		
    	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_PROPIEDADES","ADBD|FSIBD_" + p(request.getParameter("nombre")) + "|||",q(rdpmensaje));
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	return;
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	JBDRegistradasSet set = new JBDRegistradasSet(null);
    	set.ConCat(true);
    	set.m_Where = "ID_BD = '" + p(request.getParameter("id")) + "'";
    	set.Open();
    	String database =  p(set.getAbsRow(0).getNombre());
    	String usuario = p(set.getAbsRow(0).getUsuario());
    	String mensaje = ""; short idmensaje = -1;
    	String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
    	//BORRA LA BASE DE DATOS MANUALMENTE. (NO A TRAVES DE PROCEDIMIENTO ALMACENADO)
    	// genera una conexion natural con los datos establecidos
    	Connection conn = null;
		Statement s = null;
		
		Calendar fecha = GregorianCalendar.getInstance(); 
		String pathlog = "/usr/local/forseti/log/ELIM-" + database + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
		FileWriter filewri		= new FileWriter(pathlog, true);
		PrintWriter pw			= new PrintWriter(filewri);
		
		try
    	{
			pw.println("----------------------------------------------------------------------------");
			pw.println("             " + "ELIMINANDO LA BASE DE DATOS Y ARCHIVOS EMP: " + database.substring(6) + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.println("----------------------------------------------------------------------------");
			pw.flush();
			
			String driver = "org.postgresql.Driver";
        	Class.forName(driver).newInstance();
    		// Apartir de aqui es crítico el proceso
        	// Borra la base de datos del usuario
        	String url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
        	conn = DriverManager.getConnection(url);
        	s = conn.createStatement();
        	String sql = "DROP DATABASE \"" + database + "\";\n";
        	pw.println("ELIMINANDO LA BASE DE DATOS PRINCIPAL DEL USUARIO...");
        	pw.flush();
        	s.execute(sql);
        	// Elimina el usuario
        	sql = "DROP ROLE " + usuario +  "; ";
        	pw.println("BORRANDO EL USUARIO PGSQL DUEÑO DE ESTA BASE DE DATOS...");
        	pw.flush();
        	s.execute(sql);
        	// borra registro de BD
        	url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
        	conn = DriverManager.getConnection(url);
        	s = conn.createStatement();
        	sql = "DELETE FROM tbl_bd\n";
        	sql += "WHERE id_bd = '" + q(request.getParameter("id")) + "';\n";
        	pw.println("ACTUALIZANDO TBL_BD borrando registro");
        	pw.flush();
        	s.execute(sql);
        	
        	//Borra los archivos de la bd
        	String path = "/usr/local/forseti/emp/" + database.substring(6);
        	pw.println("BORRANDO ARCHIVOS: " + path);
        	pw.flush();
        	File dir = new File(path);
        	File[] currList;
        	Stack<File> stack = new Stack<File>();
        	stack.push(dir);
        	while (!stack.isEmpty()) 
        	{
        		if (stack.lastElement().isDirectory()) 
        		{
        	        currList = stack.lastElement().listFiles();
        	        if (currList.length > 0) 
        	        {
        	            for (File curr: currList) 
        	            {
        	                stack.push(curr);
        	            }
        	        } 
        	        else 
        	        {
        	        	stack.pop().delete();
        	        }
        	    } 
        		else 
        		{
        			stack.pop().delete();
        	    }
        	}
      		//Termina el proceso critico.
    		idmensaje = 0;
    		mensaje = JUtil.Msj("SAF","ADMIN_BD", "DLG", "MSJ-PROCOK",3);
    		pw.println(JUtil.Msj("SAF","ADMIN_BD", "DLG", "MSJ-PROCOK",3));
			pw.flush();
    		//////////////////////////////////////////
          
    	}
    	catch(Throwable e)
    	{
    		idmensaje = 3;
    		mensaje = JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "<br>" + e.getMessage();
    		pw.println(JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "\n" + e.toString());
			pw.flush();
    	}
      	finally
      	{
      		try { s.close(); } catch (Exception e) { }
      	    try { conn.close(); } catch (Exception e) { }
      	}
		
		pw.println("----------------------------- FIN DE LA ELIMINACION ----------------------------------");
		pw.flush();
		pw.close();
		
    	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_ELIMINAR","ADBD|" + database + "|||",mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	return;
  }
  
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public short CambioBase(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException
    	    {
    	      	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	              
    	       	HttpSession ses = request.getSession(true);
    	       	JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
    	 
    	       	idmensaje = rec.cambioBase(request, mensaje);
    	       	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	       	return idmensaje;
    	    }
    	    
   public short CambioTabla(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
	   short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	              
	   HttpSession ses = request.getSession(true);
	   JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
    	 
	   idmensaje = rec.cambioTabla(request, mensaje);
	   getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

	   return idmensaje;
   }
    	    
   public short AgregarCabecero(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
   {
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
    	              
    	HttpSession ses = request.getSession(true);
    	JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
    	 
    	idmensaje = rec.agregaCabecero(request, mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());

    	return idmensaje;
   }
    	    
   public void Probar(HttpServletRequest request, HttpServletResponse response, String nivel)
		throws ServletException, IOException
   {
	   StringBuffer res = new StringBuffer();
	   String sqlL = "";
	   String sqlCL = "";
    	        
	   HttpSession ses = request.getSession(true);
	   JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
    	       	
	   if(nivel.equals("L1"))
	   {
		   if(!rec.getStatusL1().equals("ND"))
			   sqlL = rec.Probar("L1");
		   if(!rec.getStatusCL1().equals("ND"))
			   sqlCL = rec.Probar("CL1");
	   }
	   else if(nivel.equals("L2"))
	   {
		   if(!rec.getStatusL2().equals("ND"))
			   sqlL = rec.Probar("L2");
		   if(!rec.getStatusCL2().equals("ND"))
			   sqlCL = rec.Probar("CL2");
	   }
	   else if(nivel.equals("L3"))
	   {
		   if(!rec.getStatusL3().equals("ND"))
			   sqlL = rec.Probar("L3");
		   if(!rec.getStatusCL1().equals("ND"))
			   sqlCL = rec.Probar("CL3");
	   }
    		    
	   //System.out.println("PROBAR BDP: " + rec.getBDP());
	   Connection con = JAccesoBD.getConexion(rec.getBDP());
	   if(con != null)
	   {
		   //System.out.println("sqlL: " + sqlL);
		   //System.out.println("sqlCL: " + sqlCL);
		   if(!sqlL.equals(""))
		   {
			   try
			   {
				   Statement s    = con.createStatement();
				   String resultados = "<table width='100%' border='1' cellpadding='1' cellspacing='0' bordercolor='#000099' bgcolor='#FFFFFF'>";
				   ResultSet rs   = s.executeQuery(sqlL);
				   ResultSetMetaData rsmd = rs.getMetaData();    
				   resultados += "<tr bgcolor='#000099' class='titChico'> ";
				   for(int col = 1; col <= rsmd.getColumnCount(); col++)
				   {
					   resultados += "<td>" + rsmd.getColumnName(col) + "</td>";  
				   }
				   resultados += "</tr>";
				   while(rs.next())
				   {
					   resultados += "<tr>";
					   for(int col = 1; col <= rsmd.getColumnCount(); col++)
					   {
						   resultados += "<td>" + rs.getString(col) + "</td>";  
					   }
					   resultados += "</tr>";
				   }
				   resultados += "</table>";
				   //System.out.println("resultados: " + resultados);		   
				   if(nivel.equals("L1"))
				   {
					   rec.setCols("L1", rsmd, request); 
					   rec.setStatusL1("PRO");
					   request.setAttribute("SCL1", resultados);
				   }
				   else if(nivel.equals("L2"))
				   {
					   rec.setCols("L2", rsmd, request); 
					   rec.setStatusL2("PRO");
					   request.setAttribute("SCL2", resultados);
				   }
				   else if(nivel.equals("L3"))
				   {
					   rec.setCols("L3", rsmd, request); 
					   rec.setStatusL3("PRO");
					   request.setAttribute("SCL3", resultados);
				   }
				   s.close();
			   }
			   catch(SQLException e)
			   {
				   res.append(e.getMessage());
				   if(nivel.equals("L1"))
				   {
					   rec.setStatusL1("ERR");
					   request.setAttribute("SCL1", res.toString());
				   }
				   else if(nivel.equals("L2"))
				   {
					   rec.setStatusL2("ERR");
					   request.setAttribute("SCL2", res.toString());
				   }
				   else if(nivel.equals("L3"))
				   {
					   rec.setStatusL3("ERR");
					   request.setAttribute("SCL3", res.toString());
				   }
				   JAccesoBD.liberarConexion(con);
				   irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
				   return;
			   }
		   }
    		    	   
		   if(!sqlCL.equals(""))
		   {
			   try
			   {
				   Statement cs    = con.createStatement();
				   String cresultados = "<table width='100%' border='1' cellpadding='1' cellspacing='0' bordercolor='#FF6600' bgcolor='#FFFFFF'>";
				   ResultSet crs   = cs.executeQuery(sqlCL);
				   ResultSetMetaData crsmd = crs.getMetaData();    
				   cresultados += "<tr bgcolor='#FF6600' class='titChico'> ";
				   for(int col = 1; col <= crsmd.getColumnCount(); col++)
				   {
					   cresultados += "<td>" + crsmd.getColumnName(col) + "</td>";  
				   }
				   cresultados += "</tr>";
				   while(crs.next())
				   {
					   cresultados += "<tr>";
					   for(int col = 1; col <= crsmd.getColumnCount(); col++)
					   {
						   cresultados += "<td>" + crs.getString(col) + "</td>";  
					   }
					   cresultados += "</tr>";
				   }
				   cresultados += "</table>";
				   //System.out.println("cresultados: " + cresultados);
				   if(nivel.equals("L1"))
				   {
					   rec.setCols("CL1", crsmd, request); 
					   rec.setStatusCL1("PRO");
					   request.setAttribute("CSCL1", cresultados);
				   }
				   else if(nivel.equals("L2"))
				   {
					   rec.setCols("CL2", crsmd, request); 
					   rec.setStatusCL2("PRO");
					   request.setAttribute("CSCL2", cresultados);
				   }
				   else if(nivel.equals("L3"))
				   {
					   rec.setCols("CL3", crsmd, request); 
					   rec.setStatusCL3("PRO");
					   request.setAttribute("CSCL3", cresultados);
				   }
				   cs.close();
			   }
			   catch(SQLException e)
			   {
				   res.append(e.getMessage());
				   if(nivel.equals("L1"))
				   {
					   rec.setStatusCL1("ERR");
					   request.setAttribute("CSCL1", res.toString());
				   }
				   else if(nivel.equals("L2"))
				   {
					   rec.setStatusCL2("ERR");
					   request.setAttribute("CSCL2", res.toString());
				   }
				   else if(nivel.equals("L3"))
				   {
					   rec.setStatusCL3("ERR");
					   request.setAttribute("CSCL3", res.toString());
				   }
				   
				   JAccesoBD.liberarConexion(con);
				   irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
				   return;
			   }
		   }
		   JAccesoBD.liberarConexion(con);
		   irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
		   return;
	   }
	   else
	   {
		   if(nivel.equals("L1"))
			   request.setAttribute("SCL1", "ERROR EN LA CONEXION A LA BASE DE DATOS: " + rec.getBDP()); 
		   if(nivel.equals("L2"))
			   request.setAttribute("SCL2", "ERROR EN LA CONEXION A LA BASE DE DATOS: " + rec.getBDP()); 
		   if(nivel.equals("L3"))
			   request.setAttribute("SCL3", "ERROR EN LA CONEXION A LA BASE DE DATOS: " + rec.getBDP()); 
		   
		   irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
		   return;
	   }
    		           
   }
    	    
   public void Actualizar(HttpServletRequest request, HttpServletResponse response)
    	    	throws ServletException, IOException
   {
	   short idmensaje = -1; String mensaje = "";
    	    	
	   HttpSession ses = request.getSession(true);
	   JRepGenSes rec = (JRepGenSes)ses.getAttribute("rep_gen_dlg");
	   	   
	   String tbl = "CREATE LOCAL TEMPORARY TABLE _tmp_reports_sentences\n";
	   tbl += "(\n";
	   tbl += "	id_report smallint NOT NULL,\n";
	   tbl += "  id_sentence smallint NOT NULL,\n";
	   tbl += "  id_iscompute smallint NOT NULL,\n";
	   tbl += "  select_clause character varying(8000) NOT NULL,\n";
	   tbl += "  tabprintpnt numeric(5,2),\n";
	   tbl += "  format character varying(254)\n";
	   tbl += ");\n";
	   tbl += "CREATE LOCAL TEMPORARY TABLE _tmp_reports_sentences_columns\n";
	   tbl += "(\n";
	   tbl += "  id_report smallint NOT NULL,\n";
	   tbl += "  id_sentence smallint NOT NULL,\n";
	   tbl += "  id_iscompute smallint NOT NULL,\n";
	   tbl += "  id_column smallint NOT NULL,\n";
	   tbl += "  colname character varying(254) NOT NULL,\n";
	   tbl += "  binddatatype character varying(50) NOT NULL,\n";
	   tbl += "  willshow bit(1) NOT NULL,\n";
	   tbl += "  format character varying(254) NOT NULL,\n";
	   tbl += "  ancho numeric(5,2) NOT NULL,\n";
	   tbl += "  alinhor character varying(20),\n";
	   tbl += "  fgcolor character(6)\n";
	   tbl += ");\n";
	   tbl += "CREATE LOCAL TEMPORARY TABLE _tmp_reports_filter\n";
	   tbl += "(\n";
	   tbl += "  id_report smallint NOT NULL,\n";
	   tbl += "  id_column smallint NOT NULL,\n";
	   tbl += "  instructions character varying(254) NOT NULL,\n";
	   tbl += "  isrange bit(1) NOT NULL,\n";
	   tbl += "  pridataname character varying(254) NOT NULL,\n";
	   tbl += "  pridefault character varying(8000),\n";
	   tbl += "  secdataname character varying(254) NOT NULL,\n";
	   tbl += "  secdefault character varying(8000),\n";
	   tbl += "  binddatatype character varying(50) NOT NULL,\n";
	   tbl += "  fromcatalog bit(1) NOT NULL,\n";
	   tbl += "  select_clause character varying(8000) NOT NULL\n";
	   tbl += ");\n";
    	
	   if(rec.getStatusL1().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",1,0,'" + q(rec.getSCL1()) + "'," + rec.getTabPrintPntL1() + ",null );\n";
	   }
	   if(rec.getStatusCL1().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",1,1,'" + q(rec.getCSCL1()) + "'," + rec.getTabPrintPntCL1() + ",null );\n";
	   }
	   if(rec.getStatusL2().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",2,0,'" + q(rec.getSCL2()) + "'," + rec.getTabPrintPntL2() + ",null );\n";
	   }
	   if(rec.getStatusCL2().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",2,1,'" + q(rec.getCSCL2()) + "'," + rec.getTabPrintPntCL2() + ",null );\n";
	   }
	   if(rec.getStatusL3().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",3,0,'" + q(rec.getSCL3()) + "'," + rec.getTabPrintPntL3() + ",null );\n";
	   }
	   if(rec.getStatusCL3().equals("PRO"))
	   {
		   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES\n";
		   tbl += "VALUES( " + rec.getID_Report() + ",3,1,'" + q(rec.getCSCL3()) + "'," + rec.getTabPrintPntCL3() + ",null );\n";
	   }
    			
	   // Ahora revisa por las columnas
	   if(rec.getStatusL1().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("L1"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",1,0," + (i+1) + ",'" + rec.getColsPart("L1", i).getColName() +
    						"','" + rec.getColsPart("L1", i).getBindDataType() + "','" + (rec.getColsPart("L1", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("L1", i).getFormat() + "'," + rec.getColsPart("L1", i).getAncho() + 
    						",'" + rec.getColsPart("L1", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   if(rec.getStatusCL1().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("CL1"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",1,1," + (i+1) + ",'" + rec.getColsPart("CL1", i).getColName() +
    						"','" + rec.getColsPart("CL1", i).getBindDataType() + "','" + (rec.getColsPart("CL1", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("CL1", i).getFormat() + "'," + rec.getColsPart("CL1", i).getAncho() + 
    						",'" + rec.getColsPart("CL1", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   if(rec.getStatusL2().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("L2"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",2,0," + (i+1) + ",'" + rec.getColsPart("L2", i).getColName() +
    						"','" + rec.getColsPart("L2", i).getBindDataType() + "','" + (rec.getColsPart("L2", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("L2", i).getFormat() + "'," + rec.getColsPart("L2", i).getAncho() + 
    						",'" + rec.getColsPart("L2", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   if(rec.getStatusCL2().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("CL2"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",2,1," + (i+1) + ",'" + rec.getColsPart("CL2", i).getColName() +
    						"','" + rec.getColsPart("CL2", i).getBindDataType() + "','" + (rec.getColsPart("CL2", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("CL2", i).getFormat() + "'," + rec.getColsPart("CL2", i).getAncho() + 
    						",'" + rec.getColsPart("CL2", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   if(rec.getStatusL3().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("L3"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",3,0," + (i+1) + ",'" + rec.getColsPart("L3", i).getColName() +
    						"','" + rec.getColsPart("L3", i).getBindDataType() + "','" + (rec.getColsPart("L3", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("L3", i).getFormat() + "'," + rec.getColsPart("L3", i).getAncho() + 
    						",'" + rec.getColsPart("L3", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   if(rec.getStatusCL3().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumCols("CL3"); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_SENTENCES_COLUMNS\n";
			   tbl += "VALUES( " + rec.getID_Report() + ",3,1," + (i+1) + ",'" + rec.getColsPart("CL3", i).getColName() +
    						"','" + rec.getColsPart("CL3", i).getBindDataType() + "','" + (rec.getColsPart("CL3", i).getWillShow() ? 0 : 1) +
    						"','" + rec.getColsPart("CL3", i).getFormat() + "'," + rec.getColsPart("CL3", i).getAncho() + 
    						",'" + rec.getColsPart("CL3", i).getAlinHor() + "','000000' );\n";
		   }
	   }
	   //AHORA INSERTA EL FILTRO return
	   if(rec.getStatusL1().equals("PRO"))
	   {
		   for(int i = 0; i < rec.getNumFiltros(); i++)
		   {
			   tbl += "INSERT INTO _TMP_REPORTS_FILTER\n";
			   tbl += "VALUES( " + rec.getID_Report() + "," + (i+1) + ",'" + p(rec.getFiltro(i).getInstructions()) + "','" +
    						(rec.getFiltro(i).getIsRange() ? 1 : 0) + "','" + rec.getFiltro(i).getPriDataName() + "','" + rec.getFiltro(i).getPriDefault() + "','" +
    						rec.getFiltro(i).getSecDataName() + "','" + rec.getFiltro(i).getSecDefault() + "','" + rec.getFiltro(i).getBindDataType() + "','" +
    						(rec.getFiltro(i).getFromCatalog() ? 1 : 0) + "','" + (!rec.getFiltro(i).getFromCatalog() ? "0" : rec.getFiltro(i).getID_Catalogo()) + "' );\n";
		   }
	   }
    	
	       			
	   String drop = "DROP TABLE _TMP_REPORTS_SENTENCES;\nDROP TABLE _TMP_REPORTS_SENTENCES_COLUMNS;\nDROP TABLE _TMP_REPORTS_FILTER;\n";
    	
	   String str = "";
	   if(rec.getID_Report() == rec.getID_ReportPlnt()) // Significa cambios al reporte, por lo tanto primero lo borra
	   {
		   str += "DELETE FROM TBL_REPORTS_SENTENCES\n";
		   str += "WHERE ID_Report = '" + rec.getID_Report() + "';\n";
		   str += "DELETE FROM TBL_REPORTS_SENTENCES_COLUMNS\n";
		   str += "WHERE ID_Report = '" + rec.getID_Report() + "';\n";
		   str += "DELETE FROM TBL_REPORTS_FILTER\n";
		   str += "WHERE ID_Report = '" + rec.getID_Report() + "';\n";
		   str += "UPDATE TBL_REPORTS\n";
		   str += "SET description = '" + p(rec.getDescripcion()) + "', tipo = '" + p(rec.getTipo()) + "', titulo = '" + rec.obtEncabezado("Titulo") + "', encl1 = '" + rec.obtEncabezado("EncL1") + "', encl2 = '" + rec.obtEncabezado("EncL2") + "', encl3 = '" + rec.obtEncabezado("EncL3") + "',\n";
		   str += "    l1 = '" + rec.obtEncabezado("L1") + "', l2 = '" + rec.obtEncabezado("L2") + "', l3 = '" + rec.obtEncabezado("L3") + "', cl1 = '" + rec.obtEncabezado("CL1") + "', cl2 = '" + rec.obtEncabezado("CL2") + "',\n";
		   str += "    cl3 = '" + rec.obtEncabezado("CL3") + "', hw = '"+ rec.getHW() + "', vw = '"+ rec.getVW() + "', subtipo = '" + p(rec.getTipo()) + "', clave = '" + p(rec.getClave()) + "', graficar = '" + (rec.getGraficar() ? "1" : "0") + "'\n";
		   str += "WHERE ID_Report = '" + rec.getID_Report() + "';\n";
	   }
	   else
	   {
		   str += "INSERT INTO TBL_REPORTS\n";
		   str += "VALUES( '" + rec.getID_Report() + "','" + p(rec.getDescripcion()) + "','" + p(rec.getTipo()) + "','" + rec.obtEncabezado("Titulo") + "','" + rec.obtEncabezado("EncL1") + "','" + rec.obtEncabezado("EncL2") + "','" + rec.obtEncabezado("EncL3") + 
	    		    	"','" + rec.obtEncabezado("L1") + "','" + rec.obtEncabezado("L2") + "','" + rec.obtEncabezado("L3") + "','" + rec.obtEncabezado("CL1") + "','" + rec.obtEncabezado("CL2") + 
	    		    	"','" + rec.obtEncabezado("CL3") + "'," + rec.getHW() + "," + rec.getVW() + ",'" + p(rec.getTipo()) + "','" + p(rec.getClave()) + "','" + (rec.getGraficar() ? "1" : "0") + "');\n";
	   }

	   str += "INSERT INTO TBL_REPORTS_SENTENCES\n"; 
	   str += "SELECT * FROM _TMP_REPORTS_SENTENCES;\n";
	   str += "INSERT INTO TBL_REPORTS_SENTENCES_COLUMNS\n"; 
	   str += "SELECT * FROM _TMP_REPORTS_SENTENCES_COLUMNS;\n";
	   str += "INSERT INTO TBL_REPORTS_FILTER\n"; 
	   str += "SELECT * FROM _TMP_REPORTS_FILTER;\n";
	   
	   if(request.getParameter("FSI_PANTALLA") != null)
	   {
		   doDebugSQL(request,response, tbl + "\n" + str + "\n" + drop);
		   return;
	   }
	   else
	   {
		   Connection con = null;
		   Statement s = null;
		   try
		   {
			   con = JAccesoBD.getConexion(rec.getBD());
		       s    = con.createStatement();
		       s.executeUpdate(tbl);
		       s.executeUpdate(str);
		       s.executeUpdate(drop);
		       idmensaje = 0;
		       mensaje = "El reporte se generó con éxito";
		    }
		    catch(SQLException e)
		    {
		    	e.printStackTrace();
		    	idmensaje = 3;
		    	mensaje = "ERROR SQL: " + e.getMessage();
		    }
		    finally
		    {
		    	try { s.close(); } catch (SQLException e) {}
		    	try { con.close();	} catch (SQLException e) {}
		    }
		   
	   }		
		   
	   RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"ADMIN_BD_REPGEN","ADBD|" + rec.getBD() + "/" + rec.getID_Report() + "|||",mensaje);
       getSesion(request).setID_Mensaje(idmensaje, mensaje);
       irApag("/forsetiadmin/administracion/adm_bd_dlg_repgen.jsp", request, response);
       
    }

    
}
