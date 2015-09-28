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
 
public class tbl_srv_servicios_bd
{
	private String m_ID_Servidor;
	private String m_Basedatos;
	private String m_Status;
	private double m_CostoMail;
	private double m_CostoS3MB;
	private double m_CostoSello;
	private double m_Saldo;
	private boolean m_CobrarSello;
	private boolean m_CobrarMail;
	private boolean m_CobrarS3MB;

	public void setID_Servidor(String ID_Servidor)
	{
		m_ID_Servidor = ID_Servidor;
	}

	public void setBasedatos(String Basedatos)
	{
		m_Basedatos = Basedatos;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setCostoMail(double CostoMail)
	{
		m_CostoMail = CostoMail;
	}

	public void setCostoS3MB(double CostoS3MB)
	{
		m_CostoS3MB = CostoS3MB;
	}

	public void setCostoSello(double CostoSello)
	{
		m_CostoSello = CostoSello;
	}

	public void setSaldo(double Saldo)
	{
		m_Saldo = Saldo;
	}

	public void setCobrarSello(boolean CobrarSello)
	{
		m_CobrarSello = CobrarSello;
	}

	public void setCobrarMail(boolean CobrarMail)
	{
		m_CobrarMail = CobrarMail;
	}

	public void setCobrarS3MB(boolean CobrarS3MB)
	{
		m_CobrarS3MB = CobrarS3MB;
	}


	public String getID_Servidor()
	{
		return m_ID_Servidor;
	}

	public String getBasedatos()
	{
		return m_Basedatos;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public double getCostoMail()
	{
		return m_CostoMail;
	}

	public double getCostoS3MB()
	{
		return m_CostoS3MB;
	}

	public double getCostoSello()
	{
		return m_CostoSello;
	}

	public double getSaldo()
	{
		return m_Saldo;
	}

	public boolean getCobrarSello()
	{
		return m_CobrarSello;
	}

	public boolean getCobrarMail()
	{
		return m_CobrarMail;
	}

	public boolean getCobrarS3MB()
	{
		return m_CobrarS3MB;
	}


}

