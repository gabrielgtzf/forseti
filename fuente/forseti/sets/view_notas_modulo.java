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

public class view_notas_modulo
{
	private short m_ID_Block;
	private String m_ID_Nota;
	private short m_ID_Nivel;
	private Date m_Fecha;
	private Time m_Hora;
	private String m_Mensaje;
	private String m_ID_Usuario;
	private String m_Nombre;
	private String m_Etiqueta;
	
	public Time getHora() 
	{
		return m_Hora;
	}

	public void setHora(Time Hora) 
	{
		m_Hora = Hora;
	}

	public void setID_Block(short ID_Block)
	{
		m_ID_Block = ID_Block;
	}

	public void setID_Nota(String ID_Nota)
	{
		m_ID_Nota = ID_Nota;
	}

	public void setID_Nivel(short ID_Nivel)
	{
		m_ID_Nivel = ID_Nivel;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setMensaje(String Mensaje)
	{
		m_Mensaje = Mensaje;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public short getID_Block()
	{
		return m_ID_Block;
	}

	public String getID_Nota()
	{
		return m_ID_Nota;
	}

	public short getID_Nivel()
	{
		return m_ID_Nivel;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getMensaje()
	{
		return m_Mensaje;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public void setEtiqueta(String Etiqueta) 
	{
		m_Etiqueta = Etiqueta;		
	}

	public String getEtiqueta()
	{
		return m_Etiqueta;
	}
}

