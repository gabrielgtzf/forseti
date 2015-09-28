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


public class view_invserv_inventarios_mas
{
	private String m_Clave;
	private float m_Precio;
	private float m_StockMin;
	private float m_StockMax;
	private boolean m_SeProduce;
	private String m_Obs;
	private String m_ID_UnidadSalida;
	private float m_Factor;
	private float m_Empaque;
	private float m_PorSurtir;
	private float m_PorRecibir;
	private float m_Apartado;
	private int m_Dias;
	private float m_UltimoCosto;
	private float m_CostoPromedio;
	private float m_ImpIEPS;
	private boolean m_IVA;
	private byte m_TipoCosteo;
	private float m_CantidadAcum;
	private float m_MontoAcum;
	private float m_Precio2;
	private float m_Precio3;
	private float m_Precio4;
	private float m_Precio5;
	private float m_PrecioOfertaWeb;
	private float m_PrecioWeb;
	private boolean m_NoSeVende;
	private String m_Status;
	private String m_DescripcionWeb;
	private String m_ComentariosWeb;
	private String m_DescripcionWebING;
	private String m_ComentariosWebING;
	private String m_Ref_FotoWeb;
	private String m_Ref_FotoChicaWeb;
	private boolean m_NuevoWeb;
	private int m_NumRecomWeb;
	private int m_NumExperienciasWeb;
	private float m_KgsWeb;
	private String m_LineaDescripcion;
	private String m_CuentaNombre;
	private String m_Codigo;
	private float m_PrecioMin;
	private float m_PrecioMax;
	private float m_IVA_Deducible;
	private float m_ImpIVARet;
	private float m_ImpISRRet;
	
	public float getPrecioMax() 
	{
		return m_PrecioMax;
	}

	public void setPrecioMax(float PrecioMax) 
	{
		m_PrecioMax = PrecioMax;
	}

	public float getPrecioMin() 
	{
		return m_PrecioMin;
	}

	public void setPrecioMin(float PrecioMin) 
	{
		m_PrecioMin = PrecioMin;
	}

	public String getCodigo() 
	{
		return m_Codigo;
	}

	public void setCodigo(String Codigo) 
	{
		m_Codigo = Codigo;
	}

	public void setClave(String Clave)
	{
		m_Clave = Clave;
	}

	public void setPrecio(float Precio)
	{
		m_Precio = Precio;
	}

	public void setStockMin(float StockMin)
	{
		m_StockMin = StockMin;
	}

	public void setStockMax(float StockMax)
	{
		m_StockMax = StockMax;
	}

	public void setSeProduce(boolean SeProduce)
	{
		m_SeProduce = SeProduce;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}

	public void setID_UnidadSalida(String ID_UnidadSalida)
	{
		m_ID_UnidadSalida = ID_UnidadSalida;
	}

	public void setFactor(float Factor)
	{
		m_Factor = Factor;
	}

	public void setEmpaque(float Empaque)
	{
		m_Empaque = Empaque;
	}

	public void setPorSurtir(float PorSurtir)
	{
		m_PorSurtir = PorSurtir;
	}

	public void setPorRecibir(float PorRecibir)
	{
		m_PorRecibir = PorRecibir;
	}

	public void setApartado(float Apartado)
	{
		m_Apartado = Apartado;
	}

	public void setDias(int Dias)
	{
		m_Dias = Dias;
	}

	public void setUltimoCosto(float UltimoCosto)
	{
		m_UltimoCosto = UltimoCosto;
	}

	public void setCostoPromedio(float CostoPromedio)
	{
		m_CostoPromedio = CostoPromedio;
	}

	public void setImpIEPS(float ImpIEPS)
	{
		m_ImpIEPS = ImpIEPS;
	}

	public void setIVA(boolean IVA)
	{
		m_IVA = IVA;
	}

	public void setTipoCosteo(byte TipoCosteo)
	{
		m_TipoCosteo = TipoCosteo;
	}

	public void setCantidadAcum(float CantidadAcum)
	{
		m_CantidadAcum = CantidadAcum;
	}

	public void setMontoAcum(float MontoAcum)
	{
		m_MontoAcum = MontoAcum;
	}

	public void setPrecio2(float Precio2)
	{
		m_Precio2 = Precio2;
	}

	public void setPrecio3(float Precio3)
	{
		m_Precio3 = Precio3;
	}

	public void setPrecio4(float Precio4)
	{
		m_Precio4 = Precio4;
	}

	public void setPrecio5(float Precio5)
	{
		m_Precio5 = Precio5;
	}

	public void setPrecioOfertaWeb(float PrecioOfertaWeb)
	{
		m_PrecioOfertaWeb = PrecioOfertaWeb;
	}

	public void setPrecioWeb(float PrecioWeb)
	{
		m_PrecioWeb = PrecioWeb;
	}

