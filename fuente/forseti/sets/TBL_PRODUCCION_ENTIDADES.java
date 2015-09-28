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



public class TBL_PRODUCCION_ENTIDADES
{
	private int m_ID_EntidadProd;
	private short m_ID_TipoEntidad;
	private String m_Serie;
	private int m_Doc;
	private String m_Descripcion;
	private String m_Formato;
	private short m_ID_BodegaMP;
	private short m_ID_BodegaPT;
	private boolean m_Fija;
	private String m_ID_Clasificacion;
	private String m_Status;
	private String m_NombreBodegaMP;
	private String m_NombreBodegaPT;
	private String m_Nombre;

	public void setID_EntidadProd(int ID_EntidadProd)
	{
		m_ID_EntidadProd = ID_EntidadProd;
	}

	public void setID_TipoEntidad(short ID_TipoEntidad)
	{
		m_ID_TipoEntidad = ID_TipoEntidad;
	}

	public void setSerie(String Serie)
	{
		m_Serie = Serie;
	}

	public void setDoc(int Doc)
	{
		m_Doc = Doc;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}

	public void setID_BodegaMP(short ID_BodegaMP)
	{
		m_ID_BodegaMP = ID_BodegaMP;
	}

	public void setID_BodegaPT(short ID_BodegaPT)
	{
		m_ID_BodegaPT = ID_BodegaPT;
	}

	public void setFija(boolean Fija)
	{
		m_Fija = Fija;
	}


	public int getID_EntidadProd()
	{
		return m_ID_EntidadProd;
	}

	public short getID_TipoEntidad()
	{
		return m_ID_TipoEntidad;
	}

	public String getSerie()
	{
		return m_Serie;
	}

	public int getDoc()
	{
		return m_Doc;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getFormato()
	{
		return m_Formato;
	}

	public short getID_BodegaMP()
	{
		return m_ID_BodegaMP;
	}

	public short getID_BodegaPT()
	{
		return m_ID_BodegaPT;
	}

	public boolean getFija()
	{
		return m_Fija;
	}

	public void setID_Clasificacion(String ID_Clasificacion) 
	{
		m_ID_Clasificacion = ID_Clasificacion;
	}

	public void setStatus(String Status) 
	{
		m_Status = Status;
	}
	
	public String getID_Clasificacion() 
	{
		return m_ID_Clasificacion;
	}

	public String getStatus() 
	{
		return m_Status;
	}

	public void setNombreBodegaMP(String NombreBodegaMP) 
	{
		m_NombreBodegaMP = NombreBodegaMP;
	}
	
	public String getNombreBodegaMP() 
	{
		return m_NombreBodegaMP;
	}

	public void setNombreBodegaPT(String NombreBodegaPT) 
	{
		m_NombreBodegaPT = NombreBodegaPT;
	}
	
	public String getNombreBodegaPT() 
	{
		return m_NombreBodegaPT;
	}

	public void setNombre(String Nombre) 
	{
		m_Nombre = Nombre;	
	}
	
	public String getNombre() 
	{
		return m_Nombre;	
	}
}

