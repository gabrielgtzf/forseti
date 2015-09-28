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

public class JNomMovDirSesPart
{
  private int m_ID_Movimiento;
  private String m_Descripcion;
  private float m_Gravado;
  private float m_Exento;
  private float m_Deduccion;
  private boolean m_EsDeduccion;

  public JNomMovDirSesPart()
  {
  }

  public JNomMovDirSesPart(int ID_Movimiento, String Descripcion, float Gravado, float Exento, float Deduccion, boolean EsDeduccion )
  {
	 m_ID_Movimiento = ID_Movimiento;
	 m_Descripcion = Descripcion;
	 m_Gravado = Gravado;
	 m_Exento = Exento;
	 m_Deduccion = Deduccion;
	 m_EsDeduccion = EsDeduccion;
  }

  public void setPartida(int ID_Movimiento, String Descripcion, float Gravado, float Exento, float Deduccion, boolean EsDeduccion )
  {
	  m_ID_Movimiento = ID_Movimiento;
	  m_Descripcion = Descripcion;
	  m_Gravado = Gravado;
	  m_Exento = Exento;
	  m_Deduccion = Deduccion;
	  m_EsDeduccion = EsDeduccion;
  }

  public void setID_Movimiento(int ID_Movimiento)
  {
	  m_ID_Movimiento = ID_Movimiento;
  }

  public int getID_Movimiento()
  {
	  return m_ID_Movimiento;
  }

  public float getDeduccion() 
  {	
	  return m_Deduccion;
  }

  public String getDescripcion() 
  {
	  return m_Descripcion;
  }

  public boolean getEsDeduccion() 
  {
	  return m_EsDeduccion;
  }

  public float getExento() 
  {
	  return m_Exento;
  }

  public float getGravado() 
  {
	  return m_Gravado;
  }

}
