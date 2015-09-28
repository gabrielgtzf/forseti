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
package fsi_admin.admon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import forseti.JFsiScript;
import forseti.JUtil;
import forseti.sets.JAdmVariablesSet;
import fsi_admin.JFsiForsetiApl;
//import fsi_admin.JPacConn.SavingTrustManager;

public class JAdmSSLDlg extends JFsiForsetiApl
{
    private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String adm_ssl_dlg = "";
      request.setAttribute("adm_ssl_dlg",adm_ssl_dlg);

      String mensaje = ""; short idmensaje = -1;
   
      if(request.getParameter("proceso") != null && !request.getParameter("proceso").equals(""))
      {
        if(request.getParameter("proceso").equals("CONSULTAR_CERAF"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_SSL_MANEJO"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_SSL_MANEJO");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
        	  String[] valoresParam = request.getParameterValues("id");
              if(valoresParam.length == 1)
              {
            	  if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
    	          {
            		  irApag("/forsetiadmin/administracion/adm_ssl_dlg_cons.jsp", request, response);
    	        	  return;
    	          }
    	          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
    	          {
    	        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
    	        	  irApag("/forsetiadmin/administracion/adm_ssl_dlg_pass.jsp", request, response);
    	        	  return;
    	          }
              }
              else
              {
            	  idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite eliminar una empresa a la vez <br>";
            	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	  return;
              }
	          
          }
          else
          {
        	  idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador de la empresa que se quiere eliminar<br>";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
              return;
          }
        }
        else if(request.getParameter("proceso").equals("GENERAR_CERAF"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_SSL_MANEJO"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_SSL_MANEJO");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  // Verificacion
        	  if(VerificarParametrosCerAF(request, response))
        	  {
        		  GenerarCerAF(request, response);
        		  return;
        	  }
        	  irApag("/forsetiadmin/administracion/adm_ssl_dlg.jsp", request, response);
        	  return;
          }
          else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
          {
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            irApag("/forsetiadmin/administracion/adm_ssl_dlg.jsp", request, response);
            return;
          }
        }
        else if(request.getParameter("proceso").equals("INSTALAR_CERAF"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_SSL_MANEJO"))
          {
            idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_SSL_MANEJO");
            getSesion(request).setID_Mensaje(idmensaje, mensaje);
            RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL||||",mensaje);
            irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("id") != null)
          {
            String[] valoresParam = request.getParameterValues("id");
            if(valoresParam.length == 1)
            {
            	if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
  	          	{
            		InstalarCerAF(request, response);
  	        	  	return;
  	          	}
  	          	else // Como el subproceso no es ENVIAR, abre la ventana del proceso de AGREGADO para agregar `por primera vez
  	          	{
  	          		getSesion(request).setID_Mensaje(idmensaje, mensaje);
  	          		irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
  	          		return;
  	          	}
            }
            else
            {
            	idmensaje = 1; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 2); //PRECAUCION: Solo se permite eliminar una empresa a la vez <br>";
            	getSesion(request).setID_Mensaje(idmensaje, mensaje);
            	irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
            	return;
            }
          }
          else
          {
             idmensaje = 3; mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 1); //ERROR: Se debe enviar el identificador de la empresa que se quiere eliminar<br>";
             getSesion(request).setID_Mensaje(idmensaje, mensaje);
             irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
             return;
          }
        }
        else if(request.getParameter("proceso").equals("CONFIAR_CERAF"))
        {
          // Revisa si tiene permisos
          if(!getSesion(request).getPermiso("ADMIN_SSL_MANEJO"))
          {
        	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "SAF", "ADMIN_SSL_MANEJO");
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  RDP("SAF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL||||",mensaje);
        	  irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
        	  return;
          }

          // Solicitud de envio a procesar
          if(request.getParameter("subproceso") != null && request.getParameter("subproceso").equals("ENVIAR"))
          {
        	  AgregarAjssecacerts(request, response);
        	  return;
          }
          else 
          {
        	  getSesion(request).setID_Mensaje(idmensaje, mensaje);
        	  irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
        	  return;
          }
          
        }
        else
        {
          idmensaje = 3;
          mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"PRECAUCION: El parámetro de proceso no es válido<br>";
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
          return;
        }

      }
      else // si no se mandan parametros, manda a error
      {
         idmensaje = 3;
         mensaje += JUtil.Msj("GLB", "VISTA", "GLB", "SELEC-PROC", 3); //"ERROR: No se han mandado parámetros reales<br>";
         getSesion(request).setID_Mensaje(idmensaje, mensaje);
         irApag("/forsetiadmin/caja_mensajes.jsp", request, response);
         return;
      }

    }
    
    public boolean VerificarParametrosCerAF(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      short idmensaje = -1; String mensaje = "";
      // Verificacion
      if(request.getParameter("nombre") != null && request.getParameter("alias") != null
          && request.getParameter("cn") != null  && request.getParameter("ou") != null
          && request.getParameter("o") != null  && request.getParameter("l") != null 
          && request.getParameter("st") != null  && request.getParameter("c") != null 
          && request.getParameter("password") != null  && request.getParameter("confpwd") != null 
          && request.getParameter("altnames") != null
          && !request.getParameter("nombre").equals("") && !request.getParameter("alias").equals("")
          && !request.getParameter("cn").equals("")  && !request.getParameter("ou").equals("")
          && !request.getParameter("o").equals("")  && !request.getParameter("l").equals("")
          && !request.getParameter("st").equals("")  && !request.getParameter("c").equals("")
          && !request.getParameter("password").equals("")  && !request.getParameter("confpwd").equals("")
          && !request.getParameter("altnames").equals("") )
      {
    	  if(!request.getParameter("password").equals(request.getParameter("confpwd")))
    	  {
    		  idmensaje = 3; mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",1); //"ERROR: No coincide el la contraseña con su confirmación";
              getSesion(request).setID_Mensaje(idmensaje, mensaje);
              return false;
    	  }
    	  return true;
      }
      else
      {
          idmensaje = 3; mensaje = JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          return false;
      }

    }

    public void GenerarCerAF(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	String mensaje = ""; short idmensaje = -1;
    	JFsiScript sc = new JFsiScript();
		sc.setVerbose(true);
		
		String ERROR = "";
		
		try 
		{
			//System.out.println("ADQUIRIENDO LA CONEXION DEL USUARIO FORSETI PRICIPAL...");
			String CONTENT = "keytool -genkeypair -keystore /usr/local/forseti/bin/forsetikeystore" + request.getParameter("nombre") + " -dname \"CN=" + request.getParameter("cn") + 
					", OU=" + request.getParameter("ou") + ", O=" + request.getParameter("o") + ", L=" + request.getParameter("l") + ", ST=" + request.getParameter("st") + 
					", C=" + request.getParameter("c") + "\" -keypass " + request.getParameter("password") + " -storepass " + request.getParameter("confpwd") +
					" -keyalg RSA -alias " + request.getParameter("alias") + " -ext SAN=" + request.getParameter("altnames"); 
			sc.setContent(CONTENT);
			System.out.println(CONTENT);
			String RES = sc.executeCommand();
			ERROR += sc.getError();
			//Ahora genera el Archivo Base64
			if(!ERROR.equals(""))
			{
				//System.out.println(ERROR);
				idmensaje = 3; 
				mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",2) + " " + ERROR;//"Se han producido errores al crear el Certificado: " + ERROR;
			}
			else
			{
				if(RES.equals(""))
				{
					idmensaje = 0;
					mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",1); //"El Certificado Autofirmado se genero con exito";
				}
				else
				{
					idmensaje = 3;
					mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",3) + "<br>" + RES; //"ERROR generado por el keytool:<br>" + RES;
				}
			}
	    	//////////////////////////////////////////
            
      	}
      	catch(Throwable e)
      	{
      		idmensaje = 3;
      		mensaje = "ERROR Throwable: " + "<br>" + e.getMessage();
      		//System.out.println(mensaje);
      	}
      	
      	RDP("SAF",getSesion(request).getConBD(),(idmensaje == 1 ? "OK" : "ER"),getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL|" + p(request.getParameter("nombre")) + "|||",mensaje);
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiadmin/administracion/adm_ssl_dlg.jsp", request, response);
  	  	return;        
    }
 
    public void AgregarAjssecacerts(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	short idmensaje = -1;
	   	StringBuffer sb_mensaje = new StringBuffer();
    	String host = "localhost:" + request.getParameter("puerto");
    	
    	JAdmVariablesSet var = new JAdmVariablesSet(null);
		var.ConCat(true);
		var.m_Where = "ID_Variable = 'TOMCAT'";
		var.Open();
		
		if(var.getAbsRow(0).getVAlfanumerico().equals("NC"))
		{
			idmensaje = 1;
			sb_mensaje.append(JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",4));//"PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede instalar el certificado");
			getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
			irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
	  	  	return;
		}
		
    	JAdmVariablesSet var2 = new JAdmVariablesSet(null);
		var2.ConCat(true);
		var2.m_Where = "ID_Variable = 'JVM'";
		var2.Open();
		
		if(var2.getAbsRow(0).getVAlfanumerico().equals("NC"))
		{
			idmensaje = 1;
			sb_mensaje.append(JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",5)); //"PRECAUCION: La variable JVM (ruta de instalacion del OpenJDK o Java JDK) no está definida... No se puede generar");
			getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
			irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
	  	  	return;
		}
		
		try
		{
    		idmensaje = InstallCert(host,request.getParameter("password"),sb_mensaje);
    	
    		if(idmensaje == 0)
    		{
    			JFsiScript sc = new JFsiScript();
    			sc.setVerbose(true);

    			String CONTENT = "sudo cp /usr/local/forseti/bin/jssecacerts " + var2.getAbsRow(0).getVAlfanumerico() + "/jre/lib/security";
    			sc.setContent(CONTENT);
    			System.out.println(CONTENT);
    			String RES = sc.executeCommand();
    			if(!sc.getError().equals("") || !RES.equals(""))
    			{
    				idmensaje = 3; 
    				sb_mensaje.append("<br>" + JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR2",1) + " " + RES + " " + sc.getError()); //"<br>Se han producido errores al copiar el certificado a jssecacerts del jvm actual: " + RES + " " + sc.getError());
    			}
    		}
    	}
    	catch (Exception e)
		{
			idmensaje = 3;
			sb_mensaje.append("ERROR Exception JSSECASERTS: " + e.getMessage());
		}
		
		RDP("SAF",getSesion(request).getConBD(),(idmensaje == 1 ? "OK" : "ER"),getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL|jssecacerts|||",p(sb_mensaje.toString()));
        getSesion(request).setID_Mensaje(idmensaje, sb_mensaje.toString());
        irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
  	  	return;
	}
    
    @SuppressWarnings("rawtypes")
	public void InstalarCerAF(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException
    {
    	System.out.println("Inicio de instalacion");
    	short idmensaje = -1; String mensaje = "";
		
		idmensaje = 0;
		mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",1); //"El certificado se instaló con exito";
		
		try 
		{
			JAdmVariablesSet var = new JAdmVariablesSet(null);
			var.ConCat(true);
			var.m_Where = "ID_Variable = 'TOMCAT'";
			var.Open();
			
			if(var.getAbsRow(0).getVAlfanumerico().equals("NC"))
			{
				idmensaje = 1;
				mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR",4); //"PRECAUCION: La variable TOMCAT (ruta de instalacion de tomcat) no está definida... No se puede instalar el certificado";
				getSesion(request).setID_Mensaje(idmensaje, mensaje);
				irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
		  	  	return;
			}
			
			boolean cs = false;
			SAXBuilder builder = new SAXBuilder();
		   	String xml_path = var.getAbsRow(0).getVAlfanumerico() + "/conf/server.xml";
		   	Document XMLServer = builder.build(xml_path);
			//System.out.println(xml_path + "\narchivo: " + request.getParameter("id") + "\npassword: " + request.getParameter("password"));
		   	Element Server = XMLServer.getRootElement();
		   	Element Service = Server.getChild("Service");
		   	List lConnectors = Service.getChildren("Connector");
		   	Iterator iter = lConnectors.iterator();
  			while (iter.hasNext()) 
  			{
  				Element Connector = (Element)iter.next();
  				String port = Connector.getAttributeValue("port");
  				if(port != null &&
  						( port.equals("8443") || port.equals("443") ) )  
  				{
  					Connector.setAttribute("keystoreFile","/usr/local/forseti/bin/" + request.getParameter("id"));
  					Connector.setAttribute("keystorePass",request.getParameter("password"));
  					Connector.setAttribute("port",request.getParameter("puerto"));
  					cs = true;
  				}
 
  				if(port != null &&
  						( port.equals("8080") || port.equals("80") ) )  
  				{
  					if(request.getParameter("puerto").equals("8443"))
  						Connector.setAttribute("port","8080");
  					if(request.getParameter("puerto").equals("443"))
  						Connector.setAttribute("port","80");
  						
  					Connector.setAttribute("redirectPort",request.getParameter("puerto"));
  				}
  				

  			}
  			
  			if(!cs)
  			{
  				Element Connector = new Element("Connector");
  				Connector.setAttribute("protocol","HTTP/1.1");
  				Connector.setAttribute("port",request.getParameter("puerto"));
				Connector.setAttribute("maxThreads","200");
  				Connector.setAttribute("scheme","https");
  				Connector.setAttribute("secure","true");
  				Connector.setAttribute("SSLEnabled","true");
  				Connector.setAttribute("keystoreFile","/usr/local/forseti/bin/" + request.getParameter("id"));
				Connector.setAttribute("keystorePass",request.getParameter("password"));
				Connector.setAttribute("clientAuth","false");
  				Connector.setAttribute("sslProtocol","TLS");
  				Service.addContent(Connector);
  				cs = true;
  			}
  			
  			if(cs)
  			{
  				System.out.println("Prettyformat");
  			   	
  				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				format.setTextMode(TextMode.NORMALIZE);
				XMLOutputter xmlOutputter = new XMLOutputter(format);
				FileWriter writer = new FileWriter(xml_path);
				xmlOutputter.output(XMLServer, writer);
				writer.close();
				//System.out.println("PrettyformatWrite");
  			   	
				mensaje += "<br>" + JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",2) + " " + JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",3) + " " + JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",4); //"<br>El archivo server.xml ha quedado modificado con los datos de este certificado. Nota: Si la contraseña esta mal, ya no se podra reiniciar el servidor de una manera segura, por lo tanto, deberas cambiar el archivo server.xml de manera manual para corregir la contraseña. Si no estas seguro que esta bien la contraseña, consulta este mismo certificado para ver si es correcta, de lo contrario vuelve a instalar inmediatamente este certificado con la contraseña correcta antes de reiniciar el servidor";
				
				
			}
  			else
  			{
  				idmensaje = 3;
  				mensaje = JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCERR2",2);//"ERROR: No se modifico el archivo server.xml debido a errores desconocidos";
  			}
		} 
		catch (JDOMException e) 
		{
			idmensaje = 3;
			mensaje = "ERROR de JDOMException en archivo XML: " + e.getMessage();
		} 
		catch (IOException e) 
		{
			idmensaje = 3;
			mensaje = "ERROR de IOException en archivo XML: " + e.getMessage();
		}
		catch (Exception e)
		{
			idmensaje = 3;
			mensaje = "ERROR de Exception en archivo XML: " + e.getMessage();
		}
		
		RDP("SAF",getSesion(request).getConBD(),(idmensaje == 1 ? "OK" : "ER"),getSesion(request).getID_Usuario(),"ADMIN_SSL_MANEJO","ASSL|" + p(request.getParameter("id")) + "|||",p(mensaje));
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        irApag("/forsetiadmin/administracion/adm_ssl_dlg_passport.jsp", request, response);
  	  	return;
  }

    
  private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

  public short InstallCert(String host, String pass, StringBuffer sb_mensaje)
	    throws ServletException, IOException
  {
	  try
	  {
		  int port;
		  char[] passphrase;
		  if (host != null) 
		  {
			  String[] c = host.split(":");
			  host = c[0];
			  port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
			  String p = pass == null ? "changeit" : pass;
			  passphrase = p.toCharArray();
			  System.out.println(host + " " + p);
		  } 
		  else 
		  {
			  sb_mensaje.append("Modo de Uso: <host>[:port] [passphrase]");
			  return 3;	
		  }
		  File file = new File("/usr/local/forseti/bin/jssecacerts");
		  if (file.isFile() == false) 
		  {
			  char SEP = File.separatorChar;
			  System.out.println(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
			  File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
			  file = new File(dir, "jssecacerts");
			  if (file.isFile() == false) 
			  {
				  file = new File(dir, "cacerts");
			  }
		  }
		    
		  System.out.println("Cargando KeyStore " + file + "...");
		  InputStream in = new FileInputStream(file);
		  KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		  ks.load(in, passphrase);
		  in.close();
	
		  SSLContext context = SSLContext.getInstance("TLS");
		  TrustManagerFactory tmf =
				  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		  tmf.init(ks);
		  X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		  SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		  context.init(null, new TrustManager[]{tm}, null);
		  SSLSocketFactory factory = context.getSocketFactory();
	
		  System.out.println("Abriendo conexion a " + host + ":" + port + "...");
		  SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		  socket.setSoTimeout(10000);
		  try 
		  {
			  System.out.println("Empezando SSL handshake...");
			  socket.startHandshake();
			  socket.close();
			  System.out.println("Sin errores, este certificado es ya de confianza");
		  } 
		  catch (SSLException e) 
		  {
			  e.printStackTrace(System.out);
		  }
	
		  X509Certificate[] chain = tm.chain;
		  if (chain == null) 
		  {
			  sb_mensaje.append("No se puede obtener el chain del certificado del servidor");	
			  return 3;
		  }
	
		  //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		  System.out.println("El Servidor envió: " + chain.length + " certificado(s):");
		  MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		  MessageDigest md5 = MessageDigest.getInstance("MD5");
		  for (int i = 0; i < chain.length; i++) 
		  {
			  X509Certificate cert = chain[i];
			  System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
			  System.out.println("   Issuer  " + cert.getIssuerDN());
			  sha1.update(cert.getEncoded());
			  System.out.println("   sha1    " + toHexString(sha1.digest()));
			  md5.update(cert.getEncoded());
			  System.out.println("   md5     " + toHexString(md5.digest()));
		  }
	
		  //out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
		  String line = "1";
		  int k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
		    		    
		  X509Certificate cert = chain[k];
		  String alias = host + "-" + (k + 1);
		  ks.setCertificateEntry(alias, cert);
	
		  OutputStream outs = new FileOutputStream("/usr/local/forseti/bin/jssecacerts");
		  ks.store(outs, passphrase);
		  outs.close();
	
		  System.out.println(cert);
		  sb_mensaje.append(JUtil.Msj("SAF","ADMIN_SSL","DLG","MSJ-PROCOK",5) + " '" + alias + "'");//"Certificado añadido al keystore 'jssecacerts' usando alias '" + alias + "'");
		  return 0;
	  }
	  catch(Exception e)
	  {
		  sb_mensaje.append("ERROR Exception InstallCert: " + e.getMessage());
		  return 3;
	  }
	}

	private static String toHexString(byte[] bytes) 
	{
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) 
		{
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SavingTrustManager implements X509TrustManager 
	{

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) 
		{
			this.tm = tm;
		}

		//version java 7
		public X509Certificate[] getAcceptedIssuers() 
		{
            return new X509Certificate[0];
        }
        /*version java 5 y 6
		public X509Certificate[] getAcceptedIssuers() 
		{
			throw new UnsupportedOperationException();
		}
		*/
		
		public void checkClientTrusted(X509Certificate[] chain, String authType)
	                throws CertificateException 
	    {
			throw new UnsupportedOperationException();
	    }

		public void checkServerTrusted(X509Certificate[] chain, String authType)
	                throws CertificateException 
	    {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
	    }
	}
}
