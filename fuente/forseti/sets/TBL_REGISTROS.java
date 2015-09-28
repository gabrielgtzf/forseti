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
import java.sql.Time;

public class TBL_REGISTROS
{
  private int m_ID_Registro;
  private String m_IP;
  private String m_Host;
  private Date m_Fecha;
  private String m_ID_Sesion;
  private Date m_FechaDesde;
  private Time m_HoraDesde;
  private Date m_FechaHasta;
  private Time m_HoraHasta;
  private String m_Status;
  private String m_FSIBD;
  private String m_ID_Usuario;
  private String m_Password;
  private String m_ID_Tipo;
  
  public Date getFechaDesde() 
  {
	return m_FechaDesde;
  }
  public void setFechaDesde(Date fechaDesde) 
  {
	m_FechaDesde = fechaDesde;
  }
  public Date getFechaHasta() 
  {
	return m_FechaHasta;
  }
  public void setFechaHasta(Date fechaHasta) 
  {
	m_FechaHasta = fechaHasta;
  }
  public String getFSIBD() 
  {
	return m_FSIBD;
  }
  public void setFSIBD(String m_fsibd) 
  {
	m_FSIBD = m_fsibd;
  }
  public String getHost() 
  {
	return m_Host;
  }
  public void setHost(String host) 
  {
	m_Host = host;
  }
  public int getID_Registro() 
  {
	return m_ID_Registro;
  }
  public void setID_Registro(int registro) 
  {
		m_ID_Registro = registro;
  }
  public String getID_Usuario() 
  {
	return m_ID_Usuario;
  }
  public void setID_Usuario(String usuario) 
  {
	m_ID_Usuario = usuario;
  }
  public String getIP() 
  {
	return m_IP;
  }
  public void setIP(String m_ip) 
  {
	m_IP = m_ip;
  }
  public String getPassword() 
  {
	return m_Password;
  }
  public void setPassword(String password) 
  {
	m_Password = password;
  }
  public String getStatus() 
  {
	return m_Status;
  }
  public void setStatus(String status) 
  {
	m_Status = status;
  }
  public Date getFecha() 
  {
	return m_Fecha;
  }
  public void setFecha(Date fecha) 
  {
	m_Fecha = fecha;
  }
  public String getID_Sesion() 
  {
	return m_ID_Sesion;
  }
  public void setID_Sesion(String sesion) 
  {
	m_ID_Sesion = sesion;
  }
  
  public void setHoraDesde(Time HoraDesde) 
  {
	m_HoraDesde = HoraDesde;	
  }
  
  public void setHoraHasta(Time HoraHasta) 
  {
	m_HoraHasta = HoraHasta;	
  }
  
  public Time getHoraDesde() 
  {
	return m_HoraDesde;
  }
 
  public Time getHoraHasta() 
  {
	return m_HoraHasta;
  }

  public void setID_Tipo(String ID_Tipo) 
  {
	m_ID_Tipo = ID_Tipo;
  }
 
  public String getID_Tipo() 
  {
	return m_ID_Tipo;
  }

}
