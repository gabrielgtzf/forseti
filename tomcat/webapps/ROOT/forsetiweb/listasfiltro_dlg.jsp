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
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);
	// Inicia con registrar el objeto de sesion si no esta registrado
	if(!registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
	
	String titulo = request.getParameter("titulocat");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript">
<!-- 
function llamarCatalogo()
{
	document.listas.descripcion.value = document.listas.cadena.value;
	document.listas.submit();
}
function transferirClave(clave, destino)
{
	opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("lista") %>.value = clave;
	opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("destino") %>.value = destino;
	opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("lista") %>.focus();
	window.close();
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body background="../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form name="listas" action="<%= request.getRequestURI() %>" method="get" target="_self">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titChico" align="center" valign="middle" bgcolor="#0099FF"><%= titulo %></td>
  </tr>
  <tr> 
    <td bgcolor="#CCCCCC">
		<table width="100%" border="0" cellspacing="5" cellpadding="0">
          <tr> 
            <td width="75" class="titChico">
				<input name="titulocat" type="hidden" value="<%= request.getParameter("titulocat") %>">
				<input name="formul" type="hidden" value="<%= request.getParameter("formul") %>">
				<input name="lista" type="hidden" value="<%= request.getParameter("lista") %>">
				<input name="destino" type="hidden" value="<%= request.getParameter("destino") %>">
				<input name="nombre" type="hidden" value="<%= request.getParameter("nombre") %>">
				<input name="idreporte" type="hidden" value="<%= request.getParameter("idreporte") %>">
				<input name="idcolumna" type="hidden" value="<%= request.getParameter("idcolumna") %>">
				<input name="entidad" type="hidden">
				<input name="descripcion" type="hidden" value="<%= (request.getParameter("descripcion") == null) ? "" : request.getParameter("descripcion") %>">
				<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
            <td width="100"> 
				<input name="cadena" type="text" onBlur="javascript:llamarCatalogo()" size="30" maxlength="255" value="<%= (request.getParameter("descripcion") == null) ? "" : request.getParameter("descripcion") %>"> 
            </td>
            <td>&nbsp;</td>
          </tr>
		 </table>
	</td>
  </tr>
  <tr>
  	<td bgcolor="#0099FF">
		<table width="100%" border="0" cellspacing="0" cellpadding="1">
          <tr> 
            <td width="5%" class="titChico">&nbsp;</td>
            <td width="20%" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td width="65%" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
            <td width="10%" class="titChico">&nbsp;</td>
           </tr>
		  </table>
	</td>
  </tr>
  <tr>
  	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="1">
<%
	if(request.getParameter("descripcion") == null || request.getParameter("descripcion").equals(""))
	{ 
%>
                <tr> 
                  <td class="titChicoAzc"><div align="center"><%= JUtil.Msj("GLB","GLB","DLG","LISTAS") %></div></td>
                </tr>
<%	}
	else
	{
		String select;
		String where_alternativo = "";
		JReportesBindFSet setF = new JReportesBindFSet(request);
    	setF.m_Where = "ID_Report = '" + JUtil.p(request.getParameter("idreporte")) + "' and ID_Column = '" + JUtil.p(request.getParameter("idcolumna")) + "'";
       	setF.Open();
		if(setF.getNumRows() > 0)
		{
			JListasCatalogosSet cats = new  JListasCatalogosSet(request);
			cats.ConCat(true);
					
			int idcatalogo;
			try {
				idcatalogo = Integer.parseInt(setF.getAbsRow(0).getSelect_Clause());
			} catch(NumberFormatException e) {
				idcatalogo = 0;
			}
					
			if(idcatalogo == 0)
				select = setF.getAbsRow(0).getSelect_Clause();
			else
			{
				cats.m_Where = "ID_Catalogo = '" + idcatalogo + "'";
				cats.Open();
				select = cats.getAbsRow(0).getSelect_Clause();
				
				// aqui ejecuta el switch para hacer where_alternativo a algunos catalogos
				switch(idcatalogo)
				{ 
					case 16:
						//select = " Clave, Descripcion, Especial FROM view_catalog_provee_poriden ";
						where_alternativo = "ID_Entidad = '" + JUtil.p(request.getParameter("entidad")) + "'";
						break;
					case 44:
						//select = " Clave, Descripcion, Especial FROM view_catalog_client_poriden ";
						where_alternativo = "ID_Entidad = '" + JUtil.p(request.getParameter("entidad")) + "'";
						break;
					case 48:
						//select = " Clave, Descripcion, Especial FROM view_catalog_client ";
						where_alternativo = "ID_Sucursal = '" + JUtil.p(request.getParameter("entidad")) + "'";
						break;
					default:
						//select = " * from view_catalog_usuarios ";
						where_alternativo = "";
						break;
				} 
			}
		}
		else
			select = " * from view_catalog_usuarios ";
		
		JListasSet set = new JListasSet(request);
		set.setSelect(select);

		if(request.getParameter("descripcion").equals("*"))
		{
			if(!where_alternativo.equals(""))
				set.m_Where = where_alternativo;
			else
				set.m_Where = "";
		}	
		else
		{
			if(!where_alternativo.equals(""))
				set.m_Where = where_alternativo + " and Descripcion ILIKE '%" + JUtil.p(request.getParameter("descripcion")) + "%'";
			else
				set.m_Where = "Descripcion ILIKE '%" + JUtil.p(request.getParameter("descripcion")) + "%'";
		}
		
		set.m_OrderBy = "Descripcion ASC"; 
		set.Open();
		if(set.getNumRows() < 1)
		{ 
%>
                <tr> 
                  <td class="titChicoAzc"><div align="center"> <%= JUtil.Msj("GLB","GLB","DLG","LISTAS",2) + 
				   request.getParameter("descripcion") + JUtil.Msj("GLB","GLB","DLG","LISTAS",3) %></div></td>
                </tr>
<%
		}	
		for(int i=0; i < set.getNumRows(); i++)
		{
%>
<tr>
		        <td width="5%" align="center"> 
                    	<a href="javascript:transferirClave('<%= set.getAbsRow(i).getClave() %>','<%= set.getAbsRow(i).getDescripcion() %>')"><img src="../../imgfsi/lista_ok.gif" border="0"></a></td>
		  			<td width="20%" align="left"><%= set.getAbsRow(i).getClave() %></td>
          			<td width="65%" align="left"><%= set.getAbsRow(i).getDescripcion() %></td>
          			<td width="10%"><%= set.getAbsRow(i).getEspecial() %></td>
				</tr>				
<%					
    	} // fin bucle for
	} // fin if
%>
        </table>
	</td>
  </tr>
</table>
<script language="JavaScript" type="text/javascript">
<!-- 
<% 
	if(request.getParameter("entidad") != null)
	{
%>
//Entidad dif null
try 
{
    document.listas.entidad.value = <%= request.getParameter("entidad") %>;
	//alert("Request Entidad: " + <%= request.getParameter("entidad") %> + ":" + document.listas.entidad.value);
}
catch(err) 
{
    //alert("Request Excepción: " + err.message);
}
<%
	}
	else 
	{
%>
//Entidad es null
try 
{
	document.listas.entidad.value = opener.document.<%= request.getParameter("formul") %>.Entidad.value
	//alert("Opener Entidad: " + opener.document.<%= request.getParameter("formul") %>.Entidad.value + ":" + document.listas.entidad.value);
}
catch(err) 
{
    //alert("Opener Excepción: " + err.message);
}
<%
	}
%>
-->
</script>
</form> 
</body>
</html>