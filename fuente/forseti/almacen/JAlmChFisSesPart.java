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

public class JAlmChFisSesPart
{
  private String m_ID_Prod;
  private String m_Descripcion;
  private float m_Cantidad;
  private float m_DIFF;
  private String m_Unidad;
  private boolean m_Manejado;
  
  public JAlmChFisSesPart()
  {
  }

  public JAlmChFisSesPart(String idprod, String descripcion, float cantidad, float diff, String unidad, boolean manejado)
  {
	  m_ID_Prod = idprod;
	  m_Descripcion = descripcion;
	  m_Cantidad = cantidad;
	  m_DIFF = diff;
	  m_Unidad = unidad;
	  m_Manejado = manejado;
  }

  public void setParametros(String idprod, String descripcion, float cantidad, float diff, String unidad, boolean manejado)
  {
	  m_ID_Prod = idprod;
	  m_Descripcion = descripcion;
	  m_Cantidad = cantidad;
	  m_DIFF = diff;
	  m_Unidad = unidad;
	  m_Manejado = manejado;
  }
  
  public void setCantidad(float cantidad)
  {
	  m_Cantidad = cantidad;
  }
  
  public String getID_Prod()
  {
    return m_ID_Prod;
  }

  public String getDescripcion()
  {
    return m_Descripcion;
  }

  public float getCantidad()
  {
    return m_Cantidad;
  }

  public float getDIFF()
  {
    return m_DIFF;
  }

  public boolean getManejado() 
  {
	return m_Manejado;
  }

  public String getUnidad() 
  {
	return m_Unidad;
  }

}
