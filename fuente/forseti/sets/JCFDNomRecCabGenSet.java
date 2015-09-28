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

public class JCFDNomRecCabGenSet extends JManejadorSet
{
	public JCFDNomRecCabGenSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cfd_nomina_recibos_cab_generar";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cfd_nomina_recibos_cab_generar getRow(int row)
	{
		return (view_cfd_nomina_recibos_cab_generar)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cfd_nomina_recibos_cab_generar getAbsRow(int row)
	{
		return (view_cfd_nomina_recibos_cab_generar)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cfd_nomina_recibos_cab_generar pNode = new view_cfd_nomina_recibos_cab_generar();

			pNode.setID_Sucursal(m_RS.getByte("ID_Sucursal"));
			pNode.setDescripcion(m_RS.getString("Descripcion"));
			pNode.setCFD(m_RS.getBoolean("CFD"));
			pNode.setCFD_Serie(m_RS.getString("CFD_Serie"));
			pNode.setCFD_Folio(m_RS.getInt("CFD_Folio"));
			pNode.setCFD_FolioIni(m_RS.getInt("CFD_FolioIni"));
			pNode.setCFD_FolioFin(m_RS.getInt("CFD_FolioFin"));
			pNode.setCFD_NoAprobacion(m_RS.getInt("CFD_NoAprobacion"));
			pNode.setCFD_AnoAprobacion(m_RS.getInt("CFD_AnoAprobacion"));
			pNode.setCFD_NoCertificado(m_RS.getString("CFD_NoCertificado"));
			pNode.setCFD_ArchivoCertificado(m_RS.getString("CFD_ArchivoCertificado"));
			pNode.setCFD_CaducidadCertificado(m_RS.getDate("CFD_CaducidadCertificado"));
			pNode.setCFD_ArchivoLlave(m_RS.getString("CFD_ArchivoLlave"));
			pNode.setCFD_ClaveLlave(m_RS.getString("CFD_ClaveLlave"));
			pNode.setCFD_ID_Expedicion(m_RS.getInt("CFD_ID_Expedicion"));
			pNode.setCFD_Calle(m_RS.getString("CFD_Calle"));
			pNode.setCFD_NoExt(m_RS.getString("CFD_NoExt"));
			pNode.setCFD_NoInt(m_RS.getString("CFD_NoInt"));
			pNode.setCFD_Colonia(m_RS.getString("CFD_Colonia"));
			pNode.setCFD_Localidad(m_RS.getString("CFD_Localidad"));
			pNode.setCFD_Municipio(m_RS.getString("CFD_Municipio"));
			pNode.setCFD_Estado(m_RS.getString("CFD_Estado"));
			pNode.setCFD_Pais(m_RS.getString("CFD_Pais"));
			pNode.setCFD_CP(m_RS.getString("CFD_CP"));
			pNode.setID_Nomina(m_RS.getInt("ID_Nomina"));
			pNode.setNumero(m_RS.getInt("Numero"));
			pNode.setAno(m_RS.getInt("Ano"));
			pNode.setTipo(m_RS.getInt("Tipo"));
			pNode.setFecha_Desde(m_RS.getDate("Fecha_Desde"));
			pNode.setFecha_Hasta(m_RS.getDate("Fecha_Hasta"));
			pNode.setDias(m_RS.getInt("Dias"));
			pNode.setCerrado(m_RS.getBoolean("Cerrado"));
			pNode.setMes(m_RS.getByte("Mes"));
			pNode.setStatus(m_RS.getString("Status"));
			pNode.setFormaPago(m_RS.getString("FormaPago"));
			pNode.setID_Mov(m_RS.getInt("ID_Mov"));
			pNode.setID_Pol(m_RS.getInt("ID_Pol"));
			pNode.setMoneda(m_RS.getByte("Moneda"));
			pNode.setMonedaSim(m_RS.getString("MonedaSim"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setCondicion(m_RS.getInt("Condicion"));
			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setImporte(m_RS.getFloat("Importe"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setSubTotal(m_RS.getFloat("SubTotal"));
			pNode.setISR(m_RS.getFloat("ISR"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setRFC(m_RS.getString("RFC"));
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
			pNode.setCURP(m_RS.getString("CURP"));
			pNode.setTipoRegimen(m_RS.getInt("TipoRegimen"));
			pNode.setNumSeguridadSocial(m_RS.getString("NumSeguridadSocial"));
			pNode.setNumDiasPagados(m_RS.getFloat("NumDiasPagados"));
			pNode.setDepartamento(m_RS.getString("Departamento"));
			pNode.setCLABE(m_RS.getString("CLABE"));
			pNode.setBanco(m_RS.getString("Banco"));
			pNode.setFechaInicioRelLaboral(m_RS.getDate("FechaInicioRelLaboral"));
			pNode.setPuesto(m_RS.getString("Puesto"));
			pNode.setPeriodicidadPago(m_RS.getString("PeriodicidadPago"));
			pNode.setTotalGravado(m_RS.getFloat("TotalGravado"));
			pNode.setTotalExento(m_RS.getFloat("TotalExento"));
			pNode.setTotalDeducciones(m_RS.getFloat("TotalDeducciones"));
			pNode.setTotalDedGravadas(m_RS.getFloat("TotalDedGravadas"));
			pNode.setTotalDedExentas(m_RS.getFloat("TotalDedExentas"));
			pNode.setHorasExtras(m_RS.getFloat("HorasExtras"));
			pNode.setHorasTriples(m_RS.getFloat("HorasTriples"));
			pNode.setHorasDomingo(m_RS.getFloat("HorasDomingo"));
			pNode.setIXA(m_RS.getFloat("IXA"));
			pNode.setIXE(m_RS.getFloat("IXE"));
			pNode.setIXM(m_RS.getFloat("IXM"));
			pNode.setDiasHorasExtras(m_RS.getInt("DiasHorasExtras"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
