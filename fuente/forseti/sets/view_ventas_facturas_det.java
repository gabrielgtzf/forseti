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

public class view_ventas_facturas_det
{
	private long m_ID_Factura;
	private byte m_Partida;
	private float m_Cantidad;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_ID_UnidadSalida;
	private float m_Precio;
	private float m_Descuento;
	private float m_IVA;
	private String m_Obs;
	private float m_Importe;
	private String m_ID_Tipo;
	private float m_TotalPart;
	private float m_ImporteDesc;
	private float m_ImporteIVA;
	private float m_ImporteISRRet;
	private float m_ImporteIVARet;
	private float m_ImporteIEPS;
	private float m_IEPS;
	private float m_ISRRet;
	private float m_IVARet;

	public void setID_Factura(long ID_Factura)
	{
		m_ID_Factura = ID_Factura;
	}

	public void setPartida(byte Partida)
	{
		m_Partida = Partida;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_UnidadSalida(String ID_UnidadSalida)
	{
		m_ID_UnidadSalida = ID_UnidadSalida;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setImporte(float Importe)
	{
		m_Importe = Importe;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}


	public long getID_Factura()
	{
		return m_ID_Factura;
	}

	public byte getPartida()
	{
		return m_Partida;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getID_UnidadSalida()
	{
		return m_ID_UnidadSalida;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public String getObs()
	{
		return m_Obs;
	}

	public float getImporte()
	{
		return m_Importe;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public void setTotalPart(float TotalPart) 
	{
		m_TotalPart = TotalPart;
	}

	public float getTotalPart() 
	{
		return m_TotalPart;
	}

	public float getImporteDesc()
	{
		return m_ImporteDesc;
	}
	
	public float getImporteIVA()
	{
		return m_ImporteIVA;
	}
	
	public void setImporteDesc(float ImporteDesc)
	{
		m_ImporteDesc = ImporteDesc;
	}
	
	public void setImporteIVA(float ImporteIVA)
	{
		m_ImporteIVA = ImporteIVA;
	}

	public float getIVARet() 
	{
		return m_IVARet;
	}

	public float getISRRet() 
	{
		return m_ISRRet;
	}

	public float getIEPS() 
	{
		return m_IEPS;
	}

	public float getImporteIEPS() 
	{
		return m_ImporteIEPS;
	}

	public float getImporteIVARet() 
	{
		return m_ImporteIVARet;
	}

	public float getImporteISRRet() 
	{
		return m_ImporteISRRet;
	}

	public void setIEPS(float IEPS) 
	{
		m_IEPS = IEPS;
	}

	public void setIVARet(float IVARet) 
	{
		m_IVARet = IVARet;	
	}

	public void setISRRet(float ISRRet) 
	{
		m_ISRRet = ISRRet;	
	}

	public void setImporteIEPS(float ImporteIEPS) 
	{
		m_ImporteIEPS = ImporteIEPS;	
	}

	public void setImporteIVARet(float ImporteIVARet) 
	{
		m_ImporteIVARet = ImporteIVARet;	
	}

	public void setImporteISRRet(float ImporteISRRet) 
	{
		m_ImporteISRRet = ImporteISRRet;
	}

}

