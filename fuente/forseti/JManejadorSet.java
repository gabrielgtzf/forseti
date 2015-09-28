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
package forseti;

/**
 * <p>T�tulo: </p>
 * <p>Descripci�n: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

import java.io.IOException;
import java.sql.*;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("rawtypes")
public abstract class JManejadorSet extends JAccesoBD 
{
	/*
	private static boolean init = false;
	private static Driver m_Driver;
	private static String PASS;
	private static String ADDR;
	private static String PORT;
	*/

	public String m_Where;
	public String m_OrderBy;
	private boolean m_isOpen;
	protected ResultSet m_RS;
	protected String m_Select;
	protected short m_PageSize;
	protected int m_Page;
	private boolean m_construct;
	private String m_sSQL;
	private int m_ConCat;
	private String m_BD;
	
	protected HttpServletRequest request; // aqui obtiene el request que tiene
											// la sesion que a su vez tiene los
											// parametros de conexion

	protected boolean m_bMD; // esta variable dice al sistema si los metadatos
								// en m_RSMD seran generados
	 
	protected Vector m_Columns;
	 
	protected Vector m_Rows; // protegida porque se verá en sus hijos

	 
	public JManejadorSet() 
	{
		m_construct = true; // se llamara a la funcion ConstructSQL cuando se
							// abre ?
		m_Where = "";
		m_OrderBy = "";
		// m_nRows = 0;
		m_Page = 0;
		m_Rows = new Vector();
		m_ConCat = 0;
		m_bMD = false;
		m_isOpen = false;
	}

	public void setBD(String BD)
	{
		m_BD = BD;
	}
	
	public boolean isOpen() 
	{
		return m_isOpen;
	}

	public void ConCat(boolean concat) 
	{
		if(!concat)
			m_ConCat = 0;
		else
			m_ConCat = 1;
	}
	
	public void ConCat(int concat) 
	{
		m_ConCat = concat;
	}

	public void setRequest(HttpServletRequest request) 
	{
		this.request = request;
	}

	public int getFloorRow() 
	{
		return m_Page * m_PageSize;
	}

	public void IncrementPage() 
	{
		m_Page++;
	}

	public int getNumRows() 
	{
		return m_Rows.size();
	}

	public int getNumCols() 
	{
		return m_Columns.size();
	}

	public short getPageSize() 
	{
		return m_PageSize;
	}

	public int getPage() 
	{
		return m_Page;
	}

	private void ConstructSQL() 
	{
		String select, where, orderby;
		select = "select " + m_Select + "\n";
		orderby = (m_OrderBy.equals("")) ? "" : "order by " + m_OrderBy + "\n";
		where = (m_Where.equals("")) ? "" : "where " + m_Where + "\n";

		m_sSQL = select + where + orderby;
	}

	public void setPageSize(short size) 
	{
		m_PageSize = size;
	}

	public void setSQL(String sql) 
	{
		m_sSQL = sql;
		m_construct = false;
	}

	public String getSQL() 
	{
		return m_sSQL;
	}

   
	 
	@SuppressWarnings("unchecked")
	public void Open()
	{
		if (m_construct) 
		{
			ConstructSQL();
			//System.out.println(m_sSQL);
		}

		m_Rows.removeAllElements();
		// m_nRows = 0;
		m_Page = 0;
		
		Connection con = null;
		Statement s = null;
		//System.out.println("ANTES DE ABRIR");
		try 
		{
			if(m_ConCat == 1) // 1 es conexion a FORSETI_ADMIN
				con = getConexion();
			else if(m_ConCat == 0) // 0 es conexion a la BD del CEF
				con = getConexionSes(request);
			else if(m_ConCat == 2)	// 2 conexion a BD CEF por REF
				con = getConexionSesB2B(request);
			else // 3 conexion a BD del SAF o CEF, sin sesion
				con = getConexion(m_BD);
			
			s = con.createStatement();
			
			m_RS = s.executeQuery(m_sSQL);

			if (m_bMD) // si se activo Los MetaDatos los genera
			{
				ResultSetMetaData rsmd = m_RS.getMetaData();
				m_Columns = new Vector();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) 
				{
					JFsiMetaDatos pMD = new JFsiMetaDatos(
							rsmd.getColumnName(i), rsmd.getPrecision(i), rsmd
									.getScale(i), rsmd.getTableName(i), rsmd
									.getColumnType(i), rsmd
									.getColumnTypeName(i));
					m_Columns.addElement(pMD);
				}
			}

			while (m_RS.next()) 
			{
				BindRow();
				// m_nRows++;
			}

			m_isOpen = true;

		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			try 
			{ 
				if(m_ConCat == 1) // 1 es conexion a FORSETI_ADMIN
				{
					if(JUtil.getSesion(request) != null)
						JUtil.RDP("SAF",JUtil.getSesion(request).getConBD(),"AL",JUtil.getSesion(request).getID_Usuario(),"SYS_SQL_OPEN","????||||", JUtil.q(e.toString()));
					else
						JUtil.RDP("SAF","FORSETI_ADMIN","AL", "", "SYS_SQL_OPEN", "????||||", JUtil.q(e.toString()));
				}
				else if(m_ConCat == 0) // 0 es conexion a la BD del CEF
					JUtil.RDP("CEF",JUtil.getSesion(request).getConBD(),"AL", JUtil.getSesion(request).getID_Usuario(), "SYS_SQL_OPEN", "????||||", JUtil.q(e.toString()));
				else if(m_ConCat == 2)	// 2 conexion a BD CEF por REF
					JUtil.RDP("REF",JUtil.getSesion(request).getConBD(),"AL", JUtil.getSesion(request).getID_Usuario(), "SYS_SQL_OPEN", "????||||", JUtil.q(e.toString()));
				else // 3 conexion a BD del CEF, sin sesion
					JUtil.RDP("CEF",m_BD,"AL", "", "SYS_SQL_OPEN", "????||||", JUtil.q(e.toString()));
			} 
			catch (ServletException e1) 
			{
				e1.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			
		}
		finally
		{
			try { m_RS.close(); } catch (SQLException e) {}
			try { s.close(); } catch (SQLException e) {}
			try { con.close(); } catch (SQLException e) {}
		}
		
	}

	public void setSelect(String select) 
	{
		m_Select = select;
	}

	public ResultSet getRS()
	{
		return m_RS;
	}

	public void SetPage(int page) 
	{
		m_Page = page;
	}

	public JFsiMetaDatos getCol(int col) 
	{
		return (JFsiMetaDatos) m_Columns.elementAt(col);
	}

	protected abstract void BindRow();

}
