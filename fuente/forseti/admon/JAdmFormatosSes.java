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
package forseti.admon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import forseti.JAccesoBD;
import forseti.JFsiMetaDatos;
import forseti.JSesionRegsObjs;
import forseti.JUtil;
import forseti.sets.JFormatosTiposSet;

public class JAdmFormatosSes extends JSesionRegsObjs
{
	private String m_Tipo;
	private String m_ID_Formato;
	private String m_Descripcion;
	private int m_VentanaHW;
	private int m_VentanaVW;
	private float m_Cab;
	private float m_DetIni;
	private float m_DetAlt;
	private short m_DetNum;

	private String m_TituloFuente;
	private String m_TituloGrosor;
	private String m_TituloEstilo;
	private String m_TituloTam;
	private String m_EtiquetaFuente;
	private String m_EtiquetaGrosor;
	private String m_EtiquetaEstilo;
	private String m_EtiquetaTam;
	private String m_CabeceroFuente;
	private String m_CabeceroGrosor;
	private String m_CabeceroEstilo;
	private String m_CabeceroTam;
	private String m_DetalleFuente;
	private String m_DetalleGrosor;
	private String m_DetalleEstilo;
	private String m_DetalleTam;
	
	private String m_EtiquetaCab;
	private String m_EtiquetaDet;
	private String m_TipoColCab;
	private String m_TipoColDet;
	private String m_ValorValCab;
	private String m_ValorValDet;
	
	private float m_Pos_X_Cab;
	private float m_Pos_Y_Cab;
	private float m_Anc_Cab;
	private float m_Alt_Cab;
	private String m_Hor_Cab;
	private String m_Ver_Cab;
	private boolean m_Fin;
	private float m_Pos_X_Det;
	private float m_Pos_Y_Det;
	private float m_Anc_Det;
	private float m_Alt_Det;
	private String m_Hor_Det;
	private String m_Ver_Det;
	
