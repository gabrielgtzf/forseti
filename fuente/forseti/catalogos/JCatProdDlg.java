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
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JInvServExistenciasSetV2;
import forseti.sets.JPublicBodegasCatSetV2;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JPublicInvServLineasSetV2;

@SuppressWarnings("serial")
public class JCatProdDlg extends JForsetiApl
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

      String cat_prod_dlg = "";
      request.setAttribute("cat_prod_dlg",cat_prod_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_PRODUCTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("INVSERV_PROD_AGREGAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_PROD_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_PROD_AGREGAR","CATP||||",mensaje);
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
        	  
        	  irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  HttpSession ses = request.getSession(true);
              JCatProdSes cat = (JCatProdSes) ses.getAttribute("cat_prod_dlg");
              if (cat == null) 
              {
                cat = new JCatProdSes();
                ses.setAttribute("cat_prod_dlg", cat);
              }
              else
                cat.resetear();

              JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
              set.m_Where = "ID_InvServ = 'P'";
              set.Open();

              for (int i = 0; i < set.getNumRows(); i++)
                cat.agregaBodega(set.getAbsRow(i).getID_Bodega(),
                                 set.getAbsRow(i).getNombre(), 0.00F, 0.00F);

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);
              return;
          }  
            /* se trata de servicios
            {
              HttpSession ses = request.getSession(true);
              JCatProdSes cat = (JCatProdSes) ses.getAttribute("cat_serv_dlg");
              if (cat == null) {
                cat = new JCatProdSes();
                ses.setAttribute("cat_serv_dlg", cat);
              }
              else
                cat.resetear();

              JPublicInvServLineasSetV2 set2 = new JPublicInvServLineasSetV2(request);
              set2.m_Where = "ID_InvServ = 'S'";
              set2.Open();

              for (int i = 0; i < set2.getNumRows(); i++)
                cat.agregaLinea(set2.getAbsRow(i).getClave(),
                                set2.getAbsRow(i).getDescripcion(), false);

              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/almacen/cat_serv_dlg.jsp", request, response);

            }
            */
          
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_PRODUCTO"))
        {
        	 if(!getSesion(request).getPermiso("INVSERV_PROD"))
             {
                 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_PROD");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_PROD","CATP||||",mensaje);
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
        			 JCatProdSes cat = (JCatProdSes) ses.getAttribute("cat_prod_dlg");
        			 if (cat == null) 
        			 {
        				 cat = new JCatProdSes();
        				 ses.setAttribute("cat_prod_dlg", cat);
        			 }
        			 else
        				 cat.resetear();

        			 // Llena el producto
        			 JInvServExistenciasSetV2 setExist = new JInvServExistenciasSetV2(request, "P");
        			 setExist.m_Where = "Clave = '" + p(request.getParameter("id")) + "' and Tipo = 'P'";
        			 setExist.Open();
        			 for (int i = 0; i < setExist.getNumRows(); i++) 
        			 {
        				 cat.agregaBodega(setExist.getAbsRow(i).getID_Bodega(), 
                        			 setExist.getAbsRow(i).getBodega(),
                        			 	setExist.getAbsRow(i).getStockMin(),
                        			 	setExist.getAbsRow(i).getStockMax());
        			 }

        			 RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"INVSERV_PROD","CATP|" + request.getParameter("id") + "|||","");
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_PRODUCTO"))
        {
        	if(!getSesion(request).getPermiso("INVSERV_PROD_CAMBIAR"))
            {
                idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_PROD_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_PROD_CAMBIAR","CATP||||",mensaje);
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
        	        	  
        				irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);
        				return;
        			}
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				HttpSession ses = request.getSession(true);
        				JCatProdSes cat = (JCatProdSes) ses.getAttribute("cat_prod_dlg");
        				if (cat == null) 
        				{
        					cat = new JCatProdSes();
        					ses.setAttribute("cat_prod_dlg", cat);
        				}
        				else
        					cat.resetear();

        				// Llena el producto
        				JInvServExistenciasSetV2 setExist = new JInvServExistenciasSetV2(request,"P");
        				setExist.m_Where = "Clave = '" + p(request.getParameter("id")) + "' and Tipo = 'P'";
           			 	setExist.Open();
           			 	for (int i = 0; i < setExist.getNumRows(); i++) 
           			 	{
           			 		cat.agregaBodega(setExist.getAbsRow(i).getID_Bodega(), 
                           			 setExist.getAbsRow(i).getBodega(),
                           			 	setExist.getAbsRow(i).getStockMin(),
                           			 	setExist.getAbsRow(i).getStockMax());
           			 	}

           			 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);
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
      if(request.getParameter("clave") != null && request.getParameter("cuenta") != null && request.getParameter("descripcion") != null && request.getParameter("precio") != null && request.getParameter("precio2") != null &&
         request.getParameter("precio3") != null && request.getParameter("precio4") != null && request.getParameter("precio5") != null && request.getParameter("preciomin") != null && request.getParameter("preciomax") != null && request.getParameter("unidad") != null && request.getParameter("linea") != null &&
         request.getParameter("status") != null && request.getParameter("obs") != null &&
         request.getParameter("codigo") != null && request.getParameter("ieps") != null &&
         !request.getParameter("clave").equals("") && !request.getParameter("cuenta").equals("") && !request.getParameter("descripcion").equals("") && !request.getParameter("precio").equals("") && !request.getParameter("precio2").equals("") &&
         !request.getParameter("precio3").equals("") && !request.getParameter("precio4").equals("") && !request.getParameter("precio5").equals("") && !request.getParameter("preciomin").equals("") && !request.getParameter("preciomax").equals("") && !request.getParameter("unidad").equals("") && !request.getParameter("linea").equals("") &&
         !request.getParameter("status").equals("") && !request.getParameter("ieps").equals("") )
      {
        float precio = Float.parseFloat(request.getParameter("precio")),
            precio2 = Float.parseFloat(request.getParameter("precio2")),
            precio3 = Float.parseFloat(request.getParameter("precio3")),
            precio4 = Float.parseFloat(request.getParameter("precio4")),
            precio5 = Float.parseFloat(request.getParameter("precio5")),
            preciomin = Float.parseFloat(request.getParameter("preciomin")),
            preciomax = Float.parseFloat(request.getParameter("preciomax"));
                   
        if (precio < 0.00 || precio2 < 0.00 || precio3 < 0.00 || precio4 < 0.00 ||
            precio5 < 0.00 || preciomin < 0.00 || preciomax < 0.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 1); //"ERROR: Los precios no pueden ser menor que 0.00 <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
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
            mensaje = JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",1); // "PRECAUCION: La cuenta contable para este producto existe, pero no se puede agregar porque es una cuenta acumilativa <br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
          }
        }
        else
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",3); //"ERROR: La cuenta contable para este producto no existe <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }

        float ieps = Float.parseFloat(request.getParameter("ieps"));
        
        if (ieps < 0.00 || ieps > 100.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR2", 4); //ERROR: El IEPS, Retención de IVA o de ISR no pueden ser menor que 0.00 ni mayor que 100.00
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        // verifica la linea
        JPublicInvServLineasSetV2 lin = new JPublicInvServLineasSetV2(request);
        lin.m_Where = "Clave = '" + p(request.getParameter("linea")) + "' and ID_InvServ = 'P'";
        lin.Open();
        if(lin.getNumRows() < 1)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 2); //"ERROR: La linea capturada no existe. <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        // Ahora verifica los stocks
        JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
        set.m_Where = "ID_InvServ = 'P'";
        set.Open();
        String bodega = "";
        boolean flag = true;
        for(int i = 0; i< set.getNumRows(); i++)
        {
            float StockMin = Float.parseFloat(request.getParameter("FSI_MIN_" + set.getAbsRow(i).getID_Bodega()));
            float StockMax = Float.parseFloat(request.getParameter("FSI_MAX_" + set.getAbsRow(i).getID_Bodega()));
            bodega = set.getAbsRow(i).getNombre();
            if((StockMin == -1 && StockMax != -1) || (StockMax == -1 && StockMin != -1) ||
               (StockMin != -1 && StockMin < 0) || (StockMax != -1 && StockMax < 0) ||
               (StockMin > StockMax) )
            {
              flag = false;
              break;
            }
        }

        if(!flag)
        {
            idmensaje = 3;
            mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 3) + " - " + bodega; //ERROR: La bodega " + bodega + " está manejando mal los stocks. Estos deben ser -1 ambos, si no se maneja el producto en esta bodega, 0 ambos si se va a manejar el producto pero no se requiere el control de stocks. Por último, si se van a manejar los stocks, el stock mínimo debe ser menor al stock máximo. <br>";
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
        tbl += "VALUES('" + set.getAbsRow(i).getID_Bodega() + "','0.000','" + 
        		( request.getParameter("FSI_MIN_" + set.getAbsRow(i).getID_Bodega()) == null ? "0.000" : p(request.getParameter("FSI_MIN_" + set.getAbsRow(i).getID_Bodega())) ) + "','" + 
        		( request.getParameter("FSI_MAX_" + set.getAbsRow(i).getID_Bodega()) == null ? "0.000" : p(request.getParameter("FSI_MAX_" + set.getAbsRow(i).getID_Bodega())) ) + "'); \n";
      }

      String codigo = (p(request.getParameter("codigo")).equals("")) ? p(request.getParameter("clave")) : p(request.getParameter("codigo"));
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
      
      String str = "select * from  " + proc + " ('" + p(request.getParameter("clave")) + "','P','" + p(JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19)) + "','" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("linea")) + "','" + p(request.getParameter("precio")) + "','0.000','" + p(request.getParameter("unidad")) + "','0.000','0.000','" +
          (request.getParameter("seproduce") != null ? "1" : "0") + "','" + (request.getParameter("nosevende") != null ? "1" : "0") + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("obs")) + "','" + p(request.getParameter("unidad")) + "','1.000000','0.000','0.000','0.000','0.000','0','0.0000','0.0000','" + p(request.getParameter("ieps")) + "','0.000000','0.000000','0.000000','" + (request.getParameter("iva") != null ? "1" : "0") + "','0.000','1','0.000','0.00','" +
          p(request.getParameter("precio2")) + "','" + p(request.getParameter("precio3")) + "','" + p(request.getParameter("precio4")) + "','" + p(request.getParameter("precio5")) + "','0.00','0.00','','','','','','','0','0','0','0.000','2','" + p(codigo) + "','" + p(request.getParameter("preciomin"))  + "','" + p(request.getParameter("preciomax")) + "') as ( err integer, res varchar, clave varchar)";

      JRetFuncBas rfb = new JRetFuncBas();
  	
	  doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_EXISTENCIAS ", rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "INVSERV_PROD_" + proceso, "CATP|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/catalogos/cat_prod_dlg.jsp", request, response);

    }
}
