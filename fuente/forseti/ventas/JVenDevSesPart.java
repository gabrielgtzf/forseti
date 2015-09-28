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
package forseti.ventas;

public class JVenDevSesPart 
{
	  private float m_CantidadFact;
	  private float m_Cantidad;
	  private String m_Unidad;
	  private String m_ID_Prod;
	  private String m_ID_ProdNombre;
	  private float m_PrecioFact;
	  private float m_Precio;
	  private float m_Importe;
	  private float m_IVA;
	  private String m_ObsPartida;
	  private String m_ID_Tipo;
	  
	  public JVenDevSesPart()
	  {
	  }

	  public JVenDevSesPart(float Cantidad, String Unidad, String ID_Prod, String ID_ProdNombre, float Precio, float Importe, 
			  					float IVA, String ObsPartida, String ID_Tipo)
	  {
		m_CantidadFact = Cantidad;
		m_Cantidad = Cantidad;
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_PrecioFact = Precio;
		m_Precio = Precio;
		m_Importe = Importe;
		m_IVA = IVA;
		m_ObsPartida = ObsPartida;
		m_ID_Tipo = ID_Tipo;
		
	  }

	  public void setPartida(float Cantidad, String Unidad, String ID_Prod, String ID_ProdNombre, float Precio, float Importe, 
			  					float IVA, String ObsPartida, String ID_Tipo)
	  {
		m_Cantidad = Cantidad;
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_Precio = Precio;
		m_Importe = Importe;
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

	  public String getID_Prod()
	  {
	    return m_ID_Prod;
	  }

	  public String getID_ProdNombre()
	  {
	    return m_ID_ProdNombre;
	  }

	  public float getPrecio()
	  {
	    return m_Precio;
	  }

	  public float getImporte()
	  {
	    return m_Importe;
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

	  public float getCantidadFact() 
	  {
		return m_CantidadFact;
	  }

	  public void setCantidadFact(float cantidadFact) 
	  {
		m_CantidadFact = cantidadFact;
	  }

	  public float getPrecioFact() 
	  {
		return m_PrecioFact;
	  }

	  public void setPrecioFact(float precioFact) 
	  {
		m_PrecioFact = precioFact;
	  }
}
