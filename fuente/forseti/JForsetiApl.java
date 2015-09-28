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
package forseti;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forseti.sets.JAdmBancosCuentasSet;
import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JClientClientSetV2;
import forseti.sets.JMasempSet;
import forseti.sets.JNominaEntidadesSetIds;
import forseti.sets.JPublicBancosCuentasVsComprasSetV2;
import forseti.sets.JPublicBancosCuentasVsVentasSetV2;
import forseti.sets.JPublicFormatosSetV2;
import forseti.sets.JPublicCXPConeSetV2;
import forseti.sets.JPublicCXCConeSetV2;
import forseti.sets.JSatBancosSet;
import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JVentasEntidadesSetIdsV2;

@SuppressWarnings("serial")
public abstract class JForsetiApl extends HttpServlet
{
	protected static final short AGREGAR = 1;
	protected static final short CAMBIAR = 2;
	protected static final short ELIMINAR = -1;
	
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
      request.setAttribute("fsi_modulo", request.getRequestURI());
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
      if(princ == null)
      {
    	  if(request.getParameter("mobile") != null)
    		  princ = new JSesionPrincipal(true);
    	  else
    		  princ = new JSesionPrincipal(false);
       
    	  ses.setAttribute("forseti", princ);
      }
      // si ya tenia inicio de sesion, verifica que este registrado
      if(!princ.getRegistrado())
        irApag("/forsetiweb/registro.jsp",request,response);

    }

    public void doDebugSQL(HttpServletRequest request, HttpServletResponse response, String Salida)
    	throws ServletException, IOException
	{
    	JUtil.doDebugSQL(request, response, Salida);
	}
    
    protected void verificaSesion(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
      if(princ == null)
      {
    	  if(request.getParameter("mobile") != null)
    		  princ = new JSesionPrincipal(true);
    	  else
    		  princ = new JSesionPrincipal(false);
        
    	  ses.setAttribute("forseti", princ);
    	  //System.out.println("Intervalo máximo: "+ ses.getMaxInactiveInterval());
      }

    }

    protected void irApag(String pagina, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	HttpSession ses = request.getSession(true);
        JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
        String destino;
    	if(princ.isMobile())
    		destino = JUtil.preparaPaginaPM(pagina);
    	else
    		destino = pagina;
    	
    	System.out.println(destino);
    	
    	RequestDispatcher despachador = getServletContext().getRequestDispatcher(destino);
    	despachador.forward(request,response);
    }

    protected String getMensaje(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");

      return princ.getMensaje();
    }

    protected JSesionPrincipal getSesion(HttpServletRequest request)
        throws ServletException, IOException
    {
      HttpSession ses = request.getSession(true);
      return (JSesionPrincipal)ses.getAttribute("forseti");
    }

    protected String pt(String str)
    {
      return JUtil.pt(str);
    }

    protected String p(String str)
    {
      return JUtil.p(str);
    }

    protected String p2(String str, String dt, boolean nullper, String def)
    {
      return JUtil.p2(str, dt, nullper, def);
    }
    
    protected String w(String str)
    {
      return JUtil.w(str);
    }

    
    protected String q(String str)
    {
      return JUtil.q(str);
    }
    
    protected short Imprimir(String SQLCab, String SQLDet, String ID_Formato, StringBuffer Mensaje,
                                                 HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException
    {
      JPublicFormatosSetV2 set = new JPublicFormatosSetV2(request);
      set.m_Where = "ID_Formato = '" + p(ID_Formato) + "'";
      set.Open();

      if(set.getNumRows() < 1)
      {
        Mensaje.append("ERROR: Este formato de impresión no existe.");
        return -3;
      }

      request.setAttribute("sqlcab", SQLCab);
      request.setAttribute("sqldet", SQLDet);
      request.setAttribute("ID_Formato", ID_Formato);

      irApag("/forsetiweb/impresion.jsp", request, response);

      return -1;
    }
    
    protected boolean VerificarSaldo(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	String tipo = (String) request.getAttribute("fsipg_tipo");
    	
    	if(request.getParameter("fsipg_idscon") != null && !request.getParameter("fsipg_idscon").equals("") &&
    			request.getParameter("fsipg_concepto") != null && !request.getParameter("fsipg_concepto").equals(""))
    	{
    		int idconcepto = Integer.parseInt(request.getParameter("fsipg_idscon").substring(4));
    		String concepto = request.getParameter("fsipg_concepto");
    		
    		if(tipo.equals("compras"))
    		{
    			JPublicCXPConeSetV2 cxp = new JPublicCXPConeSetV2(request);
    		    cxp.m_Where = "Tipo = 'SAL' and ID_Concepto = '" + idconcepto + "'"; 
    			cxp.Open();
    			
    			if (cxp.getNumRows() < 1)
    			{
    				short idmensaje = 3;
    				String mensaje = "NS ERROR: La clave del concepto para el saldo -CXP " + idconcepto + "- no esta definida en Conceptos de cuentas por pagar.<br>";
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				return false;
    			}
    			
    		}
    		else
    		{
				// es del tipo ventas
    			JPublicCXCConeSetV2 cxc = new JPublicCXCConeSetV2(request);
    		    cxc.m_Where = "Tipo = 'SAL' and ID_Concepto = '" + idconcepto + "'"; 
    			cxc.Open();
    			
    			if (cxc.getNumRows() < 1)
    			{
    				short idmensaje = 3;
    				String mensaje = "NS ERROR: La clave del concepto para el saldo -CXC " + idconcepto + "- no esta definida en Conceptos de cuentas por cobrar.<br>";
    				getSesion(request).setID_Mensaje(idmensaje, mensaje);
    				return false;
    			}

			
			}
			
			// registra los atributos de este pago, para ser usados por el proceso que lo llamó
			request.setAttribute("fsipg_id_concepto", idconcepto);
			request.setAttribute("fsipg_desc_concepto", concepto);
			
			return true;
			
		}
		else
		{
			short idmensaje = 3;
			String mensaje = "NS ERROR: La clave o concepto del saldo no se recibió. Esta se establece al Aceptar el nuevo registro. Si no se mostró la ventana de saldos, es posible que tu navegador haya fallado, verifica con tu administrador del equipo para que configure JavaScript en tu equipo y vuelve a intentarlo<br>";
			getSesion(request).setID_Mensaje(idmensaje, mensaje);
			return false;
		}
	
	}
    
    
    protected boolean VerificarPago(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException
    {
       String tipo = (String) request.getAttribute("fsipg_tipo");
       String proc = (String) request.getAttribute("fsipg_proc");
       String ident = (String) request.getAttribute("fsipg_ident");

       if(request.getParameter("fsipg_bancaj") != null && !request.getParameter("fsipg_bancaj").equals("") )
       {
           String fsipg_forma, fsipg_id_bancaj, fsipg_ref, fsipg_tipomov, fsipg_beneficiario, fsipg_rfc, fsipg_metpagopol, fsipg_depchq, fsipg_cuentabanco, fsipg_id_satbanco, fsipg_bancoext;
           fsipg_id_bancaj = request.getParameter("fsipg_bancaj").substring(8);
           String bancaj = request.getParameter("fsipg_bancaj").substring(4,7);
           int entidad = Integer.parseInt(request.getParameter("fsipg_bancaj").substring(8));
           fsipg_ref = request.getParameter("fsipg_ref");
           fsipg_tipomov = "";
           fsipg_beneficiario = request.getParameter("fsipg_beneficiario");
           fsipg_rfc = request.getParameter("fsipg_rfc");
           fsipg_metpagopol = request.getParameter("fsipg_metpagopol");
           fsipg_depchq = request.getParameter("fsipg_depchq");
           fsipg_cuentabanco = request.getParameter("fsipg_cuentabanco");
           fsipg_id_satbanco = request.getParameter("fsipg_id_satbanco");
           fsipg_bancoext = request.getParameter("fsipg_bancoext");
           
           
           
           if(tipo.equals("compras"))
           {
        	   //System.out.println("compras");
        	   JPublicBancosCuentasVsComprasSetV2 bc = new JPublicBancosCuentasVsComprasSetV2(request);
        	   bc.m_OrderBy = "Clave ASC";
        	   if (bancaj.equals("BAN"))
        		   bc.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + p(ident) + "' and Clave = '" + entidad + "'";
        	   else
	               bc.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + p(ident) + "' and Clave = '" + entidad + "'";
	
        	   bc.Open();
        	   if (bc.getNumRows() < 1)
        	   {
        		   short idmensaje = 3;
	               String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR",1) + " -" + bancaj + " " + entidad + "-"; //"ERROR: La entidad de pago -" + bancaj + " " + entidad + "- no esta enlazada en Compras Vs Bancos.<br>";
	               getSesion(request).setID_Mensaje(idmensaje, mensaje);
	               return false;
        	   }
           }
           else
           {
        	   // es del tipo ventas
               JPublicBancosCuentasVsVentasSetV2 bv = new JPublicBancosCuentasVsVentasSetV2(request);
               bv.m_OrderBy = "Clave ASC";
               if (bancaj.equals("BAN"))
                 bv.m_Where = "Tipo = '0' and ID_EntidadVenta = '" + p(ident) + "' and Clave = '" + entidad + "'";
               else
                 bv.m_Where = "Tipo = '1' and ID_EntidadVenta = '" + p(ident) + "' and Clave = '" + entidad + "'";

               bv.Open();
               if (bv.getNumRows() < 1)
               {
                 short idmensaje = 3;
                 String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR",2) + " -" + bancaj + " " + entidad + "-"; 
                 getSesion(request).setID_Mensaje(idmensaje, mensaje);
                 return false;
               }
           }
           
           if (bancaj.equals("CAJ"))
        	   fsipg_forma = "1";
           else
        	   fsipg_forma = "0";
           
           JSatBancosSet setBan = new JSatBancosSet(request);
           setBan.m_Where = "Clave = '" + p(fsipg_id_satbanco) + "'";
           setBan.Open();
   		
           if(setBan.getNumRows() < 1)
           {
        	   short idmensaje = 3;
               String mensaje = "ERROR: El banco para el SAT no es válido<br>";
        	   getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	   return false;
           }
           else 
           {
        	   if(!setBan.getAbsRow(0).getClave().equals("000") && !fsipg_bancoext.equals(""))
        	   {
        		   short idmensaje = 1;
                   String mensaje = "PRECAUCION: No se debe establecer el banco extranjero porque ya se ha seleccionado un banco nacional<br>";
        		   getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		   return false;
        	   }
           }
           if(!fsipg_rfc.equals(""))
           {
        	   String rfcfmt = JUtil.fco(JUtil.frfc(fsipg_rfc));
        	   if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
        	   {
        		   short idmensaje = 1;
                   String mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
        		   getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		   return false;
        	   }
           }
           if(fsipg_metpagopol.equals("02") /*cheque*/ && (fsipg_beneficiario.equals("") || fsipg_depchq.equals("")) )
           {
        	   short idmensaje = 3;
               String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 3); //"ERROR: Debes proporcionar el número y beneficiario para el cheque <br>");
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               return false;
           }
           if(fsipg_metpagopol.equals("03") /*transferencia*/ && fsipg_forma.equals("1"))
           {
        	   short idmensaje = 1;
               String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 1); //PRECAUCION: No se puede agregar un depósito o retiro de caja por un método de transferencia.
               getSesion(request).setID_Mensaje(idmensaje, mensaje);
               return false;
           }
           JAdmBancosCuentasSet bcSet = new JAdmBancosCuentasSet(request);
           bcSet.m_Where = "Tipo = '" + fsipg_forma + "' and Clave = '" + entidad + "'";
           bcSet.Open();
           if(!bcSet.getAbsRow(0).getFijo()) //Banco o Caja contable
           {
        	   if(fsipg_beneficiario.equals("") || fsipg_rfc.equals(""))
               {
        		   short idmensaje = 3;
                   String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 5); //"ERROR: Debes proporcionar el beneficiario y RFC para soporte de contabilidad electrónica, si es un depósito, nuestra empresa como beneficiario y el RFC del que paga, y si es un retiro, el beneficiario y RFC, ambos del que recibe. 
        		   getSesion(request).setID_Mensaje(idmensaje, mensaje);
        		   return false;
               }
           
        	   if(fsipg_forma.equals("0")) //Pago con banco
        	   {
        		   if((fsipg_metpagopol.equals("03") || (proc.equals("deposito") && fsipg_metpagopol.equals("02"))) && (fsipg_cuentabanco.equals("") || (fsipg_id_satbanco.equals("000") && fsipg_bancoext.equals(""))))
                   {
        			   short idmensaje = 3;
                       String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 2); //"ERROR: Debes proporcionar cuenta y banco para soporte de contabilidad electrónica, si es un depósito, cuenta y banco del cheque u origen de la transferencia, si es retiro, cuenta y banco de destino de la transferencia.
                       getSesion(request).setID_Mensaje(idmensaje, mensaje);
                       return false;
                   }
        	   }
        	   else //Pago con caja
        	   {
        		   if(fsipg_metpagopol.equals("02") && (fsipg_cuentabanco.equals("") || (fsipg_id_satbanco.equals("000") && fsipg_bancoext.equals(""))))
                   {
        			   short idmensaje = 3;
                       String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 2); //ERROR: Debes proporcionar numero, cuenta y banco del cheque para soporte de contabilidad electrónica
        			   getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
        			   return false;
                   }
        	   }
           }
                      	
           if(proc.equals("retiro") && fsipg_metpagopol.equals("02")/*cheque*/)
        	   fsipg_tipomov = "CHQ";
           else if(proc.equals("deposito"))
           {
        	   if(!fsipg_metpagopol.equals("02")/*cheque*/)
        		   fsipg_tipomov = "DEP";
        	   else
        		   fsipg_tipomov = "DCH";
           } 
           else
        	   fsipg_tipomov = "RET";
           
           // registra los atributos de este pago, para ser usados por el proceso que lo llamó
           request.setAttribute("fsipg_forma", fsipg_forma);
           request.setAttribute("fsipg_id_bancaj", fsipg_id_bancaj);
           request.setAttribute("fsipg_ref", fsipg_ref);
           request.setAttribute("fsipg_tipomov", fsipg_tipomov);
           request.setAttribute("fsipg_beneficiario", fsipg_beneficiario);
           request.setAttribute("fsipg_rfc", fsipg_rfc);
           request.setAttribute("fsipg_metpagopol", fsipg_metpagopol);
           request.setAttribute("fsipg_depchq", fsipg_depchq);
           request.setAttribute("fsipg_cuentabanco", fsipg_cuentabanco);
           request.setAttribute("fsipg_id_satbanco", fsipg_id_satbanco);
           request.setAttribute("fsipg_bancoext", fsipg_bancoext);
           
           return true;

       }
       else
       {
          short idmensaje = 3;
          String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR",4); //"ERROR: La entidad de pago no se recibió. Esta se establece al Aceptar el nuevo registro. Si no se mostró la ventana de pagos, es posible que tu navegador haya fallado, verifica con tu administrador del equipo para que configure JavaScript en tu equipo y vuelve a intentarlo<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
       }

    }


    protected boolean VerificarPagoMult(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
    	String tipo = (String) request.getAttribute("fsipg_tipo");
    	String proc = (String) request.getAttribute("fsipg_proc");
		String ident = (String) request.getAttribute("fsipg_ident");
		float total_pago = (Float)request.getAttribute("fsipg_total");
		
		boolean entidadescaja = true;
		boolean entidadesbanco = true;
		Vector<JForsetiAplPagos> pagoMult = new Vector<JForsetiAplPagos>();
		
		String fsipg_beneficiario = request.getParameter("fsipg_beneficiario");
        String fsipg_rfc = request.getParameter("fsipg_rfc");
        
		///////////////////////////////////////////////////////////////////////////////////////////////
		// Agrega las entidades a temporal para revisar posteriormente
		if(tipo.equals("compras"))
		{
			JPublicBancosCuentasVsComprasSetV2 bc = new JPublicBancosCuentasVsComprasSetV2(request);
			JPublicBancosCuentasVsComprasSetV2 cc = new JPublicBancosCuentasVsComprasSetV2(request);
			
			bc.m_OrderBy = "Clave ASC";
			bc.m_Where = "Tipo = '0' and ID_EntidadCompra = '" + p(ident) + "'";
			bc.Open();
			cc.m_OrderBy = "Clave ASC";
			cc.m_Where = "Tipo = '1' and ID_EntidadCompra = '" + p(ident) + "'";
			cc.Open();
			
			if(cc.getNumRows() > 0)
			{
				for(int i = 0; i< cc.getNumRows(); i++)
				{ 
					if(!request.getParameter("FSI_CAJ_" +  cc.getAbsRow(i).getClave()).equals(""))
					{
						//Esta entidad tiene pago realizado..... Agrega a temporal
						JForsetiAplPagos pagos = new JForsetiAplPagos();
						pagos.ID_FormaPago = 1; //Es de caja
						pagos.ID_BanCaj = cc.getAbsRow(i).getClave();
						pagos.RefPago = request.getParameter("FSI_CAJ_REF_" +  cc.getAbsRow(i).getClave());
						pagos.TipoMov = "";
						pagos.Total = Float.parseFloat(request.getParameter("FSI_CAJ_" +  cc.getAbsRow(i).getClave()));
						pagos.Efectivo = Float.parseFloat(request.getParameter("FSI_CAJ_EFECTIVO_" +  cc.getAbsRow(i).getClave()));
						pagos.ID_SatMetodosPago = request.getParameter("FSI_CAJ_METPAGOPOL_" +  cc.getAbsRow(i).getClave());
						pagos.ID_SatBanco = request.getParameter("FSI_CAJ_ID_SATBANCO_" +  cc.getAbsRow(i).getClave());
						pagos.CuentaBanco = request.getParameter("FSI_CAJ_CUENTABANCO_" +  cc.getAbsRow(i).getClave());
						pagos.BancoExt = request.getParameter("FSI_CAJ_BANCOEXT_" +  cc.getAbsRow(i).getClave());
						pagos.Cheque = request.getParameter("FSI_CAJ_DEPCHQ_" +  cc.getAbsRow(i).getClave());
						pagoMult.add(pagos);
					}
				}
			}	
			else
			{
				entidadescaja = false;
			}
			
			
			if(bc.getNumRows() > 0)
			{
		        for(int i = 0; i< bc.getNumRows(); i++)
		        { 
		        	if(!request.getParameter("FSI_BAN_" +  bc.getAbsRow(i).getClave()).equals(""))
					{
		        		//Esta entidad tiene pago realizado..... Agrega a temporal
						JForsetiAplPagos pagos = new JForsetiAplPagos();
						pagos.ID_FormaPago = 0; //Es de banco
						pagos.ID_BanCaj = bc.getAbsRow(i).getClave();
						pagos.RefPago = request.getParameter("FSI_BAN_REF_" +  bc.getAbsRow(i).getClave());
						pagos.TipoMov = "";
						pagos.Total = Float.parseFloat(request.getParameter("FSI_BAN_" +  bc.getAbsRow(i).getClave()));
						pagos.Efectivo = 0.0F;
						pagos.ID_SatMetodosPago = request.getParameter("FSI_BAN_METPAGOPOL_" +  bc.getAbsRow(i).getClave());
						pagos.ID_SatBanco = request.getParameter("FSI_BAN_ID_SATBANCO_" +  bc.getAbsRow(i).getClave());
						pagos.CuentaBanco = request.getParameter("FSI_BAN_CUENTABANCO_" +  bc.getAbsRow(i).getClave());
						pagos.BancoExt = request.getParameter("FSI_BAN_BANCOEXT_" +  bc.getAbsRow(i).getClave());
						pagos.Cheque = request.getParameter("FSI_BAN_DEPCHQ_" +  bc.getAbsRow(i).getClave());
						pagoMult.add(pagos);
					}
			    }
			   
			}
			else
			{
				entidadesbanco = false;
			}
			
		}
		else // es ventas
		{
			JPublicBancosCuentasVsVentasSetV2 bv = new JPublicBancosCuentasVsVentasSetV2(request);
			JPublicBancosCuentasVsVentasSetV2 cv = new JPublicBancosCuentasVsVentasSetV2(request);
			bv.m_OrderBy = "Clave ASC";
			bv.m_Where = "Tipo = '0' and ID_EntidadVenta = '" + p(ident) + "'";
			bv.Open();
			cv.m_OrderBy = "Clave ASC";
			cv.m_Where = "Tipo = '1' and ID_EntidadVenta = '" + p(ident) + "'";
			cv.Open();
							
		    if(cv.getNumRows() > 0)
			{
				for(int i = 0; i< cv.getNumRows(); i++)
				{ 
					if(!request.getParameter("FSI_CAJ_" +  cv.getAbsRow(i).getClave()).equals(""))
					{
						//Esta entidad tiene pago realizado..... Agrega a temporal
						JForsetiAplPagos pagos = new JForsetiAplPagos();
						pagos.ID_FormaPago = 1; //Es de caja
						pagos.ID_BanCaj = cv.getAbsRow(i).getClave();
						pagos.RefPago = request.getParameter("FSI_CAJ_REF_" +  cv.getAbsRow(i).getClave());
						pagos.TipoMov = "";
						pagos.Total = Float.parseFloat(request.getParameter("FSI_CAJ_" +  cv.getAbsRow(i).getClave()));
						pagos.Efectivo = Float.parseFloat(request.getParameter("FSI_CAJ_EFECTIVO_" +  cv.getAbsRow(i).getClave()));
						pagos.ID_SatMetodosPago = request.getParameter("FSI_CAJ_METPAGOPOL_" +  cv.getAbsRow(i).getClave());
						pagos.ID_SatBanco = request.getParameter("FSI_CAJ_ID_SATBANCO_" +  cv.getAbsRow(i).getClave());
						pagos.CuentaBanco = request.getParameter("FSI_CAJ_CUENTABANCO_" +  cv.getAbsRow(i).getClave());
						pagos.BancoExt = request.getParameter("FSI_CAJ_BANCOEXT_" +  cv.getAbsRow(i).getClave());
						pagos.Cheque = request.getParameter("FSI_CAJ_DEPCHQ_" +  cv.getAbsRow(i).getClave());
						pagoMult.add(pagos);					
					}
				}
			}
			else
			{
				entidadescaja = false;
			}
		    
			if(bv.getNumRows() > 0)
			{
				for(int i = 0; i< bv.getNumRows(); i++)
				{ 
					if(!request.getParameter("FSI_BAN_" +  bv.getAbsRow(i).getClave()).equals(""))
					{
						//Esta entidad tiene pago realizado..... Agrega a temporal
						JForsetiAplPagos pagos = new JForsetiAplPagos();
						pagos.ID_FormaPago = 0; //Es de banco
						pagos.ID_BanCaj = bv.getAbsRow(i).getClave();
						pagos.RefPago = request.getParameter("FSI_BAN_REF_" +  bv.getAbsRow(i).getClave());
						pagos.TipoMov = "";
						pagos.Total = Float.parseFloat(request.getParameter("FSI_BAN_" +  bv.getAbsRow(i).getClave()));
						pagos.Efectivo = 0.0F;
						pagos.ID_SatMetodosPago = request.getParameter("FSI_BAN_METPAGOPOL_" +  bv.getAbsRow(i).getClave());
						pagos.ID_SatBanco = request.getParameter("FSI_BAN_ID_SATBANCO_" +  bv.getAbsRow(i).getClave());
						pagos.CuentaBanco = request.getParameter("FSI_BAN_CUENTABANCO_" +  bv.getAbsRow(i).getClave());
						pagos.BancoExt = request.getParameter("FSI_BAN_BANCOEXT_" +  bv.getAbsRow(i).getClave());
						pagos.Cheque = request.getParameter("FSI_BAN_DEPCHQ_" +  bv.getAbsRow(i).getClave());
						pagoMult.add(pagos);
					}
				}
			}
			else
			{
				entidadesbanco = false;
			}
				
		}
		///////////////////////////////////////////////////////////////////////////////////////////////
		String tmp_pagos;
		tmp_pagos = "CREATE LOCAL TEMPORARY TABLE _TMP_PAGOS (\n";
		tmp_pagos += "Partida serial NOT NULL ,\n";
		tmp_pagos += "ID_FormaPago smallint NOT NULL ,\n";
		tmp_pagos += "ID_BanCaj smallint NOT NULL ,\n";
		tmp_pagos += "Total numeric(19,4) NOT NULL ,\n";
		tmp_pagos += "RefPago varchar(20) NOT NULL ,\n";
		tmp_pagos += "TipoMov character(3) NOT NULL ,\n";
		tmp_pagos += "ID_SatBanco character(3) NOT NULL,\n";
		tmp_pagos += "ID_SatMetodosPago character(2) NOT NULL,\n";
		tmp_pagos += "BancoExt character varying(150) NOT NULL,\n";
		tmp_pagos += "CuentaBanco character varying(50) NOT NULL,\n";
		tmp_pagos += "Cheque character varying(20) NOT NULL\n";
		tmp_pagos += "); \n\n";
		
		float gran_total = 0.0F;
		float cajas_total = 0.0F;
		float cajas_efec = 0.0F;
		float bancos_total = 0.0F;
		
		//Comienza el bucle que contiene los pagos
		for(int i = 0; i < pagoMult.size(); i++)
		{
			JForsetiAplPagos pago = pagoMult.elementAt(i);
			
			gran_total += pago.Total;
			if(pago.ID_FormaPago == 1)
			{
				cajas_total += pago.Total;
				cajas_efec += pago.Efectivo;
			}
			else
				bancos_total += pago.Total;
			
			////////////////////////////////////////////////////////////////////////////////////
			// Hace la revision larga por cada movimiento de pago
			JSatBancosSet setBan = new JSatBancosSet(request);
	        setBan.m_Where = "Clave = '" + p(pago.ID_SatBanco) + "'";
	        setBan.Open();
	   		
	        if(setBan.getNumRows() < 1)
	        {
	        	short idmensaje = 3;
	        	String mensaje = "ERROR: El banco para el SAT no es válido<br>";
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	return false;
	        }
	        else 
	        {
	        	if(!setBan.getAbsRow(0).getClave().equals("000") && !pago.BancoExt.equals(""))
	        	{
	        		short idmensaje = 1;
	        		String mensaje = "PRECAUCION: No se debe establecer el banco extranjero porque ya se ha seleccionado un banco nacional<br>";
	        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        		return false;
	        	}
	        }
	        if(!fsipg_rfc.equals(""))
	        {
	        	String rfcfmt = JUtil.fco(JUtil.frfc(fsipg_rfc));
	        	if(rfcfmt.equals("") || rfcfmt.length() > 13 || rfcfmt.length() < 12)
	        	{
	        		short idmensaje = 1;
	        		String mensaje = "PRECAUCION: El RFC esta mal, puede que contenga caracteres no validos";
	        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        		return false;
	        	}
	        }
	        if(pago.ID_SatMetodosPago.equals("02") /*cheque*/ && (fsipg_beneficiario.equals("") || pago.Cheque.equals("")) )
	        {
	        	short idmensaje = 3;
	        	String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR", 3); //"ERROR: Debes proporcionar el número y beneficiario para el cheque <br>");
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	return false;
	        }
	        if(pago.ID_SatMetodosPago.equals("03") /*transferencia*/ && pago.ID_FormaPago == 1)
	        {
	        	short idmensaje = 1;
	        	String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 1); //PRECAUCION: No se puede agregar un depósito o retiro de caja por un método de transferencia.
	        	getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        	return false;
	        }
	        JAdmBancosCuentasSet bcSet = new JAdmBancosCuentasSet(request);
	        bcSet.m_Where = "Tipo = '" + pago.ID_FormaPago + "' and Clave = '" + pago.ID_BanCaj + "'";
	        bcSet.Open();
	        if(!bcSet.getAbsRow(0).getFijo()) //Banco o Caja contable
	        {
	        	if(fsipg_beneficiario.equals("") || fsipg_rfc.equals(""))
	        	{
	        		short idmensaje = 3;
	        		String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 5); //"ERROR: Debes proporcionar el beneficiario y RFC para soporte de contabilidad electrónica, si es un depósito, nuestra empresa como beneficiario y el RFC del que paga, y si es un retiro, el beneficiario y RFC, ambos del que recibe. 
	        		getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        		return false;
	        	}
	           
	        	if(pago.ID_FormaPago == 0) //Pago con banco
	        	{
	        		if((pago.ID_SatMetodosPago.equals("03") || (proc.equals("deposito") && pago.ID_SatMetodosPago.equals("02"))) && (pago.CuentaBanco.equals("") || (pago.ID_SatBanco.equals("000") && pago.BancoExt.equals(""))))
	        		{
	        			short idmensaje = 3;
	        			String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR3", 2); //"ERROR: Debes proporcionar cuenta y banco para soporte de contabilidad electrónica, si es un depósito, cuenta y banco del cheque u origen de la transferencia, si es retiro, cuenta y banco de destino de la transferencia.
	        			getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        			return false;
	        		}
	        	}
	        	else //Pago con caja
	        	{
	        		if(pago.ID_SatMetodosPago.equals("02") && (pago.CuentaBanco.equals("") || (pago.ID_SatBanco.equals("000") && pago.BancoExt.equals(""))))
	        		{
	        			short idmensaje = 3;
	        			String mensaje = JUtil.Msj("CEF", "BANCAJ_BANCAJ", "DLG", "MSJ-PROCERR4", 2); //ERROR: Debes proporcionar numero, cuenta y banco del cheque para soporte de contabilidad electrónica
	        			getSesion(request).setID_Mensaje(idmensaje, mensaje.toString());
	        			return false;
	        		}
	        	}
	        }
	                      	
	        if(proc.equals("retiro") && pago.ID_SatMetodosPago.equals("02")/*cheque*/)
	        	pago.TipoMov = "CHQ";
	        else if(proc.equals("deposito"))
	        {
	        	if(!pago.ID_SatMetodosPago.equals("02")/*cheque*/)
	        		pago.TipoMov = "DEP";
	        	else
	        		pago.TipoMov = "DCH";
	        } 
	        else
	        	pago.TipoMov = "RET";
	           
	        // Fin de la revision larga
			////////////////////////////////////////////////////////////////////////////////////
			tmp_pagos += "INSERT INTO _TMP_PAGOS\n";
			tmp_pagos += "VALUES(default,'" + pago.ID_FormaPago + "','" + pago.ID_BanCaj + "','" + pago.Total + "','" + p(pago.RefPago) + "','" +
					pago.TipoMov + "','" + p(pago.ID_SatBanco) + "','" + p(pago.ID_SatMetodosPago) + "','" + p(pago.BancoExt) + "','" + p(pago.CuentaBanco) + "','" + p(pago.Cheque) + "');\n";
			
		}
				
		gran_total = JUtil.redondear(gran_total, 2);
		total_pago = JUtil.redondear(total_pago, 2);
		cajas_total = JUtil.redondear(cajas_total, 2);
		cajas_efec = JUtil.redondear(cajas_efec, 2);
		
		if(entidadescaja == false && entidadesbanco == false)
		{
            short idmensaje = 3;
	        String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR",5); //"NS ERROR: No existen entidades de pago para este registro. No se puede generar el registro de contado, intenta generar el registro a crédito si es conveniente ó da de alta las entidades de pago para este registro y vuelve a intentarlo<br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
		}
		if(gran_total != total_pago)
		{
            short idmensaje = 3;
	        String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR2",1) + " -" + gran_total + "- -" + total_pago + "-"; //"NS ERROR: La suma del pago de todas las entidades " + gran_total + ", es diferente al total del registro " + total_pago + " y este debe de ser igual. Corrígelo para poder guardar el registro<br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
		}
		if(cajas_total > cajas_efec)
		{
            short idmensaje = 3;
	        String mensaje = JUtil.Msj("GLB","PAGOS","DLG","MSJ-PROCERR2",2); //"NS ERROR: La cantidad en efectivo no puede ser menor al pago en efectivo.<br>";
	        getSesion(request).setID_Mensaje(idmensaje, mensaje);
	        return false;
		}
			
		request.setAttribute("fsipg_cambio", JUtil.redondear((cajas_efec - cajas_total), 2));
		request.setAttribute("fsipg_efectivo", JUtil.redondear(cajas_efec,2));
		request.setAttribute("fsipg_bancos", JUtil.redondear(bancos_total,2));
		request.setAttribute("fsipg_tmppagos", tmp_pagos);
		
		return true;
			
	}
    
    
    protected void doCallStoredProcedure(HttpServletRequest request, HttpServletResponse response, String SQLCall, JRetFuncBas rfb)
		throws ServletException, IOException
	{
    	int idmensaje = -1; 
        String mensaje = "", clave = "";
        
    	try
        {
           Connection con = JAccesoBD.getConexionSes(request);
           con.setAutoCommit(false);
           Statement s    = con.createStatement();
           ResultSet rs   = s.executeQuery(SQLCall);
           if(rs.next())
           {
             idmensaje = rs.getInt("ERR");
             mensaje = rs.getString("RES");
             clave = rs.getString("CLAVE");
           }
           s.close();
           if(idmensaje == 0)
        	   con.commit();
           else
        	   con.rollback();
           
           JAccesoBD.liberarConexion(con);

           getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
           rfb.setRS(idmensaje, mensaje, clave);
        }
        catch(SQLException e)
        {
        	e.printStackTrace(System.out);
        	//throw new RuntimeException(e.toString());
        	idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
 	       	getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
            rfb.setRS(4, mensaje, clave);
        }
    }
	
    protected void doCallStoredProcedureNOID(HttpServletRequest request, HttpServletResponse response, String SQLCall, JRetFuncBas rfb)
    	throws ServletException, IOException
    {
    	int idmensaje = -1; 
		String mensaje = "", clave = "";
		try
    	{
    		Connection con = JAccesoBD.getConexionSes(request);
    		con.setAutoCommit(false);
    		Statement s    = con.createStatement();
    		ResultSet rs   = s.executeQuery(SQLCall);
    		if(rs.next())
    		{
    			idmensaje = rs.getInt("ERR");
    			mensaje = rs.getString("RES");
    		}	
    		s.close();
    		if(idmensaje == 0)
    			con.commit();
    		else
    			con.rollback();
               
    		JAccesoBD.liberarConexion(con);

    		getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
    		rfb.setRS(idmensaje, mensaje, "");
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace(System.out);
        	//throw new RuntimeException(e.toString());
        	idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
 	       	getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
            rfb.setRS(4, mensaje, clave);
    	}
    }
    
    protected void doCallStoredProcedure(HttpServletRequest request, HttpServletResponse response, String TMP_TBL, String SQLCall, String DELClause, JRetFuncBas rfb)
    	throws ServletException, IOException
    {
    	int idmensaje = -1; 
        String mensaje = "", clave = "";
        try
        {
           Connection con = JAccesoBD.getConexionSes(request);
           con.setAutoCommit(false);
           Statement s    = con.createStatement();
           s.executeUpdate(TMP_TBL);
           ResultSet rs   = s.executeQuery(SQLCall);
           if(rs.next())
           {
             idmensaje = rs.getInt("ERR");
             mensaje = rs.getString("RES");
             clave = rs.getString("CLAVE");
           }
           s.executeUpdate(DELClause);
           s.close();
           if(idmensaje == 0)
        	   con.commit();
           else
        	   con.rollback();
           
           JAccesoBD.liberarConexion(con);

           getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
           rfb.setRS(idmensaje, mensaje, clave);
        }
        catch(SQLException e)
        {
        	e.printStackTrace(System.out);
        	//throw new RuntimeException(e.toString());
        	idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
 	       	getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
            rfb.setRS(4, mensaje, clave);
        
        }
    	
    }
	
    
    protected String MsjPermisoDenegado(HttpServletRequest request, String Apl, String permiso)
    {
    	/*Revisa si esta vigente la sesion
        HttpSession ses = request.getSession(true);
        JSesionPrincipal princ;
        if(Apl.equals("SAF"))
    		princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");
        else if(Apl.equals("CEF"))
        	princ = (JSesionPrincipal)ses.getAttribute("forseti");
        else // REF
        	princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");
        
        if(princ == null || !princ.getRegistrado())
        {
        	response.setContentType("text/html");
      	  	PrintWriter out = response.getWriter();
      	  	out.println(Salida);
      	  	return;
        }
		*/
    	JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(request);
		if(Apl.equals("SAF"))
			set.ConCat(true);
		
		set.m_Where = "ID_Permiso = '" + p(permiso) + "'";
		set.Open();
		
		if(set.getNumRows() < 1)
			return JUtil.Msj("GLB","REGISTRO","SESION","ERROR-PERMISO", 2) + " " + permiso;
		else
			return JUtil.Msj("GLB","REGISTRO","SESION","ERROR-PERMISO", 1) + " " + permiso + " --" + set.getAbsRow(0).getDescripcion() + "--";
    }
    
    // Registro De Proceso... Se encarga de registrar los procesos involucrados en cada transaccion del SAF CEF o REF
    protected void RDP(String id_tipo, String fsibd, String status, String id_usuario, String id_proceso, String id_modulo, String resultado)
    {
    	String sql = "SELECT RDP('" + id_tipo + "','" + fsibd + "','" + status + "','" + id_usuario + "','" + id_proceso + "','" + id_modulo + "','" + resultado + "');";
    	//System.out.println(sql);
    	try
   		{
   			Connection con = JAccesoBD.getConexion();
   			Statement s    = con.createStatement();
   			s.execute(sql);
   			s.close();
   			JAccesoBD.liberarConexion(con);
   		}
   		catch(SQLException e)
   		{
   			e.printStackTrace();
   			throw new RuntimeException(e.toString());
   		}
    }
    
    protected synchronized short generarCFDI(HttpServletRequest request, HttpServletResponse response, String tipo, int id, String esp1, JManejadorSet forSet, byte tfd, StringBuffer sb_mensaje) 
    		throws ServletException, IOException
    {
    	short res = 0;
    	
    	JForsetiCFDEntidad cfd = new JForsetiCFDEntidad();
      	if(tipo.equals("NOMINA"))
    		cfd.generarNomina(request, response, tipo, id, esp1, (JNominaEntidadesSetIds)forSet, tfd);
    	else if(tipo.equals("TRASPASOS"))
    		cfd.generarTraslado(request, response, tipo, id, (JAlmacenesMovimSetIdsV2)forSet, tfd);
    	else
    		cfd.generarVenta(request, response, tipo, id, esp1, (JVentasEntidadesSetIdsV2)forSet, tfd);
    	
    	if(cfd.getStatusCFD() == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfdi
		{
			res = 3;
			sb_mensaje.append("ERROR: No se pudo sellar el CFDI, debes comunicar el error al administrador del sistema<br>ERROR DE CFDI:<br> " + cfd.getError() + "<br>");
		}
    	else if(cfd.getStatusCFD() == JForsetiCFD.OKYDOKY) // quiere decir que se sello con exito
		{
    		sb_mensaje.append("El CFDI se sello satisfactoriamente<br>");
    		
    		if(tipo.equals("NOMINA"))
    		{
    			JMasempSet set = new JMasempSet(request);
    			set.m_Where = "ID_Empleado = '" + p(esp1) + "'";
    			set.Open();
    			if(set.getAbsRow(0).getSMTP() == 2) // Maneja smtp automático
    			{
    				JFsiSMTPClient smtp = new JFsiSMTPClient();
    				smtp.enviarCFDI(request, "NOM", Integer.toString(id), esp1, set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
    				if(smtp.getStatusSMTP() == JFsiSMTPClient.ERROR)
    				{
    					res = 3;
    					sb_mensaje.append("ERROR SMTP Al enviar Archivos CFDI: " + smtp.getError() + "<br>");
    				}
    				else
    					sb_mensaje.append("Los archivos CFDI se enviaron al SMTP satisfactoriamente<br>");
    			}
    		}
    		else if(!tipo.equals("TRASPASOS"))
    		{
    			if(!esp1.equals("0")) //El cliente no es de mostrador
    			{
    				JClientClientSetV2 set = new JClientClientSetV2(request);
    				set.m_Where = "Clave = '" + p(esp1) + "'";
      		  		set.Open();
      		  		if(set.getAbsRow(0).getSMTP() == 2) // Maneja smtp automático
      		  		{
      		  			String nomar;
      		  			if(tipo.equals("FACTURAS"))
      		  				nomar = "FAC";
      		  			else if(tipo.equals("REMISIONES"))
      		  				nomar = "REM";
      		  			else
      		  				nomar = "DSV";
      		  			JFsiSMTPClient smtp = new JFsiSMTPClient();
      		  			smtp.enviarCFDI(request, nomar, Integer.toString(id), esp1, set.getAbsRow(0).getNombre(), set.getAbsRow(0).getEMail());
      		  			if(smtp.getStatusSMTP() == JFsiSMTPClient.ERROR)
      		  			{
      		  				res = 3;
      		  				sb_mensaje.append("ERROR SMTP Al enviar archivos CFDI: " + smtp.getError() + "<br>");
      		  			}
      		  			else
      		  				sb_mensaje.append("Los archivos CFDI se enviaron al SMTP satisfactoriamente<br>");
      		  		}
    			}
    		}
     		
		}
    	
		return res;
    }
    
    protected synchronized short cancelarCFDI(HttpServletRequest request, HttpServletResponse response, String tipo, int id, byte tfd, StringBuffer sb_mensaje) 
    		throws ServletException, IOException
    {
    	short res = 0;
    	
    	JForsetiCFDEntidad cfd = new JForsetiCFDEntidad();
    	cfd.cancelarCFDI(request, response, tipo, id, tfd);
    	res = cfd.getStatusCFD();
    	
    	if(cfd.getStatusCFD() == JForsetiCFD.OKYDOKY) // quiere decir que se sello con exito
			sb_mensaje.append("El sello se cancelo satisfactoriamente<br>");
    	else if(cfd.getStatusCFD() == JForsetiCFD.ERROR) // quiere decir algun tipo de error de cfdi
			sb_mensaje.append("ERROR: fallo al cancelar el sello, debes comunicar el error al administrador del sistema<br>ERROR DE CFDI:<br> " + cfd.getError() + "<br>");
    	else 
			sb_mensaje.append("");
    	
		return res;
    }
    
	class JForsetiAplPagos
	{
		byte ID_FormaPago;
		byte ID_BanCaj;
		float Total;
		float Efectivo;
		String RefPago;
		String TipoMov;
		String ID_SatBanco;
		String ID_SatMetodosPago;
		String BancoExt;
		String CuentaBanco;
		String Cheque;
		
		public JForsetiAplPagos()
		{
			
		}
	}

}
