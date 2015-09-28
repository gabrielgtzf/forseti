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
package fsi_admin.reportes;
import java.io.*;
//import java.util.Calendar;
//import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.hssf.util.CellRangeAddress;
//import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import Acme.JPM.Encoders.GifEncoder;

import fsi_admin.JFsiForsetiApl;
import forseti.JFsiGrafAreas;
import forseti.JFsiGrafCirc;
import forseti.JFsiGrafLineas;
import forseti.JFsiGraficas;
import forseti.JRepCellStyles;
import forseti.sets.JReportesBind2Set;
import forseti.sets.JReportesBind3Set;
import forseti.sets.JReportesBindFSet;
import forseti.sets.JReportesCompL1Set;
import forseti.sets.JReportesFiltroSet;
import forseti.sets.JReportesLevel1;
import forseti.sets.JReportesSentenciasColumnasSet;
import forseti.sets.JReportesSentenciasSet;
import forseti.sets.JReportesSet;
import forseti.sets.JUsuariosSubmoduloNominaPerm;
import forseti.sets.REP_LEVEL1;
import forseti.sets.REP_LEVEL2;
import forseti.sets.JListasCatalogosSet;
import forseti.sets.JUsuariosSubmoduloVentasPerm;
import forseti.sets.JUsuariosSubmoduloComprasPerm;
import forseti.sets.JUsuariosSubmoduloProduccionPerm;
import forseti.sets.JUsuariosSubmoduloBancosPerm;
import forseti.sets.JUsuariosSubmoduloBodegasPerm;
import forseti.JFsiGrafBarras;
import forseti.JUtil;

