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

public class JVenPoliticasSesOtr
{
	  private String m_Unidad;
	  private String m_ID_Prod;
	  private String m_ID_ProdNombre;
	  private float m_Descuento;
	  private float m_Descuento2;
	  private float m_Descuento3;
	  private float m_Descuento4;
	  private float m_Descuento5;
  
	  public JVenPoliticasSesOtr()
	  {
	  }

	  public JVenPoliticasSesOtr (String Unidad, String ID_Prod, String ID_ProdNombre, float Descuento, float Descuento2, float Descuento3, float Descuento4, float Descuento5)
	  {
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_Descuento = Descuento;
		m_Descuento2 = Descuento2;
		m_Descuento3 = Descuento3;
		m_Descuento4 = Descuento4;
		m_Descuento5 = Descuento5;
		
	  }

	  public void setPartida(String Unidad, String ID_Prod, String ID_ProdNombre, float Descuento, float Descuento2, float Descuento3, float Descuento4, float Descuento5)
	  {
		m_Unidad = Unidad;
		m_ID_Prod = ID_Prod;
		m_ID_ProdNombre = ID_ProdNombre;
		m_Descuento = Descuento;
		m_Descuento2 = Descuento2;
		m_Descuento3 = Descuento3;
		m_Descuento4 = Descuento4;
		m_Descuento5 = Descuento5;
		
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

	  public float getDescuento()
	  {
	    return m_Descuento;
	  }
	  
	  public float getDescuento2()
	  {
	    return m_Descuento2;
	  }
	  
	  public float getDescuento3()
	  {
	    return m_Descuento3;
	  }
	  
	  public float getDescuento4()
	  {
	    return m_Descuento4;
	  }
	  
	  public float getDescuento5()
	  {
	    return m_Descuento5;
	  }

}
