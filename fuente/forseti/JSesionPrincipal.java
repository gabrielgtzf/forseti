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

/**
 * <p>T�tulo: </p>
 * <p>Descripci�n: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.*;

import forseti.sets.JCRMUsers;
import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JUsuariosSubmoduloReportes;

public class JSesionPrincipal implements HttpSessionBindingListener
{
  private boolean m_bMobile;
  private boolean m_Registrado;
  private short m_ID_Mensaje; // 0 Informacion (sin icono), 1 Alerta (icono en amarillo), 2 Pregunta (icono en azul), 3 Info de Errores (icono en rojo)
  private String m_Mensaje;
  private String m_ID_Usuario;
  private UsuarioCRM m_UsuarioCRM;
  private String m_NombreUsuario;
  private String m_NombreCompania;
  private String m_BDCompania;
  private int m_IntentosFallidos;
  
  private byte m_NivelCC;
  private JUsuariosSubmoduloReportes m_PermisosReportes;
  
  private String m_BD;
  private String m_BDUser;
  private String m_BDPassword;
  
  private Properties m_Permisos;
  private Properties m_ConstantesCC;
  private Properties m_Enteros;

  
  //Este es el vector que almacena los objetos de sesion para cada módulo
  private Vector<JObjSes> m_VectorSes;
   
  public void setPermiso(String idPermiso, String valor)
  {
	  m_Permisos.setProperty(idPermiso, valor);
  }
  
  public boolean getPermiso(String idPermiso)
  {
	  if(m_Permisos.getProperty(idPermiso, "false").equals("true"))
		return true;
	  else
		return false;
	
  }
  
  public void setPermisoDeRol(String idPermiso, String valor)
  {
	  //System.out.println(idPermiso + " " + valor);
	  if(m_Permisos.getProperty(idPermiso).equals("true"))
		  return; //Ya es verdadero, simplemente regresa
	  else if(valor.equals("true"))
		  m_Permisos.setProperty(idPermiso, valor);
  }
  
  public void setConstanteCC(String idClave, String valor)
  {
    m_ConstantesCC.setProperty(idClave, valor);
  }
  
  public String getConstanteCC(String idClave)
  {
    return m_ConstantesCC.getProperty(idClave);
  }

  public void setEntero(String idEntero, String valor)
  {
    m_Enteros.setProperty(idEntero, valor);
  }

  public int getEntero(String idEntero)
  {
    return Integer.parseInt(m_Enteros.getProperty(idEntero));
  }

  public JSesionPrincipal(boolean Mobile)
  {
	m_bMobile = Mobile;  
	m_IntentosFallidos = 0;
    m_Registrado = false;
    m_ID_Mensaje = -1; // 0 Informacion (sin icono), 0 (Ok icono en verde) 1 Alerta (icono en amarillo), 2 Pregunta (icono en azul), 3 Info de Errores (icono en rojo)
    m_Mensaje = "";
    m_BD = "";
    m_BDUser = "";
    m_BDPassword = "";
    
    m_ID_Usuario = "";
    m_UsuarioCRM = new UsuarioCRM();
    m_Permisos = new Properties();
    m_Enteros = new Properties();
    m_ConstantesCC = new Properties();
    m_NivelCC = 2;
    
    m_VectorSes = new Vector<JObjSes>();
  
  }
  
  public void valueBound(HttpSessionBindingEvent arg0) 
  {
	//Apéndice de método generado automáticamente
	//System.out.println("Value Bound: " + arg0.getSession().getId() );	
  }
	
  public void valueUnbound(HttpSessionBindingEvent arg0) 
  {
	//Apéndice de método generado automáticamente
	Calendar fecha = GregorianCalendar.getInstance();          
	String sesion = JUtil.obtFechaHoraSQL(fecha);
	System.out.println("Sesion destruida: " + arg0.getSession().getId() + " Fecha: " + sesion);
     
	String SQL = "UPDATE TBL_REGISTROS\nSET Status = 'I', FechaHasta = '" + sesion + "'\nWHERE ID_Sesion = '" + JUtil.q(arg0.getSession().getId()) + "'";
		
	try
	{
		Connection con = JAccesoBD.getConexion();
		Statement s    = con.createStatement();
		s.executeUpdate(SQL);
		s.close();
		JAccesoBD.liberarConexion(con);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
		throw new RuntimeException(e.toString());
	}
	
  }
  
  public void intentoFallido()
  {
	  m_IntentosFallidos += 1;
  }
  
  public int getIntentosFallidos()
  {
	  return m_IntentosFallidos;
  }
  //////////////////////////////////////////////////
  // Trabajamos con Vector
  public void EstablecerSAF(String Img, String ID_Modulo) 
  {
	  JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(null);
	  set.ConCat(true);
	  set.m_Where = "ID_Permiso = '" + JUtil.p(ID_Modulo) + "'";
	  set.Open();
	  
	  JObjSes os = new JObjSes();
	  os.setID_Modulo(ID_Modulo);
	  os.setModulo(set.getAbsRow(0).getModulo());
	  os.setSesionPropSQL(new JSesionPropSQL("SAF", ID_Modulo, Img, JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",1), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",2), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",3), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",4), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",5),m_bMobile));
	  
	  m_VectorSes.addElement(os);
	  
  }
  
  public void EstablecerSAF(String Img, String ID_Modulo, String ID_Permiso) 
  {
	  JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(null);
	  set.ConCat(true);
	  set.m_Where = "ID_Permiso = '" + JUtil.p(ID_Permiso) + "'";
	  set.Open();
	  
	  JObjSes os = new JObjSes();
	  os.setID_Modulo(ID_Modulo);
	  os.setModulo(set.getAbsRow(0).getModulo());
	  os.setSesionPropSQL(new JSesionPropSQL("SAF", ID_Modulo, Img, JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",1), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",2), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",3), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",4), JUtil.Msj("SAF",ID_Modulo,"VISTA","TITULO",5),m_bMobile));
	  
	  m_VectorSes.addElement(os);
	  
  }
  
  public void EstablecerCEF(HttpServletRequest request, String Img, String ID_Modulo) 
  {
	  //TS: 0 SAF, 1 CEF
	  JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(request);
	  set.m_Where = "ID_Permiso = '" + JUtil.p(ID_Modulo) + "'";
	  set.Open();
		
	  JObjSes os = new JObjSes();
	  os.setID_Modulo(ID_Modulo);
	  os.setModulo(set.getAbsRow(0).getModulo());
	  os.setSesionPropSQL(new JSesionPropSQL("CEF", ID_Modulo, Img, JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",1), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",2), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",3), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",4), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",5),m_bMobile));
	  
	  m_VectorSes.addElement(os);
	  
  }
  
  public void EstablecerCEF_NO_PERM(HttpServletRequest request, String Img, String ID_Modulo, String Modulo) 
  {
	  //TS: 0 SAF, 1 CEF
	  JObjSes os = new JObjSes();
	  os.setID_Modulo(ID_Modulo);
	  os.setModulo(Modulo);
	  os.setSesionPropSQL(new JSesionPropSQL("CEF", ID_Modulo, Img, JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",1), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",2), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",3), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",4), JUtil.Msj("CEF",ID_Modulo,"VISTA","TITULO",5),m_bMobile));
	  
	  m_VectorSes.addElement(os);
	  
  }
  public boolean getEst(String ID_Modulo)
  {
	  boolean res = false;
	  for(int i = 0; i < m_VectorSes.size(); i++)
	  {
		  JObjSes os = (JObjSes)m_VectorSes.elementAt(i);
		  if(os.getID_Modulo().equals(ID_Modulo))
		  {
			  res = true;
			  break;
		  }
	  }
	  return res;
  }
  public JSesionPropSQL getSesion(String ID_Modulo)
  {
	  JSesionPropSQL res = null;
	  for(int i = 0; i < m_VectorSes.size(); i++)
	  {
		  JObjSes os = (JObjSes)m_VectorSes.elementAt(i);
		  if(os.getID_Modulo().equals(ID_Modulo))
		  {
			  res = os.getSesionPropSQL();
			  break;
		  }
	  }
	  return res;
  }
 
  public void setPermisosReportes(JUsuariosSubmoduloReportes PermisosReportes)
  {
    m_PermisosReportes = PermisosReportes;
  }
  
  public boolean getPermisoReporte(int idReporte)
  {
	//System.out.println("PERMISO REPORTE: " + idReporte);
    for(int i = 0; i < m_PermisosReportes.getNumRows(); i++)
    {
      //System.out.println("PERMISO: " + m_PermisosReportes.getAbsRow(i).getID_Report());
      if(m_PermisosReportes.getAbsRow(i).getID_Report() == idReporte)
        return true;

    }
    return false;
  }

  public byte getNivelCC()
  {
    return m_NivelCC;
  }

  public void setNivelCC(byte NivelCC)
   {
     m_NivelCC = NivelCC;
   }

  public void setID_Usuario(String usuario, String apl, HttpServletRequest request)
  {
	  m_ID_Usuario = usuario;
	 
	  if(apl.equals("CEF") && usuario != "cef-su")
	  {
		  JCRMUsers set = new JCRMUsers(request);
		  set.m_Where = "fsi_user_id = '" + usuario + "'";
		  set.Open();
		  if(set.getNumRows() == 1)
		  {
			  m_UsuarioCRM.gu_user = set.getAbsRow(0).getGU_user();
			  m_UsuarioCRM.gu_workarea = set.getAbsRow(0).getGU_workarea();
		  }
	  }
  }
  
  public void setConBD(String ConBD, String Usuario, String Password)
  {
    m_BD = ConBD;
    m_BDUser = Usuario;
    m_BDPassword = Password;
  }

  public void setRegistrado(boolean Registrado)
  {
    m_Registrado = Registrado;
  }

  public void setID_Mensaje(short ID_Mensaje, String Mensaje)
  {
    m_ID_Mensaje = ID_Mensaje;
    String mens1 = "<tr><td><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\" bgcolor=\""; // mensaje antes del color
    String mens2 = "\"><tr><td align=\"right\" valign=\"top\"><img src=\""; // mensaje antes de imagen
    String mens3 = "\"></td><td class=\""; // mensaje antes de clase que define el color del texto
    String mens4 = "\"align=\"left\">"; //Mensaje antes de definir el texto principal (variable GBL_Mensaje)
    String mens5 = "</td></tr></table></td></tr>"; // Final de estructura de mensaje

    switch(m_ID_Mensaje)
    {
      case 0: // Mensaje ok en verde e icono de Afirmativo
         m_Mensaje = mens1 + "#00CC00" + mens2 + "../imgfsi/icono-ok.gif" + mens3 + "titChico" + mens4 + Mensaje + mens5;
         break;
      case 1: // Alerta en amarillo e icono de alerta
        m_Mensaje = mens1 + "#FF9900" + mens2 + "../imgfsi/icono-alerta.gif" + mens3 + "titChico" + mens4 + Mensaje + mens5;
        break;
      case 2: // Pregunta en azul e icono de pregunta
        m_Mensaje = mens1 + "#3366CC" + mens2 + "../imgfsi/icono-pregunta.gif" + mens3 + "titChico" + mens4 + Mensaje + mens5;
        break;
      case 3: // Error en rojo e icono de error
        m_Mensaje = mens1 + "#CC0000" + mens2 + "../imgfsi/icono-error.gif" + mens3 + "titChico" + mens4 + Mensaje + mens5;
        break;
      default: // Informacion (sin icono, sin mensaje)
        m_Mensaje = "";
        break;
    }

  }

  public String getID_Usuario()
  {
    return m_ID_Usuario;
  }
  
  public UsuarioCRM getUsuarioCRM()
  {
    return m_UsuarioCRM;
  }
  
  public String getConBD()
  {
    return m_BD;
  }
  
  public String getUser()
  {
    return m_BDUser;
  }
  
  public String getPassword()
  {
    return m_BDPassword;
  }

  public short getID_Mensaje()
  {
    return m_ID_Mensaje;
  }

  public String getMensaje()
  {
    return m_Mensaje;
  }

  public boolean getRegistrado()
  {
    return m_Registrado;
  }
	
  public String getNombreCompania() 
  {
	return m_NombreCompania;
  }
	
  public void setNombreCompania(String NombreCompania) 
  {
	m_NombreCompania = NombreCompania;
  }
  
  public String getBDCompania() 
  {
	return m_BDCompania;
  }
	
  public void setBDCompania(String BDCompania) 
  {
	m_BDCompania = BDCompania;
  }

  public String getNombreUsuario() 
  {
	return m_NombreUsuario;
  }
	
  public void setNombreUsuario(String NombreUsuario) 
  {
	m_NombreUsuario = NombreUsuario;
  }
  
  public boolean isMobile() 
  {
	return m_bMobile; 
  }
  
  public class UsuarioCRM
  {
	  public String gu_user;
	  public String gu_workarea;
	  
	  UsuarioCRM()
	  {
		  gu_user = "cef-su";
		  gu_workarea = "*";
	  }

	  
  }
  
}
