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


public class TBL_MOVIMIENTOS_NOMINA
{
	private int m_ID_Movimiento;
	private String m_Tipo_Movimiento;
	private String m_Descripcion;
	private boolean m_Deduccion;
	private boolean m_IMSS;
	private boolean m_ISPT;
	private boolean m_DOSPOR;
	private boolean m_SAR;
	private boolean m_INFONAVIT;
	private boolean m_PTU;
	private String m_ID_SAT;

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setTipo_Movimiento(String Tipo_Movimiento)
	{
		m_Tipo_Movimiento = Tipo_Movimiento;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setDeduccion(boolean Deduccion)
	{
		m_Deduccion = Deduccion;
	}
	
	public void setIMSS(boolean IMSS)
	{
		m_IMSS = IMSS;
	}

	public void setISPT(boolean ISPT)
	{
		m_ISPT = ISPT;
	}

	public void setDOSPOR(boolean DOSPOR)
	{
		m_DOSPOR = DOSPOR;
	}

	public void setSAR(boolean SAR)
	{
		m_SAR = SAR;
	}

	public void setINFONAVIT(boolean INFONAVIT)
	{
		m_INFONAVIT = INFONAVIT;
	}

	public void setPTU(boolean PTU)
	{
		m_PTU = PTU;
	}


	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getTipo_Movimiento()
	{
		return m_Tipo_Movimiento;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getDeduccion()
	{
		return m_Deduccion;
	}
		
	public boolean getIMSS()
	{
		return m_IMSS;
	}

	public boolean getISPT()
	{
		return m_ISPT;
	}

	public boolean getDOSPOR()
	{
		return m_DOSPOR;
	}

	public boolean getSAR()
	{
		return m_SAR;
	}

	public boolean getINFONAVIT()
	{
		return m_INFONAVIT;
	}

	public boolean getPTU()
	{
		return m_PTU;
	}

	public void setID_SAT(String ID_SAT) 
	{
		m_ID_SAT = ID_SAT;
	}

	public String getID_SAT() 
	{
		return m_ID_SAT;
	}
}

