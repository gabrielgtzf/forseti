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


public class TBL_COMPRAS_ENTIDADES
{
	private int m_ID_EntidadCompra;
	private short m_ID_TipoEntidad;
	private String m_Serie;
	private long m_Doc;
	private String m_Descripcion;
	private String m_Formato;
	private int m_ID_Bodega;
	private boolean m_Fija;
	private boolean m_FijaCost;
	private long m_Devolucion;
	private long m_Orden;
	private String m_Fmt_Devolucion;
	private String m_Fmt_Orden;
	private float m_IVA;
	private short m_InfoPlantOC;
	private short m_InfoGasRec;
	private String m_ID_Clasificacion;
	private String m_Status;
	private String m_NombreBodega;
	private long m_Recepcion;
	private String m_Fmt_Recepcion;
	
	public void setID_EntidadCompra(int ID_EntidadCompra)
	{
		m_ID_EntidadCompra = ID_EntidadCompra;
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

	public void setDevolucion(long Devolucion)
	{
		m_Devolucion = Devolucion;
	}

	public void setOrden(long Orden)
	{
		m_Orden = Orden;
	}


	public int getID_EntidadCompra()
	{
		return m_ID_EntidadCompra;
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

	public long getDevolucion()
	{
		return m_Devolucion;
	}

	public long getOrden()
	{
		return m_Orden;
	}

	public String getFmt_Devolucion() 
	{
		return m_Fmt_Devolucion;
	}

	public void setFmt_Devolucion(String Fmt_Devolucion) 
	{
		m_Fmt_Devolucion = Fmt_Devolucion;
	}

	public String getFmt_Orden()
	{
		return m_Fmt_Orden;
	}

	public void setFmt_Orden(String Fmt_Orden) 
	{
		m_Fmt_Orden = Fmt_Orden;
	}

	public void setIVA(float IVA) 
	{
		m_IVA = IVA;
	}

    public float getIVA()
    {
    	return m_IVA;
    }

	public void setInfoPlantOC(short InfoPlantOC) 
	{
		m_InfoPlantOC = InfoPlantOC;
	}
	
	public void setInfoGasRec(short InfoGasRec) 
	{
		m_InfoGasRec = InfoGasRec;
	}
	
	public short getInfoPlantOC()
	{
		return m_InfoPlantOC;
	}
	
	public short getInfoGasRec()
	{
		return m_InfoGasRec;
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

	public void setRecepcion(long Recepcion) 
	{
		m_Recepcion = Recepcion;
	}

	public void setFmt_Recepcion(String Fmt_Recepcion) 
	{
		m_Fmt_Recepcion = Fmt_Recepcion;
	}
	
	public long getRecepcion() 
	{
		return m_Recepcion;
	}

	public String getFmt_Recepcion() 
	{
		return m_Fmt_Recepcion;
	}
}

