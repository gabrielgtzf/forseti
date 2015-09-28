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

public class tbl_compras_gastos_pagos 
{
	private int m_ID_Gasto;
	private int m_ID_Mov;

	public void setID_Gasto(int ID_Gasto)
	{
		m_ID_Gasto = ID_Gasto;
	}

	public void setID_Mov(int ID_Mov)
	{
		m_ID_Mov = ID_Mov;
	}


	public int getID_Gasto()
	{
		return m_ID_Gasto;
	}

	public int getID_Mov()
	{
		return m_ID_Mov;
	}

}
