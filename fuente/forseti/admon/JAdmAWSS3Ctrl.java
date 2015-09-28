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
package forseti.admon;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JForsetiApl;

public class JAdmAWSS3Ctrl extends JForsetiApl
{
 	private static final long serialVersionUID = 1L;
 	private static final String m_HOST = getHOST();
 	
 	public static String getHOST()
	{
		String host = "";
		try
		{
	 		FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_pac");
			BufferedReader buff = new BufferedReader(file);
			boolean eof = false;
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
								
						if(key.equals("SERV"))
							host = value;
					}
					catch(NoSuchElementException e)
                	{
                		continue;
                	}
				}
			}
			buff.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
			
		return host;
 	}
 	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
      super.doPost(request,response);

      String adm_awss3 = "";
      request.setAttribute("adm_awss3",adm_awss3);

      String mensaje = ""; short idmensaje = -1;
   
      if(!getSesion(request).getPermiso("ADM_AWSS3"))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", "ADM_AWSS3");
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3","AAS3||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }

      if(!getSesion(request).getPermiso(request.getParameter("ID_MODULO")))
      {
    	  idmensaje = 3; mensaje += MsjPermisoDenegado(request, "CEF", request.getParameter("ID_MODULO"));
          getSesion(request).setID_Mensaje(idmensaje, mensaje);
          RDP("CEF",getSesion(request).getConBD(),"NA",getSesion(request).getID_Usuario(),"ADM_AWSS3","AAS3||||",mensaje);
          irApag("/forsetiweb/caja_mensajes_vsta.jsp", request, response);
          return;
      }
      
      // establece en la sesion que los mensajes se estan configurando por primera ocasion
      if(getSesion(request).getEst("ADM_AWSS3") == false)
      {
        getSesion(request).EstablecerCEF(request, "adm_awss3.png", "ADM_AWSS3");
  	  	getSesion(request).getSesion("ADM_AWSS3").setParametros("", "", "", "", "", "");
  	  	getSesion(request).getSesion("ADM_AWSS3").setOrden(p(request.getParameter("etq")),"");
  	  	getSesion(request).getSesion("ADM_AWSS3").setEspecial("");
        
        getSesion(request).setID_Mensaje(idmensaje, mensaje);
        RDP("CEF",getSesion(request).getConBD(),"OK",getSesion(request).getID_Usuario(),"ADM_AWSS3","AAS3||||","");
      }

      // Construye la sentencia:
      String ID_MODULO = p(request.getParameter("ID_MODULO"));
      String OBJIDS = request.getParameter("OBJIDS");
      String IDSEP = request.getParameter("IDSEP");
      
      String WHERE = "Servidor = '" + m_HOST + "' and BD = '" + getSesion(request).getBDCompania() + "' and ID_Modulo = '" + p(ID_MODULO) + "'";
	  if(IDSEP.equals(""))
	  {
		  if(OBJIDS != null)
			  WHERE += " and Obj_ID1 = '" + p(OBJIDS) + "'";
		  else
			  WHERE += " and Obj_ID1 = ''";
		  
		  WHERE += " and Obj_ID2 = ''";
	  }
	  
	  getSesion(request).getSesion("ADM_AWSS3").setEntidad(WHERE,ID_MODULO);
	  getSesion(request).getSesion("ADM_AWSS3").setTiempo("",OBJIDS);
	  
      if(request.getParameter("orden") != null && !request.getParameter("orden").equals(""))
      {
    	  getSesion(request).getSesion("ADM_AWSS3").setOrden(p(request.getParameter("etq")),p(request.getParameter("orden")));
      }
      
      
      getSesion(request).setID_Mensaje(idmensaje, mensaje);
      irApag("/forsetiweb/administracion/adm_awss3_vsta.jsp", request, response);

    }

}
