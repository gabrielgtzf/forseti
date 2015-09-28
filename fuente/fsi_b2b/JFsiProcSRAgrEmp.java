package fsi_b2b;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import forseti.JAccesoBD;
import forseti.JBDRegistradasSet;
import forseti.JBajarArchivo;
import forseti.JUtil;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JSRVServidoresSet;

@SuppressWarnings("serial")
public class JFsiProcSRAgrEmp extends HttpServlet
{
	private static boolean m_bCrearBDExt = false;
	
	@Override
	public void init() 
	{
		JAdmVariablesSet set = new JAdmVariablesSet(null);
		set.ConCat(true);
		set.m_Where = "ID_Variable = 'CREARBDEXT'";
		set.Open();
		
		if(set.getAbsRow(0).getVEntero() == 1) // Si permite el agregado desde el exterior de bases de datos
			m_bCrearBDExt = true;
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
	{
		String ERROR = null, codErr = null; StringBuffer msj = new StringBuffer();
			
		try 
		{
			// Verificacion
			if(request.getParameter("SERVER") == null || request.getParameter("USER") == null || request.getParameter("PASSWORD") == null ||
					request.getParameter("nombre") == null || request.getParameter("compania") == null
					|| request.getParameter("direccion") == null  || request.getParameter("poblacion") == null
					|| request.getParameter("cp") == null  || request.getParameter("mail") == null || request.getParameter("mail").equals("")
					|| request.getParameter("web") == null
					|| request.getParameter("password") == null  || request.getParameter("confpwd") == null ||
					request.getParameter("nombre").equals("") || request.getParameter("compania").equals("") ||
					request.getParameter("password").equals("") || request.getParameter("confpwd").equals("") )
			{
				System.out.println("No recibi parametros de conexion antes de la empresa a crear");
				ERROR = "ERROR: El servidor no recibió todos los parametros de conexión";
				codErr = "3";
				ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
						request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);
			}
			else
			{
				if(!m_bCrearBDExt) // No permite el agregado desde el exterior de bases de datos
				{
					System.out.println("No permite el agregado desde el exterior de bases de datos");
					ERROR = "ERROR: Este servidor no permite el agregado de bases de datos desde el exterior";
					codErr = "3";
					ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
							request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);
				}
			}
	  		
	  		//Hasta aqui se han enviado todos los parametros ninguno nulo
			if(ERROR == null)
			{
				// Primero obtiene info del PAC
				if(!obtenInfoSRV(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
						request.getParameter("USER"), request.getParameter("PASSWORD"), msj))
				{
					System.out.println("El usuario y/o contraseña de servicio estan mal");
					ERROR = msj.toString();
					codErr = "3";
					ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
							request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 3);

				}
				else
				{
					if(!puedeAgregar(request, response, msj))
					{
						System.out.println("La empresa ya existe o se esta actualizando el servidor");
						ERROR = msj.toString();
						codErr = "1";
						ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
								request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 1);
					}
					else
					{
						if(!agregarEmpresa(request, response, msj))
						{
							System.out.println("No se pudo agregar la empresa");
							ERROR = msj.toString();
							codErr = "2";
							ingresarRegistroFallido(request.getRemoteAddr(), request.getRemoteHost(), request.getParameter("SERVER"),
									request.getParameter("USER"), request.getParameter("PASSWORD"), request, ERROR, 2);
						}
						else
						{
							ingresarRegistroExitoso(request.getParameter("SERVER"), request.getParameter("nombre"));
						}
					}
					
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ERROR = "ERROR DE EXCEPCION EN SERVIDOR DE AGREGADO DE EMPRESAS: " + e.getMessage();
		}
		
		//Genera el archivo XML de con la respuesta para ser devuelto al Servidor
		Element SIGN = new Element("SIGN");
		if(ERROR != null)
		{
			SIGN.setAttribute("CodError",codErr);
			SIGN.setAttribute("Msj",ERROR);
		}
		else
		{
			SIGN.setAttribute("CodError","0");
			SIGN.setAttribute("Msj",msj.toString());
		}
		Document Reporte = new Document(SIGN);
			
		Format format = Format.getPrettyFormat();
		format.setEncoding("utf-8");
		format.setTextMode(TextMode.NORMALIZE);
		XMLOutputter xmlOutputter = new XMLOutputter(format);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		xmlOutputter.output(Reporte, out);
			
