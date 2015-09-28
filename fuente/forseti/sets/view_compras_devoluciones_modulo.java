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

public class view_compras_devoluciones_modulo 
{
	private String m_Proveedor;
	private Date m_Fecha;
	private long m_ID_Proveedor;
	private int m_ID_Entidad;
	private long m_ID_Devolucion;
	private long m_ID_Pol;
	private byte m_Moneda;
	private long m_Numero;
	private String m_Referencia;
	private String m_Status;
	private float m_TC;
	private float m_Total;
	private int m_Factura;
	private long m_ID_CFD;
	private byte m_TFD;
	private String m_Simbolo;
	private String m_DevReb;
	private long m_ID_PolCost;
	private String m_Ref;
	
	public byte getTFD() 
	{
		return m_TFD;
	}

	public void setTFD(byte TFD) 
	{
		m_TFD = TFD;
	}

	public int getFactura() 
	{
		return m_Factura;
	}

	public void setFactura(int Factura) 
	{
		m_Factura = Factura;
	}

	public void setProveedor(String Proveedor)
	{
		m_Proveedor = Proveedor;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Proveedor(long ID_Proveedor)
	{
		m_ID_Proveedor = ID_Proveedor;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Devolucion(long ID_Devolucion)
	{
		m_ID_Devolucion = ID_Devolucion;
	}

	public void setID_Pol(long ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setID_PolCost(long ID_PolCost)
	{
		m_ID_PolCost = ID_PolCost;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
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


	public String getProveedor()
	{
		return m_Proveedor;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public long getID_Proveedor()
	{
		return m_ID_Proveedor;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public long getID_Devolucion()
	{
		return m_ID_Devolucion;
	}

	public long getID_Pol()
	{
		return m_ID_Pol;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public long getID_PolCost()
	{
		return m_ID_PolCost;
	}

	public String getReferencia()
	{
		return m_Referencia;
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

	public void setID_CFD(long ID_CFD) 
	{
		m_ID_CFD = ID_CFD;	
	}

	public long getID_CFD() 
	{
		return m_ID_CFD;
	}
	
	public void setSimbolo(String Simbolo) 
	{
		m_Simbolo = Simbolo;
	}

	public String getSimbolo() 
	{
		return m_Simbolo;
	}

	public void setDevReb(String DevReb) 
	{
		m_DevReb = DevReb;	
	}

	public String getDevReb() 
	{
		return m_DevReb;	
	}

	public void setRef(String Ref) 
	{
		m_Ref = Ref;
	}
	
	public String getRef() 
	{
		return m_Ref;
	}

}
