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

import java.sql.*;
import forseti.sets.TBL_REGISTROS;
import javax.servlet.http.HttpServletRequest;


public class JBDRegistrarSet extends JManejadorSet
{
  public JBDRegistrarSet(HttpServletRequest request)
  {
    m_Select = " * FROM TBL_REGISTROS ";
    m_PageSize = 50;
    this.request = request;
  }

  public TBL_REGISTROS getRow(int row)
  {
    return (TBL_REGISTROS)m_Rows.elementAt((getFloorRow() + row));
  }

  public TBL_REGISTROS getAbsRow(int row)
  {
    return (TBL_REGISTROS)m_Rows.elementAt((getFloorRow() + row));
  }

   
  @SuppressWarnings("unchecked")
  protected void BindRow()
  {
    try
    {
      TBL_REGISTROS pNode = new TBL_REGISTROS();
      pNode.setID_Registro(m_RS.getInt("ID_Registro"));
      pNode.setIP(m_RS.getString("IP"));
      pNode.setHost(m_RS.getString("Host"));
      pNode.setFecha(m_RS.getDate("Fecha"));
      pNode.setID_Sesion(m_RS.getString("ID_Sesion"));
      pNode.setFechaDesde(m_RS.getDate("FechaDesde"));
      pNode.setHoraDesde(m_RS.getTime("FechaDesde"));
      pNode.setFechaHasta(m_RS.getDate("FechaHasta"));
      pNode.setHoraHasta(m_RS.getTime("FechaHasta"));
      pNode.setStatus(m_RS.getString("Status"));
      pNode.setFSIBD(m_RS.getString("FSIBD"));
      pNode.setID_Usuario(m_RS.getString("ID_Usuario"));
      pNode.setPassword(m_RS.getString("Password"));
      pNode.setID_Tipo(m_RS.getString("ID_Tipo"));
      
      m_Rows.addElement(pNode);

    }
    catch(SQLException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e.toString());
    }

  }
}
