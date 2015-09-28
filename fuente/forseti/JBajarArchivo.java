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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class JBajarArchivo
{
    public void doDownload( HttpServletResponse resp, ServletContext context, String filename, String original_filename)
        //throws IOException
    {
    	
    	try
    	{
    		ServletOutputStream op       = resp.getOutputStream();
            File                f        = new File(filename);
	        int                 length   = 0;
	        String              mimetype = context.getMimeType( filename );
	
	        resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
	        resp.setContentLength( (int)f.length() );
	        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );
	        
	        byte[] bbuf = new byte[1024];
	        DataInputStream in = new DataInputStream(new FileInputStream(f));
	
	        while ((in != null) && ((length = in.read(bbuf)) != -1))
	        {
	            op.write(bbuf,0,length);
	        }
	
	        in.close();
	        op.flush();
	        op.close();
	        
	    }
	    catch (IOException e) 
		{
			e.printStackTrace();
		} 
		  
    }
   
	public void doDownloadMultipleFilesInZip(HttpServletResponse response, ServletContext context, String filename, String fileIds [], String names[])
	{
		String  mimetype = context.getMimeType( filename );
        response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + filename + "\"" );
        
		final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
		   
		ZipOutputStream output = null;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

		try 
		{
			output = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE));

			for (int i=0; i < fileIds.length; i++) 
			{
				InputStream input = null;

				try 
				{
					input = new BufferedInputStream(new FileInputStream(fileIds[i]), DEFAULT_BUFFER_SIZE);
					output.putNextEntry(new ZipEntry(names[i]));
					for (int length = 0; (length = input.read(buffer)) > 0;) 
					{
						output.write(buffer, 0, length);
					}
					output.closeEntry();
				} 
				finally 
				{
					if (input != null) try { input.close(); } catch (IOException logOrIgnore) { /**/ }
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (output != null) try { output.close(); } catch (IOException logOrIgnore) { /**/ }
		}
	}
	
    public void doDownload( HttpServletResponse resp, ServletContext context, ByteArrayInputStream istream, String mimetype, int islength, String original_filename )
            		throws IOException
    {
    	
    	int length = 0;
        ServletOutputStream op       = resp.getOutputStream();
    	resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
    	resp.setContentLength( islength );
    	resp.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );

    	//
    	//  Stream to the requester.
    	//
    	byte[] bbuf = new byte[1024];
    	
    	while ((istream != null) && ((length = istream.read(bbuf)) != -1))
    	{
    		op.write(bbuf,0,length);
    	}

    	istream.close();
    	op.flush();
    	op.close();
    }

    
    
}
