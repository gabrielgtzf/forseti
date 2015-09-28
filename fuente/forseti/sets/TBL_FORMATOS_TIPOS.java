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



public class TBL_FORMATOS_TIPOS
{
	private String m_ID_Tipo;
	private String m_Descripcion;
	private String m_ImpCab;
	private String m_ImpDet;

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setImpCab(String ImpCab)
	{
		m_ImpCab = ImpCab;
	}

	public void setImpDet(String ImpDet)
	{
		m_ImpDet = ImpDet;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getImpCab()
	{
		return m_ImpCab;
	}

	public String getImpDet()
	{
		return m_ImpDet;
	}


}

