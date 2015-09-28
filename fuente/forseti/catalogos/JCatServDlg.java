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
package forseti.catalogos;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JPublicBodegasCatSetV2;
import forseti.sets.JPublicInvServLineasSetV2;

@SuppressWarnings("serial")
public class JCatServDlg extends JForsetiApl
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

      String cat_serv_dlg = "";
      request.setAttribute("cat_serv_dlg",cat_serv_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_SERVICIO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("INVSERV_SERV_AGREGAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_SERV_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_SERV_AGREGAR","CATS||||",mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametros(request, response))
        	  {
        		  AgregarCambiar(request, response, "AGREGAR");
        	   	  return;
        	  }
        	  
        	  irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);
              return;
          }  
            
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_SERVICIO"))
        {
        	 if(!getSesion(request).getPermiso("INVSERV_SERV"))
             {
                 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_SERV");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_SERV","CATS||||",mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
             }

        	 // Solicitud de envio a procesar
        	 if(request.getParameter("id") != null)
        	 {
        		 String[] valoresParam = request.getParameterValues("id");
        		 if(valoresParam.length == 1)
        		 {
        			 RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"INVSERV_SERV","CATS|" + request.getParameter("id") + "|||","");
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);
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
        		 idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 irApag("/forsetiweb/caja_mensajes.jsp", request, response);
                 return;
        	 }

        }
        else if(request.getParameter("proceso").equals("CAMBIAR_SERVICIO"))
        {
        	if(!getSesion(request).getPermiso("INVSERV_SERV_CAMBIAR"))
            {
                idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_SERV_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_SERV_CAMBIAR","CATS||||",mensaje);
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
        					AgregarCambiar(request, response, "CAMBIAR");
        					return;
        				}
        	        	  
        				irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);
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
        	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); 
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else
        {
        	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiweb/caja_mensajes.jsp", request, response);
            return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
    	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); 
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
      if(request.getParameter("clave") != null && request.getParameter("descripcion") != null && request.getParameter("precio") != null && 
         request.getParameter("preciomin") != null && request.getParameter("preciomax") != null && request.getParameter("linea") != null &&
         request.getParameter("status") != null && request.getParameter("obs") != null && request.getParameter("ivaret") != null && request.getParameter("isrret") != null && 
         !request.getParameter("clave").equals("") && !request.getParameter("descripcion").equals("") && !request.getParameter("precio").equals("") && 
         !request.getParameter("preciomin").equals("") && !request.getParameter("preciomax").equals("") && !request.getParameter("linea").equals("") &&
         !request.getParameter("status").equals("") && !request.getParameter("ivaret").equals("") && !request.getParameter("isrret").equals("") )
      {
    	float precio = Float.parseFloat(request.getParameter("precio")),
            preciomin = Float.parseFloat(request.getParameter("preciomin")),
            preciomax = Float.parseFloat(request.getParameter("preciomax"));
                   
        if (precio < 0.00 || preciomin < 0.00 || preciomax < 0.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 1); //"ERROR: Los precios no pueden ser menor que 0.00 <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        float ivaret = Float.parseFloat(request.getParameter("ivaret"));
        float isrret = Float.parseFloat(request.getParameter("isrret"));

        if (ivaret < 0.00 || isrret < 0.00 || ivaret > 100.00 || isrret > 100.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR2", 4); //ERROR: El IEPS, Retención de IVA o de ISR no pueden ser menor que 0.00 ni mayor que 100.00
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        // verifica la linea
        JPublicInvServLineasSetV2 lin = new JPublicInvServLineasSetV2(request);
        lin.m_Where = "Clave = '" + p(request.getParameter("linea")) + "' and ID_InvServ = 'S'";
        lin.Open();
        if(lin.getNumRows() < 1)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 2); //"ERROR: La linea capturada no existe. <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
                        
        // Ahora verifica el status
        if(!request.getParameter("status").equals("V") && !request.getParameter("status").equals("D"))
        {
          idmensaje = 2;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 4); //"PREGUNTA: ¿ Estas usando una versión diferente de forseti ?. El sistema ha encontrado que el status no pertenece a los establecidos, Falló en el status del producto.
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        return true;
      }
      else
      {
        idmensaje = 1; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"); //"PRECAUCION: Alguno de los parametros necesarios es Nulo <br>";
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        return false;
      }
    }

    public void AgregarCambiar(HttpServletRequest request, HttpServletResponse response, String proceso)
      throws ServletException, IOException
    {
      String tbl;
      tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_EXISTENCIAS (\n";
      tbl += "ID_Bodega smallint NOT NULL ,\n";
      tbl += "Existencia numeric(9, 3) NOT NULL ,\n";
      tbl += "StockMin numeric(9, 3) NOT NULL ,\n";
      tbl += "StockMax numeric(9, 3) NOT NULL \n";
      tbl += "); \n";
      
      JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
      set.Open();
      for(int i = 0; i< set.getNumRows(); i++)
      {
        tbl += "INSERT INTO _TMP_INVSERV_EXISTENCIAS \n";
        tbl += "VALUES('" + set.getAbsRow(i).getID_Bodega() + "','0.000','0.000','0.000'); \n";
      }
      
      String codigo = p(request.getParameter("clave"));
      String proc = ( proceso.equals("AGREGAR") ) ? "sp_invserv_inventarios_agregar" : "sp_invserv_inventarios_cambiar";
      /*
      _ID_Prod varchar, _ID_Tipo char, _ID_CC char, _Descripcion varchar, _ID_Linea varchar, _Precio numeric, _Existencia numeric, _ID_Unidad varchar,
      _StockMin numeric, _StockMax numeric, _SeProduce bit, _NoSeVende bit, _Status char,  _Obs varchar, _ID_UnidadSalida varchar, _Factor numeric, 
      _Empaque numeric, _PorSurtir numeric, _PorRecibir numeric, _Apartado numeric, _Dias smallint, _UltimoCosto numeric, _CostoPromedio numeric,  _ImpIEPS numeric, _IVA_Deducible numeric,
      _IVA bit, _CantidadPaquete numeric, _TipoCosteo smallint, _CantidadAcum numeric, _MontoAcum numeric,
      _Precio2 numeric, _Precio3 numeric, _Precio4 numeric, _Precio5 numeric, 
      _PrecioWeb numeric, _PrecioOfertaWeb numeric, _DescripcionWeb character varying, _ComentariosWeb character varying, _DescripcionWebIng character varying, _ComentariosWebIng character varying, _Ref_FotoWeb character varying, _Ref_FotoChicaWeb character varying, _NuevoWeb bit, 
      _NumRecomWeb smallint, _NumExperienciasWeb smallint, _KgsWeb numeric, _SincronizacionWeb smallint, _Codigo varchar, _PrecioMin numeric, _PrecioMax numeric
      */
      
      String str = "select * from  " + proc + " ('" + p(request.getParameter("clave")) + "','S',null,'" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("linea")) + "','" + p(request.getParameter("precio")) + "','0.000','','0.000','0.000','" +
          "0','0','" + p(request.getParameter("status")) + "','" + p(request.getParameter("obs")) + "','','1.000000','0.000','0.000','0.000','0.000','0','0.0000','0.0000','0.000000','" + p(request.getParameter("ivaret")) + "','" + p(request.getParameter("isrret")) + "','0.000000','" + (request.getParameter("iva") != null ? "1" : "0") + "','0.000','1','0.000','0.00','" +
          p(request.getParameter("precio")) + "','" + p(request.getParameter("precio")) + "','" + p(request.getParameter("precio")) + "','" + p(request.getParameter("precio")) + "','0.00','0.00','','','','','','','0','0','0','0.000','2','" + p(codigo) + "','" + p(request.getParameter("preciomin"))  + "','" + p(request.getParameter("preciomax")) + "') as ( err integer, res varchar, clave varchar)";

      JRetFuncBas rfb = new JRetFuncBas();
  	
	  doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_EXISTENCIAS ", rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "INVSERV_SERV_" + proceso, "CATS|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/catalogos/cat_serv_dlg.jsp", request, response);


    }

}
