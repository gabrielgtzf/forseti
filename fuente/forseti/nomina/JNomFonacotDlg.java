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
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JFonacotDetSet;
import forseti.sets.JMasempSet;

@SuppressWarnings("serial")
public class JNomFonacotDlg extends JForsetiApl
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
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_FONACOT_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_FONACOT_AGREGAR","NFON||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}
            // Solicitud de envio a procesar
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            {
            	// Verificacion
            	if(VerificarParametros(request, response))
            	{
            		Agregar(request, response);
            		return;
            	}
            	irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
                return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
              if(VerificarParametrosPartida(request, response))
                AgregarPartida(request, response);

              irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
            {
              if(VerificarParametrosPartida(request, response))
                EditaPartida(request, response);
              
              irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
               BorrarPartida(request, response);
               
               irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
               return;
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
              return;
            }

    		
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_FONACOT"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_FONACOT"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_FONACOT");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_FONACOT","NFON||||",mensaje);
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

              // Llena el credito
              JFonacotDetSet set = new JFonacotDetSet(request);
              set.m_Where = "ID_Credito = '" + p(request.getParameter("id")) + "'";
		      set.Open();
              for(int i = 0; i < set.getNumRows(); i++)
              {
                rec.agregaPartida( set.getAbsRow(i).getFecha(), set.getAbsRow(i).getDescuento());
              }
             
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
          }
          else
          {
        	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR_FONACOT"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_FONACOT_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_FONACOT_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_FONACOT_CAMBIAR","NFON||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(VerificarParametros(request, response))
        	  {
        		  Cambiar(request, response);
        		  return;
        	  }
        	  irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
        	  if(VerificarParametrosPartida(request, response))
        		  AgregarPartida(request, response);
        	  
        	  irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
        	  if(VerificarParametrosPartida(request, response))
        		  EditaPartida(request, response);
        	  
        	  irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
             
             irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
             return;
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

                    // Llena el credito
                    JFonacotDetSet set = new JFonacotDetSet(request);
                    set.m_Where = "ID_Credito = '" + p(request.getParameter("id")) + "'";
                    System.out.println(set.getSQL());
      		      	set.Open();
                    for(int i = 0; i < set.getNumRows(); i++)
                    {
                      rec.agregaPartida( set.getAbsRow(i).getFecha(), set.getAbsRow(i).getDescuento());
                    }
                   
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);
                    return;
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_FONACOT"))
    	{
    		// Revisa si tiene fonacot
    		if(!getSesion(request).getPermiso("NOM_FONACOT_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_FONACOT_ELIMINAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_FONACOT_ELIMINAR","NFON||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}

    		// Solicitud de envio a procesar
    		if(request.getParameter("id") != null)
    		{
    			String[] valoresParam = request.getParameterValues("id");
    			if(valoresParam.length == 1)
    			{
    				Eliminar(request, response);
    				return;
    			}
    			else
    			{
    				idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); 
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                    return;
    			}
    		}
    		else
    		{
    			idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}
    	}
        else
        {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
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
	   
	}
    
    public void EditaPartida(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	
	    idmensaje = rec.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.estFecha(request.getParameter("fechadesc")), Float.parseFloat(request.getParameter("descuento")), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	}

	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
		
	    rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
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
    		  idmensaje = 3; mensaje += "ERROR: El empleado al que deseas aplicarle el crédito no existe<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
    	  return true;
      }
      else
      {
          idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
  	  	//String str = "EXEC  sp_fonacot_eliminar '" + request.getParameter("id") + "'";
  	  	String str = "select * from  sp_nom_fonacot_eliminar( '" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave varchar)";
		
	  	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, str, rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_FONACOT_ELIMINAR", "NFON|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	}
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	  	HttpSession ses = request.getSession(true);
	  	JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	  	
	  	String tbl;
	  	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_FONACOT_DET (\n";
	  	tbl += " Fecha timestamp NOT NULL, \n";
	  	tbl += " Descuento decimal(19,4) NOT NULL \n";
	  	tbl += ");\n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "insert into _TMP_FONACOT_DET\nvalues('" + JUtil.obtFechaSQL(rec.getPartida(i).getFechaDesc()) + "','" + 
	  			rec.getPartida(i).getDescuento() + "');\n";
	  	}
		     
	  	String str = "select * from  sp_nom_fonacot_cambiar( '" + p(request.getParameter("id_credito")) + "','" + p(request.getParameter("id_empleado")) + "','" + JUtil.obtFechaSQL(request.getParameter("fecha")) + "','" + p(request.getParameter("meses")) + "','" +
	  		  	p(request.getParameter("plazo")) + "','" +  p(request.getParameter("importe")) + "','" + p(request.getParameter("retencion")) + "') as (err integer, res varchar, clave varchar)";
		
	  	JRetFuncBas rfb = new JRetFuncBas();
	  	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_FONACOT_DET", rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_FONACOT_CAMBIAR", "NFON|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);  		
	  	
 	  	//doDebugSQL(request, response, str);
  	}
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	  	HttpSession ses = request.getSession(true);
	  	JNomFonacotSes rec = (JNomFonacotSes)ses.getAttribute("nom_fonacot_dlg");
	  	
	  	String tbl;
	  	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_FONACOT_DET (\n";
	  	tbl += " Fecha timestamp NOT NULL, \n";
	  	tbl += " Descuento decimal(19,4) NOT NULL \n";
	  	tbl += ");\n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "insert into _TMP_FONACOT_DET\nvalues('" + JUtil.obtFechaSQL(rec.getPartida(i).getFechaDesc()) + "','" + 
	  			rec.getPartida(i).getDescuento() + "');\n";
	  	}
	  	
	  	String str = "select * from  sp_nom_fonacot_agregar('" + p(request.getParameter("id_credito")) + "','" + p(request.getParameter("id_empleado")) + "','" + JUtil.obtFechaSQL(request.getParameter("fecha")) + "','" + p(request.getParameter("meses")) + "','" +
	  		  	p(request.getParameter("plazo")) + "','" +  p(request.getParameter("importe")) + "','" + p(request.getParameter("retencion")) + "') as (err integer, res varchar, clave varchar)";
		
	  	JRetFuncBas rfb = new JRetFuncBas();
	  	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_FONACOT_DET", rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_FONACOT_AGREGAR", "NFON|" + rfb.getClaveret() + "|||",rfb.getRes());
		irApag("/forsetiweb/nomina/nom_fonacot_dlg.jsp", request, response);  	
				  	
		//doDebugSQL(request, response, str);
	}

}