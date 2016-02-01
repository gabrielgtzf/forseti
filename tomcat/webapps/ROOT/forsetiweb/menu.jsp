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
<%@ page import="forseti.JUtil" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head><title>Menu Principal</title></head>
<body bgcolor="#333333">
<!--script language='JavaScript1.2' src='../compfsi/coolmenus3.js'>
</script>
<script>
function lib_bwcheck()
{
	this.ver=navigator.appVersion; this.agent=navigator.userAgent
	this.dom=document.getElementById?1:0
	this.ie5=(this.ver.indexOf("MSIE 5")>-1 && this.dom)?1:0;
	this.ie6=(this.ver.indexOf("MSIE 6")>-1 && this.dom)?1:0;
	this.ie4=(document.all && !this.dom)?1:0;
	this.ie=this.ie4||this.ie5||this.ie6
	this.mac=this.agent.indexOf("Mac")>-1
	this.opera5=this.agent.indexOf("Opera 5")>-1
	this.ns6=(this.dom && parseInt(this.ver) >= 5) ?1:0;
	this.ns4=(document.layers && !this.dom)?1:0;
	this.bw=(this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5 || this.dom)
	return this
}
var bw=new lib_bwcheck()
var mDebugging=2
oCMenu1=new makeCoolMenu("oCMenu1")
oCMenu1.useframes=1
oCMenu1.frame="cuerpo"
oCMenu1.useclick=0
oCMenu1.useNS4links=1
oCMenu1.NS4padding=2
oCMenu1.checkselect=0
oCMenu1.offlineUrl=""
oCMenu1.onlineUrl=""
oCMenu1.pagecheck=1
oCMenu1.checkscroll=1
oCMenu1.resizecheck=1
oCMenu1.wait=1000
oCMenu1.usebar=0
oCMenu1.barcolor="#23196C"
oCMenu1.barwidth="menu"
oCMenu1.barheight="menu"
oCMenu1.barx="menu"
oCMenu1.bary="menu"
oCMenu1.barinheritborder=0
oCMenu1.rows=1
oCMenu1.fromleft=0
oCMenu1.fromtop=0
oCMenu1.pxbetween=5
oCMenu1.menuplacement="left"

oCMenu1.level[0]=new Array()

oCMenu1.level[0].clip=1
oCMenu1.level[0].clippx=5
oCMenu1.level[0].cliptim=20
oCMenu1.level[0].filter=0

oCMenu1.level[0].align="bottom"


oCMenu1.level[0].width=140
oCMenu1.level[0].height=17
oCMenu1.level[0].bgcoloroff="#333333"
oCMenu1.level[0].bgcoloron="#0099FF"
oCMenu1.level[0].textcolor="#FFFFFF"
oCMenu1.level[0].hovercolor="#FFFFFF"
oCMenu1.level[0].style="padding:3px; font-family: Arial, 'Apple Chancery','DejaVu Sans Mono', monospace; font-size:10px; font-weight:Bold"
oCMenu1.level[0].border=1
oCMenu1.level[0].bordercolor="#444444"
oCMenu1.level[0].offsetX=10
oCMenu1.level[0].offsetY=0
oCMenu1.level[0].NS4font="Tahoma, Arial, Helvetica"
oCMenu1.level[0].NS4fontSize="2"

oCMenu1.level[1]=new Array()

oCMenu1.level[1].width=140
oCMenu1.level[1].height=18
oCMenu1.level[1].bgcoloroff="#0099FF"
oCMenu1.level[1].bgcoloron="#666666"
oCMenu1.level[1].textcolor="#FFFFFF"
oCMenu1.level[1].hovercolor="#FFFFFF"
oCMenu1.level[1].style="padding:2px; font-family: Arial, 'Apple Chancery','DejaVu Sans Mono', monospace; font-size:10px; font-weight:Normal"
oCMenu1.level[1].border=0
oCMenu1.level[1].bordercolor="#444444"
oCMenu1.level[1].offsetX=-5
oCMenu1.level[1].offsetY=5
oCMenu1.level[1].NS4font="Tahoma, Arial, Helvetica"
oCMenu1.level[1].NS4fontSize="2"

oCMenu1.level[2]=new Array()

