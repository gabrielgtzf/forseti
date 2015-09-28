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
package forseti.nomina;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JMasempSet;
import forseti.sets.JMovimientosNomSet;

public class JNomMovDirSes extends JSesionRegs
{
  private String m_UUID;
  private String m_ID_Empleado;
  private String m_Nombre;
  private float m_Dias;
  private float m_Faltas;
  private int m_Recibo;
  private float m_HE;
  private float m_HD;
  private float m_HT;
  private byte m_DiasHorasExtras;
  private float m_IXA;
  private float m_IXE;
  private float m_IXM;
 
  private float m_SumGravado;
  private float m_SumExento;
  private float m_SumDeduccion;

  public JNomMovDirSes()
  {
	m_UUID = "";
	m_ID_Empleado = "";
	m_Nombre = "";
	m_Dias = 0;
	m_Faltas = 0;
	m_Recibo = 0;
	m_HE = 0;
	m_HD = 0;
	m_HT = 0;
	m_DiasHorasExtras = 0;
	m_IXA = 0;
	m_IXE = 0;
	m_IXM = 0;
    m_SumGravado = 0;
    m_SumExento = 0;
    m_SumDeduccion = 0;
  }

  public void setParametros(String ID_Empleado, String Nombre)
  {
    m_ID_Empleado = ID_Empleado;
    m_Nombre = Nombre;
  }

  public float getSumGravado()
  {
    return m_SumGravado;
  }

  public float getSumExento()
  {
    return m_SumExento;
  }

  public float getSumDeduccion()
  {
    return m_SumDeduccion;
  }

  public void setID_Empleado(String ID_Empleado)
  {
    m_ID_Empleado = ID_Empleado;
  }

  public void setNombre(String Nombre)
  {
    m_Nombre = Nombre;
  }

  public String getID_Empleado()
  {
    return m_ID_Empleado;
  }

  public String getNombre()
  {
    return m_Nombre;
  }

  public JNomMovDirSesPart getPartida(int ind)
  {
    return (JNomMovDirSesPart)m_Partidas.elementAt(ind);
  }

