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
package fsi_admin;

public class JRepGenSesFiltro 
{
	private String m_Instructions;
	private boolean m_IsRange;
	private String m_PriDataName;
	private String m_PriDefault;
	private String m_SecDataName;
	private String m_SecDefault;
	private String m_BindDataType;
	private boolean m_FromCatalog;
	private int m_ID_Catalogo;
	
	public JRepGenSesFiltro(String instructions, boolean isRange, String priDataName, String priDefault, String secDataName, String secDefault, String bindDataType, boolean fromCatalog, int ID_Catalogo) 
	{
		m_Instructions = instructions;
		m_IsRange = isRange;
		m_PriDataName = priDataName;
		m_PriDefault = priDefault;
		m_SecDataName = secDataName;
		m_SecDefault = secDefault;
		m_BindDataType = bindDataType;
		m_FromCatalog = fromCatalog;
		m_ID_Catalogo = ID_Catalogo;
	}

	public void setPartida(String instructions, boolean isRange, String priDataName, String priDefault, String secDataName, String secDefault, String bindDataType, boolean fromCatalog, int ID_Catalogo) 
	{
		m_Instructions = instructions;
		m_IsRange = isRange;
		m_PriDataName = priDataName;
		m_PriDefault = priDefault;
		m_SecDataName = secDataName;
		m_SecDefault = secDefault;
		m_BindDataType = bindDataType;
		m_FromCatalog = fromCatalog;
		m_ID_Catalogo = ID_Catalogo;
	}

	public String getBindDataType() 
	{
		return m_BindDataType;
	}
	public boolean getFromCatalog() 
	{
		return m_FromCatalog;
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
	public int getID_Catalogo() 
	{
		return m_ID_Catalogo;
	}
	
}
