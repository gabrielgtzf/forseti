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
package fsi_admin;

public class JRepGenSesPart 
{
	private String m_ColName;
	private String m_BindDataType;
	private boolean m_WillShow;
	private String m_Format;
	private float m_Ancho;
	private String m_AlinHor;
	
	public JRepGenSesPart()
	{
		
	}
	
	public JRepGenSesPart(String ColName, String BindDataType, boolean WillShow, String Format, float Ancho, String AlinHor)
	{
		m_ColName = ColName;
		m_BindDataType = BindDataType;
		m_WillShow = WillShow;
		m_Format = Format;
		m_Ancho = Ancho;
		m_AlinHor = AlinHor;
	}
	
	public void setPartida(String ColName, String BindDataType, boolean WillShow, String Format, float Ancho, String AlinHor)
	{
		m_ColName = ColName;
		m_BindDataType = BindDataType;
		m_WillShow = WillShow;
		m_Format = Format;
		m_Ancho = Ancho;
		m_AlinHor = AlinHor;
	}

	public String getAlinHor() 
	{
		return m_AlinHor;
	}
	public float getAncho() 
	{
		return m_Ancho;
	}
	public String getBindDataType() 
	{
		return m_BindDataType;
	}
	public String getColName() 
	{
		return m_ColName;
	}
	public String getFormat() 
	{
		return m_Format;
	}
	public boolean getWillShow() 
	{
		return m_WillShow;
	}
	
}
