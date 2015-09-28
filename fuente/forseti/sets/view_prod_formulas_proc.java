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

public class view_prod_formulas_proc
{
	private int m_ID_Formula;
	private int m_ID_Proceso;
	private String m_Nombre;
	private String m_ID_SubProd;
	private String m_Descripcion;
	private float m_Cantidad;
	private String m_Unidad;
	private float m_MasMenos;
	private boolean m_Principal;
	private int m_Tiempo;
	private float m_Porcentaje;

	public void setID_Formula(int ID_Formula)
	{
		m_ID_Formula = ID_Formula;
	}

	public void setID_Proceso(int ID_Proceso)
	{
		m_ID_Proceso = ID_Proceso;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}


	public int getID_Formula()
	{
		return m_ID_Formula;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public void setID_SubProd(String ID_SubProd)
	{
		m_ID_SubProd = ID_SubProd;
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

	public void setPorcentaje(float Porcentaje)
	{
		m_Porcentaje = Porcentaje;
	}

	public void setTiempo(int Tiempo)
	{
		m_Tiempo = Tiempo;
	}

	public void setMasMenos(float MasMenos)
	{
		m_MasMenos = MasMenos;
	}

	public int getID_Proceso()
	{
		return m_ID_Proceso;
	}

	public String getID_SubProd()
	{
		return m_ID_SubProd;
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

	public float getPorcentaje()
	{
		return m_Porcentaje;
	}

	public int getTiempo()
	{
		return m_Tiempo;
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

