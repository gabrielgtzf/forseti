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



public class TBL_REPORTS_SENTENCES
{
	private int m_ID_Report;
	private short m_ID_Sentence;
	private short m_ID_IsCompute;
	private String m_Select_Clause;
	private float m_TabPrintPnt;
	private String m_Format;

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

	public void setSelect_Clause(String Select_Clause)
	{
		m_Select_Clause = Select_Clause;
	}

	public void setTabPrintPnt(float TabPrintPnt)
	{
		m_TabPrintPnt = TabPrintPnt;
	}

	public void setFormat(String Format)
	{
		m_Format = Format;
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

	public String getSelect_Clause()
	{
		return m_Select_Clause;
	}

	public float getTabPrintPnt()
	{
		return m_TabPrintPnt;
	}

	public String getFormat()
	{
		return m_Format;
	}


}

