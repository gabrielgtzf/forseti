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

public class view_turnos
{
	private byte m_ID_Turno;
	private String m_Descripcion;
	private boolean m_Lunes;
	private Time m_ELunes;
	private Time m_SLunes;
	private boolean m_Martes;
	private Time m_EMartes;
	private Time m_SMartes;
	private boolean m_Miercoles;
	private Time m_EMiercoles;
	private Time m_SMiercoles;
	private boolean m_Jueves;
	private Time m_EJueves;
	private Time m_SJueves;
	private boolean m_Viernes;
	private Time m_EViernes;
	private Time m_SViernes;
	private boolean m_Sabado;
	private Time m_ESabado;
	private Time m_SSabado;
	private boolean m_Domingo;
	private Time m_EDomingo;
	private Time m_SDomingo;
	private float m_HNALunes;
	private float m_HEALunes;
	private float m_HNAMartes;
	private float m_HEAMartes;
	private float m_HNAMiercoles;
	private float m_HEAMiercoles;
	private float m_HNAJueves;
	private float m_HEAJueves;
	private float m_HNAViernes;
	private float m_HEAViernes;
	private float m_HNASabado;
	private float m_HEASabado;
	private float m_HNADomingo;
	private float m_HEADomingo;
	private byte m_TTLun;
	private byte m_TTMar;
	private byte m_TTMie;
	private byte m_TTJue;
	private byte m_TTVie;
	private byte m_TTSab;
	private byte m_TTDom;

