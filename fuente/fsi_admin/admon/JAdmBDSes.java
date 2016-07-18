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

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.mutable.MutableBoolean;

import forseti.JBDRegistradasSet;
import forseti.JUtil;

public class JAdmBDSes 
{
	// Datos generales de la empresa
	private int m_IDBD;
	private String m_Nombre;
	private String m_Compania;
	private String m_Direccion;
	private String m_Poblacion;
	private String m_CP;
	private String m_Mail;
	private String m_Web;
	private String m_Password;
	private String m_ConfPwd;
	
	private String m_Tab;
	private String m_TipoInstalacion;
	private String m_Usuario;
	private String m_UsuarioNombre;
	private int m_Mes;
	private int m_Ano;
	private String m_Etapa;
	
	private String m_Predefinida;
	private String m_BaseERP;
	private String m_BasePlantilla;
	
	private JSeleccionModulos m_SeleccionModulos;
	
	private Vector<JEntCajas> m_EntCajas;
	private Vector<JEntBancos> m_EntBancos;
	private Vector<JEntBodegasMP> m_EntBodegasMP;
	private Vector<JEntAlmacenesUten> m_EntAlmacenesUten;
	private Vector<JEntCompras> m_EntCompras;
	private Vector<JEntGastos> m_EntGastos;
	private Vector<JEntVentas> m_EntVentas;
	private Vector<JEntProduccion> m_EntProduccion;
	private Vector<JEntNomina> m_EntNomina;
	
	public Vector<JEntCajas> getEntCajas()
	{
		return m_EntCajas;
	}
	public JEntCajas getEntCajas(int index)
	{
		return m_EntCajas.elementAt(index);
	}
	
	public Vector<JEntBancos> getEntBancos()
	{
		return m_EntBancos;
	}
	public JEntBancos getEntBancos(int index)
	{
		return m_EntBancos.elementAt(index);
	}
	
	public Vector<JEntBodegasMP> getEntBodegasMP()
	{
		return m_EntBodegasMP;
	}
	public JEntBodegasMP getEntBodegasMP(int index)
	{
		return m_EntBodegasMP.elementAt(index);
	}
	
	public Vector<JEntAlmacenesUten> getEntAlmacenesUten()
	{
		return m_EntAlmacenesUten;
	}
	public JEntAlmacenesUten getEntAlmacenesUten(int index)
	{
		return m_EntAlmacenesUten.elementAt(index);
	}
	
	public Vector<JEntCompras> getEntCompras()
	{
		return m_EntCompras;
	}
	public JEntCompras getEntCompras(int index)
	{
		return m_EntCompras.elementAt(index);
	}
	
	public Vector<JEntGastos> getEntGastos()
	{
		return m_EntGastos;
	}
	public JEntGastos getEntGastos(int index)
	{
		return m_EntGastos.elementAt(index);
	}
	
	public Vector<JEntVentas> getEntVentas()
	{
		return m_EntVentas;
	}
	public JEntVentas getEntVentas(int index)
	{
		return m_EntVentas.elementAt(index);
	}
	
	public Vector<JEntProduccion> getEntProduccion()
	{
		return m_EntProduccion;
	}
	public JEntProduccion getEntProduccion(int index)
	{
		return m_EntProduccion.elementAt(index);
	}
	
	public Vector<JEntNomina> getEntNomina()
	{
		return m_EntNomina;
	}
	public JEntNomina getEntNomina(int index)
	{
		return m_EntNomina.elementAt(index);
	}
	
	
	public JAdmBDSes()
	{
		m_SeleccionModulos = new JSeleccionModulos();
		m_EntCajas = new Vector<JEntCajas>();
		m_EntBancos = new Vector<JEntBancos>();
		m_EntBodegasMP = new Vector<JEntBodegasMP>();
		m_EntAlmacenesUten = new Vector<JEntAlmacenesUten>();
		m_EntCompras = new Vector<JEntCompras>();
		m_EntGastos = new Vector<JEntGastos>();
		m_EntVentas = new Vector<JEntVentas>();
		m_EntProduccion = new Vector<JEntProduccion>();
		m_EntNomina = new Vector<JEntNomina>();
		
		resetear();
				
	}
	public void resetear()
	{
		Calendar fecha = GregorianCalendar.getInstance();
		
		m_IDBD = -1;
		m_Nombre = "";
		m_Compania = "";
		m_Direccion = "";
		m_Poblacion = "";
		m_CP = "";
		m_Mail = "";
		m_Web = "";
		m_Password = "";
		m_ConfPwd = "";
		
		m_Tab = "DATOS_GENERALES";
		m_TipoInstalacion = "PREDEFINIDA";
		m_Usuario = "Clave";
		m_UsuarioNombre = "Nombre del usuario";
		m_Ano = JUtil.obtAno(fecha);
	    m_Mes = JUtil.obtMes(fecha);
	    m_Etapa = "PRIMERA";
		
		m_Predefinida = "FACTURACION";
		m_BaseERP = "";
		m_BasePlantilla = "";
		
		m_SeleccionModulos.resetear();
	}
	
	public void predefinirFacturacion()
	{
		m_SeleccionModulos.resetear();
		
		m_SeleccionModulos.InvservProductos = true;
		m_SeleccionModulos.InvservServicios = true;
		m_SeleccionModulos.MasterInvserv = true;
		
		m_SeleccionModulos.VenClient = true;
		m_SeleccionModulos.VenFac = true;
		m_SeleccionModulos.MasterVen = true;
		
		m_SeleccionModulos.AdmCfdi = true;
		m_SeleccionModulos.AdmPeriodos = true;
		m_SeleccionModulos.AdmFormatos = true;
		
		JEntVentas ent = new JEntVentas("Facturas","3");
		m_EntVentas.add(ent);
	}
	
	public void predefinirPlantilla()
	{
		m_SeleccionModulos.resetear();
		
		m_SeleccionModulos.ContCatcuentas = true;
		m_SeleccionModulos.ContRubros = true;
		m_SeleccionModulos.ContTipopoliza = true;
		m_SeleccionModulos.ContEnlaces = true;
		m_SeleccionModulos.ContPolizas = true;
		m_SeleccionModulos.ContPolcierre = true;
		m_SeleccionModulos.MasterCont = true;
		
		m_SeleccionModulos.InvservGastos = true;
		m_SeleccionModulos.MasterInvserv = true;
		
		m_SeleccionModulos.AdmSaldos = true;
		m_SeleccionModulos.AdmUsuarios = true;
		m_SeleccionModulos.AdmEntidades = true;
		m_SeleccionModulos.AdmVendedores = true;
		m_SeleccionModulos.AdmCfdi = true;
		m_SeleccionModulos.AdmPeriodos = true;
		m_SeleccionModulos.AdmMonedas = true;
		m_SeleccionModulos.AdmVariables = true;
		m_SeleccionModulos.AdmFormatos = true;
		m_SeleccionModulos.MasterAdm = true;
		
	}
	
