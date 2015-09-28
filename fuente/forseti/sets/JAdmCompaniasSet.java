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

public class JAdmCompaniasSet extends JManejadorSet
{
	public JAdmCompaniasSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_COMPANIAS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_COMPANIAS getRow(int row)
	{
		return (TBL_COMPANIAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_COMPANIAS getAbsRow(int row)
	{
		return (TBL_COMPANIAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_COMPANIAS pNode = new TBL_COMPANIAS();

			pNode.setID_Compania(m_RS.getByte("ID_Compania"));
			pNode.setID_Sucursal(m_RS.getByte("ID_Sucursal"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setTipo(m_RS.getByte("Tipo"));
			pNode.setPeriodo(m_RS.getString("Periodo"));
			pNode.setFmt_Nomina(m_RS.getString("Fmt_Nomina"));
			pNode.setFmt_Recibo(m_RS.getString("Fmt_Recibo"));
			pNode.setNumero(m_RS.getByte("Numero"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setContCuenTipo(m_RS.getInt("ContCuenTipo"));
			pNode.setContCuenClave(m_RS.getInt("ContCuenClave"));
			pNode.setFijaCuenTipo(m_RS.getInt("FijaCuenTipo"));
			pNode.setFijaCuenClave(m_RS.getInt("FijaCuenClave"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setCFD(m_RS.getString("CFD"));
			pNode.setCFD_NoCertificado(m_RS.getString("CFD_NoCertificado"));
			pNode.setCFD_ID_Expedicion(m_RS.getByte("CFD_ID_Expedicion"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}
}
