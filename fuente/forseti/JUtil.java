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

import java.awt.geom.*;
import java.awt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Workbook;

import org.apache.commons.lang.mutable.MutableInt;

import forseti.JRepCellStyles;
import forseti.sets.*;
import fsi_admin.JFsiTareas;

public final class JUtil
{
	private static final boolean m_INIT = INIT();
	private static String m_LANG;
	private static String m_PASS;
	private static String m_ADDR;
	private static String m_PORT;
	private static boolean m_REINICIAR;
	private static JFsiTareas m_TAREAS; 
	
	public static synchronized boolean INIT()
	{
		boolean init = false;
		// Inicializacion de propiedades de configuracion
		System.out.println("Iniciando JUtil y Configuracion de Sistema");
		m_TAREAS = new JFsiTareas();
		m_REINICIAR = false;
		
		try
        {
            FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_conf");
            BufferedReader buff     = new BufferedReader(file);
            boolean eof             = false;
            //masProp = new Properties();

            while(!eof)
            {
                String line = buff.readLine();
                if(line == null)
                {
                    eof = true;
                }
                else
                {
                	try
                	{
                		StringTokenizer st = new StringTokenizer(line,"=");
                		String key         = st.nextToken();
                		String value       = st.nextToken();
                		if(key.equals("LANG"))
                			m_LANG = value;
                		if(key.equals("PASS"))
                			m_PASS = value;
                		else if(key.equals("ADDR"))
                			m_ADDR = value;
                		else if(key.equals("PORT"))
                			m_PORT = value;
                		else if(key.equals("INIT"))
                			init = (value.equals("true") ? true : false);
                	}
                	catch(NoSuchElementException e)
                	{
                		continue;
                	}
                }
            }
            buff.close();
            return init;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
	} 
	
	public static synchronized JFsiTareas getFsiTareas() 
	{
		return m_TAREAS;
	}
	
	public static synchronized boolean getINIT()
	{
		return m_INIT;
	}
	
	public static synchronized String getLANG()
    {
        return m_LANG;
	}

	public static synchronized String getADDR()
    {
        return m_ADDR;
	}
	
	public static synchronized String getPORT()
    {
        return m_PORT;
	}
	
	public static synchronized String getPASS()
    {
        return m_PASS;
	}
	
	public  static synchronized void setPASS(String PASS) 
	{
		m_PASS = PASS;
	}

	public static synchronized boolean getREINICIAR()
    {
        return m_REINICIAR;
	}
	
	public static synchronized void setREINICIAR(boolean REINICIAR)
    {
        m_REINICIAR = REINICIAR;
	}
		
	public static synchronized void doCallStoredProcedure(HttpServletRequest request, HttpServletResponse response, String BD, String SQLCall, JRetFuncBas rfb)
			throws ServletException, IOException
	{
		int idmensaje = -1; 
		String mensaje = "", clave = "";
	        
		try
		{
			Connection con = JAccesoBD.getConexion(BD);
			con.setAutoCommit(false);
			Statement s    = con.createStatement();
			ResultSet rs   = s.executeQuery(SQLCall);
			if(rs.next())
			{
				idmensaje = rs.getInt("ERR");
				mensaje = rs.getString("RES");
				clave = rs.getString("CLAVE");
			}
			s.close();
			if(idmensaje == 0)
				con.commit();
			else
				con.rollback();
	           
			JAccesoBD.liberarConexion(con);

			//getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
			rfb.setRS(idmensaje, mensaje, clave);
		}
		catch(SQLException e)
		{
			e.printStackTrace(System.out);
			//throw new RuntimeException(e.toString());
			idmensaje = 3; mensaje = "ERROR de SQLCall: " + p(e.getMessage()); clave = "";
			getSesion(request).setID_Mensaje((short)idmensaje, mensaje);
			rfb.setRS(4, mensaje, clave);
		}
	}
	
	public static synchronized void doDebugSQL(HttpServletRequest request, HttpServletResponse response, String Salida)
	    	throws ServletException, IOException
	{
	   	response.setContentType("text/html");
	   	PrintWriter out = response.getWriter();
	   	out.println(Salida);
	}
	
	public static synchronized String Msj(String alc, String mod, String sub, String elm, int numMsj)
	{
		String msj;
		
		JMsjSet set = new JMsjSet(null);
		set.m_Where = "alc = '" + alc + "' and mod = '" + mod + "' and sub = '" + sub + "' and elm = '" + elm + "'";
		set.ConCat(true);
		set.Open();
				
		if(set.getNumRows() < 1)
			msj = "-" + alc + "," + mod + "," + sub + "," + elm + "-";
		else
		{
			switch(numMsj)
			{
			case 1: 
				msj = set.getAbsRow(0).getMsj1();
				break;
			case 2: 
				msj =  set.getAbsRow(0).getMsj2();
				break;
			case 3: 
				msj =  set.getAbsRow(0).getMsj3();
				break;
			case 4: 
				msj =  set.getAbsRow(0).getMsj4();
				break;
			case 5: 
				msj =  set.getAbsRow(0).getMsj5();
				break;
			default: 
				msj =  "MSJ: NUM??????????????????????????????";
				break;
			
			}
		}
		
		return msj;
	}
	
	public static synchronized String Elm(String base, int elm)
	{
		String res;
		try
		{
			String[] arrayBase = base.split("\\|");
			res = arrayBase[(elm -1)];
		}
		catch(Exception e)
		{
			res = "-ERROR-";
		}
		
		return res; 
	}
	
	public static synchronized int obtNumElm(String base)
	{
		int res;
		try
		{
			String[] arrayBase = base.split("\\|");
			res = arrayBase.length;
		}
		catch(Exception e)
		{
			res = 0;
		}
		
		return res; 
	}

	
	public static synchronized String Msj(String alc, String mod, String sub, String elm, int ini, int fin)
	{
		String msj = "";
		
		JMsjSet set = new JMsjSet(null);
		set.m_Where = "alc = '" + alc + "' and mod = '" + mod + "' and sub = '" + sub + "' and elm = '" + elm + "'";
		set.ConCat(true);
		set.Open();
				
		if(set.getNumRows() < 1)
			msj = "-" + alc + "," + mod + "," + sub + "," + elm + "-";
		else
		{
			for(int i = ini; i <= fin; i++)
			{
				switch(i)
				{
				case 1: 
					msj += set.getAbsRow(0).getMsj1();
					break;
				case 2: 
					msj += " " + set.getAbsRow(0).getMsj2();
					break;
				case 3: 
					msj += " " + set.getAbsRow(0).getMsj3();
					break;
				case 4: 
					msj += " " + set.getAbsRow(0).getMsj4();
					break;
				case 5: 
					msj += " " + set.getAbsRow(0).getMsj5();
					break;
				default: 
					msj += " MSJ: NUM??????????????????????????????";
					break;
				
				}
			}	
		}
		
		return msj;
	}
	
	public static synchronized String Msj(String alc, String mod, String sub, String elm)
	{
		String msj;
		
		JMsjSet set = new JMsjSet(null);
		set.m_Where = "alc = '" + alc + "' and mod = '" + mod + "' and sub = '" + sub + "' and elm = '" + elm + "'";
		set.ConCat(true);
		set.Open();
				
		if(set.getNumRows() < 1)
			msj = "-" + alc + "," + mod + "," + sub + "," + elm + "-";
		else
			msj = set.getAbsRow(0).getMsj1();
		
		return msj;
	}
	
	// Calcula la raíz cuadrada de un número.
    public static synchronized int sqrt(int val)
    {
            int temp, g = 0, b = 0x8000, bshft = 15;
            do
            {
                    if (val >= (temp = (((g << 1) + b) << bshft--)))
                    {
                            g += b;
                            val -= temp;
                    }
            } while ((b >>= 1) > 0);

            return g;
    }
    
   /** 
	* El metodo frfc formatea el elemento rfc o curp de la cadena original quitando cualquier caracter diferente de 0-9 y A-Z del rfc o curp
	*/
    public static synchronized String frfc(String rfc)
	{
		String res = "";

		for(int i = 0; i < rfc.length(); i++)
		{
			if((rfc.charAt(i) >= 48 && rfc.charAt(i) <= 57) || (rfc.charAt(i) >= 65 && rfc.charAt(i) <= 90))
				res += rfc.charAt(i);
	    }

		return res;
	}
    /** 
	 * El metodo fco formatea el elemento quitando espacios de mas, retornos de carro, saltos de linea y tabuladores,
	 * y llavando la cadena al trim para quitar espacios al inicio y final de la cadena
	*/
    public static synchronized String fco(String eco)
	{
		String match = eco.trim();
		boolean espacio;
		if(match.length() > 0)
		{
			if(match.charAt(0) == '\n' || match.charAt(0) == '\r' || match.charAt(0) == '\t' || match.charAt(0) == ' ')
				espacio = true;
			else
				espacio = false;
		}
		else
			espacio = false;
		
		String res = "";
		for(int i = 0; i < match.length(); i++)
		{
			if(match.charAt(i) == '\n' || match.charAt(i) == '\r'
				|| match.charAt(i) == '\t' || match.charAt(i) == ' ')
			{
				//Es salto de linea, retorno, tabulador o espacio
				if(espacio)
					continue;
				else
				{
					res += match.charAt(i);
					espacio = true;
				}
			}
			else
			{
				if(espacio)
					espacio = false;
				
				res += match.charAt(i);
			}
	    }
		
		return res;
	}
	
    /**
     * Lee un número de caracteres de un BufferedInputStream y los escribe en
     * un array de bytes a partir de una posición dada
     * @param sourceStream BufferedInputStream del que leer caracteres.
     * @param target byte[] donde se escribiran los caracteres leídos
     * @param start int posición a partir de la cual se escribe en el array.
     * @param count int máximo de caracteres a leer del buffer.
     * @return int número de caracteres leídos. -1 sí no se leyo ningún caracter
     */
    public static synchronized int ReadInput(java.io.BufferedInputStream sourceStream, byte[] target,
                    int start, int count)
    {
            // Devuelve 0 bytes si no hay suficiente espacio
            if (target.length == 0)
                    return 0;

            byte[] receiver = new byte[target.length];
            int bytesRead;

            try
            {
                    bytesRead = sourceStream.read(receiver, start, count);
            }
            catch(IOException ioex)
            {
                    throw new EncodingFailedException("Fallo de lectura en el archivo data");
            }

            // Returns -1 if EOF
            if (bytesRead == 0)
                    return -1;

            for (int i = start; i < start + bytesRead; i++)
                    target[i] = (byte) receiver[i];

            return bytesRead;
    }

    /**
     * Lee un número de caracteres de un BufferedReader y los escribe en
     * un array de bytes a partir de una posición dada
     * @param sourceTextReader BufferedReader del que leer caracteres.
     * @param target byte[] donde se escribiran los caracteres leídos
     * @param start int posición a partir de la cual se escribe en el array.
     * @param count int máximo de caracteres a leer del buffer.
     * @return int número de caracteres leídos. -1 sí no se leyo ningún caracter
     */
    public static synchronized int ReadInput(java.io.BufferedReader sourceTextReader,
                    short[] target, int start, int count)
    {
            // Devuelve 0 bytes si no hay suficiente espacio
            if (target.length == 0)
                    return 0;

            char[] charArray = new char[target.length];
            int bytesRead;

            try
            {
                    bytesRead = sourceTextReader.read(charArray, start, count);
            }
            catch(IOException ioex)
            {
                    throw new EncodingFailedException("Fallo de lectura en el archivo data");
            }

            // Devuelve -1 si EOF
            if (bytesRead == 0)
                    return -1;

            for (int index = start; index < start + bytesRead; index++)
                    target[index] = (short) charArray[index];

            return bytesRead;
    }

    /**
     * Realiza n desplazamientos a la derecha sin signo a un int
     * @param valor int 
     * @param bits int cantidad de bits que desplazar
     * @return int 
     */
    public static synchronized int URShift(int valor, int bits)
    {
            if (valor >= 0)
                    return valor >> bits;
            else
                    return (valor >> bits) + (2 << ~bits);
    }

    /**
     * Realiza n desplazamientos a la derecha sin signo a un int
     * @param valor int
     * @param bits long cantidad de bits que desplazar
     * @return int
     */
    public static synchronized int URShift(int valor, long bits)
    {
            return URShift(valor, (int) bits);
    }

    /**
     * Realiza n desplazamientos a la derecha sin signo a un long
     * @param valor long
     * @param bits int cantidad de bits que desplazar
     * @return long
     */
    public static synchronized long URShift(long valor, int bits)
    {
            if (valor >= 0)
                    return valor >> bits;
            else
                    return (valor >> bits) + (2L << ~bits);
    }

    /**
     * Realiza n desplazamientos a la derecha sin signo a un long
     * @param valor long
     * @param bits long cantidad de bits que desplazar
     * @return long
     */
    public static synchronized long URShift(long valor, long bits)
    {
    	return URShift(valor, (int) bits);
    }
    
    
	public static synchronized String replace(String base, String aEnc, String sust)
    {
        int count = aEnc.length();
        
        do
        {
        	int index = base.indexOf(aEnc);
        	if(index == -1)
        		return base;

        	String loPrimero = base.substring(0,index);
        	String loUltimo = base.substring(index + count, base.length());
        	base = loPrimero + sust + loUltimo;
        } while (base.indexOf(aEnc) != -1);

        return base;
    }
	
	public static synchronized String obtCadenaEnLineas(String base, char flin, int numcars)
    {
		String res = "";
        int total = base.length();
        if(total == 0)
        	return "";
        int line = 1;	
        for(int i = 0; i < total; i++)
        {
        	res += base.substring(i, i + 1); 
        	if(i == (numcars * line))
        	{
        		res += flin;
        		line += 1;
        	}
        }

        return res;
    }
	
	public static synchronized String preparaPaginaPM(String base)
    {
        int index = base.indexOf('.');
        if(index == -1)
        	return base;

        String loPrimero = base.substring(0,index);
        String loUltimo = base.substring(index, base.length());
        base = loPrimero + "_pm" + loUltimo;
        
        return base;
    }
	
	public static synchronized String replaceFirst(String base, String aEnc, String sust)
    {
        int count = aEnc.length();
        
        int index = base.indexOf(aEnc);
        if(index == -1)
        	return base;

        String loPrimero = base.substring(0,index);
        String loUltimo = base.substring(index + count, base.length());
        base = loPrimero + sust + loUltimo;
        
        return base;
    }
	
	 
	@SuppressWarnings("rawtypes")
	public static synchronized boolean procesaFicheros(HttpServletRequest req, String dir) 
	{
		boolean res = true;
		
		try 
		{
		   // construimos el objeto que es capaz de parsear la perición
		   DiskFileUpload fu = new DiskFileUpload();

		   // maximo numero de bytes
		   fu.setSizeMax(1024*512); // 512 K

		   //System.out.println("Ponemos el tamaño máximo");
		   // tamaño por encima del cual los ficheros son escritos directamente en disco
		   fu.setSizeThreshold(4096);

		   // directorio en el que se escribirán los ficheros con tamaño superior al soportado en memoria
		   fu.setRepositoryPath("/tmp");

		   // ordenamos procesar los ficheros
		   List fileItems = fu.parseRequest(req);

		   if(fileItems == null)
		   {
			   //System.out.println("La lista es nula");
			   return false;
		   }

		   //System.out.println("El número de ficheros subidos es: " +  fileItems.size());

		   // Iteramos por cada fichero
		   
		   Iterator i = fileItems.iterator();
		   FileItem actual = null;
		   //System.out.println("estamos en la iteración");

		   while (i.hasNext())
		   {
			   actual = (FileItem)i.next();
			   String fileName = actual.getName();
			   //System.out.println("Nos han subido el fichero" + fileName);

			   // construimos un objeto file para recuperar el trayecto completo
			   File fichero = new File(fileName);
			   //System.out.println("El nombre del fichero es " + fichero.getName());

			   // nos quedamos solo con el nombre y descartamos el path
			   fichero = new  File(dir + fichero.getName());

			   // escribimos el fichero colgando del nuevo path
			   actual.write(fichero);
		   }

	   }
	   catch(Exception e) 
	   {
		   System.out.println("Error de Fichero: " + e.getMessage());
		   res = false;
	   }

	   return res;
	}
	 
	public static synchronized String _rn(String str)
	{
		String res = "";

		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) == 13)
				continue;
	        else
	        	res += str.charAt(i);
		}

		return res;

	}

	public static synchronized String p(String str)
    {
	  if(str == null)
		  return null;
	  
      String res = "";

      for(int i = 0; i < str.length(); i++)
      {
        if(str.charAt(i) == 39)
          res += "''";
        else if(str.charAt(i) == 60)
          res += "&lt;";
        else if(str.charAt(i) == 62)
          res += "&gt;";
        else
          res += str.charAt(i);
      }

      return res;

    }
	
	public static synchronized String p2(String str, String dt, boolean nullper, String def)
    {
    	if(nullper && (str == null || str.equals("") ))
    		return "null";
    	else if((str == null || str.equals("")) && !def.equals(""))
    		return "'" + def + "'";
    	else
    	{
    		if(dt.equals("date"))
    			return "'" + JUtil.obtFechaSQL(str) + "'";
    		else // dt.equals("str")
    			return "'" + JUtil.p(str) + "'";
    	}
    }
	
	public static synchronized String pt(String str)
    {
      String res = "";

      for(int i = 0; i < str.length(); i++)
      {
        if(str.charAt(i) == 60)
          res += "&lt;";
        else if(str.charAt(i) == 62)
          res += "&gt;";
        else
          res += str.charAt(i);
      }

      return res;

    }
	
	public static synchronized String q(String str)
    {
      String res = "";

      for(int i = 0; i < str.length(); i++)
      {
        if(str.charAt(i) == 39)
          res += "''";
        else
          res += str.charAt(i);
      }

      return res;
    }

	public static synchronized String w(String str)
    {
      String res = "";

      for(int i = 0; i < str.length(); i++)
      {
        if(str.charAt(i) == 39)
          res += "''";
        else
          res += str.charAt(i);
      }

      return res;

    }

	public static synchronized void RDP(String id_tipo, String fsibd, String status, String id_usuario, String id_proceso, String id_modulo, String resultado)
    {
    	String sql = "SELECT RDP('" + id_tipo + "','" + fsibd + "','" + status + "','" + id_usuario + "','" + id_proceso + "','" + id_modulo + "','" + resultado + "');";
    	//System.out.println(sql);
    	Connection con = null;
		Statement s    = null;
			
    	try
   		{
    		con = JAccesoBD.getConexion();
   			s    = con.createStatement();
   			s.execute(sql);
   			
   		}
   		catch(SQLException e)
   		{
   			e.printStackTrace();
   		}
    	finally
    	{
    		try { s.close(); } catch (SQLException e) {}
    		try { con.close(); } catch (SQLException e) {}
    	}
    }
	
