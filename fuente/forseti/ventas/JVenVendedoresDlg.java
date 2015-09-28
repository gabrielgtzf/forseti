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
package forseti.ventas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

@SuppressWarnings("serial")
public class JVenVendedoresDlg extends JForsetiApl
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

      String ven_vendedores_dlg = "";
      request.setAttribute("ven_vendedores_dlg",ven_vendedores_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMON_VENDEDORES_AGREGAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para agregar vendedores<br>";
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
            irApag("/forsetiweb/ventas/ven_vendedores_dlg.jsp", request, response);
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMON_USUARIOS_CAMBIAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para cambiar vendedores<br>";
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
                irApag("/forsetiweb/ventas/ven_vendedores_dlg.jsp", request, response);
              }

            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite cambiar un vendedor a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
             idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del vendedor que se quiere cambiar <br>";
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
      if(request.getParameter("idvendedor") != null && request.getParameter("nombre") != null
          && request.getParameter("comision") != null  && 
          !request.getParameter("idvendedor").equals("") && !request.getParameter("nombre").equals("")
          && !request.getParameter("comision").equals("") )
      {
          return true;
      }
      else
      {
          idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/ventas/ven_vendedores_dlg.jsp", request, response);
          return false;
      }

    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      String str = "EXEC  sp_vendedores_cambiar " + request.getParameter("idvendedor") + ",'" + p(request.getParameter("nombre")) + "'," + request.getParameter("comision");

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
         irApag("/forsetiweb/ventas/ven_vendedores_dlg.jsp", request, response);
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
      String str = "EXEC  sp_vendedores_agregar " + request.getParameter("idvendedor") + ",'" + p(request.getParameter("nombre")) + "'," + request.getParameter("comision");

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
         irApag("/forsetiweb/ventas/ven_vendedores_dlg.jsp", request, response);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e.toString());
      }

    }

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String str = "EXEC  sp_usuarios_eliminar '" + p(request.getParameter("id")) + "'";

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
    
     
	@SuppressWarnings("rawtypes")
	public void CambiarPermisos(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	String tbl;			  
    	tbl =  "CREATE TABLE [#TMP_USUARIOS_PERMISOS] (\n";
    	tbl += " 	[ID_Permiso] [varchar] (30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL \n";
		tbl += ") ON [PRIMARY]\n";
		tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_REPORTES] (\n";
    	tbl += "	[ID_Report] [smallint] NOT NULL \n";
    	tbl += ") ON [PRIMARY]\n";
    	tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_BANCOS] (\n";
   		tbl += "	[Tipo] [tinyint] NOT NULL ,\n";
		tbl += "	[Clave] [tinyint] NOT NULL\n";
		tbl += ") ON [PRIMARY]\n";
    	tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_BODEGAS] (\n";
    	tbl += "	[ID_Bodega] [smallint] NOT NULL\n";
    	tbl += ") ON [PRIMARY]\n";
    	tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_COMPRAS] (\n";
    	tbl += "	[ID_EntidadCompra] [smallint] NOT NULL \n";
    	tbl += ") ON [PRIMARY]\n";
    	tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_VENTAS] (\n";
    	tbl += "	[ID_EntidadVenta] [smallint] NOT NULL \n";
    	tbl += ") ON [PRIMARY]\n";
    	tbl += "CREATE TABLE [#TMP_USUARIOS_SUBMODULO_PRODUCCION] (\n";
    	tbl += "	[ID_EntidadProd] [smallint] NOT NULL \n";
    	tbl += ") ON [PRIMARY]\n";

        Enumeration nombresParam = request.getParameterNames();
        while(nombresParam.hasMoreElements())
        {
            String nombreParam = (String)nombresParam.nextElement();
            if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
            	continue;
            
            String claveParam = nombreParam.substring(0,8);
            String valorParam = nombreParam.substring(8);
            if(claveParam.equals("PER_PER_"))
            {
            	tbl += "insert into #TMP_USUARIOS_PERMISOS\n";
            	tbl += "values('" + valorParam + "')\n";
            }
            else if(claveParam.equals("PER_REP_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_REPORTES\n";
            	tbl += "values(" + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_BAN_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_BANCOS\n";
            	tbl += "values(0," + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_CAJ_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_BANCOS\n";
            	tbl += "values(1," + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_BOD_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_BODEGAS\n";
            	tbl += "values(" + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_COM_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_COMPRAS\n";
            	tbl += "values(" + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_VEN_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_VENTAS\n";
            	tbl += "values(" + valorParam + ")\n";
            }
            else if(claveParam.equals("PER_PRD_"))
            {
            	tbl += "insert into #TMP_USUARIOS_SUBMODULO_PRODUCCION\n";
            	tbl += "values(" + valorParam + ")\n";
            }

 
        }

        String str = "EXEC  sp_usuarios_permisos '" + p(request.getParameter("idusuario")) + "'";
		String drop = "DROP TABLE [#TMP_USUARIOS_PERMISOS]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_REPORTES]\n" + 
		"DROP TABLE [#TMP_USUARIOS_SUBMODULO_BANCOS]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_BODEGAS]\n" + 
		"DROP TABLE [#TMP_USUARIOS_SUBMODULO_COMPRAS]\nDROP TABLE [#TMP_USUARIOS_SUBMODULO_VENTAS]\n" + 
		"DROP TABLE [#TMP_USUARIOS_SUBMODULO_PRODUCCION]\n";
    	
		//doDebugSQL(request,response,tbl + "\n" + str + "\n" + drop);
		
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
	       s.executeUpdate(drop);
	       s.close();
	       JAccesoBD.liberarConexion(con);
	
	       getSesion(request).setID_Mensaje(idmensaje, mensaje);
	       irApag("/forsetiweb/ventas/ven_vendedores_dlg_perm.jsp", request, response);
	    }
	    catch(SQLException e)
	    {
	       e.printStackTrace();
	       throw new RuntimeException(e.toString());
	    }
		
	}   
*/
}
