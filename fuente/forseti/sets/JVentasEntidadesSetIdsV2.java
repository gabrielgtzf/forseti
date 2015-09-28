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

public class JVentasEntidadesSetIdsV2 extends JManejadorSet
{
	public JVentasEntidadesSetIdsV2(HttpServletRequest request, String usuario, String entidad)
	{
		m_Select = " * FROM view_ventas_entidades_ids ";
		String sql = "select * from view_ventas_entidades_ids('" + usuario + "','" + entidad + 
		"') as ( " +
		"id_usuario varchar, id_entidad smallint, ID_Tipo smallint, Serie varchar, Descripcion varchar, Doc int, Formato varchar, FormatoMOSTR varchar, ID_Bodega smallint, " +
		" Bodega varchar, IVA numeric, DesgloseMOSTR bit, DesgloseCLIENT bit, MostrAplicaPolitica bit, AuditarAlm bit, TipoCobro smallint, CambioNumero bit, DesdeCliente int, HastaCliente int, Pedido int, AjusteDePrecio smallint, FactorDeAjuste numeric, ImprimeSinEm bit, Fija bit, FijaCost bit, Devolucion int, " +
		" ID_Vendedor smallint, VendedorNombre varchar, Remision int, Cotizacion int, CFD bit(2), Fmt_Devolucion varchar, Fmt_Remision varchar, Fmt_Pedido varchar, Fmt_Cotizacion varchar, ManejoStocks smallint )";
		setSQL(sql);
        m_PageSize = 50;
		this.request = request;
	}

	public view_ventas_entidades_ids getRow(int row)
	{
		return (view_ventas_entidades_ids)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ventas_entidades_ids getAbsRow(int row)
	{
		return (view_ventas_entidades_ids)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ventas_entidades_ids pNode = new view_ventas_entidades_ids();

			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setDoc(m_RS.getLong("Doc"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setFormatoMOSTR(m_RS.getString("FormatoMOSTR"));
			pNode.setID_Entidad(m_RS.getInt("ID_Entidad"));
			pNode.setID_Tipo(m_RS.getByte("ID_Tipo"));
			pNode.setBodega(m_RS.getString("Bodega"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
			pNode.setDesgloseCLIENT(m_RS.getBoolean("DesgloseCLIENT"));
			pNode.setDesgloseMOSTR(m_RS.getBoolean("DesgloseMOSTR"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setAuditarAlm(m_RS.getBoolean("AuditarAlm"));
			pNode.setMostrAplicaPolitica(m_RS.getBoolean("MostrAplicaPolitica"));
			pNode.setTipoCobro(m_RS.getByte("TipoCobro"));
			pNode.setCambioNumero(m_RS.getBoolean("CambioNumero"));
			pNode.setDesdeCliente(m_RS.getInt("DesdeCliente"));
			pNode.setHastaCliente(m_RS.getInt("HastaCliente"));
			pNode.setPedido(m_RS.getInt("Pedido"));
			pNode.setAjusteDePrecio(m_RS.getByte("AjusteDePrecio"));
			pNode.setFactorDeAjuste(m_RS.getFloat("FactorDeAjuste"));
			pNode.setImprimeSinEm(m_RS.getBoolean("ImprimeSinEm"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setFijaCost(m_RS.getBoolean("FijaCost"));
			pNode.setDevolucion(m_RS.getInt("Devolucion"));
			pNode.setID_Vendedor(m_RS.getInt("ID_Vendedor"));
			pNode.setVendedorNombre(m_RS.getString("VendedorNombre"));
			pNode.setRemision(m_RS.getInt("Remision"));
			pNode.setCotizacion(m_RS.getInt("Cotizacion"));
			pNode.setCFD(m_RS.getBoolean("CFD"));
			pNode.setFmt_Devolucion(m_RS.getString("Fmt_Devolucion"));
			pNode.setFmt_Remision(m_RS.getString("Fmt_Remision"));
			pNode.setFmt_Pedido(m_RS.getString("Fmt_Pedido"));
			pNode.setFmt_Cotizacion(m_RS.getString("Fmt_Cotizacion"));
			pNode.setManejoStocks(m_RS.getByte("ManejoStocks"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
