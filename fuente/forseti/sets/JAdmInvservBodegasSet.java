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

public class JAdmInvservBodegasSet extends JManejadorSet
{
	public JAdmInvservBodegasSet(HttpServletRequest request)
	{
		m_Select = " * FROM TBL_INVSERV_BODEGAS";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_INVSERV_BODEGAS getRow(int row)
	{
		return (TBL_INVSERV_BODEGAS)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_INVSERV_BODEGAS getAbsRow(int row)
	{
		return (TBL_INVSERV_BODEGAS)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_INVSERV_BODEGAS pNode = new TBL_INVSERV_BODEGAS();

			pNode.setID_Bodega(m_RS.getShort("ID_Bodega"));
			pNode.setID_InvServ(m_RS.getString("ID_InvServ"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setNumero(m_RS.getLong("Numero"));
			pNode.setSalida(m_RS.getInt("Salida"));
			pNode.setFmt_Movimientos(m_RS.getString("Fmt_Movimientos"));
			pNode.setFmt_Traspasos(m_RS.getString("Fmt_Traspasos"));
			pNode.setAuditarAlm(m_RS.getBoolean("AuditarAlm"));
			pNode.setManejoStocks(m_RS.getByte("ManejoStocks"));
			pNode.setRequerimiento(m_RS.getInt("Requerimiento"));
			pNode.setFmt_Requerimientos(m_RS.getString("Fmt_Requerimientos"));
			pNode.setPlantilla(m_RS.getInt("Plantilla"));
			pNode.setFmt_Plantillas(m_RS.getString("Fmt_Plantilla"));
			pNode.setCFD(m_RS.getString("CFD"));
			pNode.setCFD_NoAprobacion(m_RS.getInt("CFD_NoAprobacion"));
			pNode.setCFD_NoCertificado(m_RS.getString("CFD_NoCertificado"));
			pNode.setCFD_ID_Expedicion(m_RS.getByte("CFD_ID_Expedicion"));
			pNode.setCFD_ID_Receptor(m_RS.getByte("CFD_ID_Receptor"));
			pNode.setNumChFis(m_RS.getInt("NumChFis"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setFmt_ChFis(m_RS.getString("Fmt_ChFis"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
