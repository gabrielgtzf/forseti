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


public class view_ventas_entidades_ids
{
	private String m_Serie;
	private long m_Doc;
	private String m_Formato;
	private String m_FormatoMOSTR;
	private int m_ID_Entidad;
	private byte m_ID_Tipo;
	private String m_Bodega;
	private int m_ID_Bodega;
	private String m_Descripcion;
	private String m_ID_Usuario;
	private boolean m_DesgloseCLIENT;
	private boolean m_DesgloseMOSTR;
	private float m_IVA;
	private boolean m_AuditarAlm;
	private boolean m_MostrAplicaPolitica;
	private byte m_TipoCobro;
	private boolean m_CambioNumero;
	private int m_DesdeCliente;
	private int m_HastaCliente;
	private int m_Pedido;
	private byte m_AjusteDePrecio;
	private float m_FactorDeAjuste;
	private boolean m_ImprimeSinEm;
	private boolean m_Fija;
	private boolean m_FijaCost;
	private int m_Devolucion;
	private int m_ID_Vendedor;
	private String m_VendedorNombre;
	private int m_Remision;
	private int m_Cotizacion;
	private boolean m_CFD;
	private String m_Fmt_Remision;
	private String m_Fmt_Devolucion;
	private String m_Fmt_Pedido;
	private String m_Fmt_Cotizacion;
	private byte m_ManejoStocks;
	
	public void setFmt_Remision(String Fmt_Remision)
	{
		m_Fmt_Remision = Fmt_Remision;
	}
	
	public void setFmt_Devolucion(String Fmt_Devolucion)
	{
		m_Fmt_Devolucion = Fmt_Devolucion;
	}
	
	public void setFmt_Pedido(String Fmt_Pedido)
	{
		m_Fmt_Pedido = Fmt_Pedido;
	}
	
	public void setFmt_Cotizacion(String Fmt_Cotizacion)
	{
		m_Fmt_Cotizacion = Fmt_Cotizacion;
	}
	
	public int getDevolucion() 
	{
		return m_Devolucion;
	}

	public void setDevolucion(int Devolucion) 
	{
		m_Devolucion = Devolucion;
	}

	public void setSerie(String Serie)
	{
		m_Serie = Serie;
	}

	public void setDoc(long Doc)
	{
		m_Doc = Doc;
	}

	public void setFormato(String Formato)
	{
		m_Formato = Formato;
	}
	
	public void setFormatoMOSTR(String FormatoMOSTR)
	{
		m_FormatoMOSTR = FormatoMOSTR;
	}

	public void setID_Entidad(int ID_Entidad)
	{
		m_ID_Entidad = ID_Entidad;
	}

	public void setID_Tipo(byte ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setBodega(String Bodega)
	{
		m_Bodega = Bodega;
	}

	public void setID_Bodega(int ID_Bodega)
	{
		m_ID_Bodega = ID_Bodega;
	}

	public void setDescripcion(String Descripcion)
	{
		m_Descripcion = Descripcion;
	}

	public void setID_Usuario(String ID_Usuario)
	{
		m_ID_Usuario = ID_Usuario;
	}

	public void setDesgloseCLIENT(boolean DesgloseCLIENT)
	{
		m_DesgloseCLIENT = DesgloseCLIENT;
	}

	public void setDesgloseMOSTR(boolean DesgloseMOSTR)
	{
		m_DesgloseMOSTR = DesgloseMOSTR;
	}

	public void setIVA(float IVA)
	{
		m_IVA = IVA;
	}

	public void setAuditarAlm(boolean AuditarAlm)
	{
		m_AuditarAlm = AuditarAlm;
	}

	public void setMostrAplicaPolitica(boolean MostrAplicaPolitica)
	{
		m_MostrAplicaPolitica = MostrAplicaPolitica;
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

	public void setPedido(int Pedido)
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

	public void setImprimeSinEm(boolean ImprimeSinEm)
	{
		m_ImprimeSinEm = ImprimeSinEm;
	}


	public String getSerie()
	{
		return m_Serie;
	}

	public long getDoc()
	{
		return m_Doc;
	}

	public String getFormato()
	{
		return m_Formato;
	}
	
	public String getFormatoMOSTR()
	{
		return m_FormatoMOSTR;
	}

	public int getID_Entidad()
	{
		return m_ID_Entidad;
	}

	public byte getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public String getBodega()
	{
		return m_Bodega;
	}

	public int getID_Bodega()
	{
		return m_ID_Bodega;
	}

	public String getDescripcion()
	{
		return m_Descripcion;
	}

	public String getID_Usuario()
	{
		return m_ID_Usuario;
	}

	public boolean getDesgloseCLIENT()
	{
		return m_DesgloseCLIENT;
	}

	public boolean getDesgloseMOSTR()
	{
		return m_DesgloseMOSTR;
	}

	public float getIVA()
	{
		return m_IVA;
	}

	public boolean getAuditarAlm()
	{
		return m_AuditarAlm;
	}

	public boolean getMostrAplicaPolitica()
	{
		return m_MostrAplicaPolitica;
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

	public int getPedido()
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

	public boolean getImprimeSinEm()
	{
		return m_ImprimeSinEm;
	}

	public boolean getFija() 
	{
		return m_Fija;
	}

	public void setFija(boolean Fija) 
	{
		m_Fija = Fija;
	}

	public boolean getFijaCost() 
	{
		return m_FijaCost;
	}

	public void setFijaCost(boolean FijaCost) 
	{
		m_FijaCost = FijaCost;
	}

	public int getID_Vendedor() 
	{
		return m_ID_Vendedor;
	}

	public void setID_Vendedor(int ID_Vendedor) 
	{
		m_ID_Vendedor = ID_Vendedor;
	}

	public String getVendedorNombre() 
	{
		return m_VendedorNombre;
	}

	public void setVendedorNombre(String VendedorNombre) 
	{
		m_VendedorNombre = VendedorNombre;
	}

	public void setRemision(int Remision) 
	{
		m_Remision = Remision;
	}
	
	public void setCotizacion(int Cotizacion) 
	{
		m_Cotizacion = Cotizacion;
	}

	public int getCotizacion() 
	{
		return m_Cotizacion;
	}

	public int getRemision() 
	{
		return m_Remision;
	}

	public boolean getCFD()
	{
		return m_CFD;
	}
	
	public void setCFD(boolean CFD) 
	{
		m_CFD = CFD;
	}

	public String getFmt_Remision() 
	{
		return m_Fmt_Remision;
	}
	
	public String getFmt_Devolucion() 
	{
		return m_Fmt_Devolucion;
	}
	
	public String getFmt_Pedido() 
	{
		return m_Fmt_Pedido;
	}
	
	public String getFmt_Cotizacion() 
	{
		return m_Fmt_Cotizacion;
	}

	public byte getManejoStocks() 
	{
		return m_ManejoStocks;
	}
	
	public void setManejoStocks(byte ManejoStocks) 
	{
		m_ManejoStocks = ManejoStocks;
	}
}

