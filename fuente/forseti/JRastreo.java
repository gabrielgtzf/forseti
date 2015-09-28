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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import forseti.sets.JAdmBancosCuentasSet;
import forseti.sets.JAdmComprasEntidades;
import forseti.sets.JAdmInvservCostosConceptosSet;
import forseti.sets.JAdmProduccionEntidades;
import forseti.sets.JAdmVentasEntidades;
import forseti.sets.JAlmChFisDetSet;
import forseti.sets.JAlmacenesCHFISSet;
import forseti.sets.JAlmacenesMovBodSetDetallesV2;
import forseti.sets.JAlmacenesMovBodSetV2;
import forseti.sets.JAlmacenesMovReqSet;
import forseti.sets.JAlmacenesMovimPlantSet;
import forseti.sets.JAlmacenesMovimSetDetallesV2;
import forseti.sets.JAlmacenesMovimSetV2;
import forseti.sets.JBancosSetMovsV2;
import forseti.sets.JBancosTransferenciasSet;
import forseti.sets.JClientCXCSetV2;
import forseti.sets.JComprasDevolucionesPagos;
import forseti.sets.JComprasDevolucionesSet;
import forseti.sets.JComprasFactSet;
import forseti.sets.JComprasFactSetCab;
import forseti.sets.JComprasFactSetDet;
import forseti.sets.JComprasFacturasPagos;
import forseti.sets.JComprasGastosPagos;
import forseti.sets.JComprasGastosSet;
import forseti.sets.JComprasOrdenesSet;
import forseti.sets.JComprasRecepSetV2;
import forseti.sets.JContPolizasDetalleCEChequesSet;
import forseti.sets.JContPolizasDetalleCEComprobantesSet;
import forseti.sets.JContPolizasDetalleCEOtrMetodoPagoSet;
import forseti.sets.JContPolizasDetalleCETransferenciasSet;
import forseti.sets.JContaPolizasClasificacionesSet;
import forseti.sets.JContaPolizasDetalleCESet;
import forseti.sets.JContaPolizasSetV2;
import forseti.sets.JProdProdSetDetV2;
import forseti.sets.JProdProdSetDetprodV2;
import forseti.sets.JProdProdSetProcV2;
import forseti.sets.JProdProdSetV2;
import forseti.sets.JProveeCXPSetV2;
import forseti.sets.JPublicContMonedasSetV2;
import forseti.sets.JUsuariosPermisosCatalogoSet;
import forseti.sets.JVentasCotizacionesSet;
import forseti.sets.JVentasDevolucionesPagos;
import forseti.sets.JVentasDevolucionesSet;
import forseti.sets.JVentasFactSetCabV2;
import forseti.sets.JVentasFactSetDetV2;
import forseti.sets.JVentasFactSetV2;
import forseti.sets.JVentasFacturasPagos;
import forseti.sets.JVentasPedidosSet;
import forseti.sets.JVentasRemisionesSet;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JRastreo 
{
	private Vector m_Niv;
	private String m_Titulo;
	
	public int getNiveles()
	{
		//return m_Niveles;
		return m_Niv.size();
	}
	
	public void _POLZ(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		_llenaPOLZ(request, Niv, ID);
	}
	
	public void _VCXC(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2, JRastreoNiv Niv3)
	{
		_llenaVCXC(request, Niv, ID);
		
		JClientCXCSetV2 set = new JClientCXCSetV2(request);
		set.m_Where = "Clave = '" + JUtil.p(ID) + "'";
		set.Open();
		
		if(set.getAbsRow(0).getID_PagoBanCaj() > 0)
		{
			if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			}
			_MBANCAJ(request, Niv2, Long.toString(set.getAbsRow(0).getID_PagoBanCaj()), Niv3);
		}
		else if(set.getAbsRow(0).getID_Pol() != -1)
	    {
			if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			}
			_POLZ(request, Niv2, Long.toString(set.getAbsRow(0).getID_Pol()));
	    }
	}
	
	public void _CCXP(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2, JRastreoNiv Niv3)
	{
		_llenaCCXP(request, Niv, ID);
		
		JProveeCXPSetV2 set = new JProveeCXPSetV2(request);
		set.m_Where = "Clave = '" + JUtil.p(ID) + "'";
		set.Open();
		
		if(set.getAbsRow(0).getID_PagoBanCaj() > 0)
		{
			if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			}
			_MBANCAJ(request, Niv2, Long.toString(set.getAbsRow(0).getID_PagoBanCaj()), Niv3);
		}
		else if(set.getAbsRow(0).getID_Pol() != -1)
	    {
			if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			}
			_POLZ(request, Niv2, Long.toString(set.getAbsRow(0).getID_Pol()));
	    }
	}
	
	public void _MALM(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		_llenaMALM(request, Niv, ID);
		
		JAlmacenesMovimSetV2 set = new JAlmacenesMovimSetV2(request);
		set.m_Where = "ID_Movimiento = '" + JUtil.p(ID) + "'";
		set.Open();
		
		if(set.getAbsRow(0).getID_Pol() != -1)
	    {
			if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			}
			_POLZ(request, Niv2, Long.toString(set.getAbsRow(0).getID_Pol()));
	    }	
	}
	
	public void _PPRD(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		JRastreoNiv Niv2 = null, Niv3 = null;
		_llenaPPRD(request, Niv, ID);
		
		JProdProdSetDetV2 SetDet = new JProdProdSetDetV2(request);
		SetDet.m_Where = "ID_Reporte = '" + JUtil.p(ID) + "'";
        SetDet.m_OrderBy = "Partida ASC";
		SetDet.Open();
		              				
		for(int d = 0; d < SetDet.getNumRows(); d++)
		{
			
			JProdProdSetProcV2 SetProc = new JProdProdSetProcV2(request);
            SetProc.m_Where = "ID_Reporte = '" + JUtil.p(ID) + "' AND Partida = '" + (d+1) + "'";
			SetProc.m_OrderBy = "ID_Proceso ASC";
       		SetProc.Open();
       		
			for(int p = 0; p < SetProc.getNumRows(); p++)
			{
				if(SetProc.getAbsRow(p).getID_Pol() != -1)
		        {
					JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
		        	mov.m_Where = "ID_Movimiento = '" + SetProc.getAbsRow(p).getID_Pol() + "'"; 
		   		 	mov.Open();
		   		 	if(Niv2 == null)
		    		{
		   		 		Niv2 = new JRastreoNiv();
		   		 		m_Niv.addElement(Niv2);
		   		 		Niv3 = new JRastreoNiv();
		   		 		m_Niv.addElement(Niv3);
	    			}
		   		 	_MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), Niv3);      	        
			    }
				
				if(SetProc.getAbsRow(p).getID_PolSP() != -1)
		        {
					JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
		        	mov.m_Where = "ID_Movimiento = '" + SetProc.getAbsRow(p).getID_PolSP() + "'";
		   		 	mov.Open();
		   		 	if(Niv2 == null)
		    		{
		   		 		Niv2 = new JRastreoNiv();
		   		 		m_Niv.addElement(Niv2);
		   		 		Niv3 = new JRastreoNiv();
		   		 		m_Niv.addElement(Niv3);
		    		}
		   		 	_MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), Niv3);      	        
			    }
			}
			
			if(SetDet.getAbsRow(d).getID_Pol() != -1)
	        {
				JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
	        	mov.m_Where = "ID_Movimiento = '" + SetDet.getAbsRow(d).getID_Pol() + "'"; 
	   		 	mov.Open();
	   		 	if(Niv2 == null)
	    		{
	    			Niv2 = new JRastreoNiv();
	    			m_Niv.addElement(Niv2);
	    			Niv3 = new JRastreoNiv();
	    			m_Niv.addElement(Niv3);
	    		}
	   		 	_MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), Niv3);      	        
		    }	
        	
        }
	}

	public void _PALM(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		JRastreoNiv Niv2 = null;
		_llenaPALM(request, Niv, ID);
    	
    	JAlmacenesMovimPlantSet set = new JAlmacenesMovimPlantSet(request);
    	set.m_Where = "ID_MovimPlant = '" + JUtil.p(ID) + "'";
		set.Open();
		
		if(set.getAbsRow(0).getID_Movimiento() != 0)
        {
			JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
        	mov.m_Where = "ID_Movimiento = '" + set.getAbsRow(0).getID_Movimiento() + "'";
   		 	mov.Open();
   		 	Niv2 = new JRastreoNiv();
   		 	m_Niv.addElement(Niv2); //1 nivel, 0 elementos nivel 1
			_MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), null);      	        
	    }	        
		
	}

	public void _TALM(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		JRastreoNiv Niv2 = new JRastreoNiv();
		JRastreoNiv Niv3 = new JRastreoNiv();
   		_llenaTALM(request, Niv, ID);
    	
    	JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
        mov.m_Where = "REF LIKE 'TALM|" + JUtil.p(ID) + "|%'";
   		mov.Open();
        	
   		m_Niv.addElement(Niv2);
   		m_Niv.addElement(Niv3);
        _MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), Niv3);
        _MALM(request, Niv2, Long.toString(mov.getAbsRow(1).getID_Movimiento()), Niv3);
	}

	public void _RALM(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		JRastreoNiv Niv2 = null;
		_llenaRALM(request, Niv, ID);
	
    	JAlmacenesMovReqSet set = new JAlmacenesMovReqSet(request);
    	set.m_Where = "ID_Movimiento = '" + JUtil.p(ID) + "'";
		set.Open();
		
		if(set.getAbsRow(0).getID_Traspaso() != 0)
        {
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
	        _TALM(request, Niv2, Long.toString(set.getAbsRow(0).getID_Traspaso()));
		}
	}

	public void _CHFI(HttpServletRequest request, JRastreoNiv Niv, String ID)
	{
		JRastreoNiv Niv2 = null, Niv3 = null;
		_llenaCHFI(request, Niv, ID);

		JAlmacenesCHFISSet set = new JAlmacenesCHFISSet(request);
    	set.m_Where = "ID_CHFIS = '" + JUtil.p(ID) + "'";
		set.Open();
		 	
		if(set.getAbsRow(0).getGenerado())
		{
			JAlmacenesMovimSetV2 mov = new JAlmacenesMovimSetV2(request);
			mov.m_Where = "REF LIKE 'CHFI|" + JUtil.p(ID) + "|%'";
			mov.Open();
        	
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
			Niv3 = new JRastreoNiv();
			m_Niv.addElement(Niv3);
			_MALM(request, Niv2, Long.toString(mov.getAbsRow(0).getID_Movimiento()), null);
			_MALM(request, Niv2, Long.toString(mov.getAbsRow(1).getID_Movimiento()), null);
		}
	}
	
	public void _MBANCAJ(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		System.out.println("POL_ID: " + ID);
		_llenaMBANCAJ(request, Niv, ID);
		System.out.println("POL_ID: " + ID);
		JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
        set.m_Where = "ID = '" + JUtil.p(ID) + "'";
        set.Open();
		JBancosTransferenciasSet bt = new JBancosTransferenciasSet(request);
    	bt.m_Where = "IDMovOrigen = '" + JUtil.p(ID) + "'";
    	bt.Open();
    	if(set.getAbsRow(0).getPol_ID() != -1)
        {
    		System.out.println("POL_ID: " + set.getAbsRow(0).getPol_ID());
    		if(Niv2 == null)
    		{
    			Niv2 = new JRastreoNiv();
    			m_Niv.addElement(Niv2);
    		}
			_POLZ(request, Niv2, Long.toString(set.getAbsRow(0).getPol_ID()));
        }	        
        if(set.getAbsRow(0).getEsTrans() && bt.getNumRows() > 0) // es el retiro de la transferencia de fondos: El Movimiento de origen
        {
        	_llenaMBANCAJ(request, Niv, Integer.toString(bt.getAbsRow(0).getIDMovDestino()));
        	
        	JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
	        set2.m_Where = "ID = '" + bt.getAbsRow(0).getIDMovDestino() + "'";
	        set2.Open();
	        
	        if(set2.getAbsRow(0).getPol_ID() != -1)
        	{
	        	if(Niv2 == null)
	        	{
	        		Niv2 = new JRastreoNiv();
	    			m_Niv.addElement(Niv2); 
	        	}
        		_POLZ(request, Niv2, Long.toString(set2.getAbsRow(0).getPol_ID()));
	        }
		}
	}

	public void _VDEV(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2, JRastreoNiv Niv3)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		_llenaVDEV(request, Niv, ID);
		
    	//Agrega bancos o cxc y movimientos si los hay
    	JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
    	SetMod.m_Where = "ID_Devolucion = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"DEVOLUCIONES");
    	SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
    	SetCab.Open();
	
    	if(SetCab.getAbsRow(0).getCondicion() == 0) // es contado
    	{
    		JVentasDevolucionesPagos sban = new JVentasDevolucionesPagos(request);
    		sban.m_Where = "ID_Devolucion = '" + JUtil.p(ID) + "'";
    		sban.m_OrderBy = "ID_Mov asc";
    		sban.Open();
    		long ID_Pol = -1;
    		for(int i = 0; i < sban.getNumRows(); i++)
    		{
    			_llenaMBANCAJ(request, Niv2, Integer.toString(sban.getAbsRow(i).getID_Mov()));
    			JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
    			set2.m_Where = "ID = '" + sban.getAbsRow(i).getID_Mov() + "'";
    			set2.Open();
	        
    			if(set2.getAbsRow(0).getPol_ID() != -1 && ID_Pol == -1)
    				ID_Pol = set2.getAbsRow(0).getPol_ID();
		 	}
    		if(ID_Pol != -1)
			{
    			if(Niv3 == null)
    			{
    				Niv3 = new JRastreoNiv();
    				m_Niv.addElement(Niv3);
    			}
				_POLZ(request, Niv3, Long.toString(ID_Pol));
			}
		}
		else
		{
			if(Niv3 == null)
			{
				Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
			}
			_VCXC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Pol()), Niv3, null);
		}
		 	
		if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		{
			_MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		}
    	
	}

	public void _CDEV(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2, JRastreoNiv Niv3)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		_llenaCDEV(request, Niv, ID);
		
    	//Agrega bancos o cxc y movimientos si los hay
    	JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
    	SetMod.m_Where = "ID_Devolucion = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"DEVOLUCIONES");
    	SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
    	SetCab.Open();
	
    	if(SetCab.getAbsRow(0).getCondicion() == 0) // es contado
    	{
    		JComprasDevolucionesPagos sban = new JComprasDevolucionesPagos(request);
    		sban.m_Where = "ID_Devolucion = '" + JUtil.p(ID) + "'";
    		sban.m_OrderBy = "ID_Mov asc";
    		sban.Open();
    		long ID_Pol = -1;
    		for(int i = 0; i < sban.getNumRows(); i++)
    		{
    			_llenaMBANCAJ(request, Niv2, Integer.toString(sban.getAbsRow(i).getID_Mov()));
    			JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
    			set2.m_Where = "ID = '" + sban.getAbsRow(i).getID_Mov() + "'";
    			set2.Open();
	        
    			if(set2.getAbsRow(0).getPol_ID() != -1 && ID_Pol == -1)
    				ID_Pol = set2.getAbsRow(0).getPol_ID();
		 	}
    		if(ID_Pol != -1)
			{
    			if(Niv3 == null)
    			{
    				Niv3 = new JRastreoNiv();
    				m_Niv.addElement(Niv3);
    			}
				_POLZ(request, Niv3, Long.toString(ID_Pol));
			}
		}
		else
		{
			if(Niv3 == null)
			{
				Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
			}
			_CCXP(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Pol()), Niv3, null);
		}
		 	
		if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		{
			_MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		}
    	
	}
	
	public void _VCOT(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		_llenaVCOT(request, Niv, ID);
		
    	JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
    	SetMod.m_Where = "ID_Cotizacion = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	  
    	if(SetMod.getAbsRow(0).getTipoEnlace() != null)
		{
    		if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2);
			}
			
			if(SetMod.getAbsRow(0).getTipoEnlace().equals("VPED"))
				_VPED(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
			else if(SetMod.getAbsRow(0).getTipoEnlace().equals("VREM"))
				_VREM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
			else //SetMod.getAbsRow(0).getTipoEnlace().equals("VFAC"))
				_VFAC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
		}
	}

	public void _VPED(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		_llenaVPED(request, Niv, ID);
		
    	JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
    	SetMod.m_Where = "ID_Pedido = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	    	 	
    	if(SetMod.getAbsRow(0).getTipoEnlace() != null)
		{
    		if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2);
			}
			
			if(SetMod.getAbsRow(0).getTipoEnlace().equals("VREM"))
				_VREM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
			else //SetMod.getAbsRow(0).getTipoEnlace().equals("VFAC"))
				_VFAC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
		}
    	
	}
	
	public void _CORD(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		_llenaCORD(request, Niv, ID);
		
    	JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
    	SetMod.m_Where = "ID_Orden = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	    	 	
    	if(SetMod.getAbsRow(0).getTipoEnlace() != null)
		{
    		if(Niv2 == null)
			{
				Niv2 = new JRastreoNiv();
				m_Niv.addElement(Niv2);
			}
			
    		
			if(SetMod.getAbsRow(0).getTipoEnlace().equals("CREC"))
				_CREC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
			else //SetMod.getAbsRow(0).getTipoEnlace().equals("CFAC"))
				_CFAC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), null);
			
		}
    	
	}

	public void _VREM(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		JRastreoNiv Niv3 = null;
		_llenaVREM(request, Niv, ID);
		
    	//Agrega bancos o cxc y movimientos si los hay
    	JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
    	SetMod.m_Where = "ID_Remision = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	    	 	
		if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		{
			Niv3 = new JRastreoNiv();
			m_Niv.addElement(Niv3);
			_MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		}
		
		if(SetMod.getAbsRow(0).getID_Factura() > 0)
		{
			if(Niv3 == null)
			{
				Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
			}
			_VFAC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), Niv3);
		}
    	
	}

	public void _CREC(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		JRastreoNiv Niv3 = null;
		_llenaCREC(request, Niv, ID);
		
    	//Agrega bancos o cxp y movimientos si los hay
    	JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
    	SetMod.m_Where = "ID_Recepcion = '" + JUtil.p(ID) + "'";
    	SetMod.Open();
    	    	 	
		if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		{
			Niv3 = new JRastreoNiv();
			m_Niv.addElement(Niv3);
			_MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		}
		
		if(SetMod.getAbsRow(0).getID_Factura() > 0)
		{
			if(Niv3 == null)
			{
				Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
			}
			_CFAC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Factura()), Niv3);
		}
    	
	}

	public void _VFAC(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		JRastreoNiv Niv3 = null, Niv4 = null, Niv5 = null;
		_llenaVFAC(request, Niv, ID);
    	
		//Agrega bancos o cxc y movimientos si los hay
    	JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
		SetMod.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetMod.Open();
		JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"FACTURAS");
		SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetCab.Open();
		JVentasDevolucionesSet SetDev = new JVentasDevolucionesSet(request);
		SetDev.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetDev.Open();
		JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		ent.Open();
		 
		if(SetCab.getAbsRow(0).getCondicion() == 0) // es contado
		{
			JVentasFacturasPagos sban = new JVentasFacturasPagos(request);
			sban.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
			sban.m_OrderBy = "ID_Mov asc";
			sban.Open();
			long ID_Pol = -1;
			for(int i = 0; i < sban.getNumRows(); i++)
		 	{
				// Directo a llenar Movimiento con _llenaMBANCAJ, para no duplicar poliza de cada pago con _MBANCAJ
				_llenaMBANCAJ(request, Niv2, Integer.toString(sban.getAbsRow(i).getID_Mov()));
				JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
		 		set2.m_Where = "ID = '" + sban.getAbsRow(i).getID_Mov() + "'";
		 		set2.Open();
	        
		 		if(set2.getAbsRow(0).getPol_ID() != -1 && ID_Pol == -1)
		 			ID_Pol = set2.getAbsRow(0).getPol_ID();
		 	}
		 	if(ID_Pol != -1)
			{
		 		Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
				_POLZ(request, Niv3, Long.toString(ID_Pol));
			}
		 }
		 else
		 {
			 Niv3 = new JRastreoNiv();
			 m_Niv.addElement(Niv3);
			 _VCXC(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Pol()), Niv3, null);
		 }

		 if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		 {
			 _MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		 }
		 
		 if(SetCab.getAbsRow(0).getCondicion() != 0) // es credito, verifica los pagos y asociaciones a la cxc
		 {
			 JClientCXCSetV2 set = new JClientCXCSetV2(request);
			 set.m_Where = "ID_Aplicacion = '" + SetMod.getAbsRow(0).getID_Pol() + "' and Ref = ''";
			 set.m_OrderBy = "Clave asc";
			 set.Open();
	
			 if(set.getNumRows() > 0)
			 {
				 Niv4 = new JRastreoNiv();
				 m_Niv.addElement(Niv4);
				 if(!ent.getAbsRow(0).getFija())
				 {
					 Niv5 = new JRastreoNiv();
				 	 m_Niv.addElement(Niv5);
				 }
				 for(int i = 0; i < set.getNumRows(); i++)
				 {
					 _VCXC(request, Niv3, Long.toString(set.getAbsRow(i).getClave()), Niv4, Niv5);
				 }
			 }
		 }
		 
		 if(SetDev.getNumRows() > 0)
		 {
			 if((SetDev.getAbsRow(0).getDevReb().equals("DEV") && !ent.getAbsRow(0).getFijaCost()) || !ent.getAbsRow(0).getFija())
			 {
				 if(Niv4 == null)
				 {
					 Niv4 = new JRastreoNiv();	
					 m_Niv.addElement(Niv4);
				 }
			 }
			 
			 for(int i = 0; i < SetDev.getNumRows(); i++)
			 {
				 _VDEV(request, Niv2, Long.toString(SetDev.getAbsRow(i).getID_Devolucion()), Niv3, Niv4);
			 }
		 }
	}

	public void _CFAC(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		JRastreoNiv Niv3 = null, Niv4 = null, Niv5 = null;
		_llenaCFAC(request, Niv, ID);
    	
		//Agrega bancos o cxc y movimientos si los hay
    	JComprasFactSet SetMod = new JComprasFactSet(request);
		SetMod.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetMod.Open();
		JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"FACTURAS");
		SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetCab.Open();
		JComprasDevolucionesSet SetDev = new JComprasDevolucionesSet(request);
		SetDev.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetDev.Open();
		JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		ent.Open();
		 
		if(SetCab.getAbsRow(0).getCondicion() == 0) // es contado
		{
			JComprasFacturasPagos sban = new JComprasFacturasPagos(request);
			sban.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
			sban.m_OrderBy = "ID_Mov asc";
			sban.Open();
			long ID_Pol = -1;
			for(int i = 0; i < sban.getNumRows(); i++)
		 	{
				// Directo a llenar Movimiento con _llenaMBANCAJ, para no duplicar poliza de cada pago con _MBANCAJ
				_llenaMBANCAJ(request, Niv2, Integer.toString(sban.getAbsRow(i).getID_Mov()));
				JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
		 		set2.m_Where = "ID = '" + sban.getAbsRow(i).getID_Mov() + "'";
		 		set2.Open();
	        
		 		if(set2.getAbsRow(0).getPol_ID() != -1 && ID_Pol == -1)
		 			ID_Pol = set2.getAbsRow(0).getPol_ID();
		 	}
		 	if(ID_Pol != -1)
			{
		 		Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
				_POLZ(request, Niv3, Long.toString(ID_Pol));
			}
		 }
		 else
		 {
			 Niv3 = new JRastreoNiv();
			 m_Niv.addElement(Niv3);
			 _CCXP(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Pol()), Niv3, null);
		 }

		 if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		 {
			 _MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		 }
		 
		 if(SetCab.getAbsRow(0).getCondicion() != 0) // es credito, verifica los pagos y asociaciones a la cxc
		 {
			 JProveeCXPSetV2 set = new JProveeCXPSetV2(request);
			 set.m_Where = "ID_Aplicacion = '" + SetMod.getAbsRow(0).getID_Pol() + "' and Ref = ''";
			 set.m_OrderBy = "Clave asc";
			 set.Open();
	
			 if(set.getNumRows() > 0)
			 {
				 Niv4 = new JRastreoNiv();
				 m_Niv.addElement(Niv4);
				 if(!ent.getAbsRow(0).getFija())
				 {
					 Niv5 = new JRastreoNiv();
				 	 m_Niv.addElement(Niv5);
				 }
				 for(int i = 0; i < set.getNumRows(); i++)
				 {
					 _CCXP(request, Niv3, Long.toString(set.getAbsRow(i).getClave()), Niv4, Niv5);
				 }
			 }
		 }
		 
		 if(SetDev.getNumRows() > 0)
		 {
			 if((SetDev.getAbsRow(0).getDevReb().equals("DEV") && !ent.getAbsRow(0).getFijaCost()) || !ent.getAbsRow(0).getFija())
			 {
				 if(Niv4 == null)
				 {
					 Niv4 = new JRastreoNiv();	
					 m_Niv.addElement(Niv4);
				 }
			 }
			 
			 for(int i = 0; i < SetDev.getNumRows(); i++)
			 {
				 _CDEV(request, Niv2, Long.toString(SetDev.getAbsRow(i).getID_Devolucion()), Niv3, Niv4);
			 }
		 }
	}
	
	public void _CGAS(HttpServletRequest request, JRastreoNiv Niv, String ID, JRastreoNiv Niv2)
	{
		if(Niv2 == null)
		{
			Niv2 = new JRastreoNiv();
			m_Niv.addElement(Niv2);
		}
		JRastreoNiv Niv3 = null, Niv4 = null, Niv5 = null;
		_llenaCGAS(request, Niv, ID);
    	
		//Agrega bancos o cxc y movimientos si los hay
    	JComprasGastosSet SetMod = new JComprasGastosSet(request);
		SetMod.m_Where = "ID_Gasto = '" + JUtil.p(ID) + "'";
		SetMod.Open();
		JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"GASTOS");
		SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID) + "'";
		SetCab.Open();
		JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		ent.Open();
		 
		if(SetCab.getAbsRow(0).getCondicion() == 0) // es contado
		{
			JComprasGastosPagos sban = new JComprasGastosPagos(request);
			sban.m_Where = "ID_Gasto = '" + JUtil.p(ID) + "'";
			sban.m_OrderBy = "ID_Mov asc";
			sban.Open();
			long ID_Pol = -1;
			for(int i = 0; i < sban.getNumRows(); i++)
		 	{
				// Directo a llenar Movimiento con _llenaMBANCAJ, para no duplicar poliza de cada pago con _MBANCAJ
				_llenaMBANCAJ(request, Niv2, Integer.toString(sban.getAbsRow(i).getID_Mov()));
				JBancosSetMovsV2 set2 = new JBancosSetMovsV2(request);
		 		set2.m_Where = "ID = '" + sban.getAbsRow(i).getID_Mov() + "'";
		 		set2.Open();
	        
		 		if(set2.getAbsRow(0).getPol_ID() != -1 && ID_Pol == -1)
		 			ID_Pol = set2.getAbsRow(0).getPol_ID();
		 	}
		 	if(ID_Pol != -1)
			{
		 		Niv3 = new JRastreoNiv();
				m_Niv.addElement(Niv3);
				_POLZ(request, Niv3, Long.toString(ID_Pol));
			}
		 }
		 else
		 {
			 Niv3 = new JRastreoNiv();
			 m_Niv.addElement(Niv3);
			 _CCXP(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_Pol()), Niv3, null);
		 }

		/* tadavia no soporta movimiento al almacen de utensilios
		 if(SetMod.getAbsRow(0).getID_PolCost() != -1)
		 {
			 _MALM(request, Niv2, Long.toString(SetMod.getAbsRow(0).getID_PolCost()), Niv3);
		 }
		 */
		 if(SetCab.getAbsRow(0).getCondicion() != 0) // es credito, verifica los pagos y asociaciones a la cxc
		 {
			 JProveeCXPSetV2 set = new JProveeCXPSetV2(request);
			 set.m_Where = "ID_Aplicacion = '" + SetMod.getAbsRow(0).getID_Pol() + "' and Ref = ''";
			 set.m_OrderBy = "Clave asc";
			 set.Open();
	
			 if(set.getNumRows() > 0)
			 {
				 Niv4 = new JRastreoNiv();
				 m_Niv.addElement(Niv4);
				 if(!ent.getAbsRow(0).getFija())
				 {
					 Niv5 = new JRastreoNiv();
				 	 m_Niv.addElement(Niv5);
				 }
				 for(int i = 0; i < set.getNumRows(); i++)
				 {
					 _CCXP(request, Niv3, Long.toString(set.getAbsRow(i).getClave()), Niv4, Niv5);
				 }
			 }
		 }
		 
		 
	}
	
	public void _llenaPOLZ(HttpServletRequest request, JRastreoNiv Niv, String ID_Pol) 
	{
		String coletq = JUtil.Msj("CEF","CONT_POLIZAS","DLG","COLUMNAS",1);
		String Tipos = JUtil.Msj("CEF","CONT_POLIZAS","DLG","TIPOS");
		String sts = JUtil.Msj("CEF", "CONT_POLIZAS", "VISTA", "STATUS",2);
		
		int numcols = JUtil.obtNumElm(coletq);
		
		JContaPolizasSetV2 set = new JContaPolizasSetV2(request);
		set.m_Where = "ID = '" + JUtil.p(ID_Pol) + "'";
		set.Open();
				
		JContaPolizasClasificacionesSet setc = new JContaPolizasClasificacionesSet(request);
		setc.m_Where = "ID_Clasificacion = '" + JUtil.p(set.getAbsRow(0).getID_Clasificacion()) + "'";
		setc.Open();
		
		String tipo,status,clasificacion;
		
		if(set.getAbsRow(0).getTipo().equals("DR")) 
			tipo = JUtil.Elm(Tipos,1);
		else if(set.getAbsRow(0).getTipo().equals("IG")) 
			tipo = JUtil.Elm(Tipos,2);
		else if(set.getAbsRow(0).getTipo().equals("EG"))
			tipo = JUtil.Elm(Tipos,3);
		else if(set.getAbsRow(0).getTipo().equals("AJ"))
			tipo = JUtil.Elm(Tipos,4);
		else if(set.getAbsRow(0).getTipo().equals("PE"))
			tipo = JUtil.Elm(Tipos,5);
		else
			tipo = "";
		
		if(set.getAbsRow(0).getEstatus().equals("G"))
			status = JUtil.Elm(sts,2); 
		else if(set.getAbsRow(0).getEstatus().equals("C"))
			status = JUtil.Elm(sts,3);
		else if(set.getAbsRow(0).getEstatus().equals("T"))
			status = JUtil.Elm(sts,4);
		else
			status = "";
			
		clasificacion = setc.getAbsRow(0).getDescripcion(); 
			
		int numcab = 0;
		JRastreoElm elm = new JRastreoElm(request, "CONT_POLIZAS",10,numcols);
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
        elm.cabsval[numcab++] = ID_Pol;
       	elm.cabsetq[numcab] = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TITULO",3); //Entidad
		elm.cabsval[numcab++] = clasificacion;
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
		elm.cabsval[numcab++] = tipo;
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
		elm.cabsval[numcab++] = Integer.toString(set.getAbsRow(0).getNum());
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
		elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
		elm.cabsetq[numcab] = JUtil.Msj("CEF","CONT_POLIZAS","VISTA","TITULO",5); //Status
		elm.cabsval[numcab++] = status;
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
		elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
		elm.cabsetq[numcab] = JUtil.Elm(coletq,6);
		elm.cabsval[numcab++] = JUtil.Converts(set.getAbsRow(0).getDebe(), ",", ".", 2, false);
		elm.cabsetq[numcab] = JUtil.Elm(coletq,7);
		elm.cabsval[numcab++] = JUtil.Converts(set.getAbsRow(0).getHaber(), ",", ".", 2, false);
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
		elm.cabsval[numcab++] = set.getAbsRow(0).getRef();
		
		for(int i = 0; i < numcols; i++ )
			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
		
		// Llena la poliza
		JContaPolizasDetalleCESet det = new JContaPolizasDetalleCESet(request);
        det.m_Where = "ID = '" + JUtil.p(ID_Pol) + "'";
        det.m_OrderBy = "Part ASC";
        det.Open();
        for(int i = 0; i < det.getNumRows(); i++)
        {
        	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
        	setMon.m_Where = "Clave = '" + det.getAbsRow(i).getMoneda() + "'";
        	setMon.Open();

        	String [] detalle = new String [] { JUtil.obtCuentaFormato(new StringBuffer(det.getAbsRow(i).getNumero()), request), 
        										det.getAbsRow(i).getNombre(), 
        										det.getAbsRow(i).getConcepto(),
        										JUtil.Converts(det.getAbsRow(i).getParcial(), ",", ".", 2, false), 
        										setMon.getAbsRow(0).getMoneda() + " " + JUtil.Converts(det.getAbsRow(0).getTC(), ",", ".", 4, false), 
        										JUtil.Converts(det.getAbsRow(i).getDebe(), ",", ".", 2, true), 
        										JUtil.Converts(det.getAbsRow(i).getHaber(), ",", ".", 2, true),
        										det.getAbsRow(i).getCE()};
        	
        	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	
        	JContPolizasDetalleCEChequesSet chq = new JContPolizasDetalleCEChequesSet(request);
        	chq.m_Where = "ID_Pol = '" + JUtil.p(ID_Pol) + "' and ID_Part = '" + det.getAbsRow(i).getPart() + "'";
        	chq.Open();
        	if(chq.getNumRows() > 0)
        	{
        		String [][] schq = new String [chq.getNumRows()][11];
        		
        		for(int j = 0; j < chq.getNumRows(); j++)
        		{
        			schq [j][0] = "CHQ:";
        			schq [j][1] = chq.getAbsRow(j).getNum();
        			schq [j][2] = chq.getAbsRow(j).getCtaOri();
        			schq [j][3] = chq.getAbsRow(j).getBanco();
        			schq [j][4] = chq.getAbsRow(j).getBanEmisExt();
        			schq [j][5] = JUtil.obtFechaTxt(chq.getAbsRow(j).getFecha(), "dd/MMM/yyyy");
        			schq [j][6] = JUtil.Converts(chq.getAbsRow(j).getMonto(), ",", ".", 2, true);
        			schq [j][7] = chq.getAbsRow(j).getMoneda();
        			schq [j][8] = JUtil.Converts(chq.getAbsRow(j).getTipCamb(), ",", ".", 4, true);
        			schq [j][9] = chq.getAbsRow(j).getBenef();
        			schq [j][10] = chq.getAbsRow(j).getRFC();
        		}
        		
        		detalles.cechq = schq;
            }
        	JContPolizasDetalleCETransferenciasSet trn = new JContPolizasDetalleCETransferenciasSet(request);
        	trn.m_Where = "ID_Pol = '" + JUtil.p(ID_Pol) + "' and ID_Part = '" + det.getAbsRow(i).getPart() + "'";
        	trn.Open();
        	if(trn.getNumRows() > 0)
        	{
        		String [][] strn = new String [trn.getNumRows()][13];
        		
        		for(int j = 0; j < trn.getNumRows(); j++)
        		{
        			strn [j][0] = "TRN:";
        			strn [j][1] = trn.getAbsRow(j).getCtaOri();
        			strn [j][2] = trn.getAbsRow(j).getBancoOri();
        			strn [j][3] = trn.getAbsRow(j).getBancoOriExt();
        			strn [j][4] = JUtil.Converts(trn.getAbsRow(j).getMonto(), ",", ".", 2, true);
        			strn [j][5] = trn.getAbsRow(j).getMoneda();
        			strn [j][6] = JUtil.Converts(trn.getAbsRow(j).getTipCamb(), ",", ".", 4, true);
        			strn [j][7] = trn.getAbsRow(j).getCtaDest();
        			strn [j][8] = trn.getAbsRow(j).getBancoDest();
        			strn [j][9] = trn.getAbsRow(j).getBancoDestExt();
        			strn [j][10] = JUtil.obtFechaTxt(trn.getAbsRow(j).getFecha(), "dd/MMM/yyyy");
        			strn [j][11] = trn.getAbsRow(j).getBenef();
        			strn [j][12] = trn.getAbsRow(j).getRFC();
        		}
        		
        		detalles.cetrn = strn;
            }
        	JContPolizasDetalleCEOtrMetodoPagoSet omp = new JContPolizasDetalleCEOtrMetodoPagoSet(request);
        	omp.m_Where = "ID_Pol = '" + JUtil.p(ID_Pol) + "' and ID_Part = '" + det.getAbsRow(i).getPart() + "'";
        	omp.Open();
        	if(omp.getNumRows() > 0)
        	{
        		String [][] somp = new String [omp.getNumRows()][8];
        		
        		for(int j = 0; j < omp.getNumRows(); j++)
        		{
        			somp [j][0] = "OMP:";
        			somp [j][1] = omp.getAbsRow(j).getMetPagoPol();
        			somp [j][2] = JUtil.obtFechaTxt(omp.getAbsRow(j).getFecha(), "dd/MMM/yyyy");
        			somp [j][3] = JUtil.Converts(omp.getAbsRow(j).getMonto(), ",", ".", 2, true);
        			somp [j][4] = omp.getAbsRow(j).getMoneda();
        			somp [j][5] = JUtil.Converts(omp.getAbsRow(j).getTipCamb(), ",", ".", 4, true);
        			somp [j][6] = omp.getAbsRow(j).getBenef();
        			somp [j][7] = omp.getAbsRow(j).getRFC();
        		}
       
        		detalles.ceomp = somp;
            }
        	JContPolizasDetalleCEComprobantesSet xml = new JContPolizasDetalleCEComprobantesSet(request);
        	xml.m_Where = "ID_Pol = '" + JUtil.p(ID_Pol) + "' and ID_Part = '" + det.getAbsRow(i).getPart() + "'";
        	xml.Open();
        	if(xml.getNumRows() > 0)
        	{
        		String [][] sxml = new String [xml.getNumRows()][10];
        		
        		for(int j = 0; j < xml.getNumRows(); j++)
        		{
        			if(xml.getAbsRow(j).getID_Tipo().equals("CompNal"))
        				sxml [j][0] = "XML:";
        			else if(xml.getAbsRow(j).getID_Tipo().equals("CompNalOtr"))
        				sxml [j][0] = "CBB:";
        			else
        				sxml [j][0] = "EXT:";
        			sxml [j][1] = xml.getAbsRow(j).getUUID_CFDI();
        			sxml [j][2] = xml.getAbsRow(j).getCFD_CBB_Serie();
        			sxml [j][3] = Integer.toString(xml.getAbsRow(j).getCFD_CBB_NumFol());
        			sxml [j][4] = xml.getAbsRow(j).getRFC();
        			sxml [j][5] = xml.getAbsRow(j).getNumFactExt();
        			sxml [j][6] = xml.getAbsRow(j).getTAXID();
        			sxml [j][7] = JUtil.Converts(xml.getAbsRow(j).getMonto(), ",", ".", 2, true);
        			sxml [j][8] = xml.getAbsRow(j).getMoneda();
        			sxml [j][9] = JUtil.Converts(xml.getAbsRow(j).getTipCamb(), ",", ".", 4, true);
        		}
        		
        		detalles.cexml = sxml;
            }
        	
        	elm.dets.addElement(detalles);
        	
        }
        
		Niv.Niv.addElement(elm);
		
	}
	
	public void _llenaMALM(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "ALM_MOVIM", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","ALM_MOVIM","DLG","COLUMNAS",1);
			
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JAlmacenesMovimSetV2 set = new JAlmacenesMovimSetV2(request);
		 JAlmacenesMovimSetDetallesV2 det = new JAlmacenesMovimSetDetallesV2(request);
		 set.m_Where = "ID_Movimiento = '" + JUtil.p(ID_Mov) + "'";
		 det.m_Where = "ID_Movimiento = '" + JUtil.p(ID_Mov) + "'";
		 det.m_OrderBy = "ID_Costo ASC";
		 set.Open();
		 det.Open();
		 JAdmInvservCostosConceptosSet con = new JAdmInvservCostosConceptosSet(request);
		 con.m_Where = "ID_Concepto = '" + set.getAbsRow(0).getID_Concepto() + "'";
		 con.Open();
   	  
		 String status;
			
         if(set.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getStatus().equals("U"))
			status = JUtil.Elm(sts,3);
         else if(set.getAbsRow(0).getStatus().equals("R"))
			status = JUtil.Elm(sts,4);
         else if(set.getAbsRow(0).getStatus().equals("P"))
 			status = JUtil.Elm(sts,5);
         else if(set.getAbsRow(0).getStatus().equals("C"))
 			status = JUtil.Elm(sts,6);
         else
			status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "ALM_MOVIM",10,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_MOVIM","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodega();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         if(con.getAbsRow(0).getTipo().equals("ENT"))
        	 elm.cabsval[numcab++] =  JUtil.Msj("GLB","GLB","GLB","ENTRADA");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","SALIDA");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getNum());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_MOVIM","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = set.getAbsRow(0).getID_Concepto() + " - "+ set.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getRef();
        
         //Numcols 
         
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < det.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts((det.getAbsRow(i).getEntrada() - det.getAbsRow(i).getSalida()), ",", ".", 3, true), 
         										det.getAbsRow(i).getUnidad(), 
         										det.getAbsRow(i).getID_Prod(),
         										det.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(det.getAbsRow(i).getUC(), ",", ".", 4, true) + " " +
         										JUtil.Converts(det.getAbsRow(i).getCP(), ",", ".", 4, true) + " " +
         										JUtil.Converts((det.getAbsRow(i).getDebe() - det.getAbsRow(i).getHaber()), ",", ".", 2, true) };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaMBANCAJ(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "BANCAJ_BANCOS", "VISTA", "STATUS",2);
		 
		 JBancosSetMovsV2 set = new JBancosSetMovsV2(request);
         set.m_Where = "ID = '" + JUtil.p(ID_Mov) + "'";
         set.Open();
         
         JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
    	 setMon.m_Where = "Clave = '" + set.getAbsRow(0).getID_Moneda() + "'";
    	 setMon.Open();
    	 
         String status;
		
         if(set.getAbsRow(0).getEstatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getEstatus().equals("T"))
			status = JUtil.Elm(sts,3);
         else if(set.getAbsRow(0).getEstatus().equals("C"))
			status = JUtil.Elm(sts,4);
         else
			status = "";
         
         JAdmBancosCuentasSet setc = new JAdmBancosCuentasSet(request);
         setc.m_Where = "Tipo = '" + set.getAbsRow(0).getTipo() + "' and Clave = '" + set.getAbsRow(0).getClave() + "'";
         setc.Open();
         
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, (set.getAbsRow(0).getTipo() == 0 ? "BANCAJ_BANCOS" : "BANCAJ_CAJAS"),13,0);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","BANCOS",2) + " / " + JUtil.Msj("GLB","GLB","GLB","CAJAS",2); 
         elm.cabsval[numcab++] = setc.getAbsRow(0).getCuenta();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         if(set.getAbsRow(0).getTipoMov().equals("DEP"))
        	 elm.cabsval[numcab++] =  JUtil.Msj("GLB","GLB","GLB","DEPOSITO");
         else if(set.getAbsRow(0).getTipoMov().equals("CHQ"))  
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CHEQUE") + " " +  set.getAbsRow(0).getDoc();
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","RETIRO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Integer.toString(set.getAbsRow(0).getNum());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","BANCAJ_BANCOS","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CANTIDAD");
         elm.cabsval[numcab++] = ((set.getAbsRow(0).getRetiro() == 0.0) ? JUtil.Converts(set.getAbsRow(0).getDeposito(), ",", ".", 2, false) : JUtil.Converts(set.getAbsRow(0).getRetiro(), ",", ".", 2, false));
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","BENEFICIARIO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getBeneficiario();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = setMon.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(set.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getRef();
        
         Niv.Niv.addElement(elm);
	}

	
	public JRastreo(HttpServletRequest request, String Titulo, String ID_Modulo, String ID) 
	{
		super();
		m_Titulo = Titulo;
		m_Niv = new Vector();
		JRastreoNiv niv = new JRastreoNiv();
		m_Niv.addElement(niv); //1 nivel, 0 elementos nivel 1
		if(ID_Modulo.equals("POLZ"))
			_POLZ(request, niv, ID);
		else if(ID_Modulo.equals("MALM"))
			_MALM(request, niv, ID, null);
		else if(ID_Modulo.equals("PALM"))
			_PALM(request, niv, ID);
		else if(ID_Modulo.equals("TALM"))
			_TALM(request, niv, ID);
		else if(ID_Modulo.equals("RALM"))
			_RALM(request, niv, ID);
		else if(ID_Modulo.equals("CHFI"))
			_CHFI(request, niv, ID);
		else if(ID_Modulo.equals("MBAN") || ID_Modulo.equals("MCAJ"))
			_MBANCAJ(request, niv, ID, null);
		else if(ID_Modulo.equals("VFAC"))
			_VFAC(request, niv, ID, null);
		else if(ID_Modulo.equals("VDEV"))
			_VDEV(request, niv, ID, null, null);
		else if(ID_Modulo.equals("VREM"))
			_VREM(request, niv, ID, null);
		else if(ID_Modulo.equals("VPED"))
			_VPED(request, niv, ID, null);
		else if(ID_Modulo.equals("VCOT"))
			_VCOT(request, niv, ID, null);
		else if(ID_Modulo.equals("VCXC"))
			_VCXC(request, niv, ID, null, null);
		else if(ID_Modulo.equals("CFAC"))
			_CFAC(request, niv, ID, null);
		else if(ID_Modulo.equals("CDEV"))
			_CDEV(request, niv, ID, null, null);
		else if(ID_Modulo.equals("CREC"))
			_CREC(request, niv, ID, null);
		else if(ID_Modulo.equals("CORD"))
			_CORD(request, niv, ID, null);
		else if(ID_Modulo.equals("CCXP"))
			_CCXP(request, niv, ID, null, null);
		else if(ID_Modulo.equals("CGAS"))
			_CGAS(request, niv, ID, null);
		else if(ID_Modulo.equals("PPRD"))
			_PPRD(request, niv, ID);
		

	}
	
	public String getTitulo()
	{
		return m_Titulo;
	}
	
	public int getNumElmsNiv(int Nivel) 
	{
		/*
		int res;
			
		switch(Nivel)
		{
		case 1:
			res = m_NumElmsNiv1;
			break;
		case 2:
			res = m_NumElmsNiv2;
			break;
		case 3:
			res = m_NumElmsNiv3;
			break;
		case 4:
			res = m_NumElmsNiv4;
			break;
		default:
			res = 0;
		}
			 
		return res;
		*/
		JRastreoNiv niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return niv.NumElmsNiv();
	}
	
	/*
	public int getNumElmsNiv1() 
	{
		return m_NumElmsNiv1;
	}
	
	public int getNumElmsNiv2() 
	{
		return m_NumElmsNiv2;
	}

	public int getNumElmsNiv3() 
	{
		return m_NumElmsNiv3;
	}

	public int getNumElmsNiv4() 
	{
		return m_NumElmsNiv4;
	}
	*/
	
	public void _llenaPPRD(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		String sts = JUtil.Msj("CEF", "PROD_PRODUCCION", "VISTA", "STATUS", 2);
		String tipo = JUtil.Msj("CEF", "PROD_PRODUCCION", "VISTA", "STATUS");
		String coletq = JUtil.Msj("CEF","PROD_PRODUCCION","DLG","COLUMNAS",1);
	 	
		int numcols = JUtil.obtNumElm(coletq);
		
		JProdProdSetV2 smod = new JProdProdSetV2(request);
		smod.m_Where = "ID_Reporte = '" + JUtil.p(ID_Mov) + "'";
		smod.Open();
		
		String Tipo, status;
		
		if(!smod.getAbsRow(0).getCDA())
			Tipo = JUtil.Elm(tipo,2); 
		else 
			Tipo = JUtil.Elm(tipo,3);
		
		if(smod.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,1); 
		else if(smod.getAbsRow(0).getStatus().equals("E"))
			status = JUtil.Elm(sts,2);
		else if(smod.getAbsRow(0).getStatus().equals("C"))
			status = JUtil.Elm(sts,3);
		else if(smod.getAbsRow(0).getStatus().equals("R"))
			status = JUtil.Elm(sts,4);
		else if(smod.getAbsRow(0).getStatus().equals("P"))
			status = JUtil.Elm(sts,5);
		else
			status = "";
		 
		JAdmProduccionEntidades ent = new JAdmProduccionEntidades(request);
		ent.m_Where = "ID_EntidadProd = '" + smod.getAbsRow(0).getID_Entidad() + "'";
		ent.Open();
		           
		int numcab = 0;
		JRastreoElm elm = new JRastreoElm(request, "PROD_PRODUCCION",11,numcols);
         
		elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
        elm.cabsval[numcab++] = ID_Mov;
        elm.cabsetq[numcab] = JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","TITULO",3); //Entidad
        elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
        elm.cabsval[numcab++] = JUtil.obtFechaTxt(smod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
        elm.cabsval[numcab++] = Long.toString(smod.getAbsRow(0).getNumero());
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
        elm.cabsval[numcab++] = Tipo;
        elm.cabsetq[numcab] = JUtil.Msj("CEF","PROD_PRODUCCION","VISTA","TITULO",5); //Status
        elm.cabsval[numcab++] = status;
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
        elm.cabsval[numcab++] = smod.getAbsRow(0).getConcepto();
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","PROCESO");
        elm.cabsval[numcab++] = Long.toString(smod.getAbsRow(0).getActual()) + " / " + Long.toString(smod.getAbsRow(0).getNumProc());
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ORIGEN"); 
        elm.cabsval[numcab++] = smod.getAbsRow(0).getBodega_MP();
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESTINO"); 
        elm.cabsval[numcab++] = smod.getAbsRow(0).getBodega_PT();
        elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
        elm.cabsval[numcab++] = smod.getAbsRow(0).getObs();
        
        //Numcols 
        for(int i = 0; i < numcols; i++ )
			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
		
        // Llena el movimiento
		JProdProdSetDetV2 SetDet = new JProdProdSetDetV2(request);
		SetDet.m_Where = "ID_Reporte = '" + JUtil.p(ID_Mov) + "'";
        SetDet.m_OrderBy = "Partida ASC";
		SetDet.Open();
		              				
		for(int d = 0; d < SetDet.getNumRows(); d++)
		{
			String [] detalle = new String [] {
					Integer.toString(d+1),
					JUtil.obtFechaTxt(SetDet.getAbsRow(d).getFecha(),"dd/MMM/yyyy"),
	               	JUtil.Converts(SetDet.getAbsRow(d).getCantidad(), ",", ".", 3, false), 
					SetDet.getAbsRow(d).getUnidad(), 
					SetDet.getAbsRow(d).getClave(),
					SetDet.getAbsRow(d).getDescripcion(),
					SetDet.getAbsRow(d).getFormula(),
					SetDet.getAbsRow(d).getLote(),
					SetDet.getAbsRow(d).getObs() };
			JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
        	
			JProdProdSetProcV2 SetProc = new JProdProdSetProcV2(request);
            SetProc.m_Where = "ID_Reporte = '" + JUtil.p(ID_Mov) + "' AND Partida = '" + (d+1) + "'";
			SetProc.m_OrderBy = "ID_Proceso ASC";
       		SetProc.Open();

			for(int p = 0; p < SetProc.getNumRows(); p++)
			{
				String [] proceso = new String [] {
					"",
					JUtil.obtFechaTxt(SetProc.getAbsRow(p).getFecha(), "dd/MMM/yyyy") + " <b>" + JUtil.obtFechaTxt(SetProc.getAbsRow(p).getFechaSP(), "dd/MMM/yyyy") + "</b>",
					SetProc.getAbsRow(p).getClave() != null ? JUtil.Converts(SetProc.getAbsRow(p).getCantidad(), ",", ".", 3, false) : "",
					SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getUnidad() : "",
					SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getClave() : "",
					SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getDescripcion() : "",
					SetProc.getAbsRow(p).getClave() != null ? SetProc.getAbsRow(p).getPorcentaje() + "%" : "",
					SetProc.getAbsRow(p).getClave() != null ? JUtil.Converts(SetProc.getAbsRow(p).getMasMenos(), ",", ".", 3, false) : "",
			        SetProc.getAbsRow(p).getNombre() };
				JRastreoDets procesos = new JRastreoDets();
	        	procesos.detalles = proceso;
	        	elm.dets.addElement(procesos);
				
				JProdProdSetDetprodV2 SetDetprod = new JProdProdSetDetprodV2(request);
              	SetDetprod.m_Where = "ID_Reporte = '" + JUtil.p(ID_Mov) + "' AND Partida = '" + (d+1) + "' AND ID_Proceso = '" + (p+1) + "'";
				SetDetprod.m_OrderBy = "Secuencia ASC";
            	SetDetprod.Open();
				
				for(int dp = 0; dp < SetDetprod.getNumRows(); dp++)
				{
					String [] detprod = new String [] {
						"",
						"",
						JUtil.Converts(SetDetprod.getAbsRow(dp).getCantidad(), ",", ".", 3, false), 
						SetDetprod.getAbsRow(dp).getUnidad(),
						SetDetprod.getAbsRow(dp).getID_Prod(),
						SetDetprod.getAbsRow(dp).getDescripcion(),
						JUtil.Converts(SetDetprod.getAbsRow(dp).getMasMenos(), ",", ".", 3, false),
						"",
						"" };
					JRastreoDets detprods = new JRastreoDets();
		        	detprods.detalles = detprod;
		        	elm.dets.addElement(detprods);
					
				}
			}
        	
        }
                
        Niv.Niv.addElement(elm);
	
	}

	public void _llenaVCXC(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		String Tipos = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "TIPO", 3);
		String sts = JUtil.Msj("CEF", "VEN_CXC", "VISTA", "STATUS");
		
		JClientCXCSetV2 smod = new JClientCXCSetV2(request);
		smod.m_Where = "Clave = '" + JUtil.p(ID_Mov) + "'";
		smod.Open();
		
		String tipocxc, status;
		
		if(smod.getAbsRow(0).getID_TipoCP().equals("ALT")) 
			tipocxc = JUtil.Elm(Tipos,2);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("ANT")) 
			tipocxc = JUtil.Elm(Tipos,3);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("PAG")) 
			tipocxc = JUtil.Elm(Tipos,4);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("SAL")) 
			tipocxc = JUtil.Elm(Tipos,5);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("APL")) 
			tipocxc = JUtil.Elm(Tipos,6);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("DPA")) 
			tipocxc = JUtil.Elm(Tipos,7);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("DEV")) 
			tipocxc = JUtil.Elm(Tipos,8);
		else
			tipocxc = "";
				
		if(smod.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,1); 
		else if(smod.getAbsRow(0).getStatus().equals("C"))
			status = JUtil.Elm(sts,2);
		else
			status = "";
		 
         JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
    	 setMon.m_Where = "Clave = '" + smod.getAbsRow(0).getMoneda() + "'";
    	 setMon.Open();
          
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_CXC",12,0);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         elm.cabsval[numcab++] =  tipocxc;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] =  JUtil.obtFechaTxt(smod.getAbsRow(0).getFecha(),"dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","STATUS");
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getRef();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLIENTE");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] =  smod.getAbsRow(0).getConcepto();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getTotal() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getTotalPesos();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SALDO");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getSaldo() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getSaldoPesos();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENCIMIENTO");
         elm.cabsval[numcab++] =  JUtil.obtFechaTxt(smod.getAbsRow(0).getVencimiento(),"dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","PAGO");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getPagoBanCaj();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","APLICACION");
         if(smod.getAbsRow(0).getClave() != smod.getAbsRow(0).getID_Aplicacion())
        	 elm.cabsval[numcab++] = Long.toString(smod.getAbsRow(0).getID_Aplicacion());
         else
        	 elm.cabsval[numcab++] = "";
         Niv.Niv.addElement(elm);
	
	}

	public void _llenaCCXP(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		String Tipos = JUtil.Msj("CEF", "COMP_CXP", "VISTA", "TIPO", 3);
		String sts = JUtil.Msj("CEF", "COMP_CXP", "VISTA", "STATUS");
		
		JProveeCXPSetV2 smod = new JProveeCXPSetV2(request);
		smod.m_Where = "Clave = '" + JUtil.p(ID_Mov) + "'";
		smod.Open();
		
		String tipocxp, status;
		
		if(smod.getAbsRow(0).getID_TipoCP().equals("ALT")) 
			tipocxp = JUtil.Elm(Tipos,2);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("ANT")) 
			tipocxp = JUtil.Elm(Tipos,3);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("PAG")) 
			tipocxp = JUtil.Elm(Tipos,4);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("SAL")) 
			tipocxp = JUtil.Elm(Tipos,5);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("APL")) 
			tipocxp = JUtil.Elm(Tipos,6);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("DPA")) 
			tipocxp = JUtil.Elm(Tipos,7);
		else if(smod.getAbsRow(0).getID_TipoCP().equals("DEV")) 
			tipocxp = JUtil.Elm(Tipos,8);
		else
			tipocxp = "";
				
		if(smod.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,1); 
		else if(smod.getAbsRow(0).getStatus().equals("C"))
			status = JUtil.Elm(sts,2);
		else
			status = "";
		 
         JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
    	 setMon.m_Where = "Clave = '" + smod.getAbsRow(0).getMoneda() + "'";
    	 setMon.Open();
          
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_CXP",12,0);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         elm.cabsval[numcab++] =  tipocxp;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] =  JUtil.obtFechaTxt(smod.getAbsRow(0).getFecha(),"dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","STATUS");
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getRef();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","PROVEEDOR");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] =  smod.getAbsRow(0).getConcepto();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getTotal() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getTotalPesos();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SALDO");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getMonedaSim() + " " +  smod.getAbsRow(0).getSaldo() + " -" + smod.getAbsRow(0).getTC() + "- " + smod.getAbsRow(0).getSaldoPesos();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENCIMIENTO");
         elm.cabsval[numcab++] =  JUtil.obtFechaTxt(smod.getAbsRow(0).getVencimiento(),"dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","PAGO");
         elm.cabsval[numcab++] = smod.getAbsRow(0).getPagoBanCaj();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","APLICACION");
         if(smod.getAbsRow(0).getClave() != smod.getAbsRow(0).getID_Aplicacion())
        	 elm.cabsval[numcab++] = Long.toString(smod.getAbsRow(0).getID_Aplicacion());
         else
        	 elm.cabsval[numcab++] = "";
         Niv.Niv.addElement(elm);
	
	}
	
	public void _llenaRALM(HttpServletRequest request, JRastreoNiv Niv, String ID_MovReq) 
	{
		 String sts = JUtil.Msj("CEF", "ALM_REQUERIMIENTOS", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","ALM_TRASPASOS","DLG","COLUMNAS");
			
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JAlmacenesMovReqSet set = new JAlmacenesMovReqSet(request);
         set.m_Where = "ID_Movimiento = '" + JUtil.p(ID_MovReq) + "'";
     	 JAlmacenesMovBodSetDetallesV2 det = new JAlmacenesMovBodSetDetallesV2(request, "REQUERIMIENTOS");
         det.m_Where = "ID_Movimiento = '" + JUtil.p(ID_MovReq) + "'";
         det.m_OrderBy = "Partida ASC";
     	 set.Open();
		 det.Open();
		 
		 String status;
			
         if(set.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getStatus().equals("N"))
			status = JUtil.Elm(sts,3);
         else if(set.getAbsRow(0).getStatus().equals("C"))
			status = JUtil.Elm(sts,4);
         else
			status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "ALM_REQUERIMIENTOS",8,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_MovReq;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ORIGEN"); 
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodega();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESTINO"); 
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodegaDEST();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getRequerimiento());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
         
         //Numcols 
         
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         for(int i = 0; i < det.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(det.getAbsRow(i).getCantidad(), ",", ".", 3, true), 
         										det.getAbsRow(i).getUnidad(), 
         										det.getAbsRow(i).getID_Prod(),
         										det.getAbsRow(i).getDescripcion() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaTALM(HttpServletRequest request, JRastreoNiv Niv, String ID_MovTrs) 
	{
		 String sts = JUtil.Msj("CEF", "ALM_TRASPASOS", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","ALM_TRASPASOS","DLG","COLUMNAS");
			
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JAlmacenesMovBodSetV2 set = new JAlmacenesMovBodSetV2(request);
         set.m_Where = "ID_Movimiento = '" + JUtil.p(ID_MovTrs) + "'";
     	 JAlmacenesMovBodSetDetallesV2 det = new JAlmacenesMovBodSetDetallesV2(request, "TRASPASOS");
         det.m_Where = "ID_Movimiento = '" + JUtil.p(ID_MovTrs) + "'";
         det.m_OrderBy = "Partida ASC";
     	 set.Open();
		 det.Open();
		 
		 String status;
			
         if(set.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getStatus().equals("E"))
			status = JUtil.Elm(sts,3);
         else if(set.getAbsRow(0).getStatus().equals("R"))
			status = JUtil.Elm(sts,4);
         else if(set.getAbsRow(0).getStatus().equals("P"))
 			status = JUtil.Elm(sts,5);
         else if(set.getAbsRow(0).getStatus().equals("C"))
 			status = JUtil.Elm(sts,6);
         else
			status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "ALM_TRASPASOS",10,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_MovTrs;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ORIGEN"); 
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodega();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESTINO"); 
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodegaDEST();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getSalida());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_TRASPASOS","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CFD"); 
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getID_CFD());
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TFD"); 
         elm.cabsval[numcab++] = Byte.toString(set.getAbsRow(0).getTFD());
         
         //Numcols 
         
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena la plantilla 	  rec.agregaPartida( , SetDet.getAbsRow(i).getUC());
     	
         for(int i = 0; i < det.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(det.getAbsRow(i).getCantidad(), ",", ".", 3, true), 
         										det.getAbsRow(i).getUnidad(), 
         										det.getAbsRow(i).getID_Prod(),
         										det.getAbsRow(i).getDescripcion() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaPALM(HttpServletRequest request, JRastreoNiv Niv, String ID_MovPln) 
	{
		 String sts = JUtil.Msj("CEF", "ALM_MOVPLANT", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","ALM_MOVIM","DLG","COLUMNAS",1);
			
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JAlmacenesMovimPlantSet set = new JAlmacenesMovimPlantSet(request);
   	  	 JAlmacenesMovimSetDetallesV2 det = new JAlmacenesMovimSetDetallesV2(request,"PLANTILLAS");
   	  	 set.m_Where = "ID_MovimPlant = '" + JUtil.p(ID_MovPln) + "'";
		 det.m_Where = "ID_Movimiento = '" + JUtil.p(ID_MovPln) + "'";
		 det.m_OrderBy = "ID_Costo ASC";
		 set.Open();
		 det.Open();
		 JAdmInvservCostosConceptosSet con = new JAdmInvservCostosConceptosSet(request);
		 con.m_Where = "ID_Concepto = '" + set.getAbsRow(0).getID_Concepto() + "'";
		 con.Open();
		 
		 System.out.println("NUM ROWS: " + det.getNumRows() + " NUM COLS: " + numcols + "");
      	
		 String status;
			
         if(set.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getStatus().equals("E"))
			status = JUtil.Elm(sts,3);
         else if(set.getAbsRow(0).getStatus().equals("R"))
			status = JUtil.Elm(sts,4);
         else if(set.getAbsRow(0).getStatus().equals("N"))
 			status = JUtil.Elm(sts,5);
         else if(set.getAbsRow(0).getStatus().equals("C"))
 			status = JUtil.Elm(sts,6);
         else
			status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "ALM_MOVPLANT",9,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_MovPln;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_MOVPLANT","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = set.getAbsRow(0).getBodega();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         if(con.getAbsRow(0).getTipo().equals("ENT"))
        	 elm.cabsval[numcab++] =  JUtil.Msj("GLB","GLB","GLB","ENTRADA");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","SALIDA");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getNum());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_MOVPLANT","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = set.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = set.getAbsRow(0).getID_Concepto() + " - "+ set.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONCEPTO");
         elm.cabsval[numcab++] = set.getAbsRow(0).getConcepto();
         
         //Numcols 
         
         for(int i = 0; i < numcols; i++ )
         	 elm.conceptos[i] = JUtil.Elm(coletq,i+1);
                  
         // Llena la plantilla 	  rec.agregaPartida( , SetDet.getAbsRow(i).getUC());
     	
         for(int i = 0; i < det.getNumRows(); i++)
         {
           	String [] detalle = new String [] { JUtil.Converts((con.getAbsRow(0).getTipo().equals("ENT") ? det.getAbsRow(i).getEntrada() : det.getAbsRow(i).getSalida()), ",", ".", 3, true), 
         										det.getAbsRow(i).getUnidad(), 
         										det.getAbsRow(i).getID_Prod(),
         										det.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(det.getAbsRow(i).getUC(), ",", ".", 4, true) };
           	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	public void _llenaCHFI(HttpServletRequest request, JRastreoNiv Niv, String ID_ChFis) 
	{
		 String sts = JUtil.Msj("CEF", "ALM_CHFIS", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","ALM_CHFIS","DLG","COLUMNAS",1);
		 String coletq2 = JUtil.Msj("CEF","ALM_CHFIS","VISTA","COLUMNAS",2);
			
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JAlmacenesCHFISSet set = new JAlmacenesCHFISSet(request);
		 JAlmChFisDetSet det = new JAlmChFisDetSet(request);
   	  	 set.m_Where = "ID_CHFIS = '" + JUtil.p(ID_ChFis) + "'";
		 det.m_Where = "ID_CHFIS = '" + JUtil.p(ID_ChFis) + "'";
		 det.m_OrderBy = "ID_Prod ASC";
		 set.Open();
		 det.Open();
	
		 //System.out.println("NUM ROWS: " + det.getNumRows() + " NUM COLS: " + numcols + "");
      	
		 String status;
			
         if(set.getAbsRow(0).getStatus().equals("G"))
			status = JUtil.Elm(sts,2); 
         else if(set.getAbsRow(0).getStatus().equals("E"))
			status = JUtil.Elm(sts,3);
          else if(set.getAbsRow(0).getStatus().equals("C"))
 			status = JUtil.Elm(sts,4);
         else
			status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "ALM_CHFIS",7,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_ChFis;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_CHFIS","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = set.getAbsRow(0).getNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(set.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(set.getAbsRow(0).getChequeo());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","ALM_CHFIS","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Elm(coletq2,4);
         elm.cabsval[numcab++] = set.getAbsRow(0).getCerrado() ? "X" : "--";
         elm.cabsetq[numcab] = JUtil.Elm(coletq2,5);
         elm.cabsval[numcab++] = set.getAbsRow(0).getGenerado() ? "X" : "--";
         
         //Numcols 
         for(int i = 0; i < numcols; i++ )
         	 elm.conceptos[i] = JUtil.Elm(coletq,i+1);
                  
         // Llena la plantilla 	  rec.agregaPartida( , SetDet.getAbsRow(i).getUC());
     	
         for(int i = 0; i < det.getNumRows(); i++)
         {
           	String [] detalle = new String [] { det.getAbsRow(i).getID_Prod(), 
           										det.getAbsRow(i).getDescripcion(),
           										JUtil.Converts(det.getAbsRow(i).getCantidad(), ",", ".", 3, true),
           										det.getAbsRow(i).getUnidad(), 
           										JUtil.Converts(det.getAbsRow(i).getDiff(), ",", ".", 3, true) };
           	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}


	public void _llenaVDEV(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "VEN_DEV", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","VEN_DEV","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JVentasDevolucionesSet SetMod = new JVentasDevolucionesSet(request);
		 SetMod.m_Where = "ID_Devolucion = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"DEVOLUCIONES");
		 JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"DEVOLUCIONES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		 ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_DEV",20,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_DEV","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         if(SetMod.getAbsRow(0).getDevReb().equals("REB"))
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","REBAJA");
		 else
			 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","DEVOLUCION");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_DEV","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FACTURA");
         elm.cabsval[numcab++] = Integer.toString(SetMod.getAbsRow(0).getFactura());
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Cliente() + " - "+ SetMod.getAbsRow(0).getCliente();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENDEDOR");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Vendedor() + " - "+ SetMod.getAbsRow(0).getVendedorNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	public void _llenaCDEV(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "COMP_DEV", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","COMP_DEV","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JComprasDevolucionesSet SetMod = new JComprasDevolucionesSet(request);
		 SetMod.m_Where = "ID_Devolucion = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"DEVOLUCIONES");
		 JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"DEVOLUCIONES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		 ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_DEV",19,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_DEV","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TIPO");
         if(SetMod.getAbsRow(0).getDevReb().equals("REB"))
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","REBAJA");
		 else
			 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","DEVOLUCION");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_DEV","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FACTURA");
         elm.cabsval[numcab++] = Integer.toString(SetMod.getAbsRow(0).getFactura());
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Proveedor() + " - "+ SetMod.getAbsRow(0).getProveedor();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaVFAC(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "VEN_FAC", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","VEN_VEN","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JVentasFactSetV2 SetMod = new JVentasFactSetV2(request);
		 SetMod.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"FACTURAS");
		 JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"FACTURAS");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		 ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 JVentasRemisionesSet SetRem = new JVentasRemisionesSet(request);
         SetRem.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
         SetRem.Open();
         
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_FAC",19,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REMISION");
         if(SetRem.getNumRows() > 0)
        	 elm.cabsval[numcab++] = Long.toString(SetRem.getAbsRow(0).getNumero());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Cliente() + " - "+ SetMod.getAbsRow(0).getCliente();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENDEDOR");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Vendedor() + " - "+ SetMod.getAbsRow(0).getVendedorNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	public void _llenaCFAC(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "COMP_FAC", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","COMP_COMP","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JComprasFactSet SetMod = new JComprasFactSet(request);
		 SetMod.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"FACTURAS");
		 JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"FACTURAS");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		 ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 JComprasRecepSetV2 SetRec = new JComprasRecepSetV2(request);
         SetRec.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
         SetRec.Open();
         
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_FAC",18,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","RECEPCION");
         if(SetRec.getNumRows() > 0)
        	 elm.cabsval[numcab++] = Long.toString(SetRec.getAbsRow(0).getNumero());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Proveedor() + " - "+ SetMod.getAbsRow(0).getProveedor();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaCGAS(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "COMP_GAS", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","COMP_COMP","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JComprasGastosSet SetMod = new JComprasGastosSet(request);
		 SetMod.m_Where = "ID_Gasto = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"GASTOS");
		 JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"GASTOS");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		 ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		  else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_GAS",17,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Proveedor() + " - "+ SetMod.getAbsRow(0).getProveedor();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaVCOT(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "VEN_COT", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","VEN_VEN","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JVentasCotizacionesSet SetMod = new JVentasCotizacionesSet(request);
		 SetMod.m_Where = "ID_Cotizacion = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"COTIZACIONES");
		 JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"COTIZACIONES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		 ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("F"))
			 status = JUtil.Elm(sts,4);
		  else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_COT",18,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_REM","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ENLACE");
         if(SetMod.getAbsRow(0).getFactura() > 0)
        	 elm.cabsval[numcab++] = SetMod.getAbsRow(0).getTipoEnlace() + " " + Integer.toString(SetMod.getAbsRow(0).getFactura());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Cliente() + " - "+ SetMod.getAbsRow(0).getCliente();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENDEDOR");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Vendedor() + " - "+ SetMod.getAbsRow(0).getVendedorNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
               
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public void _llenaVPED(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "VEN_PED", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","VEN_VEN","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JVentasPedidosSet SetMod = new JVentasPedidosSet(request);
		 SetMod.m_Where = "ID_Pedido = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"PEDIDOS");
		 JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"PEDIDOS");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		 ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("F"))
			 status = JUtil.Elm(sts,4);
		  else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_PED",19,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_REM","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ENLACE");
         if(SetMod.getAbsRow(0).getFactura() > 0)
        	 elm.cabsval[numcab++] = SetMod.getAbsRow(0).getTipoEnlace() + " " + Integer.toString(SetMod.getAbsRow(0).getFactura());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Cliente() + " - "+ SetMod.getAbsRow(0).getCliente();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENDEDOR");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Vendedor() + " - "+ SetMod.getAbsRow(0).getVendedorNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	public void _llenaCORD(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "COMP_ORD", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","COMP_COMP","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JComprasOrdenesSet SetMod = new JComprasOrdenesSet(request);
		 SetMod.m_Where = "ID_Orden = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"ORDENES");
		 JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"ORDENES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		 ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("F"))
			 status = JUtil.Elm(sts,4);
		  else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_ORD",18,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","COMP_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","COMP_REC","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ENLACE");
         if(SetMod.getAbsRow(0).getFactura() > 0)
        	 elm.cabsval[numcab++] = SetMod.getAbsRow(0).getTipoEnlace() + " " + Integer.toString(SetMod.getAbsRow(0).getFactura());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Proveedor() + " - "+ SetMod.getAbsRow(0).getProveedor();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	
	public void _llenaVREM(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "VEN_REM", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","VEN_VEN","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JVentasRemisionesSet SetMod = new JVentasRemisionesSet(request);
		 SetMod.m_Where = "ID_Remision = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JVentasFactSetCabV2 SetCab = new JVentasFactSetCabV2(request,"REMISIONES");
		 JVentasFactSetDetV2 SetDet = new JVentasFactSetDetV2(request,"REMISIONES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmVentasEntidades ent = new JAdmVentasEntidades(request);
		 ent.m_Where = "ID_EntidadVenta = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "VEN_REM",19,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_REM","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FACTURA");
         if(SetMod.getAbsRow(0).getFactura() > 0)
        	 elm.cabsval[numcab++] = Integer.toString(SetMod.getAbsRow(0).getFactura());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Cliente() + " - "+ SetMod.getAbsRow(0).getCliente();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","VENDEDOR");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Vendedor() + " - "+ SetMod.getAbsRow(0).getVendedorNombre();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
			
         }
      
         Niv.Niv.addElement(elm);
    	
	}

	public void _llenaCREC(HttpServletRequest request, JRastreoNiv Niv, String ID_Mov) 
	{
		 String sts = JUtil.Msj("CEF", "COMP_REC", "VISTA", "STATUS");
		 String coletq = JUtil.Msj("CEF","COMP_COMP","DLG","COLUMNAS",1);
		 	
		 int numcols = JUtil.obtNumElm(coletq);
			
		 JComprasRecepSetV2 SetMod = new JComprasRecepSetV2(request);
		 SetMod.m_Where = "ID_Recepcion = '" + JUtil.p(ID_Mov) + "'";
		 SetMod.Open();
		 JComprasFactSetCab SetCab = new JComprasFactSetCab(request,"RECEPCIONES");
		 JComprasFactSetDet SetDet = new JComprasFactSetDet(request,"RECEPCIONES");
		 SetCab.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetDet.m_Where = "ID_Factura = '" + JUtil.p(ID_Mov) + "'";
		 SetCab.Open();
		 SetDet.Open();
		 JAdmComprasEntidades ent = new JAdmComprasEntidades(request);
		 ent.m_Where = "ID_EntidadCompra = '" + SetMod.getAbsRow(0).getID_Entidad() + "'";
		 ent.Open();
		 
		 String status;
			
		 if(SetMod.getAbsRow(0).getStatus().equals("G"))
			 status = JUtil.Elm(sts,2); 
		 else if(SetMod.getAbsRow(0).getStatus().equals("C"))
			 status = JUtil.Elm(sts,3);
		 else if(SetMod.getAbsRow(0).getStatus().equals("E"))
			 status = JUtil.Elm(sts,4);
		 else if(SetMod.getAbsRow(0).getStatus().equals("R"))
			 status = JUtil.Elm(sts,5);
		 else
			 status = "";
		 
         int numcab = 0;
         JRastreoElm elm = new JRastreoElm(request, "COMP_REC",18,numcols);
         
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","ID");
         elm.cabsval[numcab++] = ID_Mov;
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_FAC","VISTA","TITULO",3); //Entidad
         elm.cabsval[numcab++] = ent.getAbsRow(0).getDescripcion();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FECHA");
         elm.cabsval[numcab++] = JUtil.obtFechaTxt(SetMod.getAbsRow(0).getFecha(), "dd/MMM/yyyy");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","NUMERO");
         elm.cabsval[numcab++] = Long.toString(SetMod.getAbsRow(0).getNumero());
         elm.cabsetq[numcab] = JUtil.Msj("CEF","VEN_REM","VISTA","TITULO",5); //Status
         elm.cabsval[numcab++] = status;
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getReferencia();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","FACTURA");
         if(SetMod.getAbsRow(0).getFactura() > 0)
        	 elm.cabsval[numcab++] = Integer.toString(SetMod.getAbsRow(0).getFactura());
         else
        	 elm.cabsval[numcab++] = "";
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","MONEDA");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getMoneda();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TC");
         elm.cabsval[numcab++] = JUtil.Converts(SetMod.getAbsRow(0).getTC(), ",", ".", 4, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CONDICION");
         if(SetCab.getAbsRow(0).getCondicion() == 0)
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CONTADO");
         else
        	 elm.cabsval[numcab++] = JUtil.Msj("GLB","GLB","GLB","CREDITO");
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","CLAVE");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getID_Proveedor() + " - "+ SetMod.getAbsRow(0).getProveedor();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IMPORTE");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getImporte(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","DESCUENTO");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getDescuento(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","SUB-TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getSubTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","IVA");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getIVA(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","TOTAL");
         elm.cabsval[numcab++] = JUtil.Converts(SetCab.getAbsRow(0).getTotal(), ",", ".", 2, false);
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","OBS");
         elm.cabsval[numcab++] = SetCab.getAbsRow(0).getObs();
         elm.cabsetq[numcab] = JUtil.Msj("GLB","GLB","GLB","REFERENCIA");
         elm.cabsval[numcab++] = SetMod.getAbsRow(0).getRef();
                
         //Numcols 
         for(int i = 0; i < numcols; i++ )
 			elm.conceptos[i] = JUtil.Elm(coletq,i+1);
 		
         // Llena el movimiento
         for(int i = 0; i < SetDet.getNumRows(); i++)
         {
         	String [] detalle = new String [] { JUtil.Converts(SetDet.getAbsRow(i).getCantidad(), ",", ".", 3, false), 
         										SetDet.getAbsRow(i).getID_UnidadSalida(), 
         										SetDet.getAbsRow(i).getID_Prod(),
         										SetDet.getAbsRow(i).getDescripcion(),
         										JUtil.Converts(SetDet.getAbsRow(i).getPrecio(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporte(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getDescuento(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getIVA(), ",", ".", 4, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteDesc(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getImporteIVA(), ",", ".", 2, false),
         										JUtil.Converts(SetDet.getAbsRow(i).getTotalPart(), ",", ".", 2, false),
         										SetDet.getAbsRow(i).getObs(),
         										SetDet.getAbsRow(i).getID_Tipo() };
         	JRastreoDets detalles = new JRastreoDets();
        	detalles.detalles = detalle;
        	elm.dets.addElement(detalles);
         }
      
         Niv.Niv.addElement(elm);
    	
	}
	
	public int NumDets(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.size();
	}
	
	public boolean tieneCECHQ(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		if(((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cechq == null)
			return false;
		else
			return true;
	}
	
	public boolean tieneCEOMP(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		if(((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).ceomp == null)
			return false;
		else
			return true;
	}
	
	public boolean tieneCETRN(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		if(((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cetrn == null)
			return false;
		else
			return true;
	}
	
	public boolean tieneCEXML(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		if(((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cexml == null)
			return false;
		else
			return true;
	}
	
	public String [] DetsDetalles(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).detalles;
	}
	
	public String [][] DetsCECHQ(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cechq;
	}
	
	public String [][] DetsCEOMP(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).ceomp;
	}
	
	public String [][] DetsCETRN(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cetrn;
	}
	
	public String [][] DetsCEXML(int Nivel, int numElm, int numDet)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoDets)((JRastreoElm)Niv.Niv.elementAt(numElm-1)).dets.elementAt(numDet)).cexml;
	}
	
	public int NumCabs(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).cabsetq.length;
	}
	
	public String [] CabsEtq(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).cabsetq;
	}
	
	public String [] CabsVal(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).cabsval;
	}
	
	public int NumConceptos(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).conceptos.length;
	}
	
	public String [] Conceptos(int Nivel, int numElm)
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).conceptos;
	}

	public String getTitulo(int Nivel, int numElm) 
	{
		JRastreoNiv Niv = (JRastreoNiv)m_Niv.elementAt(Nivel -1);
		return ((JRastreoElm)Niv.Niv.elementAt(numElm-1)).titElm;
	}
	
	class JRastreoElm
	{
		int numElm;
		String titElm;
		private String [] cabsetq;
		private String [] cabsval;
		
		private String [] conceptos;
		private Vector dets;
		
		
		JRastreoElm(HttpServletRequest request, String idPerm, int numEtq, int numCols)
		{
			JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(request);
			set.m_Where = "ID_Permiso = '" + JUtil.p(idPerm) + "'";
			set.Open();
			this.titElm = set.getAbsRow(0).getModulo();
			//this.numElm = numElm;
			conceptos = new String[numCols];
			cabsetq = new String[numEtq];
			cabsval = new String[numEtq];
			dets = new Vector();
		}
		
		
		JRastreoElm(HttpServletRequest request, String idPerm, int numElm, int numEtq, int numCols)
		{
			JUsuariosPermisosCatalogoSet set = new JUsuariosPermisosCatalogoSet(request);
			set.m_Where = "ID_Permiso = '" + JUtil.p(idPerm) + "'";
			set.Open();
			this.titElm = set.getAbsRow(0).getModulo();
			this.numElm = numElm;
			conceptos = new String[numCols];
			cabsetq = new String[numEtq];
			cabsval = new String[numEtq];
			dets = new Vector();
		}
		
		
		
	}
	
	class JRastreoDets
	{
		private String [] detalles;
		private String [][] cechq;
		private String [][] cetrn;
		private String [][] cexml;
		private String [][] ceomp;
	}
	
	class JRastreoNiv
	{
		private Vector  Niv;
		
		JRastreoNiv()
		{
			Niv = new Vector();
		}
		
		public int NumElmsNiv()
		{
			return Niv.size();
		}
	}
	
}
