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

public class JCalculoNominaCabSet extends JManejadorSet
{
	public  JCalculoNominaCabSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_NOM_CALCULO_NOMINA_CAB";
		m_PageSize = 50;
		this.request = request;
	}

	public VIEW_CALCULO_NOMINA_CAB getRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_CAB)m_Rows.elementAt((getFloorRow() + row));
	}

	public VIEW_CALCULO_NOMINA_CAB getAbsRow(int row)
	{
		return (VIEW_CALCULO_NOMINA_CAB)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			VIEW_CALCULO_NOMINA_CAB pNode = new VIEW_CALCULO_NOMINA_CAB();

			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setCompania(m_RS.getString("Compania"));
			pNode.setNombre_Compania(m_RS.getString("Nombre_Compania"));
			pNode.setDepartamento(m_RS.getString("Departamento"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setTipo_Nomina(m_RS.getString("Tipo_Nomina"));
			pNode.setNumero_Nomina(m_RS.getInt("Numero_Nomina"));
			pNode.setFecha_Desde(m_RS.getDate("Fecha_Desde"));
			pNode.setFecha_Hasta(m_RS.getDate("Fecha_Hasta"));
			pNode.setDias(m_RS.getFloat("Dias"));
			pNode.setFaltas(m_RS.getFloat("Faltas"));
			pNode.setRecibo(m_RS.getInt("Recibo"));
			pNode.setHE(m_RS.getFloat("HE"));
			pNode.setHD(m_RS.getFloat("HD"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setNum_Registro_IMSS(m_RS.getString("Num_Registro_IMSS"));
			pNode.setSalario_Nominal(m_RS.getFloat("Salario_Nominal"));
			pNode.setPago_Neto(m_RS.getFloat("Pago_Neto"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
