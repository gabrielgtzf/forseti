package forseti.sets;

import java.sql.Date;

public class view_cfdcompotr
{
	private int m_ID_CFD;
	private String m_FSI_Tipo;
	private int m_FSI_ID;
	private String m_Tipo;
	private String m_Factura;
	private String m_CFD_CBB_Serie;
	private int m_CFD_CBB_NumFol;
	private String m_NumFactExt;
	private String m_UUID;
	private String m_Ext;
	private String m_Nombre_Original;
	private float m_Total;
	private int m_ID_Moneda;
	private float m_TC;
	private Date m_Fecha;
	private String m_Enlace;

	public void setID_CFD(int ID_CFD)
	{
		m_ID_CFD = ID_CFD;
	}

	public void setFSI_Tipo(String FSI_Tipo)
	{
		m_FSI_Tipo = FSI_Tipo;
	}

	public void setFSI_ID(int FSI_ID)
	{
		m_FSI_ID = FSI_ID;
	}

	public void setTipo(String Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setFactura(String Factura)
	{
		m_Factura = Factura;
	}

	public void setCFD_CBB_Serie(String CFD_CBB_Serie)
	{
		m_CFD_CBB_Serie = CFD_CBB_Serie;
	}

	public void setCFD_CBB_NumFol(int CFD_CBB_NumFol)
	{
		m_CFD_CBB_NumFol = CFD_CBB_NumFol;
	}

	public void setNumFactExt(String NumFactExt)
	{
		m_NumFactExt = NumFactExt;
	}

	public void setUUID(String UUID)
	{
		m_UUID = UUID;
	}

	public void setExt(String Ext)
	{
		m_Ext = Ext;
	}

	public void setNombre_Original(String Nombre_Original)
	{
		m_Nombre_Original = Nombre_Original;
	}

	public void setTotal(float Total)
	{
		m_Total = Total;
	}

	public void setID_Moneda(int ID_Moneda)
	{
		m_ID_Moneda = ID_Moneda;
	}

	public void setTC(float TC)
	{
		m_TC = TC;
	}

	public void setEnlace(String Enlace)
	{
		m_Enlace = Enlace;
	}
	
	public int getID_CFD()
	{
		return m_ID_CFD;
	}

	public String getFSI_Tipo()
	{
		return m_FSI_Tipo;
	}

	public int getFSI_ID()
	{
		return m_FSI_ID;
	}

	public String getTipo()
	{
		return m_Tipo;
	}

	public String getFactura()
	{
		return m_Factura;
	}

	public String getCFD_CBB_Serie()
	{
		return m_CFD_CBB_Serie;
	}

	public int getCFD_CBB_NumFol()
	{
		return m_CFD_CBB_NumFol;
	}

	public String getNumFactExt()
	{
		return m_NumFactExt;
	}

	public String getUUID()
	{
		return m_UUID;
	}

	public String getExt()
	{
		return m_Ext;
	}

	public String getNombre_Original()
	{
		return m_Nombre_Original;
	}

	public float getTotal()
	{
		return m_Total;
	}

	public int getID_Moneda()
	{
		return m_ID_Moneda;
	}

	public float getTC()
	{
		return m_TC;
	}

	public void setFecha(Date Fecha) 
	{
		m_Fecha = Fecha;
	}

	public Date getFecha() 
	{
		return m_Fecha;
	}

	public String getEnlace() 
	{
		return m_Enlace;
	}
	
}

