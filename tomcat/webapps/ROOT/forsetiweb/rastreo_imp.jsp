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
<%@ page import="forseti.*, java.util.*, java.io.*"%>
<%
	String rastreo_imp = (String)request.getAttribute("rastreo_imp");
	if(rastreo_imp == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	JRastreo rastreo = (JRastreo)request.getAttribute("rastreo");
	int Niv = rastreo.getNiveles();
	String titulo =  rastreo.getTitulo();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../compfsi/staticbar.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clockCef"> 
        			<input type="button" name="cerrar" onClick="javascript:history.back()" value="<%= JUtil.Msj("GLB","GLB","GLB","CERRAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="109" bgcolor="#333333">&nbsp;</td>
	</tr>
	<tr> 
    	<td> 
<%
	for(int N = 1; N <= Niv; N++)
	{
		//System.out.println(JUtil.Msj("GLB","GLB","GLB","RASTREO-IMP",N));
%>
			<table width="100%" border="0">
        		<tr>
				  <td height="30" class="clockCef" align="center"><%= JUtil.Msj("GLB","GLB","GLB","RASTREO-IMP",N) %></td>
			  	</tr>
			</table>
<%
		for(int ne = 1; ne <= rastreo.getNumElmsNiv(N); ne++)
		{
			//System.out.println(rastreo.getTitulo(N,ne));
%>
			<table width="100%" border="0">
        		<tr>
				  <td class="txtChicoBco" bgcolor="#999999" align="center"><%= rastreo.getTitulo(N,ne) %></td>
			  	</tr>
			</table>
			<table width="100%" border="0">
<%
			for(int i = 0; i < rastreo.NumCabs(N,ne); i++)
			{
				//System.out.println(rastreo.CabsEtq(N,ne)[i]);
				//System.out.println(rastreo.CabsVal(N,ne)[i]);
%>
			  <tr>
				<td class="titChicoAzc"><%= rastreo.CabsEtq(N,ne)[i] %></td>
				<td><%= rastreo.CabsVal(N,ne)[i] %></td>
			  </tr>
<%
			}
%>
			</table>
			<table width="100%" border="0">
			  <tr>
<%
			for(int i = 0; i < rastreo.NumConceptos(N,ne); i++)
			{
				//System.out.println(rastreo.Conceptos(N,ne)[i]);
%>
				<td class="titChicoNeg"><%= rastreo.Conceptos(N,ne)[i] %></td>
<%
			}
%>
			  </tr>
<%
			for(int i = 0; i < rastreo.NumDets(N,ne); i++)
			{
%>			  
  			  <tr>
<%
				for(int j = 0; j < rastreo.NumConceptos(N,ne); j++)
				{
					//System.out.println(rastreo.DetsDetalles(N,ne,i)[j]);
%>
				<td><%= rastreo.DetsDetalles(N,ne,i)[j] %></td>
<%
				}
%>
			  </tr>
<%
				if(rastreo.tieneCECHQ(N,ne,i))
				{
%>
			  <tr>
			    <td colspan="<%= rastreo.NumConceptos(N,ne) %>">
<%
					for(int j = 0; j < rastreo.DetsCECHQ(N,ne,i).length; j++)
					{ 
%>
				  <table width="65%" border="0">
				  	<td class="txtChicoAzc" width="50"><%= rastreo.DetsCECHQ(N,ne,i)[j][0] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][1] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][2] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][3] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][4] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][5] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][6] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][7] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][8] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][9] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCECHQ(N,ne,i)[j][10] %></td>
				  </table>
<%
					}
%>
				</td>
			  </tr>	
<%
				}
				
				if(rastreo.tieneCETRN(N,ne,i))
				{
%>
			  <tr>
			    <td colspan="<%= rastreo.NumConceptos(N,ne) %>">
<%
					for(int j = 0; j < rastreo.DetsCETRN(N,ne,i).length; j++)
					{ 
%>
				  <table width="80%" border="0">
				  	<td class="txtChicoAzc" width="50"><%= rastreo.DetsCETRN(N,ne,i)[j][0] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][1] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][2] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][3] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][4] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][5] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][6] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][7] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][8] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][9] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][10] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][11] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCETRN(N,ne,i)[j][12] %></td>
				  </table>
<%
					}
%>
				</td>
			  </tr>	
<%
				}
				
				if(rastreo.tieneCEOMP(N,ne,i))
				{
%>
			  <tr>
			    <td colspan="<%= rastreo.NumConceptos(N,ne) %>">
<%
					for(int j = 0; j < rastreo.DetsCEOMP(N,ne,i).length; j++)
					{ 
%>
				  <table width="65%" border="0">
				  	<td class="txtChicoAzc" width="50"><%= rastreo.DetsCEOMP(N,ne,i)[j][0] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][1] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][2] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][3] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][4] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][5] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][6] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEOMP(N,ne,i)[j][7] %></td>
				  </table>
<%
					}
%>
				</td>
			  </tr>	
<%
				}
				
				if(rastreo.tieneCEXML(N,ne,i))
				{
%>
			  <tr>
			    <td colspan="<%= rastreo.NumConceptos(N,ne) %>">
<%
					for(int j = 0; j < rastreo.DetsCEXML(N,ne,i).length; j++)
					{ 
%>
				  <table width="40%" border="0">
				  	<td class="txtChicoAzc" width="50"><%= rastreo.DetsCEXML(N,ne,i)[j][0] %></td>
<%
						if((rastreo.DetsCEXML(N,ne,i)[j][0]).equals("XML:"))
						{
%>
				  	<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][1] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][4] %></td>
<%
						}
						else if((rastreo.DetsCEXML(N,ne,i)[j][0]).equals("CBB:"))
						{
%>
				  	<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][2] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][3] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][4] %></td>
<%
						}
						else
						{
%>
				  	<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][5] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][6] %></td>
<%
						}
%>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][7] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][8] %></td>
					<td class="txtChicoAzc"><%= rastreo.DetsCEXML(N,ne,i)[j][9] %></td>
				  </table>
<%
					}
%>
				</td>
			  </tr>	
<%
				}
			}
%>
 			</table>
<%			
		}
	}
%>
    	</td>
	</tr>
</table>
</body>
</html>
