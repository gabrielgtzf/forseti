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
package forseti.compras;


public class JCompPoliticasSesObjs 
{
	private String m_ID_Tipo;
	private String m_Clave;
	private String m_Descripcion;
	private String m_Linea;
	private String m_Unidad;
	private String m_Status;
	private float m_PComp;
	private byte m_ID_Moneda;
	
	public JCompPoliticasSesObjs()
	{
	}

	public JCompPoliticasSesObjs(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float PComp, byte ID_Moneda)
	{
		m_ID_Tipo = ID_Tipo;
		m_Clave = Clave;
		m_Descripcion = Descripcion;
		m_Linea = Linea;
		m_Unidad = Unidad;
		m_Status = Status;
		m_PComp = PComp;
		m_ID_Moneda = ID_Moneda;
	}

	public void setPartida(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float PComp, byte ID_Moneda)
	{
		m_ID_Tipo = ID_Tipo;
		m_Clave = Clave;
		m_Descripcion = Descripcion;
		m_Linea = Linea;
		m_Unidad = Unidad;
		m_Status = Status;
		m_PComp = PComp;
		m_ID_Moneda = ID_Moneda;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getLinea()
	{
		return m_Linea;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public float getPComp()
	{
		return m_PComp;
	}
	
	public byte getID_Moneda()
	{
		return m_ID_Moneda;
	}
	
}



