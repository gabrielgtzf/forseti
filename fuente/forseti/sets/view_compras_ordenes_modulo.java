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

public class view_compras_ordenes_modulo
{
	private String m_Proveedor;
	private Date m_Fecha;
	private long m_ID_Proveedor;
	private int m_ID_Entidad;
	private long m_ID_Orden;
	private byte m_Moneda;
	private long m_Numero;
	private String m_Referencia;
	private String m_Status;
	private float m_TC;
	private float m_Total;
	private int m_Recepcion;
	private String m_Simbolo;
	private int m_Factura;
	private String m_TipoEnlace;
	private long m_ID_Factura;
	private String m_Ref;

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

	public void setID_Orden(long ID_Orden)
	{
		m_ID_Orden = ID_Orden;
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

	public void setRecepcion(int Recepcion)
	{
		m_Recepcion = Recepcion;
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

	public long getID_Orden()
	{
		return m_ID_Orden;
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

	public int getRecepcion()
	{
		return m_Recepcion;
	}

	public void setSimbolo(String Simbolo) 
	{
		m_Simbolo = Simbolo;
	}

	public void setFactura(int Factura) 
	{
		m_Factura = Factura;
	}

	public void setTipoEnlace(String TipoEnlace) 
	{
		m_TipoEnlace = TipoEnlace;
	}

	public void setID_Factura(long ID_Factura) 
	{
		m_ID_Factura = ID_Factura;
	}

	public void setRef(String Ref) 
	{
		m_Ref = Ref;
	}

	public String getSimbolo() 
	{
		return m_Simbolo;
	}

	public int getFactura() 
	{
		return m_Factura;
	}

	public String getTipoEnlace() 
	{
		return m_TipoEnlace;
	}

	public long getID_Factura() 
	{
		return m_ID_Factura;
	}

	public String getRef() 
	{
		return m_Ref;
	}
}

