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

/*
* JFsiStreamGobbler.java
*
* Para capturar la salida de la ejecucion del comando.
* @modified clase original de javaworld modificada por jdelgado
* @modified clase original de jdelgado modificada por el equipo forseti
*/

import java.io.*;
class JFsiStreamGobbler extends Thread 
{
	// Flujo de entrada
	InputStream is;
	// Tipo de Flujo: generalmente output o error
	String type;
	// Flujo de salida
	OutputStream os;
	// Variable para mostrar por la salida estandard
	boolean debug = false;
	// para guardar la salida generada
	String output = "";

	/*
	* Constructor
	*
	* @param is
	* InputStream
	* @param type
	* tipo de flujo (OUTPUT o ERROR)
	* @param debug
	* indica si se debe mostrar o no la salida por la salida
	* estandard
	*/
	JFsiStreamGobbler(InputStream is, String type, boolean debug) 
	{
		this(is, type, null, debug);
	}
	
	/*
	* Constructor
	*
	* @param is
	* InputStram
	* @param type
	* tipo de flujo (OUTPUT o ERROR)
	* @param redirect
	* OutputStream donde redireccionar la salida
	* @param debug
	* indica si se debe mostrar o no la salida por la salida
	* estandard
	*/
	JFsiStreamGobbler(InputStream is, String type, OutputStream redirect, boolean debug) 
	{
		this.is = is;
		this.type = type;
		this.os = redirect;
		this.debug = debug;
	}
	
	/*
	* Ejecutar el hilo
	*/
	public void run() 
	{
		try 
		{
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);
	
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			
			while ((line = br.readLine()) != null) 
			{
				// si hay fichero lo imprime a fichero
				if (pw != null)
					pw.println(line);
				
				//if (debug)
				//	System.out.println(type + "> " + line);
				
				output = output + line + "\r\n";
			}
	
			if (pw != null)
				pw.flush();
			
		} 
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
		}
	}
	
	/*
	* Recuperar el flujo de datos generado
	*
	* @return un String con el output
	*/
	public String getOutput() 
	{
		return output;
	}

}
// end of class StreamGobbler.java