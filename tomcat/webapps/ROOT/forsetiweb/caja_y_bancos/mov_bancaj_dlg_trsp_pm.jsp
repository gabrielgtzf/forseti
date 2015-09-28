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
<%@ page import="forseti.*, forseti.sets.*, forseti.caja_y_bancos.*, java.util.*, java.io.*"%>
<%
	String mov_bancaj_dlg = (String)request.getAttribute("mov_bancaj_dlg");
	if(mov_bancaj_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  ( mov_bancaj_dlg.equals("BANCOS") ? 
						JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").generarTitulo(JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA",request.getParameter("proceso"),3)) :
						JUtil.getSesion(request).getSesion("BANCAJ_CAJAS").generarTitulo(JUtil.Msj("CEF","BANCAJ_CAJAS","VISTA",request.getParameter("proceso"),3)) );

	session = request.getSession(true);
    JMovBancariosSes pol = ( mov_bancaj_dlg.equals("BANCOS") ? 
								(JMovBancariosSes)session.getAttribute("mov_bancarios_dlg") :
								(JMovBancariosSes)session.getAttribute("mov_cajas_dlg") );

	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();
	
	JPublicBancosCuentasSetV2 bc = new JPublicBancosCuentasSetV2(request);
	bc.m_OrderBy = "Clave ASC";
	bc.m_Where = ( mov_bancaj_dlg.equals("BANCOS") ? 
					"Tipo = '0' and Clave <> '" + JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial() + "'" :
					"Tipo = '0'" );
	bc.Open();
	
	JPublicBancosCuentasSetV2 cc = new JPublicBancosCuentasSetV2(request);
	cc.m_OrderBy = "Clave ASC";
	cc.m_Where = ( mov_bancaj_dlg.equals("CAJAS") ? 
					"Tipo = '1' and Clave <> '" + JUtil.getSesion(request).getSesion("BANCAJ_CAJAS").getEspecial() + "'" :
					"Tipo = '1'" );
	cc.Open();

%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.subproceso.value == "ENVIAR")
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
	else
		return true;
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosDlg" : "CEFMovCajaDlg" ) %>" method="post" enctype="application/x-www-form-urlencoded" name="mov_bancaj_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle">
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= ( mov_bancaj_dlg.equals("BANCOS") ? "CEFMovBancariosCtrl" : "CEFMovCajaCtrl" ) %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"></td>
			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF">
        <table width="100%" border="0" cellpadding="5" cellspacing="5">
          <tr> 
            <td width="10%" align="left" valign="top"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/> 
                    <input name="modulo" type="hidden" value="<%= mov_bancaj_dlg %>"/> 
                    <input type="hidden" name="subproceso" value="ENVIAR"> 
					<input type="hidden" name="ID" value="<%= request.getParameter("ID") %>"/> 
                    <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>"/>
                    <%= JUtil.Msj("GLB","GLB","GLB","TIPO") %>
			</td>
            <td>
				  	<% 
				if( mov_bancaj_dlg.equals("BANCOS") ) 
				{ 
					%> 
						<input name="tipomov" type="radio" value="cheque"<% if(pol.getTipoMov().equals("cheque"))  { out.print(" checked"); }%>/><%= JUtil.Msj("GLB","GLB","GLB","CHEQUE") %>&nbsp;<%= pol.getSigCheque() %>&nbsp;
					<% 	
				} 
					%>
					<input name="tipomov" type="radio" value="retiro"<% if(pol.getTipoMov().equals("retiro"))  { out.print(" checked"); }%>/><%= JUtil.Msj("GLB","GLB","GLB","RETIRO") %></td>
		 </tr>
        </table>
		<table width="100%" border="0" cellpadding="5" cellspacing="5">
       	  <tr> 
           <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
           <td width="15%"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO") %>&nbsp;</td>
           <td width="30%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %>&nbsp;</td>
           <td width="25%"><%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>&nbsp;</td>
          </tr>                                               
          <tr>                                            
            <td><table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(pol.getFecha(), "dd/MMM/yyyy") %>"/></td><td width="24"><a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" width="24" height="24" align="absmiddle"></a></td></tr></table></td>
			<td><input name="numero" type="text" id="numero" style="width:100%" maxlength="15" readonly="true" value="<%= pol.getNum() %>"/></td>
            <td><input name="ref" type="text" id="ref" style="width:100%" maxlength="50" value="<%= pol.getRef() %>"/></td>
            <td><input name="cantidad" type="text" id="cantidad" style="width:100%" maxlength="50" value="<%= pol.getCantidad() %>"/></td>
          </tr>
         </table>
         <table width="100%" border="0" cellspacing="5" cellpadding="5">
		   <tr> 
             <td width="30"><%= JUtil.Msj("GLB","GLB","GLB","BENEFICIARIO") %></td>
             <td><input type="checkbox" name="status"<% if(pol.getStatus().equals("T")) { out.print(" checked"); } %>/><%= JUtil.Msj("GLB","GLB","GLB","TRANSITO") %></td>
           </tr>
		   <tr>
		   	 <td colspan="2"><input name="beneficiario" type="text" id="beneficiario" style="width:100%" maxlength="80" value="<%= pol.getBeneficiario() %>"/></td>
		   </tr>
		 </table> 
         <table width="100%" border="0" cellspacing="5" cellpadding="5">
		   <tr> 
              <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
									<select style="width: 100%" name="bancaj" class="cpoColAzc">
                              		    <option value="BANCOS"<% 
									if(pol.getDestino().equals("BANCOS")) { out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","BANCOS") %> ---</option>
                                  	<%
								  	for(int i = 0; i< bc.getNumRows(); i++)
								  	{
		                            	out.print("<option value=\"FSI_BAN_" + bc.getAbsRow(i).getClave() + "\""); 
										if(pol.getDestino().equals("FSI_BAN_" + Byte.toString(bc.getAbsRow(i).getClave()))) 
								  			{ out.print(" selected>" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
										else { out.print(">" + bc.getAbsRow(i).getCuenta() + "</option>"); } 
								  	}
								  	%>
                                  		<option value="CAJAS"<% 
									if(pol.getDestino().equals("CAJAS")) { out.print(" selected"); } %>>--- <%= JUtil.Msj("GLB","GLB","GLB","CAJAS") %> ---</option>
                                 	<%
								  	for(int i = 0; i< cc.getNumRows(); i++)
								  	{
		                            	out.print("<option value=\"FSI_CAJ_" + cc.getAbsRow(i).getClave() + "\""); 
										if(pol.getDestino().equals("FSI_CAJ_" + Byte.toString(cc.getAbsRow(i).getClave()))) 
								  			{ out.print(" selected>" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
										else { out.print(">" + cc.getAbsRow(i).getCuenta() + "</option>"); } 
								  	}
								  %>
                    				</select>
						</td>
						<td width="10%" align="center"><%= JUtil.Msj("GLB","GLB","GLB","TC",2) %></td>
						<td width="30%"><input name="tcGEN" type="text" id="tcGEN" style="width:100%" maxlength="50" value="<%= pol.getTC() %>"/></td>
					</tr>
			  </table>
		 	 </td>
		   </tr>
		   <tr>   
			 <td colspan="2"><textarea name="concepto" style="width:100%" rows="2" id="textarea"><%= pol.getConcepto() %></textarea></td>
		   </tr>
		  </table> 		
      </td>
  </tr>
</table>
</form>
</body>
</html>
