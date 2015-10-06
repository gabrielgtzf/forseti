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

import forseti.sets.JAdmVariablesSet;

public class JSesionPropSQL
{
  private String m_Apl;	
  private String m_ID_Modulo;	
  private String m_Departamento;
  private String m_Img;
  private String m_Modulo;
  private String m_Orden;
  private String m_Ordenado;
  private String m_Entidad;
  private String m_EntidadTit;
  private String m_Tiempo;
  private String m_TiempoTit;
  private String m_Status;
  private String m_StatusTit;
  private String m_Especial;
  private String m_PanelEntidad;
  private String m_PanelTiempo;
  private String m_PanelStatus;
  private String m_OrdenEtq;
  private String m_OrdenTit;
  private String m_ProcesoTit;
  private boolean m_bMobile;
  private String m_URLAyuda;
  
  public JSesionPropSQL()
  {
	m_Apl = "";
	m_ID_Modulo = "";  
	m_Departamento = "";
	m_Img = "";
	m_Modulo = "";
    m_Orden = "";
    m_Ordenado = "";
    m_OrdenEtq = "";
    m_Especial = "";
    m_PanelEntidad = "ENTIDAD";
    m_PanelTiempo = "RANGO";
    m_PanelStatus = "STATUS";
    m_OrdenTit = JUtil.Msj("GLB","GLB","GLB","ORDEN");
    m_ProcesoTit = JUtil.Msj("GLB","GLB","GLB","PROCESO");
    JAdmVariablesSet var = new JAdmVariablesSet(null);
    var.ConCat(true);
    var.m_Where = "ID_Variable = 'URLAYUDA'";
    var.Open();
    m_URLAyuda = var.getAbsRow(0).getVAlfanumerico();
    
  }

  public JSesionPropSQL(String Modulo)
  {
	m_Apl = "";
	m_ID_Modulo = "";  
	m_Departamento = "MODULO";  
	m_Modulo = Modulo;
	m_Img = "";
    m_Orden = "";
    m_Ordenado = "";
    m_OrdenEtq = "";
    m_Especial = "";
    m_PanelEntidad = "ENTIDAD";
    m_PanelTiempo = "RANGO";
    m_PanelStatus = "STATUS";
    m_OrdenTit = JUtil.Msj("GLB","GLB","GLB","ORDEN");
    m_ProcesoTit = JUtil.Msj("GLB","GLB","GLB","PROCESO");
    JAdmVariablesSet var = new JAdmVariablesSet(null);
    var.ConCat(true);
    var.m_Where = "ID_Variable = 'URLAYUDA'";
    var.Open();
    m_URLAyuda = var.getAbsRow(0).getVAlfanumerico();
  }
  
  public JSesionPropSQL(String Modulo, String PanelEntidad, String PanelTiempo, String PanelStatus)
  {
	m_Apl = "";
	m_ID_Modulo = "";    
	m_Departamento = "MODULO";  
	m_Modulo = Modulo;
	m_Img = "";
	m_Orden = "";
    m_Ordenado = "";
    m_OrdenEtq = "";
    m_Especial = "";
    m_PanelEntidad = PanelEntidad;
    m_PanelTiempo = PanelTiempo;
    m_PanelStatus = PanelStatus;
    m_OrdenTit = JUtil.Msj("GLB","GLB","GLB","ORDEN");
    m_ProcesoTit = JUtil.Msj("GLB","GLB","GLB","PROCESO");
    JAdmVariablesSet var = new JAdmVariablesSet(null);
    var.ConCat(true);
    var.m_Where = "ID_Variable = 'URLAYUDA'";
    var.Open();
    m_URLAyuda = var.getAbsRow(0).getVAlfanumerico(); 
  }
  
  
  public JSesionPropSQL(String Apl, String ID_Modulo, String Img, String Departamento, String Modulo, String PanelEntidad, String PanelTiempo, String PanelStatus, boolean bMobile)
  {
	m_Apl = Apl;
	m_ID_Modulo = ID_Modulo;
	m_Departamento = Departamento;  
	m_Modulo = Modulo;
	m_Img = Img;
	m_Orden = "";
    m_Ordenado = "";
    m_OrdenEtq = "";
    m_Especial = "";
    m_PanelEntidad = PanelEntidad;
    m_PanelTiempo = PanelTiempo;
    m_PanelStatus = PanelStatus;
    m_OrdenTit = JUtil.Msj("GLB","GLB","GLB","ORDEN");
    m_ProcesoTit = JUtil.Msj("GLB","GLB","GLB","PROCESO");
    m_bMobile = bMobile;
    JAdmVariablesSet var = new JAdmVariablesSet(null);
    var.ConCat(true);
    var.m_Where = "ID_Variable = 'URLAYUDA'";
    var.Open();
    m_URLAyuda = var.getAbsRow(0).getVAlfanumerico();
  }
  
