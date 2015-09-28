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
package forseti.nomina;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

@SuppressWarnings("serial")
public class JNomMovimDlg extends JForsetiApl
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

      String nom_movim_dlg = "";
      request.setAttribute("nom_movim_dlg",nom_movim_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_MOVIM_AGREGAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para agregar movimientos de nómina<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
            if(VerificarParametros(request, response))
              Agregar(request, response);

          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/nomina/nom_movim_dlg.jsp", request, response);
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_MOVIM_CAMBIAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para cambiar movimientos de nómina<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if(VerificarParametros(request, response))
                  Cambiar(request, response);

              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/nomina/nom_movim_dlg.jsp", request, response);
              }

            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite cambiar un movimiento a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
             idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del movimiento que se quiere cambiar <br>";
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
      if(request.getParameter("id_movimiento") != null && request.getParameter("descripcion") != null &&
    		  request.getParameter("id_sistema")  != null &&
         !request.getParameter("id_movimiento").equals("") && !request.getParameter("descripcion").equals("") &&
          !request.getParameter("id_sistema").equals("") )
      {
          return true;
      }
      else
      {
          idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/nomina/nom_movim_dlg.jsp", request, response);
          return false;
      }
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String str = "EXEC  sp_movimientos_cambiar " + request.getParameter("id_movimiento") + ",'" +
          p(request.getParameter("descripcion")) + "'," + request.getParameter("id_sistema");
      
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
         irApag("/forsetiweb/nomina/nom_movim_dlg.jsp", request, response);
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
    	String str = "EXEC  sp_movimientos_agregar " + request.getParameter("id_movimiento") + ",'" +
        p(request.getParameter("descripcion")) + "'," + request.getParameter("id_sistema");

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
    		irApag("/forsetiweb/nomina/nom_movim_dlg.jsp", request, response);
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    		throw new RuntimeException(e.toString());
    	}

    }
*/
}
