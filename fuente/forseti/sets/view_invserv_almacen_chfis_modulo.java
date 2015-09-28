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
import java.sql.Date;

public class view_invserv_almacen_chfis_modulo
{
	private int m_Chequeo;
	private Date m_Fecha;
	private String m_Status;
	private boolean m_Cerrado;
	private boolean m_Generado;
	private int m_ID_Bodega;
	private int m_ID_CHFIS;
	private String m_Nombre;
	
	public void setChequeo(int Chequeo)
	{
		m_Chequeo = Chequeo;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setCerrado(boolean Cerrado)
	{
		m_Cerrado = Cerrado;
	}

	public void setGenerado(boolean Generado)
	{
		m_Generado = Generado;
	}

	public int getChequeo()
	{
		return m_Chequeo;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public boolean getCerrado()
	{
		return m_Cerrado;
	}

	public boolean getGenerado()
	{
		return m_Generado;
	}

	public String getStatus() 
	{
		return m_Status;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}

	public void setID_CHFIS(int ID_CHFIS) 
	{
		m_ID_CHFIS = ID_CHFIS;	
	}
	
	public int getID_CHFIS()
	{
		return m_ID_CHFIS;
	}

	public void setNombre(String Nombre) 
	{
		m_Nombre = Nombre;
	}
	
	public String getNombre()
	{
		return m_Nombre;
	}
}