	public void predefinirERP()
	{
		m_SeleccionModulos.resetear();
		
		m_SeleccionModulos.ContCatcuentas = true;
		m_SeleccionModulos.ContRubros = true;
		m_SeleccionModulos.ContTipopoliza = true;
		m_SeleccionModulos.ContEnlaces = true;
		m_SeleccionModulos.ContPolizas = true;
		m_SeleccionModulos.ContPolcierre = true;
		m_SeleccionModulos.MasterCont = true;
		
		m_SeleccionModulos.BancajBancos = true;
		m_SeleccionModulos.BancajCajas = true;
		m_SeleccionModulos.BancajVales = true;
		m_SeleccionModulos.BancajCierres = true;
		m_SeleccionModulos.MasterBancaj = true;
		
		m_SeleccionModulos.InvservLineas = true;
		m_SeleccionModulos.InvservProductos = true;
		m_SeleccionModulos.InvservServicios = true;
		m_SeleccionModulos.InvservGastos = true;
		m_SeleccionModulos.MasterInvserv = true;
		
		m_SeleccionModulos.AlmMovim = true;
		m_SeleccionModulos.AlmChfis = true;
		m_SeleccionModulos.MasterAlm = true;
		
		m_SeleccionModulos.CompProvee = true;
		m_SeleccionModulos.CompCxp = true;
		m_SeleccionModulos.CompOrd = true;
		m_SeleccionModulos.CompRec = true;
		m_SeleccionModulos.CompFac = true;
		m_SeleccionModulos.CompDev = true;
		m_SeleccionModulos.CompPol = true;
		m_SeleccionModulos.CompGas = true;
		m_SeleccionModulos.MasterComp = true;
		
		m_SeleccionModulos.VenClient = true;
		m_SeleccionModulos.VenCxc = true;
		m_SeleccionModulos.VenCot = true;
		m_SeleccionModulos.VenPed = true;
		m_SeleccionModulos.VenRem = true;
		m_SeleccionModulos.VenFac = true;
		m_SeleccionModulos.VenDev = true;
		m_SeleccionModulos.VenPol = true;
		m_SeleccionModulos.MasterVen = true;
		
		if(m_BaseERP.equals("MANUFACTURERA"))
		{
			m_SeleccionModulos.ModProduccion = true;
			m_SeleccionModulos.MasterProd = true;
			m_SeleccionModulos.ModNomina = true;
			m_SeleccionModulos.MasterNom = true;
		}
		
		m_SeleccionModulos.AdmSaldos = true;
		m_SeleccionModulos.AdmUsuarios = true;
		m_SeleccionModulos.AdmEntidades = true;
		m_SeleccionModulos.AdmVendedores = true;
		m_SeleccionModulos.AdmCfdi = true;
		m_SeleccionModulos.AdmPeriodos = true;
		m_SeleccionModulos.AdmMonedas = true;
		m_SeleccionModulos.AdmVariables = true;
		m_SeleccionModulos.AdmFormatos = true;
		m_SeleccionModulos.MasterAdm = true;

		//Ahora agrega entidades y asocia
		JEntCajas caj = new JEntCajas("Caja", "Caja general");
		m_EntCajas.addElement(caj);
		JEntBancos ban = new JEntBancos("Banco", "xxxxxxxxx", 1, "002");
		m_EntBancos.addElement(ban);
		JEntBodegasMP bod = new JEntBodegasMP("Bodega", "Bodega general");
		m_EntBodegasMP.addElement(bod);
		JEntCompras comp = new JEntCompras("Compras", "2");
		comp.Bodega = 0;
		comp.Bancos.addElement(new MutableBoolean(true));
		comp.Cajas.addElement(new MutableBoolean(true));
		m_EntCompras.addElement(comp);
		JEntGastos gas = new JEntGastos("Gastos", "2");
		gas.Almacen = -1;
		gas.Bancos.addElement(new MutableBoolean(true));
		gas.Cajas.addElement(new MutableBoolean(true));
		m_EntGastos.addElement(gas);
		JEntVentas ven = new JEntVentas("Ventas", "2");
		ven.Bodega = 0;
		ven.Bancos.addElement(new MutableBoolean(true));
		ven.Cajas.addElement(new MutableBoolean(true));
		m_EntVentas.addElement(ven);
	
		if(m_BaseERP.equals("MANUFACTURERA"))
		{
			JEntProduccion prod = new JEntProduccion("Produccion", "Producción general");
			prod.Bodega = 0;
			m_EntProduccion.addElement(prod);
			JEntNomina nom = new JEntNomina("Nomina", "Nómina general", "2", "qui");
			nom.Banco = 0;
			nom.Caja = -1;
			m_EntNomina.addElement(nom);
		}
		
		
	
	}
	
