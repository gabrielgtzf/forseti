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

public class JFsiMetaDatos
{
  private String m_NombreCol;
  private int m_Precision;
  private int m_Escala;
  private String m_NombreTabla;
  private int m_TipoCol;
  private String m_NombreTipoCol;

  public JFsiMetaDatos(String NombreCol, int Precision, int Escala, String NombreTabla, int TipoCol, String NombreTipoCol)
  {

    m_NombreCol = NombreCol;
    m_Precision = Precision;
    m_Escala = Escala;
    m_NombreTabla = NombreTabla;
    m_TipoCol = TipoCol;
    m_NombreTipoCol = NombreTipoCol;

  }

  public String getNombreCol()
  {
        return m_NombreCol;
  }

  public int getPrecision()
  {
        return m_Precision;
  }

  public int getEscala()
  {
        return m_Escala;
  }

  public String getNombreTabla()
  {
        return m_NombreTabla;
  }

  public int getTipoCol()
  {
        return m_TipoCol;
  }

  public String getNombreTipoCol()
  {
        return m_NombreTipoCol;
  }


}
