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
package forseti.compras;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JProveeProveeMasSetV2;
import forseti.sets.JProveeProveeSetV2;
import forseti.sets.JComprasEntidadesSetIdsV2;
import forseti.sets.JPublicCXPConeSetV2;
import forseti.sets.JPublicContCatalogSetV2;

@SuppressWarnings("serial")
public class JCompProveeDlg extends JForsetiApl
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

    	String comp_provee_dlg = "";
    	request.setAttribute("comp_provee_dlg",comp_provee_dlg);

    	String mensaje = ""; short idmensaje = -1;
    	String usuario = getSesion(request).getID_Usuario();

    	if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
    	{
    		// revisa por las entidades
    		JComprasEntidadesSetIdsV2 setids = new JComprasEntidadesSetIdsV2(request,usuario,getSesion(request).getSesion("COMP_PROVEE").getEspecial());
    		setids.Open();
    	        
    		if(setids.getNumRows() < 1)
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_PROVEE");
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_PROVEE", "CPRO" + "||||",mensaje);
    			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			return;
    		}
    	        
    		// Revisa por intento de intrusion (Salto de permiso de entidad)
    		if(!request.getParameter("proceso").equals("AGREGAR_PROVEEDOR") && request.getParameter("id") != null)
    		{
    			JProveeProveeSetV2 set = new JProveeProveeSetV2(request);
    			set.m_Where = "ID_EntidadCompra = '" + setids.getAbsRow(0).getID_Entidad() + "' and Clave = '" + p(request.getParameter("id")) + "'";
    			set.Open();
    			if(set.getNumRows() < 1)
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_PROVEE");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"AL",getSesion(request).getID_Usuario(),"COMP_PROVEE","CPRO|" + request.getParameter("id") + "|" + setids.getAbsRow(0).getID_Entidad() + "||",mensaje);
    				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    				return;
    			}
    		}
    	        
    		if(request.getParameter("proceso").equals("AGREGAR_PROVEEDOR"))
    		{
    			// Revisa si tiene permisos
    			if(!getSesion(request).getPermiso("COMP_PROVEE_AGREGAR"))
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_PROVEE_AGREGAR");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_PROVEE_AGREGAR","CPRO||||",mensaje);
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
    	            
    				irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
    				return;
    			}	
    			else
    			{
    				JProveeProveeSetV2 set = new JProveeProveeSetV2(request);
                	String sql = "select * FROM view_provee_provee_modulo where id_tipo = 'PR' and id_entidadcompra = " + setids.getAbsRow(0).getID_Entidad() + " order by numero desc limit 1";
                	set.setSQL(sql);
                	set.Open();
                	if(set.getNumRows() == 1)
                		request.setAttribute("id_numero",Integer.toString((set.getAbsRow(0).getNumero() + 1)));
                	else
                		request.setAttribute("id_numero","");

    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
    				return;
    			}
    		}
    		else if(request.getParameter("proceso").equals("CONSULTAR_PROVEEDOR"))
    		{
    			// Revisa si tiene permisos
    			if(!getSesion(request).getPermiso("COMP_PROVEE"))
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_PROVEE");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"COMP_PROVEE","CPRO||||",mensaje);
    				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    				return;
    			}
    	 
    	            // Solicitud de envio a procesar
    			if(request.getParameter("id") != null)
    			{
    				String[] valoresParam = request.getParameterValues("id");
    				if(valoresParam.length == 1)
    				{
    					RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"COMP_PROVEE","CPRO|" + request.getParameter("id") + "|" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "||","");
    	                getSesion(request).setID_Mensaje(idmensaje, mensaje);
    					irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
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
    		else if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR"))
    		{
    			// Revisa si tiene permisos
    			if(!getSesion(request).getPermiso("COMP_PROVEE_CAMBIAR"))
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_PROVEE_CAMBIAR");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_PROVEE_CAMBIAR","CPRO||||",mensaje);
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
    	            		
    						irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
    						return;
    					}
    					else
    					{
    						getSesion(request).setID_Mensaje(idmensaje, mensaje);
    						irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
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
    			if(!getSesion(request).getPermiso("COMP_CXP_PAGAR")) 
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_CXP_PAGAR");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_CXP_PAGAR","CCXP||||",mensaje);
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
    						request.setAttribute("fsipg_tipo","compras");
    						request.setAttribute("fsipg_proc","retiro");
    						request.setAttribute("fsipg_ident",getSesion(request).getSesion("COMP_PROVEE").getEspecial());
    						if(VerificarParametrosAlta(request, response) && VerificarPago(request, response))
    						{
    							AltaAnticipo(request, response);
    							return;
    						}
    						irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);
    						return;
    					}
    					else
    					{
    						getSesion(request).setID_Mensaje(idmensaje, mensaje);
    						irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);
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
                if(!getSesion(request).getPermiso("COMP_CXP_AGREGAR")) 
                {
              	  	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_CXP_AGREGAR");
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_CXP_AGREGAR","CCXP||||",mensaje);
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
                        AltaCXP(request, response);
                        return;
                    }
                    irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);
                    return;
                  }
                  else
                  {
                    getSesion(request).setID_Mensaje(idmensaje, mensaje);
                    irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);
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
    		// Verifica que el proveedor de la cuenta realmente pertenece a esta entidad
    		JProveeProveeMasSetV2 pr = new JProveeProveeMasSetV2(request);
    		pr.m_Where = "ID_Clave = '" + p(request.getParameter("id")) + "'";
    		pr.Open();
    		if(pr.getAbsRow(0).getID_EntidadCompra() != Integer.parseInt(getSesion(request).getSesion("COMP_PROVEE").getEspecial()) )
    		{
    			idmensaje = 3;
    			mensaje = JUtil.Msj("CEF", "COMP_PROVEE","DLG","MSJ-PROCERR",1); //ERROR: El proveedor no pertenece a la entidad mandada. <br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}

    		// Por ultimo verifica la cantidad el tc y el id concepto
    		float cantidad = Float.parseFloat(request.getParameter("cantidad"));
    		float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    		if (cantidad < 0.00 || tc < 0.0000)
    		{
    			idmensaje = 3;
    			mensaje = JUtil.Msj("GLB","CXCP","DLG","MSJ-PROCERR",1); //"ERROR: La cantidad o el tipo de cambio est�n mal <br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}

    		// solo verifica el id concepto para cuenta y no para anticipo
    		if(!request.getParameter("proceso").equals("AGREGAR_ANTICIPO"))
    		{
    			JPublicCXPConeSetV2 con = new JPublicCXPConeSetV2(request);
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
    			request.getParameter("fecha") != null && request.getParameter("obs") != null && 
    			request.getParameter("noext") != null && request.getParameter("noint") != null && request.getParameter("municipio") != null &&
    			request.getParameter("estado") != null && request.getParameter("pais") != null && request.getParameter("metododepago") != null &&
    			request.getParameter("status") != null && request.getParameter("id_satbanco") != null &&
    			!request.getParameter("numero").equals("") &&  
    			!request.getParameter("dias").equals("") && !request.getParameter("limite").equals("") && !request.getParameter("descuento").equals("") &&
    			!request.getParameter("cuenta").equals("") && !request.getParameter("nombre").equals("") && !request.getParameter("fecha").equals("") )

    	{
    		if(!request.getParameter("rfc").equals(""))
        	{
        		String rfcfmt = JUtil.fco(JUtil.frfc(request.getParameter("rfc")));
        		if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
        		{
        			idmensaje = 1; mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
        			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        			return false;
        		}
        	}
    		
    		if(request.getParameter("proceso").equals("CAMBIAR_PROVEEDOR"))
    		{
    			// Verifica que el proveedor de la cuenta realmente pertenece a esta entidad
    			JProveeProveeMasSetV2 pr = new JProveeProveeMasSetV2(request);
    			pr.m_Where = "ID_Clave = '" + p(request.getParameter("id")) + "'";
    			pr.Open();
    			if(pr.getAbsRow(0).getID_EntidadCompra() != Integer.parseInt(getSesion(request).getSesion("COMP_PROVEE").getEspecial()) )
    			{
    				idmensaje = 3;
    				mensaje = JUtil.Msj("CEF", "COMP_PROVEE","DLG","MSJ-PROCERR",1); //"ERROR: El proveedor no pertenece a la entidad mandada. <br>";
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
    	            mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES","DLG","MSJ-PROCERR2",5);//"PRECAUCION: La cuenta contable para este proveedor existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
    	            getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	            return false;
    			}
    		}
    		else
    		{
    			idmensaje = 3;
    			mensaje = JUtil.Msj("CEF", "ADM_ENTIDADES","DLG","MSJ-PROCERR3",1);//"ERROR: La cuenta contable para este proveedor no existe <br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}

    		// Por ultimo verifica el descuento
    		float descuento = Float.parseFloat(request.getParameter("descuento"));
    		if (descuento < 0.00 || descuento > 100.00)
    		{
    			idmensaje = 3;
    			mensaje = JUtil.Msj("CEF", "COMP_PROVEE","DLG","MSJ-PROCERR",2); //"ERROR: El descuento no puede ser menor que 0.00 <br>";
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			return false;
    		}

    		return true;
    	}
    	else
    	{
    		idmensaje = 1; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		return false;
    	}
    }

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String str = "select * from sp_provee_provee_cambiar('PR','" + p(request.getParameter("id")) + "','" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "',null,'" + p(request.getParameter("nombre")) + "','0.00','" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" +
    		p(request.getParameter("atncompras")) + "','" + p(request.getParameter("atnpagos")) + "','" + p(request.getParameter("colonia")) + "','" + p(request.getParameter("cp")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("correo")) + "','" + p(request.getParameter("fax")) + "','" + p(request.getParameter("poblacion")) +
    		"','" + p(request.getParameter("tel")) + "','0.00','" + p(request.getParameter("descuento")) + "','" + p(request.getParameter("dias")) + "','" + p(request.getParameter("limite")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("obs")) + "','0','0"
    		+ "','" + p(request.getParameter("noext")) + "','" + p(request.getParameter("noint")) + "','" + p(request.getParameter("municipio")) + "','" + p(request.getParameter("estado")) + "','" + p(request.getParameter("pais")) + "','" + p(request.getParameter("metododepago")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("id_satbanco")) + "','0') as ( err integer, res varchar, clave integer );";
    	     
    	JRetFuncBas rfb = new JRetFuncBas();
    			
    	doCallStoredProcedure(request, response, str, rfb);
    	 
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "COMP_PROVEE_CAMBIAR", "CPRO|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
    	         
    }

    public void AltaCXP(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
    	float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

    	String str = "select * from sp_provee_cxp_alta('" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','PR','" + p(request.getParameter("id")) + "','" + p(request.getParameter("concepto")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + total + "','" + p(request.getParameter("clave")) + "',null,null,null) as ( err integer, res varchar, clave integer );";
    													//_ID_Entidad smallint,                                                   _Fecha timestamp,                      _ID_TipoClient char(2), _ID_ClaveClient int,                  _Concepto varchar(80),                              _Moneda smallint,                    _TC numeric,             _Total numeric,                  _Cantidad numeric,            _ID_Concepto smallint)

    	JRetFuncBas rfb = new JRetFuncBas();
		
        doCallStoredProcedure(request, response, str, rfb);
   
        RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "COMP_CXP_AGREGAR", "CCXP|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "||",rfb.getRes());
        irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);
   
    }

    public void AltaAnticipo(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      float tc = ( Byte.parseByte(request.getParameter("idmoneda")) == 1 ) ? 1.0F : Float.parseFloat(request.getParameter("tc"));
      float total = JUtil.redondear( (Float.parseFloat(request.getParameter("cantidad")) * tc),2);

      String str = "select * from sp_provee_cxp_prestamo('" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','PR','" + p(request.getParameter("id")) + "','" + p(request.getParameter("concepto")) + "','" + p((String)request.getAttribute("fsipg_ref")) + "','" + p(request.getParameter("idmoneda")) + "','" + tc + "','" + p(request.getParameter("cantidad")) + "','" + p((String)request.getAttribute("fsipg_forma")) + "','" + p((String)request.getAttribute("fsipg_id_bancaj")) + "','" + total + "','" +
    		  p((String)request.getAttribute("fsipg_tipomov")) + "','" +
    		  p((String)request.getAttribute("fsipg_id_satbanco")) + "','" +
    		  p((String)request.getAttribute("fsipg_metpagopol")) + "','" +
    		  p((String)request.getAttribute("fsipg_bancoext")) + "','" +
    		  p((String)request.getAttribute("fsipg_cuentabanco")) + "','" +
    		  p((String)request.getAttribute("fsipg_depchq")) + "') as ( err integer, res varchar, clave integer );";
      
      JRetFuncBas rfb = new JRetFuncBas();

      doCallStoredProcedure(request, response, str, rfb);

      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "COMP_CXP_PAGAR", "CCXP|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "||",rfb.getRes());
      irApag("/forsetiweb/compras/comp_cxp_dlg.jsp", request, response);

    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String str = "select * from sp_provee_provee_agregar('PR','" + p(request.getParameter("numero")) + "','" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "',null,'" + p(request.getParameter("nombre")) + "','0.00','" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "','" + p(JUtil.fco(JUtil.frfc(request.getParameter("rfc")))) + "','" +
    		p(request.getParameter("atncompras")) + "','" + p(request.getParameter("atnpagos")) + "','" + p(request.getParameter("colonia")) + "','" + p(request.getParameter("cp")) + "','" + p(request.getParameter("direccion")) + "','" + p(request.getParameter("correo")) + "','" + p(request.getParameter("fax")) + "','" + p(request.getParameter("poblacion")) +
    		"','" + p(request.getParameter("tel")) + "','0.00','" + p(request.getParameter("descuento")) + "','" + p(request.getParameter("dias")) + "','" + p(request.getParameter("limite")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(request.getParameter("obs")) + "','0','0"
    		+ "','" + p(request.getParameter("noext")) + "','" + p(request.getParameter("noint")) + "','" + p(request.getParameter("municipio")) + "','" + p(request.getParameter("estado")) + "','" + p(request.getParameter("pais")) + "','" + p(request.getParameter("metododepago")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("id_satbanco")) + "','0') as ( err integer, res varchar, clave integer );";
    	      
    	JRetFuncBas rfb = new JRetFuncBas();
    			
    	doCallStoredProcedure(request, response, str, rfb);
    	 
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "COMP_PROVEE_AGREGAR", "CPRO|" + rfb.getClaveret() + "|" + getSesion(request).getSesion("COMP_PROVEE").getEspecial() + "||",rfb.getRes());
    	irApag("/forsetiweb/compras/comp_provee_dlg.jsp", request, response);
    	      
    }

}
