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
package forseti.ventas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JUtil;

public class JVenDevSes  extends JVenFactSes
{
	  private long m_ID_Factura;
	  private long m_DevNum;
	  	  
	  public JVenDevSes()
	  {
		  
	  }

	  public short VerificacionesFinales(HttpServletRequest request, StringBuffer sbmensaje)
	  {
		short res = -1;
		
		for(int i = 0; i < m_Partidas.size(); i++)
		{
		   JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
		   res = JUtil.VerificaStocks(request, sbmensaje, (byte)m_ID_Bodega, part.getID_Prod(), m_ManejoStocks, part.getCantidad());
		   if(res != -1)
		      break;
		   
		}

		return res;
	  
	  }
	  
	  public JVenDevSes(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)
	  {
		  super(request, ID_Entidad, usuario, tipomov);
		  
		  m_ID_Factura = 0;
		  m_DevNum = m_FactNum;
	  }
	  
	  public short editarPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, float precio, String descuento, 
			  String iva, String obs_partida, StringBuffer mensaje) 
	  {
		  short res = -1;
		  JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(indPartida);
		  
		  if( (m_DevReb.equals("DEV") && cantidad <= part.getCantidadOrig() && precio == part.getPrecioOrig())
				|| (m_DevReb.equals("REB") && cantidad == part.getCantidadOrig() && precio <= part.getPrecioOrig()) )
		  {
			 float fimporte, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, cantidadOrig, precioOrig;
			 	 
			 m_part_Unidad = part.getUnidad();
			 m_part_Clave = part.getID_Prod();
			 m_part_Descripcion = part.getID_ProdNombre();
			 m_part_ID_Tipo = part.getID_Tipo();
			 m_part_Descuento = part.getDescuento();
			 m_part_IVA = part.getIVA();
			 m_part_IEPS = part.getIEPS();
			 m_part_IVARet = part.getIVARet();
			 m_part_ISRRet = part.getISRRet();
			 
			 cantidadOrig = part.getCantidadOrig();
			 precioOrig = part.getPrecioOrig();
			 // Ahora hace las comparaciones.
			 
			 fimporte = JUtil.redondear(precio * cantidad, 2);
			 fimportedesc = (m_part_Descuento != 0.0) ? ((fimporte * m_part_Descuento) / 100.0F) : 0.0F;
			 fimporteiva = (m_part_IVA != 0.0) ? (((fimporte - fimportedesc) * m_part_IVA) / 100.0F) : 0.0F;
			 fimporteieps = (m_part_IEPS != 0.0) ? (((fimporte - fimportedesc) * m_part_IEPS) / 100.0F) : 0.0F;
			 fimporteivaret = (m_part_IVARet != 0.0) ? (((fimporte - fimportedesc) * m_part_IVARet) / 100.0F) : 0.0F;
			 fimporteisrret = (m_part_ISRRet != 0.0) ? (((fimporte - fimportedesc) * m_part_ISRRet) / 100.0F) : 0.0F;
			 ftotalpart = (((fimporte - fimportedesc) + (fimporteiva + fimporteieps)) - (fimporteivaret + fimporteisrret));
			 fimportedesc = JUtil.redondear(fimportedesc, 4);
			 fimporteiva = JUtil.redondear(fimporteiva, 4);
			 fimporteieps = JUtil.redondear(fimporteieps, 4);
			 fimporteivaret = JUtil.redondear(fimporteivaret, 4);
			 fimporteisrret = JUtil.redondear(fimporteisrret, 4);
			 ftotalpart = JUtil.redondear(ftotalpart, 4);
			   
			 if(cantidad < 0.0 || precio < 0.0)
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: La cantidad o precio no son correctas, No se cambió la partida");
			 }
			 else
			 {
				// Aqui aplica la partida
				part.setPartida(cantidad, m_part_Unidad, m_part_Clave, m_part_Clave, m_part_Descripcion, precio, fimporte, 
						m_part_Descuento, m_part_IVA, m_part_IEPS, m_part_IVARet, m_part_ISRRet, fimportedesc, 
							fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, obs_partida, m_part_ID_Tipo, cantidadOrig, precioOrig);
	
				establecerResultados();
				resetearPart();
			 }

		  }
		  else
		  {
			 resetearPart();
			 
		     res = 3;
		     if(m_DevReb.equals("DEV"))
		    	 mensaje.append("ERROR: No puedes cambiar el precio en una devolucion, ni devolver más cantidad de lo facturado");
		     else
		      	 mensaje.append("ERROR: No puedes cambiar la cantidad en una rebaja, ni igualar ó aumentar más el precio a lo facturado");
		  }

		  return res;
	  }
	  	 
	  public void resetear(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)
	  {
		  super.resetear(request, ID_Entidad, usuario, tipomov);
		  
		  m_ID_Factura = 0;
		  m_DevNum = m_FactNum;
		  
	  }
	  
	  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	  {
		  short res = -1;
		  
	      if(request.getParameter("factura") != null && request.getParameter("referencia") != null && 
	          	request.getParameter("tc") != null && request.getParameter("fecha") != null && 
	          	request.getParameter("obs") != null && 
	          	!request.getParameter("factura").equals("") && 
	          	!request.getParameter("tc").equals("") && !request.getParameter("fecha").equals("") )
	      {
	      }
	      else
	      {
	          res = 3; 
	          sb_mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo <br>");
	          return res;
	      }
	      
	      byte identidad = -1;
	      
	      identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("VEN_FAC").getEspecial());
	      
	  	  if(m_ID_Entidad != identidad)
		  {
		      res = 3; 
		      sb_mensaje.append("ERROR: La entidad de venta ya no es la misma que la inicial <br>");
		      return res;
		  }
		  
		  int devnum = Integer.parseInt(request.getParameter("factura"));
		  
		  if(devnum != m_DevNum && !m_CambioNumero)
		  {
	         res = 1; 
	         sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la Devolución en esta entidad. Este campo es exclusivamente informativo. Para cambiar la devolución, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
	         return res;
		  }
		  m_DevNum = devnum;
		 
		  m_Referencia = request.getParameter("referencia");
		  
		  m_Obs = request.getParameter("obs");
		  m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
		  
	      float tc = Float.parseFloat(request.getParameter("tc"));

	      if(m_ID_Moneda == 1 && tc != 1.0)
	      {
	         res = 1; 
	         sb_mensaje.append("PRECAUCION: El tipo de cambio para la moneda local no puede ser diferente de 1 <br>");
	         return res;
	      }
	      	      
	      m_TC = tc;

	      return res;
	  }

	  public long getFactNum() 
	  {
		return m_DevNum;
	  }

	  public void setFactNum(long FactNum) 
	  {
		m_DevNum = FactNum;
	  }

	  public long getID_Factura() 
	  {
		return m_ID_Factura;
	  }

	  public void setID_Factura(long ID_Factura) 
	  {
		m_ID_Factura = ID_Factura;
	  }

}









