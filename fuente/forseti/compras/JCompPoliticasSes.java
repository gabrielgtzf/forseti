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
package forseti.compras;

import forseti.JSesionRegsObjsOtr;

public class JCompPoliticasSes extends JSesionRegsObjsOtr
{

  private int m_ClaveProvee;
  private int m_NumeroProvee;
  private short m_ID_EntidadCompra;
  private String m_EntidadCompra;
  private String m_NombreProveedor;
  
  
  public JCompPoliticasSes()
  {
	  m_ClaveProvee = 0;
	  m_NumeroProvee = 0;
	  m_ID_EntidadCompra = 0;
	  m_EntidadCompra = "";
	  m_NombreProveedor = "";
  
  }

  public void setParametros(int ClaveProvee, int NumeroProvee, short ID_EntidadCompra, String EntidadCompra, String NombreProveedor)
  {
	  m_ClaveProvee = ClaveProvee;
	  m_NumeroProvee = NumeroProvee;
	  m_ID_EntidadCompra = ID_EntidadCompra;
	  m_EntidadCompra = EntidadCompra;
	  m_NombreProveedor = NombreProveedor;
  }

  public JCompPoliticasSesObjs getObjeto(int ind)
  {
    return (JCompPoliticasSesObjs)m_Objetos.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
  public void agregaObjeto(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float PComp, byte ID_Moneda)
  {
	 JCompPoliticasSesObjs part = new JCompPoliticasSesObjs(ID_Tipo, Clave, Descripcion, Linea, 
			 	Unidad, Status, PComp, ID_Moneda);
	 m_Objetos.addElement(part);

  }
     
  public void resetear()
  {
	  m_ClaveProvee = 0;
	  m_NumeroProvee = 0;
	  m_ID_EntidadCompra = 0;
	  m_EntidadCompra = "";
	  m_NombreProveedor = "";
      
	  super.resetear();
      
  }

  public int getClaveProvee() 
  {
	return m_ClaveProvee;
  }

  public void setClaveProvee(int ClaveProvee) 
  {
	m_ClaveProvee = ClaveProvee;
  }

  public String getEntidadCompra() 
  {
	return m_EntidadCompra;
  }

  public void set_EntidadCompra(String EntidadCompra) 
  {
	m_EntidadCompra = EntidadCompra;
  }

  public short getID_EntidadCompra() 
  {
	return m_ID_EntidadCompra;
  }

  public void setID_EntidadCompra(short ID_EntidadCompra) 
  {
	m_ID_EntidadCompra = ID_EntidadCompra;
  }

  public String getNombreProveedor() 
  {
	return m_NombreProveedor;
  }

  public void setNombreProveedor(String NombreProveedor) 
  {
	m_NombreProveedor = NombreProveedor;
  }

  public int getNumeroProvee() 
  {
	return m_NumeroProvee;
  }

  public void setNumeroProvee(int NumeroProvee) 
  {
	m_NumeroProvee = NumeroProvee;
  }

}
