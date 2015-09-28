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
package forseti.caja_y_bancos;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JBDSSet;
import forseti.sets.JBancosSetIdsV2;
import forseti.sets.JPublicBancosCuentasSetV2;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JPublicContMonedasSetV2;

public class JMovBancariosSes extends JSesionRegs
{

  private String m_Status;
  private String m_TipoPrimario;
  private String m_TipoMov;
  private long m_SigCheque;
  private String m_DepChq;
  private String m_CuentaBanco;
  private String m_ID_SatBanco;
  private String m_BancoExt;
  private String m_MetPagoPol;
  private byte m_ID_Moneda;
  private double m_TC;
  private Date m_Fecha;
  private String m_Concepto;
  private String m_StatusCuenta;
  private String m_Beneficiario;
  private String m_RFC;
  private String m_Ref;
  private int m_Num;
  private int m_ID_Entidad;
  private double m_Cantidad;
  private double m_SumDebe;
  private double m_SumHaber;
  private double m_TotalDebe;
  private double m_TotalHaber;
  private boolean m_Fijo;
  private boolean m_EsTrans;
  private byte m_Tipo;
  private byte m_Clave;
  private byte m_ID_MonedaDEST;
  private boolean m_FijoDEST;
  private String m_Destino;
  
  public String getDestino() 
  {
	  return m_Destino;
  }

  public void setDestino(String Destino) 
  {
	  m_Destino = Destino;
  }

  public boolean getEsTrans() 
  {
	  return m_EsTrans;
  }

  public void setEsTrans(boolean EsTrans) 
  {
	  m_EsTrans = EsTrans;
  }

  public byte getTipo() 
  {
	  return m_Tipo;
  }

  public void setTipo(byte Tipo) 
  {
	  m_Tipo = Tipo;
  }

  public byte getClave() 
  {
	  return m_Clave;
  }

  public void setClave(byte Clave) 
  {
	  m_Clave = Clave;
  }

  public double getTotalDebe() 
  {
	  return m_TotalDebe;
  }

  public void setTotalDebe(double TotalDebe) 
  {
	  m_TotalDebe = TotalDebe;
  }

  public double getTotalHaber() 
  {
	  return m_TotalHaber;
  }

  public void setTotalHaber(double TotalHaber) 
  {
	  m_TotalHaber = TotalHaber;
  }

