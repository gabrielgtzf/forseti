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


public class view_prod_entidades_ids
{
	private int m_ID_Entidad;
	private byte m_ID_Tipo;
	private String m_Serie;
	private long m_Doc;
	private String m_Formato;
	private String m_BodegaMP;
	private String m_BodegaPT;
	private int m_ID_BodegaMP;
	private int m_ID_BodegaPT;
	private String m_ID_Usuario;
	private String m_Descripcion;
	private boolean m_AuditarAlm;
	private byte m_ManejoStocks;

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

	public void setDoc(long Doc)
	{
		m_Doc = Doc;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}

	public void setBodegaMP(String BodegaMP)
	{
		m_BodegaMP = BodegaMP;
	}

	public void setBodegaPT(String BodegaPT)
	{
		m_BodegaPT = BodegaPT;
	}

	public void setID_BodegaMP(int ID_BodegaMP)
	{
		m_ID_BodegaMP = ID_BodegaMP;
	}

	public void setID_BodegaPT(int ID_BodegaPT)
	{
		m_ID_BodegaPT = ID_BodegaPT;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setAuditarAlm(boolean AuditarAlm)
	{
		m_AuditarAlm = AuditarAlm;
	}

	public void setManejoStocks(byte ManejoStocks)
	{
		m_ManejoStocks = ManejoStocks;
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

	public long getDoc()
	{
		return m_Doc;
	}

	public String getFormato()
	{
		return m_Formato;
	}

	public String getBodegaMP()
	{
		return m_BodegaMP;
	}

	public String getBodegaPT()
	{
		return m_BodegaPT;
	}

	public int getID_BodegaMP()
	{
		return m_ID_BodegaMP;
	}

	public int getID_BodegaPT()
	{
		return m_ID_BodegaPT;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getAuditarAlm()
	{
		return m_AuditarAlm;
	}

	public byte getManejoStocks()
	{
		return m_ManejoStocks;
	}


}

