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

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class REP_LEVEL2
{
  private Properties m_STS;
  private JReportesLevel3 m_setL3;
  private JReportesCompL1Set m_setCL3;

  public REP_LEVEL2(HttpServletRequest request)
  {
    m_STS = new Properties();
    m_setL3 = new JReportesLevel3(request);
    m_setCL3 = new JReportesCompL1Set(request);
  }

  public void setSTS(String key, String val)
  {
    m_STS.setProperty(key, val);
  }

  public String getSTS(String key)
  {
    return m_STS.getProperty(key);
  }


	public void setSetL3(JReportesLevel3 SetL3)
	{
		m_setL3 = SetL3;
	}

	public void setSetCL3(JReportesCompL1Set SetCL3)
	{
		m_setCL3 = SetCL3;
	}

	public JReportesLevel3 getSetL3()
	{
		return m_setL3;
	}

	public JReportesCompL1Set getSetCL3()
	{
		return m_setCL3;
	}


}

