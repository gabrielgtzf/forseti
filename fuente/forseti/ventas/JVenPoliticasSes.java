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
package forseti.ventas;
import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegsObjsOtr;
import forseti.JUtil;
import forseti.sets.JPublicContMonedasSetV2;
import forseti.sets.JPublicInvServInvCatalogSetV2;

public class JVenPoliticasSes extends JSesionRegsObjsOtr
{

  private int m_ClaveClient;
  private int m_NumeroClient;
  private short m_ID_EntidadVenta;
  private String m_EntidadVenta;
  private String m_NombreCliente;
  
  
  public JVenPoliticasSes()
  {
	  m_ClaveClient = 0;
	  m_NumeroClient = 0;
	  m_ID_EntidadVenta = 0;
	  m_EntidadVenta = "";
	  m_NombreCliente = "";
  
  }

  public void setParametros(int ClaveClient, int NumeroClient, short ID_EntidadVenta, String EntidadVenta, String NombreCliente)
  {
	  m_ClaveClient = ClaveClient;
	  m_NumeroClient = NumeroClient;
	  m_ID_EntidadVenta = ID_EntidadVenta;
	  m_EntidadVenta = EntidadVenta;
	  m_NombreCliente = NombreCliente;
  }

  public JVenPoliticasSesOtr getOtro(int ind)
  {
    return (JVenPoliticasSesOtr)m_Otros.elementAt(ind);
  }
  
  public JVenPoliticasSesPart getPartida(int ind)
  {
    return (JVenPoliticasSesPart)m_Partidas.elementAt(ind);
  }
  
  public JVenPoliticasSesObjs getObjeto(int ind)
  {
    return (JVenPoliticasSesObjs)m_Objetos.elementAt(ind);
  }

   
  @SuppressWarnings("unchecked")
  public short agregaPartidaOtr(HttpServletRequest request, String ID_Prod, float Descuento, float Descuento2, 
		  float Descuento3, float Descuento4, float Descuento5, StringBuffer mensaje)
  {
	    short res = -1;
	    
		JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
		set.m_Where = "(Clave = '" + JUtil.p(ID_Prod) + "' or Codigo = '" + JUtil.p(ID_Prod) + "') and Status = 'V'";
		set.Open();
		  
		if(set.getNumRows() > 0)
	    {
			 if(existeEnListaOtr(ID_Prod))
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: El producto ya existe en la lista, No se agregó la partida");
			 }
			 else
			 {			
				 JVenPoliticasSesOtr otr = new JVenPoliticasSesOtr(set.getAbsRow(0).getID_UnidadSalida(), 
		    		ID_Prod, set.getAbsRow(0).getDescripcion(), Descuento, Descuento2, Descuento3, Descuento4, Descuento5);
				 m_Otros.addElement(otr);
			 } 
	    }
	    else
	    {
	        res = 3;
	        mensaje.append("ERROR: No se encontró el producto especificado. Puede ser que ya esté descontinuado");
	    }
	
