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


public class TBL_CFD_EXPREC
{
	private byte m_CFD_ID_ExpRec;
	private String m_CFD_Nombre;
	private String m_CFD_Calle;
	private String m_CFD_NoExt;
	private String m_CFD_NoInt;
	private String m_CFD_Colonia;
	private String m_CFD_Localidad;
	private String m_CFD_Municipio;
	private String m_CFD_Estado;
	private String m_CFD_Pais;
	private String m_CFD_CP;

	public void setCFD_ID_ExpRec(byte CFD_ID_ExpRec)
	{
		m_CFD_ID_ExpRec = CFD_ID_ExpRec;
	}

	public void setCFD_Calle(String CFD_Calle)
	{
		m_CFD_Calle = CFD_Calle;
	}

	public void setCFD_NoExt(String CFD_NoExt)
	{
		m_CFD_NoExt = CFD_NoExt;
	}

	public void setCFD_NoInt(String CFD_NoInt)
	{
		m_CFD_NoInt = CFD_NoInt;
	}

	public void setCFD_Colonia(String CFD_Colonia)
	{
		m_CFD_Colonia = CFD_Colonia;
	}

	public void setCFD_Localidad(String CFD_Localidad)
	{
		m_CFD_Localidad = CFD_Localidad;
	}

	public void setCFD_Municipio(String CFD_Municipio)
	{
		m_CFD_Municipio = CFD_Municipio;
	}

	public void setCFD_Estado(String CFD_Estado)
	{
		m_CFD_Estado = CFD_Estado;
	}

	public void setCFD_Pais(String CFD_Pais)
	{
		m_CFD_Pais = CFD_Pais;
	}

	public void setCFD_CP(String CFD_CP)
	{
		m_CFD_CP = CFD_CP;
	}



	public byte getCFD_ID_ExpRec()
	{
		return m_CFD_ID_ExpRec;
	}

	public String getCFD_Calle()
	{
		return m_CFD_Calle;
	}

	public String getCFD_NoExt()
	{
		return m_CFD_NoExt;
	}

	public String getCFD_NoInt()
	{
		return m_CFD_NoInt;
	}

	public String getCFD_Colonia()
	{
		return m_CFD_Colonia;
	}

	public String getCFD_Localidad()
	{
		return m_CFD_Localidad;
	}

	public String getCFD_Municipio()
	{
		return m_CFD_Municipio;
	}

	public String getCFD_Estado()
	{
		return m_CFD_Estado;
	}

	public String getCFD_Pais()
	{
		return m_CFD_Pais;
	}

	public String getCFD_CP()
	{
		return m_CFD_CP;
	}

	public void setCFD_Nombre(String CFD_Nombre) 
	{
		m_CFD_Nombre = CFD_Nombre;
	}

	public String getCFD_Nombre() 
	{
		return m_CFD_Nombre;
	}

}