@SuppressWarnings({ "serial", "deprecation" })
public class JReportesDlg extends JFsiForsetiApl
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    super.doPost(request,response);

    String reps_reportes_dlg = "";
    request.setAttribute("reps_reportes_dlg",reps_reportes_dlg);

    String mensaje = ""; short idmensaje = -1;

    if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
    {
      if(request.getParameter("proceso").equals("CARGAR_REPORTE"))
      {
        if(request.getParameter("REPID") != null)
        {
           String[] valoresParam = request.getParameterValues("REPID");
           if(valoresParam.length == 1)
           {
             // Verifica que este usuario tenga permiso del reporte
             if (!getSesion(request).getPermisoReporte(Integer.parseInt(request.getParameter("REPID"))))
             {
          	   idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REPS_REPORTES");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("SAF",getSesion(request).getConBD(),"ER",getSesion(request).getID_Usuario(),"REPS_REPORTES","RPRP|" + request.getParameter("REPID") + "|||",mensaje);
                 irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
                 return;
             }
             
             if (request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("GENERAR"))
             {
               // Solicitud de envio a procesar
          	 if(VerificarFiltro(request, response))
          	 {
          		 try { CargarReporte(request, response); } catch (DocumentException e) { e.printStackTrace(); }
          		 return;
          	 }
             }
             else
             {
               // Como el subproceso no es GENERAR, abre la ventana del proceso de FILTRADO para cargar `por primera vez
          	 JReportesSet m_RepSet = new JReportesSet(request);
          	 m_RepSet.m_Where = "ID_Report = '" + p(request.getParameter("REPID")) + "'";
          	 m_RepSet.ConCat(true);
          	 m_RepSet.Open();
          	 
          	 JReportesBindFSet set = new JReportesBindFSet(request);
          	 set.m_Where = "ID_Report = '" + p(request.getParameter("REPID")) + "'";
          	 set.m_OrderBy = "ID_Column ASC";
          	 set.ConCat(true);
          	 set.Open();

          	 String fechasql1, fechasql2;
          	 
          	 fechasql1 = "dd/MMM/yyyy";
          	 fechasql2 = "ddmmmyyyy";
                               
          	 request.setAttribute("BindFSet", set);
          	 request.setAttribute("fechasql1", fechasql1);
          	 request.setAttribute("fechasql2", fechasql2);
               
          	 getSesion(request).setID_Mensaje(idmensaje, mensaje);
          	 irApag("/forsetiadmin/reportes/rep_reportes_dlg.jsp", request, response);
          	 return;
            }
           }
           else
           {
          	 idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
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
      else
      {
      	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          return;
      }

    }
    else
    {
    	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
    	return;
    }
  }

  public String generarReporteHtml(HttpServletRequest request, HttpServletResponse response)
  	throws ServletException, IOException
  {
  	String html = "";
  	
  	JReportesSet m_RepSet = (JReportesSet)request.getAttribute("m_RepSet");
      JReportesLevel1 m_setL1 = (JReportesLevel1)request.getAttribute("m_setL1");
      JReportesCompL1Set m_setCL1 = (JReportesCompL1Set)request.getAttribute("m_setCL1");
  	Boolean m_bSelectL1 = (Boolean)request.getAttribute("m_bSelectL1");
      Boolean m_bSelectL2 = (Boolean)request.getAttribute("m_bSelectL2");
      Boolean m_bSelectL3 = (Boolean)request.getAttribute("m_bSelectL3");
      Boolean m_bComputeL1 = (Boolean)request.getAttribute("m_bComputeL1");
      Boolean m_bComputeL2 = (Boolean)request.getAttribute("m_bComputeL2");
      Boolean m_bComputeL3 = (Boolean)request.getAttribute("m_bComputeL3");
  	JReportesBind2Set m_selectL1 = (JReportesBind2Set)request.getAttribute("m_selectL1");
      JReportesBind2Set m_selectL2 = (JReportesBind2Set)request.getAttribute("m_selectL2");
      JReportesBind2Set m_selectL3 = (JReportesBind2Set)request.getAttribute("m_selectL3");
      JReportesBind2Set m_computeL1 = (JReportesBind2Set)request.getAttribute("m_computeL1");
      JReportesBind2Set m_computeL2 = (JReportesBind2Set)request.getAttribute("m_computeL2");
      JReportesBind2Set m_computeL3 = (JReportesBind2Set)request.getAttribute("m_computeL3");
  	JReportesBind3Set m_colL1 = (JReportesBind3Set)request.getAttribute("m_colL1");
      JReportesBind3Set m_colL2 = (JReportesBind3Set)request.getAttribute("m_colL2");
      JReportesBind3Set m_colL3 = (JReportesBind3Set)request.getAttribute("m_colL3");
      JReportesBind3Set m_colCL1 = (JReportesBind3Set)request.getAttribute("m_colCL1");
      JReportesBind3Set m_colCL2 = (JReportesBind3Set)request.getAttribute("m_colCL2");
      JReportesBind3Set m_colCL3 = (JReportesBind3Set)request.getAttribute("m_colCL3");

  	String fsi_filtro = (String)request.getAttribute("fsi_filtro");
  	
  	String fsiTitulo = "font-family: Arial, Helvetica, sans-serif; font-size: 14pt; font-style: italic; font-weight: bold;";
  	String fsiEncL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 10pt; font-style: normal; font-weight: bold;";
  	String fsiEncL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
  	String fsiEncL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: bold;";
  	String fsiL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
  	String fsiL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal;";
  	String fsiL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: normal;";
  	String fsiCL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 9pt; font-style: normal; font-weight: bold;";
  	String fsiCL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
  	String fsiCL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: bold;";
  	

  	//html += "\r\n";
  	//html += "<!--DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"-->\r\n";
  	//html += "<html>\r\n";
  	html += "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n";
  	html += "<html xmlns='http://www.w3.org/1999/xhtml'>\n";
  	html += "<head>\r\n";
  	//html += "<script language=\"JavaScript\">\r\n";
  	//html += "\twindow.resizeTo(" + m_RepSet.getAbsRow(0).getHW() + "," + m_RepSet.getAbsRow(0).getVW() + ");\r\n";
  	//html += "</script>\r\n";
  	html += "<title>";
  	html +=  m_RepSet.getAbsRow(0).getDescription();
  	html += "</title>\r\n";
  	//html += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n";
  	html += "<style type=\"text/css\">\r\n";
  	html += "<!--\r\n";
  	html += ".fsiTitulo {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getTitulo() == null || m_RepSet.getAbsRow(0).getTitulo().equals("") ) ? fsiTitulo : m_RepSet.getAbsRow(0).getTitulo()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiEncL1 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getEncL1() == null || m_RepSet.getAbsRow(0).getEncL1().equals("") ) ? fsiEncL1 : m_RepSet.getAbsRow(0).getEncL1()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiEncL2 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getEncL2() == null || m_RepSet.getAbsRow(0).getEncL2().equals("") ) ? fsiEncL2 : m_RepSet.getAbsRow(0).getEncL2()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiEncL3 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getEncL3() == null || m_RepSet.getAbsRow(0).getEncL3().equals("") ) ? fsiEncL3 : m_RepSet.getAbsRow(0).getEncL3()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiL1 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getL1() == null || m_RepSet.getAbsRow(0).getL1().equals("") ) ? fsiL1 : m_RepSet.getAbsRow(0).getL1()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiL2 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getL2() == null || m_RepSet.getAbsRow(0).getL2().equals("") ) ? fsiL2 : m_RepSet.getAbsRow(0).getL2()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiL3 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getL3() == null || m_RepSet.getAbsRow(0).getL3().equals("") ) ? fsiL3 : m_RepSet.getAbsRow(0).getL3()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiCL1 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getCL1() == null || m_RepSet.getAbsRow(0).getCL1().equals("") ) ? fsiCL1 : m_RepSet.getAbsRow(0).getCL1()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiCL2 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getCL2() == null || m_RepSet.getAbsRow(0).getCL2().equals("") ) ? fsiCL2 : m_RepSet.getAbsRow(0).getCL2()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += ".fsiCL3 {\r\n";
  	html += "\t";
  	html +=  (( m_RepSet.getAbsRow(0).getCL3() == null || m_RepSet.getAbsRow(0).getCL3().equals("") ) ? fsiCL3 : m_RepSet.getAbsRow(0).getCL3()) ;
  	html += "\r\n";
  	html += "}\r\n";
  	html += "-->\r\n";
  	html += "</style>\r\n";
  	html += "</head>\r\n";
  	html += "<body leftmargin=\"0\" topmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\r\n";
  	html += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n";
  	html += "  <tr>\r\n";
  	html += "    <td class=\"fsiTitulo\" align=\"center\">";
  	html +=  m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro ;
  	html += "</td>\r\n";
  	html += "  </tr>\r\n";
  	html += "  <tr>\r\n";
  	html += "    <td class=\"fsiL1\" align=\"center\">&nbsp;</td>\r\n";
  	html += "  </tr>\r\n";
  	html += "  <tr>\r\n";
  	html += "\t<td><img src=\"../imgfsi/t_negra.gif\" style=\"height:0.5mm; width:100%;\" border=\"0\"></img></td>\r\n";
  	html += "  </tr>  \r\n";
  	html += "  <tr>\r\n";
  	html += "    <td>\r\n";
  	html += "\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n";
  	html += "\t\t  <tr>\r\n";
  	html += "\t\t\t<td>\r\n";
  	if(m_bSelectL1.booleanValue())
  	{	
  		html += "\r\n";
  		html += "\t\t\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  		html += "\t\t\t  \t<tr>\r\n";
  		if(m_selectL1.getAbsRow(0).getTabPrintPnt() > 0) 
  		{ 
  			html += " \r\n";
  			html += "\t\t  <td class=\"fsiEncL1\" width=\"";
  			html +=  m_selectL1.getAbsRow(0).getTabPrintPnt() ;
  			html += "%\">&nbsp;  </td> \r\n";
  			html += "\t";
  		}	
  		for(int i = 0; i < m_colL1.getNumRows(); i++)
  		{ 
  			if(m_colL1.getAbsRow(i).getWillShow()) 
  			{ 
  				html += "\r\n";
  				html += "\t\t\t\t  <td class=\"fsiEncL1\" width=\"";
  				html +=  m_colL1.getAbsRow(i).getAncho() ;
  				html += "%\" align=\"";
  				html +=  m_colL1.getAbsRow(i).getAlinHor() ;
  				html += '"';
  				html +=  (( m_colL1.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL1.getAbsRow(i).getFGColor() + ";\"" : "" ) ;
  				html += '>';
  				html +=  m_colL1.getAbsRow(i).getColName() ;
  				html += "</td>\r\n";
  				html += "\t";
  			} 
  		}	
  		html += "\r\n";
  		html += "\t\t\t\t</tr>\r\n";
  		html += "\t\t\t  </table>\r\n";
  		html += " ";
  	}

  	html += "\t\t\t</td>\r\n";
  	html += "\t\t  </tr>\r\n";
  	html += "\t\t  <tr>\r\n";
  	html += "\t\t\t<td>\r\n";
  	if(m_bSelectL2.booleanValue())
  	{	
  		html += "\r\n";
  		html += "\t\t\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  		html += "\t\t\t  \t<tr>\r\n";
  		if(m_selectL2.getAbsRow(0).getTabPrintPnt() > 0) 
  		{ 
  			html += " \r\n";
  			html += "\t\t  <td class=\"fsiEncL2\" width=\"";
  			html +=  m_selectL2.getAbsRow(0).getTabPrintPnt() ;
  			html += "%\">&nbsp;  </td> \r\n";
  			html += "\t";
  		}	
  		for(int i = 0; i < m_colL2.getNumRows(); i++)
  		{ 
  			if(m_colL2.getAbsRow(i).getWillShow()) 
  			{ 
  				html += "\r\n";
  				html += "\t\t\t\t  <td class=\"fsiEncL2\" width=\"";
  				html +=  m_colL2.getAbsRow(i).getAncho() ;
  				html += "%\" align=\"";
  				html +=  m_colL2.getAbsRow(i).getAlinHor() ;
  				html += '"';
  				html +=  (( m_colL2.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL2.getAbsRow(i).getFGColor() + ";\"" : "" ) ;
  				html += '>';
  				html +=  m_colL2.getAbsRow(i).getColName() ;
  				html += "</td>\r\n";
  				html += "\t";
  			} 
  		}	
  		html += "\r\n";
  		html += "\t\t\t\t</tr>\r\n";
  		html += "\t\t\t  </table>\r\n";
  		html += " ";
  	}

  	html += "\t\t\t</td>\r\n";
  	html += "\t\t  </tr>\r\n";
  	html += "\t\t  <tr>\r\n";
  	html += "\t\t\t<td>\r\n";
  	if(m_bSelectL3.booleanValue())
  	{	
  		html += "\r\n";
  		html += "\t\t\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  		html += "\t\t\t  \t<tr>\r\n";
  		if(m_selectL3.getAbsRow(0).getTabPrintPnt() > 0) 
  		{ 
  			html += " \r\n";
  			html += "\t\t  <td class=\"fsiEncL3\" width=\"";
  			html +=  m_selectL3.getAbsRow(0).getTabPrintPnt() ;
  			html += "%\">&nbsp;  </td> \r\n";
  			html += "\t";
  		}	
  		for(int i = 0; i < m_colL3.getNumRows(); i++)
  		{ 
  			if(m_colL3.getAbsRow(i).getWillShow()) 
  			{ 
  				html += "\r\n";
  				html += "\t\t\t\t  <td class=\"fsiEncL3\" width=\"";
  				html +=  m_colL3.getAbsRow(i).getAncho() ;
  				html += "%\" align=\"";
  				html +=  m_colL3.getAbsRow(i).getAlinHor() ;
  				html += '"';
  				html +=  (( m_colL3.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL3.getAbsRow(i).getFGColor() + ";\"" : "" ) ;
  				html += '>';
  				html +=  m_colL3.getAbsRow(i).getColName() ;
  				html += "</td>\r\n";
  				html += "\t";
  			} 
  		}	
  		html += "\r\n";
  		html += "\t\t\t\t</tr>\r\n";
  		html += "\t\t\t  </table>\r\n";
  		html += " ";
  	}

  	html += "\t\t\t</td>\r\n";
  	html += "\t\t  </tr>\r\n";
  	html += "\t\t</table>\r\n";
  	html += "\t</td>\r\n";
  	html += "  </tr>\r\n";
  	html += "  <tr>\r\n";
  	html += "\t<td><img src=\"../imgfsi/t_negra.gif\" style=\"height:0.5mm; width:100%;\" border=\"0\"></img></td>\r\n";
  	html += "  </tr>  \r\n";
  	html += "  <tr>\r\n";
  	html += "    <td class=\"fsiL1\" align=\"center\">&nbsp;</td>\r\n";
  	html += "  </tr>\r\n";
  	html += "  <tr>\r\n";
  	html += "    <td>\r\n";
  		
  	if(m_bSelectL1.booleanValue())
  	{
  		for (int RL1 = 0; RL1 < m_setL1.getNumRows(); RL1++)
  		{  
  			html += "\r\n";
  			html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  			html += "\t  \t<tr> \r\n";
  			html += "\t";
  			if(m_selectL1.getAbsRow(0).getTabPrintPnt() > 0) 
  			{ 
  				html += " \r\n";
  				html += "\t\t  <td class=\"fsiL1\" width=\"";
  				html +=  m_selectL1.getAbsRow(0).getTabPrintPnt() ;
  				html += "%\">&nbsp;  </td> \r\n";
  				html += "\t";
  			}
  			for(int CL1 = 0; CL1 < m_colL1.getNumRows(); CL1++)
  			{	
  				if(m_colL1.getAbsRow(CL1).getWillShow()) 
  				{ 
  					String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSTS(m_colL1.getAbsRow(CL1).getColName()), m_colL1.getAbsRow(CL1).getFormat(), m_colL1.getAbsRow(CL1).getBindDataType(), request); 
  					html += "\r\n";
  					html += "\t\t  <td class=\"fsiL1\" width=\"";
  					html +=  m_colL1.getAbsRow(CL1).getAncho() ;
  					html += "%\" align=\"";
  					html +=  m_colL1.getAbsRow(CL1).getAlinHor() ;
  					html += '"';
  					html +=  (( m_colL1.getAbsRow(CL1).getFGColor() != null ) ? " style=\"color:#" +  m_colL1.getAbsRow(CL1).getFGColor() + ";\"" : "" ) ;
  					html += '>';
  					html += cabval;
  					html += "</td>\r\n";
  					html += "\t";
  				} 
  			}	
  			html += "\r\n";
  			html += "\t\t</tr>\r\n";
  			html += "\t  </table> ";

  			// Nivel 2
  			if(m_bSelectL2.booleanValue())
  			{
  				for (int RL2 = 0; RL2 < m_setL1.getAbsRow(RL1).getSetL2().getNumRows(); RL2++)
  				{ 
  					html += "\r\n";
  					html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  					html += "\t  \t<tr> \r\n";
  					html += "\t\t\t";
  					if(m_selectL2.getAbsRow(0).getTabPrintPnt() > 0) 
  					{ 
  						html += " \r\n";
  						html += "\t\t  <td class=\"fsiL2\" width=\"";
  						html +=  m_selectL2.getAbsRow(0).getTabPrintPnt()  ;
  						html += "%\">&nbsp;  </td> \r\n";
  						html += "\t\t\t";
  					}
  					for(int CL2 = 0; CL2 < m_colL2.getNumRows(); CL2++)
  					{
  						if(m_colL2.getAbsRow(CL2).getWillShow()) 
  						{ 
  							String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSTS(m_colL2.getAbsRow(CL2).getColName()), m_colL2.getAbsRow(CL2).getFormat(), m_colL2.getAbsRow(CL2).getBindDataType(), request); 
  							html += "\r\n";
  							html += "\t\t  <td class=\"fsiL2\" width=\"";
  							html +=  m_colL2.getAbsRow(CL2).getAncho() ;
  							html += "%\" align=\"";
  							html +=  m_colL2.getAbsRow(CL2).getAlinHor() ;
  							html += '"';
  							html +=  (( m_colL2.getAbsRow(CL2).getFGColor() != null ) ? " style=\"color:#" +  m_colL2.getAbsRow(CL2).getFGColor() + ";\"" : "" ) ;
  							html += '>';
  							html +=  cabval ;
  							html += "</td>\r\n";
  							html += "\t\t\t";
  						}    
  					} 
  					html += "\r\n";
  					html += "\t\t</tr>\r\n";
  					html += "\t  </table> ";
  				
  					// Nivel 3
  					if(m_bSelectL3.booleanValue())
  					{
  						for (int RL3 = 0; RL3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getNumRows(); RL3++)
  						{ 
  							html += "\r\n";
  							html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  							html += "\t  \t<tr> \r\n";
  							html += "\t\t\t\t\t";
  							if(m_selectL3.getAbsRow(0).getTabPrintPnt() > 0) 
  							{ 
  								html += " \r\n";
  								html += "\t\t  <td class=\"fsiL3\" width=\"";
  								html +=  m_selectL3.getAbsRow(0).getTabPrintPnt() ;
  								html += "%\">&nbsp;  </td> \r\n";
  								html += "\t\t\t\t\t";
  							}
  							for(int CL3 = 0; CL3 < m_colL3.getNumRows(); CL3++)
  							{ 
  								if(m_colL3.getAbsRow(CL3).getWillShow()) 
  								{  
  									String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getAbsRow(RL3).getSTS(m_colL3.getAbsRow(CL3).getColName()), m_colL3.getAbsRow(CL3).getFormat(), m_colL3.getAbsRow(CL3).getBindDataType(), request); 
  									html += "\r\n";
  									html += "\t\t  <td class=\"fsiL3\" width=\"";
  									html +=  m_colL3.getAbsRow(CL3).getAncho() ;
  									html += "%\" align=\"";
  									html +=  m_colL3.getAbsRow(CL3).getAlinHor() ;
  									html += '"';
  									html +=  (( m_colL3.getAbsRow(CL3).getFGColor() != null ) ? " style=\"color:#" +  m_colL3.getAbsRow(CL3).getFGColor() + ";\"" : "" ) ;
  									html += '>';
  									html +=  cabval ;
  									html += "</td>\r\n";
  									html += "\t\t\t\t\t";
  								}
  							}  
  							html += "\r\n";
  							html += "\t\t</tr>\r\n";
  							html += "\t  </table> ";
  				
  						}	
  						if(m_bComputeL3.booleanValue())
  						{
  							for (int RC3 = 0; RC3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getNumRows(); RC3++)
  							{ 
  								html += "\r\n";
  								html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  								html += "\t  \t<tr> \r\n";
  								html += "\t\t\t\t\t\t";
							    	if(m_computeL3.getAbsRow(0).getTabPrintPnt() > 0) 
							    	{ 
							    		html += " \r\n";
							    		html += "\t\t  <td class=\"fsiCL3\" width=\"";
							    		html +=  m_computeL3.getAbsRow(0).getTabPrintPnt() ;
							    		html += "%\">&nbsp;  </td> \r\n";
							    		html += "\t\t\t\t\t\t";
							     	}
							    	for(int CC3 = 0; CC3 < m_colCL3.getNumRows(); CC3++)
							    	{
							    		if(m_colCL3.getAbsRow(CC3).getWillShow()) 
							    		{  
							    			String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getAbsRow(RC3).getSTS(m_colCL3.getAbsRow(CC3).getColName()), m_colCL3.getAbsRow(CC3).getFormat(), m_colCL3.getAbsRow(CC3).getBindDataType(), request); 
							    			html += "\r\n";
							    			html += "\t\t  <td class=\"fsiCL3\" width=\"";
							    			html +=  m_colCL3.getAbsRow(CC3).getAncho() ;
							    			html += "%\" align=\"";
							    			html +=  m_colCL3.getAbsRow(CC3).getAlinHor() ;
							    			html += '"';
							    			html +=  (( m_colCL3.getAbsRow(CC3).getFGColor() != null ) ? " style=\"color:#" +  m_colCL3.getAbsRow(CC3).getFGColor() + ";\"" : "" ) ;
							    			html += '>';
							    			html +=  cabval ;
							    			html += "</td>\r\n";
							    			html += "            \t\t\t\t";
							    		}				
							    	}	
							    	html += "\r\n";
							    	html += "\t\t</tr>\r\n";
							    	html += "\t  </table>\t\r\n";
							    	html += "            \t\t";
  							}
  						} // Fin SI CL3
  					} // Fin SI L3
  				}
  				if(m_bComputeL2.booleanValue())
  				{
  					for (int RC2 = 0; RC2 < m_setL1.getAbsRow(RL1).getSetCL2().getNumRows(); RC2++)
  					{ 
  						html += "\r\n";
  						html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  						html += "\t  \t<tr> \r\n";
  						html += "\t\t\t\t";
  						if(m_computeL2.getAbsRow(0).getTabPrintPnt() > 0) 
  						{ 
  							html += " \r\n";
  							html += "\t\t  <td class=\"fsiCL2\" width=\"";
  							html +=  m_computeL2.getAbsRow(0).getTabPrintPnt() ;
  							html += "%\">&nbsp;  </td> \r\n";
  							html += "\t\t\t\t";
  						}
  						for(int CC2 = 0; CC2 < m_colCL2.getNumRows(); CC2++)
  						{
  							if(m_colCL2.getAbsRow(CC2).getWillShow()) 
  							{  
  								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetCL2().getAbsRow(RC2).getSTS(m_colCL2.getAbsRow(CC2).getColName()), m_colCL2.getAbsRow(CC2).getFormat(), m_colCL2.getAbsRow(CC2).getBindDataType(), request); 
  								html += "\r\n";
  								html += "\t\t  <td class=\"fsiCL2\" width=\"";
  								html +=  m_colCL2.getAbsRow(CC2).getAncho() ;
  								html += "%\" align=\"";
  								html +=  m_colCL2.getAbsRow(CC2).getAlinHor() ;
  								html += '"';
  								html +=  (( m_colCL2.getAbsRow(CC2).getFGColor() != null ) ? " style=\"color:#" +  m_colCL2.getAbsRow(CC2).getFGColor() + ";\"" : "" ) ;
  								html += '>';
  								html +=  cabval ;
  								html += "</td>\r\n";
  								html += "          \t\t\t";
  							}	
  						} 
  						html += "\r\n";
  						html += "\t\t</tr>\r\n";
  						html += "\t  </table>\t\r\n";
  						html += "    \t\t";
  					}
  				} // Fin SI CL2
  			} // Fin SI L2
  		}
  		if(m_bComputeL1.booleanValue())
  		{
  			for (int RC1 = 0; RC1 < m_setCL1.getNumRows(); RC1++)
  			{ 
  				html += "\r\n";
  				html += "\t  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">\r\n";
  				html += "\t  \t<tr> \r\n";
  				html += "\t\t";
  				if(m_computeL1.getAbsRow(0).getTabPrintPnt() > 0) 
  				{ 
  					html += " \r\n";
  					html += "\t\t  <td class=\"fsiCL1\" width=\"";
  					html +=  m_computeL1.getAbsRow(0).getTabPrintPnt() ;
  					html += "%\">&nbsp;  </td> \r\n";
  					html += "\t\t";
  				}
  				for(int CC1 = 0; CC1 < m_colCL1.getNumRows(); CC1++)
  				{
  					if(m_colCL1.getAbsRow(CC1).getWillShow()) 
  					{ 
  						String cabval = JUtil.FormatearRep(m_setCL1.getAbsRow(RC1).getSTS(m_colCL1.getAbsRow(CC1).getColName()), m_colCL1.getAbsRow(CC1).getFormat(), m_colCL1.getAbsRow(CC1).getBindDataType(), request); 
  						html += "\r\n";
  						html += "\t\t  <td class=\"fsiCL1\" width=\"";
  						html +=  m_colCL1.getAbsRow(CC1).getAncho() ;
  						html += "%\" align=\"";
  						html +=  m_colCL1.getAbsRow(CC1).getAlinHor() ;
  						html += '"';
  						html +=  (( m_colCL1.getAbsRow(CC1).getFGColor() != null ) ? " style=\"color:#" +  m_colCL1.getAbsRow(CC1).getFGColor() + ";\"" : "" ) ;
  						html += '>';
  						html +=  cabval ;
  						html += "</td>\r\n";
  						html += " \t\t";
  					}
  				} 
  				html += "\r\n";
  				html += "\t\t</tr>\r\n";
  				html += "\t  </table>\t\r\n";
  				html += "    ";
  			}
  		} // Fin SI CL1
  	} // Fin SI L1
  					
  	html += "\r\n";
  	html += "\t</td>\r\n";
  	html += "  </tr>\r\n";
  	html += "</table>\r\n";
  	html += "</body>\r\n";
  	html += "</html>\r\n";

  	return html;
  }									

  public String generarArchivoSQL(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
  {
	  String html = "";
	  JReportesSet RepSet = (JReportesSet)request.getAttribute("m_RepSet");
	  JReportesSentenciasSet RepSentSet = new JReportesSentenciasSet(request);
	  RepSentSet.ConCat(true);
	  RepSentSet.m_Where = "ID_Report = '" + RepSet.getAbsRow(0).getID_Report() + "'";
	  RepSentSet.Open();
	  JReportesSentenciasColumnasSet RepSentColSet = new JReportesSentenciasColumnasSet(request);
	  RepSentColSet.ConCat(true);
	  RepSentColSet.m_Where = "ID_Report = '" + RepSet.getAbsRow(0).getID_Report() + "'";
	  RepSentColSet.Open();
	  JReportesFiltroSet RepFilSet = new JReportesFiltroSet(request);
	  RepFilSet.ConCat(true);
	  RepFilSet.m_Where = "ID_Report = '" + RepSet.getAbsRow(0).getID_Report() + "'";
	  RepFilSet.Open();
	  
	  html += "INSERT INTO TBL_REPORTS\n";
	  html += "VALUES('" + RepSet.getAbsRow(0).getID_Report() + "','" + p(RepSet.getAbsRow(0).getDescription()) + "','" + p(RepSet.getAbsRow(0).getTipo()) + "','" + p(RepSet.getAbsRow(0).getTitulo()) + "','" + p(RepSet.getAbsRow(0).getEncL1()) + "','" + p(RepSet.getAbsRow(0).getEncL2()) + "','" + p(RepSet.getAbsRow(0).getEncL3()) + 
   		    	"','" + p(RepSet.getAbsRow(0).getL1()) + "','" + p(RepSet.getAbsRow(0).getL2()) + "','" + p(RepSet.getAbsRow(0).getL3()) + "','" + p(RepSet.getAbsRow(0).getCL1()) + "','" + p(RepSet.getAbsRow(0).getCL2()) + 
   		    	"','" + p(RepSet.getAbsRow(0).getCL3()) + "'," + RepSet.getAbsRow(0).getHW() + "," + RepSet.getAbsRow(0).getVW() + ",'" + p(RepSet.getAbsRow(0).getSubTipo()) + "','" + p(RepSet.getAbsRow(0).getClave()) + "','" + (RepSet.getAbsRow(0).getGraficar() ? "1" : "0") + "');\n\n";
	  
	  for(int i = 0; i < RepSentSet.getNumRows(); i++)
	  {
		  html += "INSERT INTO TBL_REPORTS_SENTENCES\n";
		  html += "VALUES(" + RepSentSet.getAbsRow(i).getID_Report() + "," + RepSentSet.getAbsRow(i).getID_Sentence() + "," + RepSentSet.getAbsRow(i).getID_IsCompute() + ",'" + q(RepSentSet.getAbsRow(i).getSelect_Clause()) + "'," + RepSentSet.getAbsRow(i).getTabPrintPnt() + ",null );\n";
	  }
	  
	  html += "\n";
	  
	  for(int i = 0; i < RepSentColSet.getNumRows(); i++)
	  {
		  html += "INSERT INTO TBL_REPORTS_SENTENCES_COLUMNS\n";
		  html += "VALUES(" + RepSentColSet.getAbsRow(i).getID_Report() + "," + RepSentColSet.getAbsRow(i).getID_Sentence() + "," + RepSentColSet.getAbsRow(i).getID_IsCompute() + "," + RepSentColSet.getAbsRow(i).getID_Column() + ",'" + q(RepSentColSet.getAbsRow(i).getColName()) +
						"','" + q(RepSentColSet.getAbsRow(i).getBindDataType()) + "','" + (RepSentColSet.getAbsRow(i).getWillShow() ? 1 : 0) +
						"','" + q(RepSentColSet.getAbsRow(i).getFormat()) + "'," + RepSentColSet.getAbsRow(i).getAncho() + 
						",'" + q(RepSentColSet.getAbsRow(i).getAlinHor()) + "','000000' );\n";
	  }
	  
	  html += "\n";
	  
	  for(int i = 0; i < RepFilSet.getNumRows(); i++)
	  {
		  html += "INSERT INTO TBL_REPORTS_FILTER\n";
		  html += "VALUES(" + RepFilSet.getAbsRow(i).getID_Report() + "," + RepFilSet.getAbsRow(i).getID_Column() + ",'" + p(RepFilSet.getAbsRow(i).getInstructions()) + "','" +
						(RepFilSet.getAbsRow(i).getIsRange() ? 1 : 0) + "','" + q(RepFilSet.getAbsRow(i).getPriDataName()) + "','" + q(RepFilSet.getAbsRow(i).getPriDefault()) + "','" +
						q(RepFilSet.getAbsRow(i).getSecDataName()) + "','" + q(RepFilSet.getAbsRow(i).getSecDefault()) + "','" + q(RepFilSet.getAbsRow(i).getBindDataType()) + "','" +
						(RepFilSet.getAbsRow(i).getFromCatalog() ? 1 : 0) + "','" + q(RepFilSet.getAbsRow(i).getSelect_Clause()) + "' );\n";
	  }
	  
	  return html;
  
  }
  
  public String generarArchivoCSV(HttpServletRequest request, HttpServletResponse response, String sep)
  		throws ServletException, IOException
  {
  	String html = "";
      	
  	JReportesSet m_RepSet = (JReportesSet)request.getAttribute("m_RepSet");
  	JReportesLevel1 m_setL1 = (JReportesLevel1)request.getAttribute("m_setL1");
  	JReportesCompL1Set m_setCL1 = (JReportesCompL1Set)request.getAttribute("m_setCL1");
  	Boolean m_bSelectL1 = (Boolean)request.getAttribute("m_bSelectL1");
  	Boolean m_bSelectL2 = (Boolean)request.getAttribute("m_bSelectL2");
  	Boolean m_bSelectL3 = (Boolean)request.getAttribute("m_bSelectL3");
  	Boolean m_bComputeL1 = (Boolean)request.getAttribute("m_bComputeL1");
  	Boolean m_bComputeL2 = (Boolean)request.getAttribute("m_bComputeL2");
  	Boolean m_bComputeL3 = (Boolean)request.getAttribute("m_bComputeL3");
  	JReportesBind3Set m_colL1 = (JReportesBind3Set)request.getAttribute("m_colL1");
  	JReportesBind3Set m_colL2 = (JReportesBind3Set)request.getAttribute("m_colL2");
  	JReportesBind3Set m_colL3 = (JReportesBind3Set)request.getAttribute("m_colL3");
  	JReportesBind3Set m_colCL1 = (JReportesBind3Set)request.getAttribute("m_colCL1");
  	JReportesBind3Set m_colCL2 = (JReportesBind3Set)request.getAttribute("m_colCL2");
  	JReportesBind3Set m_colCL3 = (JReportesBind3Set)request.getAttribute("m_colCL3");
  	
  	String fsi_filtro = (String)request.getAttribute("fsi_filtro");
  	
  	html += sep + m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro + sep + "\r\n";
  	if(m_bSelectL1.booleanValue())
  	{	
  		for(int i = 0; i < m_colL1.getNumRows(); i++)
  		{ 
  			if(m_colL1.getAbsRow(i).getWillShow()) 
     				html += sep + m_colL1.getAbsRow(i).getColName() + sep;
   
     		}	
     		html += "\r\n";
     	}

     	if(m_bSelectL2.booleanValue())
     	{	
     		for(int i = 0; i < m_colL2.getNumRows(); i++)
     		{ 
     			if(m_colL2.getAbsRow(i).getWillShow()) 
     				html += sep + m_colL2.getAbsRow(i).getColName() + sep ;
     			 
     		}	
     		html += "\r\n";
     	}

    	if(m_bSelectL3.booleanValue())
    	{	
      	for(int i = 0; i < m_colL3.getNumRows(); i++)
      	{ 
      		if(m_colL3.getAbsRow(i).getWillShow()) 
      			html += sep + m_colL3.getAbsRow(i).getColName() + sep ;
     		}	
     		html += "\r\n";
     	}
		  
		if(m_bSelectL1.booleanValue())
		{
			for (int RL1 = 0; RL1 < m_setL1.getNumRows(); RL1++)
			{  
				for(int CL1 = 0; CL1 < m_colL1.getNumRows(); CL1++)
				{	
					if(m_colL1.getAbsRow(CL1).getWillShow()) 
					{ 
						String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSTS(m_colL1.getAbsRow(CL1).getColName()), m_colL1.getAbsRow(CL1).getFormat(), m_colL1.getAbsRow(CL1).getBindDataType(), request); 
      				html += sep + cabval + sep;
     				} 
     			}	
     			html += "\r\n";

     			// Nivel 2
     			if(m_bSelectL2.booleanValue())
     			{
     				for (int RL2 = 0; RL2 < m_setL1.getAbsRow(RL1).getSetL2().getNumRows(); RL2++)
     				{ 
     					for(int CL2 = 0; CL2 < m_colL2.getNumRows(); CL2++)
     					{
     						if(m_colL2.getAbsRow(CL2).getWillShow()) 
     						{ 
     							String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSTS(m_colL2.getAbsRow(CL2).getColName()), m_colL2.getAbsRow(CL2).getFormat(), m_colL2.getAbsRow(CL2).getBindDataType(), request); 
     							html += sep + cabval + sep;
     						}    
     					} 
      				html += "\r\n";
      				
      				// Nivel 3
      				if(m_bSelectL3.booleanValue())
      				{
      					for (int RL3 = 0; RL3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getNumRows(); RL3++)
      					{ 
      						for(int CL3 = 0; CL3 < m_colL3.getNumRows(); CL3++)
      						{ 
      							if(m_colL3.getAbsRow(CL3).getWillShow()) 
      							{  
      								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getAbsRow(RL3).getSTS(m_colL3.getAbsRow(CL3).getColName()), m_colL3.getAbsRow(CL3).getFormat(), m_colL3.getAbsRow(CL3).getBindDataType(), request); 
      								html += sep + cabval + sep;
     								}
     							}  
     							html += "\r\n";
      				
     						}	
     						if(m_bComputeL3.booleanValue())
     						{
     							for (int RC3 = 0; RC3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getNumRows(); RC3++)
     							{ 
     								for(int CC3 = 0; CC3 < m_colCL3.getNumRows(); CC3++)
     								{
     									if(m_colCL3.getAbsRow(CC3).getWillShow()) 
     									{  
     										String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getAbsRow(RC3).getSTS(m_colCL3.getAbsRow(CC3).getColName()), m_colCL3.getAbsRow(CC3).getFormat(), m_colCL3.getAbsRow(CC3).getBindDataType(), request); 
  							    		html += sep + cabval + sep;
 							    		}				
 							    	}	
 							    	html += "\r\n";
     							}
     						} // Fin SI CL3
     					} // Fin SI L3
     				}
     				if(m_bComputeL2.booleanValue())
     				{
     					for (int RC2 = 0; RC2 < m_setL1.getAbsRow(RL1).getSetCL2().getNumRows(); RC2++)
     					{ 
     						for(int CC2 = 0; CC2 < m_colCL2.getNumRows(); CC2++)
     						{
     							if(m_colCL2.getAbsRow(CC2).getWillShow()) 
     							{  
     								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetCL2().getAbsRow(RC2).getSTS(m_colCL2.getAbsRow(CC2).getColName()), m_colCL2.getAbsRow(CC2).getFormat(), m_colCL2.getAbsRow(CC2).getBindDataType(), request); 
      							html += sep + cabval + sep;
     							}	
     						} 
     						html += "\r\n";
     					}
     				} // Fin SI CL2
     			} // Fin SI L2
     		}
     		if(m_bComputeL1.booleanValue())
     		{
     			for (int RC1 = 0; RC1 < m_setCL1.getNumRows(); RC1++)
     			{ 
     				for(int CC1 = 0; CC1 < m_colCL1.getNumRows(); CC1++)
     				{
     					if(m_colCL1.getAbsRow(CC1).getWillShow()) 
     					{ 
     						String cabval = JUtil.FormatearRep(m_setCL1.getAbsRow(RC1).getSTS(m_colCL1.getAbsRow(CC1).getColName()), m_colCL1.getAbsRow(CC1).getFormat(), m_colCL1.getAbsRow(CC1).getBindDataType(), request); 
      					html +=  cabval ;
      				}
      			} 
      			html += "\r\n";
      		}
      	} // Fin SI CL1
      } // Fin SI L1
      
      return html;
  }	    
  
  public String generarArchivoDOM(HttpServletRequest request, HttpServletResponse response, Document document)
  		throws ServletException, IOException
  {
  	JReportesSet m_RepSet = (JReportesSet)request.getAttribute("m_RepSet");
  	JReportesLevel1 m_setL1 = (JReportesLevel1)request.getAttribute("m_setL1");
  	JReportesCompL1Set m_setCL1 = (JReportesCompL1Set)request.getAttribute("m_setCL1");
  	Boolean m_bSelectL1 = (Boolean)request.getAttribute("m_bSelectL1");
  	Boolean m_bSelectL2 = (Boolean)request.getAttribute("m_bSelectL2");
  	Boolean m_bSelectL3 = (Boolean)request.getAttribute("m_bSelectL3");
  	Boolean m_bComputeL1 = (Boolean)request.getAttribute("m_bComputeL1");
  	Boolean m_bComputeL2 = (Boolean)request.getAttribute("m_bComputeL2");
  	Boolean m_bComputeL3 = (Boolean)request.getAttribute("m_bComputeL3");
  	JReportesBind3Set m_colL1 = (JReportesBind3Set)request.getAttribute("m_colL1");
  	JReportesBind3Set m_colL2 = (JReportesBind3Set)request.getAttribute("m_colL2");
  	JReportesBind3Set m_colL3 = (JReportesBind3Set)request.getAttribute("m_colL3");
  	JReportesBind3Set m_colCL1 = (JReportesBind3Set)request.getAttribute("m_colCL1");
  	JReportesBind3Set m_colCL2 = (JReportesBind3Set)request.getAttribute("m_colCL2");
  	JReportesBind3Set m_colCL3 = (JReportesBind3Set)request.getAttribute("m_colCL3");
  	
  	String fsi_filtro = (String)request.getAttribute("fsi_filtro");
  	
  	Element reporte = new Element("reporte");
  	document.setRootElement(reporte);
  	
  	reporte.setAttribute(new Attribute("id", Integer.toString(m_RepSet.getAbsRow(0).getID_Report())));
  	reporte.setAttribute(new Attribute("descripcion", m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro));
  	
  	if(m_bSelectL1.booleanValue())
  	{
  		Element sentencia = new Element("sentencia");
  		sentencia.setAttribute(new Attribute("nivel","1"));
  		reporte.addContent(sentencia);
  		for (int RL1 = 0; RL1 < m_setL1.getNumRows(); RL1++)
			{ 
  			Element rl1 = new Element("linea");
	        	sentencia.addContent(rl1);
	        	for(int CL1 = 0; CL1 < m_colL1.getNumRows(); CL1++)
				{	
					if(m_colL1.getAbsRow(CL1).getWillShow()) 
					{ 
						String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSTS(m_colL1.getAbsRow(CL1).getColName()), m_colL1.getAbsRow(CL1).getFormat(), m_colL1.getAbsRow(CL1).getBindDataType(), request); 
						rl1.addContent(new Element(m_colL1.getAbsRow(CL1).getColName()).setText(cabval));
					} 
     			}	

     			// Nivel 2
     			if(m_bSelectL2.booleanValue())
     			{
     	    		Element sentencia2 = new Element("sentencia");
     	    		sentencia2.setAttribute(new Attribute("nivel","2"));
     	    		sentencia.addContent(sentencia2);
     				for (int RL2 = 0; RL2 < m_setL1.getAbsRow(RL1).getSetL2().getNumRows(); RL2++)
     				{ 
     					Element rl2 = new Element("linea");
     		        	sentencia2.addContent(rl2);
     		        	for(int CL2 = 0; CL2 < m_colL2.getNumRows(); CL2++)
     					{
     						if(m_colL2.getAbsRow(CL2).getWillShow()) 
     						{ 
     							String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSTS(m_colL2.getAbsRow(CL2).getColName()), m_colL2.getAbsRow(CL2).getFormat(), m_colL2.getAbsRow(CL2).getBindDataType(), request); 
     							rl2.addContent(new Element(m_colL2.getAbsRow(CL2).getColName()).setText(cabval));
     						}    
     					} 
      				
      				// Nivel 3
      				if(m_bSelectL3.booleanValue())
      				{
      					Element sentencia3 = new Element("sentencia");
             	    		sentencia3.setAttribute(new Attribute("nivel","3"));
             	    		sentencia2.addContent(sentencia3);
             				for (int RL3 = 0; RL3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getNumRows(); RL3++)
      					{ 
             					Element rl3 = new Element("linea");
             		        	sentencia3.addContent(rl3);
             		        	for(int CL3 = 0; CL3 < m_colL3.getNumRows(); CL3++)
      						{ 
      							if(m_colL3.getAbsRow(CL3).getWillShow()) 
      							{  
      								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getAbsRow(RL3).getSTS(m_colL3.getAbsRow(CL3).getColName()), m_colL3.getAbsRow(CL3).getFormat(), m_colL3.getAbsRow(CL3).getBindDataType(), request); 
      								rl3.addContent(new Element(m_colL3.getAbsRow(CL3).getColName()).setText(cabval));
      			       			}
     							}  
     						}

     						if(m_bComputeL3.booleanValue())
     						{
     							Element agregado3 = new Element("agregado");
                 	    		agregado3.setAttribute(new Attribute("nivel","3"));
                 	    		sentencia2.addContent(agregado3);
                 				for (int RC3 = 0; RC3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getNumRows(); RC3++)
     							{ 
                 					Element rc3 = new Element("linea");
                 		        	agregado3.addContent(rc3);
                 		        	for(int CC3 = 0; CC3 < m_colCL3.getNumRows(); CC3++)
     								{
     									if(m_colCL3.getAbsRow(CC3).getWillShow()) 
     									{  
     										String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getAbsRow(RC3).getSTS(m_colCL3.getAbsRow(CC3).getColName()), m_colCL3.getAbsRow(CC3).getFormat(), m_colCL3.getAbsRow(CC3).getBindDataType(), request); 
     										rc3.addContent(new Element(m_colCL3.getAbsRow(CC3).getColName()).setText(cabval));
     				        			}				
 							    	}	
 							    }
     						} // Fin SI CL3
     					} // Fin SI L3
     				}

     				if(m_bComputeL2.booleanValue())
     				{
     					Element agregado2 = new Element("agregado");
         	    		agregado2.setAttribute(new Attribute("nivel","2"));
         	    		sentencia.addContent(agregado2);
         				for(int RC2 = 0; RC2 < m_setL1.getAbsRow(RL1).getSetCL2().getNumRows(); RC2++)
     					{ 
         					Element rc2 = new Element("linea");
         		        	agregado2.addContent(rc2);
         		        	for(int CC2 = 0; CC2 < m_colCL2.getNumRows(); CC2++)
     						{
     							if(m_colCL2.getAbsRow(CC2).getWillShow()) 
     							{  
     								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetCL2().getAbsRow(RC2).getSTS(m_colCL2.getAbsRow(CC2).getColName()), m_colCL2.getAbsRow(CC2).getFormat(), m_colCL2.getAbsRow(CC2).getBindDataType(), request); 
     								rc2.addContent(new Element(m_colCL2.getAbsRow(CC2).getColName()).setText(cabval));
         				        }	
     						} 
     					}
     				} // Fin SI CL2
     			} // Fin SI L2
     		}

     		if(m_bComputeL1.booleanValue())
     		{
     			Element agregado = new Element("agregado");
     			agregado.setAttribute(new Attribute("nivel","1"));
     			reporte.addContent(agregado);
     			for (int RC1 = 0; RC1 < m_setCL1.getNumRows(); RC1++)
     			{ 
     				Element rc1 = new Element("linea");
 		        	agregado.addContent(rc1);
 		        	for(int CC1 = 0; CC1 < m_colCL1.getNumRows(); CC1++)
     				{
     					if(m_colCL1.getAbsRow(CC1).getWillShow()) 
     					{ 
     						String cabval = JUtil.FormatearRep(m_setCL1.getAbsRow(RC1).getSTS(m_colCL1.getAbsRow(CC1).getColName()), m_colCL1.getAbsRow(CC1).getFormat(), m_colCL1.getAbsRow(CC1).getBindDataType(), request); 
     						rc1.addContent(new Element(m_colCL1.getAbsRow(CC1).getColName()).setText(cabval));
         				}
      			} 
     			}
       	} // Fin SI CL1
      } // Fin SI L1
  
  	  Format format = Format.getPrettyFormat();
  	  format.setEncoding("utf-8");
  	  format.setTextMode(TextMode.NORMALIZE);
  	  XMLOutputter xmlOutputer = new XMLOutputter(format);
		
  	  return xmlOutputer.outputString(document);
		        
  }	    
 
  public void generarArchivoXLS(HttpServletRequest request, HttpServletResponse response, Workbook wb)
  		throws ServletException, IOException
  {
  	JReportesSet m_RepSet = (JReportesSet)request.getAttribute("m_RepSet");
  	JReportesLevel1 m_setL1 = (JReportesLevel1)request.getAttribute("m_setL1");
  	JReportesCompL1Set m_setCL1 = (JReportesCompL1Set)request.getAttribute("m_setCL1");
  	Boolean m_bSelectL1 = (Boolean)request.getAttribute("m_bSelectL1");
  	Boolean m_bSelectL2 = (Boolean)request.getAttribute("m_bSelectL2");
  	Boolean m_bSelectL3 = (Boolean)request.getAttribute("m_bSelectL3");
  	Boolean m_bComputeL1 = (Boolean)request.getAttribute("m_bComputeL1");
  	Boolean m_bComputeL2 = (Boolean)request.getAttribute("m_bComputeL2");
  	Boolean m_bComputeL3 = (Boolean)request.getAttribute("m_bComputeL3");
  	JReportesBind3Set m_colL1 = (JReportesBind3Set)request.getAttribute("m_colL1");
  	JReportesBind3Set m_colL2 = (JReportesBind3Set)request.getAttribute("m_colL2");
  	JReportesBind3Set m_colL3 = (JReportesBind3Set)request.getAttribute("m_colL3");
  	JReportesBind3Set m_colCL1 = (JReportesBind3Set)request.getAttribute("m_colCL1");
  	JReportesBind3Set m_colCL2 = (JReportesBind3Set)request.getAttribute("m_colCL2");
  	JReportesBind3Set m_colCL3 = (JReportesBind3Set)request.getAttribute("m_colCL3");
  	
  	String fsi_filtro = (String)request.getAttribute("fsi_filtro");
  	
  	Sheet sheet = wb.createSheet("reporte " + Integer.toString(m_RepSet.getAbsRow(0).getID_Report()));
    	 
  	short nrow = 0;
  	Row row = sheet.createRow(nrow++);
  	Font font = wb.createFont();
  	font.setBoldweight(Font.BOLDWEIGHT_BOLD);
  	font.setColor(HSSFColor.DARK_BLUE.index);
  	Cell cell = row.createCell(0);
  	cell.setCellValue(m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro);
    CellStyle cellStyle = wb.createCellStyle();
    cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
    cellStyle.setFont(font);
    cell.setCellStyle(cellStyle);
      
    JRepCellStyles cellStyles = new JRepCellStyles(wb);
    
  	if(m_bSelectL1.booleanValue())
  	{
      	Row rowl = sheet.createRow(nrow++);
      	for(int i = 0; i < m_colL1.getNumRows(); i++)
  		{ 
  			if(m_colL1.getAbsRow(i).getWillShow()) 
  			{
					JUtil.DatoXLS(cellStyles, rowl, i, m_colL1.getAbsRow(i).getColName(), "general", "STRING", m_colL1.getAbsRow(i).getAlinHor(), "encabezado", "fenc", request);
					//celll1.getCellStyle().setBorderTop(CellStyle.BORDER_THIN);
					//celll1.getCellStyle().setBorderBottom(CellStyle.BORDER_THIN);
				}
  		}	
     	}

     	if(m_bSelectL2.booleanValue())
     	{
     		Row rowl = sheet.createRow(nrow++);
     		for(int i = 0; i < m_colL2.getNumRows(); i++)
     		{ 
     			if(m_colL2.getAbsRow(i).getWillShow()) 
     			{
     				JUtil.DatoXLS(cellStyles, rowl, i, m_colL2.getAbsRow(i).getColName(), "general", "STRING", m_colL2.getAbsRow(i).getAlinHor(), "encabezado", "fenc", request);
     				//celll2.getCellStyle().setBorderTop(CellStyle.BORDER_THIN);
					//celll2.getCellStyle().setBorderBottom(CellStyle.BORDER_THIN);
				}
    		}	
     	}

    	if(m_bSelectL3.booleanValue())
    	{
    		Row rowl = sheet.createRow(nrow++);
    		for(int i = 0; i < m_colL3.getNumRows(); i++)
	      	{ 
	      		if(m_colL3.getAbsRow(i).getWillShow()) 
	      		{
	      			JUtil.DatoXLS(cellStyles, rowl, i, m_colL3.getAbsRow(i).getColName(), "general", "STRING", m_colL3.getAbsRow(i).getAlinHor(), "encabezado", "fenc", request);
	      			//celll3.getCellStyle().setBorderTop(CellStyle.BORDER_THIN);
						//celll3.getCellStyle().setBorderBottom(CellStyle.BORDER_THIN);
				}
	     	}	
	    }
		  
    	if(m_bSelectL1.booleanValue())
		{
			for (int RL1 = 0; RL1 < m_setL1.getNumRows(); RL1++)
			{ 
				Row rowl1 = sheet.createRow(nrow++);
				for(int CL1 = 0; CL1 < m_colL1.getNumRows(); CL1++)
				{	
					if(m_colL1.getAbsRow(CL1).getWillShow()) 
						JUtil.DatoXLS(cellStyles, rowl1, CL1, m_setL1.getAbsRow(RL1).getSTS(m_colL1.getAbsRow(CL1).getColName()), m_colL1.getAbsRow(CL1).getFormat(), m_colL1.getAbsRow(CL1).getBindDataType(), m_colL1.getAbsRow(CL1).getAlinHor(), null, "fnorm", request);
				}	
     			
     			// Nivel 2
     			if(m_bSelectL2.booleanValue())
     			{
     				for (int RL2 = 0; RL2 < m_setL1.getAbsRow(RL1).getSetL2().getNumRows(); RL2++)
     				{ 
     					Row rowl2 = sheet.createRow(nrow++);
     					for(int CL2 = 0; CL2 < m_colL2.getNumRows(); CL2++)
     					{
     						if(m_colL2.getAbsRow(CL2).getWillShow()) 
     							JUtil.DatoXLS(cellStyles, rowl2, CL2, m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSTS(m_colL2.getAbsRow(CL2).getColName()), m_colL2.getAbsRow(CL2).getFormat(), m_colL2.getAbsRow(CL2).getBindDataType(), m_colL2.getAbsRow(CL2).getAlinHor(), null, "fnorm", request); 
     					} 
      				
      				// Nivel 3
      				if(m_bSelectL3.booleanValue())
      				{
      					for(int RL3 = 0; RL3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getNumRows(); RL3++)
      					{ 
      						Row rowl3 = sheet.createRow(nrow++);
      						for(int CL3 = 0; CL3 < m_colL3.getNumRows(); CL3++)
      						{ 
      							if(m_colL3.getAbsRow(CL3).getWillShow()) 
      								JUtil.DatoXLS(cellStyles, rowl3, CL3, m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getAbsRow(RL3).getSTS(m_colL3.getAbsRow(CL3).getColName()), m_colL3.getAbsRow(CL3).getFormat(), m_colL3.getAbsRow(CL3).getBindDataType(), m_colL3.getAbsRow(CL3).getAlinHor(), null, "fnorm", request); 
      							
      						}  
      				
      					}	
      					if(m_bComputeL3.booleanValue())
      					{
      						for (int RC3 = 0; RC3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getNumRows(); RC3++)
      						{ 
      							Row rowc3 = sheet.createRow(nrow++);
      							for(int CC3 = 0; CC3 < m_colCL3.getNumRows(); CC3++)
      							{
      								if(m_colCL3.getAbsRow(CC3).getWillShow()) 
      									JUtil.DatoXLS(cellStyles, rowc3, CC3, m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getAbsRow(RC3).getSTS(m_colCL3.getAbsRow(CC3).getColName()), m_colCL3.getAbsRow(CC3).getFormat(), m_colCL3.getAbsRow(CC3).getBindDataType(), m_colCL3.getAbsRow(CC3).getAlinHor(), "agregado", "fenc", request); 
      							}	
 							}
     					} // Fin SI CL3
     				} // Fin SI L3
     			}
     			if(m_bComputeL2.booleanValue())
     			{
     				for (int RC2 = 0; RC2 < m_setL1.getAbsRow(RL1).getSetCL2().getNumRows(); RC2++)
     				{ 
     					Row rowc2 = sheet.createRow(nrow++);
     					for(int CC2 = 0; CC2 < m_colCL2.getNumRows(); CC2++)
     					{
     						if(m_colCL2.getAbsRow(CC2).getWillShow()) 
     							JUtil.DatoXLS(cellStyles, rowc2, CC2, m_setL1.getAbsRow(RL1).getSetCL2().getAbsRow(RC2).getSTS(m_colCL2.getAbsRow(CC2).getColName()), m_colCL2.getAbsRow(CC2).getFormat(), m_colCL2.getAbsRow(CC2).getBindDataType(), m_colCL2.getAbsRow(CC2).getAlinHor(), "agregado", "fenc", request); 
     					} 
     				}
     			} // Fin SI CL2
     		} // Fin SI L2
     	}
	
		if(m_bComputeL1.booleanValue())
		{
			for (int RC1 = 0; RC1 < m_setCL1.getNumRows(); RC1++)
			{ 
				Row rowc1 = sheet.createRow(nrow++);
				for(int CC1 = 0; CC1 < m_colCL1.getNumRows(); CC1++)
				{
					if(m_colCL1.getAbsRow(CC1).getWillShow()) 
						JUtil.DatoXLS(cellStyles, rowc1, CC1, m_setCL1.getAbsRow(RC1).getSTS(m_colCL1.getAbsRow(CC1).getColName()), m_colCL1.getAbsRow(CC1).getFormat(), m_colCL1.getAbsRow(CC1).getBindDataType(), m_colCL1.getAbsRow(CC1).getAlinHor(), "agregado", "fenc", request); 
				} 
       		}
      	} // Fin SI CL1
	} // Fin SI L1
		
    int colsmer;
    if(m_colL1.getNumRows() > m_colL2.getNumRows() && m_colL1.getNumRows() > m_colL3.getNumRows())
    	colsmer = m_colL1.getNumRows() -1;
	else if(m_colL2.getNumRows() > m_colL1.getNumRows() && m_colL2.getNumRows() > m_colL3.getNumRows())
		colsmer = m_colL2.getNumRows() -1;
	else
		colsmer = m_colL3.getNumRows() -1;
		
    sheet.addMergedRegion(new CellRangeAddress(0,0,0,(colsmer == -1 ? 0 : colsmer)));
	    
  }	    
  
  @SuppressWarnings({ "unused" })
	public void CargarReporte(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, DocumentException
  {
    super.doPost(request, response);

    response.setContentType("text/html");
   
    int m_ID_Report = Integer.parseInt(request.getParameter("REPID"));
    int m_LinesNum = 0;

    Boolean m_bFilterL1 = new Boolean(true);
    Boolean m_bSelectL1 = new Boolean(true);
    Boolean m_bSelectL2 = new Boolean(true);
    Boolean m_bSelectL3 = new Boolean(true);
    Boolean m_bComputeL1 = new Boolean(true);
    Boolean m_bComputeL2 = new Boolean(true);
    Boolean m_bComputeL3 = new Boolean(true);

    // Construye las estructuras
    JReportesSet m_RepSet = new JReportesSet(request);
    m_RepSet.m_Where = "ID_Report = " + m_ID_Report;
    m_RepSet.ConCat(true);
    m_RepSet.Open();
    boolean m_Graficar = m_RepSet.getAbsRow(0).getGraficar();
    
    // empieza por el select de primer nivel
    JReportesBind2Set m_selectL1 = new JReportesBind2Set(request);
    m_selectL1.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '1' and ID_IsCompute = '0'";
    m_selectL1.m_OrderBy = "ID_Sentence ASC";
    m_selectL1.ConCat(true);
    m_selectL1.Open();
    if (m_selectL1.getNumRows() < 1)
      m_bSelectL1 = Boolean.FALSE;

    JReportesBind3Set m_colL1 = new JReportesBind3Set(request);
    m_colL1.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '1' and ID_IsCompute = '0'";
    m_colL1.m_OrderBy = "ID_Column asc";
    m_colL1.ConCat(true);
    m_colL1.Open();

      // sigue por el select de segundo nivel
    JReportesBind2Set m_selectL2 = new JReportesBind2Set(request);
    m_selectL2.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '2' and ID_IsCompute = '0'";
    m_selectL2.m_OrderBy = "ID_Sentence ASC";
    m_selectL2.ConCat(true);
    m_selectL2.Open();
    if (m_selectL2.getNumRows() < 1)
      m_bSelectL2 = Boolean.FALSE;

    JReportesBind3Set m_colL2 = new JReportesBind3Set(request);
    m_colL2.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '2' and ID_IsCompute = '0'";
    m_colL2.m_OrderBy = "ID_Column asc";
    m_colL2.ConCat(true);
    m_colL2.Open();

    // sigue por el select de tercer nivel
    JReportesBind2Set m_selectL3 = new JReportesBind2Set(request);
    m_selectL3.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '3' and ID_IsCompute = '0'";
    m_selectL3.m_OrderBy = "ID_Sentence ASC";
    m_selectL3.ConCat(true);
    m_selectL3.Open();
    if (m_selectL3.getNumRows() < 1)
      m_bSelectL3 = Boolean.FALSE;

    JReportesBind3Set m_colL3 = new JReportesBind3Set(request);
    m_colL3.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '3' and ID_IsCompute = '0'";
    m_colL3.m_OrderBy = "ID_Column asc";
    m_colL3.ConCat(true);
    m_colL3.Open();


    // Ahora revisa las sentencias compute
    // empieza por el compute de primer nivel
    JReportesBind2Set m_computeL1 = new JReportesBind2Set(request);
    m_computeL1.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '1' and ID_IsCompute = '1'";
    m_computeL1.m_OrderBy = "ID_Sentence ASC";
    m_computeL1.ConCat(true);
    m_computeL1.Open();
    if (m_computeL1.getNumRows() < 1)
      m_bComputeL1 = Boolean.FALSE;

    JReportesBind3Set m_colCL1 = new JReportesBind3Set(request);
    m_colCL1.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '1' and ID_IsCompute = '1'";
    m_colCL1.m_OrderBy = "ID_Column asc";
    m_colCL1.ConCat(true);
    m_colCL1.Open();

      // sigue por el compute de segundo nivel
    JReportesBind2Set m_computeL2 = new JReportesBind2Set(request);
    m_computeL2.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '2' and ID_IsCompute = '1'";
    m_computeL2.m_OrderBy = "ID_Sentence ASC";
    m_computeL2.ConCat(true);
    m_computeL2.Open();
    if (m_computeL2.getNumRows() < 1)
      m_bComputeL2 = Boolean.FALSE;

    JReportesBind3Set m_colCL2 = new JReportesBind3Set(request);
    m_colCL2.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '2' and ID_IsCompute = '1'";
    m_colCL2.m_OrderBy = "ID_Column asc";
    m_colCL2.ConCat(true);
    m_colCL2.Open();

    // sigue por el compute de tercer nivel
    JReportesBind2Set m_computeL3 = new JReportesBind2Set(request);
    m_computeL3.m_Where = "ID_Report = '" + m_ID_Report +
        "' and ID_Sentence = '3' and ID_IsCompute = '1'";
    m_computeL3.m_OrderBy = "ID_Sentence ASC";
    m_computeL3.ConCat(true);
    m_computeL3.Open();
    if (m_computeL3.getNumRows() < 1)
      m_bComputeL3 = Boolean.FALSE;

    JReportesBind3Set m_colCL3 = new JReportesBind3Set(request);
    m_colCL3.m_Where = "ID_Report = '" + m_ID_Report +
       "' and ID_Sentence = '3' and ID_IsCompute = '1'";
    m_colCL3.m_OrderBy = "ID_Column asc";
    m_colCL3.ConCat(true);
    m_colCL3.Open();

   //System.out.println("CARGADAS LAS SENTENCIAS");
    // Ahora carga los datos
    JReportesLevel1 m_setL1 = new JReportesLevel1(request);
    // Carga la sentencia del compute;
    JReportesCompL1Set m_setCL1 = new JReportesCompL1Set(request);

    if (m_bSelectL1.booleanValue())
    {

      if (m_bFilterL1.booleanValue())
      {
        // si existe un filtro especial lo cargar�
        String sqll1 = VerifyFilerClause(new StringBuffer(m_selectL1.getRow(0).getSelect_Clause()), request);
        m_setL1.setSQL(sqll1);
      }
      else
      {
        //out.println(m_selectL1.getRow(0).getSelect_Clause());
        m_setL1.setSQL(m_selectL1.getRow(0).getSelect_Clause());
      }
      m_setL1.ConCat(true);
      m_setL1.Open();
      m_LinesNum += m_setL1.getNumRows();

      if (m_bComputeL1.booleanValue())
      {

        if (m_bFilterL1.booleanValue()) // si existe un filtro especial lo cargar�
          m_setCL1.setSQL(VerifyFilerClause(new StringBuffer(m_computeL1.getRow(0).getSelect_Clause()), request));
        else
          m_setCL1.setSQL(m_computeL1.getRow(0).getSelect_Clause());

          //AfxMessageBox(sql);
        m_setCL1.ConCat(true);
        m_setCL1.Open();
        m_LinesNum += m_setCL1.getNumRows();
      }

      if (m_bSelectL2.booleanValue())
      {
        for (int contL1 = 0; contL1 < m_setL1.getNumRows(); contL1++)
        {
          REP_LEVEL1 pNodeL1 = (REP_LEVEL1)m_setL1.getAbsRow(contL1);
          pNodeL1.getSetL2().setSQL(VerifyWhereClause(new StringBuffer(m_selectL2.getAbsRow(0).getSelect_Clause()), pNodeL1));
          pNodeL1.getSetL2().ConCat(true);
          pNodeL1.getSetL2().Open();
          m_LinesNum += pNodeL1.getSetL2().getNumRows();

          if (m_bComputeL2.booleanValue())
          {
            pNodeL1.getSetCL2().setSQL(VerifyWhereClause(new StringBuffer(m_computeL2.getRow(0).getSelect_Clause()), pNodeL1));
            pNodeL1.getSetCL2().ConCat(true);
            pNodeL1.getSetCL2().Open();
            m_LinesNum += pNodeL1.getSetCL2().getNumRows();
          }

          if (m_bSelectL3.booleanValue())
          {
            for (int contL2 = 0; contL2 < pNodeL1.getSetL2().getNumRows(); contL2++)
            {
              REP_LEVEL2 pNodeL2 = pNodeL1.getSetL2().getAbsRow(contL2);
              pNodeL2.getSetL3().setSQL(VerifyWhereClause(new StringBuffer(m_selectL3.getAbsRow(0).getSelect_Clause()), pNodeL2));
              pNodeL2.getSetL3().ConCat(true);
              pNodeL2.getSetL3().Open();
              m_LinesNum += pNodeL2.getSetL3().getNumRows();

              if (m_bComputeL3.booleanValue())
              {
                pNodeL2.getSetCL3().setSQL(VerifyWhereClause(new StringBuffer(m_computeL3.getAbsRow(0).getSelect_Clause()), pNodeL2));
                pNodeL2.getSetCL3().ConCat(true);
                pNodeL2.getSetCL3().Open();
                m_LinesNum += pNodeL2.getSetCL3().getNumRows();
              }
            }
          }
        }
      }
    }

//  Verifica a donde irse
    boolean graficar = false;
    
    if(m_Graficar) // es gráfica
     	  graficar =  (request.getParameter("fsi_sino").equals("1") ? true : false);
   	 
    if(graficar) // es gráfica
    {
  	  String GRAF = request.getParameter("fsi_graf");
  	  boolean enCols = (request.getParameter("fsi_encols").equals("1") ? true : false);
  	  boolean linejes = (request.getParameter("fsi_linejes").equals("1") ? true : false);
  	  byte tipo = Byte.parseByte(request.getParameter("fsi_tipo"));
  	  String fsi_filtro = (String)request.getAttribute("fsi_filtro");
  	  
  	  response.setContentType("image/gif");
  	  OutputStream Out = response.getOutputStream();
      	  
  	  JFsiGraficas graf;
      	  
  	  if(GRAF.equals("BAR"))
  		  graf = new JFsiGrafBarras(m_colL1, m_setL1, m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro, 
  				  enCols, linejes, tipo, m_RepSet.getAbsRow(0).getHW(), m_RepSet.getAbsRow(0).getVW() );
  	  else if(GRAF.equals("LIN"))
  		  graf = new JFsiGrafLineas(m_colL1, m_setL1, m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro, 
  				  enCols, linejes, tipo, m_RepSet.getAbsRow(0).getHW(), m_RepSet.getAbsRow(0).getVW() ); 
  	  else if(GRAF.equals("CIRC"))
    		  graf = new JFsiGrafCirc(m_colL1, m_setL1, m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro, 
        			  enCols, tipo, m_RepSet.getAbsRow(0).getHW(), m_RepSet.getAbsRow(0).getVW() ); 
  	  else
  		  graf = new JFsiGrafAreas(m_colL1, m_setL1, m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro, 
       			  enCols, linejes, tipo, m_RepSet.getAbsRow(0).getHW(), m_RepSet.getAbsRow(0).getVW() ); 
      	  
  	  try
  	  {
  		  new GifEncoder(graf.getGrafica(), Out).encode();
  	  }
  	  catch(IOException ioe)
  	  {
  		  System.out.println("Error en el GIF: " + ioe);
  	  }
    }
    else
    {
	      String rep_permitido = "true";
	      request.setAttribute("rep_permitido", rep_permitido);
	
	      // Ahora pone los atributos para el jsp
	      request.setAttribute("m_RepSet", m_RepSet);
	      request.setAttribute("m_setL1", m_setL1);
	      request.setAttribute("m_setCL1", m_setCL1);
	      request.setAttribute("m_bSelectL1", m_bSelectL1);
	      request.setAttribute("m_bSelectL2", m_bSelectL2);
	      request.setAttribute("m_bSelectL3", m_bSelectL3);
	      request.setAttribute("m_bComputeL1", m_bComputeL1);
	      request.setAttribute("m_bComputeL2", m_bComputeL2);
	      request.setAttribute("m_bComputeL3", m_bComputeL3);
	      request.setAttribute("m_selectL1", m_selectL1);
	      request.setAttribute("m_selectL2", m_selectL2);
	      request.setAttribute("m_selectL3", m_selectL3);
	      request.setAttribute("m_computeL1", m_computeL1);
	      request.setAttribute("m_computeL2", m_computeL2);
	      request.setAttribute("m_computeL3", m_computeL3);
	      request.setAttribute("m_colL1", m_colL1);
	      request.setAttribute("m_colL2", m_colL2);
	      request.setAttribute("m_colL3", m_colL3);
	      request.setAttribute("m_colCL1", m_colCL1);
	      request.setAttribute("m_colCL2", m_colCL2);
	      request.setAttribute("m_colCL3", m_colCL3);

	      if(request.getParameter("exportacion").equals("html"))
	      { 
	    	  response.setContentType("text/html");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarReporteHtml(request, response));
	      }
	      else if(request.getParameter("exportacion").equals("odt"))
	      { 
	    	  response.setContentType("application/vnd.oasis.opendocument.text");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarReporteHtml(request, response));
	      }
	      else if(request.getParameter("exportacion").equals("doc"))
	      { 
	    	  response.setContentType("application/msword");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarReporteHtml(request, response));
	      }
	      else if(request.getParameter("exportacion").equals("rtf"))
	      { 
	    	  response.setContentType("application/rtf");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarReporteHtml(request, response));
	      }
	      else if(request.getParameter("exportacion").equals("xls"))
	      {
	    	  response.setContentType("application/vnd.ms-excel");
	    	  OutputStream Out = response.getOutputStream();
	      	  Workbook wb = new HSSFWorkbook();
	      	  generarArchivoXLS(request, response, wb);
	      	  wb.write(Out);
	      	  Out.close();
	      }
	      else if(request.getParameter("exportacion").equals("ods"))
	      {
	    	  response.setContentType("application/vnd.oasis.opendocument.spreadsheet");
	    	  OutputStream Out = response.getOutputStream();
	    	  Workbook wb = new HSSFWorkbook();
	    	  generarArchivoXLS(request, response, wb);
	    	  wb.write(Out);
	    	  Out.close();
	      }
	      else if(request.getParameter("exportacion").equals("csv"))
	      {
	    	  response.setContentType("text/plain");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarArchivoCSV(request, response, "|"));
	      }
	      else if(request.getParameter("exportacion").equals("xml"))
	      {
	    	  Document document = new Document();
	    	  response.setContentType("application/xml");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarArchivoDOM(request, response, document));
	      }
	      else if(request.getParameter("exportacion").equals("pdf"))
	      {
	    	  response.setContentType("application/pdf");
	    	  OutputStream Out = response.getOutputStream();
	    	  ITextRenderer renderer = new ITextRenderer();
	    	  renderer.setDocumentFromString(generarReporteHtml(request, response));
	    	  renderer.layout();
	    	  renderer.createPDF(Out);
	      }
	      else if(request.getParameter("exportacion").equals("sql"))
	      {
	    	  response.setContentType("text/plain");
	    	  PrintWriter out = response.getWriter();
		      out.print(generarArchivoSQL(request, response));
	      }
	      else
    	  {
    		  response.setContentType("text/plain");
    		  PrintWriter out = response.getWriter();
    		  out.print(generarReporteHtml(request, response));
    	  }
	      
	      //irApag("/forsetiadmin/reportes/rep_reportes_imp.jsp", request, response); 
    }
  }

  private boolean VerificarFiltro(HttpServletRequest request, HttpServletResponse response)
  	throws ServletException, IOException
  {
  	  short idmensaje = -1; String mensaje = "", Descripcion = "";
  	  
        JReportesBindFSet setF = new JReportesBindFSet(request);
        setF.m_Where = "ID_Report = '" + p(request.getParameter("REPID")) + "'";
        setF.m_OrderBy = "ID_Column ASC";
        setF.ConCat(true);
        setF.Open();
        
        for(int i = 0; i < setF.getNumRows(); i++)
        {
      	  String atr = request.getParameter(setF.getAbsRow(i).getPriDataName());
			  String atr2 = request.getParameter(setF.getAbsRow(i).getSecDataName());
		  	  Descripcion += setF.getAbsRow(i).getPriDataName() + ": ";

      	  if(!setF.getAbsRow(i).getFromCatalog()) // No es de catalogo
      	  { 
      		  // --------------------------------------------------------------------------------
      		  	
      			if(setF.getAbsRow(i).getBindDataType().equals("INT") || setF.getAbsRow(i).getBindDataType().equals("BYTE"))
  				{ 
  					if(setF.getAbsRow(i).getPriDefault().equals("mes"))
  					{
  						int mes = Integer.parseInt(atr);
     						Descripcion += JUtil.convertirMesLargo(mes);
  
  					}
  					else if(setF.getAbsRow(i).getPriDefault().length() > 0 && setF.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
  					{
  						Descripcion += JUtil.obtValorDeFiltro(setF.getAbsRow(i).getPriDefault(), atr);
  					}
  					else 
  					{
  						Descripcion += atr;
  					}
  					
   					if(setF.getAbsRow(i).getIsRange())
  					{ 
  
  						if(setF.getAbsRow(i).getSecDefault().equals("mes"))
  						{
  							int mes2 = Integer.parseInt(atr2);
  							Descripcion += " a " + JUtil.convertirMesLargo(mes2);
  						}
  						else if(setF.getAbsRow(i).getSecDefault().length() > 0 && setF.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
  						{
      						Descripcion += " a " + JUtil.obtValorDeFiltro(setF.getAbsRow(i).getSecDefault(), atr2);
  						}
  						else
  						{
  							Descripcion += " a " + atr2;
   						}
  					}				
  				}
  				else if(setF.getAbsRow(i).getBindDataType().equals("BOOL"))
  				{ 
  					Descripcion += atr.equals("1") ? "SI" : "NO";
  				}	
  				else if(setF.getAbsRow(i).getBindDataType().equals("TIME"))
  				{ 
  					Descripcion += atr;
  					if(setF.getAbsRow(i).getIsRange())
  					{ 
  						Descripcion += " a " + atr2;
  					}				
  				}
  				else if(setF.getAbsRow(i).getBindDataType().equals("DECIMAL") || setF.getAbsRow(i).getBindDataType().equals("MONEY"))
  				{ 
  					if(setF.getAbsRow(i).getPriDefault().length() > 0 && setF.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
  					{
     						Descripcion += JUtil.obtValorDeFiltro(setF.getAbsRow(i).getPriDefault(), atr);
     					}
  					else
  					{
  						Descripcion += atr;
   					}
  					
   					if(setF.getAbsRow(i).getIsRange())
  					{ 
   						if(setF.getAbsRow(i).getSecDefault().length() > 0 && setF.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
  						{
      						Descripcion += " a " + JUtil.obtValorDeFiltro(setF.getAbsRow(i).getSecDefault(), atr2);
  						}
  						else
  						{
  							Descripcion += " a " + atr2;
   						}
  					}				
  				}
  				else // STRING O CUALQUIER OTRO MAS
  				{
  					if(setF.getAbsRow(i).getPriDefault().length() > 0 && setF.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
  					{
  						Descripcion += JUtil.obtValorDeFiltro(setF.getAbsRow(i).getPriDefault(), atr);
  					}
  					else
  					{ 
  						Descripcion += atr;
   					}
  					
   					if(setF.getAbsRow(i).getIsRange())
  					{ 
    						if(setF.getAbsRow(i).getSecDefault().length() > 0 && setF.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
  						{
    							Descripcion += JUtil.obtValorDeFiltro(setF.getAbsRow(i).getSecDefault(), atr);
  						}
  						else
  						{
  							Descripcion += " a " + atr2;
  						}	
  					}				
  				}
  				  
      		  
      	  } // --------------------------------------------------------------------------------
      	  else // Si es de catalogo
      	  {
      		  JListasCatalogosSet cats = new  JListasCatalogosSet(request);
      		  cats.ConCat(true);
      						
      		  int idcatalogo;
      		  try {
      			  idcatalogo = Integer.parseInt(setF.getAbsRow(i).getSelect_Clause());
      		  } catch(NumberFormatException e) {
      			  idcatalogo = 0;
      		  }
      						
      		  if(idcatalogo == 0) // El catalogo no esta especificado en los catalogos, el filtro simplemente muestra el nombre del atributo y su valor
      		  {
      			  Descripcion +=  request.getParameter(setF.getAbsRow(i).getPriDataName() + "_FSIDESC");
					  if(setF.getAbsRow(i).getIsRange())
					  { 
						Descripcion += " a " + request.getParameter(setF.getAbsRow(i).getSecDataName() + "_FSIDESC");
    				  }		
					  //Descripcion += atr;
      			  //if(setF.getAbsRow(i).getIsRange())
      				//  Descripcion += " a " + atr2;
      		      // continue;
      		  }
      		  else //es de catalogo especificado
      		  {
      			  cats.m_Where = "ID_Catalogo = '" + idcatalogo + "'";
      			  cats.ConCat(true);
      			  cats.Open();
      			  if(!cats.getAbsRow(0).getSeguridad().equals("")) // Significa que es catalogo de seguridad
      			  {
      				  // Los catalogos de seguridad, no pueden ser rango
      				  if(setF.getAbsRow(i).getIsRange())
      				  {
      					  idmensaje = 3;
      					  mensaje = "ERROR: El atributo " + setF.getAbsRow(i).getPriDataName() + " de este filtro, no puede pertenecer a un rango, porque el catálogo requiere seguridad";
      					  break;
      				  }
      				  else
      				  {
      					  // Revisa el nivel de seguridad del catalogo
      					  String usuario = getSesion(request).getID_Usuario();
      					  String seg = cats.getAbsRow(0).getSeguridad();
      					  if(seg.equals("VENTAS")) // Revisa por la seguridad en ventas o en otras entidades
      					  {
      						  JUsuariosSubmoduloVentasPerm set = new JUsuariosSubmoduloVentasPerm(request, usuario, atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de venta " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getDescripcion();
      					  }
      					  else if(seg.equals("COMPRAS"))
      					  {
      						  JUsuariosSubmoduloComprasPerm set = new JUsuariosSubmoduloComprasPerm(request, usuario, atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de compra " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getDescripcion();
      					  }
      					  else if(seg.equals("BANCOS")) // Revisa por la seguridad en ventas
      					  {
      						  JUsuariosSubmoduloBancosPerm set = new JUsuariosSubmoduloBancosPerm(request, usuario, "0", atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de banco " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getCuenta();
      					  }
      					  else if(seg.equals("CAJAS")) // Revisa por la seguridad en ventas
      					  {
      						  JUsuariosSubmoduloBancosPerm set = new JUsuariosSubmoduloBancosPerm(request, usuario, "1", atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de caja " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getCuenta();
      					  }
      					  else if(seg.equals("BODEGAS")) // Revisa por la seguridad en ventas
      					  {
      						  JUsuariosSubmoduloBodegasPerm set = new JUsuariosSubmoduloBodegasPerm(request, usuario, atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de bodega " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getNombre();
      					  }
      					  else if(seg.equals("PRODUCCION")) // Revisa por la seguridad en ventas
      					  {
      						  JUsuariosSubmoduloProduccionPerm set = new JUsuariosSubmoduloProduccionPerm(request, usuario, atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de producción " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  }
      						  Descripcion += set.getAbsRow(0).getDescripcion();
      					  }
      					  else if(seg.equals("NOMINA")) // Revisa por la seguridad en ventas
      					  {
      						  JUsuariosSubmoduloNominaPerm set = new JUsuariosSubmoduloNominaPerm(request, usuario, atr);
      						  set.ConCat(true);
      						  set.Open();
      						  if(set.getNumRows() < 1)
      						  {
      							  idmensaje = 3;
              					  mensaje = "ERROR: No tienes acceso a la entidad de nómina " + atr + " del atributo " + setF.getAbsRow(i).getPriDataName();
              					  break;
      						  } 
      						  Descripcion += set.getAbsRow(0).getDescripcion();
      					  }
      					  else // Revisa por la seguridad en ventas
      					  {
      						  idmensaje = 3;
              				  mensaje = "ERROR: No existe la etiqueta de seguridad: " + seg + ". Revisa con el proveedor del sistema sobre este error";
              				  break;
      						
      					  }
      				  }
      			  }
      			  else
      			  {
    					  Descripcion +=  request.getParameter(setF.getAbsRow(i).getPriDataName() + "_FSIDESC");
    					  if(setF.getAbsRow(i).getIsRange())
    					  { 
    						Descripcion += " a " + request.getParameter(setF.getAbsRow(i).getSecDataName() + "_FSIDESC");
        				  }				

      			  }
      		  }
         	  }
      	  
      	  Descripcion += ", ";
        }

        if(idmensaje != -1)
    	  {
      	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
      	  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
      	  return false;
    	  }
        
        String fsi_filtro;
    	  if(Descripcion == null || Descripcion.equals(""))
    		fsi_filtro = "";
    	  else
    		fsi_filtro = Descripcion.substring(0, Descripcion.length() -2);
    		
        request.setAttribute("fsi_filtro", fsi_filtro);
        
        return true;

}
  
  @SuppressWarnings({ "rawtypes" })
	private String VerifyFilerClause(StringBuffer select, HttpServletRequest request)
    throws ServletException, IOException
  {
          /*System.out.println( "----------------------------" );
          System.out.println( select.toString() );
          System.out.println( "----------------------------" );*/
          
  		//String fsi_filtro = "";

          int initial = select.indexOf("[",0);
          int fin = (initial == -1) ? -1 : select.indexOf("]", initial);

          while(initial != -1 && fin != -1)
          {
                  String key = select.substring(initial+1, fin); // ej ID_Empleado

                  // Carga los valores por default
                  Enumeration nombresAtr = request.getParameterNames();
                  while(nombresAtr.hasMoreElements())
                  {
                      String nombreAtr = (String)nombresAtr.nextElement();
                      String valorAtr = (String)request.getParameter(nombreAtr);
                      if(nombreAtr.equals(key))
                      {
                      	if(valorAtr.matches("\\d{1,2}/(ene|feb|mar|abr|may|jun|jul|ago|sep|oct|nov|dic)/\\d{4}")) // Es fecha....
                      		valorAtr = JUtil.obtFechaSQL(valorAtr);
                          
                      	select.replace(initial, fin+1, valorAtr);
                      	//System.out.println(key  + "|"  + nombreAtr  + "|"  + valorAtr);
                      	//fsi_filtro += " { " + nombreAtr + " " + valorAtr + " } ";
                      }
                  }
                  //System.out.println(key  + "|"  + initial  + "|"  + fin);

                  initial = select.indexOf("[",0);
                  fin = (initial == -1) ? -1 : select.indexOf("]", initial);
          }

          /*System.out.println( "----------------------------" );
          System.out.println( select.toString() );
          System.out.println( "----------------------------" );*/

          //request.setAttribute("fsi_filtro", fsi_filtro);

          return select.toString();

  }

  private String VerifyWhereClause(StringBuffer select, REP_LEVEL1 pNodeL1)
  {
          int initial = select.indexOf("[",0);
          int fin = (initial == -1) ? -1 : select.indexOf("]", initial);

          while(initial != -1 && fin != -1)
          {
                  //String extract = select.substring(initial, (fin + 1));
                  String key = select.substring(initial + 1, fin);
                  String replace = pNodeL1.getSTS(key);
                  //AfxMessageBox(select);
                  select.replace(initial, fin+1, replace);
                  //AfxMessageBox(select);


                  initial = select.indexOf("[",0);
                  fin = (initial == -1) ? -1 : select.indexOf("]", initial);
          }

          return select.toString();
  }

  private String VerifyWhereClause(StringBuffer select, REP_LEVEL2 pNodeL2)
  {
          int initial = select.indexOf("[", 0);
          int fin = (initial == -1) ? -1 : select.indexOf("]", initial);

          while (initial != -1 && fin != -1)
          {
            //String extract = select.substring(initial, (fin + 1));
            String key = select.substring(initial + 1, fin);
            String replace = pNodeL2.getSTS(key);
            //AfxMessageBox(select);
            select.replace(initial, fin + 1, replace);
            //AfxMessageBox(select);

            initial = select.indexOf("[", 0);
            fin = (initial == -1) ? -1 : select.indexOf("]", initial);
    }

    return select.toString();

  }


}
