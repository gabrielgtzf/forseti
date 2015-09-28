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


public class TBL_CFD_FOLIOS
{
	private int m_CFD_NoAprobacion;
	private int m_CFD_AnoAprobacion;
	private String m_CFD_Serie;
	private int m_CFD_Folio;
	private int m_CFD_FolioINI;
	private int m_CFD_FolioFIN;
	
	public void setCFD_Serie(String CFD_Serie)
	{
		m_CFD_Serie = CFD_Serie;
	}

	public void setCFD_Folio(int CFD_Folio)
	{
		m_CFD_Folio = CFD_Folio;
	}

	public void setCFD_FolioINI(int CFD_FolioINI)
	{
		m_CFD_FolioINI = CFD_FolioINI;
	}

	public void setCFD_FolioFIN(int CFD_FolioFIN)
	{
		m_CFD_FolioFIN = CFD_FolioFIN;
	}

	public void setCFD_NoAprobacion(int CFD_NoAprobacion)
	{
		m_CFD_NoAprobacion = CFD_NoAprobacion;
	}

	public void setCFD_AnoAprobacion(int CFD_AnoAprobacion)
	{
		m_CFD_AnoAprobacion = CFD_AnoAprobacion;
	}

	
	public String getCFD_Serie()
	{
		return m_CFD_Serie;
	}

	public int getCFD_Folio()
	{
		return m_CFD_Folio;
	}

	public int getCFD_FolioINI()
	{
		return m_CFD_FolioINI;
	}

	public int getCFD_FolioFIN()
	{
		return m_CFD_FolioFIN;
	}

	public int getCFD_NoAprobacion()
	{
		return m_CFD_NoAprobacion;
	}

	public int getCFD_AnoAprobacion()
	{
		return m_CFD_AnoAprobacion;
	}

	

}

