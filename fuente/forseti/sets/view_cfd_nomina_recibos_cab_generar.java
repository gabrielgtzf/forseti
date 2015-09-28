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

public class view_cfd_nomina_recibos_cab_generar
{
	private byte m_ID_Sucursal;
	private String m_Descripcion;
	private boolean m_CFD;
	private String m_CFD_Serie;
	private int m_CFD_Folio;
	private int m_CFD_FolioIni;
	private int m_CFD_FolioFin;
	private int m_CFD_NoAprobacion;
	private int m_CFD_AnoAprobacion;
	private String m_CFD_NoCertificado;
	private String m_CFD_ArchivoCertificado;
	private Date m_CFD_CaducidadCertificado;
	private String m_CFD_ArchivoLlave;
	private String m_CFD_ClaveLlave;
	private int m_CFD_ID_Expedicion;
	private String m_CFD_Calle;
	private String m_CFD_NoExt;
	private String m_CFD_NoInt;
	private String m_CFD_Colonia;
	private String m_CFD_Localidad;
	private String m_CFD_Municipio;
	private String m_CFD_Estado;
	private String m_CFD_Pais;
	private String m_CFD_CP;
	private int m_ID_Nomina;
	private int m_Numero;
	private int m_Ano;
	private int m_Tipo;
	private Date m_Fecha_Desde;
	private Date m_Fecha_Hasta;
	private int m_Dias;
	private boolean m_Cerrado;
	private byte m_Mes;
	private String m_Status;
	private String m_FormaPago;
	private int m_ID_Mov;
	private int m_ID_Pol;
	private byte m_Moneda;
	private String m_MonedaSim;
	private float m_TC;
	private int m_Condicion;
	private String m_ID_Empleado;
	private float m_Importe;
	private float m_Descuento;
	private float m_SubTotal;
	private float m_ISR;
	private float m_Total;
	private String m_Nombre;
	private String m_RFC;
	private String m_Calle;
	private String m_NoExt;
	private String m_NoInt;
	private String m_Colonia;
	private String m_Localidad;
	private String m_Municipio;
	private String m_Estado;
	private String m_Pais;
	private String m_CP;
	private String m_MetodoDePago;
	private String m_CURP;
	private int m_TipoRegimen;
	private String m_NumSeguridadSocial;
	private float m_NumDiasPagados;
	private String m_Departamento;
	private String m_CLABE;
	private String m_Banco;
	private Date m_FechaInicioRelLaboral;
	private String m_Puesto;
	private String m_PeriodicidadPago;
	private float m_TotalGravado;
	private float m_TotalExento;
	private float m_TotalDeducciones;
	private float m_TotalDedGravadas;
	private float m_TotalDedExentas;
	private float m_HorasExtras;
	private float m_HorasTriples;
	private float m_HorasDomingo;
	private float m_IXA;
	private float m_IXE;
	private float m_IXM;
	private int m_DiasHorasExtras;

