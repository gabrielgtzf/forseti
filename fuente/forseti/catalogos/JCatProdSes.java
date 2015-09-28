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
package forseti.catalogos;
import forseti.JSesionRegsObjs;

public class JCatProdSes extends JSesionRegsObjs
{
  public JCatProdSes()
  {

  }

   
@SuppressWarnings("unchecked")
public void agregaBodega(int idbodega, String nombre, float stockmin, float stockmax)
  {
    JCatProdSesBodegas obj = new JCatProdSesBodegas(idbodega, nombre, stockmin, stockmax);
    m_Objetos.addElement(obj);
  }

   
@SuppressWarnings("unchecked")
public void agregaLinea(String idlinea, String descripcion, boolean activada)
  {
    JCatProdSesLineas part = new JCatProdSesLineas(idlinea, descripcion, activada);
    m_Partidas.addElement(part);
  }

  public JCatProdSesBodegas getObjeto(int ind)
  {
    return (JCatProdSesBodegas)m_Objetos.elementAt(ind);
  }

  public JCatProdSesLineas getPartida(int ind)
  {
    return (JCatProdSesLineas)m_Partidas.elementAt(ind);
  }


}
