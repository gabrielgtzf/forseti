import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forseti.JBDRegistradasSet;
import forseti.JUtil;

public class JMsj extends HttpServlet
{
   	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("ACTUALIZANDO MENSAJES SOBRE FORSETI_ADMIN<br><br>");
		out.flush();
		
    	// Se encarga de actualizar mensajes en la base de datos
    	// genera una conexion natural con los datos establecidos
    	// Solo se utiliza en desarrollo
		try
      	{
			String PASS = JUtil.getPASS(), ADDR = JUtil.getADDR(), PORT = JUtil.getPORT();
		            
			String driver = "org.postgresql.Driver";
          	String url = "jdbc:postgresql://" + ADDR + ":" + PORT + "/FORSETI_ADMIN?user=forseti&password=" + PASS;
          	Class.forName(driver).newInstance();
      		Connection conn = DriverManager.getConnection(url);
      		Statement s = conn.createStatement();
      		// Ahora toma del archivo de lenguaje segun sea el caso (Hasta ahora solo permite Español (es) En proximas actualizaciones será multiidioma)
      		FileReader file = new FileReader("/usr/local/forseti/bin/.forseti_es");
            BufferedReader buff = new BufferedReader(file);
            boolean eof  = false;
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
                		String sqlcount = "SELECT count(*) as NE FROM tbl_msj WHERE alc = " + alc + " and mod = " + mod + " and sub = " + sub + " and elm = " + elm;
                		ResultSet rs   = s.executeQuery(sqlcount);
                        int NE = 0; String sqllang = "";
                		if(rs.next())
                			NE = rs.getInt("NE");
                                        		
                		if(NE > 0)
                		{
                			sqllang =  "UPDATE tbl_msj\n";
                			sqllang += "SET msj1 = " + msj1 + ", msj2 = " + msj2 + ", msj3 = " + msj3 + ", msj4 = " + msj4 + ", msj5 = " + msj5 + "\n";
                			sqllang += "WHERE alc = " + alc + " and mod = " + mod + " and sub = " + sub + " and elm = " + elm;
                		}
                		else
                		{
                			sqllang = "INSERT INTO tbl_msj\nVALUES(";
                			sqllang += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ")";
                		}
                		//Aqui genera el registro
                		System.out.println(sqllang);
                        s.execute(sqllang);
                	}
                }
            }
            buff.close();
            file.close();
            buff = null;
            file = null;
            // Ahora, cierra la conexion......
            s.close();
      		conn.close();
      		//////////////////////////////////////////////////////////////////////////////////
      		//AHORA TRABAJA CON CADA BASE DE DATOS DEL SISTEMA DE EMPRESAS
      		JBDRegistradasSet bdr = new JBDRegistradasSet(null);
      		bdr.ConCat(true);
      		bdr.Open();
      		
      		for(int bd = 0; bd < bdr.getNumRows(); bd++)
      		{
      			out.println("ACTUALIZANDO MENSAJES SOBRE " + bdr.getAbsRow(bd).getNombre() + "<br><br>");
      			out.flush();
    			url = "jdbc:postgresql://" + ADDR + ":" + PORT + "/" + bdr.getAbsRow(bd).getNombre() + "?user=" + bdr.getAbsRow(bd).getUsuario() + "&password=" + bdr.getAbsRow(bd).getPassword();
              	conn = DriverManager.getConnection(url);
          		s = conn.createStatement();
          		// Ahora toma del archivo de lenguaje segun sea el caso (Hasta ahora solo permite Español (es) En proximas actualizaciones será multiidioma)
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
                    		String sqlcount = "SELECT count(*) as NE FROM tbl_msj WHERE alc = " + alc + " and mod = " + mod + " and sub = " + sub + " and elm = " + elm;
                    		ResultSet rs   = s.executeQuery(sqlcount);
                            int NE = 0; String sqllang = "";
                    		if(rs.next())
                    			NE = rs.getInt("NE");
                                            		
                    		if(NE > 0)
                    		{
                    			sqllang =  "UPDATE tbl_msj\n";
                    			sqllang += "SET msj1 = " + msj1 + ", msj2 = " + msj2 + ", msj3 = " + msj3 + ", msj4 = " + msj4 + ", msj5 = " + msj5 + "\n";
                    			sqllang += "WHERE alc = " + alc + " and mod = " + mod + " and sub = " + sub + " and elm = " + elm;
                    		}
                    		else
                    		{
                    			sqllang = "INSERT INTO tbl_msj\nVALUES(";
                    			sqllang += alc + "," + mod + "," + sub + "," + elm + "," + msj1 + "," + msj2 + "," + msj3 + "," + msj4 + "," + msj5 + ")";
                    		}
                    		//Aqui genera el registro
                    		//System.out.println(sqllang);
                            s.execute(sqllang);
                    	}
                    }
                }
                buff.close();
                file.close();
                buff = null;
                file = null;
                // Ahora, cierra la conexion......
                s.close();
          		conn.close();

      		}
      		out.println("Los mensajes se han actualizado satisfactoriamente");
      	} 
      	catch(Throwable e)
      	{
      		out.println("Hubieron errores al actualizar los mensajes<br>ERROR:<br>" + e.getMessage());
      	}		
	}
}