	private String m_ImpCab;
	private String m_ImpDet;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_COLUMNASCab;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_COLUMNASDet;


	 
	@SuppressWarnings("rawtypes")
	public JAdmFormatosSes(HttpServletRequest request)
		throws ServletException, IOException
	{
		m_TituloFuente = "Arial, Helvetica, sans-serif";
		m_TituloGrosor = "bold";
		m_TituloEstilo = "italic";
		m_TituloTam = "14pt";

		m_EtiquetaFuente = "Arial, Helvetica, sans-serif";
		m_EtiquetaGrosor = "bold";
		m_EtiquetaEstilo = "normal";
		m_EtiquetaTam = "10pt";
		
		m_CabeceroFuente = "Arial, Helvetica, sans-serif";
		m_CabeceroGrosor = "bold";
		m_CabeceroEstilo = "normal";
		m_CabeceroTam = "8pt";
		
		m_DetalleFuente = "Arial, Helvetica, sans-serif";
		m_DetalleGrosor = "bold";
		m_DetalleEstilo = "normal";
		m_DetalleTam = "6pt";

		m_Tipo = "CONT_POLIZAS";
		m_ID_Formato = "";
		m_Descripcion = "";
		m_VentanaHW = 800;
		m_VentanaVW = 600;
		m_Cab = 259.5F;
		m_DetIni = 100;
		m_DetAlt = 5;
		m_DetNum = 15;
		
		m_ValorValCab = "";
		m_ValorValDet = "";
		m_EtiquetaCab = "FSI_ETIQUETA";
		m_EtiquetaDet = "FSI_ETIQUETA";
		m_TipoColCab = "ESP";
		m_TipoColDet = "ESP";
		m_ImpCab = "";
		m_ImpDet = "";
		m_COLUMNASCab = new Vector();
		m_COLUMNASDet = new Vector();
		
		m_Pos_X_Cab = 0.0F;
		m_Pos_Y_Cab = 0.0F;
		m_Anc_Cab = 0.0F;
		m_Alt_Cab = 0.0F;
		m_Hor_Cab = "left";
		m_Ver_Cab = "top";
		m_Fin = false;
		m_Pos_X_Det = 0.0F;
		m_Pos_Y_Det = 0.0F;
		m_Anc_Det = 0.0F;
		m_Alt_Det = 0.0F;
		m_Hor_Det = "left";
		m_Ver_Det = "top";

		CargarVista(request);
	}
	
	 
	@SuppressWarnings("rawtypes")
	public void resetear(HttpServletRequest request)
		throws ServletException, IOException
	{
		m_TituloFuente = "Arial, Helvetica, sans-serif";
		m_TituloGrosor = "bold";
		m_TituloEstilo = "italic";
		m_TituloTam = "14pt";
	
		m_EtiquetaFuente = "Arial, Helvetica, sans-serif";
		m_EtiquetaGrosor = "bold";
		m_EtiquetaEstilo = "normal";
		m_EtiquetaTam = "10pt";
		
		m_CabeceroFuente = "Arial, Helvetica, sans-serif";
		m_CabeceroGrosor = "bold";
		m_CabeceroEstilo = "normal";
		m_CabeceroTam = "8pt";
		
		m_DetalleFuente = "Arial, Helvetica, sans-serif";
		m_DetalleGrosor = "bold";
		m_DetalleEstilo = "normal";
		m_DetalleTam = "6pt";
	
		m_Tipo = "CONT_POLIZAS";
		m_ID_Formato = "";
		m_Descripcion = "";
		m_VentanaHW = 800;
		m_VentanaVW = 600;
		m_Cab = 259.5F;
		m_DetIni = 100;
		m_DetAlt = 5;
		m_DetNum = 15;
		
		m_ValorValCab = "";
		m_ValorValDet = "";
		m_EtiquetaCab = "FSI_ETIQUETA";
		m_EtiquetaDet = "FSI_ETIQUETA";
		m_TipoColCab = "ESP";
		m_TipoColDet = "ESP";
		m_ImpCab = "";
		m_ImpDet = "";
		m_COLUMNASCab = new Vector();
		m_COLUMNASDet = new Vector();
	
		m_Pos_X_Cab = 0.0F;
		m_Pos_Y_Cab = 0.0F;
		m_Anc_Cab = 0.0F;
		m_Alt_Cab = 0.0F;
		m_Hor_Cab = "left";
		m_Ver_Cab = "top";
		m_Fin = false;
		m_Pos_X_Det = 0.0F;
		m_Pos_Y_Det = 0.0F;
		m_Anc_Det = 0.0F;
		m_Alt_Det = 0.0F;
		m_Hor_Det = "left";
		m_Ver_Det = "top";

		CargarVista(request);
		
		super.resetear();
	}
	
	public JAdmFormatosSesPart getPartida(int ind)
	{
		return (JAdmFormatosSesPart)m_Partidas.elementAt(ind);
	}
	
	public JAdmFormatosSesPart getObjeto(int ind)
	{
		return (JAdmFormatosSesPart)m_Objetos.elementAt(ind);
	}

	public void preEditaPartida(String tipo, int indPartida)
	{
		if(tipo.equals("Cab"))
		{
			JAdmFormatosSesPart part = (JAdmFormatosSesPart) m_Partidas.elementAt(indPartida);
			m_EtiquetaCab = part.getEtiqueta();
			m_ValorValCab = part.getValor();
			m_Pos_X_Cab = part.getX();
			m_Pos_Y_Cab = part.getY();
			m_Anc_Cab = part.getAnc();
			m_Alt_Cab = part.getAlt();
			m_Hor_Cab = part.getHor();
			m_Ver_Cab = part.getVer();
			
			cambioEtiqueta("Cab");
		}
		else
		{
			JAdmFormatosSesPart part = (JAdmFormatosSesPart) m_Objetos.elementAt(indPartida);
			m_EtiquetaDet = part.getEtiqueta();
			m_ValorValDet = part.getValor();
			m_Pos_X_Det = part.getX();
			m_Pos_Y_Det = part.getY();
			m_Anc_Det = part.getAnc();
			m_Alt_Det = part.getAlt();
			m_Hor_Det = part.getHor();
			m_Ver_Det = part.getVer();
			
			cambioEtiqueta("Det");
		}

	}
	