  public void setEspecial(String Especial)
  {
    // agrega la propiedad especial
    m_Especial = Especial;
  }
  
  public String getPanelEntidad() 
  {
	return m_PanelEntidad;
  }
  
  public void setPanelEntidad(String PanelEntidad) 
  {
	m_PanelEntidad = PanelEntidad;
  }
  
  public String getEntidadTit() 
  {
	return m_EntidadTit;
  }

  public String getPanelTiempo() 
  {
	return m_PanelTiempo;
  }

  public void setPanelTiempo(String PanelTiempo) 
  {
	m_PanelTiempo = PanelTiempo;
  }

  public String getTiempoTit() 
  {
	return m_TiempoTit;
  }

  public String getPanelStatus() 
  {
	return m_PanelStatus;
  }

  public void setPanelStatus(String PanelStatus) 
  {
	m_PanelStatus = PanelStatus;
  }

  public String getStatusTit() 
  {
	return m_StatusTit;
  }

  public String getEspecial()
  {
    return m_Especial;
  }

  public void setOrden(String etq, String col, boolean desc)
  {
    // agrega la propiedad de orden segun sea el caso
    m_Orden = col;
    m_Ordenado = (desc) ? "DESC" : "ASC";
    
    if(etq == null || etq.equals("") || etq.equals("null") || etq.equals("NULL"))
    	m_OrdenEtq = col;
    else
    	m_OrdenEtq = etq;
  }
  
  public void setOrden(String etq, String col)
  {
    // agrega la propiedad de orden segun sea el caso
    if( m_Orden.equals(col) )
    {
      if(m_Ordenado.equals("ASC"))
        m_Ordenado = "DESC";
      else
        m_Ordenado = "ASC";
    }
    else
    {
      m_Orden = col;
      m_Ordenado = "DESC";
    }
    
    if(etq == null || etq.equals("") || etq.equals("null") || etq.equals("NULL"))
    	m_OrdenEtq = col;
    else
    	m_OrdenEtq = etq;
  }
  
  public void setParametros(String Entidad, String Tiempo, String Status, String EntidadTit, String TiempoTit, String StatusTit)
  {
    m_Entidad = Entidad;
    m_Tiempo = Tiempo;
    m_Status = Status;
    m_EntidadTit = EntidadTit;
    m_TiempoTit = TiempoTit;
    m_StatusTit = StatusTit;
  }

  public String getModulo() 
  {
	return m_Modulo;
  }

  public String getDepartamento() 
  {
	return m_Departamento;
  }
  
  public void setModulo(String Modulo) 
  {
	m_Modulo = Modulo;
  }
  
  public void setEntidad(String Entidad, String EntidadTit)
  {
    m_Entidad = Entidad;
    m_EntidadTit = EntidadTit;
  }
  
  public void setEntidad(String Entidad)
  {
    m_Entidad = Entidad;
  }

  public void setTiempo(String Tiempo, String TiempoTit)
  {
    m_Tiempo = Tiempo;
    m_TiempoTit = TiempoTit;
  }

  public String getTiempo()
  {
    return m_Tiempo;
  }

  public String getEntidad()
  {
    return m_Entidad;
  }

  public String getStatus()
  {
    return m_Status;
  }

  public void setStatus(String Status, String StatusTit)
  {
    m_Status = Status;
    m_StatusTit = StatusTit;
  }

  public String generarWhere()
  {
    String res = m_Tiempo;
    if(!m_Entidad.equals(""))
    {
      if(!res.equals(""))
        res += " AND " + m_Entidad;
      else
        res += m_Entidad;
    }
    if(!m_Status.equals(""))
    {
      if(!res.equals(""))
          res += " AND " + m_Status;
        else
          res += m_Status;
    }

    return res;
  }

