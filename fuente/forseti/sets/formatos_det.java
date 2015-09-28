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


public class formatos_det
{
	private String m_ID_Formato;
	private int m_ID_Part;
	private String m_Etiqueta;
	private String m_Valor;
	private float m_XPos;
	private float m_YPos;
	private float m_Ancho;
	private float m_Alto;
	private String m_Formato;
	private String m_FGColor;
	private String m_AlinHor;
	private String m_AlinVer;

	public void setID_Formato(String ID_Formato)
	{
		m_ID_Formato = ID_Formato;
	}

	public void setID_Part(int ID_Part)
	{
		m_ID_Part = ID_Part;
	}

	public void setEtiqueta(String Etiqueta)
	{
		m_Etiqueta = Etiqueta;
	}

	public void setValor(String Valor)
	{
		m_Valor = Valor;
	}

	public void setXPos(float XPos)
	{
		m_XPos = XPos;
	}

	public void setYPos(float YPos)
	{
		m_YPos = YPos;
	}

	public void setAncho(float Ancho)
	{
		m_Ancho = Ancho;
	}

	public void setAlto(float Alto)
	{
		m_Alto = Alto;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}

	public void setFGColor(String FGColor)
	{
		m_FGColor = FGColor;
	}

	public void setAlinHor(String AlinHor)
	{
		m_AlinHor = AlinHor;
	}

	public void setAlinVer(String AlinVer)
	{
		m_AlinVer = AlinVer;
	}


	public String getID_Formato()
	{
		return m_ID_Formato;
	}

	public int getID_Part()
	{
		return m_ID_Part;
	}

	public String getEtiqueta()
	{
		return m_Etiqueta;
	}

	public String getValor()
	{
		return m_Valor;
	}

	public float getXPos()
	{
		return m_XPos;
	}

	public float getYPos()
	{
		return m_YPos;
	}

	public float getAncho()
	{
		return m_Ancho;
	}

	public float getAlto()
	{
		return m_Alto;
	}

	public String getFormato()
	{
		return m_Formato;
	}

	public String getFGColor()
	{
		return m_FGColor;
	}

	public String getAlinHor()
	{
		return m_AlinHor;
	}

	public String getAlinVer()
	{
		return m_AlinVer;
	}


}