	public short editaPartida(String tipo, int indPartida, String etiqueta, String valor, float x, float y, float anc, float alt, String hor, String ver, boolean fin)
	{
		short res = -1;
		
		if(tipo.equals("Cab"))
		{
        	m_EtiquetaCab = etiqueta;
        	m_ValorValCab = valor;
        	m_Pos_X_Cab = x;
        	m_Pos_Y_Cab = y;
        	m_Anc_Cab = anc;
        	m_Alt_Cab = alt;
        	m_Hor_Cab = hor;
        	m_Ver_Cab = ver;
    
			JAdmFormatosSesPart part = (JAdmFormatosSesPart)m_Partidas.elementAt(indPartida); 
			part.setPartida(etiqueta, valor, x, y, anc, alt, hor, ver, fin);
		}
        else
        {
        	m_EtiquetaCab = etiqueta;
        	m_ValorValCab = valor;
        	m_Pos_X_Cab = x;
        	m_Pos_Y_Cab = y;
        	m_Anc_Cab = anc;
        	m_Alt_Cab = alt;
        	m_Hor_Cab = hor;
        	m_Ver_Cab = ver;
    
        	JAdmFormatosSesPart part = (JAdmFormatosSesPart)m_Objetos.elementAt(indPartida); 
			part.setPartida(etiqueta, valor, x, y, anc, alt, hor, ver, fin);
        }
        
		return res;
	
	}
	
	 
	@SuppressWarnings("unchecked")
	public short agregaPartida(String tipo, String etiqueta, String valor, float x, float y, float anc, float alt, String hor, String ver, boolean fin)
	{
		short res = -1;

		JAdmFormatosSesPart part = new JAdmFormatosSesPart(etiqueta, valor, x, y, anc, alt, hor, ver, fin);
        if(tipo.equals("Cab"))
        {
        	m_EtiquetaCab = etiqueta;
        	m_ValorValCab = valor;
        	m_Pos_X_Cab = x;
        	m_Pos_Y_Cab = y;
        	m_Anc_Cab = anc;
        	m_Alt_Cab = alt;
        	m_Hor_Cab = hor;
        	m_Ver_Cab = ver;
        	
        	m_Partidas.addElement(part);
        }
        else if(tipo.equals("Det"))
        {
        	m_EtiquetaDet = etiqueta;
        	m_ValorValDet = valor;
        	m_Pos_X_Det = x;
        	m_Pos_Y_Det = y;
        	m_Anc_Det = anc;
        	m_Alt_Det = alt;
        	m_Hor_Det = hor;
        	m_Ver_Det = ver;
        	
        	m_Objetos.addElement(part);
        }
        else if(tipo.equals("Cab_NAP"))
        	m_Partidas.addElement(part);
        else if(tipo.equals("Det_NAP"))
        	m_Objetos.addElement(part);
        
		return res;
	
	}

	public void borraPartida(String tipo, int indPartida)
	{
		if(tipo.equals("Cab"))
			super.borraPartida(indPartida);
		else
			super.borraPartidaObj(indPartida);
	}

	public String obtEncabezado(String enc)
	{
		//String fsiTitulo = "font-family: Arial, Helvetica, sans-serif; font-size: 14pt; font-style: italic; font-weight: bold;";
		String res;
		
		if(enc.equals("Titulo"))
			res = "font-family: " + m_TituloFuente + "; font-size: " + m_TituloTam + "; font-style: " + m_TituloEstilo + "; font-weight: " + m_TituloGrosor + ";";
		else if(enc.equals("Etiqueta"))
			res = "font-family: " + m_EtiquetaFuente + "; font-size: " + m_EtiquetaTam + "; font-style: " + m_EtiquetaEstilo + "; font-weight: " + m_EtiquetaGrosor + ";";
		else if(enc.equals("Cabecero"))
			res = "font-family: " + m_CabeceroFuente + "; font-size: " + m_CabeceroTam + "; font-style: " + m_CabeceroEstilo + "; font-weight: " + m_CabeceroGrosor + ";";
		else if(enc.equals("Detalle"))
			res = "font-family: " + m_DetalleFuente + "; font-size: " + m_DetalleTam + "; font-style: " + m_DetalleEstilo + "; font-weight: " + m_DetalleGrosor + ";";
		else
			res = "font-family: Arial, Helvetica, sans-serif; font-size: 10pt; font-style: italic; font-weight: bold;";
		
		return res;
	}
	
