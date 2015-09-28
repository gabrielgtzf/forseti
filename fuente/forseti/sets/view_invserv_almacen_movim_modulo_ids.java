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

public class view_invserv_almacen_movim_modulo_ids
{
	private byte m_ID_Bodega;
	private String m_Nombre;
	private long m_Numero;
	private long m_Salida;
	private long m_Requerimiento;
	private long m_Plantilla;
 	private String m_ID_Usuario;
	private String m_Fmt_Movimientos;
	private String m_Fmt_Traspasos;
	private boolean m_AuditarAlm;
	private byte m_ManejoStocks;
	private String m_CFD;
	
	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setSalida(long Salida)
	{
		m_Salida = Salida;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setFmt_Movimientos(String Fmt_Movimientos)
	{
		m_Fmt_Movimientos = Fmt_Movimientos;
	}

	public void setFmt_Traspasos(String Fmt_Traspasos)
	{
		m_Fmt_Traspasos = Fmt_Traspasos;
	}

	public void setAuditarAlm(boolean AuditarAlm)
	{
		m_AuditarAlm = AuditarAlm;
	}

	public void setCFD(String CFD)
	{
		m_CFD = CFD;
	}
	
	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public long getSalida()
	{
		return m_Salida;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public String getFmt_Movimientos()
	{
		return m_Fmt_Movimientos;
	}

	public String getFmt_Traspasos()
	{
		return m_Fmt_Traspasos;
	}

	public boolean getAuditarAlm()
	{
		return m_AuditarAlm;
	}

	public long getRequerimiento() 
	{
		return m_Requerimiento;
	}

	public void setRequerimiento(long Requerimiento) 
	{
		m_Requerimiento = Requerimiento;
	}

	public byte getManejoStocks() 
	{
		return m_ManejoStocks;
	}

	public void setManejoStocks(byte ManejoStocks) 
	{
		m_ManejoStocks = ManejoStocks;
	}

	public void setPlantilla(long Plantilla) 
	{
		m_Plantilla = Plantilla;
	}
	
	public long getPlantilla() 
	{
		return m_Plantilla;
	}

	public String getCFD() 
	{
		return m_CFD;
	}

}

