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

public class JAdmComprasEntidades extends JManejadorSet
{
	public JAdmComprasEntidades(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_COMPRAS_ENTIDADES";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_COMPRAS_ENTIDADES getRow(int row)
	{
		return (TBL_COMPRAS_ENTIDADES)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_COMPRAS_ENTIDADES getAbsRow(int row)
	{
		return (TBL_COMPRAS_ENTIDADES)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_COMPRAS_ENTIDADES pNode = new TBL_COMPRAS_ENTIDADES();

			pNode.setID_EntidadCompra(m_RS.getInt("ID_EntidadCompra"));
			pNode.setID_TipoEntidad(m_RS.getShort("ID_TipoEntidad"));
			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setDoc(m_RS.getLong("Doc"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setNombreBodega(m_RS.getString("NombreBodega"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setFijaCost(m_RS.getBoolean("FijaCost"));
			pNode.setDevolucion(m_RS.getLong("Devolucion"));
			pNode.setOrden(m_RS.getLong("Orden"));
			pNode.setFmt_Devolucion(m_RS.getString("Fmt_Devolucion"));
			pNode.setFmt_Orden(m_RS.getString("Fmt_Orden"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setInfoPlantOC(m_RS.getShort("InfoPlantOC"));
			pNode.setInfoGasRec(m_RS.getShort("InfoGasRec"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setRecepcion(m_RS.getLong("Recepcion"));
			pNode.setFmt_Recepcion(m_RS.getString("Fmt_Recepcion"));
			pNode.setTipoCobro(m_RS.getByte("TipoCobro"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
