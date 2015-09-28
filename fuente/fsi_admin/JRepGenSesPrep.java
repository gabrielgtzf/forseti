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

public class JRepGenSesPrep 
{
	private String m_Nombre;
	private String m_Valor;
	
	public JRepGenSesPrep(String Nombre, String Valor) 
	{
		m_Nombre = Nombre;
		m_Valor = Valor;
	}

	public String getNombre() 
	{
		return m_Nombre;
	}

	public String getValor() 
	{
		return m_Valor;
	}
	
	
}
