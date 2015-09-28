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
import forseti.sets.JAdmClientCXCConceptos;
import forseti.sets.JAdmInvservCostosConceptosSet;
import forseti.sets.JAdmProveeCXPConceptos;
import forseti.sets.JPublicContCatalogSetV2;

@SuppressWarnings("serial")
public class JContaEnlacesDlg extends JForsetiApl
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

      String conta_enlaces_dlg = "";
      request.setAttribute("conta_enlaces_dlg",conta_enlaces_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_ENLACE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_ENLACES_AGREGAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_ENLACES_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_ENLACES_AGREGAR","ENLA||||",mensaje);
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
            irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);
            return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_ENLACE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_ENLACES_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_ENLACES_CAMBIAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_ENLACES_CAMBIAR","ENLA||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(getSesion(request).getSesion("CONT_ENLACES").getEspecial().equals("CXP"))
              {
            	  JAdmProveeCXPConceptos set = new JAdmProveeCXPConceptos(request);
            	  set.m_Where = "ID_Concepto = '" + p(request.getParameter("id")) + "'";
          		  set.Open();
            	  if(set.getNumRows() > 0 && set.getAbsRow(0).getDeSistema())
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",1);
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
              if(getSesion(request).getSesion("CONT_ENLACES").getEspecial().equals("CXC"))
              {
            	  JAdmClientCXCConceptos set = new JAdmClientCXCConceptos(request);
            	  set.m_Where = "ID_Concepto = '" + p(request.getParameter("id")) + "'";
          		  set.Open();
            	  if(set.getNumRows() > 0 && set.getAbsRow(0).getDeSistema())
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",1);
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
              if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
              {
                // Verificacion
                if(VerificarParametros(request, response))
                {
                	Cambiar(request, response);
                	return;
                }
                irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);
                return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_ENLACE"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("CONT_ENLACES_ELIMINAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "CONT_ENLACES_ELIMINAR");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"CONT_ENLACES_ELIMINAR","ENLA||||",mensaje);
        	  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        	  return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
              if(getSesion(request).getSesion("CONT_ENLACES").getEspecial().equals("CXP"))
              {
            	  JAdmProveeCXPConceptos set = new JAdmProveeCXPConceptos(request);
            	  set.m_Where = "ID_Concepto = '" + p(request.getParameter("id")) + "'";
          		  set.Open();
            	  if(set.getNumRows() > 0 && set.getAbsRow(0).getDeSistema())
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR2",3);
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
              else if(getSesion(request).getSesion("CONT_ENLACES").getEspecial().equals("CXC"))
              {
            	  JAdmClientCXCConceptos set = new JAdmClientCXCConceptos(request);
            	  set.m_Where = "ID_Concepto = '" + p(request.getParameter("id")) + "'";
          		  set.Open();
            	  if(set.getNumRows() > 0 && set.getAbsRow(0).getDeSistema())
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR2",3);
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
              else if(getSesion(request).getSesion("CONT_ENLACES").getEspecial().equals("ALMACEN"))
              {
            	  JAdmInvservCostosConceptosSet set = new JAdmInvservCostosConceptosSet(request);
            	  set.m_Where = "ID_Concepto = '" + p(request.getParameter("id")) + "'";
          		  set.Open();
            	  if(set.getNumRows() > 0 && set.getAbsRow(0).getDeSistema())
            	  {
            		  idmensaje = 1; mensaje += JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR2",3);
            		  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		  return;
            	  }
              }
             
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
    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("idconcepto") != null && request.getParameter("descripcion") != null
          && request.getParameter("tipo") != null  && request.getParameter("cuenta") != null &&
          !request.getParameter("idconcepto").equals("") && !request.getParameter("descripcion").equals("")
          && !request.getParameter("tipo").equals(""))
      {
    	  String ent = getSesion(request).getSesion("CONT_ENLACES").getEspecial();
    	  String tipo = request.getParameter("tipo");
		  int idconcepto = Integer.parseInt(request.getParameter("idconcepto"));
		 
    	  if(ent.equals("ALMACEN"))
    	  {
    		  if(tipo.equals("ENT"))
    		  {
    			  if((idconcepto >= 0 && idconcepto < 50) || (idconcepto > 99 && idconcepto < 150))
    			  {
    				  //OK
    			  }
    			  else
    			  {
    				  idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",2);//"PRECAUCION: Un concepto de entrada debe establecerse entre 1 - 49 ó 100 - 149<br>";
    	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	              return false; 
    			  }
    		  }
    		  else if(tipo.equals("SAL"))
    		  {
       			  if((idconcepto > 49 && idconcepto < 100) || (idconcepto > 149 && idconcepto < 200))
    			  {
    				  // OK
    			  }
    			  else
    			  {
    				  idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",3);//"PRECAUCION: Un concepto de salida debe establecerse entre 50 - 99 ó 150 - 199<br>";
    	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	              return false; 
    			  }
   			  
    		  }
    		  else
    		  {
   				  idmensaje = 3; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR2",1);//"ERROR: No se ha especificado si es entrada o salida<br>";
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              return false; 
	   		  }
    	  }
    	  else if(ent.equals("CXP") || ent.equals("CXC"))
    	  {
    		  if(tipo.equals("ALT"))
    		  {
    			  if(idconcepto > 0 && idconcepto < 100)
    			  {
    				  // OK
    			  }
    			  else
    			  {
    				  idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",4);//"PRECAUCION: Un concepto de Alta debe establecerse entre 1 y 99<br>";
    	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	              return false; 
    			  }
    		  }
    		  else if(tipo.equals("SAL"))
    		  {
       			  if(idconcepto > 99 && idconcepto < 200)
    			  {
    				  // OK
    			  }
    			  else
    			  {
    				  idmensaje = 1; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR",5);//"PRECAUCION: Un concepto de Saldo debe establecerse entre 100 y 199<br>";
    	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	              return false; 
    			  }
   			  
    		  }
    		  else
    		  {
   				  idmensaje = 3; mensaje = JUtil.Msj("CEF", "CONT_ENLACES", "DLG", "MSJ-PROCERR2",2);//"ERROR: No se ha especificado si es alta o saldo<br>";
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              return false; 
	   		  }
    	  }
    	  
          // Verifica la cuenta
    	  if(!request.getParameter("cuenta").equals(""))
    	  {
    		  JPublicContCatalogSetV2 num = new JPublicContCatalogSetV2(request);
    		  num.m_Where = "Numero = '" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "'";
    		  num.Open();

    		  if(num.getNumRows() > 0)
    		  {
    			  if(num.getAbsRow(0).getAcum() == true)
    			  {
    				  idmensaje = 1; mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES", "DLG", "MSJ-PROCERR2",5); //"PRECAUCION: La cuenta contable para este concepto existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
    				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				  return false;
    			  }
    		  }
    		  else
    		  {
    			  idmensaje = 3; mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES", "DLG", "MSJ-PROCERR3"); //"ERROR: La cuenta contable para este concepto no existe <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
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

    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String ent = getSesion(request).getSesion("CONT_ENLACES").getEspecial();
    	int idconcepto = Integer.parseInt(request.getParameter("id"));
    	String str = "SELECT * FROM ";
    	      
    	if(ent.equals("ALMACEN"))
    		str += "sp_invserv_costos_conceptos_eliminar('" + idconcepto + "'";
    	else if(ent.equals("CXP"))
    		str += "sp_provee_cxp_conceptos_eliminar('" + idconcepto + "'";
    	else if(ent.equals("CXC"))
    		str += "sp_client_cxc_conceptos_eliminar('" + idconcepto + "'";

    	str += ") as ( err integer, res varchar, clave smallint ) ";
    	      
    	JRetFuncBas rfb = new JRetFuncBas();
    	  	
    	doCallStoredProcedure(request, response, str, rfb);
    	      
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_ENLACES_ELIMINAR","ENLA|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
   	  String ent = getSesion(request).getSesion("CONT_ENLACES").getEspecial();
	  String tipo = p(request.getParameter("tipo"));
	  int idconcepto = Integer.parseInt(request.getParameter("idconcepto"));
      String str = "SELECT * FROM ";
      
      if(ent.equals("ALMACEN"))
	  	str += "sp_invserv_costos_conceptos_cambiar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + (request.getParameter("recosto") == null ? "0" : "1") + "','" + 
	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "'");
      else if(ent.equals("CXP"))
  	  	str += "sp_provee_cxp_conceptos_cambiar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + 
	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "'");
      else if(ent.equals("CXC"))
   	  	str += "sp_client_cxc_conceptos_cambiar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + 
	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "'");

      str += ") as ( err integer, res varchar, clave smallint ) ";
      
      JRetFuncBas rfb = new JRetFuncBas();
  	
      doCallStoredProcedure(request, response, str, rfb);
      
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_ENLACES_CAMBIAR","ENLA|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);
      
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
     	  String ent = getSesion(request).getSesion("CONT_ENLACES").getEspecial();
    	  String tipo = p(request.getParameter("tipo"));
    	  int idconcepto = Integer.parseInt(request.getParameter("idconcepto"));
          String str = "SELECT * FROM ";
          
          if(ent.equals("ALMACEN"))
    	  	str += "sp_invserv_costos_conceptos_agregar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + (request.getParameter("recosto") == null ? "0" : "1") + "','" + 
    	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19) + "'");
          else if(ent.equals("CXP"))
      	  	str += "sp_provee_cxp_conceptos_agregar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + 
    	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19) + "'");
          else if(ent.equals("CXC"))
       	  	str += "sp_client_cxc_conceptos_agregar('" + idconcepto + "','" + p(request.getParameter("descripcion")) + "','" + 
    	  	tipo + "'," + (request.getParameter("cuenta").equals("") ? "null" : "'" + JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19) + "'");

          str += ") as ( err integer, res varchar, clave smallint ) ";
          
          JRetFuncBas rfb = new JRetFuncBas();
      	
          doCallStoredProcedure(request, response, str, rfb);
          
          RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(),"CONT_ENLACES_AGREGAR","ENLA|" + rfb.getClaveret() + "|||",rfb.getRes());
          irApag("/forsetiweb/contabilidad/conta_enlaces_dlg.jsp", request, response);

    }
 
}
