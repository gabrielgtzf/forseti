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
package forseti.produccion;

import java.io.IOException;
import java.util.Date;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import forseti.JSesionRegs;
import forseti.JUtil;
import forseti.sets.JPublicInvServInvCatalogSetV2;
import forseti.sets.JProdFormulasSetDetprodV2;

public class JProdFormulasSes extends JSesionRegs
{

	  private String m_ID_Prod;
	  private int m_FormulaNum; // el numero de formula de este producto
	  private String m_Formula;
	  private String m_Descripcion;
	  private String m_Unidad;
	  
	  private long m_ID_Formula; // el id global de la formula
	  private long m_ID_Proceso; // el id global del proceso
	  private float m_Rendimiento;
	  private float m_MasMenos;
	  private boolean m_CantLotes; 
	  private float m_Merma;
	  private float m_CPT;
	  private float m_UCT;
	  
	  private String m_part_ID_Prod;
	  private String m_part_Descripcion;
	  private String m_part_Unidad;
	  
	 	  
	  public JProdFormulasSes()
	  {
		  m_ID_Prod = "";
		  m_ID_Formula = 0;
		  m_ID_Proceso = 0;
		  m_Formula = "";
		  m_Descripcion = "";
		  m_Unidad = "";
		  m_FormulaNum = 0;
		  m_Rendimiento = 0.0F;
		  m_MasMenos = 0.0F;
		  m_CantLotes = false; 
		  m_Merma = 0.0F;
		  m_CPT = 0.0F;
		  m_UCT = 0.0F;
		  
		  resetearPart();

	  }
	  
	  public JProdFormulasSesPartProc getPartida(int ind)
	  {
	    return (JProdFormulasSesPartProc)m_Partidas.elementAt(ind);
	  }
	  
	  public short borraPartida(String proceso, int idPartida, StringBuffer mensaje)
	  {
		  short res = -1;
		  			 
		  if(proceso.equals("EDITAR_FORMULA"))
		  {
			  res = 3; 
	    	  mensaje.append("ERROR: No se puede borrar un proceso cuando se está editando una formula<br>");
	    	  return res;			  
		  }
		  
		  super.borraPartida(idPartida);
		  establecerResultados();
		  
		  return res;
	  }
	  
	  public short borraPartidaDet(String proceso, int idPartida, int indPartida, StringBuffer mensaje)
	  {
		  short res = -1;
		  JProdFormulasSesPart part = (JProdFormulasSesPart) getPartida(idPartida).getPartidas().elementAt(indPartida);
			 
		  if(proceso.equals("EDITAR_FORMULA"))
		  {
			  if(part.getMasMenosXU() < part.getCantidadXU())
			  {
				  res = 3; 
	    		  mensaje.append("ERROR: La partida no se puede borrar porque las diferencias de este producto en la fórmula deben ser igual a la cantidad en la misma<br>");
	    		  return res;
			  }
		  }
		  
		  getPartida(idPartida).getPartidas().removeElementAt(indPartida);
		  //super.borraPartida(indPartida);
		  establecerResultados();
		  
		  return res;
	  }
	  
