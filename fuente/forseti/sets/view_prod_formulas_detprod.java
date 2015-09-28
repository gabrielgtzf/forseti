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

public class view_prod_formulas_detprod
{
	private int m_ID_Proceso;
	private String m_ID_Prod;
	private String m_Descripcion;
	private float m_Cantidad;
	private String m_Unidad;
	private float m_CP;
	private float m_UC;
	private float m_MasMenos;
	private boolean m_Principal;
	
	public void setID_Proceso(int ID_Proceso)
	{
		m_ID_Proceso = ID_Proceso;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setCP(float CP)
	{
		m_CP = CP;
	}

	public void setUC(float UC)
	{
		m_UC = UC;
	}

	public void setMasMenos(float MasMenos)
	{
		m_MasMenos = MasMenos;
	}

	public int getID_Proceso()
	{
		return m_ID_Proceso;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getCP()
	{
		return m_CP;
	}

	public float getUC()
	{
		return m_UC;
	}

	public float getMasMenos() 
	{
		return m_MasMenos;
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

