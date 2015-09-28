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

public class view_nomina_entidades_ids
{
	private String m_ID_Usuario;
	private byte m_ID_Sucursal;
	private byte m_Tipo;
	private String m_Periodo;
	private String m_Descripcion;
	private String m_Nombre;
	private String m_Fmt_Nomina;
	private String m_Fmt_Recibo;
	private boolean m_CFD;

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setID_Sucursal(byte ID_Sucursal)
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setPeriodo(String Periodo)
	{
		m_Periodo = Periodo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFmt_Nomina(String Fmt_Nomina)
	{
		m_Fmt_Nomina = Fmt_Nomina;
	}

	public void setFmt_Recibo(String Fmt_Recibo)
	{
		m_Fmt_Recibo = Fmt_Recibo;
	}


	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public byte getID_Sucursal()
	{
		return m_ID_Sucursal;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getPeriodo()
	{
		return m_Periodo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getFmt_Nomina()
	{
		return m_Fmt_Nomina;
	}

	public String getFmt_Recibo()
	{
		return m_Fmt_Recibo;
	}

	public boolean getCFD() 
	{
		return m_CFD;
	}

	public void setCFD(boolean CFD) 
	{
		m_CFD = CFD;
	}
}

