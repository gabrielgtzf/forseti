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
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

//import forseti.JAccesoBD;
import forseti.JForsetiApl;
//import forseti.sets.JPermisosGrupoExclusionesSet;
//import forseti.sets.JMovimientosSet;
//import forseti.sets.JAdmCompaniasSet;
//import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomPermGrupoDlg extends JForsetiApl
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

      String nom_permgrupo_dlg = "";
      request.setAttribute("nom_permgrupo_dlg",nom_permgrupo_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	if(request.getParameter("proceso").equals("AGREGAR"))
        {
    		// Revisa si tiene permgrupo
    		if(!getSesion(request).getPermiso("NOM_PERMGRUPO_AGREGAR"))
    		{
	              idmensaje = 3; mensaje += " No tienes permiso para agregar permisos de grupo de nómina<br>";
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
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
              if(VerificarParametrosPartida(request, response))
                AgregarPartida(request, response);

            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
               BorrarPartida(request, response);
            }
            else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
              HttpSession ses = request.getSession(true);
              JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_pergrupo_dlg");
              if(rec == null)
              {
                rec = new JNomPermGrupoSes(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));
                ses.setAttribute("nom_permgrupo_dlg", rec);
              }
              else
            	rec.resetear(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
            }

    		
        }
        else if(request.getParameter("proceso").equals("CONSULTAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_PERMGRUPO_CONSULTAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para consultar permisos por grupo de nómina<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {

              HttpSession ses = request.getSession(true);
              JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
              if(rec == null)
              {
                rec = new JNomPermGrupoSes(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));
                ses.setAttribute("nom_permgrupo_dlg", rec);
              }
              else
                rec.resetear(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));

              // Llena el permiso
              JPermisosGrupoExclusionesSet set = new JPermisosGrupoExclusionesSet(request);
              set.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial() + " and ID_Movimiento = " + 
              	JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + " and ID_FechaMovimiento = '" + JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "'";
		      set.Open();
              for(int i = 0; i < set.getNumRows(); i++)
              {
                rec.agregaPartida( set.getAbsRow(i).getID_Empleado(), set.getAbsRow(i).getNombre());
              }
             
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
            }
            else
            {
              idmensaje = 1; mensaje += "PRECAUCION: Solo se permite consultar un permiso por grupo de nómina a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
          else
          {
             idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del permiso por grupo de nómina que se quiere consultar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_PERMGRUPO_CAMBIAR"))
          {
            idmensaje = 3; mensaje += " No tienes permiso para cambiar permisos de grupo de nómina<br>";
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
                    JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
                    if(rec == null)
                    {
                      rec = new JNomPermGrupoSes(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));
                      ses.setAttribute("nom_permgrupo_dlg", rec);
                    }
                    else
                      rec.resetear(Byte.parseByte(getSesion(request).getSesionNomPermGrupo().getEspecial()));

                    // Llena el permiso
                    JPermisosGrupoExclusionesSet set = new JPermisosGrupoExclusionesSet(request);
                    set.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial() + " and ID_Movimiento = " + 
                    	JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + " and ID_FechaMovimiento = '" + JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "'";
      		      	set.Open();
                    for(int i = 0; i < set.getNumRows(); i++)
                    {
                      rec.agregaPartida( set.getAbsRow(i).getID_Empleado(), set.getAbsRow(i).getNombre());
                    }
                   
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
                
              }
              else
              {
                idmensaje = 1; mensaje += "PRECAUCION: Solo se permite cambiar un permiso de grupo de nómina a la vez <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              }
            }
            else
            {
               idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del permiso de grupo de nómina que se quiere cambiar <br>";
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            }
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR"))
    	{
    		// Revisa si tiene permgrupo
    		if(!getSesion(request).getPermiso("NOM_PERMGRUPO_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += " No tienes permiso para borrar permisos de grupo de nómina<br>";
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
    				idmensaje = 1; mensaje += "PRECAUCION: Solo se permite borrar un permiso de grupo de nómina a la vez <br>";
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			}
    		}
    		else
    		{
    			idmensaje = 3; mensaje += " ERROR: Se debe enviar el identificador del permiso de grupo de nómina que se quiere borrar <br>";
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
	    if(request.getParameter("id_empleado") != null && !request.getParameter("id_empleado").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	        idmensaje = 1; mensaje = "PRECAUCION: Se debe enviar el empleado a excluir <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
	        return false;
	    }
	}

    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
	
	    idmensaje = rec.agregaPartida(request, p(request.getParameter("id_empleado")), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
	
	}
	
	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
		
	    rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
	
	}

    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_movimiento") != null && request.getParameter("desde") != null &&
    	 request.getParameter("hasta") != null && 
    	!request.getParameter("id_movimiento").equals("") && !request.getParameter("desde").equals("") && 
    	!request.getParameter("hasta").equals(""))
      {
    	  JMovimientosSet set = new JMovimientosSet(request);
    	  set.m_Where = "ID_Movimiento = " + request.getParameter("id_movimiento");
    	  set.Open();
    	  
    	  if(set.getNumRows() < 1)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: El movimiento para el permiso no existe<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
              return false;
    	  }
    	  
    	  // Ahora revisa los tiempos segun tipo de movimiento. Si es de horas, los dias deben ser iguales y las horas diferentes
    	  // Y en ambos casos, las fechas hasta mayores o iguales a las fechas desde
    	  if(set.getAbsRow(0).getDC())
    	  {
    		  Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
    		  Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
    		  if(desde.getTime() > hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La fecha de inicio del permiso no puede ser mayor a la del final del permiso, en permisos de dias completos. Diferencia " + ((hasta.getTime()/(3600*24*1000)) - (desde.getTime()/(3600*24*1000))) + " dia(s) <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
    			  return false;
    		  }
    	  }
    	  else
    	  {
    		  Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
    		  Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
    		  if(desde.getTime() != hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La fecha de inicio del permiso no puede ser diferente a la del final del permiso, en permisos de horas. <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
    			  return false;
    		  }
    		  desde = JUtil.estFechaHora(request.getParameter("desde"));
    		  hasta = JUtil.estFechaHora(request.getParameter("hasta"));
    		  if(desde.getTime() > hasta.getTime())
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: La hora de inicio del permiso no puede ser mayor a la del final del permiso, en permisos de horas. Diferencia " + ((hasta.getTime()/(3600*1000)) - (desde.getTime()/(3600*1000))) + " hora(s) <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
    			  return false;
    		  }
    	  }
    	  
    	  if( set.getAbsRow(0).getPorEmpleado() )
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es aplicable a nóminas completas, Puede ser un tipo de movimiento de empleado ( Solo para una persona ) <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
              return false;
     	  }
    	  
    	  if(set.getAbsRow(0).getAplicaAlTipo() == -1)
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es compatible con permisos de empleados ni nómina. Este es un movimiento solamente de sistema <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
              return false;
     	  }
    	  
    	  JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
    	  setcom.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial();
    	  setcom.Open();
 
    	  
    	  if(set.getAbsRow(0).getAplicaAlTipo() != 0 && set.getAbsRow(0).getAplicaAlTipo() != setcom.getAbsRow(0).getTipo())
    	  {
       		  idmensaje = 3;
    	      mensaje += "ERROR: El tipo de movimiento no es compatible al tipo de nómina (Por Ej. movimiento para nómina de onfianza con nómina estricta) <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
              return false;
     	  }
          return true;
      }
      else
      {
          idmensaje = 3; mensaje = "ERROR: Alguno de los parametros necesarios es Nulo <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
          return false;
      }
    }
    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
  	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
  	  	setcom.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial();
  	  	setcom.Open();

  	  	String str = "EXEC  sp_permisos_grupo_eliminar '" + setcom.getAbsRow(0).getDescripcion() + "'," + JUtil.obtSubCadena(request.getParameter("id"),"_FM_","|") + ",'" + 
		 JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|") + "'";
		
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
  	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
  	  	setcom.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial();
  	  	setcom.Open();

    	JMovimientosSet set = new JMovimientosSet(request);
   	  	set.m_Where = "ID_Movimiento = " + request.getParameter("id_movimiento");
   	  	set.Open();
   	  	
   	  	int num_dias = 0;
   	  	float num_horas = 0f;
   	  	
   	  	if(set.getAbsRow(0).getDC())
   	  	{
   	  		Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
   	  		num_dias = (int)JUtil.getFechaDiff(hasta, desde, "dias")+1;
   	  	}
   	  	else
   	  	{
   	  		Date desde = JUtil.estFechaHora(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFechaHora(request.getParameter("hasta"));
   	  		num_horas = JUtil.redondear((float)JUtil.getFechaDiff(hasta, desde, "minutos")/60,2);
   	  		
   	  	}
   	  	
        HttpSession ses = request.getSession(true);
        JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
    	
        String tbl;
        tbl =  "CREATE TABLE [#TMP_PERMISOS_GRUPO_EXCLUSIONES] (\n";
    	tbl += " [ID_Empleado] [char] (6) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL \n";
    	tbl += ") ON [PRIMARY] \n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into #TMP_PERMISOS_GRUPO_EXCLUSIONES\nvalues('" + rec.getPartida(i).getID_Empleado() + "')";
        }

       
   	  	String str = "EXEC  sp_permisos_grupo_cambiar '" + setcom.getAbsRow(0).getDescripcion() + "'," + request.getParameter("id_movimiento") + ",'" +  
   	  		JUtil.obtFechaSQLh24(request.getParameter("desde")) + "'," + (set.getAbsRow(0).getDC() ? "1" :"0") + ",'" + 
   	  		(set.getAbsRow(0).getDC() ? JUtil.obtFechaSQLh24(request.getParameter("desde")) : JUtil.obtFechaHoraSQL(request.getParameter("desde"))) + "','" + 
   	  		(set.getAbsRow(0).getDC() ? JUtil.obtFechaSQLh24(request.getParameter("hasta"))  + " 23:59:59" : JUtil.obtFechaHoraSQL(request.getParameter("hasta"))) + "'," + num_dias + "," + num_horas + "," + num_horas;
	    ///
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
           
           s.executeUpdate("\nDROP TABLE [#TMP_PERMISOS_GRUPO_EXCLUSIONES]");
           s.close();
           JAccesoBD.liberarConexion(con);

	       getSesion(request).setID_Mensaje(idmensaje, mensaje);
	       irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
	    }
	    catch(SQLException e)
	    {
	         e.printStackTrace();
	         throw new RuntimeException(e.toString());
	    }
		///
   	  	
   	  	//doDebugSQL(request, response, str);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
  	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
  	  	setcom.m_Where = "ID_Compania = 0 and ID_Sucursal = " + getSesion(request).getSesionNomPermGrupo().getEspecial();
  	  	setcom.Open();

    	JMovimientosSet set = new JMovimientosSet(request);
   	  	set.m_Where = "ID_Movimiento = " + request.getParameter("id_movimiento");
   	  	set.Open();
   	  	
   	  	int num_dias = 0;
   	  	float num_horas = 0f;
   	  	
   	  	if(set.getAbsRow(0).getDC())
   	  	{
   	  		Date desde = JUtil.estFecha_h24(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFecha_h24(request.getParameter("hasta"));
   	  		num_dias = (int)JUtil.getFechaDiff(hasta, desde, "dias")+1;
   	  	}
   	  	else
   	  	{
   	  		Date desde = JUtil.estFechaHora(request.getParameter("desde"));
   	  		Date hasta = JUtil.estFechaHora(request.getParameter("hasta"));
   	  		num_horas = JUtil.redondear((float)JUtil.getFechaDiff(hasta, desde, "minutos")/60,2);
   	  		
   	  	}
   	  	
        HttpSession ses = request.getSession(true);
        JNomPermGrupoSes rec = (JNomPermGrupoSes)ses.getAttribute("nom_permgrupo_dlg");
    	
        String tbl;
        tbl =  "CREATE TABLE [#TMP_PERMISOS_GRUPO_EXCLUSIONES] (\n";
    	tbl += " [ID_Empleado] [char] (6) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL \n";
    	tbl += ") ON [PRIMARY] \n\n";
    	
    	for(int i = 0; i < rec.getPartidas().size(); i++)
        {
    		tbl += "\n\ninsert into #TMP_PERMISOS_GRUPO_EXCLUSIONES\nvalues('" + rec.getPartida(i).getID_Empleado() + "')";
        }

       
   	  	String str = "EXEC  sp_permisos_grupo_agregar '" + setcom.getAbsRow(0).getDescripcion() + "'," + request.getParameter("id_movimiento") + ",'" +  
   	  		JUtil.obtFechaSQLh24(request.getParameter("desde")) + "'," + (set.getAbsRow(0).getDC() ? "1" :"0") + ",'" + 
   	  		(set.getAbsRow(0).getDC() ? JUtil.obtFechaSQLh24(request.getParameter("desde")) : JUtil.obtFechaHoraSQL(request.getParameter("desde"))) + "','" + 
   	  		(set.getAbsRow(0).getDC() ? JUtil.obtFechaSQLh24(request.getParameter("hasta"))  + " 23:59:59" : JUtil.obtFechaHoraSQL(request.getParameter("hasta"))) + "'," + num_dias + "," + num_horas + "," + num_horas;
	    
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
           
           s.executeUpdate("\nDROP TABLE [#TMP_PERMISOS_GRUPO_EXCLUSIONES]");
           s.close();
           JAccesoBD.liberarConexion(con);

	       getSesion(request).setID_Mensaje(idmensaje, mensaje);
	       irApag("/forsetiweb/nomina/nom_permgrupo_dlg.jsp", request, response);
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
