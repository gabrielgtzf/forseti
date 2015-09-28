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
package forseti.produccion;

public class JProdFormulasSesPart 
{
	private float m_CantidadXU;
	private float m_Cantidad;
	private float m_MasMenosXU;
	private float m_MasMenos;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_Unidad;
	private float m_CP;
	private float m_UC;
	private float m_CPT;
	private float m_UCT;
	private boolean m_Principal;
	
	public JProdFormulasSesPart()
	{
		
	}
	
	public JProdFormulasSesPart(float CantidadXU, float Cantidad, float MasMenosXU, float MasMenos, String ID_Prod, String Descripcion, String Unidad, 
				float CP, float UC, float CPT, float UCT, boolean Principal)
	{
		m_CantidadXU = CantidadXU;
		m_Cantidad = Cantidad;
		m_MasMenosXU = MasMenosXU;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CP = CP;
		m_UC = UC;
		m_CPT = CPT;
		m_UCT = UCT;
		m_Principal = Principal;
	}
	
	public void setPartida(float CantidadXU, float Cantidad, float MasMenosXU, float MasMenos, String ID_Prod, String Descripcion, String Unidad, 
				float CP, float UC, float CPT, float UCT, boolean Principal)
	{
		m_CantidadXU = CantidadXU;
		m_Cantidad = Cantidad;
		m_MasMenosXU = MasMenosXU;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CP = CP;
		m_UC = UC;
		m_CPT = CPT;
		m_UCT = UCT;
		m_Principal = Principal;
	}

	public float getCantidad() 
	{
		return m_Cantidad;
	}

	public float getCantidadXU() 
	{
		return m_CantidadXU;
	}
	
	public float getMasMenos() 
	{
		return m_MasMenos;
	}

	public float getMasMenosXU() 
	{
		return m_MasMenosXU;
	}
	
	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public String getID_Prod() 
	{
		return m_ID_Prod;
	}

	public String getUnidad() 
	{
		return m_Unidad;
	}

	public float getCP() 
	{
		return m_CP;
	}

	public float getCPT() 
	{
		return m_CPT;
	}

	public float getUC() 
	{
		return m_UC;
	}

	public float getUCT() 
	{
		return m_UCT;
	}
	
	public boolean getPrincipal() 
	{
		return m_Principal;
	}

	public void setCantidad(float Cantidad) 
	{
		m_Cantidad = Cantidad;
	}

	public void setCantidadXU(float CantidadXU) 
	{
		m_CantidadXU = CantidadXU;
	}

}
