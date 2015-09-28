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

public class view_regproc_modulo
{
	private int m_ID_RegProc;
	private String m_ID_Tipo;
	private String m_FSIBD;
	private Date m_Fecha;
	private Time m_Hora;
	private String m_Status;
	private String m_ID_Usuario;
	private String m_ID_Proceso;
	private String m_Proceso;
	private String m_ID_Modulo;
	private String m_Resultado;

	public void setID_RegProc(int ID_RegProc)
	{
		m_ID_RegProc = ID_RegProc;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setFSIBD(String FSIBD)
	{
		m_FSIBD = FSIBD;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setHora(Time Hora)
	{
		m_Hora = Hora;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setID_Proceso(String ID_Proceso)
	{
		m_ID_Proceso = ID_Proceso;
	}

	public void setProceso(String Proceso)
	{
		m_Proceso = Proceso;
	}

	public void setID_Modulo(String ID_Modulo)
	{
		m_ID_Modulo = ID_Modulo;
	}

	public void setResultado(String Resultado)
	{
		m_Resultado = Resultado;
	}


	public int getID_RegProc()
	{
		return m_ID_RegProc;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getFSIBD()
	{
		return m_FSIBD;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public Time getHora()
	{
		return m_Hora;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public String getID_Proceso()
	{
		return m_ID_Proceso;
	}

	public String getProceso()
	{
		return m_Proceso;
	}

	public String getID_Modulo()
	{
		return m_ID_Modulo;
	}

	public String getResultado()
	{
		return m_Resultado;
	}


}

