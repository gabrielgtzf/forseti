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
package forseti.almacen;

public class JAlmMovimientosSesPart
{
  private float m_Cantidad;
  private String m_Unidad;
  private String m_ID_Prod;
  private String m_ID_ProdNombre;
  private float m_Costo;
  private String m_Obs;
  
  public JAlmMovimientosSesPart()
  {
  }

  public JAlmMovimientosSesPart(float Cantidad, String Unidad, String ID_Prod, String ID_ProdNombre, float Costo, String Obs)
  {
	m_Cantidad = Cantidad;
	m_Unidad = Unidad;
	m_ID_Prod = ID_Prod;
	m_ID_ProdNombre = ID_ProdNombre;
	m_Costo = Costo;
	m_Obs = Obs;
  }

  public void setPartida(float Cantidad, String Unidad, String ID_Prod, String ID_ProdNombre, float Costo, String Obs)
  {
	m_Cantidad = Cantidad;
	m_Unidad = Unidad;
	m_ID_Prod = ID_Prod;
	m_ID_ProdNombre = ID_ProdNombre;
	m_Costo = Costo;
	m_Obs = Obs;

  }

  public float getCosto()
  {
    return m_Costo;
  }

  public float getCantidad()
  {
    return m_Cantidad;
  }

  public String getUnidad()
  {
    return m_Unidad;
  }

  public String getID_Prod()
  {
    return m_ID_Prod;
  }

  public String getID_ProdNombre()
  {
    return m_ID_ProdNombre;
  }

  public String getObs() 
  {
	return m_Obs;
  }

}
