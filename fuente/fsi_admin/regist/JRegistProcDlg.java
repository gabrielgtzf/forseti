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
package fsi_admin.regist;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forseti.JRetFuncBas;
import forseti.JUtil;
import fsi_admin.JFsiForsetiApl;

public class JRegistProcDlg extends JFsiForsetiApl
{
   	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);
 
      String regist_proc_dlg = "";
      request.setAttribute("regist_proc_dlg",regist_proc_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("TRUNCAR_REGISTRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("REGIST_PROC_TRUNCAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_PROC_TRUNCAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_PROC_TRUNCAR","RPRC||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          Truncar(request, response);
          return;
          
        }
        else if(request.getParameter("proceso").equals("LIBERAR_REGISTRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("REGIST_PROC_LIBERAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_PROC_LIBERAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_PROC_LIBERAR","RPRC||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(request.getParameter("fecha") == null || request.getParameter("fecha").equals(""))
        	  {
        		  idmensaje = 1; mensaje = JUtil.Msj("SAF","REGIST_PROC","DLG","MSJ-PROCERR",1); //"PRECAUCION: No se ha enviado la fecha";
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiadmin/registros/regist_proc_dlg.jsp", request, response);
        		  return;
        	  }
        	  Liberar(request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiadmin/registros/regist_proc_dlg.jsp", request, response);
        	  return;
          }
        }
        else
        {
          idmensaje = 3;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
         return;
      }

    }
    
    
    public void Liberar(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM sp_regist_proc_eliminar_desde( '" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + " 23:59:59' ) as ( err integer, res varchar, clave varchar ) ";
        	        
    	doCallStoredProcedure(request, response, str, rfb);
        	         
    	RDP("SAF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"REGIST_PROC_LIBERAR","RPRC|" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + " 23:59:59" + "|||",rfb.getRes());
    	irApag("/forsetiadmin/registros/regist_proc_dlg.jsp", request, response);
        	         
    }
    
    public void Truncar(HttpServletRequest request, HttpServletResponse response)
       	throws ServletException, IOException
    {
    	String mensaje = ""; short idmensaje = -1;
    	String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
    	Connection conn = null;
    	Statement s = null;
    		
    	try
    	{
    		String driver = "org.postgresql.Driver";
    		Class.forName(driver).newInstance();
    		String url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
    		conn = DriverManager.getConnection(url);
    		s = conn.createStatement();
    		String sql = "TRUNCATE TABLE tbl_regproc;";
    		s.execute(sql);
    		idmensaje = 0;
    		mensaje = JUtil.Msj("SAF","REGIST_PROC","DLG","MSJ-PROCOK",1);//"El registro de procesos se trunco con exito, y todo se ha liberado";
    	}
    	catch(Throwable e)
    	{
    		idmensaje = 3;
    		mensaje = "ERROR: Al truncar registro de procesos<br>" + e.getMessage();
    	}
    	finally
    	{
    		try { s.close(); } catch (Exception e) { }
    		try { conn.close(); } catch (Exception e) { }
    	}
    		
    	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"REGIST_PROC_TRUNCAR","RPRC||||",mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        return;
    }
}
