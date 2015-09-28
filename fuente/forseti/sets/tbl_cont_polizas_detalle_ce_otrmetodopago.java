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

import java.sql.*;

public class tbl_cont_polizas_detalle_ce_otrmetodopago
{
	private int m_ID;
	private int m_ID_Pol;
	private int m_ID_Part;
	private String m_MetPagoPol;
	private Date m_Fecha;
	private String m_Benef;
	private String m_RFC;
	private double m_Monto;
	private String m_Moneda;
	private double m_TipCamb;
	   
	public String getMoneda() 
	{
		return m_Moneda;
	}

	public void setMoneda(String Moneda) 
	{
		m_Moneda = Moneda;
	}

	public double getTipCamb() 
	{
		return m_TipCamb;
	}

	public void setTipCamb(double TipCamb) 
	{
		m_TipCamb = TipCamb;
	}

	public void setID(int ID)
	{
		m_ID = ID;
	}

	public void setID_Pol(int ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setID_Part(int ID_Part)
	{
		m_ID_Part = ID_Part;
	}

	public void setMetPagoPol(String MetPagoPol)
	{
		m_MetPagoPol = MetPagoPol;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setMonto(double Monto)
	{
		m_Monto = Monto;
	}

	public void setBenef(String Benef)
	{
		m_Benef = Benef;
	}
	
	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}


	public int getID()
	{
		return m_ID;
	}

	public int getID_Pol()
	{
		return m_ID_Pol;
	}

	public int getID_Part()
	{
		return m_ID_Part;
	}

	public String getMetPagoPol()
	{
		return m_MetPagoPol;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public double getMonto()
	{
		return m_Monto;
	}

	public String getBenef()
	{
		return m_Benef;
	}

	public String getRFC()
	{
		return m_RFC;
	}

}

