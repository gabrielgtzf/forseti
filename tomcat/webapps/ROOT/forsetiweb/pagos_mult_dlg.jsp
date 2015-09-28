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
	
	JPublicBancosCuentasVsComprasSetV2 bc = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsComprasSetV2 cc = new JPublicBancosCuentasVsComprasSetV2(request);
	JPublicBancosCuentasVsVentasSetV2 bv = new JPublicBancosCuentasVsVentasSetV2(request);
	JPublicBancosCuentasVsVentasSetV2 cv = new JPublicBancosCuentasVsVentasSetV2(request);

	if(request.getParameter("va_tipo").equals("compras"))
	{
		bc.m_OrderBy = "Clave ASC";
		bc.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		bc.Open();
		cc.m_OrderBy = "Clave ASC";
		cc.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		cc.Open();
	}
	else // es ventas
	{
		bv.m_OrderBy = "Clave ASC";
		bv.m_Where = "Tipo = '0' and ID_EntidadVenta = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		bv.Open();
		cv.m_OrderBy = "Clave ASC";
		cv.m_Where = "Tipo = '1' and ID_EntidadVenta = '" + JUtil.p(request.getParameter("va_ident")) + "'";
		cv.Open();
	}
	
	JSatBancosSet setBan = new JSatBancosSet(request);
    setBan.m_OrderBy = "Clave ASC";
    setBan.Open();
	
	JSatMetodosPagoSet setMet = new JSatMetodosPagoSet(request);
    setMet.m_OrderBy = "Clave ASC";
    setMet.Open();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">

.dropcontent{
	display:block;
}

.tabstyle{
	cursor: pointer;
	cursor: hand;
	width:130px;
	font: bold 12px Arial;
	background-color: #666666;
	border-top: 1px solid gray;
	border-right: 3px outset white;
}

.tabstyle b{
	color: white;
	margin-left: 3px;
	margin-right: 23px;
}

#cyclelinks2{
	margin-right:15px;
}

#cyclelinks2 .tabsfootstyle{
	font-weight:bold;
	text-decoration:none;
	background-color:#666666;
	color:white;
	margin-right:3px;
}
</style>
<script language="JavaScript1.2" src="../compfsi/comps.js"></script>
<script language="JavaScript" type="text/javascript">
<!--
// Content Tabs Script- By JavaScriptKit.com (http://www.javascriptkit.com)
// For this and over 400+ free scripts, visit JavaScript Kit- http://www.javascriptkit.com/
// This notice must stay intact for use
if (window.addEventListener || window.attachEvent)
{
	document.write('<style type="text/css">\n')
	document.write('.dropcontent{display:none;}\n')
	document.write('</style>\n')
}

// Content Tabs script- By JavaScriptKit.com (http://www.javascriptkit.com)
// Last updated: July 25th, 05'

var names=new Array(<%
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{
		    	out.print("'" + cc.getAbsRow(i).getCuenta() + "',"); 
		    } 
		}
		if(bc.getNumRows() > 0)
		{
           	 for(int i = 0; i< bc.getNumRows(); i++)
			{
            	out.print("'" + bc.getAbsRow(i).getCuenta() + "',");
			}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
		if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{
		    	out.print("'" + cv.getAbsRow(i).getCuenta() + "',");
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{
		    	out.print("'" + bv.getAbsRow(i).getCuenta() + "',");
			}
		}
	}
%>'')

var showrecords=1 //specify number of contents to show per tab
var tabhighlightcolor="#CCCCCC" //specify tab color when selected
var taboriginalcolor="#666666" //specify default tab color. Should echo your CSS file definition.

////Stop editing here//////////////////////////////////////

document.getElementsByClass=function(tag, classname)
{
	var tagcollect=document.all? document.all.tags(tag): document.getElementsByTagName(tag) //IE5 workaround
	var filteredcollect=new Array()
	var inc=0
	for (i=0;i<tagcollect.length;i++)
	{
		if (tagcollect[i].className==classname)
			filteredcollect[inc++]=tagcollect[i]
	}
	return filteredcollect
}


