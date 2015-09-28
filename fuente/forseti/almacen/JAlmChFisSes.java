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
package forseti.almacen;

import forseti.JSesionRegs;
import java.util.*;

public class JAlmChFisSes extends JSesionRegs 
{
	private Date m_Fecha;
	private byte m_ID_Bodega;
	private String m_Bodega;
	
	public JAlmChFisSes()
	{

	}
	
	public JAlmChFisSes(byte ID_Bodega, String Bodega)
	{
		m_Bodega = Bodega;
		m_ID_Bodega = ID_Bodega;
		Calendar fecha = GregorianCalendar.getInstance();
		m_Fecha = fecha.getTime();
		
	}
	
	public void resetear(byte ID_Bodega, String Bodega)
	{
		m_Bodega = Bodega;
		m_ID_Bodega = ID_Bodega;
		Calendar fecha = GregorianCalendar.getInstance();
		m_Fecha = fecha.getTime();
		
		super.resetear();
	}
	  
	 
	@SuppressWarnings("unchecked")
	public void agregar(String idprod, String descripcion, float cantidad, float diff, String unidad, boolean manejado)
	{
		JAlmChFisSesPart part = new JAlmChFisSesPart(idprod, descripcion, cantidad, diff, unidad, manejado);
	    m_Partidas.addElement(part);
	}

	public JAlmChFisSesPart getPartida(int ind)
	{
	    return (JAlmChFisSesPart)m_Partidas.elementAt(ind);
	}

	public Date getFecha() 
	{
		return m_Fecha;
	}

	public void setFecha(Date fecha) 
	{
		m_Fecha = fecha;
	}

	public byte getID_Bodega() {
		return m_ID_Bodega;
	}

	public void setID_Bodega(byte bodega) 
	{
		m_ID_Bodega = bodega;
	}

	public String getBodega() 
	{
		return m_Bodega;
	}

	public void setBodega(String bodega) 
	{
		m_Bodega = bodega;
	}


}
