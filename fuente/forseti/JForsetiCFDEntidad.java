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
package forseti;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
//import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import forseti.sets.JAlmacenesMovimSetIdsV2;
import forseti.sets.JCFDNomRecCabGenSet;
import forseti.sets.JCFDVenFactCabGenSet;
import forseti.sets.JCalculoNominaDetSet;
import forseti.sets.JComercioExteriorCabSet;
import forseti.sets.JComercioExteriorDetDescEspSet;
import forseti.sets.JComercioExteriorDetSet;
import forseti.sets.JNominaEntidadesSetIds;
import forseti.sets.JPublicFormatosSetV2;
import forseti.sets.JSatEstadosSet;
import forseti.sets.JVentasEntidadesSetIdsV2;
import forseti.sets.JVentasFactSetDetV2;

public class JForsetiCFDEntidad  extends JForsetiCFDEmisor
{
	public JForsetiCFDEntidad() 
			throws ServletException
	{
		super();
	}
	
	
	public void cancelarCFDI(HttpServletRequest request, HttpServletResponse response, String tipo, int idfact, byte tfd) 
		throws ServletException, IOException
	{
		String nomar;
		
		if(tipo.equals("FACTURAS"))
			nomar = "FAC";
		else if(tipo.equals("REMISIONES"))
			nomar = "REM";
		else if(tipo.equals("DEVOLUCIONES"))
			nomar = "DSV";
		else
			nomar = "TRS";
		
		if(tfd == 2 || tfd == 3) // significa que no se ha hecho nada al registro
		{
			JCFDVenFactCabGenSet set = new JCFDVenFactCabGenSet(request, tipo);
			set.m_Where = "ID_Factura = '" + idfact + "'";
			set.Open();
			if(set.getAbsRow(0).getCFD() == false)
			{
				m_StatusCFD = NULO;
				return; // no necesita sellarse
			}
			else if(cargarInfoPac(request) == false)
			{
				return;
			}
			
			m_StatusCFD = OKYDOKY;
			ObtenInfoEmisor(request);
			
			if(m_CFD == CFD_NO_EMISOR)
			{
				m_StatusCFD = ERROR;
				m_Error = "Esta empresa no se ha establecido como emisor de CFD";
				return;
			}
			
			CargarCertificadosComprobandolos(request, set.getAbsRow(0).getCFD_ArchivoCertificado(), set.getAbsRow(0).getCFD_ArchivoLLave(), set.getAbsRow(0).getCFD_NoCertificado(), set.getAbsRow(0).getCFD_CaducidadCertificado(), tipo, idfact); 
		//	CargarCertificadosComprobandolos(request, 					String ArchivoCertificado, 					String ArchivoLLave, 						String NoCertificado, 								  Date notAfter, String tipo, Integer id) 
			
			if(m_StatusCertComp == ERROR)
			{
				m_StatusCFD = ERROR;
				return;
			}
			
			// Ahora ya se han hecho las verificaciones, procede a generar la cancelacion			Calendar fecha = Calendar.getInstance();
			generarCancelacion(request, nomar, idfact);
			
		} // Fin tfd = 1
			
		
	}

	public void generarNomina(HttpServletRequest request, HttpServletResponse response, String tipo, int idnom, String idemp, JNominaEntidadesSetIds forSet, byte tfd) 
			throws ServletException, IOException
	{
		String [] complementos = new String [] {"Nomina"};
		comprobanteNuevo(complementos);
			
		if(tfd == 0) // significa que no se ha hecho nada al registro
		{
			JCFDNomRecCabGenSet set = new JCFDNomRecCabGenSet(request);
			set.m_Where = "ID_Nomina = '" + idnom + "' and ID_Empleado = '" + JUtil.p(idemp) + "'";
			set.Open();
			if(set.getAbsRow(0).getCFD() == false)
			{
				m_StatusCFD = NULO;
				return; // no necesita sellarse
			}
			else if(cargarInfoPac(request) == false)
			{
				return;
			}
			
			if(VerificarDatosDeEmisor(request, set.getAbsRow(0).getCFD_ArchivoCertificado(), set.getAbsRow(0).getCFD_ArchivoLlave(), set.getAbsRow(0).getCFD_NoCertificado(), set.getAbsRow(0).getCFD_CaducidadCertificado(),
					set.getAbsRow(0).getCFD_Folio(), set.getAbsRow(0).getCFD_FolioIni(), set.getAbsRow(0).getCFD_FolioFin(), "NOM", idnom) == false)
				return;
				
			// Ahora ya se han hecho las verificaciones, procede a generar la el comprobante fiscal digital
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(set.getAbsRow(0).getFecha_Hasta());
			String condicionesDePago;
			condicionesDePago = "Contado";
			Float descuento = null;
			if(set.getAbsRow(0).getDescuento() != 0.0f)
			{
				if(set.getAbsRow(0).getDescuento() <= 0)
					descuento = Math.abs(set.getAbsRow(0).getDescuento());
				else
					descuento = -set.getAbsRow(0).getDescuento();
			}
			String Moneda = null;
			if(set.getAbsRow(0).getMoneda() != 1)
				Moneda = set.getAbsRow(0).getMonedaSat();
			Float TipoCambio = null;
			if(set.getAbsRow(0).getMoneda() != 1)
				TipoCambio = set.getAbsRow(0).getTC();
			//Metodo de pago
			
			String LugarExpedicion;
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(set.getAbsRow(0).getCFD_Estado()) + "' and CodPais3 = '" + JUtil.p(set.getAbsRow(0).getCFD_Pais()) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(set.getAbsRow(0).getCFD_Estado());
			}
			else
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(m_CFD_Estado) + "' and CodPais3 = '" + JUtil.p(m_CFD_Pais) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(m_CFD_Estado);
			}
			// Empieza por el elemento comprobante
			// Los folios son generales
			String tipoCFD;
			tipoCFD = "egreso";
				m_LineaRep[9] = "E"; // Efecto del comprobante E Egreso
			if(!generarElementoComprobante("NOM", set.getAbsRow(0).getCFD_Serie(), set.getAbsRow(0).getCFD_Folio(), Calendar.getInstance(), set.getAbsRow(0).getCFD_NoAprobacion(), set.getAbsRow(0).getCFD_AnoAprobacion(),
					tipoCFD,"Pago en una sola exhibición",condicionesDePago,
					set.getAbsRow(0).getImporte(), descuento, TipoCambio, Moneda, set.getAbsRow(0).getTotal(),
					m_NoCertificado,m_Certificado,set.getAbsRow(0).getMetodoDePago(),LugarExpedicion))
				return;
					
			//////////////////////////////////////// AHORA CONJUNTA ELEMENTO EMISOR //////////////////////////////////////////////
			if(!generarElementoEmisor(m_CFD_RFC, xmlse(m_CFD_Nombre), xmlse(m_CFD_Calle), xmlse(m_CFD_NoExt), xmlse(m_CFD_NoInt), xmlse(m_CFD_Colonia), xmlse(m_CFD_Localidad), 
					xmlse(m_CFD_Municipio), xmlse(m_CFD_Estado), xmlse(m_CFD_Pais), m_CFD_CP))
				return;
				
