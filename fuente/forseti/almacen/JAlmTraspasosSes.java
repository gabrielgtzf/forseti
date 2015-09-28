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
import forseti.sets.JPublicBodegasCatSetV2;

public class JAlmTraspasosSes extends JSesionRegs
{

  private byte m_ID_Bodega;
  private long m_Numero;
  private boolean m_AuditarAlm;
  private boolean m_AuditarAlmDest;
  private byte m_ManejoStocks;
  private byte m_ManejoStocksOrigen;
  private Date m_Fecha;
  private String m_Concepto;
  private byte m_ID_BodegaDest;
  private String m_BodegaDest_Descripcion;
  private String m_Ref;
  private long m_TraspasoNum;
  
  public JAlmTraspasosSes()
  {
 
  }
  
  public JAlmTraspasosSes(byte ID_Bodega, long Numero, boolean AuditarAlm, byte ManejoStocks)
  {
	  m_ID_Bodega = ID_Bodega;
	  m_Numero = Numero;
	  m_AuditarAlm = AuditarAlm;
	  m_AuditarAlmDest = false;
	  m_ManejoStocksOrigen = ManejoStocks;
	  m_ManejoStocks = 0;
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_ID_BodegaDest = 0;
	  m_BodegaDest_Descripcion = "";
	  m_Ref = "";
	  m_Concepto = "";
  }
  
  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	throws ServletException, IOException
  {
	  short res = -1;
	  
      if(request.getParameter("idbodega") != null && request.getParameter("traspaso") != null && request.getParameter("referencia") != null && 
        	request.getParameter("fecha") != null && request.getParameter("concepto") != null && 
        	!request.getParameter("idbodega").equals("") && !request.getParameter("traspaso").equals("") && 
        	!request.getParameter("fecha").equals("") )
  
      {
      }
      else
      {
         res = 3; 
         sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));//"ERROR: Alguno de los parametros del cabecero de este traspaso es nulo <br>");
         return res;
      }
    
      byte identidad = -1;

      if(request.getParameter("tipomov").equals("TRASPASOS"))
      	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("ALM_TRASPASOS").getEspecial());
      else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
       	identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("ALM_REQUERIMIENTOS").getEspecial());	  

	  if(m_ID_Bodega != identidad)
	  {
	      res = 3; 
	      sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "ENTIDAD-DIF"));//"ERROR: La bodega del traspaso ya no es la misma que la inicial <br>");
	      return res;
	  }
	  
      if( Integer.parseInt(request.getParameter("idbodega")) != m_ID_BodegaDest)
      {
	      JPublicBodegasCatSetV2 set = new JPublicBodegasCatSetV2(request);
	      set.m_Where = "ID_Bodega = '" + JUtil.p(request.getParameter("idbodega")) + "' and ID_InvServ = 'P'";
		  set.Open();
		
		  if(set.getNumRows() < 1 )
	      {
	          res = 1; 
	          sb_mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",1));//"PRECAUCION: No se encontró la bodega especificada<br>");
	          return res;
	      }
		  
		  else if(set.getAbsRow(0).getID_Bodega() == m_ID_Bodega)
		  {
	          res = 1; 
	          sb_mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",2));//"PRECAUCION: La bodega especificada de destino, no puede ser la misma que la de origen<br>");
	          return res;
		  }
		  
		  if(m_Partidas.size()  > 0)
	      {
	    	  super.resetear();
	     	  res = 1;
	    	  sb_mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",3));//"PRECAUCION: La bodega de destino se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no te vuelva a suceder, primero debes seleccionar la bodega de destino");
	      }
      
		  m_ID_BodegaDest = (byte)set.getAbsRow(0).getID_Bodega();
		  m_BodegaDest_Descripcion = set.getAbsRow(0).getNombre();
		  m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
		  m_AuditarAlmDest = set.getAbsRow(0).getAuditarAlm();
      }
	  
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

		res = JUtil.VerificaStocks(request, sbmensaje, m_ID_BodegaDest, part.getID_Prod(), m_ManejoStocks, part.getCantidad());
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
public short agregaPartida(HttpServletRequest request, float cantidad, String idprod, StringBuffer mensaje)
  {
	  short res = -1;
	  
	  JInvServKitsArmaSetV2 set = new JInvServKitsArmaSetV2(request);
	  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 String Unidad = set.getAbsRow(0).getUnidad();
		 String Descripcion = set.getAbsRow(0).getDescripcion();
		 float fcosto = ((set.getAbsRow(0).getTipoCosteo() == 0) ? set.getAbsRow(0).getUltimoCosto() : set.getAbsRow(0).getCostoPromedio());

		 if(cantidad < 0.0)
		 {
		     res = 1;
		     mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",4));//"PRECAUCION: La cantidad no está correcta, No se agregó la partida");
		 }
		 else
		 {
			 if(existeEnLista(idprod))
			 {
			     res = 1;
			     mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",5));//"PRECAUCION: El producto ya existe en la lista, No se agregó la partida");
			 }
			 else
			 {
				 // Aqui aplica la partida
				 String obs = "";
				 int ex = -1; 
				 StringBuffer sbmensaje = new StringBuffer(), ssmensaje = new StringBuffer();
				 
				 if(request.getParameter("tipomov").equals("TRASPASOS"))
				 {
					 ex = JUtil.VerificaExistencias(request, sbmensaje, m_ID_Bodega, idprod, m_AuditarAlm, cantidad);
					 if(ex != -1)
						 obs += sbmensaje;
					 ex = JUtil.VerificaStocks(request, ssmensaje, m_ID_BodegaDest, idprod, m_ManejoStocks, cantidad);
					 if(ex != -1)
						 obs += "<br><b>" + ssmensaje.toString() + "</b>";
				 }
				 else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
				 {
					 ex = JUtil.VerificaStocks(request, sbmensaje, m_ID_Bodega, idprod, m_ManejoStocksOrigen, cantidad);
					 //System.out.println(ex + " " + sbmensaje.toString() + " " + m_ID_Bodega + " " + idprod + " " + m_ManejoStocksOrigen + " " + cantidad);
					 if(ex != -1)
						 obs += sbmensaje.toString();
					 ex = JUtil.VerificaExistencias(request, ssmensaje, m_ID_BodegaDest, idprod, m_AuditarAlmDest, cantidad);
					 if(ex != -1)
						 obs += "<br><b>" + ssmensaje.toString() + "</b>";
					 
				 }
				 
				 JAlmMovimientosSesPart part = new JAlmMovimientosSesPart(cantidad, Unidad, idprod, Descripcion, fcosto, obs);
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
  public void agregaPartida(float cantidad, String Unidad, String idprod, String Descripcion)
  {
    JAlmMovimientosSesPart part = new JAlmMovimientosSesPart(cantidad, Unidad, idprod, Descripcion, 0.0F, "");
    m_Partidas.addElement(part);
  }
  
   
  @SuppressWarnings("unchecked")
  public void agregaPartida(HttpServletRequest request, float cantidad, String Unidad, String idprod, String Descripcion)
  {
	  String obs = "";
	  int ex = -1; 
	  StringBuffer sbmensaje = new StringBuffer(), ssmensaje = new StringBuffer();
		 
	  if(request.getParameter("tipomov").equals("TRASPASOS"))
	  {
		  ex = JUtil.VerificaExistencias(request, sbmensaje, m_ID_Bodega, idprod, m_AuditarAlm, cantidad);
		  if(ex != -1)
			  obs += sbmensaje;
		  ex = JUtil.VerificaStocks(request, ssmensaje, m_ID_BodegaDest, idprod, m_ManejoStocks, cantidad);
		  if(ex != -1)
			  obs += "<br><b>" + ssmensaje.toString() + "</b>";
	  }
	  else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
	  {
		  ex = JUtil.VerificaStocks(request, sbmensaje, m_ID_Bodega, idprod, m_ManejoStocksOrigen, cantidad);
		  //System.out.println(ex + " " + sbmensaje.toString() + " " + m_ID_Bodega + " " + idprod + " " + m_ManejoStocksOrigen + " " + cantidad);
		  if(ex != -1)
			  obs += sbmensaje.toString();
		  ex = JUtil.VerificaExistencias(request, ssmensaje, m_ID_BodegaDest, idprod, m_AuditarAlmDest, cantidad);
		  //System.out.println(ex + " " + ssmensaje.toString() + " " + m_ID_BodegaDest + " " + idprod + " " + m_AuditarAlmDest + " " + cantidad);
		  if(ex != -1)
			  obs += "<br><b>" + ssmensaje.toString() + "</b>";
		  
	  }
	  
	  JAlmMovimientosSesPart part = new JAlmMovimientosSesPart(cantidad, Unidad, idprod, Descripcion, 0.0F, obs);
	  m_Partidas.addElement(part);
  }
  
  public short editaPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, StringBuffer mensaje)
  {
	  short res = -1;
	  
	  JInvServKitsArmaSetV2 set = new JInvServKitsArmaSetV2(request);
	  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 String Unidad = set.getAbsRow(0).getUnidad();
		 String Descripcion = set.getAbsRow(0).getDescripcion();
		 float fcosto = ((set.getAbsRow(0).getTipoCosteo() == 0) ? set.getAbsRow(0).getUltimoCosto() : set.getAbsRow(0).getCostoPromedio());
		 		 
		 if(cantidad < 0.0 )
		 {
		     res = 1;
		     mensaje.append(JUtil.Msj("CEF", "ALM_TRASPASOS", "SES", "MSJ-PROCERR",4));//"PRECAUCION: La cantidad no está correcta, No se cambió la partida");
		 }
		 else
		 {
			 // Aqui cambia la partida
			 String obs = "";
			 int ex = -1; 
			 StringBuffer sbmensaje = new StringBuffer(), ssmensaje = new StringBuffer();
			 
			 if(request.getParameter("tipomov").equals("TRASPASOS"))
			 {
				 ex = JUtil.VerificaExistencias(request, sbmensaje, m_ID_Bodega, idprod, m_AuditarAlm, cantidad);
				 if(ex != -1)
					 obs += sbmensaje;
				 ex = JUtil.VerificaStocks(request, ssmensaje, m_ID_BodegaDest, idprod, m_ManejoStocks, cantidad);
				 if(ex != -1)
					 obs += "<br><b>" + ssmensaje.toString() + "</b>";
			 }
			 else if(request.getParameter("tipomov").equals("REQUERIMIENTOS"))
			 {
				 ex = JUtil.VerificaStocks(request, sbmensaje, m_ID_Bodega, idprod, m_ManejoStocksOrigen, cantidad);
				 //System.out.println(ex + " " + sbmensaje.toString() + " " + m_ID_Bodega + " " + idprod + " " + m_ManejoStocksOrigen + " " + cantidad);
				 if(ex != -1)
					 obs += sbmensaje.toString();
				 ex = JUtil.VerificaExistencias(request, ssmensaje, m_ID_BodegaDest, idprod, m_AuditarAlmDest, cantidad);
				 if(ex != -1)
					 obs += "<br><b>" + ssmensaje.toString() + "</b>";
				 
			 }

			 JAlmMovimientosSesPart part = (JAlmMovimientosSesPart) m_Partidas.elementAt(indPartida);
			 part.setPartida(cantidad, Unidad, idprod, Descripcion, fcosto, obs);
			 			
		 }

	  }
	  else
	  {
	     res = 3;
	     mensaje.append(JUtil.Msj("CEF", "ALM_MOVIM", "SES", "MSJ-PROCERR",5));//"ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio. No se pudo cambiar la partida.");
	  }

	  return res;

  }


  public void resetear(byte ID_Bodega, long Numero, boolean AuditarAlm, byte ManejoStocks)
  {
	  m_ID_Bodega = ID_Bodega;
	  m_Numero = Numero;
	  m_AuditarAlm = AuditarAlm;
	  m_AuditarAlmDest = false;
	  m_ManejoStocksOrigen = ManejoStocks;
	  m_ManejoStocks = 0;
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_ID_BodegaDest = 0;
	  m_BodegaDest_Descripcion = "";
	  m_Ref = "";
	  m_Concepto = "";
	
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

  public long getNumero() 
  {
	return m_Numero;
  }

  public byte getID_BodegaDest() 
  {
	return m_ID_BodegaDest;
  }

  public void setID_BodegaDest(byte ID_BodegaDest) 
  {
	m_ID_BodegaDest = ID_BodegaDest;
  }

  public String getBodegaDest_Descripcion() 
  {
	return m_BodegaDest_Descripcion;
  }

  public void setBodegaDest_Descripcion(String BodegaDest_Descripcion) 
  {
	m_BodegaDest_Descripcion = BodegaDest_Descripcion;
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

  public void setAuditarAlm(boolean AuditarAlm) 
  {
	m_AuditarAlm = AuditarAlm;
  }
  
  public void setManejoStocks(byte ManejoStocks) 
  {
	m_ManejoStocks = ManejoStocks;
  }

  public void setAuditarAlmDEST(boolean AuditarAlmDest) 
  {
	m_AuditarAlmDest = AuditarAlmDest;
  }
  
  public String getRef() 
  {
	return m_Ref;
  }

  public void setRef(String ref) 
  {
	m_Ref = ref;
  }

  public long getTraspasoNum() 
  {
	return m_TraspasoNum;
  }

  public void setTraspasoNum(long TraspasoNum) 
  {
	m_TraspasoNum = TraspasoNum;
  }

}