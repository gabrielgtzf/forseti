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

public class view_masemp
{
	private String m_ID_Empleado;
	private String m_Compania_Sucursal;
	private String m_ID_Departamento;
	private String m_Nombre_Departamento;
	private byte m_ID_Turno;
	private byte m_ID_Turno_Comedor;
	private byte m_ID_Categoria;
	private String m_Nombre;
	private String m_Apellido_Paterno;
	private String m_Apellido_Materno;
	private Date m_Fecha_de_Nacimiento;
	private Date m_Fecha_de_Ingreso;
	private boolean m_Clave_Cambio_Empresa;
	private Date m_Fecha_Cambio_Empresa;
	private byte m_Zona_Economica;
	private String m_RFC_Letras;
	private String m_RFC_Fecha;
	private String m_RFC_Homoclave;
	private String m_RFC_Digito;
	private String m_CURP;
	private boolean m_CalculoSimplificado;
	private byte m_Tipo_de_Nomina;
	private byte m_Jornada;
	private boolean m_Sindicalizado;
	private float m_Horas_por_Jornada;
	private byte m_Status;
	private String m_Motivo_Baja;
	private boolean m_Reparto_de_Utilidades;
	private boolean m_Premio_de_Puntualidad;
	private boolean m_Castigo_Impuntualidad;
	private String m_Puesto;
	private float m_Salario_Nominal;
	private float m_Salario_Diario;
	private float m_Salario_por_Hora;
	private float m_Salario_Integrado;
	private boolean m_Aplica_Horas_Extras;
	private Date m_Fecha_Vacaciones;
	private byte m_Dias_Vacaciones;
	private float m_Prima_de_Vacaciones;
	private String m_Num_Registro_IMSS;
	private String m_Jefe_Inmediato;
	private String m_Calle;
	private String m_Numero;
	private String m_Colonia;
	private String m_Codigo_Postal;
	private String m_Delegacion;
	private String m_Estado_Civil;
	private String m_Nombre_Esposo;
	private byte m_Num_de_Hijos;
	private String m_Nombre_de_Hijos;
	private String m_Nombre_Padre;
	private boolean m_Vive;
	private String m_Nombre_Madre;
	private boolean m_Viva;
	private String m_Escolaridad;
	private String m_Trabajo_Anterior_Grupo;
	private String m_Ultimo_Trabajo;
	private String m_Recomendado_Por;
	private String m_En_Accidente_Avisar;
	private Date m_Fecha_para_Liquidaciones;
	private boolean m_ObrEmp;
	private Date m_Fecha_Cambio_Obrero_Empleado;
	private String m_Cuenta_Bancaria;
	private String m_Historial_Puestos;
	private String m_Registro_Infonavit;
	private float m_Prestamo_Infonavit;
	private float m_Porcentaje_Descuento;
	private float m_Prestamo_VSM;
	private float m_Descuento_VSM;
	private boolean m_Clave_Alta_Infonavit;
	private Date m_Fecha_Alta_Infonavit;
	private boolean m_Clave_Liquidacion_Infonavit;
	private Date m_Fecha_Liquidacion_Infonavit;
	private String m_Registro_Fonacot;
	private String m_Numero_de_Credito;
	private float m_Prestamo_Fonacot;
	private boolean m_Ayuda_Vales_de_Despensa;
	private float m_Importe_Vales_de_Despensa;
	private String m_ID_XAction;
	private boolean m_CompensacionAnual;
	private float m_Salario_Mixto;
	private boolean m_CalculoMixto;
	private float m_CompensacionAnualFija;
	private String m_Pais;
	private String m_Estado;
	private String m_Localidad;
	private String m_NoInt;
	private String m_ID_SatBanco;
	private int m_Regimen;
	private float m_PCS;

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setCompania_Sucursal(String Compania_Sucursal)
	{
		m_Compania_Sucursal = Compania_Sucursal;
	}

	public void setID_Departamento(String ID_Departamento)
	{
		m_ID_Departamento = ID_Departamento;
	}

