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

public class view_gastos_plantillas_temporizador
{
	private int m_ID_Plantilla;
	private int m_Numero;
	private byte m_ID_Entidad;
	private byte m_Tipo;
	private String m_TipoDesc;
	private String m_Descripcion;
	private byte m_Temporizador;
	private String m_TempDesc;
	private Date m_FechaIni;
	private Date m_FechaApl;
	private String m_ID_Nota;
	private float m_Total;

	public void setID_Plantilla(int ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setNumero(int Numero)
	{
		m_Numero = Numero;
	}

	public void setID_Entidad(byte ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setTipoDesc(String TipoDesc)
	{
		m_TipoDesc = TipoDesc;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setTemporizador(byte Temporizador)
	{
		m_Temporizador = Temporizador;
	}

	public void setTempDesc(String TempDesc)
	{
		m_TempDesc = TempDesc;
	}

	public void setFechaIni(Date FechaIni)
	{
		m_FechaIni = FechaIni;
	}

	public void setFechaApl(Date FechaApl)
	{
		m_FechaApl = FechaApl;
	}

	public void setID_Nota(String ID_Nota)
	{
		m_ID_Nota = ID_Nota;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}


	public int getID_Plantilla()
	{
		return m_ID_Plantilla;
	}

	public int getNumero()
	{
		return m_Numero;
	}

	public byte getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getTipoDesc()
	{
		return m_TipoDesc;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public byte getTemporizador()
	{
		return m_Temporizador;
	}

	public String getTempDesc()
	{
		return m_TempDesc;
	}

	public Date getFechaIni()
	{
		return m_FechaIni;
	}

	public Date getFechaApl()
	{
		return m_FechaApl;
	}

	public String getID_Nota()
	{
		return m_ID_Nota;
	}

	public float getTotal()
	{
		return m_Total;
	}


}

