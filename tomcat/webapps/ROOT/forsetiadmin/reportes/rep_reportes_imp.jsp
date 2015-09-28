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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*" contentType="application/xhtml+xml" %>
<%
	String rep_permitido = (String)request.getAttribute("rep_permitido");
	if(rep_permitido == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
  
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
	
%>
<!--DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"-->
<html>
<head>
<script language="JavaScript">
	window.resizeTo(<%= m_RepSet.getAbsRow(0).getHW() %>, <%= m_RepSet.getAbsRow(0).getVW() %>);
</script>
<title><%= m_RepSet.getAbsRow(0).getDescription() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
<!--
.fsiTitulo {
	<%= (( m_RepSet.getAbsRow(0).getTitulo() == null || m_RepSet.getAbsRow(0).getTitulo().equals("") ) ? fsiTitulo : m_RepSet.getAbsRow(0).getTitulo()) %>
}
.fsiEncL1 {
	<%= (( m_RepSet.getAbsRow(0).getEncL1() == null || m_RepSet.getAbsRow(0).getEncL1().equals("") ) ? fsiEncL1 : m_RepSet.getAbsRow(0).getEncL1()) %>
}
.fsiEncL2 {
	<%= (( m_RepSet.getAbsRow(0).getEncL2() == null || m_RepSet.getAbsRow(0).getEncL2().equals("") ) ? fsiEncL2 : m_RepSet.getAbsRow(0).getEncL2()) %>
}
.fsiEncL3 {
	<%= (( m_RepSet.getAbsRow(0).getEncL3() == null || m_RepSet.getAbsRow(0).getEncL3().equals("") ) ? fsiEncL3 : m_RepSet.getAbsRow(0).getEncL3()) %>
}
.fsiL1 {
	<%= (( m_RepSet.getAbsRow(0).getL1() == null || m_RepSet.getAbsRow(0).getL1().equals("") ) ? fsiL1 : m_RepSet.getAbsRow(0).getL1()) %>
}
.fsiL2 {
	<%= (( m_RepSet.getAbsRow(0).getL2() == null || m_RepSet.getAbsRow(0).getL2().equals("") ) ? fsiL2 : m_RepSet.getAbsRow(0).getL2()) %>
}
.fsiL3 {
	<%= (( m_RepSet.getAbsRow(0).getL3() == null || m_RepSet.getAbsRow(0).getL3().equals("") ) ? fsiL3 : m_RepSet.getAbsRow(0).getL3()) %>
}
.fsiCL1 {
	<%= (( m_RepSet.getAbsRow(0).getCL1() == null || m_RepSet.getAbsRow(0).getCL1().equals("") ) ? fsiCL1 : m_RepSet.getAbsRow(0).getCL1()) %>
}
.fsiCL2 {
	<%= (( m_RepSet.getAbsRow(0).getCL2() == null || m_RepSet.getAbsRow(0).getCL2().equals("") ) ? fsiCL2 : m_RepSet.getAbsRow(0).getCL2()) %>
}
.fsiCL3 {
	<%= (( m_RepSet.getAbsRow(0).getCL3() == null || m_RepSet.getAbsRow(0).getCL3().equals("") ) ? fsiCL3 : m_RepSet.getAbsRow(0).getCL3()) %>
}
-->
</style>
</head>
<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="fsiTitulo" align="center"><%= m_RepSet.getAbsRow(0).getDescription() + " " + fsi_filtro %></td>
  </tr>
  <tr>
    <td class="fsiL1" align="center">&nbsp;</td>
  </tr>
  <tr>
	<td><img src="../../forsetiweb/imgfsi/t_negra.gif" style="height:0.5mm; width:100%;" border="0"></td>
  </tr>  
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td>
<%	if(m_bSelectL1.booleanValue())
	{	%>
			  <table width="100%" border="0" cellspacing="0" cellpadding="1">
			  	<tr>
<%	if(m_selectL1.getAbsRow(0).getTabPrintPnt() > 0) 
		{ %> 
		  <td class="fsiEncL1" width="<%= m_selectL1.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
	<% 	}	
		for(int i = 0; i < m_colL1.getNumRows(); i++)
		{ 
			if(m_colL1.getAbsRow(i).getWillShow()) 
			{ %>
				  <td class="fsiEncL1" width="<%= m_colL1.getAbsRow(i).getAncho() %>%" align="<%= m_colL1.getAbsRow(i).getAlinHor() %>"<%= (( m_colL1.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL1.getAbsRow(i).getFGColor() + ";\"" : "" ) %>><%= m_colL1.getAbsRow(i).getColName() %></td>
	<%		} 
		}	%>
				</tr>
			  </table>
 <% }
%>			</td>
		  </tr>
		  <tr>
			<td>
<%	if(m_bSelectL2.booleanValue())
	{	%>
			  <table width="100%" border="0" cellspacing="0" cellpadding="1">
			  	<tr>
<%	if(m_selectL2.getAbsRow(0).getTabPrintPnt() > 0) 
		{ %> 
		  <td class="fsiEncL2" width="<%= m_selectL2.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
	<% 	}	
		for(int i = 0; i < m_colL2.getNumRows(); i++)
		{ 
			if(m_colL2.getAbsRow(i).getWillShow()) 
			{ %>
				  <td class="fsiEncL2" width="<%= m_colL2.getAbsRow(i).getAncho() %>%" align="<%= m_colL2.getAbsRow(i).getAlinHor() %>"<%= (( m_colL2.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL2.getAbsRow(i).getFGColor() + ";\"" : "" ) %>><%= m_colL2.getAbsRow(i).getColName() %></td>
	<%		} 
		}	%>
				</tr>
			  </table>
 <% }
%>			</td>
		  </tr>
		  <tr>
			<td>
<%	if(m_bSelectL3.booleanValue())
	{	%>
			  <table width="100%" border="0" cellspacing="0" cellpadding="1">
			  	<tr>
<%	if(m_selectL3.getAbsRow(0).getTabPrintPnt() > 0) 
		{ %> 
		  <td class="fsiEncL3" width="<%= m_selectL3.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
	<% 	}	
		for(int i = 0; i < m_colL3.getNumRows(); i++)
		{ 
			if(m_colL3.getAbsRow(i).getWillShow()) 
			{ %>
				  <td class="fsiEncL3" width="<%= m_colL3.getAbsRow(i).getAncho() %>%" align="<%= m_colL3.getAbsRow(i).getAlinHor() %>"<%= (( m_colL3.getAbsRow(i).getFGColor() != null ) ? " style=\"color:#" +  m_colL3.getAbsRow(i).getFGColor() + ";\"" : "" ) %>><%= m_colL3.getAbsRow(i).getColName() %></td>
	<%		} 
		}	%>
				</tr>
			  </table>
 <% }
%>			</td>
		  </tr>
		</table>
	</td>
  </tr>
  <tr>
	<td><img src="../../forsetiweb/imgfsi/t_negra.gif" style="height:0.5mm; width:100%;" border="0"></td>
  </tr>  
  <tr>
    <td class="fsiL1" align="center">&nbsp;</td>
  </tr>
  <tr>
    <td>
<%		
	if(m_bSelectL1.booleanValue())
	{
	  for (int RL1 = 0; RL1 < m_setL1.getNumRows(); RL1++)
      {  %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
	<%	if(m_selectL1.getAbsRow(0).getTabPrintPnt() > 0) 
		{ %> 
		  <td class="fsiL1" width="<%= m_selectL1.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
	<% 	}
    	for(int CL1 = 0; CL1 < m_colL1.getNumRows(); CL1++)
        {	
			if(m_colL1.getAbsRow(CL1).getWillShow()) 
			{ 
				String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSTS(m_colL1.getAbsRow(CL1).getColName()), m_colL1.getAbsRow(CL1).getFormat(), m_colL1.getAbsRow(CL1).getBindDataType(), request); %>
		  <td class="fsiL1" width="<%= m_colL1.getAbsRow(CL1).getAncho() %>%" align="<%= m_colL1.getAbsRow(CL1).getAlinHor() %>"<%= (( m_colL1.getAbsRow(CL1).getFGColor() != null ) ? " style=\"color:#" +  m_colL1.getAbsRow(CL1).getFGColor() + ";\"" : "" ) %>><%=cabval%></td>
	<%		} 
		}	%>
		</tr>
	  </table> <%
        // Nivel 2
		if(m_bSelectL2.booleanValue())
		{
	        for (int RL2 = 0; RL2 < m_setL1.getAbsRow(RL1).getSetL2().getNumRows(); RL2++)
    	    { %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
			<%	if(m_selectL2.getAbsRow(0).getTabPrintPnt() > 0) 
				{ %> 
		  <td class="fsiL2" width="<%= m_selectL2.getAbsRow(0).getTabPrintPnt()  %>%">&nbsp;  </td> 
			<% 	}
        	  	for(int CL2 = 0; CL2 < m_colL2.getNumRows(); CL2++)
          		{
					if(m_colL2.getAbsRow(CL2).getWillShow()) 
					{ 
						String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSTS(m_colL2.getAbsRow(CL2).getColName()), m_colL2.getAbsRow(CL2).getFormat(), m_colL2.getAbsRow(CL2).getBindDataType(), request); %>
		  <td class="fsiL2" width="<%= m_colL2.getAbsRow(CL2).getAncho() %>%" align="<%= m_colL2.getAbsRow(CL2).getAlinHor() %>"<%= (( m_colL2.getAbsRow(CL2).getFGColor() != null ) ? " style=\"color:#" +  m_colL2.getAbsRow(CL2).getFGColor() + ";\"" : "" ) %>><%= cabval %></td>
			<%		}    
          		} %>
		</tr>
	  </table> <%				
          		// Nivel 3
				if(m_bSelectL3.booleanValue())
				{
	          		for (int RL3 = 0; RL3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getNumRows(); RL3++)
    	      		{ %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
					<%	if(m_selectL3.getAbsRow(0).getTabPrintPnt() > 0) 
						{ %> 
		  <td class="fsiL3" width="<%= m_selectL3.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
					<% 	}
        	    		for(int CL3 = 0; CL3 < m_colL3.getNumRows(); CL3++)
           			 	{ 
							if(m_colL3.getAbsRow(CL3).getWillShow()) 
							{  
								String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetL3().getAbsRow(RL3).getSTS(m_colL3.getAbsRow(CL3).getColName()), m_colL3.getAbsRow(CL3).getFormat(), m_colL3.getAbsRow(CL3).getBindDataType(), request); %>
		  <td class="fsiL3" width="<%= m_colL3.getAbsRow(CL3).getAncho() %>%" align="<%= m_colL3.getAbsRow(CL3).getAlinHor() %>"<%= (( m_colL3.getAbsRow(CL3).getFGColor() != null ) ? " style=\"color:#" +  m_colL3.getAbsRow(CL3).getFGColor() + ";\"" : "" ) %>><%= cabval %></td>
					<%		}
            			}  %>
		</tr>
	  </table> <%				
	           		}	
		            if(m_bComputeL3.booleanValue())
	  				{
	          			for (int RC3 = 0; RC3 < m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getNumRows(); RC3++)
    	      			{ %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
						<%	if(m_computeL3.getAbsRow(0).getTabPrintPnt() > 0) 
							{ %> 
		  <td class="fsiCL3" width="<%= m_computeL3.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
						<% 	}
        	    			for(int CC3 = 0; CC3 < m_colCL3.getNumRows(); CC3++)
            				{
            					if(m_colCL3.getAbsRow(CC3).getWillShow()) 
								{  
									String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetL2().getAbsRow(RL2).getSetCL3().getAbsRow(RC3).getSTS(m_colCL3.getAbsRow(CC3).getColName()), m_colCL3.getAbsRow(CC3).getFormat(), m_colCL3.getAbsRow(CC3).getBindDataType(), request); %>
		  <td class="fsiCL3" width="<%= m_colCL3.getAbsRow(CC3).getAncho() %>%" align="<%= m_colCL3.getAbsRow(CC3).getAlinHor() %>"<%= (( m_colCL3.getAbsRow(CC3).getFGColor() != null ) ? " style=\"color:#" +  m_colCL3.getAbsRow(CC3).getFGColor() + ";\"" : "" ) %>><%= cabval %></td>
            				<%	}				
							}	%>
		</tr>
	  </table>	
            		<%	}
					} // Fin SI CL3
				} // Fin SI L3
	        }
    	    if(m_bComputeL2.booleanValue())
	  		{
	        	for (int RC2 = 0; RC2 < m_setL1.getAbsRow(RL1).getSetCL2().getNumRows(); RC2++)
    	    	{ %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
				<%	if(m_computeL2.getAbsRow(0).getTabPrintPnt() > 0) 
					{ %> 
		  <td class="fsiCL2" width="<%= m_computeL2.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
				<% 	}
        	  		for(int CC2 = 0; CC2 < m_colCL2.getNumRows(); CC2++)
          			{
            			if(m_colCL2.getAbsRow(CC2).getWillShow()) 
						{  
							String cabval = JUtil.FormatearRep(m_setL1.getAbsRow(RL1).getSetCL2().getAbsRow(RC2).getSTS(m_colCL2.getAbsRow(CC2).getColName()), m_colCL2.getAbsRow(CC2).getFormat(), m_colCL2.getAbsRow(CC2).getBindDataType(), request); %>
		  <td class="fsiCL2" width="<%= m_colCL2.getAbsRow(CC2).getAncho() %>%" align="<%= m_colCL2.getAbsRow(CC2).getAlinHor() %>"<%= (( m_colCL2.getAbsRow(CC2).getFGColor() != null ) ? " style=\"color:#" +  m_colCL2.getAbsRow(CC2).getFGColor() + ";\"" : "" ) %>><%= cabval %></td>
          			<%	}	
		  			} %>
		</tr>
	  </table>	
    		<% 	}
			} // Fin SI CL2
		} // Fin SI L2
      }
	  if(m_bComputeL1.booleanValue())
	  {
		for (int RC1 = 0; RC1 < m_setCL1.getNumRows(); RC1++)
    	{ %>
	  <table width="100%" border="0" cellspacing="0" cellpadding="1">
	  	<tr> 
		<%	if(m_computeL1.getAbsRow(0).getTabPrintPnt() > 0) 
			{ %> 
		  <td class="fsiCL1" width="<%= m_computeL1.getAbsRow(0).getTabPrintPnt() %>%">&nbsp;  </td> 
		<% 	}
        	for(int CC1 = 0; CC1 < m_colCL1.getNumRows(); CC1++)
        	{
				if(m_colCL1.getAbsRow(CC1).getWillShow()) 
				{ 
				String cabval = JUtil.FormatearRep(m_setCL1.getAbsRow(RC1).getSTS(m_colCL1.getAbsRow(CC1).getColName()), m_colCL1.getAbsRow(CC1).getFormat(), m_colCL1.getAbsRow(CC1).getBindDataType(), request); %>
		  <td class="fsiCL1" width="<%= m_colCL1.getAbsRow(CC1).getAncho() %>%" align="<%= m_colCL1.getAbsRow(CC1).getAlinHor() %>"<%= (( m_colCL1.getAbsRow(CC1).getFGColor() != null ) ? " style=\"color:#" +  m_colCL1.getAbsRow(CC1).getFGColor() + ";\"" : "" ) %>><%= cabval %></td>
 		<%		}
        	} %>
		</tr>
	  </table>	
    <%  }
	  } // Fin SI CL1
	} // Fin SI L1
%>
	</td>
  </tr>
</table>
</body>
</html>