		byte[] data = out.toByteArray();
		ByteArrayInputStream istream = new ByteArrayInputStream(data);
					
		String destino = "SIGN.xml";
		JBajarArchivo fd = new JBajarArchivo();
		fd.doDownload(response, getServletConfig().getServletContext(), istream, "text/xml", data.length, destino);
	}

	private void ingresarRegistroFallido(String IP, String host, String servidor, String usuario, String password, HttpServletRequest request, String error, int nivelError)
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try 
	    {
	        bufferedReader =  request.getReader();
	        char[] charBuffer = new char[128];
	        int bytesRead;
	        while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) 
	        {
	            sb.append(charBuffer, 0, bytesRead);
	        }
	        
	    } 
	    catch (IOException ex) 
	    {
	        ex.printStackTrace();
	    } 
	    finally 
	    {
	    	if (bufferedReader != null) 
	    	{
	    		try 
	    		{
	                bufferedReader.close();
	            } 
	    		catch (IOException ex) 
	    		{
	                ex.printStackTrace();
	            }
	        }
	    }
		
	    //System.out.println(sb.toString());
	    Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_SRV_REGISTROS_FALLIDOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.q(IP) + "','" + JUtil.q(host) + "','" + JUtil.obtFechaHoraSQL(cal) + "','" + (servidor != null ? JUtil.q(servidor) : "nulo") + "','" + 
	    			  (usuario != null ? JUtil.q(usuario) : "nulo") + "','" + (password != null ? JUtil.q(password) : "nulo") + "','" + JUtil.q(sb.toString()) + "','" + JUtil.q(error) + "','" + nivelError + "')";
	    try
	    {
	    	Connection con = JAccesoBD.getConexion();
	    	Statement s    = con.createStatement();
	    	s.executeUpdate(SQL);
	    	s.close();
	    	JAccesoBD.liberarConexion(con);
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}

	private boolean obtenInfoSRV(String IP, String host, String servidor, String usuario, String password, StringBuffer msj)
	{
	      JSRVServidoresSet bd = new JSRVServidoresSet(null);
	      bd.ConCat(true);
	      bd.m_Where = "ID_Servidor = '" + JUtil.p(servidor) + "' and Usuario = '" + JUtil.p(usuario) + "' and Pass = '" + JUtil.q(password) + "' and Status = 'A'";
	      bd.Open();
	      
	      if(bd.getNumRows() == 0) // No existe registro de este servidor
	      {
	    	  msj.append("ERROR: La autenticación a este servidor ha fallado, verifica que tu clave de servidor, usuario y contraseña sean las correctas y vuelve a intentarlo");
	    	  return false;
	      }
	      else //Si existe el servidor, entonces da los datos para edicom
	       	  return true;
	}
	
	public boolean agregarEmpresa(HttpServletRequest request, HttpServletResponse response, StringBuffer mensaje)
  	      throws ServletException, IOException
  	{
		boolean res = false;
     	String addr = JUtil.getADDR(), port = JUtil.getPORT(), pass = JUtil.getPASS();
     	//GENERA LA BASE DE DATOS MANUALMENTE. (NO A TRAVES DE PROCEDIMIENTO ALMACENADO)
     	Calendar fecha = GregorianCalendar.getInstance(); 
     	String path = "/usr/local/forseti/log/CREAR-FSIBD_" + request.getParameter("nombre") + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
     	FileWriter filewri		= new FileWriter(path, true);
     	PrintWriter pw			= new PrintWriter(filewri);
     	Connection conn = null;
     	Statement s = null;
  	       	
     	try
     	{
     		pw.println("----------------------------------------------------------------------------");
     		pw.println("             " + "AGREGAR EMPRESA DESDE " + request.getRemoteAddr() + ": " + JUtil.p(request.getParameter("nombre")) + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
     		pw.println("----------------------------------------------------------------------------");
     		pw.flush();
  				
     		String driver = "org.postgresql.Driver";
     		Class.forName(driver).newInstance();
     		// Apartir de aqui es crítico el proceso Genera el usuario
     		String url = "jdbc:postgresql://" + addr + ":" + port + "/postgres?user=forseti&password=" + pass;
     		conn = DriverManager.getConnection(url);
     		s = conn.createStatement();
     		String sql = 
     				"CREATE ROLE " + JUtil.p(request.getParameter("nombre")).toLowerCase() +  " LOGIN\n" +
     				"ENCRYPTED PASSWORD '" + JUtil.q(request.getParameter("password")) + "'\n" +
  	           	"NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE NOREPLICATION;\n" +
  	           	"GRANT " + JUtil.p(request.getParameter("nombre")).toLowerCase() +  " TO forseti;";
     		s.execute(sql);
     		pw.println("Creado el usuario dueño de la base de datos de la empresa...");
     		pw.flush();
     		// Genera la base de datos del usuario
     		sql = 
     				"CREATE DATABASE \"FSIBD_" + JUtil.p(request.getParameter("nombre")) + "\"\n" +
     						"	WITH OWNER = " + JUtil.p(request.getParameter("nombre")).toLowerCase() + "\n" +
  	            	"	TEMPLATE = template0\n" +
  	            	"	ENCODING = 'UTF8'\n" +
  	            	"	LC_COLLATE = 'es_MX.UTF-8'\n" +
  	            	"	LC_CTYPE = 'es_MX.UTF-8'\n" +
  	            	"	CONNECTION LIMIT = -1;";
     		s.execute(sql);
     		s.close();
     		conn.close();
     		pw.println("Creada la base de datos de la empresa...");
     		pw.flush();
     		// Inserta registro nuevo de BD
     		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
     		conn = DriverManager.getConnection(url);
     		s = conn.createStatement();
     		sql = "INSERT INTO tbl_bd(id_bd, nombre, usuario, \"password\", fechaalta, compania, direccion, poblacion, cp, mail, web, su)\n";
     		sql += "VALUES (default, 'FSIBD_" + JUtil.p(request.getParameter("nombre")) + "','" + JUtil.p(request.getParameter("nombre")).toLowerCase() + "','" + JUtil.q(request.getParameter("password")) + "','" + JUtil.obtFechaSQL(Calendar.getInstance()) + "','" + JUtil.p(request.getParameter("compania")) + "','" + JUtil.p(request.getParameter("direccion")) + "','" + JUtil.p(request.getParameter("poblacion")) + "','" + JUtil.p(request.getParameter("cp")) + "','" + JUtil.p(request.getParameter("mail")) + "','" + JUtil.p(request.getParameter("web")) + "','0');\n";
     		s.execute(sql);
     		s.close();
     		conn.close();
     		pw.println("Insertado registro de base de datos en FORSETI_ADMIN... SU = 0");
     		pw.println("--------------------- AHORA LA ESTRUCTURA DE LA BASE DE DATOS: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss") + " ---------------------");
     		pw.flush();
     		// Se conecta a la base de datos principal creada para generar su estructura
     		// Esta depende del archivo .forseti_inibd que es el que tiene la estructura de todas las BD
     		url = "jdbc:postgresql://" + addr + ":" + port + "/FSIBD_" + JUtil.p(request.getParameter("nombre")) + "?user=" + JUtil.p(request.getParameter("nombre")).toLowerCase() + "&password=" + JUtil.q(request.getParameter("password"));
     		conn = DriverManager.getConnection(url);
     		s = conn.createStatement();
     		// Ahora toma del archivo de inicio
     		// bin/.forseti_inibd que contiene la estructura de inicio de cada base de datos
     		sql = "";
     		FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_inibd");
     		BufferedReader buff     = new BufferedReader(file);
     		boolean eof             = false;
     		String strbas = "";
     		Vector<String> actbd_sql = new Vector<String>();
     		
     		while(!eof)
     		{
     			String line = buff.readLine();
     			if(line == null)
     				eof = true;
     			else
     			{
     				if(line.indexOf("--@FIN_BLOQUE") == -1)
     					strbas += line + "\n";
     				else
     				{
     					actbd_sql.addElement(strbas);
     					strbas = "";
     				}
     			}
     		}
     		buff.close();
     		file.close();
     		buff = null;
     		file = null;
  	            
     		// Ahora, ejecuta la larga consulta......
     		for(int j = 0; j < actbd_sql.size(); j++)
     		{
     			sql = actbd_sql.get(j);
     			pw.println(sql);
     			pw.flush();
     			s.executeUpdate(sql);
     		}
  				            
     		pw.println("------------------------------------------------------------------------");
     		pw.println("Fin de estructura de base de datos. Ahora los mensajes del sistema. " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
     		pw.flush();
     		// Ahora toma del archivo de lenguaje segun sea el caso (Hasta ahora solo permite Español (es) En proximas actualizaciones será multiidioma)
     		sql = "";
     		file = new FileReader("/usr/local/forseti/bin/.forseti_es");
     		buff = new BufferedReader(file);
     		eof  = false;
     		while(!eof)
     		{
     			String line = buff.readLine();
     			if(line == null)
     				eof = true;
     			else
     			{
     				if(line.equals("__INIT"))
     				{
     					String alc = "", mod = "", sub = "", elm = "", msj1 = "", msj2 = "", msj3 = "", msj4 = "", msj5 = "";
     					for(int i = 1; i <= 9; i++)
     					{
     						line = buff.readLine();
     						switch(i)
     						{
     							case 1: msj1 = "'" + line + "'";
     								break;
     							case 2: msj2 = (line.equals("null") ? "null" : "'" + line + "'");
  	                			break;
     							case 3: msj3 = (line.equals("null") ? "null" : "'" + line + "'");
  	                			break;
     							case 4: msj4 = (line.equals("null") ? "null" : "'" + line + "'");
  	                			break;
     							case 5: msj5 = (line.equals("null") ? "null" : "'" + line + "'");
  	                			break;
     							case 6: alc = "'" + line + "'";
  	                			break;
     							case 7: mod = "'" + line + "'";
  	                			break;
     							case 8: sub = "'" + line + "'";
  	                			break;
     							case 9: elm = "'" + line + "'";
  	                			break;
     						}
     					} 
     					String sqllang = "INSERT INTO tbl_msj\nVALUES(";
     					sqllang += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ")";
     					//Aqui genera el registro
     					pw.println(sqllang);
     					pw.flush();
     					s.execute(sqllang);
     				}
     			}
     		}
     		buff.close();
     		file.close();
     		buff = null;
     		file = null;
  	            
     		s.close();
     		conn.close();
     		url = "jdbc:postgresql://" + addr + ":" + port + "/FORSETI_ADMIN?user=forseti&password=" + pass;
     		conn = DriverManager.getConnection(url);
     		s = conn.createStatement();
     		sql = "UPDATE tbl_bd\n";
     		sql += "SET su = '3'\n";
     		sql += "WHERE nombre = 'FSIBD_" + JUtil.p(request.getParameter("nombre")) + "';\n";
     		pw.println(sql);
     		pw.flush();
     		s.execute(sql);
     		s.close();
     		conn.close();
     		//Termina el proceso critico.
     		pw.println("-------------------- FIN DE MENSAJES DE SISTEMA " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss") + " --------------------");
     		pw.println("Creando Archivos emp...");
     		pw.flush();
     		File dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")));
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/CE");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/cont");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/nom");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/nom/TFDs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/nom/PDFs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/ven");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/ven/TFDs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/ven/PDFs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/certs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/smtp");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/comp");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/comp/TFDs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/comp/PDFs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/CFDs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/TFDs");
     		dir.mkdir();
     		dir = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/PDFs");
     		dir.mkdir();
     		// ahora los archivos de smtp
     		String msg =
     				"Nota de credito XML y PDF enviado por forseti\n" +
     				"text/html\n" +
     				"<html>\n" +
     				"  <head>\n" +
     				"    <title>Nota de crédito</title>\n" +
     				"  </head>\n" +
     				"  <body>\n" +
     				"  <h1>Nota de credito</h1>\n" +
     				"  Una Nota de credito a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
     				"  </body>\n" +
     				"</html>";
     		File f = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/smtp/DSV-FORSETI.msg");
     		FileWriter fw = new FileWriter(f);
     		fw.write(msg);
     		fw.close();
     		msg =
     				"Factura XML y PDF enviado por forseti\n" +
     				"text/html\n" +
     				"<html>\n" +
     				"  <head>\n" +
     				"    <title>Factura</title>\n" +
     				"  </head>\n" +
     				"<body>\n" +
     				" <h1>Factura</h1>\n" +
     				" Una factura a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
     				"</body>\n" +
     				"</html>";
     		f = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/smtp/FAC-FORSETI.msg");
     		fw = new FileWriter(f);
     		fw.write(msg);
     		fw.close();
     		msg =
     				"Recibo de Nomina XML y PDF enviado por forseti\n" +
     				"text/html\n" +
     				"<html>\n" +
     				"  <head>\n" +
     				"    <title>Recibo de Nomina</title>\n" +
     				"  </head>\n" +
     				"  <body>\n" +
     				"  <h1>Recibo de nomina</h1>\n" +
     				"  [[:nombre:]], tu recibo de nomina te ha sido enviado por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.\n" +
     				"  </body>\n" +
     				"</html>";
     		f = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/smtp/NOM-FORSETI.msg");
     		fw = new FileWriter(f);
     		fw.write(msg);
     		fw.close();
     		msg = 
     				"Remision (traslado) XML y PDF enviado por forseti\n" +
     				"text/html\n" +
     				"<html>\n" +
     				"  <head>\n" +
     				"    <title>Remision (traslado)</title>\n" +
     				"  </head>\n" +
     				"  <body>\n" +
     				"  <h1>Remision (traslado)</h1>\n" +
     				"  Una Remision (traslado) a nombre de [[:nombre:]] te ha sido enviada por medio del servicio forseti ( <a href=\"http://www.forseti.org.mx\">http://www.forseti.org.mx</a> ). Encuentra los archivos adjuntos XML y PDF y conservalos en un lugar seguro.<br>\n" +
     				"  </body>\n" +
     				"</html>";
     		f = new File("/usr/local/forseti/emp/" + JUtil.p(request.getParameter("nombre")) + "/smtp/REM-FORSETI.msg");
     		fw = new FileWriter(f);
     		fw.write(msg);
     		fw.close();
  	         
     		mensaje.append(JUtil.Msj("SAF","ADMIN_BD", "DLG", "MSJ-PROCOK"));
     		res = true;
     		//////////////////////////////////////////
  	            
     	}
     	catch(Throwable e)
     	{
     		e.printStackTrace(pw);
     		mensaje.append(JUtil.Msj("SAF","GLB", "GLB", "SAF-MSJ", 2) + "<br>" + e.getMessage());
     	}
     	finally
     	{
     		try { s.close(); } catch (Exception e) { }
     		try { conn.close(); } catch (Exception e) { }
     	}
  			
     	
     	pw.println("----------------------------- FIN DE LA CREACION ----------------------------------");
     	pw.flush();
     	pw.close();
  			
     	JUtil.RDP("REF","FORSETI_ADMIN",(res ? "OK" : "ER"),"ref-su:" + request.getRemoteAddr(),"ADMIN_BD_CREAR","ADBD|FSIBD_" + JUtil.p(request.getParameter("nombre")) + "|||",mensaje.toString());
     	return res;
    }
	
	public boolean puedeAgregar(HttpServletRequest request, HttpServletResponse response, StringBuffer mensaje)
	  	      throws ServletException, IOException
	{
		JBDRegistradasSet set = new JBDRegistradasSet(null);
		set.m_Where = "Nombre ~~* 'FSIBD_" + JUtil.p(request.getParameter("nombre")) + "'";
		set.ConCat(true);
		set.Open();
	
		if(set.getNumRows() > 0)
		{
			mensaje.append(JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 2));
			return false;  
		}
		else
		{
			if(JUtil.getFsiTareas().getActualizando())
	        {
				mensaje.append(JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR",5)); //"PRECAUCION: El proceso de agregado no se puede llevar a cabo porque se esta actualizando el servidor actualmente";
				return false;
	        }
			else
				return true;
		}
		
	}

	private void ingresarRegistroExitoso(String servidor, String basedatos)
	{
		Calendar cal = GregorianCalendar.getInstance();
	    String SQL = "INSERT INTO TBL_SRV_REGISTROS_EXITOSOS\n";
	    	  SQL += "VALUES(default,'" + JUtil.obtFechaHoraSQL(cal) + "','" + JUtil.q(servidor) + "','" + 
	    			  JUtil.q(("FSIBD_" + basedatos)) + "')";
	    try
	    {
	    	Connection con = JAccesoBD.getConexion();
	    	Statement s    = con.createStatement();
	    	s.executeUpdate(SQL);
	    	s.close();
	    	JAccesoBD.liberarConexion(con);
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}
}
