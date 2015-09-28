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
package forseti;

public class JRetFuncBas 
{
	private int m_idmensaje;
	private String m_res;
	private String m_claveret;
	
	public void setRS(int idmensaje, String res, String claveret) 
	{
		m_idmensaje = idmensaje;
		m_res = res;
		m_claveret = claveret;
	}

	public int getIdmensaje() 
	{
		return m_idmensaje;
	}

	public String getRes() 
	{
		return m_res;
	}
 
	public String getClaveret() 
	{
		return m_claveret;
	}

	public void setIdMensaje(int idmensaje) 
	{
		m_idmensaje = idmensaje;
	}

	public void setRes(String res) 
	{
		m_res = res;	
	}
	
}
