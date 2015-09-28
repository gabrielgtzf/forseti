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
<%@ page language="java" 
	import="net.sourceforge.wurfl.core.Device,net.sourceforge.wurfl.core.MarkUp,net.sourceforge.wurfl.core.WURFLHolder,net.sourceforge.wurfl.core.WURFLManager,net.sourceforge.wurfl.core.exc.CapabilityNotDefinedException" %>
<%
		String jspView = "";
		
		WURFLHolder wurfl = (WURFLHolder) getServletContext().getAttribute(WURFLHolder.class.getName());
		WURFLManager manager = wurfl.getWURFLManager();
		Device device = manager.getDeviceForRequest(request);
		
		boolean esMobil = device.getCapabilityAsBool("is_wireless_device");
		MarkUp markUp = device.getMarkUp();
		boolean XHTML_ADVANCED = (MarkUp.XHTML_ADVANCED.equals(markUp)) ? true : false; 
		//boolean esSmartphone = device.getVirtualCapability("is_smartphone").equals("true") ? true : false;
       	//boolean esIPhone_os = device.getVirtualCapability("is_iphone_os").equals("true") ? true : false;
       	//boolean esAndroid = device.getVirtualCapability("is_android").equals("true") ? true : false;
		boolean ajax_support_javascript = device.getCapability("ajax_support_javascript").equals("true") ? true : false;
				
		if(XHTML_ADVANCED && ajax_support_javascript) // Soporta xhtml avanzado y javascript
		{
			if(!esMobil) // navegador generico pc portail etc. 
			{		
				response.sendRedirect("../forsetiadmin/forseti.html");
				return;
			}
			else
			{
				if (XHTML_ADVANCED) 
					jspView = "XHTML_ADVANCED";
				else if (MarkUp.XHTML_SIMPLE.equals(markUp)) 
					jspView = "XHTML_SIMPLE";
				else if (MarkUp.CHTML.equals(markUp)) 
					jspView = "CHTML";
				else if (MarkUp.WML.equals(markUp)) 
					jspView = "WML";
				else
					jspView = "";
			}
		}
		else
		{
			if (XHTML_ADVANCED) 
				jspView = "XHTML_ADVANCED";
			else if (MarkUp.XHTML_SIMPLE.equals(markUp)) 
				jspView = "XHTML_SIMPLE";
			else if (MarkUp.CHTML.equals(markUp)) 
				jspView = "CHTML";
			else if (MarkUp.WML.equals(markUp)) 
				jspView = "WML";
			else
				jspView = "";
		}
		
		
		if(jspView.equals("XHTML_ADVANCED"))
		{
			response.setContentType("text/html"); 
%>
<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	   <title>Forseti XHTML AVANZADO</title>
	</head>
	
	<body>
		<h1>XHTML AVANZADO sin soporte de JavaScript no es apto para Forseti</h1>
		<p>Device: <%= device.getId() %></p>
		<p><b>Capacidades Virtuales:</b></p>
		<p>Es smartphone: <%= device.getVirtualCapability("is_smartphone") %></p>
       	<p>Es iPhone OS: <%= device.getVirtualCapability("is_iphone_os") %></p>
       	<p>Es Android: <%= device.getVirtualCapability("is_android") %></p>
	</body>
</html>	
<%
		}
		else if(jspView.equals("CHTML"))
		{
%>		
<html>
	<head>
	   <title>Forseti CHTML</title>
	</head>
	<body>
		<h1>CHTML no es apto para Forseti</h1>
		<p>Device: <%= device.getId() %></p>
		<p><b>Capacidades Virtuales:</b></p>
		<p>Es smartphone: <%= device.getVirtualCapability("is_smartphone") %></p>
       	<p>Es iPhone OS: <%= device.getVirtualCapability("is_iphone_os") %></p>
       	<p>Es Android: <%= device.getVirtualCapability("is_android") %></p>
	</body>
</html>	
<%
		}
		else if(jspView.equals("XHTML_SIMPLE"))
		{
			response.setContentType("text/html"); 
%>
<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	   <title>Forseti XHTML SIMPLE</title>
	</head>
	<body>
		<h1>XHTML SIMPLE no es apto para Forseti</h1>
		<p>Device: <%= device.getId() %></p>
		<p><b>Capacidades Virtuales:</b></p>
		<p>Es smartphone: <%= device.getVirtualCapability("is_smartphone") %></p>
       	<p>Es iPhone OS: <%= device.getVirtualCapability("is_iphone_os") %></p>
       	<p>Es Android: <%= device.getVirtualCapability("is_android") %></p>
	</body>
</html>
<%
		}
		else if(jspView.equals("WML"))
		{
			response.setContentType("text/vnd.wap.wml");
%>
<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
    <card id="w" title="Forseti WML">
        <p>
            <br/><b>WML no apto para Forseti</b><br />
			<p>Device: <%= device.getId() %></p>
			<p><b>Capacidades Virtuales:</b></p>
			<p>Es smartphone: <%= device.getVirtualCapability("is_smartphone") %></p>
       		<p>Es iPhone OS: <%= device.getVirtualCapability("is_iphone_os") %></p>
       		<p>Es Android: <%= device.getVirtualCapability("is_android") %></p>
        </p>
    </card>
</wml>
<%
		}
		else // No sabe que soporta
			return;
%>			
