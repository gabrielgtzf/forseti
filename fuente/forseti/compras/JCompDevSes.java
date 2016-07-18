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
package forseti.compras;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JUtil;
import forseti.ventas.JVenFactSesPart;

public class JCompDevSes  extends JCompFactSes
{
	  private long m_ID_Factura;
	  private long m_DevNum;
	  	  
	  public JCompDevSes()
	  {
		  
	  }

	  public short VerificacionesFinales(HttpServletRequest request, StringBuffer sbmensaje)
	  {
		short res = -1;
		if(m_FijaCost == false)
		{
			for(int i = 0; i < m_Partidas.size(); i++)
			{
				JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
				if(part.getID_Tipo().equals("P"))
				{
					res = JUtil.VerificaExistencias(request, sbmensaje, (byte)m_ID_Bodega, part.getID_Prod(), m_AuditarAlm, part.getCantidad());

					if(res != -1)
						break;
				}
			}
		}
		return res;
	  }
	  
	  public JCompDevSes(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)
	  {
		  super(request, ID_Entidad, usuario, tipomov);
		  
		  m_ID_Factura = 0;
		  m_DevNum = m_FactNum;
	  }
	  
	  public short editarPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, float precio, String descuento, 
			  String iva, String ieps, String ivaret, String isrret, String obs_partida, StringBuffer mensaje) 
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
						m_part_Descuento, m_part_IVA, m_part_IEPS, m_part_IVARet, m_part_ISRRet, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, obs_partida, m_part_ID_Tipo, cantidadOrig, precioOrig);
	
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
	      
	      identidad = Byte.parseByte(JUtil.getSesion(request).getSesion("COMP_FAC").getEspecial());
	      
	  	  if(m_ID_Entidad != identidad)
		  {
		      res = 3; 
		      sb_mensaje.append("ERROR: La entidad de compra ya no es la misma que la inicial <br>");
		      return res;
		  }
		  
		  int devnum = Integer.parseInt(request.getParameter("factura"));
		  
		  if(devnum != m_DevNum && !m_CambioNumero)
		  {
	         res = 1; 
	         sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la Devolución en esta entidad. Este campo es exclusivamente informativo. Para cambiar la devolución, necesitas cambiarla desde los parámetros de la entidad de compra <br>");
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
