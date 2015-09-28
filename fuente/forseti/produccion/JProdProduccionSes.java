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
package forseti.produccion;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import forseti.JSesionRegs;
import forseti.JUtil;

import forseti.sets.JProdEntidadesSetIdsV2;
import forseti.sets.JProdFormulasCatalogSetV2;
import forseti.sets.JProdFormulasSetV2;

public class JProdProduccionSes extends JSesionRegs
{
	  private byte m_ID_Entidad;
	  private long m_ReporteNum;
	  private Date m_Fecha; // el numero de formula de este producto
	  private String m_Concepto;
	  private boolean m_Directa;
	  private byte m_ID_BodegaMP;
	  private byte m_ID_BodegaPT;
	  private String m_BodegaPT;
	  private String m_BodegaMP;
	  private boolean m_AuditarAlm;
	  private byte m_ManejoStocks;
	  private String m_Obs;
	  
	  private String m_part_ID_Prod;
	  private String m_part_Formula;
	  private String m_part_Descripcion;
	  private String m_part_Instrucciones;
	  private String m_part_Unidad;
	  private String m_part_Lote;
	  private String m_part_Obs;
	  private float m_part_Cantidad;
	  private boolean m_part_CantLote;
	  
	  public JProdProduccionSes()
	  {

	  }
	  
	  public JProdProduccionSes(HttpServletRequest request, String ID_Entidad, String usuario)
	  {
		  JProdEntidadesSetIdsV2 set = new JProdEntidadesSetIdsV2(request,usuario,ID_Entidad);
	      set.Open();
	 
	      m_ID_Entidad = (byte)set.getAbsRow(0).getID_Entidad();
	      m_ReporteNum = set.getAbsRow(0).getDoc();
		  Calendar fecha = GregorianCalendar.getInstance();
		  m_Fecha = fecha.getTime();
	      m_ID_BodegaMP = (byte)set.getAbsRow(0).getID_BodegaMP();
	      m_ID_BodegaPT = (byte)set.getAbsRow(0).getID_BodegaPT();
	      m_BodegaMP = set.getAbsRow(0).getBodegaMP();
	      m_BodegaPT = set.getAbsRow(0).getBodegaPT();
	      m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
	      m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
		  m_Concepto = "";
		  m_Directa = false;
		  m_Obs = "";
		  
		  resetearPart();

	  }
	  
