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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

@SuppressWarnings("serial")
public class JMensajesDlg extends JForsetiApl
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

      String mensajes_dlg = "";
      request.setAttribute("mensajes_dlg",mensajes_dlg);
      
      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR"))
        {
          // Revisa si tiene permisos
          // ..........

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(request.getParameter("id_destino") != null && request.getParameter("titulo") != null
               && request.getParameter("mensaje") != null && !request.getParameter("id_destino").equals("") && !request.getParameter("titulo").equals("")
              && !request.getParameter("mensaje").equals("") )
              AgregarMensajes(request, response);
            else
            {
              idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo, No se agreg� el mensaje";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
            }
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR"))
        {
          // Revisa si tiene permisos
          // ..........

          // Solicitud de eliminacion
          if(request.getParameter("MENID") != null)
          {
            EliminarMensajes(request, response);

          }
          else
          {
            idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del mensaje que se quiere eliminar <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else if(request.getParameter("proceso").equals("RESPONDER"))
        {
          if(request.getParameter("MENID") != null)
          {
            String[] valoresParam = request.getParameterValues("MENID");
            if(valoresParam.length == 1)
            { /////////////////////////////////////////////
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if( request.getParameter("titulo") != null && request.getParameter("mensaje") != null )
                  ResponderMensajes(request, response);
                else
                {
                  idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo, No se agreg� el mensaje";
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
                }
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de RESPUESTA para responder por primera vez
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
              }
            } ///////////////////////////////////////////
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite responder un mensaje a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
            idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del mensaje que se quiere responder <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else if(request.getParameter("proceso").equals("LEER"))
        {
          if(request.getParameter("MENID") != null)
          {
            String[] valoresParam = request.getParameterValues("MENID");
            if(valoresParam.length == 1)
              LeerMensajes(request, response);
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite leer un mensaje a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
            idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del mensaje que se quiere leer <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else if(request.getParameter("proceso").equals("RASTREAR"))
        {
          if (request.getParameter("MENID") != null) {
            String[] valoresParam = request.getParameterValues("MENID");
            if (valoresParam.length == 1)
            {
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite rastrear un mensaje a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
            idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del mensaje que se quiere rastrear <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else
        {
          idmensaje = 1;
          mensaje += "PRECAUCION: El par�metro de proceso no es v�lido, debe ser AGREGAR, ELIMINAR, LEER, RESPONDER, � RASTREAR <br>";
        }

      }
      else // si no se mandan parametros, significa actualizar la vista
      {
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/mensajes/mensajes_vsta.jsp", request, response);
      }


    }

    public void ResponderMensajes(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String usuario = getSesion(request).getID_Usuario();
      String str = "EXEC  sp_mensajes_responder '" + usuario + "'," + request.getParameter("MENID") + ",'" +
          p(request.getParameter("titulo")) + "','" + p(request.getParameter("mensaje")) + "'";

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
         irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }


    }

    public void LeerMensajes(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String usuario = getSesion(request).getID_Usuario();
      String str = "EXEC  sp_mensajes_leer '" + usuario + "'," + request.getParameter("MENID");

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
         irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }


    }

    public void AgregarMensajes(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String usuario = getSesion(request).getID_Usuario();
      String str = "EXEC  sp_mensajes_agregar '" + usuario + "','" + request.getParameter("id_destino") + "','" +
          p(request.getParameter("titulo")) + "','" + p(request.getParameter("mensaje")) + "'";

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
         irApag("/forsetiweb/mensajes/mensajes_dlg.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }


    }

    public void EliminarMensajes(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String usuario = getSesion(request).getID_Usuario();

      String tbl;
      tbl =  "CREATE TABLE [#TMP_MENSAJES_ELIMINAR] (\n";
      tbl += "  [part] [smallint] IDENTITY(1,1) NOT NULL, \n";
      tbl += "  [ID_Mensaje] [int] NOT NULL \n";
      tbl += ") ON [PRIMARY] \n\n ";

      String[] valoresParam = request.getParameterValues("MENID");
      for(int i = 0; i < valoresParam.length; i++)
      {
         tbl += "INSERT INTO #TMP_MENSAJES_ELIMINAR\n";
         tbl += "VALUES( " + valoresParam[i] + " )\n";
      }

      String str = "EXEC  sp_mensajes_eliminar '" + usuario + "'";

      try
      {
         short idmensaje = -1; String mensaje = "";
         Connection con = JAccesoBD.getConexionSes(request);
         Statement s    = con.createStatement();
         s.executeUpdate(tbl);
         ResultSet rs   = s.executeQuery(str);
         if(rs.next())
         {
           idmensaje = rs.getShort("ERR");
           mensaje = rs.getString("RES");
         }
         s.executeUpdate("DROP TABLE [#TMP_MENSAJES_ELIMINAR]");
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
    */
}
