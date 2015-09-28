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


public class TBL_BANCOS_CUENTAS
{
	private byte m_Tipo;
	private short m_Clave;
	private String m_Cuenta;
	private int m_SigCheque;
	private String m_Fmt_Dep;
	private String m_Fmt_Ret;
	private String m_CC;
	private String m_Status;
	private float m_Saldo;
	private boolean m_Fijo;
	private byte m_TipoTRASP;
	private short m_ClaveTRASP;
	private boolean m_TodoTRASP;
	private long m_UltimoNumTRASP;
	private float m_FondoTRASP;
	private byte m_ID_Moneda;
	private String m_ID_Clasificacion;
	private String m_Descripcion;
	private String m_Nombre;
	private String m_ID_SatBanco;
	private String m_Banco;
	private String m_BancoExt;
	
	public short getClaveTRASP() 
	{
		return m_ClaveTRASP;
	}

	public void setClaveTRASP(short ClaveTRASP) 
	{
		m_ClaveTRASP = ClaveTRASP;
	}

	public byte getTipoTRASP() 
	{
		return m_TipoTRASP;
	}

	public void setTipoTRASP(byte TipoTRASP) 
	{
		m_TipoTRASP = TipoTRASP;
	}

	public boolean getTodoTRASP() 
	{
		return m_TodoTRASP;
	}

	public void setTodoTRASP(boolean TodoTRASP) 
	{
		m_TodoTRASP = TodoTRASP;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setClave(short Clave)
	{
		m_Clave = Clave;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setSigCheque(int SigCheque)
	{
		m_SigCheque = SigCheque;
	}

	public void setFmt_Dep(String Fmt_Dep)
	{
		m_Fmt_Dep = Fmt_Dep;
	}

	public void setFmt_Ret(String Fmt_Ret)
	{
		m_Fmt_Ret = Fmt_Ret;
	}

	public void setCC(String CC)
	{
		m_CC = CC;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setSaldo(float Saldo)
	{
		m_Saldo = Saldo;
	}

	public void setFijo(boolean Fijo)
	{
		m_Fijo = Fijo;
	}


	public byte getTipo()
	{
		return m_Tipo;
	}

	public short getClave()
	{
		return m_Clave;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public int getSigCheque()
	{
		return m_SigCheque;
	}

	public String getFmt_Dep()
	{
		return m_Fmt_Dep;
	}

	public String getFmt_Ret()
	{
		return m_Fmt_Ret;
	}

	public String getCC()
	{
		return m_CC;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getSaldo()
	{
		return m_Saldo;
	}

	public boolean getFijo()
	{
		return m_Fijo;
	}

	public float getFondoTRASP() 
	{
		return m_FondoTRASP;
	}

	public void setFondoTRASP(float FondoTRASP) 
	{
		m_FondoTRASP = FondoTRASP;
	}

	public long getUltimoNumTRASP() 
	{
		return m_UltimoNumTRASP;
	}

	public void setUltimoNumTRASP(long UltimoNumTRASP) 
	{
		m_UltimoNumTRASP = UltimoNumTRASP;
	}

	public void setID_Moneda(byte ID_Moneda) 
	{
		m_ID_Moneda = ID_Moneda;
		
	}

	public long getID_Moneda() 
	{
		return m_ID_Moneda;
	}

	public void setDescripcion(String Descripcion) 
	{
		m_Descripcion = Descripcion;	
	}
	
	public String getDescripcion()
	{
		return m_Descripcion;
	}
	
	public void setID_Clasificacion(String ID_Clasificacion) 
	{
		m_ID_Clasificacion = ID_Clasificacion;	
	}
	
	public String getID_Clasificacion()
	{
		return m_ID_Clasificacion;
	}

	public void setNombre(String Nombre) 
	{
		m_Nombre = Nombre;
	}
	
	public String getNombre()
	{
		return m_Nombre;
	}

	public void setID_SatBanco(String ID_SatBanco) 
	{
		m_ID_SatBanco = ID_SatBanco;	
	}
	
	public String getID_SatBanco() 
	{
		return m_ID_SatBanco;	
	}

	public void setBanco(String Banco) 
	{
		m_Banco = Banco;
	}
	
	public String getBanco() 
	{
		return m_Banco;
	}

	public void setBancoExt(String BancoExt) 
	{
		m_BancoExt = BancoExt;	
	}
	
	public String getBancoExt() 
	{
		return m_BancoExt;
	}
}

