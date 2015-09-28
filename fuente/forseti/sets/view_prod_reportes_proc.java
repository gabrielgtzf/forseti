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

import java.sql.*;

public class view_prod_reportes_proc
{
	private long m_ID_Reporte;
	private byte m_Partida;
	private long m_ID_Proceso;
	private String m_Nombre;
	private Date m_Fecha;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Unidad;
	private float m_Porcentaje;
	private float m_Cantidad;
	private float m_MasMenos;
	private Date m_FechaSP;
	private int m_ID_Pol;
	private int m_ID_PolSP;
	
	public void setID_Reporte(long ID_Reporte)
	{
		m_ID_Reporte = ID_Reporte;
	}

	public void setPartida(byte Partida)
	{
		m_Partida = Partida;
	}

	public void setID_Proceso(long ID_Proceso)
	{
		m_ID_Proceso = ID_Proceso;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setPorcentaje(float Porcentaje)
	{
		m_Porcentaje = Porcentaje;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setMasMenos(float MasMenos)
	{
		m_MasMenos = MasMenos;
	}


	public long getID_Reporte()
	{
		return m_ID_Reporte;
	}

	public byte getPartida()
	{
		return m_Partida;
	}

	public long getID_Proceso()
	{
		return m_ID_Proceso;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getPorcentaje()
	{
		return m_Porcentaje;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public float getMasMenos()
	{
		return m_MasMenos;
	}

	public void setFechaSP(Date FechaSP) 
	{
		m_FechaSP = FechaSP;
	}

	public Date getFechaSP() 
	{
		return m_FechaSP;
	}
	
	public int getID_Pol() 
	{
		return m_ID_Pol;
	}
	
	public void setID_Pol(int ID_Pol) 
	{
		m_ID_Pol = ID_Pol;
	}
	
	public int getID_PolSP() 
	{
		return m_ID_PolSP;
	}
	
	public void setID_PolSP(int ID_PolSP) 
	{
		m_ID_PolSP = ID_PolSP;
	}
}

