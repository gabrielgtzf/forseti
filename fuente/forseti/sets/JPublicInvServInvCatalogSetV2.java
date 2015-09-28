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

public class JPublicInvServInvCatalogSetV2 extends JManejadorSet
{
	public JPublicInvServInvCatalogSetV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_public_invserv_inventarios_catalogo ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_public_invserv_inventarios_catalogo getRow(int row)
	{
		return (view_public_invserv_inventarios_catalogo)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_public_invserv_inventarios_catalogo getAbsRow(int row)
	{
		return (view_public_invserv_inventarios_catalogo)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_public_invserv_inventarios_catalogo pNode = new view_public_invserv_inventarios_catalogo();

			pNode.setClave(m_RS.getString("Clave"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setLinea(m_RS.getString("Linea"));
			pNode.setExistencia(m_RS.getFloat("Existencia"));
			pNode.setID_UnidadSalida(m_RS.getString("ID_UnidadSalida"));
			pNode.setIVA(m_RS.getBoolean("IVA")); 
			pNode.setPrecio(m_RS.getFloat("Precio"));
			pNode.setUltimoCosto(m_RS.getFloat("UltimoCosto"));
			pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
			pNode.setID_Unidad(m_RS.getString("ID_Unidad"));
			pNode.setImpIEPS(m_RS.getFloat("ImpIEPS"));
			pNode.setImpIVARet(m_RS.getFloat("ImpIVARet"));
			pNode.setImpISRRet(m_RS.getFloat("ImpISRRet"));
			pNode.setSeProduce(m_RS.getBoolean("SeProduce"));
			pNode.setCostoPromedio(m_RS.getFloat("CostoPromedio"));
			pNode.setPrecio2(m_RS.getFloat("Precio2"));
			pNode.setPrecio3(m_RS.getFloat("Precio3"));
			pNode.setPrecio4(m_RS.getFloat("Precio4"));
			pNode.setPrecio5(m_RS.getFloat("Precio5"));
			pNode.setPrecioMin(m_RS.getFloat("PrecioMin"));
			pNode.setPrecioMax(m_RS.getFloat("PrecioMax"));
			pNode.setPrecioOfertaWeb(m_RS.getFloat("PrecioOfertaWeb"));
			pNode.setPrecioWeb(m_RS.getFloat("PrecioWeb"));
			pNode.setStatus(m_RS.getString("Status"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
