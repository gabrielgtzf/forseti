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



public class TBL_REPORTS_FILTER
{
	private int m_ID_Report;
	private short m_ID_Column;
	private String m_Instructions;
	private boolean m_IsRange;
	private String m_PriDataName;
	private String m_PriDefault;
	private String m_SecDataName;
	private String m_SecDefault;
	private String m_BindDataType;
	private boolean m_FromCatalog;
	private String m_Select_Clause;
  
	public void setID_Report(int ID_Report)
	{
		m_ID_Report = ID_Report;
	}

	public void setID_Column(short ID_Column)
	{
		m_ID_Column = ID_Column;
	}

	public void setInstructions(String Instructions)
	{
		m_Instructions = Instructions;
	}

	public void setIsRange(boolean IsRange)
	{
		m_IsRange = IsRange;
	}

	public void setPriDataName(String PriDataName)
	{
		m_PriDataName = PriDataName;
	}

	public void setPriDefault(String PriDefault)
	{
		m_PriDefault = PriDefault;
	}

	public void setSecDataName(String SecDataName)
	{
		m_SecDataName = SecDataName;
	}

	public void setSecDefault(String SecDefault)
	{
		m_SecDefault = SecDefault;
	}

	public void setBindDataType(String BindDataType)
	{
		m_BindDataType = BindDataType;
	}

	public void setFromCatalog(boolean FromCatalog)
	{
		m_FromCatalog = FromCatalog;
	}

	public void setSelect_Clause(String Select_Clause)
	{
		m_Select_Clause = Select_Clause;
	}


	public int getID_Report()
	{
		return m_ID_Report;
	}

	public short getID_Column()
	{
		return m_ID_Column;
	}

	public String getInstructions()
	{
		return m_Instructions;
	}

	public boolean getIsRange()
	{
		return m_IsRange;
	}

	public String getPriDataName()
	{
		return m_PriDataName;
	}

	public String getPriDefault()
	{
		return m_PriDefault;
	}

	public String getSecDataName()
	{
		return m_SecDataName;
	}

	public String getSecDefault()
	{
		return m_SecDefault;
	}

	public String getBindDataType()
	{
		return m_BindDataType;
	}

	public boolean getFromCatalog()
	{
		return m_FromCatalog;
	}

	public String getSelect_Clause()
	{
		return m_Select_Clause;
	}


}

