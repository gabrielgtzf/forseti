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

public class JBancosSetMovsV2 extends JManejadorSet
{
	public JBancosSetMovsV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_bancos_movimientos_modulo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_bancos_movimientos_modulo getRow(int row)
	{
		return (view_bancos_movimientos_modulo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_bancos_movimientos_modulo getAbsRow(int row)
	{
		return (view_bancos_movimientos_modulo)m_Rows.elementAt(row);
	}

	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_bancos_movimientos_modulo pNode = new view_bancos_movimientos_modulo();

			pNode.setBeneficiario(m_RS.getString("Beneficiario"));
			pNode.setConcepto(m_RS.getString("Concepto"));
			pNode.setDeposito(m_RS.getDouble("Deposito"));
			pNode.setDoc(m_RS.getString("Doc"));
			pNode.setEstatus(m_RS.getString("Estatus"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setID(m_RS.getInt("ID"));
			pNode.setMC(m_RS.getBoolean("MC"));
			pNode.setNum(m_RS.getInt("Num"));
			pNode.setPol(m_RS.getString("Pol"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setPol_ID(m_RS.getLong("Pol_ID"));
			pNode.setRetiro(m_RS.getDouble("Retiro"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setClave(m_RS.getByte("Clave"));
			pNode.setEsTrans(m_RS.getBoolean("EsTrans"));
			pNode.setSaldo(m_RS.getDouble("Saldo"));
			pNode.setID_Moneda(m_RS.getByte("ID_Moneda"));
			pNode.setTC(m_RS.getDouble("TC"));
			pNode.setTipoMov(m_RS.getString("TipoMov"));
			pNode.setReferencia(m_RS.getString("Referencia"));
			pNode.setID_SatBanco(m_RS.getString("ID_SatBanco"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setID_SatMetodosPago(m_RS.getString("ID_SatMetodosPago")); 
			pNode.setDescMetodosPago(m_RS.getString("DescMetodosPago")); 
			pNode.setCuentaBanco(m_RS.getString("CuentaBanco")); 
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
