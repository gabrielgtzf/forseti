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

public class JComprasEntidadesSetIdsV2 extends JManejadorSet
{
	
	public JComprasEntidadesSetIdsV2(HttpServletRequest request, String usuario, String entidad)
	{
		m_Select = " * FROM view_compras_entidades_ids ";
		String sql = "select * from view_compras_entidades_ids('" + usuario + "','" + entidad +  
		"') as ( " +
		"id_usuario varchar, id_entidad smallint, ID_Tipo smallint, Serie varchar, Descripcion varchar, Doc int, Formato varchar, ID_Bodega smallint, " +
		" Bodega varchar, AuditarAlm bit, ManejoStocks smallint, Orden int, Devolucion int, IVA numeric, Fmt_Orden varchar, Fmt_Devolucion varchar, Fija bit, FijaCost bit, Recepcion int, Fmt_Recepcion varchar, TipoCobro smallint )";
		setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public JComprasEntidadesSetIdsV2(HttpServletRequest request, String usuario, String entidad, int tipoentidad)
	{
		m_Select = " * FROM view_compras_entidades_ids ";
		String sql = "select * from view_compras_entidades_ids('" + usuario + "','" + entidad + "','" + tipoentidad + 
		"') as ( " +
		"id_usuario varchar, id_entidad smallint, ID_Tipo smallint, Serie varchar, Descripcion varchar, Doc int, Formato varchar, ID_Bodega smallint, " +
		" Bodega varchar, AuditarAlm bit, ManejoStocks smallint, Orden int, Devolucion int, IVA numeric, Fmt_Orden varchar, Fmt_Devolucion varchar, Fija bit, FijaCost bit, Recepcion int, Fmt_Recepcion varchar, TipoCobro smallint )";
		setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public view_compras_entidades_ids getRow(int row)
	{
		return (view_compras_entidades_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_compras_entidades_ids getAbsRow(int row)
	{
		return (view_compras_entidades_ids)m_Rows.elementAt(row);
	}

	 
	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_compras_entidades_ids pNode = new view_compras_entidades_ids();

			pNode.setDoc(m_RS.getLong("Doc"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setID_Tipo(m_RS.getByte("ID_Tipo"));
			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setBodega(m_RS.getString("Bodega"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setAuditarAlm(m_RS.getBoolean("AuditarAlm"));
			pNode.setManejoStocks(m_RS.getByte("ManejoStocks"));
			pNode.setOrden(m_RS.getInt("Orden"));
			pNode.setDevolucion(m_RS.getInt("Devolucion"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setFmt_Orden(m_RS.getString("Fmt_Orden"));
			pNode.setFmt_Devolucion(m_RS.getString("Fmt_Devolucion"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setFijaCost(m_RS.getBoolean("FijaCost"));
			pNode.setRecepcion(m_RS.getInt("Recepcion"));
			pNode.setFmt_Recepcion(m_RS.getString("Fmt_Recepcion"));
			pNode.setTipoCobro(m_RS.getInt("TipoCobro"));
						
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
