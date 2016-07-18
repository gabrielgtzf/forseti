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
<%@ page import="fsi_admin.admon.JAdmBDSes, forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
		
	String Meses = JUtil.Msj("GLB","GLB","GLB","MESES-ANO",2); 
		
	session = request.getSession(true);
    JAdmBDSes bd = (JAdmBDSes)session.getAttribute("adm_bd_dlg");
              
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
<%
if(bd.getTab().equals("DATOS_GENERALES"))
{
%>
	if(!esCadena("<%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %>", formAct.nombre.value, 3, 20) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %>", formAct.compania.value, 3, 254) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.password.value, 3, 30) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %>", formAct.confpwd.value, 3, 30) )
		return false;
	else
		return true;
<%
}
else if(bd.getTab().equals("RESUMEN"))
{
%>	
	if(formAct.subproceso.value == 'ENVIAR')
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
<%
}
else if(bd.getTab().equals("SELECCION") && bd.getTipoInstalacion().equals("MANUAL") && bd.getEtapa().equals("SEGUNDA"))
{
%>
	if(formAct.subproceso.value == 'ANTERIOR')
	{
		if(confirm("Si regresas a la etapa anterior, los cambios en los nombres de las fichas y las descripciones que has realizado se perderán. ¿Estas seguro que deseas regresar?"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
<%
}
else if(bd.getTab().equals("SELECCION") && bd.getTipoInstalacion().equals("MANUAL") && bd.getEtapa().equals("TERCERA"))
{
%>
	if(formAct.subproceso.value == 'ANTERIOR')
	{
		if(confirm("Si regresas a la etapa anterior, las asociaciones se perderán. ¿Estas seguro que deseas regresar?"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
<%
}
else
{
	out.print("return true;");
}
%>	
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_bd_dlg" target="_self">
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
              <%  if(JUtil.getSesionAdmin(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else if(bd.getTab().equals("RESUMEN")) { %>
					<input type="submit" name="atras" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'ANTERIOR');" value="<%= JUtil.Msj("GLB","GLB","GLB","ANTERIOR") %>">
					<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } else if(bd.getTab().equals("DATOS_GENERALES")) { %>
					<input type="submit" name="adelante" value="<%= JUtil.Msj("GLB","GLB","GLB","SIGUIENTE") %>">
       				<%  } else { %>
					<input type="submit" name="atras" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'ANTERIOR');" value="<%= JUtil.Msj("GLB","GLB","GLB","ANTERIOR") %>">
					<input type="submit" name="adelante" value="<%= JUtil.Msj("GLB","GLB","GLB","SIGUIENTE") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmBDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  	<tr>
 	  <td height="109" bgcolor="#333333">
	  	<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
        <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
        <input name="subproceso" type="hidden" value="ENVIAR">
		<input name="identidad" type="hidden">
	  &nbsp;</td>
	</tr>
    <%	
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
	%>
    <tr> 
      <td> 
<%
if(bd.getTab().equals("DATOS_GENERALES"))
{
%>
	  	<table width="100%" border="0" cellspacing="3" cellpadding="0">
		  <tr>
		  	<td colspan="2" bgcolor="#FF6600" class="titChico" align="center">
				Datos Generales
			</td>
		  </tr>
          <tr> 
            <td width="30%" align="right"> 
                
                <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %></td>
            <td> <input name="nombre" type="text" class="cpoCol" size="15" maxlength="20"<%= (request.getParameter("proceso").equals("CAMBIAR_PROPIEDADES")) ? " readOnly=\"true\"" : "" %>> 
              <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE",2) %></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %></td>
            <td><input name="compania" type="text" class="cpoCol" id="compania" size="80" maxlength="255"></td>
          </tr>
          <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","DIRECCION") %></td>
            <td><input name="direccion" type="text" id="direccion" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","POBLACION") %></td>
            <td><input name="poblacion" type="text" id="poblacion" size="25" maxlength="50"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td><input name="cp" type="text" id="cp" size="7" maxlength="9"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td><input name="mail" type="text" id="mail" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","WEB") %></td>
            <td><input name="web" type="text" id="web" size="80" maxlength="255"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %></td>
            <td><input name="password" type="password" class="cpoCol" id="password" size="15" maxlength="30"></td>
          </tr>
		  <tr> 
            <td align="right"><%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %></td>
            <td><input name="confpwd" type="password" class="cpoCol" id="confpwd" size="15" maxlength="30"></td>
          </tr>
        </table>
<%
}
else if(bd.getTab().equals("TIPO_INSTALACION"))
{
%>
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
		  <tr>
		  	<td colspan="6" bgcolor="#FF6600" class="titChico" align="center">
				Tipo de Instalación
			</td>
		  </tr>
          <tr> 
            <td width="3%" align="center" bgcolor="#666666"><input name="tipo_instalacion" type="radio" value="PREDEFINIDA"<% if(bd.getTipoInstalacion().equals("PREDEFINIDA")) { out.print(" checked"); } %>></td>
			<td width="30%" class="titChico" bgcolor="#666666">Empresa configurada parcialmente</td> 
			<td width="3%" align="center" bgcolor="#666666"><input name="tipo_instalacion" type="radio" value="MANUAL"<% if(bd.getTipoInstalacion().equals("MANUAL")) { out.print(" checked"); } %>></td>
			<td class="titChico" bgcolor="#666666">Selección de módulos</td>               
            <td width="3%" align="center" bgcolor="#666666"><input name="tipo_instalacion" type="radio" value="VIRGEN"<% if(bd.getTipoInstalacion().equals("VIRGEN")) { out.print(" checked"); } %>></td>
			<td width="30%" class="titChico" bgcolor="#666666">Empresa configurable desde cero</td>               
           </tr>
		   <tr>
		   	<td class="titCuerpoNeg" colspan="2" valign="top" bgcolor="">Te permite 
              seleccionar entre algunos tipos de empresas o plantillas que ya 
              están configuradas parcialmente, por ejemplo, instalación exclusiva 
              para facturación electrónica, empresa distribuidora con control 
              estricto, empresa productora general, plantilla con catálogos contable 
              y de gastos preregistrados, etc.</td>
		   	<td class="titCuerpoNeg" colspan="2" valign="top">Si ninguna empresa o plantilla 
              preconfigurada se adapta a tus necesidades de control, podrás seleccionar 
              los módulos que más te convengan, para darle mayor funcionalidad 
              a tu negocio. Por ejemplo, selección del módulo de nóminas, varios 
              puntos de venta (México DF, Monterrey, etc.), plantas de producción, 
              etc. </td>
		   	<td class="titCuerpoNeg" colspan="2" valign="top">Esta selección instala 
              una empresa totalmente configurable desde cero, por ejemplo, al 
              instalar empresas o plantillas preconfiguradas o por selección de 
              módulos, se instala un catálogo de cuentas contables definido por 
              el sistema, y sus asociaciones de igual manera. Esta selección no 
              instala nada de configuraciones de sistema.</td>
		   </tr>
		   <tr>
		  	<td bgcolor="#CCCCCC" colspan="6" class="titCuerpoNeg">
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
						
                  <td colspan="4" class="titCuerpoNeg">Ingresa el mes de inicio 
                    de actividades y la clave y nombre del usuario asociado a 
                    esta configuraci&oacute;n (No aplica para empresa configurable 
                    desde cero):</td> 
              		</tr>
					<tr>
						<td class="titChicoNeg" width="20%">Mes</td>
						<td class="titChicoNeg" width="10%">Año</td>
						
                  <td class="titChicoNeg" width="10%">Usuario</td>
						<td class="titChicoNeg">Nombre</td>
					</tr>
					<tr>
						<td class="titChicoNeg">
							<select name="mes" class="cpoBco">
								<option value="1"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("1")) { out.print(" selected"); } else if(bd.getMes() == 1) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,1) %></option>
								<option value="2"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("2")) { out.print(" selected"); } else if(bd.getMes() == 2) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,2) %></option>
								<option value="3"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("3")) { out.print(" selected"); } else if(bd.getMes() == 3) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,3) %></option>
								<option value="4"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("4")) { out.print(" selected"); } else if(bd.getMes() == 4) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,4) %></option>
								<option value="5"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("5")) { out.print(" selected"); } else if(bd.getMes() == 5) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,5) %></option>
								<option value="6"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("6")) { out.print(" selected"); } else if(bd.getMes() == 6) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,6) %></option>
								<option value="7"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("7")) { out.print(" selected"); } else if(bd.getMes() == 7) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,7) %></option>
								<option value="8"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("8")) { out.print(" selected"); } else if(bd.getMes() == 8) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,8) %></option>
								<option value="9"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("9")) { out.print(" selected"); } else if(bd.getMes() == 9) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,9) %></option>
								<option value="10"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("10")) { out.print(" selected"); } else if(bd.getMes() == 10) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,10) %></option>
								<option value="11"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("11")) { out.print(" selected"); } else if(bd.getMes() == 11) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,11) %></option>
								<option value="12"<% if(request.getParameter("mes") != null && request.getParameter("mes").equals("12")) { out.print(" selected"); } else if(bd.getMes() == 12) { out.print(" selected"); } %>><%= JUtil.Elm(Meses,12) %></option>
							</select>
						</td>
						<td class="titChicoNeg"><input name="ano" type="text" id="ano" size="8" maxlength="4" value="<% if(request.getParameter("ano") != null) { out.print(request.getParameter("ano")); } else { out.print( bd.getAno() ); } %>"></td>
						<td class="titChicoNeg"><input name="usuario" type="text" size="12" maxlength="9" value="<% if(request.getParameter("usuario") != null) { out.print(request.getParameter("usuario")); } else { out.print( bd.getUsuario() ); } %>"></td>
						<td class="titChicoNeg"><input name="usuarionombre" type="text" id="usuarionombre" size="40" maxlength="80" value="<% if(request.getParameter("usuarionombre") != null) { out.print(request.getParameter("usuarionombre")); } else { out.print( bd.getUsuarioNombre() ); } %>"></td>
					</tr>
					<tr>
						
                  <td colspan="2" class="txtChicoRj" valign="top"><strong>NOTA</strong>: 
                    El mes y a&ntilde;o de inicio de actividades es una parte 
                    muy importante. A partir de este se podr&aacute;n ingresar 
                    facturas, compras, etc. y no se puede cambiar, por lo tanto 
                    debes estar seguro de este dato.</td>
                  <td colspan="2" class="txtChicoRj" valign="top"> <strong>NOTA</strong>: 
                    Este usuario se utiliza para que te registres en el CEF y 
                    no usar el usuario administrador. La contraseña inicial será 
                    igual al usuario, mas el n&uacute;mero 1 al final, por lo 
                    tanto, es indispensable cambiarla inmediatamente después de 
                    instalar la empresa. Esto puedes hacerlo ingresando al CEF 
                    y presionando las flechas que se encuentran inmediatamente 
                    después del nombre ubicado en la barra de información inferior.</td>
					</tr>
				</table>
			</td>
		   </tr>
		   <tr>
		  	<td colspan="6" class="titCuerpoNeg" bgcolor="#CCCCCC"> <strong>1)</strong> 
              Para poder sellar facturas, recibos de nómina y cartas porte, será 
              necesario contar con una cuenta forseti en este servidor o en uno 
              externo. Puedes consultar la documentación CFDI y CE en el &aacute;rea 
              del centro de control del CEF cuando hayas concluido con la instalación.</td>
		  </tr>
		  <tr>
		  	<td colspan="6" class="titCuerpoNeg" bgcolor="#CCCCCC"><strong>2)</strong> 
              Cualquiera de los tipos de instalación, podrá ser modificado después, 
              por ejemplo, podrás agregar mas puntos de venta, bodegas, dar de 
              alta módulo de nómina o de producción, etc. Esto lo puedes hacer 
              entrando con el usuario administrador.</td>
		  </tr>               
 	     </table>
