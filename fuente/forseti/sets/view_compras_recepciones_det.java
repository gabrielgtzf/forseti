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


public class view_compras_recepciones_det
{
	private long m_ID_Recepcion;
	private byte m_Partida;
	private float m_Cantidad;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_ID_Unidad;
	private float m_Precio;
	private float m_Descuento;
	private float m_IVA;
	private String m_Obs;
	private float m_Importe;
	private String m_ID_Tipo;
	
	public void setID_Recepcion(long ID_Recepcion)
	{
		m_ID_Recepcion = ID_Recepcion;
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

	public void setID_Unidad(String ID_Unidad)
	{
		m_ID_Unidad = ID_Unidad;
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


	public long getID_Recepcion()
	{
		return m_ID_Recepcion;
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

	public String getID_Unidad()
	{
		return m_ID_Unidad;
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

	public void setID_Tipo(String ID_Tipo) 
	{
		m_ID_Tipo = ID_Tipo;
	}


}