/*
public class JVenDevSes  extends JSesionRegs
{

	  private int m_Clave;
	  private int m_Numero;
	  private String m_Nombre;
	  private String m_Direccion;
	  private String m_Colonia;
	  private String m_Poblacion;
	  private String m_CP;
	  private String m_Tels;
	  private String m_RFC;

	  private byte m_ID_Entidad;
	  private long m_ID_Factura;
	  private long m_DevNum;
	  private String m_Referencia;
	  private String m_Forma_Pago;
	  private String m_Obs;
	  private byte m_ID_Moneda;
	  private float m_TC;
	  private Date m_Fecha; 
	  private int m_ID_Bodega;
	  private String m_BodegaDesc;
	  private float m_IVAPorcentual;
	  private boolean m_CambioNumero;
	  
	  private float m_Importe;
	  private float m_IVA;
	  private float m_Total;
	  
	  private String m_part_Descripcion;
	  private String m_part_Clave;
	  private String m_part_Unidad;
	  private float m_part_IVA;
	  private String m_part_ID_Tipo;
	  
	  public JVenDevSes()
	  {
		  
	  }

	  public JVenDevSes(HttpServletRequest request, String ID_Entidad, String usuario)
	  {
		  JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request);
		  set.m_Where = "ID_Tipo = 0 and ID_Usuario = '" + usuario + "' and ID_Entidad = " + ID_Entidad;
		  set.Open();

		  m_ID_Entidad = Byte.parseByte(ID_Entidad);
		  m_ID_Factura = 0;
		  m_DevNum = set.getAbsRow(0).getDevolucion();
		  
		  Calendar fecha = GregorianCalendar.getInstance();
		  m_Fecha = fecha.getTime();
		  m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
		  m_BodegaDesc = set.getAbsRow(0).getBodega();
		  m_IVAPorcentual = set.getAbsRow(0).getIVA();
		  
		  m_Forma_Pago = (set.getAbsRow(0).getTipoCobro() == 0 ? "contado" : "credito");
		  m_CambioNumero = set.getAbsRow(0).getCambioNumero();
		  m_ID_Moneda = 1;
		  m_TC = 1.0F;
		  m_Referencia = "";
		  m_Obs = "";
		  
		  m_Clave = 0;
		  m_Numero = 0;
		  m_Nombre = "MOSTRADOR";
		  m_Direccion = "";
		  m_Colonia = "";
		  m_Poblacion = "";
		  m_CP = "";
		  m_Tels = "";
		  m_RFC = "";

		  m_Importe = 0.0F;
		  m_IVA = 0.0F;
		  m_Total = 0.0F;
		  
		  resetearPart();

	  }
	  
	  public JVenDevSesPart getPartida(int ind)
	  {
	    return (JVenDevSesPart)m_Partidas.elementAt(ind);
	  }
	  
	  public void borraPartida(int indPartida)
	  {
	    super.borraPartida(indPartida);
	    establecerResultados();
	  }
	  
	  public short editaPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, float precio, String iva, String obs_partida, StringBuffer mensaje) 
	  {
		  short res = -1;
		  JVenDevSesPart part = (JVenDevSesPart) m_Partidas.elementAt(indPartida);
		  
		  if(cantidad <= part.getCantidadFact() && precio <= part.getPrecioFact())
		  {
			 m_part_Unidad = part.getUnidad();
			 m_part_Clave = part.getID_Prod();
			 m_part_Descripcion = part.getID_ProdNombre();
			 m_part_ID_Tipo = part.getID_Tipo();
			 
			 // Ahora hace las comparaciones.
			 float fiva, fimporte;
			  	 
			 if(iva.equals(""))
				fiva = m_part_IVA; 
			 else
				fiva = Float.parseFloat(iva);
			
			 fimporte = JUtil.redondear(precio * cantidad, 2);
	
			   
			 if(cantidad < 0.0 || precio < 0.0 || fiva < 0.0 )
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: La cantidad, precio, iva o descuento, no son correctas, No se cambió la partida");
			 }
			 else
			 {
				// Aqui aplica la partida
				part.setPartida(cantidad, m_part_Unidad, m_part_Clave, m_part_Descripcion, precio, fimporte, 
						 fiva, obs_partida, m_part_ID_Tipo);

				establecerResultados();
				resetearPart();
				
			 }

		  }
		  else
		  {
			 resetearPart();
			 
		     res = 3;
		     mensaje.append("ERROR: La cantidad " + cantidad + " o el precio " + precio + " del producto especificado son mayores al de la factura original " + part.getCantidadFact() + " y " + part.getPrecioFact() + ". No se puede cambiar la partida de la devolución");
		  }

		  return res;
	  }
	  
	   
	    @SuppressWarnings("unchecked")
  public void agregaPartida(float cantidad, String unidad, String idprod, String descripcion, float precio, float importe, float iva, String obs_partida, String idtipo) 
	  {
		    // Aqui aplica la partida
			JVenDevSesPart part = new JVenDevSesPart(cantidad, unidad, idprod, descripcion, precio, importe, 
							 iva, obs_partida, idtipo);
			m_Partidas.addElement(part);
	  }
	  
	  	 
	  public boolean existeEnLista()
	  {
		  
		boolean res = false;  
	  
		for(int i = 0; i < m_Partidas.size(); i++)
		{
		  	JVenDevSesPart part = (JVenDevSesPart) m_Partidas.elementAt(i);
		  	String clave = part.getID_Prod();
		  	
		  	if(clave.compareToIgnoreCase(m_part_Clave) == 0)
		  	{
		  		res = true;
		  		break;
		  	}
		}
		
		return res;
		
	  }
	  
	  public void establecerResultados()
	  {

	    float m_parImporte = 0.0F;
	    float m_parIVA = 0.0F;
	    float m_parTotal = 0.0F;
	  
		for(int i = 0; i < m_Partidas.size(); i++)
	    {
	    	JVenDevSesPart part = (JVenDevSesPart) m_Partidas.elementAt(i);
			
	    	float parImporte = part.getImporte();
			float parIVA = (part.getIVA() != 0.0) ? ((parImporte * part.getIVA()) / 100.0F) : 0.0F;
			float parTotal = (parImporte + parIVA);

			m_parImporte += parImporte;
			m_parIVA += parIVA;
			m_parTotal += parTotal; 
	    }
		
		m_Importe = JUtil.redondear(m_parImporte, 2);
		m_IVA = JUtil.redondear(m_parIVA, 2);
		m_Total = JUtil.redondear(m_parTotal, 2);
	  }
	 

	  private void resetearPart()
	  {
		  m_part_IVA = m_IVAPorcentual;
		  m_part_Clave = "";
		  m_part_Descripcion = "";
		  m_part_Unidad = "";
		  m_part_ID_Tipo = "";
	  
		  
	  }
	  
	  public void resetear(HttpServletRequest request, String ID_Entidad, String usuario)
	  {
		  JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request);
		  set.m_Where = "ID_Tipo = 0 and ID_Usuario = '" + usuario + "' and ID_Entidad = " + ID_Entidad;
		  set.Open();

		  m_ID_Entidad = Byte.parseByte(ID_Entidad);
		  m_ID_Factura = 0;
		  m_DevNum = set.getAbsRow(0).getDevolucion();
		  
		  Calendar fecha = GregorianCalendar.getInstance();
		  m_Fecha = fecha.getTime();
		  m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
		  m_BodegaDesc = set.getAbsRow(0).getBodega();
		  m_IVAPorcentual = set.getAbsRow(0).getIVA();
		  m_Forma_Pago = (set.getAbsRow(0).getTipoCobro() == 0 ? "contado" : "credito");
		  m_CambioNumero = set.getAbsRow(0).getCambioNumero();
		  m_ID_Moneda = 1;
		  m_TC = 1.0F;
		  m_Referencia = "";
		  m_Obs = "";
		  
		  m_Clave = 0;
		  m_Numero = 0;
		  m_Nombre = "MOSTRADOR";
		  m_Direccion = "";
		  m_Colonia = "";
		  m_Poblacion = "";
		  m_CP = "";
		  m_Tels = "";
		  m_RFC = "";

		  m_Importe = 0.0F;
		  m_IVA = 0.0F;
		  m_Total = 0.0F;
		  
		  resetearPart();
		  
		  super.resetear();
	  }
	  
	  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	  {
		  short res = -1;
		  
	      if(request.getParameter("devolucion") != null && request.getParameter("referencia") != null && 
	          	request.getParameter("tc") != null && request.getParameter("fecha") != null && 
	          	request.getParameter("obs") != null && 
	          	!request.getParameter("devolucion").equals("") && 
	          	!request.getParameter("tc").equals("") && !request.getParameter("fecha").equals("") )
	      {
	      }
	      else
	      {
	          res = 3; 
	          sb_mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo <br>");
	          return res;
	      }
	      
	      byte identidad = -1;
	      
	      identidad = Byte.parseByte(JUtil.getSesion(request).getSesionVenFacturas().getEspecial());
	      
	  	  if(m_ID_Entidad != identidad)
		  {
		      res = 3; 
		      sb_mensaje.append("ERROR: La entidad de venta ya no es la misma que la inicial <br>");
		      return res;
		  }
		  
		  int devnum = Integer.parseInt(request.getParameter("devolucion"));
		  
		  if(devnum != m_DevNum && !m_CambioNumero)
		  {
	         res = 1; 
	         sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la Devolución en esta entidad. Este campo es exclusivamente informativo. Para cambiar la devolución, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
	         return res;
		  }
		  m_DevNum = devnum;
		 
		  m_Referencia = request.getParameter("referencia");
		  
		  m_Obs = request.getParameter("obs");
		  m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
		  
	      float tc = Float.parseFloat(request.getParameter("tc"));

	      if(m_ID_Moneda == 1 && tc != 1.0)
	      {
	         res = 1; 
	         sb_mensaje.append("PRECAUCION: El tipo de cambio para la moneda local no puede ser diferente de 1 <br>");
	         return res;
	      }
	      	      
	      m_TC = tc;

	      return res;
	  }

	  public String getBodegaDesc() 
	  {
		return m_BodegaDesc;
	  }

	  public int getClave() 
	  {
		return m_Clave;
	  }

	  public String getColonia() 
	  {
		return m_Colonia;
	  }

	  public String getCP() 
	  {
		return m_CP;
	  }

	  public String getDireccion() 
	  {
		return m_Direccion;
	  }

	  public byte getID_Entidad() 
	  {
		return m_ID_Entidad;
	  }

	  public Date getFecha() 
	  {
		return m_Fecha;
	  }

	  public int getID_Bodega() 
	  {
		return m_ID_Bodega;
	  }

	  public byte getID_Moneda() 
	  {
		return m_ID_Moneda;
	  }

	  public float getTC() 
	  {
		return m_TC;
	  }

	  public float getIVA() 
	  {
		return m_IVA;
	  }

	  public String getNombre() 
	  {
		return m_Nombre;
	  }

	  public int getNumero() 
	  {
		return m_Numero;
	  }

	  public String getPoblacion() 
	  {
		return m_Poblacion;
	  }
	  
	  public long getDevNum() 
	  {
		return m_DevNum;
	  }

	  public String getRFC() 
	  {
		return m_RFC;
	  }

	  public float getImporte()
	  {
		return m_Importe;
	  }

	  public String getTels() 
	  {
		return m_Tels;
	  }

	  public float getTotal() 
	  {
		return m_Total;
	  }

	  public String getForma_Pago() 
	  {
		return m_Forma_Pago;
	  }

	  public String getObs()
	  {
		return m_Obs;
	  }

	  public String getReferencia()
	  {
		return m_Referencia;
	  }

	  public void setBodegaDesc(String bodegaDesc) 
	  {
		m_BodegaDesc = bodegaDesc;
	  }

	  public void setClave(int clave) 
	  {
		m_Clave = clave;
	  }

	  public void setColonia(String colonia) 
	  {
		m_Colonia = colonia;
	  }

	  public void setCP(String m_cp) 
	  {
		m_CP = m_cp;
	  }

	  public void setDireccion(String direccion) 
	  {
		m_Direccion = direccion;
	  }

	  public void setFecha(Date fecha) 
	  {
		m_Fecha = fecha;
	  }

	  public void setForma_Pago(String forma_Pago) 
	  {
		m_Forma_Pago = forma_Pago;
	  }

	  public void setID_Bodega(int bodega) 
	  {
		m_ID_Bodega = bodega;
	  }

	  public void setID_Entidad(byte entidad) 
	  {
		m_ID_Entidad = entidad;
	  }

	  public void setID_Moneda(byte moneda) 
	  {
		m_ID_Moneda = moneda;
	  }

	  public void setImporte(float importe) 
	  {
		m_Importe = importe;
	  }

	  public void setIVA(float m_iva) 
	  {
		m_IVA = m_iva;
	  }

	  public void setNombre(String nombre) 
	  {
		m_Nombre = nombre;
	  }

	  public void setNumero(int numero) 
	  {
		m_Numero = numero;
	  }

	  public void setObs(String obs) 
	  {
		m_Obs = obs;
	  }

	  public void setPoblacion(String poblacion) 
	  {
		m_Poblacion = poblacion;
	  }

	  public void setDevNum(long DevNum) 
	  {
		m_DevNum = DevNum;
	  }

	  public void setReferencia(String referencia) 
	  {
		m_Referencia = referencia;
	  }

	  public void setRFC(String rfc) 
	  {
		m_RFC = rfc;
	  }

	  public void setTC(float tc) 
	  {
		m_TC = tc;
	  }

	  public void setTels(String tels) 
	  {
		m_Tels = tels;
	  }

	  public void setTotal(float total) 
	  {
		m_Total = total;
	  }

	  public long getID_Factura() 
	  {
		return m_ID_Factura;
	  }

	  public void setID_Factura(long ID_Factura) 
	  {
		m_ID_Factura = ID_Factura;
	  }

}
*/