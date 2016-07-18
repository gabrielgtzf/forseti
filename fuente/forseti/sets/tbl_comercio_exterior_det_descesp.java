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

public class tbl_comercio_exterior_det_descesp
{
	private int m_ID_VC;
	private int m_Partida;
	private int m_Descripcion;
	private String m_Marca;
	private String m_Modelo;
	private String m_SubModelo;
	private String m_NumeroSerie;

	public void setID_VC(int ID_VC)
	{
		m_ID_VC = ID_VC;
	}

	public void setPartida(int Partida)
	{
		m_Partida = Partida;
	}

	public void setDescripcion(int Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setMarca(String Marca)
	{
		m_Marca = Marca;
	}

	public void setModelo(String Modelo)
	{
		m_Modelo = Modelo;
	}

	public void setSubModelo(String SubModelo)
	{
		m_SubModelo = SubModelo;
	}

	public void setNumeroSerie(String NumeroSerie)
	{
		m_NumeroSerie = NumeroSerie;
	}


	public int getID_VC()
	{
		return m_ID_VC;
	}

	public int getPartida()
	{
		return m_Partida;
	}

	public int getDescripcion()
	{
		return m_Descripcion;
	}

	public String getMarca()
	{
		return m_Marca;
	}

	public String getModelo()
	{
		return m_Modelo;
	}

	public String getSubModelo()
	{
		return m_SubModelo;
	}

	public String getNumeroSerie()
	{
		return m_NumeroSerie;
	}


}

