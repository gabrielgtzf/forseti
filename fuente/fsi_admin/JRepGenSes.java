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
package fsi_admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JAccesoBD;
import forseti.JBDRegistradasSet;
import forseti.JFsiMetaDatos;
import forseti.JUtil;

public class JRepGenSes 
{
	
	private String m_BD;
	private String m_BDP;
	private String m_Tipo;
	private String m_SubTipo;
	private String m_Clave;
	private boolean m_Graficar;
	
	private int m_ID_Report;
	private int m_ID_ReportPlnt;
	private String m_Descripcion;
	private int m_HW;
	private int m_VW;
	private String m_Tabla;
	private String m_Columna;
	private String m_statusFiltro;
	
	private String m_TituloFuente;
	private String m_TituloGrosor;
	private String m_TituloEstilo;
	private String m_TituloTam;
	private String m_EncL1Fuente;
	private String m_EncL1Grosor;
	private String m_EncL1Estilo;
	private String m_EncL1Tam;
	private String m_EncL2Fuente;
	private String m_EncL2Grosor;
	private String m_EncL2Estilo;
	private String m_EncL2Tam;
	private String m_EncL3Fuente;
	private String m_EncL3Grosor;
	private String m_EncL3Estilo;
	private String m_EncL3Tam;
	private String m_L1Fuente;
	private String m_L1Grosor;
	private String m_L1Estilo;
	private String m_L1Tam;
	private String m_L2Fuente;
	private String m_L2Grosor;
	private String m_L2Estilo;
	private String m_L2Tam;
	private String m_L3Fuente;
	private String m_L3Grosor;
	private String m_L3Estilo;
	private String m_L3Tam;
	private String m_CL1Fuente;
	private String m_CL1Grosor;
	private String m_CL1Estilo;
	private String m_CL1Tam;
	private String m_CL2Fuente;
	private String m_CL2Grosor;
	private String m_CL2Estilo;
	private String m_CL2Tam;
	private String m_CL3Fuente;
	private String m_CL3Grosor;
	private String m_CL3Estilo;
	private String m_CL3Tam;


	private float m_TabPrintPntL1;
	private float m_TabPrintPntCL1;
	private String m_SCL1;
	private String m_CSCL1;
	private String m_statusL1;
	private String m_statusCL1;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_prepL1;
	
	private float m_TabPrintPntL2;
	private float m_TabPrintPntCL2;
	private String m_SCL2;
	private String m_CSCL2;
	private String m_statusL2;
	private String m_statusCL2;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_prepL2;
	
	private float m_TabPrintPntL3;
	private float m_TabPrintPntCL3;
	private String m_SCL3;
	private String m_CSCL3;
	private String m_statusL3;
	private String m_statusCL3;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_prepL3;
	
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_Filtro;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsL1;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsL2;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsL3;

	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsCL1;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsCL2;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_ColsCL3;
	
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_BASES;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_TABLAS;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_COLUMNAS;
	
	private float m_AnchoColsL1;
	private float m_AnchoColsL2;
	private float m_AnchoColsL3;
	private float m_AnchoColsCL1;
	private float m_AnchoColsCL2;
	private float m_AnchoColsCL3;

	 
	@SuppressWarnings("rawtypes")
	public JRepGenSes(HttpServletRequest request, String BD)
	{
		
		//String fsiTitulo = "font-family: Arial, Helvetica, sans-serif; font-size: 14pt; font-style: italic; font-weight: bold;";
		//String fsiEncL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 10pt; font-style: normal; font-weight: bold;";
		//String fsiEncL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
		//String fsiEncL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: bold;";
		//String fsiL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
		//String fsiL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal;";
		//String fsiL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: normal;";
		//String fsiCL1 = "font-family: Arial, Helvetica, sans-serif; font-size: 9pt; font-style: normal; font-weight: bold;";
		//String fsiCL2 = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-style: normal; font-weight: bold;";
		//String fsiCL3 = "font-family: Arial, Helvetica, sans-serif; font-size: 6pt; font-style: normal; font-weight: bold;";
		 
		m_TituloFuente = "Arial, Helvetica, sans-serif";
		m_TituloGrosor = "bold";
		m_TituloEstilo = "italic";
		m_TituloTam = "14pt";

		m_EncL1Fuente = "Arial, Helvetica, sans-serif";
		m_EncL1Grosor = "bold";
		m_EncL1Estilo = "normal";
		m_EncL1Tam = "10pt";
		m_EncL2Fuente = "Arial, Helvetica, sans-serif";
		m_EncL2Grosor = "bold";
		m_EncL2Estilo = "normal";
		m_EncL2Tam = "8pt";
		m_EncL3Fuente = "Arial, Helvetica, sans-serif";
		m_EncL3Grosor = "bold";
		m_EncL3Estilo = "normal";
		m_EncL3Tam = "6pt";

		m_L1Fuente = "Arial, Helvetica, sans-serif";
		m_L1Grosor = "normal";
		m_L1Estilo = "normal";
		m_L1Tam = "10pt";
		m_L2Fuente = "Arial, Helvetica, sans-serif";
		m_L2Grosor = "normal";
		m_L2Estilo = "normal";
		m_L2Tam = "8pt";
		m_L3Fuente = "Arial, Helvetica, sans-serif";
		m_L3Grosor = "normal";
		m_L3Estilo = "normal";
		m_L3Tam = "6pt";
		
		m_CL1Fuente = "Arial, Helvetica, sans-serif";
		m_CL1Grosor = "bold";
		m_CL1Estilo = "italic";
		m_CL1Tam = "10pt";
		m_CL2Fuente = "Arial, Helvetica, sans-serif";
		m_CL2Grosor = "bold";
		m_CL2Estilo = "italic";
		m_CL2Tam = "8pt";
		m_CL3Fuente = "Arial, Helvetica, sans-serif";
		m_CL3Grosor = "bold";
		m_CL3Estilo = "italic";
		m_CL3Tam = "6pt";

		m_BD = BD;
		m_BDP = BD;
		m_Tipo = "";
		m_SubTipo = "ESP";
		m_Clave = "CVE";
		m_Graficar = false;
		m_ID_Report = 0;
		m_ID_ReportPlnt = 0;
		m_Descripcion = "Descripción";
		m_HW = 800;
		m_VW = 600;
		m_Tabla = "TBL_BD";
		m_Columna = "ID_BD";
		m_statusFiltro = "ND";
		
		m_TabPrintPntL1 = 0;
		m_TabPrintPntCL1 = 0;
		m_SCL1 = "";
		m_CSCL1 = "";
		m_statusL1 = "ND"; 
		m_statusCL1 = "ND";
		
		m_TabPrintPntL2 = 0;
		m_TabPrintPntCL2 = 0;
		m_SCL2 = "";
		m_CSCL2 = "";
		m_statusL2 = "ND"; 
		m_statusCL2 = "ND";
		
		m_TabPrintPntL3 = 0;
		m_TabPrintPntCL3 = 0;
		m_SCL3 = "";
		m_CSCL3 = "";
		m_statusL3 = "ND"; 
		m_statusCL3 = "ND";
	
		m_Filtro = new Vector();
		m_prepL1 = new Vector();
		m_prepL2 = new Vector();
		m_prepL3 = new Vector();

		m_ColsL1 = new Vector();
		m_ColsL2 = new Vector();
		m_ColsL3 = new Vector();

		m_ColsCL1 = new Vector();
		m_ColsCL2 = new Vector();
		m_ColsCL3 = new Vector();
		
		m_BASES = new Vector();
		m_TABLAS = new Vector();
		m_COLUMNAS = new Vector();
		
		m_AnchoColsL1 = 0F;
		m_AnchoColsL2 = 0F;
		m_AnchoColsL3 = 0F;
		m_AnchoColsCL1 = 0F;
		m_AnchoColsCL2 = 0F;
		m_AnchoColsCL3 = 0F;
		
		CargarBases(request);
		CargarTablas();
		CargarColumnas();
	}
	
