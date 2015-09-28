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

public class view_invserv_inventarios_existencias_saldos
{
	private byte m_Mes;
	private int m_Ano;
	private String m_ID_Tipo;
	private String m_ID_Prod;
	private byte m_ID_Bodega;
	private String m_Nombre;
	private float m_ExistenciaIni;
	private float m_ExistenciaFin;
	private String m_ID_InvServ;
	private String m_Unidad;

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Prod(String ID_Prod)
	{
		m_ID_Prod = ID_Prod;
	}

	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setExistenciaIni(float ExistenciaIni)
	{
		m_ExistenciaIni = ExistenciaIni;
	}

	public void setExistenciaFin(float ExistenciaFin)
	{
		m_ExistenciaFin = ExistenciaFin;
	}

	public void setID_InvServ(String ID_InvServ)
	{
		m_ID_InvServ = ID_InvServ;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}


	public byte getMes()
	{
		return m_Mes;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getID_Prod()
	{
		return m_ID_Prod;
	}

	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public float getExistenciaIni()
	{
		return m_ExistenciaIni;
	}

	public float getExistenciaFin()
	{
		return m_ExistenciaFin;
	}

	public String getID_InvServ()
	{
		return m_ID_InvServ;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}


}