	  @SuppressWarnings({ "rawtypes" })
	  public short VerificacionesFinales(HttpServletRequest request, StringBuffer sbmensaje)
	  {
		  short res = -1;
			
		  Properties verifica = new Properties(); // La suma de los PT en formulas (Entradas en BodegaPT)
		  Properties verifica2 = new Properties(); // La suma de los Sub productos en procesos (Entradas en BodegaMP)
		  Properties verifica3 = new Properties(); // La suma de las MP en procesos (Salidas en BodegaMP)
		  
	      for(int i = 0; i < m_Partidas.size(); i++)
	      {
			  JProdProduccionSesPart part = (JProdProduccionSesPart) m_Partidas.elementAt(i);
	  	
	    	  String clave = part.getID_Prod();
				  	
			  if(verifica.get(clave) == null)
			  {
				  Float valor = new Float(part.getCantidad());
				  verifica.put(clave, valor);
			  }
			  else
			  {
				  Float cant = (Float)verifica.get(clave);
				  Float valor = new Float(part.getCantidad() + cant.floatValue());
			  	  verifica.put(clave, valor);
			  }
			  	
			  for(int j = 0; j < part.getPartidaFormula().numPartidas(); j++)
			  {
				  String clave2 = part.getPartidaFormula().getPartida(j).getID_Prod();
				  
				  if(!clave2.equals(""))
				  {
					  if(verifica2.get(clave2) == null)
					  {
						  Float valor2 = new Float(part.getPartidaFormula().getPartida(j).getCantidad());
						  verifica2.put(clave2, valor2);
					  }
					  else
					  {
						  Float cant2 = (Float)verifica2.get(clave2);
						  Float valor2 = new Float(part.getPartidaFormula().getPartida(j).getCantidad() + cant2.floatValue());
						  verifica2.put(clave2, valor2);
					  }
				  }
				  
				  for(int d = 0; d < part.getPartidaFormula().getPartida(j).numPartidas(); d++)
				  {
					 String clave3 =  part.getPartidaFormula().getPartida(j).getPartida(d).getID_Prod();
					 if(verifica3.get(clave3) == null)
					 {
						 Float valor3 = new Float(part.getPartidaFormula().getPartida(j).getPartida(d).getCantidad());
						 verifica3.put(clave3, valor3);
					 }
					 else
					 {
						 Float cant3 = (Float)verifica3.get(clave3);
						 Float valor3 = new Float(part.getPartidaFormula().getPartida(j).getPartida(d).getCantidad() + cant3.floatValue());
						 verifica3.put(clave3, valor3);
					 }
				  }
			  }
	      }
	      
	      Enumeration ProdMP = verifica3.propertyNames();
		  while(ProdMP.hasMoreElements())
		  {
		      String ID_ProdMP = (String)ProdMP.nextElement();
		      Float cantidadMP = (Float)verifica3.get(ID_ProdMP);
		      
		      res = JUtil.VerificaExistencias(request, sbmensaje, m_ID_BodegaMP, ID_ProdMP, m_AuditarAlm, cantidadMP.floatValue());
			  if(res != -1)
				  break;
		  }

		  if(res == -1) // No hubo errores 
		  {
			  Enumeration ProdSP = verifica2.propertyNames();
			  while(ProdSP.hasMoreElements())
			  {
				  String ID_ProdSP = (String)ProdSP.nextElement();
				  Float cantidadSP = (Float)verifica2.get(ID_ProdSP);
	
				  res = JUtil.VerificaStocks(request, sbmensaje, m_ID_BodegaMP, ID_ProdSP, m_ManejoStocks, cantidadSP.floatValue());
				  if(res != -1)
					  break;
			  }
			  
			  if(res == -1) // No hubo errores 
			  {
				  Enumeration ProdPT = verifica.propertyNames();
				  while(ProdPT.hasMoreElements())
				  {
					  String ID_ProdPT = (String)ProdPT.nextElement();
					  Float cantidadPT = (Float)verifica.get(ID_ProdPT);
	
					  res = JUtil.VerificaStocks(request, sbmensaje, m_ID_BodegaPT, ID_ProdPT, m_ManejoStocks, cantidadPT.floatValue());
					  if(res != -1)
						  break;
				  }
			  }
		  }
		  
		  return res;
  
	  }
	  
	  public JProdProduccionSesPart getPartida(int ind)
	  {
	    return (JProdProduccionSesPart)m_Partidas.elementAt(ind);
	  }
	  
	  public void borraPartida(int indPartida)
	  {
	    super.borraPartida(indPartida);
	  }
	  
	   
	  @SuppressWarnings("unchecked")
	  public void agregaPartida(String ID_Prod, String Descripcion, long ID_Formula, String Formula, float Cantidad, String Unidad, String Lote, boolean CantLote, String Obs, float MasMenos, int NumProc, int ActualProc, boolean Terminada, Date Fecha)
	  {
		  // Aqui aplica la partida
		  JProdProduccionSesPart part = new JProdProduccionSesPart(ID_Prod, Descripcion, ID_Formula, Formula, Cantidad, Unidad, Lote, CantLote, Obs, MasMenos, NumProc, ActualProc, Terminada, Fecha);
			m_Partidas.addElement(part);
	  }
	  
	   
	  @SuppressWarnings("unchecked")
	  public short agregaPartida(HttpServletRequest request, float cantidad, String idprod, long idformula, StringBuffer mensaje) 
	  {
		  short res = -1;
		  
	  	  JProdFormulasSetV2 set = new JProdFormulasSetV2(request);
	  	  set.m_Where = "Clave = '" + JUtil.p(request.getParameter("idprod_part")) + "' and ID_Formula = '" + idformula + "'";
	  	  set.Open();
	  	  
		  if( set.getNumRows() > 0 )
		  {
			 m_part_ID_Prod = set.getAbsRow(0).getClave();
			 m_part_Descripcion = set.getAbsRow(0).getDescripcion();
			 m_part_Formula = set.getAbsRow(0).getFormula();
			 m_part_Unidad = set.getAbsRow(0).getUnidad();
			 m_part_Lote = request.getParameter("lote");
			 m_part_Obs = request.getParameter("obs_partida");
			 m_part_CantLote = set.getAbsRow(0).getUnidadUnica();
			 float masmenos = set.getAbsRow(0).getMasMenos();
			 float rendimiento = set.getAbsRow(0).getCantidad();
			 // Ahora hace las comparaciones.
			 if(!m_part_CantLote && (cantidad < (rendimiento - masmenos) || cantidad > (rendimiento + masmenos)))
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: La cantidad, no es correcta, Esta parece ser menor o mayor que el rendimiento ( junto con sus diferencias aceptables ) de la fórmula. No se agregó la partida");
			 }
			 else
			 {
				 // Aqui aplica la partida
				 JProdProduccionSesPart part = new JProdProduccionSesPart(request, m_part_ID_Prod, m_part_Descripcion, idformula, m_part_Formula, cantidad, m_part_Unidad, m_part_Lote, m_part_CantLote, m_part_Obs, (cantidad == rendimiento ? masmenos : 0F));
				 m_Partidas.addElement(part);
				
			 }

		  }
		  else
		  {
			 resetearPart();
			 
		     res = 3;
		     mensaje.append("ERROR: No se encontró el producto especificado o la fórmula no pertenece al producto a fabricar");
		  }

