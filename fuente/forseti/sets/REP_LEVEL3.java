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

public class REP_LEVEL3
{
	private Properties m_STS;
        public REP_LEVEL3()
        {
                m_STS = new Properties();
        }

        public void setSTS(String key, String val)
        {
          m_STS.setProperty(key, val);
        }
        public String getSTS(String key)
        {
          return m_STS.getProperty(key);
        }

}

