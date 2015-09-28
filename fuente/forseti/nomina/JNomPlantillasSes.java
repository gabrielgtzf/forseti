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
import forseti.sets.JMasempSet;

public class JNomPlantillasSes extends JSesionRegs
{

  private String m_part_Clave;
  
  public JNomPlantillasSes()
  {
	  resetearPart();
  }

  private void resetearPart()
  {
	  m_part_Clave = "";
  }

  public JNomPlantillasSesPart getPartida(int ind)
  {
    return (JNomPlantillasSesPart)m_Partidas.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, String id_empleado, StringBuffer mensaje)
  {
    short res = -1;
	
    JMasempSet set = new JMasempSet(request);
	set.m_Where = "ID_Empleado = '" + JUtil.p(id_empleado) + "'";
	set.Open();

    if(set.getNumRows() > 0)
    {
    	m_part_Clave = set.getAbsRow(0).getID_Empleado();
    	
    	if(existeEnLista())
    	{
    		res = 1;
    		mensaje.append("PRECAUCION: El empleado ya existe en la lista, No se agregó la partida");
    	}
    	else
    	{
    		String nombre = set.getAbsRow(0).getNombre();
    		JNomPlantillasSesPart part = new JNomPlantillasSesPart(id_empleado, nombre);
    		m_Partidas.addElement(part);
    		resetearPart();
    	}
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: El empleado no existe<br>");
    }

    return res;

  }

   
    @SuppressWarnings("unchecked")
  public void agregaPartida(String id_empleado, String nombre)
  {
	 JNomPlantillasSesPart part = new JNomPlantillasSesPart(id_empleado, nombre);
	 m_Partidas.addElement(part);
  }

  public boolean existeEnLista()
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JNomPlantillasSesPart part = (JNomPlantillasSesPart) m_Partidas.elementAt(i);
	  	String clave = part.getID_Empleado();
	  	
	  	if(clave.compareToIgnoreCase(m_part_Clave) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }
  
  public short editaPartida(int indPartida, HttpServletRequest request, String id_empleado, StringBuffer mensaje)
  {
	    short res = -1;
		
	    JMasempSet set = new JMasempSet(request);
		set.m_Where = "ID_Empleado = '" + JUtil.p(id_empleado) + "'";
		set.Open();

	    if(set.getNumRows() > 0)
	    {
	      String nombre = set.getAbsRow(0).getNombre();
	      JNomPlantillasSesPart part = (JNomPlantillasSesPart) m_Partidas.elementAt(indPartida);;
	      part.setPartida(id_empleado, nombre);
	    }
	    else
	    {
	        res = 3;
	        mensaje.append("ERROR: El empleado no existe<br>");
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

}