  public String generarTitulo()
  {
	  
	String orden = "", colmodulo = "", orddescgif = "", ordascgif = "", colenc = "", imgayuda = "";
	
	if(!m_Apl.equals(""))
	{
		if(m_Apl.equals("SAF"))
		{
			colenc += "txtChicoNar";
			colmodulo += "titChicoNar";
			orddescgif = "orden_desc.gif";
			ordascgif = "orden_asc.gif";
			imgayuda += "ayudaesp.png";
		}
		else
		{
			colenc += "txtChicoAzc";
			colmodulo += "titChicoAzc";
			orddescgif = "orden_desc_azc.gif";
			ordascgif = "orden_asc_azc.gif";
			imgayuda += "ayudaespcef.png";
		}
	}
	
	if(!m_Orden.equals(""))
	{
	   if(!m_bMobile)
	   {
		   if(m_Ordenado.equals("DESC"))
			   orden += "<img src=\"../imgfsi/" + orddescgif + "\" width=\"14\" height=\"12\">" + m_OrdenEtq + "<img src=\"../imgfsi/" + orddescgif + "\" width=\"14\" height=\"12\">";
		   else
			   orden += "<img src=\"../imgfsi/" + ordascgif + "\" width=\"14\" height=\"12\">" + m_OrdenEtq + "<img src=\"../imgfsi/" + ordascgif + "\" width=\"14\" height=\"12\">";
	   }
	   else
	   {
		   if(m_Ordenado.equals("DESC"))
			   orden += "« " + m_OrdenEtq;
		   else
			   orden += m_OrdenEtq + " »";
	   }
	}
		
	String res = "";
	if(!m_bMobile)
	{
		res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">\n";
		res += "       <tr>\n";
		res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + m_Img + "\"></td>\n";	
	    res += "  		<td align=\"center\" valign=\"middle\" bgcolor=\"#343434\" class=\"titChico\">" + m_Departamento + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" valign=\"middle\" bgcolor=\"#444444\" class=\"titChico\">" + m_PanelEntidad + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" valign=\"middle\" bgcolor=\"#454545\" class=\"titChico\">" + m_PanelTiempo + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" valign=\"middle\" bgcolor=\"#555555\" class=\"titChico\">" + m_PanelStatus + "</td>\n";
	    res += "		<td width=\"20%\" align=\"center\" valign=\"middle\" bgcolor=\"#545454\" class=\"titChico\">" + m_OrdenTit + "</td>\n";
	    res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"top\"><a href=\"" + m_URLAyuda + m_ID_Modulo + ".html\" target=\"_blank\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + imgayuda + "\" border=\"0\"></a></td>\n";	
		res += "	   </tr>\n";
	    res += "	   <tr>\n";
	    res += "		<td align=\"center\" valign=\"middle\" bgcolor=\"#FFFFFF\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + m_EntidadTit + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + m_TiempoTit + "</td>\n";
	    res += "		<td width=\"15%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + m_StatusTit + "</td>\n";
	    res += "		<td width=\"20%\" align=\"center\" valign=\"middle\" class=\"cpoBco\">" + orden + "</td>\n";
	    res += "	   </tr>\n";
	    res += "	  </table>\n";
	}
	else
	{
		res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">\n";
		res += "       <tr>\n";
		res += "		<td width=\"32\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"32\" height=\"32\" src=\"../imgfsi/" + m_Img + "\"/></td>\n";	
		res += "		<td width=\"60%\" align=\"center\" valign=\"middle\" class=\"titChico\">" + m_Departamento + "</td>\n";
	    res += "		<td width=\"40%\" align=\"center\" valign=\"middle\" class=\"titChico\">" + m_OrdenTit + "</td>\n";
	    res += "	   </tr>\n";
	    res += "	   <tr>\n";
	    res += "		<td align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
	    res += "		<td align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + orden + "</td>\n";
	    res += "	   </tr>\n";
	    res += "</table><table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">\n";
	    res += "       <tr>\n";
		res += "		<td width=\"33%\" align=\"center\" valign=\"middle\" bgcolor=\"#444444\" class=\"titChico\">" + m_PanelEntidad + "</td>\n";
	    res += "		<td width=\"33%\" align=\"center\" valign=\"middle\" bgcolor=\"#454545\" class=\"titChico\">" + m_PanelTiempo + "</td>\n";
	    res += "		<td width=\"33%\" align=\"center\" valign=\"middle\" bgcolor=\"#555555\" class=\"titChico\">" + m_PanelStatus + "</td>\n";
	    res += "	   </tr>\n";
		res += "       <tr>\n";
		res += "		<td width=\"33%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + (m_EntidadTit.equals("") ? "&nbsp;" : m_EntidadTit) + "</td>\n";
	    res += "		<td width=\"33%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + (m_TiempoTit.equals("") ? "&nbsp;" : m_TiempoTit) + "</td>\n";
	    res += "		<td width=\"33%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + (m_StatusTit.equals("") ? "&nbsp;" : m_StatusTit) + "</td>\n";
	    res += "	   </tr>\n";
		res += "</table>";
	}
	
    return res;
  }
  
  
  public String generarTitulo(String proceso)
  {
		String colmodulo = "", colenc = "", imgayuda = "";
		
		if(!m_Apl.equals(""))
		{
			if(m_Apl.equals("SAF"))
			{
				colenc += "txtChicoNar";
				colmodulo += "titChicoNar";
				imgayuda += "ayudaesp.png";
			}
			else
			{
				colenc += "txtChicoAzc";
				colmodulo += "titChicoAzc";
				imgayuda += "ayudaespcef.png";
			}
		}
		
		String res = "";
		
		if(!m_bMobile)
		{
			res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" bordercolor=\"#333333\">\n";
			res += "       <tr>\n";
			res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + m_Img + "\"></td>\n";	
			res += "  		<td align=\"center\" valign=\"middle\" bgcolor=\"#343434\" class=\"titChico\">" + m_Departamento + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" bgcolor=\"#545454\" class=\"titChico\">" + m_ProcesoTit + "</td>\n";
			res += "	   </tr>\n";
			res += "	   <tr>\n";
			res += "		<td align=\"center\" valign=\"middle\" bgcolor=\"#FFFFFF\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + proceso + "</td>\n";
			res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"top\"><a href=\"" + m_URLAyuda + m_ID_Modulo + ".html#PROC\" target=\"_blank\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + imgayuda + "\" border=\"0\"></a></td>\n";	
			res += "	   </tr>\n";
			res += "	  </table>\n";
		}
		else
		{
			res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" bordercolor=\"#333333\">\n";
			res += "       <tr>\n";
			res += "		<td width=\"32\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"32\" height=\"32\" src=\"../imgfsi/" + m_Img + "\"/></td>\n";	
			res += "		<td align=\"center\" valign=\"middle\" class=\"titChico\">" + m_Departamento + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" class=\"titChico\">" + m_ProcesoTit + "</td>\n";
			res += "	   </tr>\n";
			res += "	   <tr>\n";
			res += "		<td align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + proceso + "</td>\n";
			res += "	   </tr>\n";
			res += "	  </table>\n";
		}
		return res;
  }