	  public short editaPartida(int indPartida, HttpServletRequest request, String nombre, int tiempo, StringBuffer mensaje) 
	  {
		  short res = -1;
		  String idsubprod = "", descripcion = "", unidad = "";
		  float rendimientosp = 0.0F, mmrendimientosp = 0.0F, porcentajesp = 0.0F;
		  
		  JProdFormulasSesPartProc part = (JProdFormulasSesPartProc)m_Partidas.elementAt(indPartida);
		  
		  // se está editando el rendimiento en un reporte de produccion
		  if(request.getParameter("proceso").equals("EDITAR_FORMULA") && !part.getID_Prod().equals(""))
		  {
			  if(request.getParameter("rendimientosp") == null || request.getParameter("rendimientosp").equals(""))
			  {
				  res = 3; 
		          mensaje.append("ERROR: El rendimiento del sub-producto no se recibió, no se puede editar la formula<br>");
		          return res; 
			  }
			  
			  try { rendimientosp = Float.parseFloat(request.getParameter("rendimientosp")); } catch(NumberFormatException e) { rendimientosp = 0.0F; }
			  
			  if(rendimientosp < (part.getCantidad() - part.getMasMenos()) || rendimientosp > (part.getCantidad() + part.getMasMenos()))
			  {
				  res = 3; 
				  mensaje.append("ERROR: El rendimiento del sub-producto, no es correcto, Este parece ser menor o mayor a la cantidad ( junto con sus diferencias aceptables ) de la fórmula. No se cambió el proceso<br>" + rendimientosp + " " + part.getCantidad() + " " + part.getMasMenos());
				  return res;
			  }
			  else
			  {
				  part.setCantidad(rendimientosp);
				  part.setMasMenos(0F);
			  }
		  }
		  else if(request.getParameter("proceso").equals("CAMBIAR_FORMULA"))
		  {
			  if(request.getParameter("idsubprod") != null && !request.getParameter("idsubprod").equals(""))
			  {
				  if(request.getParameter("rendimientosp") == null || request.getParameter("rendimientosp").equals("") ||
					  request.getParameter("mmrendimientosp") == null || request.getParameter("mmrendimientosp").equals("") || 
					  request.getParameter("porcentaje") == null || request.getParameter("porcentaje").equals(""))
				  {
					  res = 3; 
					  mensaje.append("ERROR: El rendimiento, diferencia o porcentaje del sub-producto no se recibió<br>");
					  return res; 
				  }
			  
				  try { rendimientosp = Float.parseFloat(request.getParameter("rendimientosp")); } catch(NumberFormatException e) { rendimientosp = 0.0F; }
				  try { mmrendimientosp = Float.parseFloat(request.getParameter("mmrendimientosp")); } catch(NumberFormatException e) { mmrendimientosp = 0.0F; }
				  try { porcentajesp = Float.parseFloat(request.getParameter("porcentaje")); } catch(NumberFormatException e) { porcentajesp = 0.0F; }
			  
				  if(rendimientosp < 0.0 || mmrendimientosp < 0 || mmrendimientosp > rendimientosp || porcentajesp >= 100.00 )
				  {
					  res = 3; 
					  mensaje.append("ERROR: Los valores del rendimiento, diferencias o porcentaje del sub-producto del proceso están mal<br>");
					  return res;
				  }
			  
				  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
				  set.m_Where = "Clave = '" + JUtil.p(request.getParameter("idsubprod")) + "' AND ID_Tipo = 'P' AND Status = 'V' AND SeProduce = '1'";
				  set.Open();
				  if(set.getNumRows() < 1)
				  {
					  res = 1; 
					  mensaje.append("PRECAUCION: La clave del sub producto del proceso no existe, está descontinuado ó es materia prima (No se produce)<br>");
					  return res;
				  }
	  		  
				  idsubprod = request.getParameter("idsubprod");
				  rendimientosp = JUtil.redondear(rendimientosp, 3);
				  mmrendimientosp = JUtil.redondear(mmrendimientosp, 3);
				  porcentajesp = JUtil.redondear(porcentajesp, 6);
				  unidad = set.getAbsRow(0).getID_UnidadSalida();
				  descripcion = set.getAbsRow(0).getDescripcion();
			  }
	
			  part.setPartida(nombre, tiempo, rendimientosp, mmrendimientosp, idsubprod, descripcion, unidad, 
		  																				porcentajesp, 0.0F, 0.0F, null, null);
		  	  establecerResultados();
			  
		  }
		  else
		  {
			  res = 1; 
			  mensaje.append("Al parecer este proceso no es editable ya que carece de sub-producto<br>");
		  }
		  
		  return res;
	  }	  
	  
