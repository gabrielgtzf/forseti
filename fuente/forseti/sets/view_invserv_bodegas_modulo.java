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


public class view_invserv_bodegas_modulo
{
	private String m_Bodega;
	private String m_Clave;
	private String m_Descripcion;
	private float m_Existencia;
	private String m_Unidad;
	private byte m_ID_Bodega;
	private float m_StockMax;
	private float m_StockMin;
	private String m_Status;
	
	public void setBodega(String Bodega)
	{
		m_Bodega = Bodega;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setExistencia(float Existencia)
	{
		m_Existencia = Existencia;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setStockMax(float StockMax)
	{
		m_StockMax = StockMax;
	}

	public void setStockMin(float StockMin)
	{
		m_StockMin = StockMin;
	}


	public String getBodega()
	{
		return m_Bodega;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getExistencia()
	{
		return m_Existencia;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public float getStockMax()
	{
		return m_StockMax;
	}

	public float getStockMin()
	{
		return m_StockMin;
	}

	public String getStatus() 
	{
		return m_Status;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}


}

