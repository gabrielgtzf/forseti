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

public class JCFDVenFactCabGenSet extends JManejadorSet
{
	public JCFDVenFactCabGenSet(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("FACTURAS"))
			m_Select = " * FROM view_cfd_ventas_facturas_cab_generar";
		else if(tipo.equals("REMISIONES"))
			m_Select = " * FROM view_cfd_ventas_remisiones_cab_generar";
		else if(tipo.equals("DEVOLUCIONES"))
			m_Select = " * FROM view_cfd_ventas_devoluciones_cab_generar";
		else if(tipo.equals("TRASPASOS"))
			m_Select = " * FROM view_cfd_invserv_traspasos_cab_generar";
		
		m_PageSize = 50;
		this.request = request;
	}

	public view_cfd_ventas_facturas_cab_generar getRow(int row)
	{
		return (view_cfd_ventas_facturas_cab_generar)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cfd_ventas_facturas_cab_generar getAbsRow(int row)
	{
		return (view_cfd_ventas_facturas_cab_generar)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cfd_ventas_facturas_cab_generar pNode = new view_cfd_ventas_facturas_cab_generar();

			pNode.setID_EntidadVenta(m_RS.getByte("ID_EntidadVenta"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setFija(m_RS.getBoolean("Fija"));
			pNode.setIVA_Entidad(m_RS.getFloat("IVA_Entidad"));
			pNode.setCFD(m_RS.getBoolean("CFD"));
			pNode.setCFD_Serie(m_RS.getString("CFD_Serie"));
			pNode.setCFD_Folio(m_RS.getInt("CFD_Folio"));
			pNode.setCFD_FolioINI(m_RS.getInt("CFD_FolioINI"));
			pNode.setCFD_FolioFIN(m_RS.getInt("CFD_FolioFIN"));
			pNode.setCFD_NoAprobacion(m_RS.getInt("CFD_NoAprobacion"));
			pNode.setCFD_AnoAprobacion(m_RS.getInt("CFD_AnoAprobacion"));
			pNode.setCFD_NoCertificado(m_RS.getString("CFD_NoCertificado"));
			pNode.setCFD_ArchivoCertificado(m_RS.getString("CFD_ArchivoCertificado"));
			pNode.setCFD_CaducidadCertificado(m_RS.getDate("CFD_CaducidadCertificado"));
			pNode.setCFD_ArchivoLLave(m_RS.getString("CFD_ArchivoLLave"));
			pNode.setCFD_ClaveLLave(m_RS.getString("CFD_ClaveLLave"));
			pNode.setCFD_ID_Expedicion(m_RS.getByte("CFD_ID_Expedicion"));
			pNode.setCFD_Calle(m_RS.getString("CFD_Calle"));
			pNode.setCFD_NoExt(m_RS.getString("CFD_NoExt"));
			pNode.setCFD_NoInt(m_RS.getString("CFD_NoInt"));
			pNode.setCFD_Colonia(m_RS.getString("CFD_Colonia"));
			pNode.setCFD_Localidad(m_RS.getString("CFD_Localidad"));
			pNode.setCFD_Municipio(m_RS.getString("CFD_Municipio"));
			pNode.setCFD_Estado(m_RS.getString("CFD_Estado"));
			pNode.setCFD_Pais(m_RS.getString("CFD_Pais"));
			pNode.setCFD_CP(m_RS.getString("CFD_CP"));
			pNode.setID_Factura(m_RS.getInt("ID_Factura"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setID_Cliente(m_RS.getInt("ID_Cliente"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setReferencia(m_RS.getString("Referencia"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setMonedaSim(m_RS.getString("MonedaSim"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setCondicion(m_RS.getByte("Condicion"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setImporte(m_RS.getFloat("Importe"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setSubTotal(m_RS.getFloat("SubTotal"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setIEPS(m_RS.getFloat("IEPS"));
			pNode.setIVARet(m_RS.getFloat("IVARet"));
			pNode.setISRRet(m_RS.getFloat("ISRRet"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setRef(m_RS.getString("Ref"));
			pNode.setID_Pol(m_RS.getString("ID_Pol"));
			pNode.setID_PolCost(m_RS.getString("ID_PolCost"));
			pNode.setID_Bodega(m_RS.getByte("ID_Bodega"));
			pNode.setEfectivo(m_RS.getFloat("Efectivo"));
			pNode.setBancos(m_RS.getFloat("Bancos"));
			pNode.setCambio(m_RS.getFloat("Cambio"));
			pNode.setID_Vendedor(m_RS.getInt("ID_Vendedor"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setDiasCredito(m_RS.getInt("DiasCredito"));
			pNode.setCalle(m_RS.getString("Calle"));
			pNode.setNoExt(m_RS.getString("NoExt"));
			pNode.setNoInt(m_RS.getString("NoInt"));
			pNode.setColonia(m_RS.getString("Colonia"));
			pNode.setLocalidad(m_RS.getString("Localidad"));
			pNode.setMunicipio(m_RS.getString("Municipio"));
			pNode.setEstado(m_RS.getString("Estado"));
			pNode.setPais(m_RS.getString("Pais"));
			pNode.setCP(m_RS.getString("CP"));
			pNode.setMetodoDePago(m_RS.getString("MetodoDePago"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
