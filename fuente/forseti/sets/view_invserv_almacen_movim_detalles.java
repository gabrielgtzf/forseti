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

public class view_invserv_almacen_movim_detalles
{
	private float m_CP;
	private String m_Descripcion;
	private float m_Entrada;
	private long m_ID_Costo;
	private long m_ID_Movimiento;
	private String m_ID_Prod;
	private float m_Salida;
	private float m_UC;
	private String m_Unidad;
	private float m_Debe;
	private float m_Haber;

	public void setCP(float CP)
	{
		m_CP = CP;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setEntrada(float Entrada)
	{
		m_Entrada = Entrada;
	}

	public void setID_Costo(long ID_Costo)
	{
		m_ID_Costo = ID_Costo;
	}

	public void setID_Movimiento(long ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setSalida(float Salida)
	{
		m_Salida = Salida;
	}

	public void setUC(float UC)
	{
		m_UC = UC;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setDebe(float Debe)
	{
		m_Debe = Debe;
	}

	public void setHaber(float Haber)
	{
		m_Haber = Haber;
	}


	public float getCP()
	{
		return m_CP;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getEntrada()
	{
		return m_Entrada;
	}

	public long getID_Costo()
	{
		return m_ID_Costo;
	}

	public long getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public float getSalida()
	{
		return m_Salida;
	}

	public float getUC()
	{
		return m_UC;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getDebe()
	{
		return m_Debe;
	}

	public float getHaber()
	{
		return m_Haber;
	}


}

