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

import java.sql.SQLException;
import forseti.sets.TBL_BD;
import javax.servlet.http.HttpServletRequest;


public class JBDRegistradasSet extends JManejadorSet
{
  public JBDRegistradasSet(HttpServletRequest request)
  {
    m_Select = " * FROM TBL_BD ";
    m_PageSize = 50;
    this.request = request;
  }

  public TBL_BD getRow(int row)
  {
    return (TBL_BD)m_Rows.elementAt((getFloorRow() + row));
  }

  public TBL_BD getAbsRow(int row)
  {
    return (TBL_BD)m_Rows.elementAt((getFloorRow() + row));
  }

   
  @SuppressWarnings("unchecked")
  protected void BindRow()
  {
    try
    {
      TBL_BD pNode = new TBL_BD();
      
      pNode.setID_BD(m_RS.getString("ID_BD"));
      pNode.setNombre(m_RS.getString("Nombre"));
      pNode.setUsuario(m_RS.getString("Usuario"));
      pNode.setPassword(m_RS.getString("Password"));
      pNode.setFechaalta(m_RS.getDate("Fechaalta"));
      pNode.setCompania(m_RS.getString("Compania"));
      pNode.setDireccion(m_RS.getString("Direccion"));
      pNode.setPoblacion(m_RS.getString("Poblacion"));
      pNode.setCP(m_RS.getString("CP"));
      pNode.setMail(m_RS.getString("Mail"));
      pNode.setWeb(m_RS.getString("Web"));
      pNode.setSU(m_RS.getString("SU"));
      
      m_Rows.addElement(pNode);

    }
    catch(SQLException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e.toString());
    }

  }
}
