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
package forseti.admon;

public class JAdmFormatosSesPart 
{
	private String m_Etiqueta;
	private String m_Valor;
	private float m_X;
	private float m_Y;
	private float m_Anc;
	private float m_Alt;
	private String m_Hor;
	private String m_Ver;
	private boolean m_Fin;
	
	public JAdmFormatosSesPart(String etiqueta, String valor, float x, float y, float anc, float alt, String hor, String ver, boolean fin) 
	{
		m_Etiqueta = etiqueta;
		m_Valor = valor;
		m_X = x;
		m_Y = y;
		m_Anc = anc;
		m_Alt = alt;
		m_Hor = hor;
		m_Ver = ver;
		m_Fin = fin;
	}
	
	public void setPartida(String etiqueta, String valor, float x, float y, float anc, float alt, String hor, String ver, boolean fin) 
	{
		m_Etiqueta = etiqueta;
		m_Valor = valor;
		m_X = x;
		m_Y = y;
		m_Anc = anc;
		m_Alt = alt;
		m_Hor = hor;
		m_Ver = ver;
		m_Fin = fin;
	}

	public float getAlt() 
	{
		return m_Alt;
	}

	public void setAlt(float alt) 
	{
		m_Alt = alt;
	}

	public float getAnc() 
	{
		return m_Anc;
	}

	public void setAnc(float anc) 
	{
		m_Anc = anc;
	}

	public String getEtiqueta() 
	{
		return m_Etiqueta;
	}

	public void setEtiqueta(String etiqueta) 
	{
		m_Etiqueta = etiqueta;
	}

	public boolean getFin() 
	{
		return m_Fin;
	}

	public void setFin(boolean fin) 
	{
		m_Fin = fin;
	}

	public String getHor() 
	{
		return m_Hor;
	}

	public void setHor(String hor) 
	{
		m_Hor = hor;
	}

	public String getValor() 
	{
		return m_Valor;
	}

	public void setValor(String valor) 
	{
		m_Valor = valor;
	}

	public String getVer() 
	{
		return m_Ver;
	}

	public void setVer(String ver) 
	{
		m_Ver = ver;
	}

	public float getX() 
	{
		return m_X;
	}

	public void setX(float x) 
	{
		m_X = x;
	}

	public float getY() 
	{
		return m_Y;
	}

	public void setY(float y) 
	{
		m_Y = y;
	}
	
	
}
