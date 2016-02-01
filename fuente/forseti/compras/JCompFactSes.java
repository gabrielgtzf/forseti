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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JUtil;
import forseti.sets.JComprasEntidadesSetIdsV2;
import forseti.sets.JComprasInvServDatXProSetV2;
import forseti.sets.JProveeProveeMasSetV2;
import forseti.sets.JProveeProveeSetV2;
import forseti.sets.JPublicInvServInvCatalogSetV2;
import forseti.sets.JPublicProveeSetV2;
import forseti.ventas.JVenFactSes;
import forseti.ventas.JVenFactSesPart;

public class JCompFactSes extends JVenFactSes 
{
	public JCompFactSes()	
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

	public JCompFactSes(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)	
	{
		JComprasEntidadesSetIdsV2 set = new JComprasEntidadesSetIdsV2(request, usuario, ID_Entidad,
				(tipomov.equals("GASTOS") ? 2 : 0));
		set.Open();
		m_ID_Entidad = Byte.parseByte(ID_Entidad);
		if(tipomov.equals("FACTURAS"))
		{
			m_IdMod = "COMP_FAC";
			m_FactNum = set.getAbsRow(0).getDoc();
		}
		else if(tipomov.equals("ORDENES"))
		{
			m_IdMod = "COMP_ORD";
			m_FactNum = set.getAbsRow(0).getOrden();
		}
		else if(tipomov.equals("RECEPCIONES"))
		{
			m_IdMod = "COMP_REC";
			m_FactNum = set.getAbsRow(0).getRecepcion();
		}
		else if(tipomov.equals("GASTOS"))
		{
			m_IdMod = "COMP_GAS";
			m_FactNum = set.getAbsRow(0).getDoc();
		}
		else
		{
			m_IdMod = "COMP_DEV";
			m_FactNum = set.getAbsRow(0).getDevolucion();
		}
		Calendar fecha = GregorianCalendar.getInstance();
		m_Fecha = fecha.getTime();
		m_FechaEntrega = fecha.getTime();
		m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
		m_BodegaDesc = set.getAbsRow(0).getBodega();
		m_IVAPorcentual = set.getAbsRow(0).getIVA();
		m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
		m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
		m_FijaCost = false; //set.getAbsRow(0).getFijaCost();
		m_Forma_Pago = "contado";
		m_PrecioEspMostr = false;
		m_ID_Moneda = 1;
		m_TC = 1.0F;
		m_Referencia = "";
		m_Obs = "";
		m_DevReb = "COMP";

		m_Clave = 0;
		m_Numero = 0;
		m_Nombre = "MOSTRADOR";
		m_Direccion = "";
		m_Colonia = "";
		m_Poblacion = "";
		m_CP = "";
		m_Tels = "";
		m_RFC = "XAXX010101000";
		m_UUID = "";

		m_Importe = 0.0F;
		m_Descuento = 0.0F;
		m_SubTotal = 0.0F;
		m_IVA = 0.0F;
		m_IEPS = 0.0F;
		m_IVARet = 0.0F;
		m_ISRRet = 0.0F;
		m_Total = 0.0F;
		
		resetearPart();

	}
	
