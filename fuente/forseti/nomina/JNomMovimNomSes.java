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
package forseti.nomina;
import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JDepartamentosSet;

public class JNomMovimNomSes extends JSesionRegs
{
  //private String m_part_Cuenta;
  private String m_part_ID_Departamento;
	
  public JNomMovimNomSes()
  {
	  
  }

  public JNomMovimNomSesPart getPartida(int ind)
  {
    return (JNomMovimNomSesPart)m_Partidas.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, String cuenta, String id_departamento, StringBuffer mensaje)
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
        mensaje.append("PRECAUCION: Al agregar, la cuenta especificada se encontr&oacute; en el cat&aacute;logo, pero no se puede insertar porque es una cuenta acumulativa");
      }
      else
      {
    	//m_part_Cuenta = set.getAbsRow(0).getNumero();
        String nombre = set.getAbsRow(0).getNombre();
        JDepartamentosSet dep = new JDepartamentosSet(request);
        dep.m_Where = "ID_Departamento = '" + JUtil.p(id_departamento) + "'";
        dep.Open();
        if(dep.getNumRows() > 0)
        {
        	m_part_ID_Departamento = dep.getAbsRow(0).getID_Departamento();
        	
        	if(existeEnLista())
			{
        		res = 1;
        		mensaje.append("PRECAUCION: La cuenta y departamento ya estan en la lista, No se agreg&oacute; la partida");
			}
        	else
        	{
        		String nombre_departamento = dep.getAbsRow(0).getNombre();
        		JNomMovimNomSesPart part = new JNomMovimNomSesPart(cuenta, nombre, id_departamento, nombre_departamento);
        		m_Partidas.addElement(part);
        		
        		resetearPart();
        	}
        }
        else
        {
            res = 3;
            mensaje.append("ERROR: No se encontr&oacute; el departamento especificado");
        }
      }
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontr&oacute; la cuenta especificada");
    }

    return res;

  }
   
  @SuppressWarnings("unchecked")
  public void agregaPartida(String cuenta, String nombre, String id_departamento, String nombre_departamento)
  {
	JNomMovimNomSesPart part = new JNomMovimNomSesPart(cuenta, nombre, id_departamento, nombre_departamento);
    m_Partidas.addElement(part);
  }

  public short editaPartida(int indPartida, HttpServletRequest request, String cuenta, String id_departamento, StringBuffer mensaje)
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
        mensaje.append("PRECAUCION: Al editar, la cuenta especificada se encontr&oacute; en el cat&aacute;logo, pero no se puede editar porque es una cuenta acumulativa");
      }
      else
      {
    	//m_part_Cuenta = set.getAbsRow(0).getNumero();
        String nombre = set.getAbsRow(0).getNombre();
        JDepartamentosSet dep = new JDepartamentosSet(request);
        dep.m_Where = "ID_Departamento = '" + JUtil.p(id_departamento) + "'";
        dep.Open();
        if(dep.getNumRows() > 0)
        {
        	m_part_ID_Departamento = dep.getAbsRow(0).getID_Departamento();
        	
        	if(existeEnLista())
			{
        		res = 1;
        		mensaje.append("PRECAUCION: La cuenta y departamento ya estan en la lista, No se cambi&oacute; la partida");
			}
        	else
        	{
        		String nombre_departamento = dep.getAbsRow(0).getNombre();
        		JNomMovimNomSesPart part = (JNomMovimNomSesPart) m_Partidas.elementAt(indPartida);
        		part.setPartida(cuenta, nombre, id_departamento, nombre_departamento);
        		
        		resetearPart();
        	}
        }
        else
        {
            res = 3;
            mensaje.append("ERROR: No se encontr&oacute; el departamento especificado");
        }
      }
    }
    else
    {
      res = 3;
      mensaje.append("ERROR: No se encontr&oacute; la cuenta especificada");
    }

    return res;

  }

  public void resetear()
  {
	  resetearPart();
	  
	  super.resetear();
  }

  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
  }

  public boolean existeEnLista()
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JNomMovimNomSesPart part = (JNomMovimNomSesPart) m_Partidas.elementAt(i);
	  	String id_departamento = part.getID_Departamento();
	  	
	  	if(id_departamento.compareToIgnoreCase(m_part_ID_Departamento) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }
  
  private void resetearPart()
  {
	  //m_part_Cuenta = "";
	  m_part_ID_Departamento = "";
  }
}
