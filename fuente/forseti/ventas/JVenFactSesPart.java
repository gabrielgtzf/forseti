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

public class JVenFactSesPart
{
  private float m_Cantidad;
  private float m_CantidadOrig;
  private String m_Unidad;
  private String m_ID_Prod;
  private String m_ID_ProdAnt;
  private String m_ID_ProdNombre;
  private float m_Precio;
  private float m_PrecioOrig;
  private float m_Importe;
  private float m_Descuento;
  private float m_IVA;
  private float m_IEPS;
  private float m_IVARet;
  private float m_ISRRet;
  private String m_ObsPartida;
  private String m_ID_Tipo;
  private float m_ImporteDesc;
  private float m_ImporteIVA;
  private float m_ImporteIEPS;
  private float m_ImporteIVARet;
  private float m_ImporteISRRet;
  private float m_TotalPart;
  
  public JVenFactSesPart()
  {
  }

  public JVenFactSesPart(float Cantidad, String Unidad, String ID_Prod, String ID_ProdAnt, String ID_ProdNombre, float Precio, float Importe, 
		  					float Descuento, float IVA, float IEPS, float IVARet, float ISRRet, float ImporteDesc, 
		  					  float ImporteIVA, float ImporteIEPS, float ImporteIVARet, float ImporteISRRet, float TotalPart, String ObsPartida, String ID_Tipo)
  {
	m_Cantidad = Cantidad;
	m_CantidadOrig = Cantidad;
	m_Unidad = Unidad;
	m_ID_Prod = ID_Prod;
	m_ID_ProdAnt = ID_ProdAnt;
	m_ID_ProdNombre = ID_ProdNombre;
	m_Precio = Precio;
	m_PrecioOrig = Precio;
	m_Importe = Importe;
	m_Descuento = Descuento;
	m_IVA = IVA;
	m_IEPS = IEPS;
	m_IVARet = IVARet;
	m_ISRRet = ISRRet;
	m_ImporteDesc = ImporteDesc;
	m_ImporteIVA = ImporteIVA;
	m_ImporteIEPS = ImporteIEPS;
	m_ImporteIVARet = ImporteIVARet;
	m_ImporteISRRet = ImporteISRRet;
	m_TotalPart = TotalPart;
	m_ObsPartida = ObsPartida;
	m_ID_Tipo = ID_Tipo;
	
  }
  
  public void setPartida(float Cantidad, String Unidad, String ID_Prod, String ID_ProdAnt, String ID_ProdNombre, float Precio, float Importe, 
		  					float Descuento, float IVA, float IEPS, float IVARet, float ISRRet, float ImporteDesc, 
		  						float ImporteIVA, float ImporteIEPS, float ImporteIVARet, float ImporteISRRet, float TotalPart, String ObsPartida, String ID_Tipo)
  {
	m_Cantidad = Cantidad;
	m_CantidadOrig = Cantidad;
	m_Unidad = Unidad;
	m_ID_Prod = ID_Prod;
	m_ID_ProdAnt = ID_ProdAnt;
	m_ID_ProdNombre = ID_ProdNombre;
	m_Precio = Precio;
	m_PrecioOrig = Precio;
	m_Importe = Importe;
	m_ImporteDesc = ImporteDesc;
	m_ImporteIVA = ImporteIVA;
	m_ImporteIEPS = ImporteIEPS;
	m_ImporteIVARet = ImporteIVARet;
	m_ImporteISRRet = ImporteISRRet;
	m_TotalPart = TotalPart;
	m_Descuento = Descuento;
	m_IVA = IVA;
	m_IEPS = IEPS;
	m_IVARet = IVARet;
	m_ISRRet = ISRRet;
	m_ObsPartida = ObsPartida;
	m_ID_Tipo = ID_Tipo;

  }

  public void setPartida(float Cantidad, String Unidad, String ID_Prod, String ID_ProdAnt, String ID_ProdNombre, float Precio, float Importe, 
							float Descuento, float IVA, float IEPS, float IVARet, float ISRRet, float ImporteDesc, 
								float ImporteIVA, float ImporteIEPS, float ImporteIVARet, float ImporteISRRet, float TotalPart, String ObsPartida, String ID_Tipo, float CantidadOrig, float PrecioOrig)
  {
	m_Cantidad = Cantidad;
	m_CantidadOrig = CantidadOrig;
	m_Unidad = Unidad;
	m_ID_Prod = ID_Prod;
	m_ID_ProdAnt = ID_ProdAnt;
	m_ID_ProdNombre = ID_ProdNombre;
	m_Precio = Precio;
	m_PrecioOrig = PrecioOrig;
	m_Importe = Importe;
	m_ImporteDesc = ImporteDesc;
	m_ImporteIVA = ImporteIVA;
	m_ImporteIEPS = ImporteIEPS;
	m_ImporteIVARet = ImporteIVARet;
	m_ImporteISRRet = ImporteISRRet;
	m_TotalPart = TotalPart;
	m_Descuento = Descuento;
	m_IVA = IVA;
	m_IEPS = IEPS;
	m_IVARet = IVARet;
	m_ISRRet = ISRRet;
	m_ObsPartida = ObsPartida;
	m_ID_Tipo = ID_Tipo;

  }

