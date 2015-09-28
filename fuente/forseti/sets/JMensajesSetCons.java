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



public class JMensajesSetCons extends JManejadorSet
{
        public JMensajesSetCons(HttpServletRequest request)
        {
                m_Select = " * FROM view_mensajes_cons ";
                m_PageSize = 50;
                this.request = request;
        }

        public view_mensajes_cons getRow(int row)
        {
                return (view_mensajes_cons)m_Rows.elementAt((getFloorRow() + row));
        }

        public view_mensajes_cons getAbsRow(int row)
        {
                return (view_mensajes_cons)m_Rows.elementAt(row);
        }

         
	  @SuppressWarnings("unchecked")
  protected void BindRow()
        {
                try
                {
                        view_mensajes_cons pNode = new view_mensajes_cons();

                        pNode.setID_Mensaje(m_RS.getInt("ID_Mensaje"));
                        pNode.setFecha(m_RS.getDate("Fecha"));
                        pNode.setHora(m_RS.getTime("Fecha"));
                        pNode.setTitulo(m_RS.getString("Titulo"));
                        pNode.setMensaje(m_RS.getString("Mensaje"));
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
