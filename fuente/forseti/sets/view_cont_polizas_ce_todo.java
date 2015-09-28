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

public class view_cont_polizas_ce_todo
{
	private int m_CL;
	private String m_TL;
	private int m_ID;
	private int m_Part;
	private byte m_Tipo;
	private String m_Num;
	private Date m_Fecha;
	private String m_Concepto;
	private String m_Status;
	private String m_DetCuenta;
	private String m_DetConcepto;
	private String m_DetMoneda;
	private float m_DetParcial;
	private float m_DetTC;
	private float m_DetDebe;
	private float m_DetHaber;
	private String m_ChqNum;
	private String m_ChqBanco;
	private String m_ChqCtaOri;
	private Date m_ChqFecha;
	private float m_ChqMonto;
	private String m_ChqBenef;
	private String m_ChqRFC;
	private String m_TrnCtaOri;
	private String m_TrnBancoOri;
	private float m_TrnMonto;
	private String m_TrnCtaDest;
	private String m_TrnBancoDest;
	private Date m_TrnFecha;
	private String m_TrnBenef;
	private String m_TrnRFC;
	private String m_XmlUUID_CFDI;
	private float m_XmlMonto;
	private String m_XmlRFC;

	public void setCL(int CL)
	{
		m_CL = CL;
	}

	public void setTL(String TL)
	{
		m_TL = TL;
	}

	public void setID(int ID)
	{
		m_ID = ID;
	}

	public void setPart(int Part)
	{
		m_Part = Part;
	}

	public void setTipo(byte Tipo)
	{
		m_Tipo = Tipo;
	}

	public void setNum(String Num)
	{
		m_Num = Num;
	}

	public void setFecha(Date Fecha)
	{
		m_Fecha = Fecha;
	}

	public void setConcepto(String Concepto)
	{
		m_Concepto = Concepto;
	}

	public void setStatus(String Status)
	{
		m_Status = Status;
	}

	public void setDetCuenta(String DetCuenta)
	{
		m_DetCuenta = DetCuenta;
	}

	public void setDetConcepto(String DetConcepto)
	{
		m_DetConcepto = DetConcepto;
	}

	public void setDetMoneda(String DetMoneda)
	{
		m_DetMoneda = DetMoneda;
	}

	public void setDetParcial(float DetParcial)
	{
		m_DetParcial = DetParcial;
	}

	public void setDetTC(float DetTC)
	{
		m_DetTC = DetTC;
	}

	public void setDetDebe(float DetDebe)
	{
		m_DetDebe = DetDebe;
	}

	public void setDetHaber(float DetHaber)
	{
		m_DetHaber = DetHaber;
	}

	public void setChqNum(String ChqNum)
	{
		m_ChqNum = ChqNum;
	}

	public void setChqBanco(String ChqBanco)
	{
		m_ChqBanco = ChqBanco;
	}

	public void setChqCtaOri(String ChqCtaOri)
	{
		m_ChqCtaOri = ChqCtaOri;
	}

	public void setChqFecha(Date ChqFecha)
	{
		m_ChqFecha = ChqFecha;
	}

	public void setChqMonto(float ChqMonto)
	{
		m_ChqMonto = ChqMonto;
	}

	public void setChqBenef(String ChqBenef)
	{
		m_ChqBenef = ChqBenef;
	}

	public void setChqRFC(String ChqRFC)
	{
		m_ChqRFC = ChqRFC;
	}

	public void setTrnCtaOri(String TrnCtaOri)
	{
		m_TrnCtaOri = TrnCtaOri;
	}

	public void setTrnBancoOri(String TrnBancoOri)
	{
		m_TrnBancoOri = TrnBancoOri;
	}

	public void setTrnMonto(float TrnMonto)
	{
		m_TrnMonto = TrnMonto;
	}

	public void setTrnCtaDest(String TrnCtaDest)
	{
		m_TrnCtaDest = TrnCtaDest;
	}

	public void setTrnBancoDest(String TrnBancoDest)
	{
		m_TrnBancoDest = TrnBancoDest;
	}

	public void setTrnFecha(Date TrnFecha)
	{
		m_TrnFecha = TrnFecha;
	}

	public void setTrnBenef(String TrnBenef)
	{
		m_TrnBenef = TrnBenef;
	}

	public void setTrnRFC(String TrnRFC)
	{
		m_TrnRFC = TrnRFC;
	}

	public void setXmlUUID_CFDI(String XmlUUID_CFDI)
	{
		m_XmlUUID_CFDI = XmlUUID_CFDI;
	}

	public void setXmlMonto(float XmlMonto)
	{
		m_XmlMonto = XmlMonto;
	}

	public void setXmlRFC(String XmlRFC)
	{
		m_XmlRFC = XmlRFC;
	}


	public int getCL()
	{
		return m_CL;
	}

	public String getTL()
	{
		return m_TL;
	}

	public int getID()
	{
		return m_ID;
	}

	public int getPart()
	{
		return m_Part;
	}

	public byte getTipo()
	{
		return m_Tipo;
	}

	public String getNum()
	{
		return m_Num;
	}

	public Date getFecha()
	{
		return m_Fecha;
	}

	public String getConcepto()
	{
		return m_Concepto;
	}

	public String getStatus()
	{
		return m_Status;
	}

	public String getDetCuenta()
	{
		return m_DetCuenta;
	}

	public String getDetConcepto()
	{
		return m_DetConcepto;
	}

	public String getDetMoneda()
	{
		return m_DetMoneda;
	}

	public float getDetParcial()
	{
		return m_DetParcial;
	}

	public float getDetTC()
	{
		return m_DetTC;
	}

	public float getDetDebe()
	{
		return m_DetDebe;
	}

	public float getDetHaber()
	{
		return m_DetHaber;
	}

	public String getChqNum()
	{
		return m_ChqNum;
	}

	public String getChqBanco()
	{
		return m_ChqBanco;
	}

	public String getChqCtaOri()
	{
		return m_ChqCtaOri;
	}

	public Date getChqFecha()
	{
		return m_ChqFecha;
	}

	public float getChqMonto()
	{
		return m_ChqMonto;
	}

	public String getChqBenef()
	{
		return m_ChqBenef;
	}

	public String getChqRFC()
	{
		return m_ChqRFC;
	}

	public String getTrnCtaOri()
	{
		return m_TrnCtaOri;
	}

	public String getTrnBancoOri()
	{
		return m_TrnBancoOri;
	}

	public float getTrnMonto()
	{
		return m_TrnMonto;
	}

	public String getTrnCtaDest()
	{
		return m_TrnCtaDest;
	}

	public String getTrnBancoDest()
	{
		return m_TrnBancoDest;
	}

	public Date getTrnFecha()
	{
		return m_TrnFecha;
	}

	public String getTrnBenef()
	{
		return m_TrnBenef;
	}

	public String getTrnRFC()
	{
		return m_TrnRFC;
	}

	public String getXmlUUID_CFDI()
	{
		return m_XmlUUID_CFDI;
	}

	public float getXmlMonto()
	{
		return m_XmlMonto;
	}

	public String getXmlRFC()
	{
		return m_XmlRFC;
	}


}

