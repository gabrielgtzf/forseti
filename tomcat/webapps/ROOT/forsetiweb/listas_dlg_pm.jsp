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
	
	int idcatalogo = Integer.parseInt(request.getParameter("idcatalogo"));
	JListasCatalogosSet cats = new  JListasCatalogosSet(request);
	cats.ConCat(true);
	cats.m_Where = "ID_Catalogo = '" + idcatalogo + "'";
	cats.Open();

	String titulo = cats.getAbsRow(0).getNombre();
	
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript">
<!-- 
function llamarCatalogo()
{
	document.listas.descripcion.value = document.listas.cadena.value;
	document.listas.submit();
}
function transferirClave(clave, destino)
{
	window.opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("lista") %>.value = clave;
	window.opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("destino") %>.value = destino;
	window.opener.document.<%= request.getParameter("formul") %>.<%= request.getParameter("lista") %>.focus();
	window.close();
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form name="listas" action="<%= request.getRequestURI() %>" method="get">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titChico" align="center" valign="middle" bgcolor="#0099FF"><%= titulo %></td>
  </tr>
  <tr> 
    <td bgcolor="#CCCCCC">
		<table width="100%" border="0" cellspacing="5" cellpadding="0">
          <tr> 
            <td width="75" class="titChico">
				<input name="formul" type="hidden" value="<%= request.getParameter("formul") %>"/>
				<input name="lista" type="hidden" value="<%= request.getParameter("lista") %>"/>
				<input name="destino" type="hidden" value="<%= request.getParameter("destino") %>"/>
				<input name="nombre" type="hidden" value="<%= request.getParameter("nombre") %>"/>
				<input name="idcatalogo" type="hidden" value="<%= request.getParameter("idcatalogo") %>"/>
				<input name="descripcion" type="hidden" value="<%= (request.getParameter("descripcion") == null) ? "" : request.getParameter("descripcion") %>"/>
				<input name="esp1" type="hidden" value="<%= request.getParameter("esp1") %>"/>
				<%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
            <td width="100"> 
				<input name="cadena" type="text" onBlur="javascript:llamarCatalogo()" style="width:100%" maxlength="255" value="<%= (request.getParameter("descripcion") == null) ? "" : request.getParameter("descripcion") %>"/> 
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
            <td width="30%" class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td class="titChico"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
            <td width="10%" class="titChico">&nbsp;</td>
           </tr>
		  </table>
	</td>
  </tr>
  <tr>
  	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="10">
<%
	if(request.getParameter("descripcion") == null || request.getParameter("descripcion").equals(""))
	{ 
%>
                <tr> 
                  <td class="titChicoAzc"><div align="center"><%= JUtil.Msj("GLB","GLB","DLG","LISTAS") %></div></td>
                </tr>
<%	
	}
	else
	{
		JListasSet set = new JListasSet(request);
		
		String where_alternativo = "";
		
		String select = cats.getAbsRow(0).getSelect_Clause();
		
		// aqui ejecuta el switch para hacer where_alternativo a algunos catalogos
		switch(idcatalogo)
		{ 
			case 12:
				//select = " Clave, Descripcion, Especial FROM view_catalog_provee ";
				where_alternativo = "ID_Entidad = '" + JUtil.p(JUtil.getSesion(request).getSesion(request.getParameter("esp1")).getEspecial()) + "'";
				break;
			case 13:
				//select = "  Clave, Descripcion, Especial FROM view_catalog_prod4 ";
				where_alternativo = "(ID_Bodega = -1 or ID_Bodega = '" + JUtil.p(request.getParameter("esp1")) + "')";
				break;
			case 14:
				//select = " Clave, Descripcion, Especial FROM view_catalog_client ";
				where_alternativo = "ID_Entidad = '" + JUtil.p(JUtil.getSesion(request).getSesion(request.getParameter("esp1")).getEspecial()) + "'";
				break;
			case 28:
				//select = " Clave, Descripcion, Especial FROM view_catalog_empleados ";
				if(!request.getParameter("esp1").equals("_FSI_CAT"))
					where_alternativo = "ID_Sucursal = '" + JUtil.p(JUtil.getSesion(request).getSesion(request.getParameter("esp1")).getEspecial()) + "'";
				else
					where_alternativo = "";
				break;
	 		default:
				//select = " * from view_catalog_usuarios ";
				where_alternativo = "";
				break;
		} 
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
		//out.println(" " +set.m_Where + " " + set.m_OrderBy );
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
		         	<td width="30%" align="right"><a style="color:#0099FF;" href="javascript:transferirClave('<%= set.getAbsRow(i).getClave() %>','<%= set.getAbsRow(i).getDescripcion() %>');"><%= set.getAbsRow(i).getClave() %></a></td>
          			<td align="left"><a style="color:#0099FF;" href="javascript:transferirClave('<%= set.getAbsRow(i).getClave() %>','<%= set.getAbsRow(i).getDescripcion() %>');"><%= set.getAbsRow(i).getDescripcion() %></a></td>
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
</form> 
</body>
</html>