<%
}
else if(bd.getTab().equals("SELECCION")) //Vista de SELECCION
{
	if(bd.getTipoInstalacion().equals("PREDEFINIDA"))
	{
%>
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
		  <tr>
		  	<td colspan="6" bgcolor="#FF6600" class="titChico" align="center">
				Selección de empresa o plantilla
			</td>
		  </tr>
          <tr> 
            <td width="3%" align="center" bgcolor="#666666"><input name="predefinida" type="radio" value="FACTURACION"<% if(bd.getPredefinida().equals("FACTURACION")) { out.print(" checked"); } %>></td>
			<td width="30%" class="titChico" bgcolor="#666666">Sistema exclusivo de facturación electrónica</td> 
			<td width="3%" align="center" bgcolor="#666666"><input name="predefinida" type="radio" value="ERP"<% if(bd.getPredefinida().equals("ERP")) { out.print(" checked"); } %>></td>
			<td width="30%" class="titChico" bgcolor="#666666">Empresa con la 
              mayor&iacute;a de m&oacute;dulos</td> 
			<td width="3%" align="center" bgcolor="#666666"><input name="predefinida" type="radio" value="PLANTILLA"<% if(bd.getPredefinida().equals("PLANTILLA")) { out.print(" checked"); } %>></td>
			<td class="titChico" bgcolor="#666666">Plantilla base con catálogo 
              de cuentas contables y catálogo de gastos, predefinidos</td>               
           </tr>
		   <tr> 
            <td colspan="2" valign="top">
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
						<td colspan="3" class="titCuerpoNeg">Para pequeños empresarios 
              que no les interesa el manejo de su contabilidad ni el control de inventarios y/o flujo de efectivo. 
              Este sistema permite agregar y cancelar facturas exclusivamente 
              e incluye una pequeña parte de módulos:</td> 
              		</tr>
					<tr>
						<td width="24"><img src="../imgfsi/invserv_productos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %></td>
						<td width="40"><img src="../imgfsi/rep_catalogos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/invserv_servicios.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %></td>
						<td width="40"><img src="../imgfsi/rep_catalogos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/ven_client.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","CLIENT") %></td>
						<td width="40"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
					</tr>
					<tr>	
						<td width="24"><img src="../imgfsi/ven_fac.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","FAC") %></td>
						<td width="40"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
					</tr>
					<tr>	
						<td width="24"><img src="../imgfsi/adm_cfdi.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","CFDI") %></td>
						<td width="40"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
					</tr>
					<tr>	
						<td width="24"><img src="../imgfsi/adm_periodos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","PERIODOS") %></td>
						<td width="40"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
					</tr>
					<tr>	
						<td width="24"><img src="../imgfsi/adm_formatos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","FORMATOS") %></td>
						<td width="40"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
					</tr>
				</table>
			</td>
		   	<td class="titCuerpoNeg" colspan="2" valign="top"> 
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
						
                  <td colspan="3" class="titCuerpoNeg">ERP para empresas establecidas 
                    en un solo sitio f&iacute;sico, por lo tanto, solo agregar&aacute; 
                    una bodega, un punto de venta, etc. Se incluyen los módulos 
                    adecuados para su control administrativo:</td> 
              		</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_contabilidad.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_catalogos.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_caja y bancos.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_almacen.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","ADM") %></td>
					</tr>
					<tr>
					  <td class="titCuerpoNeg" colspan="3">Debes seleccionar cualquiera de los siguientes tipos de empresa:</td>
					</tr>
					<tr>
						<td width="24"><input name="baseerp" type="radio" value="DISTRIBUIDORA"<% if(bd.getBaseERP().equals("DISTRIBUIDORA")) { out.print(" checked"); } %>></td>
						<td colspan="2" class="titCuerpoNeg"><strong>Empresa distribuidora</strong></td>
					</tr>
					<tr>
						<td width="24"><input name="baseerp" type="radio" value="MANUFACTURERA"<% if(bd.getBaseERP().equals("MANUFACTURERA")) { out.print(" checked"); } %>></td>
						<td colspan="2" class="titCuerpoNeg"><strong>Empresa manufacturera</strong></td>
					</tr>
					<tr>
						<td width="24">&nbsp;</td>
						<td colspan="2" class="titCuerpoNeg">Esta opción incluye:</td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_nomina.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
					</tr>
   				</table>
             </td>
			<td class="titCuerpoNeg" colspan="2" valign="top">
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
						
                  <td colspan="3" class="titCuerpoNeg">Para empresarios que desean 
                    formar su empresa a partir de esta base. Una vez instalada 
                    la plantilla, podrás dar de alta las bodegas, cajas, bancos, 
                    entidades de compra, venta, producción y nómina. Esta plantilla 
                    incluye algunos m&oacute;dulos y cat&aacute;logos:</td> 
              		</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_contabilidad.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td>
					</tr>
					<tr>	
						<td width="24"><img src="../imgfsi/invserv_gastos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %></td>
						<td width="40"><img src="../imgfsi/rep_catalogos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","ADM") %></td>
					</tr>
					
					<tr>
					  <td class="titCuerpoNeg" colspan="3">Debes seleccionar cualquiera de los dos siguientes tipos de base:</td>
					</tr>
					<tr>
						<td width="24"><input name="baseplantilla" type="radio" value="DISTRIBUIDORA"<% if(bd.getBasePlantilla().equals("DISTRIBUIDORA")) { out.print(" checked"); } %>></td>
						<td colspan="2" class="titCuerpoNeg"><strong>Base para empresa distribuidora</strong></td>
					</tr>
					<tr>
						<td width="24"><input name="baseplantilla" type="radio" value="MANUFACTURERA"<% if(bd.getBasePlantilla().equals("MANUFACTURERA")) { out.print(" checked"); } %>></td>
						<td colspan="2" class="titCuerpoNeg"><strong>Base para empresa manufacturera</strong></td>
					</tr>
					<tr>
						<td width="24">&nbsp;</td>
						<td colspan="2" class="titCuerpoNeg">Esta opción incuye cuentas de gastos de fabricaci&oacute;n.</td>
					</tr>
				</table>
			 </td>
		   </tr>
		 </table>
