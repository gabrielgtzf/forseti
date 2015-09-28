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

public class REP_LEVEL1
{
  private Properties m_STS;
  private JReportesLevel2 m_setL2;
  private JReportesCompL1Set m_setCL2;

  public REP_LEVEL1(HttpServletRequest request)
  {
    m_STS = new Properties();
    m_setL2 = new JReportesLevel2(request);
    m_setCL2 = new JReportesCompL1Set(request);
  }

  public void setSTS(String key, String val)
  {
    m_STS.setProperty(key, val);
  }

  public String getSTS(String key)
  {
	
    return m_STS.getProperty(key);
  }

  ///////////////////////////
  public void setSetL2(JReportesLevel2 SetL2)
  {
    m_setL2 = SetL2;
  }

  public void setSetCL2(JReportesCompL1Set SetCL2)
  {
    m_setCL2 = SetCL2;
  }

  public JReportesLevel2 getSetL2()
  {
    return m_setL2;
  }

  public JReportesCompL1Set getSetCL2()
  {
    return m_setCL2;
  }

}

