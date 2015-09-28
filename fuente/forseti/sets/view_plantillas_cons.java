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

public class view_plantillas_cons
{
	private int m_ID_Plantilla;
	private int m_ID_Movimiento;
	private String m_Movimiento;
	private Date m_Fecha;
	private String m_Descripcion;
	private boolean m_bID_Empleado;
	private String m_ID_Empleado;
	private String m_Empleado;
	private boolean m_bNomina;
	private int m_Ano;
	private int m_Numero_Nomina;
	private boolean m_bTipo_Nomina;
	private int m_Tipo_de_Nomina;
	private boolean m_bCompania_Sucursal;
	private String m_sCompania_Sucursal;
	private boolean m_bNivel_Confianza;
	private int m_Nivel_de_Confianza;
	private byte m_Aplicacion;
	private float m_Horas;
	private float m_Dias;
	private float m_Veces_Importe;
	private float m_Importe;
	private boolean m_bExento;
	private float m_Exento;
	private boolean m_Mixto;
	private boolean m_Inclusiones;
	private float m_Cantidad;
	
	public void setID_Plantilla(int ID_Plantilla)
	{
		m_ID_Plantilla = ID_Plantilla;
	}

	public void setID_Movimiento(int ID_Movimiento)
	{
		m_ID_Movimiento = ID_Movimiento;
	}

	public void setMovimiento(String Movimiento)
	{
		m_Movimiento = Movimiento;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setbID_Empleado(boolean bID_Empleado)
	{
		m_bID_Empleado = bID_Empleado;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setEmpleado(String Empleado)
	{
		m_Empleado = Empleado;
	}

	public void setbNomina(boolean bNomina)
	{
		m_bNomina = bNomina;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setNumero_Nomina(int Numero_Nomina)
	{
		m_Numero_Nomina = Numero_Nomina;
	}

	public void setbTipo_Nomina(boolean bTipo_Nomina)
	{
		m_bTipo_Nomina = bTipo_Nomina;
	}

	public void setTipo_de_Nomina(int Tipo_de_Nomina)
	{
		m_Tipo_de_Nomina = Tipo_de_Nomina;
	}

	public void setbCompania_Sucursal(boolean bCompania_Sucursal)
	{
		m_bCompania_Sucursal = bCompania_Sucursal;
	}

	public void setsCompania_Sucursal(String sCompania_Sucursal)
	{
		m_sCompania_Sucursal = sCompania_Sucursal;
	}

	public void setbNivel_Confianza(boolean bNivel_Confianza)
	{
		m_bNivel_Confianza = bNivel_Confianza;
	}

	public void setNivel_de_Confianza(int Nivel_de_Confianza)
	{
		m_Nivel_de_Confianza = Nivel_de_Confianza;
	}

	public void setAplicacion(byte Aplicacion)
	{
		m_Aplicacion = Aplicacion;
	}

	public void setHoras(float Horas)
	{
		m_Horas = Horas;
	}

	public void setDias(float Dias)
	{
		m_Dias = Dias;
	}

	public void setVeces_Importe(float Veces_Importe)
	{
		m_Veces_Importe = Veces_Importe;
	}

	public void setImporte(float Importe)
	{
		m_Importe = Importe;
	}

	public void setbExento(boolean bExento)
	{
		m_bExento = bExento;
	}

	public void setExento(float Exento)
	{
		m_Exento = Exento;
	}

	public void setMixto(boolean Mixto)
	{
		m_Mixto = Mixto;
	}

	public void setInclusiones(boolean Inclusiones)
	{
		m_Inclusiones = Inclusiones;
	}


	public int getID_Plantilla()
	{
		return m_ID_Plantilla;
	}

	public int getID_Movimiento()
	{
		return m_ID_Movimiento;
	}

	public String getMovimiento()
	{
		return m_Movimiento;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getbID_Empleado()
	{
		return m_bID_Empleado;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getEmpleado()
	{
		return m_Empleado;
	}

	public boolean getbNomina()
	{
		return m_bNomina;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public int getNumero_Nomina()
	{
		return m_Numero_Nomina;
	}

	public boolean getbTipo_Nomina()
	{
		return m_bTipo_Nomina;
	}

	public int getTipo_de_Nomina()
	{
		return m_Tipo_de_Nomina;
	}

	public boolean getbCompania_Sucursal()
	{
		return m_bCompania_Sucursal;
	}

	public String getsCompania_Sucursal()
	{
		return m_sCompania_Sucursal;
	}

	public boolean getbNivel_Confianza()
	{
		return m_bNivel_Confianza;
	}

	public int getNivel_de_Confianza()
	{
		return m_Nivel_de_Confianza;
	}

	public byte getAplicacion()
	{
		return m_Aplicacion;
	}

	public float getHoras()
	{
		return m_Horas;
	}

	public float getDias()
	{
		return m_Dias;
	}

	public float getVeces_Importe()
	{
		return m_Veces_Importe;
	}

	public float getImporte()
	{
		return m_Importe;
	}

	public boolean getbExento()
	{
		return m_bExento;
	}

	public float getExento()
	{
		return m_Exento;
	}

	public boolean getMixto()
	{
		return m_Mixto;
	}

	public boolean getInclusiones()
	{
		return m_Inclusiones;
	}

	public void setCantidad(float Cantidad) 
	{
		m_Cantidad = Cantidad;	
	}

	public float getCantidad()
	{
		return m_Cantidad;
	}
}

