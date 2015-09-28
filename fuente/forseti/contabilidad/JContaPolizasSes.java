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
package forseti.contabilidad;

import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JPublicContCatalogSetV2;
import forseti.sets.JPublicContMonedasSetV2;

public class JContaPolizasSes extends JSesionRegs
{

  private byte m_Periodo;
  private String m_Status;
  private String m_Ref;

  private double m_SumDebe;
  private double m_SumHaber;

  public JContaPolizasSes()
  {
    m_Periodo = -1;
    m_Status = "";
    m_Ref = "";
    m_SumDebe = 0;
    m_SumHaber = 0;

  }

  public void setParametros(String Status, String Ref, byte Periodo)
  {
    m_Periodo = Periodo;
    m_Status = Status;
    m_Ref = Ref;
  }

  public double getSumDebe()
  {
    return m_SumDebe;
  }

  public double getSumHaber()
  {
    return m_SumHaber;
  }

  public byte getPeriodo()
  {
    return m_Periodo;
  }

  public String getStatus()
  {
    return m_Status;
  }

  public String getRef()
  {
    return m_Ref;
  }

  public JContaPolizasSesPart getPartida(int ind)
  {
    return (JContaPolizasSesPart)m_Partidas.elementAt(ind);
  }

   
@SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, String cuenta, String concepto,
                            byte idmoneda, double tc, double debe,
                            double haber, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if(set.getNumRows() > 0)
    {
      if(set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",1)); // "PRECAUCION: Al agregar, la cuenta especificada se encontró en el catálogo, pero no se puede insertar porque es una cuenta acumulativa");
      }
      else if( (debe != 0 && haber != 0) || (debe == 0 && haber == 0))
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",2)); //"PRECAUCION: Al agregar, si es un abono el cargo debe de ser 0 y viceversa. No se puede agregar la partida");
      }
      else
      {
        JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
        mon.m_Where = "Clave = '" + idmoneda + "'";
        mon.Open();
        String moneda = mon.getAbsRow(0).getMoneda();
        String nombre = set.getAbsRow(0).getNombre();
        JContaPolizasSesPart part = new JContaPolizasSesPart(cuenta, nombre, concepto, idmoneda, moneda, ((idmoneda == 1) ? 1.0F : tc), debe, haber);
        m_Partidas.addElement(part);
        establecerResultados();
      }
    }
    else
    {
        res = 3;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",3)); //"ERROR: No se encontró la cuenta especificada");
    }

    return res;

  }

   
  @SuppressWarnings("unchecked")
  public void agregaPartida(String cuenta, String nombre, String concepto,
                            double parcial, byte idmoneda, double tc, double debe, double haber)
  {
    JContaPolizasSesPart part = new JContaPolizasSesPart(cuenta, nombre, concepto, parcial, idmoneda, tc, debe, haber);
    m_Partidas.addElement(part);
    establecerResultados();
  }

  public short editaPartida(int indPartida, HttpServletRequest request, String cuenta, String concepto,
                          byte idmoneda, double tc, double debe,
                          double haber, StringBuffer mensaje)
  {
    short res = -1;

    JPublicContCatalogSetV2 set = new JPublicContCatalogSetV2(request);
    set.m_Where = "Numero = '" + JUtil.p(cuenta) + "'";
    set.Open();
    if (set.getNumRows() > 0)
    {
      if (set.getAbsRow(0).getAcum() == true)
      {
        res = 1;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",1)); //"PRECAUCION: Al editar, la cuenta especificada se encontr� en el cat�logo, pero no se puede editar porque es una cuenta acumulativa");
      }
      else if ( (debe != 0 && haber != 0) || (debe == 0 && haber == 0))
      {
        res = 3;
        mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",2)); //"PRECAUCION: Al editar, si es un abono el cargo debe de ser 0 y viceversa. No se puede editar la partida");
      }
      else
      {
        JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
        mon.m_Where = "Clave = '" + idmoneda + "'";
        mon.Open();
        String moneda = mon.getAbsRow(0).getMoneda();
        String nombre = set.getAbsRow(0).getNombre();

        JContaPolizasSesPart part = (JContaPolizasSesPart) m_Partidas.elementAt(indPartida);
        part.setPartida(cuenta, nombre, concepto, idmoneda, moneda, ((idmoneda == 1) ? 1.0F : tc), debe,haber);
        establecerResultados();
      }
    }
    else
    {
      res = 3;
      mensaje.append(JUtil.Msj("CEF","CONT_POLIZAS","SES","MSJ-PROCERR",3)); //"ERROR: No se encontr� la cuenta especificada");
    }

    return res;

  }

  public void establecerResultados()
  {
    double SumDebe = 0, SumHaber = 0;
    for(int i = 0; i < m_Partidas.size(); i++)
    {
      JContaPolizasSesPart part = (JContaPolizasSesPart) m_Partidas.elementAt(i);
      SumDebe += part.getDebe();
      SumHaber += part.getHaber();
    }
    m_SumDebe = JUtil.redondear(SumDebe, 2);
    m_SumHaber = JUtil.redondear(SumHaber, 2);
  }

  public void resetear()
  {
    m_Periodo = -1;
    m_Status = "";
    m_Ref = "";
    m_SumDebe = 0;
    m_SumHaber = 0;

    super.resetear();
  }

  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
    establecerResultados();
  }

}
