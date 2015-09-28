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

public class JPlantillasSetCons extends JManejadorSet
{
	public  JPlantillasSetCons(HttpServletRequest request)
	{
		m_Select = " * FROM view_nom_plantillas_cons";
		m_PageSize = 50;
		this.request = request;
	}

	public view_plantillas_cons getRow(int row)
	{
		return (view_plantillas_cons)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_plantillas_cons getAbsRow(int row)
	{
		return (view_plantillas_cons)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_plantillas_cons pNode = new view_plantillas_cons();

			pNode.setID_Plantilla(m_RS.getInt("ID_Plantilla"));
			pNode.setID_Movimiento(m_RS.getInt("ID_Movimiento"));
			pNode.setMovimiento(m_RS.getString("Movimiento"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setbID_Empleado(m_RS.getBoolean("bID_Empleado"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setEmpleado(m_RS.getString("Empleado"));
			pNode.setbNomina(m_RS.getBoolean("bNomina"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setNumero_Nomina(m_RS.getInt("Numero_Nomina"));
			pNode.setbTipo_Nomina(m_RS.getBoolean("bTipo_Nomina"));
			pNode.setTipo_de_Nomina(m_RS.getInt("Tipo_de_Nomina"));
			pNode.setbCompania_Sucursal(m_RS.getBoolean("bCompania_Sucursal"));
			pNode.setsCompania_Sucursal(m_RS.getString("sCompania_Sucursal"));
			pNode.setbNivel_Confianza(m_RS.getBoolean("bNivel_Confianza"));
			pNode.setNivel_de_Confianza(m_RS.getInt("Nivel_de_Confianza"));
			pNode.setAplicacion(m_RS.getByte("Aplicacion"));
			pNode.setHoras(m_RS.getFloat("Horas"));
			pNode.setDias(m_RS.getFloat("Dias"));
			pNode.setVeces_Importe(m_RS.getFloat("Veces_Importe"));
			pNode.setImporte(m_RS.getFloat("Importe"));
			pNode.setbExento(m_RS.getBoolean("bExento"));
			pNode.setExento(m_RS.getFloat("Exento"));
			pNode.setMixto(m_RS.getBoolean("Mixto"));
			pNode.setInclusiones(m_RS.getBoolean("Inclusiones"));
			pNode.setCantidad(m_RS.getFloat("Cantidad"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