function contractall()
{
	var inc=0
	while (contentcollect[inc])
	{
		contentcollect[inc].style.display="none"
		inc++
	}
}

function expandone(whichpage)
{
	var lowerbound=(whichpage-1)*showrecords
	var upperbound=(tabstocreate==whichpage)? contentcollect.length-1 : lowerbound+showrecords-1
	contractall()
	for (i=lowerbound;i<=upperbound;i++)
		contentcollect[i].style.display="block"
}

function highlightone(whichtab)
{
	for (i=0;i<tabscollect.length;i++)
	{
		tabscollect[i].style.backgroundColor=taboriginalcolor
		tabscollect[i].style.borderRightColor="white"
		tabsfootcollect[i].style.backgroundColor="#666666"
	}
	tabscollect[whichtab].style.backgroundColor=tabhighlightcolor
	tabscollect[whichtab].style.borderRightColor="gray"
	tabsfootcollect[whichtab].style.backgroundColor="#999999"
}

function generatetab()
{
	contentcollect=document.getElementsByClass("p", "dropcontent")
	tabstocreate=Math.ceil(contentcollect.length/showrecords)
	linkshtml=""
	linkshtml2=""
	for (i=1;i<=tabstocreate;i++)
	{
		linkshtml+='<span class="tabstyle" onClick="expandone('+i+');highlightone('+(i-1)+')"><b>'+names[i-1]+'</b></span>'
		linkshtml2+='<a href="#" class="tabsfootstyle" onClick="expandone('+i+');highlightone('+(i-1)+');return false">'+names[i-1]+'</a> '
	}
	document.getElementById("cyclelinks").innerHTML=linkshtml
	document.getElementById("cyclelinks2").innerHTML=linkshtml2
	tabscollect=document.getElementsByClass("span", "tabstyle")
	tabsfootcollect=document.getElementsByClass("a", "tabsfootstyle")
	highlightone(0)
	expandone(1)
}

if (window.addEventListener)
	window.addEventListener("load", generatetab, false)
else if (window.attachEvent)
	window.attachEvent("onload", generatetab)

// ///////////////////////////////////////////////////////////////////////////
// Funciones de forseti
// ----------------------
window.moveTo(0, 0);
window.resizeTo(screen.availWidth, screen.availHeight);
<%
	if(request.getParameter("va_proc").equals("retiro"))
	{
%>
function establecerCheque(numchq, selBan, depchq, metpagopol)
{
	var ind = metpagopol.selectedIndex
	
	if(ind == 1) // 1 es el valor del 02 cheque
	{
		if(selBan.substring(0,7) == 'FSI_BAN')
		{
			depchq.value = numchq;
		}
		else if(selBan.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{
			depchq.value = '0';
		}
	}
	else
	{
		if(selBan.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{	
			depchq.value = '0';
		}
	}
}
<%
	}
	else // es deposito
	{
%>
function establecerRefer(selBan, depchq, metpagopol)
{
	var ind = metpagopol.selectedIndex
	
	if(ind == 1) // 1 es el valor del 02 cheque
	{
		if(selBan.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
	}
	else
	{
		if(selBan.substring(0,4) != 'FSI_')
		{
			depchq.value = '0';
		}
		else
		{	
			depchq.value = '0';
		}
	}
}
<%
	}
%>
////////////////////////////////////////////////////////////////////////////////
function calcularResultados()
{
	// Ahora revisa que el total sea igual al gran total
	var gran_total = 0.0;
	var cajas_total = 0.0;
	var cajas_efec = 0.0;
	var cambio_total = 0.0;
	var bancos_total = 0.0;
<%	
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value);
		cajas_total += parseFloat(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value);
		cajas_efec += parseFloat(document.pagos.FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>.value);
		cambio_total += parseFloat(document.pagos.FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>.value) - parseFloat(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value);
	}
<%
			}
		}	
		if(bc.getNumRows() > 0)
		{
           	for(int i = 0; i< bc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value);
		bancos_total += parseFloat(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value);
    }