			// Ahora los datos de expedicion del documento, solo que sea expedicion verdadera
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				if(!generarElementoExpedidoEn(xmlse(set.getAbsRow(0).getCFD_Calle()), xmlse(set.getAbsRow(0).getCFD_NoExt()), xmlse(set.getAbsRow(0).getCFD_NoInt()),
						xmlse(set.getAbsRow(0).getCFD_Colonia()), xmlse(set.getAbsRow(0).getCFD_Localidad()), xmlse(set.getAbsRow(0).getCFD_Municipio()), xmlse(set.getAbsRow(0).getCFD_Estado()), 
						xmlse(set.getAbsRow(0).getCFD_Pais()), set.getAbsRow(0).getCFD_CP()))
					return;
					
			}
			// Ahora el regimen fiscal
			if(m_CFD_Regimen != null && !m_CFD_Regimen.equals(""))
			{
				String[] regimen = m_CFD_Regimen.split(",");
				Element Emisor = m_Comprobante.getChild("Emisor",m_ns);
					 
				// En este momento tenemos un array en el que cada elemento es un color.
				for (int i = 0; i < regimen.length; i++) 
				{
					System.out.println(regimen[i]);
					if(!generarElementoRegimenFiscal(Emisor, xmlse(regimen[i])))
						return;
				}
								
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "No existen regimenes fiscales de origen en CFD_RegimenFiscal de la tabla TBL_BD";
				return;
			}
			////////////////////////////////////////AHORA CONJUNTA ELEMENTO RECEPTOR //////////////////////////////////////////////
			//System.out.println("ELEMENTO RECEPTOR EMPLEADO " + set.getAbsRow(0).getID_Empleado());
			if(!generarElementoReceptor(set.getAbsRow(0).getRFC(), xmlse(set.getAbsRow(0).getNombre()), xmlse(set.getAbsRow(0).getCalle()), xmlse(set.getAbsRow(0).getNoExt()), xmlse(set.getAbsRow(0).getNoInt()),
				xmlse(set.getAbsRow(0).getColonia()), xmlse(set.getAbsRow(0).getLocalidad()), xmlse(set.getAbsRow(0).getMunicipio()), xmlse(set.getAbsRow(0).getEstado()), 
				xmlse(set.getAbsRow(0).getPais()), set.getAbsRow(0).getCP()))
				return;
					
			
			/////////////////////////// AHORA LOS CONCEPTOS ////////////////////////////////////
			JCalculoNominaDetSet dset = new JCalculoNominaDetSet(request);
			dset.m_Where = "ID_Nomina = '" + idnom  + "' and ID_Empleado = '" +  JUtil.p(idemp)  + "'";
			dset.Open();
			
			Element Conceptos = new Element("Conceptos", m_ns);
			for(int j = 0; j < dset.getNumRows(); j++)
			{ 
				float valorUnitario = dset.getAbsRow(j).getGravado() + dset.getAbsRow(j).getExento();
				float importe = valorUnitario;
								
				//System.out.println("ANTES " + dset.getAbsRow(j).getDescripcion() + " CANT " + cantidad);
				if(!dset.getAbsRow(j).getEsDeduccion())
				{
					if(!generarElementoConcepto(Conceptos, 1.0F, "servicio", fcvenomsat(dset.getAbsRow(j).getID_Movimiento()),
						xmlse(dset.getAbsRow(j).getDescripcion()), valorUnitario, importe))
						return;
				}
				//System.out.println("DESPUES " + dset.getAbsRow(j).getDescripcion() + " CANT " + cantidad);
				
			}
			m_Comprobante.addContent(Conceptos);
					
			///////////////////////////////////// IMPUESTOS /////////////////////////////////////////////////////
			Element Impuestos = new Element("Impuestos",m_ns);
				
			if(set.getAbsRow(0).getISR() != 0)
			{
				float sumisr;
				if(set.getAbsRow(0).getISR() <= 0)
					sumisr = Math.abs(set.getAbsRow(0).getISR());
				else
					sumisr = -set.getAbsRow(0).getISR();
				
				Impuestos.setAttribute("totalImpuestosRetenidos",c6d(sumisr));
				m_LineaRep[7] = c6d(sumisr);
					
				//Nomina no maneja impuestos trasladados
				Element Retenciones = new Element("Retenciones",m_ns);
				if(!generarElementoRetencion(Retenciones, "ISR", sumisr))
					return;
							
				m_CadenaOriginal += "|" + c6d(sumisr);
					
				Impuestos.addContent(Retenciones);
				
			}
			else
				m_LineaRep[7] = "0.0";
			
			m_Comprobante.addContent(Impuestos);
			
			////////// Complemento de nomina
			Element Complemento = new Element("Complemento", m_ns);
			Element Nomina = new Element("Nomina", m_nsnomina);
			Complemento.addContent(Nomina);
			
			Nomina.setAttribute("Version","1.1");
			//Empleado
			Nomina.setAttribute("NumEmpleado", xmlse(set.getAbsRow(0).getID_Empleado()));
			m_CadenaOriginalNom += "|" + xmlse(set.getAbsRow(0).getID_Empleado());
			// Registro patronal no se maneja por ahora
			//CURP
			String CURP = JUtil.frfc(set.getAbsRow(0).getCURP());
			if(CURP.length() == 18)
			{
				Nomina.setAttribute("CURP", CURP);
				m_CadenaOriginalNom += "|" + CURP;
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "El CURP del empleado no parece ser válido: " + set.getAbsRow(0).getID_Empleado() + " CURP: " + set.getAbsRow(0).getCURP();
				return;
			}
			//TipoRegimen
			Nomina.setAttribute("TipoRegimen", Integer.toString(set.getAbsRow(0).getTipoRegimen()));
			m_CadenaOriginalNom += "|" + Integer.toString(set.getAbsRow(0).getTipoRegimen());
			//Numero de IMSS
			String IMSS = set.getAbsRow(0).getNumSeguridadSocial();
			if(IMSS.length() >= 1 && IMSS.length() <= 15)
			{
				Nomina.setAttribute("NumSeguridadSocial", xmlse(IMSS));
				m_CadenaOriginalNom += "|" + xmlse(IMSS);
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "El Numero de Seguridad Social del empleado no parece ser válido: " + set.getAbsRow(0).getID_Empleado() + " IMSS: " + set.getAbsRow(0).getNumSeguridadSocial();
				return;
			}
			//FechaPago
			Nomina.setAttribute("FechaPago",JUtil.obtFechaTxt(fecha, "yyyy-MM-dd"));
			m_CadenaOriginalNom += "|" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd");
			//FechaInicialPago
			Calendar fechaInicial = new GregorianCalendar();
			fechaInicial.setTime(set.getAbsRow(0).getFecha_Desde());
			Nomina.setAttribute("FechaInicialPago",JUtil.obtFechaTxt(fechaInicial, "yyyy-MM-dd"));
			m_CadenaOriginalNom += "|" + JUtil.obtFechaTxt(fechaInicial, "yyyy-MM-dd");
			//FechaFinalPago
			Nomina.setAttribute("FechaFinalPago",JUtil.obtFechaTxt(fecha, "yyyy-MM-dd"));
			m_CadenaOriginalNom += "|" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd");
			//NumDiasPagados
			Nomina.setAttribute("NumDiasPagados",c6d(set.getAbsRow(0).getNumDiasPagados()));
			m_CadenaOriginalNom += "|" + c6d(set.getAbsRow(0).getNumDiasPagados());
			//Departamento
			String Depto = set.getAbsRow(0).getDepartamento();
			if(Depto.length() >= 1 && Depto.length() <= 80)
			{
				Nomina.setAttribute("Departamento", xmlse(Depto));
				m_CadenaOriginalNom += "|" + xmlse(Depto);
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "El Departamento del empleado no parece ser válido: " + set.getAbsRow(0).getID_Empleado() + " Depto: " + set.getAbsRow(0).getDepartamento();
				return;
			}
			//CLABE
			if(!set.getAbsRow(0).getCLABE().equals("")) //Maneja cuenta de destino
			{
				String Clabe = fclabe(set.getAbsRow(0).getCLABE());
				if(Clabe.length() == 18)
				{
					Nomina.setAttribute("CLABE", Clabe);
					m_CadenaOriginalNom += "|" + Clabe;
				}
				else
				{
					m_StatusCFD = ERROR;
					m_Error = "La cuenta CLABE interbancaria del empleado no parece ser válida: " + set.getAbsRow(0).getID_Empleado() + " CLABE: " + Clabe;
					return;
				}
				//Ahora inserta la clave del Banco
				if(!set.getAbsRow(0).getBanco().equals("000"))
				{
					Nomina.setAttribute("Banco", set.getAbsRow(0).getBanco());
					m_CadenaOriginalNom += "|" + set.getAbsRow(0).getBanco();
				}
				else
				{
					m_StatusCFD = ERROR;
					m_Error = "Se debe seleccionar un banco ligado a la cuenta CLABE interbancaria del empleado: " + set.getAbsRow(0).getID_Empleado();
					return;
				}

			}
			//FechaInicioRelLaboral
			Calendar fechaInicioRelLaboral = new GregorianCalendar();
			fechaInicioRelLaboral.setTime(set.getAbsRow(0).getFechaInicioRelLaboral());
			Nomina.setAttribute("FechaInicioRelLaboral",JUtil.obtFechaTxt(fechaInicioRelLaboral, "yyyy-MM-dd"));
			m_CadenaOriginalNom += "|" + JUtil.obtFechaTxt(fechaInicioRelLaboral, "yyyy-MM-dd");
			//Puesto
			String Puesto = set.getAbsRow(0).getPuesto();
			if(!Puesto.equals(""))
			{
				Nomina.setAttribute("Puesto", xmlse(Puesto));
				m_CadenaOriginalNom += "|" + xmlse(Puesto);
			}
			//PeriodicidadPago
			Nomina.setAttribute("PeriodicidadPago", set.getAbsRow(0).getPeriodicidadPago());
			m_CadenaOriginalNom += "|" + set.getAbsRow(0).getPeriodicidadPago();
			////////////////////////////////////////////////////////////////////////////////////////
			// Ahora los detalles de los Movimientos
			////////////////////////////////////////////////////////////////////////////////////////
			boolean bIncapacidades = false, bHorasExtras = false; 
			String coPercepciones = "", coDeducciones = "", coPercepcion = "", coDeduccion = "", coIncapacidad = "", coHorasExtra = "";
			
			Element Percepciones = new Element("Percepciones", m_nsnomina);
			Percepciones.setAttribute("TotalGravado", c6d(set.getAbsRow(0).getTotalGravado()));
			coPercepciones += "|" + c6d(set.getAbsRow(0).getTotalGravado());
			Percepciones.setAttribute("TotalExento", c6d(set.getAbsRow(0).getTotalExento()));
			coPercepciones += "|" + c6d(set.getAbsRow(0).getTotalExento());
			float totdedgrav;
			if(set.getAbsRow(0).getTotalDedGravadas() <= 0)
				totdedgrav = Math.abs(set.getAbsRow(0).getTotalDedGravadas());
			else
				totdedgrav = -set.getAbsRow(0).getTotalDedGravadas();
			float totdedexen;
			if(set.getAbsRow(0).getTotalDedExentas() <= 0)
				totdedexen = Math.abs(set.getAbsRow(0).getTotalDedExentas());
			else
				totdedexen = -set.getAbsRow(0).getTotalDedExentas();
			
			Element Deducciones = new Element("Deducciones", m_nsnomina);
			Deducciones.setAttribute("TotalGravado", c6d(totdedgrav));
			coDeducciones += "|" + c6d(totdedgrav);
			Deducciones.setAttribute("TotalExento", c6d(totdedexen));
			coDeducciones += "|" + c6d(totdedexen);
			
			for(int j = 0; j < dset.getNumRows(); j++)
			{ 
				if(!dset.getAbsRow(j).getEsDeduccion())
				{
					Element Percepcion = new Element("Percepcion", m_nsnomina);
					Percepcion.setAttribute("TipoPercepcion", dset.getAbsRow(j).getID_SAT());
					coPercepcion += "|" + dset.getAbsRow(j).getID_SAT();
					Percepcion.setAttribute("Clave", fcvenomsat(dset.getAbsRow(j).getID_Movimiento()));
					coPercepcion += "|" + fcvenomsat(dset.getAbsRow(j).getID_Movimiento());
					Percepcion.setAttribute("Concepto", xmlse(dset.getAbsRow(j).getDescripcion()));
					coPercepcion += "|" + xmlse(dset.getAbsRow(j).getDescripcion());
					Percepcion.setAttribute("ImporteGravado", c6d(dset.getAbsRow(j).getGravado()));
					coPercepcion += "|" + c6d(dset.getAbsRow(j).getGravado());
					Percepcion.setAttribute("ImporteExento", c6d(dset.getAbsRow(j).getExento()));
					coPercepcion += "|" + c6d(dset.getAbsRow(j).getExento());
					Percepciones.addContent(Percepcion);
				}
				else
				{
					Element Deduccion = new Element("Deduccion", m_nsnomina);
					Deduccion.setAttribute("TipoDeduccion", dset.getAbsRow(j).getID_SAT());
					coDeduccion += "|" + dset.getAbsRow(j).getID_SAT();
					Deduccion.setAttribute("Clave", fcvenomsat(dset.getAbsRow(j).getID_Movimiento()));
					coDeduccion += "|" + fcvenomsat(dset.getAbsRow(j).getID_Movimiento());
					Deduccion.setAttribute("Concepto", xmlse(dset.getAbsRow(j).getDescripcion()));
					coDeduccion += "|" + xmlse(dset.getAbsRow(j).getDescripcion());
					Deduccion.setAttribute("ImporteGravado", c6d((dset.getAbsRow(j).getDeduccion() <= 0 ? Math.abs(dset.getAbsRow(j).getDeduccion()) : -dset.getAbsRow(j).getDeduccion() )));
					coDeduccion += "|" + c6d((dset.getAbsRow(j).getDeduccion() <= 0 ? Math.abs(dset.getAbsRow(j).getDeduccion()) : -dset.getAbsRow(j).getDeduccion() ));
					Deduccion.setAttribute("ImporteExento", c6d(0.0F));
					coDeduccion += "|" + c6d(0.0F);
					Deducciones.addContent(Deduccion);
				}
			}
						
			Nomina.addContent(Percepciones);
			Nomina.addContent(Deducciones);
			
			Element Incapacidades = new Element("Incapacidades", m_nsnomina);
			
			if(set.getAbsRow(0).getIXA() > 0.0 || set.getAbsRow(0).getIXE() > 0.0 || set.getAbsRow(0).getIXM() > 0.0)
			{
				// || dset.getAbsRow(j).getTipo().equals("IXE") || dset.getAbsRow(j).getTipo().equals("IXM"))
				bIncapacidades = true;
				if(set.getAbsRow(0).getIXA() > 0.0)
				{
					Element Incapacidad = new Element("Incapacidad", m_nsnomina);
					Incapacidad.setAttribute("DiasIncapacidad", Float.toString(set.getAbsRow(0).getIXA()));
					coIncapacidad += "|" + Float.toString(set.getAbsRow(0).getIXA());
					Incapacidad.setAttribute("TipoIncapacidad","1");
					coIncapacidad += "|1";
					float deduccion = 0.0F;
					for(int j = 0; j < dset.getNumRows(); j++)
					{ 
						if(dset.getAbsRow(j).getTipo().equals("IXA"))
							deduccion += dset.getAbsRow(j).getDeduccion();
					}
					Incapacidad.setAttribute("Descuento", c6d(deduccion));
					coIncapacidad += "|" + c6d(deduccion);
					Incapacidades.addContent(Incapacidad);	
					
				}
				if(set.getAbsRow(0).getIXE() > 0.0)
				{
					Element Incapacidad = new Element("Incapacidad", m_nsnomina);
					Incapacidad.setAttribute("DiasIncapacidad", Float.toString(set.getAbsRow(0).getIXE()));
					coIncapacidad += "|" + Float.toString(set.getAbsRow(0).getIXE());
					Incapacidad.setAttribute("TipoIncapacidad","2");
					coIncapacidad += "|2";
					float deduccion = 0.0F;
					for(int j = 0; j < dset.getNumRows(); j++)
					{ 
						if(dset.getAbsRow(j).getTipo().equals("IXE"))
							deduccion += dset.getAbsRow(j).getDeduccion();
					}
					Incapacidad.setAttribute("Descuento", c6d(deduccion));
					coIncapacidad += "|" + c6d(deduccion);
					Incapacidades.addContent(Incapacidad);
				}
				if(set.getAbsRow(0).getIXM() > 0.0)
				{
					Element Incapacidad = new Element("Incapacidad", m_nsnomina);
					Incapacidad.setAttribute("DiasIncapacidad", Float.toString(set.getAbsRow(0).getIXM()));
					coIncapacidad += "|" + Float.toString(set.getAbsRow(0).getIXM());
					Incapacidad.setAttribute("TipoIncapacidad","3");
					coIncapacidad += "|3";
					float deduccion = 0.0F;
					for(int j = 0; j < dset.getNumRows(); j++)
					{ 
						if(dset.getAbsRow(j).getTipo().equals("IXM"))
							deduccion += dset.getAbsRow(j).getDeduccion();
					}
					Incapacidad.setAttribute("Descuento", c6d(deduccion));
					coIncapacidad += "|" + c6d(deduccion);
					Incapacidades.addContent(Incapacidad);
				}
				
			}
			
			if(bIncapacidades)
				Nomina.addContent(Incapacidades);
			
			Element HorasExtras = new Element("HorasExtras", m_nsnomina);
		
			if(set.getAbsRow(0).getHorasExtras() > 0.0 || set.getAbsRow(0).getHorasTriples() > 0.0)
			{
				bHorasExtras = true;
				if(set.getAbsRow(0).getHorasExtras() > 0.0)
				{
					Element HorasExtra = new Element("HorasExtra", m_nsnomina);
					HorasExtra.setAttribute("Dias", Integer.toString(set.getAbsRow(0).getDiasHorasExtras()));
					coHorasExtra += "|" + Integer.toString(set.getAbsRow(0).getDiasHorasExtras());
					HorasExtra.setAttribute("TipoHoras", "Dobles");
					coHorasExtra += "|Dobles";
					HorasExtra.setAttribute("HorasExtra", Integer.toString((int)set.getAbsRow(0).getHorasExtras()));
					coHorasExtra += "|" + Integer.toString((int)set.getAbsRow(0).getHorasExtras());
					float percepcion = 0.0F;
					for(int j = 0; j < dset.getNumRows(); j++)
					{ 
						if(dset.getAbsRow(j).getTipo().equals("IHEE") || dset.getAbsRow(j).getTipo().equals("IHEG"))
							percepcion += dset.getAbsRow(j).getGravado() + dset.getAbsRow(j).getExento();
					}
					HorasExtra.setAttribute("ImportePagado", c6d(percepcion));
					coHorasExtra += "|" + c6d(percepcion);
			
					HorasExtras.addContent(HorasExtra);
				}
				if(set.getAbsRow(0).getHorasTriples() > 0.0)
				{
					Element HorasExtra = new Element("HorasExtra", m_nsnomina);
					HorasExtra.setAttribute("Dias", Integer.toString(set.getAbsRow(0).getDiasHorasExtras()));
					coHorasExtra += "|" + Integer.toString(set.getAbsRow(0).getDiasHorasExtras());
					HorasExtra.setAttribute("TipoHoras", "Triples");
					coHorasExtra += "|Triples";
					HorasExtra.setAttribute("HorasExtra", Integer.toString((int)set.getAbsRow(0).getHorasTriples()));
					coHorasExtra += "|" + Integer.toString((int)set.getAbsRow(0).getHorasTriples());
					float percepcion = 0.0F;
					for(int j = 0; j < dset.getNumRows(); j++)
					{ 
						if(dset.getAbsRow(j).getTipo().equals("IHDE") || dset.getAbsRow(j).getTipo().equals("IHDG") || 
								dset.getAbsRow(j).getTipo().equals("IHTE") || dset.getAbsRow(j).getTipo().equals("IHTG"))
							percepcion += dset.getAbsRow(j).getGravado() + dset.getAbsRow(j).getExento();
					}
					HorasExtra.setAttribute("ImportePagado", c6d(percepcion));
					coHorasExtra += "|" + c6d(percepcion);
			
					HorasExtras.addContent(HorasExtra);
				}
			}
			
			if(bHorasExtras)
				Nomina.addContent(HorasExtras);
						
			m_Comprobante.addContent(Complemento);
			////////// Addenda No lo maneja forseti
				
			m_CadenaOriginal += m_CadenaOriginalNom + coPercepciones + coPercepcion + coDeducciones + coDeduccion + coIncapacidad + coHorasExtra + "||";
			m_LineaRep[8] = "1"; // Estado del comprobante 1 Vigente
			m_LineaRep[10] = ""; m_LineaRep[11] = ""; m_LineaRep[12] = ""; //Informacion aduanera (No lo maneja forseti)
					
			generarCFDI(request, "NOM", idnom, idemp);
			if(m_StatusCFD == OKYDOKY)
				tfd = 1;
				
		} // Fin tfd = 1
				
		if(tfd == 1)
		{
			generarTFD(request, "NOM", idnom, idemp);
			if(m_StatusCFD == OKYDOKY)
				tfd = 2;
				
		}	// Fin tfd = 0
					
		if(tfd == 2)
		{
			String formato, SQLCab, SQLDet;
			formato = forSet.getAbsRow(0).getFmt_Recibo();
			SQLCab = "select * from view_nomina_recibos_impcab where ID_Nomina = " + idnom + " and ID_Empleado = '" + idemp + "'";
			SQLDet = "select * from view_nomina_recibos_impdet where ID_Nomina = " + idnom + " and ID_Empleado = '" + idemp + "' order by esdeduccion asc, id_movimiento asc";
			
			if(formato == null || formato.equals("null") || formato.equals("") || formato.equals("NULL"))
			{
				m_StatusCFD = JForsetiCFD.ERROR;
				m_Error = "No existe formato para la generaci&oacute;n del PDF del CFDI";
				return;
			}
			else
			{
				// Primero verifica el formato para el PDF
				JPublicFormatosSetV2 fi = new JPublicFormatosSetV2(request);
				fi.m_Where = "ID_Formato = '" + JUtil.p(formato) + "'";
				fi.Open();
				if(fi.getNumRows() < 1)
				{
					m_StatusCFD = JForsetiCFD.ERROR;
					m_Error = "ERROR: El formato para la generaci&oacute; del PDF del CFDI no existe o es Nulo";
					return;
				}
					
				generarPDF(request, response, "NOM", idnom, idemp, SQLCab, SQLDet, formato);
				if(m_StatusCFD == OKYDOKY)
					tfd = 3;
					
			}	
				
				
		} // Fin tfd == 2
		
		
	}
	
	public void generarVenta(HttpServletRequest request, HttpServletResponse response, String tipo, int idfact, String idcli, JVentasEntidadesSetIdsV2 forSet, byte tfd) 
		throws ServletException, IOException
	{
		String complementos [];
		boolean bCce = false;
		String nomar; 
		
		if(tipo.equals("FACTURAS"))
		{
			JComercioExteriorCabSet cce = new JComercioExteriorCabSet(request,"VENTA");
			cce.m_Where = "ID_VC = '" + idfact + "'";
			cce.Open();						
			if(cce.getNumRows() > 0)
			{
				bCce = true;
				complementos = new String [] {"ComercioExterior"};
			}
			else
				complementos = new String [0]; 
			nomar = "FAC";
		}
		else if(tipo.equals("REMISIONES"))
		{
			complementos = new String [0]; 
			nomar = "REM";
		}
		else
		{
			complementos = new String [0]; 
			nomar = "DSV";
		}
		
		comprobanteNuevo(complementos);
		
		if(tfd == 0) // significa que no se ha hecho nada al registro
		{
			JCFDVenFactCabGenSet set = new JCFDVenFactCabGenSet(request, tipo);
			set.m_Where = "ID_Factura = '" + idfact + "'";
			set.Open();
			if(set.getAbsRow(0).getCFD() == false)
			{
				m_StatusCFD = NULO;
				return; // no necesita sellarse
			}
			else if(cargarInfoPac(request) == false)
			{
				return;
			}
			
			if(VerificarDatosDeEmisor(request, set.getAbsRow(0).getCFD_ArchivoCertificado(), set.getAbsRow(0).getCFD_ArchivoLLave(), set.getAbsRow(0).getCFD_NoCertificado(), set.getAbsRow(0).getCFD_CaducidadCertificado(),
					set.getAbsRow(0).getCFD_Folio(), set.getAbsRow(0).getCFD_FolioINI(), set.getAbsRow(0).getCFD_FolioFIN(), nomar, idfact) == false)
				return;
			
			// Ahora ya se han hecho las verificaciones, procede a generar la el comprobante fiscal digital
			Calendar fecha = Calendar.getInstance();
			String condicionesDePago;
			if(set.getAbsRow(0).getCondicion() == 0)
				condicionesDePago = "Contado";
			else
				condicionesDePago = "Crédito " + set.getAbsRow(0).getDiasCredito() + " días";
			Float descuento = null;
			if(set.getAbsRow(0).getDescuento() != 0.0f)
				descuento = set.getAbsRow(0).getDescuento();
			String Moneda = null;
			if(set.getAbsRow(0).getMoneda() != 1)
				Moneda = set.getAbsRow(0).getMonedaSat();
			Float TipoCambio = null;
			if(set.getAbsRow(0).getMoneda() != 1)
				TipoCambio = set.getAbsRow(0).getTC();
			//Metodo de pago
			
			String LugarExpedicion;
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(set.getAbsRow(0).getCFD_Estado()) + "' and CodPais3 = '" + JUtil.p(set.getAbsRow(0).getCFD_Pais()) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(set.getAbsRow(0).getCFD_Estado());
			}
			else
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(m_CFD_Estado) + "' and CodPais3 = '" + JUtil.p(m_CFD_Pais) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(m_CFD_Estado);
			}
			// Empieza por el elemento comprobante
			// Los folios son generales
			String tipoCFD;
			if(tipo.equals("FACTURAS"))
			{
				tipoCFD = "ingreso";
				m_LineaRep[9] = "I"; // Efecto del comprobante I Ingreso
			}
			else if(tipo.equals("REMISIONES"))
			{
				tipoCFD = "traslado";
				m_LineaRep[9] = "T"; // Efecto del comprobante T Traslado
			}
			else 
			{
				tipoCFD = "egreso";
				m_LineaRep[9] = "E"; // Efecto del comprobante E Egreso
			}
			if(!generarElementoComprobante(nomar, set.getAbsRow(0).getCFD_Serie(), set.getAbsRow(0).getCFD_Folio(), fecha, set.getAbsRow(0).getCFD_NoAprobacion(), set.getAbsRow(0).getCFD_AnoAprobacion(),
					tipoCFD,"Pago en una sola exhibición",condicionesDePago,
					set.getAbsRow(0).getImporte(), descuento, TipoCambio, Moneda, set.getAbsRow(0).getTotal(),
					m_NoCertificado,m_Certificado,set.getAbsRow(0).getMetodoDePago(),LugarExpedicion))
				return;
				
			//////////////////////////////////////// AHORA CONJUNTA ELEMENTO EMISOR //////////////////////////////////////////////
			if(!generarElementoEmisor(m_CFD_RFC, xmlse(m_CFD_Nombre), xmlse(m_CFD_Calle), xmlse(m_CFD_NoExt), xmlse(m_CFD_NoInt), xmlse(m_CFD_Colonia), xmlse(m_CFD_Localidad), 
					xmlse(m_CFD_Municipio), xmlse(m_CFD_Estado), xmlse(m_CFD_Pais), m_CFD_CP))
				return;
			
			// Ahora los datos de expedicion del documento, solo que sea expedicion verdadera
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				if(!generarElementoExpedidoEn(xmlse(set.getAbsRow(0).getCFD_Calle()), xmlse(set.getAbsRow(0).getCFD_NoExt()), xmlse(set.getAbsRow(0).getCFD_NoInt()),
						xmlse(set.getAbsRow(0).getCFD_Colonia()), xmlse(set.getAbsRow(0).getCFD_Localidad()), xmlse(set.getAbsRow(0).getCFD_Municipio()), xmlse(set.getAbsRow(0).getCFD_Estado()), 
						xmlse(set.getAbsRow(0).getCFD_Pais()), set.getAbsRow(0).getCFD_CP()))
					return;
				
			}
			// Ahora el regimen fiscal
			if(m_CFD_Regimen != null && !m_CFD_Regimen.equals(""))
			{
				String[] regimen = m_CFD_Regimen.split(",");
				Element Emisor = m_Comprobante.getChild("Emisor",m_ns);
				 
				// En este momento tenemos un array en el que cada elemento es un color.
				for (int i = 0; i < regimen.length; i++) 
				{
					System.out.println(regimen[i]);
					if(!generarElementoRegimenFiscal(Emisor, xmlse(regimen[i])))
						return;
				}
								
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "No existen regimenes fiscales de origen en CFD_RegimenFiscal de la tabla TBL_BD";
				return;
			}
			////////////////////////////////////////AHORA CONJUNTA ELEMENTO RECEPTOR //////////////////////////////////////////////
			//System.out.println("ELEMENTO RECEPTOR CLIENTES " + set.getAbsRow(0).getID_Cliente());
			if(set.getAbsRow(0).getID_Cliente() == 0) // Es de mostrador
			{
				if(!generarElementoReceptor("XAXX010101000", null, null, null, null, null, null, null, null, "México", null))
					return;
			}
			else
			{
				if(!generarElementoReceptor(set.getAbsRow(0).getRFC(), xmlse(set.getAbsRow(0).getNombre()), xmlse(set.getAbsRow(0).getCalle()), xmlse(set.getAbsRow(0).getNoExt()), xmlse(set.getAbsRow(0).getNoInt()),
						xmlse(set.getAbsRow(0).getColonia()), xmlse(set.getAbsRow(0).getLocalidad()), xmlse(set.getAbsRow(0).getMunicipio()), xmlse(set.getAbsRow(0).getEstado()), 
						xmlse(set.getAbsRow(0).getPais()), set.getAbsRow(0).getCP()))
					return;
				
			}
			/////////////////////////// AHORA LOS CONCEPTOS ////////////////////////////////////
			JVentasFactSetDetV2 det = new JVentasFactSetDetV2(request,tipo);
			det.m_Where = "ID_Factura = '" + idfact + "'";
			det.m_OrderBy = "Partida ASC";
			det.Open();
			
			Element Conceptos = new Element("Conceptos", m_ns);
			
			for(int i = 0; i < det.getNumRows(); i++)
			{
				float valorUnitario = (det.getAbsRow(i).getImporte() / det.getAbsRow(i).getCantidad());
				float importe = det.getAbsRow(i).getImporte();
				
				if(!generarElementoConcepto(Conceptos, det.getAbsRow(i).getCantidad(),xmlse(det.getAbsRow(i).getID_UnidadSalida()),xmlse(det.getAbsRow(i).getID_Prod()),
						xmlse(det.getAbsRow(i).getDescripcion()), valorUnitario, importe))
					return;
			}
			m_Comprobante.addContent(Conceptos);
				
			///////////////////////////////////// IMPUESTOS /////////////////////////////////////////////////////
			Element Impuestos = new Element("Impuestos",m_ns);
			
			if(set.getAbsRow(0).getIVA() != 0.0f || set.getAbsRow(0).getIEPS() != 0.0f || set.getAbsRow(0).getIVARet() != 0.0f || set.getAbsRow(0).getISRRet() != 0.0f)
			{
				float imptrs = set.getAbsRow(0).getIVA() + set.getAbsRow(0).getIEPS();
				float impret = set.getAbsRow(0).getIVARet() + set.getAbsRow(0).getISRRet();
				
				if(imptrs != 0.0f)
					Impuestos.setAttribute("totalImpuestosTrasladados",c6d(imptrs));
				if(impret != 0.0f)
					Impuestos.setAttribute("totalImpuestosRetenidos",c6d(impret));
				
				m_LineaRep[7] = c6d(imptrs + impret);
				
				if(set.getAbsRow(0).getIVARet() != 0.0f || set.getAbsRow(0).getISRRet() != 0.0f)
				{
					Element Retenciones = new Element("Retenciones",m_ns);
					// ahora cada impuesto por detalle
					for(int i = 0; i < det.getNumRows(); i++)
					{
						if(det.getAbsRow(i).getIVARet() != 0.0f)
						{
							float importe = 
									(det.getAbsRow(i).getDescuento() != 0.0) 
									?	(((det.getAbsRow(i).getImporte() - ((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getDescuento()) / 100)) * det.getAbsRow(i).getIVARet()) / 100)
									: 	((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getIVARet()) / 100);
							System.out.println(det.getAbsRow(i).getID_Prod() + "IVARet: " + det.getAbsRow(i).getIVARet() + " " + importe);
							if(!generarElementoRetencion(Retenciones, "IVA", importe))
								return;
							
						}
						// Aqui va ISR Retenido
						if(det.getAbsRow(i).getISRRet() != 0.0f)
						{
							float importe = 
									(det.getAbsRow(i).getDescuento() != 0.0) 
									?	(((det.getAbsRow(i).getImporte() - ((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getDescuento()) / 100)) * det.getAbsRow(i).getISRRet()) / 100)
									: 	((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getISRRet()) / 100);
							System.out.println(det.getAbsRow(i).getID_Prod() + " ISRRet: " + det.getAbsRow(i).getISRRet() + " " + importe);
							if(!generarElementoRetencion(Retenciones, "ISR", importe))
								return;
						}
					}
					m_CadenaOriginal += "|" + c6d(impret);
					Impuestos.addContent(Retenciones);
				}
				
				if(set.getAbsRow(0).getIVA() != 0.0f || set.getAbsRow(0).getIEPS() != 0.0f)
				{
					Element Traslados = new Element("Traslados",m_ns);
					// ahora cada impuesto por detalle
					for(int i = 0; i < det.getNumRows(); i++)
					{
						if(det.getAbsRow(i).getIVA() != 0.0f)
						{
							float importe = 
									(det.getAbsRow(i).getDescuento() != 0.0) 
									?	(((det.getAbsRow(i).getImporte() - ((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getDescuento()) / 100)) * det.getAbsRow(i).getIVA()) / 100)
									: 	((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getIVA()) / 100);
							System.out.println(det.getAbsRow(i).getID_Prod() + " IVA: " + det.getAbsRow(i).getIVA() + " " + importe);
							if(!generarElementoTraslado(Traslados, "IVA", det.getAbsRow(i).getIVA(), importe))
								return;
						}
						// Aqui va IEPS
						if(det.getAbsRow(i).getIEPS() != 0.0f)
						{
							float importe = 
									(det.getAbsRow(i).getDescuento() != 0.0) 
									?	(((det.getAbsRow(i).getImporte() - ((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getDescuento()) / 100)) * det.getAbsRow(i).getIEPS()) / 100)
									: 	((det.getAbsRow(i).getImporte() * det.getAbsRow(i).getIEPS()) / 100);
							System.out.println(det.getAbsRow(i).getID_Prod() + " IEPS: " + det.getAbsRow(i).getIEPS() + " " + importe);
							if(!generarElementoTraslado(Traslados, "IEPS", det.getAbsRow(i).getIEPS(), importe))
								return;
						}
					}
					m_CadenaOriginal += "|" + c6d(imptrs);
					Impuestos.addContent(Traslados);
				}
						
			}
			else
				m_LineaRep[7] = "0.0";
			
			m_Comprobante.addContent(Impuestos);
			
			////////// Complemento De comercio exterior
			if(bCce)
			{
				JComercioExteriorCabSet cceSet = new JComercioExteriorCabSet(request,"VENTA");
				cceSet.m_Where = "ID_VC = '" + idfact + "'";
				cceSet.Open();
			
				Element Complemento = new Element("Complemento", m_ns);
				Element ComercioExterior = new Element("ComercioExterior", m_nscce);
				Complemento.addContent(ComercioExterior);
				
				//a. Información del Nodo cce:ComercioExterior
				//1. Version
				ComercioExterior.setAttribute("Version","1.0");
				//2. TipoOperacion
				if(!cceSet.getAbsRow(0).getTipoOperacion().equals("-"))
				{
					ComercioExterior.setAttribute("TipoOperacion", cceSet.getAbsRow(0).getTipoOperacion());
					m_CadenaOriginalComExt += "|" + cceSet.getAbsRow(0).getTipoOperacion();
				}
				else
				{
					m_StatusCFD = ERROR;
					m_Error = "El Tipo de Operación del complemento de comercio exterior del CFDI no es válido. Debes corregir los datos de exportación de la factura para poder sellarla";
					return;
				}
				//3. ClaveDePedimento
				ComercioExterior.setAttribute("ClaveDePedimento", cceSet.getAbsRow(0).getClaveDePedimento());
				m_CadenaOriginalComExt += "|" + cceSet.getAbsRow(0).getClaveDePedimento();
				//4. CertificadoOrigen
				if(cceSet.getAbsRow(0).getCertificadoOrigen() != -1)
				{
					ComercioExterior.setAttribute("CertificadoOrigen", Byte.toString(cceSet.getAbsRow(0).getCertificadoOrigen()));
					m_CadenaOriginalComExt += "|" + Byte.toString(cceSet.getAbsRow(0).getCertificadoOrigen());
				}
				//5. NumCertificadoOrigen
				String NumCertificadoOrigen = xmlse(cceSet.getAbsRow(0).getNumCertificadoOrigen());
				if(!NumCertificadoOrigen.equals(""))
				{
					ComercioExterior.setAttribute("NumCertificadoOrigen", NumCertificadoOrigen);
					m_CadenaOriginalComExt += "|" + NumCertificadoOrigen;
				}
				//6. NumeroExportadorConfiable
				String NumeroExportadorConfiable = xmlse(cceSet.getAbsRow(0).getNumeroExportadorConfiable());
				if(!NumeroExportadorConfiable.equals(""))
				{
					ComercioExterior.setAttribute("NumeroExportadorConfiable", NumeroExportadorConfiable);
					m_CadenaOriginalComExt += "|" + NumeroExportadorConfiable;
				}
				//7. Incoterm
				if(!cceSet.getAbsRow(0).getIncoterm().equals(""))
				{
					ComercioExterior.setAttribute("Incoterm", cceSet.getAbsRow(0).getIncoterm());
					m_CadenaOriginalComExt += "|" + cceSet.getAbsRow(0).getIncoterm();
				}
				//8. Subdivision
				if(cceSet.getAbsRow(0).getSubdivision() != -1)
				{
					ComercioExterior.setAttribute("Subdivision", Byte.toString(cceSet.getAbsRow(0).getSubdivision()));
					m_CadenaOriginalComExt += "|" + Byte.toString(cceSet.getAbsRow(0).getSubdivision());
				}
				//9. Observaciones
				String Observaciones = xmlse(cceSet.getAbsRow(0).getObservaciones());
				if(!Observaciones.equals(""))
				{
					ComercioExterior.setAttribute("Observaciones", Observaciones);
					m_CadenaOriginalComExt += "|" + Observaciones;
				}
				//10. TipoCambioUSD
				if(cceSet.getAbsRow(0).getTipoCambioUsd() != 0.00)
				{
					ComercioExterior.setAttribute("TipoCambioUSD", c6d(cceSet.getAbsRow(0).getTipoCambioUsd()));
					m_CadenaOriginalComExt += "|" + c6d(cceSet.getAbsRow(0).getTipoCambioUsd());
				}
				//11. TotalUSD
				if(cceSet.getAbsRow(0).getTotalUsd() != 0.00)
				{
					ComercioExterior.setAttribute("TotalUSD", c6d(cceSet.getAbsRow(0).getTotalUsd()));
					m_CadenaOriginalComExt += "|" + c6d(cceSet.getAbsRow(0).getTotalUsd());
				}
				//b. Información del Nodo cce:Emisor
				//1. Curp
				if(!cceSet.getAbsRow(0).getEmisor_Curp().equals(""))
				{
					String CURP = JUtil.frfc(cceSet.getAbsRow(0).getEmisor_Curp());
					if(CURP.length() == 18)
					{
						Element Emisor = new Element("Emisor", m_nscce);
						ComercioExterior.addContent(Emisor);
						Emisor.setAttribute("Curp", CURP);
						m_CadenaOriginalComExt += "|" + CURP;
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El CURP del emisor no parece ser válido. CURP: " + CURP;
						return;
					}
				}
				//c. Información del Nodo cce:Receptor
				Element Receptor = new Element("Receptor", m_nscce);
				ComercioExterior.addContent(Receptor);
				//1. Curp
				if(!cceSet.getAbsRow(0).getReceptor_Curp().equals(""))
				{
					String CURP = JUtil.frfc(cceSet.getAbsRow(0).getReceptor_Curp());
					if(CURP.length() == 18)
					{
						Receptor.setAttribute("Curp", CURP);
						m_CadenaOriginalComExt += "|" + CURP;
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El CURP del receptor no parece ser válido. CURP: " + CURP;
						return;
					}
				}
				//2. NumRegIdTrib
				if(!cceSet.getAbsRow(0).getReceptor_NumRegIdTrib().equals(""))
				{
					Receptor.setAttribute("NumRegIdTrib", xmlse(cceSet.getAbsRow(0).getReceptor_NumRegIdTrib()));
					m_CadenaOriginalComExt += "|" + xmlse(cceSet.getAbsRow(0).getReceptor_NumRegIdTrib());
				}
				else
				{
					m_StatusCFD = ERROR;
					m_Error = "El Registro tributario del elemento Recepror del complemento de comercio exterior del CFDI es requerido. Debes corregir los datos de exportación de la factura para poder sellarla";
					return;
				}
				//d. Información del Nodo cce:Destinatario
				if(!cceSet.getAbsRow(0).getDestinatario_NumRegIdTrib().equals("") || !cceSet.getAbsRow(0).getDestinatario_RFC().equals("") ||
						!cceSet.getAbsRow(0).getDestinatario_Curp().equals("") || !cceSet.getAbsRow(0).getDestinatario_Nombre().equals(""))
				{
					Element Destinatario = new Element("Destinatario", m_nscce);
					ComercioExterior.addContent(Destinatario);
					//1. NumRegIdTrib
					if(!cceSet.getAbsRow(0).getDestinatario_NumRegIdTrib().equals(""))
					{
						Destinatario.setAttribute("NumRegIdTrib", xmlse(cceSet.getAbsRow(0).getDestinatario_NumRegIdTrib()));
						m_CadenaOriginalComExt += "|" + xmlse(cceSet.getAbsRow(0).getDestinatario_NumRegIdTrib());
					}
					//2. Rfc
					String rfcfmt = JUtil.fco(JUtil.frfc(cceSet.getAbsRow(0).getDestinatario_RFC()));
					if(!rfcfmt.equals(""))
					{
						if(rfcfmt.length() == 13 || rfcfmt.length() == 12)
						{
							Destinatario.setAttribute("Rfc", rfcfmt); 
							m_CadenaOriginalComExt += "|" + rfcfmt;
						}
						else
						{
							m_StatusCFD = ERROR;
							m_Error = "El atributo RFC " + rfcfmt + " del Destinatario parece ser nulo o no contiene entre 12 y 13 caracteres";
							return;
						}
					}
					//3. Curp
					if(!cceSet.getAbsRow(0).getDestinatario_Curp().equals(""))
					{
						String CURP = JUtil.frfc(cceSet.getAbsRow(0).getDestinatario_Curp());
						if(CURP.length() == 18)
						{
							Destinatario.setAttribute("Curp", CURP);
							m_CadenaOriginalComExt += "|" + CURP;
						}
						else
						{
							m_StatusCFD = ERROR;
							m_Error = "El CURP del destinatario no parece ser válido. CURP: " + CURP;
							return;
						}
					}
					//4. Nombre
					if(!cceSet.getAbsRow(0).getDestinatario_Nombre().equals(""))
					{
						Destinatario.setAttribute("Nombre", xmlse(cceSet.getAbsRow(0).getDestinatario_Nombre()));
						m_CadenaOriginalComExt += "|" + xmlse(cceSet.getAbsRow(0).getDestinatario_Nombre());
					}
					//e. Información del Nodo cce:Destinatario:Domicilio
					Element Domicilio = new Element("Domicilio", m_nscce);
					Destinatario.addContent(Domicilio);
					//1. Calle
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Calle().equals(""))
					{
						Domicilio.setAttribute("Calle", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Calle())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Calle());
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El atributo requerido Calle del Domicilio del Destinatario del complemento de comercio exterior es incorrecto";
						return;
					}
					//2. NumeroExterior
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroExterior().equals(""))
					{
						Domicilio.setAttribute("NumeroExterior", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroExterior())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroExterior());
					}
					//3. NumeroInterior
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroInterior().equals(""))
					{
						Domicilio.setAttribute("NumeroInterior", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroInterior())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_NumeroInterior());
					}
					//4. Colonia
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Colonia().equals(""))
					{
						Domicilio.setAttribute("Colonia", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Colonia())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Colonia());
					}
					//5. Localidad
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Localidad().equals(""))
					{
						Domicilio.setAttribute("Localidad", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Localidad())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Localidad());
					}
					//6. Referencia
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Referencia().equals(""))
					{
						Domicilio.setAttribute("Referencia", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Referencia())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Referencia());
					}
					//7. Municipio
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Municipio().equals(""))
					{
						Domicilio.setAttribute("Municipio", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Municipio())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Municipio());
					}
					//8. Estado
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Estado().equals(""))
					{
						Domicilio.setAttribute("Estado", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Estado())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Estado());
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El atributo requerido Estado del Domicilio del Destinatario del complemento de comercio exterior es incorrecto";
						return;
					}
					//9. Pais
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_Pais().equals(""))
					{
						Domicilio.setAttribute("Pais", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Pais())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_Pais());
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El atributo requerido Pais del Domicilio del Destinatario del complemento de comercio exterior es incorrecto";
						return;
					}
					//10. CodigoPostal
					if(!cceSet.getAbsRow(0).getDestinatario_Domicilio_CodigoPostal().equals(""))
					{
						Domicilio.setAttribute("CodigoPostal", JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_CodigoPostal())); 
						m_CadenaOriginalComExt += "|" + JUtil.fco(cceSet.getAbsRow(0).getDestinatario_Domicilio_CodigoPostal());
					}
					else
					{
						m_StatusCFD = ERROR;
						m_Error = "El atributo requerido Codigo Postal del Domicilio del Destinatario del complemento de comercio exterior es incorrecto";
						return;
					}
					
				}
				
				//f. Información del Nodo cce:Mercancias:Mercancia
				JComercioExteriorDetSet detSet = new JComercioExteriorDetSet(request,"VENTA");
				detSet.m_Where = "ID_VC = '" + idfact + "'";
				detSet.m_OrderBy = "Partida ASC";
				detSet.Open();
				
				if(detSet.getNumRows() > 0)
				{
					Element Mercancias = new Element("Mercancias", m_nscce);
					ComercioExterior.addContent(Mercancias);
					
					for(int j = 0; j < detSet.getNumRows(); j++)
					{ 
						Element Mercancia = new Element("Mercancia", m_nscce);
						//1. NoIdentificacion
						Mercancia.setAttribute("NoIdentificacion", xmlse(detSet.getAbsRow(j).getNoIdentificacion()));
						m_CadenaOriginalComExt += "|" + xmlse(detSet.getAbsRow(j).getNoIdentificacion());
						//2. FraccionArancelaria
						Mercancia.setAttribute("FraccionArancelaria", xmlse(detSet.getAbsRow(j).getFraccionArancelaria()));
						m_CadenaOriginalComExt += "|" + xmlse(detSet.getAbsRow(j).getFraccionArancelaria());
						//3. CantidadAduana
						Mercancia.setAttribute("CantidadAduana", c3d(detSet.getAbsRow(j).getCantidadAduana()));
						m_CadenaOriginalComExt += "|" + c3d(detSet.getAbsRow(j).getCantidadAduana());
						//4. UnidadAduana
						Mercancia.setAttribute("UnidadAduana", Integer.toString(detSet.getAbsRow(j).getUnidadAduana()));
						m_CadenaOriginalComExt += "|" + Integer.toString(detSet.getAbsRow(j).getUnidadAduana());
						//5. ValorUnitarioAduana
						Mercancia.setAttribute("ValorUnitarioAduana", c2d(detSet.getAbsRow(j).getValorUnitarioAduana()));
						m_CadenaOriginalComExt += "|" + c2d(detSet.getAbsRow(j).getValorUnitarioAduana());
						//6. ValorDolares
						Mercancia.setAttribute("ValorDolares", c2d(detSet.getAbsRow(j).getValorDolares()));
						m_CadenaOriginalComExt += "|" + c2d(detSet.getAbsRow(j).getValorDolares());
						
						//g. Información del Nodo cce:DescripcionesEspecificas
						JComercioExteriorDetDescEspSet espSet = new JComercioExteriorDetDescEspSet(request,"VENTA");
						espSet.m_Where = "ID_VC = '" + idfact + "' and Partida = '" + detSet.getAbsRow(j).getPartida() + "'";
						espSet.m_OrderBy = "Descripcion ASC";
						espSet.Open();
						
						for(int k = 0; k < espSet.getNumRows(); k++)
						{
							if(!espSet.getAbsRow(k).getMarca().equals("") || !espSet.getAbsRow(k).getModelo().equals("") ||
									!espSet.getAbsRow(k).getSubModelo().equals("") || !espSet.getAbsRow(k).getNumeroSerie().equals(""))
							{
								Element DescripcionesEspecificas = new Element("DescripcionesEspecificas", m_nscce);
								//1. Marca
								if(!espSet.getAbsRow(k).getMarca().equals(""))
								{
									DescripcionesEspecificas.setAttribute("Marca", xmlse(espSet.getAbsRow(k).getMarca()));
									m_CadenaOriginalComExt += "|" + xmlse(espSet.getAbsRow(k).getMarca());
								}
								else
								{
									m_StatusCFD = ERROR;
									m_Error = "El atributo requerido Marca de las descripciones específicas de la mercancía " + detSet.getAbsRow(j).getNoIdentificacion() + " del complemento de comercio exterior es incorrecto";
									return;
								}
								//2. Modelo
								if(!espSet.getAbsRow(k).getModelo().equals(""))
								{
									DescripcionesEspecificas.setAttribute("Modelo", xmlse(espSet.getAbsRow(k).getModelo()));
									m_CadenaOriginalComExt += "|" + xmlse(espSet.getAbsRow(k).getModelo());
								}
								//3. SubModelo
								if(!espSet.getAbsRow(k).getSubModelo().equals(""))
								{
									DescripcionesEspecificas.setAttribute("SubModelo", xmlse(espSet.getAbsRow(k).getSubModelo()));
									m_CadenaOriginalComExt += "|" + xmlse(espSet.getAbsRow(k).getSubModelo());
								}
								//4. NumeroSerie
								if(!espSet.getAbsRow(k).getNumeroSerie().equals(""))
								{
									DescripcionesEspecificas.setAttribute("NumeroSerie", xmlse(espSet.getAbsRow(k).getNumeroSerie()));
									m_CadenaOriginalComExt += "|" + xmlse(espSet.getAbsRow(k).getNumeroSerie());
								}
								Mercancia.addContent(DescripcionesEspecificas);
							}
						}
						
						Mercancias.addContent(Mercancia);
					}
				}
				m_Comprobante.addContent(Complemento);
			}
			////////// Addenda No lo maneja forseti
			if(bCce)
				m_CadenaOriginal += m_CadenaOriginalComExt;
			
			m_CadenaOriginal += "||";
			m_LineaRep[8] = "1"; // Estado del comprobante 1 Vigente
			m_LineaRep[10] = ""; m_LineaRep[11] = ""; m_LineaRep[12] = ""; //Informacion aduanera (No lo maneja forseti)
				
			generarCFDI(request, nomar, idfact, "");
				if(m_StatusCFD == OKYDOKY)
					tfd = 1;
			
		} // Fin tfd = 1
			
		if(tfd == 1)
		{
			generarTFD(request, nomar, idfact, "");
			if(m_StatusCFD == OKYDOKY)
				tfd = 2;
			
		}	// Fin tfd = 0
				
		if(tfd == 2)
		{
			String formato, SQLCab, SQLDet;
			if(tipo.equals("FACTURAS"))
			{
				formato = forSet.getAbsRow(0).getFormato();
				SQLCab = "select * from view_ventas_facturas_impcab where ID_Factura = " + idfact;
				SQLDet = "select * from view_ventas_facturas_impdet where ID_Factura = " + idfact;
			}
			else if(tipo.equals("REMISIONES"))
			{
				formato = forSet.getAbsRow(0).getFmt_Remision();
				SQLCab = "select * from view_ventas_remisiones_impcab where ID_Remision = " + idfact;
				SQLDet = "select * from view_ventas_remisiones_impdet where ID_Remision = " + idfact;
			}
			else 
			{
				formato = forSet.getAbsRow(0).getFmt_Devolucion();
				SQLCab = "select * from view_ventas_devoluciones_impcab where ID_Devolucion = " + idfact;
				SQLDet = "select * from view_ventas_devoluciones_impdet where ID_Devolucion = " + idfact;
			}
		
			if(formato == null || formato.equals("null") || formato.equals("") || formato.equals("NULL"))
			{
				m_StatusCFD = JForsetiCFD.ERROR;
				m_Error = "No existe formato para la generaci&oacute;n del PDF del CFDI";
				return;
			}
			else
			{
				// Primero verifica el formato para el PDF
				JPublicFormatosSetV2 fi = new JPublicFormatosSetV2(request);
				fi.m_Where = "ID_Formato = '" + JUtil.p(formato) + "'";
				fi.Open();
				if(fi.getNumRows() < 1)
				{
					m_StatusCFD = JForsetiCFD.ERROR;
					m_Error = "ERROR: El formato para la generación del PDF del CFDI no existe o es Nulo";
					return;
				}
				
				generarPDF(request, response, nomar, idfact, "", SQLCab, SQLDet, formato);
				if(m_StatusCFD == OKYDOKY)
					tfd = 3;
				
			}
			
			
		} // Fin tfd == 2
		
		
	}
	
	public void generarTraslado(HttpServletRequest request, HttpServletResponse response, String tipo, int idfact, JAlmacenesMovimSetIdsV2 forSet, byte tfd) 
		throws ServletException, IOException
	{
		String complementos [] = new String [0];
		comprobanteNuevo(complementos); // "TRS"
		
		if(tfd == 0) // significa que no se ha hecho nada al registro
		{
			JCFDVenFactCabGenSet set = new JCFDVenFactCabGenSet(request, tipo);
			set.m_Where = "ID_Factura = '" + idfact + "'";
			set.Open();
			if(set.getAbsRow(0).getCFD() == false)
			{
				m_StatusCFD = NULO;
				return; // no necesita sellarse
			}
			else if(cargarInfoPac(request) == false)
			{
				return;
			}
			
			if(VerificarDatosDeEmisor(request, set.getAbsRow(0).getCFD_ArchivoCertificado(), set.getAbsRow(0).getCFD_ArchivoLLave(), set.getAbsRow(0).getCFD_NoCertificado(), set.getAbsRow(0).getCFD_CaducidadCertificado(),
					set.getAbsRow(0).getCFD_Folio(), set.getAbsRow(0).getCFD_FolioINI(), set.getAbsRow(0).getCFD_FolioFIN(), "TRS", idfact) == false)
				return;
		
			// Ahora ya se han hecho las verificaciones, procede a generar la carta porte digital
			Calendar fecha = Calendar.getInstance();
			String condicionesDePago = null;
			Float descuento = null;
			//String metodoDePago = "No Identificado";
			String LugarExpedicion;
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(set.getAbsRow(0).getCFD_Estado()) + "' and CodPais3 = '" + JUtil.p(set.getAbsRow(0).getCFD_Pais()) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(set.getAbsRow(0).getCFD_Estado());
			}
			else
			{
				JSatEstadosSet est = new JSatEstadosSet(request);
				est.m_Where = "CodEstado = '" + JUtil.p(m_CFD_Estado) + "' and CodPais3 = '" + JUtil.p(m_CFD_Pais) + "'";
				est.Open();
				if(est.getNumRows() > 0)
					LugarExpedicion = xmlse(est.getAbsRow(0).getNombre());
				else
					LugarExpedicion = xmlse(m_CFD_Estado);
			}
			// Empieza por el elemento comprobante
			// Los folios son generales
			String tipoCFD = null;
			//if(tipo.equals("TRASPASOS"))
			//{
			tipoCFD = "traslado";
			m_LineaRep[9] = "T"; // Efecto del comprobante T Traslado
			//}
		
			if(!generarElementoComprobante("TRS", set.getAbsRow(0).getCFD_Serie(), set.getAbsRow(0).getCFD_Folio(), fecha, set.getAbsRow(0).getCFD_NoAprobacion(), set.getAbsRow(0).getCFD_AnoAprobacion(),
					tipoCFD,"Pago en una sola exhibición",condicionesDePago,set.getAbsRow(0).getImporte(),descuento,null,null,set.getAbsRow(0).getTotal(),m_NoCertificado,m_Certificado,set.getAbsRow(0).getMetodoDePago(),LugarExpedicion))
				return;
		
			//////////////////////////////////////// AHORA CONJUNTA ELEMENTO EMISOR //////////////////////////////////////////////
			if(!generarElementoEmisor(m_CFD_RFC, xmlse(m_CFD_Nombre), xmlse(m_CFD_Calle), xmlse(m_CFD_NoExt), xmlse(m_CFD_NoInt), xmlse(m_CFD_Colonia), xmlse(m_CFD_Localidad), 
					xmlse(m_CFD_Municipio), xmlse(m_CFD_Estado), xmlse(m_CFD_Pais), m_CFD_CP))
				return;
		
			// Ahora los datos de expedicion del documento, solo que sea expedicion verdadera
			if(set.getAbsRow(0).getCFD_ID_Expedicion() > 0)
			{
				if(!generarElementoExpedidoEn(xmlse(set.getAbsRow(0).getCFD_Calle()), xmlse(set.getAbsRow(0).getCFD_NoExt()), xmlse(set.getAbsRow(0).getCFD_NoInt()),
						xmlse(set.getAbsRow(0).getCFD_Colonia()), xmlse(set.getAbsRow(0).getCFD_Localidad()), xmlse(set.getAbsRow(0).getCFD_Municipio()), xmlse(set.getAbsRow(0).getCFD_Estado()), 
							xmlse(set.getAbsRow(0).getCFD_Pais()), set.getAbsRow(0).getCFD_CP()))
					return;
			
			}
			// Ahora el regimen fiscal
			if(m_CFD_Regimen != null && !m_CFD_Regimen.equals(""))
			{
				String[] regimen = m_CFD_Regimen.split(",");
				Element Emisor = m_Comprobante.getChild("Emisor",m_ns);
				 
				// En este momento tenemos un array en el que cada elemento es un color.
				for (int i = 0; i < regimen.length; i++) 
				{
					System.out.println(regimen[i]);
					if(!generarElementoRegimenFiscal(Emisor, xmlse(regimen[i])))
						return;
				}
								
			}
			else
			{
				m_StatusCFD = ERROR;
				m_Error = "No existen regimenes fiscales de origen en CFD_RegimenFiscal de la tabla TBL_BD";
				return;
			}
			
			////////////////////////////////////////AHORA CONJUNTA ELEMENTO RECEPTOR //////////////////////////////////////////////
			//System.out.println("ELEMENTO RECEPTOR CLIENTES " + set.getAbsRow(0).getID_Cliente());
			if(!generarElementoReceptor(m_CFD_RFC, xmlse(m_CFD_Nombre), xmlse(set.getAbsRow(0).getCalle()), xmlse(set.getAbsRow(0).getNoExt()), xmlse(set.getAbsRow(0).getNoInt()),
					xmlse(set.getAbsRow(0).getColonia()), xmlse(set.getAbsRow(0).getLocalidad()), xmlse(set.getAbsRow(0).getMunicipio()), xmlse(set.getAbsRow(0).getEstado()), 
					xmlse(set.getAbsRow(0).getPais()), set.getAbsRow(0).getCP()))
				return;
			/////////////////////////// AHORA LOS CONCEPTOS ////////////////////////////////////
			JVentasFactSetDetV2 det = new JVentasFactSetDetV2(request,tipo);
			det.m_Where = "ID_Factura = '" + idfact + "'";
			det.m_OrderBy = "Partida ASC";
			det.Open();
		
			Element Conceptos = new Element("Conceptos", m_ns);
		
			for(int i = 0; i < det.getNumRows(); i++)
			{
				float valorUnitario = 0f;
				float importe = 0f;
				if(!generarElementoConcepto(Conceptos, det.getAbsRow(i).getCantidad(),det.getAbsRow(i).getID_UnidadSalida(),det.getAbsRow(i).getID_Prod(),
						det.getAbsRow(i).getDescripcion(), valorUnitario, importe))
					return;
			}
			m_Comprobante.addContent(Conceptos);
		
			///////////////////////////////////// IMPUESTOS /////////////////////////////////////////////////////
			Element Impuestos = new Element("Impuestos",m_ns);
			m_LineaRep[7] = "0.000000";
			
			//Cartas porte no maneja impuestos retenidos ni trasladados
			//m_CadenaOriginal += "|" + c4d(set.getAbsRow(0).getIVA()/* + set.getAbsRow(0).getIEPS()*/);
			m_Comprobante.addContent(Impuestos);
		
		
			////////// Complemento No lo maneja forseti
			////////// Addenda No lo maneja forseti
		
			m_CadenaOriginal += "||";
			m_LineaRep[8] = "1"; // Estado del comprobante 1 Vigente
			m_LineaRep[10] = ""; m_LineaRep[11] = ""; m_LineaRep[12] = ""; //Informacion aduanera (No lo maneja forseti)
		
			//System.out.println(getCadenaOriginalUTF8());
			//System.out.println(getLineaReporteMensual());
			generarCFDI(request, "TRS", idfact, "");
			if(m_StatusCFD == OKYDOKY)
				tfd = 1;
			
		} // Fin tfd = 0
		
		if(tfd == 1)
		{
			generarTFD(request, "TRS", idfact, "");
			if(m_StatusCFD == OKYDOKY)
				tfd = 2;
		}
		
		if(tfd == 2)
		{
			String formato, SQLCab, SQLDet;
			formato = forSet.getAbsRow(0).getFmt_Traspasos();
			SQLCab = "select * from view_invserv_traspasos_impcab where ID_Movimiento = " + idfact;
			SQLDet = "select * from view_invserv_traspasos_impdet where ID_Movimiento = " + idfact;
					
			if(formato == null || formato.equals("null") || formato.equals("") || formato.equals("NULL"))
			{
				m_StatusCFD = JForsetiCFD.ERROR;
				m_Error = "No existe formato para la generaci&oacute;n del PDF del CFDI";
				return;
			}
			else
			{
				// Primero verifica el formato para el PDF
				JPublicFormatosSetV2 fi = new JPublicFormatosSetV2(request);
				fi.m_Where = "ID_Formato = '" + JUtil.p(formato) + "'";
				fi.Open();
				if(fi.getNumRows() < 1)
				{
					m_StatusCFD = JForsetiCFD.ERROR;
					m_Error = "ERROR: El formato para la generaci&oacute; del PDF del CFDI no existe o es Nulo";
					return;
				}
				
				generarPDF(request, response, "TRS", idfact, "", SQLCab, SQLDet, formato);
				if(m_StatusCFD == OKYDOKY)
					tfd = 3;
				
			}
			
		} // Fin tfd == 2
		
	}
	
	
}
