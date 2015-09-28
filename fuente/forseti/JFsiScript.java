/*
* JFsiScript.java
*
* Clase para ejecutar scripts unix o SQL localmente
* @modified clase original Script de jdelgado modificada por el equipo forseti
*
*/

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

import java.io.File;
import java.io.FileOutputStream;
public class JFsiScript 
{
	// contenido a ejecutar
	private String content = null;
	// interprete de comandos: Si usas Windows adaptalo para usar CMD
	private String SHELL = "/bin/sh";
	// resultado de la ejecucion
	private String output = null;
	// posibles errores en la ejecucion
	private String error = null;
	// imprimir o no traza en los threads
	private Boolean verbose = false;
	// path por defecto para ficheros temporales
	private String RUTA = "/tmp";
	// cabecera para shell scripts
	private String SHELLHEADER = "#!/bin/sh";
	// indica si hay que añadir el interprete a los shell scripts que se generen
	private boolean addSHELLHEADER = false;
	// caracter de retorno
	private String RETORNO = "\n";
	// —-
	public JFsiScript() 
	{
	}
	
	public JFsiScript(String content) 
	{
		this.content = content;
	}
	
	public String executeCommand() throws Exception 
	{
		output = null;
		error = null;
		JFsiStreamGobbler errorGobbler = null;
		JFsiStreamGobbler outputGobbler = null;
		try 
		{
			if (content == null)
				throw new Exception("no se mandaron comandos");
			// String ruta="/home/jose/Desktop/temporal.txt";
			// FileOutputStream fos = new FileOutputStream(ruta);
			Runtime rt = Runtime.getRuntime();
			// Process proc = rt.exec(new String[]{"/bin/sh", "-c", "cd
			// /home/jose/Desktop\n./SQLPLUS.sh\ncal\\nps -ef"});
			String[] data = new String[3];
			data[0] = this.SHELL;
			data[1] = "-c";
			data[2] = this.content;
			Process proc = rt.exec(data);
			// any error message?
			errorGobbler = new JFsiStreamGobbler(proc.getErrorStream(), "ERROR",
			this.verbose);
			// any output?
			outputGobbler = new JFsiStreamGobbler(proc.getInputStream(), "OUTPUT",
			null, this.verbose);
			// StreamGobbler(proc.getInputStream(), "OUTPUT", fos);
			// kick them off
			errorGobbler.start();
			outputGobbler.start();
			// any error???
			/*int exitVal =*/ proc.waitFor();
		} 
		catch (Throwable t) 
		{
			t.printStackTrace();
		} 
		finally 
		{
			String out = outputGobbler.getOutput();
			this.output = out;
			output = out;
			out = errorGobbler.getOutput();
			this.error = out;
			error = out;
		}
		return output;
	}

	/*
	* Recupera la salida de la ejecucion del comando
	* @return un string con el resultado de la ejecucion
	*/
	public String getOutput() 
	{
		return output;
	}
	
	/*
	* Recupera el error de la ejecucion del comando
	* @return un string con el error de la ejecucion
	*/
	public String getError() 
	{
		return error;
	}
	
