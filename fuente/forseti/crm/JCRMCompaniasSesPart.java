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
package forseti.crm;
public class JCRMCompaniasSesPart
{
  private String m_Clave;
  private String m_Descripcion;

  public JCRMCompaniasSesPart()
  {
  }

  public JCRMCompaniasSesPart(String Clave,String Descripcion )
  {
    m_Clave = Clave;
    m_Descripcion = Descripcion;
    
  }

  public void setPartida(String Clave,String Descripcion )
  {
    m_Clave = Clave;
    m_Descripcion = Descripcion;
  }

  public String getClave()
  {
    return m_Clave;
  }

  public String getDescripcion()
  {
    return m_Descripcion;
  }

  
}
