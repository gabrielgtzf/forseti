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
package forseti.sets;



public class view_public_invserv_inventarios_catalogo
{
	private String m_Clave;
	private String m_Descripcion;
	private String m_Linea;
	private float m_Existencia;
	private String m_ID_UnidadSalida;
	private boolean m_IVA;
	private float m_Precio;
	private float m_UltimoCosto;
	private String m_ID_Tipo;
	private String m_ID_Unidad;
	private float m_ImpIEPS;
	private float m_ImpIVARet;
	private float m_ImpISRRet;
	private boolean m_SeProduce;
	private float m_CostoPromedio;
	private float m_Precio2;
	private float m_Precio3;
	private float m_Precio4;
	private float m_Precio5;
	private float m_PrecioMin;
	private float m_PrecioMax;
	private float m_PrecioOfertaWeb;
	private float m_PrecioWeb;
	private String m_Status;
	
	public void setStatus(String Status)
	{
		m_Status = Status;
	}
	
	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setLinea(String Linea)
	{
		m_Linea = Linea;
	}

	public void setExistencia(float Existencia)
	{
		m_Existencia = Existencia;
	}

	public void setID_UnidadSalida(String ID_UnidadSalida)
	{
		m_ID_UnidadSalida = ID_UnidadSalida;
	}

	public void setIVA(boolean IVA)
	{
		m_IVA = IVA;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setUltimoCosto(float UltimoCosto)
	{
		m_UltimoCosto = UltimoCosto;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Unidad(String ID_Unidad)
	{
		m_ID_Unidad = ID_Unidad;
	}

	public void setImpIEPS(float ImpIEPS)
	{
		m_ImpIEPS = ImpIEPS;
	}

	public void setSeProduce(boolean SeProduce)
	{
		m_SeProduce = SeProduce;
	}

	public void setCostoPromedio(float CostoPromedio)
	{
		m_CostoPromedio = CostoPromedio;
	}

	public void setPrecio2(float Precio2)
	{
		m_Precio2 = Precio2;
	}

	public void setPrecio3(float Precio3)
	{
		m_Precio3 = Precio3;
	}

	public void setPrecio4(float Precio4)
	{
		m_Precio4 = Precio4;
	}

	public void setPrecio5(float Precio5)
	{
		m_Precio5 = Precio5;
	}

	public void setPrecioOfertaWeb(float PrecioOfertaWeb)
	{
		m_PrecioOfertaWeb = PrecioOfertaWeb;
	}

	public void setPrecioWeb(float PrecioWeb)
	{
		m_PrecioWeb = PrecioWeb;
	}

	public String getStatus()
	{
		return m_Status;
	}
	
	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getLinea()
	{
		return m_Linea;
	}

	public float getExistencia()
	{
		return m_Existencia;
	}

	public String getID_UnidadSalida()
	{
		return m_ID_UnidadSalida;
	}

	public boolean getIVA()
	{
		return m_IVA;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public float getUltimoCosto()
	{
		return m_UltimoCosto;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getID_Unidad()
	{
		return m_ID_Unidad;
	}

	public float getImpIEPS()
	{
		return m_ImpIEPS;
	}

	public float getImpIVARet()
	{
		return m_ImpIVARet;
	}
	
	public float getImpISRRet()
	{
		return m_ImpISRRet;
	}
	
	public boolean getSeProduce()
	{
		return m_SeProduce;
	}

	public float getCostoPromedio()
	{
		return m_CostoPromedio;
	}

	public float getPrecio2()
	{
		return m_Precio2;
	}

	public float getPrecio3()
	{
		return m_Precio3;
	}

	public float getPrecio4()
	{
		return m_Precio4;
	}

	public float getPrecio5()
	{
		return m_Precio5;
	}

	public float getPrecioOfertaWeb()
	{
		return m_PrecioOfertaWeb;
	}

	public float getPrecioWeb()
	{
		return m_PrecioWeb;
	}

	public float getPrecioMax() 
	{
		return m_PrecioMax;
	}

	public void setPrecioMax(float PrecioMax) 
	{
		m_PrecioMax = PrecioMax;
	}
 
	public float getPrecioMin() 
	{
		return m_PrecioMin;
	}

	public void setPrecioMin(float PrecioMin) 
	{
		m_PrecioMin = PrecioMin;
	}

	public void setImpIVARet(float ImpIVARet) 
	{
		m_ImpIVARet = ImpIVARet;	
	}

	public void setImpISRRet(float ImpISRRet) 
	{
		m_ImpISRRet = ImpISRRet;	
	}
}

