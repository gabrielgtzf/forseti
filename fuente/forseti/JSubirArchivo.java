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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

public class JSubirArchivo 
{
	private long m_MaxSize;
	private String m_Path;
	private String m_Error;
	 
	@SuppressWarnings("rawtypes")
	private Vector  m_Files;
	
	public JSubirArchivo()
	{
		m_MaxSize = 512;
		m_Path = ""; // Si no se especifica, copia en la ruta por defaut
		m_Error = "";
	}
	
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSubirArchivo(long MaxSize, String Path, String [] Exts, boolean [] Frz)
	{
		m_MaxSize = MaxSize;
		m_Path = Path;
		m_Error = "";
		m_Files = new Vector();
		for(int i = 0; i < Exts.length; i++)
		{
			MyUploadedFiles f = new MyUploadedFiles();
			f.m_Ext = Exts[i];
			f.m_File = "";
			f.m_Frz = Frz[i];
			m_Files.addElement(f);
		}
	}
	
	public int NumExts()
	{
		return m_Files.size();
	}
	
	public String getExt(int index)
	{
		return ((MyUploadedFiles)m_Files.elementAt(index)).m_Ext;
	}
	
	public boolean isFrz(int index)
	{
		return ((MyUploadedFiles)m_Files.elementAt(index)).m_Frz;
	}
	
	public String getFile(int index)
	{
		return ((MyUploadedFiles)m_Files.elementAt(index)).m_File;
	}

	 
	@SuppressWarnings("rawtypes")
	public int processFiles(HttpServletRequest request, boolean onlyOne) // Request No debe contener parametros de formulario, sino solo los parámetro del archivo
    {
    	int numFiles = 0, thisFile = 0;
    	
        try 
        {
            // construimos el objeto que es capaz de parsear la perición
            DiskFileUpload fu = new DiskFileUpload();
            // maximo numero de bytes
            fu.setSizeMax(1024*m_MaxSize); // 512 K
            // tamaño por encima del cual los ficheros son escritos directamente en disco
            fu.setSizeThreshold(4096);
            // directorio en el que se escribirán los ficheros con tamaño superior al soportado en memoria
            fu.setRepositoryPath("/tmp");
            // ordenamos procesar los ficheros
            List fileItems = fu.parseRequest(request);
            if(fileItems == null)
            {
            	m_Error += JUtil.Msj("GLB","GLB","GLB","ARCHIVO",3) + " null"; 
                return 0;
            }
            // Iteramos por cada fichero
            Iterator i = fileItems.iterator();
            FileItem actual = null;

            if(onlyOne) 
            {
            	actual = (FileItem)i.next();
            	String fileName = actual.getName();
                // construimos un objeto file para recuperar el trayecto completo
                File file = new File(fileName);
                
                // Verifica que el archivo sea de la extension esperada
                for(int f = 0; f < m_Files.size(); f++)
                {
                	String ext = "." + getExt(f).toLowerCase(); 
                    System.out.println("Archivo: " + fileName + " Ext esperada: " + ext);
                    
                	if(file.getName().toLowerCase().indexOf(ext) != -1)
                	{
                		// nos quedamos solo con el nombre y descartamos el path
                		file = new  File(m_Path + file.getName());
                		// escribimos el fichero colgando del nuevo path
                		actual.write(file);
                		MyUploadedFiles part = (MyUploadedFiles) m_Files.elementAt(thisFile);
   				 		part.m_File = file.getName();

   				 		numFiles += 1;
   				 		break;
                	}
                }               
                
                if(numFiles == 0)
                	m_Error += " " + JUtil.Msj("GLB","GLB","GLB","ARCHIVO",4) + " " + file.getName(); 
       
            }
            else
            {
            	while((actual = (FileItem)i.next()) != null)
	            {
	            	String fileName = actual.getName();
	                // construimos un objeto file para recuperar el trayecto completo
	                File file = new File(fileName);
	                String ext = "." + getExt(thisFile).toLowerCase(); 
	                System.out.println("Archivo: " + fileName + " Ext esperada: " + ext);
	                
	                // Verifica que el archivo sea de la extension esperada
	                if(file.getName().toLowerCase().indexOf(ext) != -1)
	                {
	                	// nos quedamos solo con el nombre y descartamos el path
	                	file = new  File(m_Path + file.getName());
	                	// escribimos el fichero colgando del nuevo path
	                	actual.write(file);
	                	MyUploadedFiles part = (MyUploadedFiles) m_Files.elementAt(thisFile);
	   				 	part.m_File = file.getName();
	
	                	numFiles += 1;
	                }
	                else
	                	m_Error += " " + JUtil.Msj("GLB","GLB","GLB","ARCHIVO",4) + " " + file.getName() + " - " + ext; 
	                
	                thisFile += 1;
	            }
	            System.out.println("Despues while");
            }
        }
        catch(Exception e)
        {
            if(e != null)
            	m_Error += " " + JUtil.Msj("GLB","GLB","GLB","ARCHIVO",3) + " " + e.getMessage(); //"Error al subir archivos: " + e.getMessage();
        }

        return numFiles;
    }

	@SuppressWarnings("rawtypes")
	public int processFiles(Vector archivos)
    {
    	int numFiles = 0, thisFile = 0;
    	
        try 
        {
            FileItem actual = null;

            for(int i = 0; i < archivos.size(); i++) 
            {
            	actual = (FileItem)archivos.elementAt(i);
            	String fileName = actual.getName();
                // construimos un objeto file para recuperar el trayecto completo
                File file = new File(fileName);
                String ext = "." + getExt(thisFile).toLowerCase(); 
                boolean frz = isFrz(thisFile);
                //System.out.println("Archivo: " + fileName + " Ext esperada: " + ext);
                
                // Verifica que el archivo sea de la extension esperada
                if(file.getName().toLowerCase().indexOf(ext) != -1)
                {
                	// nos quedamos solo con el nombre y descartamos el path
                	file = new  File(m_Path + file.getName());
                	// escribimos el fichero colgando del nuevo path
                	actual.write(file);
                	MyUploadedFiles part = (MyUploadedFiles) m_Files.elementAt(thisFile);
   				 	part.m_File = file.getName();

                	numFiles += 1;
                }
                else if(frz) //Si era forzozo 
                	m_Error += " " + JUtil.Msj("GLB","GLB","GLB","ARCHIVO",4) + " " + file.getName() + " - " + ext; 
                
                thisFile += 1; 
       
            }
            
        }
        catch(Exception e)
        {
            if(e != null)
            	m_Error += " " + JUtil.Msj("GLB","GLB","GLB","ARCHIVO",3) + " " + e.getMessage(); //"Error al subir archivos: " + e.getMessage();
        }

        return numFiles;
    }

    public String getError()
    {
    	return m_Error;
    }
    
    class MyUploadedFiles
    {
    	String m_File;
    	String m_Ext;
    	boolean m_Frz;
    }
}
