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
package forseti.catalogos;

public class JCatProdSesLineas
{

  private String m_ID_Linea;
  private String m_Descripcion;
  private boolean m_Activada;
  public JCatProdSesLineas()
  {
  }

  public JCatProdSesLineas(String idlinea, String descripcion, boolean activada)
  {
    m_ID_Linea = idlinea;
    m_Descripcion = descripcion;
    m_Activada = activada;
  }

  public void setParametros(String idlinea, String descripcion, boolean activada )
  {
    m_ID_Linea = idlinea;
    m_Descripcion = descripcion;
    m_Activada = activada;
  }
  public boolean getActivada()
  {
    return m_Activada;
  }
  public String getID_Linea()
  {
    return m_ID_Linea;
  }

  public String getDescripcion()
  {
    return m_Descripcion;
  }

}