	public void estEncabezado(String enc, String str)
	{
		//String fsiTitulo = "font-family: Arial, Helvetica, sans-serif; font-size: 14pt; font-style: italic; font-weight: bold;";
		String fuente, tam, grosor, estilo;
	
		int index = str.indexOf("font-family: ");
		int index2 = str.indexOf(";", index+13);
		if(index == -1 || index2 == -1)
			return;
		fuente = str.substring(index+13, index2);
		
		index = str.indexOf("font-size: ", index2);
		index2 = str.indexOf(";", index+11);
		if(index == -1 || index2 == -1)
			return;
		tam = str.substring(index+11, index2);
		
		index = str.indexOf("font-style: ", index2);
		index2 = str.indexOf(";", index+12);
		if(index == -1 || index2 == -1)
			return;
		estilo = str.substring(index+12, index2);

		index = str.indexOf("font-weight: ", index2);
		index2 = str.indexOf(";", index+13);
		if(index == -1 || index2 == -1)
			return;
		grosor = str.substring(index+13, index2);
		
		if(enc.equals("Titulo"))
		{
			m_TituloFuente = fuente;
			m_TituloTam = tam;
			m_TituloGrosor = grosor;
			m_TituloEstilo = estilo;
		}
		else if(enc.equals("Etiqueta"))
		{
			m_EtiquetaFuente = fuente;
			m_EtiquetaTam = tam;
			m_EtiquetaGrosor = grosor;
			m_EtiquetaEstilo = estilo;
		}
		else if(enc.equals("Cabecero"))
		{
			m_CabeceroFuente = fuente;
			m_CabeceroTam = tam;
			m_CabeceroGrosor = grosor;
			m_CabeceroEstilo = estilo;
		}
		else if(enc.equals("Detalle"))
		{
			m_DetalleFuente = fuente;
			m_DetalleTam = tam;
			m_DetalleGrosor = grosor;
			m_DetalleEstilo = estilo;
		}
		

	}
	
	public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	{
		short res = -1;
		  
		if( request.getParameter("idformato") != null && request.getParameter("tipo") != null && 
			request.getParameter("descripcion") != null && request.getParameter("ventana_hw") != null && 
          	request.getParameter("ventana_vw") != null && request.getParameter("cab") != null && request.getParameter("det_ini") != null && 
          	request.getParameter("det_alt") != null && request.getParameter("det_num") != null && 
          	request.getParameter("titulo_fuente") != null && request.getParameter("titulo_tam") != null && 
          	request.getParameter("titulo_grosor") != null && request.getParameter("titulo_estilo") != null && 
          	request.getParameter("etiqueta_fuente") != null && request.getParameter("etiqueta_tam") != null && 
          	request.getParameter("etiqueta_grosor") != null && request.getParameter("etiqueta_estilo") != null && 
          	request.getParameter("cabecero_fuente") != null && request.getParameter("cabecero_tam") != null && 
          	request.getParameter("cabecero_grosor") != null && request.getParameter("cabecero_estilo") != null && 
          	request.getParameter("detalle_fuente") != null && request.getParameter("detalle_tam") != null && 
          	request.getParameter("detalle_grosor") != null && request.getParameter("detalle_estilo") != null && 
     
          	!request.getParameter("idformato").equals("") && !request.getParameter("tipo").equals("") && 
			!request.getParameter("descripcion").equals("") && !request.getParameter("ventana_hw").equals("") && 
          	!request.getParameter("ventana_vw").equals("") && !request.getParameter("cab").equals("") && !request.getParameter("det_ini").equals("") && 
          	!request.getParameter("det_alt").equals("") && !request.getParameter("det_num").equals("") && 
          	!request.getParameter("titulo_fuente").equals("") && !request.getParameter("titulo_tam").equals("") && 
          	!request.getParameter("titulo_grosor").equals("") && !request.getParameter("titulo_estilo").equals("") && 
          	!request.getParameter("etiqueta_fuente").equals("") && !request.getParameter("etiqueta_tam").equals("") && 
          	!request.getParameter("etiqueta_grosor").equals("") && !request.getParameter("etiqueta_estilo").equals("") && 
          	!request.getParameter("cabecero_fuente").equals("") && !request.getParameter("cabecero_tam").equals("") && 
          	!request.getParameter("cabecero_grosor").equals("") && !request.getParameter("cabecero_estilo").equals("") && 
          	!request.getParameter("detalle_fuente").equals("") && !request.getParameter("detalle_tam").equals("") && 
          	!request.getParameter("detalle_grosor").equals("") && !request.getParameter("detalle_estilo").equals("") )
		{
		}
	    else
	    {
	    	res = 3; 
	    	sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
	        return res;
	    }
	    
		m_TituloFuente = request.getParameter("titulo_fuente");
		m_TituloTam = request.getParameter("titulo_tam");
		m_TituloGrosor = request.getParameter("titulo_grosor");
		m_TituloEstilo = request.getParameter("titulo_estilo");
		m_EtiquetaFuente = request.getParameter("etiqueta_fuente");
		m_EtiquetaTam = request.getParameter("etiqueta_tam");
		m_EtiquetaGrosor = request.getParameter("etiqueta_grosor");
		m_EtiquetaEstilo = request.getParameter("etiqueta_estilo");
		m_CabeceroFuente = request.getParameter("cabecero_fuente");
		m_CabeceroTam = request.getParameter("cabecero_tam");
		m_CabeceroGrosor = request.getParameter("cabecero_grosor");
		m_CabeceroEstilo = request.getParameter("cabecero_estilo");
		m_DetalleFuente = request.getParameter("detalle_fuente");
		m_DetalleTam = request.getParameter("detalle_tam");
		m_DetalleGrosor = request.getParameter("detalle_grosor");
		m_DetalleEstilo = request.getParameter("detalle_estilo");

		m_ID_Formato = request.getParameter("idformato");
		m_Tipo = request.getParameter("tipo");
		m_Descripcion = request.getParameter("descripcion");
		m_VentanaHW = Integer.parseInt(request.getParameter("ventana_hw"));
		m_VentanaVW = Integer.parseInt(request.getParameter("ventana_vw"));
		m_Cab = Float.parseFloat(request.getParameter("cab"));
		m_DetIni = Float.parseFloat(request.getParameter("det_ini"));
		m_DetAlt = Float.parseFloat(request.getParameter("det_alt"));
		m_DetNum = Short.parseShort(request.getParameter("det_num"));
		
	    
		return res;
	}
	