<%
	}
	else if(bd.getTipoInstalacion().equals("MANUAL"))
	{
%>
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
		  <tr>
		  	<td colspan="6" bgcolor="#FF6600" class="titChico" align="center">
				Selección de módulos para tu empresa
			</td>
		  </tr>
          <tr> 
            <td width="3%" align="center" bgcolor="#666666" class="titChico">1</td>
			<td width="30%" class="titChico" bgcolor="#666666">Selecciona los 
              m&oacute;dulos</td> 
			<td width="3%" align="center" bgcolor="#666666" class="titChico">2</td>
			<td width="30%" class="titChico" bgcolor="#666666">Ingresa las entidades de tu empresa</td> 
			<td width="3%" align="center" bgcolor="#666666" class="titChico">3</td>
			<td class="titChico" bgcolor="#666666">Asocia las entidades l&oacute;gicas 
              con las f&iacute;sicas</td>               
           </tr>
		   <tr> 
            <td colspan="2" valign="top">
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
					 <td colspan="3" class="titCuerpoNeg">Primero debes seleccionar 
                    los m&oacute;dulos que deseas integrar a tu instalaci&oacute;n:</td> 
						
              		</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_contabilidad.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_catcuentas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContCatcuentas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %></td>
						<td width="40"><img src="../imgfsi/cont_catcuentas.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_rubros" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContRubros) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","RUBROS") %></td>
						<td width="40"><img src="../imgfsi/cont_rubros.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_tipopoliza" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContTipopoliza) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","TIPOPOLIZA") %></td>
						<td width="40"><img src="../imgfsi/cont_tipopoliza.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_enlaces" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContEnlaces) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","ENLACES") %></td>
						<td width="40"><img src="../imgfsi/cont_enlaces.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_polizas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContPolizas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLIZAS") %></td>
						<td width="40"><img src="../imgfsi/cont_polizas.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="cont_polcierre" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ContPolcierre) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLCIERRE") %></td>
						<td width="40"><img src="../imgfsi/cont_polcierre.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_catalogos.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %></td>
					</tr>
					<tr>
						<td width="24"><input name="invserv_lineas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().InvservLineas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","LINEAS") %></td>
						<td width="40"><img src="../imgfsi/invserv_lineas.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="invserv_productos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().InvservProductos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %></td>
						<td width="40"><img src="../imgfsi/invserv_productos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="invserv_servicios" type="checkbox" value="1"<% if(bd.getSeleccionModulos().InvservServicios) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %></td>
						<td width="40"><img src="../imgfsi/invserv_servicios.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="invserv_gastos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().InvservGastos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %></td>
						<td width="40"><img src="../imgfsi/invserv_gastos.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_caja y bancos.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %></td>
					</tr>
					<tr>
						<td width="24"><input name="bancaj_bancos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().BancajBancos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %></td>
						<td width="40"><img src="../imgfsi/bancaj_bancos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="bancaj_cajas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().BancajCajas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %></td>
						<td width="40"><img src="../imgfsi/bancaj_cajas.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="bancaj_vales" type="checkbox" value="1"<% if(bd.getSeleccionModulos().BancajVales) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","BANCAJ","VALES") %></td>
						<td width="40"><img src="../imgfsi/bancaj_vales.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="bancaj_cierres" type="checkbox" value="1"<% if(bd.getSeleccionModulos().BancajCierres) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","BANCAJ","CIERRES") %></td>
						<td width="40"><img src="../imgfsi/bancaj_cierres.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_almacen.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","ALM") %></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_movim" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmMovim) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","MOVIM") %></td>
						<td width="40"><img src="../imgfsi/alm_movim.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_movplant" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmMovplant) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","MOVPLANT") %></td>
						<td width="40"><img src="../imgfsi/alm_movplant.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_traspasos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmTraspasos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","TRASPASOS") %></td>
						<td width="40"><img src="../imgfsi/alm_traspasos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_requerimientos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmRequerimientos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","REQUERIMIENTOS") %></td>
						<td width="40"><img src="../imgfsi/alm_requerimientos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_chfis" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmChfis) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","CHFIS") %></td>
						<td width="40"><img src="../imgfsi/alm_chfis.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="alm_utensilios" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AlmUtensilios) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ALM","UTENSILIOS") %></td>
						<td width="40"><img src="../imgfsi/alm_utensilios.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","COMP") %></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_provee" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompProvee) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","PROVEE") %></td>
						<td width="40"><img src="../imgfsi/comp_provee.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_cxp" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompCxp) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","CXP") %></td>
						<td width="40"><img src="../imgfsi/comp_cxp.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_ord" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompOrd) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","ORD") %></td>
						<td width="40"><img src="../imgfsi/comp_ord.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_rec" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompRec) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","REC") %></td>
						<td width="40"><img src="../imgfsi/comp_rec.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_fac" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompFac) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","FAC") %></td>
						<td width="40"><img src="../imgfsi/comp_fac.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_dev" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompDev) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","DEV") %></td>
						<td width="40"><img src="../imgfsi/comp_dev.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_pol" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompPol) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","POL") %></td>
						<td width="40"><img src="../imgfsi/comp_pol.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="comp_gas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().CompGas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","COMP","GAS") %></td>
						<td width="40"><img src="../imgfsi/comp_gas.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","VEN") %></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_client" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenClient) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","CLIENT") %></td>
						<td width="40"><img src="../imgfsi/ven_client.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_cxc" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenCxc) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","CXC") %></td>
						<td width="40"><img src="../imgfsi/ven_cxc.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_cot" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenCot) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","COT") %></td>
						<td width="40"><img src="../imgfsi/ven_cot.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_ped" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenPed) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","PED") %></td>
						<td width="40"><img src="../imgfsi/ven_ped.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_rem" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenRem) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","REM") %></td>
						<td width="40"><img src="../imgfsi/ven_rem.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_fac" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenFac) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","FAC") %></td>
						<td width="40"><img src="../imgfsi/ven_fac.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_dev" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenDev) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","DEV") %></td>
						<td width="40"><img src="../imgfsi/ven_dev.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="ven_pol" type="checkbox" value="1"<% if(bd.getSeleccionModulos().VenPol) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","VEN","POL") %></td>
						<td width="40"><img src="../imgfsi/ven_pol.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
					</tr>
					<tr>
						<td width="24"><input name="mod_produccion" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ModProduccion) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","PROD") %></td>
						<td width="40"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_nomina.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
					</tr>
					<tr>
						<td width="24"><input name="mod_nomina" type="checkbox" value="1"<% if(bd.getSeleccionModulos().ModNomina) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","NOM") %></td>
						<td width="40"><img src="../imgfsi/rep_nomina.png" width="24" height="24"/></td>
					</tr>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_centro_de_control.png" width="24" height="24"/></td>
						<td colspan="2" class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","GLB","ADM") %></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_saldos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmSaldos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","SALDOS") %></td>
						<td width="40"><img src="../imgfsi/adm_saldos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_usuarios" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmUsuarios) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","USUARIOS") %></td>
						<td width="40"><img src="../imgfsi/adm_usuarios.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_entidades" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmEntidades) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","ENTIDADES") %></td>
						<td width="40"><img src="../imgfsi/adm_entidades.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_vendedores" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmVendedores) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","VENDEDORES") %></td>
						<td width="40"><img src="../imgfsi/adm_vendedores.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_cfdi" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmCfdi) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","CFDI") %></td>
						<td width="40"><img src="../imgfsi/adm_cfdi.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_periodos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmPeriodos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","PERIODOS") %></td>
						<td width="40"><img src="../imgfsi/adm_periodos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_monedas" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmMonedas) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","MONEDAS") %></td>
						<td width="40"><img src="../imgfsi/adm_monedas.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_variables" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmVariables) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","VARIABLES") %></td>
						<td width="40"><img src="../imgfsi/adm_variables.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td width="24"><input name="adm_formatos" type="checkbox" value="1"<% if(bd.getSeleccionModulos().AdmFormatos) { out.print(" checked"); } %>></td>
						<td class="titCuerpoNeg"><%= JUtil.Msj("CEF","MENU","ADM","FORMATOS") %></td>
						<td width="40"><img src="../imgfsi/adm_formatos.png" width="24" height="24"/></td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
						<td align="right"><input name="segundo" type="submit" id="segundo" value="&gt;"></td>
					</tr>

				</table>
			</td>
		   	<td class="titCuerpoNeg" colspan="2" valign="top"> 
