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


public class TBL_REPORTS_HELP
{
	private int m_ID_Report;
	private String m_Help;
	
	public void setID_Report(int ID_Report)
	{
		m_ID_Report = ID_Report;
	}

	public void setHelp(String Help)
	{
		m_Help = Help;
	}

	public int getID_Report()
	{
		return m_ID_Report;
	}

	public String getHelp()
	{
		return m_Help;
	}
	
}

