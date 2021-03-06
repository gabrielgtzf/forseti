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
<%@ page import="forseti.*, forseti.sets.*, fsi_admin.*, java.util.*, java.io.*"%>
<% 
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
	JBDRegistradasSet set = new JBDRegistradasSet(null);
	set.ConCat(true);
	set.m_OrderBy = "ID_BD ASC";
	set.Open();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/safdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esCadena("<%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE") %>", formAct.nombre.value, 3, 20) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD") %>", formAct.password.value, 3, 30) ||
		!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","PASSWORD",2) %>", formAct.confpwd.value, 3, 30) )
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
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
            <td width="20%" align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                Mostrar respaldos con fecha mayor o igual a</td>
            <td>
				<table width="100%" border="0" cellspacing="1" cellpadding="0">
				 <tr>
				 	<td><input name="fecha" type="text" id="fecha" size="12" maxlength="15" readonly="true"> 
                          <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
                    
                  <td>Copiar Empresa instalada:</td>
				 	<td>
					<select style="width: 90%;" name="basedatos" class="cpoBco" onChange="javascript: establecerProcesoSVE(this.form.subproceso, 'MOSTRAR_BDS'); this.form.submit();">
                		<option value="NC"<% if(request.getParameter("basedatos") != null) {
										if(request.getParameter("basedatos").equals("NC")) {
											out.println(" selected");
										}
									 } %>>--- Empresa ---</option>
                <%
								  for(int i = 0; i< set.getNumRows(); i++)
								  {
		%>
                		<option value="<%= set.getAbsRow(i).getNombre() %>"<% 
									if(request.getParameter("basedatos") != null) {
										if(request.getParameter("basedatos").equals(set.getAbsRow(i).getNombre())) {
											out.print(" selected");
										}
									 } %>> 
                	<%=  set.getAbsRow(i).getNombre().substring(6)  %>
                	</option>
                <%
								  }
				%>
              </select>
					</td>
					
                  <td>Restaurar empresa perdida:</td>
				  	
                  <td><input style="width: 90%;" type="text" name="bdperdida" onBlur="javascript: establecerProcesoSVE(this.form.subproceso, 'MOSTRAR_BDS'); this.form.submit();"></td>
				   </tr>
				</table>
			</td>
          </tr>
		  <tr>
		  	<td align="right" valign="top">Selecciona los archivos de respaldo</td>
            <td>
				<select name="respaldo" style="width: 90%;" size="10" >
<%
			String ruta_resp = (String)request.getAttribute("ruta_resp");
			if(ruta_resp != null && request.getParameter("fecha") != null)
			{
				Calendar fecha = JUtil.estCalendario(request.getParameter("fecha"));
				Calendar hoy = GregorianCalendar.getInstance();
        		while(hoy.compareTo(fecha) >= 0)
        		{
					String dirarch = ruta_resp;
        			String filtro;
					if(!request.getParameter("basedatos").equals("NC"))
						filtro = request.getParameter("basedatos") + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd") + "-\\d{2}-\\d{2}.zip";
					else
						filtro = "FSIBD_" + request.getParameter("bdperdida") + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd") + "-\\d{2}-\\d{2}.zip";
					
					System.out.println(ruta_resp + "/" + filtro);
        			JFsiFiltroMatch f = new JFsiFiltroMatch(filtro);
        			File dir = new File(dirarch);	
        			String [] dirlist = dir.list(f);
        			  
        			for(int i=0; i < dirlist.length; i++)
        			{
%>
  				<option value="<%= dirlist[i] %>">Respaldo: <%= dirlist[i] %></option>
<%
        			}
					fecha.add(Calendar.DATE, 1);
        		}
			}
%>
              </select></td>
		  </tr>
          <tr> 
            <td align="right">Restaurar como</td>
            <td><input name="nombre" type="text" class="cpoCol" size="15" maxlength="20"> 
              <%= JUtil.Msj("SAF","ADMIN_BD","DLG","NOMBRE",2) %></td> 
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
	  </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
document.adm_bd_dlg.bdperdida.value = '<% if(request.getParameter("bdperdida") != null) { out.print( request.getParameter("bdperdida") ); } else { out.print(""); } %>'
document.adm_bd_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else { out.print(""); } %>'
document.adm_bd_dlg.fecha.value = '<% if(request.getParameter("fecha") != null) { out.print( request.getParameter("fecha") ); } else { out.print( JUtil.obtFechaTxt(new Date(), "dd/MMM/yyyy") ); } %>'
</script>
</body>
</html>
