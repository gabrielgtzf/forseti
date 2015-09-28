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


public class VIEW_ISR
{
	private byte m_ID_Isr;
	private float m_Limite_Inferior;
	private float m_Limite_Superior;
	private float m_Cuota_Fija;
	private float m_Porcentaje_Exd;
	private float m_Subsidio;
	private float m_Subsidio_SIM;
	private float m_Limite_Inferior_Anual;
	private float m_Limite_Superior_Anual;
	private float m_Cuota_Fija_Anual;
	private float m_Porcentaje_Exd_Anual;
	private float m_Subsidio_Anual;
	private float m_Subsidio_SIM_Anual;

	public void setID_Isr(byte ID_Isr)
	{
		m_ID_Isr = ID_Isr;
	}

	public void setLimite_Inferior(float Limite_Inferior)
	{
		m_Limite_Inferior = Limite_Inferior;
	}

	public void setLimite_Superior(float Limite_Superior)
	{
		m_Limite_Superior = Limite_Superior;
	}

	public void setCuota_Fija(float Cuota_Fija)
	{
		m_Cuota_Fija = Cuota_Fija;
	}

	public void setPorcentaje_Exd(float Porcentaje_Exd)
	{
		m_Porcentaje_Exd = Porcentaje_Exd;
	}

	public void setSubsidio(float Subsidio)
	{
		m_Subsidio = Subsidio;
	}

	public void setSubsidio_SIM(float Subsidio_SIM)
	{
		m_Subsidio_SIM = Subsidio_SIM;
	}

	public void setLimite_Inferior_Anual(float Limite_Inferior_Anual)
	{
		m_Limite_Inferior_Anual = Limite_Inferior_Anual;
	}

	public void setLimite_Superior_Anual(float Limite_Superior_Anual)
	{
		m_Limite_Superior_Anual = Limite_Superior_Anual;
	}

	public void setCuota_Fija_Anual(float Cuota_Fija_Anual)
	{
		m_Cuota_Fija_Anual = Cuota_Fija_Anual;
	}

	public void setPorcentaje_Exd_Anual(float Porcentaje_Exd_Anual)
	{
		m_Porcentaje_Exd_Anual = Porcentaje_Exd_Anual;
	}

	public void setSubsidio_Anual(float Subsidio_Anual)
	{
		m_Subsidio_Anual = Subsidio_Anual;
	}

	public void setSubsidio_SIM_Anual(float Subsidio_SIM_Anual)
	{
		m_Subsidio_SIM_Anual = Subsidio_SIM_Anual;
	}


	public byte getID_Isr()
	{
		return m_ID_Isr;
	}

	public float getLimite_Inferior()
	{
		return m_Limite_Inferior;
	}

	public float getLimite_Superior()
	{
		return m_Limite_Superior;
	}

	public float getCuota_Fija()
	{
		return m_Cuota_Fija;
	}

	public float getPorcentaje_Exd()
	{
		return m_Porcentaje_Exd;
	}

	public float getSubsidio()
	{
		return m_Subsidio;
	}

	public float getSubsidio_SIM()
	{
		return m_Subsidio_SIM;
	}

	public float getLimite_Inferior_Anual()
	{
		return m_Limite_Inferior_Anual;
	}

	public float getLimite_Superior_Anual()
	{
		return m_Limite_Superior_Anual;
	}

	public float getCuota_Fija_Anual()
	{
		return m_Cuota_Fija_Anual;
	}

	public float getPorcentaje_Exd_Anual()
	{
		return m_Porcentaje_Exd_Anual;
	}

	public float getSubsidio_Anual()
	{
		return m_Subsidio_Anual;
	}

	public float getSubsidio_SIM_Anual()
	{
		return m_Subsidio_SIM_Anual;
	}


}