	/*
	* Ejecuta un Script creando un fichero .sh con el contenido y ejecutandolo
	*
	* @param content
	* contenido del script
	* @param pathname
	* el nombre del fichero que se crea si es null lo crea en /tmp y
	* de la forma /tmp/timestamp.sh
	* @param delete
	* indica si se debe borrar o no el fichero temporal tras su
	* ejecucion
	* @result el resultado de la ejecucion del Script
	* @throws Exception
	* Excepcion levantada en caso de error
	*/
	public String executeScript(String content, String pathname, boolean delete)
		throws Exception 
	{
		File f = null;
		FileOutputStream fout = null;
		try 
		{
			// crear fichero .sh
			if (pathname == null) 
			{
				long timestamp = System.currentTimeMillis();
				pathname = RUTA + File.separator + timestamp + ".sh";
			}
			if (this.addSHELLHEADER)
			{
				content = this.SHELLHEADER+this.RETORNO+content;
			}
			traza("executeScript","Generando fichero [" + pathname + "]…");
			f = new File(pathname);
			fout = new FileOutputStream(f);
			fout.write(content.getBytes());
			if (fout != null)
				fout.close();
			traza("executeScript","Fichero generado");
			// darle permisos de ejecucion
			traza("executeScript","Asignando permisos…");
			JFsiScript permisos = new JFsiScript("chmod +x " + pathname);
			permisos.setVerbose(this.verbose);
			permisos.executeCommand();
			String error = permisos.getError();
			if (error != null) 
			{
				if (error.length() > 0)
					throw new Exception("Permission denied : " + error);
			}
			traza("executeScript","Permisos asignados");
			// ejecutar fichero .sh
			traza("executeScript","Ejecutando script…");
			JFsiScript ejecucion = new JFsiScript(pathname);
			ejecucion.setVerbose(this.verbose);
			String result = ejecucion.executeCommand();
			error = ejecucion.getError();
			this.error = error;
			this.output = result;
			traza("executeScript","Script ejecutado");
			// eliminar fichero .sh
			if (delete) 
			{
				if (f != null) 
				{
					if (f.exists())
						f.delete();
				}
				traza("executeScript","fichero eliminado");
			}
			
			return result;
			
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			JFsiScript sc = new JFsiScript();
			sc.setVerbose(true);
			String CONTENT = "sqlplus2\nls -l\nps\necho \"End\\nFin\"";
			String OUTPUT = sc.executeScript(CONTENT,
			"/home/jose/Desktop/MiScript.sh", true);
			System.out.println(OUTPUT);
			String ERROR = sc.getError();
			System.out.println("**ERROR**" + ERROR);
			JFsiScript sql = new JFsiScript();
			sql.setVerbose(false);
			sql.setAddSHELLHEADER(true);
			sql.setSHELLHEADER("#!/bin/sh");
			//sql.setRUTA("/home/jose/Desktop");
			CONTENT = "SELECT sysdate FROM DUAL;\nSELECT sysdate FECHA_HOY FROM DUAL;";
			OUTPUT = sql.executeSQLScript(CONTENT, "EVO", "EVO", "EVODES",
			null, "/home/jose/Desktop/MiSQL.sh", false);
			System.out.println(OUTPUT);
			ERROR = sql.getError();
			System.out.println("**ERROR**" + ERROR);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	* Completa un script de SQL para poder ejecutarlo con sqlplus desde un
	* shell script
	*
	* @param sqlcontent
	* sentencias SQL
	* @param user
	* usuario de base de datos
	* @param password
	* password de la base de datos (Todo: guardar encriptada)
	* @param sid
	* sid de la base de datos
	* @param redirect
	* si se indica es el fichero donde se redirecciona la salida del
	* sqlplus
	* @return un string con el sqlplus
	*/
	public String prepareSQLScript(String sqlcontent, String user, String password, String sid, String redirect) 
	{
		if (redirect == null)
			redirect = "";
		else
			redirect = "> " + redirect;
	
		String INICIO_SQL = "sqlplus " + user + "/" + password + "@" + sid
			+ " << EOF" + " " + redirect;
		String FIN_SQL = "EOF";
		String sql = sqlcontent;
		sql = INICIO_SQL + RETORNO + sql + RETORNO + "exit" + RETORNO + FIN_SQL;
		
		return sql;
	}
	
	/*
	* Ejecuta un Script SQL mediante sqlplus
	*
	* @param sqlcontent
	* las sentencias SQL
	* @param user
	* usuario de la base de datos
	* @param password
	* la password de la base de datos
	* @param redirect
	* si se indica es el fichero donde se redirecciona la salida del
	* sqlplus
	* @param pathname
	* nombre del shell script que se crea
	* @param delete
	* indica si se debe borrar o no el shell script tras la
	* ejecucion
	* @return el resultado de la ejecucion del script
	*/
	public String executeSQLScript(String sqlcontent, String user, String password, String sid, String redirect, 
			String pathname, boolean delete) throws Exception 
	{
		String SQL_CONTENT = prepareSQLScript(sqlcontent, user, password, sid, redirect);
		return executeScript(SQL_CONTENT, pathname, delete);
	}

	/*
	* Recupera el valor del flag
	* @return el valor del flag
	*/
	public Boolean getVerbose() 
	{
		return verbose;
	}
	
	/*
	* Fija el valor del flag para mostrar por la consola estandard la ejecucion
	* @param debug el valor del flag
	*/
	public void setVerbose(Boolean debug) 
	{
		this.verbose = debug;
	}
	
	/*
	* Para imprimir un mensaje de traza
	*
	* @param metodo
	* nombre del metodo
	* @param mensaje
	* nombre de la clase
	*/
	public void traza(String metodo, String mensaje) 
	{
		// Todo: reemplazar aqui para emplear LOG4J
		// System.out.println(mensaje);
	}
	
	public boolean isAddSHELLHEADER() 
	{
		return addSHELLHEADER;
	}
	
	public void setAddSHELLHEADER(boolean addSHELLHEADER) 
	{
		this.addSHELLHEADER = addSHELLHEADER;
	}
	
	public String getContent() 
	{
		return content;
	}
	
	public void setContent(String content) 
	{
		this.content = content;
	}
	
	public String getRETORNO() 
	{
		return RETORNO;
	}
	
	public void setRETORNO(String retorno) 
	{
		RETORNO = retorno;
	}
	public String getRUTA() 
	{
		return RUTA;
	}
	
	public void setRUTA(String ruta) 
	{
		RUTA = ruta;
	}
	
	public String getSHELL() 
	{
		return SHELL;
	}
	
	public void setSHELL(String shell) 
	{
		SHELL = shell;
	}
	
	public String getSHELLHEADER() 
	{
		return SHELLHEADER;
	}
	
	public void setSHELLHEADER(String shellheader) 
	{
		SHELLHEADER = shellheader;
	}
	
	public void setError(String error) 
	{
		this.error = error;
	}
	
	public void setOutput(String output) 
	{
		this.output = output;
	}
}
