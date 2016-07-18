import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import forseti.JUtil;
     
public class HelloWorld extends HttpServlet
{

	private static final long serialVersionUID = 5132936917759337060L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		float num = Float.parseFloat(request.getParameter("num"));
		out.println("<html><body><p>" + JUtil.Converts(num, ".", ",", 2, false) + "</p></body></html>");
		//out.println("Hola Mundo de Forseti");
		
		
	}
/*
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
	{
		
		try 
		{
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			
			doGetXLSRead(request, response, out);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void doGetXLSRead(HttpServletRequest request, HttpServletResponse response, PrintWriter out) 
			throws IOException, ServletException
	{
		try
        {
			String sql = "insert into tbl_cont_rubros<br>";
			sql += "values(default,'AC','ACTIVO CIRCULANTE','1010','1210');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'AF','ACTIVO FIJO','1510','1910');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'PC','PASIVO A CORTO PLAZO','2010','2180');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'PL','PASIVO A LARGO PLAZO','2510','2600');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'CC','CAPITAL CONTABLE','3010','3060');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'RI','INGRESOS','4010','4030');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'RC','EGRESOS Y COSTOS','5010','5050');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'RG','GASTOS DE OPERACION','6010','6140');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'RO','OTROS GASTOS Y/O PRODUCTOS','7010','7040');<br>";
			sql += "insert into tbl_cont_rubros<br>";
			sql += "values(default,'IP','CUENTAS DE ORDEN','8010','8990');<br>";
			out.println(sql);
			out.flush();
			
			FileReader file         = new FileReader("/usr/local/forseti/rec/cuentas_cont.csv");
            BufferedReader buff     = new BufferedReader(file);
            boolean eof             = false;
            Vector<String> plantilla = new Vector<String>();
			
            boolean MasterNom = (request.getParameter("N") != null) ? true : false;
            boolean MasterProd = (request.getParameter("P") != null) ? true : false;
           while(!eof)
            {
                String linea = buff.readLine();
                if(linea == null)
                {
                    eof = true;
                }
                else
                {
                	if(!JUtil.Elm(linea, 1).equals("1") && !JUtil.Elm(linea, 1).equals("2")) //No es linea de catalogo
                		continue;
                	if(!JUtil.Elm(linea, 3).equals("1")) // Es linea de catalogo pero no aplica para forseti.
                		continue;
                	if(JUtil.Elm(linea, 4).equals("P") && !MasterProd) //No se ha especificado cuentas de produccion
                		continue;
                	if(JUtil.Elm(linea, 4).equals("N") && !MasterNom) //No se ha especificado cuentas de produccion
                		continue;
                	plantilla.addElement(linea);
                }
            }
            buff.close();
            file.close();
            
            for(int i = 0; i < plantilla.size(); i++)
			{
				String linea = plantilla.get(i);
				String agrupador;
				int ind = JUtil.Elm(linea, 2).indexOf('.');
				if(ind != -1)
				{
					if(JUtil.Elm(linea, 2).substring(ind+1).length() == 1)
						agrupador = JUtil.Elm(linea, 2) + "0";
					else
						agrupador = JUtil.Elm(linea, 2);
				}
				else
					agrupador = JUtil.Elm(linea, 2);
				String descripcion;
				if(JUtil.Elm(linea, 8).length() > 50)
					descripcion = JUtil.Elm(linea, 8).substring(0, 49);
				else
					descripcion = JUtil.Elm(linea, 8);
				sql = "INSERT INTO TBL_CONT_CATALOGO\n";
				sql += "VALUES('" + JUtil.Elm(linea, 10) + "', '" + JUtil.Elm(linea, 12) + "','" + descripcion + "', '0.0', 'A','" + agrupador + "','" + JUtil.Elm(linea, 11) + "');\n";
				out.println(sql);
			}
            
            out.println("<br><br>");
            
            Vector<String>gastos = new Vector<String>();
            Vector<String>sqlgastos = new Vector<String>();
             
            for(int i = 0; i < plantilla.size(); i++)
			{
				String idgastos = JUtil.Elm(plantilla.get(i), 6);
				String descripciones = JUtil.Elm(plantilla.get(i), 7);
				String cuenta = JUtil.Elm(plantilla.get(i), 10);
				if(idgastos.equals("X"))
					continue;
								
				String [] idgasto = idgastos.split("\\;");
				String [] descripcion = descripciones.split("\\;");
				for(int j = 0; j < idgasto.length; j++)
				{
					String gastoclv = JUtil.Elm(idgasto[j],1,":");
					boolean existe = false;
					int indexist = 0;
					for(int k = 0; k < gastos.size(); k++)
					{
						if(gastoclv.equals(gastos.get(k)))
						{
							existe = true;
							indexist = k;
							break;
						}
					}
						
					if(!existe)
					{
						//out.println("Agregando a gastos: " + gastoclv + " " + descripcion[j] + "<br>");
						gastos.add(gastoclv);
						String lin = 	JUtil.Elm(idgasto[j],1,":") + "|" + 
										JUtil.Elm(idgasto[j],2,":") + "|" + 
										JUtil.Elm(idgasto[j],3,":") + "|" + 
										JUtil.Elm(idgasto[j],4,":") + "|" + 
										JUtil.Elm(idgasto[j],5,":") + "|" + 
										JUtil.Elm(idgasto[j],6,":") + "|" + 
										descripcion[j] + "|" +
										cuenta;
						//out.println(lin + "<br>");
						sqlgastos.add(lin);
					}
					else
					{
						//out.println("Existia en gastos: " + gastoclv + " " + descripcion[j] + "<br>");
						String str = sqlgastos.get(indexist);
						str += ";" + cuenta;
						//out.println(str + "<br>");
						sqlgastos.set(indexist, str);
					}
				}
			}
            
            sql =	"INSERT INTO tbl_invserv_lineas(id_linea, id_invserv, descripcion)<br>";
            sql +=	"VALUES ('GAS', 'G', 'Gastos Generales');<br>";
            out.println(sql);
            
            for(int i = 0; i < sqlgastos.size(); i++)
            {
            	String [] gasto = sqlgastos.get(i).split("\\|");
            	sql =  "INSERT INTO TBL_INVSERV_INVENTARIOS<br>";
        		sql += "VALUES('" + gasto[0] + "','G',null,'" + gasto[6] + "','GAS','0.00','0.000','','0.000','0.000','0','0','0',";
        		sql += "'A','','','1.000000','0.000','0.000','0.000','0.000','0','0.0000','0.0000','" + gasto[2] + "','" + gasto[5]+ "',";
        		sql += "'" + gasto[1] + "','0.000','1','0.000','0.0000','0.0000','1','0.0000','0.0000','0.0000','0.0000',";
        		sql += "'0.0000','0.0000','','','','','','','0',";
        		sql += "'0','0','0.000','2','" + gasto[0] + "','0.0000','0.0000','" + gasto[3] + "','" + gasto[4] + "');<br>";
        		
        		out.println(sql);
        		int tot = JUtil.ElmNum(gasto[7], ";");
        		if(tot == 1)
        		{
        			sql = "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + gasto[7] + "','100.00');<br>";
        		}
        		else if(tot == 2)
        		{
           			sql =  "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],1,";") + "','50.00');<br>";
        			sql += "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],2,";") + "','50.00');<br>";
        		}
        		else if(tot == 3)
        		{
           			sql =  "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],1,";") + "','33.33');<br>";
        			sql += "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],2,";") + "','33.33');<br>";
        			sql += "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],3,";") + "','33.34');<br>";
        		}
        		else
        		{
        			sql =  "INSERT INTO TBL_INVSERV_GASTOS_PORCENTAJES<br>";
        			sql += "VALUES('" + gasto[0] + "','" + JUtil.Elm(gasto[7],1,";") + "','100.00');<br>";
        		}
        		
        		out.println("Cuentas: " + tot + "<br>");
        		out.println(sql);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace(out);
        }

		
	}
*/
}
