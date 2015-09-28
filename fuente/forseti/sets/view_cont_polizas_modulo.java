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

import java.sql.Date;

public class view_cont_polizas_modulo
{
	private String m_Concepto;
	private float m_Debe;
	private String m_Estatus;
	private Date m_Fecha;
	private float m_Haber;
	private long m_ID;
	private int m_Num;
	private String m_Ref;
	private String m_Tipo;
	private String m_ID_Clasificacion;
	private String m_CE;
	
	public String getID_Clasificacion() 
	{
		return m_ID_Clasificacion;
	}

	public void setID_Clasificacion(String ID_Clasificacion) 
	{
		m_ID_Clasificacion = ID_Clasificacion;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setDebe(float Debe)
	{
		m_Debe = Debe;
	}

	public void setEstatus(String Estatus)
	{
		m_Estatus = Estatus;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setHaber(float Haber)
	{
		m_Haber = Haber;
	}

	public void setID(long ID)
	{
		m_ID = ID;
	}

	public void setNum(int Num)
	{
		m_Num = Num;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}


	public String getConcepto()
	{
		return m_Concepto;
	}

	public float getDebe()
	{
		return m_Debe;
	}

	public String getEstatus()
	{
		return m_Estatus;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public float getHaber()
	{
		return m_Haber;
	}

	public long getID()
	{
		return m_ID;
	}

	public int getNum()
	{
		return m_Num;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public String getCE()
	{
		return m_CE;
	}
	
	public void setCE(String CE)
	{
		m_CE = CE;
	}
}

