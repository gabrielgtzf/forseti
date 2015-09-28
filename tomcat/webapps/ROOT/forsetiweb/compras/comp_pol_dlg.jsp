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
<%@ page import="forseti.*, forseti.sets.*, forseti.compras.*, java.util.*, java.io.*"%>
<%
	String comp_pol_dlg = (String)request.getAttribute("comp_pol_dlg");
	if(comp_pol_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("COMP_POL").generarTitulo(JUtil.Msj("CEF","COMP_POL","VISTA",request.getParameter("proceso"),3));
	
	session = request.getSession(true);
    JCompPoliticasSes rec = ( JCompPoliticasSes)session.getAttribute("comp_pol_dlg");
	String ent = JUtil.getSesion(request).getSesion("COMP_POL").getEspecial();

	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
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
<%
	if(ent.equals("PRODUCTOS") )
	{
		if(request.getParameter("proceso").equals("PRECIOS_PROD"))
		{
%>
function enviarlo(formAct)
{
		return true;
}	
<%
		}
	}
%>
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFCompPoliticasDlg" method="post" enctype="application/x-www-form-urlencoded" name="comp_pol_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFCompPoliticasCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
<%
	if(ent.equals("PRODUCTOS"))
	{
		if(request.getParameter("proceso").equals("PRECIOS_PROD"))
		{
%>
<tr>
   <td>
	   <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
                    <input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"> 
                     </td>
          </tr>
          <tr>
            <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="15%" align="left" class="titChicoAzc">Clave</td>
                  <td class="titChicoAzc">Descripci&oacute;n</td>
				  <td width="5%" align="center" class="titChicoAzc">Uni</td>
				  <td width="15%" align="right" class="titChicoAzc">Precio</td>
				  <td width="15%" align="center" class="titChicoAzc">Moneda</td>
				</tr>
<%
			for(int i = 0; i < rec.numObjetos(); i++)
			{
%>
                <tr> 
                  <td width="15%" align="left"><%= rec.getObjeto(i).getClave() %></td>
                  <td><%= rec.getObjeto(i).getDescripcion() %></td>
				  <td width="5%" align="center"><%= rec.getObjeto(i).getUnidad() %></td>
                  <td width="15%" align="right">
							<input name="FSI_PCOMP_<%= rec.getObjeto(i).getClave() %>" type="text" value="<% if(request.getParameter("FSI_PCOMP_" + rec.getObjeto(i).getClave()) == null) { out.print(rec.getObjeto(i).getPComp()); } else { out.print(request.getParameter("FSI_PCOMP_" + rec.getObjeto(i).getClave())); } %>" size="7" maxlength="15"></td>
                  <td width="15%" align="center">
					 <select name="FSI_MCOMP_<%= rec.getObjeto(i).getClave() %>">
<%
				for(byte j = 0; j < setMon.getNumRows(); j++)
				{
%>
						<option value="<%= setMon.getAbsRow(j).getClave() %>"<% 
							if(request.getParameter("FSI_MCOMP_" + rec.getObjeto(i).getClave()) != null ) {
								if(request.getParameter("FSI_MCOMP_" + rec.getObjeto(i).getClave()).equals(Integer.toString(setMon.getAbsRow(j).getClave())) ) { 
									out.print(" selected"); 
								}
							} else { 
								if(setMon.getAbsRow(j).getClave()  == rec.getObjeto(i).getID_Moneda() ) {
									out.println(" selected"); 
								} 
							}%>><%= setMon.getAbsRow(j).getMoneda() %>
						</option>
<%
				}
%>
					  </select>
					</td>
			    </tr>
<%
			}
%>                
              </table>
			</td>
          </tr>
           <tr>
            <td>&nbsp;</td>
          </tr>
      </table>
   </td>
</tr>	
<%
		} // FinPrecios
	} // Fin Ent PRODUCTOS
%>
</table>
</form>
</body>
</html>