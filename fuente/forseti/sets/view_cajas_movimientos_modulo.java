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

public class view_cajas_movimientos_modulo
{
	private long m_ID;
	private byte m_Tipo_ID;
	private int m_Num;
	private Date m_Fecha;
	private String m_Concepto;
	private float m_Deposito;
	private float m_Retiro;
	private String m_Estatus;
	private String m_Ref;
	private long m_Pol_ID;
	private String m_Pol;
	private boolean m_MC;
	private boolean m_EsTrans;
	private String m_Doc;
	private float m_Saldo;

	public void setID(long ID)
	{
		m_ID = ID;
	}

	public void setTipo_ID(byte Tipo_ID)
	{
		m_Tipo_ID = Tipo_ID;
	}

	public void setNum(int Num)
	{
		m_Num = Num;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDeposito(float Deposito)
	{
		m_Deposito = Deposito;
	}

	public void setRetiro(float Retiro)
	{
		m_Retiro = Retiro;
	}

	public void setEstatus(String Estatus)
	{
		m_Estatus = Estatus;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setPol_ID(long Pol_ID)
	{
		m_Pol_ID = Pol_ID;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setMC(boolean MC)
	{
		m_MC = MC;
	}

	public void setEsTrans(boolean EsTrans)
	{
		m_EsTrans = EsTrans;
	}

	public void setDoc(String Doc)
	{
		m_Doc = Doc;
	}

	public void setSaldo(float Saldo)
	{
		m_Saldo = Saldo;
	}


	public long getID()
	{
		return m_ID;
	}

	public byte getTipo_ID()
	{
		return m_Tipo_ID;
	}

	public int getNum()
	{
		return m_Num;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public float getDeposito()
	{
		return m_Deposito;
	}

	public float getRetiro()
	{
		return m_Retiro;
	}

	public String getEstatus()
	{
		return m_Estatus;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public long getPol_ID()
	{
		return m_Pol_ID;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public boolean getMC()
	{
		return m_MC;
	}

	public boolean getEsTrans()
	{
		return m_EsTrans;
	}

	public String getDoc()
	{
		return m_Doc;
	}

	public float getSaldo()
	{
		return m_Saldo;
	}


}

