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

public class view_cfd_ventas_facturas_cab_generar
{
	private byte m_ID_EntidadVenta;
	private String m_Descripcion;
	private boolean m_Fija;
	private float m_IVA_Entidad;
	private boolean m_CFD;
	private String m_CFD_Serie;
	private int m_CFD_Folio;
	private int m_CFD_FolioINI;
	private int m_CFD_FolioFIN;
	private int m_CFD_NoAprobacion;
	private int m_CFD_AnoAprobacion;
	private String m_CFD_NoCertificado;
	private String m_CFD_ArchivoCertificado;
	private Date m_CFD_CaducidadCertificado;
	private String m_CFD_ArchivoLLave;
	private String m_CFD_ClaveLLave;
	private byte m_CFD_ID_Expedicion;
	private String m_CFD_Calle;
	private String m_CFD_NoExt;
	private String m_CFD_NoInt;
	private String m_CFD_Colonia;
	private String m_CFD_Localidad;
	private String m_CFD_Municipio;
	private String m_CFD_Estado;
	private String m_CFD_Pais;
	private String m_CFD_CP;
	private int m_ID_Factura;
	private int m_Numero;
	private int m_ID_Cliente;
	private Date m_Fecha;
	private String m_Referencia;
	private String m_Status;
	private byte m_Moneda;
	private String m_MonedaSim;
	private float m_TC;
	private byte m_Condicion;
	private String m_Obs;
	private float m_Importe;
	private float m_Descuento;
	private float m_SubTotal;
	private float m_IVA;
	private float m_IEPS;
	private float m_IVARet;
	private float m_ISRRet;
	private float m_Total;
	private String m_Ref;
	private String m_ID_Pol;
	private String m_ID_PolCost;
	private byte m_ID_Bodega;
	private float m_Efectivo;
	private float m_Bancos;
	private float m_Cambio;
	private int m_ID_Vendedor;
	private String m_Nombre;
	private String m_RFC;
	private int m_DiasCredito;
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
	private String m_MonedaSat;

	public void setID_EntidadVenta(byte ID_EntidadVenta)
	{
		m_ID_EntidadVenta = ID_EntidadVenta;
	}
	
	public void setMonedaSim(String MonedaSim)
	{
		m_MonedaSim = MonedaSim;
	}
	
	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setFija(boolean Fija)
	{
		m_Fija = Fija;
	}

	public void setIVA_Entidad(float IVA_Entidad)
	{
		m_IVA_Entidad = IVA_Entidad;
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

	public void setCFD_FolioINI(int CFD_FolioINI)
	{
		m_CFD_FolioINI = CFD_FolioINI;
	}

	public void setCFD_FolioFIN(int CFD_FolioFIN)
	{
		m_CFD_FolioFIN = CFD_FolioFIN;
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

	public void setCFD_ArchivoLLave(String CFD_ArchivoLLave)
	{
		m_CFD_ArchivoLLave = CFD_ArchivoLLave;
	}

	public void setCFD_ClaveLLave(String CFD_ClaveLLave)
	{
		m_CFD_ClaveLLave = CFD_ClaveLLave;
	}

	public void setCFD_ID_Expedicion(byte CFD_ID_Expedicion)
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

	public void setID_Factura(int ID_Factura)
	{
		m_ID_Factura = ID_Factura;
	}

	public void setNumero(int Numero)
	{
		m_Numero = Numero;
	}

	public void setID_Cliente(int ID_Cliente)
	{
		m_ID_Cliente = ID_Cliente;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setReferencia(String Referencia)
	{
		m_Referencia = Referencia;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setMoneda(byte Moneda)
	{
		m_Moneda = Moneda;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setCondicion(byte Condicion)
	{
		m_Condicion = Condicion;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
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

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setRef(String Ref)
	{
		m_Ref = Ref;
	}

	public void setID_Pol(String ID_Pol)
	{
		m_ID_Pol = ID_Pol;
	}

	public void setID_PolCost(String ID_PolCost)
	{
		m_ID_PolCost = ID_PolCost;
	}

	public void setID_Bodega(byte ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setEfectivo(float Efectivo)
	{
		m_Efectivo = Efectivo;
	}

	public void setBancos(float Bancos)
	{
		m_Bancos = Bancos;
	}

	public void setCambio(float Cambio)
	{
		m_Cambio = Cambio;
	}

	public void setID_Vendedor(int ID_Vendedor)
	{
		m_ID_Vendedor = ID_Vendedor;
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


	public byte getID_EntidadVenta()
	{
		return m_ID_EntidadVenta;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public boolean getFija()
	{
		return m_Fija;
	}

	public float getIVA_Entidad()
	{
		return m_IVA_Entidad;
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

	public int getCFD_FolioINI()
	{
		return m_CFD_FolioINI;
	}

	public int getCFD_FolioFIN()
	{
		return m_CFD_FolioFIN;
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

	public String getCFD_ArchivoLLave()
	{
		return m_CFD_ArchivoLLave;
	}

	public String getCFD_ClaveLLave()
	{
		return m_CFD_ClaveLLave;
	}

	public byte getCFD_ID_Expedicion()
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

	public int getID_Factura()
	{
		return m_ID_Factura;
	}

	public int getNumero()
	{
		return m_Numero;
	}

	public int getID_Cliente()
	{
		return m_ID_Cliente;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getReferencia()
	{
		return m_Referencia;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public byte getMoneda()
	{
		return m_Moneda;
	}

	public float getTC()
	{
		return m_TC;
	}

	public byte getCondicion()
	{
		return m_Condicion;
	}

	public String getObs()
	{
		return m_Obs;
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

	public float getIVA()
	{
		return m_IVA;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public String getRef()
	{
		return m_Ref;
	}

	public String getID_Pol()
	{
		return m_ID_Pol;
	}

	public String getID_PolCost()
	{
		return m_ID_PolCost;
	}

	public byte getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public float getEfectivo()
	{
		return m_Efectivo;
	}

	public float getBancos()
	{
		return m_Bancos;
	}

	public float getCambio()
	{
		return m_Cambio;
	}

	public int getID_Vendedor()
	{
		return m_ID_Vendedor;
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

	public void setDiasCredito(int DiasCredito) 
	{
		m_DiasCredito = DiasCredito;
	}

	public int getDiasCredito()
	{
		return m_DiasCredito;
	}

	public String getMonedaSim() 
	{
		return m_MonedaSim;
	}

	public void setCFD_CaducidadCertificado(Date CFD_CaducidadCertificado) 
	{
		m_CFD_CaducidadCertificado = CFD_CaducidadCertificado;
	}
	
	public Date getCFD_CaducidadCertificado() 
	{
		return m_CFD_CaducidadCertificado;
	}

	public void setMetodoDePago(String MetodoDePago) 
	{
		m_MetodoDePago = MetodoDePago;
	}

	public String getMetodoDePago() 
	{
		return m_MetodoDePago;
	}

	public float getIEPS() 
	{
		return m_IEPS;
	}
	
	public float getIVARet() 
	{
		return m_IVARet;
	}
	 
	public float getISRRet() 
	{
		return m_ISRRet;
	}

	public void setIEPS(float IEPS) 
	{
		m_IEPS = IEPS;
	}
	
	public void setIVARet(float IVARet) 
	{
		m_IVARet = IVARet;
	}
	 
	public void setISRRet(float ISRRet) 
	{
		m_ISRRet = ISRRet;
	}

	public void setMonedaSat(String MonedaSat) 
	{
		m_MonedaSat = MonedaSat;
	}

	public String getMonedaSat() 
	{
		return m_MonedaSat;
	}
}