	@SuppressWarnings("unchecked")
	public void agregaFiltro(String instructions, boolean isRange, String priDataName, String priDefault, String secDataName, String secDefault, String bindDataType, boolean fromCatalog, int ID_Catalogo) 
	{
		JRepGenSesFiltro part = new JRepGenSesFiltro(instructions, isRange, priDataName, priDefault, secDataName, secDefault, bindDataType, fromCatalog, ID_Catalogo);
		m_Filtro.addElement(part);
	}
	
	
	 
	@SuppressWarnings("unchecked")
	public void agregaColumna(String L, String ColName, String BindDataType, boolean WillShow, String Format, float Ancho, String AlinHor)
	{
	   JRepGenSesPart part = new JRepGenSesPart(ColName, BindDataType, WillShow, Format, Ancho, AlinHor);
	   
       if(L.equals("L1"))
    	   m_ColsL1.addElement(part);
       if(L.equals("L2")) 
    	   m_ColsL2.addElement(part);
       if(L.equals("L3")) 
    	   m_ColsL3.addElement(part);
       if(L.equals("CL1")) 
    	   m_ColsCL1.addElement(part);
       if(L.equals("CL2")) 
    	   m_ColsCL2.addElement(part);
       if(L.equals("CL3")) 
    	   m_ColsCL3.addElement(part);
	}
		
	public int getNumBases()
	{
		return m_BASES.size();
	}
	
	public int getNumFiltros()
	{
		return m_Filtro.size();
	}
	
	public JRepGenSesFiltro getFiltro(int ind)
	{
		return (JRepGenSesFiltro)m_Filtro.get(ind);
	}

	 
	@SuppressWarnings("unchecked")
	public void setCols(String L, ResultSetMetaData rsmd, HttpServletRequest request)
	{
        if(L.equals("L1"))
        {
        	m_AnchoColsL1 = 0F;
         	m_ColsL1.removeAllElements();
        }
        if(L.equals("L2")) 
        {
        	m_AnchoColsL2 = 0F;
         	m_ColsL2.removeAllElements();
        }
        if(L.equals("L3")) 
        {
        	m_AnchoColsL3 = 0F;
         	m_ColsL3.removeAllElements();
        }
        if(L.equals("CL1")) 
        {
        	m_AnchoColsCL1 = 0F;
        	m_ColsCL1.removeAllElements();
        }
       	if(L.equals("CL2")) 
        {
        	m_AnchoColsCL2 = 0F;
         	m_ColsCL2.removeAllElements();
        }
       	if(L.equals("CL3")) 
       	{
       		m_AnchoColsCL3 = 0F;
        	m_ColsCL3.removeAllElements();
       	}
      
		try
		{
			for(int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				String nombre = rsmd.getColumnName(i);
				String bdt = JUtil.obtTipoColumnaRep(rsmd.getColumnTypeName(i));
				boolean ws = (request.getParameter("FSI_WS_" + L + "_" + nombre) == null) ? false : true;
				String format;
				if(request.getParameter("FSI_FMT_" + L + "_" + nombre) == null ||
					(request.getParameter("FSI_FMT_" + L + "_" + nombre) != null && 
							!request.getParameter("FSI_BDT_" + L + "_" + nombre).equals(bdt))	)
				{
					if(bdt.equals("BYTE") || bdt.equals("INT"))
						format = ",|0";
					else if(bdt.equals("STRING"))
						format = "general";
					else if(bdt.equals("DECIMAL") || bdt.equals("MONEY"))
					    format = ",|.|" + Integer.toString(rsmd.getScale(i)) + "|0";
					else if(bdt.equals("TIME"))
					    format = "dd/MMM/yyyy";
					else if(bdt.equals("BOOL"))
					    format = "V_F";
					else
						format = "";
				}
				else
				{
					format = request.getParameter("FSI_FMT_" + L + "_" + nombre);
				}
			
				float anc = (request.getParameter("FSI_ANC_" + L + "_" + nombre) == null) ? JUtil.redondear((100F/rsmd.getColumnCount()), 2) : Float.parseFloat(request.getParameter("FSI_ANC_" + L + "_" + nombre));
		       
				if(L.equals("L1"))
		           	m_AnchoColsL1 += anc;
		        else if(L.equals("L2")) 
		           	m_AnchoColsL2 += anc;
		        else if(L.equals("L3")) 
		        	m_AnchoColsL3 += anc;
		        else if(L.equals("CL1")) 
		        	m_AnchoColsCL1 += anc;
		        else if(L.equals("CL2")) 
		        	m_AnchoColsCL2 += anc;
		        else if(L.equals("CL3")) 
		       		m_AnchoColsCL3 += anc;
		        
		        String alh;
				if(request.getParameter("FSI_ALH_" + L + "_" + nombre) == null)
				{
					if(bdt.equals("BYTE") || bdt.equals("INT"))
						alh = "right";
					else if(bdt.equals("STRING"))
						alh = "left";
					else if(bdt.equals("DECIMAL") || bdt.equals("MONEY"))
					    alh = "right";
					else if(bdt.equals("TIME"))
					    alh = "center";
					else
						alh = "left";
				}
				else
					alh = request.getParameter("FSI_ALH_" + L + "_" + nombre);
				
				JRepGenSesPart pMD = new JRepGenSesPart(nombre, bdt, ws, format, anc, alh);
		        
		         if(L.equals("L1")) 
		        	 m_ColsL1.addElement(pMD);
		         if(L.equals("L2")) 
		        	 m_ColsL2.addElement(pMD);
		         if(L.equals("L3")) 
			       	 m_ColsL3.addElement(pMD);
		         if(L.equals("CL1")) 
				   	 m_ColsCL1.addElement(pMD);
		         if(L.equals("CL2")) 
				   	 m_ColsCL2.addElement(pMD);
		         if(L.equals("CL3")) 
				   	 m_ColsCL3.addElement(pMD);
			}
			
			configurarFiltro(request);
		}
		catch(SQLException e)
		{
			
		}
	}
	
	 
	@SuppressWarnings("unchecked")
	private void configurarFiltro(HttpServletRequest request)
	{
		m_Filtro.removeAllElements();
		m_statusFiltro = "ND";
		//primero llena el filtro principal que se compone del primer nivel (m_prepL1)
		for(int i = 0; i < getNumPrep("L1"); i++)
		{
			//m_Instructions = instructions;
			//m_IsRange = isRange;
			//m_PriDataName = priDataName;
			//m_PriDefault = priDefault;
			//m_SecDataName = secDataName;
			//m_SecDefault = secDefault;
			//m_BindDataType = bindDataType;
			//m_FromCatalog = fromCatalog;
			//m_Select_Clause = select_Clause;
			 
			String pridataname = getPrepPart("L1",i).getNombre();
			String pridefault = (request.getParameter("FIL_PDF_" + pridataname) == null) ? getPrepPart("L1",i).getValor() : request.getParameter("FIL_PDF_" + pridataname);
			String secdataname;
			String secdefault;
			boolean isrange = false;
			
			try { 
				secdataname = getPrepPart("L1",(i+1)).getNombre(); 
				secdefault = (request.getParameter("FIL_SDF_" + pridataname) == null) ? getPrepPart("L1",i).getValor() : request.getParameter("FIL_SDF_" + pridataname); 
			} 
			catch(ArrayIndexOutOfBoundsException e)
			{ 
				secdataname = "";
			 	secdefault = "";
			}
			
			if(!secdataname.equals(""))
			{
				String secname = pridataname + "2";
				if(secname.equals(secdataname))
				{
					isrange = true;
					i++;
				}
				else
				{
					secdataname = "";
					secdefault = "";
					isrange = false;
				}
			}
			
			String instructions = (request.getParameter("FIL_INS_" + pridataname) == null) ? "" : request.getParameter("FIL_INS_" + pridataname);
			String binddatatype = (request.getParameter("FIL_BDT_" + pridataname) == null) ? "STRING" : request.getParameter("FIL_BDT_" + pridataname);
			int idcatalogo = (request.getParameter("FIL_IDC_" + pridataname) == null) ? 0 : Integer.parseInt(request.getParameter("FIL_IDC_" + pridataname));
			boolean fromcatalog = idcatalogo == 0 ? false : true;
			
			JRepGenSesFiltro fil = new JRepGenSesFiltro(instructions, isrange, pridataname, pridefault, 
											secdataname, secdefault, binddatatype, fromcatalog, idcatalogo);
			
			m_Filtro.addElement(fil);
			
		}
		
		m_statusFiltro = "PRO";
	}
	
