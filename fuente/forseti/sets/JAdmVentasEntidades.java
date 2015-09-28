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

public class JAdmVentasEntidades extends JManejadorSet
{
	public JAdmVentasEntidades(HttpServletRequest request)
	{
		m_Select = " * FROM VIEW_VENTAS_ENTIDADES";
		m_PageSize = 50;
		this.request = request;
	}

	public TBL_VENTAS_ENTIDADES getRow(int row)
	{
		return (TBL_VENTAS_ENTIDADES)m_Rows.elementAt((getFloorRow() + row));
	}

	public TBL_VENTAS_ENTIDADES getAbsRow(int row)
	{
		return (TBL_VENTAS_ENTIDADES)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			TBL_VENTAS_ENTIDADES pNode = new TBL_VENTAS_ENTIDADES();

			pNode.setID_EntidadVenta(m_RS.getInt("ID_EntidadVenta"));
			pNode.setID_TipoEntidad(m_RS.getShort("ID_TipoEntidad"));
			pNode.setSerie(m_RS.getString("Serie"));
			pNode.setDoc(m_RS.getLong("Doc"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFormato(m_RS.getString("Formato"));
			pNode.setFormatoMOSTR(m_RS.getString("FormatoMOSTR"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setNombreBodega(m_RS.getString("NombreBodega"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setFijaCost(m_RS.getBoolean("FijaCost"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setDesgloseMOSTR(m_RS.getBoolean("DesgloseMOSTR"));
			pNode.setDesgloseCLIENT(m_RS.getBoolean("DesgloseCLIENT"));
			pNode.setMostrAplicaPolitica(m_RS.getBoolean("MostrAplicaPolitica"));
			pNode.setImprimeSinEm(m_RS.getBoolean("ImprimeSinEm"));
			pNode.setTipoCobro(m_RS.getByte("TipoCobro"));
			pNode.setCambioNumero(m_RS.getBoolean("CambioNumero"));
			pNode.setDesdeCliente(m_RS.getInt("DesdeCliente"));
			pNode.setHastaCliente(m_RS.getInt("HastaCliente"));
			pNode.setPedido(m_RS.getLong("Pedido"));
			pNode.setAjusteDePrecio(m_RS.getByte("AjusteDePrecio"));
			pNode.setFactorDeAjuste(m_RS.getFloat("FactorDeAjuste"));
			pNode.setDevolucion(m_RS.getLong("Devolucion"));
			pNode.setID_Vendedor(m_RS.getInt("ID_Vendedor"));
			pNode.setNombreVendedor(m_RS.getString("NombreVendedor"));
			pNode.setFmt_Pedido(m_RS.getString("Fmt_Pedido"));
			pNode.setFmt_Devolucion(m_RS.getString("Fmt_Devolucion"));
			pNode.setFactNumCIE(m_RS.getLong("FactNumCIE"));
			pNode.setDevNumCIE(m_RS.getLong("DevNumCIE"));
			pNode.setRemision(m_RS.getLong("Remision"));
			pNode.setCotizacion(m_RS.getLong("Cotizacion"));
			pNode.setFmt_Remision(m_RS.getString("Fmt_Remision"));
			pNode.setFmt_Cotizacion(m_RS.getString("Fmt_Cotizacion"));
			pNode.setCFD(m_RS.getString("CFD"));
			pNode.setCFD_NoAprobacion(m_RS.getInt("CFD_NoAprobacion"));
			pNode.setCFD_NoCertificado(m_RS.getString("CFD_NoCertificado"));
			pNode.setCFD_ID_Expedicion(m_RS.getByte("CFD_ID_Expedicion"));
			pNode.setCFD_NoAprobacionDev(m_RS.getInt("CFD_NoAprobacionDev"));
			pNode.setCFD_NoCertificadoDev(m_RS.getString("CFD_NoCertificadoDev"));
			pNode.setCFD_ID_ExpedicionDev(m_RS.getByte("CFD_ID_ExpedicionDev"));
			pNode.setCFD_NoAprobacionRem(m_RS.getInt("CFD_NoAprobacionRem"));
			pNode.setCFD_NoCertificadoRem(m_RS.getString("CFD_NoCertificadoRem"));
			pNode.setCFD_ID_ExpedicionRem(m_RS.getByte("CFD_ID_ExpedicionRem"));
			pNode.setID_Clasificacion(m_RS.getString("ID_Clasificacion"));
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
