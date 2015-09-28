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



public class TBL_REPORTS_SENTENCES_COLUMNS
{
	private int m_ID_Report;
	private short m_ID_Sentence;
	private short m_ID_IsCompute;
	private short m_ID_Column;
	private String m_ColName;
	private String m_BindDataType;
	private boolean m_WillShow;
	private String m_Format;
	private float m_Ancho;
	private String m_AlinHor;
	private String m_FGColor;

	public void setID_Report(int ID_Report)
	{
		m_ID_Report = ID_Report;
	}

	public void setID_Sentence(short ID_Sentence)
	{
		m_ID_Sentence = ID_Sentence;
	}

	public void setID_IsCompute(short ID_IsCompute)
	{
		m_ID_IsCompute = ID_IsCompute;
	}

	public void setID_Column(short ID_Column)
	{
		m_ID_Column = ID_Column;
	}

	public void setColName(String ColName)
	{
		m_ColName = ColName;
	}

	public void setBindDataType(String BindDataType)
	{
		m_BindDataType = BindDataType;
	}

	public void setWillShow(boolean WillShow)
	{
		m_WillShow = WillShow;
	}

	public void setFormat(String Format)
	{
		m_Format = Format;
	}

	public void setAncho(float Ancho)
	{
		m_Ancho = Ancho;
	}

	public void setAlinHor(String AlinHor)
	{
		m_AlinHor = AlinHor;
	}

	public void setFGColor(String FGColor)
	{
		m_FGColor = FGColor;
	}


	public int getID_Report()
	{
		return m_ID_Report;
	}

	public short getID_Sentence()
	{
		return m_ID_Sentence;
	}

	public short getID_IsCompute()
	{
		return m_ID_IsCompute;
	}

	public short getID_Column()
	{
		return m_ID_Column;
	}

	public String getColName()
	{
		return m_ColName;
	}

	public String getBindDataType()
	{
		return m_BindDataType;
	}

	public boolean getWillShow()
	{
		return m_WillShow;
	}

	public String getFormat()
	{
		return m_Format;
	}

	public float getAncho()
	{
		return m_Ancho;
	}

	public String getAlinHor()
	{
		return m_AlinHor;
	}

	public String getFGColor()
	{
		return m_FGColor;
	}


}

