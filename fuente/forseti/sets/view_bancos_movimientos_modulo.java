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
package forseti.sets;

import java.sql.Date;

public class view_bancos_movimientos_modulo
{
	private String m_Beneficiario;
	private String m_Concepto;
	private double m_Deposito;
	private String m_Doc;
	private String m_Estatus;
	private Date m_Fecha;
	private int m_ID;
	private boolean m_MC;
	private int m_Num;
	private String m_Pol;
	private long m_Pol_ID;
	private String m_Referencia;
	private double m_Retiro;
	private byte m_Tipo;
	private byte m_Clave;
	private boolean m_EsTrans;
	private double m_Saldo;
	private byte m_ID_Moneda;
	private double m_TC;
	private String m_TipoMov;
	private String m_Ref;
	private String m_RFC;
	private String m_ID_SatBanco;
	private String m_DescMetodosPago;
	private String m_ID_SatMetodosPago;
	private String m_CuentaBanco;
	private String m_BancoExt;
	
	public void setID_Moneda(byte moneda) 
	{
		m_ID_Moneda = moneda;
	}

	public void setTC(double tc) 
	{
		m_TC = tc;
	}

	public void setBeneficiario(String Beneficiario)
	{
		m_Beneficiario = Beneficiario;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDeposito(double Deposito)
	{
		m_Deposito = Deposito;
	}

	public void setDoc(String Doc)
	{
		m_Doc = Doc;
	}

	public void setEstatus(String Estatus)
	{
		m_Estatus = Estatus;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID(int ID)
	{
		m_ID = ID;
	}

	public void setMC(boolean MC)
	{
		m_MC = MC;
	}

	public void setNum(int Num)
	{
		m_Num = Num;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setPol_ID(long Pol_ID)
	{
		m_Pol_ID = Pol_ID;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setRetiro(double Retiro)
	{
		m_Retiro = Retiro;
	}

	public void setClave(byte Clave)
	{
		m_Clave = Clave;
	}

	public void setEsTrans(boolean EsTrans)
	{
		m_EsTrans = EsTrans;
	}

	public void setSaldo(double Saldo)
	{
		m_Saldo = Saldo;
	}


	public String getBeneficiario()
	{
		return m_Beneficiario;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public double getDeposito()
	{
		return m_Deposito;
	}

	public String getDoc()
	{
		return m_Doc;
	}

	public String getEstatus()
	{
		return m_Estatus;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID()
	{
		return m_ID;
	}

	public boolean getMC()
	{
		return m_MC;
	}

	public int getNum()
	{
		return m_Num;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public long getPol_ID()
	{
		return m_Pol_ID;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public double getRetiro()
	{
		return m_Retiro;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}
	
	public byte getClave()
	{
		return m_Clave;
	}

	public boolean getEsTrans()
	{
		return m_EsTrans;
	}

	public double getSaldo()
	{
		return m_Saldo;
	}

	public byte getID_Moneda() 
	{
		return m_ID_Moneda;
	}

	public double getTC() 
	{
		return m_TC;
	}

	public void setTipo(byte Tipo) 
	{
		m_Tipo = Tipo;
		
	}

	public String getTipoMov() 
	{
		return m_TipoMov;
	}

	public void setTipoMov(String TipoMov) 
	{
		m_TipoMov = TipoMov;
	}

	public String getRef() 
	{
		return m_Ref;
	}

	public void setRef(String Ref) 
	{
		m_Ref = Ref;
	}

	public String getID_SatBanco() 
	{
		return m_ID_SatBanco;
	}

	public String getRFC() 
	{
		return m_RFC;
	}

	public void setID_SatBanco(String ID_SatBanco) 
	{
		m_ID_SatBanco = ID_SatBanco;
	}

	public void setRFC(String RFC) 
	{
		m_RFC = RFC;
	}

	public void setID_SatMetodosPago(String ID_SatMetodosPago) 
	{
		m_ID_SatMetodosPago = ID_SatMetodosPago;
	}

	public void setDescMetodosPago(String DescMetodosPago) 
	{
		m_DescMetodosPago = DescMetodosPago;
	}
	
	public String getID_SatMetodosPago() 
	{
		return m_ID_SatMetodosPago;
	}

	public String getDescMetodosPago() 
	{
		return m_DescMetodosPago;
	}

	public String getCuentaBanco() 
	{
		return m_CuentaBanco;
	}

	public String getBancoExt() 
	{
		return m_BancoExt;
	}
	
	public void setCuentaBanco(String CuentaBanco) 
	{
		m_CuentaBanco = CuentaBanco;
	}

	public void setBancoExt(String BancoExt) 
	{
		m_BancoExt = BancoExt;
	}
}