	public void resetear(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)	
	{
		JComprasEntidadesSetIdsV2 set = new JComprasEntidadesSetIdsV2(request, usuario, ID_Entidad,
				(tipomov.equals("GASTOS") ? 2 : 0));
		set.Open();
		m_ID_Entidad = Byte.parseByte(ID_Entidad);
		if(tipomov.equals("FACTURAS"))
		{
			m_IdMod = "COMP_FAC";
			m_FactNum = set.getAbsRow(0).getDoc();
		}
		else if(tipomov.equals("ORDENES"))
		{
			m_IdMod = "COMP_ORD";
			m_FactNum = set.getAbsRow(0).getOrden();
		}
		else if(tipomov.equals("RECEPCIONES"))
		{
			m_IdMod = "COMP_REC";
			m_FactNum = set.getAbsRow(0).getRecepcion();
		}
		else if(tipomov.equals("GASTOS"))
		{
			m_IdMod = "COMP_GAS";
			m_FactNum = set.getAbsRow(0).getDoc();
		}
		else
		{
			m_IdMod = "COMP_DEV";
			m_FactNum = set.getAbsRow(0).getDevolucion();
		}
		Calendar fecha = GregorianCalendar.getInstance();
		m_Fecha = fecha.getTime();
		m_FechaEntrega = fecha.getTime();
		m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
		m_BodegaDesc = set.getAbsRow(0).getBodega();
		m_IVAPorcentual = set.getAbsRow(0).getIVA();
		m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
		m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
		m_FijaCost = false; //set.getAbsRow(0).getFijaCost();
		m_Forma_Pago = "contado";
		m_PrecioEspMostr = false;
		m_ID_Moneda = 1;
		m_TC = 1.0F;
		m_Referencia = "";
		m_Obs = "";
		m_DevReb = "COMP";
		
		m_Clave = 0;
		m_Numero = 0;
		m_Nombre = "MOSTRADOR";
		m_Direccion = "";
		m_Colonia = "";
		m_Poblacion = "";
		m_CP = "";
		m_Tels = "";
		m_RFC = "XAXX010101000";
		m_UUID = "";

		m_Importe = 0.0F;
		m_Descuento = 0.0F;
		m_SubTotal = 0.0F;
		m_IVA = 0.0F;
		m_IEPS = 0.0F;
		m_IVARet = 0.0F;
		m_ISRRet = 0.0F;
		m_Total = 0.0F;
		
		resetearPart();
		
		vaciarPart();
		
	}
	
