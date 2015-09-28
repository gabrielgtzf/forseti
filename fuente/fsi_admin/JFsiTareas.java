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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import forseti.JAccesoBD;
import forseti.JBDRegistradasSet;
import forseti.JFsiScript;
import forseti.JUtil;
import forseti.JZipUnZipUtil;
import forseti.sets.JAdmVariablesSet;
import forseti.sets.JBDSSet;

public class JFsiTareas implements JTimerInterface
{
	private static boolean actualizando;
	private boolean auto_act;
	private boolean auto_resp;
	private boolean auto_slds;
	private int auto_hora;
	private int auto_min;
	private String respaldos;
	private String postgresql;
	private String actualizar;
	private String tomcat;
	
	JTimer timer; 
	String salida;
	short idsalida;
	String log;
	
	public JFsiTareas()
	{
		actualizando = false;
		timer = new JTimer(this,60000);
		auto_act = false;
		auto_resp = false;
		auto_slds = false;
		auto_hora = 0;
		auto_min = 0;
		respaldos = "NC";
		postgresql = "general";
		actualizar = "NC";
		tomcat = "NC";
		salida  = "";
		log = "";
		idsalida = -1;
		
		// Ahora inicia el gestor de tareas
		try
      	{
			FileReader file         = new FileReader("/usr/local/forseti/bin/.forseti_auto");
			BufferedReader buff     = new BufferedReader(file);
			boolean eof             = false;
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
                		if(key.equals("HORA"))
                		{
                			String[] temp = value.split(":");
                			auto_hora = Integer.parseInt(temp[0]);
                			auto_min = Integer.parseInt(temp[1]);
                		}
                		else if(key.equals("ACT"))
                			auto_act = (value.equals("1") ? true : false);
                		else if(key.equals("RESP"))
                			auto_resp = (value.equals("1") ? true : false);
                		else if(key.equals("SLDS"))
                			auto_slds = (value.equals("1") ? true : false);
                		else if(key.equals("POSTGRESQL"))
                			postgresql = value;
                		else if(key.equals("RESPALDOS"))
                			respaldos = value;
                		else if(key.equals("ACTUALIZAR"))
                			actualizar = value;
                		else if(key.equals("TOMCAT"))
                			tomcat = value;
                	}
                	catch(NoSuchElementException e)
                	{
                		continue;
                	}
					
				}
				
			}
	        
      	}
		catch(Throwable e)
      	{
      		e.printStackTrace(System.out);
      	}
		
		timer.startTimer();
		System.out.println("El sistema de tareas se ha iniciado a las " + auto_hora + ":" + auto_min + "\n" + 
				"PGSQL: " + postgresql + " RUTA: " + respaldos + " ACT = " + auto_act + " RESP = " + auto_resp + " SLDS = " + auto_slds);
		
	}

	public void verificarPorTareas()
	{
		// esta se llama cada minuto para verificar por la actualizacion de saldos
	    Calendar fecha = GregorianCalendar.getInstance();
	    if(auto_hora == fecha.get(Calendar.HOUR_OF_DAY) && auto_min == fecha.get(Calendar.MINUTE))
		   tareasAutomatizadas();
			
	}
	
	public boolean getActualizando()
	{
		return actualizando;
	}
	
	public void tareasAutomatizadas()
	{
		if(!actualizando);
		{
			actualizando = true;
			System.out.println("OK llamada a Tareas Automatizadas");
			if(auto_slds)
				actualizarSaldos();
			if(auto_resp)
				respaldarServidor(Calendar.getInstance(), new StringBuffer());
			if(auto_act)
				actualizarServidor(new StringBuffer());
			
			actualizando = false;
		}
	}
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized void actualizarSaldos()
	{
		//		 Primero recopila la informacion de las bases de datos.
		Vector BASES = new Vector();
		
	    JBDRegistradasSet bdr = new JBDRegistradasSet(null);
  	    bdr.ConCat(true);
  	    bdr.Open();
	
  	    for(int i = 0; i < bdr.getNumRows(); i++)
  	    {
  	    	String nombre = bdr.getAbsRow(i).getNombre();
	    	BASES.addElement(nombre);

  	    }
		short res = 0;	String mensaje = "";
		Connection con;
	
		try
		{
			Calendar fecha = GregorianCalendar.getInstance();
			FileWriter filewri		= new FileWriter("/usr/local/forseti/log/SLDS-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-hh-mm") + ".log", true);
			PrintWriter pw			= new PrintWriter(filewri);
	
			for(int i = 0; i < BASES.size(); i++)
			{
				String nombre = (String)BASES.get(i);
					
			    pw.println("----------------------------------------------------------------------------");
			    pw.println("             " + "ACTUALIZANDO LA BASE DE DATOS: " + nombre + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			    pw.println("----------------------------------------------------------------------------");
			    pw.flush();
			
				System.out.println("ACTUALIZANDO LA BASE DE DATOS: " + nombre + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			    
				//Empieza por la de saldos
				con = JAccesoBD.getConexion(nombre);
				String sql[] = { 
						"select * from sp_invserv_actualizar_existencias() as ( err integer, res varchar ) ",
				    	"select * from sp_prod_formulas_actualizar_niveles() as ( err integer, res varchar ) ",
				    	"select * from sp_invserv_actualizar_sdos() as ( err integer, res varchar ) ",
				    	"select * from sp_invserv_actualizar_polcant() as ( err integer, res varchar ) ",
				    	"select * from sp_cont_catalogo_actualizar_sdos() as ( err integer, res varchar ) ",
				    	"select * from sp_bancos_actualizar_sdos() as ( err integer, res varchar ) ",
				    	"select * from sp_provee_actualizar_sdos() as ( err integer, res varchar ) ",
				    	"select * from sp_client_actualizar_sdos() as ( err integer, res varchar ) "	
				};
				
				try
				{
			       Statement s    = con.createStatement();
			       for(int j = 0; j < sql.length; j++)
				   {
			    	   System.out.println("EJECUTANDO: " + sql[j]);
			    	   pw.println("EJECUTANDO: " + sql[j] + "\n");
			    	   pw.flush();

			    	   ResultSet rs   = s.executeQuery(sql[j]);
			    	   if(rs.next())
			    	   {
			    		   res = rs.getShort("ERR");
			    		   mensaje = rs.getString("RES");
			    	   }
			    	   
			    	   //outprintln("REGRESO: ERR: " + res + " RES: " + mensaje + " " + JUtil.obtFechaTxt(new Date(), "hh:mm:ss"));
			    	   pw.println("REGRESO: ERR: " + res + " RES: " + mensaje + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			    	   pw.flush();

				   }
			       s.close();
			       			      
			    }
			    catch(SQLException e)
			    {
			       System.out.println(e.toString());
			       pw.println(e.toString() + "\n");
			       pw.flush();
			    }
			    finally
			    {
			    	JAccesoBD.liberarConexion(con);
			    }
	
			} // Fin for BA>SES
			
			pw.println("----------------------------- FIN DE LA ACTUALIZACION ----------------------------------");
			pw.flush();
			
			idsalida = 0;
		}
		catch(IOException e)
		{
			idsalida = 3;
			salida += "OCURRIERON ERRORES AL ABRIR O COPIAR ARCHIVOS... REVISA EL REGISTRO DE ACTUALIZACIONES<br>";
			e.printStackTrace();
		}

	}
	
	public synchronized short respaldarEmpresa(JBDSSet set, int ind, Calendar fecha, StringBuffer sb_mensaje, PrintWriter prntwri)
	{
		short idmensaje = 0;
				
		try
		{
			//Primero respalda emp/NOMBRE/
			String nombre = set.getAbsRow(ind).getNombre();
			String path = "/usr/local/forseti/log/RESP-" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
			PrintWriter pw;
			if(prntwri == null)
				pw = new PrintWriter(new FileWriter(path, true));
			else
				pw = prntwri;
			
			pw.println("----------------------------------------------------------------------------");
			pw.println("             " + "RESPALDANDO LA BASE DE DATOS Y ARCHIVOS EMP: " + nombre.substring(6) + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.println("----------------------------------------------------------------------------");
			pw.flush();
			
			if(respaldos.equals("NC"))
			{
				sb_mensaje.append("PRECAUCION: La variable RESPALDOS (ruta para los archivos de respaldo) no está definida... No se puede generar");
				pw.println("PRECAUCION: La variable RESPALDOS (ruta para los archivos de respaldo) no está definida... No se puede generar");
				pw.flush();
				return 1;
			}
			
			JFsiScript sc = new JFsiScript();
			sc.setVerbose(true);
			
			String ERROR = "";
			
			try 
			{
				File dir;
				//System.out.println(dir.getAbsolutePath());
				String CONTENT;
				if(prntwri == null)
				{
					dir = new File(respaldos, (nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm")));
					dir.mkdir();
					CONTENT = "rsync -av --stats /usr/local/forseti/emp/" + nombre.substring(6) + " " + respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm");
				}
				else
				{
					dir = new File(respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"), (nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm")));
					dir.mkdir();
					CONTENT = "rsync -av --stats /usr/local/forseti/emp/" + nombre.substring(6) + " " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm");
				}
				sc.setContent(CONTENT);
				System.out.println(CONTENT);
				String RES = sc.executeCommand();
				ERROR += sc.getError();
				if(!ERROR.equals(""))
				{
					//System.out.println(ERROR);
					idmensaje = 3; 
					sb_mensaje.append("ERROR al respaldar en RSYNC: " + ERROR);
					pw.println("ERROR al respaldar en RSYNC: " + ERROR);
					pw.flush();
				}
				else
				{
					sb_mensaje.append("El respaldo de los archivos se genero con exito en: " + respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
					pw.println("El respaldo de los archivos se genero con éxito en: " + respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
					pw.flush();
				}
		
				pw.println("FINALIZANDO RESPALDO DE ARCHIVOS EMP: " + nombre.substring(6) + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
		
				ERROR = "";
				if(prntwri == null)
					CONTENT = "PGUSER=forseti PGPASSWORD=" + JUtil.getPASS() + " pg_dump --host=" + JUtil.getADDR() + " --port=" + JUtil.getPORT() + " --file=" + respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".dump --no-owner " + nombre; 
				else
					CONTENT = "PGUSER=forseti PGPASSWORD=" + JUtil.getPASS() + " pg_dump --host=" + JUtil.getADDR() + " --port=" + JUtil.getPORT() + " --file=" + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".dump --no-owner " + nombre; 
				
				sc.setContent(CONTENT);
				//System.out.println(CONTENT);
				RES = sc.executeCommand();
				ERROR += sc.getError();
				if(!ERROR.equals(""))
				{
					//System.out.println(ERROR);
					idmensaje = 3; 
					sb_mensaje.append("<br>ERROR al crear el respaldo de la base de datos: " + ERROR);
					pw.println("ERROR al crear el respaldo de la base de datos: " + ERROR);
					pw.flush();
				}
				else
				{
					if(RES.equals(""))
					{
						sb_mensaje.append("<br>El respaldo de la base de datos se genero con exito como archivo .dump dentro de este directorio");
						pw.println("El respaldo de la base de datos se genero con exito como archivo .dump dentro de este directorio");
						pw.flush();
					}
					else
					{
						idmensaje = 3;
						sb_mensaje.append("<br>RESPUESTA PG_DUMP: " + RES);
						pw.println("RESPUESTA PG_DUMP: " + RES);
						pw.flush();
					}
				}
				pw.println("FINALIZANDO RESPALDO DE LA BASE DE DATOS: " + nombre + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
				
				File file;
				if(prntwri == null)
					file = new File(respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".conf");
				else
					file = new File(respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".conf");
				
				String conf =
						  "id_bd|" + set.getAbsRow(ind).getID_BD() + "\n" +
						  "nombre|" + set.getAbsRow(ind).getNombre() + "\n" +
						  "usuario|" + set.getAbsRow(ind).getUsuario() + "\n" +
						  "password|" + set.getAbsRow(ind).getPassword() + "\n" +
						  "fechaalta|" + JUtil.obtFechaSQL(set.getAbsRow(ind).getFechaAlta()) + "\n" +
						  "compania|" + set.getAbsRow(ind).getCompania() + "\n" +
						  "direccion|" + set.getAbsRow(ind).getDireccion() + "\n" +
						  "poblacion|" + set.getAbsRow(ind).getPoblacion() + "\n" +
						  "cp|" + set.getAbsRow(ind).getCP() + "\n" +
						  "mail|" + set.getAbsRow(ind).getMail() + "\n" +
						  "web|" + set.getAbsRow(ind).getWeb() + "\n" +
						  "su|" + set.getAbsRow(ind).getSU() + "\n" +
						  "rfc|" + set.getAbsRow(ind).getRFC() + "\n" +
						  "cfd|" + set.getAbsRow(ind).getCFD() + "\n" +
						  "cfd_calle|" + set.getAbsRow(ind).getCFD_Calle() + "\n" +
						  "cfd_noext|" + set.getAbsRow(ind).getCFD_NoExt() + "\n" +
						  "cfd_noint|" + set.getAbsRow(ind).getCFD_NoInt() + "\n" +
						  "cfd_colonia|" + set.getAbsRow(ind).getCFD_Colonia() + "\n" +
						  "cfd_localidad|" + set.getAbsRow(ind).getCFD_Localidad() + "\n" +
						  "cfd_municipio|" + set.getAbsRow(ind).getCFD_Municipio() + "\n" +
						  "cfd_estado|" + set.getAbsRow(ind).getCFD_Estado() + "\n" +
						  "cfd_pais|" + set.getAbsRow(ind).getCFD_Pais() + "\n" +
						  "cfd_cp|" + set.getAbsRow(ind).getCFD_CP() + "\n" +
						  "cfd_regimenfiscal|" + set.getAbsRow(ind).getCFD_RegimenFiscal();
	            FileWriter fconf = new FileWriter(file);
	            fconf.write(conf);
	            fconf.close();
	            sb_mensaje.append("<br>El respaldo de la configuracion se genero con exito como archivo .conf en este directorio");
				pw.println("El respaldo de la configuracion se genero con exito como archivo .conf en este directorio");
				pw.flush();
				if(prntwri == null)
				{
					pw.println("Generando el archivo zip...");
					pw.flush();
					JZipUnZipUtil azip= new JZipUnZipUtil();
					azip.zipFolder(respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"), respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".zip");
					pw.println("Eliminando carpeta de respaldo...");
					pw.flush();
					//Borra los archivos del respaldo
					File dirbd = new File(respaldos + "/" + nombre + "-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
					File[] currList;
					Stack<File> stack = new Stack<File>();
					stack.push(dirbd);
					while (!stack.isEmpty()) 
					{
						if (stack.lastElement().isDirectory()) 
						{
							currList = stack.lastElement().listFiles();
							if (currList.length > 0) 
							{
								for (File curr: currList) 
								{
									stack.push(curr);
								}
							} 
							else 
							{
								stack.pop().delete();
							}
						} 
						else 
						{
							stack.pop().delete();
						}
					}
				}
			}
	      	catch(Throwable e)
	      	{
	      		idmensaje = 3;
	      		sb_mensaje.append("<br>ERROR Throwable: " + e.getMessage());
	      		System.out.println(e.toString());
			    pw.println(e.toString() + "\n");
			    pw.flush();
	      	}
	      	
			pw.println("----------------------------- FIN DEL RESPALDO: " + nombre + " ----------------------------------");
			pw.flush();
			if(prntwri == null)
				pw.close();			
		}
		catch(IOException e)
		{
			idmensaje = 3;
			sb_mensaje.append("OCURRIERON ERRORES AL ABRIR O COPIAR ARCHIVOS<br>" + e.getMessage());
			//e.printStackTrace();
		}
		
		return idmensaje;
	}
	
	public synchronized short respaldarServidor(Calendar fecha, StringBuffer sb_mensaje)
	{
		short idmensaje = 0;
			
		try
		{
			String path = "/usr/local/forseti/log/RESP-FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log";
			FileWriter filewri		= new FileWriter(path, true);
			PrintWriter pw			= new PrintWriter(filewri);
	
			pw.println("----------------------------------------------------------------------------");
			pw.println("             RESPALDANDO LA BASE DE DATOS PRINCIPAL: FORSETI_ADMIN Y ARCHIVOS " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			pw.println("----------------------------------------------------------------------------");
			pw.flush();
			 
			if(respaldos.equals("NC"))
			{
				sb_mensaje.append("PRECAUCION: La variable RESPALDOS (ruta para los archivos de respaldo) no está definida... No se puede generar");
				pw.println("PRECAUCION: La variable RESPALDOS (ruta para los archivos de respaldo) no está definida... No se puede generar");
				pw.flush();
				return 1;
			}
				
			if(tomcat.equals("NC"))
			{
				sb_mensaje.append("PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede generar");
				pw.println("PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede generar");
				pw.flush();
				return 1;
			}
			
			JFsiScript sc = new JFsiScript();
			sc.setVerbose(true);
			
			String ERROR = "", RES = "";
			
			try 
			{
				File dir = new File(respaldos, "FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
				dir.mkdir();
				File diremp = new File(respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"), "emp");
				diremp.mkdir();
				
				//System.out.println(dir.getAbsolutePath());
				String CONTENT = "";
				CONTENT += "rsync -av --stats /usr/local/forseti/act " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				CONTENT += "rsync -av --stats /usr/local/forseti/bin " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				CONTENT += "rsync -av --stats /usr/local/forseti/log " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				CONTENT += "rsync -av --stats /usr/local/forseti/pac " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				CONTENT += "rsync -av --stats /usr/local/forseti/rec " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				CONTENT += "rsync -av --stats " + tomcat + "/webapps/ROOT.war " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "\n";
				
				sc.setContent(CONTENT);
				System.out.println(CONTENT);
				RES = sc.executeCommand();
				ERROR += sc.getError();
				if(!ERROR.equals(""))
				{
					//System.out.println(ERROR);
					idmensaje = 3; 
					sb_mensaje.append("ERROR al respaldar en RSYNC: " + ERROR + "<br>");
					pw.println("ERROR al respaldar en RSYNC: " + ERROR);
					pw.flush();
				}
				else
				{
					sb_mensaje.append("El respaldo de los archivos se genero con exito en: " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "<br>");
					pw.println("El respaldo de los archivos se genero con éxito en: " + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
					pw.flush();
				}
		
				pw.println("FINALIZANDO RESPALDO DE ARCHIVOS FORSETI_ADMIN: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
		
				RES = ""; ERROR = "";
				CONTENT = "PGUSER=forseti PGPASSWORD=" + JUtil.getPASS() + " pg_dump --host=" + JUtil.getADDR() + " --port=" + JUtil.getPORT() + " --file=" + respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".dump FORSETI_ADMIN"; 
				sc.setContent(CONTENT);
				//System.out.println(CONTENT);
				RES = sc.executeCommand();
				ERROR += sc.getError();
				//Ahora genera el Archivo Base64
				if(!ERROR.equals(""))
				{
					//System.out.println(ERROR);
					idmensaje = 3; 
					sb_mensaje.append("ERROR al crear el respaldo de la base de datos principal: " + ERROR + "<br>");
					pw.println("ERROR al crear el respaldo de la base de datos principal: " + ERROR);
					pw.flush();
				}
				else
				{
					if(RES.equals(""))
					{
						sb_mensaje.append("El respaldo de la base de datos principal se genero con exito como archivo .dump dentro de este directurio<br>");
						pw.println("El respaldo de la base de datos principal se genero con exito como archivo .dump dentro de este directurio");
						pw.flush();
					}
					else
					{
						idmensaje = 3;
						sb_mensaje.append("<br>RESPUESTA PG_DUMP: " + RES);
						pw.println("RESPUESTA PG_DUMP: " + RES);
						pw.flush();
					}
				}
		    	//////////////////////////////////////////
				pw.println("FINALIZANDO RESPALDO DE LA BASE DE DATOS: FORSETI_ADMIN " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.flush();
	      	}
	      	catch(Throwable e)
	      	{
	      		idmensaje = 3;
	      		sb_mensaje.append("ERROR Throwable: " + "<br>" + e.getMessage());
	      		System.out.println(e.toString());
			    pw.println(e.toString() + "\n");
			    pw.flush();
	      	}
	      	
			JBDSSet set = new JBDSSet(null);
	    	set.ConCat(true);
	    	set.m_OrderBy = "ID_BD ASC";
	    	set.Open();
	    	
	    	for(int i = 0; i < set.getNumRows(); i++)
	    	{
	    		if(!set.getAbsRow(i).getSU().equals("3")) // La base de datos esta corrupta, se debe eliminar
	    			continue;
	    		else
	    			respaldarEmpresa(set, i, fecha, sb_mensaje, pw);
	    	}
	    	pw.println("Generando el archivo zip...");
			pw.flush();
			JZipUnZipUtil azip= new JZipUnZipUtil();
			azip.zipFolder(respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"), respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".zip");
			pw.println("Eliminando carpeta de respaldo...");
			pw.flush();
			//Borra los archivos del respaldo
			File dirbd = new File(respaldos + "/FORSETI_ADMIN-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm"));
			File[] currList;
			Stack<File> stack = new Stack<File>();
			stack.push(dirbd);
			while (!stack.isEmpty()) 
			{
				if (stack.lastElement().isDirectory()) 
				{
					currList = stack.lastElement().listFiles();
					if (currList.length > 0) 
					{
						for (File curr: currList) 
						{
							stack.push(curr);
						}
					} 
					else 
					{
						stack.pop().delete();
					}
				} 
				else 
				{
					stack.pop().delete();
				}
			}
	    	pw.println("----------------------------- FIN DEL RESPALDO ----------------------------------");
			pw.flush();
			pw.close();
						
		}
		catch(IOException e)
		{
			idmensaje = 3;
			sb_mensaje.append("OCURRIERON ERRORES DE IOException<br>" + e.getMessage());
			//e.printStackTrace();
		}
		catch(Exception e)
		{
			idmensaje = 3;
			sb_mensaje.append("OCURRIERON ERRORES DE Exception<br>" + e.getMessage());
			//e.printStackTrace();
		}
		return idmensaje;
	}
	
	public synchronized short actualizarServidor(StringBuffer sb_mensaje)
	{
		short idmensaje = 0;
		float version = -1F; // la version actual
	   	int revision = 0;
	   	float versiondisp = -1F;
	   	int revisiondisp = 0;
	   	
	   	//String sversion = "";
	   	String dir_act = "/usr/local/forseti/act";

		JAdmVariablesSet set = new JAdmVariablesSet(null);
		set.ConCat(true);
		set.m_Where = "ID_Variable = 'VERSION'";
		set.Open();
		
		version = set.getAbsRow(0).getVDecimal();
		revision = set.getAbsRow(0).getVEntero();
		
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		String CONTENT;
		
		Calendar fecha = GregorianCalendar.getInstance();
		try
		{
			FileWriter filewri		= new FileWriter("/usr/local/forseti/log/ACT-" + JUtil.obtFechaTxt(fecha, "yyyy-MM-dd-HH-mm") + ".log", true);
			PrintWriter pw = new PrintWriter(filewri);
	    	try
			{
				pw.println("----------------------------------------------------------------------------");
			    pw.println("             ACTUALIZACION DEL SERVIDOR: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
			    pw.println("----------------------------------------------------------------------------");
			    
			    if(actualizar.equals("NC"))
				{
					sb_mensaje.append("PRECAUCION: La variable ACTUALIZAR (url de descarga de actualizaciones) no está definida... No se puede actualizar");
					pw.println("PRECAUCION: La variable ACTUALIZAR (url de descarga de actualizaciones) no está definida... No se puede actualizar");
					pw.flush();
					return 1;
				}
			    
			    pw.println("Obteniendo indice de actualizacion desde: " + actualizar);
				pw.flush();
				
				CONTENT = "wget -O " + dir_act + "/indice.si " + actualizar + "/indice.si";
				sc.setContent(CONTENT);
				//System.out.println(CONTENT);
				sc.executeCommand();
				pw.println(sc.getError());
				pw.println("FINALIZANDO DESCARGA DEL INDICE: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
				pw.println("------------------------------------------------------------------------------");
				pw.flush();
					
				FileReader file         = new FileReader( dir_act + "/indice.si");
				BufferedReader buff     = new BufferedReader(file);
				boolean eof            = false;
				while(!eof)
				{
					String line = buff.readLine();
					if(line == null)
						eof = true;
					else
					{
						try
						{
							boolean descargar = false;
							StringTokenizer st = new StringTokenizer(line,"|");
							String key         = st.nextToken();
							String value       = st.nextToken();
							try { versiondisp = Float.parseFloat(key); } catch(NumberFormatException e) { versiondisp = -2F; };
							try { revisiondisp = Integer.parseInt(value); } catch(NumberFormatException e) { revisiondisp = -2; };
								
							if(version > versiondisp)
								continue;
							else // Nuevas versiones o revisiones disponibles
							{
								if(version == versiondisp) //la misma version, checa la revision
								{
									if(revision >= revisiondisp)
										continue;
									else //revision menor a la disponible
									{
										File status = new File(dir_act + "/act-" + versiondisp + "." + revisiondisp + "/status_log");
										if(!status.exists())
										{
											pw.println("Descarga de revision disponible: " + versiondisp + "." + revisiondisp + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
											pw.flush();
											descargar = true;
										}
									}
								}
								else //version menor a las disponibles
								{
									File status = new File(dir_act + "/act-" + versiondisp + "." + revisiondisp + "/status_log");
									if(!status.exists())
									{
										pw.println("Descarga de version disponible: " + versiondisp + "." + revisiondisp + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
										pw.flush();
										descargar = true;
									}
								}
							}
							//Si se debe descargar.... lo hace
							if(descargar)
							{
								CONTENT = "wget -O " + dir_act + "/act-" + versiondisp + "." + revisiondisp + ".zip " + actualizar + "/act-" + versiondisp + "." + revisiondisp + ".zip";
								sc.setContent(CONTENT);
								//System.out.println(CONTENT);
								sc.executeCommand();
								pw.println(sc.getError());
								pw.println("Desempaquetando......");
								pw.flush();
								desempaquetarActualizacion(dir_act + "/act-" + versiondisp + "." + revisiondisp + ".zip", dir_act + "/act-" + versiondisp + "." + revisiondisp + "/", pw);
							}
						}
						catch(NoSuchElementException e)
						{
							continue;
						}
							
					}
				}
				buff.close();
				file.close();
				buff = null;
				file = null;
				//Ahora ya tiene las actualizaciones descargadas, y desempaquetadas procede a instalarlas
				//System.out.println("1");
				file = new FileReader( dir_act + "/indice.si");
				buff = new BufferedReader(file);
				eof = false;
				while(!eof)
				{
					String line = buff.readLine();
					if(line == null)
						eof = true;
					else
					{
						try
						{
							boolean bActualizar = false;
							StringTokenizer st 	= new StringTokenizer(line,"|");
							String key         = st.nextToken();
							String value       = st.nextToken();
							try { versiondisp = Float.parseFloat(key); } catch(NumberFormatException e) { versiondisp = -2F; };
							try { revisiondisp = Integer.parseInt(value); } catch(NumberFormatException e) { revisiondisp = -2; };
							System.out.println("ACTUALIZACION DISP: " + versiondisp + "." + revisiondisp + " " + version + "." + revision);	
							if(version > versiondisp)
								continue;
							else // Nuevas versiones o revisiones disponibles
							{
								if(version == versiondisp) //la misma version, checa la revision
								{
									if(revision >= revisiondisp)
										continue;
									else //revision menor a la disponible
									{
										System.out.println("OK procede...");
										File status = new File(dir_act + "/act-" + versiondisp + "." + revisiondisp + "/status_log");
										if(status.exists())
										{
											pw.println("Actualizacion de revision: " + versiondisp + "." + revisiondisp + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
											pw.flush();
											bActualizar = true;
										}
									}
								}
								else //version menor a las disponibles
								{
									File status = new File(dir_act + "/act-" + versiondisp + "." + revisiondisp + "/status_log");
									if(status.exists())
									{
										pw.println("Actualizacion de version: " + versiondisp + "." + revisiondisp + " " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
										pw.flush();
										bActualizar = true;
									}
								}
							}
							//Si se debe actualizar.... lo hace
							if(bActualizar)
							{
								CONTENT = "wget -O " + dir_act + "/act-" + versiondisp + "." + revisiondisp + ".zip " + actualizar + "/act-" + versiondisp + "." + revisiondisp + ".zip";
								instalarActualizacion(dir_act + "/act-" + versiondisp + "." + revisiondisp + "/", versiondisp, revisiondisp, version, revision, pw);
							}
						}
						catch(NoSuchElementException e)
						{
							continue;
						}
							
					}
				}
				buff.close();
				file.close();
				//System.out.println("2");
				sb_mensaje.append("Se han descargado y actualizado el servidor<br>");
				sb_mensaje.append("Probablemente se necesitará reiniciar este servidor si tomcat no esta configurado con autodeploy....");
				pw.println("Se han descargado y actualizado el servidor");
				pw.println("Probablemente se necesitará reiniciar este servidor si tomcat no esta configurado con autodeploy....");
				pw.flush();
								
			}
	    	catch(IOException e)
			{
				idmensaje = 3;
				sb_mensaje.append("ERROR de IOException<br>" + e.getMessage());
				pw.println("ERROR de IOException: " + e.toString());
				pw.flush();
			}
		    catch (Exception e1) 
			{
		    	idmensaje = 3;
		    	sb_mensaje.append("ERROR de Exception:<br>" + e1.toString());
		    	pw.println("ERROR de Exception: " + e1.toString());
				pw.flush();
			}
	    	pw.println("----------------------------- FIN DE LA ACTUALIZACION ----------------------------------");
			pw.flush();
			pw.close();
		}
		catch(IOException e)
		{
			idmensaje = 3;
			sb_mensaje.append("OCURRIERON ERRORES AL ABRIR O COPIAR ARCHIVOS<br>" + e.getMessage());
		}
		
		return idmensaje;
	}

	private void instalarActualizacion(String folderact, float versiondisp, int revisiondisp, float version, int revision, PrintWriter pw) 
		throws Exception 
	{
		pw.println("COMENZANDO LA ACTUALIZACION DE BASES DE DATOS (V4010) " + versiondisp + "." + revisiondisp + ": " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
		pw.println("------------------------------------------------------------------------------");
	    pw.flush();
		
		FileReader file         = new FileReader(folderact + "status_log");
		BufferedReader buff     = new BufferedReader(file);
		boolean eof            = false;
		Map<String, String> map = new HashMap<String, String>();
		Vector<String> actbd_sql = new Vector<String>();
		Vector<String> actfsi_sql = new Vector<String>();
		while(!eof)
		{
			String line = buff.readLine();
			if(line == null)
				eof = true;
			else
			{
				try
				{
					StringTokenizer st = new StringTokenizer(line,"=");
					String key         = st.nextToken();
					String value       = st.nextToken();
					map.put(key, value);
				}
				catch(NoSuchElementException e)
				{
					continue;
				}
			}
		}
		buff.close();
		file.close();
		
		//ya tenemos el status_log en memoria, ahora lo utilizamos como base para la actualizacion
		JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		String ERROR = "";
		String dirfsi = "/usr/local/forseti";
		if(map.get("STATUS").equals("ACTBD")) // significa que no ha hecho nada, o que no se completo la actulizacion anterior
		{
			JBDSSet set = new JBDSSet(null);
			set.ConCat(true);
			set.m_Where = "ID_BD >= " + map.get("BD") + " and SU = '3'"; 
			set.m_OrderBy = "ID_BD ASC";
			set.Open();
			
			for(int i = 0; i < set.getNumRows(); i++)
			{
				//verifica que la base de datos a actualizar, no sea mas antigua que las actuales en este servidor
				//Esto pudiera suceder porque se acaba de restaurar una base de datos que habia sido respaldada antes
				//de la ultima actualizacion del servidor
				JAdmVariablesSet var = new JAdmVariablesSet(null);
				var.setBD(set.getAbsRow(i).getNombre());
				var.ConCat(3);
				var.m_Where = "ID_Variable = 'VERSION'";
				var.Open();
				//pw.println("SQL: " + set.getAbsRow(i).getNombre() + " " + var.getSQL());
				//pw.println("TOTAL: " + var.getNumRows());
				float varversion = var.getAbsRow(0).getVDecimal();
	            int varrevision = var.getAbsRow(0).getVEntero();
	            if(varversion < version || 
						(varversion == version && varrevision < revision ))
				{
					pw.println("LA BASE DE DATOS ESTA DESFASADA EN VERSION O REVISION Y NO SE PUEDE ACTUALIZAR: " + set.getAbsRow(i).getNombre() + ":" + varversion + "." + varrevision + " ---> " + versiondisp + "." + revisiondisp);
					pw.println("ESTO PUEDE SER DEBIDO A QUE SE HA RESTAURADO UNA BASE DE DATOS QUE HABIA SIDO");
					pw.println("RESPALDADA ANTES DE LA ULTIMA ACTUALIZACION DEL SERVIDOR... INTENTA POR ACTUALIZACION DE EMPRESAS DESFASADAS");
				    pw.println("-------------------------------------------------------------------------------------------------------------------");
				    pw.flush();
					continue;
				}
	            
	            pw.println("Actualizando BD :" + set.getAbsRow(i).getNombre());
			    pw.flush();
				
				if(map.get("PUNTO").equals("NC")) // nada actalizado a esta base de datos, comienza por los archivos
				{
					File dir = new File(folderact + "emp");
					if (dir.exists()) 
					{
						pw.println("Grabando los archivos del sistema para BD: " + set.getAbsRow(i).getNombre());
						pw.flush();
						String CONTENT = "rsync -av --stats " + folderact + "emp/ " + dirfsi + "/emp/" + set.getAbsRow(i).getNombre().substring(6);
						sc.setContent(CONTENT);
						pw.println(CONTENT);
						String RES = sc.executeCommand();
						ERROR += sc.getError();
						if(!ERROR.equals(""))
						{
							//System.out.println(ERROR);
							pw.println(ERROR);
							pw.flush();
							return;
						}
						else
						{
							pw.println(RES);
							pw.flush();
						}	
					}
					map.put("PUNTO", "ARCHIVOS");
					File f = new File(folderact + "status_log");
					FileWriter fsl = new FileWriter(f);
					fsl.write("STATUS=ACTBD\nBD=" + set.getAbsRow(i).getID_BD() + "\nPUNTO=ARCHIVOS");
					fsl.close();
					
					pw.println("-----------------------------------------------------------------------------");
					pw.flush();
				}
				
				if(map.get("PUNTO").equals("ARCHIVOS")) // archivos actualizados, ahora la estructura de la bd
				{
					File sql = new File(folderact + "actbd.sql");
					if(sql.exists()) 
					{
						//////////////////////////////////////////////////////////////////////////////////////////////////
						if(actbd_sql.size() == 0)
						{
							FileReader filebas     = new FileReader(folderact + "actbd.sql");
							BufferedReader buffbas     = new BufferedReader(filebas);
							boolean eofbas             = false;
							String strbas = "";
							
							while(!eofbas)
							{
								String linebas = buffbas.readLine();
								if(linebas == null)
									eofbas = true;
								else
								{
									if(linebas.indexOf("--@FIN_BLOQUE") == -1)
										strbas += linebas + "\n";
									else
									{
										actbd_sql.addElement(strbas);
										strbas = "";
									}
					            }
							}
					        buffbas.close();
							filebas.close();
						}
						
						pw.println("Executando estructura SQL para BD: " + set.getAbsRow(i).getNombre());
						pw.flush();
						Connection con = JAccesoBD.getConexion(set.getAbsRow(i).getNombre());
						con.setAutoCommit(false);
				        Statement s    = con.createStatement();
						for(int j = 0; j < actbd_sql.size(); j++)
						{
							String actbdblq_sql = JUtil.replace(actbd_sql.get(j), "[[owner]]", set.getAbsRow(i).getUsuario());
							pw.println(actbdblq_sql + "\n");
							pw.flush();
							s.executeUpdate(actbdblq_sql);
						}
						String varbd_sql = 
								"UPDATE TBL_VARIABLES\n" +
								"SET VDecimal = '" + versiondisp + "', VEntero = '" + revisiondisp + "', VAlfanumerico = '" + versiondisp + "." + revisiondisp + "'\n" +
								"WHERE ID_Variable = 'VERSION';";
								//"REASSIGN OWNED BY forseti TO " + set.getAbsRow(i).getUsuario() + ";"; 
								
						pw.println(varbd_sql + "\n");
						pw.flush();
						s.executeUpdate(varbd_sql);
						con.commit();
						s.close();
						con.close();
						////////////////////////////////////////////////////////////////////////////////////////////////
					}
					else
					{
						Connection con = JAccesoBD.getConexion(set.getAbsRow(i).getNombre());
						con.setAutoCommit(false);
				        Statement s    = con.createStatement();
						String varbd_sql = 
								"UPDATE TBL_VARIABLES\n" +
								"SET VDecimal = '" + versiondisp + "', VEntero = '" + revisiondisp + "', VAlfanumerico = '" + versiondisp + "." + revisiondisp + "'\n" +
								"WHERE ID_Variable = 'VERSION';";
						pw.println(varbd_sql + "\n");
						pw.flush();
						s.executeUpdate(varbd_sql);
						con.commit();
						s.close();
						con.close();
					}
					map.put("PUNTO", "ESTRUCTURA");
					File f = new File(folderact + "status_log");
					FileWriter fsl = new FileWriter(f);
					fsl.write("STATUS=ACTBD\nBD=" + set.getAbsRow(i).getID_BD() + "\nPUNTO=ESTRUCTURA");
					fsl.close();
					
				}
				//////////////////////////////////////////////////////////////////////
				if(map.get("PUNTO").equals("ESTRUCTURA")) // Ahora los mensajes de esta bd los actualiza
				{
					File msj = new File(folderact + "bin/.forseti_es");
					if(msj.exists()) 
					{
						FileReader filemsj    = new FileReader(folderact + "bin/.forseti_es");
						BufferedReader buffmsj     = new BufferedReader(filemsj);
						boolean eofmsj             = false;
						
						pw.println("Executando mensajes para " + set.getAbsRow(i).getNombre());
						pw.flush();
						Connection con = JAccesoBD.getConexion(set.getAbsRow(i).getNombre());
						Statement s    = con.createStatement();
						String varmsj_sql = "TRUNCATE TABLE TBL_MSJ;";
						pw.println(varmsj_sql + "\n");
						pw.flush();
						s.executeUpdate(varmsj_sql);
						
						while(!eofmsj)
			            {
			                String line = buffmsj.readLine();
			                if(line == null)
			                	eofmsj = true;
			                else
			                {
			                	if(line.equals("__INIT"))
			                	{
			                		String alc = "", mod = "", sub = "", elm = "", msj1 = "", msj2 = "", msj3 = "", msj4 = "", msj5 = "";
			                		for(int im = 1; im <= 9; im++)
			                		{
			                			line = buffmsj.readLine();
			                			switch(im)
			                			{
			                			case 1: msj1 = "'" + line + "'";
			                			break;
			                			case 2: msj2 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
			                			break;
			                			case 3: msj3 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
			                			break;
			                			case 4: msj4 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
			                			break;
			                			case 5: msj5 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
			                			break;
			                			case 6: alc = "'" + JUtil.p(line) + "'";
			                			break;
			                			case 7: mod = "'" + JUtil.p(line) + "'";
			                			break;
			                			case 8: sub = "'" + JUtil.p(line) + "'";
			                			break;
			                			case 9: elm = "'" + JUtil.p(line) + "'";
			                			break;
			                			}
			                		}
			                		
			                		varmsj_sql = "INSERT INTO tbl_msj\nVALUES(";
		                			varmsj_sql += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ");";
				                	
			    					pw.println(varmsj_sql + "\n");
			    					pw.flush();
			    					s.executeUpdate(varmsj_sql);
			    					
			                	}
			                }
			            }
						
						s.close();
						con.close();
						
						buffmsj.close();
						filemsj.close();
					}
					map.put("PUNTO", "MSJ");
					File f = new File(folderact + "status_log");
					FileWriter fsl = new FileWriter(f);
					fsl.write("STATUS=ACTBD\nBD=" + set.getAbsRow(i).getID_BD() + "\nPUNTO=MSJ");
					fsl.close();
									
				}
				///////////////////////////////////////////////////////////////////////
				map.put("PUNTO", "NC");
			}
			map.put("STATUS", "ACTFSI");
			map.put("BD", "FSI");
			map.put("PUNTO", "NC");
			File f = new File(folderact + "status_log");
			FileWriter fsl = new FileWriter(f);
			fsl.write("STATUS=ACTFSI\nBD=FSI\nPUNTO=NC");
			fsl.close();
		}
		
		if(map.get("STATUS").equals("ACTFSI")) //Significa que las bases de datos ya estan actualizadas, y falta FORSETI_ADMIN
		{
			if(map.get("PUNTO").equals("NC")) // nada actalizado a esta base de datos, comienza por los archivos
			{
				pw.println("--------------------------------- FORSETI_ADMIN -------------------------------------------");
				pw.flush();
				
				File dir = new File(folderact + "act");
				if(dir.exists()) 
				{
					pw.println("Grabando los archivos act...");
					pw.flush();
					String CONTENT = "rsync -av --stats " + folderact + "act/ " + dirfsi + "/act";
					sc.setContent(CONTENT);
					pw.println(CONTENT);
					String RES = sc.executeCommand();
					ERROR = sc.getError();
					if(!ERROR.equals(""))
					{
						//System.out.println(ERROR);
						pw.println(ERROR);
						pw.flush();
						return;
					}
					else
					{
						pw.println(RES);
						pw.flush();
					}	
				}
				dir = new File(folderact + "bin");
				if(dir.exists()) 
				{
					pw.println("Grabando los archivos bin...");
					pw.flush();
					String CONTENT = "rsync -av --stats " + folderact + "bin/ " + dirfsi + "/bin";
					sc.setContent(CONTENT);
					pw.println(CONTENT);
					String RES = sc.executeCommand();
					ERROR = sc.getError();
					if(!ERROR.equals(""))
					{
						//System.out.println(ERROR);
						pw.println(ERROR);
						pw.flush();
						return;
					}
					else
					{
						pw.println(RES);
						pw.flush();
					}	
				}
				dir = new File(folderact + "pac");
				if(dir.exists()) 
				{
					pw.println("Grabando los archivos pac...");
					pw.flush();
					String CONTENT = "rsync -av --stats " + folderact + "pac/ " + dirfsi + "/pac";
					sc.setContent(CONTENT);
					pw.println(CONTENT);
					String RES = sc.executeCommand();
					ERROR = sc.getError();
					if(!ERROR.equals(""))
					{
						//System.out.println(ERROR);
						pw.println(ERROR);
						pw.flush();
						return;
					}
					else
					{
						pw.println(RES);
						pw.flush();
					}	
				}
				dir = new File(folderact + "rec");
				if(dir.exists()) 
				{
					pw.println("Grabando los archivos rec...");
					pw.flush();
					String CONTENT = "rsync -av --stats " + folderact + "rec/ " + dirfsi + "/rec";
					sc.setContent(CONTENT);
					pw.println(CONTENT);
					String RES = sc.executeCommand();
					ERROR = sc.getError();
					if(!ERROR.equals(""))
					{
						//System.out.println(ERROR);
						pw.println(ERROR);
						pw.flush();
						return ;
					}
					else
					{
						pw.println(RES);
						pw.flush();
					}	
				}
				map.put("PUNTO", "ARCHIVOS");
				File f = new File(folderact + "status_log");
				FileWriter fsl = new FileWriter(f);
				fsl.write("STATUS=ACTFSI\nBD=FSI\nPUNTO=ARCHIVOS");
				fsl.close();
			}
			
			if(map.get("PUNTO").equals("ARCHIVOS")) // archivos actualizados, ahora la estructura de la bd
			{
				File sql = new File(folderact + "actfsi.sql");
				if(sql.exists()) 
				{
					//////////////////////////////////////////////////////////////////////////////////////////////////
					FileReader filebas     = new FileReader(folderact + "actfsi.sql");
					BufferedReader buffbas     = new BufferedReader(filebas);
					boolean eofbas             = false;
					String strbas = "";
						
					while(!eofbas)
					{
						String linebas = buffbas.readLine();
						if(linebas == null)
							eofbas = true;
						else
						{
							if(linebas.indexOf("--@FIN_BLOQUE") == -1)
								strbas += linebas + "\n";
							else
							{
								actfsi_sql.addElement(strbas);
								strbas = "";
							}
						}
					}
					buffbas.close();
					filebas.close();
					
					pw.println("Executando estructura SQL para FORSETI_ADMIN");
					pw.flush();
					Connection con = JAccesoBD.getConexion();
					con.setAutoCommit(false);
			        Statement s    = con.createStatement();
					for(int j = 0; j < actfsi_sql.size(); j++)
					{
						String actfsiblq_sql = actfsi_sql.get(j);
						pw.println(actfsiblq_sql + "\n");
						pw.flush();
						s.executeUpdate(actfsiblq_sql);
					}
					String varfsi_sql = 
							"UPDATE TBL_VARIABLES\n" +
							"SET VDecimal = '" + versiondisp + "', VEntero = '" + revisiondisp + "', VAlfanumerico = '" + versiondisp + "." + revisiondisp + "'\n" +
							"WHERE ID_Variable = 'VERSION';";
					pw.println(varfsi_sql + "\n");
					pw.flush();
					s.executeUpdate(varfsi_sql);
					con.commit();
					s.close();
					con.close();
				}
				else
				{
					Connection con = JAccesoBD.getConexion();
					con.setAutoCommit(false);
			        Statement s    = con.createStatement();
					String varfsi_sql = 
							"UPDATE TBL_VARIABLES\n" +
							"SET VDecimal = '" + versiondisp + "', VEntero = '" + revisiondisp + "', VAlfanumerico = '" + versiondisp + "." + revisiondisp + "'\n" +
							"WHERE ID_Variable = 'VERSION';";
					pw.println(varfsi_sql + "\n");
					pw.flush();
					s.executeUpdate(varfsi_sql);
					con.commit();
					s.close();
					con.close();
				}
				map.put("PUNTO", "ESTRUCTURA");
				File f = new File(folderact + "status_log");
				FileWriter fsl = new FileWriter(f);
				fsl.write("STATUS=ACTFSI\nBD=FSI\nPUNTO=ESTRUCTURA");
				fsl.close();
				
			}
			
			if(map.get("PUNTO").equals("ESTRUCTURA")) // archivos actualizados, ahora el archivo ROOT
			{
				File root = new File(folderact + "ROOT.war");
				if(root.exists()) 
				{
					pw.println("Grabando el archivo ROOT para tomcat...");
					pw.flush();
					String CONTENT = "rsync -av --stats " + folderact + "ROOT.war " + tomcat + "/webapps";
					sc.setContent(CONTENT);
					pw.println(CONTENT);
					String RES = sc.executeCommand();
					ERROR = sc.getError();
					if(!ERROR.equals(""))
					{
						//System.out.println(ERROR);
						pw.println(ERROR);
						pw.flush();
						return;
					}
					else
					{
						pw.println(RES);
						pw.flush();
					}
				}
				map.put("PUNTO", "ROOT");
				File f = new File(folderact + "status_log");
				FileWriter fsl = new FileWriter(f);
				fsl.write("STATUS=ACTFSI\nBD=FSI\nPUNTO=ROOT");
				fsl.close();
				//JUtil.setREINICIAR(true);
				
			}
			
			if(map.get("PUNTO").equals("ROOT")) // Ahora los mensajes los actualiza
			{
				File msj = new File(folderact + "bin/.forseti_es");
				if(msj.exists()) 
				{
					FileReader filemsj    = new FileReader(folderact + "bin/.forseti_es");
					BufferedReader buffmsj     = new BufferedReader(filemsj);
					boolean eofmsj             = false;
					
					pw.println("Executando mensajes para FORSETI_ADMIN");
					pw.flush();
					Connection con = JAccesoBD.getConexion();
					Statement s    = con.createStatement();
					String varmsj_sql = "TRUNCATE TABLE TBL_MSJ;";
					pw.println(varmsj_sql + "\n");
					pw.flush();
					s.executeUpdate(varmsj_sql);
					
					while(!eofmsj)
		            {
		                String line = buffmsj.readLine();
		                if(line == null)
		                	eofmsj = true;
		                else
		                {
		                	if(line.equals("__INIT"))
		                	{
		                		String alc = "", mod = "", sub = "", elm = "", msj1 = "", msj2 = "", msj3 = "", msj4 = "", msj5 = "";
		                		for(int i = 1; i <= 9; i++)
		                		{
		                			line = buffmsj.readLine();
		                			switch(i)
		                			{
		                			case 1: msj1 = "'" + line + "'";
		                			break;
		                			case 2: msj2 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
		                			break;
		                			case 3: msj3 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
		                			break;
		                			case 4: msj4 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
		                			break;
		                			case 5: msj5 = (line.equals("null") ? "null" : "'" + JUtil.p(line) + "'");
		                			break;
		                			case 6: alc = "'" + JUtil.p(line) + "'";
		                			break;
		                			case 7: mod = "'" + JUtil.p(line) + "'";
		                			break;
		                			case 8: sub = "'" + JUtil.p(line) + "'";
		                			break;
		                			case 9: elm = "'" + JUtil.p(line) + "'";
		                			break;
		                			}
		                		}
		                		
		                		varmsj_sql = "INSERT INTO tbl_msj\nVALUES(";
	                			varmsj_sql += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ");";
			                	
		    					pw.println(varmsj_sql + "\n");
		    					pw.flush();
		    					s.executeUpdate(varmsj_sql);
		    					
		                	}
		                }
		            }
					
					s.close();
					con.close();
					
					buffmsj.close();
					filemsj.close();
				}
				File f = new File(folderact + "status_log");
				FileWriter fsl = new FileWriter(f);
				fsl.write("STATUS=OK\nBD=FSI\nPUNTO=MSJ");
				fsl.close();
			}
			
			
		}
		
        pw.println("FINALIZADA LA ACTUALIZACION DE BASES DE DATOS:: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
		pw.println("------------------------------------------------------------------------------");
		
	}

	private void desempaquetarActualizacion(String path_zip, String destfolder, PrintWriter pw) throws IOException
	{
		JZipUnZipUtil uzapl = new JZipUnZipUtil();
		uzapl.unZipAct(path_zip, destfolder);
		
		String status_log =	"STATUS=ACTBD\nBD=-1\nPUNTO=NC";
		FileWriter fsl = null;
		File file = new File(destfolder + "status_log");
		fsl = new FileWriter(file);
		fsl.write(status_log);
		fsl.close();
        
		pw.println("FINALIZADA LA DESCARGA Y DESEMPAQUETADO DE LA ACTUALIZACION: " + JUtil.obtFechaTxt(new Date(), "HH:mm:ss"));
		pw.println("------------------------------------------------------------------------------");
				
	}
	
}
