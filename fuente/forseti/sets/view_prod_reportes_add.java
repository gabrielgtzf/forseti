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


public class view_prod_reportes_add
{
	private long m_ID_Formula;
	private String m_ID_Prod;
	private String m_Descripcion;
	private long m_NumProc;
	private String m_Formula;
	private boolean m_UnidadUnica;
	private float m_Cantidad;
	private String m_Unidad;
	private boolean m_Principal;
	
	public void setID_Formula(long ID_Formula)
	{
		m_ID_Formula = ID_Formula;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setNumProc(long NumProc)
	{
		m_NumProc = NumProc;
	}

	public void setFormula(String Formula)
	{
		m_Formula = Formula;
	}

	public void setUnidadUnica(boolean UnidadUnica)
	{
		m_UnidadUnica = UnidadUnica;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public long getID_Formula()
	{
		return m_ID_Formula;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public long getNumProc()
	{
		return m_NumProc;
	}

	public String getFormula()
	{
		return m_Formula;
	}

	public boolean getUnidadUnica()
	{
		return m_UnidadUnica;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public void setPrincipal(boolean Principal) 
	{
		m_Principal = Principal;	
	}
	
	public boolean getPrincipal()
	{
		return m_Principal;
	}


}

