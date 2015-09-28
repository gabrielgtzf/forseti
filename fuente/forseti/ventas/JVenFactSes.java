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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

import forseti.sets.*;
import forseti.*;



public class JVenFactSes extends JSesionRegsObjs
{
  protected int m_Clave;
  protected int m_Numero;
  protected String m_Nombre;
  protected String m_Direccion;
  protected String m_Colonia;
  protected String m_Poblacion;
  protected String m_CP;
  protected String m_Tels;
  protected String m_RFC;
  protected String m_UUID;
  protected float m_TotalUUIDs;

  protected String m_IdMod;
  protected byte m_ID_Entidad;
  protected long m_FactNum;
  protected String m_Referencia;
  protected boolean m_PagoMixto;
  protected String m_Forma_Pago;
  protected String m_Obs;
  protected byte m_ID_Moneda;
  protected String m_Moneda;
  protected float m_TC;
  protected Date m_Fecha; 
  protected Date m_FechaEntrega;
  protected int m_ID_Bodega;
  protected String m_BodegaDesc;
  protected float m_IVAPorcentual;
  protected boolean m_DesgloseMOSTR;
  protected boolean m_DesgloseCLIENT;
  protected String m_NotaPrecio;
  protected boolean m_MostrAplicaPolitica;
  protected boolean m_AuditarAlm;
  protected byte m_ManejoStocks;
  protected boolean m_FijaCost;
  protected boolean m_CambioNumero;
  protected byte m_AjusteDePrecio;
  protected float m_FactorDeAjuste;
  protected boolean m_PrecioEspMostr;
  protected int m_ID_Vendedor;
  protected String m_VendedorNombre;
  protected float m_Importe;
  protected float m_Descuento;
  protected float m_SubTotal;
  protected float m_IVA;
  protected float m_IEPS;
  protected float m_IVARet;
  protected float m_ISRRet;
  protected float m_Total;
  
  protected float m_part_Precio;
  protected float m_part_PrecioClient;
  protected float m_part_PrecioProvee;
  protected String m_part_Descripcion;
  protected String m_part_Clave;
  protected String m_part_Unidad;
  protected float m_part_IVA;
  protected float m_part_IEPS;
  protected float m_part_IVARet;
  protected float m_part_ISRRet;
  protected float m_part_Descuento;
  protected String m_part_ID_Tipo;
  protected String m_DevReb;
  
  public JVenFactSes()
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
  
  
  public JVenFactSes(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)
  {
	  JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request, usuario, ID_Entidad);
	  set.Open();
	  m_ID_Entidad = Byte.parseByte(ID_Entidad);
	  if(tipomov.equals("FACTURAS"))
	  {
		  m_IdMod = "VEN_FAC";
	   	  m_FactNum = set.getAbsRow(0).getDoc();
	  }
	  else if(tipomov.equals("PEDIDOS"))
	  {
		  m_IdMod = "VEN_PED";
	   	  m_FactNum = set.getAbsRow(0).getPedido();
	  }
	  else if(tipomov.equals("REMISIONES"))
	  {
		  m_IdMod = "VEN_REM";
	   	  m_FactNum = set.getAbsRow(0).getRemision();
	  }
	  else if(tipomov.equals("COTIZACIONES"))
	  {
		  m_IdMod = "VEN_COT";
	   	  m_FactNum = set.getAbsRow(0).getCotizacion();
	  }
	  else
	  {
		  m_IdMod = "VEN_DEV";
	   	  m_FactNum = set.getAbsRow(0).getDevolucion();
	  }
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_FechaEntrega = fecha.getTime();
	  m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
	  m_BodegaDesc = set.getAbsRow(0).getBodega();
	  m_IVAPorcentual = set.getAbsRow(0).getIVA();
	  m_DesgloseMOSTR = set.getAbsRow(0).getDesgloseMOSTR();
	  m_DesgloseCLIENT = set.getAbsRow(0).getDesgloseCLIENT();
	  if(m_DesgloseMOSTR)
		  m_NotaPrecio = "A este precio de Mostrador se le desglosará el iva";
	  else
		  m_NotaPrecio = "A este precio de Mostrador se le aumentará el iva";
	  
	  m_MostrAplicaPolitica = set.getAbsRow(0).getMostrAplicaPolitica();
	  m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
	  m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
	  m_FijaCost = set.getAbsRow(0).getFijaCost();
	  m_PagoMixto = set.getAbsRow(0).getTipoCobro() == 2 ? true : false; 
	  if(set.getAbsRow(0).getTipoCobro() == 0)
		  m_Forma_Pago = "contado";
	  else if(set.getAbsRow(0).getTipoCobro() == 1)
		  m_Forma_Pago = "credito";
	  else
		  m_Forma_Pago = "contado";
	  m_CambioNumero = set.getAbsRow(0).getCambioNumero();
	  m_AjusteDePrecio = set.getAbsRow(0).getAjusteDePrecio();
	  m_FactorDeAjuste = set.getAbsRow(0).getFactorDeAjuste();
	  m_ID_Vendedor = set.getAbsRow(0).getID_Vendedor();
	  m_VendedorNombre = set.getAbsRow(0).getVendedorNombre();;
	  m_PrecioEspMostr = false;
	  m_ID_Moneda = 1;
	  m_TC = 1.0F;
	  m_Referencia = "";
	  m_Obs = "";
	  m_DevReb = "VEN";

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
	  m_TotalUUIDs = 0.0F;
	  
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
  
  public JVenFactSesPart getPartida(int ind)
  {
    return (JVenFactSesPart)m_Partidas.elementAt(ind);
  }
  
  @SuppressWarnings("unchecked")
  public void agregaCFDI(JFacturasXML compfactxml) 
  {
	  m_Objetos.addElement(compfactxml);
  }
	