<%
			if(bd.getEtapa().equals("SEGUNDA") || bd.getEtapa().equals("TERCERA"))
			{
%>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
					   <td colspan="3" class="titCuerpoNeg">Ahora es necesario agregar 
                    las entidades f&iacute;sicas y l&oacute;gicas como cajas, 
                    cuentas bancarias, bodegas, &aacute;reas de compra, puntos 
                    de venta y producci&oacute;n y &aacute;reas de n&oacute;mina 
                    segun se hayan seleccionado los m&oacute;dulos:</td> 
              		</tr>
<%
				if(bd.getSeleccionModulos().BancajCajas)
				{
%>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_caja y bancos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Cajas</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_CAJA');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntCajas().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Descripción</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_CAJA', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="caj_ficha_<%= i %>" type="text" id="caj_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntCajas(i).Ficha %>"> </td>
									<td colspan="2"><input name="caj_descripcion_<%= i %>" id="caj_descripcion_<%= i %>" type="text" size="40" maxlength="20" value="<%= bd.getEntCajas(i).Descripcion %>"></td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().BancajBancos)
				{
%>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_caja y bancos.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Cuentas bancarias</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_BANCO');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntBancos().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">No. de cuenta o tarjeta</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_BANCO', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="ban_ficha_<%= i %>" type="text" id="ban_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntBancos(i).Ficha %>"> </td>
									<td colspan="2"><input name="ban_cuenta_<%= i %>" id="ban_cuenta_<%= i %>" type="text" size="40" maxlength="20" value="<%= bd.getEntBancos(i).Cuenta %>"></td>
								</tr>
								<tr>
									<td class="txtChicoNar">No. de cheque</td>
									<td colspan="2" class="txtChicoNar">Banco</td>
								</tr>
								<tr>
									<td><input name="ban_cheque_<%= i %>" type="text" id="ban_cheque_<%= i %>" size="15" maxlength="12" value="<%= bd.getEntBancos(i).Cheque %>"> </td>
									<td colspan="2">
										<select style="width: 90%;" name="ban_banco_<%= i %>" class="cpoCol">
<%
										JSatBancosSet setBan = new JSatBancosSet(null);
										setBan.ConCat(true);
										setBan.m_OrderBy = "Clave ASC";
    									setBan.Open();
										for(int b = 0; b < setBan.getNumRows(); b++)
										{
%>
											<option value="<%=setBan.getAbsRow(b).getClave()%>"<% if(bd.getEntBancos(i).Banco.equals(setBan.getAbsRow(b).getClave())) { out.print(" selected"); } %>><%= setBan.getAbsRow(b).getNombre() %></option>
<%
										}
%>
										</select>	
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().AlmMovim)
				{
%>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_almacen.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Bodegas de Materiales</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_BODEGAMP');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntBodegasMP().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Descripción</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_BODEGAMP', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="bod_ficha_<%= i %>" type="text" id="bod_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntBodegasMP(i).Ficha %>"> </td>
									<td colspan="2"><input name="bod_descripcion_<%= i %>" id="bod_descripcion_<%= i %>" type="text" size="40" maxlength="200" value="<%= bd.getEntBodegasMP(i).Descripcion %>"></td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().AlmUtensilios)
				{
%>
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_almacen.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Almacén de utensilios</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_ALMACENUTEN');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntAlmacenesUten().size(); i++)
              		{
%>					
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Descripción</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_ALMACENUTEN', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="alm_ficha_<%= i %>" type="text" id="alm_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntAlmacenesUten(i).Ficha %>"> </td>
									<td colspan="2"><input name="alm_descripcion_<%= i %>" id="alm_descripcion_<%= i %>" type="text" size="40" maxlength="200" value="<%= bd.getEntAlmacenesUten(i).Descripcion %>"></td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterComp && (bd.getSeleccionModulos().CompFac || bd.getSeleccionModulos().CompProvee))
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Areas de compra</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_COMPRA');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntCompras().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Tipo</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_COMPRA', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="comp_ficha_<%= i %>" type="text" id="comp_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntCompras(i).Ficha %>"> </td>
									<td colspan="2">
									  <select name="comp_tipo_<%= i %>" class="cpoCol">
										<option value="0"<% if(bd.getEntCompras(i).Tipo.equals("0")) { out.print(" selected"); } %>>Solo pagos de contado (efectivo, tc, cheque)</option>
										<option value="1"<% if(bd.getEntCompras(i).Tipo.equals("1")) { out.print(" selected"); } %>>Compra de crédito (se registrará la cuenta por pagar)</option>
										<option value="2"<% if(bd.getEntCompras(i).Tipo.equals("2")) { out.print(" selected"); } %>>Mixta, se podrá comprar de contado o a crédito</option>
									 	<option value="3"<% if(bd.getEntCompras(i).Tipo.equals("3")) { out.print(" selected"); } %>>Ninguno, sin seguimiento del pago</option>
									  </select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().CompGas)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Areas de gastos</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_GASTO');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntGastos().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Tipo</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_GASTO', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="gas_ficha_<%= i %>" type="text" id="gas_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntGastos(i).Ficha %>"> </td>
									<td colspan="2">
									  <select name="gas_tipo_<%= i %>" class="cpoCol">
										<option value="0"<% if(bd.getEntGastos(i).Tipo.equals("0")) { out.print(" selected"); } %>>Solo pagos de contado (efectivo, tc, cheque)</option>
										<option value="1"<% if(bd.getEntGastos(i).Tipo.equals("1")) { out.print(" selected"); } %>>Gasto de crédito (se registrará la cuenta por pagar)</option>
										<option value="2"<% if(bd.getEntGastos(i).Tipo.equals("2")) { out.print(" selected"); } %>>Mixta, se podrá comprar de contado o a crédito</option>
									 	<option value="3"<% if(bd.getEntGastos(i).Tipo.equals("3")) { out.print(" selected"); } %>>Ninguno, sin seguimiento del pago</option>
										</select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterVen)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Puntos de venta</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_VENTA');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntVentas().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Tipo</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_VENTA', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="ven_ficha_<%= i %>" type="text" id="ven_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntVentas(i).Ficha %>"> </td>
									<td colspan="2">
									  <select name="ven_tipo_<%= i %>" class="cpoCol">
										<option value="0"<% if(bd.getEntVentas(i).Tipo.equals("0")) { out.print(" selected"); } %>>Solo ventas de contado (efectivo, tc, cheque)</option>
										<option value="1"<% if(bd.getEntVentas(i).Tipo.equals("1")) { out.print(" selected"); } %>>Venta de crédito (se registrará la cuenta por cobrar)</option>
										<option value="2"<% if(bd.getEntVentas(i).Tipo.equals("2")) { out.print(" selected"); } %>>Mixta, se podrá vender de contado o a crédito</option>
									 	<option value="3"<% if(bd.getEntVentas(i).Tipo.equals("3")) { out.print(" selected"); } %>>Ninguno, sin seguimiento del cobro</option>
									  </select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterProd)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Puntos de producci&oacute;n</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_PRODUCCION');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntProduccion().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Descripción</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_PRODUCCION', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="prod_ficha_<%= i %>" type="text" id="prod_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntProduccion(i).Ficha %>"> </td>
									<td colspan="2"><input name="prod_descripcion_<%= i %>" id="prod_descripcion_<%= i %>" type="text" size="40" maxlength="80" value="<%= bd.getEntProduccion(i).Descripcion %>"></td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterNom)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_nomina.png" width="24" height="24"/></td>
						<td class="titCuerpoNeg">Areas de n&oacute;mina</td>
						<td width="40" align="center" valign="middle"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="agregarent" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'AGREGAR_NOMINA');" value="+"><% } %></td>
					</tr>