		  return res;
	  }
	 
	  public boolean existeEnLista()
	  {
		  
		boolean res = false;  
	  
		for(int i = 0; i < m_Partidas.size(); i++)
		{
		  	JProdProduccionSesPart part = (JProdProduccionSesPart) m_Partidas.elementAt(i);
		  	String clave = part.getID_Prod();
		  	
		  	if(clave.compareToIgnoreCase(m_part_ID_Prod) == 0)
		  	{
		  		res = true;
		  		break;
		  	}
		}
		
		return res;
		
	  }

 
	  private void resetearPart()
	  {
		  m_part_ID_Prod = "";
		  m_part_Formula = "";
		  m_part_Descripcion = "";
		  m_part_Instrucciones = "";
		  m_part_Unidad = "";
		  m_part_Lote = "";
		  m_part_Obs = "";
		  m_part_Cantidad = 0.0F;
		  m_part_CantLote = false;
	  }
	  
	  public void resetear(HttpServletRequest request, String ID_Entidad, String usuario)
	  {
		  JProdEntidadesSetIdsV2 set = new JProdEntidadesSetIdsV2(request,usuario,ID_Entidad);
	      set.m_Where = "ID_Tipo = '1' and ID_Ususario = '" + JUtil.p(usuario) + "' and ID_Entidad = '" + JUtil.p(ID_Entidad) + "'"; // 1 es la entidad de reportes
	      set.Open();
	 
	      m_ID_Entidad = (byte)set.getAbsRow(0).getID_Entidad();
	      m_ReporteNum = set.getAbsRow(0).getDoc();
		  Calendar fecha = GregorianCalendar.getInstance();
		  m_Fecha = fecha.getTime();
	      m_ID_BodegaMP = (byte)set.getAbsRow(0).getID_BodegaMP();
	      m_ID_BodegaPT = (byte)set.getAbsRow(0).getID_BodegaPT();
	      m_BodegaMP = set.getAbsRow(0).getBodegaMP();
	      m_BodegaPT = set.getAbsRow(0).getBodegaPT();
	      m_AuditarAlm = set.getAbsRow(0).getAuditarAlm();
	      m_ManejoStocks = set.getAbsRow(0).getManejoStocks();
	      m_Concepto = "";
	      m_Directa = false;
		  m_Obs = "";
		  
		  resetearPart();
		  
		  super.resetear();
	  }
	  
	public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	{
		short res = -1;
	    
		if(request.getParameter("reporte") != null && request.getParameter("fecha") != null && request.getParameter("concepto") != null && 
			request.getParameter("idprod_part") != null && 
			!request.getParameter("reporte").equals("") && !request.getParameter("fecha").equals("") && !request.getParameter("idprod_part").equals(""))
	    {
	    }
	    else
	    {
	        res = 3; 
	        sb_mensaje.append("ERROR: Alguno de los parametros del cabecero es nulo <br>");
	        return res;
	    }
	        
	  	if(m_ID_Entidad != Byte.parseByte(JUtil.getSesion(request).getSesion("PROD_PRODUCCION").getEspecial()))
	  	{
	  	    res = 3; 
	  	    sb_mensaje.append("ERROR: La entidad de producción ya no es la misma que la inicial <br>");
	  	    return res;
	  	}
	  	  
	  	m_ReporteNum = Integer.parseInt(request.getParameter("reporte"));
	  	m_Concepto = request.getParameter("concepto");
	    m_Fecha = JUtil.estFecha(request.getParameter("fecha")); 
	  	m_Directa = (request.getParameter("directa") != null ? true : false);
	  	
	    if( !request.getParameter("idprod_part").equals(m_part_ID_Prod) )
	    {
	  	   	JProdFormulasCatalogSetV2 set = new JProdFormulasCatalogSetV2(request);
	  	   	set.m_OrderBy = "Principal DESC, ID_Formula ASC";
	  	   	set.m_Where = "ID_Prod = '" + JUtil.p(request.getParameter("idprod_part")) + "' and Status = 'V'";
			set.Open();
	  	    if(set.getNumRows() < 1 )
	  	    {
	  	        res = 1; 
	  	        sb_mensaje.append("PRECAUCION: El producto no existe o no es un producto que se produzca. Selecciona otro producto <br>");
	  	        return res;
	  	    }
	  	    
	  	    m_part_ID_Prod = request.getParameter("idprod_part");
	  	    m_part_Cantidad = set.getAbsRow(0).getCantidad();
	  	    m_part_Descripcion = set.getAbsRow(0).getDescripcion();
	  	    m_part_Instrucciones = (set.getAbsRow(0).getUnidadUnica() == true) ? "Producción inmediata: Captura la cantidad de " + set.getAbsRow(0).getUnidad() + " a fabricar" :
				"Producción por lote: La cantidad debe ser igual o similar a " + set.getAbsRow(0).getCantidad() + " " + set.getAbsRow(0).getUnidad();
	    }
	      	  
	    return res;
	}

	public void setObs(String Obs)
	{
		m_Obs = Obs;
	}
	
	public String getObs()
	{
		return m_Obs;
	}
	
	public String getDescripcion_Part()
	{
		return m_part_Descripcion;
	}
	
	public String getObs_Part()
	{
		return m_part_Obs;
	}

	public String getLote_Part()
	{
		return m_part_Lote;
	}

	public float getCantidad_Part()
	{
		return m_part_Cantidad;
	}

	public String getID_Prod_Part()
	{
		return m_part_ID_Prod;
	}

	public String getInstrucciones_Part()
	{
		return m_part_Instrucciones;
	}
	
	public boolean getAuditarAlm() 
	{
		return m_AuditarAlm;
	}

	public void setAuditarAlm(boolean auditarAlm) 
	{
		m_AuditarAlm = auditarAlm;
	}

	public String getBodegaMP() 
	{
		return m_BodegaMP;
	}

	public void setBodegaMP(String bodegaMP) 
	{
		m_BodegaMP = bodegaMP;
	}

	public String getBodegaPT() 
	{
		return m_BodegaPT;
	}

	public void setBodegaPT(String bodegaPT) 
	{
		m_BodegaPT = bodegaPT;
	}

	public Date getFecha() 
	{
		return m_Fecha;
	}

	public void setFecha(Date fecha) 
	{
		m_Fecha = fecha;
	}

	public byte getID_BodegaMP() 
	{
		return m_ID_BodegaMP;
	}

	public void setID_BodegaMP(byte bodegaMP) 
	{
		m_ID_BodegaMP = bodegaMP;
	}

	public byte getID_BodegaPT() 
	{
		return m_ID_BodegaPT;
	}

	public void setID_BodegaPT(byte bodegaPT) 
	{
		m_ID_BodegaPT = bodegaPT;
	}

	public byte getID_Entidad() 
	{
		return m_ID_Entidad;
	}

	public void setID_Entidad(byte entidad) 
	{
		m_ID_Entidad = entidad;
	}

	public byte getManejoStocks() 
	{
		return m_ManejoStocks;
	}

	public void setManejoStocks(byte manejoStocks) 
	{
		m_ManejoStocks = manejoStocks;
	}

	public String getConcepto() 
	{
		return m_Concepto;
	}

	public void setConcepto(String Concepto) 
	{
		m_Concepto = Concepto;
	}

	public long getReporteNum() 
	{
		return m_ReporteNum;
	}

	public void setReporteNum(long reporteNum) 
	{
		m_ReporteNum = reporteNum;
	}

	public void setDirecta(boolean Directa)
	{
		m_Directa = Directa;
	}
	
	public boolean getDirecta()
	{
		return m_Directa;
	}
}