oCMenu1.level[2].width=140
oCMenu1.level[2].height=18
oCMenu1.level[2].bgcoloroff="#0099FF"
oCMenu1.level[2].bgcoloron="#666666"
oCMenu1.level[2].textcolor="#FFFFFF"
oCMenu1.level[2].hovercolor="#FFFFFF"
oCMenu1.level[2].style="padding:2px; font-family: Arial, 'Apple Chancery','DejaVu Sans Mono', monospace; font-size:10px; font-weight:Normal"
oCMenu1.level[2].border=0
oCMenu1.level[2].bordercolor="#444444"
oCMenu1.level[2].offsetX=-5
oCMenu1.level[2].offsetY=5
oCMenu1.level[2].NS4font="Tahoma, Arial, Helvetica"
oCMenu1.level[2].NS4fontSize="2"

oCMenu1.level[3]=new Array()

oCMenu1.level[3].width=140
oCMenu1.level[3].height=18
oCMenu1.level[3].bgcoloroff="#0099FF"
oCMenu1.level[3].bgcoloron="#666666"
oCMenu1.level[3].textcolor="#FFFFFF"
oCMenu1.level[3].hovercolor="#FFFFFF"
oCMenu1.level[3].style="padding:2px; font-family: Arial, 'Apple Chancery','DejaVu Sans Mono', monospace; font-size:10px; font-weight:Normal"
oCMenu1.level[3].border=0
oCMenu1.level[3].bordercolor="#444444"
oCMenu1.level[3].offsetX=-5
oCMenu1.level[3].offsetY=5
oCMenu1.level[3].NS4font="Tahoma, Arial, Helvetica"
oCMenu1.level[3].NS4fontSize="2"

