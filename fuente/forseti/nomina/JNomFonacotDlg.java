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
public class JNomFonacotDlg extends JForsetiApl
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

      String nom_fonacot_dlg = "";
      request.setAttribute("nom_fonacot_dlg",nom_fonacot_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	if(request.getParameter("proceso").equals("AGREGAR_FONACOT"))
        {
    		// Revisa si tiene fonacot
    		if(!getSesion(request).getPermiso("NOM_FONACOT_AGREGAR"))
    		{
	              idmensaje = 3; mensaje += " No tienes permiso para agregar créditos fonacot de nómina<br>";
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
	              return;
    		}
            // Solicitud de envio a procesar
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            {
              // Verificacion
              if(VerificarParametros(request, response))
                Agregar(request, response);

            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
              if(VerificarParametrosPartida(request, response))
                AgregarPartida(request, response);

            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
            {
              if(VerificarParametrosPartida(request, response))
                EditaPartida(request, response);

            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
               BorrarPartida(request, response);
            }
            else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
              HttpSession ses = request.getSession(true);
              JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
              if(rec == null)
              {
                rec = new JNomFonacotSes();
                ses.setAttribute("nom_fonacot_dlg", rec);
              }
              else
            	rec.resetear();

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
            }

    		
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_FONACOT"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_FONACOT_CONSULTAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para consultar créditos fonacot de nómina<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar;
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {

              HttpSession ses = request.getSession(true);
              JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
              if(rec == null)
              {
                rec = new JNomFonacotSes();
                ses.setAttribute("nom_fonacot_dlg", rec);
              }
              else
                rec.resetear();

              // Llena el permiso
              JFonacotDetSet set = new JFonacotDetSet(request);
              set.m_Where = "ID_Credito = '" + p(request.getParameter("id")) + "'";
		      set.Open();
              for(int i = 0; i < set.getNumRows(); i++)
              {
                rec.agregaPartida( set.getAbsRow(i).getFecha(), set.getAbsRow(i).getDescuento());
              }
             
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite consultar un crédito fonacot de nómina a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
             idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del crédito fonacot de nómina que se quiere consultar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR_FONACOT"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_FONACOT_CAMBIAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para cambiar créditos fonacot de nómina<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            if(VerificarParametros(request, response))
              Cambiar(request, response);
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              AgregarPartida(request, response);

          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              EditaPartida(request, response);

          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
          }
          else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de CAMBIADO `por primera vez
          {
            if(request.getParameter("id") != null)
            {
              String[] valoresParam = request.getParameterValues("id");
              if(valoresParam.length == 1)
              {
                    HttpSession ses = request.getSession(true);
                    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
                    if(rec == null)
                    {
                      rec = new JNomFonacotSes();
                      ses.setAttribute("nom_fonacot_dlg", rec);
                    }
                    else
                      rec.resetear();

                    // Llena el permiso
                    JFonacotDetSet set = new JFonacotDetSet(request);
                    set.m_Where = "ID_Credito = '" + p(request.getParameter("id")) + "'";
      		      	set.Open();
                    for(int i = 0; i < set.getNumRows(); i++)
                    {
                      rec.agregaPartida( set.getAbsRow(i).getFecha(), set.getAbsRow(i).getDescuento());
                    }
                   
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
                
              }
              else
              {
                idmensaje = 1; mensaje += "PRECAUCION: Solo se permite cambiar un crédito fonacot de nómina a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              }
            }
            else
            {
               idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del crédito fonacot de nómina que se quiere cambiar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_FONACOT"))
    	{
    		// Revisa si tiene fonacot
    		if(!getSesion(request).getPermiso("NOM_FONACOT_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += " No tienes permiso para borrar créditos fonacot de nómina<br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		}

    		// Solicitud de envio a procesar
    		if(request.getParameter("id") != null)
    		{
    			String[] valoresParam = request.getParameterValues("id");
    			if(valoresParam.length == 1)
    			{
    				Eliminar(request, response);
    			}
    			else
    			{
    				idmensaje = 1; mensaje += "PRECAUCION: Solo se permite borrar un crédito fonacot de nómina a la vez <br>";
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			}
    		}
    		else
    		{
    			idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del crédito fonacot de nómina que se quiere borrar <br>";
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

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("descuento") != null && !request.getParameter("descuento").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	        idmensaje = 1; mensaje = "PRECAUCION: Se debe enviar el descuento de la partida <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	        return false;
	    }
	}

    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	
	    idmensaje = rec.agregaPartida(request, JUtil.estFecha(request.getParameter("fechadesc")), Float.parseFloat(request.getParameter("descuento")), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	
	}
    public void EditaPartida(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	
	    idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.estFecha(request.getParameter("fechadesc")), Float.parseFloat(request.getParameter("descuento")), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	
	}

	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
		
	    rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	
	}

    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_credito") != null && request.getParameter("fecha") != null &&
    	 request.getParameter("id_empleado") != null && request.getParameter("meses") != null && 
    	 request.getParameter("plazo") != null && request.getParameter("importe") != null && request.getParameter("retencion") != null && 
    	!request.getParameter("id_credito").equals("") && !request.getParameter("fecha").equals("") && 
    	!request.getParameter("id_empleado").equals("") && !request.getParameter("meses").equals("")&& 
    	!request.getParameter("plazo").equals("") && !request.getParameter("importe").equals("") && !request.getParameter("retencion").equals(""))
      {
    	  JMasempSet setemp = new JMasempSet(request);
    	  setemp.m_Where = "ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
    	  setemp.Open();
    	  
    	  if(setemp.getNumRows() < 1)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: El empleado al que deseas aplicarle el crédito no existe<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      irApag("/forsetiweb/nomina/nom_permisos_dlg.jsp", request, response);
              return false;
    	  }
    	  
    	  return true;
      }
      else
      {
          idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
          return false;
      }
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
  	  	String str = "EXEC  sp_fonacot_eliminar '" + request.getParameter("id") + "'";
		
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
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	  	HttpSession ses = request.getSession(true);
	  	JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	  	
	  	String tbl;
	  	tbl =  "CREATE TABLE [#TMP_FONACOT_DET] (\n";
	  	tbl += " [Fecha] [smalldatetime] NOT NULL, \n";
	  	tbl += " [Descuento] [money] NOT NULL \n";
	  	tbl += ") ON [PRIMARY] \n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "\n\ninsert into #TMP_FONACOT_DET\nvalues('" + JUtil.obtFechaSQL(rec.getPartida(i).getFechaDesc()) + "'," + 
	  			rec.getPartida(i).getDescuento() + ")";
	  	}
		     
	  	String str = "EXEC  sp_fonacot_cambiar '" + p(request.getParameter("id")) + "','" + p(request.getParameter("id_empleado")) + "','" + JUtil.obtFechaSQL(request.getParameter("fecha")) + "'," + request.getParameter("meses") + "," +
	  	request.getParameter("plazo") + "," +  request.getParameter("importe") + "," + request.getParameter("retencion");
		  		
	  	
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
	         
	         s.executeUpdate("\nDROP TABLE [#TMP_FONACOT_DET]");
	         s.close();
	         JAccesoBD.liberarConexion(con);
	
	         getSesion(request).setID_Mensaje(idmensaje, mensaje);
	         irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	  	}
	  	catch(SQLException e)
	  	{
	  		e.printStackTrace();
	  		throw new RuntimeException(e.toString());
	  	}
	  	
 	  	
 	  	//doDebugSQL(request, response, str);
  	}
    public void Agregar(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	  	HttpSession ses = request.getSession(true);
	  	JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	  	
	  	String tbl;
	  	tbl =  "CREATE TABLE [#TMP_FONACOT_DET] (\n";
	  	tbl += " [Fecha] [smalldatetime] NOT NULL, \n";
	  	tbl += " [Descuento] [money] NOT NULL \n";
	  	tbl += ") ON [PRIMARY] \n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "\n\ninsert into #TMP_FONACOT_DET\nvalues('" + JUtil.obtFechaSQL(rec.getPartida(i).getFechaDesc()) + "'," + 
	  			rec.getPartida(i).getDescuento() + ")";
	  	}
		     
	  	String str = "EXEC  sp_fonacot_agregar '" + p(request.getParameter("id_credito")) + "','" + p(request.getParameter("id_empleado")) + "','" + JUtil.obtFechaSQL(request.getParameter("fecha")) + "'," + request.getParameter("meses") + "," +
	  	request.getParameter("plazo") + "," +  request.getParameter("importe") + "," + request.getParameter("retencion");
		  		
	  	 
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
	         
	         s.executeUpdate("\nDROP TABLE [#TMP_FONACOT_DET]");
	         s.close();
	         JAccesoBD.liberarConexion(con);
	
	         getSesion(request).setID_Mensaje(idmensaje, mensaje);
	         irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
	  	}
	  	catch(SQLException e)
	  	{
	  		e.printStackTrace();
	  		throw new RuntimeException(e.toString());
	  	}
	  	
		  	
		//doDebugSQL(request, response, str);
	}
*/
}