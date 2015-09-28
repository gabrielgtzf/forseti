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
package forseti.mensajes;

import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import forseti.JAccesoBD;
import forseti.JForsetiApl;
//import forseti.sets.JNotasBlocksIdsSet;

@SuppressWarnings("serial")
public class JNotasDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }
/*
    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String notas_dlg = "";
      request.setAttribute("notas_dlg",notas_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	// revisa por las entidades
    	JNotasBlocksIdsSet set = new JNotasBlocksIdsSet(request);
    	set.m_Where = "ID_Usuario = '" + usuario + "' and ID_Block = " + getSesion(request).getSesionNotas().getEspecial(); 
        set.Open();

        if(set.getNumRows() < 1)
        {
           idmensaje = 3; mensaje += " No tienes permiso a esta entidad de notas. <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           irApag("/forsetiweb/caja_mensajes.jsp", request, response);
           return;
        }

        if(request.getParameter("proceso").equals("AGREGAR"))
        {
          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            if(VerificarParametros(request, response))
              Agregar(request, response);
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/mensajes/notas_dlg.jsp", request, response);
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR"))
        {
          // Solicitud de envio a procesar
          if(request.getParameter("clave") != null)
          {
            String[] valoresParam = request.getParameterValues("clave");
            if(valoresParam.length == 1)
            {
              Eliminar(request, response);
            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite eliminar una nota de origen a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
             idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador de la nota de origen que se quiere eliminar<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }
        }
        else
        {
          idmensaje = 1;
          mensaje += "PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += "ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      }

    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if( request.getParameter("clave") != null && request.getParameter("mensaje") != null &&
          !request.getParameter("mensaje").equals(""))
      {
        return true;
      }
      else
      {
        idmensaje = 1; mensaje = "PRECAUCION: Alguno de los parametros necesarios es Nulo <br>";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiweb/mensajes/notas_dlg.jsp", request, response);
        return false;
      }
    }

  
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String str = "EXEC  sp_notas_eliminar " + getSesion(request).getSesionNotas().getEspecial() + ",'" + request.getParameter("clave") + "','" +
      	getSesion(request).getID_Usuario() + "'";
      
      try
      {
         short idmensaje = -1; String mensaje = "";
         Connection con = JAccesoBD.getConexionSes(request);
         Statement s    = con.createStatement();
         ResultSet rs   = s.executeQuery(str);
         if(rs.next())
         {
           idmensaje = rs.getShort("ERR");
           mensaje = rs.getString("RES");
         }
         s.close();
         JAccesoBD.liberarConexion(con);

         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    
      String str = "EXEC  sp_notas_agregar " + getSesion(request).getSesionNotas().getEspecial()  + ",'" + request.getParameter("clave") + "','" + 
      getSesion(request).getID_Usuario() + "','" + p(request.getParameter("mensaje")) + "'";

      try
      {
         short idmensaje = -1; String mensaje = "";
         Connection con = JAccesoBD.getConexionSes(request);
         Statement s    = con.createStatement();
         ResultSet rs   = s.executeQuery(str);
         if(rs.next())
         {
           idmensaje = rs.getShort("ERR");
           mensaje = rs.getString("RES");
         }
         s.close();
         JAccesoBD.liberarConexion(con);

         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/mensajes/notas_dlg.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }

    }
*/
   

}