	public void setNoSeVende(boolean NoSeVende)
	{
		m_NoSeVende = NoSeVende;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setDescripcionWeb(String DescripcionWeb)
	{
		m_DescripcionWeb = DescripcionWeb;
	}

	public void setComentariosWeb(String ComentariosWeb)
	{
		m_ComentariosWeb = ComentariosWeb;
	}

	public void setDescripcionWebING(String DescripcionWebING)
	{
		m_DescripcionWebING = DescripcionWebING;
	}

	public void setComentariosWebING(String ComentariosWebING)
	{
		m_ComentariosWebING = ComentariosWebING;
	}

	public void setRef_FotoWeb(String Ref_FotoWeb)
	{
		m_Ref_FotoWeb = Ref_FotoWeb;
	}

	public void setRef_FotoChicaWeb(String Ref_FotoChicaWeb)
	{
		m_Ref_FotoChicaWeb = Ref_FotoChicaWeb;
	}

	public void setNuevoWeb(boolean NuevoWeb)
	{
		m_NuevoWeb = NuevoWeb;
	}

	public void setNumRecomWeb(int NumRecomWeb)
	{
		m_NumRecomWeb = NumRecomWeb;
	}

	public void setNumExperienciasWeb(int NumExperienciasWeb)
	{
		m_NumExperienciasWeb = NumExperienciasWeb;
	}

	public void setKgsWeb(float KgsWeb)
	{
		m_KgsWeb = KgsWeb;
	}

	public void setLineaDescripcion(String LineaDescripcion)
	{
		m_LineaDescripcion = LineaDescripcion;
	}

	public void setCuentaNombre(String CuentaNombre)
	{
		m_CuentaNombre = CuentaNombre;
	}


	public String getClave()
	{
		return m_Clave;
	}

	public float getPrecio()
	{
		return m_Precio;
	}

	public float getStockMin()
	{
		return m_StockMin;
	}

	public float getStockMax()
	{
		return m_StockMax;
	}

	public boolean getSeProduce()
	{
		return m_SeProduce;
	}

	public String getObs()
	{
		return m_Obs;
	}

	public String getID_UnidadSalida()
	{
		return m_ID_UnidadSalida;
	}

	public float getFactor()
	{
		return m_Factor;
	}

	public float getEmpaque()
	{
		return m_Empaque;
	}

	public float getPorSurtir()
	{
		return m_PorSurtir;
	}

	public float getPorRecibir()
	{
		return m_PorRecibir;
	}

	public float getApartado()
	{
		return m_Apartado;
	}

	public int getDias()
	{
		return m_Dias;
	}

	public float getUltimoCosto()
	{
		return m_UltimoCosto;
	}

	public float getCostoPromedio()
	{
		return m_CostoPromedio;
	}

	public float getImpIEPS()
	{
		return m_ImpIEPS;
	}

	public boolean getIVA()
	{
		return m_IVA;
	}

	public byte getTipoCosteo()
	{
		return m_TipoCosteo;
	}

	public float getCantidadAcum()
	{
		return m_CantidadAcum;
	}

	public float getMontoAcum()
	{
		return m_MontoAcum;
	}

	public float getPrecio2()
	{
		return m_Precio2;
	}

	public float getPrecio3()
	{
		return m_Precio3;
	}

	public float getPrecio4()
	{
		return m_Precio4;
	}

	public float getPrecio5()
	{
		return m_Precio5;
	}

	public float getPrecioOfertaWeb()
	{
		return m_PrecioOfertaWeb;
	}

	public float getPrecioWeb()
	{
		return m_PrecioWeb;
	}

	public boolean getNoSeVende()
	{
		return m_NoSeVende;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getDescripcionWeb()
	{
		return m_DescripcionWeb;
	}

	public String getComentariosWeb()
	{
		return m_ComentariosWeb;
	}

	public String getDescripcionWebING()
	{
		return m_DescripcionWebING;
	}

	public String getComentariosWebING()
	{
		return m_ComentariosWebING;
	}

	public String getRef_FotoWeb()
	{
		return m_Ref_FotoWeb;
	}

	public String getRef_FotoChicaWeb()
	{
		return m_Ref_FotoChicaWeb;
	}

	public boolean getNuevoWeb()
	{
		return m_NuevoWeb;
	}

	public int getNumRecomWeb()
	{
		return m_NumRecomWeb;
	}

	public int getNumExperienciasWeb()
	{
		return m_NumExperienciasWeb;
	}

	public float getKgsWeb()
	{
		return m_KgsWeb;
	}

	public String getLineaDescripcion()
	{
		return m_LineaDescripcion;
	}

	public String getCuentaNombre()
	{
		return m_CuentaNombre;
	}

	public void setIVA_Deducible(float IVA_Deducible) 
	{
		m_IVA_Deducible = IVA_Deducible;
	}

	public float getIVA_Deducible() 
	{
		return m_IVA_Deducible;
	}

	public void setImpIVARet(float ImpIVARet) 
	{
		m_ImpIVARet = ImpIVARet;
	}

	public void setImpISRRet(float ImpISRRet) 
	{
		m_ImpISRRet = ImpISRRet;
	}
	
	public float getImpIVARet() 
	{
		return m_ImpIVARet;
	}

	public float getImpISRRet() 
	{
		return m_ImpISRRet;
	}
}

