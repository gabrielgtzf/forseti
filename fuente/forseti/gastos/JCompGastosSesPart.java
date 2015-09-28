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
package forseti.gastos;

public class JCompGastosSesPart
{
  private float m_Cantidad;
  private String m_Unidad;
  private String m_ID_Gasto;
  private String m_ID_GastoNombre;
  private float m_Precio;
  private float m_Importe;
  private float m_Descuento;
  private float m_IVA;
  private String m_ObsPartida;
  private String m_ID_Tipo;
  
  public JCompGastosSesPart()
  {
  }

  public JCompGastosSesPart(float Cantidad, String Unidad, String ID_Gasto, String ID_GastoNombre, float Precio, float Importe, 
		  					float Descuento, float IVA, String ObsPartida, String ID_Tipo)
  {
	m_Cantidad = Cantidad;
	m_Unidad = Unidad;
	m_ID_Gasto = ID_Gasto;
	m_ID_GastoNombre = ID_GastoNombre;
	m_Precio = Precio;
	m_Importe = Importe;
	m_Descuento = Descuento;
	m_IVA = IVA;
	m_ObsPartida = ObsPartida;
	m_ID_Tipo = ID_Tipo;
	
  }

  public void setPartida(float Cantidad, String Unidad, String ID_Gasto, String ID_GastoNombre, float Precio, float Importe, 
		  					float Descuento, float IVA, String ObsPartida, String ID_Tipo)
  {
	m_Cantidad = Cantidad;
	m_Unidad = Unidad;
	m_ID_Gasto = ID_Gasto;
	m_ID_GastoNombre = ID_GastoNombre;
	m_Precio = Precio;
	m_Importe = Importe;
	m_Descuento = Descuento;
	m_IVA = IVA;
	m_ObsPartida = ObsPartida;
	m_ID_Tipo = ID_Tipo;

  }

  public float getCantidad()
  {
    return m_Cantidad;
  }

  public String getUnidad()
  {
    return m_Unidad;
  }

  public String getID_Gasto()
  {
    return m_ID_Gasto;
  }

  public String getID_GastoNombre()
  {
    return m_ID_GastoNombre;
  }

  public float getPrecio()
  {
    return m_Precio;
  }

  public float getImporte()
  {
    return m_Importe;
  }

  public float getDescuento()
  {
    return m_Descuento;
  }

  public float getIVA()
  {
    return m_IVA;
  }
  
  public String getObsPartida()
  {
    return m_ObsPartida;
  }

  public String getID_Tipo()
  {
	  return m_ID_Tipo;
  }
}
