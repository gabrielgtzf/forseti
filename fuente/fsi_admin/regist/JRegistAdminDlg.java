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
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forseti.JUtil;
import fsi_admin.JFsiFiltroMatch;
import fsi_admin.JFsiForsetiApl;

public class JRegistAdminDlg extends JFsiForsetiApl
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
 
      String regist_admin_dlg = "";
      request.setAttribute("regist_admin_dlg",regist_admin_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("TRUNCAR_REGISTRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("REGIST_ADMIN_TRUNCAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_ADMIN_TRUNCAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_ADMIN_TRUNCAR","RADM||||",mensaje);
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
          if(!getSesion(request).getPermiso("REGIST_ADMIN_LIBERAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_ADMIN_LIBERAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_ADMIN_LIBERAR","RADM||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(request.getParameter("fecha") == null || request.getParameter("fecha").equals(""))
        	  {
        		  idmensaje = 1; mensaje = JUtil.Msj("SAF","REGIST_ADMIN","DLG","MSJ-PROCERR",1);//"PRECAUCION: No se ha enviado la fecha";
        		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		  irApag("/forsetiadmin/registros/regist_admin_dlg.jsp", request, response);
        		  return;
        	  }
        	  Liberar(request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiadmin/registros/regist_admin_dlg.jsp", request, response);
        	  return;
          }
        }
        else if(request.getParameter("proceso").equals("ABRIR_REGISTRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("REGIST_ADMIN"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "REGIST_ADMIN");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"REGIST_ADMIN","RADM||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	irApag("/forsetiadmin/registros/regist_admin_dlg_cons.jsp", request, response);
                return;
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite eliminar una empresa a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador de la empresa que se quiere eliminar<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
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
    	String mensaje = ""; short idmensaje = -1;
    	
    	Calendar fecha = JUtil.estCalendario(request.getParameter("fecha"));
    	fecha.add(Calendar.DATE, 1);
		Calendar hoy = new GregorianCalendar();
		//Primero genera la lista de archivos que no se eliminaran
		String dirarch = "/usr/local/forseti/log";
		String noelim = "|";
		while(hoy.compareTo(fecha) >= 0)
		{
			String filtro = ".+-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd") + "-.+";
			JFsiFiltroMatch f = new JFsiFiltroMatch(filtro);
			File dir = new File(dirarch);	
			String [] dirlist = dir.list(f);
			for(int i=0; i < dirlist.length; i++)
			{
				noelim += dirlist[i] + "|";
				//System.out.println("NOELIM: " + dirlist[i]);
			}
			fecha.add(Calendar.DATE, 1);
    	}
		
		//Ahora elimina los archivos que no estan en la lista
		File dir = new File(dirarch);	
		for(File file: dir.listFiles()) 
		{
			if(noelim.indexOf(file.getName()) == -1) //No existe indice de este archivo.... Entonces elimina
			{
				//System.out.println("ELIMINAR: " + file.getName());
				file.delete();
			}
		}
		
		idmensaje = 0;
		mensaje = JUtil.Msj("SAF","REGIST_ADMIN","DLG","MSJ-PROCOK",1);//"El registro administrativo se ha liberado con exito desde la fecha indicada hacia atras";
    	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"REGIST_ADMIN_LIBERAR","RADM|" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + " 23:59:59" + "|||",mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiadmin/registros/regist_admin_dlg.jsp", request, response);
    	return;
        	         
    }
    
    public void Truncar(HttpServletRequest request, HttpServletResponse response)
       	throws ServletException, IOException
    {
    	String mensaje = ""; short idmensaje = -1;
    	
    	//Borra los archivos log de la bd
    	File dir = new File("/usr/local/forseti/log");
    	for(File file: dir.listFiles()) 
    		file.delete();
    	
    	idmensaje = 0;
    	mensaje = JUtil.Msj("SAF","REGIST_ADMIN","DLG","MSJ-PROCOK",2);//"El registro administrativo se eliminó con éxito, y todo se ha liberado";
    		
    	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 0 ? "OK" : (idmensaje == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"REGIST_ADMIN_TRUNCAR","RADM||||",mensaje);
    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        return;
    }
}
