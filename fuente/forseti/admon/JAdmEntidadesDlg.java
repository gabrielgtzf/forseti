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
package forseti.admon;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
//import forseti.JForsetiCFD;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JPublicBodegasCatSetV2;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JVendedoresSet;

@SuppressWarnings("serial")
public class JAdmEntidadesDlg extends JForsetiApl
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

      String adm_entidades_dlg = "";
      request.setAttribute("adm_entidades_dlg",adm_entidades_dlg);

      String mensaje = ""; short idmensaje = -1;
      /*
      if (request.getContentType() != null && 
    		    request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) 
      {
    	  if(!getSesion(request).getRegistrado()) 
    	  { 
    		 irApag("/forsetiweb/errorAtributos.jsp",request,response);
    		 return;
    	  }
    	  else
    	  {
              if(!getSesion(request).getPermiso("ADM_ENTIDADES_AGREGAR") || !getSesion(request).getPermiso("ADMON_ENTIDADES_CAMBIAR"))
              {
            	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES_AGREGAR");
                  getSesion(request).setID_Mensaje(idmensaje, mensaje);
                  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES_AGREGAR","AENT||||",mensaje);
                  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                  return;
              }

    		  SubirArchivosCFD(request, response);
    		  return;
    	  }
    	  
      }
      */
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	/*if(request.getParameter("proceso").equals("VERIFICAR_CFD"))
        {
    		// Verificacion
        	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
        	
        	if(ent.equals("VENTAS"))
        	{
                if(!getSesion(request).getPermiso("ADM_ENTIDADES_AGREGAR"))
                {
                	idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES_AGREGAR");
                	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                	RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES_AGREGAR","AENT||||",mensaje);
                	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                	return;
                }

        		VerificarCertificados(request, response);
        		return;
            }
        }
    	else*/ if(request.getParameter("proceso").equals("AGREGAR_ENTIDAD"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_ENTIDADES_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES_AGREGAR","AENT||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
            // Verificacion
        	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
        	
        	if(ent.equals("BANCOS") || ent.equals("CAJAS"))
        	{
        		if(VerificarParametrosBancos(request, response))
        		{
                    Agregar(request, response);
                    return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp", request, response);
                return;
        	}
        	else if(ent.equals("BODEGAS"))
        	{
        		if(VerificarParametrosBodegas(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp", request, response);
                return;
        	}
        	else if(ent.equals("COMPRAS"))
        	{
        		if(VerificarParametrosCompras(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_compras.jsp", request, response);
                return;
        	}
           	else if(ent.equals("VENTAS"))
        	{
        		if(VerificarParametrosVentas(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp", request, response);
                return;
        	}
           	else if(ent.equals("PRODUCCION"))
        	{
        		if(VerificarParametrosProduccion(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp", request, response);
                return;
        	}
           	else if(ent.equals("NOMINA"))
        	{
        		if(VerificarParametrosNomina(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp", request, response);
                return;
        	}
           	else if(ent.equals("MENSAJES"))
        	{
        		if(VerificarParametrosMensajes(request, response))
        		{
        			Agregar(request, response);
        			return;
        		}
        		irApag("/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp", request, response);
                return;
        	}
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
             	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
            	String str = "";
            	
            	if(ent.equals("BANCOS") || ent.equals("CAJAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
            	else if(ent.equals("BODEGAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp";
            	else if(ent.equals("COMPRAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_compras.jsp";
            	else if(ent.equals("VENTAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
            	/*{
            		str = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
            		HttpSession ses = request.getSession(true);
                    JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
                    if(rec == null)
                    {
        	           	rec = new JForsetiCFD();
        	            ses.setAttribute("ven_cfd", rec);
                    }
                    else
                        rec.resetearCertificado();
                    
            	}*/
            	else if(ent.equals("PRODUCCION"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp";
            	else if(ent.equals("NOMINA"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp";
            	else if(ent.equals("MENSAJES"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp";
            	            		
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag(str, request, response);
                return;
          }
        }
        else if(request.getParameter("proceso").equals("CAMBIAR_ENTIDAD"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_ENTIDADES_AGREGAR"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES_AGREGAR","AENT||||",mensaje);
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
              	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
              	if(ent.equals("BANCOS") || ent.equals("CAJAS"))
            	{
            		if(VerificarParametrosBancos(request, response))
            		{
                        Cambiar(request, response);
                        return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp", request, response);
                    return;
            	}
            	else if(ent.equals("BODEGAS"))
            	{
            		if(VerificarParametrosBodegas(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp", request, response);
                    return;
            	}
            	else if(ent.equals("COMPRAS"))
            	{
            		if(VerificarParametrosCompras(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_compras.jsp", request, response);
                    return;
            	}
               	else if(ent.equals("VENTAS"))
            	{
            		if(VerificarParametrosVentas(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp", request, response);
                    return;
            	}
               	else if(ent.equals("PRODUCCION"))
            	{
            		if(VerificarParametrosProduccion(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp", request, response);
                    return;
            	}
               	else if(ent.equals("NOMINA"))
            	{
            		if(VerificarParametrosNomina(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp", request, response);
                    return;
            	}
               	else if(ent.equals("MENSAJES"))
            	{
            		if(VerificarParametrosMensajes(request, response))
            		{
            			Cambiar(request, response);
            			return;
            		}
            		irApag("/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp", request, response);
                    return;
            	}
              }
              else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
              {
            	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
            	String str = "";
            	
            	if(ent.equals("BANCOS") || ent.equals("CAJAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
            	else if(ent.equals("BODEGAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp";
            	else if(ent.equals("COMPRAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_compras.jsp";
            	else if(ent.equals("VENTAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
            	else if(ent.equals("PRODUCCION"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp";
            	else if(ent.equals("NOMINA"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp";
            	else if(ent.equals("MENSAJES"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp";
            	
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag(str, request, response);
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
        else if(request.getParameter("proceso").equals("CONSULTAR_ENTIDAD"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADM_ENTIDADES"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_ENTIDADES");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_ENTIDADES","AENT||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
               	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
            	String str = "";
            	
            	if(ent.equals("BANCOS") || ent.equals("CAJAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
            	else if(ent.equals("BODEGAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp";
            	else if(ent.equals("COMPRAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_compras.jsp";
            	else if(ent.equals("VENTAS"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
            	else if(ent.equals("PRODUCCION"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp";
            	else if(ent.equals("NOMINA"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp";
            	else if(ent.equals("MENSAJES"))
            		str = "/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp";

            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                irApag(str, request, response);
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
    
/*
     public void SubirArchivosCFD(HttpServletRequest request, HttpServletResponse response)
 		throws ServletException, IOException
	{
        short idmensaje = -1; StringBuffer mensaje = new StringBuffer();

        HttpSession ses = request.getSession(true);
        JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
       
        idmensaje = rec.SubirArchivosCert(request, mensaje);

        getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        if(idmensaje != 0)
        	irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        else
        	irApag("/forsetiweb/administracion/adm_entidades_dlg_ventas_vercfd.jsp", request, response);
	}

   public void VerificarCertificados(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
    	
    	short idmensaje = -1; StringBuffer mensaje = new StringBuffer();
    	if(request.getParameter("password") == null || request.getParameter("password").equals(""))
    	{
			idmensaje = 3; mensaje.append("ERROR: No se recibi&oacute; la clave de la llave <br>");
		    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
		    irApag("/forsetiweb/administracion/adm_entidades_dlg_ventas_vercfd.jsp", request, response);
		    return;
    	}
    	else
    	{
    		HttpSession ses = request.getSession(true);
    		JForsetiCFD rec = (JForsetiCFD)ses.getAttribute("ven_cfd");
       
    		idmensaje = rec.VerificarCertificadosSubidos(request, request.getParameter("password"), mensaje);

    		getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
    		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    		return;
    	}
	}
*/    
    public boolean VerificarParametrosVentas(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("identidad") != null && request.getParameter("ficha") != null 
		   	&& request.getParameter("serie") != null  && request.getParameter("numero") != null 
		   	&& request.getParameter("ivaporcentual") != null && request.getParameter("idbodega") != null
		    && request.getParameter("pedido") != null  && request.getParameter("devolucion") != null && request.getParameter("remision") != null  && request.getParameter("cotizacion") != null 
		    && request.getParameter("tipopago") != null  && request.getParameter("idvendedor") != null 
		    && request.getParameter("ajustedeprecio") != null && request.getParameter("factordeajuste") != null && 
		    request.getParameter("cfd") != null && request.getParameter("cfd_id_expedicion") != null && request.getParameter("cfd_nocertificado") != null && 
		    !request.getParameter("identidad").equals("") && !request.getParameter("ficha").equals("")
		  	&& !request.getParameter("serie").equals("")  && !request.getParameter("numero").equals("") 
		  	&& !request.getParameter("ivaporcentual").equals("") && !request.getParameter("idbodega").equals("")
		    && !request.getParameter("pedido").equals("") && !request.getParameter("devolucion").equals("") 
	    	&& !request.getParameter("tipopago").equals("")  && !request.getParameter("idvendedor").equals("") 
		    && !request.getParameter("ajustedeprecio").equals("") && !request.getParameter("factordeajuste").equals("") 
		    && !request.getParameter("cfd").equals(""))
	
	    {
		    JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
		    set.m_Where = "ID_Bodega = '" + p(request.getParameter("idbodega")) + "'";
			set.Open();
			
			if(set.getNumRows() < 1 )
		    {
				idmensaje = 1; mensaje = JUtil.Msj("CEF","ALM_TRASPASOS","SES","MSJ-PROCERR"); //"PRECAUCION: No existe la bodega especificada <br>";
			    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    return false;
		    }
			
			// Verifica el vendedor
		    JVendedoresSet setven  = new JVendedoresSet(request);
		    setven.m_Where = "ID_Vendedor = '" + p(request.getParameter("idvendedor")) + "'";
		    setven.Open();
		    if(setven.getNumRows() == 0)
		    {
				idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR"); //"PRECAUCION: No existe el vendedor especificado <br>";
			    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    return false;
		    }
		    // Verifica que si la entidad es fija ( NO CONTABLE ) el iva sea 0 y no emita CFD
		    //float iva = Float.parseFloat(request.getParameter("ivaporcentual"));
		    
		    if(request.getParameter("fija") != null) 
		    {	
		    	/*if(iva != 0.0)
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",2); //"PRECAUCION: Como esta entidad se establece como fija, el IVA debe ser 0 <br>";
		    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		    		return false;
		    	}*/
		    	if(!request.getParameter("cfd").equals("00"))
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",3); //"PRECAUCION: Como esta entidad se establece como fija, no permite Comprobantes Fiscales Digitales <br>";
		    		getSesion(request).setID_Mensaje(idmensaje, mensaje);
		    		return false;
		    	}
		    }
		    
		    // permite comprobantes fiscales digitales
		    if(!request.getParameter("cfd").equals("00"))
		    {
		    	/*
		    	if(request.getParameter("cfd_noaprobacion").equals("0") || 
		    	   			request.getParameter("cfd_noaprobaciondev").equals("0") || request.getParameter("cfd_noaprobacionrem").equals("0")) 
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",4); //"PRECAUCION: Es necesaria toda la informaci&oacute;n de folios de facturas, remisiones, y devoluciones para Comprobantes Fiscales Digitales <br>";
			    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    	return false;
		    	}
		    	*/
		    	if(request.getParameter("cfd_nocertificado").equals("")) 
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",5); //"PRECAUCION: Es necesaria toda la informaci&oacute;n de certificados de facturas, remisiones, y devoluciones para Comprobantes Fiscales Digitales <br>";
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
    
    
 
    public boolean VerificarParametrosCompras(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("identidad") != null && request.getParameter("ficha") != null && request.getParameter("tipo") != null
		   	&& request.getParameter("serie") != null  && request.getParameter("numero") != null && request.getParameter("idbodega") != null
		    && request.getParameter("orden") != null && request.getParameter("recepcion") != null && request.getParameter("devolucion") != null && request.getParameter("ivaporcentual") != null
		    /*&& request.getParameter("infoplantoc") != null && request.getParameter("infogasrec") != null*/   
		    && !request.getParameter("identidad").equals("") && !request.getParameter("ficha").equals("") && !request.getParameter("tipo").equals("")
		  	&& !request.getParameter("serie").equals("")  && !request.getParameter("numero").equals("") && !request.getParameter("idbodega").equals("")
		    && !request.getParameter("orden").equals("") && !request.getParameter("recepcion").equals("") && !request.getParameter("devolucion").equals("") && !request.getParameter("ivaporcentual").equals("")
		    /*&& !request.getParameter("infoplantoc").equals("") && !request.getParameter("infogasrec").equals("")*/)
		{
	    	
		    JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
		    set.m_Where = "ID_Bodega = '" + p(request.getParameter("idbodega")) + "' and ID_InvServ = '" + 
		    		(request.getParameter("tipo").equals("0") ? "P" : "G") + "'";
			set.Open();
			
			if(set.getNumRows() < 1 )
		    {
				idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR3",4); //ERROR: No existe la bodega especificada o el tipo de bodega no corresponde al tipo de entidad. Ejemplo: Compras / Almacén de utensilios o Gastos / Almacén de productos
			    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    return false;
		    }
		    // Verifica que si la entidad es fija ( NO CONTABLE ) el iva sea 0
		    Float iva = Float.parseFloat(request.getParameter("ivaporcentual"));
		    if(request.getParameter("fija") != null && iva != 0.0)
		    {
				idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",2); //"PRECAUCION: Como esta entidad se establece como fija, el IVA debe ser 0 <br>";
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

    public boolean VerificarParametrosNomina(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("identidad") != null && request.getParameter("ficha") != null  
	    	&& request.getParameter("descripcion") != null && request.getParameter("tipo") != null   && request.getParameter("periodo") != null &&
	    	 request.getParameter("numero") != null &&  request.getParameter("fecha") != null  && request.getParameter("contbancaj") != null && request.getParameter("fijabancaj") != null && 
	    			    request.getParameter("cfd") != null && request.getParameter("cfd_id_expedicion") != null && request.getParameter("cfd_nocertificado") != null &&
	    	!request.getParameter("identidad").equals("") && !request.getParameter("ficha").equals("")
	    	&& !request.getParameter("descripcion").equals("") && !request.getParameter("tipo").equals("") && !request.getParameter("periodo").equals("")
	    	&& !request.getParameter("numero").equals("") && !request.getParameter("fecha").equals("") && !request.getParameter("contbancaj").equals("") && !request.getParameter("fijabancaj").equals("")
	    	 && !request.getParameter("cfd").equals(""))
	    {
	    	// permite comprobantes fiscales digitales
		    if(!request.getParameter("cfd").equals("00"))
		    {
		    	if(request.getParameter("cfd_nocertificado").equals("")) 
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",5); //"PRECAUCION: Es necesaria toda la informaci&oacute;n de certificados de facturas, remisiones, y devoluciones para Comprobantes Fiscales Digitales <br>";
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
    
    public boolean VerificarParametrosMensajes(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("idblock") != null && request.getParameter("ficha") != null  
	    	&& request.getParameter("descripcion") != null &&
	    	!request.getParameter("idblock").equals("") && !request.getParameter("ficha").equals("")
	    	&& !request.getParameter("descripcion").equals("") )
	    {
	  	    return true;
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	
	}    
    
    public boolean VerificarParametrosProduccion(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("identidad") != null && request.getParameter("ficha") != null  && request.getParameter("descripcion") != null  
	    	&& request.getParameter("serie") != null && request.getParameter("numero") != null  &&
	    	!request.getParameter("identidad").equals("") && !request.getParameter("ficha").equals("") && !request.getParameter("descripcion").equals("")
	    	&& !request.getParameter("serie").equals("")  && !request.getParameter("numero").equals(""))
	    {
	    	JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
		    set.m_Where = "ID_Bodega = '" + p(request.getParameter("idbodegamp")) + "'";
			set.Open();
			
			if(set.getNumRows() < 1 )
		    {
				idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR2",1); //"PRECAUCION: No existe la bodega especificada de materia prima <br>";
			    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    return false;
		    }
			/*
			JPublicBodegasCatSetV2 set2 = new JPublicBodegasCatSetV2(request);
			set2.m_Where = "ID_Bodega = '" + p(request.getParameter("idbodegapt")) + "'";
			set2.Open();
			
			if(set2.getNumRows() < 1 )
		    {
				idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR2",2);//"PRECAUCION: No existe la bodega especificada de producto terminado <br>";
			    getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    return false;
		    }*/
			
	  	    return true;
	    }
	    else
	    {
	    	idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }

	}    
    
    public boolean VerificarParametrosBodegas(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("idbodega") != null && request.getParameter("ficha") != null
	    	&& request.getParameter("descripcion") != null  && request.getParameter("numero") != null
	        && request.getParameter("salida") != null  && request.getParameter("requerimiento") != null && request.getParameter("plantilla") != null  && request.getParameter("numchfis") != null && 
	        request.getParameter("cfd") != null && request.getParameter("cfd_id_expedicion") != null && request.getParameter("cfd_nocertificado") != null && 
		    !request.getParameter("idbodega").equals("") && !request.getParameter("ficha").equals("")
	    	&& !request.getParameter("descripcion").equals("")  && !request.getParameter("numero").equals("")
	        && !request.getParameter("salida").equals("") && !request.getParameter("requerimiento").equals("") && !request.getParameter("plantilla").equals("") 
	        && !request.getParameter("cfd").equals(""))
	    {
	    	// permite comprobantes fiscales digitales
		    if(!request.getParameter("cfd").equals("00"))
		    {
		    	if(request.getParameter("cfd_nocertificado").equals("")) 
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR",5);//"PRECAUCION: Es necesaria la informaci&oacute;n del certificado de traslado (Carta Porte) para Comprobantes Fiscales Digitales <br>";
			    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
			    	return false;
		    	}
		    	
		    	if(request.getParameter("cfd_id_expedicion").equals("0")) 
		    	{
		    		idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR2",3);//"PRECAUCION: Es necesaria toda informaci&oacute;n de expedici&oacute;n y receptor de traslado (Carta Porte) para Comprobantes Fiscales Digitales <br>";
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
    
    public boolean VerificarParametrosBancos(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("clave") != null && request.getParameter("ficha") != null &&
          request.getParameter("descripcion") != null  && request.getParameter("id_satbanco") != null  && request.getParameter("bancoext") != null &&
          request.getParameter("cuenta") != null  && request.getParameter("status") != null && 
          !request.getParameter("clave").equals("") && !request.getParameter("ficha").equals("")
          && !request.getParameter("descripcion").equals("") )
      {
    	  if(!request.getParameter("id_satbanco").matches("\\d{3}"))
		  {
			  idmensaje = 3; mensaje = "ERROR: La clave del banco para el SAT debe constar de tres digitos exactamente <br>";
			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  return false;
		  }
    	  
    	  if(request.getParameter("ENTIDAD").equals("BANCOS"))
    	  {
    		  if(request.getParameter("sigcheque") != null && request.getParameter("idmoneda") != null
    				  && !request.getParameter("sigcheque").equals("") && !request.getParameter("idmoneda").equals(""))
    		  {
    			// Ok Existe el cheque
    		  }
    		  else
    		  {
    			  idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR2",4); //"PRECAUCION: Falta especificar el cheque o la moneda de la cuenta. Si esta cuenta representa una tarjeta, inversión o cualquier otro tipo de cuenta, puedes establecer el cheque como 1 y generar retiros por transferencia exclisivamente.<br>";
	              getSesion(request).setID_Mensaje(idmensaje, mensaje);
	              return false; 
    		  }
    		  if((request.getParameter("id_satbanco").equals("000") && request.getParameter("bancoext").equals(""))
    			|| (!request.getParameter("id_satbanco").equals("000") && !request.getParameter("bancoext").equals("")))
    		  {
    			  idmensaje = 1; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR3",5); //"PRECAUCION: No se especificó un banco nacional del catálogo ni se ingresó un banco extranjero justo debajo de este. Este aviso también puede estar mostrándose porque ambos están definidos.
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
    				  idmensaje = 1;
    				  mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR2",5); //"PRECAUCION: La cuenta contable para esta entidad existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
    				  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				  return false;
    			  }
    		  }
    		  else
    		  {
    			  idmensaje = 3;
    			  mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR3",1); //"ERROR: La cuenta contable para esta entidad no existe <br>";
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  return false;
    		  }
    	  }
    	  else
    	  {
			  idmensaje = 1;
			  mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR3",2); //"PRECAUCION: Se debe enviar la cuenta contable de esta entidad. Si estás estableciendo esta entidad como Fija, la cuenta contable no se utilizará, pero de todas formas se necesita definir <br>";
			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
			  return false;
    	  }
    	  
    	  if(request.getParameter("ENTIDAD").equals("CAJAS"))
    	  {
	          if(request.getParameter("bancaj") == null || request.getParameter("bancaj").equals("")
	                  || request.getParameter("bancaj").equals("BANCOS") || request.getParameter("bancaj").equals("CAJAS"))
	          {
	        	  idmensaje = 3; mensaje = JUtil.Msj("CEF","ADM_ENTIDADES","DLG","MSJ-PROCERR3",3);//"ERROR: Debe especificarse el banco o la caja de destino para traspasos en arqueo Z<br>";
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

    public void Cambiar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
        String str = "select * from ";
        String irA = "";
        String tbl = "";
        String drop = "";
     
        if(ent.equals("BANCOS"))
        {
        	str += "sp_bancos_cuentas_cambiar( '0','" + p(request.getParameter("clave")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("sigcheque")) + "'," + 
        	(request.getParameter("fmt_dep").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_dep")) + "'") + "," + (request.getParameter("fmt_ret").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_ret")) + "'") + ",'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "','" + p(request.getParameter("status")) +
        	"','" + (request.getParameter("fija") == null ? "0" : "1") + "','0','" + p(request.getParameter("clave")) + "','0" +
        	"','0','0.00','" + p(request.getParameter("idmoneda")) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("id_satbanco")) + "','" + p(request.getParameter("bancoext")) + "')";
         	irA = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
        }
        else if(ent.equals("CAJAS"))
        {
        	String clave;
        	String tipo;
      	  	if(request.getParameter("bancaj").equals("NINGUNA"))
      	  	{
      	  		tipo = "1";
      	  		clave = request.getParameter("clave");
      	  	}
      	  	else if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1) // es hacia banco
      	  	{
      	  		tipo = "0";
      	  		clave = request.getParameter("bancaj").substring(8);
      	  	}
      	  	else
      	  	{
      	  		tipo = "1";
      	  		clave = request.getParameter("bancaj").substring(8);
      	  	}
 
      	  	str += "sp_bancos_cuentas_cambiar( '1','" + p(request.getParameter("clave")) + "','" + p(request.getParameter("ficha")) + "','0'," + 
      	  	(request.getParameter("fmt_dep").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_dep")) + "'") + "," + (request.getParameter("fmt_ret").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_ret")) + "'") + ",'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "','" + p(request.getParameter("status")) +
      	  	"','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + p(tipo) + "','" + p(clave) + "','0" +
      	  	"','" + p(request.getParameter("ultimonumtrasp")) + "','" + p(request.getParameter("fondotrasp")) + "','1','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("descripcion")) + "','000','')";
      	  	irA = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
      }
      else if(ent.equals("BODEGAS"))
      {
	      	str += "sp_invserv_bodegas_cambiar('" + p(request.getParameter("idbodega")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("numero")) +
	  		"','" + p(request.getParameter("salida")) + "','" + p(request.getParameter("requerimiento")) + "','" + p(request.getParameter("plantilla")) + "','" 
	  		+ (request.getParameter("fmt_movimientos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_movimientos"))) + "','" + (request.getParameter("fmt_traspasos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_traspasos"))) + "','" 
	  		+ (request.getParameter("fmt_requerimientos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_requerimientos"))) + "','" + (request.getParameter("fmt_plantillas").equals("NINGUNO") ? "" : p(request.getParameter("fmt_plantillas"))) + "','" + 
	  		// certificados fiscales digitales
	        q(request.getParameter("cfd")) + "','0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "','0'," + 
	        "'" + p(request.getParameter("idinvserv")) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("numchfis")) + "','" + p(request.getParameter("status")) + "','" + (request.getParameter("fmt_chfis").equals("NINGUNO") ? "" : p(request.getParameter("fmt_chfis"))) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "')";
	  	   	irA = "/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp";
      }
      else if(ent.equals("COMPRAS"))
      {
    	  drop += "DROP TABLE _TMP_BANCOS_VS_COMPRAS\n";
    	  tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_VS_COMPRAS (\n";
    	  tbl += "	Tipo smallint NOT NULL ,\n";
    	  tbl += "	Clave smallint NOT NULL\n";
    	  tbl += ");\n";
      	
          @SuppressWarnings("rawtypes")
          Enumeration nombresParam = request.getParameterNames();
          while(nombresParam.hasMoreElements())
          {
              String nombreParam = (String)nombresParam.nextElement();
              if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
              	continue;
              
              String claveParam = nombreParam.substring(0,8);
              String valorParam = nombreParam.substring(8);
              if(claveParam.equals("PER_BAN_"))
              {
              	tbl += "insert into _TMP_BANCOS_VS_COMPRAS\n";
              	tbl += "values('0','" + p(valorParam) + "');\n";
              }
              else if(claveParam.equals("PER_CAJ_"))
              {
              	tbl += "insert into _TMP_BANCOS_VS_COMPRAS\n";
              	tbl += "values('1','" + p(valorParam) + "');\n";
              }
          }
          
          str += " sp_compras_entidades_cambiar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("tipo")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + p(request.getParameter("ficha")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + 
          p(request.getParameter("idbodega")) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + (request.getParameter("fijacost") == null ? "0" : "1") + "','" + p(request.getParameter("devolucion")) + "','" + p(request.getParameter("orden")) + "','" + (request.getParameter("fmt_devolucion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_devolucion"))) + "','" + (request.getParameter("fmt_orden").equals("NINGUNO") ? "" : p(request.getParameter("fmt_orden"))) + "','" + request.getParameter("ivaporcentual") +
          "','" + /*p(request.getParameter("infoplantoc"))*/"-1" + "','" + /*p(request.getParameter("infogasrec"))*/"-1" + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("recepcion")) + "','" + (request.getParameter("fmt_recepcion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_recepcion"))) + "') ";
      	
          irA = "/forsetiweb/administracion/adm_entidades_dlg_compras.jsp";
      }
      else if(ent.equals("VENTAS"))
      {
    	  drop += "DROP TABLE _TMP_BANCOS_VS_VENTAS\n";
          tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_VS_VENTAS (\n";
          tbl += "	Tipo smallint NOT NULL ,\n";
          tbl += "	Clave smallint NOT NULL\n";
          tbl += ");\n";
      	
          @SuppressWarnings("rawtypes")
          Enumeration nombresParam = request.getParameterNames();
          while(nombresParam.hasMoreElements())
          {
              String nombreParam = (String)nombresParam.nextElement();
              if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
              	continue;
              
              String claveParam = nombreParam.substring(0,8);
              String valorParam = nombreParam.substring(8);
              if(claveParam.equals("PER_BAN_"))
              {
              	tbl += "insert into _TMP_BANCOS_VS_VENTAS\n";
              	tbl += "values('0','" + p(valorParam) + "');\n";
              }
              else if(claveParam.equals("PER_CAJ_"))
              {
              	tbl += "insert into _TMP_BANCOS_VS_VENTAS\n";
              	tbl += "values('1','" + p(valorParam) + "');\n";
              }
          }
          
          str += " sp_ventas_entidades_cambiar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + p(request.getParameter("ficha")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + 
          /*(request.getParameter("formato_mostr").equals("NINGUNO") ? "" : p(request.getParameter("formato_mostr"))) +*/ "','" + p(request.getParameter("idbodega")) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + (request.getParameter("fijacost") == null ? "0" : "1") + "','" + (request.getParameter("desglose") == null ? "0" : "1")  + "','" + (request.getParameter("mostraplicapolitica") == null ? "0" : "1")  + "','" + 
          p(request.getParameter("tipopago")) + "','" + (request.getParameter("cambionumero") == null ? "0" : "1")  + "','" + p(request.getParameter("pedido")) + "','" + p(request.getParameter("ajustedeprecio")) + "','" + p(request.getParameter("factordeajuste")) +  "','" + p(request.getParameter("devolucion")) + "','" +
          p(request.getParameter("idvendedor")) + "','" + (request.getParameter("fmt_devolucion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_devolucion"))) + "','" + (request.getParameter("fmt_pedido").equals("NINGUNO") ? "" : p(request.getParameter("fmt_pedido"))) + "','" + /*p(request.getParameter("factnumcie"))*/"0" + "','" + /*p(request.getParameter("devnumcie"))*/"0" + "','" + p(request.getParameter("ivaporcentual")) + "','" + p(request.getParameter("remision")) + "','" + p(request.getParameter("cotizacion")) + "','" + (request.getParameter("fmt_remision").equals("NINGUNO") ? "" : p(request.getParameter("fmt_remision"))) + "','" + (request.getParameter("fmt_cotizacion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_cotizacion"))) + "','" +
          // certificados fiscales digitales
          q(request.getParameter("cfd")) + "','0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "'," + 
          "'0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "'," + 
          "'0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "','" + q(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "') ";
          
          irA = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
      }
      else if(ent.equals("PRODUCCION"))
      {
    	  str += " sp_prod_entidades_cambiar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + 
    	  	p(request.getParameter("ficha")) + "','" +	p(request.getParameter("descripcion")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + p(request.getParameter("idbodegamp")) + "','" + p(request.getParameter("idbodegamp")) + "','" + 
			p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "') ";

    	  irA = "/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp";
      }
      else if(ent.equals("NOMINA"))
      {
    	  String contclave;
    	  String conttipo;
    	  String fijaclave;
    	  String fijatipo;
    	
    	  if(request.getParameter("contbancaj").equals("-1") || request.getParameter("contbancaj").equals("-2"))
    	  {
    		  conttipo = "-1";
    		  contclave = "-1";
    	  }
    	  else if(request.getParameter("contbancaj").indexOf("FSI_CONTBAN_") != -1) // es hacia banco
    	  {
    		  conttipo = "0";
    		  contclave = request.getParameter("contbancaj").substring(12);
    	  }
    	  else
    	  {
    		  conttipo = "1";
    		  contclave = request.getParameter("contbancaj").substring(12);
    	  }
    	  if(request.getParameter("fijabancaj").equals("-1") || request.getParameter("fijabancaj").equals("-2"))
    	  {
    		  fijatipo = "-1";
    		  fijaclave = "-1";
    	  }
    	  else if(request.getParameter("fijabancaj").indexOf("FSI_FIJABAN_") != -1) // es hacia banco
    	  {
    		  fijatipo = "0";
    		  fijaclave = request.getParameter("fijabancaj").substring(12);
    	  }
    	  else
    	  {
    		  fijatipo = "1";
    		  fijaclave = request.getParameter("fijabancaj").substring(12);
    	  }
	  	  	
    	  str += "sp_companias_cambiar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("tipo")) + "','" +
    	  		p(request.getParameter("periodo")) + "','" + /*(request.getParameter("fmt_nomina").equals("NINGUNO") ? "" : p(request.getParameter("fmt_nomina"))) + */ "','" + (request.getParameter("fmt_recibos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_recibos"))) + "','" +
  	  			p(request.getParameter("numero")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(conttipo) + "','" + p(contclave) + "','" + p(fijatipo) + "','" + p(fijaclave) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "','" +
        		q(request.getParameter("cfd")) + "','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "') ";

    	  irA = "/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp";
      	}
      	else if(ent.equals("MENSAJES"))
      	{
      		str += " sp_notas_blocks_cambiar('" + p(request.getParameter("idblock")) + "','" + p(request.getParameter("ficha")) + "','" + 
      			p(request.getParameter("descripcion")) + "') ";
    	
      		irA = "/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp";
      	}
        
        str += " as ( err integer, res varchar, clave varchar ) ";
        JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, tbl, str, drop, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_ENTIDADES_AGREGAR", "AENT|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag(irA, request, response);
		    
    }

    public void Agregar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        String ent = getSesion(request).getSesion("ADM_ENTIDADES").getEspecial();
        String str = "select * from ";
        String irA = "";
        String tbl = "";
        String drop = "";
        
        if(ent.equals("BANCOS"))
        {
        	str += "sp_bancos_cuentas_agregar( '0','" + p(request.getParameter("clave")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("sigcheque")) + "'," + 
        	(request.getParameter("fmt_dep").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_dep")) + "'") + "," + (request.getParameter("fmt_ret").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_ret")) + "'") + ",'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "','" + p(request.getParameter("status")) +
        	"','" + (request.getParameter("fija") == null ? "0" : "1") + "','0','" + p(request.getParameter("clave")) + "','0" +
        	"','0','0.00','" + p(request.getParameter("idmoneda")) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("id_satbanco")) + "','" + p(request.getParameter("bancoext")) + "')";
         	irA = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
        }
        else if(ent.equals("CAJAS"))
        {
         	String clave;
        	String tipo;
          	if(request.getParameter("bancaj").equals("NINGUNA"))
        	{
          		tipo = "1";
        	  	clave = request.getParameter("clave");
        	}
        	else if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1) // es hacia banco
        	{
        		tipo = "0";
        		clave = request.getParameter("bancaj").substring(8);
        	}
        	else
        	{
        		tipo = "1";
        		clave = request.getParameter("bancaj").substring(8);
        	}
          	
        	str += "sp_bancos_cuentas_agregar( '1','" + p(request.getParameter("clave")) + "','" + p(request.getParameter("ficha")) + "','0'," + 
        	(request.getParameter("fmt_dep").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_dep")) + "'") + "," + (request.getParameter("fmt_ret").equals("NINGUNO") ? "''" : "'" + p(request.getParameter("fmt_ret")) + "'") + ",'" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "','" + p(request.getParameter("status")) +
        	"','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + p(tipo) + "','" + p(clave) + "','0" +
        	"','" + p(request.getParameter("ultimonumtrasp")) + "','" + p(request.getParameter("fondotrasp")) + "','1','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("descripcion")) + "','000','')";
         	irA = "/forsetiweb/administracion/adm_entidades_dlg_bancos.jsp";
        }
        else if(ent.equals("BODEGAS"))
        {
        	str += "sp_invserv_bodegas_agregar('" + p(request.getParameter("idbodega")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("numero")) +
      		"','" + p(request.getParameter("salida")) + "','" + p(request.getParameter("requerimiento")) + "','" + p(request.getParameter("plantilla")) + "','" 
      		+ (request.getParameter("fmt_movimientos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_movimientos"))) + "','" + (request.getParameter("fmt_traspasos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_traspasos"))) + "','" 
      		+ (request.getParameter("fmt_requerimientos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_requerimientos"))) + "','" + (request.getParameter("fmt_plantillas").equals("NINGUNO") ? "" : p(request.getParameter("fmt_plantillas"))) + "','" + 
      		// certificados fiscales digitales
            q(request.getParameter("cfd")) + "','0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "','0'," + 
            "'" + p(request.getParameter("idinvserv")) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("numchfis")) + "','" + p(request.getParameter("status")) + "','" + (request.getParameter("fmt_chfis").equals("NINGUNO") ? "" : p(request.getParameter("fmt_chfis"))) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "')";
      	   	irA = "/forsetiweb/administracion/adm_entidades_dlg_bodegas.jsp";
        }
        else if(ent.equals("COMPRAS"))
        {
        	drop += "DROP TABLE _TMP_BANCOS_VS_COMPRAS\n";
      	  	tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_VS_COMPRAS (\n";
      	  	tbl += "	Tipo smallint NOT NULL ,\n";
      	  	tbl += "	Clave smallint NOT NULL\n";
      	  	tbl += ");\n";
        	
            @SuppressWarnings("rawtypes")
			Enumeration nombresParam = request.getParameterNames();
            while(nombresParam.hasMoreElements())
            {
                String nombreParam = (String)nombresParam.nextElement();
                if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
                	continue;
                
                String claveParam = nombreParam.substring(0,8);
                String valorParam = nombreParam.substring(8);
                if(claveParam.equals("PER_BAN_"))
                {
                	tbl += "insert into _TMP_BANCOS_VS_COMPRAS\n";
                	tbl += "values('0','" + p(valorParam) + "');\n";
                }
                else if(claveParam.equals("PER_CAJ_"))
                {
                	tbl += "insert into _TMP_BANCOS_VS_COMPRAS\n";
                	tbl += "values('1','" + p(valorParam) + "');\n";
                }
            }
            
            str += " sp_compras_entidades_agregar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("tipo")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + p(request.getParameter("ficha")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + 
            p(request.getParameter("idbodega")) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + (request.getParameter("fijacost") == null ? "0" : "1") + "','" + p(request.getParameter("devolucion")) + "','" + p(request.getParameter("orden")) + "','" + (request.getParameter("fmt_devolucion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_devolucion"))) + "','" + (request.getParameter("fmt_orden").equals("NINGUNO") ? "" : p(request.getParameter("fmt_orden"))) + "','" + p(request.getParameter("ivaporcentual")) +
          	"','" + /*p(request.getParameter("infoplantoc"))*/"-1" + "','" + /*p(request.getParameter("infogasrec"))*/"-1" + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("recepcion")) + "','" + (request.getParameter("fmt_recepcion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_recepcion"))) + "') ";
        	
            irA = "/forsetiweb/administracion/adm_entidades_dlg_compras.jsp";
        }
        else if(ent.equals("VENTAS"))
        {
        	drop += "DROP TABLE _TMP_BANCOS_VS_VENTAS\n";
           	tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_BANCOS_VS_VENTAS (\n";
       		tbl += "	Tipo smallint NOT NULL ,\n";
    		tbl += "	Clave smallint NOT NULL\n";
    		tbl += ");\n";
        	
            @SuppressWarnings("rawtypes")
			Enumeration nombresParam = request.getParameterNames();
            while(nombresParam.hasMoreElements())
            {
                String nombreParam = (String)nombresParam.nextElement();
                if(nombreParam.length() < 4 || !nombreParam.substring(0,4).equals("PER_"))
                	continue;
                
                String claveParam = nombreParam.substring(0,8);
                String valorParam = nombreParam.substring(8);
                if(claveParam.equals("PER_BAN_"))
                {
                	tbl += "insert into _TMP_BANCOS_VS_VENTAS\n";
                	tbl += "values('0','" + p(valorParam) + "');\n";
                }
                else if(claveParam.equals("PER_CAJ_"))
                {
                	tbl += "insert into _TMP_BANCOS_VS_VENTAS\n";
                	tbl += "values('1','" + p(valorParam) + "');\n";
                }
            }
            
            str += " sp_ventas_entidades_agregar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + p(request.getParameter("ficha")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + 
            /*(request.getParameter("formato_mostr").equals("NINGUNO") ? "" : p(request.getParameter("formato_mostr"))) +*/ "','" + p(request.getParameter("idbodega")) + "','" + (request.getParameter("fija") == null ? "0" : "1") + "','" + (request.getParameter("fijacost") == null ? "0" : "1") + "','" + (request.getParameter("desglose") == null ? "0" : "1")  + "','" + (request.getParameter("mostraplicapolitica") == null ? "0" : "1")  + "','" + 
            p(request.getParameter("tipopago")) + "','" + (request.getParameter("cambionumero") == null ? "0" : "1")  + "','" + p(request.getParameter("pedido")) + "','" + p(request.getParameter("ajustedeprecio")) + "','" + p(request.getParameter("factordeajuste")) +  "','" + p(request.getParameter("devolucion")) + "','" +
            p(request.getParameter("idvendedor")) + "','" + (request.getParameter("fmt_devolucion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_devolucion"))) + "','" + (request.getParameter("fmt_pedido").equals("NINGUNO") ? "" : p(request.getParameter("fmt_pedido"))) + "','" + /*p(request.getParameter("factnumcie"))*/"0" + "','" + /*p(request.getParameter("devnumcie"))*/"0" + "','" + p(request.getParameter("ivaporcentual")) + "','" + p(request.getParameter("remision")) + "','" + p(request.getParameter("cotizacion")) + "','" + (request.getParameter("fmt_remision").equals("NINGUNO") ? "" : p(request.getParameter("fmt_remision"))) + "','" + (request.getParameter("fmt_cotizacion").equals("NINGUNO") ? "" : p(request.getParameter("fmt_cotizacion"))) + "','" +
            // certificados fiscales digitales
            q(request.getParameter("cfd")) + "','0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "'," + 
            "'0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "'," + 
            "'0','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "') ";
            
            irA = "/forsetiweb/administracion/adm_entidades_dlg_ventas.jsp";
        }
        else if(ent.equals("PRODUCCION"))
        {
        	str += " sp_prod_entidades_agregar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("serie")) + "','" + p(request.getParameter("numero")) + "','" + 
        		p(request.getParameter("ficha")) + "','" +	p(request.getParameter("descripcion")) + "','" + (request.getParameter("formato").equals("NINGUNO") ? "" : p(request.getParameter("formato"))) + "','" + p(request.getParameter("idbodegamp")) + "','" + p(request.getParameter("idbodegamp")) + "','" + 
      			p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "') ";
      	
        	irA = "/forsetiweb/administracion/adm_entidades_dlg_produccion.jsp";
        }
        else if(ent.equals("NOMINA"))
        {
        	String contclave;
        	String conttipo;
        	String fijaclave;
        	String fijatipo;
        	
      	  	if(request.getParameter("contbancaj").equals("-1") || request.getParameter("contbancaj").equals("-2"))
      	  	{
      	  		conttipo = "-1";
      	  		contclave = "-1";
      	  	}
      	  	else if(request.getParameter("contbancaj").indexOf("FSI_CONTBAN_") != -1) // es hacia banco
      	  	{
      	  		conttipo = "0";
      	  		contclave = request.getParameter("contbancaj").substring(12);
      	  	}
      	  	else
      	  	{
      	  		conttipo = "1";
      	  		contclave = request.getParameter("contbancaj").substring(12);
      	  	}
      	  	if(request.getParameter("fijabancaj").equals("-1") || request.getParameter("fijabancaj").equals("-2"))
    	  	{
    	  		fijatipo = "-1";
    	  		fijaclave = "-1";
    	  	}
    	  	else if(request.getParameter("fijabancaj").indexOf("FSI_FIJABAN_") != -1) // es hacia banco
    	  	{
    	  		fijatipo = "0";
    	  		fijaclave = request.getParameter("fijabancaj").substring(12);
    	  	}
    	  	else
    	  	{
    	  		fijatipo = "1";
    	  		fijaclave = request.getParameter("fijabancaj").substring(12);
    	  	}
      	  	
        	str += "sp_companias_agregar('" + p(request.getParameter("identidad")) + "','" + p(request.getParameter("ficha")) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("tipo")) + "','" +
        		p(request.getParameter("periodo")) + "','" + /*(request.getParameter("fmt_nomina").equals("NINGUNO") ? "" : p(request.getParameter("fmt_nomina"))) +*/ "','" + (request.getParameter("fmt_recibos").equals("NINGUNO") ? "" : p(request.getParameter("fmt_recibos"))) + "','" +
        		p(request.getParameter("numero")) + "','" + p(JUtil.obtFechaSQL(request.getParameter("fecha"))) + "','" + p(conttipo) + "','" + p(contclave) + "','" + p(fijatipo) + "','" + p(fijaclave) + "','" + p(request.getParameter("idclasificacion")) + "','" + p(request.getParameter("status")) + "','" +
        		q(request.getParameter("cfd")) + "','" + q(request.getParameter("cfd_nocertificado")) + "','" + q(request.getParameter("cfd_id_expedicion")) + "') ";
      	
        	irA = "/forsetiweb/administracion/adm_entidades_dlg_nomina.jsp";
        }
        else if(ent.equals("MENSAJES"))
        {
        	str += " sp_notas_blocks_agregar('" + p(request.getParameter("idblock")) + "','" + p(request.getParameter("ficha")) + "','" + 
        		p(request.getParameter("descripcion")) + "') ";
      	
        	irA = "/forsetiweb/administracion/adm_entidades_dlg_mensajes.jsp";
        }
        str += " as ( err integer, res varchar, clave varchar ) ";
        JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedure(request, response, tbl, str, drop, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_ENTIDADES_AGREGAR", "AENT|" + rfb.getClaveret() + "|||",rfb.getRes());
	    irApag(irA, request, response);
    }
 

}