<%
					for(int i = 0; i < bd.getEntNomina().size(); i++)
              		{
%>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="txtChicoNar">Ficha</td>
									<td class="txtChicoNar">Descripción</td>
									<td align="right"><% if(bd.getEtapa().equals("SEGUNDA")) { %><input type="submit" name="eliminarent" onClick="javascript:establecerDobleProcesoSVE(this.form.subproceso, 'ELIMINAR_NOMINA', this.form.identidad, '<%= i %>');" value="x"><% } %></td>
								</tr>
								<tr>
									<td><input name="nom_ficha_<%= i %>" type="text" id="nom_ficha_<%= i %>" size="15" maxlength="10" value="<%= bd.getEntNomina(i).Ficha %>"> </td>
									<td colspan="2"><input name="nom_descripcion_<%= i %>" id="nom_descripcion_<%= i %>" type="text" size="40" maxlength="200" value="<%= bd.getEntNomina(i).Descripcion %>"></td>
								</tr>
								<tr>
									<td class="txtChicoNar">Periodo</td>
									<td colspan="2" class="txtChicoNar">Tipo</td>
								</tr>
								<tr>
									<td>
										<select class="cpoCol" name="nom_periodo_<%= i %>">
                							<option value="sem"<% if(bd.getEntNomina(i).Periodo.equals("sem")) { out.print(" selected"); } %>>Pago semanal</option>
                							<option value="qui"<% if(bd.getEntNomina(i).Periodo.equals("qui")) { out.print(" selected"); } %>>Pago quincenal</option>
											<option value="men"<% if(bd.getEntNomina(i).Periodo.equals("men")) { out.print(" selected"); } %>>Pago mensual</option>
										</select>
									</td>
									<td colspan="2">
										<select class="cpoCol" name="nom_tipo_<%= i %>">
                							<option value="1"<% if(bd.getEntNomina(i).Tipo.equals("1")) { out.print(" selected"); } %>>Estricta, registro de asistencias obligado</option>
                							<option value="2"<% if(bd.getEntNomina(i).Tipo.equals("2")) { out.print(" selected"); } %>>Empleados de confianza</option>
              							</select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
%>							
   				</table>
<%
			} // FIN ETAPA SEGUNDA o TERCERA
