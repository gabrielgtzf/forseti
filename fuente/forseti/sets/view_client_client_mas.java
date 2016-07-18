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

public class view_client_client_mas
{
	private String m_ID_Tipo;
	private int m_ID_Clave;
	private int m_ID_EntidadVenta;
	private String m_EntidadNombre;
	private String m_RFC;
	private String m_AtnPagos;
	private String m_Colonia;
	private String m_CP;
	private String m_Direccion;
	private String m_Fax;
	private String m_Poblacion;
	private float m_CompraAnual;
	private float m_Descuento;
	private float m_LimiteCredito;
	private Date m_UltimaCompra;
	private String m_Obs;
	private boolean m_PrecioEspMostr;
	private String m_CuentaNombre;
	private int m_ID_Vendedor;
	private String m_VendedorNombre;
	private String m_NoExt;
	private String m_NoInt;
	private String m_Municipio;
	private String m_Estado;
	private String m_Pais;
	private String m_MetodoDePago;
	private String m_Status;
	private String m_ID_SatBanco;
	private String m_Pedimento;
	
	public String getNoExt() 
	{
		return m_NoExt;
	}

	public String getNoInt() 
	{
		return m_NoInt;
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

	public void setNoExt(String NoExt) 
	{
		m_NoExt = NoExt;
	}

	public void setNoInt(String NoInt) 
	{
		m_NoInt = NoInt;
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

	public void setID_Tipo(String ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Clave(int ID_Clave)
	{
		m_ID_Clave = ID_Clave;
	}

	public void setID_EntidadVenta(int ID_EntidadVenta)
	{
		m_ID_EntidadVenta = ID_EntidadVenta;
	}

	public void setEntidadNombre(String EntidadNombre)
	{
		m_EntidadNombre = EntidadNombre;
	}

	public void setRFC(String RFC)
	{
		m_RFC = RFC;
	}

	public void setAtnPagos(String AtnPagos)
	{
		m_AtnPagos = AtnPagos;
	}

	public void setColonia(String Colonia)
	{
		m_Colonia = Colonia;
	}

	public void setCP(String CP)
	{
		m_CP = CP;
	}

	public void setDireccion(String Direccion)
	{
		m_Direccion = Direccion;
	}

	public void setFax(String Fax)
	{
		m_Fax = Fax;
	}

	public void setPoblacion(String Poblacion)
	{
		m_Poblacion = Poblacion;
	}

	public void setCompraAnual(float CompraAnual)
	{
		m_CompraAnual = CompraAnual;
	}

	public void setDescuento(float Descuento)
	{
		m_Descuento = Descuento;
	}

	public void setLimiteCredito(float LimiteCredito)
	{
		m_LimiteCredito = LimiteCredito;
	}

	public void setUltimaCompra(Date UltimaCompra)
	{
		m_UltimaCompra = UltimaCompra;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setPrecioEspMostr(boolean PrecioEspMostr)
	{
		m_PrecioEspMostr = PrecioEspMostr;
	}

	public void setCuentaNombre(String CuentaNombre)
	{
		m_CuentaNombre = CuentaNombre;
	}


	public String getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public int getID_Clave()
	{
		return m_ID_Clave;
	}

	public int getID_EntidadVenta()
	{
		return m_ID_EntidadVenta;
	}

	public String getEntidadNombre()
	{
		return m_EntidadNombre;
	}

	public String getRFC()
	{
		return m_RFC;
	}

	public String getAtnPagos()
	{
		return m_AtnPagos;
	}

	public String getColonia()
	{
		return m_Colonia;
	}

	public String getCP()
	{
		return m_CP;
	}

	public String getDireccion()
	{
		return m_Direccion;
	}

	public String getFax()
	{
		return m_Fax;
	}

	public String getPoblacion()
	{
		return m_Poblacion;
	}

	public float getCompraAnual()
	{
		return m_CompraAnual;
	}

	public float getDescuento()
	{
		return m_Descuento;
	}

	public float getLimiteCredito()
	{
		return m_LimiteCredito;
	}

	public Date getUltimaCompra()
	{
		return m_UltimaCompra;
	}

	public String getObs()
	{
		return m_Obs;
	}

	public boolean getPrecioEspMostr()
	{
		return m_PrecioEspMostr;
	}

	public String getCuentaNombre()
	{
		return m_CuentaNombre;
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

	public void setMetodoDePago(String MetodoDePago) 
	{
		m_MetodoDePago = MetodoDePago;
	}

	public String getMetodoDePago()
	{
		return m_MetodoDePago;
	}
	
	public void setStatus(String Status) 
	{
		m_Status = Status;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public void setID_SatBanco(String ID_SatBanco)
	{
		m_ID_SatBanco = ID_SatBanco;		
	}
	
	public String getID_SatBanco()
	{
		return m_ID_SatBanco;		
	}

	public void setPedimento(String Pedimento) 
	{
		m_Pedimento = Pedimento;
	}
	
	public String getPedimento() 
	{
		return m_Pedimento;
	}
}