<%
			}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
	    if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value);
		cajas_total += parseFloat(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value);
		cajas_efec += parseFloat(document.pagos.FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>.value);
    }  
<%
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value);
		bancos_total += parseFloat(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value);
	}
<%
			}
		}
	}
%>
	document.getElementById("total_cajas").innerHTML = cajas_total;
	document.getElementById("total_bancos").innerHTML = bancos_total;
	document.getElementById("gran_total").innerHTML = gran_total;
	document.getElementById("total_efectivo").innerHTML = cajas_efec;
	document.getElementById("total_cambio").innerHTML = cambio_total;
}  
/////////////////////////////////////////////////////////////////
function transferirResultados()
{
/////////////////////////////////////////////////////
<%
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value != '')
	{
		if(	!esNumeroDecimal("Cantidad en <%= cc.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2) ||
			!esNumeroDecimal("Efectivo en <%= cc.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2) )
			return;
	}		
<%
			}
		}	
		if(bc.getNumRows() > 0)
		{
           	for(int i = 0; i< bc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value != '')
	{
		if(	!esNumeroDecimal("Cantidad en <%= bc.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2)  )
			return;
	}
<%
			}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
	    if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value != '')
	{
		if(	!esNumeroDecimal("Cantidad en <%= cv.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2) ||
			!esNumeroDecimal("Efectivo en <%= cv.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2) )
			return;
	}
<%
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value != '')
	{
		if(	!esNumeroDecimal("Cantidad en <%= bv.getAbsRow(i).getCuenta() %>:", document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value, 0, 9999999999, 2) )
			return;
	}
<%
			}
		}
	}
	// AHORA SIGUEN LOS TOTALES
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{ 
%>
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value;
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_REF_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_REF_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_METPAGOPOL_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_METPAGOPOL_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_DEPCHQ_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_DEPCHQ_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_CUENTABANCO_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_CUENTABANCO_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_ID_SATBANCO_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_ID_SATBANCO_<%= cc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_BANCOEXT_<%= cc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_BANCOEXT_<%= cc.getAbsRow(i).getClave() %>.value;			

<%
			}
		}	
		if(bc.getNumRows() > 0)
		{
       		for(int i = 0; i< bc.getNumRows(); i++)
			{ 
%>
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value;
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_REF_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_REF_<%= bc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_METPAGOPOL_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_METPAGOPOL_<%= bc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_DEPCHQ_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_DEPCHQ_<%= bc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_CUENTABANCO_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_CUENTABANCO_<%= bc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_ID_SATBANCO_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_ID_SATBANCO_<%= bc.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_BANCOEXT_<%= bc.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_BANCOEXT_<%= bc.getAbsRow(i).getClave() %>.value;			
<% 
	   		}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
	    if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value;
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_REF_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_REF_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_METPAGOPOL_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_METPAGOPOL_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_DEPCHQ_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_DEPCHQ_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_CUENTABANCO_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_CUENTABANCO_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_ID_SATBANCO_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_ID_SATBANCO_<%= cv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_CAJ_BANCOEXT_<%= cv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_CAJ_BANCOEXT_<%= cv.getAbsRow(i).getClave() %>.value;			

<%
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value;
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_REF_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_REF_<%= bv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_METPAGOPOL_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_METPAGOPOL_<%= bv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_DEPCHQ_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_DEPCHQ_<%= bv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_CUENTABANCO_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_CUENTABANCO_<%= bv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_ID_SATBANCO_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_ID_SATBANCO_<%= bv.getAbsRow(i).getClave() %>.value;			
	opener.document.<%= request.getParameter("formul") %>.FSI_BAN_BANCOEXT_<%= bv.getAbsRow(i).getClave() %>.value = document.pagos.FSI_BAN_BANCOEXT_<%= bv.getAbsRow(i).getClave() %>.value;			
<% 		
	   		}
		}
	}
%>	
	// Ahora revisa que el total sea igual al gran total
	var gran_total = 0.0;
	var cajas_total = 0.0;
	var cajas_efec = 0.0;
	var bancos_total = 0.0;
