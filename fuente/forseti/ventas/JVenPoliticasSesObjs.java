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

public class JVenPoliticasSesObjs 
{
	private String m_ID_Tipo;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Linea;
	private String m_Unidad;
	private String m_Status;
	private float m_P1;
	private float m_P2;
	private float m_P3;
	private float m_P4;
	private float m_P5;
	private float m_PMin;
	private float m_PMax;
	private float m_PW;
	private float m_POW;
	private float m_PComp;
	private byte m_ID_Moneda;
	
	public JVenPoliticasSesObjs()
	{
	}

	public JVenPoliticasSesObjs(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float P1, float P2, float P3, float P4, float P5, float PMin, float PMax, float PW, float POW, 
				float PComp, byte ID_Moneda)
	{
		m_ID_Tipo = ID_Tipo;
		m_Clave = Clave;
		m_Descripcion = Descripcion;
		m_Linea = Linea;
		m_Unidad = Unidad;
		m_Status = Status;
		m_P1 = P1;
		m_P2 = P2;
		m_P3 = P3;
		m_P4 = P4;
		m_P5 = P5;
		m_PMin = PMin;
		m_PMax = PMax;	
		m_PW = PW;
		m_POW = POW;		
		m_PComp = PComp;
		m_ID_Moneda = ID_Moneda;
	}

	public void setPartida(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float P1, float P2, float P3, float P4, float P5, float PMin, float PMax, float PW, float POW, 
			float PComp, byte ID_Moneda)
	{
		m_ID_Tipo = ID_Tipo;
		m_Clave = Clave;
		m_Descripcion = Descripcion;
		m_Linea = Linea;
		m_Unidad = Unidad;
		m_Status = Status;
		m_P1 = P1;
		m_P2 = P2;
		m_P3 = P3;
		m_P4 = P4;
		m_P5 = P5;
		m_PMin = PMin;
		m_PMax = PMax;	
		m_PW = PW;
		m_POW = POW;		
		m_PComp = PComp;
		m_ID_Moneda = ID_Moneda;
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

	public float getPMin()
	{
		return m_PMin;
	}

	public float getPMax()
	{
		return m_PMax;
	}
	
	public float getPW()
	{
		return m_PW;
	}

	public float getPOW()
	{
		return m_POW;
	}
	public float getPComp()
	{
		return m_PComp;
	}

	public byte getID_Moneda()
	{
		return m_ID_Moneda;
	}

}