  public JFacturasXML obtenCFDI(int ind)
  {
	  return (JFacturasXML)m_Objetos.elementAt(ind);
  }
	
  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
    establecerResultados();
  }
  
  public short editaPartida(int indPartida, HttpServletRequest request, float cantidad, String idprod, String precio, String descuento, 
		  String iva, String obs_partida, StringBuffer mensaje) 
  {
	  short res = -1;
	  
	  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	  set.m_Where = "ID_Tipo <> 'G' and (Clave = '" + JUtil.p(idprod) + "' or Codigo = '" + JUtil.p(idprod) + "') and NoSeVende = '0' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 m_part_Clave = set.getAbsRow(0).getClave();
		 m_part_Unidad = set.getAbsRow(0).getID_Unidad();
		 m_part_Descripcion = set.getAbsRow(0).getDescripcion();
		 m_part_ID_Tipo = set.getAbsRow(0).getID_Tipo();
		 m_part_IVA = set.getAbsRow(0).getIVA() ? m_IVAPorcentual : 0.0F ;
		 m_part_IEPS = set.getAbsRow(0).getImpIEPS();
		 m_part_IVARet = set.getAbsRow(0).getImpIVARet();
		 m_part_ISRRet = set.getAbsRow(0).getImpISRRet();
		 
		 // Ahora hace las comparaciones.
		 float avprecio, fprecio, fdescuento, fiva, fieps, fivaret, fisrret, fimporte, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart;
		 
		 if(m_part_ID_Tipo.equals("P"))
		 {
			 DatosXClient(request, set, cantidad);
			 if(m_MostrAplicaPolitica)
				 avprecio = m_part_PrecioClient;
			 else
			 {
				 if(precio.equals(""))
					 avprecio = m_part_PrecioClient; 
				 else
					 avprecio = Float.parseFloat(precio);
			 }
		 }
		 else // es servivio, es mas facil el precio
		 {
			 if(precio.equals(""))
		 		 avprecio = set.getAbsRow(0).getPrecio(); 
		 	 else
		 		 avprecio = Float.parseFloat(precio);
		 }
		 
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
		
		 fieps = m_part_IEPS; 
		 fivaret = m_part_IVARet; 
		 fisrret = m_part_ISRRet; 
		  
		 if(m_Clave == 0)
		 {
			 if(m_DesgloseMOSTR)
			 {
				 if(fiva != 0.0)
				 {
					//MessageBox("Desglose Mostr IVA != 0");
					fprecio = JUtil.redondear( (avprecio / ((m_part_IVA / 100) + 1)), 4) ;
				 }
				 else
				 {
					//MessageBox("Desglose Mostr IVA == 0");
					fprecio = avprecio;
				 }
			 }
			else
			{
				//MessageBox("NO DESGLOSADO");
				fprecio = avprecio;
			}
		 }
		 else
		 {
			if(m_DesgloseCLIENT)
			{
				if(fiva != 0.0)
				{
					//MessageBox("Desglose Mostr IVA != 0");
					fprecio = JUtil.redondear( (avprecio / ((fiva / 100) + 1)), 4);
				}
				else
				{
					//MessageBox("Desglose Mostr IVA == 0");
					fprecio = avprecio;
				}
			}
			else
			{
				//MessageBox("NO DESGLOSADO");
				fprecio = avprecio;
			}
		 }

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
			 if(m_part_ID_Tipo.equals("P") && (fprecio < set.getAbsRow(0).getPrecioMin() || fprecio > set.getAbsRow(0).getPrecioMax()) )
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: El precio para este producto, parece ser menor al mínimo ó mayor al máximo. No se puede agregar el producto");
			 }
			 else
			 {
				 // Aqui aplica la partida
				 JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(indPartida);
				 part.setPartida(cantidad, m_part_Unidad, m_part_Clave, m_part_Clave, m_part_Descripcion, fprecio, fimporte, 
					 fdescuento, fiva, fieps, fivaret, fisrret, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart, obs_partida, m_part_ID_Tipo);

				 establecerResultados();
				 resetearPart();
			 }
		 }

	  }
	  else
	  {
		 resetearPart();
		 
	     res = 3;
	     mensaje.append("ERROR: No se encontró el producto o servicio especificado");
	  }

	  return res;
  }
  
   
  @SuppressWarnings("unchecked")
  public void agregaPartida(float cantidad, String unidad, String idprod, String idprodant, String descripcion, float precio, float importe, float descuento, float iva, float ieps, float ivaret, float isrret, float importedesc, float importeiva, float importeieps, float importeivaret, float importeisrret, float totalpart, String obs_partida, String idtipo) 
  {
	    // Aqui aplica la partida
		JVenFactSesPart part = new JVenFactSesPart(cantidad, unidad, idprod, idprodant, descripcion, precio, importe, 
						 descuento, iva, ieps, ivaret, isrret, importedesc, importeiva, importeieps, importeivaret, importeisrret, totalpart, obs_partida, idtipo);
		m_Partidas.addElement(part);
  }
  
   
  @SuppressWarnings("unchecked")
  public short agregaPartida(HttpServletRequest request, float cantidad, String idprod, String precio, String descuento, 
		  String iva, String obs_partida, StringBuffer mensaje) 
  {
	  short res = -1;
	  
	  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	  set.m_Where = "ID_Tipo <> 'G' and (Clave = '" + JUtil.p(idprod) + "' or Codigo = '" + JUtil.p(idprod) + "') and NoSeVende = '0' and Status = 'V'";
	  set.Open();
	  
	  if( set.getNumRows() > 0 )
	  {
		 m_part_Clave = set.getAbsRow(0).getClave();
		 m_part_Unidad = set.getAbsRow(0).getID_Unidad();
		 m_part_Descripcion = set.getAbsRow(0).getDescripcion();
		 m_part_ID_Tipo = set.getAbsRow(0).getID_Tipo();
		 m_part_IVA = set.getAbsRow(0).getIVA() ? m_IVAPorcentual : 0.0F ;
		 m_part_IEPS = set.getAbsRow(0).getImpIEPS();
		 m_part_IVARet = set.getAbsRow(0).getImpIVARet();
		 m_part_ISRRet = set.getAbsRow(0).getImpISRRet();
		 
		 // Ahora hace las comparaciones.
		 float avprecio, fprecio, fdescuento, fiva, fieps, fivaret, fisrret, fimporte, fimportedesc, fimporteiva, fimporteieps, fimporteivaret, fimporteisrret, ftotalpart;
		 		 
		 if(m_part_ID_Tipo.equals("P"))
		 {
			 DatosXClient(request, set, cantidad);
		 	 if(m_MostrAplicaPolitica)
		 		 avprecio = m_part_PrecioClient;
		 	 else
		 	 {
		 		 if(precio.equals(""))
		 			avprecio = m_part_PrecioClient; 
		 		 else
				 	avprecio = Float.parseFloat(precio);
		 	 }
		 }
		 else // es servivio, es mas facil el precio
		 {
			 if(precio.equals(""))
		 		 avprecio = set.getAbsRow(0).getPrecio(); 
		 	 else
		 		 avprecio = Float.parseFloat(precio);
		 }
		 
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
		 
		 fieps = m_part_IEPS; 
		 fivaret = m_part_IVARet; 
		 fisrret = m_part_ISRRet; 
		  
		 if(m_Clave == 0)
		 {
			 if(m_DesgloseMOSTR)
			 {
				 if(fiva != 0.0)
				 {
					//MessageBox("Desglose Mostr IVA != 0");
					fprecio = JUtil.redondear( (avprecio / ((m_part_IVA / 100) + 1)), 4) ;
				 }
				 else
				 {
					//MessageBox("Desglose Mostr IVA == 0");
					fprecio = avprecio;
				 }
			 }
			 else
			 {
				//MessageBox("NO DESGLOSADO");
				fprecio = avprecio;
			 }
		 }
		 else
		 {
			if(m_DesgloseCLIENT)
			{
				if(fiva != 0.0)
				{
					//MessageBox("Desglose Mostr IVA != 0");
					fprecio = JUtil.redondear( (avprecio / ((fiva / 100) + 1)), 4);
				}
				else
				{
					//MessageBox("Desglose Mostr IVA == 0");
					fprecio = avprecio;
				}
			}
			else
			{
				//MessageBox("NO DESGLOSADO");
				fprecio = avprecio;
			}
		 }
	
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
			 if(m_part_ID_Tipo.equals("P") && (fprecio < set.getAbsRow(0).getPrecioMin() || fprecio > set.getAbsRow(0).getPrecioMax()) )
			 {
				 res = 1;
				 mensaje.append("PRECAUCION: El precio para este producto, parece ser menor al mínimo ó mayor al máximo. No se puede agregar el producto");
			 }
			 else
			 {
				 // Aqui aplica la partida
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
	     mensaje.append("ERROR: No se encontró el producto o servicio especificado");
	  }

	  return res;
  }
  
  public void establecerResultados()
  {

    float m_parImporte = 0.0F;
    float m_parDescuento = 0.0F;
    float m_parSubTotal = 0.0F;
    float m_parIVA = 0.0F;
    float m_parIEPS = 0.0F;
    float m_parIVARet = 0.0F;
    float m_parISRRet = 0.0F;
    float m_parTotal = 0.0F;
  
	for(int i = 0; i < m_Partidas.size(); i++)
    {
    	JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
		
    	float parImporte = part.getImporte();
		float parDescuento = (part.getDescuento() != 0.0) ? ((part.getImporte() * part.getDescuento()) / 100.0F) : 0.0F;
		float parSubTotal = (parImporte - parDescuento); 
		float parIVA = (part.getIVA() != 0.0) ? ((parSubTotal * part.getIVA()) / 100.0F) : 0.0F;
		float parIEPS = (part.getIEPS() != 0.0) ? ((parSubTotal * part.getIEPS()) / 100.0F) : 0.0F;
		float parIVARet = (part.getIVARet() != 0.0) ? ((parSubTotal * part.getIVARet()) / 100.0F) : 0.0F;
		float parISRRet = (part.getISRRet() != 0.0) ? ((parSubTotal * part.getISRRet()) / 100.0F) : 0.0F;
		
		float parTotal = ((parSubTotal + parIVA + parIEPS) - (parIVARet + parISRRet));

		m_parImporte += parImporte;
		m_parDescuento += parDescuento;
		m_parSubTotal += parSubTotal;
		m_parIVA += parIVA;
		m_parIEPS += parIEPS;
		m_parIVARet += parIVARet;
		m_parISRRet += parISRRet;
		m_parTotal += parTotal; 
		
    }
	
	m_Importe = JUtil.redondear(m_parImporte, 2);
	m_Descuento = JUtil.redondear(m_parDescuento, 2);
	m_SubTotal = JUtil.redondear(m_parSubTotal, 2);
	m_IVA = JUtil.redondear(m_parIVA, 2);
	m_IEPS = JUtil.redondear(m_parIEPS, 2);
	m_IVARet = JUtil.redondear(m_parIVARet, 2);
	m_ISRRet = JUtil.redondear(m_parISRRet, 2);
	m_Total = JUtil.redondear(m_parTotal, 2);
	
  }
 
  public void DatosXClient(HttpServletRequest request, JPublicInvServInvCatalogSetV2 _Set, float cantidad)
  {
	boolean PrecioEspEnProd = false, AplDescCli = false, AplDescEnt = false;
	byte NumPolitica = 0, NumDesc = 0, NumDescCli = 0, TipoAplEnt = 0;
	
	float PrecioTentativoCli = 0F, PrecioTentativoEnt = 0F;
	
	
	JVentasPoliticasEntDescSet setEnt = new JVentasPoliticasEntDescSet(request);
	setEnt.m_Where = "ID_Entidad = '" + m_ID_Entidad + "' and Clave = '" + JUtil.p(m_part_Clave) + "'";
	setEnt.Open();
	
	JPoliticasInvServCantPrecioSetV2 setPol = new JPoliticasInvServCantPrecioSetV2(request);
	setPol.m_Where = "ID_Prod = '" + JUtil.p(m_part_Clave) + "'";
	
	JVentasCliVsDesc set = new JVentasCliVsDesc(request);
	set.m_Where = "ID_Client = '" + m_Clave + "' and ID_Prod = '" + JUtil.p(m_part_Clave) + "'";

	JVentasInvServDatXCliSetV2 setcli = new JVentasInvServDatXCliSetV2(request);
 	setcli.m_Where = "ID_Client = '" + m_Clave + "' and ID_Prod = '" + JUtil.p(m_part_Clave) + "' and Moneda = '" + m_ID_Moneda + "'";
 	 	
	//m_cbPrecio.ResetContent();
	
	if(m_Clave != 0 && m_PrecioEspMostr == true) // si es cliente, y este es cliente con precio directo especial abre el set para ver si tiene realmente un precio especial 
	{
		setcli.Open();
		if(setcli.getNumRows() > 0)
			PrecioEspEnProd = true;

	}

	if(m_Clave != 0 && m_PrecioEspMostr == true && PrecioEspEnProd == true) // si es cliente, y este es cliente con precio directo especial EN ESTE PRODUCTO
	{
		m_part_PrecioClient = setcli.getAbsRow(0).getPrecio();
	}
	else
	{
		if(m_MostrAplicaPolitica == true) // si no es cliente con precio especial y si aplica la politica, selecciona el precio de las politicas
		{
			//MessageBox("Mostrador, o cliente sin precio precio especial");
		
			setPol.Open();
			if(setPol.getNumRows() == 0 || cantidad == 0.00)
			{
				//CString strmes;
				//strmes.Format("Count = %d, Cantidad = %.3f",setPol.GetNumRows(),m_Cantidad);
				//MessageBox(strmes);
				// Si no existe la politica de este producto aplica el precio normal
				m_part_PrecioClient = _Set.getAbsRow(0).getPrecio() / m_TC;
				NumPolitica = 1;
			}
			else
			{ 
				//MessageBox("Revisa politica");	
				// Revisa el precioque que se adapte a la politica
				if(cantidad >= setPol.getAbsRow(0).getCantDesde() && cantidad <= setPol.getAbsRow(0).getCantHasta())
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio() / m_TC;
					NumPolitica = 1;
				}
				else if(cantidad >= setPol.getAbsRow(0).getCantDesde2() && cantidad <= setPol.getAbsRow(0).getCantHasta2())
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio2() / m_TC;
					NumPolitica = 2;
				}
				else if(cantidad >= setPol.getAbsRow(0).getCantDesde3() && cantidad <= setPol.getAbsRow(0).getCantHasta3())
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio3() / m_TC;
					NumPolitica = 3;
				}
				else if(cantidad >= setPol.getAbsRow(0).getCantDesde4() && cantidad <= setPol.getAbsRow(0).getCantHasta4())
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio4() / m_TC;
					NumPolitica = 4;
				}
				else if(cantidad >= setPol.getAbsRow(0).getCantDesde5() && cantidad <= setPol.getAbsRow(0).getCantHasta5())
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio5() / m_TC;
					NumPolitica = 5;
				}
				else
				{
					m_part_PrecioClient = _Set.getAbsRow(0).getPrecio() / m_TC;
					NumPolitica = 1;
				}
				
			}
						
		}
		else // si no aplica politica esta entidad y se trata de mostrador o de cliente, simplemente selecciona el precio del catalogo
		{
			m_part_PrecioClient = _Set.getAbsRow(0).getPrecio() / m_TC;
			NumPolitica = 1;
		}

		if(m_Clave != 0) // si aplica cliente, agrega descuento tentativo por cliente si lo tiene 
		{
			//MessageBox("Cliente con precio especial");
			set.Open();
			if(set.getNumRows() > 0)
			{
				float pdc = 0.00F, cdc = 0.00F;
				
				if(NumPolitica == 1 && set.getAbsRow(0).getDescuento() != 0)
				{
					NumDescCli = 1;
					pdc = set.getAbsRow(0).getDescuento();
				}
				else if(NumPolitica == 2 && set.getAbsRow(0).getDescuento2() != 0)
				{
					NumDescCli = 2;
					pdc = set.getAbsRow(0).getDescuento2();
				}
				else if(NumPolitica == 3 && set.getAbsRow(0).getDescuento3() != 0)
				{
					NumDescCli = 3;
					pdc = set.getAbsRow(0).getDescuento3();
				}
				else if(NumPolitica == 4 && set.getAbsRow(0).getDescuento4() != 0)
				{
					NumDescCli = 4;
					pdc = set.getAbsRow(0).getDescuento4();
				}
				else if(NumPolitica == 5 && set.getAbsRow(0).getDescuento5() != 0)
				{
					NumDescCli = 5;
					pdc = set.getAbsRow(0).getDescuento5();
				}
				
				if(NumDescCli != 0)
				{
					cdc = ((m_part_PrecioClient * pdc) / 100);
					PrecioTentativoCli = m_part_PrecioClient - cdc;
					AplDescCli = true;
				}
				
			}
		}
		
		if(setEnt.getNumRows() > 0) // si existe politica de entidad asociada a este producto, agrega descuento tentativo por entidad
		{
			//Verifica si existe esta politica de entidad sobre la politica del producto
			//m_part_PrecioClient = 51.98F;
			
			float pde = 0.00F, cde = 0.00F;
			TipoAplEnt = setEnt.getAbsRow(0).getAplicacion();
			
			if(NumPolitica == 1 && setEnt.getAbsRow(0).getP1() != 0)
			{
				NumDesc = 1;
				pde = setEnt.getAbsRow(0).getP1();
			}	
			else if(NumPolitica == 2 && setEnt.getAbsRow(0).getP2() != 0)
			{
				NumDesc = 2;
				pde = setEnt.getAbsRow(0).getP2();
			}	
			else if(NumPolitica == 3 && setEnt.getAbsRow(0).getP3() != 0)
			{
				NumDesc = 3;
				pde = setEnt.getAbsRow(0).getP3();
			}	
			else if(NumPolitica == 4 && setEnt.getAbsRow(0).getP4() != 0)
			{
				NumDesc = 4;
				pde = setEnt.getAbsRow(0).getP4();
			}	
			else if(NumPolitica == 5 && setEnt.getAbsRow(0).getP5() != 0)
			{
				NumDesc = 5;
				pde = setEnt.getAbsRow(0).getP5();
			}	
			
			if(NumDesc != 0)
			{
				cde = ((m_part_PrecioClient * pde) / 100);
				PrecioTentativoEnt = m_part_PrecioClient - cde;
				AplDescEnt = true;
			}
			
		}
		
		// Ya tiene descuentos tentativos, pasa a verificar si cual aplica
		if(AplDescCli && !AplDescEnt) 
			m_part_PrecioClient = PrecioTentativoCli;
		else if(!AplDescCli && AplDescEnt)
			m_part_PrecioClient = PrecioTentativoEnt;
		else if(AplDescCli && AplDescEnt)
		{
			if(TipoAplEnt == 0) // Aplica el mas bajo
			{
				if(PrecioTentativoCli < PrecioTentativoEnt)
					m_part_PrecioClient = PrecioTentativoCli;
				else
					m_part_PrecioClient = PrecioTentativoEnt;
			}
			else if(TipoAplEnt == 2) //Aplica el mas alto
			{
				if(PrecioTentativoCli > PrecioTentativoEnt)
					m_part_PrecioClient = PrecioTentativoCli;
				else
					m_part_PrecioClient = PrecioTentativoEnt;
			}
			else // 1, es descuento de la entidad sin importar el del cliente sea mayor o menor
				m_part_PrecioClient = PrecioTentativoEnt;
			
		}
		
		if(m_AjusteDePrecio != 0)
		{
			float aj = 0.00F;
			switch(m_AjusteDePrecio)
			{
			case 1: // aumenta o disminuye el precio en lo que diga el factor
				m_part_PrecioClient += m_FactorDeAjuste;
				break;
			case 2:
				aj = ((m_part_PrecioClient * m_FactorDeAjuste) / 100);
				m_part_PrecioClient += aj;
				break;
			}
		} // fin de ajustes

		

	}
	
	//m_cbPrecio.AddString(CUtil::Converts(m_dPrecio,false,2,false,1,false));
	//m_Precio = CUtil::Converts(m_dPrecio,false,2,false,1,false);

	if(m_Clave != 0)	// si se trata de un cliente ( NO ES DE MOSTRADOR ) verifica si tiene descuento general 
						// (Descuento general del cliente puesto como descuento explicito. Este se suma al descuento previo por producto o al 
						// precio especial directo de un cliente)
	{

		JPublicClientSetV2 setCli = new JPublicClientSetV2(request);
		setCli.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + m_Clave + "'";
		setCli.Open();
		if(setCli.getNumRows() > 0)
			m_part_Descuento = setCli.getAbsRow(0).getDescuento();
		else
			m_part_Descuento = 0.0F;
		
		
	}
  	

  }  

  protected void resetearPart()
  {
	  m_part_Precio = 0.0F;
	  m_part_PrecioClient = 0.0F;
	  m_part_PrecioProvee = 0.0F;
	  m_part_Descripcion = "";
	  m_part_Clave = "";
	  m_part_Unidad = "";
	  m_part_IVA = 0.0F;
	  m_part_Descuento = 0.0F;
	  m_part_ID_Tipo = "";
	  
  }
  
  public void resetear(HttpServletRequest request, String ID_Entidad, String usuario, String tipomov)
  {
	  JVentasEntidadesSetIdsV2 set = new JVentasEntidadesSetIdsV2(request, usuario, ID_Entidad);
	  set.Open();

	  m_ID_Entidad = Byte.parseByte(ID_Entidad);
	  if(tipomov.equals("FACTURAS"))
	  {
		  m_IdMod = "VEN_FAC";
	   	  m_FactNum = set.getAbsRow(0).getDoc();
	  }
	  else if(tipomov.equals("PEDIDOS"))
	  {
		  m_IdMod = "VEN_PED";
	   	  m_FactNum = set.getAbsRow(0).getPedido();
	  }
	  else if(tipomov.equals("REMISIONES"))
	  {
		  m_IdMod = "VEN_REM";
	   	  m_FactNum = set.getAbsRow(0).getRemision();
	  }
	  else if(tipomov.equals("COTIZACIONES"))
	  {
		  m_IdMod = "VEN_COT";
	   	  m_FactNum = set.getAbsRow(0).getCotizacion();
	  }
	  else
	  {
		  m_IdMod = "VEN_DEV";
	   	  m_FactNum = set.getAbsRow(0).getDevolucion();
	  }
	  
	  Calendar fecha = GregorianCalendar.getInstance();
	  m_Fecha = fecha.getTime();
	  m_FechaEntrega = fecha.getTime();
	  m_ID_Bodega = set.getAbsRow(0).getID_Bodega();
	  m_BodegaDesc = set.getAbsRow(0).getBodega();
	  m_IVAPorcentual = set.getAbsRow(0).getIVA();
	  m_DesgloseMOSTR = set.getAbsRow(0).getDesgloseMOSTR();
	  m_DesgloseCLIENT = set.getAbsRow(0).getDesgloseCLIENT();
	  if(m_DesgloseMOSTR)
		m_NotaPrecio = "A este precio de Mostrador se le desglosará el iva";
	  else
		m_NotaPrecio = "A este precio de Mostrador se le aumentará el iva";
	  
	  m_MostrAplicaPolitica = set.getAbsRow(0).getMostrAplicaPolitica();
	  m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
	  m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
	  m_FijaCost = set.getAbsRow(0).getFijaCost();
	  m_PagoMixto = set.getAbsRow(0).getTipoCobro() == 2 ? true : false; 
	  if(set.getAbsRow(0).getTipoCobro() == 0)
		  m_Forma_Pago = "contado";
	  else if(set.getAbsRow(0).getTipoCobro() == 1)
		  m_Forma_Pago = "credito";
	  else
		  m_Forma_Pago = "contado";
	  
	  m_CambioNumero = set.getAbsRow(0).getCambioNumero();
	  m_AjusteDePrecio = set.getAbsRow(0).getAjusteDePrecio();
	  m_FactorDeAjuste = set.getAbsRow(0).getFactorDeAjuste();
	  m_ID_Vendedor = set.getAbsRow(0).getID_Vendedor();
	  m_VendedorNombre = set.getAbsRow(0).getVendedorNombre();;
	  m_PrecioEspMostr = false;
	  m_ID_Moneda = 1;
	  m_TC = 1.0F;
	  m_Referencia = "";
	  m_Obs = "";
	  m_DevReb = "VEN";

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
	  m_TotalUUIDs = 0.0F;
	  
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
  
  public void vaciarPart()
  {
	  super.resetear();
  }
 
  public float obtenerImporte(String str, String tipo)
  {
	  if(str == null || str.equals(""))
		  return 0;
	  float res;
	  if(str.matches("\\d+\\.?\\d*:\\d+\\.?\\d*"))
	  {
		  res = -1;//No determinado; 
	  }
	  else if(str.matches("\\d+\\.?\\d*%"))
	  {
		  String por = str.substring(0,(str.length()-1));
		  if(tipo.equals("PORCENTAJE"))
			  res = Float.parseFloat(por);
		  else
			  res = -1;//No determinado
	  }
	  else if(str.matches("\\d+\\.?\\d*"))
	  {
		  if(tipo.equals("PORCENTAJE"))
			  res = -1; //No determinado
		  else
			  res = Float.parseFloat(str); 
	  }	
	  else
		  res = -3; //Error
		
	  return res;
  }  

  public short establecerConcordancia(HttpServletRequest request, StringBuffer sb_mensaje)
  {
		short res = -1; 
		int numIVA = 0, numIEPS = 0, numIVARet = 0, numISRRet = 0;
		int objIVA = 0, objIEPS = 0, objIVARet = 0, objISRRet = 0;
		float sumIVA = 0, sumIEPS = 0, sumIVARet = 0, sumISRRet = 0;
		float sumObjIVA = 0, sumObjIEPS = 0, sumObjIVARet = 0, sumObjISRRet = 0;
		
		for(int i = 0; i < m_Partidas.size(); i++)
		{
			JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
			
			//Primero revisa por la cantidad y precio. Si son diferentes al de la partida (solo en caso de COMPRAS O GASTOS) se adaptará a las nuevas cantidades.
			String cantidad = request.getParameter("cantidad_" + i);
			String precio = request.getParameter("precio_" + i);
			if(cantidad != null && precio != null)
			{
				float fcantidad = obtenerImporte(cantidad, "CANTIDAD");
				float fprecio = obtenerImporte(precio, "CANTIDAD");
				
				if(fcantidad < 0 || fprecio < 0)
				{
					res = 3;
					sb_mensaje.append("ERROR: Partida " + (i+1) + ": La cantidad o el precio esta mal. Este debe ser un importe mayor a cero. Se restableció a como estaba antes<br>");
				}
				else
				{
					part.setCantidad(fcantidad);
					part.setPrecio(fprecio);
					part.setImporte(JUtil.redondear(fprecio * fcantidad, 2));
				}
			}
			
			if(!part.getID_Prod().equals(part.getID_ProdAnt())) // si ha cambiado el producto, lo carga desde la base de datos
			{
				JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
				//set.Open();
				if(m_IdMod.equals("COMP_GAS"))
					set.m_Where = "Clave = '" + JUtil.p(part.getID_Prod()) + "' and ID_Tipo = 'G' and Status = 'V'";
				else if(m_IdMod.equals("COMP_FAC") || m_IdMod.equals("COMP_REC"))
					set.m_Where = "Clave = '" + JUtil.p(part.getID_Prod()) + "' and ID_Tipo = 'P' and SeProduce = '0' and Status = 'V'";
				else // m_IdMod igual a calquier venta (Remision o Factura)
					set.m_Where = "(Clave = '" + JUtil.p(part.getID_Prod()) + "' or Codigo = '" + JUtil.p(part.getID_Prod()) + "') and NoSeVende = '0' and Status = 'V'";
				 
				set.Open();
			
				if(set.getNumRows() < 1)
				{
					res = 3;
					sb_mensaje.append("ERROR: Partida " + (i+1) + ": La clave esta vacía, no existe en el catálogo de productos o gastos, esta descontinuada, o es un producto de fabricación<br>");
		    	}
				else // establece resultados de la partida
				{
					part.setID_ProdNombre(set.getAbsRow(0).getDescripcion());
					part.setUnidad(set.getAbsRow(0).getID_Unidad());
					part.setID_Tipo(set.getAbsRow(0).getID_Tipo());
					part.setIVA(set.getAbsRow(0).getIVA() ? m_IVAPorcentual : 0.0F);
					part.setIEPS(set.getAbsRow(0).getImpIEPS());
					part.setIVARet(set.getAbsRow(0).getImpIVARet());
					part.setISRRet(set.getAbsRow(0).getImpISRRet());
					//sb_mensaje.append("Porcentajes " + i+1 + ": IVA: " + part.getIVA() + " IEPS: " + part.getIEPS() + " IVARet: " + part.getIVARet() + " ISRRet: " + part.getISRRet() + "<br>");
				}
			}
			else // la clave no ha cambiado, revisa si han cambiado los importes o porcentajes de IVA, IEPS, IVARET o ISRRET
			{
				String iva = request.getParameter("iva_" + i);
				String ieps = request.getParameter("ieps_" + i);
				String ivaret = request.getParameter("ivaret_" + i);
				String isrret = request.getParameter("isrret_" + i);
				if(iva != null && ieps != null || ivaret != null || isrret != null) //Existen importes, regularmente por COMPRAS o GASTOS unicamente.
				{
					float fiva, fivaimp, fieps, fiepsimp, fivaret, fivaretimp, fisrret, fisrretimp;
				 		
					fiva = obtenerImporte(iva, "PORCENTAJE");
					fivaimp = obtenerImporte(iva, "CANTIDAD");
					fieps = obtenerImporte(ieps, "PORCENTAJE");
					fiepsimp = obtenerImporte(ieps, "CANTIDAD");
					fivaret = obtenerImporte(ivaret, "PORCENTAJE");
					fivaretimp = obtenerImporte(ivaret, "CANTIDAD");
					fisrret = obtenerImporte(isrret, "PORCENTAJE");
					fisrretimp = obtenerImporte(isrret, "CANTIDAD");
					
					if(fiva == -3 || fivaimp == -3 || fieps == -3 || fiepsimp == -3 ||
							fivaret == -3 || fivaretimp == -3 || fisrret == -3 || fisrretimp == -3)
					{
						res = 3;
						sb_mensaje.append("ERROR: Partida " + (i+1) + ": El iva, ieps, iva retenido o isr retenido esta mal. Este debe ser porcentaje:importe, porcentaje% o importe. Se restableció a como estaba antes<br>");
					}
					else
					{
						if(fiva == -1 && fivaimp == -1) //No determinados, permanecen igual
						{
							fiva = part.getIVA();
							fivaimp = part.getImporteIVA();
						}
						else if(fiva == -1) //Importe determinado... Calcula la tasa segun importe total menos descuento
							fiva = (( fivaimp / (part.getImporte() - part.getImporteDesc()) ) * 100.0F);
						
						if(fieps == -1 && fieps == -1) //No determinados, permanecen igual
						{
							fieps = part.getIEPS();
							fiepsimp = part.getImporteIEPS();
						}
						else if(fieps == -1) //Importe determinado... Calcula la tasa segun importe total menos descuento
							fieps = (( fiepsimp / (part.getImporte() - part.getImporteDesc()) ) * 100.0F);
						
						if(fivaret == -1 && fivaretimp == -1) //No determinados, permanecen igual
						{
							fivaret = part.getIVARet();
							fivaretimp = part.getImporteIVARet();
						}
						else if(fivaret == -1) //Importe determinado... Calcula la tasa segun importe total menos descuento
							fivaret = (( fivaretimp / (part.getImporte() - part.getImporteDesc()) ) * 100.0F);
						
						if(fisrret == -1 && fisrretimp == -1) //No determinados, permanecen igual
						{
							fisrret = part.getISRRet();
							fisrretimp = part.getImporteISRRet();
						}
						else if(fisrret == -1) //Importe determinado... Calcula la tasa segun importe total menos descuento
							fisrret = (( fisrretimp / (part.getImporte() - part.getImporteDesc()) ) * 100.0F);
					
						// Ahora asigna los porcentajes
						part.setIVA(fiva);
						part.setIEPS(fieps);
						part.setIVARet(fivaret);
						part.setISRRet(fisrret);
						
					}	
				}
			}
						
			float parImporte = part.getImporte();
			float parDescuento = (part.getDescuento() != 0.0) ? ((part.getImporte() * part.getDescuento()) / 100.0F) : 0.0F;
			part.setImporteDesc(parDescuento);
			float parSubTotal = (parImporte - parDescuento); 
			float parIVA = (part.getIVA() != 0.0) ? ((parSubTotal * part.getIVA()) / 100.0F) : 0.0F;
			part.setImporteIVA(parIVA);
			float parIEPS = (part.getIEPS() != 0.0) ? ((parSubTotal * part.getIEPS()) / 100.0F) : 0.0F;
			part.setImporteIEPS(parIEPS);
			float parIVARet = (part.getIVARet() != 0.0) ? ((parSubTotal * part.getIVARet()) / 100.0F) : 0.0F;
			part.setImporteIVARet(parIVARet);
			float parISRRet = (part.getISRRet() != 0.0) ? ((parSubTotal * part.getISRRet()) / 100.0F) : 0.0F;
			part.setImporteISRRet(parISRRet);
			float parTotal = ((parSubTotal + parIVA + parIEPS) - (parIVARet + parISRRet));
			part.setTotalPart(parTotal);
			//////////////////////////////////////////////////////////////
			
			boolean fIVA = (parIVA == 0.0 ? true : false); 
			boolean fIEPS = (parIEPS == 0.0 ? true : false); 
			boolean fIVARet = (parIVARet == 0.0 ? true : false);
			boolean fISRRet = (parISRRet == 0.0 ? true : false);
			if(parIVA != 0.0) 
			{
				numIVA++; 
				sumIVA += parIVA;
			}
			if(parIEPS != 0.0) 
			{
				numIEPS++; 
				sumIEPS += parIEPS;
			}
			if(parIVARet != 0.0) 
			{
				numIVARet++; 
				sumIVARet += parIVARet;
			}
			if(parISRRet != 0.0) 
			{
				numISRRet++; 
				sumISRRet += parISRRet;
			}
			
			//sb_mensaje.append("Importes " + i+1 + ": ImpIVA: " + parIVA + " ImpIEPS: " + parIEPS + " ImpIVARet: " + parIVARet + " ImpISRRet: " + parISRRet + " <br>");
			for(int ob = 0; ob < m_Objetos.size(); ob++)
			{
				JFacturasXML compfactxml = (JFacturasXML)m_Objetos.elementAt(ob);
				if(!fIVA)
				{
					//Extrae el IVA 
					for(int j = 0; j < compfactxml.getTraslados().size(); j++)
					{
						Properties traslado = (Properties)compfactxml.getTraslados().elementAt(j);
						String impuesto = traslado.getProperty("impuesto");
						float imp = Float.parseFloat(traslado.getProperty("importe"));
						if(!impuesto.equals("IVA") || imp == 0.00)
							continue;
						//sb_mensaje.append("Partida " + i+1 + ": TRASLADO: " + impuesto + "/" + imp + "<br>");
						if((parIVA - imp) < 0.011F && (parIVA - imp) > -0.011F )
						{
							fIVA = true;
							break;
						}
					}
				}
				if(!fIEPS)
				{
					//Extrae el IEPS
					for(int j = 0; j < compfactxml.getTraslados().size(); j++)
					{
						Properties traslado = (Properties)compfactxml.getTraslados().elementAt(j);
						String impuesto = traslado.getProperty("impuesto");
						float imp = Float.parseFloat(traslado.getProperty("importe"));
						if(!impuesto.equals("IEPS") || imp == 0.00)
							continue;
						//sb_mensaje.append("Partida " + i+1 + ": TRASLADO: " + impuesto + "/" + imp + "<br>");
						if((parIEPS - imp) < 0.011F && (parIEPS - imp) > -0.011F )
						{
							fIEPS = true;
							break;
						}
					}
				}
				if(!fIVARet)
				{
					//Extrae el IVA Retenido
					for(int j = 0; j < compfactxml.getRetenciones().size(); j++)
					{
						Properties retencion = (Properties)compfactxml.getRetenciones().elementAt(j);
						String impuesto = retencion.getProperty("impuesto");
	  			  	  	float imp = Float.parseFloat(retencion.getProperty("importe"));
	  			  	  	if(!impuesto.equals("IVA") || imp == 0.00)
	  			  	  		continue;
	  			  	  	//sb_mensaje.append("Partida " + i+1 + ": RETENCION: " + impuesto + "/" + imp + "<br>");
	  			  		if((parIVARet - imp) < 0.011F && (parIVARet - imp) > -0.011F )
						{
							fIVARet = true;
							break;
						}
					}
				}
				if(!fISRRet)
				{
					//Extrae el ISR Retenido
					for(int j = 0; j < compfactxml.getRetenciones().size(); j++)
					{
						Properties retencion = (Properties)compfactxml.getRetenciones().elementAt(j);
						String impuesto = retencion.getProperty("impuesto");
						float imp = Float.parseFloat(retencion.getProperty("importe"));
						if(!impuesto.equals("ISR") || imp == 0.00)
							continue;
						//sb_mensaje.append("Partida " + i+1 + ": RETENCION: " + impuesto + "/" + imp + "<br>");
						if((parISRRet - imp) < 0.011F && (parISRRet - imp) > -0.011F )
						{
							fIVARet = true;
							break;
						}
					}
		    	}
			} // Fin de analisis de todos los objetos
			if(!fIVA)
				sb_mensaje.append("Partida " + (i+1) + ": IVA sin concordancia en XML (" + JUtil.redondear(parIVA,2) + "). El IVA en el XML no existe, o esta establecido a una tasa diferente<br>");
			if(!fIEPS)
				sb_mensaje.append("Partida " + (i+1) + ": IEPS sin concordancia en XML (" + JUtil.redondear(parIEPS,2) + "). El IEPS en el XML no existe, o esta establecido a una tasa diferente<br>");
			if(!fIVARet)
				sb_mensaje.append("Partida " + (i+1) + ": IVA Retenido sin concordancia en XML (" + JUtil.redondear(parIVARet,2) + "). La retención de IVA en el XML no existe, o esta establecida a una tasa diferente<br>");
			if(!fISRRet)
				sb_mensaje.append("Partida " + (i+1) + ": ISR Retenido sin concordancia en XML (" + JUtil.redondear(parISRRet,2) + "). La retención de ISR en el XML no existe o esta establecida a una tasa diferente<br>");
				
		}
		
		//Ahora los objetos XML seran analizados
		for(int ob = 0; ob < m_Objetos.size(); ob++)
		{
			JFacturasXML compfactxml = (JFacturasXML)m_Objetos.elementAt(ob);
			
			for(int j = 0; j < compfactxml.getTraslados().size(); j++)
			{
				Properties traslado = (Properties)compfactxml.getTraslados().elementAt(j);
				String impuesto = traslado.getProperty("impuesto");
				float tasa = Float.parseFloat(traslado.getProperty("tasa"));
				float imp = Float.parseFloat(traslado.getProperty("importe"));
				if(!impuesto.equals("IVA") || imp == 0.00)
					continue;
				objIVA++;
				sumObjIVA += imp;
				boolean fIVA = false;
				for(int i = 0; i < m_Partidas.size(); i++)
				{
					JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
					//sb_mensaje.append("Partida " + i + ": Traslado IVA " + imp + " Part IVA " + part.getImporteIVA() + "<br>");
					if((imp - part.getImporteIVA()) < 0.011F && (imp - part.getImporteIVA()) > -0.011F )
					{
						fIVA = true;
						break;
					}
				}
				if(!fIVA)
					sb_mensaje.append("Traslado " + objIVA + " IVA en XML: Sin concordancia en Documento (" + JUtil.redondear(tasa,2) + ":" + JUtil.redondear(imp,2) + "). Una clave en el catálogo no esta manejando el IVA, mientras que este XML si lo está haciendo<br>");
			}
			for(int j = 0; j < compfactxml.getTraslados().size(); j++)
			{
				Properties traslado = (Properties)compfactxml.getTraslados().elementAt(j);
				String impuesto = traslado.getProperty("impuesto");
				float tasa = Float.parseFloat(traslado.getProperty("tasa"));
				float imp = Float.parseFloat(traslado.getProperty("importe"));
				if(!impuesto.equals("IEPS") || imp == 0.00)
					continue;
				objIEPS++;
				sumObjIEPS += imp;
				boolean fIEPS = false;
				//sb_mensaje.append("Partida " + ((ob+1)*(j+1)) + ": TRASLADO: " + impuesto + "/" + imp + "<br>");
				for(int i = 0; i < m_Partidas.size(); i++)
				{
					JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
					if((imp - part.getImporteIEPS()) < 0.011F && (imp - part.getImporteIEPS()) > -0.011F )
					{
						fIEPS = true;
						break;
					}
				}
				if(!fIEPS)
					sb_mensaje.append("Traslado " + objIEPS + " IEPS en XML: Sin concordancia en Documento (" + JUtil.redondear(tasa,2) + ":" + JUtil.redondear(imp,2) + "). Una clave en el catálogo no esta manejando el IEPS, mientras que este XML si lo está haciendo<br>");
			}
			for(int j = 0; j < compfactxml.getRetenciones().size(); j++)
			{
				Properties retencion = (Properties)compfactxml.getRetenciones().elementAt(j);
				String impuesto = retencion.getProperty("impuesto");
				float imp = Float.parseFloat(retencion.getProperty("importe"));
				if(!impuesto.equals("IVA") || imp == 0.00)
					continue;
				objIVARet++;
				sumObjIVARet += imp;
				boolean fIVARet = false;
				//sb_mensaje.append("Partida " + ((ob+1)*(j+1)) + ": TRASLADO: " + impuesto + "/" + imp + "<br>");
				for(int i = 0; i < m_Partidas.size(); i++)
				{
					JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
					if((imp - part.getImporteIVARet()) < 0.011F && (imp - part.getImporteIVARet()) > -0.011F )
					{
						fIVARet = true;
						break;
					}
				}
				if(!fIVARet)
					sb_mensaje.append("Retención " + objIVARet + " IVA en XML: Sin concordancia en Documento (" + JUtil.redondear(imp,2) + "). Una clave en el catálogo no esta manejando Retención de IVA, mientras que este XML si lo está haciendo<br>");
			}
			for(int j = 0; j < compfactxml.getRetenciones().size(); j++)
			{
				Properties retencion = (Properties)compfactxml.getRetenciones().elementAt(j);
				String impuesto = retencion.getProperty("impuesto");
				float imp = Float.parseFloat(retencion.getProperty("importe"));
				if(!impuesto.equals("ISR") || imp == 0.00)
					continue;
				objISRRet++;
				sumObjISRRet += imp;
				boolean fISRRet = false;
				//sb_mensaje.append("Partida " + ((ob+1)*(j+1)) + ": TRASLADO: " + impuesto + "/" + imp + "<br>");
				for(int i = 0; i < m_Partidas.size(); i++)
				{
					JVenFactSesPart part = (JVenFactSesPart) m_Partidas.elementAt(i);
					if((imp - part.getImporteISRRet()) < 0.011F && (imp - part.getImporteISRRet()) > -0.011F )
					{
						fISRRet = true;
						break;
					}
				}
				if(!fISRRet)
					sb_mensaje.append("Retención " + objISRRet + " ISR en XML: Sin concordancia en Documento (" + JUtil.redondear(imp,2) + "). Una clave en el catálogo no esta manejando Retención de ISR, mientras que este XML si lo está haciendo<br>");
			}
		}
		
		if(numIVA != objIVA)
			sb_mensaje.append("Importes de IVA en el documento: " + numIVA + " Importes de IVA en el XML: " + objIVA + " Diferencia: " + (numIVA - objIVA) + "<br>");
		if((sumIVA - sumObjIVA) > 0.011F || (sumIVA - sumObjIVA) < -0.011F)
		{
			res = 3;
			sb_mensaje.append("ERROR: Total de IVA en el documento: " + JUtil.redondear(sumIVA,2) + " Total de IVA en el XML: " + JUtil.redondear(sumObjIVA,2) + " Diferencia: " + JUtil.redondear(sumIVA - sumObjIVA,4) + "<br>");
		}
		if(numIEPS != objIEPS)
			sb_mensaje.append("Importes de IEPS en el documento: " + numIEPS + " Importes de IEPS en el XML: " + objIEPS + " Diferencia: " + (numIEPS - objIEPS) + "<br>");
		if((sumIEPS - sumObjIEPS) > 0.011F || (sumIEPS - sumObjIEPS) < -0.011F)
		{
			res = 3;
			sb_mensaje.append("ERROR: Total de IEPS en el documento: " + JUtil.redondear(sumIEPS,2) + " Total de IEPS en el XML: " + JUtil.redondear(sumObjIEPS,2) + " Diferencia: " + JUtil.redondear(sumIEPS - sumObjIEPS,4) + "<br>");
		}
		if(numIVARet != objIVARet)
			sb_mensaje.append("Importes de IVA Retenido en el documento: " + numIVARet + " Importes de IVA Retenido en el XML: " + objIVARet + " Diferencia: " + (numIVARet - objIVARet) + "<br>");
		if((sumIVARet - sumObjIVARet) > 0.011F || (sumIVARet - sumObjIVARet) < -0.011F)
		{
			res = 1;
			sb_mensaje.append("ERROR: Total de IVA Retenido en el documento: " + JUtil.redondear(sumIVARet,2) + " Total de IVA Retenido en el XML: " + JUtil.redondear(sumObjIVARet,2) + " Diferencia: " + JUtil.redondear(sumIVARet - sumObjIVARet,4) + "<br>");
		}
		if(numISRRet != objISRRet)
			sb_mensaje.append("Importes de ISR Retenido en el documento: " + numISRRet + " Importes de ISR Retenido en el XML: " + objISRRet + " Diferencia: " + (numISRRet - objISRRet) + "<br>");
		if((sumISRRet - sumObjISRRet) > 0.011F || (sumISRRet - sumObjISRRet) < -0.011F)
		{
			res = 3;
			sb_mensaje.append("ERROR: Total de ISR Retenido en el documento: " + JUtil.redondear(sumISRRet,2) + " Total de ISR Retenido en el XML: " + JUtil.redondear(sumObjISRRet,2) + " Diferencia: " + JUtil.redondear(sumISRRet - sumObjISRRet,4) + "<br>");
		}
		
		return res;
	}
  
  	public short agregaRecursos(HttpServletRequest request, StringBuffer sb_mensaje) 
		  	throws ServletException, IOException
	{
		  if(request.getParameter("factura") != null && request.getParameter("referencia") != null && 
	          	request.getParameter("tc") != null && request.getParameter("idmoneda") != null && 
	          	request.getParameter("idvendedor") != null &&
	          	!request.getParameter("factura").equals("") && 
	          	!request.getParameter("tc").equals("") && !request.getParameter("idmoneda").equals("") && 
	          	!request.getParameter("idvendedor").equals(""))
	    
		  {
			  m_Referencia = request.getParameter("referencia");
		  }
		  else
		  {
			  sb_mensaje.append("ERROR: Se debe enviar la referencia de la factura asociada<br>");
			  return 3;
		  }
			
		  byte identidad = -1;

		  identidad = Byte.parseByte(JUtil.getSesion(request).getSesion(m_IdMod).getEspecial());
		      
		  if(m_ID_Entidad != identidad)
		  {
			  sb_mensaje.append("ERROR: La entidad de venta ya no es la misma que la inicial <br>");
			  return 3;
		  }
			  
		  int factnum = Integer.parseInt(request.getParameter("factura"));
		  
		  if(factnum != m_FactNum && !m_CambioNumero)
		  {
	         sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la Cotización, Pedido, Remisión ó Factura en esta entidad. Este campo es exclusivamente informativo. Para cambiar la Cotización, Pedido, Resmisión ó Factura, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
	         return 1;
		  }
		  m_FactNum = factnum;
			
		  if(!m_PagoMixto)
		  {
			  if(!request.getParameter("forma_pago").equals(m_Forma_Pago))
			  {
				  sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la forma de pago en una Cotización, Pedido, Resmisión ó Factura. Este campo es exclusivamente informativo. Para cambiar la forma de pago, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
				  return 1;
			  }
		  }
		  m_Forma_Pago = request.getParameter("forma_pago");
		  
		  byte idmoneda = Byte.parseByte(request.getParameter("idmoneda"));
		  float tc = Float.parseFloat(request.getParameter("tc"));

		  if(idmoneda == 1 && tc != 1.0)
		  {
			  sb_mensaje.append("PRECAUCION: El tipo de cambio para la moneda local no puede ser diferente de 1 <br>");
			  return 3;
		  }
		  m_ID_Moneda = idmoneda;
		  m_TC = tc;
			
		  // Verifica el vendedor
	      int idvendedor = Integer.parseInt(request.getParameter("idvendedor"));
	      JVendedoresSet setven  = new JVendedoresSet(request);
	      setven.m_Where = "ID_Vendedor = '" + idvendedor + "'";
	      setven.Open();
	      if(setven.getNumRows() == 0)
	      {
	        sb_mensaje.append("ERROR: El vendedor especificado no existe. Selecciona otro vendedor.<br>");
	        return 3;
	      }
	      m_ID_Vendedor = idvendedor;
	  	  m_VendedorNombre = setven.getAbsRow(0).getNombre();
	  	  
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
			
		  if((m_Total - m_TotalUUIDs) > 0.1F || (m_Total - m_TotalUUIDs) < -0.1)
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
          	request.getParameter("idvendedor") != null &&
          	!request.getParameter("clave").equals("") && !request.getParameter("factura").equals("") && 
          	!request.getParameter("tc").equals("") && !request.getParameter("idmoneda").equals("") && !request.getParameter("fecha").equals("") && 
          	!request.getParameter("entrega").equals("") && !request.getParameter("numero").equals("") &&
          	!request.getParameter("idvendedor").equals(""))
    
      {
    	  m_Referencia = request.getParameter("referencia");
    	  m_Obs = request.getParameter("obs");
    	  m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
    	  m_FechaEntrega = JUtil.estFecha(request.getParameter("entrega"));
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
	      sb_mensaje.append("ERROR: La entidad de venta ya no es la misma que la inicial <br>");
	      return res;
	  }
	  
	  int factnum = Integer.parseInt(request.getParameter("factura"));
	  
	  if(factnum != m_FactNum && !m_CambioNumero)
	  {
         res = 1; 
         sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la Cotización, Pedido, Remisión ó Factura en esta entidad. Este campo es exclusivamente informativo. Para cambiar la Cotización, Pedido, Resmisión ó Factura, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
         return res;
	  }
	  m_FactNum = factnum;
	 
	  if(!m_PagoMixto)
	  {
		  if(!request.getParameter("forma_pago").equals(m_Forma_Pago))
		  {
			  res = 1; 
			  sb_mensaje.append("PRECAUCION: No está permitido cambiar directamente la forma de pago en una Cotización, Pedido, Resmisión ó Factura. Este campo es exclusivamente informativo. Para cambiar la forma de pago, necesitas cambiarla desde los parámetros de la entidad de venta <br>");
			  return res;
		  }
	  }
	  m_Forma_Pago = request.getParameter("forma_pago");
	  
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
      
      // Verifica el vendedor
      int idvendedor = Integer.parseInt(request.getParameter("idvendedor"));
      JVendedoresSet setven  = new JVendedoresSet(request);
      setven.m_Where = "ID_Vendedor = '" + idvendedor + "' and Status = 'A'";
      setven.Open();
      if(setven.getNumRows() == 0)
      {
        res = 3;
        sb_mensaje.append("ERROR: El vendedor especificado no existe, o esta dado de baja temporalmente. Selecciona otro vendedor.<br>");
        return res;
      }
      m_ID_Vendedor = idvendedor;
  	  m_VendedorNombre = setven.getAbsRow(0).getNombre();
  	  
      if( Integer.parseInt(request.getParameter("numero")) != m_Numero)
      {
  	      JClientClientSetV2 set = new JClientClientSetV2(request);
  	      set.m_Where = "ID_Tipo = 'CL' and ID_EntidadVenta = '" + JUtil.getSesion(request).getSesion(m_IdMod).getEspecial() + "' and Numero = '" + JUtil.p(request.getParameter("numero")) + "'";
 	      set.Open();
	      if(set.getNumRows() < 1 )
	      {
	          res = 1; 
	          sb_mensaje.append("PRECAUCION: El cliente no existe en esta entidad. Selecciona otra entidad para poder ingresar el cliente<br>");
	          return res;
	      }
	      
	      JClientClientMasSetV2 mas = new JClientClientMasSetV2(request);
	      mas.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + set.getAbsRow(0).getClave() + "'";
	      mas.Open();
	       
	      if(m_Partidas.size()  > 0)
	      {
	    	  super.resetear();
	     	  res = 1;
	    	  sb_mensaje.append("PRECAUCION: El cliente se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no te vuelva a suceder, primero debes escoger el número del cliente y luego agregar las partidas.<br>");
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
		  m_PrecioEspMostr = mas.getAbsRow(0).getPrecioEspMostr();
      }
      

  	  if(m_Clave == 0)
	  {
  		if(m_DesgloseMOSTR)
			m_NotaPrecio = "A este precio de Mostrador se le desglosará el iva";
		else
			m_NotaPrecio = "A este precio de Mostrador se le aumentará el iva";
	  }
	  else
	  {
		if(m_DesgloseCLIENT)
			m_NotaPrecio = "A este precio de Cliente se le desglosará el iva";
		else
			m_NotaPrecio = "A este precio de Cliente se le aumentará el iva";
	  
		JClientClientMasSetV2 mas = new JClientClientMasSetV2(request);
	    mas.m_Where = "ID_Tipo = 'CL' and ID_Clave = '" + m_Clave + "'";
	    mas.Open();
	    
	    if(idvendedor != mas.getAbsRow(0).getID_Vendedor())
	    {
	        res = 1;
	        sb_mensaje.append("PRECAUCION: El vendedor especificado en la factura no es el mismo vendedor asignado a este cliente. Es necesario cambiarlo<br>");
	        return res;
	    }
	  }
     
  	  
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

  public float getDescuento() 
  {
	return m_Descuento;
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

  public Date getFechaEntrega() 
  {
	return m_FechaEntrega;
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

  public float getImporte() 
  {
	return m_Importe;
  }

  public float getIVA() 
  {
	return m_IVA;
  }

  public float getIEPS() 
  {
	return m_IEPS;
  }
  
  public float getIVARet() 
  {
	return m_IVARet;
  }
  
  public float getISRRet() 
  {
	return m_ISRRet;
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
  
  public String getNotaPrecio() 
  {
	return m_NotaPrecio;
  }

  public long getFactNum() 
  {
	return m_FactNum;
  }

  public String getRFC() 
  {
	return m_RFC;
  }

  public float getSubTotal()
  {
	return m_SubTotal;
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

  public void setDescuento(float descuento) 
  {
	m_Descuento = descuento;
  }

  public void setDireccion(String direccion) 
  {
	m_Direccion = direccion;
  }

  public void setFecha(Date fecha) 
  {
	m_Fecha = fecha;
  }

  public void setFechaEntrega(Date fechaEntrega) 
  {
	m_FechaEntrega = fechaEntrega;
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
  
  public void setIEPS(float m_ieps) 
  {
	m_IEPS = m_ieps;
  }
  
  public void setIVARet(float m_ivaret) 
  {
	m_IVARet = m_ivaret;
  }
  
  public void setISRRet(float m_isrret) 
  {
	m_ISRRet = m_isrret;
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

  public void setFactNum(long FactNum) 
  {
	m_FactNum = FactNum;
  }

  public void setReferencia(String referencia) 
  {
	m_Referencia = referencia;
  }

  public void setRFC(String rfc) 
  {
	m_RFC = rfc;
  }

  public void setSubTotal(float subTotal) 
  {
	m_SubTotal = subTotal;
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

  public int getID_Vendedor() 
  {
	return m_ID_Vendedor;
  }

  public void setID_Vendedor(int Vendedor) 
  {
	m_ID_Vendedor = Vendedor;
  }

  public String getVendedorNombre() 
  {
	return m_VendedorNombre;
  }

  public void setVendedorNombre(String VendedorNombre) 
  {
	m_VendedorNombre = VendedorNombre;
  }

  public String getIdMod() 
  {
	return m_IdMod;
  }
  
  public String getMoneda() 
  {
	return m_Moneda;
  }

  public void setMoneda(String Moneda) 
  {
	m_Moneda = Moneda;
  }
  
  public long getID_Factura()
  {
	  return 0;
  }
  
  public void setID_Factura(long ID_Factura)
  {
	  
  }

  public void setDevReb(String DevReb) 
  {
	m_DevReb = DevReb;
  }
  
  public String getDevReb() 
  {
	return m_DevReb;
  }
  
  public String getUUID()
  {
	  return m_UUID;
  }
	
  public void setUUID(String UUID)
  {
	  m_UUID = UUID;
  }
  
  public float getTotalUUIDs()
  {
	  return m_TotalUUIDs;
  }
	
  public void setTotalUUIDs(float TotalUUIDs)
  {
	  m_TotalUUIDs = TotalUUIDs;
  }
}
