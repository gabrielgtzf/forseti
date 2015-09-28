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
import forseti.sets.JMovimientosNomSet;
import forseti.sets.JMovimientosNomDetallesSet;

@SuppressWarnings("serial")
public class JNomMovimNomDlg extends JForsetiApl
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

      String nom_movimientos_dlg = "";
      request.setAttribute("nom_movimientos_dlg",nom_movimientos_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_MOVIMIENTOS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_MOVIMIENTOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_MOVIMIENTOS_AGREGAR","NMOV||||",mensaje);
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
            
            irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              AgregarPartida(request, response);
            
            irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              EditarPartida(request, response);

            irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
             
             irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
             return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  HttpSession ses = request.getSession(true);
              JNomMovimNomSes cat = (JNomMovimNomSes) ses.getAttribute("nom_movimientos_dlg");
              if (cat == null) 
              {
                cat = new JNomMovimNomSes();
                ses.setAttribute("nom_movimientos_dlg", cat);
              }
              else
                cat.resetear();
              
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
              return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_MOVIMIENTOS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_MOVIMIENTOS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_MOVIMIENTOS_CAMBIAR","NMOV||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
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
                {
                	Cambiar(request, response);
                	return;
                }
                irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
                return;
              }
              else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
              {
                if(VerificarParametrosPartida(request, response))
                  AgregarPartida(request, response);

                irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
                return;
              }
              else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
              {
                if(VerificarParametrosPartida(request, response))
                  EditarPartida(request, response);

                irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
                return;
              }
              else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
              {
                 BorrarPartida(request, response);
                 
                 irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
                 return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	  HttpSession ses = request.getSession(true);
                  JNomMovimNomSes cat = (JNomMovimNomSes) ses.getAttribute("nom_movimientos_dlg");
                  if (cat == null) 
                  {
                    cat = new JNomMovimNomSes();
                    ses.setAttribute("nom_movimientos_dlg", cat);
                  }
                  else
                    cat.resetear();
                  
                  // Llena el movimiento
                  JMovimientosNomDetallesSet set = new JMovimientosNomDetallesSet(request);
                  set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id")) + "'";
                  set.Open();
                  for(int i = 0; i < set.getNumRows(); i++)
                  {
                    cat.agregaPartida( set.getAbsRow(i).getCuenta(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getID_Departamento(), set.getAbsRow(i).getNombre_Departamento() );
                  }
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
            	  return;
              }

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
        else if(request.getParameter("proceso").equals("CONSULTAR_MOVIMIENTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_MOVIMIENTOS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_MOVIMIENTOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_MOVIMIENTOS","NMOV||||",mensaje);
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
            	JNomMovimNomSes cat = (JNomMovimNomSes) ses.getAttribute("nom_movimientos_dlg");
            	if (cat == null) 
            	{
            		cat = new JNomMovimNomSes();
                    ses.setAttribute("nom_movimientos_dlg", cat);
            	}
            	else
            		cat.resetear();
                  
            	// Llena el movimiento
            	JMovimientosNomDetallesSet set = new JMovimientosNomDetallesSet(request);
            	set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id")) + "'";
            	set.Open();
            	for(int i = 0; i < set.getNumRows(); i++)
            	{
            		cat.agregaPartida( set.getAbsRow(i).getCuenta(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getID_Departamento(), set.getAbsRow(i).getNombre_Departamento() );
            	}
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
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

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_movimiento") != null && request.getParameter("descripcion") != null && request.getParameter("id_sat") != null &&
         !request.getParameter("id_movimiento").equals("") && !request.getParameter("descripcion").equals("") && !request.getParameter("id_sat").equals("") )
      {
    	 
    	 if(request.getParameter("proceso").equals("CAMBIAR_MOVIMIENTO"))
    	 {
    		 JMovimientosNomSet set = new JMovimientosNomSet(request);
    		 set.m_Where = "ID_Movimiento = '" + p(request.getParameter("id_movimiento")) + "'";
    		 set.Open();
    		 if(!set.getAbsRow(0).getTipo_Movimiento().equals("DIN"))
    		 {
    			 if(request.getParameter("imss") != null || request.getParameter("ispt") != null || request.getParameter("dospor") != null || 
    	    			 request.getParameter("sar") != null || request.getParameter("infonavit") != null || request.getParameter("ptu") != null )
    	    	 {
    	    		 idmensaje = 3;
    	    		 mensaje += "ERROR: No se deben aplicar impuestos a los movimientos de sistema <br>";
    	    		 getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	    		 return false;
    	    	 }
    		 }
    	 }
    	 else
    	 {
    		 int mov = Integer.parseInt(request.getParameter("id_movimiento"));
    		 int ind = request.getParameter("id_movimiento").length();
    		 String ultimo =  request.getParameter("id_movimiento").substring(ind - 1);
    		 if(mov > 99 && ultimo.equals("0"))
    		 {
    			 idmensaje = 1; mensaje += "PRECAUCION: El Id del movimiento no debe terminar con el número Cero cuando es mayor a 99 porque estos movimientos estan reservados a movimientos de sistema futuros";
	    		 getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    		 return false;
    		 }
    	 }
    	 
    	 if(!request.getParameter("id_sat").matches("\\d{3}"))
    	 {
    		 idmensaje = 3;
    		 mensaje += "ERROR: El ID del movimiento para el SAT debe constar de tres digitos exactamente <br>";
    		 getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		 return false;
    	 }
    	 
    	 return true;
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
    	String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_NOM_MOVIMIENTOS_NOMINA_DET (\n";
        tbl += "Cuenta char(19) NOT NULL ,\n";
        tbl += "ID_Departamento char(4) NOT NULL \n";
        tbl += ");\n\n";

        HttpSession ses = request.getSession(true);
        JNomMovimNomSes pol = (JNomMovimNomSes)ses.getAttribute("nom_movimientos_dlg");

        for(int i = 0; i < pol.getPartidas().size(); i++)
        {
           tbl += "INSERT INTO _TMP_NOM_MOVIMIENTOS_NOMINA_DET\n";
           tbl += "VALUES( '" + p(JUtil.obtCuentas(pol.getPartida(i).getCuenta(),(byte)19)) + "','" +
               p(pol.getPartida(i).getID_Departamento()) + "');\n";
        }
        
    	String str = "select * from sp_nom_movimientos_nomina_cambiar('" + p(request.getParameter("id_movimiento")) + "','DIN','" +
        p(request.getParameter("descripcion")) + "','" + (request.getParameter("esdeduccion") != null ? "1" : "0") + "','" +
        (request.getParameter("imss") != null ? "1" : "0") + "','" + (request.getParameter("ispt") != null ? "1" : "0") + "','" + (request.getParameter("dospor") != null ? "1" : "0")
         + "','" + (request.getParameter("sar") != null ? "1" : "0")  + "','" + (request.getParameter("infonavit") != null ? "1" : "0") + "','" + (request.getParameter("ptu") != null ? "1" : "0") 
         + "','" + p(request.getParameter("id_sat")) + "') as ( err integer, res varchar, clave smallint )";
 
    	JRetFuncBas rfb = new JRetFuncBas();
      			
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_NOM_MOVIMIENTOS_NOMINA_DET ", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_MOVIMIENTOS_CAMBIAR", "NMOV|" + rfb.getClaveret() + "|||",rfb.getRes());
        
    	irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
    
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_NOM_MOVIMIENTOS_NOMINA_DET (\n";
        tbl += "Cuenta char(19) NOT NULL ,\n";
        tbl += "ID_Departamento char(4) NOT NULL \n";
        tbl += ");\n\n";

        HttpSession ses = request.getSession(true);
        JNomMovimNomSes pol = (JNomMovimNomSes)ses.getAttribute("nom_movimientos_dlg");

        for(int i = 0; i < pol.getPartidas().size(); i++)
        {
           tbl += "INSERT INTO _TMP_NOM_MOVIMIENTOS_NOMINA_DET\n";
           tbl += "VALUES( '" + p(JUtil.obtCuentas(pol.getPartida(i).getCuenta(),(byte)19)) + "','" +
               p(pol.getPartida(i).getID_Departamento()) + "');\n";
        }
        
    	String str = "select * from sp_nom_movimientos_nomina_agregar('" + p(request.getParameter("id_movimiento")) + "','DIN','" +
        p(request.getParameter("descripcion")) + "','" + (request.getParameter("esdeduccion") != null ? "1" : "0") + "','" +
        (request.getParameter("imss") != null ? "1" : "0") + "','" + (request.getParameter("ispt") != null ? "1" : "0") + "','" + (request.getParameter("dospor") != null ? "1" : "0")
         + "','" + (request.getParameter("sar") != null ? "1" : "0")  + "','" + (request.getParameter("infonavit") != null ? "1" : "0") + "','" + (request.getParameter("ptu") != null ? "1" : "0") 
         + "','" + p(request.getParameter("id_sat")) + "') as ( err integer, res varchar, clave smallint )";
 
    	JRetFuncBas rfb = new JRetFuncBas();
      			
        doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_NOM_MOVIMIENTOS_NOMINA_DET ", rfb);
      
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_MOVIMIENTOS_AGREGAR", "NMOV|" + rfb.getClaveret() + "|||",rfb.getRes());
        
    	irApag("/forsetiweb/nomina/nom_movimientos_dlg.jsp", request, response);
    	

    }

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("cuenta") != null && !request.getParameter("cuenta").equals("") &&
	       request.getParameter("id_departamento") != null && !request.getParameter("id_departamento").equals(""))
	    {
	      return true;
	    }
	    else
	    {
	        idmensaje = 1; mensaje = "PRECAUCION: Se deben enviar los parametros de cuenta y clave del departamento <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	}
	
	public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovimNomSes pol = (JNomMovimNomSes)ses.getAttribute("nom_movimientos_dlg");
	
	    idmensaje = pol.agregaPartida(request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), request.getParameter("id_departamento"), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    
	}
	
	public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovimNomSes pol = (JNomMovimNomSes)ses.getAttribute("nom_movimientos_dlg");
	
	    idmensaje = pol.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), request.getParameter("id_departamento"), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	}
	
	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomMovimNomSes pol = (JNomMovimNomSes)ses.getAttribute("nom_movimientos_dlg");
	
	    pol.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		
	}
   
    
}
