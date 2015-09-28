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
import javax.servlet.http.HttpSession;

import forseti.JForsetiApl;
import forseti.JRetFuncBas;
import forseti.JUtil;
import forseti.sets.*;
import forseti.compras.JCompPoliticasSes;


@SuppressWarnings("serial")
public class JCompPoliticasDlg extends JForsetiApl
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	//request.setAttribute("fsi_modulo",request.getRequestURI());
    	super.doPost(request,response);

    	String comp_pol_dlg = "";
    	request.setAttribute("comp_pol_dlg",comp_pol_dlg);
    	
    	String mensaje = ""; short idmensaje = -1;
    	
    	if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
    	{
    		if(request.getParameter("proceso").equals("PRECIOS_PROD"))
    		{
    			// Revisa si tiene permisos
    			if(!getSesion(request).getPermiso("COMP_POL_PRODUCTOS"))
    			{
    				idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "COMP_POL_PRODUCTOS");
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(), "COMP_POL_PRODUCTOS", "VPOL" + "||||",mensaje);
    				irApag("/forsetiweb/caja_mensajes.jsp", request, response);
    				return;
    			}
    	            	
    			if(getSesion(request).getSesion("COMP_POL").getEspecial().equals("PRODUCTOS"))
    			{
    				if(request.getParameter("subproceso") == null)
    				{
    					HttpSession ses = request.getSession(true);
    					JCompPoliticasSes rec = (JCompPoliticasSes)ses.getAttribute("comp_pol_dlg");
    					if(rec == null)
    					{
    						rec = new JCompPoliticasSes();
    						ses.setAttribute("comp_pol_dlg", rec);
    					}
    					else
    						rec.resetear();
    	            		                
    					// Llena la politica
    					JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
    					set.m_Where = getSesion(request).getSesion("COMP_POL").generarWhere();
    					set.m_OrderBy = getSesion(request).getSesion("COMP_POL").generarOrderBy();
    					set.Open();
    					
    					for(int i = 0; i< set.getNumRows(); i++)
    					{
    						rec.agregaObjeto(set.getAbsRow(i).getID_Tipo(), set.getAbsRow(i).getClave(), set.getAbsRow(i).getDescripcion(), set.getAbsRow(i).getLinea(), 
    								set.getAbsRow(i).getUnidad(), set.getAbsRow(i).getStatus(), set.getAbsRow(i).getPComp(), set.getAbsRow(i).getID_Moneda());

    					}
    	           	
    					getSesion(request).setID_Mensaje(idmensaje, mensaje);
    					irApag("/forsetiweb/compras/comp_pol_dlg.jsp", request, response);
    					return;
    				}
    				else
    				{

    					// Solicitud de envio a procesar
    					if(request.getParameter("subproceso").equals("ENVIAR"))
    					{
    						if(VerificarParametrosPreciosProd(request, response))
    						{
    							CambiarPreciosProd(request, response);
    							return;
    						}
    						irApag("/forsetiweb/compras/comp_pol_dlg.jsp", request, response);
    						return;
    					}
    				}
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
    	 
    	    
    public boolean VerificarParametrosPreciosProd(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
       // Ahora verifica las existenciastbl += "[PComp] [money] NOT NULL \n";
       JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
       set.m_Where = getSesion(request).getSesion("COMP_POL").generarWhere();
       set.m_OrderBy = getSesion(request).getSesion("COMP_POL").generarOrderBy();
       set.Open();       				
       String clave = "";
       boolean flag = true;
       for(int i = 0; i< set.getNumRows(); i++)
       {
            	clave = set.getAbsRow(i).getClave();
            	try 
            	{
            		float PComp = Float.parseFloat(request.getParameter("FSI_PCOMP_" + set.getAbsRow(i).getClave()));
            		byte ID_Moneda = Byte.parseByte(request.getParameter("FSI_MCOMP_" + set.getAbsRow(i).getClave()));
            		if( PComp < 0.0 || ID_Moneda < 1)
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
           idmensaje = 1;
           mensaje = "PRECAUCION: El precio del producto " + clave + " no está correcta ó es menor que cero. <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           return false;
       }
     
       return true;
    }

    public void CambiarPreciosProd(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String tbl;
        tbl =  "CREATE LOCAL TEMPORARY TABLE _TMP_INVSERV_PRECIOSCOMP (\n";
        tbl += "ID_Prod varchar(20) NOT NULL , \n";
        tbl += "PComp money NOT NULL , \n";
        tbl += "ID_Moneda smallint NOT NULL \n";
        tbl += ");\n";
        
        JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
    	set.m_Where = getSesion(request).getSesion("COMP_POL").generarWhere();
    	set.m_OrderBy = getSesion(request).getSesion("COMP_POL").generarOrderBy();
    	set.Open();
  
        for(int i = 0; i< set.getNumRows(); i++)
        {
            tbl += "INSERT INTO _TMP_INVSERV_PRECIOSCOMP \n";
            tbl += "VALUES('" + p(set.getAbsRow(i).getClave()) + "','" 
            		+ p(request.getParameter("FSI_PCOMP_" + p(set.getAbsRow(i).getClave()))) + "','"
            		+ p(request.getParameter("FSI_MCOMP_" + p(set.getAbsRow(i).getClave()))) + "'); \n";
        }
        
        String str = "select * from sp_invserv_cambio_precios_compra('', '0', '0.00') as (err integer, res varchar, clave varchar)";
    	//doDebugSQL(request,response,tbl + "<br>" + str);
    	JRetFuncBas rfb = new JRetFuncBas();
    			
    	doCallStoredProcedure(request, response, tbl, str, "DROP TABLE _TMP_INVSERV_PRECIOSCOMP", rfb);
    	   
    	RDP("CEF",getSesion(request).getConBD(),(rfb.getIdmensaje() == 0 ? "OK" : (rfb.getIdmensaje() == 4 ? "AL" : "ER")),getSesion(request).getID_Usuario(), "COMP_POL_PRODUCTOS", "CPOL|" + rfb.getClaveret() + "|||",rfb.getRes());
    	irApag("/forsetiweb/compras/comp_pol_dlg.jsp", request, response);
    }
    	      	     	
	/*
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      //request.setAttribute("fsi_modulo",request.getRequestURI());
      super.doPost(request,response);

      String comp_politicas_dlg = "";
      request.setAttribute("comp_politicas_dlg",comp_politicas_dlg);

      String mensaje = ""; short idmensaje = -1;
    
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
  
        if(request.getParameter("proceso").equals("PRECIOS"))
        {
            // Revisa si tiene permisos
            if(!getSesion(request).getPermiso("COMP_POLITICAS_CAMBIAR"))
            {
              idmensaje = 3; mensaje += "ERROR No tienes permiso para cambiar politicas de compra<br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiweb/caja_mensajes.jsp", request, response);
              return;
            }
            
            if(getSesion(request).getSesion("COMP_POL").getEspecial().equals("CANTIDADES"))
            {
            	if(request.getParameter("subproceso") == null)
            	{
            		HttpSession ses = request.getSession(true);
            		JCompPoliticasSes rec = (JCompPoliticasSes)ses.getAttribute("comp_politicas_dlg");
            		if(rec == null)
            		{
            			rec = new JCompPoliticasSes();
            			ses.setAttribute("comp_politicas_dlg", rec);
            		}
            		else
            		{
            			rec.resetear();
            		}
                
	                // Llena la politica
            		JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
            		set.m_Where = "ID_Tipo = 'P'  and SeProduce = '0' and Status = 'V'";
            		set.m_OrderBy = "Clave ASC"; 
            		set.Open();
            		
		            for(int i = 0; i< set.getNumRows(); i++)
		            {
		            	 rec.agregaObjeto(set.getAbsRow(i).getID_Tipo(), set.getAbsRow(i).getClave(), set.getAbsRow(i).getDescripcion(), set.getAbsRow(i).getLinea(), 
		            			 set.getAbsRow(i).getUnidad(), set.getAbsRow(i).getStatus(), set.getAbsRow(i).getPComp(), set.getAbsRow(i).getID_Moneda());
		            }
           	
	            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	                irApag("/forsetiweb/compras/comp_politicas_dlg.jsp", request, response);
	              
            	}
            	else
                {

      	       	  	// Solicitud de envio a procesar
      	       	  	if(request.getParameter("subproceso").equals("ENVIAR"))
      	       	  	{

             			  if(VerificarParametrosPrecios(request, response))
             				  CambiarPrecios(request, response);
            			  
      	       	  	}
      	       	 	   	  
      	      	}
            }
          
              
           
        }        	 
        else
        {
          idmensaje = 1;
          mensaje += "PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiweb/caja_mensajes.jsp", request, response);
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += "ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiweb/caja_mensajes.jsp", request, response);
      }

    }
 
    public boolean VerificarParametrosPrecios(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        short idmensaje = -1; String mensaje = "";
		
       // Ahora verifica las existenciastbl += "[PComp] [money] NOT NULL \n";
       JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
       set.m_Where = "ID_Tipo = 'P'  and SeProduce = '0' and Status = 'V'";
       set.m_OrderBy = "Clave ASC"; 
       set.Open();
       String clave = "";
       boolean flag = true;
       for(int i = 0; i< set.getNumRows(); i++)
       {
            	clave = set.getAbsRow(i).getClave();
            	try 
            	{
            		float PComp = Float.parseFloat(request.getParameter("FSI_PCOMP_" + set.getAbsRow(i).getClave()));
            		byte ID_Moneda = Byte.parseByte(request.getParameter("FSI_MCOMP_" + set.getAbsRow(i).getClave()));
            		if( PComp < 0.0 || ID_Moneda < 1)
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
           idmensaje = 1;
           mensaje = "PRECAUCION: El precio del producto " + clave + " no está correcta ó es menor que cero. <br>";
           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           irApag("/forsetiweb/compras/comp_politicas_dlg.jsp", request, response);
           return false;
       }
     
       return true;
       
	
    }

    public void CambiarPrecios(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	String tbl;
        tbl =  "CREATE TABLE [#TMP_INVSERV_PRECIOSCOMP] (\n";
        tbl += "[ID_Prod] [varchar] (20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL , \n";
        tbl += "[PComp] [money] NOT NULL , \n";
        tbl += "[ID_Moneda] [tinyint] NOT NULL \n";
        tbl += ") ON [PRIMARY]  \n";
        
        JInvServInvPreciosSet set = new JInvServInvPreciosSet(request);
        set.m_Where = "ID_Tipo = 'P' and SeProduce = '0' and Status = 'V'";
        set.m_OrderBy = "Clave ASC"; 
        set.Open();
  
        for(int i = 0; i< set.getNumRows(); i++)
        {
            tbl += "INSERT INTO #TMP_INVSERV_PRECIOSCOMP \n";
            tbl += "VALUES('" + p(set.getAbsRow(i).getClave()) + "','" 
            + p(request.getParameter("FSI_PCOMP_" + p(set.getAbsRow(i).getClave()))) + "','"
            + p(request.getParameter("FSI_MCOMP_" + p(set.getAbsRow(i).getClave()))) + ") \n";
        }
        
        String str = "EXEC  sp_invserv_cambio_precios_comp '', 0, $0.00 ";
               //doDebugSQL(request,response,tbl + "<br>" + str);
        
        try
        {
           short idmensaje = -1; String mensaje = "";
           Connection con = JAccesoBD.getConexionSes(request);
           Statement s    = con.createStatement();

           s.executeUpdate(tbl);
           ResultSet rs   = s.executeQuery(str);
           if(rs.next())
           {
             idmensaje = rs.getShort("ERR");
             mensaje = rs.getString("RES");
           }
           s.executeUpdate("DROP TABLE [#TMP_INVSERV_PRECIOSCOMP]");
           s.close();
           JAccesoBD.liberarConexion(con);

           getSesion(request).setID_Mensaje(idmensaje, mensaje);
           irApag("/forsetiweb/compras/comp_politicas_dlg.jsp", request, response);
        }
        catch(SQLException e)
        {
           e.printStackTrace();
           throw new RuntimeException(e.toString());
        }
        
    }
    */
}
