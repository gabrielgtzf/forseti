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
	String adm_cfdi_dlg = (String)request.getAttribute("adm_cfdi_dlg");
	if(adm_cfdi_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("ADM_CFDI").generarTitulo(JUtil.Msj("CEF","ADM_CFDI","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	String ent = JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
	{
		formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd_dlg" target="_self">
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
             	<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmCFDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
	%>
    <tr> 
      <td> 
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td> 
         		A continuacion se muestra el archivo de registro
            </td>
          </tr>
		  <tr> 
            <td align="center"> 
       			<textarea name="archivo" style="width:100%;" rows="25" readonly="readonly">
<%
	int ano = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 1));
    int mes = Integer.parseInt(JUtil.Elm(request.getParameter("id"), 2));
    String nomArchFech;
    if(mes < 10)
    	nomArchFech = "-" + ano + "-0" + mes;
    else
    	nomArchFech = "-" + ano + "-" + mes;
    String nomArch = "/usr/local/forseti/emp/" + JUtil.getSesion(request).getBDCompania() + "/CE/" + ent + nomArchFech + ".log";
      		 		
	File f = new File(nomArch);
	FileReader fr = null;
	BufferedReader br = null;
	try
	{
		fr = new FileReader(f);
		br = new BufferedReader(fr);
		String s;
		while((s = br.readLine())  != null)
		{
			out.println(s);
		}
		br.close();
	}
	catch(IOException e1)
	{
		out.print(e1);
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
