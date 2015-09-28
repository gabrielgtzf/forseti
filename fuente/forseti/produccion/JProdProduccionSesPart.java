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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import forseti.JUtil;
import forseti.sets.JProdFormulasSetDetprodV2;
import forseti.sets.JProdFormulasSetProcV2;
import forseti.sets.JProdFormulasSetV2;

public class JProdProduccionSesPart 
{
	private String m_ID_Prod;
	private String m_Descripcion;
	private long m_ID_Formula;
	private String m_Formula;
	private float  m_Cantidad;
	private float  m_MasMenos;
	private String m_Unidad;
	private String m_Lote;
	private boolean m_CantLotes;
	private String m_Obs;
	private int m_NumProc;
	private int m_ActualProc;
	private boolean m_Terminada;
	private Date m_Fecha;
	
	private JProdFormulasSes m_rec;
	
	public JProdProduccionSesPart()
	{
		
	}
	
	public JProdProduccionSesPart(String ID_Prod, String Descripcion, long ID_Formula, String Formula, float Cantidad, String Unidad, String Lote, boolean CantLotes, String Obs, float MasMenos, int NumProc, int ActualProc, boolean Terminada, Date Fecha)
	{
		m_Lote = Lote;
		m_Formula = Formula;
		m_ID_Formula = ID_Formula;
		m_Cantidad = Cantidad;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CantLotes = CantLotes;
		m_Obs = Obs;
		m_NumProc = NumProc;
		m_ActualProc = ActualProc;
		m_Terminada = Terminada;
		m_Fecha = Fecha;
		
		m_rec = new JProdFormulasSes();
		
	}
	
	public JProdProduccionSesPart(HttpServletRequest request, String ID_Prod, String Descripcion, long ID_Formula, String Formula, float Cantidad, String Unidad, String Lote, boolean CantLotes, String Obs, float MasMenos)
	{
		m_Lote = Lote;
		m_Formula = Formula;
		m_ID_Formula = ID_Formula;
		m_Cantidad = Cantidad;
		m_MasMenos = MasMenos;
		m_ID_Prod = ID_Prod;
		m_Descripcion = Descripcion;
		m_Unidad = Unidad;
		m_CantLotes = CantLotes;
		m_Obs = Obs;
		m_NumProc = 0;
		m_ActualProc = 0;
		m_Terminada = false;
		m_Fecha = null;
		
		m_rec = new JProdFormulasSes();
		
		LLenarFormula(request);
		
	}
	
	
	
