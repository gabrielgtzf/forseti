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


public class listas_catalogo
{
        private String m_Clave;
        private String m_Descripcion;
        private String m_Especial;

        public void setClave(String Clave)
        {
          m_Clave = Clave;
        }
        public void setDescripcion(String Descripcion)
        {
          m_Descripcion = Descripcion;
        }
        public void setEspecial(String Especial)
        {
          m_Especial = Especial;
        }

        public String getClave()
        {
          return m_Clave;
        }
        public String getDescripcion()
        {
          return m_Descripcion;
        }
        public String getEspecial()
        {
          return m_Especial;
        }


}
