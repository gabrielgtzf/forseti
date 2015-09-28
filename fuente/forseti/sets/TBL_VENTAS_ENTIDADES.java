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


public class TBL_VENTAS_ENTIDADES
{
	private int m_ID_EntidadVenta;
	private short m_ID_TipoEntidad;
	private String m_Serie;
	private long m_Doc;
	private String m_Descripcion;
	private String m_Formato;
	private String m_FormatoMOSTR;
	private int m_ID_Bodega;
	private boolean m_Fija;
	private boolean m_FijaCost;
	private float m_IVA;
	private boolean m_DesgloseMOSTR;
	private boolean m_DesgloseCLIENT;
	private boolean m_MostrAplicaPolitica;
	private boolean m_ImprimeSinEm;
	private byte m_TipoCobro;
	private boolean m_CambioNumero;
	private int m_DesdeCliente;
	private int m_HastaCliente;
	private long m_Pedido;
	private byte m_AjusteDePrecio;
	private float m_FactorDeAjuste;
	private long m_Devolucion;
	private int m_ID_Vendedor;
	private String m_Fmt_Pedido;
	private String m_Fmt_Devolucion;
	private long m_FactNumCIE;
	private long m_DevNumCIE;
	private long m_Remision;
	private long m_Cotizacion;
	private String m_Fmt_Remision;
	private String m_Fmt_Cotizacion;
	private String m_CFD;
	private int m_CFD_NoAprobacion;
	private String m_CFD_NoCertificado;
	private byte m_CFD_ID_Expedicion;
	private int m_CFD_NoAprobacionDev;
	private String m_CFD_NoCertificadoDev;
	private byte m_CFD_ID_ExpedicionDev;
	private int m_CFD_NoAprobacionRem;
	private String m_CFD_NoCertificadoRem;
	private byte m_CFD_ID_ExpedicionRem;
	private String m_ID_Clasificacion;
	private String m_Status;
	private String m_NombreBodega;
	private String m_NombreVendedor;

	public long getRemision() 
	{
		return m_Remision;
	}

	public long getCotizacion() 
	{
		return m_Cotizacion;
	}

	public String getFmt_Remision() 
	{
		return m_Fmt_Remision;
	}

	public String getFmt_Cotizacion() 
	{
		return m_Fmt_Cotizacion;
	}

	public void setRemision(long Remision) 
	{
		m_Remision = Remision;
	}

	public void setCotizacion(long Cotizacion) 
	{
		m_Cotizacion = Cotizacion;
	}

	public void setFmt_Remision(String Fmt_Remision) 
	{
		m_Fmt_Remision = Fmt_Remision;
	}

	public void setFmt_Cotizacion(String Fmt_Cotizacion) 
	{
		m_Fmt_Cotizacion = Fmt_Cotizacion;
	}

	public void setID_EntidadVenta(int ID_EntidadVenta)
	{
		m_ID_EntidadVenta = ID_EntidadVenta;
	}

	public void setID_TipoEntidad(short ID_TipoEntidad)
	{
		m_ID_TipoEntidad = ID_TipoEntidad;
	}

	public void setSerie(String Serie)
	{
		m_Serie = Serie;
	}

	public void setDoc(long Doc)
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

