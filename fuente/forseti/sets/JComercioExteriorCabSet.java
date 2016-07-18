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

public class JComercioExteriorCabSet extends JManejadorSet
{
	public JComercioExteriorCabSet(HttpServletRequest request, String tipo)
	{
		if(tipo.equals("COMPRA"))
			m_Select = " * FROM tbl_compras_facturas_comext_cab";
		else if(tipo.equals("VENTA"))
			m_Select = " * FROM tbl_ventas_facturas_comext_cab";
		else // será GASTO
			m_Select = " * FROM tbl_compras_gastos_comext_cab";
		
		m_PageSize = 50;
		this.request = request;
	}

	public tbl_comercio_exterior_cab getRow(int row)
	{
		return (tbl_comercio_exterior_cab)m_Rows.elementAt((getFloorRow() + row));
	}

	public tbl_comercio_exterior_cab getAbsRow(int row)
	{
		return (tbl_comercio_exterior_cab)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			tbl_comercio_exterior_cab pNode = new tbl_comercio_exterior_cab();

			pNode.setID_VC(m_RS.getInt("ID_VC"));
			pNode.setTipoOperacion(m_RS.getString("TipoOperacion"));
			pNode.setClaveDePedimento(m_RS.getString("ClaveDePedimento"));
			pNode.setCertificadoOrigen(m_RS.getByte("CertificadoOrigen"));
			pNode.setNumCertificadoOrigen(m_RS.getString("NumCertificadoOrigen"));
			pNode.setNumeroExportadorConfiable(m_RS.getString("NumeroExportadorConfiable"));
			pNode.setIncoterm(m_RS.getString("Incoterm"));
			pNode.setSubdivision(m_RS.getByte("Subdivision"));
			pNode.setObservaciones(m_RS.getString("Observaciones"));
			pNode.setTipoCambioUsd(m_RS.getFloat("TipoCambioUsd"));
			pNode.setTotalUsd(m_RS.getFloat("TotalUsd"));
			pNode.setEmisor_Curp(m_RS.getString("Emisor_Curp"));
			pNode.setReceptor_Curp(m_RS.getString("Receptor_Curp"));
			pNode.setReceptor_NumRegIdTrib(m_RS.getString("Receptor_NumRegIdTrib"));
			pNode.setDestinatario_NumRegIdTrib(m_RS.getString("Destinatario_NumRegIdTrib"));
			pNode.setDestinatario_RFC(m_RS.getString("Destinatario_RFC"));
			pNode.setDestinatario_Curp(m_RS.getString("Destinatario_Curp"));
			pNode.setDestinatario_Nombre(m_RS.getString("Destinatario_Nombre"));
			pNode.setDestinatario_Domicilio_Calle(m_RS.getString("Destinatario_Domicilio_Calle"));
			pNode.setDestinatario_Domicilio_NumeroExterior(m_RS.getString("Destinatario_Domicilio_NumeroExterior"));
			pNode.setDestinatario_Domicilio_NumeroInterior(m_RS.getString("Destinatario_Domicilio_NumeroInterior"));
			pNode.setDestinatario_Domicilio_Colonia(m_RS.getString("Destinatario_Domicilio_Colonia"));
			pNode.setDestinatario_Domicilio_Localidad(m_RS.getString("Destinatario_Domicilio_Localidad"));
			pNode.setDestinatario_Domicilio_Referencia(m_RS.getString("Destinatario_Domicilio_Referencia"));
			pNode.setDestinatario_Domicilio_Municipio(m_RS.getString("Destinatario_Domicilio_Municipio"));
			pNode.setDestinatario_Domicilio_Estado(m_RS.getString("Destinatario_Domicilio_Estado"));
			pNode.setDestinatario_Domicilio_Pais(m_RS.getString("Destinatario_Domicilio_Pais"));
			pNode.setDestinatario_Domicilio_CodigoPostal(m_RS.getString("Destinatario_Domicilio_CodigoPostal"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
