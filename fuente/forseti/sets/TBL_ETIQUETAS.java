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

public class TBL_ETIQUETAS
{
	private String m_ID_TEtiqueta;
	private String m_ID_Etiqueta;
	private String m_Instrucciones;
	private String m_Codigo;
	private String m_Codigo2;
	private String m_Codigo3;
	private String m_Codigo4;
	private int m_Posicion;
	private byte m_Part;
	private boolean m_Opc;
	
	public void setID_TEtiqueta(String ID_TEtiqueta)
	{
		m_ID_TEtiqueta = ID_TEtiqueta;
	}

	public void setID_Etiqueta(String ID_Etiqueta)
	{
		m_ID_Etiqueta = ID_Etiqueta;
	}

	public void setInstrucciones(String Instrucciones)
	{
		m_Instrucciones = Instrucciones;
	}

	public void setCodigo(String Codigo)
	{
		m_Codigo = Codigo;
	}

	public void setCodigo2(String Codigo2)
	{
		m_Codigo2 = Codigo2;
	}

	public void setCodigo3(String Codigo3)
	{
		m_Codigo3 = Codigo3;
	}

	public void setCodigo4(String Codigo4)
	{
		m_Codigo4 = Codigo4;
	}

	public void setPosicion(int Posicion)
	{
		m_Posicion = Posicion;
	}

	public void setPart(byte Part)
	{
		m_Part = Part;
	}

	public void setOpc(boolean Opc)
	{
		m_Opc = Opc;
	}
	
	public String getID_TEtiqueta()
	{
		return m_ID_TEtiqueta;
	}

	public String getID_Etiqueta()
	{
		return m_ID_Etiqueta;
	}

	public String getInstrucciones()
	{
		return m_Instrucciones;
	}

	public String getCodigo()
	{
		return m_Codigo;
	}

	public String getCodigo2()
	{
		return m_Codigo2;
	}

	public String getCodigo3()
	{
		return m_Codigo3;
	}

	public String getCodigo4()
	{
		return m_Codigo4;
	}

	public int getPosicion()
	{
		return m_Posicion;
	}

	public byte getPart()
	{
		return m_Part;
	}

	public boolean getOpc() 
	{
		return m_Opc;
	}

	


}

