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

public class tbl_cont_polizas_detalle_ce_comprobantes
{
	private int m_ID;
	private int m_ID_Pol;
	private int m_ID_Part;
	private String m_UUID_CFDI;
	private double m_Monto;
	private String m_RFC;
	private String m_ID_Tipo;
	private String m_Moneda;
	private double m_TipCamb;
	private String m_CFD_CBB_Serie;
	private int m_CFD_CBB_NumFol;
	private String m_NumFactExt;
	private String m_TAXID;
	
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

	public void setUUID_CFDI(String UUID_CFDI)
	{
		m_UUID_CFDI = UUID_CFDI;
	}

	public void setMonto(double Monto)
	{
		m_Monto = Monto;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public int getID()
	{
		return m_ID;
	}

	public String getID_Tipo() 
	{
		return m_ID_Tipo;
	}

	public void setID_Tipo(String ID_Tipo) 
	{
		m_ID_Tipo = ID_Tipo;
	}

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

	public String getCFD_CBB_Serie() 
	{
		return m_CFD_CBB_Serie;
	}

	public void setCFD_CBB_Serie(String CFD_CBB_Serie) 
	{
		m_CFD_CBB_Serie = CFD_CBB_Serie;
	}

	public int getCFD_CBB_NumFol() 
	{
		return m_CFD_CBB_NumFol;
	}

	public void setCFD_CBB_NumFol(int CFD_CBB_NumFol) 
	{
		m_CFD_CBB_NumFol = CFD_CBB_NumFol;
	}

	public String getNumFactExt() 
	{
		return m_NumFactExt;
	}

	public void setNumFactExt(String NumFactExt) 
	{
		m_NumFactExt = NumFactExt;
	}

	public String getTAXID() 
	{
		return m_TAXID;
	}

	public void setTAXID(String TAXID) {
		m_TAXID = TAXID;
	}

	public int getID_Pol()
	{
		return m_ID_Pol;
	}

	public int getID_Part()
	{
		return m_ID_Part;
	}

	public String getUUID_CFDI()
	{
		return m_UUID_CFDI;
	}

	public double getMonto()
	{
		return m_Monto;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	


}

