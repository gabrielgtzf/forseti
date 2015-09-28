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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.sets.JPlantillasExclusionesSet;
import forseti.sets.JMovimientosNomSet;
import forseti.sets.JAdmCompaniasSet;
import forseti.sets.JMasempSet;

import forseti.JUtil;

@SuppressWarnings("serial")
public class JNomPlantillasDlg extends JForsetiApl
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

      String nom_plantillas_dlg = "";
      request.setAttribute("nom_plantillas_dlg",nom_plantillas_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	if(request.getParameter("proceso").equals("AGREGAR_PLANTILLA"))
        {
    		// Revisa si tiene plantillas
    		if(!getSesion(request).getPermiso("NOM_PLANTILLAS_AGREGAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS_AGREGAR","NPLN||||",mensaje);
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
              irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
              return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
            {
              if(VerificarParametrosPartida(request, response))
            	  AgregarPartida(request, response);
              
              irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
              return;
            }
            else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
            {
               BorrarPartida(request, response);
               
               irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
               return;
            }
            else // Como el subproceso no es ENVIAR ni AGR_PART ni EDIT_PART ni BORR_PART, abre la ventana del proceso de AGREGADO para agregar `por primera vez
            {
              HttpSession ses = request.getSession(true);
              JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
              if(rec == null)
              {
                rec = new JNomPlantillasSes();
                ses.setAttribute("nom_plantillas_dlg", rec);
              }
              else
            	rec.resetear();

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
              return;
            }

    		
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_PLANTILLA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_PLANTILLAS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS","NPLN||||",mensaje);
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
              JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
              if(rec == null)
              {
                rec = new JNomPlantillasSes();
                ses.setAttribute("nom_plantillas_dlg", rec);
              }
              else
                rec.resetear();

              // Llena el permiso
              JPlantillasExclusionesSet set = new JPlantillasExclusionesSet(request);
              set.m_Where = "ID_Plantilla = '" + p(request.getParameter("id")) + "'";
		      set.Open();
              for(int i = 0; i < set.getNumRows(); i++)
              {
                rec.agregaPartida( set.getAbsRow(i).getID_Empleado(), set.getAbsRow(i).getNombre());
              }
             
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_PLANTILLA"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_PLANTILLAS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS_CAMBIAR","NPLN||||",mensaje);
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
            irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
            if(VerificarParametrosPartida(request, response))
              AgregarPartida(request, response);

            irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
            return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
             BorrarPartida(request, response);
             
             irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
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
                    JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
                    if(rec == null)
                    {
                      rec = new JNomPlantillasSes();
                      ses.setAttribute("nom_plantillas_dlg", rec);
                    }
                    else
                      rec.resetear();

                    // Llena el permiso
                    JPlantillasExclusionesSet set = new JPlantillasExclusionesSet(request);
                    set.m_Where = "ID_Plantilla = '" + p(request.getParameter("id")) + "'";
      		      	set.Open();
                    for(int i = 0; i < set.getNumRows(); i++)
                    {
                      rec.agregaPartida( set.getAbsRow(i).getID_Empleado(), set.getAbsRow(i).getNombre());
                    }
                   
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("ELIMINAR_PLANTILLA"))
    	{
    		// Revisa si tiene plantillas
    		if(!getSesion(request).getPermiso("NOM_PLANTILLAS_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS_ELIMINAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS_ELIMINAR","NPLN||||",mensaje);
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
        else if(request.getParameter("proceso").equals("INHIBIR_PLANTILLA"))
    	{
    		// Revisa si tiene plantillas
    		if(!getSesion(request).getPermiso("NOM_PLANTILLAS_ELIMINAR"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_PLANTILLAS_ELIMINAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_PLANTILLAS_ELIMINAR","NPLN||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
    		}

    		// Solicitud de envio a procesar
    		if(request.getParameter("id") != null)
    		{
    			String[] valoresParam = request.getParameterValues("id");
    			if(valoresParam.length == 1)
    			{
    				Inhibir(request, response);
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
	    if(request.getParameter("id_empleadoex") != null && !request.getParameter("id_empleadoex").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	        idmensaje = 1; mensaje = "PRECAUCION: Se debe enviar el empleado a excluir / incluir <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	}

    public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
	
	    idmensaje = rec.agregaPartida(request, p(request.getParameter("id_empleadoex")), mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    
	}
	
	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
		
	    rec.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	    
	}

    
    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("id_movimiento") != null && request.getParameter("fecha") != null &&
    	 request.getParameter("descripcion") != null && request.getParameter("aplicacion") != null && 
    	 request.getParameter("cantidad") != null && request.getParameter("exento") != null && 
    	!request.getParameter("id_movimiento").equals("") && !request.getParameter("fecha").equals("") && 
    	!request.getParameter("descripcion").equals("") && !request.getParameter("aplicacion").equals("")&& 
    	!request.getParameter("cantidad").equals("") && !request.getParameter("exento").equals(""))
      {
    	  JMovimientosNomSet set = new JMovimientosNomSet(request);
    	  set.m_Where = "Tipo_Movimiento = 'DIN' and ID_Movimiento = '" + p(request.getParameter("id_movimiento")) + "'";
    	  set.Open();
    	  
    	  if(set.getNumRows() < 1)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: El movimiento para la plantilla no existe o no es un movimiento dinámico<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
    	  if(request.getParameter("tipo_de_nomina").equals("-1"))
    	  {
    	   	  idmensaje = 3;
    	      mensaje += "ERROR: Debes proporcionar el tipo de nómina para el cálculo de esta plantilla <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
      
    	  if(request.getParameter("bempleado") == null && request.getParameter("bnumero") == null &&  
    			  request.getParameter("bcompania_sucursal") == null && request.getParameter("bnivel_confianza") == null)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: Debes proporcionar por lo menos una forma de aplicar esta plantilla<br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    	  
    	  int index = request.getParameter("cantidad").indexOf('.');
    	  if(request.getParameter("aplicacion").equals("1") && index != -1)
    	  {
    		  idmensaje = 3;
    	      mensaje += "ERROR: Como la aplicación es de dias de salario, la cantidad no puede ser decimal. Sustituye la cantidad a entero <br>";
    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	      return false;
    	  }
    		  
  	      HttpSession ses = request.getSession(true);
  	      JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
		
  	      if( request.getParameter("bempleado") != null )
    	  {
    		  if(request.getParameter("id_empleado") == null || request.getParameter("id_empleado").equals(""))
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: Debes proporcionar la clave del empleado (EMP)<br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    		  else
    		  {
    	    	  JMasempSet setemp = new JMasempSet(request);
    	    	  setemp.m_Where = "ID_Empleado = '" + p(request.getParameter("id_empleado")) + "'";
    	    	  setemp.Open();
    	    	  
    	    	  if(setemp.getNumRows() < 1)
    	    	  {
    	    		  idmensaje = 3;
    	    	      mensaje += "ERROR: El empleado no existe <br>";
    	    	      getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	              return false;
    	    	  }
    	   	  }
     	  }
    	  
    	  if( request.getParameter("bnumero") != null )
    	  {
    		  int numero = Integer.parseInt(request.getParameter("numero_nomina"));
    		  int ano = Integer.parseInt(request.getParameter("ano"));
    		  Calendar fecha = GregorianCalendar.getInstance();
              
    		  if(ano < 2000 || ano > (fecha.get(Calendar.YEAR)+1) || numero < 1 || numero > 53)
    	      {
    	    	  idmensaje = 3;
    	          mensaje += "ERROR: Debes proporcionar un año y número de nómina validos (NUM) <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false;
    	      }
    	  }
    	  
    	  if( request.getParameter("bcompania_sucursal") != null )
    	  {
    		  if(request.getParameter("compania_sucursal").equals("_FSI_CS"))
    	      {
    	    	  idmensaje = 3;
    	          mensaje += "ERROR: Debes proporcionar la nómina (NOM) <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false;
    	      }
    		  else if(request.getParameter("bempleado") != null)
    		  {
    	    	  idmensaje = 1;
    	          mensaje += "PRECAUCION: Como se selecciono por nómina, no debes seleccionar al empleado. En este caso, debes proporcionar la lista de empleados a incluir o excluir de la plantilla (NOM) <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false;
     		  }
    		  
     	  }
    	  
    	  if( request.getParameter("bnivel_confianza") != null )
    	  {
    		  if(request.getParameter("nivel_de_confianza").equals("-1"))
    	      {
    	    	  idmensaje = 3;
    	          mensaje += "ERROR: Debes proporcionar el nivel de confianza del empleado (NC) <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false;
    	      }
    		  else if(request.getParameter("bempleado") != null)
    		  {
    	    	  idmensaje = 1;
    	          mensaje += "PRECAUCION: Como se selecciono por empleado, ya no importa el nivel de confianza del empleado ya que este se define en su registro y no puede cambiar. (NC) <br>";
    	          getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	          return false;
 
    		  }
     	  }
    	  
    	  if(rec.numPartidas() > 0)
    	  {
    		  if(request.getParameter("bempleado") != null )
    		  {
    			  idmensaje = 3;
    			  mensaje += "ERROR: Una plantilla de empleado no puede tener inclusiones o exclusiones <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    		  else if(request.getParameter("bcompania_sucursal") != null )
    		  {
    			  byte ID_Compania, ID_Sucursal;
    			  boolean res = true;
    			  String mensajeemp = "";
    			  
    	    	  JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
    	    	  setcom.m_Where = "Descripcion = '" + p(request.getParameter("compania_sucursal")) + "'";
    	    	  setcom.Open();
    	    	  ID_Compania = setcom.getAbsRow(0).getID_Compania();
    	    	  ID_Sucursal = setcom.getAbsRow(0).getID_Sucursal();
    			  
    	    	  for(int i = 0; i < rec.numPartidas(); i++)
    	    	  {
    	    		  JNomPlantillasSesPart part = (JNomPlantillasSesPart)rec.getPartida(i);
    	    		  JMasempSet setemp = new JMasempSet(request);
        	    	  setemp.m_Where = "ID_Compania = '" + ID_Compania + "' and ID_Sucursal = '" + ID_Sucursal + "' and ID_Empleado = '" + p(part.getID_Empleado()) + "'";
        	    	  setemp.Open();
        	    	  
        	    	  if(setemp.getNumRows() < 1)
        	    	  {
        	    		  res = false; 
        	    		  mensajeemp +=  ", " + part.getID_Empleado();
        	    	  }
    	    	  }
    	    	  
    			  if(!res)
    			  {
    				  idmensaje = 3;
    				  mensaje += "ERROR: Las siguientes claves de empleado incluidos/excluidos, no pertenecen a la nómina seleccionada en la plantilla:<br> " + mensajeemp;
    				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				  return false;
    			  }
    		  }
    		  else
    		  {
    			 
    			  
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

    
    public void Inhibir(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	String str = "select * from  sp_nom_plantillas_inhibir( '" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave int)";
		
    	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, str, rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PLANTILLAS_ELIMINAR", "NPLN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PLANTILLAS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/caja_mensajes.jsp", request, response);

	}

    
    public void Eliminar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
  	  	String str = "select * from  sp_nom_plantillas_eliminar( '" + p(request.getParameter("id")) + "') as (err integer, res varchar, clave int)";
		
  	  	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, str, rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PLANTILLAS_ELIMINAR", "NPLN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PLANTILLAS").getEspecial() + "||",rfb.getRes());
		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  	}
    
    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	  	HttpSession ses = request.getSession(true);
	  	JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
	  	
	  	String tbl;
	  	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_PLANTILLAS_EXCLUSIONES (\n";
	  	tbl += " ID_Empleado char(6) NOT NULL \n";
	  	tbl += ");\n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "\n\ninsert into _TMP_PLANTILLAS_EXCLUSIONES\nvalues('" + p(rec.getPartida(i).getID_Empleado()) + "');";
	  	}
	
	    int aplicacion = Integer.parseInt(request.getParameter("aplicacion"));
	    
	  	String str = "select * from  sp_nom_plantillas_cambiar( '" + p(request.getParameter("id")) + "','" + p(request.getParameter("id_movimiento")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("descripcion")) + "','" +
	  	(request.getParameter("bempleado") != null ? "1" : "0") + "'," +  ( request.getParameter("bempleado") == null ? "null" : "'" + p(request.getParameter("id_empleado")) + "'" ) + ",'" +
	  	(request.getParameter("bnumero") != null ? "1" : "0") + "'," +  ( request.getParameter("bnumero") == null ? "null" : "'" + p(request.getParameter("ano")) + "'" ) + "," + ( request.getParameter("bnumero") == null ? "null" : "'" + p(request.getParameter("numero_nomina")) + "'" ) + ",'1','" + 
	  	p(request.getParameter("tipo_de_nomina")) + "','" + 
	  	(request.getParameter("bcompania_sucursal") != null ? "1" : "0") + "'," +  ( request.getParameter("bcompania_sucursal") == null ? "null" : "'" + p(request.getParameter("compania_sucursal")) + "'" ) + ",'" +
	  	(request.getParameter("bnivel_confianza") != null ? "1" : "0") + "'," +  ( request.getParameter("bnivel_confianza") == null ? "null" : "'" + p(request.getParameter("nivel_de_confianza")) + "'" ) + ",'" + 
	  	aplicacion + "'," +
	  	(aplicacion == 0 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + "," +
	  	(aplicacion == 1 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + "," +
	  	(aplicacion == 2 ? "'1'" : "null") + "," +
	  	(aplicacion == 2 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + ",'" +
	  	(request.getParameter("bexento") != null ? "1" : "0") + "'," +  ( request.getParameter("bexento") == null ? "null" : "'" + p(request.getParameter("exento")) + "'" ) + ",'" + 
	  	(aplicacion == 1 ? (request.getParameter("mixto") != null ? "1" : "0") : "0") + "','" +
	  	(request.getParameter("inclusiones") != null ? "1" : "0") + "') as (err integer, res varchar, clave int)";
		  		
	  	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_PLANTILLAS_EXCLUSIONES", rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PLANTILLAS_CAMBIAR", "NPLN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PLANTILLAS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
  	
    }
    
    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JNomPlantillasSes rec = (JNomPlantillasSes)ses.getAttribute("nom_plantillas_dlg");
    	
	  	String tbl;
	  	tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_PLANTILLAS_EXCLUSIONES (\n";
	  	tbl += " ID_Empleado char(6) NOT NULL \n";
	  	tbl += ");\n\n";
	  	
	  	for(int i = 0; i < rec.getPartidas().size(); i++)
	  	{
	  		tbl += "\n\ninsert into _TMP_PLANTILLAS_EXCLUSIONES\nvalues('" + p(rec.getPartida(i).getID_Empleado()) + "');";
	  	}
	  	
	  	int aplicacion = Integer.parseInt(request.getParameter("aplicacion"));
	        
	  	String str = "select * from  sp_nom_plantillas_agregar( '" + p(request.getParameter("id_movimiento")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("descripcion")) + "','" +
	  	(request.getParameter("bempleado") != null ? "1" : "0") + "'," +  ( request.getParameter("bempleado") == null ? "null" : "'" + p(request.getParameter("id_empleado")) + "'" ) + ",'" +
	  	(request.getParameter("bnumero") != null ? "1" : "0") + "'," +  ( request.getParameter("bnumero") == null ? "null" : "'" + p(request.getParameter("ano")) + "'" ) + "," + ( request.getParameter("bnumero") == null ? "null" : "'" + p(request.getParameter("numero_nomina")) + "'" ) + ",'1','" + 
	  	p(request.getParameter("tipo_de_nomina")) + "','" + 
	  	(request.getParameter("bcompania_sucursal") != null ? "1" : "0") + "'," +  ( request.getParameter("bcompania_sucursal") == null ? "null" : "'" + p(request.getParameter("compania_sucursal")) + "'" ) + ",'" +
	  	(request.getParameter("bnivel_confianza") != null ? "1" : "0") + "'," +  ( request.getParameter("bnivel_confianza") == null ? "null" : "'" + p(request.getParameter("nivel_de_confianza")) + "'" ) + ",'" + 
	  	aplicacion + "'," +
	  	(aplicacion == 0 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + "," +
	  	(aplicacion == 1 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + "," +
	  	(aplicacion == 2 ? "'1'" : "null") + "," +
	  	(aplicacion == 2 ? "'" + p(request.getParameter("cantidad")) + "'" : "null") + ",'" +
	  	(request.getParameter("bexento") != null ? "1" : "0") + "'," +  ( request.getParameter("bexento") == null ? "null" : "'" + p(request.getParameter("exento")) + "'" ) + ",'" + 
	  	(aplicacion == 1 ? (request.getParameter("mixto") != null ? "1" : "0") : "0") + "','" +
	  	(request.getParameter("inclusiones") != null ? "1" : "0") + "') as (err integer, res varchar, clave int)";
		
	  	JRetFuncBas rfb = new JRetFuncBas();
		doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_PLANTILLAS_EXCLUSIONES", rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_PLANTILLAS_AGREGAR", "NPLN|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_PLANTILLAS").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/nomina/nom_plantillas_dlg.jsp", request, response);
  	
    }

}