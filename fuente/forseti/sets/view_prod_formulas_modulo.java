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


public class view_prod_formulas_modulo
{
	private long m_ID_Formula;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Formula;
	private float m_Cantidad;
	private String m_ID_Linea;
	private long m_NumProc;
	private String m_Unidad;
	private boolean m_UnidadUnica;
	private float m_MasMenos;
	private boolean m_Principal;
	
	public boolean getPrincipal() 
	{
		return m_Principal;
	}

	public void setID_Formula(long ID_Formula)
	{
		m_ID_Formula = ID_Formula;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setFormula(String Formula)
	{
		m_Formula = Formula;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}
	
	public void setMasMenos(float MasMenos)
	{
		m_MasMenos = MasMenos;
	}

	public void setID_Linea(String ID_Linea)
	{
		m_ID_Linea = ID_Linea;
	}

	public void setNumProc(long NumProc)
	{
		m_NumProc = NumProc;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setUnidadUnica(boolean UnidadUnica)
	{
		m_UnidadUnica = UnidadUnica;
	}


	public long getID_Formula()
	{
		return m_ID_Formula;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getFormula()
	{
		return m_Formula;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getID_Linea()
	{
		return m_ID_Linea;
	}

	public long getNumProc()
	{
		return m_NumProc;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public boolean getUnidadUnica()
	{
		return m_UnidadUnica;
	}

	public float getMasMenos() 
	{
		return m_MasMenos;
	}

	public void setPrincipal(boolean Principal) 
	{
		m_Principal = Principal;
	}


}