	public short cambioTipo(HttpServletRequest request, StringBuffer sb_mensaje) 
  		throws ServletException, IOException
	{
		short res = -1;
		  
		if(request.getParameter("tipo") != null && !request.getParameter("tipo").equals(""))
		{
		}
	    else
	    {
	    	res = 3; 
	    	sb_mensaje.append(JUtil.Msj("CEF", "ADM_FORMATOS", "DLG", "MSJ-PROCERR"));//"ERROR: No se ha mandado el tipo de formato<br>");
	        return res;
	    }
		
	    if(m_Partidas.size()  > 0 || m_Objetos.size()  > 0)
	    {
	    	super.resetear();
	     	res = 1;
	    	sb_mensaje.append(JUtil.Msj("CEF", "ADM_FORMATOS", "DLG", "MSJ-PROCERR",2));//"PRECAUCION: El tipo de formato ha cambiado, esto generó que las paridas del cabecero y del detalle se hayan borrado. Para que esto no te vuelva a suceder, primero debes establecer el tipo de formato, y luego agregar las partidas.");
	    }

		m_Tipo = request.getParameter("tipo");
	  	m_ImpCab = "";
	  	m_ImpDet = "";
	  	
	   	m_COLUMNASCab.removeAllElements();
	   	m_COLUMNASDet.removeAllElements();
		
		CargarVista(request);
		
		return res;
	}

	public void RecargarVista(HttpServletRequest request)
		throws ServletException, IOException
	{
	    // Aqui va codigo del set de las vistas de impcab e impdet	
		m_COLUMNASCab.removeAllElements();
		m_COLUMNASDet.removeAllElements();
		
		CargarVista(request);
	}
	
	public void CargarVista(HttpServletRequest request)
 		throws ServletException, IOException
	{
	    // Aqui va codigo del set de las vistas de impcab e impdet	
		JFormatosTiposSet set = new JFormatosTiposSet(request);
		set.m_Where = "ID_Tipo = '" + JUtil.p(m_Tipo) + "'";
		set.ConCat(true);
		set.Open();
		//System.out.println(m_Tipo);
		m_ImpCab = set.getAbsRow(0).getImpCab();
		m_ImpDet = set.getAbsRow(0).getImpDet();
		
		CargarColumnas(request);
	}
	
