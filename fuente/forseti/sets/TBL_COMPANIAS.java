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

public class TBL_COMPANIAS 
{
	private byte m_ID_Compania;
	private byte m_ID_Sucursal;
	private String m_Descripcion;
	private String m_Nombre;
	private byte m_Tipo;
	private String m_Periodo;
	private String m_Fmt_Nomina;
	private String m_Fmt_Recibo;
	private byte m_Numero;
	private Date m_Fecha;
	private int m_ContCuenTipo;
	private int m_ContCuenClave;
	private int m_FijaCuenTipo;
	private int m_FijaCuenClave;
	private String m_ID_Clasificacion;
	private String m_Status;
	private String m_CFD;
	private String m_CFD_NoCertificado;
	private byte m_CFD_ID_Expedicion;
	
	public String getFmt_Nomina() 
	{
		return m_Fmt_Nomina;
	}
	public void setFmt_Nomina(String Fmt_Nomina) 
	{
		m_Fmt_Nomina = Fmt_Nomina;
	}
	public String getFmt_Recibo() 
	{
		return m_Fmt_Recibo;
	}
	public void setFmt_Recibo(String Fmt_Recibo) 
	{
		m_Fmt_Recibo = Fmt_Recibo;
	}
	public String getPeriodo() 
	{
		return m_Periodo;
	}
	public void setPeriodo(String Periodo) 
	{
		m_Periodo = Periodo;
	}
	public byte getTipo() 
	{
		return m_Tipo;
	}
	public void setTipo(byte Tipo) 
	{
		m_Tipo = Tipo;
	}
	public String getDescripcion() 
	{
		return m_Descripcion;
	}
	public void setDescripcion(String Descripcion) 
	{
		m_Descripcion = Descripcion;
	}
	public byte getID_Compania() 
	{
		return m_ID_Compania;
	}
	public void setID_Compania(byte ID_Compania) 
	{
		m_ID_Compania = ID_Compania;
	}
	public byte getID_Sucursal() 
	{
		return m_ID_Sucursal;
	}
	public void setID_Sucursal(byte ID_Sucursal) 
	{
		m_ID_Sucursal = ID_Sucursal;
	}
	public String getNombre() 
	{
		return m_Nombre;
	}
	public void setNombre(String Nombre) 
	{
		m_Nombre = Nombre;
	}
	public void setNumero(byte Numero) 
	{
		m_Numero = Numero;	
	}
	public Date getFecha() 
	{
		return m_Fecha;
	}
	public void setFecha(Date Fecha) 
	{
		m_Fecha = Fecha;
	}
	public byte getNumero() 
	{
		return m_Numero;
	}
	public void setContCuenTipo(int ContCuenTipo) 
	{
		m_ContCuenTipo = ContCuenTipo;
	}
	public void setContCuenClave(int ContCuenClave) 
	{
		m_ContCuenClave = ContCuenClave;
	}
	public void setFijaCuenTipo(int FijaCuenTipo) 
	{
		m_FijaCuenTipo = FijaCuenTipo;
	}
	public void setFijaCuenClave(int FijaCuenClave) 
	{
		m_FijaCuenClave = FijaCuenClave;
	}
	public int getContCuenTipo() 
	{
		return m_ContCuenTipo;
	}
	public int getContCuenClave() 
	{
		return m_ContCuenClave;
	}
	public int getFijaCuenTipo() 
	{
		return m_FijaCuenTipo;
	}
	public int getFijaCuenClave() 
	{
		return m_FijaCuenClave;
	}
	
	public void setID_Clasificacion(String ID_Clasificacion) 
	{
		m_ID_Clasificacion = ID_Clasificacion;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}
	
	public String getID_Clasificacion() 
	{
		return m_ID_Clasificacion;
	}

	public String getStatus() 
	{
		return m_Status;
	}
	
	public void setCFD(String CFD) 
	{
		m_CFD = CFD;
	}
	
	public void setCFD_NoCertificado(String CFD_NoCertificado) 
	{
		m_CFD_NoCertificado = CFD_NoCertificado;
	}
	
	public void setCFD_ID_Expedicion(byte CFD_ID_Expedicion) 
	{
		m_CFD_ID_Expedicion = CFD_ID_Expedicion;
	}
	
	public String getCFD() 
	{
		return m_CFD;
	}
	
	public String getCFD_NoCertificado() 
	{
		return m_CFD_NoCertificado;
	}
	
	public byte getCFD_ID_Expedicion() 
	{
		return m_CFD_ID_Expedicion;
	}

}
