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



public class view_invserv_almacen_chfis_det
{
	private int m_ID_CHFIS;
	private String m_ID_Prod;
	private String m_Descripcion;
	private float m_Cantidad;
	private String m_Unidad;
	private float m_Diff;
	private String m_Status;
	private byte m_ID_Bodega;
	private float m_StockMin;
	private float m_StockMax;

	public void setID_CHFIS(int ID_CHFIS)
	{
		m_ID_CHFIS = ID_CHFIS;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setDiff(float Diff)
	{
		m_Diff = Diff;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setStockMin(float StockMin)
	{
		m_StockMin = StockMin;
	}

	public void setStockMax(float StockMax)
	{
		m_StockMax = StockMax;
	}


	public int getID_CHFIS()
	{
		return m_ID_CHFIS;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getDiff()
	{
		return m_Diff;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public float getStockMin()
	{
		return m_StockMin;
	}

	public float getStockMax()
	{
		return m_StockMax;
	}


}