<%	
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value);
		cajas_total += parseFloat(document.pagos.FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>.value);
		cajas_efec += parseFloat(document.pagos.FSI_CAJ_REF_<%= cc.getAbsRow(i).getClave() %>.value);
	}
<%
			}
		}	
		if(bc.getNumRows() > 0)
		{
           	for(int i = 0; i< bc.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value);
		bancos_total += parseFloat(document.pagos.FSI_BAN_<%= bc.getAbsRow(i).getClave() %>.value);
    }
<%
			}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
	    if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value);
		cajas_total += parseFloat(document.pagos.FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>.value);
		cajas_efec += parseFloat(document.pagos.FSI_CAJ_REF_<%= cv.getAbsRow(i).getClave() %>.value);
    }  
<%
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
	if(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value != '')
	{
		gran_total += parseFloat(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value);
		bancos_total += parseFloat(document.pagos.FSI_BAN_<%= bv.getAbsRow(i).getClave() %>.value);
	}
<%
			}
		}
	}
%>
/////////////////////////////////////////////////////
	if(redondear(gran_total,2) != <%= ( request.getParameter("va_idmon").equals("1") ? request.getParameter("va_total") :  request.getParameter("va_cantidad")  ) %>)
	{
		alert("ERROR: La suma del pago de todas las entidades " + redondear(gran_total,2) + ", es diferente al total del registro <%=  ( request.getParameter("va_idmon").equals("1") ? request.getParameter("va_total")  + " M.N." :  request.getParameter("va_cantidad") + " M.E." )  %>. Este debe de ser igual. Corrígelo para poder guardar el registro");
		return;
	}
	if(cajas_total > cajas_efec)
	{
		alert("ERROR: La cantidad en efectivo no puede ser menor al pago en efectivo.");
		return;
	}
	opener.document.<%= request.getParameter("formul") %>.fsipg_cambio.value = redondear((cajas_efec - cajas_total),2);
	opener.document.<%= request.getParameter("formul") %>.fsipg_efectivo.value = redondear(cajas_efec,2);
	
	window.close();
	opener.document.<%= request.getParameter("formul") %>.submit();
}
-->
</script>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form name="pagos" action="<%= request.getRequestURI() %>" method="get" target="_self">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><input name="tiempo" type="hidden" value="MAS"><%= JUtil.Msj("GLB","GLB","GLB","PAGOS-JSP") %></td>
  </tr>
  <tr> 
    <td align="right">&nbsp;
		
	</td>
  </tr>
  <tr> 
    <td valign="top"> 
     	<table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr> 
                        <td width="35%" class="titCuerpoNeg">
						  <%= ((request.getParameter("va_proc").equals("retiro")) ? "Total del Cargo:" : "Total del Abono:" ) %></td>
                        <td class="titCuerpoAzc"><%= ( request.getParameter("va_idmon").equals("1") ? request.getParameter("va_total")  + " M.N." :  request.getParameter("va_cantidad") + " M.E." ) %></td>
                      </tr>
					  <tr>
					  	<td colspan="2" bgcolor="#CCCCCC">
					  		<div id="cyclelinks"></div>
