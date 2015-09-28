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



public class TBL_INVSERV_BODEGAS
{
	private short m_ID_Bodega;
	private String m_Nombre;
	private String m_Descripcion;
	private long m_Numero;
	private int m_Salida;
	private String m_Fmt_Movimientos;
	private String m_Fmt_Traspasos;
	private boolean m_AuditarAlm;
	private byte m_ManejoStocks;
	private int m_Requerimiento;
	private String m_Fmt_Requerimientos;
	private int m_Plantilla;
	private String m_Fmt_Plantillas;
	private String m_CFD;
	private int m_CFD_NoAprobacion;
	private String m_CFD_NoCertificado;
	private byte m_CFD_ID_Expedicion;
	private byte m_CFD_ID_Receptor;
	private int m_NumChFis;
	private String m_ID_Clasificacion;
	private String m_Status;
	private String m_Fmt_ChFis;
	private String m_ID_InvServ;
	private boolean m_Fija;
	
	public void setID_Bodega(short ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setNumero(long Numero)
	{
		m_Numero = Numero;
	}

	public void setSalida(int Salida)
	{
		m_Salida = Salida;
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

	public void setManejoStocks(byte ManejoStocks)
	{
		m_ManejoStocks = ManejoStocks;
	}

	public void setRequerimiento(int Requerimiento)
	{
		m_Requerimiento = Requerimiento;
	}


	public short getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public long getNumero()
	{
		return m_Numero;
	}

	public int getSalida()
	{
		return m_Salida;
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

	public byte getManejoStocks()
	{
		return m_ManejoStocks;
	}

	public int getRequerimiento()
	{
		return m_Requerimiento;
	}

	public String getFmt_Requerimientos() 
	{
		return m_Fmt_Requerimientos;
	}

	public void setFmt_Requerimientos(String Fmt_Requerimientos) 
	{
		m_Fmt_Requerimientos = Fmt_Requerimientos;
	}

	public String getFmt_Plantillas() 
	{
		return m_Fmt_Plantillas;
	}

	public void setFmt_Plantillas(String Fmt_Plantillas) 
	{
		m_Fmt_Plantillas = Fmt_Plantillas;
	}

	public int getPlantilla() 
	{
		return m_Plantilla;
	}

	public void setPlantilla(int Plantilla) 
	{
		m_Plantilla = Plantilla;
	}
	
	public void setCFD(String CFD)
	{
		m_CFD = CFD;
	}

	public void setCFD_NoAprobacion(int CFD_NoAprobacion)
	{
		m_CFD_NoAprobacion = CFD_NoAprobacion;
	}

	public void setCFD_NoCertificado(String CFD_NoCertificado)
	{
		m_CFD_NoCertificado = CFD_NoCertificado;
	}
	
	public void setCFD_ID_Expedicion(byte CFD_ID_Expedicion)
	{
		m_CFD_ID_Expedicion = CFD_ID_Expedicion;
	}
		
	public void setCFD_ID_Receptor(byte CFD_ID_Receptor)
	{
		m_CFD_ID_Receptor = CFD_ID_Receptor;
	}
	
	public String getCFD()
	{
		return m_CFD;
	}

	public int getCFD_NoAprobacion()
	{
		return m_CFD_NoAprobacion;
	}

	public String getCFD_NoCertificado()
	{
		return m_CFD_NoCertificado;
	}
	
	public byte getCFD_ID_Expedicion()
	{
		return m_CFD_ID_Expedicion;
	}

	public byte getCFD_ID_Receptor()
	{
		return m_CFD_ID_Receptor;
	}

	public void setNumChFis(int NumChFis) 
	{
		m_NumChFis = NumChFis;
	}

	public void setID_Clasificacion(String ID_Clasificacion) 
	{
		m_ID_Clasificacion = ID_Clasificacion;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}
	
	public int getNumChFis() 
	{
		return m_NumChFis;
	}

	public String getID_Clasificacion() 
	{
		return m_ID_Clasificacion;
	}

	public String getStatus() 
	{
		return m_Status;
	}

	public void setFmt_ChFis(String Fmt_ChFis) 
	{
		m_Fmt_ChFis = Fmt_ChFis;
	}
	
	public String getFmt_ChFis() 
	{
		return m_Fmt_ChFis;
	}

	public void setID_InvServ(String ID_InvServ) 
	{
		m_ID_InvServ = ID_InvServ;
	}
	
	public String getID_InvServ() 
	{
		return m_ID_InvServ;
	}

	public void setFija(boolean Fija) 
	{
		m_Fija = Fija;		
	}

	public boolean getFija() 
	{
		return m_Fija;		
	}

}

