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

public class JInvServInvSetMasV2 extends JManejadorSet
{
	public JInvServInvSetMasV2(HttpServletRequest request)
	{
		m_Select = " * FROM view_invserv_inventarios_mas ";
		m_PageSize = 50;
		this.request = request;
	}

	public view_invserv_inventarios_mas getRow(int row)
	{
		return (view_invserv_inventarios_mas)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_invserv_inventarios_mas getAbsRow(int row)
	{
		return (view_invserv_inventarios_mas)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_invserv_inventarios_mas pNode = new view_invserv_inventarios_mas();

			pNode.setClave(m_RS.getString("Clave"));
			pNode.setPrecio(m_RS.getFloat("Precio"));
			pNode.setStockMin(m_RS.getFloat("StockMin"));
			pNode.setStockMax(m_RS.getFloat("StockMax"));
			pNode.setSeProduce(m_RS.getBoolean("SeProduce"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setID_UnidadSalida(m_RS.getString("ID_UnidadSalida"));
			pNode.setFactor(m_RS.getFloat("Factor"));
			pNode.setEmpaque(m_RS.getFloat("Empaque"));
			pNode.setPorSurtir(m_RS.getFloat("PorSurtir"));
			pNode.setPorRecibir(m_RS.getFloat("PorRecibir"));
			pNode.setApartado(m_RS.getFloat("Apartado"));
			pNode.setDias(m_RS.getInt("Dias"));
			pNode.setUltimoCosto(m_RS.getFloat("UltimoCosto"));
			pNode.setCostoPromedio(m_RS.getFloat("CostoPromedio"));
			pNode.setImpIEPS(m_RS.getFloat("ImpIEPS"));
			pNode.setImpIVARet(m_RS.getFloat("ImpIVARet"));
			pNode.setImpISRRet(m_RS.getFloat("ImpISRRet"));
			pNode.setIVA_Deducible(m_RS.getFloat("IVA_Deducible"));
			pNode.setIVA(m_RS.getBoolean("IVA"));
			pNode.setTipoCosteo(m_RS.getByte("TipoCosteo"));
			pNode.setCantidadAcum(m_RS.getFloat("CantidadAcum"));
			pNode.setMontoAcum(m_RS.getFloat("MontoAcum"));
			pNode.setPrecio2(m_RS.getFloat("Precio2"));
			pNode.setPrecio3(m_RS.getFloat("Precio3"));
			pNode.setPrecio4(m_RS.getFloat("Precio4"));
			pNode.setPrecio5(m_RS.getFloat("Precio5"));
			pNode.setPrecioOfertaWeb(m_RS.getFloat("PrecioOfertaWeb"));
			pNode.setPrecioWeb(m_RS.getFloat("PrecioWeb"));
			pNode.setNoSeVende(m_RS.getBoolean("NoSeVende"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setDescripcionWeb(m_RS.getString("DescripcionWeb"));
			pNode.setComentariosWeb(m_RS.getString("ComentariosWeb"));
			pNode.setDescripcionWebING(m_RS.getString("DescripcionWebING"));
			pNode.setComentariosWebING(m_RS.getString("ComentariosWebING"));
			pNode.setRef_FotoWeb(m_RS.getString("Ref_FotoWeb"));
			pNode.setRef_FotoChicaWeb(m_RS.getString("Ref_FotoChicaWeb"));
			pNode.setNuevoWeb(m_RS.getBoolean("NuevoWeb"));
			pNode.setNumRecomWeb(m_RS.getInt("NumRecomWeb"));
			pNode.setNumExperienciasWeb(m_RS.getInt("NumExperienciasWeb"));
			pNode.setKgsWeb(m_RS.getFloat("KgsWeb"));
			pNode.setLineaDescripcion(m_RS.getString("LineaDescripcion"));
			pNode.setCuentaNombre(m_RS.getString("CuentaNombre"));
			pNode.setCodigo(m_RS.getString("Codigo"));
			pNode.setPrecioMin(m_RS.getFloat("PrecioMin"));
			pNode.setPrecioMax(m_RS.getFloat("PrecioMax"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
