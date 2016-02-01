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
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmCompaniasSet;
import forseti.sets.JMasempSet;
import forseti.sets.JNominaEntidadesSetIds;

@SuppressWarnings("serial")
public class JNomEmpleadosDlg extends JForsetiApl
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

      String nom_empleados_dlg = "";
      request.setAttribute("nom_empleados_dlg",nom_empleados_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        // revisa por las entidades
    	JNominaEntidadesSetIds setids = new  JNominaEntidadesSetIds(request,usuario,getSesion(request).getSesion("NOM_EMPLEADOS").getEspecial());
        setids.Open();

        if(setids.getNumRows() < 1)
        {
      		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP||||",mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_EMPLEADO") && request.getParameter("id") != null)
        {
        	JMasempSet set = new JMasempSet(request);
        	set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + setids.getAbsRow(0).getID_Sucursal() + "' and ID_Empleado = '" + p(request.getParameter("id")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getID_Sucursal() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_EMPLEADO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_EMPLEADOS_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS_AGREGAR","NEMP||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  if(VerificarParametros(request, response))
        	  {
        		  AgregarCambiar(request, response, "agregar");
        		  return;
        	  }
        	  irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
              return;
          }
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_EMPLEADO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_EMPLEADOS"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS","NEMP||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
              return;
          }
 
          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_EMPLEADO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("NOM_EMPLEADOS_CAMBIAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "NOM_EMPLEADOS_CAMBIAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"NOM_EMPLEADOS_CAMBIAR","NEMP||||",mensaje);
              irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
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
            	  if(VerificarParametros(request, response))
            	  {
            		  AgregarCambiar(request, response, "cambiar");
            		  return;
            	  }
            	  irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
            	  return;
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
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
      if(   request.getParameter("id_empleado") != null && 
    		request.getParameter("id_turno") != null && 
    		request.getParameter("id_departamento") != null && 
    		request.getParameter("puesto") != null && 
    		request.getParameter("nombre") != null && 
    		request.getParameter("apellido_paterno") != null && 
    		request.getParameter("apellido_materno") != null && 
    		request.getParameter("fecha_de_nacimiento") != null && 
    		request.getParameter("rfc_letras") != null && 
    		request.getParameter("rfc_fecha") != null && 
    		request.getParameter("rfc_homoclave") != null && 
    		request.getParameter("rfc_digito") != null && 
    		request.getParameter("num_registro_imss") != null && 
    		request.getParameter("curp") != null && 
    		request.getParameter("fecha_de_ingreso") != null && 
    		request.getParameter("cuenta_bancaria") != null && 
    		request.getParameter("status") != null && 
    		request.getParameter("fecha_para_liquidaciones") != null && 
    		request.getParameter("motivo_baja") != null && 
    	    request.getParameter("salario_diario") != null && 
    		request.getParameter("salario_nominal") != null && 
    		request.getParameter("salario_por_hora") != null && 
    		request.getParameter("salario_integrado") != null && 
    		request.getParameter("salario_mixto") != null && 
    	    request.getParameter("id_categoria") != null && 
    		request.getParameter("calle") != null && 
    		request.getParameter("numero") != null && 
    		request.getParameter("colonia") != null && 
    		request.getParameter("codigo_postal") != null && 
    		request.getParameter("delegacion") != null && 
    		request.getParameter("noint") != null &&
    		request.getParameter("localidad") != null &&
    		request.getParameter("estado") != null &&
    		request.getParameter("pais") != null &&
    		request.getParameter("id_satbanco") != null && 
    	    request.getParameter("en_accidente_avisar") != null && 
    		request.getParameter("ultimo_trabajo") != null && 
    		request.getParameter("recomendado_por") != null && 
    		request.getParameter("estado_civil") != null && 
    		request.getParameter("nombre_esposo") != null && 
    		request.getParameter("nombre_padre") != null && 
    		request.getParameter("nombre_madre") != null && 
    		request.getParameter("escolaridad") != null && 
    	    request.getParameter("fecha_alta_infonavit") != null && 
    		request.getParameter("registro_infonavit") != null && 
    		request.getParameter("prestamo_infonavit") != null && 
    		request.getParameter("porcentaje_descuento") != null && 
    		request.getParameter("prestamo_vsm") != null && 
    		request.getParameter("descuento_vsm") != null && 
    	    request.getParameter("importe_vales_de_despensa") != null && 	  
    	    request.getParameter("smtp") != null && 	  
    	    request.getParameter("email") != null && 	  
    	    request.getParameter("pcs") != null && 	  
    	    	    	    	    
    	    !request.getParameter("id_empleado").equals("") && 
    	    !request.getParameter("id_turno").equals("") && 
    	    !request.getParameter("id_departamento").equals("") && 
    	    !request.getParameter("nombre").equals("") && 
    	    !request.getParameter("apellido_paterno").equals("") && 
    	    !request.getParameter("apellido_materno").equals("") && 
    	    !request.getParameter("fecha_de_nacimiento").equals("") && 
    	    !request.getParameter("rfc_letras").equals("") && 
    	    !request.getParameter("rfc_fecha").equals("") && 
    	    !request.getParameter("fecha_de_ingreso").equals("") && 
    	    !request.getParameter("status").equals("") && 
    	    !request.getParameter("salario_diario").equals("") && 
    	    !request.getParameter("salario_nominal").equals("") && 
    	    !request.getParameter("salario_por_hora").equals("") && 
    	    !request.getParameter("salario_integrado").equals("") && 
    	    !request.getParameter("salario_mixto").equals("") && 
    	    !request.getParameter("id_categoria").equals("") && 
    	    !request.getParameter("prestamo_infonavit").equals("") && 
    	    !request.getParameter("porcentaje_descuento").equals("") && 
    	    !request.getParameter("prestamo_vsm").equals("") && 
    	    !request.getParameter("descuento_vsm").equals("") && 
    	    !request.getParameter("importe_vales_de_despensa").equals("") &&
    	    !request.getParameter("smtp").equals("") && 			
    	  	!request.getParameter("pcs").equals("") ) 	
      {
    	if(request.getParameter("id_empleado").length() != 6 || !request.getParameter("id_empleado").matches("\\w{6}"))
    	{
    		idmensaje = 1; mensaje = "PRECAUCION: La clave del empleado, debe constar de 6 letras y/o digitos exactos. Se sugiere usar las iniciales del RFC mas dos posiciones para el número del empleado en caso de tener iniciales iguales dos o mas empleados. Por Ej: GUFG01, GUFG02, etc...<br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
    	}
    	
    	if(request.getParameter("clave_alta_infonavit") != null)
    	{
    		if(request.getParameter("fecha_alta_infonavit").equals(""))
    		{
    			idmensaje = 1; mensaje = "PRECAUCION: Como se esta registrando en el infonavit, la fecha de alta del infonavit NO debe ser nula <br>";
    	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	        return false;
    		}
    	}
    	
    	if(request.getParameter("status").equals("2"))
    	{
    		if(request.getParameter("fecha_para_liquidaciones").equals(""))
    		{
    			idmensaje = 1; mensaje = "PRECAUCION: Como el Status es BAJA, la fecha de baja NO debe ser nula <br>";
    	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	        return false;
    		}
    	}
    	
    	if(!request.getParameter("id_satbanco").matches("\\d{3}"))
    	{
    		idmensaje = 3; mensaje = "ERROR: La clave del banco para el SAT debe constar de tres digitos exactamente <br>";
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

    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
   	  	JAdmCompaniasSet setcom = new JAdmCompaniasSet(request);
	  	setcom.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + getSesion(request).getSesion("NOM_EMPLEADOS").getEspecial() + "'";
	  	setcom.Open();

	  	
    	String str = "select * from " + (proceso.equals("agregar") ? " sp_nom_masemp_agregar(" : " sp_nom_masemp_cambiar(");
    	str += "'" + p(request.getParameter("id_empleado")) + "','" + p(setcom.getAbsRow(0).getDescripcion()) + "','" + p(request.getParameter("id_departamento")) + "','" +
    	p(request.getParameter("id_turno")) + "','0','" + p(request.getParameter("id_categoria")) + "','" + p(request.getParameter("nombre")) + "','" +
    	p(request.getParameter("apellido_paterno")) + "','" + p(request.getParameter("apellido_materno")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha_de_nacimiento"))) + "','" +
    	p(JUtil.obtFechaSQL(request.getParameter("fecha_de_ingreso"))) + "',null,'1','" + p(request.getParameter("rfc_letras")) + "','" +
    	p(request.getParameter("rfc_fecha")) + "','" + p(request.getParameter("rfc_homoclave")) + "','" + p(request.getParameter("rfc_digito")) + "','" +
    	p(request.getParameter("curp")) + "','" + (request.getParameter("calculosimplificado") != null ? "1" : "0") + "','" + setcom.getAbsRow(0).getTipo() + "',/*Jornada*/'1','" + (request.getParameter("sindicalizado") != null ? "1" : "0") + 
    	"',/*Horas_por_Jornada*/'9','" + p(request.getParameter("status")) + "','" + p(request.getParameter("motivo_baja")) + "'," + 
    	"/*reparto_de_utilidades*/'0',/*Premio_de_Puntualidad*/'0','" + (request.getParameter("castigo_impuntualidad") != null ? "1" : "0") + "','" +
    	p(request.getParameter("puesto")) + "','" + p(request.getParameter("salario_nominal")) + "','" + p(request.getParameter("salario_diario")) + "','" +
    	p(request.getParameter("salario_por_hora")) + "','" + p(request.getParameter("salario_integrado")) + "','" +  (request.getParameter("aplica_horas_extras") != null ? "1" : "0") + "'," +
        "/*Fecha_Vacaciones*/'" + p(JUtil.obtFechaSQL(request.getParameter("fecha_de_ingreso"))) + "',/*Dias_Vacaciones*/'0', /*Prima_de_Vacaciones*/ '0','" +
        p(request.getParameter("num_registro_imss")) + "',/*Jefe_Inmediato*/'','" + p(request.getParameter("calle")) + "','" + 
        p(request.getParameter("numero")) + "','" + p(request.getParameter("colonia")) + "','" + p(request.getParameter("codigo_postal")) + "','" + p(request.getParameter("delegacion")) + "','" +   
        p(request.getParameter("estado_civil")) + "','" + p(request.getParameter("nombre_esposo")) + "',/*Num_de_Hijos*/'0',/*Nombre_de_Hijos*/'','" +
        p(request.getParameter("nombre_padre")) + "',/*Vivo*/'1','" + p(request.getParameter("nombre_madre")) + "',/*Viva*/'1','" +  p(request.getParameter("escolaridad")) + "',/*Trabajo_Anterior_Grupo*/'    ','" +
        p(request.getParameter("ultimo_trabajo")) + "','" + p(request.getParameter("recomendado_por")) + "','" + p(request.getParameter("en_accidente_avisar")) + "'," + ( request.getParameter("status").equals("0") ? "null" : "'" + p(JUtil.obtFechaSQL(request.getParameter("fecha_para_liquidaciones"))) + "'" ) +   
    	",/*Fecha_Cambio_Obrero_Empleado*/ null,'" + p(request.getParameter("cuenta_bancaria")) + "',/*Historial_Puestos*/ '','" + 
    	p(request.getParameter("registro_infonavit")) + "','" + p(request.getParameter("prestamo_infonavit")) + "','" + p(request.getParameter("porcentaje_descuento")) + "','" +
    	p(request.getParameter("prestamo_vsm")) + "','" + p(request.getParameter("descuento_vsm")) + "'," +  ( request.getParameter("clave_alta_infonavit") == null ? "null" : "'" + p(JUtil.obtFechaSQL(request.getParameter("fecha_alta_infonavit"))) + "'" ) + 
    	",/*Fecha_Liquidacion_Infonavit*/ null,"  +
    	"/*Registro_Fonacot*/ '', /*Numero_de_Credito*/ '','" + (request.getParameter("prestamo_fonacot") != null ? "1" : "0") + "','" + 
    	(request.getParameter("ayuda_vales_de_despensa") != null ? "1" : "0") + "','" + p(request.getParameter("importe_vales_de_despensa")) + "',/*ID_XAction*/ '','" + (request.getParameter("compensacionanual") != null ? "1" : "0") + "','" + p(request.getParameter("salario_mixto")) + "','" + (request.getParameter("calculo_mixto") != null ? "1" : "0") + "',/*CompensacionAnualFija*/ '0.0'" +
    	",'" + p(request.getParameter("regimen")) + "','" + p(request.getParameter("id_satbanco")) + "','" + p(request.getParameter("noint")) + "','" + p(request.getParameter("localidad")) + "','" + p(request.getParameter("estado")) + "','" + p(request.getParameter("pais")) + "','" + p(request.getParameter("smtp")) + "','" + p(request.getParameter("email")) + "','" +
    	p(request.getParameter("pcs")) + "') as (err integer, res varchar, clave bpchar)";
     
    	JRetFuncBas rfb = new JRetFuncBas();
		
		doCallStoredProcedure(request, response, str, rfb);
		            
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "NOM_EMPLEADOS_" + (proceso.equals("agregar") ? "AGREGAR" : "CAMBIAR"), "NEMP|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("NOM_EMPLEADOS").getEspecial() + "||",rfb.getRes());

        irApag("/forsetiweb/nomina/nom_empleados_dlg.jsp", request, response);
     
    }

}