	public boolean DatosGenerales(HttpServletRequest request, StringBuffer sb_mensaje, short proc) 
		  	throws ServletException, IOException
	{
		m_Nombre = JUtil.p(request.getParameter("nombre"));
		m_Compania = JUtil.p(request.getParameter("compania"));
		m_Direccion = JUtil.p(request.getParameter("direccion"));
		m_Poblacion = JUtil.p(request.getParameter("poblacion"));
		m_CP = JUtil.p(request.getParameter("cp"));
		m_Mail = JUtil.p(request.getParameter("mail"));
		m_Web = JUtil.p(request.getParameter("web"));
		m_Password = JUtil.p(request.getParameter("password"));
		m_ConfPwd = JUtil.p(request.getParameter("confpwd"));
			
		// Verificacion
		if(request.getParameter("nombre") != null && request.getParameter("compania") != null
		          && request.getParameter("direccion") != null  && request.getParameter("poblacion") != null
		          && request.getParameter("cp") != null  && request.getParameter("mail") != null 
		          && request.getParameter("web") != null
		          && request.getParameter("password") != null  && request.getParameter("confpwd") != null &&
		          !request.getParameter("nombre").equals("") && !request.getParameter("compania").equals("") &&
		          !request.getParameter("password").equals("") && !request.getParameter("confpwd").equals(""))
		{
			if(proc == JUtil.AGREGAR)
			{
				if(!request.getParameter("nombre").matches("[A-Z]{4,20}"))
				{
					sb_mensaje.append(JUtil.Msj("SAF","ADMIN_BD","DLG","MSJ-PROCERR3",2));//"ERROR: El nombre de la base de datos debe de constar de entre 4 y 20 caracteres de la A a la Z mayusculas";
					return false;  
				}
		    		  
				JBDRegistradasSet set = new JBDRegistradasSet(null);
				set.m_Where = "Nombre ~~* 'FSIBD_" + JUtil.p(request.getParameter("nombre")) + "'";
				set.ConCat(true);
				set.Open();
				if(set.getNumRows() > 0)
				{
					sb_mensaje.append(JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 2));
					return false;  
				}
			}
		    	  
			if(!request.getParameter("password").equals(request.getParameter("confpwd")))
			{
				sb_mensaje.append(JUtil.Msj("SAF", "ADMIN_BD", "DLG", "MSJ-PROCERR", 1));
				return false;
			}
			
			if(proc == JUtil.AGREGAR)
				m_Tab = "TIPO_INSTALACION";
			
			return true;
		}
		else
		{
			sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
			return false;
		}
		
	}
	
	public boolean TipoInstalacion(HttpServletRequest request, StringBuffer sb_mensaje)
  	      throws ServletException, IOException
  	{
		m_TipoInstalacion = JUtil.p(request.getParameter("tipo_instalacion"));
		m_Usuario = JUtil.p(request.getParameter("usuario"));
		m_UsuarioNombre = JUtil.p(request.getParameter("usuarionombre"));
		Calendar fecha = GregorianCalendar.getInstance();
		try { m_Mes = Integer.parseInt(request.getParameter("mes")); } catch(NumberFormatException e) { m_Mes = JUtil.obtMes(fecha); }
		try { m_Ano = Integer.parseInt(request.getParameter("ano")); } catch(NumberFormatException e) { m_Ano = JUtil.obtAno(fecha); }
		
		// Verificacion
		if(request.getParameter("tipo_instalacion") != null && !request.getParameter("tipo_instalacion").equals(""))
		{
			if(m_Usuario.compareToIgnoreCase("cef-su") == 0 ||
   		  		(m_Usuario.length() > 4 && m_Usuario.substring(0, 4).compareToIgnoreCase("cef-") == 0) ||
   		  															m_Usuario.equals(m_Nombre.toLowerCase()))
	  	  	{
				sb_mensaje.append(JUtil.Msj("CEF","ADM_USUARIOS","DLG","MSJ-PROCERR"));//"PRECAUCION: El usuario no puede llamarse cef-su, o cualquier otro nombre de usuario de sistema";
				return false;
	  	  	}
 		}
		else
		{
			sb_mensaje.append(JUtil.Msj("GLB", "GLB", "GLB", "PARAM-NULO"));
			return false;
		}
		
		if(!m_TipoInstalacion.equals("PREDEFINIDA") && !m_TipoInstalacion.equals("MANUAL"))
			m_Tab = "RESUMEN";
		else
			m_Tab = "SELECCION";
		return true;
  	}
	
	public boolean Predefinida(HttpServletRequest request, StringBuffer sb_mensaje)
	  	      throws ServletException, IOException
	{
		// Verificacion
		m_Predefinida = JUtil.p(request.getParameter("predefinida"));
				
	  	if(request.getParameter("predefinida") != null && !request.getParameter("predefinida").equals(""))
	  	{
	  		if(request.getParameter("predefinida").equals("ERP"))
		  	{
	  			if(request.getParameter("baseerp") == null)
			  	{
	  				sb_mensaje.append("ERROR: En instalaciones ERP, debes seleccionar el tipo de empresa, ya sea distribuidora o manufacturera");
	  		  		return false;
			  	}
	  			
		  	}
	  		
	  		if(request.getParameter("predefinida").equals("PLANTILLA"))
		  	{
	  			if(request.getParameter("baseplantilla") == null)
			  	{
	  				sb_mensaje.append("ERROR: En instalaciones de Plantillas, debes seleccionar el tipo de base, ya sea distribuidora o manufacturera");
	  		  		return false;
			  	}
	  		}
	  	}
	  	else
	    {
	  		sb_mensaje.append("ERROR: Es necesario seleccionar un tipo predefinido de instalación.");
	  		return false;
	    }
	  	
	  	if(m_Predefinida.equals("FACTURACION"))
  			predefinirFacturacion();
  		else if(m_Predefinida.equals("ERP"))
  		{
			m_BaseERP = JUtil.p(request.getParameter("baseerp"));
			predefinirERP();
  		}
  		else if(m_Predefinida.equals("PLANTILLA"))
  		{
			m_BasePlantilla = JUtil.p(request.getParameter("baseplantilla"));
			predefinirPlantilla();
  		} 		
		m_Tab = "RESUMEN";
		
		return true;
	}
	
	public int getIDBD() 
	{
		return m_IDBD;
	}

	public void setIDBD(int IDBD) 
	{
		m_IDBD = IDBD;
	}
	
	public String getNombre() 
	{
		return m_Nombre;
	}

	public void setNombre(String Nombre) 
	{
		m_Nombre = Nombre;
	}

	public String getCompania() 
	{
		return m_Compania;
	}

	public void setCompania(String Compania) 
	{
		m_Compania = Compania;
	}

	public String getDireccion() 
	{
		return m_Direccion;
	}

	public void setDireccion(String Direccion) 
	{
		m_Direccion = Direccion;
	}

	public String getPoblacion() 
	{
		return m_Poblacion;
	}

	public void setPoblacion(String Poblacion) 
	{
		m_Poblacion = Poblacion;
	}

	public String getCP() 
	{
		return m_CP;
	}

	public void setCP(String CP) 
	{
		m_CP = CP;
	}

	public String getMail() 
	{
		return m_Mail;
	}

	public void setMail(String Mail) 
	{
		m_Mail = Mail;
	}

	public String getWeb() 
	{
		return m_Web;
	}

	public void setWeb(String Web) 
	{
		m_Web = Web;
	}

	public String getPassword() 
	{
		return m_Password;
	}

	public void setPassword(String Password) 
	{
		m_Password = Password;
	}

	public String getConfPwd() 
	{
		return m_ConfPwd;
	}

	public void setConfPwd(String ConfPwd) 
	{
		m_ConfPwd = ConfPwd;
	}

	public String getTab() 
	{
		return m_Tab;
	}

	public void setTab(String Tab) 
	{
		m_Tab = Tab;
	}

	public String getTipoInstalacion() 
	{
		return m_TipoInstalacion;
	}

	public void setTipoInstalacion(String TipoInstalacion) 
	{
		m_TipoInstalacion = TipoInstalacion;
	}

	public String getPredefinida() 
	{
		return m_Predefinida;
	}

	public void setPredefinida(String Predefinida) 
	{
		m_Predefinida = Predefinida;
	}

	public String getBaseERP() 
	{
		return m_BaseERP;
	}

	public void setBaseERP(String BaseERP) 
	{
		m_BaseERP = BaseERP;
	}

	public String getBasePlantilla() 
	{
		return m_BasePlantilla;
	}

	public void setBasePlantilla(String BasePlantilla) 
	{
		m_BasePlantilla = BasePlantilla;
	}

	public String getUsuario() 
	{
		return m_Usuario;
	}

	public void setUsuario(String Usuario) 
	{
		m_Usuario = Usuario;
	}

	public String getUsuarioNombre() 
	{
		return m_UsuarioNombre;
	}

	public void setUsuarioNombre(String UsuarioNombre) 
	{
		m_UsuarioNombre = UsuarioNombre;
	}

	public int getMes() 
	{
		return m_Mes;
	}

	public void setMes(int Mes) 
	{
		m_Mes = Mes;
	}

	public int getAno() 
	{
		return m_Ano;
	}

	public void setAno(int Ano) 
	{
		m_Ano = Ano;
	}

	public String getEtapa() 
	{
		return m_Etapa;
	}

	public void setEtapa(String Etapa) 
	{
		m_Etapa = Etapa;
	}

	public JSeleccionModulos getSeleccionModulos()
	{
		return m_SeleccionModulos;
	}
	
	public class JSeleccionModulos
	{
		public boolean MasterCont;
		public boolean MasterInvserv;
		public boolean MasterBancaj;
		public boolean MasterAlm;
		public boolean MasterComp;
		public boolean MasterVen;
		public boolean MasterProd;
		public boolean MasterNom;
		public boolean MasterAdm;
				
		//Contabilidad
		public boolean ContCatcuentas;
		public boolean ContRubros;
		public boolean ContTipopoliza;
		public boolean ContEnlaces;
		public boolean ContPolizas;
		public boolean ContPolcierre;
		//Catalogos
		public boolean InvservLineas;
		public boolean InvservProductos;
		public boolean InvservServicios;
		public boolean InvservGastos;
		//Caja y bancos
		public boolean BancajBancos;
		public boolean BancajCajas;
		public boolean BancajVales;
		public boolean BancajCierres;
		//Almacen
		public boolean AlmMovim;
		public boolean AlmMovplant;
		public boolean AlmTraspasos;
		public boolean AlmRequerimientos;
		public boolean AlmChfis;
		public boolean AlmUtensilios;
		//Compras
		public boolean CompProvee;
		public boolean CompCxp;
		public boolean CompOrd;
		public boolean CompRec;
		public boolean CompFac;
		public boolean CompDev;
		public boolean CompPol;
		public boolean CompGas;
		//Ventas
		public boolean VenClient;
		public boolean VenCxc;
		public boolean VenCot;
		public boolean VenPed;
		public boolean VenRem;
		public boolean VenFac;
		public boolean VenDev;
		public boolean VenPol;
		//Produccion
		public boolean ModProduccion;
		//Nomina
		public boolean ModNomina;
		//Centro de control
		public boolean AdmSaldos;
		public boolean AdmUsuarios;
		public boolean AdmEntidades;
		public boolean AdmVendedores;
		public boolean AdmCfdi;
		public boolean AdmPeriodos;
		public boolean AdmMonedas;
		public boolean AdmVariables;
		public boolean AdmFormatos;
		
		public JSeleccionModulos()
		{
		}
		
		public void resetear()
		{
			//Contabilidad
			ContCatcuentas = false;
			ContRubros = false;
			ContTipopoliza = false;
			ContEnlaces = false;
			ContPolizas = false;
			ContPolcierre = false;
			//Catalogos
			InvservLineas = false;
			InvservProductos = false;
			InvservServicios = false;
			InvservGastos = false;
			//Caja y bancos
			BancajBancos = false;
			BancajCajas = false;
			BancajVales = false;
			BancajCierres = false;
			//Almacen
			AlmMovim = false;
			AlmMovplant = false;
			AlmTraspasos = false;
			AlmRequerimientos = false;
			AlmChfis = false;
			AlmUtensilios = false;
			//Compras
			CompProvee = false;
			CompCxp = false;
			CompOrd = false;
			CompRec = false;
			CompFac = false;
			CompDev = false;
			CompPol = false;
			CompGas = false;
			//Ventas
			VenClient = false;
			VenCxc = false;
			VenCot = false;
			VenPed = false;
			VenRem = false;
			VenFac = false;
			VenDev = false;
			VenPol = false;
			//Produccion
			ModProduccion = false;
			//Nomina
			ModNomina = false;
			//Centro de control
			AdmSaldos = false;
			AdmUsuarios = false;
			AdmEntidades = true;
			AdmVendedores = false;
			AdmCfdi = false;
			AdmPeriodos = true;
			AdmMonedas = false;
			AdmVariables = true;
			AdmFormatos = false;
			
			MasterCont = false;
			MasterInvserv = false;
			MasterBancaj = false;
			MasterAlm = false;
			MasterComp = false;
			MasterVen = false;
			MasterProd = false;
			MasterNom = false;
			MasterAdm = true;
			
			m_EntCajas.removeAllElements();
			m_EntBancos.removeAllElements();
			m_EntBodegasMP.removeAllElements();
			m_EntAlmacenesUten.removeAllElements();
			m_EntCompras.removeAllElements();
			m_EntGastos.removeAllElements();
			m_EntVentas.removeAllElements();
			m_EntProduccion.removeAllElements();
			m_EntNomina.removeAllElements();
			
		}
		
		public boolean PrimeraEtapa(HttpServletRequest request, StringBuffer sb_mensaje)
	  	      throws ServletException, IOException
	  	{
			//Contabilidad
			ContCatcuentas = request.getParameter("cont_catcuentas") != null ? true : false;
			ContRubros = request.getParameter("cont_rubros") != null ? true : false;
			ContTipopoliza = request.getParameter("cont_tipopoliza") != null ? true : false;
			ContEnlaces = request.getParameter("cont_enlaces") != null ? true : false;
			ContPolizas = request.getParameter("cont_polizas") != null ? true : false;
			ContPolcierre = request.getParameter("cont_polcierre") != null ? true : false;
			//Catalogos
			InvservLineas = request.getParameter("invserv_lineas") != null ? true : false;
			InvservProductos = request.getParameter("invserv_productos") != null ? true : false;
			InvservServicios = request.getParameter("invserv_servicios") != null ? true : false;
			InvservGastos = request.getParameter("invserv_gastos") != null ? true : false;
			//Caja y bancos
			BancajBancos = request.getParameter("bancaj_bancos") != null ? true : false;
			BancajCajas = request.getParameter("bancaj_cajas") != null ? true : false;
			BancajVales = request.getParameter("bancaj_vales") != null ? true : false;
			BancajCierres = request.getParameter("bancaj_cierres") != null ? true : false;
			//Almacen
			AlmMovim = request.getParameter("alm_movim") != null ? true : false;
			AlmMovplant = request.getParameter("alm_movplant") != null ? true : false;
			AlmTraspasos = request.getParameter("alm_traspasos") != null ? true : false;
			AlmRequerimientos = request.getParameter("alm_requerimientos") != null ? true : false;
			AlmChfis = request.getParameter("alm_chfis") != null ? true : false;
			AlmUtensilios = request.getParameter("alm_utensilios") != null ? true : false;
			//Compras
			CompProvee = request.getParameter("comp_provee") != null ? true : false;
			CompCxp = request.getParameter("comp_cxp") != null ? true : false;
			CompOrd = request.getParameter("comp_ord") != null ? true : false;
			CompRec = request.getParameter("comp_rec") != null ? true : false;
			CompFac = request.getParameter("comp_fac") != null ? true : false;
			CompDev = request.getParameter("comp_dev") != null ? true : false;
			CompPol = request.getParameter("comp_pol") != null ? true : false;
			CompGas = request.getParameter("comp_gas") != null ? true : false;
			//Ventas
			VenClient = request.getParameter("ven_client") != null ? true : false;
			VenCxc = request.getParameter("ven_cxc") != null ? true : false;
			VenCot = request.getParameter("ven_cot") != null ? true : false;
			VenPed = request.getParameter("ven_ped") != null ? true : false;
			VenRem = request.getParameter("ven_rem") != null ? true : false;
			VenFac = request.getParameter("ven_fac") != null ? true : false;
			VenDev = request.getParameter("ven_dev") != null ? true : false;
			VenPol = request.getParameter("ven_pol") != null ? true : false;
			//Produccion
			ModProduccion = request.getParameter("mod_produccion") != null ? true : false;
			//Nomina
			ModNomina = request.getParameter("mod_nomina") != null ? true : false;
			//Centro de control
			AdmSaldos = request.getParameter("adm_saldos") != null ? true : false;
			AdmUsuarios = request.getParameter("adm_usuarios") != null ? true : false;
			AdmEntidades = request.getParameter("adm_entidades") != null ? true : false;
			AdmVendedores = request.getParameter("adm_vendedores") != null ? true : false;
			AdmCfdi = request.getParameter("adm_cfdi") != null ? true : false;
			AdmPeriodos = request.getParameter("adm_periodos") != null ? true : false;
			AdmMonedas = request.getParameter("adm_monedas") != null ? true : false;
			AdmVariables = request.getParameter("adm_variables") != null ? true : false;
			AdmFormatos = request.getParameter("adm_formatos") != null ? true : false;

			//////////////////////////////////////////////////////////////////////////////////////
			// REVISION DE MODULOS
			
			//Primero especifica si procede cada módulo maestro
			if(ContCatcuentas || ContRubros || ContTipopoliza || ContEnlaces || ContPolizas || ContPolcierre)
				MasterCont = true;
			else
				MasterCont = false;
			if(BancajBancos || BancajCajas || BancajVales || BancajCierres)
				MasterBancaj = true;
			else
				MasterBancaj = false;
			if(InvservLineas || InvservProductos || InvservServicios || InvservGastos)
				MasterInvserv = true;
			else
				MasterInvserv = false;
			if(AlmMovim || AlmMovplant || AlmTraspasos || AlmRequerimientos || AlmChfis || AlmUtensilios)
				MasterAlm = true;
			else
				MasterAlm = false;
			if(CompProvee || CompCxp || CompOrd || CompRec || CompFac || CompDev || CompPol || CompGas)
				MasterComp = true;
			else
				MasterComp = false;
			if(VenClient || VenCxc || VenCot || VenPed || VenRem || VenFac || VenDev || VenPol)
				MasterVen = true;
			else
				MasterVen = false;
			if(ModProduccion)
				MasterProd = true;
			else
				MasterProd = false;
			if(ModNomina)
				MasterNom = true;
			else
				MasterNom = false;
			if(AdmSaldos || AdmUsuarios || AdmEntidades || AdmVendedores || AdmCfdi || AdmPeriodos || AdmMonedas || AdmVariables || AdmFormatos)
				MasterAdm = true;
			else
				MasterAdm = false;
			
			//Ahora revisa dependencias de módulos
			//Contabilidad
			if((ContCatcuentas && !ContRubros) || (!ContCatcuentas && ContRubros))
			{
				sb_mensaje.append("ERROR: Si se desea integrar el módulo de contabilidad, es indispensable integrar ambos módulos, Rubros y Catálogo ya que estos dependen uno del otro");
				return false;
			}
			if((ContTipopoliza || ContEnlaces || ContPolcierre) && !ContCatcuentas)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de cuentas contables ya que se han seleccionado módulos de la contabilidad que dependen de éste");
				return false;
			}
			//Catalogos
			//No se relacionan entre si
			//Caja y bancos
			if((BancajVales || BancajCierres) && !BancajCajas)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de cajas ya que se han seleccionado módulos de caja y bancos que dependen de éste");
				return false;
			}
			//Almacen
			if((AlmMovplant || AlmTraspasos || AlmRequerimientos || AlmChfis) && !AlmMovim)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de movimientos al almacén ya que se han seleccionado módulos de almacén que dependen de éste");
				return false;
			}
			//Compras
			if(CompCxp && !CompProvee)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de proveedores ya que el módulo de cuentas por pagar depende de éste");
				return false;
			}
			if((CompOrd || CompRec || CompDev || CompPol) && !CompFac)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de compras ya que se han seleccionado otros módulos de compras que dependen de éste");
				return false;
			}
			if(CompFac && (!InvservProductos || !CompProvee))
			{
				sb_mensaje.append("ERROR: Es necesario integrar ambos módulos, el catálogo de productos y de proveedores, ya que el módulo de compras depende de estos dos");
				return false;
			}	
			if(CompGas && (!InvservGastos || !CompProvee))
			{
				sb_mensaje.append("ERROR: Es necesario integrar ambos módulos, el catálogo de gastos y de proveedores, ya que el módulo de gastos depende de estos dos");
				return false;
			}	
			//Ventas
			if(VenCxc && !VenClient)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de clientes ya que el módulo de cuentas por cobrar depende de éste");
				return false;
			}
			if((VenCot || VenPed || VenRem || VenDev || VenPol) && !VenFac)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de facturas ya que se han seleccionado otros módulos de ventas que dependen de éste");
				return false;
			}
			if(VenFac && ((!InvservProductos && !InvservServicios) || !VenClient))
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de catálogo de productos y/o de servicios, además del módulo de clientes, ya que el módulo de facturas depende de estos");
				return false;
			}	
			//Produccion
			if(ModProduccion && !InvservProductos)
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de catálogo de productos ya que el módulo producción depende de éste");
				return false;
			}	
			//Nomina
			if(ModNomina && ((!BancajBancos && !BancajCajas) || !ContCatcuentas))
			{
				sb_mensaje.append("ERROR: Es necesario integrar el módulo de contabilidad a través del catálogo contable, además del módulo de bancos y/o de cajas, ya que el módulo de nómina depende de estos");
				return false;
			}	
			//Centro de control
							
			m_Etapa = "SEGUNDA";
			return true;
	  	}
		
		public boolean SegundaEtapa(HttpServletRequest request, StringBuffer sb_mensaje, boolean soloasign)
				throws ServletException, IOException
		{
			//Asignación de parametros
			for(int i = 0; i < m_EntCajas.size(); i++)
			{
				m_EntCajas.elementAt(i).Ficha = request.getParameter("caj_ficha_" + i);
				m_EntCajas.elementAt(i).Descripcion = request.getParameter("caj_descripcion_" + i);
			}
			for(int i = 0; i < m_EntBancos.size(); i++)
			{
				m_EntBancos.elementAt(i).Ficha = request.getParameter("ban_ficha_" + i);
				m_EntBancos.elementAt(i).Cuenta = request.getParameter("ban_cuenta_" + i);
				try { m_EntBancos.elementAt(i).Cheque = Integer.parseInt(request.getParameter("ban_cheque_" + i)); } catch(NumberFormatException e) { m_EntBancos.elementAt(i).Cheque = 1; }
				m_EntBancos.elementAt(i).Banco = request.getParameter("ban_banco_" + i);
			}
			for(int i = 0; i < m_EntBodegasMP.size(); i++)
			{
				m_EntBodegasMP.elementAt(i).Ficha = request.getParameter("bod_ficha_" + i);
				m_EntBodegasMP.elementAt(i).Descripcion = request.getParameter("bod_descripcion_" + i);
			}
			for(int i = 0; i < m_EntAlmacenesUten.size(); i++)
			{
				m_EntAlmacenesUten.elementAt(i).Ficha = request.getParameter("alm_ficha_" + i);
				m_EntAlmacenesUten.elementAt(i).Descripcion = request.getParameter("alm_descripcion_" + i);
			}
			for(int i = 0; i < m_EntCompras.size(); i++)
			{
				m_EntCompras.elementAt(i).Ficha = request.getParameter("comp_ficha_" + i);
				m_EntCompras.elementAt(i).Tipo = request.getParameter("comp_tipo_" + i);
			}
			for(int i = 0; i < m_EntGastos.size(); i++)
			{
				m_EntGastos.elementAt(i).Ficha = request.getParameter("gas_ficha_" + i);
				m_EntGastos.elementAt(i).Tipo = request.getParameter("gas_tipo_" + i);
			}
			for(int i = 0; i < m_EntVentas.size(); i++)
			{
				m_EntVentas.elementAt(i).Ficha = request.getParameter("ven_ficha_" + i);
				m_EntVentas.elementAt(i).Tipo = request.getParameter("ven_tipo_" + i);
			}
			for(int i = 0; i < m_EntProduccion.size(); i++)
			{
				m_EntProduccion.elementAt(i).Ficha = request.getParameter("prod_ficha_" + i);
				m_EntProduccion.elementAt(i).Descripcion = request.getParameter("prod_descripcion_" + i);
			}
			for(int i = 0; i < m_EntNomina.size(); i++)
			{
				m_EntNomina.elementAt(i).Ficha = JUtil.p(request.getParameter("nom_ficha_" + i));
				m_EntNomina.elementAt(i).Descripcion = JUtil.p(request.getParameter("nom_descripcion_" + i));
				m_EntNomina.elementAt(i).Tipo = JUtil.p(request.getParameter("nom_tipo_" + i));
				m_EntNomina.elementAt(i).Periodo = JUtil.p(request.getParameter("nom_periodo_" + i));
			}
			
			if(soloasign)
				return true;
			
			//Ahora la verificación de errores
			if(BancajCajas && m_EntCajas.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de cajas, es necesario agregar por lo menos una caja");
				return false;
			}
			if(BancajBancos && m_EntBancos.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de bancos, es necesario agregar por lo menos una cuenta bancaria");
				return false;
			}
			if(AlmMovim && m_EntBodegasMP.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de movimientos al almacén de materias primas, es necesario agregar por lo menos una bodega de materiales");
				return false;
			}
			if(AlmUtensilios && m_EntAlmacenesUten.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de utensilios, es necesario agregar por lo menos un almacén de utensilios");
				return false;
			}
			if((MasterComp && (CompFac || CompProvee)) && m_EntCompras.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el todo o parte del módulo de compras, es necesario agregar por lo menos un área de compras");
				return false;
			}
			if(CompGas && m_EntGastos.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de gastos, es necesario agregar por lo menos un área de gastos");
				return false;
			}
			if(MasterVen && m_EntVentas.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró todo o parte del módulo de ventas, es necesario agregar por lo menos un punto de venta");
				return false;
			}
			if(MasterProd && m_EntProduccion.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de producción, es necesario agregar por lo menos un punto de producción");
				return false;
			}
			if(MasterNom && m_EntNomina.size() == 0)
			{
				sb_mensaje.append("ERROR: Como se integró el módulo de nómina, es necesario agregar por lo menos un area de nómina");
				return false;
			}

			//Revision de fichas
			String error = "ERROR: Las fichas no pueden contener caracteres especiales ni espacios: ";
			if(BancajCajas)
			{
				for(int i = 0; i < m_EntCajas.size(); i++)
				{
					if(!m_EntCajas.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntCajas.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(BancajBancos)
			{
				for(int i = 0; i < m_EntBancos.size(); i++)
				{
					if(!m_EntBancos.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntBancos.elementAt(i).Ficha);
						return false;
					}
					if(m_EntBancos.elementAt(i).Banco.equals("000"))
					{
						sb_mensaje.append("ERROR: Debes seleccionar el banco de la entidad: " + m_EntBancos.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(AlmMovim)
			{
				for(int i = 0; i < m_EntBodegasMP.size(); i++)
				{
					if(!m_EntBodegasMP.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntBodegasMP.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(AlmUtensilios)
			{
				for(int i = 0; i < m_EntAlmacenesUten.size(); i++)
				{
					if(!m_EntAlmacenesUten.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntAlmacenesUten.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(MasterComp && (CompFac || CompProvee))
			{
				for(int i = 0; i < m_EntCompras.size(); i++)
				{
					if(!m_EntCompras.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntCompras.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(CompGas)
			{
				for(int i = 0; i < m_EntGastos.size(); i++)
				{
					if(!m_EntGastos.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntGastos.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(MasterVen)
			{
				for(int i = 0; i < m_EntVentas.size(); i++)
				{
					if(!m_EntVentas.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntVentas.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(MasterProd)
			{
				for(int i = 0; i < m_EntProduccion.size(); i++)
				{
					if(!m_EntProduccion.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntProduccion.elementAt(i).Ficha);
						return false;
					}
				}
			}
			if(MasterNom)
			{
				for(int i = 0; i < m_EntNomina.size(); i++)
				{
					if(!m_EntNomina.elementAt(i).Ficha.matches("[A-Za-z0-9]{1,10}"))
					{
						sb_mensaje.append(error + m_EntNomina.elementAt(i).Ficha);
						return false;
					}
				}
			}
			
			m_Etapa = "TERCERA";
			return true;
		}

		public boolean TerceraEtapa(HttpServletRequest request, StringBuffer sb_mensaje)
				throws ServletException, IOException
		{
			if(MasterComp && (CompFac || CompProvee))
			{
				for(int i = 0; i < m_EntCompras.size(); i++)
				{
					if(!request.getParameter("comp_bod_" + i).equals("-1"))
						m_EntCompras.elementAt(i).Bodega = Integer.parseInt(request.getParameter("comp_bod_" + i));
					else
						m_EntCompras.elementAt(i).Bodega = -1;
					for(int c = 0; c < getEntCompras(i).Cajas.size(); c++)
					{
						if(request.getParameter("comp_caj_" + i + "_" + c) != null) 
							getEntCompras(i).Cajas.elementAt(c).setValue(true);
						else
							getEntCompras(i).Cajas.elementAt(c).setValue(false);
					}
					for(int b = 0; b < getEntCompras(i).Bancos.size(); b++)
					{
						if(request.getParameter("comp_ban_" + i + "_" + b) != null)
							getEntCompras(i).Bancos.elementAt(b).setValue(true);
						else
							getEntCompras(i).Bancos.elementAt(b).setValue(false);
					}
				}
			}
			if(CompGas)
			{
				for(int i = 0; i < m_EntGastos.size(); i++)
				{
					if(!request.getParameter("gas_alm_" + i).equals("-1"))
						m_EntGastos.elementAt(i).Almacen = Integer.parseInt(request.getParameter("gas_alm_" + i));
					else
						m_EntGastos.elementAt(i).Almacen = -1;
					for(int c = 0; c < getEntGastos(i).Cajas.size(); c++)
					{
						if(request.getParameter("gas_caj_" + i + "_" + c) != null) 
							getEntGastos(i).Cajas.elementAt(c).setValue(true);
						else
							getEntGastos(i).Cajas.elementAt(c).setValue(false);
					}
					for(int b = 0; b < getEntGastos(i).Bancos.size(); b++)
					{
						if(request.getParameter("gas_ban_" + i + "_" + b) != null)
							getEntGastos(i).Bancos.elementAt(b).setValue(true);
						else
							getEntGastos(i).Bancos.elementAt(b).setValue(false);
					}
				}
			}
			if(MasterVen)
			{
				for(int i = 0; i < m_EntVentas.size(); i++)
				{
					if(!request.getParameter("ven_bod_" + i).equals("-1"))
						m_EntVentas.elementAt(i).Bodega = Integer.parseInt(request.getParameter("ven_bod_" + i));
					else
						m_EntVentas.elementAt(i).Bodega = -1;
					for(int c = 0; c < getEntVentas(i).Cajas.size(); c++)
					{
						if(request.getParameter("ven_caj_" + i + "_" + c) != null) 
							getEntVentas(i).Cajas.elementAt(c).setValue(true);
						else
							getEntVentas(i).Cajas.elementAt(c).setValue(false);
					}
					for(int b = 0; b < getEntVentas(i).Bancos.size(); b++)
					{
						if(request.getParameter("ven_ban_" + i + "_" + b) != null)
							getEntVentas(i).Bancos.elementAt(b).setValue(true);
						else
							getEntVentas(i).Bancos.elementAt(b).setValue(false);
					}
				}
			}
			if(MasterProd)
			{
				for(int i = 0; i < m_EntProduccion.size(); i++)
				{
					if(!request.getParameter("prod_bod_" + i).equals("-1"))
						m_EntProduccion.elementAt(i).Bodega = Integer.parseInt(request.getParameter("prod_bod_" + i));
					else
						m_EntProduccion.elementAt(i).Bodega = -1;
				}
			}
			if(MasterNom)
			{
				for(int i = 0; i < m_EntNomina.size(); i++)
				{
					if(request.getParameter("nom_pago_" + i) != null)
					{
						if(request.getParameter("nom_pago_" + i).substring(0, 4).equals("ban_"))
						{
							m_EntNomina.elementAt(i).Banco = Integer.parseInt(request.getParameter("nom_pago_" + i).substring(4));
							m_EntNomina.elementAt(i).Caja = -1;
						}
						else
						{
							m_EntNomina.elementAt(i).Banco = -1;
							m_EntNomina.elementAt(i).Caja = Integer.parseInt(request.getParameter("nom_pago_" + i).substring(4));
						}
					}
					else
					{
						m_EntNomina.elementAt(i).Banco = -1;
						m_EntNomina.elementAt(i).Caja = -1;
					}
				}
			}
			
			m_Tab = "RESUMEN";
			return true;
		}
	
	
	}
	
	public boolean agregarCaja(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntCajas.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}
		JEntCajas ent = new JEntCajas("Caja" + Integer.toString((m_EntCajas.size() + 1)), "Caja " + Integer.toString((m_EntCajas.size() + 1)));
		m_EntCajas.addElement(ent);
		
		for(int i = 0; i < m_EntCompras.size(); i++)
			getEntCompras(i).Cajas.addElement(new MutableBoolean(false));
		for(int i = 0; i < m_EntGastos.size(); i++)
			getEntGastos(i).Cajas.addElement(new MutableBoolean(false));
		for(int i = 0; i < m_EntVentas.size(); i++)
			getEntVentas(i).Cajas.addElement(new MutableBoolean(false));
		
		return true;
	}
	public boolean eliminarCaja(int index,HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		
		m_EntCajas.removeElementAt(index);
		for(int i = 0; i < m_EntCompras.size(); i++)
			getEntCompras(i).Cajas.removeElementAt(index);
		for(int i = 0; i < m_EntGastos.size(); i++)
			getEntGastos(i).Cajas.removeElementAt(index);
		for(int i = 0; i < m_EntVentas.size(); i++)
			getEntVentas(i).Cajas.removeElementAt(index);
		for(int i = 0; i < m_EntNomina.size(); i++)
		{
			if(getEntNomina(i).Caja == index)
				getEntNomina(i).Caja = -1;
		}
		
		return true;
	}
	public boolean agregarBanco(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntBancos.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntBancos ent = new JEntBancos("Cuenta" + Integer.toString((m_EntBancos.size() + 1)), "00000000", 1, "000");
		m_EntBancos.addElement(ent);
		
		for(int i = 0; i < m_EntCompras.size(); i++)
			getEntCompras(i).Bancos.addElement(new MutableBoolean(false));
		for(int i = 0; i < m_EntGastos.size(); i++)
			getEntGastos(i).Bancos.addElement(new MutableBoolean(false));
		for(int i = 0; i < m_EntVentas.size(); i++)
			getEntVentas(i).Bancos.addElement(new MutableBoolean(false));
		
		return true;
	}
	public boolean eliminarBanco(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		for(int i = 0; i < m_EntCompras.size(); i++)
			getEntCompras(i).Bancos.removeElementAt(index);
		for(int i = 0; i < m_EntGastos.size(); i++)
			getEntGastos(i).Bancos.removeElementAt(index);
		for(int i = 0; i < m_EntVentas.size(); i++)
			getEntVentas(i).Bancos.removeElementAt(index);
		for(int i = 0; i < m_EntNomina.size(); i++)
		{
			if(getEntNomina(i).Banco == index)
				getEntNomina(i).Banco = -1;
		}
		return true;
	}
	public boolean agregarBodegaMP(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntBodegasMP.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntBodegasMP ent = new JEntBodegasMP("Bodega" + Integer.toString((m_EntBodegasMP.size() + 1)), "Bodega " + Integer.toString((m_EntBodegasMP.size() + 1)));
		m_EntBodegasMP.addElement(ent);
		return true;
	}
	public boolean eliminarBodegaMP(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		
		m_SeleccionModulos.SegundaEtapa(request, new StringBuffer(), true);
		m_EntBodegasMP.removeElementAt(index);
		return true;
	}
	public boolean agregarAlmacenUten(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntAlmacenesUten.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntAlmacenesUten ent = new JEntAlmacenesUten("Almacen" + Integer.toString((m_EntAlmacenesUten.size() + 1)), "Almacén " + Integer.toString((m_EntAlmacenesUten.size() + 1)));
		m_EntAlmacenesUten.addElement(ent);
		return true;
	}
	public boolean eliminarAlmacenUten(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntAlmacenesUten.removeElementAt(index);
		return true;
	}
	public boolean agregarCompra(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntCompras.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntCompras ent = new JEntCompras("Compras" + Integer.toString((m_EntCompras.size() + 1)), "2");
		for(int i = 0; i < m_EntCajas.size(); i++)
			ent.Cajas.add(i, new MutableBoolean(false));
		for(int i = 0; i < m_EntBancos.size(); i++)
			ent.Bancos.add(i, new MutableBoolean(false));
		
		m_EntCompras.addElement(ent);
		return true;
	}
	public boolean eliminarCompra(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntCompras.removeElementAt(index);
		return true;
	}
	public boolean agregarGasto(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntGastos.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntGastos ent = new JEntGastos("Gastos" + Integer.toString((m_EntGastos.size() + 1)), "2");
		for(int i = 0; i < m_EntCajas.size(); i++)
			ent.Cajas.add(i, new MutableBoolean(false));
		for(int i = 0; i < m_EntBancos.size(); i++)
			ent.Bancos.add(i, new MutableBoolean(false));
		
		m_EntGastos.addElement(ent);
		return true;
	}
	public boolean eliminarGasto(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntGastos.removeElementAt(index);
		return true;
	}
	public boolean agregarVenta(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntVentas.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}
		
		JEntVentas ent = new JEntVentas("Ventas" + Integer.toString((m_EntVentas.size() + 1)), "2");
		for(int i = 0; i < m_EntCajas.size(); i++)
			ent.Cajas.add(i, new MutableBoolean(false));
		for(int i = 0; i < m_EntBancos.size(); i++)
			ent.Bancos.add(i, new MutableBoolean(false));
		
		m_EntVentas.addElement(ent);
		return true;
	}
	public boolean eliminarVenta(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntVentas.removeElementAt(index);
		return true;
	}
	public boolean agregarProduccion(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntProduccion.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntProduccion ent = new JEntProduccion("Prod" + Integer.toString((m_EntProduccion.size() + 1)), "Producción " + Integer.toString((m_EntProduccion.size() + 1)));
		m_EntProduccion.addElement(ent);
		return true;
	}
	public boolean eliminarProduccion(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntProduccion.removeElementAt(index);
		return true;
	}
	public boolean agregarNomina(HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		if(m_EntNomina.size() > 19) //Solo puede agregar veinte entidades
		{
			sb_mensaje.append("ERROR: Solo es posible agregar veite entidades del mismo tipo como máximo. Si necesitas agregar mas entidades, debes hacerlo desde el Centro de Control del CEF ya que hayas instalado la empresa");
			return false;
		}	
		JEntNomina ent = new JEntNomina("Nomina" + Integer.toString((m_EntNomina.size() + 1)), "Nómina " + Integer.toString((m_EntNomina.size() + 1)), "2", "sem");
		m_EntNomina.addElement(ent);
		return true;
	}
	public boolean eliminarNomina(int index, HttpServletRequest request, StringBuffer sb_mensaje)
			throws ServletException, IOException
	{
		if(!m_SeleccionModulos.SegundaEtapa(request, sb_mensaje, true))
			return false;
		m_EntNomina.removeElementAt(index);
		return true;
	}
	
	public class JEntidades
	{
		public String Ficha;
		public String Descripcion;
		public String Tipo;
		public Vector<MutableBoolean>Cajas;
		public Vector<MutableBoolean>Bancos;
		
		JEntidades(String Ficha, String Descripcion, String Tipo)
		{
			this.Ficha = Ficha;
			this.Descripcion = Descripcion;
			this.Tipo = Tipo;
			Cajas = new Vector<MutableBoolean>();
			Bancos = new Vector<MutableBoolean>();
		}
		
	}
	
	public class JEntCajas extends JEntidades
	{
		JEntCajas(String Ficha, String Descripcion) 
		{
			super(Ficha, Descripcion, "");
		}
	}
	
	public class JEntBancos extends JEntidades
	{
		public String Cuenta;
		public int Cheque;
		public String Banco;
		
		JEntBancos(String Ficha, String Cuenta, int Cheque, String Banco)
		{
			super(Ficha, "", "");
			this.Cuenta = Cuenta;
			this.Cheque = Cheque;
			this.Banco = Banco;
		}
	}
	public class JEntBodegasMP extends JEntidades
	{
		JEntBodegasMP(String Ficha, String Descripcion) 
		{
			super(Ficha, Descripcion, "");
		}
	}
	public class JEntAlmacenesUten extends JEntidades
	{
		JEntAlmacenesUten(String Ficha, String Descripcion) 
		{
			super(Ficha, Descripcion, "");
		}
	}
	public class JEntCompras extends JEntidades
	{
		public int Bodega;
		
		JEntCompras(String Ficha, String Tipo) 
		{
			super(Ficha, "", Tipo);
			Bodega = -1;
		}
	}
	public class JEntGastos extends JEntidades
	{
		public int Almacen;
		
		JEntGastos(String Ficha, String Tipo) 
		{
			super(Ficha, "", Tipo);
			Almacen = -1;
		}
	}
	public class JEntVentas extends JEntidades
	{
		public int Bodega;
		
		JEntVentas(String Ficha, String Tipo) 
		{
			super(Ficha, "", Tipo);
			Bodega = -1;
		}
	}
	public class JEntProduccion extends JEntidades
	{
		public int Bodega;
		
		JEntProduccion(String Ficha, String Descripcion) 
		{
			super(Ficha, Descripcion, "");
			Bodega = -1;
		}
	}
	public class JEntNomina extends JEntidades
	{
		public String Periodo;
		public int Banco;
		public int Caja;
		
		JEntNomina(String Ficha, String Descripcion, String Tipo, String Periodo) 
		{
			super(Ficha, Descripcion, Tipo);
			this.Periodo = Periodo;
			Banco = -1;
			Caja = -1;
		}
	}
}