<%
	if(request.getParameter("va_tipo").equals("compras"))
	{
		if(cc.getNumRows() > 0)
		{
			for(int i = 0; i< cc.getNumRows(); i++)
			{ 
%>
 					 		 <p class="dropcontent">
					  			<table width="100%" border="0" cellspacing="2" cellpadding="0">
					  				<tr> 
                        				<td width="15%" class="titChicoNeg"><%= ((request.getParameter("va_proc").equals("retiro")) ? "Retiro:" : "Depósito:" ) %></td>
                        				<td class="titChico"><input name="FSI_CAJ_<%= cc.getAbsRow(i).getClave() %>" type="text" value="" size="15" maxlength="20" onBlur="javascript:calcularResultados();"></td>
										<td width="15%" class="titChicoNeg">Efectivo:</td>
                        			  	<td class="titChico"><input name="FSI_CAJ_EFECTIVO_<%= cc.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="" onBlur="javascript:calcularResultados();"></td>
                      					<td width="15%" class="titChicoNeg">Referencia:</td>
	                       			  	<td class="titChico"><input name="FSI_CAJ_REF_<%= cc.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value=""></td>
									</tr>
									<tr> 
                        			 	<td colspan="6">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											  <tr> 
												<td width="25%">Metodo</td>
												<td width="7%"><%= request.getParameter("va_proc").equals("deposito") ? "Cheque Recibido" : "Cheque Emitido" %></td>
												<td><%= request.getParameter("va_proc").equals("deposito") ? "Cuenta Origen" : "Cuenta Destino" %></td>
												<td width="25%">Banco Nacional</td>
												<td width="25%">Banco Extranjero</td>
											  </tr>
											  <tr> 
												<td><select style="width: 90%;" name="FSI_CAJ_METPAGOPOL_<%= cc.getAbsRow(i).getClave() %>" class="cpoBco"
							<% 
								if(request.getParameter("va_proc").equals("retiro")) { 
									out.print(" onChange=\"javascript:establecerCheque('0','FSI_CAJ_" + cc.getAbsRow(i).getClave() + "', this.form.FSI_CAJ_DEPCHQ_" + cc.getAbsRow(i).getClave() + ", this.form.FSI_CAJ_METPAGOPOL_" + cc.getAbsRow(i).getClave() + ");\""); } else { 
									out.print(" onChange=\"javascript:establecerRefer('FSI_CAJ_" + cc.getAbsRow(i).getClave() + "', this.form.FSI_CAJ_DEPCHQ_" + cc.getAbsRow(i).getClave() + ", this.form.FSI_CAJ_METPAGOPOL_" + cc.getAbsRow(i).getClave() + ");\""); } %>>
						<%
								for(int m = 0; m < setMet.getNumRows(); m++)
								{	
						%>
													<option value="<%=setMet.getAbsRow(m).getClave()%>"><%= setMet.getAbsRow(m).getNombre() %></option>
						<%	
								}
						%>				
													</select></td>
												<td><input name="FSI_CAJ_DEPCHQ_<%= cc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="25" value="0"<%= request.getParameter("va_proc").equals("deposito") ? "" : " readOnly='true'" %>></td>
												<td><input name="FSI_CAJ_CUENTABANCO_<%= cc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="50"></td>
												<td><select style="width: 90%;" name="FSI_CAJ_ID_SATBANCO_<%= cc.getAbsRow(i).getClave() %>" class="cpoBco">
						<%
								for(int b = 0; b < setBan.getNumRows(); b++)
								{	
						%>
													<option value="<%=setBan.getAbsRow(b).getClave()%>"><%= setBan.getAbsRow(b).getNombre() %></option>
						<%	
								}
						%>
												  </select>
												</td>
												<td><input name="FSI_CAJ_BANCOEXT_<%= cc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="150"></td>
											  </tr>
											</table>
										</td>
                      				</tr>
								</table>
					  		</p>
<%
			}
		}	
		if(bc.getNumRows() > 0)
		{
           	for(int i = 0; i< bc.getNumRows(); i++)
			{ 
%>
					 		 <p class="dropcontent">
					  			<table width="100%" border="0" cellspacing="2" cellpadding="0">
					  				<tr> 
                        				<td width="15%" class="titChicoNeg"><%= ((request.getParameter("va_proc").equals("retiro")) ? "Retiro:" : "Depósito:" ) %></td>
                        				<td class="titChico"><input name="FSI_BAN_<%= bc.getAbsRow(i).getClave() %>" type="text" value="" size="15" maxlength="20" onBlur="javascript:calcularResultados();"></td>
										<td width="15%" class="titChicoNeg">Referencia:</td>
                        			 	<td class="titChico"><input name="FSI_BAN_REF_<%= bc.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="" ></td>
							  		</tr>
									<tr>
										<td colspan="4">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											  <tr> 
												<td width="25%">Metodo</td>
												<td width="7%"><%= request.getParameter("va_proc").equals("deposito") ? "Cheque Recibido" : "Cheque Emitido" %></td>
												<td><%= request.getParameter("va_proc").equals("deposito") ? "Cuenta Origen" : "Cuenta Destino" %></td>
												<td width="25%">Banco Nacional</td>
												<td width="25%">Banco Extranjero</td>
											  </tr>
											  <tr> 
												<td><select style="width: 90%;" name="FSI_BAN_METPAGOPOL_<%= bc.getAbsRow(i).getClave() %>" class="cpoBco"
							<%
								if(request.getParameter("va_proc").equals("retiro")) { 
									out.print(" onChange=\"javascript:establecerCheque('" + bc.getAbsRow(i).getSigCheque() + "','FSI_BAN_" + bc.getAbsRow(i).getClave() + "', this.form.FSI_BAN_DEPCHQ_" + bc.getAbsRow(i).getClave() + ", this.form.FSI_BAN_METPAGOPOL_" + bc.getAbsRow(i).getClave() + ");\""); } else { 
									out.print(" onChange=\"javascript:establecerRefer('FSI_BAN_" + bc.getAbsRow(i).getClave() + "', this.form.FSI_BAN_DEPCHQ_" + bc.getAbsRow(i).getClave() + ", this.form.FSI_BAN_METPAGOPOL_" + bc.getAbsRow(i).getClave() + ");\""); } %>>
						<%
								for(int m = 0; m < setMet.getNumRows(); m++)
								{	
						%>
													<option value="<%=setMet.getAbsRow(m).getClave()%>"><%= setMet.getAbsRow(m).getNombre() %></option>
						<%	
								}
						%>				
													</select></td>
												<td><input name="FSI_BAN_DEPCHQ_<%= bc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="25" value="0"<%= request.getParameter("va_proc").equals("deposito") ? "" : " readOnly='true'" %>></td>
												<td><input name="FSI_BAN_CUENTABANCO_<%= bc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="50"></td>
												<td><select style="width: 90%;" name="FSI_BAN_ID_SATBANCO_<%= bc.getAbsRow(i).getClave() %>" class="cpoBco">
						<%
								for(int b = 0; b < setBan.getNumRows(); b++)
								{	
						%>
													<option value="<%=setBan.getAbsRow(b).getClave()%>"><%= setBan.getAbsRow(b).getNombre() %></option>
						<%	
								}
						%>
												  </select>
												</td>
												<td><input name="FSI_BAN_BANCOEXT_<%= bc.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="150"></td>
											  </tr>
											</table>
										</td>
									</tr>
								</table>
					  		</p>
<%
			}
		}
	}
	else // es ventas ///////////////////////////////////////////////////////////////////////////////////////////
	{
	    if(cv.getNumRows() > 0)
		{
			for(int i = 0; i< cv.getNumRows(); i++)
			{ 
%>
					 		 <p class="dropcontent">
					  			<table width="100%" border="0" cellspacing="2" cellpadding="0">
					  				<tr> 
                        				<td width="15%" class="titChicoNeg"><%= ((request.getParameter("va_proc").equals("retiro")) ? "Retiro:" : "Depósito:" ) %></td>
                        				<td class="titChico"><input name="FSI_CAJ_<%= cv.getAbsRow(i).getClave() %>" type="text" value="" size="15" maxlength="20" onBlur="javascript:calcularResultados();"></td>
										<td width="15%" class="titChicoNeg">Efectivo:</td>
                        			  	<td class="titChico"><input name="FSI_CAJ_EFECTIVO_<%= cv.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="" onBlur="javascript:calcularResultados();"></td>
                      					<td width="15%" class="titChicoNeg">Referencia:</td>
	                       			  	<td class="titChico"><input name="FSI_CAJ_REF_<%= cv.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value=""></td>
									</tr>
									<tr> 
                        			 	<td colspan="6">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											  <tr> 
												<td width="25%">Metodo</td>
												<td width="7%"><%= request.getParameter("va_proc").equals("deposito") ? "Cheque Recibido" : "Cheque Emitido" %></td>
												<td><%= request.getParameter("va_proc").equals("deposito") ? "Cuenta Origen" : "Cuenta Destino" %></td>
												<td width="25%">Banco Nacional</td>
												<td width="25%">Banco Extranjero</td>
											  </tr>
											  <tr> 
												<td><select style="width: 90%;" name="FSI_CAJ_METPAGOPOL_<%= cv.getAbsRow(i).getClave() %>" class="cpoBco"
						<%
							 	if(request.getParameter("va_proc").equals("retiro")) { 
									out.print(" onChange=\"javascript:establecerCheque('0','FSI_CAJ_" + cv.getAbsRow(i).getClave() + "', this.form.FSI_CAJ_DEPCHQ_" + cv.getAbsRow(i).getClave() + ", this.form.FSI_CAJ_METPAGOPOL_" + cv.getAbsRow(i).getClave() + ");\""); } else {
									out.print(" onChange=\"javascript:establecerRefer('FSI_CAJ_" + cv.getAbsRow(i).getClave() + "', this.form.FSI_CAJ_DEPCHQ_" + cv.getAbsRow(i).getClave() + ", this.form.FSI_CAJ_METPAGOPOL_" + cv.getAbsRow(i).getClave() + ");\""); } %>>
						<%
								for(int m = 0; m < setMet.getNumRows(); m++)
								{	
						%>
													<option value="<%=setMet.getAbsRow(m).getClave()%>"><%= setMet.getAbsRow(m).getNombre() %></option>
						<%	
								}
						%>				
													</select></td>
												<td><input name="FSI_CAJ_DEPCHQ_<%= cv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="25" value="0"<%= request.getParameter("va_proc").equals("deposito") ? "" : " readOnly='true'" %>></td>
												<td><input name="FSI_CAJ_CUENTABANCO_<%= cv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="50"></td>
												<td><select style="width: 90%;" name="FSI_CAJ_ID_SATBANCO_<%= cv.getAbsRow(i).getClave() %>" class="cpoBco">
						<%
								for(int b = 0; b < setBan.getNumRows(); b++)
								{	
						%>
													<option value="<%=setBan.getAbsRow(b).getClave()%>"><%= setBan.getAbsRow(b).getNombre() %></option>
						<%	
								}
						%>
												  </select>
												</td>
												<td><input name="FSI_CAJ_BANCOEXT_<%= cv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="150"></td>
											  </tr>
											</table>
										</td>
                      				</tr>
								</table>
					  		</p>
<%
			}
		}
		if(bv.getNumRows() > 0)
		{
			for(int i = 0; i< bv.getNumRows(); i++)
			{ 
%>
					 		 <p class="dropcontent">
					  			<table width="100%" border="0" cellspacing="2" cellpadding="0">
					  				<tr> 
                        				<td width="15%" class="titChicoNeg"><%= ((request.getParameter("va_proc").equals("retiro")) ? "Retiro:" : "Depósito:" ) %></td>
                        				<td class="titChico"><input name="FSI_BAN_<%= bv.getAbsRow(i).getClave() %>" type="text" value="" size="15" maxlength="20" onBlur="javascript:calcularResultados();"></td>
										<td width="15%" class="titChicoNeg">Referencia:</td>
                        			 	<td class="titChico"><input name="FSI_BAN_REF_<%= bv.getAbsRow(i).getClave() %>" type="text" size="15" maxlength="20" value="" ></td>
							  		</tr>
									<tr>
										<td colspan="4">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											  <tr> 
												<td width="25%">Metodo</td>
												<td width="7%"><%= request.getParameter("va_proc").equals("deposito") ? "Cheque Recibido" : "Cheque Emitido" %></td>
												<td><%= request.getParameter("va_proc").equals("deposito") ? "Cuenta Origen" : "Cuenta Destino" %></td>
												<td width="25%">Banco Nacional</td>
												<td width="25%">Banco Extranjero</td>
											  </tr>
											  <tr> 
												<td><select style="width: 90%;" name="FSI_BAN_METPAGOPOL_<%= bv.getAbsRow(i).getClave() %>" class="cpoBco"
							<%
								if(request.getParameter("va_proc").equals("retiro")) {
									out.print(" onChange=\"javascript:establecerCheque('" + bv.getAbsRow(i).getSigCheque() + "','FSI_BAN_" + bv.getAbsRow(i).getClave() + "', this.form.FSI_BAN_DEPCHQ_" + bv.getAbsRow(i).getClave() + ", this.form.FSI_BAN_METPAGOPOL_" + bv.getAbsRow(i).getClave() + ");\""); } else {
									out.print(" onChange=\"javascript:establecerRefer('FSI_BAN_" + bv.getAbsRow(i).getClave() + "', this.form.FSI_BAN_DEPCHQ_" + bv.getAbsRow(i).getClave() + ", this.form.FSI_BAN_METPAGOPOL_" + bv.getAbsRow(i).getClave() + ");\""); } %>>
						<%
								for(int m = 0; m < setMet.getNumRows(); m++)
								{	
						%>
													<option value="<%=setMet.getAbsRow(m).getClave()%>"><%= setMet.getAbsRow(m).getNombre() %></option>
						<%	
								}
						%>				
													</select></td>
												<td><input name="FSI_BAN_DEPCHQ_<%= bv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="25" value="0"<%= request.getParameter("va_proc").equals("deposito") ? "" : " readOnly='true'" %>></td>
												<td><input name="FSI_BAN_CUENTABANCO_<%= bv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="50"></td>
												<td><select style="width: 90%;" name="FSI_BAN_ID_SATBANCO_<%= bv.getAbsRow(i).getClave() %>" class="cpoBco">
						<%
								for(int b = 0; b < setBan.getNumRows(); b++)
								{	
						%>
													<option value="<%=setBan.getAbsRow(b).getClave()%>"><%= setBan.getAbsRow(b).getNombre() %></option>
						<%	
								}
						%>
												  </select>
												</td>
												<td><input name="FSI_BAN_BANCOEXT_<%= bv.getAbsRow(i).getClave() %>" type="text" style="width: 90%;" maxlength="150"></td>
											  </tr>
											</table>
										</td>
									</tr>
								</table>
					  		</p>
<%
			}
		}
	}