/*
  public static synchronized String generarEstructuraDeReporte(TBL_REPORTS rs)
  {
	  	String tbl = ""; 
	  	/////////////////////////////////////////////////////////////
	  	JReportesSentenciasSet rss = new JReportesSentenciasSet(null);
	  	rss.m_Where = "ID_Report = '" + rs.getID_Report() + "'";
	  	rss.m_OrderBy = "ID_Sentence ASC, ID_IsCompute ASC";
	  	rss.ConCat(true);
	  	rss.Open();
	  	JReportesSentenciasColumnasSet rscs = new JReportesSentenciasColumnasSet(null);
	  	rscs.m_Where = "ID_Report = '" + rs.getID_Report() + "'";
	  	rscs.m_OrderBy = "ID_Sentence ASC, ID_IsCompute ASC, ID_Column ASC";
	  	rscs.ConCat(true);
	  	rscs.Open();
	  	JReportesFiltroSet rfs = new JReportesFiltroSet(null);
	  	rfs.m_Where = "ID_Report = '" + rs.getID_Report() + "'";
	  	rfs.m_OrderBy = "ID_Report ASC, ID_Column ASC";
	  	rfs.ConCat(true);
	  	rfs.Open();
	
	  	tbl +=   "CREATE TABLE [#TMP_REPORTS_SENTENCES] (\n";
	  	tbl +=  " [ID_Report] [smallint] NOT NULL ,\n";
	  	tbl +=  " [ID_Sentence] [tinyint] NOT NULL ,\n";
	  	tbl +=  " [ID_IsCompute] [tinyint] NOT NULL ,\n";
	  	tbl +=  " [Select_Clause] [varchar] (8000) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  " [TabPrintPnt] [decimal](5, 2) NULL ,\n";
	  	tbl +=  " [Format] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NULL \n";
	  	tbl +=  ") ON [PRIMARY]\n";
	  	tbl +=  "CREATE TABLE [#TMP_REPORTS_SENTENCES_COLUMNS] (\n";
	  	tbl +=  "	[ID_Report] [smallint] NOT NULL ,\n";
	  	tbl +=  "	[ID_Sentence] [tinyint] NOT NULL ,\n";
	  	tbl +=  "	[ID_IsCompute] [tinyint] NOT NULL ,\n";
	  	tbl +=  "	[ID_Column] [tinyint] NOT NULL ,\n";
	  	tbl +=  "	[ColName] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[BindDataType] [varchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[WillShow] [bit] NOT NULL ,\n";
	  	tbl +=  "	[Format] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[Ancho] [decimal](5, 2) NOT NULL ,\n";
	  	tbl +=  "	[AlinHor] [varchar] (20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,\n";
	  	tbl +=  "	[FGColor] [char] (6) COLLATE SQL_Latin1_General_CP1_CI_AS NULL \n";
	  	tbl +=  ") ON [PRIMARY]\n";
	  	tbl +=  "CREATE TABLE [#TMP_REPORTS_FILTER] (\n";
	  	tbl +=  "	[ID_Report] [smallint] NOT NULL ,\n";
	  	tbl +=  "	[ID_Column] [tinyint] NOT NULL ,\n";
	  	tbl +=  "	[Instructions] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[IsRange] [bit] NOT NULL ,\n";
	  	tbl +=  "	[PriDataName] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[PriDefault] [varchar] (8000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,\n";
	  	tbl +=  "	[SecDataName] [varchar] (254) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[SecDefault] [varchar] (8000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,\n";
	  	tbl +=  "	[BindDataType] [varchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,\n";
	  	tbl +=  "	[FromCatalog] [bit] NOT NULL ,\n";
	  	tbl +=  "	[Select_Clause] [varchar] (8000) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL \n";
	  	tbl +=  ") ON [PRIMARY]\n";
	  	for(int j = 0; j < rss.getNumRows(); j++)
	  	{
	  		tbl += "INSERT INTO #TMP_REPORTS_SENTENCES\n";
	  		tbl += "VALUES( " + rs.getID_Report() + "," + rss.getAbsRow(j).getID_Sentence() + "," + rss.getAbsRow(j).getID_IsCompute() +
	  		",'" + q(rss.getAbsRow(j).getSelect_Clause()) + "'," + rss.getAbsRow(j).getTabPrintPnt() + ",null )\n";
	  		
	  	}
	  	for(int k = 0; k < rscs.getNumRows(); k++)
	  	{
	  		tbl += "INSERT INTO #TMP_REPORTS_SENTENCES_COLUMNS\n";
	  		tbl += "VALUES( " + rs.getID_Report() + "," + rscs.getAbsRow(k).getID_Sentence() + "," + rscs.getAbsRow(k).getID_IsCompute() + "," + rscs.getAbsRow(k).getID_Column() + 
	  		",'" + rscs.getAbsRow(k).getColName() +	"','" + rscs.getAbsRow(k).getBindDataType() + "'," + (rscs.getAbsRow(k).getWillShow() ? 1 : 0) +
	  		",'" + rscs.getAbsRow(k).getFormat() + "'," + rscs.getAbsRow(k).getAncho() + ",'" + rscs.getAbsRow(k).getAlinHor() + "','000000' )\n";
	  		
	  	}
	  	for(int l = 0; l < rfs.getNumRows(); l++)
	  	{
	  		tbl += "INSERT INTO #TMP_REPORTS_FILTER\n";
	  		tbl += "VALUES( " + rs.getID_Report() + "," + rfs.getAbsRow(l).getID_Column() + ",'" + q(rfs.getAbsRow(l).getInstructions()) + "'," +
	  		(rfs.getAbsRow(l).getIsRange() ? 1 : 0) + ",'" + rfs.getAbsRow(l).getPriDataName() + "','" + rfs.getAbsRow(l).getPriDefault() + "','" +
	  		rfs.getAbsRow(l).getSecDataName() + "','" + rfs.getAbsRow(l).getSecDefault() + "','" + rfs.getAbsRow(l).getBindDataType() + "'," +
	  		(rfs.getAbsRow(l).getFromCatalog() ? 1 : 0) + ",'" + q(rfs.getAbsRow(l).getSelect_Clause()) + "' )\n";
	  		
	  	}
			
	  	String str = "EXEC  sp_reportes_actualizar " + rs.getID_Report() + ",'" + q(rs.getDescription()) + "','" + rs.getTipo() + "','" + q(rs.getSubTipo()) + "','" + q(rs.getClave()) + "'," + (rs.getGraficar() ? "1" : "0") + 
	  	",'" + rs.getTitulo() + "','" + rs.getEncL1() + "','" + rs.getEncL2() + "','" + rs.getEncL3() + 
	  	"','" + rs.getL1() + "','" + rs.getL2() + "','" + rs.getL3() + "','" + rs.getCL1() + "','" + rs.getCL2() + 
	  	"','" + rs.getCL3() + "'," + rs.getHW() + "," + rs.getVW();

	  	tbl += str + "\n";
	  	
	  	tbl += "DROP TABLE #TMP_REPORTS_SENTENCES\n";
	  	tbl += "DROP TABLE #TMP_REPORTS_SENTENCES_COLUMNS\n";
	  	tbl += "DROP TABLE #TMP_REPORTS_FILTER\n";
	  		  	
	  	return tbl;
	  	//////////////////////////////////////////////////  
  }
  */
  public static synchronized Image generarImagenMensaje(String mensaje, String nombreFuente, int tamanioFuente)
  {
	Frame f = new Frame();
	f.addNotify();
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	env.getAvailableFontFamilyNames();
	Font fuente = new Font(nombreFuente, Font.PLAIN, tamanioFuente);
	FontMetrics medidas = f.getFontMetrics(fuente);
	int anchoMensaje = medidas.stringWidth(mensaje);
	int lineaBaseX = anchoMensaje / 10;
	int ancho = anchoMensaje + 2 * (lineaBaseX + tamanioFuente);
	int alto = tamanioFuente * 7 / 2;
	int lineaBaseY = alto * 8 / 10;
	Image imagenMensaje = f.createImage(ancho, alto);
	Graphics2D g2d = (Graphics2D)imagenMensaje.getGraphics();
	g2d.setFont(fuente);
	g2d.translate(lineaBaseX, lineaBaseY);
	g2d.setPaint(Color.lightGray);
	AffineTransform origTransform = g2d.getTransform();
	g2d.shear(-0.95, 0);
	g2d.scale(1,3);
	g2d.drawString(mensaje, 0, 0);
	g2d.setTransform(origTransform);
	g2d.setPaint(Color.black);
	g2d.drawString(mensaje, 0, 0);
	
	return(imagenMensaje);
  }
  
  public static synchronized void obtValoresFiltro(String strval, Properties ops)
  {
      int initial = strval.indexOf("[", 0);
      int fin = (initial == -1) ? -1 : strval.indexOf("]", initial);

      while (initial != -1 && fin != -1)
      {
        String elem = strval.substring(initial + 1, fin);
        StringTokenizer st = new StringTokenizer(elem,"=");
        String key         = st.nextToken();
        String value       = st.nextToken();
        ops.put(key, value);

        //

        //
        
        initial = strval.indexOf("[", fin);
        fin = (initial == -1) ? -1 : strval.indexOf("]", initial);
      }
  }
 
  public static synchronized String obtValorDeFiltro(String strval, String atr)
  {
	  String res = "";
      int initial = strval.indexOf("[", 0);
      int fin = (initial == -1) ? -1 : strval.indexOf("]", initial);

      while (initial != -1 && fin != -1)
      {
        String elem = strval.substring(initial + 1, fin);
        StringTokenizer st = new StringTokenizer(elem,"=");
        String key         = st.nextToken();
        String value       = st.nextToken();
        if(key.equals(atr))
        {
        	res = value;
        	break;
        }
                
        initial = strval.indexOf("[", fin);
        fin = (initial == -1) ? -1 : strval.indexOf("]", initial);
      }
      
      return res;
  }
  
  public static synchronized short VerificaExistencias(HttpServletRequest request, StringBuffer sbmensaje, byte ID_Bodega, String ID_Prod, boolean AuditarAlm, float Cantidad)
  {
	  if(Cantidad == 0.00  || !AuditarAlm)
		  return -1;
	  
	  short res = -1;
	  
	  JInvServExistenciasSetV2 set = new JInvServExistenciasSetV2(request, "P");
	  set.m_Where = "Clave = '" + p(ID_Prod) + "' and ID_Bodega = '" +  ID_Bodega + "'";
	  set.Open();
				
	  if(set.getNumRows() < 1)
	  {
		sbmensaje.append("ERROR: El producto no aparece en las existencias de la bodega o viceversa. " + ID_Prod);
		return 3;
	  }

	  if(Cantidad > set.getAbsRow(0).getExistencia())
	  {
		  res = 1;
		  sbmensaje.append("PRECAUCION: El producto " + ID_Prod + " tiene una existencia de " + set.getAbsRow(0).getExistencia() + " en la bodega " + ID_Bodega + ", lo cual no permite una salida de " + Cantidad);
	  } 
	  
	  return res;
  }

  public static synchronized short VerificaStocks(HttpServletRequest request, StringBuffer sbmensaje, byte ID_Bodega, String ID_Prod, byte ManejoStocks, float Cantidad)
  {
	    //System.out.println("MANEJO: " + ManejoStocks);
	  	JInvServExistenciasSetV2 set = new JInvServExistenciasSetV2(request, "P");
		set.m_Where = "Clave = '" + p(ID_Prod) + "' and ID_Bodega = '" +  ID_Bodega + "'";
		set.Open();
			
		if(set.getNumRows() < 1)
		{
			sbmensaje.append("ERROR: El producto no aparece en las existencias de la bodega, o viceversa. " + ID_Prod);
			return 3;
		}
		
		short res = -1;
	    if(ManejoStocks == -1) // No maneja la bodega stock (-1). No hace revision alguna
	    	return res;

		double existencia, stockmin, stockmax;
		
		existencia = set.getAbsRow(0).getExistencia();
		stockmax = set.getAbsRow(0).getStockMax();
		stockmin = set.getAbsRow(0).getStockMin();

		if(stockmin == 0 && stockmax == 0) // si el stock minimo y el maximo son cero, no maneja stocks en este material aunque la bodega si los maneje
			return -1;
		if(stockmin == -1 && stockmax == -1) // si el stock minimo y el maximo son -1, no maneja este material en esta bodega (Aplica solo si se manejan stocks en la bodega)
		{
			sbmensaje.append("PRECAUCION: No se puede agregar la entrada del producto " + ID_Prod + " porque la bodega no lo maneja");
			return 1;
		}

		switch(ManejoStocks)
		{
		case -1: // No maneja nigun stock
			break;
		case 0: //Falla si la existencia no es menor al stock minimo
			if( stockmin < existencia )
			{
				res = 1;
				sbmensaje.append("PRECAUCION: No se puede agregar la entrada del producto " + ID_Prod + " a la bodega " + ID_Bodega + " porque tiene una existencia actual de " + existencia + " mayor al stock mínimo de " + stockmin);
			}
			break;
		case 1: // Falla si la cantidad de entrada mas la existencia actual, sobrepasa la cantidad de stock maximo
			if( (Cantidad + existencia) > stockmax )
			{
				res = 1;
				sbmensaje.append("PRECAUCION: No se puede agregar la entrada del producto " + ID_Prod + " a la bodega " + ID_Bodega + " porque la existencia de " + existencia + " sumado a la entrada de " + Cantidad + " sobrepasaría el stock máximo de " + stockmax);
			}
			break;
		case 2: //Falla si la existencia no es menor al stock minimo, o si la cantidad de entrada mas la existencia actual sobrepasa la cantidad de stock maximo	
			if( stockmin < existencia )
			{
				res = 1;
				sbmensaje.append("PRECAUCION: No se puede agregar la entrada del producto " + ID_Prod + " a la bodega " + ID_Bodega + " porque tiene una existencia actual de " + existencia + " mayor al stock mínimo de " + stockmin);
			}
			if( (Cantidad + existencia) > stockmax )
			{
				res = 1;
				sbmensaje.append("PRECAUCION: No se puede agregar la entrada del producto " + ID_Prod + " a la bodega " + ID_Bodega + " porque la existencia de " + existencia + " sumado a la entrada de " + Cantidad + " sobrepasaría el stock máximo de " + stockmax);
			}
			break;
		}

		return res;
  }
	
  // Version anterior en byte
  public static synchronized float redondear(float cant, byte dec)
  {
    float res;
    switch (dec)
    {
      case 0:
    	res = (float) Math.rint(cant);
    	break;
      case 1:
        res = (float) Math.rint(cant * 10) / 10;
        break;
      case 2:
        res = (float) Math.rint(cant * 100) / 100;
        break;
      case 3:
        res = (float) Math.rint(cant * 1000) / 1000;
        break;
      case 4:
        res = (float) Math.rint(cant * 10000) / 10000;
        break;
      case 5:
        res = (float) Math.rint(cant * 100000) / 100000;
        break;
      case 6:
        res = (float) Math.rint(cant * 1000000) / 1000000;
        break;
      case 7:
        res = (float) Math.rint(cant * 10000000) / 10000000;
        break;
      case 8:
        res = (float) Math.rint(cant * 100000000) / 100000000;
        break;
      case 9:
        res = (float) Math.rint(cant * 1000000000) / 1000000000;
        break;
      default:
        res = cant;
        break;
    }
    return res;
  }
  
  // Version en int por cuestiones de velocidad
  public static synchronized float redondear(float cant, int dec)
  {
    float res;
    switch (dec)
    {
      case 0:
    	res = (float) Math.rint(cant);
    	break;
      case 1:
        res = (float) Math.rint(cant * 10) / 10;
        break;
      case 2:
        res = (float) Math.rint(cant * 100) / 100;
        break;
      case 3:
        res = (float) Math.rint(cant * 1000) / 1000;
        break;
      case 4:
        res = (float) Math.rint(cant * 10000) / 10000;
        break;
      case 5:
        res = (float) Math.rint(cant * 100000) / 100000;
        break;
      case 6:
        res = (float) Math.rint(cant * 1000000) / 1000000;
        break;
      case 7:
        res = (float) Math.rint(cant * 10000000) / 10000000;
        break;
      case 8:
        res = (float) Math.rint(cant * 100000000) / 100000000;
        break;
      case 9:
        res = (float) Math.rint(cant * 1000000000) / 1000000000;
        break;
      default:
        res = cant;
        break;
    }
    return res;
  }

  //Version double
  public static synchronized double redondear(double cant, int dec)
  {
   double res;
   switch (dec)
   {
     case 0:
       res = Math.rint(cant);
   	   break;
     case 1:
       res = Math.rint(cant * 10) / 10;
       break;
     case 2:
       res = Math.rint(cant * 100) / 100;
       break;
     case 3:
       res = Math.rint(cant * 1000) / 1000;
       break;
     case 4:
       res = Math.rint(cant * 10000) / 10000;
       break;
     case 5:
       res = Math.rint(cant * 100000) / 100000;
       break;
     case 6:
       res = Math.rint(cant * 1000000) / 1000000;
       break;
     case 7:
       res = Math.rint(cant * 10000000) / 10000000;
       break;
     case 8:
       res = Math.rint(cant * 100000000) / 100000000;
       break;
     case 9:
       res = Math.rint(cant * 1000000000) / 1000000000;
       break;
     default:
       res = cant;
       break;
   }
   return res;
  }

  public static synchronized String obtCuentaFormato(StringBuffer cuenta,
      HttpServletRequest request)
  {
    if(cuenta.length() != 19)
      return "";

    HttpSession ses = request.getSession(true);
    JSesionPrincipal pr = (JSesionPrincipal) ses.getAttribute("forseti");

    byte nivel = pr.getNivelCC();

    for (int i = 1; i < 6; i++) {
      cuenta.insert(i * 4, '-');
    }

    String cuentaForm = cuenta.toString();
    return cuentaForm.substring(0, nivel * 4);

  }
  
  public static synchronized String obtCuentaFormato(String str_cuenta,
    HttpServletRequest request)
  {
	if(str_cuenta == null || str_cuenta.length() != 19)
      return "";

	StringBuffer cuenta = new StringBuffer(str_cuenta);
    HttpSession ses = request.getSession(true);
    JSesionPrincipal pr = (JSesionPrincipal) ses.getAttribute("forseti");

    byte nivel = pr.getNivelCC();

    for (int i = 1; i < 6; i++) {
      cuenta.insert(i * 4, '-');
    }

    String cuentaForm = cuenta.toString();
    return cuentaForm.substring(0, nivel * 4);

  }

  public static synchronized String Converts(String strval, String separador, boolean si_o_ret_nada)
  {
	if(strval == null || strval.equals("") || strval.equals("null"))
		return "&nbsp;";
		
    StringBuffer str = new StringBuffer(strval);
    int cantidad = Integer.parseInt(strval);
    int comas, len;
    boolean negativo = (cantidad < 0) ? true : false;
    if(cantidad == 0 && si_o_ret_nada)
      return "&nbsp;";

    if(negativo)
      str.deleteCharAt(0);

    if(!separador.equals(" "))
    {
      comas = (str.length() / 3);
      len = str.length();
      for(int i = 0; i < comas; i++)
      {
         len -= 3;
         if(len > 0)
            str.insert(len,separador);
      }
    }

    if(negativo)
      str.insert(0,'-');

    return str.toString();

  }

  public static synchronized String Converts(float val, String separador, String punto, int dec, boolean si_o_ret_nada)
  {
    float cantidad = val;
    int index, comas, len;
    boolean negativo = (cantidad < 0) ? true : false;
    if(cantidad == 0 && si_o_ret_nada)
      return "&nbsp;";

    String s = Float.toString(redondear(cantidad,dec));
    StringBuffer str = new StringBuffer(s);

    if(negativo)
      str.deleteCharAt(0);

    index = str.indexOf(".");
    if(!separador.equals(" "))
    {
       comas = (index == -1) ? (str.length() / 3) : (index / 3);
       len = (index == -1) ? str.length() : index;
       for(int i = 0; i < comas; i++)
       {
          len -= 3;
          if(len > 0)
              str.insert(len,separador);
       }
    }

    index = str.indexOf(".");
    if(index != -1)
    {
      str.replace(index, index+1, punto);
      int hay = str.length() - (index+1);
      int diff = dec - hay;
      if(diff > 0)
      {
        for(int i = 0; i < diff; i++)
        {
          str.append('0');
        }
      }
      //str.append("|" + dec + "|" + index + "|" + diff + "|");
    }

    if(negativo)
      str.insert(0,'-');

    return str.toString();

  }

  public static synchronized String Converts(double val, String separador, String punto, int dec, boolean si_o_ret_nada)
  {
    double cantidad = val;
    int index, comas, len;
    boolean negativo = (cantidad < 0) ? true : false;
    if(cantidad == 0 && si_o_ret_nada)
      return "&nbsp;";

    String s = Double.toString(redondear(cantidad,dec));
    StringBuffer str = new StringBuffer(s);

    if(negativo)
      str.deleteCharAt(0);

    index = str.indexOf(".");
    if(!separador.equals(" "))
    {
       comas = (index == -1) ? (str.length() / 3) : (index / 3);
       len = (index == -1) ? str.length() : index;
       for(int i = 0; i < comas; i++)
       {
          len -= 3;
          if(len > 0)
              str.insert(len,separador);
       }
    }

    index = str.indexOf(".");
    if(index != -1)
    {
      str.replace(index, index+1, punto);
      int hay = str.length() - (index+1);
      int diff = dec - hay;
      if(diff > 0)
      {
        for(int i = 0; i < diff; i++)
        {
          str.append('0');
        }
      }
      //str.append("|" + dec + "|" + index + "|" + diff + "|");
    }

    if(negativo)
      str.insert(0,'-');

    return str.toString();

  }
  
  public static synchronized String Converts(String strval, String separador, String punto, int dec, boolean si_o_ret_nada)
  {
	if(strval == null || strval.equals("") || strval.equals("null"))
		return "&nbsp;";
			
    float cantidad = Float.parseFloat(strval);
    int index, comas, len;
    boolean negativo = (cantidad < 0) ? true : false;
    if(cantidad == 0 && si_o_ret_nada)
      return "&nbsp;";

    String s = Float.toString(redondear(cantidad,dec));
    StringBuffer str = new StringBuffer(s);

    if(negativo)
      str.deleteCharAt(0);

    index = str.indexOf(".");
    if(!separador.equals(" "))
    {
       comas = (index == -1) ? (str.length() / 3) : (index / 3);
       len = (index == -1) ? str.length() : index;
       for(int i = 0; i < comas; i++)
       {
          len -= 3;
          if(len > 0)
              str.insert(len,separador);
       }
    }

    index = str.indexOf(".");
    if(index != -1)
    {
      str.replace(index, index+1, punto);
      int hay = str.length() - (index+1);
      int diff = dec - hay;
      if(diff > 0)
      {
        for(int i = 0; i < diff; i++)
        {
          str.append('0');
        }
      }
      //str.append("|" + dec + "|" + index + "|" + diff + "|");
    }

    if(negativo)
      str.insert(0,'-');

    return str.toString();

  }
  
  public static synchronized Cell DatoXLS(/*Workbook wb*/ JRepCellStyles cellStyles, Row row, int cel, String strval, String formato, 
		  String tipocol, String alin, String tipocel, String font, HttpServletRequest request)
  {
	  
	Cell cell = row.createCell(cel);
	
	if(strval == null) 
		cell.setCellValue(Cell.CELL_TYPE_BLANK);
	else if(tipocol.equals("BYTE") || tipocol.equals("INT"))
		cell.setCellValue(Integer.parseInt(strval));
    else if(tipocol.equals("STRING"))
    {
      if(formato.equals("cuenta"))
    	  cell.setCellValue(obtCuentaFormato(new StringBuffer(strval), request));
      else if(!formato.equals("general"))
      {
        if(strval.length() > Integer.parseInt(formato))
        	cell.setCellValue(strval.substring(0, Integer.parseInt(formato)));
        else
        	cell.setCellValue(strval);
      }
      else
    	  cell.setCellValue(strval);
    }
    else if(tipocol.equals("DECIMAL") || tipocol.equals("MONEY"))
    {
      if(formato.equals("LETRA"))
    	  cell.setCellValue(CantEnLetra(Float.parseFloat(strval), null, null));
      else
    	  cell.setCellValue(Float.parseFloat(strval));
    }
    else if(tipocol.equals("TIME"))
    	cell.setCellValue(obtFechaIMP(strval, formato));
    else
    	cell.setCellValue(strval);
	
	cell.setCellStyle(cellStyles.getStyle(alin, tipocel, font));
    	
	return cell;
  }

  public static synchronized String FormatearRep(String strval, String formato, String tipocol, HttpServletRequest request)
  {
	if(strval == null)
		return "Valor-NULO";
	
	if(tipocol.equals("BYTE") || tipocol.equals("INT"))
    {
      String separador = formato.substring(0, 1);
      boolean si_o_ret_nada = formato.substring(2, 3).equals("1") ? true : false;
      return Converts(strval, separador, si_o_ret_nada);
    }
    else if(tipocol.equals("STRING"))
    {
      if(formato.equals("cuenta"))
        return obtCuentaFormato(new StringBuffer(strval), request);
      else if(!formato.equals("general"))
      {
        if(strval.length() > Integer.parseInt(formato))
          return strval.substring(0, Integer.parseInt(formato));
        else
          return strval;
      }
      else
        return strval;
    }
    else if(tipocol.equals("DECIMAL") || tipocol.equals("MONEY"))
    {
      if(formato.equals("LETRA"))
      {
        return CantEnLetra(Float.parseFloat(strval), null, null);
      }
      else
      {
        String separador = formato.substring(0, 1);
        String punto = formato.substring(2, 3);
        byte dec = Byte.parseByte(formato.substring(4, 5));
        boolean si_o_ret_nada = formato.substring(6, 7).equals("1") ? true : false;
        return Converts(strval, separador, punto, dec, si_o_ret_nada);
      }
    }
    else if(tipocol.equals("TIME"))
    {
    	System.out.println(strval + " " + formato);
    	return obtFechaIMP(strval, formato);
    }
    else
    {
      return strval;
    }

  }

  public static synchronized String obtTipoColumnaRep(String tipocol)
  {
	  if(tipocol.equals("int"))//if(tipocol.equals("tinyint"))
	        return "BYTE";
	    else if(tipocol.equals("int2") || tipocol.equals("int4"))//if(tipocol.equals("smallint") || tipocol.equals("int"))
	       	return "INT";
	    else if(tipocol.equals("varchar") || tipocol.equals("bpchar") || tipocol.equals("text") || tipocol.equals("char")) //if(tipocol.equals("varchar") || tipocol.equals("char") || tipocol.equals("text") || tipocol.equals("nvarchar") || tipocol.equals("nchar") || tipocol.equals("ntext"))
	    	return "STRING";
	    else if(tipocol.equals("numeric"))//if(tipocol.equals("decimal")) 
	    	return "DECIMAL";
	    //else if(tipocol.equals("money"))
	    //	return "MONEY";
	    else if(tipocol.equals("timestamp")) //if(tipocol.equals("datetime") || tipocol.equals("smalldatetime"))
	        return "TIME";
	    else if(tipocol.equals("bit") || tipocol.equals("boolean"))//if(tipocol.equals("bit")) 
	    	return "BOOL";
	    else
	        return "STRING";
  
  }
  
  public static synchronized String ConvertsBool(String strval, String formato)
  {
	  if(strval == null || strval.equals("") || strval.equals("null"))
			return "&nbsp;";
	  
	  String res;
	  
	  if(formato.equals("VER_FAL"))
	  	res = (strval.equals("1") || strval.equals("true") ? "Verdadero" : "Falso");
	  else if(formato.equals("V_F"))
		res = (strval.equals("1") || strval.equals("true") ? "V" : "F");
	  else if(formato.equals("SI_NO"))
		res = (strval.equals("1") || strval.equals("true") ? "Si" : "No");
	  else if(formato.equals("1_0"))
		res = (strval.equals("1") || strval.equals("true") ? "1" : "0");
	  else
		res = (strval.equals("1") || strval.equals("true") ? "?1SiV" : "?0NoF");
		  
	  return res;
  }
  
  public static synchronized String FormatearImp(String strval, String formato, String tipocol, HttpServletRequest request, String id_moneda, String moneda)
  {
	
	if(tipocol.equals("int") || tipocol.equals("int2") || tipocol.equals("int4"))
    {
      String separador = formato.substring(0, 1);
      boolean si_o_ret_nada = formato.substring(2, 3).equals("1") ? true : false;
      return Converts(strval, separador, si_o_ret_nada);
    }
	else if(tipocol.equals("bit") || tipocol.equals("boolean"))
    {
	  return ConvertsBool(strval, formato);
    }
    else if(tipocol.equals("varchar") || tipocol.equals("bpchar") || tipocol.equals("text") || tipocol.equals("char")) //if(tipocol.equals("varchar") || tipocol.equals("char") || tipocol.equals("text") || tipocol.equals("nvarchar") || tipocol.equals("nchar") || tipocol.equals("ntext"))
	{
      if(formato.equals("cuenta"))
        return obtCuentaFormato(new StringBuffer(strval), request);
      else if(!formato.equals("general"))
      {
        if(strval.length() > Integer.parseInt(formato))
          return strval.substring(0, Integer.parseInt(formato));
        else
          return strval;
      }
      else
        return strval;
    }
    else if(tipocol.equals("numeric") || tipocol.equals("decimal"))
    {
      if(formato.equals("LETRA"))
      {
    	String LETRA = CantEnLetra(Float.parseFloat(strval), id_moneda, moneda);
    	return LETRA.toUpperCase();
      }
      if(formato.equals("letra"))
      {
    	String letra = CantEnLetra(Float.parseFloat(strval), id_moneda, moneda);
    	return letra.toLowerCase();
        
      }
      else
      {
        String separador = formato.substring(0, 1);
        String punto = formato.substring(2, 3);
        byte dec = Byte.parseByte(formato.substring(4, 5));
        boolean si_o_ret_nada = formato.substring(6, 7).equals("1") ? true : false;
        return Converts(strval, separador, punto, dec, si_o_ret_nada);
      }
    }
    else if(tipocol.equals("timestamp"))
    {
      //System.out.println(strval + " " + formato);
      return obtFechaIMP(strval, formato);
    }
    else
    {
      return strval;
    }

  }
  
  public static synchronized String obtSubCadena(String sCadena, String tokenIni, String tokenFin)
  {
        String res = "";
        int index = sCadena.indexOf(tokenIni);
        if(index != -1)
        {
        	int indexIni = index + tokenIni.length();
        	int indexFin = sCadena.indexOf(tokenFin, indexIni);
        	if(indexFin != -1)
            {
            	res = sCadena.substring(indexIni,indexFin);
            }
        }

        return res;
  }
  
  public static synchronized String obtCuentas(String sCuenta, byte nMax)
  {
        String res = "";
        int len = sCuenta.length();

        for(int i = 0; i < len; i++)
        {
          if(sCuenta.charAt(i) >= 48 && sCuenta.charAt(i) <= 57)
            res += sCuenta.substring(i, i+1);
        }

        len = res.length();
        int hasta = nMax - len;
        for(int i = 0; i < hasta; i++)
                res += "0";

        return res;
  }

   
  @SuppressWarnings("rawtypes")
