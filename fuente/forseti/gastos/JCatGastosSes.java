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
package forseti.gastos;
import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JPublicContCatalogSetV2;

public class JCatGastosSes extends JSesionRegs
{
  private float m_TotalDeduccion;

  public JCatGastosSes()
  {
    m_TotalDeduccion = 0;
  }

  public float getTotalDeduccion()
  {
    return m_TotalDeduccion;
  }

  public JCatGastosSesPart getPartida(int ind)
  {
    return (JCatGastosSesPart)m_Partidas.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, String cuenta, float porcentaje, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if(set.getNumRows() > 0)
    {
      if(set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append("PRECAUCION: Al agregar, la cuenta especificada se encontró en el catálogo, pero no se puede insertar porque es una cuenta acumulativa");
      }
      else if( porcentaje < 0 )
      {
        res = 1;
        mensaje.append("PRECAUCION: El porcentaje no puede ser menor a cero");
      }
      else
      {
        String nombre = set.getAbsRow(0).getNombre();
        JCatGastosSesPart part = new JCatGastosSesPart(cuenta, nombre, porcentaje);
        m_Partidas.addElement(part);
        establecerResultados();
      }
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontró la cuenta especificada");
    }

    return res;

  }

   
  @SuppressWarnings("unchecked")
  public void agregaPartida(String cuenta, String nombre, float porcentaje)
  {
    JCatGastosSesPart part = new JCatGastosSesPart(cuenta, nombre, porcentaje);
    m_Partidas.addElement(part);
    establecerResultados();
  }

  public short editaPartida(int indPartida, HttpServletRequest request, String cuenta, float porcentaje, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if (set.getNumRows() > 0)
    {
      if (set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append("PRECAUCION: Al editar, la cuenta especificada se encontr� en el cat�logo, pero no se puede editar porque es una cuenta acumulativa");
      }
      else if ( porcentaje < 0 )
      {
        res = 3;
        mensaje.append("PRECAUCION: Al editar, el porcentaje no puede ser menor que 0");
      }
      else
      {
        String nombre = set.getAbsRow(0).getNombre();
        JCatGastosSesPart part = (JCatGastosSesPart) m_Partidas.elementAt(indPartida);
        part.setPartida(cuenta, nombre, porcentaje);
        establecerResultados();
      }
    }
    else
    {
      res = 3;
      mensaje.append("ERROR: No se encontr� la cuenta especificada");
    }

    return res;

  }

  public void establecerResultados()
  {
    float TotalDeduccion = 0;
    for(int i = 0; i < m_Partidas.size(); i++)
    {
      JCatGastosSesPart part = (JCatGastosSesPart) m_Partidas.elementAt(i);
      TotalDeduccion += part.getPorcentaje();
    }
    m_TotalDeduccion = JUtil.redondear(TotalDeduccion, 2);
  }

  public void resetear()
  {
    m_TotalDeduccion = 0;

    super.resetear();
  }

  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
    establecerResultados();
  }

}
