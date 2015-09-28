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
<%@ page import="forseti.*, java.util.*, java.io.*, forseti.sets.*"%>
<%
	String impresion = (String)request.getAttribute("impresion");
	String tipo_imp = (String)request.getAttribute("tipo_imp");
	String formato_default = (String)request.getAttribute("formato_default");

	// Inicia con registrar el objeto de sesion si no esta registrado
	if(impresion == null) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript1.2" src="../compfsi/comps.js"></script>
<script language="JavaScript1.2">
<!-- 
function ventanaIMP()
{
	parametrs = "toolbar=0,location=0,directories=0,status=1,menubar=1,scrollbars=1,resizable=1,width=0,height=0";
	ventana = window.open('', 'ventImp', parametrs);
	ventana.focus();
}
-->
</script>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>

<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/<%= impresion %>" method="post" enctype="application/x-www-form-urlencoded" name="impresion" target="ventImp">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","FORMATOS-IMP") %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
     <td>
		<input name="subproceso" type="hidden" value="IMPRESION">
<%
    Enumeration nombresParam = request.getParameterNames();
    while(nombresParam.hasMoreElements())
    {
        String nombreParam = (String)nombresParam.nextElement();
        String[] valoresParam = request.getParameterValues(nombreParam);
        if(valoresParam.length == 1)
        {
            String valorParam = valoresParam[0];
            if(valorParam.length() == 0)
              out.print("<input name=\"" + nombreParam + "\" type=\"hidden\" value=\"\">");
            else
              out.print("<input name=\"" + nombreParam + "\" type=\"hidden\" value=\"" + valorParam + "\">");
			  
        }
        else
        {
            for(int i = 0; i < valoresParam.length; i++)
            {
              out.print("<input name=\"" + nombreParam + "\" type=\"hidden\" value=\"" + valoresParam[i] + "\">");
            }
        }
    }
%>	  
	 </td>
  </tr>
  <tr>
    <td>
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	     <td class="titChicoAzc" width="5%" align="center">&nbsp;</td>
	     <td class="titChicoAzc" width="25%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
         <td class="titChicoAzc" width="70%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
	   </tr>		

<%	
	JPublicFormatosSetV2 set = new JPublicFormatosSetV2(request);
	set.m_Where = "Tipo = '" + JUtil.p(tipo_imp) + "'";
    set.Open();
	if(set.getNumRows() < 1)
	{ 
%>
		<tr>
			<td colspan=3 class="titChicoAzc"><div align="center"><%= JUtil.Msj("GLB","GLB","GLB","FORMATOS-IMP",2) %></div>
		</tr>		
<%		
	}		
	for(int i=0; i < set.getNumRows(); i++)
	{
%>
		  <tr>
	        <td width="5%" align="center"><input type="radio" name="idformato" value="<%= set.getAbsRow(i).getID_Formato() %>"<% if( formato_default == null  && i == 0) { out.print(" checked"); } else if( formato_default != null && formato_default.equals(set.getAbsRow(i).getID_Formato()) )  { out.print(" checked"); } %>></td>
			<td width="25%" align="left"><%= set.getAbsRow(i).getID_Formato() %></td>
    		<td width="70%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
		  </tr>		
<%					
   	} // fin bucle for
%>        
		  <tr>
    		<td colspan="3">&nbsp;</td>
  		  </tr>
  		  <tr>
    		<td colspan="3" align="right">
				 	<input type="submit" name="aceptar" onClick="javascript:ventanaIMP();" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"<% if(set.getNumRows() < 1) { out.print(" disabled"); } %>>
    				<input type="button" name="cancelar" onClick="javascript:history.back()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
			</td>
  		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
</html>