public static synchronized String depurarParametros(HttpServletRequest request)
  {
    String res;
    res = "<tr><td bgcolor=\"#999999\"><table border=1>";
    Enumeration nombresParam = request.getParameterNames();
    while(nombresParam.hasMoreElements())
    {
        String nombreParam = (String)nombresParam.nextElement();
        res += "<tr><td>" + nombreParam + "\n<td>";
        String[] valoresParam = request.getParameterValues(nombreParam);
        if(valoresParam.length == 1)
        {
                String valorParam = valoresParam[0];
                if(valorParam.length() == 0)
                        res += "<I>SIN VALOR</I>";
                else
                        res += valorParam;
        }
        else
        {
                res += "<UL>";
                for(int i = 0; i < valoresParam.length; i++)
                {
                        res += "<LI>" + valoresParam[i];
                }
                res += "</UL>";
        }
    }
    res += "</table></td></tr>";
    return res;
  }
  
  public static synchronized int obtDia(Calendar cal)
  {
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public static synchronized int obtMes(Calendar cal)
  {
    return (cal.get(Calendar.MONTH) + 1);
  }

  public static synchronized int obtAno(Calendar cal)
  {
    return cal.get(Calendar.YEAR);
  }
  
  @SuppressWarnings("deprecation")
  public static synchronized int obtDia(Date fech)
  {
	return  fech.getDate();
  }
  @SuppressWarnings("deprecation")
  public static synchronized int obtMes(Date fech)
  {
	return  fech.getMonth();
  }
  @SuppressWarnings("deprecation")
  public static synchronized int obtAno(Date fech)
  {
	return  fech.getYear();
  }
 
  public static synchronized Date estFecha(String fech)
  {
    String mess = (fech.length() == 11) ? fech.substring(3,6) : fech.substring(2,5);
    int dia = (fech.length() == 11) ? Integer.parseInt(fech.substring(0,2)) : Integer.parseInt(fech.substring(0,1));
    int mes;
    
    if(mess.equals("ene"))
      mes = 0;
    else if(mess.equals("feb"))
      mes = 1;
    else if(mess.equals("mar"))
      mes = 2;
    else if(mess.equals("abr"))
      mes = 3;
    else if(mess.equals("may"))
      mes = 4;
    else if(mess.equals("jun"))
      mes = 5;
    else if(mess.equals("jul"))
      mes = 6;
    else if(mess.equals("ago"))
      mes = 7;
    else if(mess.equals("sep"))
      mes = 8;
    else if(mess.equals("oct"))
      mes = 9;
    else if(mess.equals("nov"))
      mes = 10;
    else if(mess.equals("dic"))
      mes = 11;
    else
      mes = 0;

    int ano = (fech.length() == 11) ? Integer.parseInt(fech.substring(7,11)) : Integer.parseInt(fech.substring(6,10));
    
    return new GregorianCalendar(ano, mes, dia).getTime();
  }
  
  public static synchronized GregorianCalendar estCalendario(String fech)
  {
    String mess = (fech.length() == 11) ? fech.substring(3,6) : fech.substring(2,5);
    int dia = (fech.length() == 11) ? Integer.parseInt(fech.substring(0,2)) : Integer.parseInt(fech.substring(0,1));
    int mes;
    
    if(mess.equals("ene"))
      mes = 0;
    else if(mess.equals("feb"))
      mes = 1;
    else if(mess.equals("mar"))
      mes = 2;
    else if(mess.equals("abr"))
      mes = 3;
    else if(mess.equals("may"))
      mes = 4;
    else if(mess.equals("jun"))
      mes = 5;
    else if(mess.equals("jul"))
      mes = 6;
    else if(mess.equals("ago"))
      mes = 7;
    else if(mess.equals("sep"))
      mes = 8;
    else if(mess.equals("oct"))
      mes = 9;
    else if(mess.equals("nov"))
      mes = 10;
    else if(mess.equals("dic"))
      mes = 11;
    else
      mes = 0;

    int ano = (fech.length() == 11) ? Integer.parseInt(fech.substring(7,11)) : Integer.parseInt(fech.substring(6,10));
    
    return new GregorianCalendar(ano, mes, dia);
  }

  public static synchronized Date estFecha_h24(String fech)
  {
    String mess = (fech.length() == 17) ? fech.substring(3,6) : fech.substring(2,5);
    int dia = (fech.length() == 17) ? Integer.parseInt(fech.substring(0,2)) : Integer.parseInt(fech.substring(0,1));
    int mes;
    
    if(mess.equals("ene"))
      mes = 0;
    else if(mess.equals("feb"))
      mes = 1;
    else if(mess.equals("mar"))
      mes = 2;
    else if(mess.equals("abr"))
      mes = 3;
    else if(mess.equals("may"))
      mes = 4;
    else if(mess.equals("jun"))
      mes = 5;
    else if(mess.equals("jul"))
      mes = 6;
    else if(mess.equals("ago"))
      mes = 7;
    else if(mess.equals("sep"))
      mes = 8;
    else if(mess.equals("oct"))
      mes = 9;
    else if(mess.equals("nov"))
      mes = 10;
    else if(mess.equals("dic"))
      mes = 11;
    else
      mes = 0;

    int ano = (fech.length() == 17) ? Integer.parseInt(fech.substring(7,11)) : Integer.parseInt(fech.substring(6,10));
    
    return new GregorianCalendar(ano, mes, dia).getTime();
  }
  
  public static synchronized Date estFechaHora(String fech)
  {
	int dia = (fech.length() == 17) ? Integer.parseInt(fech.substring(0,2)) : Integer.parseInt(fech.substring(0,1));
	String mess = (fech.length() == 17) ? fech.substring(3,6) : fech.substring(2,5);
	    
    int mes;
    if(mess.equals("ene"))
    	mes = 1;
    else if(mess.equals("feb"))
    	mes = 2;
    else if(mess.equals("mar"))
    	mes = 3;
    else if(mess.equals("abr"))
    	mes = 4;
    else if(mess.equals("may"))
    	mes = 5;
    else if(mess.equals("jun"))
    	mes = 6;
    else if(mess.equals("jul"))
    	mes = 7;
    else if(mess.equals("ago"))
    	mes = 8;
    else if(mess.equals("sep"))
    	mes = 9;
    else if(mess.equals("oct"))
    	mes = 10;
    else if(mess.equals("nov"))
    	mes = 11;
    else if(mess.equals("dic"))
    	mes = 12;
    else
    	mes = 0;

    int ano = (fech.length() == 17) ? Integer.parseInt(fech.substring(7,11)) : Integer.parseInt(fech.substring(6,10));
    int hora = (fech.length() == 17) ? Integer.parseInt(fech.substring(12,14)) : Integer.parseInt(fech.substring(11,13));
    int min = (fech.length() == 17) ? Integer.parseInt(fech.substring(15,17)) : Integer.parseInt(fech.substring(14,16));

    return new GregorianCalendar(ano, mes, dia, hora, min, 0).getTime();
  }
  
  public static synchronized int obtDia(String fech)
  {
    //Por default el lenguaje es ingles ( fechas en ingles )
    return Integer.parseInt(fech.substring(0,2));
  }

  public static synchronized int obtMes(String fech)
  {
    String mess = (fech.length() == 11) ? fech.substring(3,6) : fech.substring(2,5);

    int mes;
    if(mess.equals("ene"))
      mes = 1;
    else if(mess.equals("feb"))
      mes = 2;
    else if(mess.equals("mar"))
      mes = 3;
    else if(mess.equals("abr"))
      mes = 4;
    else if(mess.equals("may"))
      mes = 5;
    else if(mess.equals("jun"))
      mes = 6;
    else if(mess.equals("jul"))
      mes = 7;
    else if(mess.equals("ago"))
      mes = 8;
    else if(mess.equals("sep"))
      mes = 9;
    else if(mess.equals("oct"))
      mes = 10;
    else if(mess.equals("nov"))
      mes = 11;
    else if(mess.equals("dic"))
      mes = 12;
    else
      mes = 0;

    return mes;
  }

  public static synchronized int obtAno(String fech)
  {
    //Por default el lenguaje es ingles ( fechas en ingles )
    return Integer.parseInt( (fech.length() == 11) ? fech.substring(7,11) : fech.substring(6,10) );
  }

  public static synchronized String obtFechaSQL(String fech) // obtiene la fecha sql a partir de una cadena del navegador ej (12/Feb/2007);
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    String dia = (fech.length() == 11) ? fech.substring(0,2) : fech.substring(0,1);
    String mess = (fech.length() == 11) ? fech.substring(3,6) : fech.substring(2,5);
    String ano = (fech.length() == 11) ? fech.substring(7,11) : fech.substring(6,10);
    int mes;
    if(mess.equals("ene"))
      mes = 1;
    else if(mess.equals("feb"))
      mes = 2;
    else if(mess.equals("mar"))
      mes = 3;
    else if(mess.equals("abr"))
      mes = 4;
    else if(mess.equals("may"))
      mes = 5;
    else if(mess.equals("jun"))
      mes = 6;
    else if(mess.equals("jul"))
      mes = 7;
    else if(mess.equals("ago"))
      mes = 8;
    else if(mess.equals("sep"))
      mes = 9;
    else if(mess.equals("oct"))
      mes = 10;
    else if(mess.equals("nov"))
      mes = 11;
    else if(mess.equals("dic"))
      mes = 12;
    else
      mes = 0;

    String fecha;
    if(m_LANG.equals("ES"))
    	fecha = dia + "/" + mes + "/" + ano;
    else
    	fecha = mes + "/" + dia + "/" + ano;
   
    return fecha;
  }
  
  public static synchronized String obtFechaSQLh24(String fech) // obtiene la fecha sql a partir de una cadena del navegador ej (12/Feb/2007 12:23) Esta es con hora en formato 24 hrs sin AM o PM;
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    String dia = (fech.length() == 17) ? fech.substring(0,2) : fech.substring(0,1);
    String mess = (fech.length() == 17) ? fech.substring(3,6) : fech.substring(2,5);
    String ano = (fech.length() == 17) ? fech.substring(7,11) : fech.substring(6,10);
    int mes;
    if(mess.equals("ene"))
      mes = 1;
    else if(mess.equals("feb"))
      mes = 2;
    else if(mess.equals("mar"))
      mes = 3;
    else if(mess.equals("abr"))
      mes = 4;
    else if(mess.equals("may"))
      mes = 5;
    else if(mess.equals("jun"))
      mes = 6;
    else if(mess.equals("jul"))
      mes = 7;
    else if(mess.equals("ago"))
      mes = 8;
    else if(mess.equals("sep"))
      mes = 9;
    else if(mess.equals("oct"))
      mes = 10;
    else if(mess.equals("nov"))
      mes = 11;
    else if(mess.equals("dic"))
      mes = 12;
    else
      mes = 0;
    
    String fecha;
    if(m_LANG.equals("ES"))
    	fecha = dia + "/" + mes + "/" + ano;
    else
    	fecha = mes + "/" + dia + "/" + ano;
   
    return fecha;
  }
  
  public static synchronized Date getFecha(String strFecha, String format, Locale locale)
  {
	  SimpleDateFormat formatoDelTexto = new SimpleDateFormat(format,locale);
	  Date fecha = null;
	  try 
	  {
	      fecha = formatoDelTexto.parse(strFecha);
	      return fecha;
	  } 
	  catch (ParseException ex) 
	  {
	      System.out.println("Error en la fecha");
	  }
	  return null;
  }
  
  public static synchronized Date estFechaCFDI(String fech)
  {
	  int ano = Integer.parseInt(fech.substring(0,4)); 
	  int mes = Integer.parseInt(fech.substring(5,7));
	  int dia = Integer.parseInt(fech.substring(8,10));
	    
	  return new GregorianCalendar(ano, (mes-1), dia).getTime();
  }
  
  public static synchronized int getFechaDiff(Date fech1, Date fech2, String parte) // obtiene la diferencia de "Parte" entre fech1 y fech2
  {
    long diff = 0;
    long ms1 = fech1.getTime();
    long ms2 = fech2.getTime();
    
    if(parte.equals("meses"))
    	diff = ((ms1/(30*3600*24*1000)) - (ms2/(30*3600*24*1000)));
    else if(parte.equals("dias"))
    	diff = ((ms1/(3600*24*1000)) - (ms2/(3600*24*1000)));
    else if(parte.equals("horas"))
    	diff = ((ms1/(3600*1000)) - (ms2/(3600*1000)));
    else if(parte.equals("minutos"))
    	diff = ((ms1/(60*1000)) - (ms2/(60*1000)));
    else // segundos
       	diff = ((ms1/(1000)) - (ms2/(1000)));
    
    return (int)diff;
  }
  
  public static synchronized String obtFechaSQL(Date cal) // obtiene la fecha sql; este es el formato de texto para el lenguaje en el servidor sql
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    SimpleDateFormat standar;
    if(m_LANG.equals("ES"))
        standar = new SimpleDateFormat("dd/MM/yyyy");
    else
    	standar = new SimpleDateFormat("MM/dd/yyyy");
   
    return standar.format(cal);
  }

  public static synchronized String obtFechaSQL(Calendar cal) // obtiene la fecha sql; este es el formato de texto para el lenguaje en el servidor sql
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    SimpleDateFormat standar;
    Date fecha = cal.getTime();
    if(m_LANG.equals("ES"))
        standar = new SimpleDateFormat("dd/MM/yyyy");
    else
    	standar = new SimpleDateFormat("MM/dd/yyyy");
   
    return standar.format(fecha);
  }
  
  public static synchronized String obtFechaHoraSQL(Date cal) // obtiene la fecha sql; este es el formato de texto para el lenguaje en el servidor sql
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    SimpleDateFormat standar;
    if(m_LANG.equals("ES"))
        standar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    else
    	standar = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	    
    return standar.format(cal);
  }

  public static synchronized String obtFechaHoraSQL(Calendar cal) // obtiene la fecha sql; este es el formato de texto para el lenguaje en el servidor sql
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    SimpleDateFormat standar;
    Date fecha = cal.getTime();
    if(m_LANG.equals("ES"))
    	standar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    else // if
    	standar = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    return standar.format(fecha);
  }
  
  public static synchronized String obtFechaHoraSQLtfd(String fech) // obtiene la fecha sql a partir de una cadena del Timbre Fiscal Digital ej (12/Feb/2007);
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    String ano = fech.substring(0,4);
    String mes = fech.substring(5,7);
    String dia = fech.substring(8,10);
    String hora = fech.substring(11,13);
    String min = fech.substring(14,16);
    String seg = fech.substring(17,19);
    
    String fecha;
    if(m_LANG.equals("ES"))
    	fecha = dia + "/" + mes + "/" + ano + " " + hora + ":" + min + ":" + seg;
    else
    	fecha = mes + "/" + dia + "/" + ano + " " + hora + ":" + min + ":" + seg;
    
    return fecha;
  }
  
  public static synchronized String obtFechaHoraSQL(String fech) // obtiene la fecha sql a partir de una cadena del navegador ej (12/Feb/2007);
  {
	//Por default el lenguaje es ingles ( fechas en ingles )
    String dia = (fech.length() == 17) ? fech.substring(0,2) : fech.substring(0,1);
    String mess = (fech.length() == 17) ? fech.substring(3,6) : fech.substring(2,5);
    String ano = (fech.length() == 17) ? fech.substring(7,11) : fech.substring(6,10);
    String hora = (fech.length() == 17) ? fech.substring(12,14) : fech.substring(11,13);
    String min = (fech.length() == 17) ? fech.substring(15,17) : fech.substring(14,16);
    
    int mes;
    if(mess.equals("ene"))
      mes = 1;
    else if(mess.equals("feb"))
      mes = 2;
    else if(mess.equals("mar"))
      mes = 3;
    else if(mess.equals("abr"))
      mes = 4;
    else if(mess.equals("may"))
      mes = 5;
    else if(mess.equals("jun"))
      mes = 6;
    else if(mess.equals("jul"))
      mes = 7;
    else if(mess.equals("ago"))
      mes = 8;
    else if(mess.equals("sep"))
      mes = 9;
    else if(mess.equals("oct"))
      mes = 10;
    else if(mess.equals("nov"))
      mes = 11;
    else if(mess.equals("dic"))
      mes = 12;
    else
      mes = 0;
    
    String fecha;
    if(m_LANG.equals("ES"))
    	fecha = dia + "/" + mes + "/" + ano + " " + hora + ":" + min + ":00";
    else
    	fecha = mes + "/" + dia + "/" + ano + " " + hora + ":" + min + ":00";
        
    return fecha;
  }
  
  public static synchronized String obtFechaTxt(Calendar cal, String formato)
  {
	if(cal == null)
		return "";
	
    //dd MM yyyy hh mm ss
    SimpleDateFormat standar;
    Date fecha = cal.getTime();
    standar = new SimpleDateFormat(formato);
    return standar.format(fecha);
  }

  public static synchronized  String obtFechaTxt(Date cal, String formato)
  {
	if(cal == null)
		return "";
	
    SimpleDateFormat standar;
    standar = new SimpleDateFormat(formato);
    return standar.format(cal);
  }

  public static synchronized String obtHoraTxt(java.sql.Time hor, String formato)
  {
	if(hor == null)
		return "";
  
    SimpleDateFormat standar;
    standar = new SimpleDateFormat(formato);
    return standar.format(hor);
  }

  public static synchronized String obtFechaIMP(String fech, String formato) // obtiene la fecha a partir de una cadena que convierte el servlet de una fecha en el servidor y la aplica al formato de impresion (Funciona segun el lenguaje del servidor);
  {
	if(fech == null || fech.equals(""))
		return "";
	int ano, mes, dia, hora, min, seg;
	// revisar en postgres ya que esta la obtiene del sistema operativo, Es la que viene del set dinámico traduciendo esta fecha a texto
	if(fech.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") || fech.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"))
	{
		//System.out.println("1");
		ano = Integer.parseInt(fech.substring(0,4));
		mes = Integer.parseInt(fech.substring(5,7))-1;
		dia = Integer.parseInt(fech.substring(8,10));
		hora = Integer.parseInt(fech.substring(11,13));
		min = Integer.parseInt(fech.substring(14,16));
		seg = Integer.parseInt(fech.substring(17,19));
	}
	else if(fech.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}") || fech.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"))
	{
		//System.out.println("2");
		dia = Integer.parseInt(fech.substring(0,2));
		mes = Integer.parseInt(fech.substring(3,5))-1;
		ano = Integer.parseInt(fech.substring(6,10));
		hora = Integer.parseInt(fech.substring(11,13));
		min = Integer.parseInt(fech.substring(14,16));
		seg = Integer.parseInt(fech.substring(17,19));
	}
	else if(fech.matches("\\d{4}-\\d{2}-\\d{2}") || fech.matches("\\d{4}/\\d{2}/\\d{2}"))
	{
		//System.out.println("3");
		ano = Integer.parseInt(fech.substring(0,4));
		mes = Integer.parseInt(fech.substring(5,7))-1;
		dia = Integer.parseInt(fech.substring(8,10));
		hora = 0;
		min = 0;
		seg = 0;
	}
	else if(fech.matches("\\d{2}-\\d{2}-\\d{4}") || fech.matches("\\d{2}/\\d{2}/\\d{4}"))
	{
		//System.out.println("4");
		dia = Integer.parseInt(fech.substring(0,2));
		mes = Integer.parseInt(fech.substring(3,5))-1;
		ano = Integer.parseInt(fech.substring(6,10));
		hora = 0;
		min = 0;
		seg = 0;
	}
	else
	{
		//System.out.println("5");
		ano = Integer.parseInt(fech.substring(0,4));
		mes = Integer.parseInt(fech.substring(5,7))-1;
		dia = Integer.parseInt(fech.substring(8,10));
		hora = 0;
		min = 0;
		seg = 0;
	}
	
    Calendar cal = new GregorianCalendar(ano, mes, dia, hora, min, seg);

    SimpleDateFormat standar;
    Date fecha = cal.getTime();
    standar = new SimpleDateFormat(formato);
    
    return standar.format(fecha);

  }

  public static synchronized String convertirMesNumerico(int mes)
  {
    String res;
    switch(mes)
    {
      case 1:
        res = "01";
        break;
      case 2:
        res = "02";
        break;
      case 3:
        res = "03";
        break;
      case 4:
        res = "04";
        break;
      case 5:
        res = "05";
        break;
      case 6:
        res = "06";
        break;
      case 7:
        res = "07";
        break;
      case 8:
        res = "08";
        break;
      case 9:
        res = "09";
        break;
      case 10:
        res = "10";
        break;
      case 11:
        res = "11";
        break;
      case 12:
        res = "12";
        break;
      default:
        res = "13";
        break;

    }

    return res;

  }
  
  public static synchronized String convertirMesCorto(int mes)
  {
    String res;
    switch(mes)
    {
      case 1:
        res = Msj("GLB", "GLB", "GLB", "ENE", 3);
        break;
      case 2:
        res = Msj("GLB", "GLB", "GLB", "FEB", 3);
        break;
      case 3:
        res = Msj("GLB", "GLB", "GLB", "MAR", 3);
        break;
      case 4:
        res = Msj("GLB", "GLB", "GLB", "ABR", 3);
        break;
      case 5:
        res = Msj("GLB", "GLB", "GLB", "MAY", 3);
        break;
      case 6:
        res = Msj("GLB", "GLB", "GLB", "JUN", 3);
        break;
      case 7:
        res = Msj("GLB", "GLB", "GLB", "JUL", 3);
        break;
      case 8:
        res = Msj("GLB", "GLB", "GLB", "AGO", 3);
        break;
      case 9:
        res = Msj("GLB", "GLB", "GLB", "SEP", 3);
        break;
      case 10:
        res = Msj("GLB", "GLB", "GLB", "OCT", 3);
        break;
      case 11:
        res = Msj("GLB", "GLB", "GLB", "NOV", 3);
        break;
      case 12:
        res = Msj("GLB", "GLB", "GLB", "DIC", 3);
        break;
      default:
        res = Msj("GLB", "GLB", "GLB", "ESP", 3);
        break;

    }

    return res;

  }
  
  public static synchronized String convertirMesLargo(int mes)
  {
    String res;
    switch(mes)
    {
      case 1:
        res = Msj("GLB", "GLB", "GLB", "ENE", 4);
        break;
      case 2:
        res = Msj("GLB", "GLB", "GLB", "FEB", 4);
        break;
      case 3:
        res = Msj("GLB", "GLB", "GLB", "MAR", 4);
        break;
      case 4:
        res = Msj("GLB", "GLB", "GLB", "ABR", 4);
        break;
      case 5:
        res = Msj("GLB", "GLB", "GLB", "MAY", 4);
        break;
      case 6:
        res = Msj("GLB", "GLB", "GLB", "JUN", 4);
        break;
      case 7:
        res = Msj("GLB", "GLB", "GLB", "JUL", 4);
        break;
      case 8:
        res = Msj("GLB", "GLB", "GLB", "AGO", 4);
        break;
      case 9:
        res = Msj("GLB", "GLB", "GLB", "SEP", 4);
        break;
      case 10:
        res = Msj("GLB", "GLB", "GLB", "OCT", 4);
        break;
      case 11:
        res = Msj("GLB", "GLB", "GLB", "NOV", 4);
        break;
      case 12:
        res = Msj("GLB", "GLB", "GLB", "DIC", 4);
        break;
      default:
        res = Msj("GLB", "GLB", "GLB", "ESP", 4);
        break;

    }

    return res;

  }
  // Regresa verdadero si ya existe un registro, y falso si no existe
  public static synchronized  boolean yaRegistradoEnFsi(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    HttpSession ses = request.getSession(true);
    JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
    if(princ == null)
    {
    	if(request.getParameter("mobile") != null)
    		princ = new JSesionPrincipal(true);
    	else
    		princ = new JSesionPrincipal(false);
    	
    	ses.setAttribute("forseti", princ);
    	return false;
    }
    // si ya tenia inicio de sesion, verifica que este registrado
    if(!princ.getRegistrado())
      return false;

    return true;
  }
  
  public static synchronized  boolean yaRegistradoEnFsiAdmin(HttpServletRequest request, HttpServletResponse response)
  	throws ServletException, IOException
  {
	HttpSession ses = request.getSession(true);
	JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");
	if(princ == null)
	{
	  princ = new JSesionPrincipal(false);
	  ses.setAttribute("fsi_admin", princ);
	  return false;
	}
	// si ya tenia inicio de sesion, verifica que este registrado
	if(!princ.getRegistrado())
	  return false;
	
	return true;
  }

  public static synchronized  boolean yaRegistradoEnFsiB2B(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
  {
	HttpSession ses = request.getSession(true);
	JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");
	if(princ == null)
	{
	  princ = new JSesionPrincipal(false);
	  ses.setAttribute("fsi_b2b", princ);
	  return false;
	}
	// si ya tenia inicio de sesion, verifica que este registrado
	if(!princ.getRegistrado())
	  return false;
	
	return true;
  }
  
  public static synchronized String getMensaje(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    HttpSession ses = request.getSession(true);
    JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");

    return princ.getMensaje();
  }
  
  public static synchronized short getID_Mensaje(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
  {
	HttpSession ses = request.getSession(true);
	JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("forseti");
	
	return princ.getID_Mensaje();
  }

  public static synchronized String getMensajeAdmin(HttpServletRequest request, HttpServletResponse response)
  	throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_admin");

	  return princ.getMensaje();
  }

  public static synchronized String getMensajeProcSR(MutableInt ID_Mensaje, StringBuffer sbMensaje)
  {
	  if(ID_Mensaje == null)
		  return "";
	  
	  String Mensaje;
	  String mens1 = "<tr><td><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\" bgcolor=\""; // mensaje antes del color
	  String mens2 = "\"><tr><td align=\"right\" valign=\"top\"><img src=\""; // mensaje antes de imagen
	  String mens3 = "\"></td><td class=\""; // mensaje antes de clase que define el color del texto
	  String mens4 = "\"align=\"left\">"; //Mensaje antes de definir el texto principal (variable GBL_Mensaje)
	  String mens5 = "</td></tr></table></td></tr>"; // Final de estructura de mensaje

	  switch(ID_Mensaje.intValue())
	  { 
	      case 0: // Mensaje ok en verde e icono de Afirmativo
	        Mensaje = mens1 + "#00CC00" + mens2 + "../imgfsi/icono-ok.gif" + mens3 + "titChico" + mens4 + sbMensaje.toString() + mens5;
	        break;
	      case 1: // Alerta en amarillo e icono de alerta
	        Mensaje = mens1 + "#FF9900" + mens2 + "../imgfsi/icono-alerta.gif" + mens3 + "titChico" + mens4 + sbMensaje.toString() + mens5;
	        break;
	      case 2: // Pregunta en azul e icono de pregunta
	        Mensaje = mens1 + "#3366CC" + mens2 + "../imgfsi/icono-pregunta.gif" + mens3 + "titChico" + mens4 + sbMensaje.toString() + mens5;
	        break;
	      case 3: // Error en rojo e icono de error
	        Mensaje = mens1 + "#CC0000" + mens2 + "../imgfsi/icono-error.gif" + mens3 + "titChico" + mens4 + sbMensaje.toString() + mens5;
	        break;
	      default: // Informacion (sin icono, sin mensaje)
	        Mensaje = "";
	        break;
	  }
 
	  return Mensaje;
  }
  
  public static synchronized String getMensajeB2B(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  JSesionPrincipal princ = (JSesionPrincipal)ses.getAttribute("fsi_b2b");

	  return princ.getMensaje();
  }

  
  public static synchronized JSesionPrincipal getSesion(HttpServletRequest request)
    throws ServletException, IOException
  {
    HttpSession ses = request.getSession(true);
    return (JSesionPrincipal)ses.getAttribute("forseti");
  }
  
  public static synchronized JSesionPrincipal getSesionAdmin(HttpServletRequest request)
  	throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  return (JSesionPrincipal)ses.getAttribute("fsi_admin");
  }
  
  public static synchronized JSesionPrincipal getSesionB2B(HttpServletRequest request)
	throws ServletException, IOException
  {
	  HttpSession ses = request.getSession(true);
	  return (JSesionPrincipal)ses.getAttribute("fsi_b2b");
  }

  public static synchronized String CantEnLetra(float cant, String id_moneda, String moneda)
  {
	 String MONEDA = (moneda == null) ? "" : moneda;
	 String MN;
	 if(id_moneda == null || !id_moneda.equals("1"))
		 MN = "";
	 else
		 MN = "m.n.";
	 
     if(cant == 0.00)
        return "cero " + MONEDA + " 00/100 " + MN;

     StringBuffer sCant;
     String sDec = "",sCen = "",sMil = "",s10Mil = "",s100Mil = "",sMillon = "",
                  s10Millon = "",s100Millon = "",s1000Millon = "";
     String str, decim;
     str = Float.toString(redondear(cant, 2));

     int index = str.indexOf('.');
     if(index == -1)
        decim = "00/100";
     else
     {
        decim = str.substring(index + 1);
        if(decim.length() == 1)
       	 	decim = decim + "0/100";
        else
        	decim = decim + "/100";
       
     }
     
     
     int cantent = (int)Math.floor((double)redondear(cant, (byte)2));
     int dec = 0, cen = 0, mil = 0, diezmil = 0, cienmil = 0, millon = 0,
                  diezmillon = 0, cienmillon = 0, milmillon = 0;

     sCant = new StringBuffer(Integer.toString(cantent));
     sCant.reverse();

     int len = sCant.length();

     if(len > 0)
     {
        dec = Integer.parseInt(sCant.substring(0,1));
        switch(dec)
        {
           case 1: sDec = "un";
               break;
           case 2: sDec = "dos";
               break;
           case 3: sDec = "tres";
               break;
           case 4: sDec = "cuatro";
               break;
           case 5: sDec = "cinco";
               break;
           case 6: sDec = "seis";
               break;
           case 7: sDec = "siete";
               break;
           case 8: sDec = "ocho";
               break;
           case 9: sDec = "nueve";
               break;
           default: sDec = "";
               break;
        }
     }

     if(len > 1)
     {
        cen = Integer.parseInt(sCant.substring(1,2));
        switch(cen)
        {
           case 1: if(dec == 0)
                      sCen = "diez";
                   else if(dec == 1)
                   {
                      sCen = "once";
                      sDec = "";
                   }
                   else if(dec == 2)
                   {
                      sCen = "doce";
                      sDec = "";
                   }
                   else if(dec == 3)
                   {
                      sCen = "trece";
                      sDec = "";
                   }
                   else if(dec == 4)
                   {
                      sCen = "catorce";
                      sDec = "";
                   }
                   else if(dec == 5)
                   {
                      sCen = "quince";
                      sDec = "";
                   }
                   else
                      sCen = "dieci";
                break;
           case 2: if(dec == 0)
                      sCen = "veinte";
                   else
                      sCen = "veinti";
                break;
           case 3: if(dec == 0)
                      sCen = "treinta";
                   else
                      sCen = "treinta y ";
                break;
           case 4: if(dec == 0)
                      sCen = "cuarenta";
                   else
                      sCen = "cuarenta y ";
                break;
           case 5: if(dec == 0)
                      sCen = "cincuenta";
                   else
                      sCen = "cincuenta y ";
                break;
           case 6: if(dec == 0)
                      sCen = "sesenta";
                   else
                      sCen = "sesenta y ";
                break;
           case 7: if(dec == 0)
                      sCen = "setenta";
                   else
                      sCen = "setenta y ";
                break;
           case 8: if(dec == 0)
                      sCen = "ochenta";
                   else
                      sCen = "ochenta y ";
                break;
           case 9: if(dec == 0)
                      sCen = "noventa";
                   else
                      sCen = "noventa y ";
                break;
           default: sCen = "";
                break;
        }
     }

     if(len > 2)
     {
        mil = Integer.parseInt(sCant.substring(2,3));
        switch(mil)
        {
           case 1: if(dec == 0 && cen == 0)
                      sMil = "cien";
                   else
                      sMil = "ciento ";
                break;
           case 2: sMil = "doscientos ";
                break;
           case 3: sMil = "trescientos ";
                break;
           case 4: sMil = "cuatrocientos ";
                break;
           case 5: sMil = "quinientos ";
                break;
           case 6: sMil = "seiscientos ";
                break;
           case 7: sMil = "setecientos ";
                break;
           case 8: sMil = "ochocientos ";
                break;
           case 9: sMil = "novecientos ";
                break;
           default: sMil = "";
                break;
        }
     }

     if(len > 3)
     {
        diezmil = Integer.parseInt(sCant.substring(3,4));
        switch(diezmil)
        {
           case 1: s10Mil = "un mil ";
                break;
           case 2: s10Mil = "dos mil ";
                break;
           case 3: s10Mil = "tres mil ";
                break;
           case 4: s10Mil = "cuatro mil ";
                break;
           case 5: s10Mil = "cinco mil ";
                break;
           case 6: s10Mil = "seis mil ";
                break;
           case 7: s10Mil = "siete mil ";
                break;
           case 8: s10Mil = "ocho mil ";
                break;
           case 9: s10Mil = "nueve mil ";
                break;
           default: s10Mil = "";
                break;
        }
     }

     if(len > 4 )
     {
        cienmil = Integer.parseInt(sCant.substring(4,5));
        switch(cienmil)
        {
           case 1: if(diezmil == 0)
                      s100Mil = "diez mil ";
                   else if(diezmil == 1)
                   {
                      s100Mil = "once mil ";
                      s10Mil = "";
                   }
                   else if(diezmil == 2)
                   {
                      s100Mil = "doce mil ";
                      s10Mil = "";
                   }
                   else if(diezmil == 3)
                   {
                      s100Mil = "trece mil ";
                      s10Mil = "";
                   }
                   else if(diezmil == 4)
                   {
                      s100Mil = "catorce mil ";
                      s10Mil = "";
                   }
                   else if(diezmil == 5)
                   {
                      s100Mil = "quince mil ";
                      s10Mil = "";
                   }
                   else
                      s100Mil = "dieci";
                break;
           case 2: if(diezmil == 0)
                      s100Mil = "veinte mil ";
                   else
                      s100Mil = "veinti";
                break;
           case 3: if(diezmil == 0)
                      s100Mil = "treinta mil";
                   else
                      s100Mil = "treinta y ";
                break;
           case 4: if(diezmil == 0)
                      s100Mil = "cuarenta mil ";
                   else
                      s100Mil = "cuarenta y ";
                break;
           case 5: if(diezmil == 0)
                      s100Mil = "cincuenta mil ";
                   else
                      s100Mil = "cincuenta y ";
                break;
           case 6: if(diezmil == 0)
                      s100Mil = "sesenta mil ";
                   else
                      s100Mil = "sesenta y ";
                break;
           case 7: if(diezmil == 0)
                      s100Mil = "setenta mil ";
                   else
                      s100Mil = "setenta y ";
                break;
           case 8: if(diezmil == 0)
                      s100Mil = "ochenta mil ";
                   else
                      s100Mil = "ochenta y ";
                break;
           case 9: if(diezmil == 0)
                      s100Mil = "noventa mil ";
                   else
                      s100Mil = "noventa y ";
                break;
           default: s100Mil = "";
                break;
        }
     }

     if(len > 5)
     {
        millon = Integer.parseInt(sCant.substring(5,6));
        switch(millon)
        {
           case 1: if(cienmil == 0 && diezmil == 0)
                      sMillon = "cien mil ";
                   else
                      sMillon = "ciento ";
                break;
           case 2: if(cienmil == 0 && diezmil == 0)
                      sMillon = "doscientos mil ";
                   else
                      sMillon = "doscientos ";
                break;
           case 3: if(cienmil == 0 && diezmil == 0)
                      sMillon = "trescientos mil ";
                   else
                      sMillon = "trescientos ";
                break;
           case 4: if(cienmil == 0 && diezmil == 0)
                      sMillon = "cuatrocientos mil ";
                   else
                      sMillon = "cuatrocientos ";
                break;
           case 5: if(cienmil == 0 && diezmil == 0)
                      sMillon = "quinientos mil ";
                   else
                      sMillon = "quinientos ";
                break;
           case 6: if(cienmil == 0 && diezmil == 0)
                      sMillon = "seiscientos mil ";
                   else
                      sMillon = "seiscientos ";
                break;
           case 7: if(cienmil == 0 && diezmil == 0)
                      sMillon = "setecientos mil ";
                   else
                      sMillon = "setecientos ";
                break;
           case 8: if(cienmil == 0 && diezmil == 0)
                      sMillon = "ochocientos mil ";
                   else
                      sMillon = "ochocientos ";
                break;
           case 9: if(cienmil == 0 && diezmil == 0)
                      sMillon = "novecientos mil ";
                   else
                      sMillon = "novecientos ";
                break;
           default: sMillon = "";
                break;
        }
     }

     // millones
     if(len > 6)
     {
        diezmillon = Integer.parseInt(sCant.substring(6,7));
        switch(diezmillon)
        {
           case 1: s10Millon = "un millon ";
                break;
           case 2: s10Millon = "dos millones ";
                break;
           case 3: s10Millon = "tres millones ";
                break;
           case 4: s10Millon = "cuatro millones ";
                break;
           case 5: s10Millon = "cinco millones ";
                break;
           case 6: s10Millon = "seis millones ";
                break;
           case 7: s10Millon = "siete millones ";
                break;
           case 8: s10Millon = "ocho millones ";
                break;
           case 9: s10Millon = "nueve millones ";
                break;
           default: s10Millon = "";
                break;
        }
     }

     if(len > 7 )
     {
        cienmillon = Integer.parseInt(sCant.substring(7,8));
        switch(cienmillon)
        {
           case 1: if(diezmillon == 0)
                      s100Millon = "diez millones ";
                   else if(diezmillon == 1)
                   {
                      s100Millon = "once millones ";
                      s10Millon = "";
                   }
                   else if(diezmillon == 2)
                   {
                      s100Millon = "doce millones ";
                      s10Millon = "";
                   }
                   else if(diezmillon == 3)
                   {
                      s100Millon = "trece millones ";
                      s10Millon = "";
                   }
                   else if(diezmillon == 4)
                   {
                      s100Millon = "catorce millones ";
                      s10Millon = "";
                   }
                   else if(diezmillon == 5)
                   {
                      s100Millon = "quince millones ";
                      s10Millon = "";
                   }
                   else
                      s100Millon = "dieci";
                break;
           case 2: if(diezmillon == 0)
                      s100Millon = "veinte millones ";
                   else
                      s100Millon = "veinti";
                break;
           case 3: if(diezmillon == 0)
                      s100Millon = "treinta millones ";
                   else
                      s100Millon = "treinta y ";
                break;
           case 4: if(diezmillon == 0)
                      s100Millon = "cuarenta millones ";
                   else
                      s100Millon = "cuarenta y ";
                break;
           case 5: if(diezmillon == 0)
                      s100Millon = "cincuenta millones ";
                   else
                      s100Millon = "cincuenta y ";
                break;
           case 6: if(diezmillon == 0)
                      s100Millon = "sesenta millones ";
                   else
                      s100Millon = "sesenta y ";
                break;
           case 7: if(diezmillon == 0)
                      s100Millon = "setenta millones ";
                   else
                      s100Millon = "setenta y ";
                break;
           case 8: if(diezmillon == 0)
                      s100Millon = "ochenta millones ";
                   else
                      s100Millon = "ochenta y ";
                break;
           case 9: if(diezmillon == 0)
                      s100Millon = "noventa millones ";
                   else
                      s100Millon = "noventa y ";
                break;
           default: s100Millon = "";
                break;
         }
         if( millon == 0 && cienmil == 0 &&
                   diezmil == 0 && mil == 0 && cen == 0 && dec == 0)
            s100Millon += "de";
     }

     if(len > 8)
     {
        milmillon = Integer.parseInt(sCant.substring(8,9));
        switch(milmillon)
        {
           case 1: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "cien millones ";
                   else
                      s1000Millon = "ciento ";
                break;
           case 2: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "doscientos millones ";
                   else
                      s1000Millon = "doscientos ";
                break;
           case 3: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "trescientos millones ";
                   else
                      s1000Millon = "trescientos ";
                break;
           case 4: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "cuatrocientos millones ";
                   else
                      s1000Millon = "cuatrocientos ";
                break;
           case 5: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "quinientos millones ";
                   else
                      s1000Millon = "quinientos ";
                break;
           case 6: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "seiscientos millones ";
                   else
                      s1000Millon = "seiscientos ";
                break;
           case 7: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "setecientos millones ";
                   else
                      s1000Millon = "setecientos ";
                break;
           case 8: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "ochocientos millones ";
                   else
                      s1000Millon = "ochocientos ";
                break;
           case 9: if(cienmillon == 0 && diezmillon == 0)
                      s1000Millon = "novecientos millones ";
                   else
                      s1000Millon = "novecientos ";
                break;
           default: s1000Millon = "";
                break;
        }
     }

     String res = s1000Millon + s100Millon + s10Millon + sMillon + s100Mil + s10Mil + sMil + sCen + sDec;
     res += " " + MONEDA + " " + decim + " " + MN;

     //sCant.MakeUpper();

     return res;

  }

}