	public void setFormatoMOSTR(String FormatoMOSTR)
	{
		m_FormatoMOSTR = FormatoMOSTR;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setFija(boolean Fija)
	{
		m_Fija = Fija;
	}

	public void setFijaCost(boolean FijaCost)
	{
		m_FijaCost = FijaCost;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setDesgloseMOSTR(boolean DesgloseMOSTR)
	{
		m_DesgloseMOSTR = DesgloseMOSTR;
	}

	public void setDesgloseCLIENT(boolean DesgloseCLIENT)
	{
		m_DesgloseCLIENT = DesgloseCLIENT;
	}

	public void setMostrAplicaPolitica(boolean MostrAplicaPolitica)
	{
		m_MostrAplicaPolitica = MostrAplicaPolitica;
	}

	public void setImprimeSinEm(boolean ImprimeSinEm)
	{
		m_ImprimeSinEm = ImprimeSinEm;
	}

	public void setTipoCobro(byte TipoCobro)
	{
		m_TipoCobro = TipoCobro;
	}

	public void setCambioNumero(boolean CambioNumero)
	{
		m_CambioNumero = CambioNumero;
	}

	public void setDesdeCliente(int DesdeCliente)
	{
		m_DesdeCliente = DesdeCliente;
	}

	public void setHastaCliente(int HastaCliente)
	{
		m_HastaCliente = HastaCliente;
	}

	public void setPedido(long Pedido)
	{
		m_Pedido = Pedido;
	}

	public void setAjusteDePrecio(byte AjusteDePrecio)
	{
		m_AjusteDePrecio = AjusteDePrecio;
	}

	public void setFactorDeAjuste(float FactorDeAjuste)
	{
		m_FactorDeAjuste = FactorDeAjuste;
	}

	public void setDevolucion(long Devolucion)
	{
		m_Devolucion = Devolucion;
	}

	public void setID_Vendedor(int ID_Vendedor)
	{
		m_ID_Vendedor = ID_Vendedor;
	}

	public void setFmt_Pedido(String Fmt_Pedido)
	{
		m_Fmt_Pedido = Fmt_Pedido;
	}

	public void setFmt_Devolucion(String Fmt_Devolucion)
	{
		m_Fmt_Devolucion = Fmt_Devolucion;
	}

	public void setFactNumCIE(long FactNumCIE)
	{
		m_FactNumCIE = FactNumCIE;
	}

	public void setDevNumCIE(long DevNumCIE)
	{
		m_DevNumCIE = DevNumCIE;
	}

	public void setCFD(String CFD)
	{
		m_CFD = CFD;
	}

	public void setCFD_NoAprobacion(int CFD_NoAprobacion)
	{
		m_CFD_NoAprobacion = CFD_NoAprobacion;
	}

	public void setCFD_NoCertificado(String CFD_NoCertificado)
	{
		m_CFD_NoCertificado = CFD_NoCertificado;
	}
	
	public void setCFD_ID_Expedicion(byte CFD_ID_Expedicion)
	{
		m_CFD_ID_Expedicion = CFD_ID_Expedicion;
	}
	
	public void setCFD_NoAprobacionDev(int CFD_NoAprobacionDev)
	{
		m_CFD_NoAprobacionDev = CFD_NoAprobacionDev;
	}

	public void setCFD_NoCertificadoDev(String CFD_NoCertificadoDev)
	{
		m_CFD_NoCertificadoDev = CFD_NoCertificadoDev;
	}
	
	public void setCFD_ID_ExpedicionDev(byte CFD_ID_ExpedicionDev)
	{
		m_CFD_ID_ExpedicionDev = CFD_ID_ExpedicionDev;
	}

	public void setCFD_NoAprobacionRem(int CFD_NoAprobacionRem)
	{
		m_CFD_NoAprobacionRem = CFD_NoAprobacionRem;
	}

	public void setCFD_NoCertificadoRem(String CFD_NoCertificadoRem)
	{
		m_CFD_NoCertificadoRem = CFD_NoCertificadoRem;
	}
	
	public void setCFD_ID_ExpedicionRem(byte CFD_ID_ExpedicionRem)
	{
		m_CFD_ID_ExpedicionRem = CFD_ID_ExpedicionRem;
	}

	
	public int getID_EntidadVenta()
	{
		return m_ID_EntidadVenta;
	}

	public short getID_TipoEntidad()
	{
		return m_ID_TipoEntidad;
	}

	public String getSerie()
	{
		return m_Serie;
	}

	public long getDoc()
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

	public String getFormatoMOSTR()
	{
		return m_FormatoMOSTR;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public boolean getFija()
	{
		return m_Fija;
	}

	public boolean getFijaCost()
	{
		return m_FijaCost;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public boolean getDesgloseMOSTR()
	{
		return m_DesgloseMOSTR;
	}

	public boolean getDesgloseCLIENT()
	{
		return m_DesgloseCLIENT;
	}

	public boolean getMostrAplicaPolitica()
	{
		return m_MostrAplicaPolitica;
	}

	public boolean getImprimeSinEm()
	{
		return m_ImprimeSinEm;
	}

	public byte getTipoCobro()
	{
		return m_TipoCobro;
	}

	public boolean getCambioNumero()
	{
		return m_CambioNumero;
	}

	public int getDesdeCliente()
	{
		return m_DesdeCliente;
	}

	public int getHastaCliente()
	{
		return m_HastaCliente;
	}

	public long getPedido()
	{
		return m_Pedido;
	}

	public byte getAjusteDePrecio()
	{
		return m_AjusteDePrecio;
	}

	public float getFactorDeAjuste()
	{
		return m_FactorDeAjuste;
	}

	public long getDevolucion()
	{
		return m_Devolucion;
	}

	public int getID_Vendedor()
	{
		return m_ID_Vendedor;
	}

	public String getFmt_Pedido()
	{
		return m_Fmt_Pedido;
	}

	public String getFmt_Devolucion()
	{
		return m_Fmt_Devolucion;
	}

	public long getFactNumCIE()
	{
		return m_FactNumCIE;
	}

	public long getDevNumCIE()
	{
		return m_DevNumCIE;
	}

	public String getCFD()
	{
		return m_CFD;
	}

	public int getCFD_NoAprobacion()
	{
		return m_CFD_NoAprobacion;
	}

	public String getCFD_NoCertificado()
	{
		return m_CFD_NoCertificado;
	}
	
	public byte getCFD_ID_Expedicion()
	{
		return m_CFD_ID_Expedicion;
	}

	public int getCFD_NoAprobacionDev()
	{
		return m_CFD_NoAprobacionDev;
	}

	public String getCFD_NoCertificadoDev()
	{
		return m_CFD_NoCertificadoDev;
	}
	
	public byte getCFD_ID_ExpedicionDev()
	{
		return m_CFD_ID_ExpedicionDev;
	}
	
	public int getCFD_NoAprobacionRem()
	{
		return m_CFD_NoAprobacionRem;
	}

	public String getCFD_NoCertificadoRem()
	{
		return m_CFD_NoCertificadoRem;
	}
	
	public byte getCFD_ID_ExpedicionRem()
	{
		return m_CFD_ID_ExpedicionRem;
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

	public void setNombreBodega(String NombreBodega) 
	{
		m_NombreBodega = NombreBodega;
	}
	
	public String getNombreBodega() 
	{
		return m_NombreBodega;
	}

	public void setNombreVendedor(String NombreVendedor) 
	{
		m_NombreVendedor = NombreVendedor;
	}
	
	public String getNombreVendedor() 
	{
		return m_NombreVendedor;
	}
}

