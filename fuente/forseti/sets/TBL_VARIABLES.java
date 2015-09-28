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

public class TBL_VARIABLES
{
	private String m_ID_Variable;
	private String m_Descripcion;
	private String m_Tipo;
	private int m_VEntero;
	private float m_VDecimal;
	private Date m_VFecha;
	private Time m_Hora;
	private String m_VAlfanumerico;
	private byte m_DeSistema;
	private String m_Modulo;
	
	public void setID_Variable(String ID_Variable)
	{
		m_ID_Variable = ID_Variable;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setVEntero(int VEntero)
	{
		m_VEntero = VEntero;
	}

	public void setVDecimal(float VDecimal)
	{
		m_VDecimal = VDecimal;
	}

	public void setVFecha(Date VFecha)
	{
		m_VFecha = VFecha;
	}

	public void setVAlfanumerico(String VAlfanumerico)
	{
		m_VAlfanumerico = VAlfanumerico;
	}

	public void setDeSistema(byte DeSistema)
	{
		m_DeSistema = DeSistema;
	}


	public String getID_Variable()
	{
		return m_ID_Variable;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public int getVEntero()
	{
		return m_VEntero;
	}

	public float getVDecimal()
	{
		return m_VDecimal;
	}

	public Date getVFecha()
	{
		return m_VFecha;
	}

	public String getVAlfanumerico()
	{
		return m_VAlfanumerico;
	}

	public byte getDeSistema()
	{
		return m_DeSistema;
	}

	public void setTipo(String Tipo) 
	{
		m_Tipo = Tipo;	
	}

	public void setModulo(String Modulo) 
	{
		m_Modulo = Modulo;	
	}

	public String getTipo() 
	{
		return m_Tipo;	
	}

	public String getModulo() 
	{
		return m_Modulo;	
	}

	public void setVHora(Time Hora) 
	{
		m_Hora = Hora;
	}

	public Time getVHora() 
	{
		return m_Hora;
	}

}

