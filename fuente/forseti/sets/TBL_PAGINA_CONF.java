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

public class TBL_PAGINA_CONF
{
	private String m_ID_Configuracion;
	private String m_Tipo;
	private String m_Descripcion;
	private String m_Codigo;
	private String m_Codigo2;
	private String m_Codigo3;
	private String m_Codigo4;

	public void setID_Configuracion(String ID_Configuracion)
	{
		m_ID_Configuracion = ID_Configuracion;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCodigo(String Codigo)
	{
		m_Codigo = Codigo;
	}

	public void setCodigo2(String Codigo2)
	{
		m_Codigo2 = Codigo2;
	}

	public void setCodigo3(String Codigo3)
	{
		m_Codigo3 = Codigo3;
	}

	public void setCodigo4(String Codigo4)
	{
		m_Codigo4 = Codigo4;
	}


	public String getID_Configuracion()
	{
		return m_ID_Configuracion;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getCodigo()
	{
		return m_Codigo;
	}

	public String getCodigo2()
	{
		return m_Codigo2;
	}

	public String getCodigo3()
	{
		return m_Codigo3;
	}

	public String getCodigo4()
	{
		return m_Codigo4;
	}


}

