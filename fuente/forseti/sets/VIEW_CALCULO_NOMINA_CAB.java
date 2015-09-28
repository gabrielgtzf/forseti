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

public class VIEW_CALCULO_NOMINA_CAB
{
	private int m_ID_Nomina;
	private String m_ID_Empleado;
	private String m_Nombre;
	private String m_Compania;
	private String m_Nombre_Compania;
	private String m_Departamento;
	private int m_Ano;
	private byte m_Tipo;
	private String m_Tipo_Nomina;
	private int m_Numero_Nomina;
	private Date m_Fecha_Desde;
	private Date m_Fecha_Hasta;
	private float m_Dias;
	private float m_Faltas;
	private int m_Recibo;
	private float m_HE;
	private float m_HD;
	private String m_RFC;
	private String m_Num_Registro_IMSS;
	private float m_Salario_Nominal;
	private float m_Pago_Neto;

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

	public void setCompania(String Compania)
	{
		m_Compania = Compania;
	}

	public void setNombre_Compania(String Nombre_Compania)
	{
		m_Nombre_Compania = Nombre_Compania;
	}

	public void setDepartamento(String Departamento)
	{
		m_Departamento = Departamento;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setTipo_Nomina(String Tipo_Nomina)
	{
		m_Tipo_Nomina = Tipo_Nomina;
	}

	public void setNumero_Nomina(int Numero_Nomina)
	{
		m_Numero_Nomina = Numero_Nomina;
	}

	public void setFecha_Desde(Date Fecha_Desde)
	{
		m_Fecha_Desde = Fecha_Desde;
	}

	public void setFecha_Hasta(Date Fecha_Hasta)
	{
		m_Fecha_Hasta = Fecha_Hasta;
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

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setNum_Registro_IMSS(String Num_Registro_IMSS)
	{
		m_Num_Registro_IMSS = Num_Registro_IMSS;
	}

	public void setSalario_Nominal(float Salario_Nominal)
	{
		m_Salario_Nominal = Salario_Nominal;
	}

	public void setPago_Neto(float Pago_Neto)
	{
		m_Pago_Neto = Pago_Neto;
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

	public String getCompania()
	{
		return m_Compania;
	}

	public String getNombre_Compania()
	{
		return m_Nombre_Compania;
	}

	public String getDepartamento()
	{
		return m_Departamento;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getTipo_Nomina()
	{
		return m_Tipo_Nomina;
	}

	public int getNumero_Nomina()
	{
		return m_Numero_Nomina;
	}

	public Date getFecha_Desde()
	{
		return m_Fecha_Desde;
	}

	public Date getFecha_Hasta()
	{
		return m_Fecha_Hasta;
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

	public String getRFC()
	{
		return m_RFC;
	}

	public String getNum_Registro_IMSS()
	{
		return m_Num_Registro_IMSS;
	}

	public float getSalario_Nominal()
	{
		return m_Salario_Nominal;
	}

	public float getPago_Neto()
	{
		return m_Pago_Neto;
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
	
}

