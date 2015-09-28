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

public class view_client_cxc_modulo
{
  private long m_Clave;
  private String m_ID_TipoClient;
  private int m_ID_ClaveClient;
  private byte m_Pagada;
  private Date m_Fecha;
  private String m_Nombre;
  private String m_Doc;
  private byte m_Moneda;
  private float m_Total;
  private float m_Saldo;
  private Date m_Vencimiento;
  private String m_Ref;
  private String m_Status;
  private String m_Concepto;
  private float m_TC;
  private long m_ID_Pol;
  private String m_Pol;
  private int m_ID_Entidad;
  private byte m_ID_Concepto;
  private String m_Descripcion;
private String m_ID_TipoCP;
private long m_ID_Aplicacion;
private String m_MonedaSim;
private long m_ID_PagoBanCaj;
private String m_PagoBanCaj;
private float m_TotalPesos;
private float m_SaldoPesos;

	public void setClave(long Clave)
	{
		m_Clave = Clave;
	}

        public void setID_TipoClient(String ID_TipoClient)
        {
                m_ID_TipoClient = ID_TipoClient;
        }

        public void setID_ClaveClient(int ID_ClaveClient)
        {
                m_ID_ClaveClient = ID_ClaveClient;
        }

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDoc(String Doc)
	{
		m_Doc = Doc;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Pol(long ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setPagada(byte Pagada)
	{
		m_Pagada = Pagada;
	}

	public void setPol(String Pol)
	{
		m_Pol = Pol;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setSaldo(float Saldo)
	{
		m_Saldo = Saldo;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setVencimiento(Date Vencimiento)
	{
		m_Vencimiento = Vencimiento;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

       public void setID_Concepto(byte ID_Concepto)
       {
               m_ID_Concepto = ID_Concepto;
       }

       public void setDescripcion(String Descripcion)
       {
               m_Descripcion = Descripcion;
       }


	public long getClave()
	{
		return m_Clave;
	}

       public String getID_TipoClient()
       {
               return m_ID_TipoClient;
       }

       public int getID_ClaveClient()
       {
              return m_ID_ClaveClient;
       }

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getDoc()
	{
		return m_Doc;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public long getID_Pol()
	{
		return m_ID_Pol;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public byte getPagada()
	{
		return m_Pagada;
	}

	public String getPol()
	{
		return m_Pol;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public float getSaldo()
	{
		return m_Saldo;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getTC()
	{
		return m_TC;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public Date getVencimiento()
	{
		return m_Vencimiento;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

        public byte getID_Concepto()
        {
                return m_ID_Concepto;
        }

        public String getDescripcion()
        {
                return m_Descripcion;
        }

		public void setID_TipoCP(String ID_TipoCP) 
		{
			m_ID_TipoCP = ID_TipoCP;
		}

		public String getID_TipoCP() 
		{
			return m_ID_TipoCP;
		}

		public void setID_Aplicacion(long ID_Aplicacion) 
		{
			m_ID_Aplicacion = ID_Aplicacion;
		}

		public long getID_Aplicacion() 
		{
			return m_ID_Aplicacion;
		}

		public void setMonedaSim(String MonedaSim) 
		{
			m_MonedaSim = MonedaSim;	
		}
		
		public String getMonedaSim() 
		{
			return m_MonedaSim;	
		}

		public void setID_PagoBanCaj(long ID_PagoBanCaj) 
		{
			m_ID_PagoBanCaj = ID_PagoBanCaj;
		}
		
		public long getID_PagoBanCaj() 
		{
			return m_ID_PagoBanCaj;
		}

		public void setPagoBanCaj(String PagoBanCaj) 
		{
			m_PagoBanCaj = PagoBanCaj;
		}

		public String getPagoBanCaj() 
		{
			return m_PagoBanCaj;
		}

		public void setTotalPesos(float TotalPesos) 
		{
			m_TotalPesos = TotalPesos;
		}

		public float getTotalPesos() 
		{
			return m_TotalPesos;
		}

		public void setSaldoPesos(float SaldoPesos) 
		{
			m_SaldoPesos = SaldoPesos;
		}

		public float getSaldoPesos() 
		{
			return m_SaldoPesos;
		}

}

