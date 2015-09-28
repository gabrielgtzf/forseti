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
package forseti.caja_y_bancos;
import forseti.JUtil;

public class JMovBancariosSesPart
{
  private String m_Cuenta;
  private String m_Nombre;
  private String m_Concepto;
  private byte m_ID_Moneda;
  private String m_Moneda;
  private double m_TC;
  private double m_Parcial;
  private double m_Debe;
  private double m_Haber;

  public JMovBancariosSesPart()
  {
  }

  public JMovBancariosSesPart(String Cuenta,String Nombre, String Concepto, byte ID_Moneda, String Moneda,
                              double TC, double Debe, double Haber )
  {
    m_Cuenta = Cuenta;
    m_Nombre = Nombre;
    m_Concepto = Concepto;
    m_ID_Moneda = ID_Moneda;
    m_Moneda = Moneda;
    m_TC = TC;
    m_Parcial = (Debe != 0.0F) ? Debe : Haber;
    m_Debe = (Debe != 0.0F) ? JUtil.redondear((m_Parcial * m_TC), 2) : 0.0F;
    m_Haber = (Haber != 0.0F) ? JUtil.redondear((m_Parcial * m_TC), 2) : 0.0F;
  }

  public JMovBancariosSesPart(String Cuenta, String Nombre, String Concepto,
                            double Parcial, byte ID_Moneda, double TC, double Debe, double Haber )
  {
    m_Cuenta = Cuenta;
    m_Nombre = Nombre;
    m_Concepto = Concepto;
    m_ID_Moneda = ID_Moneda;
    m_Moneda = "";
    m_TC = TC;
    m_Parcial = Parcial;
    m_Debe = Debe;
    m_Haber = Haber;
  }

  public void setPartida(String Cuenta,String Nombre, String Concepto, byte ID_Moneda, String Moneda,
                              double TC, double Debe, double Haber )
  {
    m_Cuenta = Cuenta;
    m_Nombre = Nombre;
    m_Concepto = Concepto;
    m_ID_Moneda = ID_Moneda;
    m_Moneda = Moneda;
    m_TC = TC;
    m_Parcial = (Debe != 0.0F) ? Debe : Haber;
    m_Debe = (Debe != 0.0F) ? JUtil.redondear((m_Parcial * m_TC), 2) : 0.0F;
    m_Haber = (Haber != 0.0F) ? JUtil.redondear((m_Parcial * m_TC), 2) : 0.0F;
  }

  public String getCuenta()
  {
    return m_Cuenta;
  }

  public String getNombre()
  {
    return m_Nombre;
  }

  public String getConcepto()
  {
    return m_Concepto;
  }

  public byte getID_Moneda()
  {
    return m_ID_Moneda;
  }

  public String getMoneda()
  {
    return m_Moneda;
  }

  public double getTC()
  {
    return m_TC;
  }

  public double getParcial()
  {
    return m_Parcial;
  }

  public double getDebe()
  {
    return m_Debe;
  }

  public double getHaber()
  {
    return m_Haber;
  }

}
