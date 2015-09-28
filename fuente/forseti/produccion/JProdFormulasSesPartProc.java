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

import java.util.Date;

import forseti.JSesionRegs;

public class JProdFormulasSesPartProc extends JSesionRegs
{
	private String m_Nombre;
	private int m_Tiempo;
	private Date m_Fecha;
	private Date m_FechaSP;
	private String m_ID_Prod;
	private String m_Descripcion;
	private String m_Unidad;
	private float m_Porcentaje;
	private float m_Cantidad;
	private float m_MasMenos;
	private float m_CPT;
	private float m_UCT;
	
	public JProdFormulasSesPartProc()
	{
		
	}
	
	public JProdFormulasSesPartProc(String Nombre, int Tiempo, float Cantidad, float MasMenos, String ID_Prod, String Descripcion, String Unidad, 
				float Porcentaje, float CPT, float UCT, Date Fecha, Date FechaSP)
	{
		m_Porcentaje = Porcentaje;
		m_Cantidad = Cantidad;
		m_Nombre = Nombre;
		m_Tiempo = Tiempo;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CPT = CPT;
		m_UCT = UCT;
		m_Fecha = Fecha;
		m_FechaSP = FechaSP;
	}
	
	public void setPartida(String Nombre, int Tiempo, float Cantidad, float MasMenos, String ID_Prod, String Descripcion, String Unidad, 
			float Porcentaje, float CPT, float UCT, Date Fecha, Date FechaSP)
	{
		m_Porcentaje = Porcentaje;
		m_Cantidad = Cantidad;
		m_Nombre = Nombre;
		m_Tiempo = Tiempo;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CPT = CPT;
		m_UCT = UCT;
		m_Fecha = Fecha;
		m_FechaSP = FechaSP;
	}
	
	public JProdFormulasSesPart getPartida(int ind)
	{
	    return (JProdFormulasSesPart)m_Partidas.elementAt(ind);
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public float getCantidad() 
	{
		return m_Cantidad;
	}

	public float getPorcentaje() 
	{
		return m_Porcentaje;
	}
	
	public float getMasMenos() 
	{
		return m_MasMenos;
	}

	public int getTiempo() 
	{
		return m_Tiempo;
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

	
	public float getCPT() 
	{
		return m_CPT;
	}

	public float getUCT() 
	{
		return m_UCT;
	}
	
	public void setCPT(float CPT) 
	{
		m_CPT = CPT;
	}

	public void setUCT(float UCT) 
	{
		m_UCT = UCT;
	}

	public void setCantidad(float Cantidad) 
	{
		m_Cantidad = Cantidad;
 	}
	
	public void setMasMenos(float MasMenos) 
	{
		m_MasMenos = MasMenos;
 	}
	
	public Date getFecha()
	{
		return m_Fecha;
	}
	
	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}
	
	public Date getFechaSP()
	{
		return m_FechaSP;
	}
	
	public void setFechaSP(Date FechaSP)
	{
		m_FechaSP = FechaSP;
	}
}