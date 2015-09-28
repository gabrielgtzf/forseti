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


public class view_invserv_inventarios_modulo
{
	private String m_Clave;
	private String m_Descripcion;
	private float m_Existencia;
	private String m_Cuenta;
	private String m_Unidad;
	private String m_Linea;
	private String m_ID_Tipo;
    private String m_Status;
	private String m_Descripcion_Linea;

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setExistencia(float Existencia)
	{
		m_Existencia = Existencia;
	}

	public void setCuenta(String Cuenta)
	{
		m_Cuenta = Cuenta;
	}

	public void setUnidad(String Unidad)
	{
		m_Unidad = Unidad;
	}

	public void setLinea(String Linea)
	{
		m_Linea = Linea;
	}

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

        public void setStatus(String Status)
        {
                m_Status = Status;
        }

	public String getClave()
	{
		return m_Clave;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public float getExistencia()
	{
		return m_Existencia;
	}

	public String getCuenta()
	{
		return m_Cuenta;
	}

	public String getUnidad()
	{
		return m_Unidad;
	}

	public String getLinea()
	{
		return m_Linea;
	}

	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public void setDescripcion_Linea(String Descripcion_Linea) 
	{
		m_Descripcion_Linea = Descripcion_Linea;		
	}

	public String getDescripcion_Linea() 
	{
		return m_Descripcion_Linea;		
	}
}

