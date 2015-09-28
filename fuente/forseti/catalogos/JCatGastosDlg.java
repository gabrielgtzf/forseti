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
import forseti.catalogos.JCatGastosSes;
import forseti.sets.JInvServExistenciasSetV2;
import forseti.sets.JInvServGastosDetallesSetV2;
import forseti.sets.JPublicBodegasCatSetV2;
import forseti.sets.JPublicInvServLineasSetV2;

@SuppressWarnings("serial")
public class JCatGastosDlg extends JForsetiApl
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

      String cat_gastos_dlg = "";
      request.setAttribute("cat_gastos_dlg",cat_gastos_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("AGREGAR_GASTO"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("INVSERV_GASTOS_AGREGAR"))
          {
              idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_GASTOS_AGREGAR");
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_GASTOS_AGREGAR","CATG||||",mensaje);
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
        	  
        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
          {
        	  if(VerificarParametrosPartida(request, response))
        		  AgregarPartida(request, response);
        	          	  
        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
              return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
          {
        	  if(VerificarParametrosPartida(request, response))
        		  EditarPartida(request, response);
            
        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        	  return;
          }
          else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
          {
        	  BorrarPartida(request, response);
        	  
        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
              return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
        	  HttpSession ses = request.getSession(true);
        	  JCatGastosSes cat = (JCatGastosSes) ses.getAttribute("cat_gastos_dlg");
        	  if (cat == null) 
        	  {
        		  cat = new JCatGastosSes();
        		  ses.setAttribute("cat_gastos_dlg", cat);
        	  }
        	  else
        		  cat.resetear();

        	  JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
              set.m_Where = "ID_InvServ = 'G'";
              set.Open();

              for (int i = 0; i < set.getNumRows(); i++)
                cat.agregaBodega(set.getAbsRow(i).getID_Bodega(),
                                 set.getAbsRow(i).getNombre(), true);
              
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        	  return;
          }
            
          
        }
        else if(request.getParameter("proceso").equals("CONSULTAR_GASTO"))
        {
        	 if(!getSesion(request).getPermiso("INVSERV_GASTOS"))
             {
                 idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_GASTOS");
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_GASTOS","CATG||||",mensaje);
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
        			 JCatGastosSes cat = (JCatGastosSes) ses.getAttribute("cat_gastos_dlg");
        			 if (cat == null) 
        			 {
        				 cat = new JCatGastosSes();
        				 ses.setAttribute("cat_gastos_dlg", cat);
        			 }
        			 else
        				 cat.resetear();

        			 // Llena el gasto
                     JInvServGastosDetallesSetV2 set = new JInvServGastosDetallesSetV2(request);
                     set.m_Where = "ID_Prod = '" + p(request.getParameter("id")) + "'";
                     set.Open();
                     for(int i = 0; i < set.getNumRows(); i++)
                     {
                    	 cat.agregaPartida( set.getAbsRow(i).getCuenta(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getPorcentaje() );
                     }

                     // Llena el gasto en bodegas
                     JInvServExistenciasSetV2 setExist = new JInvServExistenciasSetV2(request,"G");
                     setExist.m_Where = "Clave = '" + p(request.getParameter("id")) + "' and Tipo = 'G'";
                     setExist.Open();
                     for (int i = 0; i < setExist.getNumRows(); i++) 
                     {
                    	 cat.agregaBodega(setExist.getAbsRow(i).getID_Bodega(), 
                        			 setExist.getAbsRow(i).getBodega(),
                        			(setExist.getAbsRow(i).getStockMin() == 0 ? true : false));
                     }
        			 
        			 RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"INVSERV_GASTOS","CATG|" + request.getParameter("id") + "|||","");
                     getSesion(request).setID_Mensaje(idmensaje, mensaje);
                     irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
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
        else if(request.getParameter("proceso").equals("CAMBIAR_GASTO"))
        {
        	if(!getSesion(request).getPermiso("INVSERV_GASTOS_CAMBIAR"))
            {
                idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "INVSERV_GASTOS_CAMBIAR");
                getSesion(request).setID_Mensaje(idmensaje, mensaje);
                RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"INVSERV_GASTOS_CAMBIAR","CATG||||",mensaje);
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
        	        	  
        				irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        				return;
        			}
        			else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("AGR_PART"))
        	        {
        	        	  if(VerificarParametrosPartida(request, response))
        	        		  AgregarPartida(request, response);
        	        	          	  
        	        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        	              return;
        	        }
        	        else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("EDIT_PART"))
        	        {
        	        	  if(VerificarParametrosPartida(request, response))
        	        		  EditarPartida(request, response);
        	            
        	        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        	        	  return;
        	        }
        	        else if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("BORR_PART"))
        	        {
        	        	  BorrarPartida(request, response);
        	        	  
        	        	  irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
        	              return;
        	        }
        			else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
        			{
        				HttpSession ses = request.getSession(true);
        				JCatGastosSes cat = (JCatGastosSes) ses.getAttribute("cat_gastos_dlg");
        				if (cat == null) 
        				{
        					cat = new JCatGastosSes();
        					ses.setAttribute("cat_gastos_dlg", cat);
        				}
        				else
        					cat.resetear();

        				// Llena el gasto
                        JInvServGastosDetallesSetV2 set = new JInvServGastosDetallesSetV2(request);
                        set.m_Where = "ID_Prod = '" + p(request.getParameter("id")) + "'";
                        set.Open();
                        for(int i = 0; i < set.getNumRows(); i++)
                        {
                          cat.agregaPartida( set.getAbsRow(i).getCuenta(), set.getAbsRow(i).getNombre(), set.getAbsRow(i).getPorcentaje() );
                        }

                        // Llena el gasto en bodegas
        				JInvServExistenciasSetV2 setExist = new JInvServExistenciasSetV2(request,"G");
        				setExist.m_Where = "Clave = '" + p(request.getParameter("id")) + "' and Tipo = 'G'";
           			 	setExist.Open();
           			 	//System.out.println(setExist.getSQL() + " : " + setExist.getNumRows());
           			 	for (int i = 0; i < setExist.getNumRows(); i++) 
           			 	{
           			 		//System.out.println(setExist.getAbsRow(i).getBodega());
         			 		cat.agregaBodega(setExist.getAbsRow(i).getID_Bodega(), 
                           			 setExist.getAbsRow(i).getBodega(),
                           			(setExist.getAbsRow(i).getStockMin() == 0 ? true : false));
           			 	}
           			 	
           			 	getSesion(request).setID_Mensaje(idmensaje, mensaje);
                        irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);
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

    public boolean VerificarParametrosPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; String mensaje = "";
	    // Verificacion
	    if(request.getParameter("cuenta") != null && !request.getParameter("cuenta").equals("") &&
	       request.getParameter("porcentaje") != null && !request.getParameter("porcentaje").equals(""))
	    {
	    	return true;
	    }
	    else
	    {
	        idmensaje = 1; 
	        mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 5); //"PRECAUCION: Se deben enviar los parametros de cuenta y porcentaje <br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
	    }
	}
	
	public void AgregarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JCatGastosSes pol = (JCatGastosSes)ses.getAttribute("cat_gastos_dlg");
	
	    float porcentaje = (request.getParameter("porcentaje") != null && !request.getParameter("porcentaje").equals("")) ?
	        Float.parseFloat(request.getParameter("porcentaje")) : 0F;
	
	    idmensaje = pol.agregaPartida(request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), porcentaje, mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	  
	}
	
	public void EditarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JCatGastosSes pol = (JCatGastosSes)ses.getAttribute("cat_gastos_dlg");
	
	    float porcentaje = (request.getParameter("porcentaje") != null && !request.getParameter("porcentaje").equals("")) ?
	        Float.parseFloat(request.getParameter("porcentaje")) : 0F;
	
	    idmensaje = pol.editaPartida(Integer.parseInt(request.getParameter("idpartida")), request, JUtil.obtCuentas(request.getParameter("cuenta"),(byte)19), porcentaje, mensaje);
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	}
	
	public void BorrarPartida(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    short idmensaje = -1; StringBuffer mensaje = new StringBuffer(254);
	
	    HttpSession ses = request.getSession(true);
	    JCatGastosSes pol = (JCatGastosSes)ses.getAttribute("cat_gastos_dlg");
	
	    pol.borraPartida(Integer.parseInt(request.getParameter("idpartida")));
	
	    getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	
	}

    public boolean VerificarParametros(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("clave") != null && request.getParameter("descripcion") != null && request.getParameter("preciomax") != null && 
    		request.getParameter("linea") != null && request.getParameter("status") != null && request.getParameter("obs") != null && request.getParameter("deduccioniva") != null &&
    				request.getParameter("ieps") != null && request.getParameter("ivaret") != null && request.getParameter("isrret") != null && 
         !request.getParameter("clave").equals("") && !request.getParameter("descripcion").equals("") && !request.getParameter("preciomax").equals("") && 
         	!request.getParameter("linea").equals("") && !request.getParameter("status").equals("") && !request.getParameter("deduccioniva").equals("") &&
         	!request.getParameter("ieps").equals("") && !request.getParameter("ivaret").equals("") && !request.getParameter("isrret").equals(""))
      {
        float preciomax = Float.parseFloat(request.getParameter("preciomax"));
                   
        if (preciomax < 0.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 1); //"ERROR: Los precios no pueden ser menor que 0.00 <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        float deduccioniva = Float.parseFloat(request.getParameter("deduccioniva"));
        float ieps = Float.parseFloat(request.getParameter("ieps"));
        float ivaret = Float.parseFloat(request.getParameter("ivaret"));
        float isrret = Float.parseFloat(request.getParameter("isrret"));

        if (deduccioniva < 0.00 )
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR2", 1); //"ERROR: El iva y su deduccion no pueden ser menor que 0.00 <br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        if (ieps < 0.00 || ivaret < 0.00 || isrret < 0.00 || ieps > 100.00 || ivaret > 100.00 || isrret > 100.00)
        {
          idmensaje = 3;
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR2", 4); //ERROR: El IEPS, Retención de IVA o de ISR no pueden ser menor que 0.00 ni mayor que 100.00
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
        }
        
        // verifica la linea
        JPublicInvServLineasSetV2 lin = new JPublicInvServLineasSetV2(request);
        lin.m_Where = "Clave = '" + p(request.getParameter("linea")) + "' and ID_InvServ = 'G'";
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
        
        HttpSession ses = request.getSession(true);
        JCatGastosSes pol = (JCatGastosSes)ses.getAttribute("cat_gastos_dlg");

        if(pol.getPartidas().size() < 1 || pol.getTotalDeduccion() != 100.0F)
        {
          idmensaje = 3; 
          mensaje = JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR2", 2); //"ERROR: El gasto no contiene cuentas contables a las que se direcionar&aacute;n o los porcentajes no suman el 100% exacto necesario. <br>";
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
      tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_GASTOS_PORCENTAJES (\n";
      tbl += "ID_CC char(19) NOT NULL ,\n";
      tbl += "Porcentaje numeric(9,6) NOT NULL \n";
      tbl += "); \n";
     
      /*
      JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
      set.Open();
      for(int i = 0; i< set.getNumRows(); i++)
      {
        tbl += "INSERT INTO _TMP_INVSERV_EXISTENCIAS \n";
        tbl += "VALUES('" + set.getAbsRow(i).getID_Bodega() + "','0.000','0.000','0.000'); \n";
      }*/
      JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
      set.Open();
      for(int i = 0; i< set.getNumRows(); i++)
      {
        tbl += "INSERT INTO _TMP_INVSERV_EXISTENCIAS \n";
        tbl += "VALUES('" + set.getAbsRow(i).getID_Bodega() + "','0.000','" + 
        		( request.getParameter("FSI_MAN_" + set.getAbsRow(i).getID_Bodega()) == null ? "0.000" : (request.getParameter("FSI_MAN_" + set.getAbsRow(i).getID_Bodega()).equals("1") ? "0.000" : "-1.000") ) + "','" + 
        		( request.getParameter("FSI_MAN_" + set.getAbsRow(i).getID_Bodega()) == null ? "0.000" : (request.getParameter("FSI_MAN_" + set.getAbsRow(i).getID_Bodega()).equals("1") ? "0.000" : "-1.000") ) + "'); \n";
      }
      
      HttpSession ses = request.getSession(true);
      JCatGastosSes pol = (JCatGastosSes)ses.getAttribute("cat_gastos_dlg");

      for(int i = 0; i < pol.getPartidas().size(); i++)
      {
         tbl += "INSERT INTO _TMP_INVSERV_GASTOS_PORCENTAJES\n";
         tbl += "VALUES( '" + p(JUtil.obtCuentas(pol.getPartida(i).getCuenta(),(byte)19)) + "','" + pol.getPartida(i).getPorcentaje() + "');\n";
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
      
      String str = "select * from  " + proc + " ('" + p(request.getParameter("clave")) + "','G',null,'" + p(request.getParameter("descripcion")) + "','" + p(request.getParameter("linea")) + "','0.00','0.000','','0.000','0.000','0','" + (request.getParameter("usointerno") != null ? "1" : "0") + "','" + p(request.getParameter("status")) + "','" + p(request.getParameter("obs")) + "','','1.000000','" +
      "0.000','0.000','0.000','0.000','0','0.0000','0.0000','" + p(request.getParameter("ieps")) + "','" + p(request.getParameter("ivaret")) + "','" + p(request.getParameter("isrret")) + "','" + p(request.getParameter("deduccioniva")) + "','" + 
      (request.getParameter("iva") != null ? "1" : "0") + "','0.000','1','0.000','0.00','" +
       "0.00','0.00','0.00','0.00','0.00','0.00','','','','','','','0','0','0','0.000','2','" + p(codigo) + "','0.00','" + p(request.getParameter("preciomax")) + "') as ( err integer, res varchar, clave varchar)";

      JRetFuncBas rfb = new JRetFuncBas();
  	
	  doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_GASTOS_PORCENTAJES; DROP TABLE _TMP_INVSERV_EXISTENCIAS; ", rfb);
    
      RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "INVSERV_GASTOS_" + proceso, "CATG|" + rfb.getClaveret() + "|||",rfb.getRes());
      irApag("/forsetiweb/catalogos/cat_gastos_dlg.jsp", request, response);


    }

}
