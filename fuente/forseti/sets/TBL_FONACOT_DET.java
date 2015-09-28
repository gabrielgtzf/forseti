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

public class TBL_FONACOT_DET
{
	private String m_ID_Credito;
	private Date m_Fecha;
	private float m_Descuento;

	public void setID_Credito(String ID_Credito)
	{
		m_ID_Credito = ID_Credito;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}


	public String getID_Credito()
	{
		return m_ID_Credito;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}


}

