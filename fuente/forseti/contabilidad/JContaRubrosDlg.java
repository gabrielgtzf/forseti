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



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;


@SuppressWarnings("serial")
public class JContaRubrosDlg extends JForsetiApl
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

      String conta_rubros_dlg = "";
      request.setAttribute("conta_rubros_dlg",conta_rubros_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_RUBRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_RUBROS_CREAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_RUBROS_CREAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_RUBROS_CREAR","RUBS||||",mensaje);
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
          
            irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_RUBRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_RUBROS_CAMBIAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_RUBROS_CAMBIAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_RUBROS_CAMBIAR","RUBS||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("Clave") != null)
          {
            String[] valoresParam = request.getParameterValues("Clave");
            if(valoresParam.length == 1)
            {
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
            	System.out.println("AVP: " + request.getParameter("tipo"));
                // Verificacion
                if(VerificarParametros(request, response))
                {
                  Cambiar(request, response);
                  return;
                }
                irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);
                return;
              }

            }
            else
            {
              idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite cambiar un concepto a la vez <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del concepto que se quiere cambiar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("ELIMINAR_RUBRO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_RUBROS_ELIMINAR"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_RUBROS_ELIMINAR");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_RUBROS_ELIMINAR","RUBS||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("Clave") != null)
          {
            String[] valoresParam = request.getParameterValues("Clave");
            if(valoresParam.length == 1)
            {
            	Eliminar(request, response);
            	return;
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite cambiar un concepto a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador del concepto que se quiere cambiar <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else
        {
          idmensaje = 3; 
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3);
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
         return;
      }

    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("tipo") != null && request.getParameter("nombre") != null
          && request.getParameter("desde") != null  && request.getParameter("hasta") != null &&
          !request.getParameter("tipo").equals("") && !request.getParameter("nombre").equals("")
          && !request.getParameter("desde").equals("")  && !request.getParameter("hasta").equals(""))
      {
          if(!request.getParameter("tipo").equals("AC") &&
             !request.getParameter("tipo").equals("AF") &&
             !request.getParameter("tipo").equals("AD") &&
             !request.getParameter("tipo").equals("PC") &&
             !request.getParameter("tipo").equals("PL") &&
             !request.getParameter("tipo").equals("PD") &&
             !request.getParameter("tipo").equals("CC") &&
             !request.getParameter("tipo").equals("RI") &&
             !request.getParameter("tipo").equals("RC") &&
             !request.getParameter("tipo").equals("RG") &&
             !request.getParameter("tipo").equals("RO") &&
             !request.getParameter("tipo").equals("IP") )
          {
            idmensaje = 3; mensaje =  JUtil.Msj("CEF", "CONT_RUBROS", "DLG", "MSJ-PROCERR", 1); // El tipo de rubro invalido
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
          }
          else // revisa las cuentas desde y hasta
          {
        	System.out.println(request.getParameter("tipo"));
            boolean band = true;
            String desde = request.getParameter("desde");
            String hasta = request.getParameter("hasta");

            int dlen = desde.length();
            int hlen = hasta.length();

            if(dlen != 4 || hlen != 4)
              band = false;

            char [] dch = desde.toCharArray();
            for(int i = 0; i < dch.length; i++)
            {
              if(dch[i] < 48 || dch[i] > 57)
                band = false;
            }

            char [] hch = hasta.toCharArray();
            for(int i = 0; i < hch.length; i++)
            {
              if(hch[i] < 48 || hch[i] > 57)
                band = false;
            }

            if(band == true)
            {
              if(desde.compareTo(hasta) > 0)
              {
                idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_RUBROS", "DLG", "MSJ-PROCERR", 2);  //"PRECAUCION: La cuenta desde debe ser menor o igual a la cuenta hasta <br>";
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                return false;
              }
              return true;
            }
            else
            {
              idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_RUBROS", "DLG", "MSJ-PROCERR", 3); //"PRECAUCION: La cuenta desde o hasta debe constar de 4 d&iacute;gitos num&eacute;ricos los cuales oscilan entre 0000 y 9999 <br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
            }
          }
      }
      else
      {
          idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }

    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	//String str = "SELECT * FROM sp_cont_rubros_cambiar( '" + request.getParameter("Clave") + "','" + p(request.getParameter("nombre")) + "' )  as ( err integer, res varchar ) ";
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM  sp_cont_rubros_cambiar( '" + p(request.getParameter("Clave")) + "','" + p(request.getParameter("tipo")) + "','" +
          p(request.getParameter("nombre")) + "','" + p(request.getParameter("desde")) + "','" + p(request.getParameter("hasta")) + "' )  as ( err integer, res varchar, clave integer ) ";
      
    	/*
    	doDebugSQL(request, response, str);
    	*/
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_RUBROS_CAMBIAR","RUBS|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);

    }

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	//String str = "SELECT * FROM sp_cont_rubros_cambiar( '" + request.getParameter("Clave") + "','" + p(request.getParameter("nombre")) + "' )  as ( err integer, res varchar ) ";
    	JRetFuncBas rfb = new JRetFuncBas();
    	    	
    	String str = "SELECT * FROM  sp_cont_rubros_eliminar( '" + p(request.getParameter("Clave")) + "' )  as ( err integer, res varchar, clave integer ) ";
    	      
    	doCallStoredProcedure(request, response, str, rfb);
    	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_RUBROS_ELIMINAR","RUBS|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	JRetFuncBas rfb = new JRetFuncBas();
    	
    	String str = "SELECT * FROM  sp_cont_rubros_agregar( '" + p(request.getParameter("tipo")) + "','" +
          p(request.getParameter("nombre")) + "','" + p(request.getParameter("desde")) + "','" + p(request.getParameter("hasta")) + "' )  as ( err integer, res varchar, clave integer ) ";
      
    	doCallStoredProcedure(request, response, str, rfb);
      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_RUBROS_CREAR","RUBS|" + rfb.getClaveret() + "|||",rfb.getRes());
        irApag("/forsetiweb/contabilidad/conta_rubros_dlg.jsp", request, response);
        
    }

}
