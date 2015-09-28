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


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

public class JProcedimientosSet extends JManejadorSet 
{
	private short m_ID_Mensaje;
	private String m_Mensaje;
	
	public JProcedimientosSet()
	{
		m_ID_Mensaje = -1;
		m_Mensaje = "";
	}
	
	public short getID_Mensaje()
	{
		return m_ID_Mensaje;
	}
	
	public String getMensaje()
	{
		return m_Mensaje;
	}
	
	public void Open(HttpServletRequest request, String TMP_TBL, String SQLCall, String DELClause)
	{
        try
        {
           
           Connection con = getConexionSes(request);
           Statement s    = con.createStatement();
           s.executeUpdate(TMP_TBL);
           ResultSet rs   = s.executeQuery(SQLCall);
           if(rs.next())
           {
             m_ID_Mensaje = rs.getShort("ERR");
             m_Mensaje = rs.getString("RES");
           }
           s.executeUpdate(DELClause);
           s.close();
           liberarConexion(con);
        }
        catch(SQLException e)
        {
           e.printStackTrace();
           throw new RuntimeException(e.toString());
        }		
	}	
	
	protected void BindRow() 
	{
	
	}

}