  public void setPartida(float IVA, float IEPS, float IVARet, float ISRRet, 
			  float ImporteIVA, float ImporteIEPS, float ImporteIVARet, float ImporteISRRet, float TotalPart)
  {
	m_ImporteIVA = ImporteIVA;
	m_ImporteIEPS = ImporteIEPS;
	m_ImporteIVARet = ImporteIVARet;
	m_ImporteISRRet = ImporteISRRet;
	m_TotalPart = TotalPart;
	m_IVA = IVA;
	m_IEPS = IEPS;
	m_IVARet = IVARet;
	m_ISRRet = ISRRet;
  }

  public float getCantidad()
  {
    return m_Cantidad;
  }

  public float getCantidadOrig()
  {
    return m_CantidadOrig;
  }

  public String getUnidad()
  {
    return m_Unidad;
  }

  public String getID_Prod()
  {
    return m_ID_Prod;
  }

  public String getID_ProdAnt()
  {
    return m_ID_ProdAnt;
  }
  
  public String getID_ProdNombre()
  {
    return m_ID_ProdNombre;
  }

  public float getPrecio()
  {
    return m_Precio;
  }

  public float getPrecioOrig()
  {
    return m_PrecioOrig;
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
  
  public float getIEPS()
  {
    return m_IEPS;
  }
  
  public float getIVARet()
  {
    return m_IVARet;
  }
  
  public float getISRRet()
  {
    return m_ISRRet;
  }
  
  public String getObsPartida()
  {
    return m_ObsPartida;
  }

  public String getID_Tipo()
  {
	  return m_ID_Tipo;
  }

  public float getImporteDesc() 
  {
	  return m_ImporteDesc;
  }

  public float getImporteIVA() 
  {
	  return m_ImporteIVA;
  }

  public float getImporteIEPS() 
  {
	  return m_ImporteIEPS;
  }
  
  public float getImporteIVARet() 
  {
	  return m_ImporteIVARet;
  }
  
  public void setCantidad(float Cantidad) 
  {
	  m_Cantidad = Cantidad;
  }

  public void setUnidad(String Unidad) 
  {
	  m_Unidad = Unidad;
  }

  public void setID_ProdNombre(String ID_ProdNombre) 
  {
	  m_ID_ProdNombre = ID_ProdNombre;
  }

  public void setPrecio(float Precio) 
  {
	  m_Precio = Precio;
  }

  public void setImporte(float Importe) 
  {
	  m_Importe = Importe;
  }

  public void setDescuento(float Descuento) 
  {
	  m_Descuento = Descuento;
  }

  public void setIVA(float IVA) 
  {
	  m_IVA = IVA;
  }

  public void setIEPS(float IEPS) 
  {
	  m_IEPS = IEPS;
  }

  public void setIVARet(float IVARet) 
  {
	  m_IVARet = IVARet;
  }

  public void setISRRet(float ISRRet) 
  {
	  m_ISRRet = ISRRet;
  }

  public void setObsPartida(String ObsPartida) 
  {
	  m_ObsPartida = ObsPartida;
  }

  public void setID_Tipo(String ID_Tipo) 
  {
	  m_ID_Tipo = ID_Tipo;
  }

  public void setImporteDesc(float ImporteDesc) 
  {
	  m_ImporteDesc = ImporteDesc;
  }

  public void setImporteIVA(float ImporteIVA) 
  {
	  m_ImporteIVA = ImporteIVA;
  }

  public void setImporteIEPS(float ImporteIEPS) 
  {
	  m_ImporteIEPS = ImporteIEPS;
  }

  public void setImporteIVARet(float ImporteIVARet) 
  {
	  m_ImporteIVARet = ImporteIVARet;
  }

  public void setImporteISRRet(float ImporteISRRet) 
  {
	  m_ImporteISRRet = ImporteISRRet;
  }

  public void setTotalPart(float TotalPart) 
  {
	  m_TotalPart = TotalPart;
  }

  public float getImporteISRRet() 
  {
	  return m_ImporteISRRet;
  }
  
  public float getTotalPart() 
  {
	  return m_TotalPart;
  }
  
  public void setID_Prod(String ID_Prod)
  {
	  m_ID_Prod = ID_Prod;
  }
  
  public void setID_ProdAnt(String ID_ProdAnt) 
  {
	  m_ID_ProdAnt = ID_ProdAnt;
  }

  

}
