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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import forseti.JManejadorSet;

public class JCajasSetMovsV2 extends JManejadorSet
{
	public JCajasSetMovsV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_cajas_movimientos_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cajas_movimientos_modulo getRow(int row)
	{
		return (view_cajas_movimientos_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cajas_movimientos_modulo getAbsRow(int row)
	{
		return (view_cajas_movimientos_modulo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_cajas_movimientos_modulo pNode = new view_cajas_movimientos_modulo();

			pNode.setID(m_RS.getLong("ID"));
			pNode.setTipo_ID(m_RS.getByte("Tipo_ID"));
			pNode.setNum(m_RS.getInt("Num"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setDeposito(m_RS.getFloat("Deposito"));
			pNode.setRetiro(m_RS.getFloat("Retiro"));
			pNode.setEstatus(m_RS.getString("Estatus"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setPol_ID(m_RS.getLong("Pol_ID"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setMC(m_RS.getBoolean("MC"));
			pNode.setEsTrans(m_RS.getBoolean("EsTrans"));
			pNode.setDoc(m_RS.getString("Doc"));
			pNode.setSaldo(m_RS.getFloat("Saldo"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
