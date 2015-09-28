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

public class JAdmBancosCuentasSet extends JManejadorSet
{
	public JAdmBancosCuentasSet(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_BANCOS_CUENTAS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_BANCOS_CUENTAS getRow(int row)
	{
		return (TBL_BANCOS_CUENTAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_BANCOS_CUENTAS getAbsRow(int row)
	{
		return (TBL_BANCOS_CUENTAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_BANCOS_CUENTAS pNode = new TBL_BANCOS_CUENTAS();

			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getShort("Clave"));
			pNode.setCuenta(m_RS.getString("Cuenta"));
			pNode.setSigCheque(m_RS.getInt("SigCheque"));
			pNode.setFmt_Dep(m_RS.getString("Fmt_Dep"));
			pNode.setFmt_Ret(m_RS.getString("Fmt_Ret"));
			pNode.setCC(m_RS.getString("CC"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setSaldo(m_RS.getFloat("Saldo"));
			pNode.setFijo(m_RS.getBoolean("Fijo"));
			pNode.setTipoTRASP(m_RS.getByte("TipoTRASP"));
			pNode.setClaveTRASP(m_RS.getShort("ClaveTRASP")); 
			pNode.setTodoTRASP(m_RS.getBoolean("TodoTRASP"));
			pNode.setUltimoNumTRASP(m_RS.getLong("UltimoNumTRASP"));
			pNode.setFondoTRASP(m_RS.getFloat("FondoTRASP"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setID_SatBanco(m_RS.getString("ID_SatBanco"));
			pNode.setBanco(m_RS.getString("Banco"));
			pNode.setBancoExt(m_RS.getString("BancoExt"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