oCMenu1.makeMenu('MENU','','&nbsp;<%= JUtil.Msj("GLB","GLB","GLB","MENU") %>','')
<%
if(JUtil.getSesion(request).getPermiso("CONT"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_CONTABILIDAD','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %>','')
<%if(JUtil.getSesion(request).getPermiso("CONT_CATCUENTAS")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_CATCUENTAS','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","CATCUENTAS") %>','/servlet/CEFContaCatCuentasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_RUBROS")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_RUBROS','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","RUBROS") %>','/servlet/CEFContaRubrosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_TIPOPOLIZA")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_TIPOPOLIZA','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","TIPOPOLIZA") %>','/servlet/CEFContaTipoPolizasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLIZAS")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_POLIZAS','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLIZAS") %>','/servlet/CEFContaPolizasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_ENLACES")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_ENLACES','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","ENLACES") %>','/servlet/CEFContaEnlacesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("CONT_POLCIERRE")) {%>oCMenu1.makeMenu('CEF_MENU_CONTABILIDAD_POLCIERRE','CEF_MENU_GLB_CONTABILIDAD','&nbsp;<%= JUtil.Msj("CEF","MENU","CONTABILIDAD","POLCIERRE") %>','/servlet/CEFContaPolizasCierreCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("INVSERV"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_INVSERV','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %>','')
<%if(JUtil.getSesion(request).getPermiso("INVSERV_LINEAS")) {%>oCMenu1.makeMenu('CEF_MENU_INVSERV_LINEAS','CEF_MENU_GLB_INVSERV','&nbsp;<%= JUtil.Msj("CEF","MENU","INVSERV","LINEAS") %>','/servlet/CEFCatLineasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_PROD")) {%>oCMenu1.makeMenu('CEF_MENU_INVSERV_PROD','CEF_MENU_GLB_INVSERV','&nbsp;<%= JUtil.Msj("CEF","MENU","INVSERV","PROD") %>','/servlet/CEFCatProdCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_SERV")) {%>oCMenu1.makeMenu('CEF_MENU_INVSERV_SERV','CEF_MENU_GLB_INVSERV','&nbsp;<%= JUtil.Msj("CEF","MENU","INVSERV","SERV") %>','/servlet/CEFCatServCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("INVSERV_GASTOS")) {%>oCMenu1.makeMenu('CEF_MENU_INVSERV_GASTOS','CEF_MENU_GLB_INVSERV','&nbsp;<%= JUtil.Msj("CEF","MENU","INVSERV","GASTOS") %>','/servlet/CEFCatGastosCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("BANCAJ"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_BANCAJ','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %>','')
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_BANCOS")) {%>oCMenu1.makeMenu('CEF_MENU_BANCAJ_BANCOS','CEF_MENU_GLB_BANCAJ','&nbsp;<%= JUtil.Msj("CEF","MENU","BANCAJ","BANCOS") %>','/servlet/CEFMovBancariosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CAJAS")) {%>oCMenu1.makeMenu('CEF_MENU_BANCAJ_CAJAS','CEF_MENU_GLB_BANCAJ','&nbsp;<%= JUtil.Msj("CEF","MENU","BANCAJ","CAJAS") %>','/servlet/CEFMovCajaCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_VALES")) {%>oCMenu1.makeMenu('CEF_MENU_BANCAJ_VALES','CEF_MENU_GLB_BANCAJ','&nbsp;<%= JUtil.Msj("CEF","MENU","BANCAJ","VALES") %>','/servlet/CEFValesCajaCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("BANCAJ_CIERRES")) {%>oCMenu1.makeMenu('CEF_MENU_BANCAJ_CIERRES','CEF_MENU_GLB_BANCAJ','&nbsp;<%= JUtil.Msj("CEF","MENU","BANCAJ","CIERRES") %>','/servlet/CEFCierresCajaCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("ALM"))
{
%>	
oCMenu1.makeMenu('CEF_MENU_GLB_ALM','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","ALM") %>','')
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVIM")) {%>oCMenu1.makeMenu('CEF_MENU_ALM_MOVIM','CEF_MENU_GLB_ALM','&nbsp;<%= JUtil.Msj("CEF","MENU","ALM","MOVIM") %>','/servlet/CEFAlmMovimientosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_MOVPLANT")) {%>oCMenu1.makeMenu('CEF_MENU_ALM_MOVPLANT','CEF_MENU_GLB_ALM','&nbsp;<%= JUtil.Msj("CEF","MENU","ALM","MOVPLANT") %>','/servlet/CEFAlmMovimPlantCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_TRASPASOS")) {%>oCMenu1.makeMenu('CEF_MENU_ALM_TRASPASOS','CEF_MENU_GLB_ALM','&nbsp;<%= JUtil.Msj("CEF","MENU","ALM","TRASPASOS") %>','/servlet/CEFAlmTraspasosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_REQUERIMIENTOS")) {%>oCMenu1.makeMenu('CEF_MENU_ALM_REQUERIMIENTOS','CEF_MENU_GLB_ALM','&nbsp;<%= JUtil.Msj("CEF","MENU","ALM","REQUERIMIENTOS") %>','/servlet/CEFAlmRequerimientosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("ALM_CHFIS")) {%>oCMenu1.makeMenu('CEF_MENU_ALM_CHFIS','CEF_MENU_GLB_ALM','&nbsp;<%= JUtil.Msj("CEF","MENU","ALM","CHFIS") %>','/servlet/CEFAlmChFisCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("COMP"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_COMP','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","COMP") %>','')
<%if(JUtil.getSesion(request).getPermiso("COMP_PROVEE")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_PROVEE','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","PROVEE") %>','/servlet/CEFCompProveeCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_CXP")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_CXP','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","CXP") %>','/servlet/CEFCompCXPCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_ORD")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_ORD','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","ORD") %>','/servlet/CEFCompOrdenesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_REC")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_REC','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","REC") %>','/servlet/CEFCompRecepcionesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_FAC")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_FAC','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","FAC") %>','/servlet/CEFCompFactCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_DEV")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_DEV','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","DEV") %>','/servlet/CEFCompDevolucionesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_POL")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_POL','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","POL") %>','/servlet/CEFCompPoliticasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("COMP_GAS")) {%>oCMenu1.makeMenu('CEF_MENU_COMP_GAS','CEF_MENU_GLB_COMP','&nbsp;<%= JUtil.Msj("CEF","MENU","COMP","GAS") %>','/servlet/CEFCompGastosCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("VEN"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_VEN','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","VEN") %>','')
<%if(JUtil.getSesion(request).getPermiso("VEN_CLIENT")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_CLIENT','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","CLIENT") %>','/servlet/CEFVenClientCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_CXC")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_CXC','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","CXC") %>','/servlet/CEFVenCXCCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_COT")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_COT','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","COT") %>','/servlet/CEFVenCotizacionesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_PED")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_PED','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","PED") %>','/servlet/CEFVenPedidosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_REM")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_REM','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","REM") %>','/servlet/CEFVenRemisionesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_FAC")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_FAC','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","FAC") %>','/servlet/CEFVenFactCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_DEV")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_DEV','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","DEV") %>','/servlet/CEFVenDevolucionesCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("VEN_POL")) {%>oCMenu1.makeMenu('CEF_MENU_VEN_POL','CEF_MENU_GLB_VEN','&nbsp;<%= JUtil.Msj("CEF","MENU","VEN","POL") %>','/servlet/CEFVenPoliticasCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("PROD"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_PROD','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","PROD") %>','')
<%if(JUtil.getSesion(request).getPermiso("PROD_FORMULAS")) {%>oCMenu1.makeMenu('CEF_MENU_PROD_FORMULAS','CEF_MENU_GLB_PROD','&nbsp;<%= JUtil.Msj("CEF","MENU","PROD","FORMULAS") %>','/servlet/CEFProdFormulasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("PROD_PRODUCCION")) {%>oCMenu1.makeMenu('CEF_MENU_PROD_PRODUCCION','CEF_MENU_GLB_PROD','&nbsp;<%= JUtil.Msj("CEF","MENU","PROD","PRODUCCION") %>','/servlet/CEFProdProduccionCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("NOM"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_NOM','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","NOM") %>','')
oCMenu1.makeMenu('CEF_MENU_NOM_CAT','CEF_MENU_GLB_NOM','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","CAT") %>','')
<%if(JUtil.getSesion(request).getPermiso("NOM_MOVIMIENTOS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_MOVIMIENTOS','CEF_MENU_NOM_CAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","MOVIMIENTOS") %>','/servlet/CEFNomMovimNomCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_DEPARTAMENTOS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_DEPARTAMENTOS','CEF_MENU_NOM_CAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","DEPARTAMENTOS") %>','/servlet/CEFNomDepartamentosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_TURNOS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_TURNOS','CEF_MENU_NOM_CAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","TURNOS") %>','/servlet/CEFNomTurnosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CATEGORIAS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_CATEGORIAS','CEF_MENU_NOM_CAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","CATEGORIAS") %>','/servlet/CEFNomCategoriasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_EMPLEADOS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_EMPLEADOS','CEF_MENU_NOM_CAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","EMPLEADOS") %>','/servlet/CEFNomEmpleadosCtrl')<%}%>
oCMenu1.makeMenu('CEF_MENU_NOM_TBL','CEF_MENU_GLB_NOM','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","TBL") %>','')
<%if(JUtil.getSesion(request).getPermiso("NOM_ISR")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_ISR','CEF_MENU_NOM_TBL','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","ISR") %>','/servlet/CEFNomIsrCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_IMSS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_IMSS','CEF_MENU_NOM_TBL','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","IMSS") %>','/servlet/CEFNomImssCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_CREDSAL")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_CREDSAL','CEF_MENU_NOM_TBL','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","CREDSAL") %>','/servlet/CEFNomCredSalCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_AGUINALDO")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_AGUINALDO','CEF_MENU_NOM_TBL','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","AGUINALDO") %>','/servlet/CEFNomAguinaldoCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_VACACIONES")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_VACACIONES','CEF_MENU_NOM_TBL','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","VACACIONES") %>','/servlet/CEFNomVacacionesCtrl')<%}%>
oCMenu1.makeMenu('CEF_MENU_NOM_DAT','CEF_MENU_GLB_NOM','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","DAT") %>','')
<%if(JUtil.getSesion(request).getPermiso("NOM_ASISTENCIAS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_ASISTENCIAS','CEF_MENU_NOM_DAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","ASISTENCIAS") %>','/servlet/CEFNomAsistenciasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PERMISOS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_PERMISOS','CEF_MENU_NOM_DAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","PERMISOS") %>','/servlet/CEFNomPermisosCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_PLANTILLAS")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_PLANTILLAS','CEF_MENU_NOM_DAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","PLANTILLAS") %>','/servlet/CEFNomPlantillasCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_FONACOT")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_FONACOT','CEF_MENU_NOM_DAT','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","FONACOT") %>','/servlet/CEFNomFonacotCtrl')<%}%>
oCMenu1.makeMenu('CEF_MENU_NOM_PROC','CEF_MENU_GLB_NOM','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","PROC") %>','')
<%if(JUtil.getSesion(request).getPermiso("NOM_CIERRE")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_CIERRE','CEF_MENU_NOM_PROC','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","CIERRE") %>','/servlet/CEFNomCierreDiarioCtrl')<%}%>
<%if(JUtil.getSesion(request).getPermiso("NOM_NOMINA")) {%>oCMenu1.makeMenu('CEF_MENU_NOM_NOMINA','CEF_MENU_NOM_PROC','&nbsp;<%= JUtil.Msj("CEF","MENU","NOM","NOMINA") %>','/servlet/CEFNomMovDirCtrl')<%}%>
<%
}
if(JUtil.getSesion(request).getPermiso("CRM"))
{
%>
oCMenu1.makeMenu('CEF_MENU_GLB_CRM','MENU','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","CRM") %>','')
oCMenu1.makeMenu('CEF_MENU_CRM_AGECOL','CEF_MENU_GLB_CRM','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","AGECOL") %>','')
	oCMenu1.makeMenu('CEF_MENU_CRM_CALENDARIO','CEF_MENU_CRM_AGECOL','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","CALENDARIO") %>','/servlet/CEFCRMCalendarioCtrl')
oCMenu1.makeMenu('CEF_MENU_CRM_GESCON','CEF_MENU_GLB_CRM','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","GESCON") %>','')
	oCMenu1.makeMenu('CEF_MENU_CRM_COMPANIAS','CEF_MENU_CRM_GESCON','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","COMPANIAS") %>','/servlet/CEFCRMCompaniasCtrl')
	oCMenu1.makeMenu('CEF_MENU_CRM_PERSONAS','CEF_MENU_CRM_GESCON','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","PERSONAS") %>','/servlet/CEFCRMPersonasCtrl')
	oCMenu1.makeMenu('CEF_MENU_CRM_OPORTUNIDADES','CEF_MENU_CRM_GESCON','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","OPORTUNIDADES") %>','/servlet/CEFCRMOportunidadesCtrl')
oCMenu1.makeMenu('CEF_MENU_CRM_MERC','CEF_MENU_GLB_CRM','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","MERC") %>','')
oCMenu1.makeMenu('CEF_MENU_CRM_GESPRO','CEF_MENU_GLB_CRM','&nbsp;<%= JUtil.Msj("CEF","MENU","CRM","GESPRO") %>','')
<%
}
if(JUtil.getSesion(request).getPermiso("ADM"))
{
%>	
oCMenu1.makeMenu('CEF_MENU_GLB_ADM','','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","ADM") %>','')
oCMenu1.makeMenu('CEF_MENU_ADM_SALDOS','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","SALDOS") %>','/servlet/CEFAdmSaldosCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_USUARIOS','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","USUARIOS") %>','/servlet/CEFAdmUsuariosCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_ENTIDADES','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","ENTIDADES") %>','/servlet/CEFAdmEntidadesCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_VENDEDORES','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","VENDEDORES") %>','/servlet/CEFAdmVendedoresCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_CFDI','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","CFDI") %>','/servlet/CEFAdmCFDCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_PERIODOS','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","PERIODOS") %>','/servlet/CEFAdmPeriodosCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_MONEDAS','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","MONEDAS") %>','/servlet/CEFAdmMonedasCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_VARIABLES','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","VARIABLES") %>','/servlet/CEFAdmVariablesCtrl')
oCMenu1.makeMenu('CEF_MENU_ADM_FORMATOS','CEF_MENU_GLB_ADM','&nbsp;<%= JUtil.Msj("CEF","MENU","ADM","FORMATOS") %>','/servlet/CEFAdmFormatosCtrl')
<%
}
%>
oCMenu1.makeMenu('CEF_MENU_GLB_REP','','&nbsp;<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>','')
oCMenu1.makeMenu('CEF_MENU_REP_CONTABILIDAD','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","CONTABILIDAD") %>','/servlet/CEFReportesCtrl?tipo=CONT')
oCMenu1.makeMenu('CEF_MENU_REP_INVSERV','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","INVSERV") %>','/servlet/CEFReportesCtrl?tipo=INVSERV')
oCMenu1.makeMenu('CEF_MENU_REP_BANCAJ','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","BANCAJ") %>','/servlet/CEFReportesCtrl?tipo=BANCAJ')
oCMenu1.makeMenu('CEF_MENU_REP_ALM','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","ALM") %>','/servlet/CEFReportesCtrl?tipo=ALM')
oCMenu1.makeMenu('CEF_MENU_REP_COMP','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","COMP") %>','/servlet/CEFReportesCtrl?tipo=COMP')
oCMenu1.makeMenu('CEF_MENU_REP_VEN','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","VEN") %>','/servlet/CEFReportesCtrl?tipo=VEN')
oCMenu1.makeMenu('CEF_MENU_REP_PROD','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","PROD") %>','/servlet/CEFReportesCtrl?tipo=PROD')
oCMenu1.makeMenu('CEF_MENU_REP_NOM','CEF_MENU_GLB_REP','&nbsp;<%= JUtil.Msj("CEF","MENU","GLB","NOM") %>','/servlet/CEFReportesCtrl?tipo=NOM')

oCMenu1.makeStyle(); oCMenu1.construct()
</script-->
</body>
</html>
