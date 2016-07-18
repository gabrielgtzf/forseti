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


public class view_compras_entidades_ids
{
	private long m_Doc;
	private String m_Formato;
	private int m_ID_Entidad;
	private byte m_ID_Tipo;
	private String m_Serie;
	private int m_ID_Bodega;
	private String m_Bodega;
	private String m_Descripcion;
	private String m_ID_Usuario;
	private boolean m_AuditarAlm;
	private byte m_ManejoStocks;
	private int m_Orden;
	private int m_Devolucion;
	private float m_IVA;
	private String m_Fmt_Orden;
	private String m_Fmt_Devolucion;
	private boolean m_FijaCost;
	private String m_Fmt_Recepcion;
	private int m_Recepcion;
	private boolean m_Fija;
	private int m_TipoCobro;
	
	public void setDoc(long Doc)
	{
		m_Doc = Doc;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Tipo(byte ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setSerie(String Serie)
	{
		m_Serie = Serie;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setBodega(String Bodega)
	{
		m_Bodega = Bodega;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setAuditarAlm(boolean AuditarAlm)
	{
		m_AuditarAlm = AuditarAlm;
	}

	public void setManejoStocks(byte ManejoStocks)
	{
		m_ManejoStocks = ManejoStocks;
	}


	public long getDoc()
	{
		return m_Doc;
	}

	public String getFormato()
	{
		return m_Formato;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public byte getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getSerie()
	{
		return m_Serie;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getBodega()
	{
		return m_Bodega;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public boolean getAuditarAlm()
	{
		return m_AuditarAlm;
	}

	public byte getManejoStocks()
	{
		return m_ManejoStocks;
	}

	public int getOrden() 
	{
		return m_Orden;
	}

	public void setOrden(int Orden) 
	{
		m_Orden = Orden;
	}

	public int getDevolucion() 
	{
		return m_Devolucion;
	}

	public void setDevolucion(int Devolucion) 
	{
		m_Devolucion = Devolucion;
	}
	
	public int getRecepcion() 
	{
		return m_Recepcion;
	}

	public void setRecepcion(int Recepcion) 
	{
		m_Recepcion = Recepcion;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}
	
	public float getIVA() 
	{
		return m_IVA;
	}

	public void setFmt_Orden(String Fmt_Orden) 
	{
		m_Fmt_Orden = Fmt_Orden;
	}

	public void setFmt_Devolucion(String Fmt_Devolucion) 
	{
		m_Fmt_Devolucion = Fmt_Devolucion;	
	}
	
	public void setFmt_Recepcion(String Fmt_Recepcion) 
	{
		m_Fmt_Recepcion = Fmt_Recepcion;	
	}

	public String getFmt_Orden() 
	{
		return m_Fmt_Orden;
	}

	public String getFmt_Devolucion() 
	{
		return m_Fmt_Devolucion;	
	}

	public String getFmt_Recepcion() 
	{
		return m_Fmt_Recepcion;	
	}

	public boolean getFijaCost() 
	{
		return m_FijaCost;
	}

	public void setFijaCost(boolean FijaCost) 
	{
		m_FijaCost = FijaCost;
	}

	public void setFija(boolean Fija) 
	{
		m_Fija = Fija;
	}

	public boolean getFija() 
	{
		return m_Fija;
	}

	public int getTipoCobro() 
	{
		return m_TipoCobro;
	}

	public void setTipoCobro(int TipoCobro) 
	{
		m_TipoCobro = TipoCobro;	
	}
}

