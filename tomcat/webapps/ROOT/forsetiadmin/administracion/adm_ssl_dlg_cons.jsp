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
	String adm_ssl_dlg = (String)request.getAttribute("adm_ssl_dlg");
	if(adm_ssl_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_SSL").generarTitulo(JUtil.Msj("SAF","ADMIN_SSL","VISTA",request.getParameter("proceso"),3));
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.password.value, 3, 30))
		return false;
	else
		return true;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmSSLDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_ssl_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clock"> 
             <input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmSSLCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
    <%	
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
	%>
    <tr> 
      <td> 
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td> 
         		A continuacion se muestra el certificado en este almacen de claves
            </td>
          </tr>
		  <tr> 
            <td align="center"> 
       			<textarea name="certificado" class="cpoCol" style="width:100%;" rows="25" readonly="readonly">
<%
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		
		try 
		{
			//System.out.println("ADQUIRIENDO LA CONEXION DEL USUARIO FORSETI PRICIPAL...");
			String CONTENT = "keytool -list -v -keystore /usr/local/forseti/bin/" + request.getParameter("id") + " -keypass " + request.getParameter("password") + " -storepass " + request.getParameter("password"); 
			sc.setContent(CONTENT);
			System.out.println(CONTENT);
			String RES = sc.executeCommand();
			ERROR += sc.getError();
			out.println(RES + "\n" + ERROR);
      	}
      	catch(Throwable e)
      	{
      		out.println("ERROR Throwable: " + "<br>" + e.getMessage());
      	}
%>				
				</textarea>
	        </td>
          </tr>
        </table>
	  </td>
    </tr>
</table>
</form>
</body>
</html>