	public String getNombreBase(int ind)
	{
		return (String)m_BASES.get(ind);
	}
		
	 
	@SuppressWarnings("unchecked")
	public void CargarBases(HttpServletRequest request)
	{
	    JBDRegistradasSet bdr = new JBDRegistradasSet(request);
  	    bdr.ConCat(true);
  	    bdr.Open();
	
  	    for(int i = 0; i < bdr.getNumRows(); i++)
  	    {
  	    	String nombre = bdr.getAbsRow(i).getNombre();
	    	m_BASES.addElement(nombre);

  	    }
	}

	public int getNumTablas()
	{
		return m_TABLAS.size();
	}
	
	public String getNombreTabla(int ind)
	{
		return (String)m_TABLAS.get(ind);
	}
	
	 
	@SuppressWarnings("unchecked")
	public void CargarTablas()
	{
		Connection con = null;
	    try
	    {
	       con = JAccesoBD.getConexion(m_BDP);
	       DatabaseMetaData dbmd = con.getMetaData();
	       String[] tableTypes = { "TABLE","VIEW" };
	       ResultSet tablas = dbmd.getTables(null,null,"%",tableTypes);
	       while( tablas.next() )
	       {
	    	   String nombre = tablas.getString("TABLE_NAME");
	    	   m_TABLAS.addElement(nombre);
	       }
	    }
	    catch(Throwable e)
        {
	    	System.out.println("ERROR EN TABLAS: " + e.getMessage());
        }
	    finally
	    {
	     	try { con.close(); } catch (SQLException e) {}
	    }
	}
	
	public int getNumColumnas()
	{
		return m_COLUMNAS.size();
	}
	
	public JFsiMetaDatos getColumna(int ind)
	{
		return (JFsiMetaDatos)m_COLUMNAS.get(ind);
	}

	public String getDescColumna(int ind)
	{
		JFsiMetaDatos md = (JFsiMetaDatos)m_COLUMNAS.get(ind);
		
		String nombre = md.getNombreCol() + " (" + md.getNombreTipoCol() + " " + md.getPrecision() + "," + md.getEscala() + ")";
		return nombre;
	}
	
