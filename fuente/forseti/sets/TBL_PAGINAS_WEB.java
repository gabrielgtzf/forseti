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

public class TBL_PAGINAS_WEB
{
	private String m_ID_Pagina;
	private String m_ID_Plantilla;
	private String m_Tipo;
	private String m_Titulo;
	private String m_Descripcion;
	private String m_ID_Complemento;
	private String m_ID_Complemento2;
	private String m_ID_Complemento3;
	private String m_FormatoHTML;
	private String m_FinalHTML;

	public void setID_Pagina(String ID_Pagina)
	{
		m_ID_Pagina = ID_Pagina;
	}

	public void setID_Plantilla(String ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setTitulo(String Titulo)
	{
		m_Titulo = Titulo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Complemento(String ID_Complemento)
	{
		m_ID_Complemento = ID_Complemento;
	}

	public void setID_Complemento2(String ID_Complemento2)
	{
		m_ID_Complemento2 = ID_Complemento2;
	}

	public void setID_Complemento3(String ID_Complemento3)
	{
		m_ID_Complemento3 = ID_Complemento3;
	}

	public void setFormatoHTML(String FormatoHTML)
	{
		m_FormatoHTML = FormatoHTML;
	}

	public void setFinalHTML(String FinalHTML)
	{
		m_FinalHTML = FinalHTML;
	}


	public String getID_Pagina()
	{
		return m_ID_Pagina;
	}

	public String getID_Plantilla()
	{
		return m_ID_Plantilla;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public String getTitulo()
	{
		return m_Titulo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getID_Complemento()
	{
		return m_ID_Complemento;
	}

	public String getID_Complemento2()
	{
		return m_ID_Complemento2;
	}

	public String getID_Complemento3()
	{
		return m_ID_Complemento3;
	}

	public String getFormatoHTML()
	{
		return m_FormatoHTML;
	}

	public String getFinalHTML()
	{
		return m_FinalHTML;
	}


}

