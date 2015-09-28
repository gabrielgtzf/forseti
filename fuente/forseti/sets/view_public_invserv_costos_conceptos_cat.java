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

public class view_public_invserv_costos_conceptos_cat
{
	private short m_ID_Concepto;
	private String m_Descripcion;
	private boolean m_DeSistema;
	private boolean m_RecalcularCosto;
	private String m_Tipo;
	
	public void setID_Concepto(short ID_Concepto)
	{
		m_ID_Concepto = ID_Concepto;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}
	
	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}
	
	public void setDeSistema(boolean DeSistema)
	{
		m_DeSistema = DeSistema;
	}

	public void setRecalcularCosto(boolean RecalcularCosto)
	{
		m_RecalcularCosto = RecalcularCosto;
	}


	public short getID_Concepto()
	{
		return m_ID_Concepto;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getTipo()
	{
		return m_Tipo;
	}
	
	public boolean getDeSistema()
	{
		return m_DeSistema;
	}

	public boolean getRecalcularCosto()
	{
		return m_RecalcularCosto;
	}

	public byte getTipoMov() 
	{
		return 0;
	}


}