	public void setNombre_Departamento(String Nombre_Departamento)
	{
		m_Nombre_Departamento = Nombre_Departamento;
	}

	public void setID_Turno(byte ID_Turno)
	{
		m_ID_Turno = ID_Turno;
	}

	public void setID_Turno_Comedor(byte ID_Turno_Comedor)
	{
		m_ID_Turno_Comedor = ID_Turno_Comedor;
	}

	public void setID_Categoria(byte ID_Categoria)
	{
		m_ID_Categoria = ID_Categoria;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setApellido_Paterno(String Apellido_Paterno)
	{
		m_Apellido_Paterno = Apellido_Paterno;
	}

	public void setApellido_Materno(String Apellido_Materno)
	{
		m_Apellido_Materno = Apellido_Materno;
	}

	public void setFecha_de_Nacimiento(Date Fecha_de_Nacimiento)
	{
		m_Fecha_de_Nacimiento = Fecha_de_Nacimiento;
	}

	public void setFecha_de_Ingreso(Date Fecha_de_Ingreso)
	{
		m_Fecha_de_Ingreso = Fecha_de_Ingreso;
	}

	public void setClave_Cambio_Empresa(boolean Clave_Cambio_Empresa)
	{
		m_Clave_Cambio_Empresa = Clave_Cambio_Empresa;
	}

	public void setFecha_Cambio_Empresa(Date Fecha_Cambio_Empresa)
	{
		m_Fecha_Cambio_Empresa = Fecha_Cambio_Empresa;
	}

	public void setZona_Economica(byte Zona_Economica)
	{
		m_Zona_Economica = Zona_Economica;
	}

	public void setRFC_Letras(String RFC_Letras)
	{
		m_RFC_Letras = RFC_Letras;
	}

	public void setRFC_Fecha(String RFC_Fecha)
	{
		m_RFC_Fecha = RFC_Fecha;
	}

	public void setRFC_Homoclave(String RFC_Homoclave)
	{
		m_RFC_Homoclave = RFC_Homoclave;
	}

	public void setRFC_Digito(String RFC_Digito)
	{
		m_RFC_Digito = RFC_Digito;
	}

	public void setCURP(String CURP)
	{
		m_CURP = CURP;
	}

	public void setCalculoSimplificado(boolean CalculoSimplificado)
	{
		m_CalculoSimplificado = CalculoSimplificado;
	}

	public void setTipo_de_Nomina(byte Tipo_de_Nomina)
	{
		m_Tipo_de_Nomina = Tipo_de_Nomina;
	}

	public void setJornada(byte Jornada)
	{
		m_Jornada = Jornada;
	}

	public void setSindicalizado(boolean Sindicalizado)
	{
		m_Sindicalizado = Sindicalizado;
	}

	public void setHoras_por_Jornada(float Horas_por_Jornada)
	{
		m_Horas_por_Jornada = Horas_por_Jornada;
	}

	public void setStatus(byte Status)
	{
		m_Status = Status;
	}

	public void setMotivo_Baja(String Motivo_Baja)
	{
		m_Motivo_Baja = Motivo_Baja;
	}

	public void setReparto_de_Utilidades(boolean Reparto_de_Utilidades)
	{
		m_Reparto_de_Utilidades = Reparto_de_Utilidades;
	}

	public void setPremio_de_Puntualidad(boolean Premio_de_Puntualidad)
	{
		m_Premio_de_Puntualidad = Premio_de_Puntualidad;
	}

	public void setCastigo_Impuntualidad(boolean Castigo_Impuntualidad)
	{
		m_Castigo_Impuntualidad = Castigo_Impuntualidad;
	}

	public void setPuesto(String Puesto)
	{
		m_Puesto = Puesto;
	}

	public void setSalario_Nominal(float Salario_Nominal)
	{
		m_Salario_Nominal = Salario_Nominal;
	}

	public void setSalario_Diario(float Salario_Diario)
	{
		m_Salario_Diario = Salario_Diario;
	}

	public void setSalario_por_Hora(float Salario_por_Hora)
	{
		m_Salario_por_Hora = Salario_por_Hora;
	}

	public void setSalario_Integrado(float Salario_Integrado)
	{
		m_Salario_Integrado = Salario_Integrado;
	}

	public void setAplica_Horas_Extras(boolean Aplica_Horas_Extras)
	{
		m_Aplica_Horas_Extras = Aplica_Horas_Extras;
	}

	public void setFecha_Vacaciones(Date Fecha_Vacaciones)
	{
		m_Fecha_Vacaciones = Fecha_Vacaciones;
	}

	public void setDias_Vacaciones(byte Dias_Vacaciones)
	{
		m_Dias_Vacaciones = Dias_Vacaciones;
	}

	public void setPrima_de_Vacaciones(float Prima_de_Vacaciones)
	{
		m_Prima_de_Vacaciones = Prima_de_Vacaciones;
	}

	public void setNum_Registro_IMSS(String Num_Registro_IMSS)
	{
		m_Num_Registro_IMSS = Num_Registro_IMSS;
	}

	public void setJefe_Inmediato(String Jefe_Inmediato)
	{
		m_Jefe_Inmediato = Jefe_Inmediato;
	}

	public void setCalle(String Calle)
	{
		m_Calle = Calle;
	}

	public void setNumero(String Numero)
	{
		m_Numero = Numero;
	}

	public void setColonia(String Colonia)
	{
		m_Colonia = Colonia;
	}

	public void setCodigo_Postal(String Codigo_Postal)
	{
		m_Codigo_Postal = Codigo_Postal;
	}

	public void setDelegacion(String Delegacion)
	{
		m_Delegacion = Delegacion;
	}

	public void setEstado_Civil(String Estado_Civil)
	{
		m_Estado_Civil = Estado_Civil;
	}

	public void setNombre_Esposo(String Nombre_Esposo)
	{
		m_Nombre_Esposo = Nombre_Esposo;
	}

	public void setNum_de_Hijos(byte Num_de_Hijos)
	{
		m_Num_de_Hijos = Num_de_Hijos;
	}

	public void setNombre_de_Hijos(String Nombre_de_Hijos)
	{
		m_Nombre_de_Hijos = Nombre_de_Hijos;
	}

	public void setNombre_Padre(String Nombre_Padre)
	{
		m_Nombre_Padre = Nombre_Padre;
	}

	public void setVive(boolean Vive)
	{
		m_Vive = Vive;
	}

	public void setNombre_Madre(String Nombre_Madre)
	{
		m_Nombre_Madre = Nombre_Madre;
	}

	public void setViva(boolean Viva)
	{
		m_Viva = Viva;
	}

	public void setEscolaridad(String Escolaridad)
	{
		m_Escolaridad = Escolaridad;
	}

	public void setTrabajo_Anterior_Grupo(String Trabajo_Anterior_Grupo)
	{
		m_Trabajo_Anterior_Grupo = Trabajo_Anterior_Grupo;
	}

	public void setUltimo_Trabajo(String Ultimo_Trabajo)
	{
		m_Ultimo_Trabajo = Ultimo_Trabajo;
	}

	public void setRecomendado_Por(String Recomendado_Por)
	{
		m_Recomendado_Por = Recomendado_Por;
	}

	public void setEn_Accidente_Avisar(String En_Accidente_Avisar)
	{
		m_En_Accidente_Avisar = En_Accidente_Avisar;
	}

	public void setFecha_para_Liquidaciones(Date Fecha_para_Liquidaciones)
	{
		m_Fecha_para_Liquidaciones = Fecha_para_Liquidaciones;
	}

	public void setObrEmp(boolean ObrEmp)
	{
		m_ObrEmp = ObrEmp;
	}

	public void setFecha_Cambio_Obrero_Empleado(Date Fecha_Cambio_Obrero_Empleado)
	{
		m_Fecha_Cambio_Obrero_Empleado = Fecha_Cambio_Obrero_Empleado;
	}

	public void setCuenta_Bancaria(String Cuenta_Bancaria)
	{
		m_Cuenta_Bancaria = Cuenta_Bancaria;
	}

	public void setHistorial_Puestos(String Historial_Puestos)
	{
		m_Historial_Puestos = Historial_Puestos;
	}

	public void setRegistro_Infonavit(String Registro_Infonavit)
	{
		m_Registro_Infonavit = Registro_Infonavit;
	}

	public void setPrestamo_Infonavit(float Prestamo_Infonavit)
	{
		m_Prestamo_Infonavit = Prestamo_Infonavit;
	}

	public void setPorcentaje_Descuento(float Porcentaje_Descuento)
	{
		m_Porcentaje_Descuento = Porcentaje_Descuento;
	}

	public void setPrestamo_VSM(float Prestamo_VSM)
	{
		m_Prestamo_VSM = Prestamo_VSM;
	}

	public void setDescuento_VSM(float Descuento_VSM)
	{
		m_Descuento_VSM = Descuento_VSM;
	}

	public void setClave_Alta_Infonavit(boolean Clave_Alta_Infonavit)
	{
		m_Clave_Alta_Infonavit = Clave_Alta_Infonavit;
	}

	public void setFecha_Alta_Infonavit(Date Fecha_Alta_Infonavit)
	{
		m_Fecha_Alta_Infonavit = Fecha_Alta_Infonavit;
	}

	public void setClave_Liquidacion_Infonavit(boolean Clave_Liquidacion_Infonavit)
	{
		m_Clave_Liquidacion_Infonavit = Clave_Liquidacion_Infonavit;
	}

	public void setFecha_Liquidacion_Infonavit(Date Fecha_Liquidacion_Infonavit)
	{
		m_Fecha_Liquidacion_Infonavit = Fecha_Liquidacion_Infonavit;
	}

	public void setRegistro_Fonacot(String Registro_Fonacot)
	{
		m_Registro_Fonacot = Registro_Fonacot;
	}

	public void setNumero_de_Credito(String Numero_de_Credito)
	{
		m_Numero_de_Credito = Numero_de_Credito;
	}

	public void setPrestamo_Fonacot(float Prestamo_Fonacot)
	{
		m_Prestamo_Fonacot = Prestamo_Fonacot;
	}

	public void setAyuda_Vales_de_Despensa(boolean Ayuda_Vales_de_Despensa)
	{
		m_Ayuda_Vales_de_Despensa = Ayuda_Vales_de_Despensa;
	}

	public void setImporte_Vales_de_Despensa(float Importe_Vales_de_Despensa)
	{
		m_Importe_Vales_de_Despensa = Importe_Vales_de_Despensa;
	}

	public void setID_XAction(String ID_XAction)
	{
		m_ID_XAction = ID_XAction;
	}

	public void setCompensacionAnual(boolean CompensacionAnual)
	{
		m_CompensacionAnual = CompensacionAnual;
	}

	public void setSalario_Mixto(float Salario_Mixto)
	{
		m_Salario_Mixto = Salario_Mixto;
	}

	public void setCalculoMixto(boolean CalculoMixto)
	{
		m_CalculoMixto = CalculoMixto;
	}

	public void setCompensacionAnualFija(float CompensacionAnualFija)
	{
		m_CompensacionAnualFija = CompensacionAnualFija;
	}


	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public String getCompania_Sucursal()
	{
		return m_Compania_Sucursal;
	}

	public String getID_Departamento()
	{
		return m_ID_Departamento;
	}

	public String getNombre_Departamento()
	{
		return m_Nombre_Departamento;
	}

	public byte getID_Turno()
	{
		return m_ID_Turno;
	}

	public byte getID_Turno_Comedor()
	{
		return m_ID_Turno_Comedor;
	}

	public byte getID_Categoria()
	{
		return m_ID_Categoria;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getApellido_Paterno()
	{
		return m_Apellido_Paterno;
	}

	public String getApellido_Materno()
	{
		return m_Apellido_Materno;
	}

	public Date getFecha_de_Nacimiento()
	{
		return m_Fecha_de_Nacimiento;
	}

	public Date getFecha_de_Ingreso()
	{
		return m_Fecha_de_Ingreso;
	}

	public boolean getClave_Cambio_Empresa()
	{
		return m_Clave_Cambio_Empresa;
	}

	public Date getFecha_Cambio_Empresa()
	{
		return m_Fecha_Cambio_Empresa;
	}

	public byte getZona_Economica()
	{
		return m_Zona_Economica;
	}

	public String getRFC_Letras()
	{
		return m_RFC_Letras;
	}

	public String getRFC_Fecha()
	{
		return m_RFC_Fecha;
	}

	public String getRFC_Homoclave()
	{
		return m_RFC_Homoclave;
	}

	public String getRFC_Digito()
	{
		return m_RFC_Digito;
	}

	public String getCURP()
	{
		return m_CURP;
	}

	public boolean getCalculoSimplificado()
	{
		return m_CalculoSimplificado;
	}

	public byte getTipo_de_Nomina()
	{
		return m_Tipo_de_Nomina;
	}

	public byte getJornada()
	{
		return m_Jornada;
	}

	public boolean getSindicalizado()
	{
		return m_Sindicalizado;
	}

	public float getHoras_por_Jornada()
	{
		return m_Horas_por_Jornada;
	}

	public byte getStatus()
	{
		return m_Status;
	}

	public String getMotivo_Baja()
	{
		return m_Motivo_Baja;
	}

	public boolean getReparto_de_Utilidades()
	{
		return m_Reparto_de_Utilidades;
	}

	public boolean getPremio_de_Puntualidad()
	{
		return m_Premio_de_Puntualidad;
	}

	public boolean getCastigo_Impuntualidad()
	{
		return m_Castigo_Impuntualidad;
	}

	public String getPuesto()
	{
		return m_Puesto;
	}

	public float getSalario_Nominal()
	{
		return m_Salario_Nominal;
	}

	public float getSalario_Diario()
	{
		return m_Salario_Diario;
	}

	public float getSalario_por_Hora()
	{
		return m_Salario_por_Hora;
	}

	public float getSalario_Integrado()
	{
		return m_Salario_Integrado;
	}

	public boolean getAplica_Horas_Extras()
	{
		return m_Aplica_Horas_Extras;
	}

	public Date getFecha_Vacaciones()
	{
		return m_Fecha_Vacaciones;
	}

	public byte getDias_Vacaciones()
	{
		return m_Dias_Vacaciones;
	}

	public float getPrima_de_Vacaciones()
	{
		return m_Prima_de_Vacaciones;
	}

	public String getNum_Registro_IMSS()
	{
		return m_Num_Registro_IMSS;
	}

	public String getJefe_Inmediato()
	{
		return m_Jefe_Inmediato;
	}

	public String getCalle()
	{
		return m_Calle;
	}

	public String getNumero()
	{
		return m_Numero;
	}

	public String getColonia()
	{
		return m_Colonia;
	}

	public String getCodigo_Postal()
	{
		return m_Codigo_Postal;
	}

	public String getDelegacion()
	{
		return m_Delegacion;
	}

	public String getEstado_Civil()
	{
		return m_Estado_Civil;
	}

	public String getNombre_Esposo()
	{
		return m_Nombre_Esposo;
	}

	public byte getNum_de_Hijos()
	{
		return m_Num_de_Hijos;
	}

	public String getNombre_de_Hijos()
	{
		return m_Nombre_de_Hijos;
	}

	public String getNombre_Padre()
	{
		return m_Nombre_Padre;
	}

	public boolean getVive()
	{
		return m_Vive;
	}

	public String getNombre_Madre()
	{
		return m_Nombre_Madre;
	}

	public boolean getViva()
	{
		return m_Viva;
	}

	public String getEscolaridad()
	{
		return m_Escolaridad;
	}

	public String getTrabajo_Anterior_Grupo()
	{
		return m_Trabajo_Anterior_Grupo;
	}

	public String getUltimo_Trabajo()
	{
		return m_Ultimo_Trabajo;
	}

	public String getRecomendado_Por()
	{
		return m_Recomendado_Por;
	}

	public String getEn_Accidente_Avisar()
	{
		return m_En_Accidente_Avisar;
	}

	public Date getFecha_para_Liquidaciones()
	{
		return m_Fecha_para_Liquidaciones;
	}

	public boolean getObrEmp()
	{
		return m_ObrEmp;
	}

	public Date getFecha_Cambio_Obrero_Empleado()
	{
		return m_Fecha_Cambio_Obrero_Empleado;
	}

	public String getCuenta_Bancaria()
	{
		return m_Cuenta_Bancaria;
	}

	public String getHistorial_Puestos()
	{
		return m_Historial_Puestos;
	}

	public String getRegistro_Infonavit()
	{
		return m_Registro_Infonavit;
	}

	public float getPrestamo_Infonavit()
	{
		return m_Prestamo_Infonavit;
	}

	public float getPorcentaje_Descuento()
	{
		return m_Porcentaje_Descuento;
	}

	public float getPrestamo_VSM()
	{
		return m_Prestamo_VSM;
	}

	public float getDescuento_VSM()
	{
		return m_Descuento_VSM;
	}

	public boolean getClave_Alta_Infonavit()
	{
		return m_Clave_Alta_Infonavit;
	}

	public Date getFecha_Alta_Infonavit()
	{
		return m_Fecha_Alta_Infonavit;
	}

	public boolean getClave_Liquidacion_Infonavit()
	{
		return m_Clave_Liquidacion_Infonavit;
	}

	public Date getFecha_Liquidacion_Infonavit()
	{
		return m_Fecha_Liquidacion_Infonavit;
	}

	public String getRegistro_Fonacot()
	{
		return m_Registro_Fonacot;
	}

	public String getNumero_de_Credito()
	{
		return m_Numero_de_Credito;
	}

	public float getPrestamo_Fonacot()
	{
		return m_Prestamo_Fonacot;
	}

	public boolean getAyuda_Vales_de_Despensa()
	{
		return m_Ayuda_Vales_de_Despensa;
	}

	public float getImporte_Vales_de_Despensa()
	{
		return m_Importe_Vales_de_Despensa;
	}

	public String getID_XAction()
	{
		return m_ID_XAction;
	}

	public boolean getCompensacionAnual()
	{
		return m_CompensacionAnual;
	}

	public float getSalario_Mixto()
	{
		return m_Salario_Mixto;
	}

	public boolean getCalculoMixto()
	{
		return m_CalculoMixto;
	}

	public float getCompensacionAnualFija()
	{
		return m_CompensacionAnualFija;
	}

	public void setRegimen(int Regimen) 
	{
		m_Regimen = Regimen;
	}

	public void setID_SatBanco(String ID_SatBanco) 
	{
		m_ID_SatBanco = ID_SatBanco;	
	}

	public void setNoInt(String NoInt) 
	{
		m_NoInt = NoInt;
	}

	public void setLocalidad(String Localidad) 
	{
		m_Localidad = Localidad;
	}

	public void setEstado(String Estado) 
	{
		m_Estado = Estado;
	}

	public void setPais(String Pais) 
	{
		m_Pais = Pais;
	}

	public int getRegimen() 
	{
		return m_Regimen;
	}

	public String getID_SatBanco() 
	{
		return m_ID_SatBanco;	
	}

	public String getNoInt() 
	{
		return m_NoInt;
	}

	public String getLocalidad() 
	{
		return m_Localidad;
	}

	public String getEstado() 
	{
		return m_Estado;
	}

	public String getPais() 
	{
		return m_Pais;
	}

	public void setPCS(float PCS) 
	{
		m_PCS = PCS;	
	}

	public float getPCS() 
	{
		return m_PCS;	
	}
}