	public String getNombreColumna(int ind)
	{
		return ((JFsiMetaDatos)m_COLUMNAS.get(ind)).getNombreCol();
	}
	
	 
	@SuppressWarnings("unchecked")
	public void CargarColumnas()
	{
		Connection con = null;
		Statement s = null;
		
	    try
	    {
	       con = JAccesoBD.getConexion(m_BDP);
	       s = con.createStatement();
	       ResultSet rs = s.executeQuery("select * from " + m_Tabla + " limit 1;");
	       ResultSetMetaData rsmd = rs.getMetaData();
	       
	       for(int i = 1; i <= rsmd.getColumnCount(); i++)
	       {
	          JFsiMetaDatos pMD = new JFsiMetaDatos(
	                 rsmd.getColumnName(i), rsmd.getPrecision(i), rsmd.getScale(i),
	                       rsmd.getTableName(i), rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
	          m_COLUMNAS.addElement(pMD);
	       }
	       	        
	    }
	    catch(Throwable e)
        {
	    	System.out.println("ERROR EN COLUMNAS: " + e.getMessage());
        }
	    finally
	    {
	    	try { s.close(); } catch (SQLException e) {}
	    	try { con.close(); } catch (SQLException e) {}
	    }
	}
	
	public void resetear(HttpServletRequest request, String BD)
	{
		
		m_TituloFuente = "Arial, Helvetica, sans-serif";
		m_TituloGrosor = "bold";
		m_TituloEstilo = "italic";
		m_TituloTam = "14pt";

		m_EncL1Fuente = "Arial, Helvetica, sans-serif";
		m_EncL1Grosor = "bold";
		m_EncL1Estilo = "normal";
		m_EncL1Tam = "10pt";
		m_EncL2Fuente = "Arial, Helvetica, sans-serif";
		m_EncL2Grosor = "bold";
		m_EncL2Estilo = "normal";
		m_EncL2Tam = "8pt";
		m_EncL3Fuente = "Arial, Helvetica, sans-serif";
		m_EncL3Grosor = "bold";
		m_EncL3Estilo = "normal";
		m_EncL3Tam = "6pt";

		m_L1Fuente = "Arial, Helvetica, sans-serif";
		m_L1Grosor = "normal";
		m_L1Estilo = "normal";
		m_L1Tam = "10pt";
		m_L2Fuente = "Arial, Helvetica, sans-serif";
		m_L2Grosor = "normal";
		m_L2Estilo = "normal";
		m_L2Tam = "8pt";
		m_L3Fuente = "Arial, Helvetica, sans-serif";
		m_L3Grosor = "normal";
		m_L3Estilo = "normal";
		m_L3Tam = "6pt";
		
		m_CL1Fuente = "Arial, Helvetica, sans-serif";
		m_CL1Grosor = "bold";
		m_CL1Estilo = "italic";
		m_CL1Tam = "10pt";
		m_CL2Fuente = "Arial, Helvetica, sans-serif";
		m_CL2Grosor = "bold";
		m_CL2Estilo = "italic";
		m_CL2Tam = "8pt";
		m_CL3Fuente = "Arial, Helvetica, sans-serif";
		m_CL3Grosor = "bold";
		m_CL3Estilo = "italic";
		m_CL3Tam = "6pt";
		
		m_BD = BD;
		m_BDP = BD;
		m_Tipo = "";
		m_SubTipo = "ESP";
		m_Clave = "CVE";
		m_Graficar = false;
		m_ID_Report = 0;
		m_ID_ReportPlnt = 0;
		m_Descripcion = "Descripción";
		m_HW = 800;
		m_VW = 600;
		m_Tabla = "TBL_BD";
		m_Columna = "ID_BD";
		m_statusFiltro = "ND";
		
		m_TabPrintPntL1 = 0;
		m_TabPrintPntCL1 = 0;
		m_SCL1 = "";
		m_CSCL1 = "";
		m_statusL1 = "ND"; 
		m_statusCL1 = "ND";
		
		m_TabPrintPntL2 = 0;
		m_TabPrintPntCL2 = 0;
		m_SCL2 = "";
		m_CSCL2 = "";
		m_statusL2 = "ND"; 
		m_statusCL2 = "ND";
		
		m_TabPrintPntL3 = 0;
		m_TabPrintPntCL3 = 0;
		m_SCL3 = "";
		m_CSCL3 = "";
		m_statusL3 = "ND"; 
		m_statusCL3 = "ND";
		
		m_Filtro.removeAllElements();
		m_prepL1.removeAllElements();
		m_prepL2.removeAllElements();
		m_prepL3.removeAllElements();

		m_ColsL1.removeAllElements();
		m_ColsL2.removeAllElements();
		m_ColsL3.removeAllElements();

		m_ColsCL1.removeAllElements();
		m_ColsCL2.removeAllElements();
		m_ColsCL3.removeAllElements();
		
		m_BASES.removeAllElements();;
		m_TABLAS.removeAllElements();;
		m_COLUMNAS.removeAllElements();;

		m_AnchoColsL1 = 0F;
		m_AnchoColsL2 = 0F;
		m_AnchoColsL3 = 0F;
		m_AnchoColsCL1 = 0F;
		m_AnchoColsCL2 = 0F;
		m_AnchoColsCL3 = 0F;
		
		CargarBases(request);
		CargarTablas();
		CargarColumnas();
	}

	public String obtEncabezado(String enc)
	{
		//String fsiTitulo = "font-family: Arial, Helvetica, sans-serif; font-size: 14pt; font-style: italic; font-weight: bold;";
		String res;
		
		if(enc.equals("Titulo"))
			res = "font-family: " + m_TituloFuente + "; font-size: " + m_TituloTam + "; font-style: " + m_TituloEstilo + "; font-weight: " + m_TituloGrosor + ";";
		else if(enc.equals("EncL1"))
			res = "font-family: " + m_EncL1Fuente + "; font-size: " + m_EncL1Tam + "; font-style: " + m_EncL1Estilo + "; font-weight: " + m_EncL1Grosor + ";";
		else if(enc.equals("EncL2"))
			res = "font-family: " + m_EncL2Fuente + "; font-size: " + m_EncL2Tam + "; font-style: " + m_EncL2Estilo + "; font-weight: " + m_EncL2Grosor + ";";
		else if(enc.equals("EncL3"))
			res = "font-family: " + m_EncL3Fuente + "; font-size: " + m_EncL3Tam + "; font-style: " + m_EncL3Estilo + "; font-weight: " + m_EncL3Grosor + ";";
		else if(enc.equals("L1"))
			res = "font-family: " + m_L1Fuente + "; font-size: " + m_L1Tam + "; font-style: " + m_L1Estilo + "; font-weight: " + m_L1Grosor + ";";
		else if(enc.equals("L2"))
			res = "font-family: " + m_L2Fuente + "; font-size: " + m_L2Tam + "; font-style: " + m_L2Estilo + "; font-weight: " + m_L2Grosor + ";";
		else if(enc.equals("L3"))
			res = "font-family: " + m_L3Fuente + "; font-size: " + m_L3Tam + "; font-style: " + m_L3Estilo + "; font-weight: " + m_L3Grosor + ";";
		else if(enc.equals("CL1"))
			res = "font-family: " + m_CL1Fuente + "; font-size: " + m_CL1Tam + "; font-style: " + m_CL1Estilo + "; font-weight: " + m_CL1Grosor + ";";
		else if(enc.equals("CL2"))
			res = "font-family: " + m_CL2Fuente + "; font-size: " + m_CL2Tam + "; font-style: " + m_CL2Estilo + "; font-weight: " + m_CL2Grosor + ";";
		else if(enc.equals("CL3"))
			res = "font-family: " + m_CL3Fuente + "; font-size: " + m_CL3Tam + "; font-style: " + m_CL3Estilo + "; font-weight: " + m_CL3Grosor + ";";
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
		else if(enc.equals("EncL1"))
		{
			m_EncL1Fuente = fuente;
			m_EncL1Tam = tam;
			m_EncL1Grosor = grosor;
			m_EncL1Estilo = estilo;
		}
		else if(enc.equals("EncL2"))
		{
			m_EncL2Fuente = fuente;
			m_EncL2Tam = tam;
			m_EncL2Grosor = grosor;
			m_EncL2Estilo = estilo;
		}
		else if(enc.equals("EncL3"))
		{
			m_EncL3Fuente = fuente;
			m_EncL3Tam = tam;
			m_EncL3Grosor = grosor;
			m_EncL3Estilo = estilo;
		}
		else if(enc.equals("L1"))
		{
			m_L1Fuente = fuente;
			m_L1Tam = tam;
			m_L1Grosor = grosor;
			m_L1Estilo = estilo;
		}
		else if(enc.equals("L2"))
		{
			m_L2Fuente = fuente;
			m_L2Tam = tam;
			m_L2Grosor = grosor;
			m_L2Estilo = estilo;
		}
		else if(enc.equals("L3"))
		{
			m_L3Fuente = fuente;
			m_L3Tam = tam;
			m_L3Grosor = grosor;
			m_L3Estilo = estilo;
		}
		else if(enc.equals("CL1"))
		{
			m_CL1Fuente = fuente;
			m_CL1Tam = tam;
			m_CL1Grosor = grosor;
			m_CL1Estilo = estilo;
		}
		else if(enc.equals("CL2"))
		{
			m_CL2Fuente = fuente;
			m_CL2Tam = tam;
			m_CL2Grosor = grosor;
			m_CL2Estilo = estilo;
		}
		else if(enc.equals("CL3"))
		{
			m_CL3Fuente = fuente;
			m_CL3Tam = tam;
			m_CL3Grosor = grosor;
			m_CL3Estilo = estilo;
		}

	}
	public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	{
		short res = -1;
		  
		if(request.getParameter("idreport") != null && 
          	request.getParameter("tipo") != null && request.getParameter("clave") != null && request.getParameter("descripcion") != null && request.getParameter("hw") != null && 
          	request.getParameter("vw") != null && request.getParameter("tabprintpntL1") != null && request.getParameter("tabprintpntCL1") != null && 
          	request.getParameter("tabprintpntL2") != null && request.getParameter("tabprintpntCL2") != null && request.getParameter("tabprintpntL3") != null && 
          	request.getParameter("tabprintpntCL3") != null && request.getParameter("select_clauseL1") != null && request.getParameter("select_clauseCL1") != null && 
          	request.getParameter("select_clauseL2") != null && request.getParameter("select_clauseCL2") != null && 
          	request.getParameter("select_clauseL3") != null && request.getParameter("select_clauseCL3") != null && 
          	request.getParameter("titulo_fuente") != null && request.getParameter("titulo_tam") != null && 
          	request.getParameter("titulo_grosor") != null && request.getParameter("titulo_estilo") != null && 
          	request.getParameter("encl1_fuente") != null && request.getParameter("encl1_tam") != null && 
          	request.getParameter("encl1_grosor") != null && request.getParameter("encl1_estilo") != null && 
          	request.getParameter("encl2_fuente") != null && request.getParameter("encl2_tam") != null && 
          	request.getParameter("encl2_grosor") != null && request.getParameter("encl2_estilo") != null && 
          	request.getParameter("encl3_fuente") != null && request.getParameter("encl3_tam") != null && 
          	request.getParameter("encl3_grosor") != null && request.getParameter("encl3_estilo") != null && 
          	request.getParameter("l1_fuente") != null && request.getParameter("l1_tam") != null && 
          	request.getParameter("l1_grosor") != null && request.getParameter("l1_estilo") != null && 
          	request.getParameter("l2_fuente") != null && request.getParameter("l2_tam") != null && 
          	request.getParameter("l2_grosor") != null && request.getParameter("l2_estilo") != null && 
          	request.getParameter("l3_fuente") != null && request.getParameter("l3_tam") != null && 
          	request.getParameter("l3_grosor") != null && request.getParameter("l3_estilo") != null && 
          	request.getParameter("cl1_fuente") != null && request.getParameter("cl1_tam") != null && 
          	request.getParameter("cl1_grosor") != null && request.getParameter("cl1_estilo") != null && 
          	request.getParameter("cl2_fuente") != null && request.getParameter("cl2_tam") != null && 
          	request.getParameter("cl2_grosor") != null && request.getParameter("cl2_estilo") != null && 
          	request.getParameter("cl3_fuente") != null && request.getParameter("cl3_tam") != null && 
          	request.getParameter("cl3_grosor") != null && request.getParameter("cl3_estilo") != null && 
 
          	!request.getParameter("idreport").equals("") && 
          	!request.getParameter("tipo").equals("") && !request.getParameter("clave").equals("") && !request.getParameter("descripcion").equals("") && !request.getParameter("hw").equals("") && 
          	!request.getParameter("vw").equals("") && !request.getParameter("tabprintpntL1").equals("") && !request.getParameter("tabprintpntCL1").equals("") && 
          	!request.getParameter("tabprintpntL2").equals("") && !request.getParameter("tabprintpntCL2").equals("") && !request.getParameter("tabprintpntL3").equals("") && 
          	!request.getParameter("tabprintpntCL3").equals("") &&
          	!request.getParameter("titulo_fuente").equals("") && !request.getParameter("titulo_tam").equals("") && 
          	!request.getParameter("titulo_grosor").equals("") && !request.getParameter("titulo_estilo").equals("") &&
           	!request.getParameter("encl1_fuente").equals("") && !request.getParameter("encl1_tam").equals("") && 
          	!request.getParameter("encl1_grosor").equals("") && !request.getParameter("encl1_estilo").equals("") &&      	
           	!request.getParameter("encl2_fuente").equals("") && !request.getParameter("encl2_tam").equals("") && 
          	!request.getParameter("encl2_grosor").equals("") && !request.getParameter("encl2_estilo").equals("") &&      	
           	!request.getParameter("encl3_fuente").equals("") && !request.getParameter("encl3_tam").equals("") && 
          	!request.getParameter("encl3_grosor").equals("") && !request.getParameter("encl3_estilo").equals("") &&      	
           	!request.getParameter("l1_fuente").equals("") && !request.getParameter("l1_tam").equals("") && 
          	!request.getParameter("l1_grosor").equals("") && !request.getParameter("l1_estilo").equals("") &&      	
           	!request.getParameter("l2_fuente").equals("") && !request.getParameter("l2_tam").equals("") && 
          	!request.getParameter("l2_grosor").equals("") && !request.getParameter("l2_estilo").equals("") &&      	
           	!request.getParameter("l3_fuente").equals("") && !request.getParameter("l3_tam").equals("") && 
          	!request.getParameter("l3_grosor").equals("") && !request.getParameter("l3_estilo").equals("") &&      	
          	!request.getParameter("cl1_fuente").equals("") && !request.getParameter("cl1_tam").equals("") && 
          	!request.getParameter("cl1_grosor").equals("") && !request.getParameter("cl1_estilo").equals("") &&      	
           	!request.getParameter("cl2_fuente").equals("") && !request.getParameter("cl2_tam").equals("") && 
          	!request.getParameter("cl2_grosor").equals("") && !request.getParameter("cl2_estilo").equals("") &&      	
           	!request.getParameter("cl3_fuente").equals("") && !request.getParameter("cl3_tam").equals("") && 
          	!request.getParameter("cl3_grosor").equals("") && !request.getParameter("cl3_estilo").equals("") )
    
		{
		}
	    else
	    {
	    	res = 3; 
	    	sb_mensaje.append("ERROR: Alguno de los parametros del reporte es nulo<br>");
	        return res;
	    }
	    
		m_TituloFuente = request.getParameter("titulo_fuente");
		m_TituloTam = request.getParameter("titulo_tam");
		m_TituloGrosor = request.getParameter("titulo_grosor");
		m_TituloEstilo = request.getParameter("titulo_estilo");
		m_EncL1Fuente = request.getParameter("encl1_fuente");
		m_EncL1Tam = request.getParameter("encl1_tam");
		m_EncL1Grosor = request.getParameter("encl1_grosor");
		m_EncL1Estilo = request.getParameter("encl1_estilo");
		m_EncL2Fuente = request.getParameter("encl2_fuente");
		m_EncL2Tam = request.getParameter("encl2_tam");
		m_EncL2Grosor = request.getParameter("encl2_grosor");
		m_EncL2Estilo = request.getParameter("encl2_estilo");
		m_EncL3Fuente = request.getParameter("encl3_fuente");
		m_EncL3Tam = request.getParameter("encl3_tam");
		m_EncL3Grosor = request.getParameter("encl3_grosor");
		m_EncL3Estilo = request.getParameter("encl3_estilo");
		m_L1Fuente = request.getParameter("l1_fuente");
		m_L1Tam = request.getParameter("l1_tam");
		m_L1Grosor = request.getParameter("l1_grosor");
		m_L1Estilo = request.getParameter("l1_estilo");
		m_L2Fuente = request.getParameter("l2_fuente");
		m_L2Tam = request.getParameter("l2_tam");
		m_L2Grosor = request.getParameter("l2_grosor");
		m_L2Estilo = request.getParameter("l2_estilo");
		m_L3Fuente = request.getParameter("l3_fuente");
		m_L3Tam = request.getParameter("l3_tam");
		m_L3Grosor = request.getParameter("l3_grosor");
		m_L3Estilo = request.getParameter("l3_estilo");
		m_CL1Fuente = request.getParameter("cl1_fuente");
		m_CL1Tam = request.getParameter("cl1_tam");
		m_CL1Grosor = request.getParameter("cl1_grosor");
		m_CL1Estilo = request.getParameter("cl1_estilo");
		m_CL2Fuente = request.getParameter("cl2_fuente");
		m_CL2Tam = request.getParameter("cl2_tam");
		m_CL2Grosor = request.getParameter("cl2_grosor");
		m_CL2Estilo = request.getParameter("cl2_estilo");
		m_CL3Fuente = request.getParameter("cl3_fuente");
		m_CL3Tam = request.getParameter("cl3_tam");
		m_CL3Grosor = request.getParameter("cl3_grosor");
		m_CL3Estilo = request.getParameter("cl3_estilo");

		m_ID_Report = Integer.parseInt(request.getParameter("idreport"));
		m_Tipo = request.getParameter("tipo");
		m_Clave = request.getParameter("clave");
		m_Graficar = (request.getParameter("graficar") != null) ? true : false;
		m_Descripcion = request.getParameter("descripcion");
		m_HW = Integer.parseInt(request.getParameter("hw"));
		m_VW = Integer.parseInt(request.getParameter("vw"));
		m_TabPrintPntL1 = Float.parseFloat(request.getParameter("tabprintpntL1"));
		m_TabPrintPntCL1 = Float.parseFloat(request.getParameter("tabprintpntCL1"));
		m_TabPrintPntL2 = Float.parseFloat(request.getParameter("tabprintpntL2"));
		m_TabPrintPntCL2 = Float.parseFloat(request.getParameter("tabprintpntCL2"));
		m_TabPrintPntL3 = Float.parseFloat(request.getParameter("tabprintpntL3"));
		m_TabPrintPntCL3 = Float.parseFloat(request.getParameter("tabprintpntCL3"));

	    if(!m_SCL1.equals(request.getParameter("select_clauseL1")))
	    {
	    	m_SCL1 = request.getParameter("select_clauseL1");
	    	
	    	if(request.getParameter("select_clauseL1").equals("")) // Cadena Vacia
	    	{
	    		m_prepL1.removeAllElements();
	    		m_statusL1 = "ND";
	    		m_Filtro.removeAllElements();
	    		m_statusFiltro = "ND";
	    	}
	    	else
	    		m_statusL1 = "NP";
	    	
	    }
	    if(!m_statusL1.equals("ND") && !m_statusL1.equals("PRO"))
    	{
    		PrepararGen(request, "FSI_L1_", m_prepL1, m_SCL1);
			m_statusL1 = "PRE";
    	}
	    if(!m_CSCL1.equals(request.getParameter("select_clauseCL1")))
	    {
	    	m_CSCL1 = request.getParameter("select_clauseCL1");
	    	
	    	if(request.getParameter("select_clauseCL1").equals(""))
	    		m_statusCL1 = "ND";
	    	else
	    		m_statusCL1 = "NP";
	    }
	    if(!m_statusCL1.equals("ND") && !m_statusCL1.equals("PRO"))
    		m_statusCL1 = "PRE";
    	
	    if(!m_SCL2.equals(request.getParameter("select_clauseL2")))
	    {
	    	m_SCL2 = request.getParameter("select_clauseL2");
	    	
	    	if(request.getParameter("select_clauseL2").equals("")) // Cadena Vacia
	    	{
	    		m_prepL2.removeAllElements();
	    		m_statusL2 = "ND";
	    	}
	    	else
	    		m_statusL2 = "NP";

	    }
	    if(!m_statusL2.equals("ND") && !m_statusL2.equals("PRO"))
    	{
    		PrepararGen(request, "FSI_L2_", m_prepL2, m_SCL2);
			m_statusL2 = "PRE";
    	}
	    if(!m_CSCL2.equals(request.getParameter("select_clauseCL2")))
	    {
	    	m_CSCL2 = request.getParameter("select_clauseCL2");
	    	
	    	if(request.getParameter("select_clauseCL2").equals(""))
	    		m_statusCL2 = "ND";
	    	else
	    		m_statusCL2 = "NP";
	    }
	    if(!m_statusCL2.equals("ND") && !m_statusCL2.equals("PRO"))
    		m_statusCL2 = "PRE";

	    if(!m_SCL3.equals(request.getParameter("select_clauseL3")))
	    {
	    	m_SCL3 = request.getParameter("select_clauseL3");
	    	
	    	if(request.getParameter("select_clauseL3").equals("")) // Cadena Vacia
	    	{
	    		m_prepL3.removeAllElements();
	    		m_statusL3 = "ND";
	    	}
	    	else
	    		m_statusL3 = "NP";

	    }
	    if(!m_statusL3.equals("ND") && !m_statusL3.equals("PRO"))
    	{
    		PrepararGen(request, "FSI_L3_", m_prepL3, m_SCL3);
			m_statusL3 = "PRE";
    	}
	    if(!m_CSCL3.equals(request.getParameter("select_clauseCL3")))
	    {
	    	m_CSCL3 = request.getParameter("select_clauseCL3");
	    	
	    	if(request.getParameter("select_clauseCL3").equals(""))
	    		m_statusCL3 = "ND";
	    	else
	    		m_statusCL3 = "NP";
	    }
	    if(!m_statusCL3.equals("ND") && !m_statusCL3.equals("PRO"))
    		m_statusCL3 = "PRE";

		return res;
	}
	
	public String Probar(String nivel) 
  	{
		String res = "";
		StringBuffer st = new StringBuffer();
		
		if(nivel.equals("L1"))
		{
			res = ProbarGen(m_prepL1, m_SCL1, st);
			m_statusL1 = st.toString();
		}
		else if(nivel.equals("L2"))
		{
			res = ProbarGen(m_prepL2, m_SCL2, st);
			m_statusL2 = st.toString();
		}
		else if(nivel.equals("L3"))
		{
			res = ProbarGen(m_prepL3, m_SCL3, st);
			m_statusL3 = st.toString();
		}
		else if(nivel.equals("CL1"))
		{
			res = ProbarGen(m_prepL1, m_CSCL1, st);
			m_statusCL1 = st.toString();
		}
		else if(nivel.equals("CL2"))
		{
			res = ProbarGen(m_prepL2, m_CSCL2, st);
			m_statusCL2 = st.toString();
		}
		else if(nivel.equals("CL3"))
		{
			res = ProbarGen(m_prepL3, m_CSCL3, st);
			m_statusCL3 = st.toString();
		}
		return res;
  	}
	
	 
	@SuppressWarnings({ "rawtypes", "unused" })
	private String ProbarGen(Vector prep, String sc, StringBuffer sts)
	{
		StringBuffer select = new StringBuffer(sc);
        String fsi_filtro = "";

        //System.out.println( "----------------------------" );
        //System.out.println( select.toString() );
        //System.out.println( "----------------------------" );
                
        int initial = select.indexOf("[",0);
        int fin = (initial == -1) ? -1 : select.indexOf("]", initial);

        while(initial != -1 && fin != -1)
	    {
        	boolean encontrado = false;
	        String key = select.substring(initial+1, fin); // ej ID_Empleado
	        // Carga los valores por default
            for(int i = 0; i < prep.size(); i++)
            {
            	JRepGenSesPrep Atrs = (JRepGenSesPrep)prep.get(i);
                String nombreAtr = Atrs.getNombre();
                String valorAtr = Atrs.getValor();
                if(nombreAtr.equals(key))
                {
                	encontrado = true;
                    select.replace(initial, fin+1, valorAtr);
                    //System.out.println(key  + "|"  + nombreAtr  + "|"  + valorAtr);
                    fsi_filtro += " { " + nombreAtr + " " + valorAtr + " } ";
                    break; // Lo encontro, sale del bucle for para continuar con la carga
                }
                
            }
            
            //System.out.println(key  + "|"  + initial  + "|"  + fin);
            
            if(!encontrado)
            {
            	sts.append("EDA");
            	return "";
            }
            
            initial = select.indexOf("[",0);
            fin = (initial == -1) ? -1 : select.indexOf("]", initial);
        }
        sts.append("PRE");
        
        //System.out.println( "----------------------------" );
        //System.out.println( select.toString() );
        //System.out.println( "----------------------------" );
         	    
	    return select.toString();

	}
	
	public JRepGenSesPart getColsPart(String nivel, int ind)
	{
		if(nivel.equals("L1"))
			return ((JRepGenSesPart)m_ColsL1.get(ind));
		else if(nivel.equals("L2"))
			return ((JRepGenSesPart)m_ColsL2.get(ind));
		else if(nivel.equals("L3"))
			return ((JRepGenSesPart)m_ColsL3.get(ind));
		else if(nivel.equals("CL1"))
			return ((JRepGenSesPart)m_ColsCL1.get(ind));
		else if(nivel.equals("CL2"))
			return ((JRepGenSesPart)m_ColsCL2.get(ind));
		else if(nivel.equals("CL3"))
			return ((JRepGenSesPart)m_ColsCL3.get(ind));
		else
			return null;
		
	}

	public int getNumCols(String nivel)
	{
		if(nivel.equals("L1"))
			return m_ColsL1.size();
		else if(nivel.equals("L2"))
			return m_ColsL2.size();
		else if(nivel.equals("L3"))
			return m_ColsL3.size();
		else if(nivel.equals("CL1"))
			return m_ColsCL1.size();
		else if(nivel.equals("CL2"))
			return m_ColsCL2.size();
		else if(nivel.equals("CL3"))
			return m_ColsCL3.size();
		else
			return 0;
	}
	
	public JRepGenSesPrep getPrepPart(String nivel, int ind)
	{
		if(nivel.equals("L1"))
			return ((JRepGenSesPrep)m_prepL1.get(ind));
		else if(nivel.equals("L2"))
			return ((JRepGenSesPrep)m_prepL2.get(ind));
		else if(nivel.equals("L3"))
			return ((JRepGenSesPrep)m_prepL3.get(ind));
		else
			return null;
		
	}

	public int getNumPrep(String nivel)
	{
		if(nivel.equals("L1"))
			return m_prepL1.size();
		else if(nivel.equals("L2"))
			return m_prepL2.size();
		else if(nivel.equals("L3"))
			return m_prepL3.size();
		else
			return 0;
	}
	
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void PrepararGen(HttpServletRequest request, String l, Vector prep, String sc)
	{
		prep.removeAllElements();
		
        //System.out.println( "-----------PREPARANDO-----------------" );
        //System.out.println( sc.toString() );
        //System.out.println( "-----------PREPARANDO-----------------" );
      
        int initial = sc.indexOf("[",0);
        int fin = (initial == -1) ? -1 : sc.indexOf("]", initial);

        while(initial != -1 && fin != -1)
        {
                String nombre = sc.substring(initial+1, fin); // ej ID_Empleado
                String valor = (request.getParameter(l + nombre) == null) ? "" : request.getParameter(l + nombre);
                //System.out.println("ATR: " + nombre + " Val: " + valor);
                
                // Revisa si existe ya el elemento
                boolean existe = false;
                for(int i = 0; i < prep.size(); i++)
                {
                	if( ((JRepGenSesPrep)prep.get(i)).getNombre().equals(nombre) )
                	{
                		existe = true;
                		break;
                	}
                }
                if(!existe)
                {
                	JRepGenSesPrep part = new JRepGenSesPrep(nombre,valor);
                	prep.addElement(part);
                }
                
                //System.out.println(nombre  + "|"  + initial  + "|"  + fin);

                initial = sc.indexOf("[",fin);
                fin = (initial == -1) ? -1 : sc.indexOf("]", initial);
        }

        //System.out.println( "-----------FIN PREPARANDO-------------" );
        
        
	}
	
	public void EstablecerStatus(HttpServletRequest request) 
	{
		m_statusL1 = (request.getParameter("select_clauseL1").equals("") ? "ND" : "NP");
		m_statusCL1 = (request.getParameter("select_clauseCL1").equals("") ? "ND" : "NP");
		m_statusL2 = (request.getParameter("select_clauseL2").equals("") ? "ND" : "NP");
		m_statusCL2 = (request.getParameter("select_clauseCL2").equals("") ? "ND" : "NP");
		m_statusL3 = (request.getParameter("select_clauseL3").equals("") ? "ND" : "NP");
    	m_statusCL3 = (request.getParameter("select_clauseCL3").equals("") ? "ND" : "NP");
	}
	
	
	public short cambioBase(HttpServletRequest request, StringBuffer sb_mensaje) 
  	throws ServletException, IOException
	{
		short res = -1;
		  
		if(request.getParameter("bdp") != null && !request.getParameter("bdp").equals(""))
		{
		}
	    else
	    {
	    	res = 3; 
	    	sb_mensaje.append("ERROR: No se ha mandado la base de datos<br>");
	        return res;
	    }
	    
		if(m_BDP.equals("FORSETI_ADMIN") || request.getParameter("bdp").equals("FORSETI_ADMIN"))
		{
			res = 1; 
	    	sb_mensaje.append("PRECAUCION: Como la base de datos cambió y estas involucran un cambio desde o hacia la base de datos FORSETI_ADMIN, se tienen que volver a establecer las pruebas y preparaciones del sistema<br>");
		}
		
		m_BDP = request.getParameter("bdp");
	  	
	   	m_TABLAS.removeAllElements();
		m_COLUMNAS.removeAllElements();
		m_Tabla = "";
		m_Columna = "";
		CargarTablas();
		
		return res;
	}

	public short cambioTabla(HttpServletRequest request, StringBuffer sb_mensaje) 
  	throws ServletException, IOException
	{
		short res = -1;
		  
		if(request.getParameter("tablas") != null && !request.getParameter("tablas").equals(""))
		{
		}
	    else
	    {
	    	res = 3; 
	    	sb_mensaje.append("ERROR: No se ha mandado la tabla<br>");
	        return res;
	    }
	    
		m_Tabla = request.getParameter("tablas");
		m_COLUMNAS.removeAllElements();
		m_Columna = "";
		//System.out.println(m_BDP + " " + m_Tabla);
		CargarColumnas();
		
		return res;
	}	
	
	public String ObtStatus(String status)
	{
		String res = "INDEFINIDO";
		if(status == null)
			return res;
		else if(status.equals("ND"))
			res = "CODIGO NO DEFINIDO AUN";
		else if(status.equals("NP"))
			res = "ESPERANDO PREPARACION";
		else if(status.equals("PRE"))
			res = "PREPARADO PARA PROBAR";
		else if(status.equals("PRO"))
			res = "PROBADO CON EXITO";
		else if(status.equals("ERR"))
			res = "ERROR EN EL CODIGO";
		else if(status.equals("EDA"))
			res = "ERROR DE ATRIBUTOS";

		return res;
		
	}
	public String getBD() 
	{
		return m_BD;
	}

	public String getBDP() 
	{
		return m_BDP;
	}

	public String getCSCL1()
	{
		return m_CSCL1;
	}
	    
	public String getCSCL2()
	{
		return m_CSCL2;
	}

	public String getCSCL3() 
	{
		return m_CSCL3;
	}

	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public int getHW()
	{
		return m_HW;
	}

	public int getID_Report() 
	{
		return m_ID_Report;
	}

	public int getID_ReportPlnt() 
	{
		return m_ID_ReportPlnt;
	}
	
	public String getSCL1() 
	{
		return m_SCL1;
	}

	public String getSCL2() 
	{
		return m_SCL2;
	}

	public String getSCL3() 
	{
		return m_SCL3;
	}

	public float getTabPrintPntL1() 
	{
		return m_TabPrintPntL1;
	}

	public float getTabPrintPntL2() 
	{
		return m_TabPrintPntL2;
	}

	public float getTabPrintPntL3() 
	{
		
		return m_TabPrintPntL3;
	}
	
	public float getTabPrintPntCL1() 
	{
		return m_TabPrintPntCL1;
	}

	public float getTabPrintPntCL2() 
	{
		return m_TabPrintPntCL2;
	}

	public float getTabPrintPntCL3() 
	{
		
		return m_TabPrintPntCL3;
	}
	
	
	public String getTipo() 
	{
		return m_Tipo;
	}
	
	public String getSubTipo() 
	{
		return m_SubTipo;
	}

	public int getVW() 
	{
		return m_VW;
	}

	public String getTabla() 
	{
		return m_Tabla;
	}

	public void setTabla(String Tabla) 
	{
		m_Tabla = Tabla;
	}

	public String getColumna() 
	{
		return m_Columna;
	}

	public void setColumna(String Columna) 
	{
		m_Columna = Columna;
	}

	public String getStatusFiltro() 
	{
		return m_statusFiltro;
	}

	public String getStatusCL1() 
	{
		return m_statusCL1;
	}

	public String getStatusCL2() 
	{
		return m_statusCL2;
	}

	public String getStatusCL3() 
	{
		return m_statusCL3;
	}

	public String getStatusL1() 
	{
		return m_statusL1;
	}

	public String getStatusL2() 
	{
		return m_statusL2;
	}

	public String getStatusL3() 
	{
		return m_statusL3;
	}
	
	public String getDescStatusFiltro() 
	{
		return ObtStatus(m_statusFiltro);
	}

	public String getDescStatusCL1() 
	{
		return ObtStatus(m_statusCL1);
	}

	public String getDescStatusCL2() 
	{
		return ObtStatus(m_statusCL2);
	}

	public String getDescStatusCL3() 
	{
		return ObtStatus(m_statusCL3);
	}

	public String getDescStatusL1() 
	{
		return ObtStatus(m_statusL1);
	}

	public String getDescStatusL2() 
	{
		return ObtStatus(m_statusL2);
	}

	public String getDescStatusL3() 
	{
		return ObtStatus(m_statusL3);
	}

	public void setStatusCL1(String statusCL1) 
	{
		m_statusCL1 = statusCL1;
	}

	public void setStatusCL2(String statusCL2) 
	{
		m_statusCL2 = statusCL2;
	}

	public void setStatusCL3(String statusCL3) 
	{
		m_statusCL3 = statusCL3;
	}
	
	public void setStatusL1(String statusL1) 
	{
		m_statusL1 = statusL1;
	}
	
	public void setStatusL2(String statusL2) 
	{
		m_statusL2 = statusL2;
	}
	
	public void setStatusL3(String statusL3) 
	{
		m_statusL3 = statusL3;
	}

	public float getAnchoCols(String nivel) 
	{
		if(nivel.equals("L1"))
			return m_AnchoColsL1;
		else if(nivel.equals("L2"))
			return m_AnchoColsL2;
		else if(nivel.equals("L3"))
			return m_AnchoColsL3;
		else if(nivel.equals("CL1"))
			return m_AnchoColsCL1;
		else if(nivel.equals("CL2"))
			return m_AnchoColsCL2;
		else if(nivel.equals("CL3"))
			return m_AnchoColsCL3;
		else
			return 0F;

	}
	
	public float getAnchoColsCL1() 
	{
		return m_AnchoColsCL1;
	}

	public float getAnchoColsCL2() 
	{
		return m_AnchoColsCL2;
	}

	public float getAnchoColsCL3() 
	{
		return m_AnchoColsCL3;
	}

	public float getAnchoColsL1() 
	{
		return m_AnchoColsL1;
	}

	public float getAnchoColsL2() 
	{
		return m_AnchoColsL2;
	}

	public float getAnchoColsL3() 
	{
		return m_AnchoColsL3;
	}

	public void setCSCL1(String cscl1) 
	{
		m_CSCL1 = cscl1;
	}

	public void setCSCL2(String cscl2) 
	{
		m_CSCL2 = cscl2;
	}

	public void setCSCL3(String cscl3) 
	{
		m_CSCL3 = cscl3;
	}

	public void setDescripcion(String descripcion) 
	{
		m_Descripcion = descripcion;
	}

	public void setHW(int hw) 
	{
		m_HW = hw;
	}

	public void setID_Report(int ID_Report) 
	{
		m_ID_Report = ID_Report;
	}

	public void setID_ReportPlnt(int ID_ReportPlnt) 
	{
		m_ID_ReportPlnt = ID_ReportPlnt;
	}
	
	public void setSCL1(String scl1) 
	{
		m_SCL1 = scl1;
	}

	public void setSCL2(String scl2) 
	{
		m_SCL2 = scl2;
	}

	public void setSCL3(String scl3) 
	{
		m_SCL3 = scl3;
	}

	public void setTabPrintPntCL1(float tabPrintPntCL1) 
	{
		m_TabPrintPntCL1 = tabPrintPntCL1;
	}

	public void setTabPrintPntCL2(float tabPrintPntCL2) 
	{
		m_TabPrintPntCL2 = tabPrintPntCL2;
	}

	public void setTabPrintPntCL3(float tabPrintPntCL3) 
	{
		m_TabPrintPntCL3 = tabPrintPntCL3;
	}

	public void setTabPrintPntL1(float tabPrintPntL1) 
	{
		m_TabPrintPntL1 = tabPrintPntL1;
	}

	public void setTabPrintPntL2(float tabPrintPntL2) 
	{
		m_TabPrintPntL2 = tabPrintPntL2;
	}

	public void setTabPrintPntL3(float tabPrintPntL3) 
	{
		m_TabPrintPntL3 = tabPrintPntL3;
	}

	public void setTipo(String tipo) 
	{
		m_Tipo = tipo;
	}
	
	public void setSubTipo(String subtipo) 
	{
		m_SubTipo = subtipo;
	}

	public void setVW(int vw) 
	{
		m_VW = vw;
	}

	
	
	
	// Encabezados del reporte
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
	
	
	public String getEncL1Estilo() 
	{
		return m_EncL1Estilo;
	}

	public String getEncL1Fuente() 
	{
		return m_EncL1Fuente;
	}

	public String getEncL1Grosor() 
	{
		return m_EncL1Grosor;
	}

	public String getEncL1Tam() 
	{
		return m_EncL1Tam;
	}

	public String getEncL2Estilo() 
	{
		return m_EncL2Estilo;
	}

	public String getEncL2Fuente() 
	{
		return m_EncL2Fuente;
	}

	public String getEncL2Grosor() 
	{
		return m_EncL2Grosor;
	}

	public String getEncL2Tam() 
	{
		return m_EncL2Tam;
	}

	public String getEncL3Estilo() 
	{
		return m_EncL3Estilo;
	}

	public String getEncL3Fuente() 
	{
		return m_EncL3Fuente;
	}

	public String getEncL3Grosor() 
	{
		return m_EncL3Grosor;
	}

	public String getEncL3Tam() 
	{
		return m_EncL3Tam;
	}
	//Ls
	public String getL1Estilo() 
	{
		return m_L1Estilo;
	}

	public String getL1Fuente() 
	{
		return m_L1Fuente;
	}

	public String getL1Grosor() 
	{
		return m_L1Grosor;
	}

	public String getL1Tam() 
	{
		return m_L1Tam;
	}

	public String getL2Estilo() 
	{
		return m_L2Estilo;
	}

	public String getL2Fuente() 
	{
		return m_L2Fuente;
	}

	public String getL2Grosor() 
	{
		return m_L2Grosor;
	}

	public String getL2Tam() 
	{
		return m_L2Tam;
	}

	public String getL3Estilo() 
	{
		return m_L3Estilo;
	}

	public String getL3Fuente() 
	{
		return m_L3Fuente;
	}

	public String getL3Grosor() 
	{
		return m_L3Grosor;
	}

	public String getL3Tam() 
	{
		return m_L3Tam;
	}
	// CLs
	public String getCL1Estilo() 
	{
		return m_CL1Estilo;
	}

	public String getCL1Fuente() 
	{
		return m_CL1Fuente;
	}

	public String getCL1Grosor() 
	{
		return m_CL1Grosor;
	}

	public String getCL1Tam() 
	{
		return m_CL1Tam;
	}

	public String getCL2Estilo() 
	{
		return m_CL2Estilo;
	}

	public String getCL2Fuente() 
	{
		return m_CL2Fuente;
	}

	public String getCL2Grosor() 
	{
		return m_CL2Grosor;
	}

	public String getCL2Tam() 
	{
		return m_CL2Tam;
	}

	public String getCL3Estilo() 
	{
		return m_CL3Estilo;
	}

	public String getCL3Fuente() 
	{
		return m_CL3Fuente;
	}

	public String getCL3Grosor() 
	{
		return m_CL3Grosor;
	}

	public String getCL3Tam() 
	{
		return m_CL3Tam;
	}

	public String getClave() 
	{
		return m_Clave;
	}

	public void setClave(String Clave) 
	{
		m_Clave = Clave;
	}

	public boolean getGraficar() 
	{
		return m_Graficar;
	}

	public void setGraficar(boolean Graficar) 
	{
		m_Graficar = Graficar;
	}
	
	public void setBD(String BD)
	{
		m_BD = BD;
	}
	
	public void setBDP(String BDP)
	{
		m_BDP = BDP;
	}
}

