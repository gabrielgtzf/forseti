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


public class view_invserv_arma_kits_promedios
{
	private String m_Clave;
	private String m_ID_Tipo;
	private String m_Descripcion;
	private float m_UltimoCosto;
	private float m_CostoPromedio;
	private float m_Precio;
	private byte m_TipoCosteo;
	private String m_Unidad;
	private boolean m_SeProduce;
	private String m_Status;

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setUltimoCosto(float UltimoCosto)
	{
		m_UltimoCosto = UltimoCosto;
	}

	public void setCostoPromedio(float CostoPromedio)
	{
		m_CostoPromedio = CostoPromedio;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setTipoCosteo(byte TipoCosteo)
	{
		m_TipoCosteo = TipoCosteo;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setSeProduce(boolean SeProduce)
	{
		m_SeProduce = SeProduce;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}


	public String getClave()
	{
		return m_Clave;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getUltimoCosto()
	{
		return m_UltimoCosto;
	}

	public float getCostoPromedio()
	{
		return m_CostoPromedio;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public byte getTipoCosteo()
	{
		return m_TipoCosteo;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public boolean getSeProduce()
	{
		return m_SeProduce;
	}

	public String getStatus()
	{
		return m_Status;
	}


}