	public void setID_Sucursal(byte ID_Sucursal)
	{
		m_ID_Sucursal = ID_Sucursal;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setCFD(boolean CFD)
	{
		m_CFD = CFD;
	}

	public void setCFD_Serie(String CFD_Serie)
	{
		m_CFD_Serie = CFD_Serie;
	}

	public void setCFD_Folio(int CFD_Folio)
	{
		m_CFD_Folio = CFD_Folio;
	}

	public void setCFD_FolioIni(int CFD_FolioIni)
	{
		m_CFD_FolioIni = CFD_FolioIni;
	}

	public void setCFD_FolioFin(int CFD_FolioFin)
	{
		m_CFD_FolioFin = CFD_FolioFin;
	}

	public void setCFD_NoAprobacion(int CFD_NoAprobacion)
	{
		m_CFD_NoAprobacion = CFD_NoAprobacion;
	}

	public void setCFD_AnoAprobacion(int CFD_AnoAprobacion)
	{
		m_CFD_AnoAprobacion = CFD_AnoAprobacion;
	}

	public void setCFD_NoCertificado(String CFD_NoCertificado)
	{
		m_CFD_NoCertificado = CFD_NoCertificado;
	}

	public void setCFD_ArchivoCertificado(String CFD_ArchivoCertificado)
	{
		m_CFD_ArchivoCertificado = CFD_ArchivoCertificado;
	}

	public void setCFD_CaducidadCertificado(Date CFD_CaducidadCertificado)
	{
		m_CFD_CaducidadCertificado = CFD_CaducidadCertificado;
	}

	public void setCFD_ArchivoLlave(String CFD_ArchivoLlave)
	{
		m_CFD_ArchivoLlave = CFD_ArchivoLlave;
	}

	public void setCFD_ClaveLlave(String CFD_ClaveLlave)
	{
		m_CFD_ClaveLlave = CFD_ClaveLlave;
	}

	public void setCFD_ID_Expedicion(int CFD_ID_Expedicion)
	{
		m_CFD_ID_Expedicion = CFD_ID_Expedicion;
	}

	public void setCFD_Calle(String CFD_Calle)
	{
		m_CFD_Calle = CFD_Calle;
	}

	public void setCFD_NoExt(String CFD_NoExt)
	{
		m_CFD_NoExt = CFD_NoExt;
	}

	public void setCFD_NoInt(String CFD_NoInt)
	{
		m_CFD_NoInt = CFD_NoInt;
	}

	public void setCFD_Colonia(String CFD_Colonia)
	{
		m_CFD_Colonia = CFD_Colonia;
	}

	public void setCFD_Localidad(String CFD_Localidad)
	{
		m_CFD_Localidad = CFD_Localidad;
	}

	public void setCFD_Municipio(String CFD_Municipio)
	{
		m_CFD_Municipio = CFD_Municipio;
	}

	public void setCFD_Estado(String CFD_Estado)
	{
		m_CFD_Estado = CFD_Estado;
	}

	public void setCFD_Pais(String CFD_Pais)
	{
		m_CFD_Pais = CFD_Pais;
	}

	public void setCFD_CP(String CFD_CP)
	{
		m_CFD_CP = CFD_CP;
	}

	public void setID_Nomina(int ID_Nomina)
	{
		m_ID_Nomina = ID_Nomina;
	}

	public void setNumero(int Numero)
	{
		m_Numero = Numero;
	}

	public void setAno(int Ano)
	{
		m_Ano = Ano;
	}

	public void setTipo(int Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setFecha_Desde(Date Fecha_Desde)
	{
		m_Fecha_Desde = Fecha_Desde;
	}

	public void setFecha_Hasta(Date Fecha_Hasta)
	{
		m_Fecha_Hasta = Fecha_Hasta;
	}

	public void setDias(int Dias)
	{
		m_Dias = Dias;
	}

	public void setCerrado(boolean Cerrado)
	{
		m_Cerrado = Cerrado;
	}

	public void setMes(byte Mes)
	{
		m_Mes = Mes;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setFormaPago(String FormaPago)
	{
		m_FormaPago = FormaPago;
	}

	public void setID_Mov(int ID_Mov)
	{
		m_ID_Mov = ID_Mov;
	}

	public void setID_Pol(int ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setMonedaSim(String MonedaSim)
	{
		m_MonedaSim = MonedaSim;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setCondicion(int Condicion)
	{
		m_Condicion = Condicion;
	}

	public void setID_Empleado(String ID_Empleado)
	{
		m_ID_Empleado = ID_Empleado;
	}

	public void setImporte(float Importe)
	{
		m_Importe = Importe;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}

	public void setSubTotal(float SubTotal)
	{
		m_SubTotal = SubTotal;
	}

	public void setISR(float ISR)
	{
		m_ISR = ISR;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setNombre(String Nombre)
	{
		m_Nombre = Nombre;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setCalle(String Calle)
	{
		m_Calle = Calle;
	}

	public void setNoExt(String NoExt)
	{
		m_NoExt = NoExt;
	}

	public void setNoInt(String NoInt)
	{
		m_NoInt = NoInt;
	}

	public void setColonia(String Colonia)
	{
		m_Colonia = Colonia;
	}

	public void setLocalidad(String Localidad)
	{
		m_Localidad = Localidad;
	}

	public void setMunicipio(String Municipio)
	{
		m_Municipio = Municipio;
	}

	public void setEstado(String Estado)
	{
		m_Estado = Estado;
	}

	public void setPais(String Pais)
	{
		m_Pais = Pais;
	}

	public void setCP(String CP)
	{
		m_CP = CP;
	}

	public void setMetodoDePago(String MetodoDePago)
	{
		m_MetodoDePago = MetodoDePago;
	}

	public void setCURP(String CURP)
	{
		m_CURP = CURP;
	}

	public void setTipoRegimen(int TipoRegimen)
	{
		m_TipoRegimen = TipoRegimen;
	}

	public void setNumSeguridadSocial(String NumSeguridadSocial)
	{
		m_NumSeguridadSocial = NumSeguridadSocial;
	}

	public void setNumDiasPagados(float NumDiasPagados)
	{
		m_NumDiasPagados = NumDiasPagados;
	}

	public void setDepartamento(String Departamento)
	{
		m_Departamento = Departamento;
	}

	public void setCLABE(String CLABE)
	{
		m_CLABE = CLABE;
	}

	public void setBanco(String Banco)
	{
		m_Banco = Banco;
	}

	public void setFechaInicioRelLaboral(Date FechaInicioRelLaboral)
	{
		m_FechaInicioRelLaboral = FechaInicioRelLaboral;
	}

	public void setPuesto(String Puesto)
	{
		m_Puesto = Puesto;
	}

	public void setPeriodicidadPago(String PeriodicidadPago)
	{
		m_PeriodicidadPago = PeriodicidadPago;
	}

	public void setTotalGravado(float TotalGravado)
	{
		m_TotalGravado = TotalGravado;
	}

	public void setTotalExento(float TotalExento)
	{
		m_TotalExento = TotalExento;
	}

	public void setTotalDeducciones(float TotalDeducciones)
	{
		m_TotalDeducciones = TotalDeducciones;
	}

	public void setTotalDedGravadas(float TotalDedGravadas)
	{
		m_TotalDedGravadas = TotalDedGravadas;
	}

	public void setTotalDedExentas(float TotalDedExentas)
	{
		m_TotalDedExentas = TotalDedExentas;
	}

	public void setHorasExtras(float HorasExtras)
	{
		m_HorasExtras = HorasExtras;
	}

	public void setHorasTriples(float HorasTriples)
	{
		m_HorasTriples = HorasTriples;
	}

	public void setHorasDomingo(float HorasDomingo)
	{
		m_HorasDomingo = HorasDomingo;
	}

	public void setIXA(float IXA)
	{
		m_IXA = IXA;
	}

	public void setIXE(float IXE)
	{
		m_IXE = IXE;
	}

	public void setIXM(float IXM)
	{
		m_IXM = IXM;
	}

	public void setDiasHorasExtras(int DiasHorasExtras)
	{
		m_DiasHorasExtras = DiasHorasExtras;
	}


	public byte getID_Sucursal()
	{
		return m_ID_Sucursal;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getCFD()
	{
		return m_CFD;
	}

	public String getCFD_Serie()
	{
		return m_CFD_Serie;
	}

	public int getCFD_Folio()
	{
		return m_CFD_Folio;
	}

	public int getCFD_FolioIni()
	{
		return m_CFD_FolioIni;
	}

	public int getCFD_FolioFin()
	{
		return m_CFD_FolioFin;
	}

	public int getCFD_NoAprobacion()
	{
		return m_CFD_NoAprobacion;
	}

	public int getCFD_AnoAprobacion()
	{
		return m_CFD_AnoAprobacion;
	}

	public String getCFD_NoCertificado()
	{
		return m_CFD_NoCertificado;
	}

	public String getCFD_ArchivoCertificado()
	{
		return m_CFD_ArchivoCertificado;
	}

	public Date getCFD_CaducidadCertificado()
	{
		return m_CFD_CaducidadCertificado;
	}

	public String getCFD_ArchivoLlave()
	{
		return m_CFD_ArchivoLlave;
	}

	public String getCFD_ClaveLlave()
	{
		return m_CFD_ClaveLlave;
	}

	public int getCFD_ID_Expedicion()
	{
		return m_CFD_ID_Expedicion;
	}

	public String getCFD_Calle()
	{
		return m_CFD_Calle;
	}

	public String getCFD_NoExt()
	{
		return m_CFD_NoExt;
	}

	public String getCFD_NoInt()
	{
		return m_CFD_NoInt;
	}

	public String getCFD_Colonia()
	{
		return m_CFD_Colonia;
	}

	public String getCFD_Localidad()
	{
		return m_CFD_Localidad;
	}

	public String getCFD_Municipio()
	{
		return m_CFD_Municipio;
	}

	public String getCFD_Estado()
	{
		return m_CFD_Estado;
	}

	public String getCFD_Pais()
	{
		return m_CFD_Pais;
	}

	public String getCFD_CP()
	{
		return m_CFD_CP;
	}

	public int getID_Nomina()
	{
		return m_ID_Nomina;
	}

	public int getNumero()
	{
		return m_Numero;
	}

	public int getAno()
	{
		return m_Ano;
	}

	public int getTipo()
	{
		return m_Tipo;
	}

	public Date getFecha_Desde()
	{
		return m_Fecha_Desde;
	}

	public Date getFecha_Hasta()
	{
		return m_Fecha_Hasta;
	}

	public int getDias()
	{
		return m_Dias;
	}

	public boolean getCerrado()
	{
		return m_Cerrado;
	}

	public byte getMes()
	{
		return m_Mes;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getFormaPago()
	{
		return m_FormaPago;
	}

	public int getID_Mov()
	{
		return m_ID_Mov;
	}

	public int getID_Pol()
	{
		return m_ID_Pol;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public String getMonedaSim()
	{
		return m_MonedaSim;
	}

	public float getTC()
	{
		return m_TC;
	}

	public int getCondicion()
	{
		return m_Condicion;
	}

	public String getID_Empleado()
	{
		return m_ID_Empleado;
	}

	public float getImporte()
	{
		return m_Importe;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}

	public float getSubTotal()
	{
		return m_SubTotal;
	}

	public float getISR()
	{
		return m_ISR;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public String getNombre()
	{
		return m_Nombre;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	public String getCalle()
	{
		return m_Calle;
	}

	public String getNoExt()
	{
		return m_NoExt;
	}

	public String getNoInt()
	{
		return m_NoInt;
	}

	public String getColonia()
	{
		return m_Colonia;
	}

	public String getLocalidad()
	{
		return m_Localidad;
	}

	public String getMunicipio()
	{
		return m_Municipio;
	}

	public String getEstado()
	{
		return m_Estado;
	}

	public String getPais()
	{
		return m_Pais;
	}

	public String getCP()
	{
		return m_CP;
	}

	public String getMetodoDePago()
	{
		return m_MetodoDePago;
	}

	public String getCURP()
	{
		return m_CURP;
	}

	public int getTipoRegimen()
	{
		return m_TipoRegimen;
	}

	public String getNumSeguridadSocial()
	{
		return m_NumSeguridadSocial;
	}

	public float getNumDiasPagados()
	{
		return m_NumDiasPagados;
	}

	public String getDepartamento()
	{
		return m_Departamento;
	}

	public String getCLABE()
	{
		return m_CLABE;
	}

	public String getBanco()
	{
		return m_Banco;
	}

	public Date getFechaInicioRelLaboral()
	{
		return m_FechaInicioRelLaboral;
	}

	public String getPuesto()
	{
		return m_Puesto;
	}

	public String getPeriodicidadPago()
	{
		return m_PeriodicidadPago;
	}

	public float getTotalGravado()
	{
		return m_TotalGravado;
	}

	public float getTotalExento()
	{
		return m_TotalExento;
	}

	public float getTotalDeducciones()
	{
		return m_TotalDeducciones;
	}

	public float getTotalDedGravadas()
	{
		return m_TotalDedGravadas;
	}

	public float getTotalDedExentas()
	{
		return m_TotalDedExentas;
	}

	public float getHorasExtras()
	{
		return m_HorasExtras;
	}

	public float getHorasTriples()
	{
		return m_HorasTriples;
	}

	public float getHorasDomingo()
	{
		return m_HorasDomingo;
	}

	public float getIXA()
	{
		return m_IXA;
	}

	public float getIXE()
	{
		return m_IXE;
	}

	public float getIXM()
	{
		return m_IXM;
	}

	public int getDiasHorasExtras()
	{
		return m_DiasHorasExtras;
	}


}

