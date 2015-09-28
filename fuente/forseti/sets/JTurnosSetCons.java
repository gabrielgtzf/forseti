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

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JTurnosSetCons extends JManejadorSet
{
	public JTurnosSetCons(HttpServletRequest request)
	{
		m_Select = " * FROM view_nom_turnos";
		m_PageSize = 50;
		this.request = request;
	}

	public view_turnos getRow(int row)
	{
		return (view_turnos)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_turnos getAbsRow(int row)
	{
		return (view_turnos)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_turnos pNode = new view_turnos();

			pNode.setID_Turno(m_RS.getByte("ID_Turno"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setLunes(m_RS.getBoolean("Lunes"));
			pNode.setELunes(m_RS.getTime("ELunes"));
			pNode.setSLunes(m_RS.getTime("SLunes"));
			pNode.setMartes(m_RS.getBoolean("Martes"));
			pNode.setEMartes(m_RS.getTime("EMartes"));
			pNode.setSMartes(m_RS.getTime("SMartes"));
			pNode.setMiercoles(m_RS.getBoolean("Miercoles"));
			pNode.setEMiercoles(m_RS.getTime("EMiercoles"));
			pNode.setSMiercoles(m_RS.getTime("SMiercoles"));
			pNode.setJueves(m_RS.getBoolean("Jueves"));
			pNode.setEJueves(m_RS.getTime("EJueves"));
			pNode.setSJueves(m_RS.getTime("SJueves"));
			pNode.setViernes(m_RS.getBoolean("Viernes"));
			pNode.setEViernes(m_RS.getTime("EViernes"));
			pNode.setSViernes(m_RS.getTime("SViernes"));
			pNode.setSabado(m_RS.getBoolean("Sabado"));
			pNode.setESabado(m_RS.getTime("ESabado"));
			pNode.setSSabado(m_RS.getTime("SSabado"));
			pNode.setDomingo(m_RS.getBoolean("Domingo"));
			pNode.setEDomingo(m_RS.getTime("EDomingo"));
			pNode.setSDomingo(m_RS.getTime("SDomingo"));
			pNode.setHNALunes(m_RS.getFloat("HNALunes"));
			pNode.setHEALunes(m_RS.getFloat("HEALunes"));
			pNode.setHNAMartes(m_RS.getFloat("HNAMartes"));
			pNode.setHEAMartes(m_RS.getFloat("HEAMartes"));
			pNode.setHNAMiercoles(m_RS.getFloat("HNAMiercoles"));
			pNode.setHEAMiercoles(m_RS.getFloat("HEAMiercoles"));
			pNode.setHNAJueves(m_RS.getFloat("HNAJueves"));
			pNode.setHEAJueves(m_RS.getFloat("HEAJueves"));
			pNode.setHNAViernes(m_RS.getFloat("HNAViernes"));
			pNode.setHEAViernes(m_RS.getFloat("HEAViernes"));
			pNode.setHNASabado(m_RS.getFloat("HNASabado"));
			pNode.setHEASabado(m_RS.getFloat("HEASabado"));
			pNode.setHNADomingo(m_RS.getFloat("HNADomingo"));
			pNode.setHEADomingo(m_RS.getFloat("HEADomingo"));
			pNode.setTTLun(m_RS.getByte("TTLun"));
			pNode.setTTMar(m_RS.getByte("TTMar"));
			pNode.setTTMie(m_RS.getByte("TTMie"));
			pNode.setTTJue(m_RS.getByte("TTJue"));
			pNode.setTTVie(m_RS.getByte("TTVie"));
			pNode.setTTSab(m_RS.getByte("TTSab"));
			pNode.setTTDom(m_RS.getByte("TTDom"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
