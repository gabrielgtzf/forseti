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

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import forseti.JManejadorSet;


public class JMensajesSet extends JManejadorSet
{
        public JMensajesSet(HttpServletRequest request, String entidad)
        {
                if(entidad == null)
                  m_Select = " * FROM view_mensajes_modulo ";
                else if(entidad.equals("PARA_MI"))
                  m_Select = " * FROM view_mensajes_para_mi_modulo ";
                else if(entidad.equals("MIOS"))
                  m_Select = " * FROM view_mensajes_mios_modulo ";
                else
                  m_Select = " * FROM view_mensajes_modulo ";

                m_PageSize = 50;
                this.request = request;
        }

        public view_mensajes_modulo getRow(int row)
        {
                return (view_mensajes_modulo)m_Rows.elementAt((getFloorRow() + row));
        }

        public view_mensajes_modulo getAbsRow(int row)
        {
                return (view_mensajes_modulo)m_Rows.elementAt(row);
        }

         
	  @SuppressWarnings("unchecked")
  protected void BindRow()
        {
                try
                {
                        view_mensajes_modulo pNode = new view_mensajes_modulo();

                        pNode.setID_Mensaje(m_RS.getInt("ID_Mensaje"));
                        pNode.setFecha(m_RS.getDate("Fecha"));
                        pNode.setHora(m_RS.getTime("Fecha"));
                        pNode.setTitulo(m_RS.getString("Titulo"));
                        pNode.setID_Origen(m_RS.getString("ID_Origen"));
                        pNode.setID_Destino(m_RS.getString("ID_Destino"));
                        pNode.setNombre(m_RS.getString("Nombre"));
                        pNode.setLeido(m_RS.getBoolean("Leido"));
                        pNode.setFechaLeido(m_RS.getDate("FechaLeido"));
                        pNode.setHoraLeido(m_RS.getTime("FechaLeido"));
                        pNode.setRespondido(m_RS.getBoolean("Respondido"));
                        pNode.setID_Respuesta(m_RS.getInt("ID_Respuesta"));
                        pNode.setID_MensOrig(m_RS.getInt("ID_MensOrig"));

                        m_Rows.addElement(pNode);

                }
                catch(SQLException e)
                {
                        e.printStackTrace();
                        throw new RuntimeException(e.toString());
                }

        }

}