%>
            </td>
			<td class="titCuerpoNeg" colspan="2" valign="top">
<%
			if(bd.getEtapa().equals("TERCERA"))
			{
%>
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
					<tr>
						<td colspan="2" class="titCuerpoNeg">Finalmente tenemos que 
                    asociar las entidades l&oacute;gicas de compras, ventas, producci&oacute;n 
                    y n&oacute;mina con sus cajas, cuentas bancarias y bodegas:</td> 
              		</tr>
<%					
				if(bd.getSeleccionModulos().MasterComp && (bd.getSeleccionModulos().CompFac || bd.getSeleccionModulos().CompProvee ))
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Areas de compra</td>
					</tr>
<%
					for(int i = 0; i < bd.getEntCompras().size(); i++)
              		{
%>
					<tr>
						<td colspan="2">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="titChicoNar"><%= bd.getEntCompras(i).Ficha %></td>
									<td class="titChicoNar"><% if(bd.getEntCompras(i).Tipo.equals("0")) { out.print("CONTADO"); } else if(bd.getEntCompras(i).Tipo.equals("1")) { out.print("CREDITO"); } else if(bd.getEntCompras(i).Tipo.equals("2")) { out.print("MIXTA"); } else { out.print("DIRECTA"); } %></td>
								</tr>
								<tr>
									<td class="txtChicoAz">Bodega</td>
									<td>
									  <select name="comp_bod_<%= i %>" class="cpoCol">
									  	<option value="-1"<% if(bd.getEntCompras(i).Bodega == -1) { out.print(" selected"); } %>>--- NINGUNA ---</option>
<%
						for(int b = 0; b < bd.getEntBodegasMP().size(); b++)
						{
%>
										<option value="<%= b %>"<% if(bd.getEntCompras(i).Bodega == b) { out.print(" selected"); } %>><%= bd.getEntBodegasMP(b).Ficha %></option>
<%
						}
%>									  
									  </select>
									</td>
								</tr>
								<tr>
								  <td class="txtChicoAz">Cajas</td>
								  <td class="txtChicoAz">Bancos</td>
								</tr>
								<tr>
								  <td valign="top">
								  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntCajas().size(); c++)
						{
%>
										<tr><td><input name="comp_caj_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntCompras(i).Cajas.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntCajas(c).Ficha %></td></tr>
<%
						}
%>										
								  	</table>	
								  </td>
								  <td valign="top">
								   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntBancos().size(); c++)
						{
%>
										<tr><td><input name="comp_ban_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntCompras(i).Bancos.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntBancos(c).Ficha %></td></tr>
<%
						}
%>										
									</table>	
								  </td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().CompGas)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_compras.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Areas de gastos</td>
					</tr>
