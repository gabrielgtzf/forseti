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

import java.sql.*;

public class view_invserv_almacen_movim_detalles_utensilios_modulo
{
	private long m_ID_Costo;
	private long m_ID_Movimiento;
	private int m_Num;
	private short m_ID_Concepto;
	private String m_Tipo;
	private Date m_Fecha;
	private String m_Concepto;
	private String m_Status;
	private String m_Ref;
	private short m_ID_Bodega;
	private String m_ID_Prod;
	private String m_Descripcion;
	private float m_Entrada;
	private float m_Salida;
	private String m_Unidad;

	public void setID_Costo(long ID_Costo)
	{
		m_ID_Costo = ID_Costo;
	}

	public void setID_Movimiento(long ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setNum(int Num)
	{
		m_Num = Num;
	}

	public void setID_Concepto(short ID_Concepto)
	{
		m_ID_Concepto = ID_Concepto;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setID_Bodega(short ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setEntrada(float Entrada)
	{
		m_Entrada = Entrada;
	}

	public void setSalida(float Salida)
	{
		m_Salida = Salida;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public long getID_Costo()
	{
		return m_ID_Costo;
	}

	public long getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public int getNum()
	{
		return m_Num;
	}

	public short getID_Concepto()
	{
		return m_ID_Concepto;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public short getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getEntrada()
	{
		return m_Entrada;
	}

	public float getSalida()
	{
		return m_Salida;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}


}