	public short agregaRecursos(HttpServletRequest request, StringBuffer sb_mensaje) 
		  	throws ServletException, IOException
	{
		if(request.getParameter("factura") != null && request.getParameter("referencia") != null && 
			request.getParameter("tc") != null && request.getParameter("idmoneda") != null && request.getParameter("fecha") != null && 
			request.getParameter("entrega") != null && request.getParameter("obs") != null && 
			!request.getParameter("factura").equals("") && !request.getParameter("referencia").equals("") && 
			!request.getParameter("tc").equals("") && !request.getParameter("idmoneda").equals("") && !request.getParameter("fecha").equals("") && 
			!request.getParameter("entrega").equals(""))
		{
			m_Referencia = request.getParameter("referencia");
			m_Forma_Pago = request.getParameter("forma_pago");
			m_Obs = request.getParameter("obs");
			m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
			m_FechaEntrega = JUtil.estFecha(request.getParameter("entrega"));
			
		}
		else
		{
			sb_mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo <br>");
			return 3;
		}
		
		byte identidad = -1;

	    identidad = Byte.parseByte(JUtil.getSesion(request).getSesion(m_IdMod).getEspecial());
	      
	    if(m_ID_Entidad != identidad)
	    {
	    	sb_mensaje.append("ERROR: La entidad de compra ya no es la misma que la inicial <br>");
	    	return 3;
	    }

	    int factnum = Integer.parseInt(request.getParameter("factura"));
		  
	    if(factnum != m_FactNum)
	    {
	    	sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente el Número de este registro este campo es exclusivamente informativo. Para cambiar el numero del registro, necesitas cambiarla desde los parámetros de la entidad de compra <br>");
	    	return 1;
	    }
	   
	    m_FactNum = factnum;
	    		  
		byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));
		float tc = Float.parseFloat(request.getParameter("tc"));

		if(idmoneda == 1 && tc != 1.0)
		{
			sb_mensaje.append("PRECAUCION: El tipo de cambio para la moneda local no puede ser diferente de 1 <br>");
			return 1;
		}
		
		m_ID_Moneda = idmoneda;
		m_TC = tc;
		
		if( m_IdMod == "COMP_GAS" && request.getParameter("numero") != null && Integer.parseInt(request.getParameter("numero")) != m_Numero)
		{
			JProveeProveeSetV2 set = new JProveeProveeSetV2(request);
			set.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + JUtil.getSesion(request).getSesion(m_IdMod).getEspecial() + "' and Numero = '" + JUtil.p(request.getParameter("numero")) + "'";
			set.Open();
			if(set.getNumRows() < 1 )
			{
				sb_mensaje.append("PRECAUCION: El proveedor no existe en esta entidad. Selecciona otra entidad para poder ingresar el proveedor<br>");
				return 1;
			}
				      
			JProveeProveeMasSetV2 mas = new JProveeProveeMasSetV2(request);
			mas.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + set.getAbsRow(0).getClave() + "'";
			mas.Open();
						      
			m_Clave = set.getAbsRow(0).getClave();
			m_Numero = set.getAbsRow(0).getNumero();
			m_Nombre = set.getAbsRow(0).getNombre();
			m_Direccion = mas.getAbsRow(0).getDireccion();
			m_Colonia = mas.getAbsRow(0).getColonia();
			m_Poblacion = mas.getAbsRow(0).getPoblacion();
			m_CP = mas.getAbsRow(0).getCP();
			m_Tels = set.getAbsRow(0).getTel();
			m_RFC = mas.getAbsRow(0).getRFC();
		}
		
		for(int i = 0; i < m_Partidas.size(); i++)
		{
			JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
			String idprodant = part.getID_Prod();
			String idprod = request.getParameter("idprod_" + i);
			part.setID_Prod(idprod);
			part.setID_ProdAnt(idprodant);
		}
		
		short idmensaje = establecerConcordancia(request, sb_mensaje);
		establecerResultados();
			
		if((m_Total - m_TotalUUIDs) > 0.1 || (m_Total - m_TotalUUIDs) < -0.1)
		{
			sb_mensaje.append("ERROR: El total en el o los CFDI no corresponden al Total calculado en el registro a partir de estos CFDI. No se puede agregar. DOC: " + m_Total + " XML: " + m_TotalUUIDs);
			return 3;
		}
		 
		return idmensaje;
		  
	}
	
	
	
	public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
			  	throws ServletException, IOException
	{
		  short res = -1;
	      
		  if(request.getParameter("clave") != null && request.getParameter("factura") != null && request.getParameter("referencia") != null && 
				  request.getParameter("tc") != null && request.getParameter("idmoneda") != null && request.getParameter("fecha") != null && 
				  request.getParameter("entrega") != null && request.getParameter("numero") != null && request.getParameter("obs") != null && 
				  !request.getParameter("clave").equals("") && !request.getParameter("factura").equals("") && !request.getParameter("referencia").equals("") && 
				  !request.getParameter("tc").equals("") && !request.getParameter("idmoneda").equals("") && !request.getParameter("fecha").equals("") && 
				  !request.getParameter("entrega").equals("") && !request.getParameter("numero").equals(""))
	      {
	      }
		  else
		  {
			  res = 3; 
			  sb_mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo <br>");
			  return res;
		  }
			      
		  byte identidad = -1;

	      identidad = Byte.parseByte(JUtil.getSesion(request).getSesion(m_IdMod).getEspecial());
	      
	  	  if(m_ID_Entidad != identidad)
		  {
		      res = 3; 
		      sb_mensaje.append("ERROR: La entidad de compra ya no es la misma que la inicial <br>");
		      return res;
		  }

	  	  int factnum = Integer.parseInt(request.getParameter("factura"));
		  
		  if(factnum != m_FactNum)
		  {
			  res = 1; 
			  sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente el Número de este registro este campo es exclusivamente informativo. Para cambiar el numero del registro, necesitas cambiarla desde los parámetros de la entidad de compra <br>");
			  return res;
		  }
		  
		  m_FactNum = factnum;
		 
		  m_Referencia = request.getParameter("referencia");
		  m_Forma_Pago = request.getParameter("forma_pago");
		  m_Obs = request.getParameter("obs");
		  m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
		  m_FechaEntrega = JUtil.estFecha(request.getParameter("entrega"));
				  
		  byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));
		  float tc = Float.parseFloat(request.getParameter("tc"));

		  if(idmoneda == 1 && tc != 1.0)
		  {
			  res = 1; 
			  sb_mensaje.append("PRECAUCION: El tipo de cambio para la moneda local no puede ser diferente de 1 <br>");
			  return res;
		  }
			      
		  if(idmoneda != m_ID_Moneda)
		  {
			  if(m_Partidas.size()  > 0)
			  {
				  super.resetear();
				  res = 1;
				  sb_mensaje.append("PRECAUCION: La moneda se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no te vuelva a suceder, primero debes establecer la moneda y luego agregar las partidas.");
			  }
		  }
		  m_ID_Moneda = idmoneda;
		  m_TC = tc;

		  if( Integer.parseInt(request.getParameter("numero")) != m_Numero)
		  {
			  JProveeProveeSetV2 set = new JProveeProveeSetV2(request);
			  set.m_Where = "ID_Tipo = 'PR' and ID_EntidadCompra = '" + JUtil.getSesion(request).getSesion(m_IdMod).getEspecial() + "' and Numero = '" + JUtil.p(request.getParameter("numero")) + "'";
			  set.Open();
			  if(set.getNumRows() < 1 )
			  {
				  res = 1; 
				  sb_mensaje.append("PRECAUCION: El proveedor no existe en esta entidad. Selecciona otra entidad para poder ingresar el proveedor<br>");
				  return res;
			  }
				      
			  JProveeProveeMasSetV2 mas = new JProveeProveeMasSetV2(request);
			  mas.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + set.getAbsRow(0).getClave() + "'";
			  mas.Open();
				       
			  if(m_Partidas.size()  > 0)
			  {
				  super.resetear();
				  res = 1;
				  sb_mensaje.append("PRECAUCION: El proveedor se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no le vuelva a suceder, primero debe escoger el número del proveedor y luego agregar las partidas.");
			  }
			      
			  m_Clave = set.getAbsRow(0).getClave();
			  m_Numero = set.getAbsRow(0).getNumero();
			  m_Nombre = set.getAbsRow(0).getNombre();
			  m_Direccion = mas.getAbsRow(0).getDireccion();
			  m_Colonia = mas.getAbsRow(0).getColonia();
			  m_Poblacion = mas.getAbsRow(0).getPoblacion();
			  m_CP = mas.getAbsRow(0).getCP();
			  m_Tels = set.getAbsRow(0).getTel();
			  m_RFC = mas.getAbsRow(0).getRFC();
		  }
			      
		  return res;
	}
	
	@SuppressWarnings("unchecked")
	public short agregaPartida(HttpServletRequest request, float cantidad, String idprod, String precio, String descuento, 
				String iva, String ieps, String ivaret, String isrret, String obs_partida, StringBuffer mensaje) 
	{
		short res = -1;
		 
		JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
		if(!m_IdMod.equals("COMP_GAS"))
			set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and ID_Tipo = 'P' and SeProduce = '0' and Status = 'V'";
		else
			set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and ID_Tipo = 'G' and Status = 'V'";
		set.Open();
		  
		if( set.getNumRows() > 0 )
		{
			m_part_Clave = set.getAbsRow(0).getClave();
			m_part_Unidad = set.getAbsRow(0).getID_Unidad();
			m_part_Descripcion = set.getAbsRow(0).getDescripcion();
			m_part_Precio = JUtil.redondear( (set.getAbsRow(0).getUltimoCosto() / m_TC), 2);
			m_part_ID_Tipo = set.getAbsRow(0).getID_Tipo();
			m_part_IVA = set.getAbsRow(0).getIVA() ? m_IVAPorcentual : 0.0F ;
			m_part_IEPS = set.getAbsRow(0).getImpIEPS();
			m_part_IVARet = set.getAbsRow(0).getImpIVARet();
			m_part_ISRRet = set.getAbsRow(0).getImpISRRet();
			 
			 
			DatosXProvee(request);
			 
			// Ahora hace las comparaciones.
			float fprecio, fdescuento, fiva, fieps, fivaret, fisrret, fimporte, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart;
				
			if(precio.equals(""))
				fprecio = m_part_PrecioProvee;
			else
				fprecio = Float.parseFloat(precio);
			 
			if(descuento.equals(""))
				fdescuento = m_part_Descuento; 
			else
				fdescuento = Float.parseFloat(descuento);
			 
			if(iva.equals(""))
				fiva = m_part_IVA; 
			else
			{
				if(m_IVAPorcentual == 0.0)
					fiva = 0.0F;
				else
					fiva = Float.parseFloat(iva);
			}
			if(ieps.equals(""))
				fieps = m_part_IEPS; 
			else
				fieps = Float.parseFloat(ieps);
			if(ivaret.equals(""))
				fivaret = m_part_IVARet; 
			else
				fivaret = Float.parseFloat(ivaret);
			if(isrret.equals(""))
				fisrret = m_part_ISRRet; 
			else
				fisrret = Float.parseFloat(isrret);
			
			 
			fimporte = JUtil.redondear(fprecio * cantidad, 2);
			fimportedesc = (fdescuento != 0.0) ? ((fimporte * fdescuento) / 100.0F) : 0.0F;
			fimporteiva = (fiva != 0.0) ? (((fimporte - fimportedesc) * fiva) / 100.0F) : 0.0F;
			fimporteieps = (fieps != 0.0) ? (((fimporte - fimportedesc) * fieps) / 100.0F) : 0.0F;
			fimporteivaret = (fivaret != 0.0) ? (((fimporte - fimportedesc) * fivaret) / 100.0F) : 0.0F;
			fimporteisrret = (fisrret != 0.0) ? (((fimporte - fimportedesc) * fisrret) / 100.0F) : 0.0F;
			ftotalpart = (((fimporte - fimportedesc) + (fimporteiva + fimporteieps)) - (fimporteivaret + fimporteisrret));
			fimportedesc = JUtil.redondear(fimportedesc, 4);
			fimporteiva = JUtil.redondear(fimporteiva, 4);
			fimporteieps = JUtil.redondear(fimporteieps, 4);
			fimporteivaret = JUtil.redondear(fimporteivaret, 4);
			fimporteisrret = JUtil.redondear(fimporteisrret, 4);
			ftotalpart = JUtil.redondear(ftotalpart, 4);
			 
			if(cantidad < 0.0 || fprecio < 0.0 || fdescuento < 0.0 || fiva < 0.0 || fieps < 0.0 || fivaret < 0.0 || fisrret < 0.0)
			{
				res = 1;
				mensaje.append("PRECAUCION: La cantidad, precio, iva, ieps. retención de iva, de isr, o el descuento, no son correctas, No se cambió la partida");	
			}
			else
			{
				if(m_part_ID_Tipo.equals("G") && fprecio > set.getAbsRow(0).getPrecioMax())
				{
					res = 1;
					mensaje.append("PRECAUCION: El precio de este tipo de insumo o servicio, parece ser mayor al máximo permitido. No se puede agregar el gasto");
				}
				else
				{
					JVenFactSesPart part = new JVenFactSesPart(cantidad, m_part_Unidad, m_part_Clave, m_part_Clave, m_part_Descripcion, fprecio, fimporte, 
						fdescuento, fiva, fieps, fivaret, fisrret, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, obs_partida, m_part_ID_Tipo);
					m_Partidas.addElement(part);
					establecerResultados();
					resetearPart();
				}
			}

		}
		else
		{
			resetearPart();
			 
			res = 3;
			mensaje.append("ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio");
		}

		return res;
	}
	
	public short editaPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, String precio, String descuento, 
			String iva, String ieps, String ivaret, String isrret, String obs_partida, StringBuffer mensaje) 
	{
		short res = -1;
		  
		JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
		if(!m_IdMod.equals("COMP_GAS"))
			set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and ID_Tipo = 'P' and SeProduce = '0' and Status = 'V'";
		else
			set.m_Where = "Clave = '" + JUtil.p(idprod) + "' and ID_Tipo = 'G' and Status = 'V'";
	
		set.Open();
		  
		if( set.getNumRows() > 0 )
		{
			m_part_Clave = set.getAbsRow(0).getClave();
			m_part_Unidad = set.getAbsRow(0).getID_Unidad();
			m_part_Descripcion = set.getAbsRow(0).getDescripcion();
			m_part_Precio = JUtil.redondear( (set.getAbsRow(0).getUltimoCosto() / m_TC), 2);
			m_part_ID_Tipo = set.getAbsRow(0).getID_Tipo();
			m_part_IVA = set.getAbsRow(0).getIVA() ? m_IVAPorcentual : 0.0F;
			m_part_IEPS = set.getAbsRow(0).getImpIEPS();
			m_part_IVARet = set.getAbsRow(0).getImpIVARet();
			m_part_ISRRet = set.getAbsRow(0).getImpISRRet();
			  
			//DatosXProvee(request);
			 
			// Ahora hace las comparaciones.
			float fprecio, fdescuento, fiva, fieps, fivaret, fisrret, fimporte, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart;
			 	
			if(precio.equals(""))
				fprecio = m_part_Precio; //m_part_PrecioProvee; 
			else
				fprecio = Float.parseFloat(precio);
			 
			if(descuento.equals(""))
				fdescuento = m_part_Descuento; 
			else
				fdescuento = Float.parseFloat(descuento);
			 
			if(iva.equals(""))
				fiva = m_part_IVA; 
			else
			{
				if(m_IVAPorcentual == 0.0)
					fiva = 0.0F;
				else
					fiva = Float.parseFloat(iva);
			}
			if(ieps.equals(""))
				fieps = m_part_IEPS; 
			else
				fieps = Float.parseFloat(ieps);
			if(ivaret.equals(""))
				fivaret = m_part_IVARet; 
			else
				fivaret = Float.parseFloat(ivaret);
			if(isrret.equals(""))
				fisrret = m_part_ISRRet; 
			else
				fisrret = Float.parseFloat(isrret);
			
			fimporte = JUtil.redondear(fprecio * cantidad, 2);
			fimportedesc = (fdescuento != 0.0) ? ((fimporte * fdescuento) / 100.0F) : 0.0F;
			fimporteiva = (fiva != 0.0) ? (((fimporte - fimportedesc) * fiva) / 100.0F) : 0.0F;
			fimporteieps = (fieps != 0.0) ? (((fimporte - fimportedesc) * fieps) / 100.0F) : 0.0F;
			fimporteivaret = (fivaret != 0.0) ? (((fimporte - fimportedesc) * fivaret) / 100.0F) : 0.0F;
			fimporteisrret = (fisrret != 0.0) ? (((fimporte - fimportedesc) * fisrret) / 100.0F) : 0.0F;
			ftotalpart = (((fimporte - fimportedesc) + (fimporteiva + fimporteieps)) - (fimporteivaret + fimporteisrret));
			fimportedesc = JUtil.redondear(fimportedesc, 4);
			fimporteiva = JUtil.redondear(fimporteiva, 4);
			fimporteieps = JUtil.redondear(fimporteieps, 4);
			fimporteivaret = JUtil.redondear(fimporteivaret, 4);
			fimporteisrret = JUtil.redondear(fimporteisrret, 4);
			ftotalpart = JUtil.redondear(ftotalpart, 4);
			 
			if(cantidad < 0.0 || fprecio < 0.0 || fdescuento < 0.0 || fiva < 0.0 || fieps < 0.0 || fivaret < 0.0 || fisrret < 0.0)
			{
				res = 1;
				mensaje.append("PRECAUCION: La cantidad, precio, iva, ieps. retención de iva, de isr, o el descuento, no son correctas, No se cambió la partida");
			}
			else
			{
				// Aqui cambia la partida
				JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(indPartida);
				part.setPartida(cantidad, m_part_Unidad, m_part_Clave, m_part_Clave, m_part_Descripcion, fprecio, fimporte, 
						fdescuento, fiva, fieps, fivaret, fisrret, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, obs_partida, m_part_ID_Tipo);
				 
				establecerResultados();
				resetearPart();
			}

		}
		else
		{
			resetearPart();
			 
			res = 3;
			mensaje.append("ERROR: No se encontró el producto especificado, ó la clave pertenece a un servicio. No se pudo cambiar la partida.");
		}

		return res;
	}
	  
	public void DatosXProvee(HttpServletRequest request)
	{
	  	JComprasInvServDatXProSetV2 set = new JComprasInvServDatXProSetV2(request);
	  	set.m_Where = "ID_Provee = '" + m_Clave + "' and ID_Prod = '" + JUtil.p(m_part_Clave) + "' and Moneda = '" + m_ID_Moneda + "'";
	  	set.Open();

	  	if(set.getNumRows() > 0)
	  		m_part_PrecioProvee = set.getAbsRow(0).getPrecio();
	  	else
	  		m_part_PrecioProvee = m_part_Precio;
	  		
	  	JPublicProveeSetV2 setPro = new JPublicProveeSetV2(request);
	  	
	  	setPro.m_Where = "ID_Tipo = 'PR' and ID_Clave = '" + m_Clave + "'";
	  	setPro.Open();
	  	
	  	if(setPro.getNumRows() > 0)
	  		m_part_Descuento = setPro.getAbsRow(0).getDescuento();
	  	else
	  		m_part_Descuento = 0.0F;
	}
   
	
	 
}
