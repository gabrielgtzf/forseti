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

public class JCatProdSesBodegas
{
  private int m_ID_Bodega;
  private String m_Nombre;
  private float m_StockMin;
  private float m_StockMax;

  public JCatProdSesBodegas()
  {
  }

  public JCatProdSesBodegas(int idbodega, String nombre, float stockmin, float stockmax )
  {
    m_ID_Bodega = idbodega;
    m_Nombre = nombre;
    m_StockMin = stockmin;
    m_StockMax = stockmax;
  }

  public void setParametros(int idbodega, String nombre, float stockmin, float stockmax )
  {
    m_ID_Bodega = idbodega;
    m_Nombre = nombre;
    m_StockMin = stockmin;
    m_StockMax = stockmax;
  }

  public int getID_Bodega()
  {
    return m_ID_Bodega;
  }

  public String getNombre()
  {
    return m_Nombre;
  }

  public float getStockMin()
  {
    return m_StockMin;
  }

  public float getStockMax()
  {
    return m_StockMax;
  }

}
