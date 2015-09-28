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


public class TBL_REPORTS
{
	private int m_ID_Report;
	private String m_Description;
	private String m_Tipo;
	private String m_Titulo;
	private String m_EncL1;
	private String m_EncL2;
	private String m_EncL3;
	private String m_L1;
	private String m_L2;
	private String m_L3;
	private String m_CL1;
	private String m_CL2;
	private String m_CL3;
	private int m_HW;
	private int m_VW;
	private String m_SubTipo;
	private String m_Clave;
	private boolean m_Graficar;
	
	public void setID_Report(int ID_Report)
	{
		m_ID_Report = ID_Report;
	}

	public void setDescription(String Description)
	{
		m_Description = Description;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setTitulo(String Titulo)
	{
		m_Titulo = Titulo;
	}

	public void setEncL1(String EncL1)
	{
		m_EncL1 = EncL1;
	}

	public void setEncL2(String EncL2)
	{
		m_EncL2 = EncL2;
	}

	public void setEncL3(String EncL3)
	{
		m_EncL3 = EncL3;
	}

	public void setL1(String L1)
	{
		m_L1 = L1;
	}

	public void setL2(String L2)
	{
		m_L2 = L2;
	}

	public void setL3(String L3)
	{
		m_L3 = L3;
	}

	public void setCL1(String CL1)
	{
		m_CL1 = CL1;
	}

	public void setCL2(String CL2)
	{
		m_CL2 = CL2;
	}

	public void setCL3(String CL3)
	{
		m_CL3 = CL3;
	}

	public void setHW(int HW)
	{
		m_HW = HW;
	}

	public void setVW(int VW)
	{
		m_VW = VW;
	}


	public int getID_Report()
	{
		return m_ID_Report;
	}

	public String getDescription()
	{
		return m_Description;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public String getTitulo()
	{
		return m_Titulo;
	}

	public String getEncL1()
	{
		return m_EncL1;
	}

	public String getEncL2()
	{
		return m_EncL2;
	}

	public String getEncL3()
	{
		return m_EncL3;
	}

	public String getL1()
	{
		return m_L1;
	}

	public String getL2()
	{
		return m_L2;
	}

	public String getL3()
	{
		return m_L3;
	}

	public String getCL1()
	{
		return m_CL1;
	}

	public String getCL2()
	{
		return m_CL2;
	}

	public String getCL3()
	{
		return m_CL3;
	}

	public int getHW()
	{
		return m_HW;
	}

	public int getVW()
	{
		return m_VW;
	}

	public String getSubTipo() 
	{
		return m_SubTipo;
	}

	public void setSubTipo(String SubTipo) 
	{
		m_SubTipo = SubTipo;
	}

	public String getClave() 
	{
		return m_Clave;
	}

	public void setClave(String Clave) 
	{
		m_Clave = Clave;
	}

	public boolean getGraficar() 
	{
		return m_Graficar;
	}

	public void setGraficar(boolean Graficar) 
	{
		m_Graficar = Graficar;
	}


}

