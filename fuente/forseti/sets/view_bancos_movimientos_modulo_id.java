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


public class view_bancos_movimientos_modulo_id
{
	private String m_Cuenta;
	private byte m_ID;
	private String m_CC;
	private String m_Estatus;
	private long m_Ref;
	private String m_Fmt_Dep;
	private String m_Fmt_Ret;
	private double m_Saldo;
	private byte m_Tipo;
	private String m_ID_Usuario;
	private double m_SaldoAplicado;
	private double m_SaldoTotal;
	private String m_Descripcion;
	private double m_DSBC;
	private double m_RPC;
	private byte m_ID_Moneda;
	private double m_TC;
	private boolean m_Fijo;
	
	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setID(byte ID)
	{
		m_ID = ID;
	}

	public void setCC(String CC)
	{
		m_CC = CC;
	}

	public void setEstatus(String Estatus)
	{
		m_Estatus = Estatus;
	}

	public void setRef(long Ref)
	{
		m_Ref = Ref;
	}

	public void setFmt_Dep(String Fmt_Dep)
	{
		m_Fmt_Dep = Fmt_Dep;
	}

	public void setFmt_Ret(String Fmt_Ret)
	{
		m_Fmt_Ret = Fmt_Ret;
	}

	public void setSaldo(double Saldo)
	{
		m_Saldo = Saldo;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setSaldoAplicado(double SaldoAplicado)
	{
		m_SaldoAplicado = SaldoAplicado;
	}

	public void setSaldoTotal(double SaldoTotal)
	{
		m_SaldoTotal = SaldoTotal;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setDSBC(double DSBC)
	{
		m_DSBC = DSBC;
	}

	public void setRPC(double RPC)
	{
		m_RPC = RPC;
	}

	public void setID_Moneda(byte ID_Moneda)
	{
		m_ID_Moneda = ID_Moneda;
	}

	public void setTC(double TC)
	{
		m_TC = TC;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public byte getID()
	{
		return m_ID;
	}

	public String getCC()
	{
		return m_CC;
	}

	public String getEstatus()
	{
		return m_Estatus;
	}

	public long getRef()
	{
		return m_Ref;
	}

	public String getFmt_Dep()
	{
		return m_Fmt_Dep;
	}

	public String getFmt_Ret()
	{
		return m_Fmt_Ret;
	}

	public double getSaldo()
	{
		return m_Saldo;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public double getSaldoAplicado()
	{
		return m_SaldoAplicado;
	}

	public double getSaldoTotal()
	{
		return m_SaldoTotal;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public double getDSBC()
	{
		return m_DSBC;
	}

	public double getRPC()
	{
		return m_RPC;
	}

	public byte getID_Moneda() 
	{
		return m_ID_Moneda;
	}

	public double getTC() 
	{
		return m_TC;
	}

	public boolean getFijo() 
	{
		return m_Fijo;
	}

	public void setFijo(boolean Fijo) 
	{
		m_Fijo = Fijo;		
	}

	
}