	public void LLenarFormula(HttpServletRequest request)
	{
		// aqui debe llenar la formula
        JProdFormulasSetV2 SetMod = new JProdFormulasSetV2(request);
        JProdFormulasSetProcV2 SetPrc = new JProdFormulasSetProcV2(request);
        SetMod.m_Where = "ID_Formula = '" + m_ID_Formula + "'";
        SetMod.Open();
        SetPrc.m_Where = "ID_Formula = '" + m_ID_Formula + "'";
        SetPrc.m_OrderBy = "ID_Proceso ASC";
        SetPrc.Open();
        
        m_NumProc = (int)SetMod.getAbsRow(0).getNumProc();
        
		m_rec.setID_Formula(SetMod.getAbsRow(0).getID_Formula());
		m_rec.setDescripcion(SetMod.getAbsRow(0).getDescripcion());
		m_rec.setID_Prod(SetMod.getAbsRow(0).getClave());
		m_rec.setFormula(SetMod.getAbsRow(0).getFormula());
		m_rec.setRendimiento(SetMod.getAbsRow(0).getCantidad());
		m_rec.setMasMenos(SetMod.getAbsRow(0).getMasMenos());
		m_rec.setUnidad(SetMod.getAbsRow(0).getUnidad());
		m_rec.setCantLotes(SetMod.getAbsRow(0).getUnidadUnica());
		m_rec.setID_Proceso(SetMod.getAbsRow(0).getID_Formula());
		
    	for (int p = 0; p < SetPrc.getNumRows(); p++)
    	{
       		
            JProdFormulasSesPartProc sesproc = m_rec.agregaPartida(SetPrc.getAbsRow(p).getNombre(), SetPrc.getAbsRow(p).getTiempo(), SetPrc.getAbsRow(p).getCantidad(), SetPrc.getAbsRow(p).getMasMenos(), (SetPrc.getAbsRow(p).getID_SubProd() == null ? "" : SetPrc.getAbsRow(p).getID_SubProd()), (SetPrc.getAbsRow(p).getDescripcion() == null ? "" : SetPrc.getAbsRow(p).getDescripcion()), (SetPrc.getAbsRow(p).getUnidad() == null ? "" : SetPrc.getAbsRow(p).getUnidad()), SetPrc.getAbsRow(p).getPorcentaje(), null, null);
            
            JProdFormulasSetDetprodV2 SetDet = new JProdFormulasSetDetprodV2(request);
            SetDet.m_Where = "ID_Proceso = '" + SetPrc.getAbsRow(p).getID_Proceso() + "'";
            SetDet.Open();

            for(int j = 0; j< SetDet.getNumRows(); j++)
            {
            	float fcantidadtotal = ( m_CantLotes ) ? 
            			JUtil.redondear(m_Cantidad * SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getCantidad(),3) 
            			: JUtil.redondear(SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getCantidad(),3);
            	
            	float fmasmenostotal = ( m_CantLotes ) ? 
                   		JUtil.redondear(m_Cantidad * SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getMasMenos(),3) 
                   		: JUtil.redondear(SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getMasMenos(),3);
               	
            	m_rec.agregaPartidaDet(sesproc, SetDet.getAbsRow(j).getCantidad(), fcantidadtotal, 
    							SetDet.getAbsRow(j).getMasMenos(), fmasmenostotal,
  																	SetDet.getAbsRow(j).getID_Prod(), SetDet.getAbsRow(j).getDescripcion(), SetDet.getAbsRow(j).getUnidad(),
  																	SetDet.getAbsRow(j).getCP(), SetDet.getAbsRow(j).getUC(), JUtil.redondear(SetDet.getAbsRow(j).getCP() * SetDet.getAbsRow(j).getCantidad(),4), JUtil.redondear(SetDet.getAbsRow(j).getUC() * SetDet.getAbsRow(j).getCantidad(),4), SetDet.getAbsRow(j).getPrincipal());
            }
    	}
    	
    	m_rec.establecerResultados();
    	
        // aqui debe llenar la formula version anterior
    	/*
        JProdFormulasSetV2 SetMod = new JProdFormulasSetV2(request);
        JProdFormulasSet
        JProdFormulasSetDetprodV2 SetDet = new JProdFormulasSetDetprodV2(request);
        SetMod.m_Where = "ID_Formula = " + m_ID_Formula;
        SetMod.Open();
        SetDet.m_Where = "ID_Proceso = " + m_ID_Formula + " and Principal = 1";
        SetDet.m_OrderBy = "ID_Prod ASC";
        SetDet.Open();
        
        // Teniendo la formula, puede checar el MasMenos del rendimiento
        m_MasMenos = SetMod.getAbsRow(0).getMasMenos();
        
        m_rec.setID_Formula(m_ID_Formula);
    	m_rec.setDescripcion(m_Descripcion);
    	m_rec.setID_Prod(m_ID_Prod);
    	m_rec.setFormula(m_Formula);
    	m_rec.setRendimiento(m_Cantidad);
    	m_rec.setMasMenos(m_MasMenos);
    	m_rec.setUnidad(m_Unidad);
    	m_rec.setCantLotes(m_CantLotes);
    	m_rec.setID_Proceso(m_ID_Formula);
    		
    		
    	for(int j = 0; j< SetDet.getNumRows(); j++)
    	{
    		float fcantidadtotal = ( m_CantLotes ) ? 
        			JUtil.redondear(m_Cantidad * SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getCantidad(),3) 
        			: JUtil.redondear(SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getCantidad(),3);
        	
        	float fmasmenostotal = ( m_CantLotes ) ? 
               		JUtil.redondear(m_Cantidad * SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getMasMenos(),3) 
               		: JUtil.redondear(SetMod.getAbsRow(0).getCantidad() * SetDet.getAbsRow(j).getMasMenos(),3);
           	
            m_rec.agregaPartida(SetDet.getAbsRow(j).getCantidad(), fcantidadtotal, 
								SetDet.getAbsRow(j).getMasMenos(), fmasmenostotal, 
  								SetDet.getAbsRow(j).getID_Prod(), SetDet.getAbsRow(j).getDescripcion(), SetDet.getAbsRow(j).getUnidad(),
  								SetDet.getAbsRow(j).getCP(), SetDet.getAbsRow(j).getUC(), JUtil.redondear(SetDet.getAbsRow(j).getCP() * SetDet.getAbsRow(j).getCantidad(),4), JUtil.redondear(SetDet.getAbsRow(j).getUC() * SetDet.getAbsRow(j).getCantidad(),4), SetDet.getAbsRow(j).getPrincipal());
    		
    	}
    	m_rec.establecerResultados();
    	*/
	
	}
	 
	public JProdFormulasSes getPartidaFormula()
	{
		return m_rec;
	}
	
	public String getObs()
	{
		return m_Obs;
	}
	
	public long getID_Formula()
	{
		return m_ID_Formula;
	}
	
	public float getCantidad() 
	{
		return m_Cantidad;
	}
	
	public float getMasMenos() 
	{
		return m_MasMenos;
	}

	public String getDescripcion() 
	{
		return m_Descripcion;
	}

	public String getFormula() 
	{
		return m_Formula;
	}

	public String getID_Prod() 
	{
		return m_ID_Prod;
	}

	public String getLote() 
	{
		return m_Lote;
	}

	public String getUnidad() 
	{
		return m_Unidad;
	}

	public int getNumProc()
	{
		return m_NumProc;
	}
	
	public int getActualProc()
	{
		return m_ActualProc;
	}
	
	public boolean getTerminada()
	{
		return m_Terminada;
	}
	
	public Date getFecha()
	{
		return m_Fecha;
	}
}
