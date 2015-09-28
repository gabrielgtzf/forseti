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
package forseti.contabilidad;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JUtil;
import forseti.sets.JAdmPeriodosSet;

@SuppressWarnings("serial")
public class JContaPolizasCierreCtrl extends JForsetiApl
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

      String conta_polcierre = "";
      request.setAttribute("conta_polcierre",conta_polcierre);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("CONT_POLCIERRE"))
      {
        idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_POLCIERRE");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_POLCIERRE","POLC||||",mensaje);
        irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        return;
      }

      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("CONT_POLCIERRE") == false)
      {
    	  JAdmPeriodosSet setf = new JAdmPeriodosSet(request);
          setf.setSQL("SELECT * FROM tbl_cont_catalogo_periodos where Mes = 13 order by Ano desc, Mes Desc LIMIT 1 ");
          setf.Open();
          
          if(setf.getNumRows() < 1)
          {
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-MODULO", 1);
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
        	  return;
          }
          
          String Tiempo = "Mes = " + setf.getAbsRow(0).getMes() + " AND Ano = " + setf.getAbsRow(0).getAno();
          String TiempoTit = Integer.toString(setf.getAbsRow(0).getAno());

          getSesion(request).EstablecerCEF(request, "cont_polcierre.png", "CONT_POLCIERRE");
          getSesion(request).getSesion("CONT_POLCIERRE").setParametros("", Tiempo, "", "", TiempoTit, "");
          getSesion(request).getSesion("CONT_POLCIERRE").setOrden(p(request.getParameter("etq")),"");
          getSesion(request).getSesion("CONT_POLCIERRE").setEspecial(TiempoTit);

          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"CONT_POLCIERRE","POLC||||","");
          irApag("/forsetiweb/contabilidad/conta_polcierre_vsta.jsp",request,response);
          return;
      }

      if(request.getParameter("tiempo") != null && !request.getParameter("tiempo").equals(""))
      {
    	  ///////////////////////////////////////////////////////////////
	  	  if(request.getParameter("tiempo").equals("MAS"))
		  {
			  if(request.getParameter("mes") != null && request.getParameter("ano") != null && !request.getParameter("mes").equals("") && !request.getParameter("ano").equals(""))
			  {
				  int mes;
				  int ano;
	
				  try 
				  { 
					  mes = Integer.parseInt(request.getParameter("mes"));
				  }
				  catch(NumberFormatException e) 
				  {
					  mes = JUtil.obtMes(GregorianCalendar.getInstance()); 
					  ano = JUtil.obtAno(GregorianCalendar.getInstance());
				  }
				  
				  try
				  { 
					  ano = Integer.parseInt(request.getParameter("ano"));
				  }
				  catch(NumberFormatException e) 
				  {
					  mes = JUtil.obtMes(GregorianCalendar.getInstance()); 
					  ano = JUtil.obtAno(GregorianCalendar.getInstance());
				  }
	
				  String TiempoTit = Integer.toString(ano);;
	
				  String Tiempo = "Mes = " + mes + " AND Ano = " + ano;
				  getSesion(request).getSesion("CONT_POLCIERRE").setTiempo(Tiempo,TiempoTit);
				  getSesion(request).getSesion("CONT_POLCIERRE").setEspecial(TiempoTit);

			  }
			  else
			  {
				  idmensaje = 1;  mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 2); // 1 Error de rango
			  }
		  }
		  else
		  {
			  idmensaje = 1; mensaje += JUtil.Msj("GLB", "GLB", "GLB", "ERROR-PARAM", 2); // 2 Error de rango
		  }
      }
      else if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
       	  getSesion(request).getSesion("CONT_POLCIERRE").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/contabilidad/conta_polcierre_vsta.jsp", request, response);

    }

}
