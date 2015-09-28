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

public class JProveeProveeMasSetV2 extends JManejadorSet
{
	public JProveeProveeMasSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_provee_provee_mas ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_provee_provee_mas getRow(int row)
	{
		return (view_provee_provee_mas)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_provee_provee_mas getAbsRow(int row)
	{
		return (view_provee_provee_mas)m_Rows.elementAt(row);
	}
	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_provee_provee_mas pNode = new view_provee_provee_mas();

			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setID_Clave(m_RS.getInt("ID_Clave"));
			pNode.setID_EntidadCompra(m_RS.getInt("ID_EntidadCompra"));
			pNode.setEntidadNombre(m_RS.getString("EntidadNombre"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setAtnPagos(m_RS.getString("AtnPagos"));
			pNode.setColonia(m_RS.getString("Colonia"));
			pNode.setCP(m_RS.getString("CP"));
			pNode.setDireccion(m_RS.getString("Direccion"));
			pNode.setFax(m_RS.getString("Fax"));
			pNode.setPoblacion(m_RS.getString("Poblacion"));
			pNode.setCompraAnual(m_RS.getFloat("CompraAnual"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setLimiteCredito(m_RS.getFloat("LimiteCredito"));
			pNode.setUltimaCompra(m_RS.getDate("UltimaCompra"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setPrecioEspMostr(m_RS.getBoolean("PrecioEspMostr"));
			pNode.setCuentaNombre(m_RS.getString("CuentaNombre"));
			pNode.setID_Vendedor(m_RS.getInt("ID_Vendedor"));
			pNode.setVendedorNombre(m_RS.getString("VendedorNombre"));
			pNode.setNoExt(m_RS.getString("NoExt"));
			pNode.setNoInt(m_RS.getString("NoInt"));
			pNode.setMunicipio(m_RS.getString("Municipio"));
			pNode.setEstado(m_RS.getString("Estado"));
			pNode.setPais(m_RS.getString("Pais"));
			pNode.setMetodoDePago(m_RS.getString("MetodoDePago"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setID_SatBanco(m_RS.getString("ID_SatBanco"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
