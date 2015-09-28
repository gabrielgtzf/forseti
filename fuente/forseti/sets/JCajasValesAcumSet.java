package forseti.sets;

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JCajasValesAcumSet extends JManejadorSet
{
	public JCajasValesAcumSet(HttpServletRequest request)
	{
		m_Select = " * FROM view_cajas_vales_acum";
		m_PageSize = 50;
		this.request = request;
	}

	public view_cajas_vales_acum getRow(int row)
	{
		return (view_cajas_vales_acum)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_cajas_vales_acum getAbsRow(int row)
	{
		return (view_cajas_vales_acum)m_Rows.elementAt(row);
	}

	@SuppressWarnings({ "unchecked" })
	protected void BindRow()
	{
		try
		{
			view_cajas_vales_acum pNode = new view_cajas_vales_acum();

			pNode.setID_Tipo(m_RS.getInt("ID_Tipo"));
			pNode.setID_Clave(m_RS.getInt("ID_Clave"));
			pNode.setProvisionales(m_RS.getDouble("Provisionales"));
			pNode.setSinFactura(m_RS.getDouble("SinFactura"));
			pNode.setFacturas(m_RS.getDouble("Facturas"));
			pNode.setPagos(m_RS.getDouble("Pagos"));
			pNode.setOtros(m_RS.getDouble("Otros"));
			pNode.setTraspasar(m_RS.getDouble("Traspasar"));

			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