<%
					for(int i = 0; i < bd.getEntGastos().size(); i++)
              		{
%>
					<tr>
						<td colspan="2">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="titChicoNar"><%= bd.getEntGastos(i).Ficha %></td>
									<td class="titChicoNar"><% if(bd.getEntGastos(i).Tipo.equals("0")) { out.print("CONTADO"); } else if(bd.getEntGastos(i).Tipo.equals("1")) { out.print("CREDITO"); } else if(bd.getEntGastos(i).Tipo.equals("2")) { out.print("MIXTA"); } else { out.print("DIRECTA"); } %></td>
								</tr>
								<tr>
									<td class="txtChicoAz">Almacen</td>
									<td>
									  <select name="gas_alm_<%= i %>" class="cpoCol">
									  	<option value="-1">--- NINGUNO ---</option>
<%
						for(int b = 0; b < bd.getEntAlmacenesUten().size(); b++)
						{
%>
										<option value="<%= b %>"<% if(bd.getEntGastos(i).Almacen == b) { out.print(" selected"); } %>><%= bd.getEntAlmacenesUten(b).Ficha %></option>
<%
						}
%>									  
									  </select>
									</td>
								</tr>
								<tr>
								  <td class="txtChicoAz">Cajas</td>
								  <td class="txtChicoAz">Bancos</td>
								</tr>
								<tr>
								  <td valign="top">
								  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntCajas().size(); c++)
						{
%>
										<tr><td><input name="gas_caj_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntGastos(i).Cajas.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntCajas(c).Ficha %></td></tr>
<%
						}
%>										
								  	</table>	
								  </td>
								  <td valign="top">
								   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntBancos().size(); c++)
						{
%>
										<tr><td><input name="gas_ban_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntGastos(i).Bancos.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntBancos(c).Ficha %></td></tr>
<%
						}
%>										
									</table>	
								  </td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterVen)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_ventas.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Puntos de venta</td>
					</tr>
<%
					for(int i = 0; i < bd.getEntVentas().size(); i++)
              		{
%>
					<tr>
						<td colspan="2">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="titChicoNar"><%= bd.getEntVentas(i).Ficha %></td>
									<td class="titChicoNar"><% if(bd.getEntVentas(i).Tipo.equals("0")) { out.print("CONTADO"); } else if(bd.getEntVentas(i).Tipo.equals("1")) { out.print("CREDITO"); } else if(bd.getEntVentas(i).Tipo.equals("2")) { out.print("MIXTA"); } else { out.print("DIRECTA"); } %></td>
								</tr>
								<tr>
									<td class="txtChicoAz">Bodega</td>
									<td>
									  <select name="ven_bod_<%= i %>" class="cpoCol">
									  	<option value="-1">--- NINGUNO ---</option>
<%
						for(int b = 0; b < bd.getEntBodegasMP().size(); b++)
						{
%>
										<option value="<%= b %>"<% if(bd.getEntVentas(i).Bodega == b) { out.print(" selected"); } %>><%= bd.getEntBodegasMP(b).Ficha %></option>
<%
						}
%>									  
									  </select>
									</td>
								</tr>
								<tr>
								  <td class="txtChicoAz">Cajas</td>
								  <td class="txtChicoAz">Bancos</td>
								</tr>
								<tr>
								  <td valign="top">
								  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntCajas().size(); c++)
						{
%>
										<tr><td><input name="ven_caj_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntVentas(i).Cajas.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntCajas(c).Ficha %></td></tr>
<%
						}
%>										
								  	</table>	
								  </td>
								  <td valign="top">
								   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntBancos().size(); c++)
						{
%>
										<tr><td><input name="ven_ban_<%= i %>_<%= c %>" type="checkbox" value="1"<% 
											if(bd.getEntVentas(i).Bancos.elementAt(c).booleanValue() == true)
													out.print(" checked"); 
											%>>&nbsp;<%= bd.getEntBancos(c).Ficha %></td></tr>
<%
						}
%>										
									</table>	
								  </td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterProd)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Puntos de produccion</td>
					</tr>
<%
					for(int i = 0; i < bd.getEntProduccion().size(); i++)
              		{
%>
					<tr>
						<td colspan="2">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="titChicoNar"><%= bd.getEntProduccion(i).Ficha %></td>
									<td class="titChicoNar"><%= bd.getEntProduccion(i).Descripcion %></td>
								</tr>
								<tr>
									<td class="txtChicoAz">Bodega</td>
									<td>
									  <select name="prod_bod_<%= i %>" class="cpoCol">
									  	<option value="-1">--- NINGUNO ---</option>
<%
						for(int b = 0; b < bd.getEntBodegasMP().size(); b++)
						{
%>
										<option value="<%= b %>"<% if(bd.getEntProduccion(i).Bodega == b) { out.print(" selected"); } %>><%= bd.getEntBodegasMP(b).Ficha %></option>
<%
						}
%>									  
									  </select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
				if(bd.getSeleccionModulos().MasterNom)
				{
%>							
					<tr bgcolor="#CCCCCC">
						<td width="24"><img src="../imgfsi/rep_produccion.png" width="24" height="24"/></td>
					    <td class="titCuerpoNeg">Areas de nómina</td>
					</tr>
<%
					for(int i = 0; i < bd.getEntNomina().size(); i++)
              		{
%>
					<tr>
						<td colspan="2">
							<table width="100%" border="0" cellspacing="3" cellpadding="0">
								<tr>
									<td class="titChicoNar"><%= bd.getEntNomina(i).Ficha %></td>
									<td class="titChicoNar" colspan="2"><%= bd.getEntNomina(i).Descripcion %></td>
								</tr>
								<tr>
									<td class="txtChicoAz" valign="top">Pago</td>
									<td valign="top">
								  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntCajas().size(); c++)
						{
%>
											<tr><td><input name="nom_pago_<%= i %>" type="radio" value="caj_<%= c %>"<% if(bd.getEntNomina(i).Caja == c) { out.print(" checked"); } %>>&nbsp;<%= bd.getEntCajas(c).Ficha %></td></tr>
<%
						}
%>										
								  		</table>	
								  	</td>
								  	<td valign="top">
								   		<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
						for(int c = 0; c < bd.getEntBancos().size(); c++)
						{
%>
											<tr><td><input name="nom_pago_<%= i %>" type="radio" value="ban_<%= c %>"<% if(bd.getEntNomina(i).Banco == c) { out.print(" checked"); } %>>&nbsp;<%= bd.getEntBancos(c).Ficha %></td></tr>
<%
						}
%>										
										</table>	
								  	</td>




								</tr>
							</table>
						</td>
					</tr>
<%
					}
				}
%>
				</table>
<%
			} //FIN Etapa TERCERA
%>
			 </td>
		   </tr>
	     </table>
