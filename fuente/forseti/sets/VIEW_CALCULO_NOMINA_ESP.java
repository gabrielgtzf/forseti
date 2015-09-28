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

public class VIEW_CALCULO_NOMINA_ESP
{
	private int m_ID_Nomina;
	private String m_ID_Empleado;
	private String m_Nombre;
	private float m_Dias;
	private float m_Faltas;
	private int m_Recibo;
	private float m_HE;
	private float m_HD;
	private byte m_TFD;
	private float m_HT;
	private byte m_DiasHorasExtras;
	private float m_IXA;
	private float m_IXE;
	private float m_IXM;
	private int m_CFD;
	private float m_Deduccion;
	private float m_Exento;
	private float m_Gravado;
	
	public void setID_Nomina(int ID_Nomina)
	{
		m_ID_Nomina = ID_Nomina;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setDias(float Dias)
	{
		m_Dias = Dias;
	}

	public void setFaltas(float Faltas)
	{
		m_Faltas = Faltas;
	}

	public void setRecibo(int Recibo)
	{
		m_Recibo = Recibo;
	}

	public int getID_Nomina()
	{
		return m_ID_Nomina;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	
	public float getDias()
	{
		return m_Dias;
	}

	public float getFaltas()
	{
		return m_Faltas;
	}

	public int getRecibo()
	{
		return m_Recibo;
	}

	public void setHE(float HE) 
	{
		m_HE = HE;
	}
	public void setHD(float HD) 
	{
		m_HD = HD;
	}

	public float getHE()
	{
		return m_HE;
	}
	
	public float getHD()
	{
		return m_HD;
	}

	public byte getTFD() 
	{
		return m_TFD;
	}
	
	public void setTFD(byte TFD) 
	{
		m_TFD = TFD;
	}

	public void setHT(float HT) 
	{
		m_HT = HT;		
	}

	public void setDiasHorasExtras(byte DiasHorasExtras) 
	{
		m_DiasHorasExtras = DiasHorasExtras;
	}

	public void setIXA(float IXA) 
	{
		m_IXA = IXA;
	}
	
	public float getHT() 
	{
		return m_HT;		
	}

	public byte getDiasHorasExtras() 
	{
		return m_DiasHorasExtras;
	}

	public float getIXA() 
	{
		return m_IXA;
	}
	
	public void setIXE(float IXE) 
	{
		m_IXE = IXE;
	}
	
	public float getIXE() 
	{
		return m_IXE;
	}
	
	public void setIXM(float IXM) 
	{
		m_IXM = IXM;
	}
	
	public float getIXM() 
	{
		return m_IXM;
	}

	public void setID_CFD(int CFD) 
	{
		m_CFD = CFD;		
	}
	
	public int getID_CFD() 
	{
		return m_CFD;		
	}

	public void setGravado(float Gravado) 
	{
		m_Gravado = Gravado;
	}

	public void setExento(float Exento) 
	{
		m_Exento = Exento;	
	}

	public void setDeduccion(float Deduccion) 
	{
		m_Deduccion = Deduccion;	
	}

	public float getGravado() 
	{
		return m_Gravado;
	}

	public float getExento() 
	{
		return m_Exento;	
	}

	public float getDeduccion() 
	{
		return m_Deduccion;	
	}
}


