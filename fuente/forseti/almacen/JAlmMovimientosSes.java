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
package forseti.almacen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JInvServKitsArmaSetV2;
import forseti.sets.JPublicInvServConceptosCatSetV2;

public class JAlmMovimientosSes extends JSesionRegs
{

  private byte m_ID_Bodega;
  private long m_Numero;
  private boolean m_AuditarAlm;
  private Date m_Fecha;
  private String m_Concepto;
  private short m_ID_Clave;
  private String m_Clave_Descripcion;
  private String m_Ref;
  private byte m_TipoMov; // 1 ENTRADA, 2 SALIDA
  private boolean m_RecalcularCosto;
  
  public JAlmMovimientosSes()
  {
 
  }
  
  public JAlmMovimientosSes(byte ID_Bodega, long Numero, boolean AuditarAlm, byte TipoMov)
  {
	m_ID_Bodega = ID_Bodega;
	m_Numero = Numero;
	m_AuditarAlm = AuditarAlm;
	Calendar fecha = GregorianCalendar.getInstance();
	m_Fecha = fecha.getTime();
	m_TipoMov = TipoMov;
	m_ID_Clave = 0;
	m_Clave_Descripcion = "";
	m_Ref = "";
	m_Concepto = "";
	m_RecalcularCosto = false;
  }
  
  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	throws ServletException, IOException
  {
	  short res = -1; 

	  m_TipoMov = (request.getParameter("tipomoves") != null && request.getParameter("tipomoves").equals("salida") ? (byte)2 : (byte)1);

      if(request.getParameter("clave") != null && request.getParameter("movimiento") != null && request.getParameter("referencia") != null && 
        	request.getParameter("fecha") != null && request.getParameter("concepto") != null && 
        	!request.getParameter("clave").equals("") && !request.getParameter("movimiento").equals("") && 
        	!request.getParameter("fecha").equals("") )
      {
      }
      else
      {
         res = 3; 
         sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));//"ERROR: Alguno de los parametros del cabecero de este movimiento es nulo <br>");
         return res;
      }
    
      byte identidad = -1;

      if(request.getParameter("tipomov").equals("MOVIMIENTOS"))
      	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("ALM_MOVIM").getEspecial());
      else if(request.getParameter("tipomov").equals("PLANTILLAS"))
       	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("ALM_MOVPLANT").getEspecial());	  

      if(m_ID_Bodega != identidad)
	  {
	      res = 3; 
	      sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "ENTIDAD-DIF"));//"ERROR: La bodega del movimiento ya no es la misma que la inicial <br>");
	      return res;
	  }
      
      if( Integer.parseInt(request.getParameter("clave")) != m_ID_Clave )
      {
	      if(m_Partidas.size()  > 0)
	      {
	    	  super.resetear();
	     	  res = 1;
	    	  sb_mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",1));//"PRECAUCION: El concepto se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no te vuelva a suceder, primero debe seleccionar el concepto del movimiento.");
	      }
      }
      
      JPublicInvServConceptosCatSetV2 set = new JPublicInvServConceptosCatSetV2(request);
      set.m_Where = "ID_Concepto = '" + JUtil.p(request.getParameter("clave")) + "' and Tipo = '" + ((m_TipoMov == 1) ? "ENT" : "SAL") + "' and DeSistema = '0'";
	  set.Open();
	
	  if(set.getNumRows() < 1 )
      {
          res = 1; 
          sb_mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",2));//"PRECAUCION: No se encontró el concepto especificado<br>");
          return res;
      }
	  
	  m_ID_Clave = set.getAbsRow(0).getID_Concepto();
      m_Clave_Descripcion = set.getAbsRow(0).getDescripcion();
	  m_RecalcularCosto = set.getAbsRow(0).getRecalcularCosto();
	  m_Ref = request.getParameter("referencia");
	  m_Concepto = request.getParameter("concepto");
	  m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
	  
	  return res;
  }	
  
  
  public short VerificacionesFinales(HttpServletRequest request, StringBuffer sbmensaje)
  {
	short res = -1;
	
	for(int i = 0; i < m_Partidas.size(); i++)
	{
		JAlmMovimientosSesPart part = (JAlmMovimientosSesPart) m_Partidas.elementAt(i);
		res = JUtil.VerificaExistencias(request, sbmensaje, (byte)m_ID_Bodega, part.getID_Prod(), m_AuditarAlm, part.getCantidad());

		if(res != -1)
			break;
		
	}

	return res;
  
  }

  public JAlmMovimientosSesPart getPartida(int ind)
  {
    return (JAlmMovimientosSesPart)m_Partidas.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
public short agregaPartida(HttpServletRequest request, float cantidad, String idprod, String Costo, StringBuffer mensaje)
  {
	  short res = -1;
	  
	  JInvServKitsArmaSetV2 set = new JInvServKitsArmaSetV2(request);
	  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 String Unidad = set.getAbsRow(0).getUnidad();
		 String Descripcion = set.getAbsRow(0).getDescripcion();
		 float fcosto;
		 
		 if(!m_RecalcularCosto)
		 {
			if(Costo.equals(""))
				fcosto = 0.0F;
			else
				fcosto = Float.parseFloat(Costo);
		 }
		 else
		 {
			fcosto = ((set.getAbsRow(0).getTipoCosteo() == 0) ? set.getAbsRow(0).getUltimoCosto() : set.getAbsRow(0).getCostoPromedio());
		 }
		 
		 if(cantidad < 0.0) // || fcosto < 0.0 )
		 {
		     res = 1;
		     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",3));//PRECAUCION: La cantidad o el costo no son correctas, No se agregó la partida");
		 }
		 else
		 {
			 if(existeEnLista(idprod))
			 {
			     res = 1;
			     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",4));//"PRECAUCION: El producto ya existe en la lista");
			 }
			 else
			 {
				 // Aqui aplica la partida
				 JAlmMovimientosSesPart part = new JAlmMovimientosSesPart(cantidad, Unidad, idprod, Descripcion, fcosto, "");
				 m_Partidas.addElement(part);
			 }
		 }

	  }
	  else
	  {
	     res = 3;
	     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",5));//"ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio");
	  }

	  return res;
  }
  
  public boolean existeEnLista(String idprod)
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JAlmMovimientosSesPart part = (JAlmMovimientosSesPart) m_Partidas.elementAt(i);
	  	String clave = part.getID_Prod();
	  	
	  	if(clave.compareToIgnoreCase(idprod) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }

   
  @SuppressWarnings("unchecked")
  public void agregaPartida(float cantidad, String Unidad, String idprod, String Descripcion, float costo)
  {
    JAlmMovimientosSesPart part = new JAlmMovimientosSesPart(cantidad, Unidad, idprod, Descripcion, costo, "");
    m_Partidas.addElement(part);
  }

  public short editaPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, String Costo, StringBuffer mensaje)
  {
	  short res = -1;
	  
	  JInvServKitsArmaSetV2 set = new JInvServKitsArmaSetV2(request);
	  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 String Unidad = set.getAbsRow(0).getUnidad();
		 String Descripcion = set.getAbsRow(0).getDescripcion();
		 float fcosto;
		 
		 if(!m_RecalcularCosto)
		 {
			if(Costo.equals(""))
				fcosto = 0.0F;
			else
				fcosto = Float.parseFloat(Costo);
		 }
		 else
		 {
			fcosto = ((set.getAbsRow(0).getTipoCosteo() == 0) ? set.getAbsRow(0).getUltimoCosto() : set.getAbsRow(0).getCostoPromedio());
		 }
		 
		 if(cantidad < 0.0) // || fcosto < 0.0 )
		 {
		     res = 1;
		     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",3));//"PRECAUCION: La cantidad o el costo no son correctas, No se cambió la partida");
		 }
		 else
		 {
			 // Aqui cambia la partida
			 JAlmMovimientosSesPart part = (JAlmMovimientosSesPart) m_Partidas.elementAt(indPartida);
			 part.setPartida(cantidad, Unidad, idprod, Descripcion, fcosto, "");
			 			
		 }

	  }
	  else
	  {
	     res = 3;
	     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",5));//ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio. No se pudo cambiar la partida.");
	  }

	  return res;

  }


  public void resetear(byte ID_Bodega, long Numero, boolean AuditarAlm, byte TipoMov)
  {
		m_ID_Bodega = ID_Bodega;
		m_Numero = Numero;
		m_AuditarAlm = AuditarAlm;
		Calendar fecha = GregorianCalendar.getInstance();
		m_Fecha = fecha.getTime();
		m_TipoMov = TipoMov;
		m_ID_Clave = 0;
		m_Clave_Descripcion = "";
		m_Ref = "";
		m_Concepto = "";
		m_RecalcularCosto = false;
	
		super.resetear();
	
  }

  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
   
  }

  public Date getFecha()
  {
	return m_Fecha;
  }
  
  public boolean getAuditarAlm() 
  {
	return m_AuditarAlm;
  }

  public byte getID_Bodega() 
  {
	return m_ID_Bodega;
  }

  public boolean getRecalcularCosto()
  {
	return m_RecalcularCosto;
  }
  
  public long getNumero() 
  {
	return m_Numero;
  }

  public short getID_Clave() 
  {
	return m_ID_Clave;
  }

  public void setID_Clave(short ID_Clave) 
  {
	m_ID_Clave = ID_Clave;
  }

  public String getClave_Descripcion() 
  {
	return m_Clave_Descripcion;
  }

  public void setClave_Descripcion(String clave_Descripcion) 
  {
	m_Clave_Descripcion = clave_Descripcion;
  }

  public String getConcepto() 
  {
	return m_Concepto;
  }

  public void setConcepto(String concepto) 
  {
	m_Concepto = concepto;
  }

  public void setFecha(Date fecha) 
  {
	m_Fecha = fecha;
  }

  public void setID_Bodega(byte bodega) 
  {
	m_ID_Bodega = bodega;
  }

  public void setNumero(long numero) 
  {
	m_Numero = numero;
  }

  public void setAuditarAlm(boolean auditarAlm) 
  {
	m_AuditarAlm = auditarAlm;
  }
  
  public void setTipoMov(byte TipoMov)
  {
	m_TipoMov = TipoMov;  
  }
  
  public byte getTipoMov() 
  {
	return m_TipoMov;
  }

  public String getRef() 
  {
	return m_Ref;
  }

  public void setRef(String ref) 
  {
	m_Ref = ref;
  }

  public void setRecalcularCosto(boolean RecalcularCosto) 
  {
	m_RecalcularCosto = RecalcularCosto;
  }

}
