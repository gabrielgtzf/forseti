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

public class tbl_ventas_facturas_pagos
{
	private int m_ID_Factura;
	private int m_ID_Mov;

	public void setID_Factura(int ID_Factura)
	{
		m_ID_Factura = ID_Factura;
	}

	public void setID_Mov(int ID_Mov)
	{
		m_ID_Mov = ID_Mov;
	}


	public int getID_Factura()
	{
		return m_ID_Factura;
	}

	public int getID_Mov()
	{
		return m_ID_Mov;
	}


}

