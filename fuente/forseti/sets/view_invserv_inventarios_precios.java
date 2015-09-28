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



public class view_invserv_inventarios_precios
{
	private String m_ID_Tipo;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Linea;
	private String m_Unidad;
	private String m_Status;
	private boolean m_SeProduce;
	private boolean m_NoSeVende;
	private float m_P1;
	private float m_P2;
	private float m_P3;
	private float m_P4;
	private float m_P5;
	private float m_PW;
	private float m_POW;
	private float m_PMin;
	private float m_PMax;
	private float m_PComp;
	private byte m_ID_Moneda;
	
	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
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

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setSeProduce(boolean SeProduce)
	{
		m_SeProduce = SeProduce;
	}
	
	public boolean getSetProduce()
	{
		return m_SeProduce;
	}
	
	public void setP1(float P1)
	{
		m_P1 = P1;
	}

	public void setP2(float P2)
	{
		m_P2 = P2;
	}

	public void setP3(float P3)
	{
		m_P3 = P3;
	}

	public void setP4(float P4)
	{
		m_P4 = P4;
	}

	public void setP5(float P5)
	{
		m_P5 = P5;
	}

	public void setPW(float PW)
	{
		m_PW = PW;
	}

	public void setPOW(float POW)
	{
		m_POW = POW;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
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

	public String getUnidad()
	{
		return m_Unidad;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getP1()
	{
		return m_P1;
	}

	public float getP2()
	{
		return m_P2;
	}

	public float getP3()
	{
		return m_P3;
	}

	public float getP4()
	{
		return m_P4;
	}

	public float getP5()
	{
		return m_P5;
	}

	public float getPW()
	{
		return m_PW;
	}

	public float getPOW()
	{
		return m_POW;
	}

	public float getPMax() 
	{
		return m_PMax;
	}

	public void setPMax(float PMax) 
	{
		m_PMax = PMax;
	}

	public float getPMin() 
	{
		return m_PMin;
	}

	public void setPMin(float PMin) 
	{
		m_PMin = PMin;
	}

	public float getPComp() 
	{
		return m_PComp;
	}

	public void setPComp(float PComp) 
	{
		m_PComp = PComp;
	}

	public byte getID_Moneda() 
	{
		return m_ID_Moneda;
	}

	public void setID_Moneda(byte ID_Moneda) 
	{
		m_ID_Moneda = ID_Moneda;
	}

	public boolean getNoSeVende()
	{
		return m_NoSeVende;
	}

	public void setNoSeVende(boolean NoSeVende) 
	{
		m_NoSeVende = NoSeVende;
	}

	
}

