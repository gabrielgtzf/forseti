<!--
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
-->
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	String SQLCab = (String)request.getAttribute("sqlcab");
	String SQLDet = (String)request.getAttribute("sqldet");
	String ID_Formato = JUtil.p((String)request.getAttribute("ID_Formato"));

	if(SQLCab == null || SQLDet == null || ID_Formato == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
  
  	JFormatosDetSet fs = new JFormatosDetSet(request);
    fs.m_Where = "ID_Formato = '" + ID_Formato + "' and Formato = 'S'";
    fs.m_OrderBy = "ID_Part ASC";
	fs.Open();
	
  	JFormatosDetSet fc = new JFormatosDetSet(request);
    fc.m_Where = "ID_Formato = '" + ID_Formato + "' and Formato = 'C'";
    fc.m_OrderBy = "ID_Part ASC";
	fc.Open();
	
  	JFormatosDetSet fd = new JFormatosDetSet(request);
    fd.m_Where = "ID_Formato = '" + ID_Formato + "' and Formato = 'D'";
    fd.m_OrderBy = "ID_Part ASC";
	fd.Open();

 	JFormatosDetSet ff = new JFormatosDetSet(request);
    ff.m_Where = "ID_Formato = '" + ID_Formato + "' and Formato = 'F'";
    ff.m_OrderBy = "ID_Part ASC";
	ff.Open();

	String fsi_imptit = "", fsi_impetq = "", fsi_impcab = "", fsi_impdet = "";
	float fsi_cab_Alt = 0, fsi_det_Y = 0, fsi_det_Alt = 0, fsi_det_lin = 0;
	int fsi_ventana_Alt = 0, fsi_ventana_Anc = 0;
	
    for(int s = 0; s < fs.getNumRows(); s++)
	{
		if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPTIT"))
			fsi_imptit = fs.getAbsRow(s).getValor();
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPETQ"))
			fsi_impetq = fs.getAbsRow(s).getValor();
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPCAB"))
			fsi_impcab = fs.getAbsRow(s).getValor();
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_IMPDET"))
			fsi_impdet = fs.getAbsRow(s).getValor();
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_DET"))
		{
			fsi_det_Y = fs.getAbsRow(s).getYPos();
			fsi_det_Alt = fs.getAbsRow(s).getAlto();
			fsi_det_lin = Integer.parseInt(fs.getAbsRow(s).getValor());
		}
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_CAB"))
			fsi_cab_Alt = fs.getAbsRow(s).getAlto();
		else if(fs.getAbsRow(s).getEtiqueta().equals("FSI_VENTANA"))
		{
			fsi_ventana_Alt = (int)fs.getAbsRow(s).getAlto();
			fsi_ventana_Anc = (int)fs.getAbsRow(s).getAncho();
		}

	}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript">
	window.resizeTo(<%= fsi_ventana_Anc %>, <%= fsi_ventana_Alt %>);
</script>
<style>
table {
	overflow: auto;
	position: absolute;
	visibility: visible;
	z-index: auto;
}
.fsiIMPTIT {
	<%= fsi_imptit %>
}
.fsiIMPETQ {
	<%= fsi_impetq %>
}
.fsiIMPCAB {
	<%= fsi_impcab %>
}
.fsiIMPDET {
	<%= fsi_impdet %>
}
</style>
<title> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<% 
	JSetDinamico cab = new JSetDinamico(request);
    cab.setSQL(SQLCab);
    cab.Open();

	JSetDinamico det = new JSetDinamico(request);
    det.setSQL(SQLDet);
    det.Open();
	
	// las paginas totales se calculan dividiendo el total de partidas de detalle / el total de lines en el reporte
	int tot_pags;
	if(fsi_det_lin < 1)
	{
		tot_pags = 1;
		fsi_det_lin = 255;
	}
	else if(det.getNumRows() <= fsi_det_lin)
		tot_pags = 1;
	else if((det.getNumRows() % fsi_det_lin) == 0)
		tot_pags = (int)(det.getNumRows() / fsi_det_lin);
	else
		tot_pags = (int)Math.floor(det.getNumRows() / fsi_det_lin) + 1;

	//out.println("!-- Cabecero: " + cab.getNumRows() + " Detalles: " + det.getNumRows() + " Total de paginas: " + tot_pags + " FC: " + fc.getNumRows() + "  LINEAS: " + fsi_det_lin + "--");
	
	for(int pag = 0; pag < tot_pags; pag++)
	{
		//out.println("!-- PAG: " + pag + " DE: " + tot_pags + "--");
		for(int c = 0; c < fc.getNumRows(); c++)
		{
			//out.println("!-- C: " + c + " DE fc.getNumRows(): " + fc.getNumRows() + "--");
			if(fc.getAbsRow(c).getEtiqueta().equals("FSI_ETIQUETA"))
			{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fc.getAbsRow(c).getXPos() %>mm; top:<%= ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) %>mm; height:<%= fc.getAbsRow(c).getAlto() %>mm; width:<%= fc.getAbsRow(c).getAncho() %>mm;">
          			<tr>
						<td align="<%= fc.getAbsRow(c).getAlinHor() %>" valign="<%= fc.getAbsRow(c).getAlinVer() %>" style="color:#<%= fc.getAbsRow(c).getFGColor() %>;" class="fsiIMPETQ"><%= fc.getAbsRow(c).getValor() %></td>
		  			</tr>
				</table>
<%
			}
			else if(fc.getAbsRow(c).getEtiqueta().equals("FSI_TITULO"))
			{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fc.getAbsRow(c).getXPos() %>mm; top:<%= ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) %>mm; height:<%= fc.getAbsRow(c).getAlto() %>mm; width:<%= fc.getAbsRow(c).getAncho() %>mm;">
          			<tr>
						<td align="<%= fc.getAbsRow(c).getAlinHor() %>" valign="<%= fc.getAbsRow(c).getAlinVer() %>" style="color:#<%= fc.getAbsRow(c).getFGColor() %>;" class="fsiIMPTIT"><%= fc.getAbsRow(c).getValor() %></td>
		  			</tr>
				</table>
<%
			}
			else if(fc.getAbsRow(c).getEtiqueta().equals("FSI_LH"))
			{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fc.getAbsRow(c).getXPos() %>mm; top:<%= ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) %>mm; height:<%= fc.getAbsRow(c).getAlto() %>mm; width:<%= fc.getAbsRow(c).getAncho() %>mm;">
          			<tr>
				      <td><img src="../imgfsi/<%= fc.getAbsRow(c).getValor() %>.gif" style="height:<%= fc.getAbsRow(c).getAlto() %>mm; width:<%= fc.getAbsRow(c).getAncho() %>mm;" border="0"></td>
		  			</tr>
				</table>
<%
			}
			else
			{
				//out.println("!-- ETIQUETA DIFERENTE FC: " + fc.getAbsRow(c).getEtiqueta() + " --");
				//out.println("!-- cabcols: " + cab.getNumCols() + " --");
				for(int dc = 0; dc < cab.getNumCols(); dc++) // recorre las columnas
				{
					//out.println(fc.getAbsRow(c).getEtiqueta() + " / " + cab.getCol(dc).getNombreCol() + "<br>");
					if(fc.getAbsRow(c).getEtiqueta().equalsIgnoreCase(cab.getCol(dc).getNombreCol())) // si la etiqueta es igual all nombre de la columna
					{
						//out.println(fc.getAbsRow(c).getEtiqueta() + " / " + cab.getCol(dc).getNombreCol() + "<br>");
						for(int ntc = 0; ntc < cab.getNumRows(); ntc++) // recorre el set del  cabecero
						{
							String cabval = JUtil.FormatearImp(cab.getAbsRow(ntc).getSTS(cab.getCol(dc).getNombreCol()), fc.getAbsRow(c).getValor(), cab.getCol(dc).getNombreTipoCol(), request, cab.getAbsRow(ntc).getSTS("id_moneda"), cab.getAbsRow(ntc).getSTS("moneda"));
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fc.getAbsRow(c).getXPos() %>mm; top:<%= ((fsi_cab_Alt * pag) + fc.getAbsRow(c).getYPos()) %>mm; height:<%= fc.getAbsRow(c).getAlto() %>mm; width:<%= fc.getAbsRow(c).getAncho() %>mm;">
          			<tr>
						<td align="<%= fc.getAbsRow(c).getAlinHor() %>" valign="<%= fc.getAbsRow(c).getAlinVer() %>" style="color:#<%= fc.getAbsRow(c).getFGColor() %>;" class="fsiIMPCAB"><%= cabval %></td>
		  			</tr>
				</table>
<%		
						}
						break;
					}
				}
			}
	    }
	}
	
	// Ahora trabaja con los detalles. actua diferente porque no se imprimen en cada pagina de datos, sino que se imprimen los respectivos
	// registros en la posicion adecuada
	int pagaux = 1;
	int cabAlt = 0;
	
	for(int ntc = 0; ntc < det.getNumRows(); ntc++) // recorre el set del detalle primero
	{
		int pag;
		if((ntc+1) <= fsi_det_lin)
		{
			pag = 1;
			cabAlt++;
		}
		else if(((ntc+1) % fsi_det_lin) == 0)
		{
			pag = (int)((ntc+1) / fsi_det_lin);
			if(pag > pagaux)
			{
				pagaux = pag;
				cabAlt = 1;
			}
			else
				cabAlt++;
		}
		else
		{
			pag = (int)Math.floor((ntc+1) / fsi_det_lin) + 1;
			if(pag > pagaux)
			{
				pagaux = pag;
				cabAlt = 1;
			}
			else
				cabAlt++;
		}

		for(int d = 0; d < fd.getNumRows(); d++)
		{
			if(fd.getAbsRow(d).getEtiqueta().equals("FSI_ETIQUETA"))
			{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fd.getAbsRow(d).getXPos() %>mm; top:<%= ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ) %>mm; height:<%= fd.getAbsRow(d).getAlto() %>mm; width:<%= fd.getAbsRow(d).getAncho() %>mm;">
          			<tr>
						<td align="<%= fd.getAbsRow(d).getAlinHor() %>" valign="<%= fd.getAbsRow(d).getAlinVer() %>" style="color:#<%= fd.getAbsRow(d).getFGColor() %>;" class="fsiIMPETQ"><%= fd.getAbsRow(d).getValor() %></td>
		  			</tr>
				</table>
<%
			}
			else if(fd.getAbsRow(d).getEtiqueta().equals("FSI_LH"))
			{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fd.getAbsRow(d).getXPos() %>mm; top:<%= ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ) %>mm; height:<%= fd.getAbsRow(d).getAlto() %>mm; width:<%= fd.getAbsRow(d).getAncho() %>mm;">
          			<tr>
				      <td><img src="../imgfsi/<%= fd.getAbsRow(d).getValor() %>.gif" style="height:<%= fd.getAbsRow(d).getAlto() %>mm; width:<%= fd.getAbsRow(d).getAncho() %>mm;" border="0"></td>
		  			</tr>
				</table>
<%
			}
			else
			{
				for(int dd = 0; dd < det.getNumCols(); dd++) // recorre las columnas
				{
					if(fd.getAbsRow(d).getEtiqueta().equalsIgnoreCase(det.getCol(dd).getNombreCol())) // si la etiqueta es el nombre de la columna
					{
						String detval = JUtil.FormatearImp(det.getAbsRow(ntc).getSTS(det.getCol(dd).getNombreCol()), fd.getAbsRow(d).getValor(), det.getCol(dd).getNombreTipoCol(), request, null, null);
						out.println("<!-- Pag m1: " + (pag -1) + " fsi_cab_Alt: " + fsi_cab_Alt + " fsi_det_Y: " + fsi_det_Y + " fsi_det_Alt: " + fsi_det_Alt + "  (cabAlt-1): " + (cabAlt-1)  + "  fd.getAbsRow(d).getYPos(): " + fd.getAbsRow(d).getYPos() + " TOTAL: " + ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ) + " -->\n");
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= fd.getAbsRow(d).getXPos() %>mm; top:<%= ((pag-1) * fsi_cab_Alt) + ( fsi_det_Y + ( fsi_det_Alt * (cabAlt-1) ) + fd.getAbsRow(d).getYPos() ) %>mm; height:<%= fd.getAbsRow(d).getAlto() %>mm; width:<%= fd.getAbsRow(d).getAncho() %>mm;">
   	       			<tr>
						<td align="<%= fd.getAbsRow(d).getAlinHor() %>" valign="<%= fd.getAbsRow(d).getAlinVer() %>" style="color:#<%= fd.getAbsRow(d).getFGColor() %>;" class="fsiIMPDET"><%= detval %></td>
		  			</tr>
				</table>
<%	
						break;						
					}
				}
			}
		}
	}
	
	// Termina por imprimir el final. este se refiere a cuando hay cabecero que se imprima no en cada hoja, sino a partir del final del ultimo registro de detalle
	for(int f = 0; f < ff.getNumRows(); f++)
	{
		if(ff.getAbsRow(f).getEtiqueta().equals("FSI_ETIQUETA"))
		{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= ff.getAbsRow(f).getXPos() %>mm; top:<%= ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) %>mm; height:<%= ff.getAbsRow(f).getAlto() %>mm; width:<%= ff.getAbsRow(f).getAncho() %>mm;">
          			<tr>
						<td align="<%= ff.getAbsRow(f).getAlinHor() %>" valign="<%= ff.getAbsRow(f).getAlinVer() %>" style="color:#<%= ff.getAbsRow(f).getFGColor() %>;" class="fsiIMPETQ"><%= ff.getAbsRow(f).getValor() %></td>
		  			</tr>
				</table>
<%
		}
		else if(ff.getAbsRow(f).getEtiqueta().equals("FSI_TITULO"))
		{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= ff.getAbsRow(f).getXPos() %>mm; top:<%=  ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) %>mm; height:<%= ff.getAbsRow(f).getAlto() %>mm; width:<%= ff.getAbsRow(f).getAncho() %>mm;">
          			<tr>
						<td align="<%= ff.getAbsRow(f).getAlinHor() %>" valign="<%= ff.getAbsRow(f).getAlinVer() %>" style="color:#<%= ff.getAbsRow(f).getFGColor() %>;" class="fsiIMPTIT"><%= ff.getAbsRow(f).getValor() %></td>
		  			</tr>
				</table>
