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

public class TBL_BD
{
  private String m_ID_BD;
  private String m_Nombre;
  private String m_Usuario;
  private String m_Password;
  private Date m_Fechaalta;
  private String m_Compania;
  private String m_Direccion;
  private String m_Poblacion;
  private String m_CP;
  private String m_Mail;
  private String m_Web;
  private String m_SU;

  public Date getFechaalta() 
  {
	return m_Fechaalta;
  }

  public void setFechaalta(Date Fechaalta) 
  {
	m_Fechaalta = Fechaalta;
  }

  public String getDireccion() 
  {
	return m_Direccion;
  }
  
  public void setDireccion(String Direccion) 
  {
	m_Direccion = Direccion;
  }
  
  public String getPoblacion() 
  {
	return m_Poblacion;
  }
  
  public void setPoblacion(String Poblacion) 
  {
	m_Poblacion = Poblacion;
  }

  public String getCP() 
  {
	return m_CP;
  }

  public void setCP(String CP) 
  {
	m_CP = CP;
  }

  public String getMail() 
  {
	return m_Mail;
  }

  public void setMail(String Mail) 
  {
	m_Mail = Mail;
  }

  public String getWeb() 
  {
	return m_Web;
  }

  public void setWeb(String Web) 
  {
	m_Web = Web;
  }

  
  
  public void setID_BD(String ID_BD)
  {
     m_ID_BD = ID_BD;
  }
  public void setNombre(String Nombre)
  {
     m_Nombre = Nombre;
  }
  public void setUsuario(String Usuario)
  {
     m_Usuario = Usuario;
  }
  public void setPassword(String Password)
  {
     m_Password = Password;
  }

  public String getID_BD()
  {
      return m_ID_BD;
  }
  public String getNombre()
  {
      return m_Nombre;
  }
  public String getUsuario()
  {
      return m_Usuario;
  }
  public String getPassword()
  {
      return m_Password;
  }
  public String getCompania() 
  {
	 return m_Compania;
  }
  public void setCompania(String Compania) 
  {
	 m_Compania = Compania;
  }
  public String getSU() 
  {
	return m_SU;
  }
  public void setSU(String SU) 
  {
	m_SU = SU;
  }

}
