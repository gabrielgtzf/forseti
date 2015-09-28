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

import java.sql.*;

public class view_nominas_modulo
{
	private int m_ID_Nomina;
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private String m_Compania;
	private int m_Ano;
	private byte m_Tipo;
	private String m_Tipo_Nomina;
	private int m_Numero_Nomina;
	private Date m_Fecha_Desde;
	private Date m_Fecha_Hasta;
	private int m_Dias;
	private boolean m_Cerrado;
	private String m_Proteccion;
	private String m_Status;
	private int m_ID_Pol;
	private String m_Pol;
	private String m_FormaPago;
	private int m_ID_Mov;
	private String m_Pago;
	
	public String getProteccion() 
	{
		return m_Proteccion;
	}

	public int getID_Pol() 
	{
		return m_ID_Pol;
	}

	public String getPol() 
	{
		return m_Pol;
	}

	public String getFormaPago() 
	{
		return m_FormaPago;
	}

	public int getID_Mov() 
	{
		return m_ID_Mov;
	}

	public String getPago() 
	{
		return m_Pago;
	}

	public void setProteccion(String Proteccion) 
	{
		m_Proteccion = Proteccion;
	}

	public void setID_Pol(int ID_Pol) 
	{
		m_ID_Pol = ID_Pol;
	}

	public void setPol(String Pol) 
	{
		m_Pol = Pol;
	}

	public void setFormaPago(String FormaPago) 
	{
		m_FormaPago = FormaPago;
	}

	public void setID_Mov(int ID_Mov)
	{
		m_ID_Mov = ID_Mov;
	}

	public void setPago(String Pago) 
	{
		m_Pago = Pago;
	}

	public void setID_Nomina(int ID_Nomina)
	{
		m_ID_Nomina = ID_Nomina;
	}

	public void setID_Compania(byte ID_Compania)
	{
		m_ID_Compania = ID_Compania;
	}

	public void setID_Sucursal(byte ID_Sucursal)
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setTipo_Nomina(String Tipo_Nomina)
	{
		m_Tipo_Nomina = Tipo_Nomina;
	}

	public void setNumero_Nomina(int Numero_Nomina)
	{
		m_Numero_Nomina = Numero_Nomina;
	}

	public void setFecha_Desde(Date Fecha_Desde)
	{
		m_Fecha_Desde = Fecha_Desde;
	}

	public void setFecha_Hasta(Date Fecha_Hasta)
	{
		m_Fecha_Hasta = Fecha_Hasta;
	}

	public void setDias(int Dias)
	{
		m_Dias = Dias;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}


	public int getID_Nomina()
	{
		return m_ID_Nomina;
	}

	public byte getID_Compania()
	{
		return m_ID_Compania;
	}

	public byte getID_Sucursal()
	{
		return m_ID_Sucursal;
	}

	public String getCompania()
	{
		return m_Compania;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getTipo_Nomina()
	{
		return m_Tipo_Nomina;
	}

	public int getNumero_Nomina()
	{
		return m_Numero_Nomina;
	}

	public Date getFecha_Desde()
	{
		return m_Fecha_Desde;
	}

	public Date getFecha_Hasta()
	{
		return m_Fecha_Hasta;
	}

	public int getDias()
	{
		return m_Dias;
	}

	public void setTipo(byte Tipo) 
	{
		m_Tipo = Tipo;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public boolean getCerrado() 
	{
		return m_Cerrado;
	}
	
	public void setCerrado(boolean Cerrado)
	{
		m_Cerrado = Cerrado;
	}

	public String getStatus() 
	{
		return m_Status;
	}
}