	public void setID_Turno(byte ID_Turno)
	{
		m_ID_Turno = ID_Turno;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setLunes(boolean Lunes)
	{
		m_Lunes = Lunes;
	}

	public void setELunes(Time ELunes)
	{
		m_ELunes = ELunes;
	}

	public void setSLunes(Time SLunes)
	{
		m_SLunes = SLunes;
	}

	public void setMartes(boolean Martes)
	{
		m_Martes = Martes;
	}

	public void setEMartes(Time EMartes)
	{
		m_EMartes = EMartes;
	}

	public void setSMartes(Time SMartes)
	{
		m_SMartes = SMartes;
	}

	public void setMiercoles(boolean Miercoles)
	{
		m_Miercoles = Miercoles;
	}

	public void setEMiercoles(Time EMiercoles)
	{
		m_EMiercoles = EMiercoles;
	}

	public void setSMiercoles(Time SMiercoles)
	{
		m_SMiercoles = SMiercoles;
	}

	public void setJueves(boolean Jueves)
	{
		m_Jueves = Jueves;
	}

	public void setEJueves(Time EJueves)
	{
		m_EJueves = EJueves;
	}

	public void setSJueves(Time SJueves)
	{
		m_SJueves = SJueves;
	}

	public void setViernes(boolean Viernes)
	{
		m_Viernes = Viernes;
	}

	public void setEViernes(Time EViernes)
	{
		m_EViernes = EViernes;
	}

	public void setSViernes(Time SViernes)
	{
		m_SViernes = SViernes;
	}

	public void setSabado(boolean Sabado)
	{
		m_Sabado = Sabado;
	}

	public void setESabado(Time ESabado)
	{
		m_ESabado = ESabado;
	}

	public void setSSabado(Time SSabado)
	{
		m_SSabado = SSabado;
	}

	public void setDomingo(boolean Domingo)
	{
		m_Domingo = Domingo;
	}

	public void setEDomingo(Time EDomingo)
	{
		m_EDomingo = EDomingo;
	}

	public void setSDomingo(Time SDomingo)
	{
		m_SDomingo = SDomingo;
	}

	public void setHNALunes(float HNALunes)
	{
		m_HNALunes = HNALunes;
	}

	public void setHEALunes(float HEALunes)
	{
		m_HEALunes = HEALunes;
	}

	public void setHNAMartes(float HNAMartes)
	{
		m_HNAMartes = HNAMartes;
	}

	public void setHEAMartes(float HEAMartes)
	{
		m_HEAMartes = HEAMartes;
	}

	public void setHNAMiercoles(float HNAMiercoles)
	{
		m_HNAMiercoles = HNAMiercoles;
	}

	public void setHEAMiercoles(float HEAMiercoles)
	{
		m_HEAMiercoles = HEAMiercoles;
	}

	public void setHNAJueves(float HNAJueves)
	{
		m_HNAJueves = HNAJueves;
	}

	public void setHEAJueves(float HEAJueves)
	{
		m_HEAJueves = HEAJueves;
	}

	public void setHNAViernes(float HNAViernes)
	{
		m_HNAViernes = HNAViernes;
	}

	public void setHEAViernes(float HEAViernes)
	{
		m_HEAViernes = HEAViernes;
	}

	public void setHNASabado(float HNASabado)
	{
		m_HNASabado = HNASabado;
	}

	public void setHEASabado(float HEASabado)
	{
		m_HEASabado = HEASabado;
	}

	public void setHNADomingo(float HNADomingo)
	{
		m_HNADomingo = HNADomingo;
	}

	public void setHEADomingo(float HEADomingo)
	{
		m_HEADomingo = HEADomingo;
	}


	public byte getID_Turno()
	{
		return m_ID_Turno;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getLunes()
	{
		return m_Lunes;
	}

	public Time getELunes()
	{
		return m_ELunes;
	}

	public Time getSLunes()
	{
		return m_SLunes;
	}

	public boolean getMartes()
	{
		return m_Martes;
	}

	public Time getEMartes()
	{
		return m_EMartes;
	}

	public Time getSMartes()
	{
		return m_SMartes;
	}

	public boolean getMiercoles()
	{
		return m_Miercoles;
	}

	public Time getEMiercoles()
	{
		return m_EMiercoles;
	}

	public Time getSMiercoles()
	{
		return m_SMiercoles;
	}

	public boolean getJueves()
	{
		return m_Jueves;
	}

	public Time getEJueves()
	{
		return m_EJueves;
	}

	public Time getSJueves()
	{
		return m_SJueves;
	}

	public boolean getViernes()
	{
		return m_Viernes;
	}

	public Time getEViernes()
	{
		return m_EViernes;
	}

	public Time getSViernes()
	{
		return m_SViernes;
	}

	public boolean getSabado()
	{
		return m_Sabado;
	}

	public Time getESabado()
	{
		return m_ESabado;
	}

	public Time getSSabado()
	{
		return m_SSabado;
	}

	public boolean getDomingo()
	{
		return m_Domingo;
	}

	public Time getEDomingo()
	{
		return m_EDomingo;
	}

	public Time getSDomingo()
	{
		return m_SDomingo;
	}

	public float getHNALunes()
	{
		return m_HNALunes;
	}

	public float getHEALunes()
	{
		return m_HEALunes;
	}

	public float getHNAMartes()
	{
		return m_HNAMartes;
	}

	public float getHEAMartes()
	{
		return m_HEAMartes;
	}

	public float getHNAMiercoles()
	{
		return m_HNAMiercoles;
	}

	public float getHEAMiercoles()
	{
		return m_HEAMiercoles;
	}

	public float getHNAJueves()
	{
		return m_HNAJueves;
	}

	public float getHEAJueves()
	{
		return m_HEAJueves;
	}

	public float getHNAViernes()
	{
		return m_HNAViernes;
	}

	public float getHEAViernes()
	{
		return m_HEAViernes;
	}

	public float getHNASabado()
	{
		return m_HNASabado;
	}

	public float getHEASabado()
	{
		return m_HEASabado;
	}

	public float getHNADomingo()
	{
		return m_HNADomingo;
	}

	public float getHEADomingo()
	{
		return m_HEADomingo;
	}

	public byte getTTDom() 
	{
		return m_TTDom;
	}

	public void setTTDom(byte TTDom) 
	{
		m_TTDom = TTDom;
	}

	public byte getTTJue() 
	{
		return m_TTJue;
	}

	public void setTTJue(byte TTJue) 
	{
		m_TTJue = TTJue;
	}

	public byte getTTLun() 
	{
		return m_TTLun;
	}

	public void setTTLun(byte TTLun) 
	{
		m_TTLun = TTLun;
	}

	public byte getTTMar() 
	{
		return m_TTMar;
	}

	public void setTTMar(byte TTMar) 
	{
		m_TTMar = TTMar;
	}

	public byte getTTMie() 
	{
		return m_TTMie;
	}

	public void setTTMie(byte TTMie) 
	{
		m_TTMie = TTMie;
	}

	public byte getTTSab() 
	{
		return m_TTSab;
	}

	public void setTTSab(byte TTSab) 
	{
		m_TTSab = TTSab;
	}

	public byte getTTVie() 
	{
		return m_TTVie;
	}

	public void setTTVie(byte TTVie) 
	{
		m_TTVie = TTVie;
	}


}

