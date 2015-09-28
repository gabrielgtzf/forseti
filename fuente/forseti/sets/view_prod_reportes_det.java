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

import java.sql.Date;

public class view_prod_reportes_det
{
	private long m_ID_Reporte;
	private byte m_Partida;
	private float m_Cantidad;
	private String m_Lote;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Formula;
	private String m_Obs;
	private long m_ID_Formula;
	private String m_Unidad;
	private float m_MasMenos;
	private byte m_NumProc;
	private byte m_ActualProc;
	private boolean m_Terminada;
	private Date m_Fecha;
	private int m_ID_Pol;
	
	public void setID_Reporte(long ID_Reporte)
	{
		m_ID_Reporte = ID_Reporte;
	}

	public void setPartida(byte Partida)
	{
		m_Partida = Partida;
	}

	public void setCantidad(float Cantidad)
	{
		m_Cantidad = Cantidad;
	}

	public void setLote(String Lote)
	{
		m_Lote = Lote;
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

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setID_Formula(long ID_Formula)
	{
		m_ID_Formula = ID_Formula;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public long getID_Reporte()
	{
		return m_ID_Reporte;
	}

	public byte getPartida()
	{
		return m_Partida;
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}

	public String getLote()
	{
		return m_Lote;
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

	public String getObs()
	{
		return m_Obs;
	}

	public long getID_Formula()
	{
		return m_ID_Formula;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public float getMasMenos() 
	{
		return m_MasMenos;
	}

	public void setMasMenos(float MasMenos) 
	{
		m_MasMenos = MasMenos;
	}

	public void setNumProc(byte NumProc) 
	{
		m_NumProc = NumProc;		
	}
	
	public void setActualProc(byte ActualProc) 
	{
		m_ActualProc = ActualProc;		
	}
	
	public void setTerminada(boolean Terminada)
	{
		m_Terminada = Terminada;
	}
	
	public byte getNumProc() 
	{
		return m_NumProc;		
	}
	
	public byte getActualProc() 
	{
		return m_ActualProc;		
	}
	
	public boolean getTerminada()
	{
		return m_Terminada;
	}

	public void setFecha(Date Fecha) 
	{
		m_Fecha = Fecha;
	}

	public Date getFecha() 
	{
		return m_Fecha;
	}

	public int getID_Pol() 
	{
		return m_ID_Pol;
	}
	
	public void setID_Pol(int ID_Pol) 
	{
		m_ID_Pol = ID_Pol;
	}
}

