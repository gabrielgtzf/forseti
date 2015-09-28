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

public class JVentasFactSetCabV2 extends JManejadorSet
{
	public JVentasFactSetCabV2(HttpServletRequest request, String tipomov)
	{
		if(tipomov.equals("FACTURAS"))
			m_Select = " * FROM view_ventas_facturas_cab ";
		else if(tipomov.equals("PEDIDOS"))
			m_Select = " * FROM view_ventas_pedidos_cab ";
		else if(tipomov.equals("DEVOLUCIONES"))
			m_Select = " * FROM view_ventas_devoluciones_cab ";
		else if(tipomov.equals("REMISIONES"))
			m_Select = " * FROM view_ventas_remisiones_cab ";
		else if(tipomov.equals("COTIZACIONES"))
			m_Select = " * FROM view_ventas_cotizaciones_cab ";
		
		m_PageSize = 50;
		this.request = request;
	}

	public view_ventas_facturas_cab getRow(int row)
	{
		return (view_ventas_facturas_cab)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_ventas_facturas_cab getAbsRow(int row)
	{
		return (view_ventas_facturas_cab)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_ventas_facturas_cab pNode = new view_ventas_facturas_cab();

			pNode.setID_Factura(m_RS.getLong("ID_Factura"));
			pNode.setID_Cliente(m_RS.getLong("ID_Cliente"));
			pNode.setNumero(m_RS.getLong("Numero"));
			pNode.setFechaEnvio(m_RS.getDate("FechaEnvio"));
			pNode.setCondicion(m_RS.getByte("Condicion"));
			pNode.setObs(m_RS.getString("Obs"));
			pNode.setMoneda(m_RS.getString("Moneda"));
			pNode.setImporte(m_RS.getFloat("Importe"));
			pNode.setDescuento(m_RS.getFloat("Descuento"));
			pNode.setSubTotal(m_RS.getFloat("SubTotal"));
			pNode.setIVA(m_RS.getFloat("IVA"));
			pNode.setIEPS(m_RS.getFloat("IEPS"));
			pNode.setIVARet(m_RS.getFloat("IVARet"));
			pNode.setISRRet(m_RS.getFloat("ISRRet"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setDireccion(m_RS.getString("Direccion"));
			pNode.setCP(m_RS.getString("CP"));
			pNode.setTel(m_RS.getString("Tel"));
			pNode.setPoblacion(m_RS.getString("Poblacion"));
			pNode.setColonia(m_RS.getString("Colonia"));
			pNode.setRFC(m_RS.getString("RFC"));
			pNode.setID_Bodega(m_RS.getInt("ID_Bodega"));
			pNode.setNombre(m_RS.getString("Nombre"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
