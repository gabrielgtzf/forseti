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

import java.sql.Date;
import java.sql.Time;

public class view_mensajes_modulo
{
  /*m.FechaLeido, m.ID_Respuesta, m.ID_MensOrig
 */
        private int m_ID_Mensaje;
        private Date m_Fecha;
        private Time m_Hora;
        private String m_Titulo;
        private String m_ID_Origen;
        private String m_ID_Destino;
        private String m_Nombre;
        private boolean m_Leido;
        private Date m_FechaLeido;
        private Time m_HoraLeido;
        private boolean m_Respondido;
        private int m_ID_Respuesta;
        private int m_ID_MensOrig;

        public void setID_Mensaje(int ID_Mensaje)
        {
          m_ID_Mensaje = ID_Mensaje;
        }
        public void setFecha(Date Fecha)
        {
          m_Fecha = Fecha;
        }
        public void setHora(Time Hora)
        {
          m_Hora = Hora;
        }
        public void setTitulo(String Titulo)
        {
          m_Titulo = Titulo;
        }
        public void setID_Origen(String ID_Origen)
        {
          m_ID_Origen = ID_Origen;
        }
        public void setID_Destino(String ID_Destino)
        {
          m_ID_Destino = ID_Destino;
        }
        public void setNombre(String Nombre)
        {
          m_Nombre = Nombre;
        }
        public void setLeido(boolean Leido)
        {
          m_Leido = Leido;
        }
        public void setFechaLeido(Date FechaLeido)
        {
          m_FechaLeido = FechaLeido;
        }
        public void setHoraLeido(Time HoraLeido)
        {
          m_HoraLeido = HoraLeido;
        }
        public void setRespondido(boolean Respondido)
        {
          m_Respondido = Respondido;
        }
        public void setID_Respuesta(int ID_Respuesta)
        {
          m_ID_Respuesta = ID_Respuesta;
        }
        public void setID_MensOrig(int ID_MensOrig)
        {
          m_ID_MensOrig = ID_MensOrig;
        }

        public int getID_Mensaje()
        {
          return m_ID_Mensaje;
        }
        public Date getFecha()
        {
          return m_Fecha;
        }
        public Time getHora()
        {
          return m_Hora;
        }
        public String getTitulo()
        {
          return m_Titulo;
        }
        public String getID_Origen()
        {
          return m_ID_Origen;
        }
        public String getID_Destino()
        {
          return m_ID_Destino;
        }
        public String getNombre()
        {
          return m_Nombre;
        }
        public boolean getLeido()
        {
          return m_Leido;
        }
        public Date getFechaLeido()
        {
          return m_FechaLeido;
        }
        public Time getHoraLeido()
        {
          return m_HoraLeido;
        }
        public boolean getRespondido()
        {
          return m_Respondido;
        }
        public int getID_Respuesta()
        {
          return m_ID_Respuesta;
        }
        public int getID_MensOrig()
        {
          return  m_ID_MensOrig;
        }


}