<%
		}
		else if(ff.getAbsRow(f).getEtiqueta().equals("FSI_LH"))
		{
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= ff.getAbsRow(f).getXPos() %>mm; top:<%=  ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) %>mm; height:<%= ff.getAbsRow(f).getAlto() %>mm; width:<%= ff.getAbsRow(f).getAncho() %>mm;">
          			<tr>
				      <td><img src="../forsetiweb/<%= ff.getAbsRow(f).getValor() %>.gif" style="height:<%= ff.getAbsRow(f).getAlto() %>mm; width:<%= ff.getAbsRow(f).getAncho() %>mm;" border="0"></td>
		  			</tr>
				</table>
<%
		}
		else
		{
			for(int dc = 0; dc < cab.getNumCols(); dc++) // recorre las columnas
			{
				if(ff.getAbsRow(f).getEtiqueta().equalsIgnoreCase(cab.getCol(dc).getNombreCol())) // si la etiqueta es igual all nombre de la columna
				{
					for(int ntc = 0; ntc < cab.getNumRows(); ntc++) // recorre el set del  cabecero
					{
						String cabval = JUtil.FormatearImp(cab.getAbsRow(ntc).getSTS(cab.getCol(dc).getNombreCol()), ff.getAbsRow(f).getValor(), cab.getCol(dc).getNombreTipoCol(), request, cab.getAbsRow(ntc).getSTS("id_moneda"), cab.getAbsRow(ntc).getSTS("moneda"));
%> 
				<table border="0" cellpadding="0" cellspacing="0" style="left:<%= ff.getAbsRow(f).getXPos() %>mm; top:<%=  ( (fsi_det_Y  + (fsi_det_Alt * det.getNumRows()))  + ff.getAbsRow(f).getYPos() ) %>mm; height:<%= ff.getAbsRow(f).getAlto() %>mm; width:<%= ff.getAbsRow(f).getAncho() %>mm;">
          			<tr>
						<td align="<%= ff.getAbsRow(f).getAlinHor() %>" valign="<%= ff.getAbsRow(f).getAlinVer() %>" style="color:#<%= ff.getAbsRow(f).getFGColor() %>;" class="fsiIMPCAB"><%= cabval %></td>
		  			</tr>
				</table>
<%		
					}
					break;
				}
			}
		}
	}
%>
</body>
</html>
