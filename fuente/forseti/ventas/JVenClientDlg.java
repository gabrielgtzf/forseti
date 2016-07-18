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
package forseti.ventas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JClientClientMasSetV2;
import forseti.sets.JClientClientSetV2;
import forseti.sets.JPublicCXCConeSetV2;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JVentasEntidadesSetIdsV2;
import forseti.sets.JVendedoresSet;

@SuppressWarnings("serial")
public class JVenClientDlg extends JForsetiApl
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

      String ven_client_dlg = "";
      request.setAttribute("ven_client_dlg",ven_client_dlg);

      String mensaje = ""; short idmensaje = -1;
      String usuario = getSesion(request).getID_Usuario();

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        // revisa por las entidades
        JVentasEntidadesSetIdsV2 setids = new JVentasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion("VEN_CLIENT").getEspecial());
        setids.Open();
        
        if(setids.getNumRows() < 1)
        {
        	 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT");
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_CLIENT", "VCLI" + "||||",mensaje);
             irApag("/forsetiweb/caja_mensajes.jsp", request, response);
             return;
        }
        
        // Revisa por intento de intrusion (Salto de permiso de entidad)
        if(!request.getParameter("proceso").equals("AGREGAR_CLIENTE") && request.getParameter("id") != null)
        {
        	JClientClientSetV2 set = new JClientClientSetV2(request);
        	set.m_Where = "ID_EntidadVenta = '" + setids.getAbsRow(0).getID_Entidad() + "' and Clave = '" + p(request.getParameter("id")) + "'";
        	set.Open();
        	if(set.getNumRows() < 1)
        	{
        		idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT");
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getID_Entidad() + "||",mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        
        if(request.getParameter("proceso").equals("AGREGAR_CLIENTE"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_CLIENT_AGREGAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CLIENT_AGREGAR","VCLI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }

            // Solicitud de envio a procesar
            if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
            {
            	if(VerificarParametros(request, response))
            	{
            		Agregar(request, response);
            		return;
            	}
            
            	irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
            	return;
            }	
            else
            {
            	JClientClientSetV2 set = new JClientClientSetV2(request);
            	String sql = "select * FROM view_client_client_modulo where id_tipo = 'CL' and id_entidadventa = " + setids.getAbsRow(0).getID_Entidad() + " order by numero desc limit 1";
            	set.setSQL(sql);
            	set.Open();
            	if(set.getNumRows() == 1)
            		request.setAttribute("id_numero",Integer.toString((set.getAbsRow(0).getNumero() + 1)));
            	else
            		request.setAttribute("id_numero","");

            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
            	return;
            }
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_CLIENTE"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_CLIENT"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI||||",mensaje);
                irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                return;
            }
 
            // Solicitud de envio a procesar
            if(request.getParameter("id") != null)
            {
            	String[] valoresParam = request.getParameterValues("id");
            	if(valoresParam.length == 1)
            	{
            		RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"VEN_CLIENT","VCLI|" + request.getParameter("id") + "|" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "||","");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
            		return;
            	}
            	else
            	{
            		idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_CLIENT_CAMBIAR"))
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CLIENT_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_CLIENT_CAMBIAR","VCLI||||",mensaje);
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
            			if(VerificarParametros(request, response))
            			{
            				Cambiar(request, response);
            				return;
            			}
            		
            			irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
            			return;
            		}
            		else
            		{
            			getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
            			return;
            		}
            	}
            	else
            	{
            		idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            		getSesion(request).setID_Mensaje(idmensaje, mensaje);
            		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            		return;
            	}
            }
            else
            {
            	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            	return;
            }
        }
        else if(request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_CXC_PAGAR")) 
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_PAGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_CXC_PAGAR","VCXC||||",mensaje);
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
        				request.setAttribute("fsipg_tipo","ventas");
        				request.setAttribute("fsipg_proc","deposito");
        				request.setAttribute("fsipg_ident",getSesion(request).getSesion("VEN_CLIENT").getEspecial());
        				if(VerificarParametrosAlta(request, response) && VerificarPago(request, response))
        				{
        					AltaAnticipo(request, response);
        					return;
        				}
        				irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
        				return;
        			}
        			else
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
        				irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
        				return;
        			}
        		}
        		else
        		{
        			idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
        			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        			return;
        		}
        	}
        	else
        	{
        		idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
        	}
        }
        else if(request.getParameter("proceso").equals("AGREGAR_CUENTA"))
        {
        	// Revisa si tiene permisos
            if(!getSesion(request).getPermiso("VEN_CXC_AGREGAR")) 
            {
          	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "VEN_CXC_AGREGAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "VEN_CXC_AGREGAR","VCXC||||",mensaje);
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
                if(VerificarParametrosAlta(request, response))
                {
                    AltaCXC(request, response);
                    return;
                }
                irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
                return;
              }
              else
              {
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
                return;
              }
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        		return;
            }
          }
          else
          {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
      		getSesion(request).setID_Mensaje(idmensaje, mensaje);
      		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      		return;
          }
        }
        else
        {
        	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: Solo se permite consultar una póliza a la vez <br>";
  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
  		return;
      }

    }

    public boolean VerificarParametrosAlta(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if( request.getParameter("clave") != null && request.getParameter("fecha") != null && request.getParameter("concepto") != null &&
          request.getParameter("tc") != null && request.getParameter("cantidad") != null && request.getParameter("idmoneda") != null &&
          !request.getParameter("clave").equals("") && !request.getParameter("fecha").equals("") && !request.getParameter("concepto").equals("") &&
          !request.getParameter("tc").equals("") && !request.getParameter("cantidad").equals("") && !request.getParameter("idmoneda").equals("") )
     {
        // Verifica que el cliente de la cuenta realmente pertenece a esta entidad
        JClientClientMasSetV2 pr = new JClientClientMasSetV2(request);
        pr.m_Where = "ID_Clave = '" + p(request.getParameter("id")) + "'";
        pr.Open();
        if(pr.getAbsRow(0).getID_EntidadVenta() != Integer.parseInt(getSesion(request).getSesion("VEN_CLIENT").getEspecial()) )
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "VEN_CLIENT","DLG","MSJ-PROCERR",1); //ERROR: El cliente no pertenece a la entidad mandada. <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        // Por ultimo verifica la cantidad el tc y el id concepto
        float cantidad = Float.parseFloat(request.getParameter("cantidad"));
        float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
        if (cantidad <= 0.00 || tc < 0.0000)
        {
           idmensaje = 3;
           mensaje = JUtil.Msj("GLB","CXCP","DLG","MSJ-PROCERR",1); //ERROR: La cantidad o el tipo de cambio están mal <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
        }

        // solo verifica el id concepto para cuenta y no para anticipo
        if(!request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
        {
          JPublicCXCConeSetV2 con = new JPublicCXCConeSetV2(request);
          con.m_Where = "ID_Concepto = '" + p(request.getParameter("clave")) + "' and Tipo = 'ALT' and DeSistema = '0'";
          con.Open();
          if(con.getNumRows() < 1)
          {
            idmensaje = 3;
            mensaje = JUtil.Msj("GLB","CXCP","DLG","MSJ-PROCERR",2); //"ERROR: No existe el concepto del alta<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
          }
        }


        return true;
      }
      else
      {
        idmensaje = 1; mensaje = JUtil.Msj("GLB","GLB","GLB","PARAM-NULO");
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        return false;
      }
    }

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("numero") != null && 
         request.getParameter("dias") != null && request.getParameter("limite") != null && request.getParameter("descuento") != null &&
         request.getParameter("cuenta") != null && request.getParameter("nombre") != null && request.getParameter("rfc") != null &&
         request.getParameter("direccion") != null && request.getParameter("colonia") != null && request.getParameter("poblacion") != null &&
         request.getParameter("cp") != null && request.getParameter("fax") != null && request.getParameter("tel") != null &&
         request.getParameter("correo") != null && request.getParameter("atnpagos") != null && request.getParameter("atncompras") != null &&
         request.getParameter("fecha") != null && request.getParameter("obs") != null && request.getParameter("idvendedor") != null &&
         request.getParameter("noext") != null && request.getParameter("noint") != null && request.getParameter("municipio") != null &&
         request.getParameter("estado") != null && request.getParameter("pais") != null && request.getParameter("metododepago") != null &&
         request.getParameter("status") != null && request.getParameter("id_satbanco") != null && request.getParameter("smtp") != null && request.getParameter("pedimento") != null &&
         !request.getParameter("numero").equals("") &&  
         !request.getParameter("dias").equals("") && !request.getParameter("limite").equals("") && !request.getParameter("descuento").equals("") &&
         !request.getParameter("cuenta").equals("") && !request.getParameter("nombre").equals("") && !request.getParameter("fecha").equals("") && 
         !request.getParameter("idvendedor").equals("") && !request.getParameter("status").equals("") && !request.getParameter("smtp").equals("") )

      {
    	if(!request.getParameter("rfc").equals(""))
      	{
    		if(request.getParameter("pais").equals("MEX"))
			{
	      		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
	      		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
	      		{
	      			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
	      			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	      			return false;
	      		}
			}
      	}
    	  
        if(request.getParameter("proceso").equals("CAMBIAR_CLIENTE"))
        {
           // Verifica que el cliente de la cuenta realmente pertenece a esta entidad
           JClientClientMasSetV2 pr = new JClientClientMasSetV2(request);
           pr.m_Where = "ID_Clave = '" + p(request.getParameter("id")) + "'";
           pr.Open();
           if(pr.getAbsRow(0).getID_EntidadVenta() != Integer.parseInt(getSesion(request).getSesion("VEN_CLIENT").getEspecial()) )
           {
             idmensaje = 3;
             mensaje = JUtil.Msj("CEF", "VEN_CLIENT","DLG","MSJ-PROCERR",1); //"ERROR: El cliente no pertenece a la entidad mandada. <br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             return false;
           }
        }
  
        // Verifica la cuenta
        JPublicContCatalogSetV2 num = new JPublicContCatalogSetV2(request);
        num.m_Where = "Numero = '" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "'";
        num.Open();

        if(num.getNumRows() > 0)
        {
          if(num.getAbsRow(0).getAcum() == true)
          {
            idmensaje = 1;
            mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES","DLG","MSJ-PROCERR2",5);//"PRECAUCION: La cuenta contable para este cliente existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
          }
        }
        else
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES","DLG","MSJ-PROCERR3",1);//"ERROR: La cuenta contable para este cliente no existe <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        //Verifica el vendedor
        JVendedoresSet setven  = new JVendedoresSet(request);
        setven.m_Where = "ID_Vendedor = '" + p(request.getParameter("idvendedor")) + "'";
        setven.Open();
        if(setven.getNumRows() == 0)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "VEN_CLIENT","DLG","MSJ-PROCERR",2); //"ERROR: El vendedor especificado no existe <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        // Por ultimo verifica el descuento
        float descuento = Float.parseFloat(request.getParameter("descuento"));
        if (descuento < 0.00 || descuento > 100.00)
        {
           idmensaje = 3;
           mensaje = JUtil.Msj("CEF", "VEN_CLIENT","DLG","MSJ-PROCERR",3); //"ERROR: El descuento no puede ser menor que 0.00 <br>";
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
    	String rfc, registro_tributario;
    	if(request.getParameter("pais").equals("MEX"))
    	{
    		rfc = p(JUtil.fco(JUtil.frfc(request.getParameter("rfc"))));
    		registro_tributario = rfc;
    	}
    	else
    	{
    		rfc = "XEXX010101000";
    		registro_tributario = p(request.getParameter("rfc"));
    	}
    	String str = "select * from sp_client_client_cambiar('CL','" + p(request.getParameter("id")) + "','" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "',null,'" + p(request.getParameter("nombre")) + "','0.00','" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "','" + rfc + "','" +
	              p(request.getParameter("atncompras")) + "','" + p(request.getParameter("atnpagos")) + "','" + p(request.getParameter("colonia")) + "','" + p(request.getParameter("cp")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("correo")) + "','" + p(request.getParameter("fax")) + "','" + p(request.getParameter("poblacion")) +
	              "','" + p(request.getParameter("tel")) + "','0.00','" + p(request.getParameter("descuento")) + "','" + p(request.getParameter("dias")) + "','" + p(request.getParameter("limite")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("obs")) + "','" + (request.getParameter("prespmostr") != null ? "1" : "0") + "','" + p(request.getParameter("idvendedor"))
	              + "','" + p(request.getParameter("noext")) + "','" + p(request.getParameter("noint")) + "','" + p(request.getParameter("municipio")) + "','" + p(request.getParameter("estado")) + "','" + p(request.getParameter("pais")) + "','" + p(request.getParameter("metododepago")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("id_satbanco")) + "','" + p(request.getParameter("smtp")) + "','" + registro_tributario + "','" + p(request.getParameter("pedimento")) + "') as ( err integer, res varchar, clave integer );";
	     
    	JRetFuncBas rfb = new JRetFuncBas();
			
    	doCallStoredProcedure(request, response, str, rfb);
	 
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CLIENT_CAMBIAR", "VCLI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
	         
    }

    public void AltaCXC(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

    	String str = "select * from sp_client_cxc_alta('" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','CL','" + p(request.getParameter("id")) + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + total + "','" + p(request.getParameter("clave")) + "',null,null,null) as ( err integer, res varchar, clave integer );";
    													//_ID_Entidad smallint,                                                   _Fecha timestamp,                      _ID_TipoClient char(2), _ID_ClaveClient int,                  _Concepto varchar(80),                              _Moneda smallint,                    _TC numeric,             _Total numeric,                  _Cantidad numeric,            _ID_Concepto smallint)

    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_AGREGAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
   
    }

    public void AltaAnticipo(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
      float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

      String str = "select * from sp_client_cxc_prestamo('" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','CL','" + p(request.getParameter("id")) + "','" + p(request.getParameter("concepto")) + "','" + p((String)request.getAttribute("fsipg_ref")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + p((String)request.getAttribute("fsipg_forma")) + "','" + p((String)request.getAttribute("fsipg_id_bancaj")) + "','" + total + "','" +
    		  p((String)request.getAttribute("fsipg_tipomov")) + "','" +
    		  p((String)request.getAttribute("fsipg_id_satbanco")) + "','" +
    		  p((String)request.getAttribute("fsipg_metpagopol")) + "','" +
    		  p((String)request.getAttribute("fsipg_bancoext")) + "','" +
    		  p((String)request.getAttribute("fsipg_cuentabanco")) + "','" +
    		  p((String)request.getAttribute("fsipg_depchq")) + "') as ( err integer, res varchar, clave integer );";
      
      //doDebugSQL(request, response, str);
      
      JRetFuncBas rfb = new JRetFuncBas();

      doCallStoredProcedure(request, response, str, rfb);

      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CXC_PAGAR", "VCXC|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "||",rfb.getRes());
      irApag("/forsetiweb/ventas/ven_cxc_dlg.jsp", request, response);
      
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String rfc, registro_tributario;
    	if(request.getParameter("pais").equals("MEX"))
    	{
    		rfc = p(JUtil.fco(JUtil.frfc(request.getParameter("rfc"))));
    		registro_tributario = rfc;
    	}
    	else
    	{
    		rfc = "XEXX010101000";
    		registro_tributario = p(request.getParameter("rfc"));
    	}
    	String str = "select * from sp_client_client_agregar('CL','" + p(request.getParameter("numero")) + "','" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "',null,'" + p(request.getParameter("nombre")) + "','0.00','" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "','" + rfc + "','" +
          p(request.getParameter("atncompras")) + "','" + p(request.getParameter("atnpagos")) + "','" + p(request.getParameter("colonia")) + "','" + p(request.getParameter("cp")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("correo")) + "','" + p(request.getParameter("fax")) + "','" + p(request.getParameter("poblacion")) +
          "','" + p(request.getParameter("tel")) + "','0.00','" + p(request.getParameter("descuento")) + "','" + p(request.getParameter("dias")) + "','" + p(request.getParameter("limite")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("obs")) + "','" + (request.getParameter("prespmostr") != null ? "1" : "0") + "','" + p(request.getParameter("idvendedor"))
          + "','" + p(request.getParameter("noext")) + "','" + p(request.getParameter("noint")) + "','" + p(request.getParameter("municipio")) + "','" + p(request.getParameter("estado")) + "','" + p(request.getParameter("pais")) + "','" + p(request.getParameter("metododepago")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("id_satbanco")) + "','" + p(request.getParameter("smtp")) + "','" + registro_tributario + "','" + p(request.getParameter("pedimento")) + "') as ( err integer, res varchar, clave integer );";
      
    	//doDebugSQL(request, response, str);
      
    	JRetFuncBas rfb = new JRetFuncBas();
		
    	doCallStoredProcedure(request, response, str, rfb);
 
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "VEN_CLIENT_AGREGAR", "VCLI|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("VEN_CLIENT").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/ventas/ven_client_dlg.jsp", request, response);
      
    }

    
}
