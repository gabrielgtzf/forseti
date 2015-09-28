/*
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
*/
package fsi_admin.admon;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fsi_admin.JFsiForsetiApl;
// /usr/lib/jvm/java-7-openjdk-amd64/bin/keytool -genkeypair -keystore /usr/local/forseti/bin/forsetikeystore7 -dname "CN=127.0.0.1, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -keypass traby233 -storepass traby233 -keyalg RSA -alias forseti7 -ext SAN=dns:localhost,ip:127.0.0.1

@SuppressWarnings("serial")
public class JAdmSSLCtrl extends JFsiForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String adm_ssl = "";
      request.setAttribute("adm_ssl",adm_ssl);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADMIN_SSL"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_SSL");
    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_SSL","ASSL||||",mensaje);
          irApag("/forsetiadmin/caja_mensajes_vsta.jsp", request, response);
    	  return;
      }

      if(getSesion(request).getEst("ADMIN_SSL") == false)
      { 
    	  getSesion(request).EstablecerSAF("admin_ssl.png","ADMIN_SSL");
    	  getSesion(request).getSesion("ADMIN_SSL").setParametros("", "", "", "", "", "");
    	  getSesion(request).getSesion("ADMIN_SSL").setOrden(p(request.getParameter("etq")),"");
    	  getSesion(request).getSesion("ADMIN_SSL").setEspecial("");

    	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	  RDP("SAF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADMIN_SSL","ASSL||||","");
          irApag("/forsetiadmin/administracion/adm_ssl_vsta.jsp",request,response);
    	  return;
      }
       
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiadmin/administracion/adm_ssl_vsta.jsp", request, response);
      return;
    }

}
