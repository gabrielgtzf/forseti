package forseti.sets;

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JCFDCompOtrSet extends JManejadorSet
{
	public JCFDCompOtrSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cfdcompotr";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cfdcompotr getRow(int row)
	{
		return (view_cfdcompotr)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cfdcompotr getAbsRow(int row)
	{
		return (view_cfdcompotr)m_Rows.elementAt(row);
	}

	@SuppressWarnings("unchecked")
	protected void BindRow()
	{
		try
		{
			view_cfdcompotr pNode = new view_cfdcompotr();

			pNode.setID_CFD(m_RS.getInt("ID_CFD"));
			pNode.setFSI_Tipo(m_RS.getString("FSI_Tipo"));
			pNode.setFSI_ID(m_RS.getInt("FSI_ID"));
			pNode.setTipo(m_RS.getString("Tipo"));
			pNode.setFactura(m_RS.getString("Factura"));
			pNode.setCFD_CBB_Serie(m_RS.getString("CFD_CBB_Serie"));
			pNode.setCFD_CBB_NumFol(m_RS.getInt("CFD_CBB_NumFol"));
			pNode.setNumFactExt(m_RS.getString("NumFactExt"));
			pNode.setUUID(m_RS.getString("UUID"));
			pNode.setExt(m_RS.getString("Ext"));
			pNode.setNombre_Original(m_RS.getString("Nombre_Original"));
			pNode.setTotal(m_RS.getFloat("Total"));
			pNode.setID_Moneda(m_RS.getInt("ID_Moneda"));
			pNode.setTC(m_RS.getFloat("TC"));
			pNode.setFecha(m_RS.getDate("Fecha"));
			pNode.setEnlace(m_RS.getString("Enlace"));
			

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
