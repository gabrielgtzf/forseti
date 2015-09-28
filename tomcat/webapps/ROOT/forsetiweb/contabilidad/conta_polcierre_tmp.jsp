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
<%@ page import="forseti.*, forseti.sets.*"%>
<html>
<head>
<link href="../../compfsi/ceftmp-coolmenus.css" rel="stylesheet" type="text/css">
<script language="JavaScript1.2" src="../../compfsi/coolmenus4.js"></script>
<script language="JavaScript1.2" src="../../compfsi/coolmenus4mccTmp.js">
</script>
<script language="JavaScript1.2">
function ventEm()
{
	parametrs = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1,width=250,height=150";
	ventana = window.open('../../forsetiweb/filtro_esp_por_mes.jsp?fsi_vista=contabilidad.JContaPolizasCtrl', '', parametrs);
	ventana.focus();
}
</script>
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<script>
<%
	JAdmPeriodosSet set = new JAdmPeriodosSet(request);
    set.setSQL("SELECT * FROM tbl_cont_catalogo_periodos where Mes = 13 order by Ano DESC LIMIT 6");
    set.Open();
	for(int i = 0; i < set.getNumRows(); i++)
	{
		String str = (set.getAbsRow(i).getCerrado()) ? " * " + Integer.toString(set.getAbsRow(i).getAno()) :  Integer.toString(set.getAbsRow(i).getAno());
%>
		oCMenu.makeMenu('top<%=i%>','','&nbsp;<%=str%>','/servlet/CEFContaPolizasCierreCtrl?tiempo=MAS&mes=<%=set.getAbsRow(i).getMes()%>&ano=<%=set.getAbsRow(i).getAno()%>','cuerpo')
<%
	}
%>

//Leave this line - it constructs the menu
oCMenu.construct()		
</script>
</body>
</html>