  public String generarTitulo(String proceso, String urldoc)
  {
		String colmodulo = "", colenc = "", imgayuda = "";
		
		if(!m_Apl.equals(""))
		{
			if(m_Apl.equals("SAF"))
			{
				colenc += "txtChicoNar";
				colmodulo += "titChicoNar";
				imgayuda += "ayudaesp.png";
			}
			else
			{
				colenc += "txtChicoAzc";
				colmodulo += "titChicoAzc";
				imgayuda += "ayudaespcef.png";
			}
		}
		
		String res = "";
		
		if(!m_bMobile)
		{
			res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" bordercolor=\"#333333\">\n";
			res += "       <tr>\n";
			res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + m_Img + "\"></td>\n";	
			res += "  		<td align=\"center\" valign=\"middle\" bgcolor=\"#343434\" class=\"titChico\">" + m_Departamento + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" bgcolor=\"#545454\" class=\"titChico\">" + m_ProcesoTit + "</td>\n";
			res += "	   </tr>\n";
			res += "	   <tr>\n";
			res += "		<td align=\"center\" valign=\"middle\" bgcolor=\"#FFFFFF\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" bgcolor=\"#FFFFFF\" valign=\"middle\" class=\"" + colenc + "\">" + proceso + "</td>\n";
			res += "		<td width=\"60\" rowspan=\"2\" align=\"center\" valign=\"top\"><a href=\"" + m_URLAyuda + m_ID_Modulo + ".html#PROC\" target=\"_blank\"><img width=\"48\" height=\"48\" src=\"../imgfsi/" + imgayuda + "\" border=\"0\"></a></td>\n";	
			res += "	   </tr>\n";
			res += "	  </table>\n";
		}
		else
		{ 
			res += "<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" bordercolor=\"#333333\">\n";
			res += "       <tr>\n";
			res += "		<td width=\"32\" rowspan=\"2\" align=\"center\" valign=\"middle\"><img width=\"32\" height=\"32\" src=\"../imgfsi/" + m_Img + "\"/></td>\n";	
			res += "		<td align=\"center\" valign=\"middle\" class=\"titChico\">" + m_Departamento + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" class=\"titChico\">" + m_ProcesoTit + "</td>\n";
			res += "	   </tr>\n";
			res += "	   <tr>\n";
			res += "		<td align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + m_Modulo + "</td>\n";
			res += "		<td width=\"50%\" align=\"center\" valign=\"middle\" class=\"" + colmodulo + "\">" + proceso + "</td>\n";
			res += "	   </tr>\n";
			res += "	  </table>\n";
		}
		return res;
  }

  public String generarOrderBy()
  {
    String res = "";

    if(!m_Orden.equals("") && !m_Ordenado.equals(""))
      res += m_Orden + " " + m_Ordenado;

    return res;

  }


}
