package forseti.sets;


public class view_cajas_vales_acum
{
	private int m_ID_Tipo;
	private int m_ID_Clave;
	private double m_Provisionales;
	private double m_SinFactura;
	private double m_Facturas;
	private double m_Pagos;
	private double m_Otros;
	private double m_Traspasar;

	public void setID_Tipo(int ID_Tipo)
	{
		m_ID_Tipo = ID_Tipo;
	}

	public void setID_Clave(int ID_Clave)
	{
		m_ID_Clave = ID_Clave;
	}

	public void setProvisionales(double Provisionales)
	{
		m_Provisionales = Provisionales;
	}

	public void setSinFactura(double SinFactura)
	{
		m_SinFactura = SinFactura;
	}

	public void setFacturas(double Facturas)
	{
		m_Facturas = Facturas;
	}

	public void setPagos(double Pagos)
	{
		m_Pagos = Pagos;
	}

	public void setOtros(double Otros)
	{
		m_Otros = Otros;
	}

	public void setTraspasar(double Traspasar)
	{
		m_Traspasar = Traspasar;
	}


	public int getID_Tipo()
	{
		return m_ID_Tipo;
	}

	public int getID_Clave()
	{
		return m_ID_Clave;
	}

	public double getProvisionales()
	{
		return m_Provisionales;
	}

	public double getSinFactura()
	{
		return m_SinFactura;
	}

	public double getFacturas()
	{
		return m_Facturas;
	}

	public double getPagos()
	{
		return m_Pagos;
	}

	public double getOtros()
	{
		return m_Otros;
	}

	public double getTraspasar()
	{
		return m_Traspasar;
	}


}

