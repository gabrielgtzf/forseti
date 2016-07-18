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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" import="forseti.*, forseti.sets.*, java.util.*, java.io.*, org.apache.commons.lang.mutable.MutableInt"%>
<%
	String rep_reportes_dlg = (String)request.getAttribute("rep_reportes_dlg");
	if(rep_reportes_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	JReportesAyudaSet ayu = new JReportesAyudaSet(request);
	ayu.m_Where = "ID_Report = " + JUtil.p(request.getParameter("REPID"));
	ayu.Open();
	
   	JReportesBind1Set rep = new JReportesBind1Set(request, JUtil.getSesion(request).getID_Usuario(), request.getParameter("REPID"), "CEF-1");
	rep.Open();
	
	String titulo =  JUtil.getSesion(request).getSesion("REP_REPORTES").generarTitulo(JUtil.Msj("CEF","REP_REPORTES","VISTA",request.getParameter("proceso"),3));
	
	// Carga el filtro
    JReportesBindFSet set = (JReportesBindFSet)request.getAttribute("BindFSet");
    boolean esGraf = rep.getAbsRow(0).getGraficar();
	String fechasql1 = (String)request.getAttribute("fechasql1");
	String fechasql2 = (String)request.getAttribute("fechasql2");
	
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
<% 	
	for(int i = 0; i < set.getNumRows(); i++)
	{	
		if(!set.getAbsRow(i).getFromCatalog())
		{
			if(set.getAbsRow(i).getBindDataType().equals("INT"))
			{
				MutableInt propMin = new MutableInt(-999999999);
				MutableInt propMax = new MutableInt(999999999); 
				JUtil.getReporteFiltroProps(set.getAbsRow(i).getPriDefault(),propMin,propMax);
%>
	if(!esNumeroEntero("<%= set.getAbsRow(i).getPriDataName() %>:", formAct.<%= set.getAbsRow(i).getPriDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%	
				if(set.getAbsRow(i).getIsRange())
				{ 
					JUtil.getReporteFiltroProps(set.getAbsRow(i).getSecDefault(),propMin,propMax);
%>
	if(!esNumeroEntero("<%= set.getAbsRow(i).getSecDataName() %>:", formAct.<%= set.getAbsRow(i).getSecDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%	
				}				
			}
			else if(set.getAbsRow(i).getBindDataType().equals("BYTE"))
			{ 
				MutableInt propMin = new MutableInt(-254);
				MutableInt propMax = new MutableInt(254); 
				JUtil.getReporteFiltroProps(set.getAbsRow(i).getPriDefault(),propMin,propMax); 
%>
	if(!esNumeroEntero("<%= set.getAbsRow(i).getPriDataName() %>:", formAct.<%= set.getAbsRow(i).getPriDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%	
				if(set.getAbsRow(i).getIsRange())
				{ 
					JUtil.getReporteFiltroProps(set.getAbsRow(i).getSecDefault(),propMin,propMax); 
%>
	if(!esNumeroEntero("<%= set.getAbsRow(i).getSecDataName() %>:", formAct.<%= set.getAbsRow(i).getSecDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%	
				}				
			}			
			else if(set.getAbsRow(i).getBindDataType().equals("DECIMAL") || set.getAbsRow(i).getBindDataType().equals("MONEY"))
			{ 
				MutableInt propMin = new MutableInt(-999999999);
				MutableInt propMax = new MutableInt(999999999); 
				JUtil.getReporteFiltroProps(set.getAbsRow(i).getPriDefault(),propMin,propMax);
%>
	if(!esNumeroDecimal("<%= set.getAbsRow(i).getPriDataName() %>:", formAct.<%= set.getAbsRow(i).getPriDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>, 9))
		return false;
<%		
				if(set.getAbsRow(i).getIsRange())
				{ 
					JUtil.getReporteFiltroProps(set.getAbsRow(i).getSecDefault(),propMin,propMax);
%>
	if(!esNumeroDecimal("<%= set.getAbsRow(i).getSecDataName() %>:", formAct.<%= set.getAbsRow(i).getSecDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>, 9))
		return false;
<%	
				}				
			}
			else if(!set.getAbsRow(i).getBindDataType().equals("BOOL")) // STRING O CUALQUIER OTRO MAS MENOS BOOL
			{
				MutableInt propMin = new MutableInt(1);
				MutableInt propMax = new MutableInt(254); 
				JUtil.getReporteFiltroProps(set.getAbsRow(i).getPriDefault(),propMin,propMax); 
%>
	if(!esCadena("<%= set.getAbsRow(i).getPriDataName() %>:", formAct.<%= set.getAbsRow(i).getPriDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%		
				if(set.getAbsRow(i).getIsRange())
				{ 
					JUtil.getReporteFiltroProps(set.getAbsRow(i).getSecDefault(),propMin,propMax); 
%>
	if(!esCadena("<%= set.getAbsRow(i).getSecDataName() %>:", formAct.<%= set.getAbsRow(i).getSecDataName() %>.value, <%= propMin.intValue() %>, <%= propMax.intValue() %>))
		return false;
<%	
				}					
			} 
		}
		else // Es de catalogo
		{	
%>
	if(!esCadena("<%= set.getAbsRow(i).getPriDataName() %>:", formAct.<%= set.getAbsRow(i).getPriDataName() %>.value, 1, 254))
		return false;
<%		
			if(set.getAbsRow(i).getIsRange())
			{ 
%>
	if(!esCadena("<%= set.getAbsRow(i).getSecDataName() %>:", formAct.<%= set.getAbsRow(i).getSecDataName() %>.value, 1, 254))
		return false;
<%	
			}					
		}	
	}	
%>
	if(confirm("Estas a punto de descargar un reporte. Ten en cuenta que dependiendo de la cantidad de datos y sus opciones de exportación, este puede tardar desde unos cuantos segundos, hasta varios minutos, incluso horas.\n\nSe paciente por favor.\n\n¿Estas seguro que deseas cargar este reporte?"))
	{
		//formAct.aceptar.disabled = true;
		return true;
	}
	else
		return false;
	
}

-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this); " action="/servlet/CEFReportesDlg" method="post" enctype="application/x-www-form-urlencoded" name="rep_reportes_dlg">
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
              		<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<input type="button" name="cancelar" onClick="javascript:history.back();" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
   if(request.getParameter("proceso").equals("CARGAR_REPORTE"))
   {
%>
   <tr> 
     <td class="txtCuerpoNg">
	 	<table width="100%" border="0" cellspacing="0" cellpadding="15">
		 <tr> 
     		<td class="txtCuerpoNg"><%= ayu.getAbsRow(0).getHelp() %></td>
   		  </tr>
		</table>
	 </td>
   </tr>
   <tr> 
     <td>&nbsp;</td>
   </tr>
<%
  	if (set.getNumRows() < 1)
  	{
%>
   <tr> 
     <td align="center" class="titChicoAzc">No existe filtro para este reporte, Presiona 
      aceptar para cargarlo o cancelar si no deseas verlo</td>
   </tr>
<%
  	}
  	else
  	{
%>
  <tr> 
    <td> 
      <table width="100%" border="0" cellspacing="3" cellpadding="0">
<%
	  	for(int i = 0; i < set.getNumRows(); i++)
		{
%>
          <tr> 
		  	<td><%= set.getAbsRow(i).getPriDataName() %></td>
			<td> 
 <%
			// Aqui se pone todas las probabilidades del filtro
			if(!set.getAbsRow(i).getFromCatalog())
			{
				if(set.getAbsRow(i).getBindDataType().equals("INT") || set.getAbsRow(i).getBindDataType().equals("BYTE"))
				{ 
					if(set.getAbsRow(i).getPriDefault().equals("{mes}"))
					{
						Calendar fecha = GregorianCalendar.getInstance();
         				int mes = JUtil.obtMes(fecha);
 
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>">
					<option value="1"<% if(mes == 1) { out.print(" selected"); } %>>enero</option>
					<option value="2"<% if(mes == 2) { out.print(" selected"); } %>>febrero</option>
					<option value="3"<% if(mes == 3) { out.print(" selected"); } %>>marzo</option>
					<option value="4"<% if(mes == 4) { out.print(" selected"); } %>>abril</option>
					<option value="5"<% if(mes == 5) { out.print(" selected"); } %>>mayo</option>
					<option value="6"<% if(mes == 6) { out.print(" selected"); } %>>junio</option>
					<option value="7"<% if(mes == 7) { out.print(" selected"); } %>>julio</option>
					<option value="8"<% if(mes == 8) { out.print(" selected"); } %>>agosto</option>
					<option value="9"<% if(mes == 9) { out.print(" selected"); } %>>septiembre</option>
					<option value="10"<% if(mes == 10) { out.print(" selected"); } %>>octubre</option>
					<option value="11"<% if(mes == 11) { out.print(" selected"); } %>>noviembre</option>
					<option value="12"<% if(mes == 12) { out.print(" selected"); } %>>diciembre</option>
				  </select>
<%
					}
					else if(set.getAbsRow(i).getPriDefault().equals("{ano}"))
					{
						Calendar fecha = GregorianCalendar.getInstance();
         				int ano = JUtil.obtAno(fecha);
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" value="<%= ano %>" size="5" maxlength="4">
 <%
					}
					else if(set.getAbsRow(i).getPriDefault().length() > 0 && set.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
					{
						Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
						JUtil.obtValoresFiltro(set.getAbsRow(i).getPriDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>">				
<%
						for(JLlaveValor lv : ops)
						{
        					//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
						}
%>
				</select>
<%
					}
					else
					{
						int index = set.getAbsRow(i).getPriDefault().lastIndexOf('}') + 1;
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" value="<%= set.getAbsRow(i).getPriDefault().substring(index) %>" size="5" maxlength="12">
 <%
 					}
					
 					if(set.getAbsRow(i).getIsRange())
					{ 
%>
				  <span class="titChicoAzc"> - Desde</span><br>
<%
						if(set.getAbsRow(i).getSecDefault().equals("{mes}"))
						{
							Calendar fecha = GregorianCalendar.getInstance();
         					int mes = JUtil.obtMes(fecha);
 
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>">
					<option value="1"<% if(mes == 1) { out.print(" selected"); } %>>enero</option>
					<option value="2"<% if(mes == 2) { out.print(" selected"); } %>>febrero</option>
					<option value="3"<% if(mes == 3) { out.print(" selected"); } %>>marzo</option>
					<option value="4"<% if(mes == 4) { out.print(" selected"); } %>>abril</option>
					<option value="5"<% if(mes == 5) { out.print(" selected"); } %>>mayo</option>
					<option value="6"<% if(mes == 6) { out.print(" selected"); } %>>junio</option>
					<option value="7"<% if(mes == 7) { out.print(" selected"); } %>>julio</option>
					<option value="8"<% if(mes == 8) { out.print(" selected"); } %>>agosto</option>
					<option value="9"<% if(mes == 9) { out.print(" selected"); } %>>septiembre</option>
					<option value="10"<% if(mes == 10) { out.print(" selected"); } %>>octubre</option>
					<option value="11"<% if(mes == 11) { out.print(" selected"); } %>>noviembre</option>
					<option value="12"<% if(mes == 12) { out.print(" selected"); } %>>diciembre</option>
				  </select><span class="titChicoAzc"> - Hasta</span>
<%
						}
						else if(set.getAbsRow(i).getSecDefault().equals("{ano}"))
						{
							Calendar fecha = GregorianCalendar.getInstance();
         					int ano = JUtil.obtAno(fecha);
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" value="<%= ano %>" size="5" maxlength="4"><span class="titChicoAzc"> - Hasta</span>
 <%
						}
						else if(set.getAbsRow(i).getSecDefault().length() > 0 && set.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
						{
							Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
							JUtil.obtValoresFiltro(set.getAbsRow(i).getSecDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>">				
<%
							for(JLlaveValor lv : ops)
							{
        						//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
							}
%>
				</select><span class="titChicoAzc"> - Hasta</span>
<%
						}
						else
						{
							int index = set.getAbsRow(i).getSecDefault().lastIndexOf('}') + 1;
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" value="<%= set.getAbsRow(i).getSecDefault().substring(index) %>" size="5" maxlength="12"><span class="titChicoAzc"> - Hasta</span>
 <%
 						}
					}				
				}
				else if(set.getAbsRow(i).getBindDataType().equals("BOOL"))
				{ 
%>
				 	SI<input name="<%= set.getAbsRow(i).getPriDataName() %>" type="radio" value="1"<% if(set.getAbsRow(i).getPriDefault().equals("true") || set.getAbsRow(i).getPriDefault().equals("1") || set.getAbsRow(i).getPriDefault().equals("si")) { out.print(" checked"); } %>>
				  	No<input name="<%= set.getAbsRow(i).getPriDataName() %>" type="radio" value="0"<% if(!set.getAbsRow(i).getPriDefault().equals("true") && !set.getAbsRow(i).getPriDefault().equals("1") && !set.getAbsRow(i).getPriDefault().equals("si")) { out.print(" checked"); } %>>
<%	
				}	
				else if(set.getAbsRow(i).getBindDataType().equals("TIME"))
				{ 
					Calendar fecha = GregorianCalendar.getInstance();
%>
				  <input name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" id="<%= set.getAbsRow(i).getPriDataName() %>" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(fecha,fechasql1) %>">
				  <a href="javascript:NewCal('<%= set.getAbsRow(i).getPriDataName() %>','<%= fechasql2 %>',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a>
<%
					if(set.getAbsRow(i).getIsRange())
					{ 
%>
				  <span class="titChicoAzc"> - Desde</span><br><input name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" id="<%= set.getAbsRow(i).getSecDataName() %>" size="12" maxlength="15" readonly="true" value="<%= JUtil.obtFechaTxt(fecha,fechasql1) %>">
				  <a href="javascript:NewCal('<%= set.getAbsRow(i).getSecDataName() %>','<%= fechasql2 %>',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a><span class="titChicoAzc"> - Hasta</span><br>
<%	
					}				
				}
				else if(set.getAbsRow(i).getBindDataType().equals("DECIMAL") || set.getAbsRow(i).getBindDataType().equals("MONEY"))
				{ 
					if(set.getAbsRow(i).getPriDefault().length() > 0 && set.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
					{
						Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
						JUtil.obtValoresFiltro(set.getAbsRow(i).getPriDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>">				
<%
						for(JLlaveValor lv : ops)
						{
        					//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
						}
%>
				</select>
<%
					}
					else
					{
						int index = set.getAbsRow(i).getPriDefault().lastIndexOf('}') + 1;
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" value="<%= set.getAbsRow(i).getPriDefault().substring(index) %>" size="10" maxlength="15">
 <%
 					}
					
 					if(set.getAbsRow(i).getIsRange())
					{ 
%>
				  <span class="titChicoAzc"> - Desde</span><br>
<%	
						if(set.getAbsRow(i).getSecDefault().length() > 0 && set.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
						{
							Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
							JUtil.obtValoresFiltro(set.getAbsRow(i).getSecDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>">				
<%
        					for(JLlaveValor lv : ops)
							{
        						//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
							}
%>
				</select><span class="titChicoAzc"> - Hasta</span>
<%
						}
						else
						{
							int index = set.getAbsRow(i).getSecDefault().lastIndexOf('}') + 1;
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" value="<%= set.getAbsRow(i).getSecDefault().substring(index) %>" size="10" maxlength="15"><span class="titChicoAzc"> - Hasta</span>
 <%
 						}
					}				
				}
				else // STRING O CUALQUIER OTRO MAS
				{
					if(set.getAbsRow(i).getPriDefault().length() > 0 && set.getAbsRow(i).getPriDefault().substring(0,1).equals("["))
					{
						Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
						JUtil.obtValoresFiltro(set.getAbsRow(i).getPriDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>">
<%
						for(JLlaveValor lv : ops)
						{
        					//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
						}
%>
				</select>
<%
					}
					else
					{ 
						int index = set.getAbsRow(i).getPriDefault().lastIndexOf('}') + 1;
%>
					<input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" value="<%= set.getAbsRow(i).getPriDefault().substring(index) %>" size="35" maxlength="254">
 <%
 					}
					
 					if(set.getAbsRow(i).getIsRange())
					{ 
%>
				  <span class="titChicoAzc"> - Desde</span><br>
<%
						if(set.getAbsRow(i).getSecDefault().length() > 0 && set.getAbsRow(i).getSecDefault().substring(0,1).equals("["))
						{
							Vector<JLlaveValor> ops = new Vector<JLlaveValor>();
							JUtil.obtValoresFiltro(set.getAbsRow(i).getSecDefault(), ops);
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>">				
<%
        					for(JLlaveValor lv : ops)
							{
        						//System.out.println(lv.getLlave() + "=" + lv.getValor());
%>
					<option value="<%= lv.getLlave() %>"><%= lv.getValor() %></option>
<%
							}
%>
				</select><span class="titChicoAzc"> - Hasta</span>
<%
						}
						else
						{
							int index = set.getAbsRow(i).getSecDefault().lastIndexOf('}') + 1;
%>				  
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" value="<%= set.getAbsRow(i).getSecDefault().substring(index) %>" size="35" maxlength="254"><span class="titChicoAzc"> - Hasta</span>
<%
						}	
					}				
				}
			}
			else // Si son de catalogo
			{
				String titulocat = "";
				
				if(set.getAbsRow(i).getBindDataType().equals("INT") || 
					  set.getAbsRow(i).getBindDataType().equals("BYTE") || 
						 set.getAbsRow(i).getBindDataType().equals("STRING"))
				{ 
					JListasCatalogosSet cats = new  JListasCatalogosSet(request);
					cats.ConCat(true);
					
					String pridefault, secdefault, seguridad, sel_clause; boolean replong;
					int idcatalogo = Integer.parseInt(set.getAbsRow(i).getSelect_Clause());
					cats.m_Where = "ID_Catalogo = '" + idcatalogo + "'";
					cats.Open();
					titulocat = cats.getAbsRow(0).getNombre();
					pridefault = cats.getAbsRow(0).getPriDefault();
					secdefault = cats.getAbsRow(0).getSecDefault();
					seguridad = cats.getAbsRow(0).getSeguridad();
					sel_clause = cats.getAbsRow(0).getSelect_Clause();
					replong = cats.getAbsRow(0).getRepLong();
										
					if(replong == true) // Es un catalogo largo, por lo tanto se muestra en input y no en select
					{
						JProcessSet setDesde = new JProcessSet(request);
						setDesde.setSQL(pridefault);
						setDesde.Open();
				
						String fsi_val = (setDesde.getNumRows() > 0) ? setDesde.getAbsRow(0).getSTS("Col1") : "";
						String fsi_desc = (setDesde.getNumRows() > 0) ? setDesde.getAbsRow(0).getSTS("Col2") : "";
%>
				  <input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>" type="text" value="<% if(request.getParameter(set.getAbsRow(i).getPriDataName()) != null) { out.print(request.getParameter(set.getAbsRow(i).getPriDataName())); } else { out.print(fsi_val); } %>" size="12" maxlength="254"><a href="javascript:abrirCatalogo('../../forsetiweb/listasfiltro_dlg.jsp?formul=rep_reportes_dlg&lista=<%= set.getAbsRow(i).getPriDataName() %>&idreporte=<%= request.getParameter("REPID") %>&idcolumna=<%= set.getAbsRow(i).getID_Column() %>&destino=<%= set.getAbsRow(i).getPriDataName() + "_FSIDESC" %>&titulocat=<%= titulocat %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a>
				    <input class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() + "_FSIDESC" %>" type="text" readonly="true" value="<% if(request.getParameter(set.getAbsRow(i).getPriDataName()) != null) { out.print(request.getParameter(set.getAbsRow(i).getPriDataName() + "_FSIDESC")); } else { out.print(fsi_desc); } %>" size="45" maxlength="254">
<%	
						if(set.getAbsRow(i).getIsRange() && seguridad.equals(""))
						{ 
							JProcessSet setHasta = new JProcessSet(request);
							setHasta.setSQL(secdefault);
							setHasta.Open();
				
							String fsi_val2 = (setHasta.getNumRows() > 0) ? setHasta.getAbsRow(0).getSTS("Col1") : "";
							String fsi_desc2 = (setHasta.getNumRows() > 0) ? setHasta.getAbsRow(0).getSTS("Col2") : "";
%>
				  <span class="titChicoAzc"> - Desde</span><br><input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>" type="text" value="<% if(request.getParameter(set.getAbsRow(i).getSecDataName()) != null) { out.print(request.getParameter(set.getAbsRow(i).getSecDataName())); } else { out.print(fsi_val2); } %>" size="12" maxlength="254"><a href="javascript:abrirCatalogo('../../forsetiweb/listasfiltro_dlg.jsp?formul=rep_reportes_dlg&lista=<%= set.getAbsRow(i).getSecDataName() %>&idreporte=<%= request.getParameter("REPID") %>&idcolumna=<%= set.getAbsRow(i).getID_Column() %>&destino=<%= set.getAbsRow(i).getSecDataName() + "_FSIDESC" %>&titulocat=<%= titulocat %>',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="20" height="20" border="0"></a>
				    <input class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() + "_FSIDESC" %>" type="text" readonly="true" value="<% if(request.getParameter(set.getAbsRow(i).getSecDataName()) != null) { out.print(request.getParameter(set.getAbsRow(i).getSecDataName() + "_FSIDESC")); } else { out.print(fsi_desc2); } %>" size="45" maxlength="254"><span class="titChicoAzc"> - Hasta</span>
<%
						}
					} // Es catalogo corto: replong == false
					else
					{
						JProcessSet setcat = new JProcessSet(request);
						setcat.setSelect(sel_clause);
						setcat.Open();
				
%>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getPriDataName() %>">				
<%
        				for(int p = 0; p < setcat.getNumRows(); p++)
        				{
							String fsi_val = setcat.getAbsRow(p).getSTS("Col1");
							String fsi_desc = setcat.getAbsRow(p).getSTS("Col2");
%>
					<option value="<%= fsi_val %>"><%= fsi_desc %></option>
<%
						}
%>
				</select>
<%
						if(set.getAbsRow(i).getIsRange() && seguridad.equals(""))
						{
%>
				<span class="titChicoAzc"> - Desde</span><br>
				<select class="cpoBco" name="<%= set.getAbsRow(i).getSecDataName() %>">				
<%
        					for(int p = (setcat.getNumRows()-1); p >= 0; p--)
        					{
								String fsi_val = setcat.getAbsRow(p).getSTS("Col1");
								String fsi_desc = setcat.getAbsRow(p).getSTS("Col2");
%>
					<option value="<%= fsi_val %>"><%= fsi_desc %></option>
<%
							}
%>
				</select><span class="titChicoAzc"> - Hasta</span>
<% 
						}
					}
				}
			}
%>
            </td>
		  </tr>
		  <tr>
    		<td>&nbsp;</td> 
			<td class="titChicoNeg"><%= set.getAbsRow(i).getInstructions() %></td>
	 	  </tr>
		  <tr> 
    		<td colspan="2"><hr></td>
	 	  </tr>
		  
<%
		} 
%>
	 </table>
	</td>
      </tr>
      <tr>
      
<% 
	}	
%>
   <tr>
  	<td>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td width="45%" class="titCuerpoAzc">Opciones de Exportacion</td>
			<td><select name="exportacion" style="width:100%;">
                <option value="html" selected>--- No exportar ---</option>
				<option value="pdf">Archivo PDF</option>
                <option value="odt">Documento de Open Document (.odt)</option>
               	<option value="doc">Documento de Microsoft Word (.doc)</option>
				<option value="rtf">Formato de Texto Enriquecido (.rtf)</option>
                <option value="ods">Hoja de Calculo de Open Document (.ods)</option>
                <option value="xls">Hoja de Calculo de Microsoft Excel (.xls)</option>
                <option value="xml">Archivo XML</option>
                <option value="csv">Valores Separados por Columna</option>
				<option value="xhtml">Salida del codigo xhtml</option>
				<option value="sql">Salida de estructura SQL - insert</option>
              </select>
            </td>
		  </tr>
		</table>
	</td>
  </tr>
  
<%
	if( esGraf )
	{
%>
 <tr>
  	<td>
  	    <table width="100%" border="0" cellpadding="2" cellspacing="2">
          <tr> 
            <td colspan="5" align="center" class="titCuerpoAzc">Este reporte se 
              puede graficar</td>
          </tr>
          <tr> 
            <td>¿ Deseas graficar ? </td>
            <td><input type="radio" name="fsi_sino" value="1">
              SI </td>
            <td><input type="radio" name="fsi_sino" value="0" checked>
              NO </td>
            <td colspan="2"> 
              <select name="formato" style="width:100%;">
                <option value="gif" selected>Graphics Interchange Format (.gif)</option>
                <option value="png">Portable Network Graphics (.png)</option>
              </select> </td>
          </tr>
          <tr> 
            <td>Graficar como: </td>
            <td> <input type="radio" name="fsi_graf" value="BAR" checked>
              BARRAS</td>
            <td> <input type="radio" name="fsi_graf" value="LIN">
              LINEAS</td>
            <td> <input type="radio" name="fsi_graf" value="CIRC">
              CIRCULAR</td>
            <td> <input type="radio" name="fsi_graf" value="AREA">
              AREAS </td>
          </tr>
          <tr> 
            <td>Variacion:</td>
            <td><input type="radio" name="fsi_tipo" value="0" checked>
              NORMAL</td>
            <td><input type="radio" name="fsi_tipo" value="1">
              EN PILAS</td>
            <td><input type="radio" name="fsi_tipo" value="2">
              PORCENTAJE</td>
            <td></td>
          </tr>
          <tr> 
            <td>Direccion de datos: </td>
            <td> <input type="radio" name="fsi_encols" value="1" checked>
              EN COLUMNAS</td>
			<td> <input type="radio" name="fsi_encols" value="0">
              EN FILAS</td>
            <td> </td>
            <td> </td>
          </tr>
          <tr> 
            <td>Mostrar lineas eje: </td>
            <td> <input type="radio" name="fsi_linejes" value="1" checked>
              SI</td>
            <td> <input type="radio" name="fsi_linejes" value="0">
              NO</td>
            <td> </td>
            <td> </td>
          </tr>
        </table>
  	</td>
  </tr>
<%
	}
%>
  <tr> 
    <td> 	
		<table width="100%" border="0" cellspacing="3" cellpadding="0">
		  <tr> 
    		<td colspan="2">
			<input name="proceso" type="hidden" value="CARGAR_REPORTE">
			<input name="subproceso" type="hidden" value="GENERAR">
			<input name="REPID" type="hidden" value="<%= request.getParameter("REPID") %>">
			&nbsp;</td>
	 	  </tr>
         </table>
      </td>
  </tr>
<%
      
    } // fin proceso
%>
</table>
</form>
</body>
</html>
