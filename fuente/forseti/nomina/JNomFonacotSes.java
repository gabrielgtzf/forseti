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
import java.util.Date;
 
public class JNomFonacotSes extends JSesionRegs
{

  private Date m_part_FechaDesc;
  
  public JNomFonacotSes()
  {
	  resetearPart();
  }

  private void resetearPart()
  {
	  m_part_FechaDesc = null;
  }

  public JNomFonacotSesPart getPartida(int ind)
  {
    return (JNomFonacotSesPart)m_Partidas.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, Date fechadesc, float descuento, StringBuffer mensaje)
  {
    short res = -1;
	
    m_part_FechaDesc = fechadesc;
    	
    if(existeEnLista())
    {
    	res = 1;
    	mensaje.append("PRECAUCION: La fecha del descuento ya existe en la lista, No se agregó la partida");
    }
    else
    {
    	JNomFonacotSesPart part = new JNomFonacotSesPart(fechadesc, descuento);
    	m_Partidas.addElement(part);
    	resetearPart();
    }
  
    return res;

  }

   
    @SuppressWarnings("unchecked")
  public void agregaPartida(Date fechadesc, float descuento)
  {
	 JNomFonacotSesPart part = new JNomFonacotSesPart(fechadesc, descuento);
	 m_Partidas.addElement(part);
  }

  public boolean existeEnLista()
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JNomFonacotSesPart part = (JNomFonacotSesPart) m_Partidas.elementAt(i);
	  	Date clave = part.getFechaDesc();
	  	
	  	if(clave.getTime() == m_part_FechaDesc.getTime())
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }
  
  public short editaPartida(int indPartida, HttpServletRequest request, Date fechadesc, float descuento, StringBuffer mensaje)
  {
	    short res = -1;
		
	    m_part_FechaDesc = fechadesc;
	    	
	    JNomFonacotSesPart part = (JNomFonacotSesPart) m_Partidas.elementAt(indPartida);;
		part.setPartida(fechadesc, descuento);
	    resetearPart();
	    
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
