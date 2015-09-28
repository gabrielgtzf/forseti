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

public class view_ventas_remisiones_modulo
{
	private String m_Cliente;
	private Date m_Fecha;
	private long m_ID_Cliente;
	private int m_ID_Entidad;
	private long m_ID_Remision;
	private byte m_Moneda;
	private long m_Numero;
	private String m_Referencia;
	private String m_Status;
	private float m_TC;
	private float m_Total;
	private int m_Factura;
	private int	m_ID_Vendedor;
	private String m_VendedorNombre;
	private long m_ID_CFD;
	private long m_ID_Factura;
	private byte m_TFD;
	private String m_Simbolo;
	private String m_Ref;
	private long m_ID_PolCost;
	
	public byte getTFD() 
	{
		return m_TFD;
	}

	public void setTFD(byte TFD) 
	{
		m_TFD = TFD;
	}

	public long getID_CFD()
	{
		return m_ID_CFD;
	}
	
	public int getID_Vendedor() 
	{
		return m_ID_Vendedor;
	}

	public void setID_Vendedor(int ID_Vendedor) 
	{
		m_ID_Vendedor = ID_Vendedor;
	}

	public String getVendedorNombre() 
	{
		return m_VendedorNombre;
	}

	public void setVendedorNombre(String VendedorNombre) 
	{
		m_VendedorNombre = VendedorNombre;
	}
	
	public void setCliente(String Cliente)
	{
		m_Cliente = Cliente;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Cliente(long ID_Cliente)
	{
		m_ID_Cliente = ID_Cliente;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Remision(long ID_Remision)
	{
		m_ID_Remision = ID_Remision;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
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

	public void setFactura(int Factura)
	{
		m_Factura = Factura;
	}


	public String getCliente()
	{
		return m_Cliente;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public long getID_Cliente()
	{
		return m_ID_Cliente;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public long getID_Remision()
	{
		return m_ID_Remision;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public long getNumero()
	{
		return m_Numero;
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

	public int getFactura()
	{
		return m_Factura;
	}
	
	public void setID_CFD(long ID_CFD) 
	{
		m_ID_CFD = ID_CFD;
	}

	public void setID_Factura(long ID_Factura) 
	{
		m_ID_Factura = ID_Factura;
	}

	public long getID_Factura() 
	{
		return m_ID_Factura;
	}

	public void setSimbolo(String Simbolo) 
	{
		m_Simbolo = Simbolo;
	}

	public String getSimbolo() 
	{
		return m_Simbolo;
	}

	public String getRef() 
	{
		return m_Ref;
	}
	
	public void setRef(String Ref) 
	{
		m_Ref = Ref;
	}

	public void setID_PolCost(long ID_PolCost) 
	{
		m_ID_PolCost = ID_PolCost;
	}

	public long getID_PolCost() 
	{
		return m_ID_PolCost;
	}
}
