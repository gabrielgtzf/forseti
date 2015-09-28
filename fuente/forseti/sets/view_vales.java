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

public class view_vales
{
	private long m_ID_Vale;
	private String m_ID_Tipo;
	private Date m_Fecha;
	private String m_ID_Gasto;
	private String m_Descripcion;
	private String m_Concepto;
	private float m_Provisional;
	private float m_Final;
	private float m_Factura;
	private float m_Compra;
	private float m_Pago;
	private float m_Traspaso;
	private float m_Cantidad;
	private float m_Descuento;
	private float m_IVA;
	private float m_Total;
	private int m_ID_Clave;
	

	public void setID_Vale(long ID_Vale)
	{
		m_ID_Vale = ID_Vale;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public int getID_Clave() 
	{
		return m_ID_Clave;
	}

	public void setID_Clave(int ID_Clave) 
	{
		m_ID_Clave = ID_Clave;
	}

	public void setID_Gasto(String ID_Gasto)
	{
		m_ID_Gasto = ID_Gasto;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setProvisional(float Provisional)
	{
		m_Provisional = Provisional;
	}

	public void setFinal(float Final)
	{
		m_Final = Final;
	}

	public void setFactura(float Factura)
	{
		m_Factura = Factura;
	}

	public void setCompra(float Compra)
	{
		m_Compra = Compra;
	}

	public void setPago(float Pago)
	{
		m_Pago = Pago;
	}

	public void setTraspaso(float Traspaso)
	{
		m_Traspaso = Traspaso;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}


	public long getID_Vale()
	{
		return m_ID_Vale;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getID_Gasto()
	{
		return m_ID_Gasto;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public float getProvisional()
	{
		return m_Provisional;
	}

	public float getFinal()
	{
		return m_Final;
	}

	public float getFactura()
	{
		return m_Factura;
	}

	public float getCompra()
	{
		return m_Compra;
	}

	public float getPago()
	{
		return m_Pago;
	}

	public float getTraspaso()
	{
		return m_Traspaso;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public float getTotal()
	{
		return m_Total;
	}


}

