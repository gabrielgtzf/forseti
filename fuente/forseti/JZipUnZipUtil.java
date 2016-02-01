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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JZipUnZipUtil 
{
	List<String> fileList;
	//private String OUTPUT_ZIP_FILE;
    private String SOURCE_FOLDER;
    private static final int BUFFER_SIZE = 4096;
    
    public JZipUnZipUtil()
	{
		fileList = new ArrayList<String>();
	}
	
	public void unZipFile(String strFilePathZip, String destinationname ) throws IOException
	{
		String filename = strFilePathZip;
		byte[] buf = new byte[1024];
		ZipInputStream zipinputstream = null;
		ZipEntry zipentry;
		zipinputstream = new ZipInputStream(new FileInputStream(filename));
		zipentry = zipinputstream.getNextEntry();
		while (zipentry != null) 
		{ 
			//for each entry to be extracted
			String entryName = zipentry.getName();
			//System.out.println("entryname "+entryName);
			int n;
			FileOutputStream fileoutputstream;
			File newFile = new File(entryName);
			String directory = newFile.getParent();
	   
			if(directory == null)
			{
				if(newFile.isDirectory())
					break;
			}
	    
			fileoutputstream = new FileOutputStream(destinationname + entryName);             
			while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
				fileoutputstream.write(buf, 0, n);
	
			fileoutputstream.close(); 
			zipinputstream.closeEntry();
			zipentry = zipinputstream.getNextEntry();
		}//while
		zipinputstream.close();
	}
	
	public void zipFile(String inputFile, String outputFile, String NAME) throws IOException
	{
		FileInputStream in = new FileInputStream(inputFile);
		FileOutputStream out = new FileOutputStream(outputFile);

		byte b[] = new byte[2048];

		ZipOutputStream zipOut = new ZipOutputStream(out);
		ZipEntry entry = new ZipEntry(NAME + ".xml");
		zipOut.putNextEntry(entry);
		int len = 0;
		while ((len = in.read(b)) != -1) 
		{
			zipOut.write(b, 0, len);
		}
		zipOut.closeEntry();
		zipOut.close();
	}
	

	
	//////////////////////////////
	public void unZipAct(String path_zip, String destfolder) throws IOException
	{
		System.out.println("ZIP/DEST: " + path_zip + " " + destfolder);
		byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new FileInputStream(path_zip));

        zipentry = zipinputstream.getNextEntry();
        while (zipentry != null) 
        {
            //for each entry to be extracted
            String entryName = destfolder + zipentry.getName();
            entryName = entryName.replace('/', File.separatorChar);
            entryName = entryName.replace('\\', File.separatorChar);
            //System.out.println("entryname " + entryName);
            int n;
            FileOutputStream fileoutputstream;
            File newFile = new File(entryName);
            if (zipentry.isDirectory()) 
            {
                if (!newFile.mkdirs()) 
                {
                    break;
                }
                zipentry = zipinputstream.getNextEntry();
                continue;
            }

            fileoutputstream = new FileOutputStream(entryName);

            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) 
            {
                fileoutputstream.write(buf, 0, n);
            }

            fileoutputstream.close();
            zipinputstream.closeEntry();
            zipentry = zipinputstream.getNextEntry();

        }//while

        zipinputstream.close();
	}
	
	/////////////////////////// Folder ignorando directorios vacios ///////////////////////////////////////
	public void zipFolder(String SOURCE_FOLDER, String OUTPUT_ZIP_FILE) throws IOException
	{
		this.SOURCE_FOLDER = SOURCE_FOLDER;
		generateFileList(new File(SOURCE_FOLDER), true);
		zipIt(OUTPUT_ZIP_FILE);
	}
	 
	public void zipIt(String zipFile) throws IOException
	{
		byte[] buffer = new byte[1024];
		 
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		 
		System.out.println("Output to Zip : " + zipFile);
		 
		for(String file : this.fileList)
		{
			//System.out.println("File Added : " + file);
			ZipEntry ze= new ZipEntry(file);
			zos.putNextEntry(ze);
		 
			char ch = file.charAt(file.length() -1);
			
			if(ch != '/')
			{
				//System.out.println("ZipIt: " + SOURCE_FOLDER + File.separator + file);
				
				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
		 
				int len;
				while ((len = in.read(buffer)) > 0) 
				{
					zos.write(buffer, 0, len);
				}
		 
				in.close();
				
			}
		}
		 
		zos.closeEntry();
		//remember close it
		zos.close();
		 
		System.out.println("Done");
	}
	
	public void generateFileList(File node, boolean init)
	{
		if(node.isFile())
		{
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
	 
		if(node.isDirectory())
		{
			if(!init)
			{
				//System.out.println("ES DIR: ");	
				fileList.add(generateZipEntry(node.getAbsolutePath().toString() + "/"));
			}
			else
			{
				System.out.println("ES TOP DIR: " + node.getName() + "/");	
			}
			String[] subNote = node.list();
			for(String filename : subNote)
			{
				generateFileList(new File(node, filename), false);
			}
		}
	 
	}
	 			
	private String generateZipEntry(String file)
	{
		String res = file.substring(SOURCE_FOLDER.length()+1, file.length());
		//System.out.println("FILE: " + file);
		//System.out.println("RES: " + res);
		return res;
	}
	///////////////////////////////// folder agregando directorios vacios ////////////////////////////////////
	/*
	public void zipFolderWithEmptyDirs(String srcFolder, String destZipFile) throws Exception 
	{
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;
        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);
        addFolderToZip("", srcFolder, zip);
        zip.flush();
        zip.close();
    }

    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws Exception 
    {
    	System.out.println("addFileToZip: " + path + " " + srcFile + " " + flag);
    	
        File folder = new File(srcFile);
        if (flag == true) 
        {
        	zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
        } 
        else 
        {
        	if (folder.isDirectory()) 
        	{
                addFolderToZip(path, srcFile, zip);
            } 
        	else 
        	{
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) 
                {
                    zip.write(buf, 0, len);
                }
            }
        }
    }
	
    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception 
    {
    	System.out.println("addFolderToZip: " + path + " " + srcFolder);
        
        File folder = new File(srcFolder);

        if (folder.list().length == 0) 
        {
        	System.out.println("folderSize = 0");
            addFileToZip(path, srcFolder, zip, true);
        } 
        else 
        {
        	System.out.println("folderSize = " + folder.list().length);
            for (String fileName : folder.list()) 
            {
                if (path.equals("")) 
                {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                } 
                else 
                {
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }
    */
    ///////////////////////////////////////// unZip ///////////////////////////////////////////////
    public void unZipDir(String zipFilePath, String destDirectory) throws IOException 
    {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) 
        {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) 
        {
        	String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) 
            {
                // if the entry is a file, extracts it
            	extractFile(zipIn, filePath);
            } 
            else 
            {
                // if the entry is a directory, make the directory
            	File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException 
    {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) 
        {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
