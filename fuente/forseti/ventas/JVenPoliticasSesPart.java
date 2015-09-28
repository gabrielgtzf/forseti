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

public class JVenPoliticasSesPart 
{
	  private String m_Unidad;
	  private String m_ID_Prod;
	  private String m_ID_ProdNombre;
	  private float m_Precio;
	  private byte m_ID_Moneda;
	  private String m_Moneda;
	  
	  public JVenPoliticasSesPart()
	  {
	  }

	  public JVenPoliticasSesPart(String Unidad, String ID_Prod, String ID_ProdNombre, float Precio, byte ID_Moneda, String Moneda)
	  {
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_Precio = Precio;
		m_ID_Moneda = ID_Moneda;
		m_Moneda = Moneda;
		
	  }

	  public void setPartida(String Unidad, String ID_Prod, String ID_ProdNombre, float Precio, byte ID_Moneda, String Moneda)
	  {
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_Precio = Precio;
		m_ID_Moneda = ID_Moneda;
		m_Moneda = Moneda;

	  }

	  public byte getID_Moneda()
	  {
	    return m_ID_Moneda;
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

	  public String getMoneda()
	  {
	    return m_Moneda;
	  }

	  

}
