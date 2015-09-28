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

public class JAlmacenesMovimSetIdsV2 extends JManejadorSet
{
	public JAlmacenesMovimSetIdsV2(HttpServletRequest request, String usuario, String entidad, String tipo)
	{
		m_Select = " * FROM view_invserv_almacen_movim_modulo_ids ";
		String sql = "select * from view_invserv_almacen_movim_modulo_ids('" + usuario + "','" + entidad + "','" + tipo + "') as ( id_usuario varchar, id_bodega smallint, nombre varchar, numero int, salida int, requerimiento int, plantilla int, fmt_movimientos varchar, fmt_traspasos varchar, auditaralm bit, manejostocks smallint, cfd bit(2))";
        setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_almacen_movim_modulo_ids getRow(int row)
	{
		return (view_invserv_almacen_movim_modulo_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_almacen_movim_modulo_ids getAbsRow(int row)
	{
		return (view_invserv_almacen_movim_modulo_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_almacen_movim_modulo_ids pNode = new view_invserv_almacen_movim_modulo_ids();

			pNode.setID_Bodega(m_RS.getByte("ID_Bodega"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setNumero(m_RS.getLong("Numero"));
			pNode.setSalida(m_RS.getLong("Salida"));
			pNode.setPlantilla(m_RS.getLong("Plantilla"));
			pNode.setRequerimiento(m_RS.getLong("Requerimiento"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setFmt_Movimientos(m_RS.getString("Fmt_Movimientos"));
			pNode.setFmt_Traspasos(m_RS.getString("Fmt_Traspasos"));
			pNode.setAuditarAlm(m_RS.getBoolean("AuditarAlm"));
			pNode.setManejoStocks(m_RS.getByte("ManejoStocks"));
			pNode.setCFD(m_RS.getString("CFD"));
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