	    return res;

  }  
  
   
  @SuppressWarnings("unchecked")
  public short agregaPartida(HttpServletRequest request, String ID_Prod, float Precio, byte ID_Moneda, StringBuffer mensaje)
  {
	  
	    short res = -1;
	
		JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
		set.m_Where = "(Clave = '" + JUtil.p(ID_Prod) + "' or Codigo = '" + JUtil.p(ID_Prod) + "') and Status = 'V'";
		set.Open();
		  
		if(set.getNumRows() > 0)
	    {
			 if(existeEnLista(ID_Prod))
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: El producto ya existe en la lista, No se agregó la partida");
			 }
			 else
			 {			
				 JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
				 mon.m_Where = "Clave = '" + ID_Moneda + "'";
				 mon.Open();
				 JVenPoliticasSesPart part = new JVenPoliticasSesPart(set.getAbsRow(0).getID_UnidadSalida(), 
		    		ID_Prod, set.getAbsRow(0).getDescripcion(), Precio, ID_Moneda, mon.getAbsRow(0).getMoneda());
				 m_Partidas.addElement(part);
			 } 
	    }
	    else
	    {
	        res = 3;
	        mensaje.append("ERROR: No se encontró el producto especificado. Puede ser que ya esté descontinuado");
	    }
	
	    return res;

  }
 
  public boolean existeEnListaOtr(String idprod)
  {
	boolean res = false;  
  
	for(int i = 0; i < m_Otros.size(); i++)
	{
	  	JVenPoliticasSesOtr otr = (JVenPoliticasSesOtr) m_Otros.elementAt(i);
	  	String clave = otr.getID_Prod();
	  	
	  	if(clave.compareToIgnoreCase(idprod) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }  
  
  public boolean existeEnLista(String idprod)
  {
	  
	boolean res = false;  
  
	for(int i = 0; i < m_Partidas.size(); i++)
	{
	  	JVenPoliticasSesPart part = (JVenPoliticasSesPart) m_Partidas.elementAt(i);
	  	String clave = part.getID_Prod();
	  	
	  	if(clave.compareToIgnoreCase(idprod) == 0)
	  	{
	  		res = true;
	  		break;
	  	}
	}
	
	return res;
	
  }

   
    @SuppressWarnings("unchecked")
  public void agregaPartidaOtr(String Unidad, String ID_Prod, String ID_ProdNombre, float Descuento, float Descuento2, float Descuento3, float Descuento4, float Descuento5)
  {
    JVenPoliticasSesOtr otr = new JVenPoliticasSesOtr(Unidad, ID_Prod, ID_ProdNombre, Descuento, Descuento2, Descuento3, Descuento4, Descuento5);
    m_Otros.addElement(otr);
    
  }
 
   
    @SuppressWarnings("unchecked")
  public void agregaPartida(String Unidad, String ID_Prod, String ID_ProdNombre, float Precio, byte ID_Moneda, String Moneda)
  {
    JVenPoliticasSesPart part = new JVenPoliticasSesPart(Unidad, ID_Prod, ID_ProdNombre, Precio, ID_Moneda, Moneda);
    m_Partidas.addElement(part);
    
  }

   
  @SuppressWarnings("unchecked")
  public void agregaObjeto(String ID_Tipo, String Clave, String Descripcion, String Linea, 
			String Unidad, String Status, float P1, float P2, float P3, float P4, float P5,   float PMin, float PMax, float PW, float POW,
				float PComp, byte ID_Moneda)
  {
	 JVenPoliticasSesObjs part = new JVenPoliticasSesObjs(ID_Tipo, Clave, Descripcion, Linea, 
				Unidad, Status, P1, P2, P3, P4, P5, PMin, PMax, PW, POW, PComp, ID_Moneda);
	 m_Objetos.addElement(part);

  }
  
  public short editaPartidaOtr(int indPartida, HttpServletRequest request, String ID_Prod, float Descuento, float Descuento2, float Descuento3, float Descuento4, float Descuento5, StringBuffer mensaje)
  {
    short res = -1;

    JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	set.m_Where = "(Clave = '" + JUtil.p(ID_Prod) + "' or Codigo = '" + JUtil.p(ID_Prod) + "') and Status = 'V'";
	set.Open();
	  
	if(set.getNumRows() > 0)
    {
	    JVenPoliticasSesOtr otr = (JVenPoliticasSesOtr) m_Otros.elementAt(indPartida);
	    otr.setPartida(set.getAbsRow(0).getID_UnidadSalida(), 
	    		ID_Prod, set.getAbsRow(0).getDescripcion(), Descuento, Descuento2, Descuento3, Descuento4, Descuento5);
	   
	      
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontró el producto especificado. Puede ser que ya esté descontinuado. Bórralo porque no se puede editar");
    }
    return res;

  } 
  
  public short editaPartida(int indPartida, HttpServletRequest request, String ID_Prod, float Precio, byte ID_Moneda, StringBuffer mensaje)
  {
    short res = -1;

    JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	set.m_Where = "(Clave = '" + JUtil.p(ID_Prod) + "' or Codigo = '" + JUtil.p(ID_Prod) + "') and Status = 'V'";
	set.Open();
	  
	if(set.getNumRows() > 0)
    {
	    JPublicContMonedasSetV2 mon = new JPublicContMonedasSetV2(request);
	    mon.m_Where = "Clave = '" + ID_Moneda + "'";
	    mon.Open();
	    JVenPoliticasSesPart part = (JVenPoliticasSesPart) m_Partidas.elementAt(indPartida);
	    part.setPartida(set.getAbsRow(0).getID_UnidadSalida(), 
	    		ID_Prod, set.getAbsRow(0).getDescripcion(), Precio, ID_Moneda, mon.getAbsRow(0).getMoneda());
	   
	      
    }
    else
    {
        res = 3;
        mensaje.append("ERROR: No se encontró el producto especificado. Puede ser que ya esté descontinuado. Bórralo porque no se puede editar");
    }
    return res;

  }
 
  public void resetear()
  {
	  m_ClaveClient = 0;
	  m_NumeroClient = 0;
	  m_ID_EntidadVenta = 0;
	  m_EntidadVenta = "";
	  m_NombreCliente = "";
      
	  super.resetear();
      
  }

  public void borraPartidaOtr(int indPartida)
  {
    super.borraPartidaOtr(indPartida);
  }
  
  public void borraPartida(int indPartida)
  {
    super.borraPartida(indPartida);
  }

  public int getClaveClient() 
  {
	return m_ClaveClient;
  }

  public void setClaveClient(int ClaveClient) 
  {
	m_ClaveClient = ClaveClient;
  }

  public String getEntidadVenta() 
  {
	return m_EntidadVenta;
  }

  public void set_EntidadVenta(String EntidadVenta) 
  {
	m_EntidadVenta = EntidadVenta;
  }

  public short getID_EntidadVenta() 
  {
	return m_ID_EntidadVenta;
  }

  public void setID_EntidadVenta(short ID_EntidadVenta) 
  {
	m_ID_EntidadVenta = ID_EntidadVenta;
  }

  public String getNombreCliente() 
  {
	return m_NombreCliente;
  }

  public void setNombreCliente(String NombreCliente) 
  {
	m_NombreCliente = NombreCliente;
  }

  public int getNumeroClient() 
  {
	return m_NumeroClient;
  }

  public void setNumeroClient(int NumeroClient) 
  {
	m_NumeroClient = NumeroClient;
  }

}
