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



public class TBL_AYUDA_PAGINAS
{
	private String m_ID_Pagina;
	private String m_Descripcion;
	private String m_Busqueda;
	private String m_Cuerpo;
	private int m_Status;
	private String m_Tipo;
	private String m_ID_Alternativo;

	public void setID_Pagina(String ID_Pagina)
	{
		m_ID_Pagina = ID_Pagina;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setBusqueda(String Busqueda)
	{
		m_Busqueda = Busqueda;
	}

	public void setCuerpo(String Cuerpo)
	{
		m_Cuerpo = Cuerpo;
	}


	public String getID_Pagina()
	{
		return m_ID_Pagina;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getBusqueda()
	{
		return m_Busqueda;
	}

	public String getCuerpo()
	{
		return m_Cuerpo;
	}

	public void setStatus(int Status) 
	{
		m_Status = Status;
	}

	public int getStatus() 
	{
		return m_Status;
	}

	public void setTipo(String Tipo) 
	{
		m_Tipo = Tipo;	
	}
	
	public String getTipo() 
	{
		return m_Tipo;	
	}

	public String getID_Alternativo() 
	{
		return m_ID_Alternativo;
	}
	
	public void setID_Alternativo(String ID_Alternativo) 
	{
		m_ID_Alternativo = ID_Alternativo;
	}

}

