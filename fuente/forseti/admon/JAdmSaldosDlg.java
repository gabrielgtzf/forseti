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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.JAdmComprasEntidades;
import forseti.sets.JAdmPeriodosSet;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JAdmVentasEntidades;
import forseti.sets.JBancosCuentasSaldosSet;
import forseti.sets.JClientSaldosDetallesSet;
import forseti.sets.JClientSaldosSet;
import forseti.sets.JContaCatalogDetalleSet;
import forseti.sets.JInvServCostosDetallesSet;
import forseti.sets.JInvServInvExistenciasSaldosSet;
import forseti.sets.JProveeSaldosDetallesSet;
import forseti.sets.JProveeSaldosSet;

@SuppressWarnings("serial")
public class JAdmSaldosDlg extends JForsetiApl
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

      String adm_saldos_dlg = "";
      request.setAttribute("adm_saldos_dlg",adm_saldos_dlg);

      String mensaje = ""; short idmensaje = -1;

      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
    	  if(request.getParameter("proceso").equals("ACTUALIZAR_TODO"))
    	  {
    		  // Revisa si tiene permisos
    		  if(!getSesion(request).getPermiso("ADM_SALDOS_TODO"))
    		  {
    			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_SALDOS_TODO");
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_SALDOS_TODO","ASLD||||",mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
    		  }

    		  ActualizarTodo(request, response);
    		  return;
            
    		  
          }
    	  else if(request.getParameter("proceso").equals("ACTUALIZAR_SALDO"))
    	  {
    		  // Revisa si tiene permisos
    		  if(!getSesion(request).getPermiso("ADM_SALDOS_INDIV"))
    		  {
    			  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_SALDOS_INDIV");
    			  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			  RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_SALDOS_INDIV","ASLD||||",mensaje);
    			  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			  return;
    		  }

    		  // Solicitud de envio a procesar
    		  if(request.getParameter("id") != null)
    		  {
    			  String[] valoresParam = request.getParameterValues("id");
    			  if(valoresParam.length == 1)
    			  {
    				  if(request.getParameter("id").substring(0, 1).equals("S"))
    				  {
    					  ActualizarIndiv(request, response);
    					  return;
    				  }
    				  else
    				  {
    					  idmensaje = 1; mensaje += JUtil.Msj("CEF", "ADM_SALDOS", "DLG", "MSJ-PROCERR", 1);
    					  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    					  irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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
    	else if(request.getParameter("proceso").equals("INICIAR_SALDO"))
    	{
    		// Revisa si tiene permisos
    		if(!getSesion(request).getPermiso("ADM_SALDOS_INDIV"))
    		{
    			idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_SALDOS_INDIV");
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_SALDOS_INDIV","ASLD||||",mensaje);
    			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			return;
    		}	

    		JAdmVariablesSet setvar = new JAdmVariablesSet(request);
    		setvar.m_Where = "ID_Variable = 'INISLDS'";
    		setvar.Open();
    		if(setvar.getAbsRow(0).getVEntero() == 0)
    		{
    			idmensaje = 3; mensaje += JUtil.Msj("CEF","ADM_SALDOS","DLG","MSJ-PROCERR",3); //Error: No se permite el inicio de saldos ya que este proceso ya esta finalizado. Si deseas cambiar los saldos de inicio aun, cambia la variable INISLDS para poder generar este proceso
    			getSesion(request).setID_Mensaje(idmensaje, mensaje);
    			RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_SALDOS_INDIV","ASLD||||",mensaje);
    			irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    			return;
    		}
    		
    		// Solicitud de envio a procesar
    		if(request.getParameter("id") != null)
    		{
    			String[] valoresParam = request.getParameterValues("id");
    			if(valoresParam.length == 1)
    			{
    				if(request.getParameter("id").substring(0, 1).equals("I"))
    				{
    					if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
    					{
    		                // Verificacion
    						if(request.getParameter("id").substring(1, 2).equals("1"))
    	    				{
    							if(VerificarParametrosIniciosCont(request, response))
          	       	  			{
          	       	  				IniciosCont(request, response);
          	       	  				return;
          	       	  			}
    	    				}
    						else if(request.getParameter("id").substring(1, 2).equals("2"))
    	    				{
    							if(VerificarParametrosIniciosBancaj(request, response))
          	       	  			{
          	       	  				IniciosBancaj(request, response);
          	       	  				return;
          	       	  			}
    	    				}
    						else if(request.getParameter("id").substring(1, 2).equals("3"))
    	    				{
    							if(VerificarParametrosIniciosProvee(request, response))
          	       	  			{
          	       	  				IniciosProvee(request, response);
          	       	  				return;
          	       	  			}
    	    				}
    						else if(request.getParameter("id").substring(1, 2).equals("4"))
    	    				{
    							if(VerificarParametrosIniciosClient(request, response))
          	       	  			{
          	       	  				IniciosClient(request, response);
          	       	  				return;
          	       	  			}
    	    				}
    						else if(request.getParameter("id").substring(1, 2).equals("5"))
    	    				{
    							if(VerificarParametrosIniciosInvServ(request, response))
          	       	  			{
          	       	  				IniciosInvServ(request, response);
          	       	  				return;
          	       	  			}
    	    				}
    						irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
          	       	  		return;
    					}
    					else // Como el subproceso no es ENVIAR, abre la ventana del proceso de CAMBIADO para cargar el cambio
    					{
    						getSesion(request).setID_Mensaje(idmensaje, mensaje);
    		                irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    		                return;
    					}
  				  	}
  				  	else
  				  	{
  				  		idmensaje = 1; mensaje += JUtil.Msj("CEF", "ADM_SALDOS", "DLG", "MSJ-PROCERR", 2);
  				  		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  				  		irApag("/forsetiweb/caja_mensajes.jsp", request, response);
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

    public void ActualizarIndiv(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String str = "";
    	if(request.getParameter("id").equals("S1"))
    		str += "select * from sp_invserv_actualizar_existencias() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S2"))
    		str += "select * from sp_prod_formulas_actualizar_niveles() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S3")) //saldos almacen
    		str += "select * from sp_invserv_actualizar_sdos() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S4")) //polizas vs costos
    		str += "select * from sp_invserv_actualizar_polcant() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S5")) //saldos contables
    		str += "select * from sp_cont_catalogo_actualizar_sdos() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S6")) //caja y bancos
    		str += "select * from sp_bancos_actualizar_sdos() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S7")) //proveedores
    		str += "select * from sp_provee_actualizar_sdos() as ( err integer, res varchar ) ";
    	else if(request.getParameter("id").equals("S8")) //clientes
    		str += "select * from sp_client_actualizar_sdos() as ( err integer, res varchar ) ";
    
    	//doDebugSQL(request, response, str);
    	
    	JRetFuncBas rfb = new JRetFuncBas();
		
	    doCallStoredProcedureNOID(request, response, str, rfb);
	    RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
	    irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }
    
    public boolean VerificarParametrosIniciosCont(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
    	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
    	perIni.Open();
    	
    	JContaCatalogDetalleSet set = new JContaCatalogDetalleSet(request);
    	if(perIni.getNumRows() > 0)
    	{
    		set.m_Where = "Acum = '0' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    		set.m_OrderBy = "Cuenta ASC";
    	}
    	else
    	{
    		idmensaje = 1;
            mensaje = "PRECAUCION: Aun no existen periodos abiertos.<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
    	}
    	set.Open();
        
    	String clave = "";
		boolean flag = true;
		for(int i = 0; i< set.getNumRows(); i++)
		{
    	   clave = set.getAbsRow(i).getCuenta();
    	   try 
    	   {
    		   Float.parseFloat(request.getParameter(set.getAbsRow(i).getCuenta()));
    	   }
    	   catch(NumberFormatException e) 
    	   {
    		   flag = false;
    		   break;
    	   }
       }
       if(!flag)
       {
    	   idmensaje = 3;
           mensaje = "ERROR: El saldo inicial de una cuenta no está correcta." + clave + "<br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
       }
       return true;
    }
    
    public void IniciosCont(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       String tbl;
       tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_CONT (\n";
       tbl += "Cuenta character(19) NOT NULL , \n";
       tbl += "Saldo numeric(19, 4) NOT NULL \n";
       tbl += ");\n";
        	        
       JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
       perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
       perIni.Open();
   	
       JContaCatalogDetalleSet set = new JContaCatalogDetalleSet(request);
       set.m_Where = "Acum = '0' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
       set.m_OrderBy = "Cuenta ASC";
       set.Open();
        			
       for(int i = 0; i< set.getNumRows(); i++)
       {
    	   tbl += "INSERT INTO _TMP_INICIOS_CONT \n";
    	   tbl += "VALUES('" + p(set.getAbsRow(i).getCuenta()) + "','" + p(request.getParameter(set.getAbsRow(i).getCuenta())) + "'); \n";
       }
        	        
       String str = "select * from sp_cont_catalogo_iniciar_sdos('" + perIni.getAbsRow(0).getMes() + "','" + perIni.getAbsRow(0).getAno() + "') as ( err integer, res varchar, clave varchar ) ";
		      	    	
       JRetFuncBas rfb = new JRetFuncBas();
    			
       doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INICIOS_CONT", rfb);
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
       irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);

    }
    
    public boolean VerificarParametrosIniciosBancaj(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
    	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
    	perIni.Open();
    	
    	JBancosCuentasSaldosSet set = new JBancosCuentasSaldosSet(request);
    	if(perIni.getNumRows() > 0)
    	{
    		set.m_Where = "Mes = " + perIni.getAbsRow(0).getMes() + " and Ano = " + perIni.getAbsRow(0).getAno();
    		set.m_OrderBy = "Tipo ASC, Clave ASC";
    	}
    	else
    	{
    		idmensaje = 1;
            mensaje = "PRECAUCION: Aun no existen periodos abiertos.<br>";
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            return false;
    	}
    	set.Open();
        
    	boolean flag = true;
		for(int i = 0; i< set.getNumRows(); i++)
		{
    	   String clave = set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave();
    	   try 
    	   {
    		   float P1 = Float.parseFloat(request.getParameter(clave));
    		   float TC = (set.getAbsRow(i).getID_Moneda() != 1) ? 	Float.parseFloat(request.getParameter("TC_" + clave)) : 1.0000F;  
    		   if( P1 < 0.0 || TC <= 0.0 )
    		   {
    			   flag = false;
    			   break;
    		   }
    	   }
    	   catch(NumberFormatException e) 
    	   {
    		   flag = false;
    		   break;
    	   }
       }
       if(!flag)
       {
    	   idmensaje = 3;
           mensaje = "ERROR: El saldo inicial o tipo de cambio de algun banco o caja, no está correcta ó es menor que cero. <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
       }
       return true;
    }
    
    public void IniciosBancaj(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       String tbl;
       tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_BANCAJ (\n";
       tbl += "Tipo smallint NOT NULL , \n";
       tbl += "Clave smallint NOT NULL , \n";
       tbl += "Fija bit NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Saldo numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n";
        	        
       JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
       perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
       perIni.Open();
   	
       JBancosCuentasSaldosSet set = new JBancosCuentasSaldosSet(request);
       set.m_Where = "Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
       set.m_OrderBy = "Tipo ASC, Clave ASC";
       set.Open();
        			
       for(int i = 0; i< set.getNumRows(); i++)
       {
    	   tbl += "INSERT INTO _TMP_INICIOS_BANCAJ \n";
    	   tbl += "VALUES('" + set.getAbsRow(i).getTipo() + "','" + set.getAbsRow(i).getClave() + "','" + (set.getAbsRow(i).getFijo() ? "1" : "0") + "','" +
    			 ((set.getAbsRow(i).getID_Moneda() != 1) ? 	Float.parseFloat(request.getParameter("TC_" + set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave())) : "1.0000") +"','" +
    			   p(request.getParameter(set.getAbsRow(i).getTipo() + "_" + set.getAbsRow(i).getClave())) + "','" + p(set.getAbsRow(i).getCC()) + "'); \n";
       }
        	        
       String str = "select * from sp_bancos_iniciar_sdos('" + perIni.getAbsRow(0).getMes() + "','" + perIni.getAbsRow(0).getAno() + "') as ( err integer, res varchar, clave varchar ) ";
		      	    	
       JRetFuncBas rfb = new JRetFuncBas();
    			
       doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INICIOS_BANCAJ", rfb);
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
       irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);

    }    
    
    
    public void ActualizarTodo(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String str = "select * from sp_actualizar_sdos_todos() as ( err int, res varchar ) ";
 
    	JRetFuncBas rfb = new JRetFuncBas();
		
    	doCallStoredProcedureNOID(request, response, str, rfb);
		RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_TODO", "ASLD|TODO|||",rfb.getRes());
    	irApag("/forsetiweb/caja_mensajes.jsp", request, response);

    }

    public boolean VerificarParametrosIniciosInvServ(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
    	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
    	perIni.Open();
    	
    	JInvServCostosDetallesSet set = new JInvServCostosDetallesSet(request);
		if(perIni.getNumRows() > 0)
		{
			set.m_Where = "ID_Tipo = 'P' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			set.m_OrderBy = "ID_Prod ASC";
		}
		else
	    {
	    	idmensaje = 1;
	    	mensaje = "PRECAUCION: Aun no existen periodos abiertos.<br>";
	    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	return false;
	    }
		set.Open();
    	        
    	boolean flag = true;
    	String clave = "";
    	
		for(int i = 0; i < set.getNumRows(); i++)
		{
    	   clave = set.getAbsRow(i).getID_Prod();
    	   try 
    	   {
    		   float P1 = Float.parseFloat(request.getParameter(clave));
    		   if( P1 < 0.0 )
    		   {
    			   flag = false;
    			   break;
    		   }
    		   else
    		   {
    			   JInvServInvExistenciasSaldosSet bod = new JInvServInvExistenciasSaldosSet(request);
    			   bod.m_Where = "ID_Tipo = 'P' and ID_Prod = '" + p(set.getAbsRow(i).getID_Prod()) + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    			   bod.m_OrderBy = "ID_Bodega ASC";
    			   bod.Open();
    			   float ET = 0.0F;
    			   for(int j = 0; j < bod.getNumRows(); j++)
    			   {
    				   float E1 = Float.parseFloat(request.getParameter("BOD_" + bod.getAbsRow(j).getID_Bodega() + "_" + clave));
    	    		   if( E1 < 0.0 )
    	    		   {
    	    			   flag = false;
    	    			   break;
    	    		   }
    	    		   else
    	    			   ET += E1;
    			   }
    			   if( ET == 0.0 && P1 > 0.0 ) // Error de division entre cero
        		   {
        			   flag = false;
        			   break;
        		   }
    		   }
    	   }
    	   catch(NumberFormatException e) 
    	   {
    		   flag = false;
    		   break;
    	   }
       }
       if(!flag)
       {
    	   idmensaje = 3;
           mensaje = "ERROR: El saldo inicial o alguna existencia no está correcta ó es menor que cero, ó, no se puede dividir el saldo entre el total de existencias porque estas últimas suman cero: " + clave;
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
       }
       return true;
    }
    
    public void IniciosInvServ(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       String tbl;
       tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_INVSERV (\n";
       tbl += "ID_Prod varchar(20) NOT NULL , \n";
       tbl += "Existencia numeric(9, 3) NOT NULL , \n";
       tbl += "Costo numeric(19, 4) NOT NULL , \n";
       tbl += "Saldo numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n";
       
       tbl +=  "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_INVSERV_BODEGAS (\n";
       tbl += "ID_Prod varchar(20) NOT NULL , \n";
       tbl += "ID_Bodega smallint NOT NULL , \n";
       tbl += "Existencia numeric(9, 3) NOT NULL \n";
       tbl += ");\n"; 	        
       
       JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
       perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
       perIni.Open();
   	
       JInvServCostosDetallesSet set = new JInvServCostosDetallesSet(request);
       set.m_Where = "ID_Tipo = 'P' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
       set.m_OrderBy = "ID_Prod ASC";
       set.Open();
   	        
       for(int i = 0; i < set.getNumRows(); i++)
       {
    	   JInvServInvExistenciasSaldosSet bod = new JInvServInvExistenciasSaldosSet(request);
    	   bod.m_Where = "ID_Tipo = 'P' and ID_Prod = '" + p(set.getAbsRow(i).getID_Prod()) + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    	   bod.m_OrderBy = "ID_Bodega ASC";
    	   bod.Open();
    	   float existencia = 0.0F;
    	   for(int j = 0; j < bod.getNumRows(); j++)
    	   {
    		   float E1 = JUtil.redondear(Float.parseFloat(request.getParameter("BOD_" + bod.getAbsRow(j).getID_Bodega() + "_" + set.getAbsRow(i).getID_Prod())),3);
    		   existencia += E1;
        	   tbl += "INSERT INTO _TMP_INICIOS_INVSERV_BODEGAS \n";
        	   tbl += "VALUES('" + p(set.getAbsRow(i).getID_Prod()) + "','" + bod.getAbsRow(j).getID_Bodega() + "','" + E1 + "'); \n";
    	   }
    	   
    	   float saldo = JUtil.redondear(Float.parseFloat(request.getParameter(set.getAbsRow(i).getID_Prod())),4);
    	   float costo = (saldo == 0.0F) ? 0.0F : JUtil.redondear(saldo / existencia, 4);
    	   tbl += "INSERT INTO _TMP_INICIOS_INVSERV \n";
    	   tbl += "VALUES('" + p(set.getAbsRow(i).getID_Prod()) + "','" + existencia + "','" + costo + "','" + saldo + "','" + p(set.getAbsRow(i).getID_CC()) + "'); \n";
       }
        	        
       String str = "select * from sp_invserv_iniciar_sdos('" + perIni.getAbsRow(0).getMes() + "','" + perIni.getAbsRow(0).getAno() + "') as ( err integer, res varchar, clave varchar ) ";
		      	    	
       JRetFuncBas rfb = new JRetFuncBas();
    			
       //doDebugSQL(request, response, tbl + "<br><br>" + str);
       doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INICIOS_INVSERV; DROP TABLE _TMP_INICIOS_INVSERV_BODEGAS; ", rfb);
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
       irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);

    }    

    public boolean VerificarParametrosIniciosProvee(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
    	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
    	perIni.Open();
    	
    	if(perIni.getNumRows() <= 0)
		{
    		idmensaje = 1;
	    	mensaje = "PRECAUCION: Aun no existen periodos abiertos.<br>";
	    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	return false;
	    }
		
		JAdmComprasEntidades set = new JAdmComprasEntidades(request);
		set.m_Where = "ID_TipoEntidad = '0'";
		set.m_OrderBy = "ID_EntidadCompra ASC";
		set.Open();

		boolean flag = true;
    	int clave = 0;
    	
    	for(int i=0; i < set.getNumRows(); i++)
		{
    		if(!flag)
    			break;
    		
			JProveeSaldosSet sal = new JProveeSaldosSet(request);
			sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadCompra() + "' and ID_Tipo = 'PR' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			sal.m_OrderBy = "ID_Numero ASC";
			sal.Open();
				
			try
		   	{
		   		for(int j = 0; j < sal.getNumRows(); j++)
		   		{
		   			if(!flag)
		    			break;
		   			
		   			clave = sal.getAbsRow(j).getID_Numero();
		  		  
		   			JProveeSaldosDetallesSet det = new JProveeSaldosDetallesSet(request);
		   			det.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
		   			det.m_OrderBy = "ID_Moneda ASC";
		   			det.Open();
				
		   			for(int k = 0; k < det.getNumRows(); k++)
		   			{
						float S1 = Float.parseFloat(request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())); 
						float TC = (det.getAbsRow(k).getID_Moneda() != 1) ? 	Float.parseFloat(request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())) : 1.0000F;  
			    		if( S1 < 0.0 || TC <= 0.0 )
			    		{
			    			flag = false;
			    			break;
			    		}
			    		String CON = request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()); 
			    		if( S1 > 0.0 && CON.equals(""))
			    		{
			    			flag = false;
			    			break;
			    		}		
		   			}
		   		}
		   	}
		   	catch(NumberFormatException e) 
	    	{
		   		flag = false;
		   		break;
	    	}
		   	
		}
    	if(!flag)
		{
			idmensaje = 3;
			mensaje = "ERROR: El concepto, saldo inicial, o tipo de cambio de alguna cuenta no está correcta ó es menor que cero: " + clave;
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			return false;
		}

		return true;
    }
    
    public void IniciosProvee(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       String tbl;
       tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_PROVEE_CXP_AGREGAR (\n";
       tbl += "ID_CXP integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 	   
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_PROVEE_CXP_CAMBIAR (\n";
       tbl += "ID_CXP integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 	
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_PROVEE_CXP_ELIMINAR (\n";
       tbl += "ID_CXP integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_PROVEE_CXP_SC (\n";
       tbl += "ID_CXP integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 
       JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
       perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
       perIni.Open();
   	
       JAdmComprasEntidades set = new JAdmComprasEntidades(request);
       set.m_Where = "ID_TipoEntidad = '0'";
       set.m_OrderBy = "ID_EntidadCompra ASC";
       set.Open();
   	
       for(int i=0; i < set.getNumRows(); i++)
       {
    	   JProveeSaldosSet sal = new JProveeSaldosSet(request);
    	   sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadCompra() + "' and ID_Tipo = 'PR' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    	   sal.m_OrderBy = "ID_Numero ASC";
    	   sal.Open();

    	   for(int j = 0; j < sal.getNumRows(); j++)
    	   {
    		   JProveeSaldosDetallesSet det = new JProveeSaldosDetallesSet(request);
    		   det.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    		   det.m_OrderBy = "ID_Moneda ASC";
    		   det.Open();
    		   
    		   for(int k = 0; k < det.getNumRows(); k++)
    		   {
    			   float S1 = JUtil.redondear(Float.parseFloat(request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())),2); 
    			   float TC = (det.getAbsRow(k).getID_Moneda() != 1) ? JUtil.redondear(Float.parseFloat(request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())),4) : 1.0000F;  
    			   String CON = request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()); 
    			   
    			   if(det.getAbsRow(k).getID_CXP() == -1 && S1 > 0.0) // La cuenta es -1 quiere decir que se agregara la cuenta por pagar
    			   {
    				   tbl += "INSERT INTO _TMP_INICIOS_PROVEE_CXP_AGREGAR (ID_CXP,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    				   tbl += "VALUES('" + det.getAbsRow(k).getID_CXP() + "','" + set.getAbsRow(i).getID_EntidadCompra() + "','PR','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
    		   	   }
    			   else if(det.getAbsRow(k).getID_CXP() != -1 && S1 > 0.0) // Quiere decir que se cambiaraa la cuenta por pagar
    			   {
    				   if(S1 != det.getAbsRow(k).getSaldoFin() || TC != det.getAbsRow(k).getTC() || !CON.equals(det.getAbsRow(k).getDescripcion()))
    				   {
     					   if(det.getAbsRow(k).getID_Aplicacion() <= 0)
        				   {
    						   tbl += "INSERT INTO _TMP_INICIOS_PROVEE_CXP_CAMBIAR (ID_CXP,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXP() + "','" + set.getAbsRow(i).getID_EntidadCompra() + "','PR','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
        				   }
    					   else
    					   {
    						   getSesion(request).setID_Mensaje((short)3, "ERROR: No se puede cambiar una cuenta porque ya tiene asociados pagos o saldos: " + det.getAbsRow(k).getID_CXP() );
    						   irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    						   return;
    					   }
    				   }
    				   else
    				   {
    					   tbl += "INSERT INTO _TMP_INICIOS_PROVEE_CXP_SC (ID_CXP,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXP() + "','" + set.getAbsRow(i).getID_EntidadCompra() + "','PR','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
    				   }
    			   }
    			   else if(det.getAbsRow(k).getID_CXP() != -1 && S1 == 0.0) // Quiere decir que se eliminara la cuenta por pagar
    			   {
    				   if(S1 != det.getAbsRow(k).getSaldoFin())
    				   {
    					   if(det.getAbsRow(k).getID_Aplicacion() <= 0)
        				   {
    						   tbl += "INSERT INTO _TMP_INICIOS_PROVEE_CXP_ELIMINAR (ID_CXP,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXP() + "','" + set.getAbsRow(i).getID_EntidadCompra() + "','PR','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
        				   }
    					   else
    					   {
    						   getSesion(request).setID_Mensaje((short)3, "ERROR: No se puede eliminar una cuenta porque ya tiene asociados pagos o saldos: " + det.getAbsRow(k).getID_CXP() );
    						   irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    						   return;
    					   }
    				   }
    			   }
    		   }
    	   }
       }

       String str = "select * from sp_provee_iniciar_sdos('" + perIni.getAbsRow(0).getMes() + "','" + perIni.getAbsRow(0).getAno() + "') as ( err integer, res varchar, clave varchar ) ";
		      	    	
       JRetFuncBas rfb = new JRetFuncBas();
    			
       //doDebugSQL(request, response, tbl + "<br><br>" + str);
       doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INICIOS_PROVEE_CXP_AGREGAR; DROP TABLE _TMP_INICIOS_PROVEE_CXP_CAMBIAR; DROP TABLE _TMP_INICIOS_PROVEE_CXP_ELIMINAR;  DROP TABLE _TMP_INICIOS_PROVEE_CXP_SC;", rfb);
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
       irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    }    

    public boolean VerificarParametrosIniciosClient(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
        // Ahora verifica
        JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
    	perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
    	perIni.Open();
    	
    	if(perIni.getNumRows() <= 0)
		{
    		idmensaje = 1;
	    	mensaje = "PRECAUCION: Aun no existen periodos abiertos.<br>";
	    	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	    	return false;
	    }
		
		JAdmVentasEntidades set = new JAdmVentasEntidades(request);
		set.m_Where = "ID_TipoEntidad = '0'";
		set.m_OrderBy = "ID_EntidadVenta ASC";
		set.Open();

		boolean flag = true;
    	int clave = 0;
    	
    	for(int i=0; i < set.getNumRows(); i++)
		{
    		if(!flag)
    			break;
    		
			JClientSaldosSet sal = new JClientSaldosSet(request);
			sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadVenta() + "' and ID_Tipo = 'CL' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
			sal.m_OrderBy = "ID_Numero ASC";
			sal.Open();
 				
			try
		   	{
		   		for(int j = 0; j < sal.getNumRows(); j++)
		   		{
		   			if(!flag)
		    			break;
		   			
		   			clave = sal.getAbsRow(j).getID_Numero();
		  		  
		   			JClientSaldosDetallesSet det = new JClientSaldosDetallesSet(request);
		   			det.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
		   			det.m_OrderBy = "ID_Moneda ASC";
		   			det.Open();
				
		   			for(int k = 0; k < det.getNumRows(); k++)
		   			{
						float S1 = Float.parseFloat(request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())); 
						float TC = (det.getAbsRow(k).getID_Moneda() != 1) ? 	Float.parseFloat(request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())) : 1.0000F;  
			    		if( S1 < 0.0 || TC <= 0.0 )
			    		{
			    			flag = false;
			    			break;
			    		}
			    		String CON = request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()); 
			    		if( S1 > 0.0 && CON.equals(""))
			    		{
			    			flag = false;
			    			break;
			    		}		
		   			}
		   		}
		   	}
		   	catch(NumberFormatException e) 
	    	{
		   		flag = false;
		   		break;
	    	}
		   	
		}
    	if(!flag)
		{
			idmensaje = 3;
			mensaje = "ERROR: El concepto, saldo inicial, o tipo de cambio de alguna cuenta no está correcta ó es menor que cero: " + clave;
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			return false;
		}

		return true;
    }
    
    public void IniciosClient(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
       String tbl;
       tbl = "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_CLIENT_CXC_AGREGAR (\n";
       tbl += "ID_CXC integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 	   
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_CLIENT_CXC_CAMBIAR (\n";
       tbl += "ID_CXC integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 	
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_CLIENT_CXC_ELIMINAR (\n";
       tbl += "ID_CXC integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 
       tbl += "CREATE LOCAL TEMPORARY TABLE _TMP_INICIOS_CLIENT_CXC_SC (\n";
       tbl += "ID_CXC integer NOT NULL , \n";
       tbl += "ID_Entidad smallint NOT NULL , \n";
       tbl += "ID_Tipo char(2) NOT NULL , \n";
       tbl += "ID_Clave integer NOT NULL , \n";
       tbl += "Concepto varchar(80) NOT NULL , \n";
       tbl += "ID_Moneda smallint NOT NULL , \n";
       tbl += "TC numeric(19, 4) NOT NULL , \n";
       tbl += "Total numeric(19, 4) NOT NULL , \n";
       tbl += "TotalPesos numeric(19, 4) NOT NULL , \n";
       tbl += "CC char(19) NOT NULL \n";
       tbl += ");\n"; 
       JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
       perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
       perIni.Open();
   	
       JAdmVentasEntidades set = new JAdmVentasEntidades(request);
       set.m_Where = "ID_TipoEntidad = '0'";
       set.m_OrderBy = "ID_EntidadVenta ASC";
       set.Open();
   	
       for(int i=0; i < set.getNumRows(); i++)
       {
    	   JClientSaldosSet sal = new JClientSaldosSet(request);
    	   sal.m_Where = "ID_Entidad = '" + set.getAbsRow(i).getID_EntidadVenta() + "' and ID_Tipo = 'CL' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    	   sal.m_OrderBy = "ID_Numero ASC";
    	   sal.Open();

    	   for(int j = 0; j < sal.getNumRows(); j++)
    	   {
    		   JClientSaldosDetallesSet det = new JClientSaldosDetallesSet(request);
    		   det.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + sal.getAbsRow(j).getID_Clave() + "' and Mes = '" + perIni.getAbsRow(0).getMes() + "' and Ano = '" + perIni.getAbsRow(0).getAno() + "'";
    		   det.m_OrderBy = "ID_Moneda ASC";
    		   det.Open();
    		   
    		   for(int k = 0; k < det.getNumRows(); k++)
    		   {
    			   float S1 = JUtil.redondear(Float.parseFloat(request.getParameter("SAL_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())),2); 
    			   float TC = (det.getAbsRow(k).getID_Moneda() != 1) ? JUtil.redondear(Float.parseFloat(request.getParameter("TC_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave())),4) : 1.0000F;  
    			   String CON = request.getParameter("CON_" + det.getAbsRow(k).getID_Moneda() + "_" + sal.getAbsRow(j).getID_Clave()); 
    			   
    			   if(det.getAbsRow(k).getID_CXC() == -1 && S1 > 0.0) // La cuenta es -1 quiere decir que se agregara la cuenta por pagar
    			   {
    				   tbl += "INSERT INTO _TMP_INICIOS_CLIENT_CXC_AGREGAR (ID_CXC,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    				   tbl += "VALUES('" + det.getAbsRow(k).getID_CXC() + "','" + set.getAbsRow(i).getID_EntidadVenta() + "','CL','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
    		   	   }
    			   else if(det.getAbsRow(k).getID_CXC() != -1 && S1 > 0.0) // Quiere decir que se cambiaraa la cuenta por pagar
    			   {
    				   //System.out.print("S1: " + S1 + " SalFin: " + det.getAbsRow(k).getSaldoFin() + " TC: " + TC + " DetTC: " + det.getAbsRow(k).getTC() + " CON:<" + CON + "> DetCON:<" + det.getAbsRow(k).getDescripcion() + ">");
    				   if(S1 != det.getAbsRow(k).getSaldoFin() || TC != det.getAbsRow(k).getTC() || !CON.equals(det.getAbsRow(k).getDescripcion()))
    				   {
     					   if(det.getAbsRow(k).getID_Aplicacion() <= 0)
        				   {
    						   tbl += "INSERT INTO _TMP_INICIOS_CLIENT_CXC_CAMBIAR (ID_CXC,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXC() + "','" + set.getAbsRow(i).getID_EntidadVenta() + "','CL','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
        				   }
    					   else
    					   {
    						   getSesion(request).setID_Mensaje((short)3, "ERROR: No se puede cambiar una cuenta porque ya tiene asociados pagos o saldos: " + det.getAbsRow(k).getID_CXC() );
    						   irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    						   return;
    					   }
    				   }
    				   else
    				   {
    					   tbl += "INSERT INTO _TMP_INICIOS_CLIENT_CXC_SC (ID_CXC,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXC() + "','" + set.getAbsRow(i).getID_EntidadVenta() + "','CL','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
    				   }
    			   }
    			   else if(det.getAbsRow(k).getID_CXC() != -1 && S1 == 0.0) // Quiere decir que se eliminara la cuenta por pagar
    			   {
    				   if(S1 != det.getAbsRow(k).getSaldoFin())
    				   {
    					   if(det.getAbsRow(k).getID_Aplicacion() <= 0)
        				   {
    						   tbl += "INSERT INTO _TMP_INICIOS_CLIENT_CXC_ELIMINAR (ID_CXC,ID_Entidad,ID_Tipo,ID_Clave,Concepto,ID_Moneda,TC,Total,TotalPesos,CC) \n";
    						   tbl += "VALUES('" + det.getAbsRow(k).getID_CXC() + "','" + set.getAbsRow(i).getID_EntidadVenta() + "','CL','" + sal.getAbsRow(j).getID_Clave() + "','" + p(CON) + "','" + det.getAbsRow(k).getID_Moneda() + "','" + TC + "','" + S1 + "','" + JUtil.redondear(S1 * TC, 2) + "','" + p(sal.getAbsRow(j).getID_CC()) + "');\n"; 
        				   }
    					   else
    					   {
    						   getSesion(request).setID_Mensaje((short)3, "ERROR: No se puede eliminar una cuenta porque ya tiene asociados pagos o saldos: " + det.getAbsRow(k).getID_CXC() );
    						   irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    						   return;
    					   }
    				   }
    			   }
    		   }
    	   }
       }

       String str = "select * from sp_client_iniciar_sdos('" + perIni.getAbsRow(0).getMes() + "','" + perIni.getAbsRow(0).getAno() + "') as ( err integer, res varchar, clave varchar ) ";
		      	    	
       JRetFuncBas rfb = new JRetFuncBas();
    			
       //doDebugSQL(request, response, tbl + "<br><br>" + str);
       doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INICIOS_CLIENT_CXC_AGREGAR; DROP TABLE _TMP_INICIOS_CLIENT_CXC_CAMBIAR; DROP TABLE _TMP_INICIOS_CLIENT_CXC_ELIMINAR; DROP TABLE _TMP_INICIOS_CLIENT_CXC_SC;", rfb);
       RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "ADM_SALDOS_INDIV", "ASLD|" + request.getParameter("id") + "|||",rfb.getRes());
       irApag("/forsetiweb/administracion/adm_saldos_dlg.jsp", request, response);
    }    

}