  public JMovBancariosSes(HttpServletRequest request, String ID_Entidad, String usuario, String modulo, boolean EsTrans, String TipoPrimario) 
		  throws ServletException, IOException
  {
	  JBancosSetIdsV2 setids;
	  if(modulo.equals("BANCOS"))
		  setids = new JBancosSetIdsV2(request,usuario,"0",ID_Entidad);
	  else
		  setids = new JBancosSetIdsV2(request,usuario,"1",ID_Entidad);
	  
	  setids.Open();  
	 	 	 
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_ID_Moneda = setids.getAbsRow(0).getID_Moneda();
	  m_TC = setids.getAbsRow(0).getTC();
	  m_StatusCuenta = setids.getAbsRow(0).getEstatus();
	  m_SigCheque = setids.getAbsRow(0).getRef();
	  m_DepChq = "";
	  m_ID_SatBanco = "000";
	  m_CuentaBanco = "";
	  m_BancoExt = "";
	  m_Concepto = "";
	  if(!setids.getAbsRow(0).getFijo() && TipoPrimario.equals("deposito"))
	  {
		  JBDSSet set = new JBDSSet(request);
		  set.ConCat(true);
		  set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
		  set.Open();
		  m_TipoMov = "deposito";
		  m_MetPagoPol = "01"; //Efectivo
		  m_Beneficiario = set.getAbsRow(0).getCompania();
	  }
	  else
	  {
		  if(modulo.equals("BANCOS"))
		  {
			  m_TipoMov = "cheque";
		  	  m_MetPagoPol = "02"; //Cheque
		  }
		  else
		  {
			  m_TipoMov = "retiro";
			  m_MetPagoPol = "01"; //Efectivo
		  }
		  m_Beneficiario = "";
	  }
	  m_RFC = "";
	  m_Ref = "";
	  m_Num = 0;
	  m_ID_Entidad = Integer.parseInt(ID_Entidad);
	  m_Status = "G";
	  m_SumDebe = 0;
	  m_SumHaber = 0;
	  m_Cantidad = 0;
	  m_TipoPrimario = TipoPrimario;
	  m_TotalDebe = 0;
	  m_TotalHaber = 0;
	  m_Fijo = setids.getAbsRow(0).getFijo();
	  m_EsTrans = EsTrans;
	  m_ID_MonedaDEST = 1;
	  m_Destino = "BANCOS";
	  m_FijoDEST = false;
  }
 
  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
		throws ServletException, IOException
  {
  		short res = -1;
		  
  		if(request.getParameter("fecha") != null && request.getParameter("concepto") != null &&
  		         request.getParameter("beneficiario") != null && request.getParameter("rfc") != null && request.getParameter("ref") != null && request.getParameter("cuentabanco") != null && request.getParameter("id_satbanco") != null && request.getParameter("bancoext") != null &&
  		         request.getParameter("depchq") != null && request.getParameter("cantidad") != null && request.getParameter("metpagopol") != null &&
  		         !request.getParameter("fecha").equals("") && !request.getParameter("concepto").equals("")  && !request.getParameter("id_satbanco").equals("")  && !request.getParameter("metpagopol").equals(""))
	    {
  			
	    }
	    else
	    {
	        res = 3; 
	        sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO", 2)); //"ERROR: Alguno de los parametros del cabecero es nulo <br>");
	        return res;
	    }
  		
  		m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
		m_Concepto = request.getParameter("concepto");
		m_Beneficiario = request.getParameter("beneficiario");
		m_RFC = request.getParameter("rfc");
		m_Ref = request.getParameter("ref");
		m_DepChq = request.getParameter("depchq");
		//System.out.println(request.getParameter("depchq"));
		m_ID_SatBanco = request.getParameter("id_satbanco");
		m_CuentaBanco = request.getParameter("cuentabanco");;
		m_BancoExt = request.getParameter("bancoext");
		m_MetPagoPol = request.getParameter("metpagopol");
		  
		try{ m_Cantidad = Float.parseFloat(request.getParameter("cantidad")); } catch(NumberFormatException e) { m_Cantidad = 0; }
		
		if(m_MetPagoPol.equals("02") && m_TipoPrimario.equals("retiro"))
			m_TipoMov = "cheque";
		else if(!m_MetPagoPol.equals("02") && m_TipoPrimario.equals("retiro"))
			m_TipoMov = "retiro";
		else
			m_TipoMov = "deposito";
		
		if(request.getParameter("status") != null)
			m_Status = "T";
		else
			m_Status = "G";
		
	    byte identidad = -1;
	    
	    if(request.getParameter("modulo").equals("BANCOS"))
	    	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("BANCAJ_BANCOS").getEspecial());
	    else 
	    	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("BANCAJ_CAJAS").getEspecial());	  
	        
		if(m_ID_Entidad != identidad)
		{
		      res = 3; 
		      sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "ENTIDAD-DIF")); //"ERROR: La entidad ya no es la misma que la inicial <br>");
		      return res;
		}
		
		if(m_EsTrans)
        {
			if(request.getParameter("bancaj") == null || request.getParameter("bancaj").equals("")
	                || request.getParameter("bancaj").equals("BANCOS") || request.getParameter("bancaj").equals("CAJAS"))
	        {
				res = 3; 
        		sb_mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "SES", "MSJ-PROCERR", 1)); //"ERROR: Debe especificarse el banco o la caja de destino para este traspaso");
        		return res;
	        }
			
			JPublicBancosCuentasSetV2 set = new JPublicBancosCuentasSetV2(request);
	        String bancaj = request.getParameter("bancaj").substring(8);
	        if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1) // es hacia banco
	        	set.m_Where = "Tipo = '0' and Clave = '" + JUtil.p(bancaj) + "'";
	        else
	        	set.m_Where = "Tipo = '1' and Clave = '" + JUtil.p(bancaj) + "'";
	        
	        set.Open();
	        
	        if(set.getNumRows() == 0)
	        {
	        	res = 3; 
        		sb_mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "SES", "MSJ-PROCERR", 2) + " " + bancaj); //"ERROR: No existe la cuenta o caja de destino " + bancaj);
        		return res;
	        }
	        
	        if(request.getParameter("bancaj").indexOf("FSI_BAN_") != -1) // es hacia banco
	           	m_Tipo = 0;
	        else
	        	m_Tipo = 1;
	        
	        m_Clave = set.getAbsRow(0).getClave();
	        m_ID_MonedaDEST = set.getAbsRow(0).getID_Moneda();
	        m_Destino = request.getParameter("bancaj");
	        m_FijoDEST = set.getAbsRow(0).getFijo();
        }  
		
		if(m_ID_Moneda != 1 || m_ID_MonedaDEST != 1)
        {
        	 if(request.getParameter("tcGEN") == null || request.getParameter("tcGEN").equals(""))
        	 {
        		 res = 3; 
        		 sb_mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "SES", "MSJ-PROCERR", 3)); // "ERROR: Como la cuenta no es de moneda nacional, no se ha mandado el tipo de cambio general o este es inválido<br>");
                 return res; 
        	 }
        	 
        	 
        	 try 
        	 { 
        		m_TC = Float.parseFloat(request.getParameter("tcGEN"));
        	 }
        	 catch(NumberFormatException e)
        	 {
        		res = 3; 
        		sb_mensaje.append(JUtil.Msj("CEF", "BANCAJ_BANCAJ", "SES", "MSJ-PROCERR", 3)); //"ERROR: Como la cuenta no es de moneda nacional, no se ha mandado el tipo de cambio general o este es inválido<br>");
                return res; 
         	 }
        }  

		establecerResultados(request);
		
		return res;
	
	}

  
  public double getCantidad()
  {
    return m_Cantidad;
  }

  public double getSumDebe()
  {
    return m_SumDebe;
  }

  public double getSumHaber()
  {
    return m_SumHaber;
  }

  public String getStatus()
  {
    return m_Status;
  }

  public long getSigCheque()
  {
    return m_SigCheque;
  }
  
  public String getDepChq()
  {
    return m_DepChq;
  }
  
  public String getID_SatBanco()
  {
    return m_ID_SatBanco;
  }
  
  public String getCuentaBanco()
  {
    return m_CuentaBanco;
  }
  
  public String getBancoExt()
  {
    return m_BancoExt;
  }
  
  public String getMetPagoPol()
  {
    return m_MetPagoPol;
  }
  
  public JMovBancariosSesPart getPartida(int ind)
  {
    return (JMovBancariosSesPart)m_Partidas.elementAt(ind);
  }
  
  public byte getID_Moneda() 
  {
	return m_ID_Moneda;
  }
  
  public String getConcepto() 
  {
	return m_Concepto;
  }

  public Date getFecha() 
  {
	return m_Fecha;
  }

  public String getStatusCuenta() 
  {
	return m_StatusCuenta;
  }

  public double getTC() 
  {
	return m_TC;
  }

  public void setFecha(Date fecha) 
  {
	m_Fecha = fecha;
  }

  public void setID_Moneda(byte moneda) 
  {
	m_ID_Moneda = moneda;
  }

  public void setSigCheque(long sigCheque) 
  {
	m_SigCheque = sigCheque;
  }
  
  public void setDepChq(String DepChq)
  {
    m_DepChq = DepChq;
  }

  public void setID_SatBanco(String ID_SatBanco) 
  {
	m_ID_SatBanco = ID_SatBanco;
  }
  
  public void setCuentaBanco(String CuentaBanco) 
  {
	m_CuentaBanco = CuentaBanco;
  }
    
  public void setBancoExt(String BancoExt) 
  {
	m_BancoExt = BancoExt;
  }
  
  public void setMetPagoPol(String MetPagoPol) 
  {
	m_MetPagoPol = MetPagoPol;
  }
  
  public void setStatus(String status) 
  {
	m_Status = status;
  }

  public void setTC(double tc) 
  {
	m_TC = tc;
  }

 
  @SuppressWarnings("unchecked")
  public short agregaPartida(HttpServletRequest request, String cuenta, String concepto,
                            byte idmoneda, double tc, double debe,
                            double haber, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if(set.getNumRows() > 0)
    {
      if(set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",1)); //"PRECAUCION: Al agregar, la cuenta especificada se encontró en el catálogo, pero no se puede insertar porque es una cuenta acumulativa");
      }
      else if( (debe != 0 && haber != 0) || (debe == 0 && haber == 0))
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",2)); //"PRECAUCION: Al agregar, si es un depósito el retiro debe de ser 0 y viceversa. No se puede agregar la partida");
      }
      else
      {
    	byte idmonedafin = (m_ID_Moneda == 1) ? idmoneda : 1;
    	double tcfin = (m_ID_Moneda == 1) ? tc : 1.0F;
    	
        JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
        mon.m_Where = "Clave = '" + idmonedafin + "'";
        mon.Open();
        String moneda = mon.getAbsRow(0).getMoneda();
        String nombre = set.getAbsRow(0).getNombre();
        JMovBancariosSesPart part = new JMovBancariosSesPart(cuenta, nombre, concepto, idmonedafin, moneda, ((idmonedafin == 1) ? 1.0F : tcfin), debe, haber);
        m_Partidas.addElement(part);
        establecerResultados(request);
      }
    }
    else
    {
        res = 3;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",3)); //"ERROR: No se encontró la cuenta especificada");
    }

    return res;

  }

   
    @SuppressWarnings("unchecked")
  public void agregaPartida(HttpServletRequest request, String cuenta, String nombre, String concepto,
                            double parcial, byte idmoneda, double tc, double debe, double haber)
  {
    JMovBancariosSesPart part = new JMovBancariosSesPart(cuenta, nombre, concepto, parcial, idmoneda, tc, debe, haber);
    m_Partidas.addElement(part);
    establecerResultados(request);
  }

  public short editaPartida(int indPartida, HttpServletRequest request, String cuenta, String concepto,
                          byte idmoneda, double tc, double debe,
                          double haber, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if (set.getNumRows() > 0)
    {
      if (set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",1)); //"PRECAUCION: Al editar, la cuenta especificada se encontr� en el cat�logo, pero no se puede editar porque es una cuenta acumulativa");
      }
      else if ( (debe != 0 && haber != 0) || (debe == 0 && haber == 0))
      {
        res = 3;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",2)); //"PRECAUCION: Al editar, si es un retiro el dep�sito debe de ser 0 y viceversa. No se puede editar la partida");
      }
      else
      {
    	byte idmonedafin = (m_ID_Moneda == 1) ? idmoneda : 1;
        double tcfin = (m_ID_Moneda == 1) ? tc : 1.0F;
     
        JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
        mon.m_Where = "Clave = '" + idmonedafin + "'";
        mon.Open();
        String moneda = mon.getAbsRow(0).getMoneda();
        String nombre = set.getAbsRow(0).getNombre();

        JMovBancariosSesPart part = (JMovBancariosSesPart) m_Partidas.elementAt(indPartida);
        part.setPartida(cuenta, nombre, concepto, idmonedafin, moneda, ((idmonedafin == 1) ? 1.0F : tcfin), debe,haber);
        establecerResultados(request);
      }
    }
    else
    {
      res = 3;
      mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",3)); //"ERROR: No se encontró la cuenta especificada");
    }

    return res;

  }

  public void establecerResultados(HttpServletRequest request)
  {
    double SumDebe = 0, SumHaber = 0;
    for(int i = 0; i < m_Partidas.size(); i++)
    {
      JMovBancariosSesPart part = (JMovBancariosSesPart) m_Partidas.elementAt(i);
      SumDebe += part.getDebe();
      SumHaber += part.getHaber();
    }
    m_SumDebe = JUtil.redondear(SumDebe, 2);
    m_SumHaber = JUtil.redondear(SumHaber, 2);
    m_Cantidad = JUtil.redondear(m_Cantidad, 2);
    
    if(m_TipoMov.equals("deposito")) // de lo contrario, siempre es retiro ya sea con cheque u otros
    {
    	m_TotalDebe = m_Cantidad + m_SumDebe;
    	m_TotalHaber = m_SumHaber;
    }
    else
    {
    	m_TotalDebe = m_SumDebe;
    	m_TotalHaber = m_Cantidad + m_SumHaber;
    }
	

  }

  public void resetear(HttpServletRequest request, String ID_Entidad, String usuario, String modulo, boolean EsTrans, String TipoPrimario) 
		  throws ServletException, IOException
  {
	  JBancosSetIdsV2 setids;
	  if(modulo.equals("BANCOS"))
		  setids = new JBancosSetIdsV2(request,usuario,"0",ID_Entidad);
	   else
		  setids = new JBancosSetIdsV2(request,usuario,"1",ID_Entidad);
	  
	  setids.Open();  
	 	 	 
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_ID_Moneda = setids.getAbsRow(0).getID_Moneda();
	  m_TC = setids.getAbsRow(0).getTC();
	  m_StatusCuenta = setids.getAbsRow(0).getEstatus();
	  m_SigCheque = setids.getAbsRow(0).getRef();
	  m_DepChq = "";
	  m_ID_SatBanco = "000";
	  m_CuentaBanco = "";
	  m_BancoExt = "";
	  m_Concepto = "";
	  if(!setids.getAbsRow(0).getFijo() && TipoPrimario.equals("deposito"))
	  {
		  JBDSSet set = new JBDSSet(request);
		  set.ConCat(true);
		  set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
		  set.Open();
		  m_TipoMov = "deposito";
		  m_MetPagoPol = "01"; //Efectivo
		  m_Beneficiario = set.getAbsRow(0).getCompania();
	  }
	  else
	  {
		  if(modulo.equals("BANCOS"))
		  {
			  m_TipoMov = "cheque";
		  	  m_MetPagoPol = "02"; //Cheque
		  }
		  else
		  {
			  m_TipoMov = "retiro";
			  m_MetPagoPol = "01"; //Efectivo
		  }
		  m_Beneficiario = "";
	  }
	  m_RFC = "";
	  m_Ref = "";
	  m_Num = 0;
	  m_ID_Entidad = Integer.parseInt(ID_Entidad);
	  m_Status = "G";
	  m_SumDebe = 0;
	  m_SumHaber = 0;
	  m_Cantidad = 0;
	  m_TipoPrimario = TipoPrimario;
	  m_TotalDebe = 0;
	  m_TotalHaber = 0;
	  m_Fijo = setids.getAbsRow(0).getFijo();
	  m_EsTrans = EsTrans;
	  m_ID_MonedaDEST = 1;
	  m_Destino = "BANCOS";
	  m_FijoDEST = false;
	  
	  super.resetear();
  }

  public boolean getFijo() 
  {
	return m_Fijo;
  }

  public void setFijo(boolean Fijo) 
  {
	m_Fijo = Fijo;

  }

  public String getTipoMov() 
  {
	return m_TipoMov;
  }

  public String getTipoPrimario() 
  {
	return m_TipoPrimario;
  }
  
  public void borraPartida(HttpServletRequest request, int indPartida)
  {
    super.borraPartida(indPartida);
    establecerResultados(request);
  }

  public String getBeneficiario() 
  {
	return m_Beneficiario;
  }

  public void setBeneficiario(String Beneficiario) 
  {
	m_Beneficiario = Beneficiario;
  }

  public String getRFC() 
  {
	return m_RFC;
  }

  public void setRFC(String RFC) 
  {
	m_RFC = RFC;
  }
  
  public String getRef() 
  {
	return m_Ref;
  }

  public void setRef(String ref) 
  {
	m_Ref = ref;
  }

  public int getNum() 
  {
	return m_Num;
  }

  public void setNum(int num) 
  {
	m_Num = num;
  }

  public void setCantidad(double cantidad) 
  {
	m_Cantidad = cantidad;
  }

  public void setConcepto(String concepto) 
  {
	m_Concepto = concepto;
  }

  public void setTipoMov(String TipoMov) 
  {
	  if(TipoMov.equals("CHQ"))
		  m_TipoMov = "cheque";
	  else if(TipoMov.equals("RET"))
		  m_TipoMov = "retiro";
	  else
		  m_TipoMov = "deposito";
  }
  
  public void setTipoPrimario(String TipoPrimario) 
  {
	  m_TipoPrimario = TipoPrimario;
  }
  
  public byte getID_MonedaDEST() 
  {
	return m_ID_MonedaDEST;
  }

  public void setID_MonedaDEST(byte ID_MonedaDEST) 
  {
	m_ID_MonedaDEST = ID_MonedaDEST;
  }

  public boolean getFijoDEST() 
  {
	return m_FijoDEST;
  }

  public void setFijoDEST(boolean FijoDEST) 
  {
	m_FijoDEST = FijoDEST;
  }

}