<%		
	}
}
else // if(bd.getTab().equals("RESUMEN"))
{
%>
		<table width="100%" border="0" cellspacing="3" cellpadding="0">
		  <tr>
		  	<td colspan="2" bgcolor="#FF6600" class="titChico" align="center">
				Resumen de la instalación
			</td>
		  </tr>
		  <tr>
		  	<td colspan="2" bgcolor="#666666" class="titChico" align="center">
				Datos Generales
			</td>
		  </tr>
          <tr> 
            <td width="30%" align="right" class="titChicoNeg"> 
                <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %></td>
            <td class="titCuerpoNeg"> <%= bd.getNombre() %></td>
          </tr>
          <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","COMPANIA") %></td>
            <td class="titCuerpoNeg"><%= bd.getCompania() %></td>
          </tr>
          <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","DIRECCION") %></td>
            <td class="titCuerpoNeg"><%= bd.getDireccion() %></td>
          </tr>
		  <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","POBLACION") %></td>
            <td class="titCuerpoNeg"><%= bd.getPoblacion() %></td>
          </tr>
		  <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
            <td class="titCuerpoNeg"><%= bd.getCP() %></td>
          </tr>
		  <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","E-MAIL") %></td>
            <td class="titCuerpoNeg"><%= bd.getMail() %></td>
          </tr>
		  <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","WEB") %></td>
            <td class="titCuerpoNeg"><%= bd.getWeb() %></td>
          </tr>
		  <tr> 
            <td align="right" class="titChicoNeg"><%= JUtil.Msj("GLB","GLB","GLB","USUARIO") %></td>
            <td class="titCuerpoNeg"><%= bd.getNombre().toLowerCase() %></td>
		   </tr>
		   <tr>
		  	<td colspan="2" class="txtChicoRj"> <strong>NOTA</strong>: El usuario 
              administrador <strong><%= bd.getNombre().toLowerCase() %></strong> puede hacer cualquier cosa sobre la empresa 
              instalada, como agregar nuevos usuarios, cambiar permisos, agregar 
              entidades físicas y lógicas, modificar los registros y asociaciones, 
              etc. Es recomendable utilizarlo con mucha precaución. Será necesario 
              recordar la contraseña introducida en esta secci&oacute;n para poder 
              registrarte con este usuario. </td>
		  </tr>
		   <tr>
		  	<td colspan="2" bgcolor="#666666" class="titChico" align="center"> 
              Detalles de la Instalaci&oacute;n </td>
		  </tr>
          <tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Tipo de instalación</td>
            <td class="titCuerpoNeg"> 
                <% if(bd.getTipoInstalacion().equals("PREDEFINIDA")) { 
						out.print("Empresa configurada parcialmente");
					}
					else if(bd.getTipoInstalacion().equals("MANUAL")) { 
						out.print("Selección de módulos");
					}
					else { 
						out.print("Empresa configurable desde cero");
					} %></td>
          </tr>
<% 
	if(bd.getTipoInstalacion().equals("PREDEFINIDA") || bd.getTipoInstalacion().equals("MANUAL")) 
	{ 
%>
		  <tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Usuario asociado a esta configuración</td>
            <td class="titCuerpoNeg"><%= bd.getUsuario() %>&nbsp;&nbsp;&nbsp;<%= bd.getUsuarioNombre() %></td>
          </tr>
		  <tr>
		  	<td colspan="2" class="txtChicoRj"> <strong>NOTA</strong>: Este usuario 
              se utiliza para que te registres en el CEF y no usar el usuario 
              administrador. La contraseña inicial será igual a la clave, por 
              lo tanto, es indispensable cambiarla inmediatamente después de instalar 
              la empresa. Esto puedes hacerlo ingresando al CEF y presionando 
              las flechas que se encuentran inmediatamente después de tu nombre 
              ubicado en la barra de información inferior.</td>
		  </tr>
		  <tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Mes de inicio de 
              actividades</td>
            <td class="titCuerpoNeg"><%= JUtil.Elm(Meses,bd.getMes()) %>&nbsp;<%= bd.getAno() %></td>
          </tr>
<%
	}
	
	if(bd.getTipoInstalacion().equals("PREDEFINIDA"))
	{
%>
		<tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Empresa o plantilla</td>
            <td class="titCuerpoNeg"><% if(bd.getPredefinida().equals("FACTURACION")) { 
						out.print("Sistema exclusivo de facturación electrónica");
					}
					else if(bd.getPredefinida().equals("ERP")) { 
						out.print("Empresa con la mayoría de módulos");
					}
					else { 
						out.print("Plantilla base con catálogo de cuentas contables y catálogo de gastos, predefinidos");
					} %></td>
          </tr>		
<%
		if(bd.getPredefinida().equals("ERP")) 
		{
%>
		<tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Tipo de empresa</td>
            <td class="titCuerpoNeg"><% if(bd.getBaseERP().equals("DISTRIBUIDORA")) { 
						out.print("Empresa distribuidora");
					}
					else { 
						out.print("Empresa manufacturera");
					} %></td>
          </tr>		
<%
		}
		if(bd.getPredefinida().equals("PLANTILLA")) 
		{
%>
		<tr> 
		  	<td width="30%" align="right" class="titChicoNeg">Tipo de base</td>
            <td class="titCuerpoNeg"><% if(bd.getBasePlantilla().equals("DISTRIBUIDORA")) { 
						out.print("Base para empresa distribuidora");
					}
					else { 
						out.print("Base para empresa manufacturera");
					} %></td>
          </tr>		
<%
		}
	}
	else if(bd.getTipoInstalacion().equals("MANUAL")) 
	{ 
	}
%>
        </table>
<%
}
%>
	  </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
<%
if(bd.getTab().equals("DATOS_GENERALES"))
{
%>
document.adm_bd_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print(request.getParameter("nombre")); } else { out.print( bd.getNombre() ); } %>'
document.adm_bd_dlg.compania.value = '<% if(request.getParameter("compania") != null) { out.print(request.getParameter("compania")); } else { out.print( bd.getCompania() ); } %>'
document.adm_bd_dlg.direccion.value = '<% if(request.getParameter("direccion") != null) { out.print(request.getParameter("direccion")); } else { out.print( bd.getDireccion() ); } %>'
document.adm_bd_dlg.poblacion.value = '<% if(request.getParameter("poblacion") != null) { out.print(request.getParameter("poblacion")); } else { out.print( bd.getPoblacion() ); } %>'
document.adm_bd_dlg.cp.value = '<% if(request.getParameter("cp") != null) { out.print(request.getParameter("cp")); } else { out.print( bd.getCP() ); } %>'
document.adm_bd_dlg.mail.value = '<% if(request.getParameter("mail") != null) { out.print(request.getParameter("mail")); } else { out.print( bd.getMail() ); } %>'
document.adm_bd_dlg.web.value = '<% if(request.getParameter("web") != null) { out.print(request.getParameter("web")); } else { out.print( bd.getWeb() ); } %>'
<%
}
%>
</script>
</body>
</html>
