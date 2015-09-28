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
package forseti.crm;

import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JPublicInvServLineasSetV2;

public class JCRMCompaniasSes extends JSesionRegs
{
  public JCRMCompaniasSes()
  {
  }

  public JCRMCompaniasSesPart getPartida(int ind)
  {
    return (JCRMCompaniasSesPart)m_Partidas.elementAt(ind);
  }
  
  @SuppressWarnings("unchecked")
  public short agregaPartida(HttpServletRequest request, String clave, StringBuffer mensaje)
  {
    short res = -1;

    JPublicInvServLineasSetV2 lin = new JPublicInvServLineasSetV2(request);
    lin.m_Where = "Clave = '" + JUtil.p(clave) + "'";
    lin.Open();
    if(lin.getNumRows() > 0)
    {
    	if(existeEnLista(lin.getAbsRow(0).getClave()))
    	{
    		res = 1;
    		mensaje.append("PRECAUCION: La linea ya existe en la lista, No se agregó la partida");	
    	}
    	else
    	{			
    		String descripcion = lin.getAbsRow(0).getDescripcion();
    		JCRMCompaniasSesPart part = new JCRMCompaniasSesPart(clave, descripcion);
    		m_Partidas.addElement(part);
    	} 
    }
    else
    {
        res = 3;
        mensaje.append(JUtil.Msj("CEF", "INVSERV_INVSERV", "DLG", "MSJ-PROCERR", 2)); //"ERROR: La linea capturada no existe. <br>";
    }

    return res;

  }
   
  public boolean existeEnLista(String idlinea)
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JCRMCompaniasSesPart part = (JCRMCompaniasSesPart) m_Partidas.elementAt(i);
	  	String clave = part.getClave();
	  	
	  	if(clave.compareToIgnoreCase(idlinea) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }
  
  @SuppressWarnings("unchecked")
  public void agregaPartida(String clave, String descripcion)
  {
	  JCRMCompaniasSesPart part = new JCRMCompaniasSesPart(clave, descripcion);
	  m_Partidas.addElement(part);
  }

    
}