	private void cambioEtiqueta(String tipo) 
	{
		if(tipo.equals("Cab"))
		{
			if(m_EtiquetaCab.equals("FSI_ETIQUETA") || m_EtiquetaCab.equals("FSI_TITULO"))
				m_TipoColCab = "ESP";
			else if(m_EtiquetaCab.equals("FSI_LH"))
				m_TipoColCab = "LH";
			else
			{
				m_TipoColCab = getTipoCol(m_EtiquetaCab, m_COLUMNASCab);
			}
		}
		else if(tipo.equals("Det"))
		{
			if(m_EtiquetaDet.equals("FSI_ETIQUETA") || m_EtiquetaDet.equals("FSI_TITULO"))
				m_TipoColDet = "ESP";
			else if(m_EtiquetaDet.equals("FSI_LH"))
				m_TipoColDet = "LH";
			else
			{
				m_TipoColDet = getTipoCol(m_EtiquetaDet, m_COLUMNASDet);
			}
		}
	}
	
	
	public short cambioEtiqueta(String tipo, HttpServletRequest request, StringBuffer sb_mensaje) 
		throws ServletException, IOException
	{
		short res = -1;
		  
		
		
		if(tipo.equals("Cab"))
		{
			if(request.getParameter("etiqueta") != null && !request.getParameter("etiqueta").equals(""))
			{
				m_EtiquetaCab = request.getParameter("etiqueta");
				if(m_EtiquetaCab.equals("FSI_ETIQUETA") || m_EtiquetaCab.equals("FSI_TITULO"))
					m_TipoColCab = "ESP";
				else if(m_EtiquetaCab.equals("FSI_LH"))
					m_TipoColCab = "LH";
				else
				{
					
					m_TipoColCab = getTipoCol(m_EtiquetaCab, m_COLUMNASCab);
				}
			}
		    else
		    {
		    	res = 3; 
		    	sb_mensaje.append(JUtil.Msj("CEF", "ADM_FORMATOS", "DLG", "MSJ-PROCERR",3));//"ERROR: No se ha mandado la etiqueta del cabecero<br>");
		        return res;
		    }
			
			
		}
		else if(tipo.equals("Det"))
		{
			if(request.getParameter("etiqueta_det") != null && !request.getParameter("etiqueta_det").equals(""))
			{
				m_EtiquetaDet = request.getParameter("etiqueta_det");
				if(m_EtiquetaDet.equals("FSI_ETIQUETA") || m_EtiquetaDet.equals("FSI_TITULO"))
					m_TipoColDet = "ESP";
				else if(m_EtiquetaDet.equals("FSI_LH"))
					m_TipoColDet = "LH";
				else
				{
					m_TipoColDet = getTipoCol(m_EtiquetaDet, m_COLUMNASDet);
				}
			}
		    else
		    {
		    	res = 3; 
		    	sb_mensaje.append(JUtil.Msj("CEF", "ADM_FORMATOS", "DLG", "MSJ-PROCERR",4));//"ERROR: No se ha mandado la etiqueta del detalle<br>");
		        return res;
		    }

		}
		
		return res;
	}

	 
	@SuppressWarnings("rawtypes")
	private String getTipoCol(String etiqueta, Vector COLUMNAS)
	{
		String res = "";
		
		for(int i = 0; i < COLUMNAS.size(); i++)
		{
			JFsiMetaDatos fsimd = (JFsiMetaDatos)COLUMNAS.get(i);
			if(fsimd.getNombreCol().equals(etiqueta))
			{
				System.out.println(fsimd.getNombreTipoCol());
				res = JUtil.obtTipoColumnaRep(fsimd.getNombreTipoCol());
				break;
			}
		}
				
		return res;
	}
	
	 
	@SuppressWarnings("unchecked")
	public void CargarColumnas(HttpServletRequest request)
	 	throws ServletException, IOException
	{
	    try
	    {
	       Connection con = JAccesoBD.getConexionSes(request);
	       Statement s = con.createStatement();
	       ResultSet rs = s.executeQuery("select * from " + JUtil.p(m_ImpCab) + " limit 1");
	       ResultSetMetaData rsmd = rs.getMetaData();
	       
	       for(int i = 1; i <= rsmd.getColumnCount(); i++)
	       {
	          JFsiMetaDatos pMD = new JFsiMetaDatos(
	                 rsmd.getColumnName(i), rsmd.getPrecision(i), rsmd.getScale(i),
	                       rsmd.getTableName(i), rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
	          m_COLUMNASCab.addElement(pMD);
	       }
	       
	       rs = s.executeQuery("select * from " + JUtil.p(m_ImpDet) + " limit 1");
	       rsmd = rs.getMetaData();
	       
	       for(int i = 1; i <= rsmd.getColumnCount(); i++)
	       {
	          JFsiMetaDatos pMD = new JFsiMetaDatos(
	                 rsmd.getColumnName(i), rsmd.getPrecision(i), rsmd.getScale(i),
	                       rsmd.getTableName(i), rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
	          m_COLUMNASDet.addElement(pMD);
	       }
	    
	       JAccesoBD.liberarConexion(con);
	        
	    }
	    catch(Throwable e)
        {
	       System.out.println("ERROR EN COLUMNAS: " + e.getMessage());
        }		
	}
	
	public int getNumColumnasCab()
	{
		return m_COLUMNASCab.size();
	}
	
	public JFsiMetaDatos getColumnaCab(int ind)
	{
		return (JFsiMetaDatos)m_COLUMNASCab.get(ind);
	}
	
	public int getNumColumnasDet()
	{
		return m_COLUMNASDet.size();
	}
	
	public JFsiMetaDatos getColumnaDet(int ind)
	{
		return (JFsiMetaDatos)m_COLUMNASDet.get(ind);
	}

	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public int getVentanaHW()
	{
		return m_VentanaHW;
	}

	public String getID_Formato() 
	{
		return m_ID_Formato;
	}

	public String getTipo() 
	{
		return m_Tipo;
	}
	
	public int getVentanaVW() 
	{
		return m_VentanaVW;
	}

	public void setDescripcion(String descripcion) 
	{
		m_Descripcion = descripcion;
	}

	public void setVentanaHW(int VentanaHW) 
	{
		m_VentanaHW = VentanaHW;
	}

	public void setID_Formato(String ID_Formato) 
	{
		m_ID_Formato = ID_Formato;
	}

	public void setTipo(String tipo) 
	{
		m_Tipo = tipo;
	}
	
	public void setVentanaVW(int VentanaVW) 
	{
		m_VentanaVW = VentanaVW;
	}

	public String getTituloEstilo() 
	{
		return m_TituloEstilo;
	}

	public String getTituloFuente() 
	{
		return m_TituloFuente;
	}

	public String getTituloGrosor() 
	{
		return m_TituloGrosor;
	}

	public String getTituloTam() 
	{
		return m_TituloTam;
	}
	
	
	public String getEtiquetaEstilo() 
	{
		return m_EtiquetaEstilo;
	}

	public String getEtiquetaFuente() 
	{
		return m_EtiquetaFuente;
	}

	public String getEtiquetaGrosor() 
	{
		return m_EtiquetaGrosor;
	}

	public String getEtiquetaTam() 
	{
		return m_EtiquetaTam;
	}

	public String getCabeceroEstilo() 
	{
		return m_CabeceroEstilo;
	}

	public String getCabeceroFuente() 
	{
		return m_CabeceroFuente;
	}

	public String getCabeceroGrosor() 
	{
		return m_CabeceroGrosor;
	}

	public String getCabeceroTam() 
	{
		return m_CabeceroTam;
	}

	public String getDetalleEstilo() 
	{
		return m_DetalleEstilo;
	}

	public String getDetalleFuente() 
	{
		return m_DetalleFuente;
	}

	public String getDetalleGrosor() 
	{
		return m_DetalleGrosor;
	}

	public String getDetalleTam() 
	{
		return m_DetalleTam;
	}

	public float getCab() 
	{
		return m_Cab;
	}

	public void setCab(float cab) 
	{
		m_Cab = cab;
	}

	public float getDetAlt() 
	{
		return m_DetAlt;
	}

	public void setDetAlt(float detAlt) 
	{
		m_DetAlt = detAlt;
	}

	public float getDetIni() 
	{
		return m_DetIni;
	}

	public void setDetIni(float detIni) 
	{
		m_DetIni = detIni;
	}

	public short getDetNum() 
	{
		return m_DetNum;
	}

	public void setDetNum(short detNum) 
	{
		m_DetNum = detNum;
	}

	public String getTipoColCab() 
	{
		return m_TipoColCab;
	}

	public void setTipoColCab(String tipoColCab) 
	{
		m_TipoColCab = tipoColCab;
	}

	public String getTipoColDet() 
	{
		return m_TipoColDet;
	}

	public void setTipoColDet(String tipoColDet) 
	{
		m_TipoColDet = tipoColDet;
	}

	public String getEtiquetaCab() 
	{
		return m_EtiquetaCab;
	}

	public void setEtiquetaCab(String etiquetaCab) 
	{
		m_EtiquetaCab = etiquetaCab;
	}

	public String getEtiquetaDet() 
	{
		return m_EtiquetaDet;
	}

	public void setEtiquetaDet(String etiquetaDet) 
	{
		m_EtiquetaDet = etiquetaDet;
	}

	public String getValorValCab() 
	{
		return m_ValorValCab;
	}

	public void setValorValCab(String valorValCab) 
	{
		m_ValorValCab = valorValCab;
	}

	public String getValorValDet() 
	{
		return m_ValorValDet;
	}

	public void setValorValDet(String valorValDet) 
	{
		m_ValorValDet = valorValDet;
	}

	public float getAlt_Cab() 
	{
		return m_Alt_Cab;
	}

	public void setAlt_Cab(float alt_Cab) 
	{
		m_Alt_Cab = alt_Cab;
	}

	public float getAlt_Det() 
	{
		return m_Alt_Det;
	}

	public void setAlt_Det(float alt_Det) 
	{
		m_Alt_Det = alt_Det;
	}

	public float getAnc_Cab() 
	{
		return m_Anc_Cab;
	}

	public void setAnc_Cab(float anc_Cab) 
	{
		m_Anc_Cab = anc_Cab;
	}

	public float getAnc_Det() 
	{
		return m_Anc_Det;
	}

	public void setAnc_Det(float anc_Det) 
	{
		m_Anc_Det = anc_Det;
	}

	public boolean getFin() 
	{
		return m_Fin;
	}

	public void setFin(boolean fin) 
	{
		m_Fin = fin;
	}

	public String getHor_Cab() 
	{
		return m_Hor_Cab;
	}

	public void setHor_Cab(String hor_Cab) 
	{
		m_Hor_Cab = hor_Cab;
	}

	public String getHor_Det() 
	{
		return m_Hor_Det;
	}

	public void setHor_Det(String hor_Det) 
	{
		m_Hor_Det = hor_Det;
	}

	public float getPos_X_Cab() 
	{
		return m_Pos_X_Cab;
	}

	public void setPos_X_Cab(float pos_X_Cab) 
	{
		m_Pos_X_Cab = pos_X_Cab;
	}

	public float getPos_X_Det() 
	{
		return m_Pos_X_Det;
	}

	public void setPos_X_Det(float pos_X_Det) 
	{
		m_Pos_X_Det = pos_X_Det;
	}

	public float getPos_Y_Cab() 
	{
		return m_Pos_Y_Cab;
	}

	public void setPos_Y_Cab(float pos_Y_Cab) 
	{
		m_Pos_Y_Cab = pos_Y_Cab;
	}

	public float getPos_Y_Det() 
	{
		return m_Pos_Y_Det;
	}

	public void setPos_Y_Det(float pos_Y_Det) 
	{
		m_Pos_Y_Det = pos_Y_Det;
	}

	public String getVer_Cab() 
	{
		return m_Ver_Cab;
	}

	public void setVer_Cab(String ver_Cab) 
	{
		m_Ver_Cab = ver_Cab;
	}

	public String getVer_Det() 
	{
		return m_Ver_Det;
	}

	public void setVer_Det(String ver_Det) 
	{
		m_Ver_Det = ver_Det;
	}
}