  public short agregaRecursos(HttpServletRequest request, StringBuffer sb_mensaje)
	throws ServletException, IOException
  {
	  short res = -1;
	  
	  for(int i = 0; i < m_Partidas.size(); i++)
	  {
		  JNomMovDirSesPart part = (JNomMovDirSesPart) m_Partidas.elementAt(i);
		  int idmovimiento; try{ idmovimiento = Integer.parseInt(request.getParameter("idmovimiento_" + i)); } catch(NumberFormatException e) { idmovimiento = 0; }
		  part.setID_Movimiento(idmovimiento);
			
		  JMovimientosNomSet set = new JMovimientosNomSet(request);
		  set.m_Where = "ID_Movimiento = '" + idmovimiento + "'";
      	  set.Open();
			 
		  if(set.getNumRows() < 1)
		  {
			  res = 3;
			  sb_mensaje.append("ERROR: La clave del movimiento de nómina no existe en el catálogo: " + idmovimiento + "<br>");
		  }
	  }
	  
	  // Verificacion
	  if(request.getParameter("id_empleado") != null && request.getParameter("faltas") != null && 
			  !request.getParameter("id_empleado").equals("") && !request.getParameter("faltas").equals("")) 
	  {
	  }
	  else
	  {
		  res = 3; 
		  sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
		  return res;
	  }
	  
	  JMasempSet setemp = new JMasempSet(request);
	  setemp.m_Where = setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "' and ID_Empleado = '" + JUtil.p(request.getParameter("id_empleado")) + "'";
	  setemp.Open();
		
	  //System.out.println("SQL: " + setemp.getSQL());
	  
	  if(setemp.getNumRows() < 1)
	  {
		  res = 3;
		  sb_mensaje.append("ERROR: El empleado no existe, o no pertenece a esta n&oacute;mina<br>");
		  return res;
	  }
		
	  m_Faltas = Float.parseFloat(request.getParameter("faltas")); 
	  
	  return res;
  }  
  
  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje)
	throws ServletException, IOException
  {
	  short res = -1;
	  
	  // Verificacion
	  if(request.getParameter("id_empleado") != null && 
			  request.getParameter("faltas") != null && request.getParameter("he") != null && request.getParameter("hd") != null &&
			  request.getParameter("ht") != null && request.getParameter("dhe") != null && request.getParameter("ixa") != null && request.getParameter("ixe") != null && request.getParameter("ixm") != null &&
			  !request.getParameter("id_empleado").equals("") && 
			  !request.getParameter("faltas").equals("") && !request.getParameter("he").equals("") && !request.getParameter("hd").equals("") &&
			  !request.getParameter("ht").equals("") && !request.getParameter("dhe").equals("") && !request.getParameter("ixa").equals("") && !request.getParameter("ixe").equals("") && !request.getParameter("ixm").equals("") ) 
	  {
	  }
	  else
	  {
		  res = 3; 
		  sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
		  return res;
	  }
	  
	  JMasempSet setemp = new JMasempSet(request);
	  setemp.m_Where = setemp.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "' and ID_Empleado = '" + JUtil.p(request.getParameter("id_empleado")) + "'";
	  setemp.Open();
		
	  //System.out.println("SQL: " + setemp.getSQL());
	  
	  if(setemp.getNumRows() < 1)
	  {
		  res = 3;
		  sb_mensaje.append("ERROR: El empleado no existe, o no pertenece a esta n&oacute;mina<br>");
		  return res;
	  }
		
	  m_ID_Empleado = setemp.getAbsRow(0).getID_Empleado();
	  m_Nombre = setemp.getAbsRow(0).getNombre();
	 
	  m_Faltas = Float.parseFloat(request.getParameter("faltas")); 
	  m_HE = Float.parseFloat(request.getParameter("he"));
	  m_HD = Float.parseFloat(request.getParameter("hd"));
	  m_HT = Float.parseFloat(request.getParameter("ht"));
	  m_DiasHorasExtras = Byte.parseByte(request.getParameter("dhe"));
	  m_IXA = Float.parseFloat(request.getParameter("ixa"));
	  m_IXE = Float.parseFloat(request.getParameter("ixe"));
	  m_IXM = Float.parseFloat(request.getParameter("ixm"));
	 
	  return res;
  }

  @SuppressWarnings("unchecked")
  public short agregaPartida(HttpServletRequest request, int id_movimiento,
                            float gravado, float exento, float deduccion, StringBuffer mensaje)
  {
    short res = -1;

    JMovimientosNomSet set = new JMovimientosNomSet(request);
    set.m_Where = "ID_Movimiento = '" + id_movimiento + "'";
    set.Open();
    if(set.getNumRows() > 0)
    {
      if(set.getAbsRow(0).getDeduccion())
      {
    	  if(gravado != 0 || exento != 0)
    	  {
    		  res = 1;
    		  mensaje.append("PRECAUCION: Al agregar, el movimiento se encontró en el catálogo, pero no se puede insertar porque es un movimiento de deduccion, y el gravado y/o exento no se establecieron en cero");
    		  return res;
    	  }
      }
      else 
      {
    	  if(deduccion != 0)
    	  {
    		  res = 1;
    		  mensaje.append("PRECAUCION: Al agregar, el movimiento se encontró en el catálogo, pero no se puede insertar porque es un movimiento gravado o exento, y la deduccion no se estableció en cero");
    		  return res;
    	  }
      }
      
      String descripcion = set.getAbsRow(0).getDescripcion();
      boolean esdeduccion = set.getAbsRow(0).getDeduccion();
      JNomMovDirSesPart part = new JNomMovDirSesPart(id_movimiento, descripcion, gravado, exento, deduccion, esdeduccion);
      m_Partidas.addElement(part);
      establecerResultados();
      
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontró el movimiento de nómina");
    }

    return res;

  }

   
    @SuppressWarnings("unchecked")
  public void agregaPartida(int id_movimiento, String descripcion, float gravado, float exento, float deduccion, boolean esdeduccion)
  {
    JNomMovDirSesPart part = new JNomMovDirSesPart(id_movimiento, descripcion, gravado, exento, deduccion, esdeduccion);
    m_Partidas.addElement(part);
    establecerResultados();
  }

  public short editaPartida(int indPartida, HttpServletRequest request, int id_movimiento,
                            float gravado, float exento, float deduccion, StringBuffer mensaje)
  {
    short res = -1;

    JMovimientosNomSet set = new JMovimientosNomSet(request);
    set.m_Where = "ID_Movimiento = '" + id_movimiento + "'";
    set.Open();
    if(set.getNumRows() > 0)
    {
      if(set.getAbsRow(0).getDeduccion())
      {
    	  if(gravado != 0 || exento != 0)
    	  {
    		  res = 1;
    		  mensaje.append("PRECAUCION: Al agregar, el movimiento se encontró en el catálogo, pero no se puede insertar porque es un movimiento de deduccion, y el gravado y/o exento no se establecieron en cero");
    		  return res;
    	  }
      }
      else 
      {
    	  if(deduccion != 0)
    	  {
    		  res = 1;
    		  mensaje.append("PRECAUCION: Al agregar, el movimiento se encontró en el catálogo, pero no se puede insertar porque es un movimiento gravado o exento, y la deduccion no se estableció en cero");
    		  return res;
    	  }
      }
      
      String descripcion = set.getAbsRow(0).getDescripcion();
      boolean esdeduccion = set.getAbsRow(0).getDeduccion();
      JNomMovDirSesPart part = (JNomMovDirSesPart) m_Partidas.elementAt(indPartida);
      part.setPartida(id_movimiento, descripcion, gravado, exento, deduccion, esdeduccion);
      establecerResultados();
      
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontró el movimiento de nómina");
    }

    return res;
   
  }

  public void establecerResultados()
  {
    float SumGravado = 0, SumExento = 0, SumDeduccion = 0;
    for(int i = 0; i < m_Partidas.size(); i++)
    {
      JNomMovDirSesPart part = (JNomMovDirSesPart) m_Partidas.elementAt(i);
      SumGravado += part.getGravado();
      SumExento += part.getExento();
      SumDeduccion += part.getDeduccion();
    }
    
    m_SumGravado = JUtil.redondear(SumGravado, 2);
    m_SumExento = JUtil.redondear(SumExento, 2);
    m_SumDeduccion = JUtil.redondear(SumDeduccion, 2);
  }

  public void resetear()
  {
	  m_UUID = "";
	  m_ID_Empleado = "";
	  m_Nombre = "";
	  m_Dias = 0;
	  m_Faltas = 0;
	  m_Recibo = 0;
	  m_HE = 0;
	  m_HD = 0;
	  m_HT = 0;
	  m_DiasHorasExtras = 0;
	  m_IXA = 0;
	  m_IXE = 0;
	  m_IXM = 0;
	  m_SumGravado = 0;
	  m_SumExento = 0;
	  m_SumDeduccion = 0;

	  super.resetear();
  }

  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
    
    establecerResultados();
  }
  
	public void setDias(float Dias)
	{
		m_Dias = Dias;
	}

	public void setFaltas(float Faltas)
	{
		m_Faltas = Faltas;
	}

	public void setRecibo(int Recibo)
	{
		m_Recibo = Recibo;
	}

	public float getDias()
	{
		return m_Dias;
	}

	public float getFaltas()
	{
		return m_Faltas;
	}

	public int getRecibo()
	{
		return m_Recibo;
	}

	public void setHE(float HE) 
	{
		m_HE = HE;
	}
	public void setHD(float HD) 
	{
		m_HD = HD;
	}

	public float getHE()
	{
		return m_HE;
	}
	
	public float getHD()
	{
		return m_HD;
	}

	public void setHT(float HT) 
	{
		m_HT = HT;		
	}

	public void setDiasHorasExtras(byte DiasHorasExtras) 
	{
		m_DiasHorasExtras = DiasHorasExtras;
	}

	public void setIXA(float IXA) 
	{
		m_IXA = IXA;
	}
	
	public float getHT() 
	{
		return m_HT;		
	}

	public byte getDiasHorasExtras() 
	{
		return m_DiasHorasExtras;
	}

	public float getIXA() 
	{
		return m_IXA;
	}
	
	public void setIXE(float IXE) 
	{
		m_IXE = IXE;
	}
	
	public float getIXE() 
	{
		return m_IXE;
	}
	
	public void setIXM(float IXM) 
	{
		m_IXM = IXM;
	}
	
	public float getIXM() 
	{
		return m_IXM;
	}
	
	public String getUUID()
	{
		return m_UUID;
	}
		
	public void setUUID(String UUID)
	{
		m_UUID = UUID;
	}
}
