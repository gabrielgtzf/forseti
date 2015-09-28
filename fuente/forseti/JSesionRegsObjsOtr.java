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
package forseti;

import java.util.Vector;

@SuppressWarnings("rawtypes")
public abstract class JSesionRegsObjsOtr
{
   
protected Vector m_Partidas;
   
protected Vector m_Objetos;
   
protected Vector m_Otros;
  
   
public JSesionRegsObjsOtr()
  {
    m_Partidas = new Vector();
    m_Objetos = new Vector();
    m_Otros = new Vector();
  }

   
public Vector getOtros()
  {
    return m_Otros;
  }
  
   
public Vector getObjetos()
  {
    return m_Objetos;
  }

   
public Vector getPartidas()
  {
    return m_Partidas;
  }
  public int numOtros()
  {
    return m_Otros.size();
  }

  public int numObjetos()
  {
    return m_Objetos.size(); 
  }

  public int numPartidas()
  {
    return m_Partidas.size();
  }

  public void resetear()
  {
	m_Otros.removeAllElements();
    m_Objetos.removeAllElements();
    m_Partidas.removeAllElements();
  }

  public void borraPartida(int indPartida)
  {
    m_Partidas.removeElementAt(indPartida);
  }
  
  public void borraPartidaObj(int indPartida)
  {
    m_Objetos.removeElementAt(indPartida);
  }
  
  public void borraPartidaOtr(int indPartida)
  {
    m_Otros.removeElementAt(indPartida);
  }


}
