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


public class view_public_bodegas_catalogo
{
	private int m_ID_Bodega;
	private String m_Nombre;
	private byte m_ManejoStocks;
	private boolean m_AuditarAlm;
	
	public boolean getAuditarAlm() 
	{
		return m_AuditarAlm;
	}

	public void setAuditarAlm(boolean AuditarAlm) 
	{
		m_AuditarAlm = AuditarAlm;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setManejoStocks(byte ManejoStocks)
	{
		m_ManejoStocks = ManejoStocks;
	}


	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public byte getManejoStocks()
	{
		return m_ManejoStocks;
	}


}