	  public short editaPartidaDet(int idPartida, int indPartida, HttpServletRequest request, float cantidad, float masmenos, String idprod, boolean principal, StringBuffer mensaje) 
	  {
		  short res = -1;
		  JProdFormulasSesPartProc partproc = (JProdFormulasSesPartProc) getPartida(idPartida); // La partida del proceso
		  JProdFormulasSesPart partdet = (JProdFormulasSesPart)partproc.getPartidas().elementAt(indPartida); // La partida del detalle del proceso
		  if(!partdet.getID_Prod().equals(idprod))
		  {
			  res = 3; 
    		  mensaje.append("ERROR: El producto no es el de la partida: " + ((JProdFormulasSesPart) getPartida(idPartida).getPartidas().elementAt(indPartida)).getID_Prod() + " " + idprod + " " + "<br>");
    		  return res;
		  }
		  
		  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	  	  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' AND ID_Tipo = 'P' AND Status = 'V'";
	  	  set.Open();
		  
	  	  if(!m_CantLotes)
	  	  {
	  		  if(masmenos < 0 || masmenos > cantidad)
	  		  {
	  			  res = 3; 
	  			  mensaje.append("ERROR: La diferencia de cantidad no puede ser menor que cero o mayor a la propia cantidad de la partida<br>");
	  			  return res;
	  		  }
	  	  }
	  	  else
	  	  {
	  		  if(masmenos != 0)
	  		  {
	  			  res = 3; 
	  			  mensaje.append("ERROR: La diferencia de cantidad de la partida, no puede ser diferente de cero en formulas simples <br>");
	  			  return res;
	  		  } 
	  	  }
		  
	  	  if( set.getNumRows() > 0 )
	  	  {
	  		  m_part_ID_Prod = idprod;
	  		  m_part_Unidad = set.getAbsRow(0).getID_UnidadSalida();
	  		  m_part_Descripcion = set.getAbsRow(0).getDescripcion();
	  		  
	  		  // Ahora hace las comparaciones.
	  		  float fcantidad, fcantidadXU, fmasmenos, fmasmenosXU;
	  		  
	  		  fcantidadXU = ((cantidad == 0) ? 0 : JUtil.redondear( (cantidad / m_Rendimiento),6) );
	  		  fcantidad = JUtil.redondear(cantidad,3);
	  		  fmasmenosXU = ((masmenos == 0) ? 0 : JUtil.redondear( (masmenos / m_Rendimiento),6) );
	  		  fmasmenos = JUtil.redondear(masmenos,3);
	  		  
	  		  if(fcantidad < 0.0 || fcantidadXU < 0.0 )
	  		  {
	  			  res = 1;
	  			  mensaje.append("PRECAUCION: La cantidad no es correcta, No se agregó el material");
	  			  return res;
	  		  }
	  		 
	  		  float cp, uc, cpt, uct;
	  		  cp = set.getAbsRow(0).getCostoPromedio();
	  		  uc = set.getAbsRow(0).getUltimoCosto();
	  		  cpt = JUtil.redondear(fcantidadXU * cp, 4);
	  		  uct = JUtil.redondear(fcantidadXU * uc, 4);
				 
	  		  if(request.getParameter("proceso").equals("EDITAR_FORMULA"))
	  		  {
	  			  if(cantidad < (partdet.getCantidad() - partdet.getMasMenos()) || cantidad > (partdet.getCantidad() + partdet.getMasMenos()))
	  			  {
	  				  res = 3; 
	  				  mensaje.append("ERROR: La cantidad, no es correcta, Esta parece ser menor o mayor a la cantidad ( junto con sus diferencias aceptables ) de la fórmula. No se cambió el producto<br>" + cantidad + " " + partdet.getCantidad() + " " + partdet.getMasMenos());
	  				  return res;
	  			  }
	  			  else
	  			  {
	  				  fmasmenosXU = 0F;
	  				  fmasmenos = 0F;
	  			  }
	  		  }
					  
	  		  partdet.setPartida(fcantidadXU, fcantidad, fmasmenosXU, fmasmenos, m_part_ID_Prod, m_part_Descripcion, m_part_Unidad, cp, uc, cpt, uct, principal);
	  		  establecerResultados();
	  		  resetearPart();
	  		  	  	
	  	  }
	  	  else
	  	  {
	  		  res = 3;
	  		  mensaje.append("ERROR: No se encontró el producto especificado de la partida, o la clave pertenece a un servicio");
	  	  }
	  	  	  
		  return res;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public void agregaPartidaDet(JProdFormulasSesPartProc proc, float CantidadXU, float Cantidad, float MasMenosXU, float MasMenos, String ID_Prod, String Descripcion, String Unidad, float CP, float UC, float CPT, float UCT, boolean Principal)
	  {
		  // Aqui aplica la partida
		  JProdFormulasSesPart part = new JProdFormulasSesPart(CantidadXU, Cantidad, MasMenosXU, MasMenos, ID_Prod, Descripcion, Unidad, CP, UC, CPT, UCT, Principal);
		  proc.getPartidas().addElement(part);
	  }
	 
	  @SuppressWarnings("unchecked")
	  public JProdFormulasSesPartProc agregaPartida(String Nombre, int Tiempo, float Rendimientosp, float mmRendimientosp, String Idsubprod, String Descripcion, String Unidad, float Porcentajesp, Date Fecha, Date FechaSP)
	  {
		  // Aqui aplica la partida
		  JProdFormulasSesPartProc part = new JProdFormulasSesPartProc(
					  Nombre, Tiempo, Rendimientosp, mmRendimientosp, Idsubprod, Descripcion, Unidad, Porcentajesp, 0.0F, 0.0F, Fecha, FechaSP);
		  m_Partidas.addElement(part);
		  return part;
	  }
	  

	  @SuppressWarnings("unchecked")
	  public short agregaPartida(HttpServletRequest request, String nombre, int tiempo, StringBuffer mensaje) 
	  {
		  short res = -1;
		  String idsubprod = "", descripcion = "", unidad = "";
		  float rendimientosp = 0.0F, mmrendimientosp = 0.0F, porcentajesp = 0.0F;
		  
		  if(request.getParameter("idsubprod") != null && !request.getParameter("idsubprod").equals(""))
		  {
			  if(request.getParameter("rendimientosp") == null || request.getParameter("rendimientosp").equals("") ||
					  request.getParameter("mmrendimientosp") == null || request.getParameter("mmrendimientosp").equals("") || 
					  request.getParameter("porcentaje") == null || request.getParameter("porcentaje").equals(""))
			  {
				  res = 3; 
		          mensaje.append("ERROR: El rendimiento, diferencia o porcentaje del sub-producto no se recibió<br>");
		          return res; 
			  }
			  
			  try { rendimientosp = Float.parseFloat(request.getParameter("rendimientosp")); } catch(NumberFormatException e) { rendimientosp = 0.0F; }
			  try { mmrendimientosp = Float.parseFloat(request.getParameter("mmrendimientosp")); } catch(NumberFormatException e) { mmrendimientosp = 0.0F; }
			  try { porcentajesp = Float.parseFloat(request.getParameter("porcentaje")); } catch(NumberFormatException e) { porcentajesp = 0.0F; }
			  
			  if(rendimientosp < 0.0 || mmrendimientosp < 0 || mmrendimientosp > rendimientosp || porcentajesp >= 100.00 )
			  {
				  res = 3; 
				  mensaje.append("ERROR: Los valores del rendimiento, diferencias o porcentaje del sub-producto del proceso están mal<br>");
				  return res;
		      }
			  
			  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	  		  set.m_Where = "Clave = '" + JUtil.p(request.getParameter("idsubprod")) + "' AND ID_Tipo = 'P' AND Status = 'V' AND SeProduce = '1'";
	  		  set.Open();
	  		  if(set.getNumRows() < 1)
	  		  {
		          res = 1; 
		          mensaje.append("PRECAUCION: La clave del sub producto del proceso no existe, está descontinuado ó es materia prima (No se produce)<br>");
		          return res;
	  		  }
	  		  
	  		  idsubprod = request.getParameter("idsubprod");
	  		  rendimientosp = JUtil.redondear(rendimientosp, 3);
	  		  mmrendimientosp = JUtil.redondear(mmrendimientosp, 3);
	  		  porcentajesp = JUtil.redondear(porcentajesp, 6);
	  		  unidad = set.getAbsRow(0).getID_UnidadSalida();
	  		  descripcion = set.getAbsRow(0).getDescripcion();
		  }
	
		  JProdFormulasSesPartProc part = new JProdFormulasSesPartProc(
				  nombre, tiempo, rendimientosp, mmrendimientosp, idsubprod, descripcion, unidad, 
					porcentajesp, 0.0F, 0.0F, null, null);
		  m_Partidas.addElement(part);
		  establecerResultados();
		  
		  return res;
	  }	  
	  
	  
	  @SuppressWarnings("unchecked")
	  public short agregaPartidaDet(HttpServletRequest request, int idPartida, float cantidad, float masmenos, String idprod, boolean principal, StringBuffer mensaje) 
	  {
		  short res = -1;
		  
		  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
		  set.m_Where = "Clave = '" + JUtil.p(idprod) + "' AND ID_Tipo = 'P' AND Status = 'V'";
		  set.Open();
		  
		  if(!m_CantLotes)
	      {
	    	  if(masmenos < 0 || masmenos > cantidad)
	          {
	    		  res = 3; 
	    		  mensaje.append("ERROR: La diferencia de cantidad no puede ser menor que cero o mayor a la propia cantidad de la partida<br>");
	    		  return res;
	          }
	      }
	      else
	      {
	    	  if(masmenos != 0)
	          {
	    		  res = 3; 
	    		  mensaje.append("ERROR: La diferencia de cantidad de la partida, no puede ser diferente de cero en formulas simples <br>");
	    		  return res;
	          } 
	      }
		  
		  if( set.getNumRows() > 0 )
		  {
			 m_part_ID_Prod = idprod;
			 m_part_Unidad = set.getAbsRow(0).getID_UnidadSalida();
			 m_part_Descripcion = set.getAbsRow(0).getDescripcion();
			 
			 // Ahora hace las comparaciones.
			 float fcantidad, fcantidadXU, fmasmenos, fmasmenosXU;
			 
			 fcantidadXU = ((cantidad == 0) ? 0 : JUtil.redondear( (cantidad / m_Rendimiento),6) );
			 fcantidad = JUtil.redondear(cantidad,3);
			 fmasmenosXU = ((masmenos == 0) ? 0 : JUtil.redondear( (masmenos / m_Rendimiento),6) );
			 fmasmenos = JUtil.redondear(masmenos,3);
			
			 if(fcantidad < 0.0 || fcantidadXU < 0.0 )
			 {
			     res = 1;
			     mensaje.append("PRECAUCION: La cantidad no es correcta, No se agregó el material");
			 }
			 else
			 {
				 if(existeEnListaDet(idPartida))
				 {
				     res = 1;
				     mensaje.append("PRECAUCION: El producto ya existe en la lista, No se agregó la partida");
				 }
				 else
				 {
					 float cp, uc, cpt, uct;
					 cp = set.getAbsRow(0).getCostoPromedio();
					 uc = set.getAbsRow(0).getUltimoCosto();
					 cpt = JUtil.redondear(fcantidadXU * cp, 4);
					 uct = JUtil.redondear(fcantidadXU * uc, 4);

					 //Aqui aplica la partida
					 if(request.getParameter("proceso").equals("EDITAR_FORMULA"))
					 {
						 JProdFormulasSetDetprodV2 fp = new JProdFormulasSetDetprodV2(request);
						 fp.m_Where = "ID_Proceso = '" + m_ID_Proceso + "' and ID_Prod = '" + JUtil.p(m_part_ID_Prod) + "' and Principal = '0'";
						 fp.Open();
						 
						 if(fp.getNumRows() < 1) // No existe el producto como para agregarlo
						 {
							 res = 3; 
							 mensaje.append("ERROR: El producto que intentas agregar, no parece ser parte opcional de esta formula. Revisa la formula y cambiala en caso de ser necesario para poder agregar el producto<br>");
							 return res;
						 }
						 else
						 {
							 if(fcantidadXU < (fp.getAbsRow(0).getCantidad() - fp.getAbsRow(0).getMasMenos()) || fcantidadXU > (fp.getAbsRow(0).getCantidad() + fp.getAbsRow(0).getMasMenos()))
							 {
								 res = 3; 
								 mensaje.append("ERROR: La cantidad, no es correcta, Esta parece ser menor o mayor a la cantidad ( junto con sus diferencias aceptables ) de la fórmula. No se agrego el producto<br>");
								 return res;
							 }
						 }
					 }
					 
					 JProdFormulasSesPart part = new JProdFormulasSesPart(fcantidadXU, fcantidad, fmasmenosXU, fmasmenos, m_part_ID_Prod, m_part_Descripcion, m_part_Unidad, cp, uc, cpt, uct, principal);
					 getPartida(idPartida).getPartidas().addElement(part);
					 establecerResultados();
					 resetearPart();
				 }
			 } 

		  }
		  else
		  {
			 resetearPart();
			 
		     res = 3;
		     mensaje.append("ERROR: No se encontró el producto especificado de la partida, o la clave pertenece a un servicio");
		  }

		  return res;
	  }
	 
	  public boolean existeEnListaDet(int idPartida)
	  {
		  
		boolean res = false;  
	  
		for(int i = 0; i < getPartida(idPartida).getPartidas().size(); i++)
		{
		  	JProdFormulasSesPart part = (JProdFormulasSesPart) getPartida(idPartida).getPartidas().elementAt(i);
		  	String clave = part.getID_Prod();
		  	
		  	if(clave.compareToIgnoreCase(m_part_ID_Prod) == 0)
		  	{
		  		res = true;
		  		break;
		  	}
		}
		
		return res;
		
	  }
	  
	  public void establecerResultados()
	  {
		  float total = 0.0F;
		  float rendimiento = m_Rendimiento;
		  float CPT = 0.0F, UCT = 0.0F;
		  
		  for(int p = 0; p < m_Partidas.size(); p++)
		  {
			  JProdFormulasSesPartProc part = (JProdFormulasSesPartProc) m_Partidas.elementAt(p);
			  rendimiento += part.getCantidad();
			  
			  float cpt = 0.0F, uct = 0.0F;
			  
			  for(int i = 0; i < part.getPartidas().size(); i++)
			  {
				  JProdFormulasSesPart partdet = (JProdFormulasSesPart)part.getPartidas().elementAt(i);
				  total += partdet.getCantidad();
				  cpt += partdet.getCPT();
				  uct += partdet.getUCT();
			  }
			  part.setCPT(JUtil.redondear(cpt,4));
			  part.setUCT(JUtil.redondear(uct,4));
			  CPT += cpt;
			  UCT += uct;
		  }
	    
		  m_Merma = JUtil.redondear(rendimiento - total, 3);
		  m_CPT = CPT;
		  m_UCT = UCT;
		
	  }
	 

	  private void resetearPart()
	  {
		  m_part_ID_Prod = "";
		  m_part_Descripcion = "";
		  m_part_Unidad = "";
	  }
	  
	  public void resetear()
	  {
		  m_ID_Prod = "";
		  m_ID_Formula = 0;
		  m_ID_Proceso = 0;
		  m_Formula = "";
		  m_Descripcion = "";
		  m_Unidad = "";
		  m_FormulaNum = 0;
		  m_Rendimiento = 0.0F;
		  m_MasMenos = 0.0F;
		  m_CantLotes = false; 
		  m_Merma = 0.0F;
		  m_CPT = 0.0F;
		  m_UCT = 0.0F;
		  
		  resetearPart();
		  
		  super.resetear();
	  }
	  
	  public short agregaCabecero(HttpServletRequest request, StringBuffer sb_mensaje) 
	  	throws ServletException, IOException
	  {
		  short res = -1;
		  
	      if(request.getParameter("idprod") == null || request.getParameter("rendimiento") == null || request.getParameter("mmrendimiento") == null || request.getParameter("formula") == null  || 
	          	request.getParameter("idprod").equals("") || request.getParameter("rendimiento").equals("")  || request.getParameter("mmrendimiento").equals("") || request.getParameter("formula").equals(""))
	      {
	          res = 3; 
	          sb_mensaje.append("ERROR: Alguno de los parámetros del cabecero es nulo <br>");
	          return res;
	      }
	      
	      boolean cantlotes = m_CantLotes;
	      m_Rendimiento = Float.parseFloat(request.getParameter("rendimiento"));
	      m_MasMenos =  Float.parseFloat(request.getParameter("mmrendimiento"));
	      m_Formula = request.getParameter("formula");
	      m_CantLotes = (request.getParameter("cantlotes") != null) ? true : false;
	      
	      if(!m_CantLotes)
	      {
	    	  if(m_MasMenos < 0 || m_MasMenos > m_Rendimiento)
	          {
	    		  res = 3; 
	    		  sb_mensaje.append("ERROR: La diferencia de rendimiento no puede ser menor que cero o mayor al propio rendimiento<br>");
	    		  return res;
	          }
	      }
	      else
	      {
	    	  if(m_MasMenos != 0)
	          {
	    		  res = 3; 
	    		  sb_mensaje.append("ERROR: La diferencia de rendimiento no puede ser diferente de cero en formulas simples <br>");
	    		  return res;
	          } 
	      }
	      
	      if(m_CantLotes != cantlotes && m_Partidas.size() > 0)
  		  {
  			  super.resetear();
  			  res = 1;
  			  sb_mensaje.append("PRECAUCION: El tipo de formula se ha cambiado, esto generó que las partidas se hayan borrado. Para que esto no te vuelva a suceder, primero debes establecer el tipo de formula ( simple o compuesta ).");
  		  }
	      
	      if(!request.getParameter("proceso").equals("AGREGAR_FORMULA") && !request.getParameter("idprod").equals(m_ID_Prod))
	      {
	          res = 3; 
	          sb_mensaje.append("ERROR: El producto no se puede cambiar cuando se está editando una fórmula<br>");
	          return res;
	      }
	      
	      if(!request.getParameter("idprod").equals(m_ID_Prod))
	      {
	    	  JPublicInvServInvCatalogSetV2 set = new JPublicInvServInvCatalogSetV2(request);
	  		  set.m_Where = "Clave = '" + JUtil.p(request.getParameter("idprod")) + "' AND ID_Tipo = 'P' AND Status = 'V' AND SeProduce = '1'";
	  		  set.Open();
	  		  if(set.getNumRows() < 1)
	  		  {
		          res = 1; 
		          sb_mensaje.append("PRECAUCION: La clave del producto del cabecero no existe, está descontinuado ó es materia prima (No se produce)<br>");
		          return res;
	  		  }
	       
	  		  if(m_Partidas.size()  > 0)
	  		  {
	  			  super.resetear();
	  			  res = 1;
	  			  sb_mensaje.append("PRECAUCION: El producto se ha cambiado, esto generó que las paridas se hayan borrado. Para que esto no te vuelva a suceder, primero debd escoger el producto y luego agregar los materiales.");
	  		  }
	  		  
	  		  m_ID_Prod = set.getAbsRow(0).getClave();
	  		  m_Descripcion = set.getAbsRow(0).getDescripcion();
	  		  m_Unidad = set.getAbsRow(0).getID_Unidad();

	      }
	      
	      establecerResultados();
	      
	      return res;
	  }

	public void setUnidad(String unidad)
	{
		m_Unidad = unidad;
	}
	
	public String getUnidad()
	{
		return m_Unidad;
	}
	
	public boolean getCantLotes() 
	{
		return m_CantLotes;
	}

	public void setCantLotes(boolean cantLotes) 
	{
		m_CantLotes = cantLotes;
	}

	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public void setDescripcion(String descripcion) 
	{
		m_Descripcion = descripcion;
	}

	public String getFormula() 
	{
		return m_Formula;
	}

	public void setFormula(String formula) 
	{
		m_Formula = formula;
	}

	public int getFormulaNum() 
	{
		return m_FormulaNum;
	}

	public void setFormulaNum(int formulaNum) 
	{
		m_FormulaNum = formulaNum;
	}

	public long getID_Formula() 
	{
		return m_ID_Formula;
	}

	public long getID_Proceso() 
	{
		return m_ID_Proceso;
	}
	
	public void setID_Formula(long formula) 
	{
		m_ID_Formula = formula;
	}

	public void setID_Proceso(long proceso) 
	{
		m_ID_Proceso = proceso;
	}
	
	public String getID_Prod() 
	{
		return m_ID_Prod;
	}

	public float getCPT() 
	{
		return m_CPT;
	}

	public float getUCT() 
	{
		return m_UCT;
	}

	public void setID_Prod(String prod) 
	{
		m_ID_Prod = prod;
	}

	public float getMerma() 
	{
		return m_Merma;
	}

	public void setMerma(float merma) 
	{
		m_Merma = merma;
	}

	public float getRendimiento() 
	{
		return m_Rendimiento;
	}

	public void setRendimiento(float rendimiento) 
	{
		m_Rendimiento = rendimiento;
	}

	public void setMasMenos(float masmenos) 
	{
		m_MasMenos = masmenos;
	}
	
	public float getMasMenos() 
	{
		return m_MasMenos;
	}

}