%>
					   		<div id="cyclelinks2" align="right"></div>
							<br>						
						</td>
					  </tr>
					 
         </table>

	</td>
  </tr>
  <tr>
	<td align="right">
		<table width="50%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td class="titCuerpoNeg">Efectivo</td>
			<td><p align="right" class="titCuerpoAzc" id="total_efectivo"></p></td>
		  </tr>
		  <tr>
			<td class="titCuerpoNeg">Total</td>
			<td><p align="right" class="titCuerpoAzc" id="total_cajas"></p></td>
		  </tr>
		  <tr>
			<td class="titCuerpoNeg">Cambio</td>
			<td><p align="right" class="titCuerpoAzc" id="total_cambio"></p></td>
		  </tr>
		  <tr>
			<td class="titCuerpoNeg">Bancos</td>
			<td><p align="right" class="titCuerpoAzc" id="total_bancos"></p></td>
		  </tr>
		  <tr>
			<td class="titCuerpoNeg">Pago</td>
			<td><p align="right" class="titCuerpoAzc" id="gran_total"></p></td>
		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="right">
		<input type="button" name="aceptar" onClick="javascript:transferirResultados();" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    	<input type="button" name="cancelar" onClick="javascript:window.close()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
	</td>
  </tr>
  <!--tr>
  	<td align="right">
		<table width="50%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td class="titGiganteAzc">Total:</td>
			<td align="right" class="cefPago"><%= ( request.getParameter("va_idmon").equals("1") ? request.getParameter("va_total") : request.getParameter("va_cantidad") ) %></td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		  </tr>
		</table>

  	</td>
  </tr-->
</table>
</form>
</body>
</html>
