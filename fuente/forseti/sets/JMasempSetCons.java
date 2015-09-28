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
package forseti.sets;

import forseti.*;
import javax.servlet.http.*;
import java.sql.*;

public class JMasempSetCons extends JManejadorSet
{
	public JMasempSetCons(HttpServletRequest request)
	{
		m_Select = " * FROM view_nom_masemp";
		m_PageSize = 50;
		this.request = request;
	}

	public view_masemp getRow(int row)
	{
		return (view_masemp)m_Rows.elementAt((getFloorRow() + row));
	}

	public view_masemp getAbsRow(int row)
	{
		return (view_masemp)m_Rows.elementAt(row);
	}

	 
  @SuppressWarnings("unchecked")
  protected void BindRow()
	{
		try
		{
			view_masemp pNode = new view_masemp();

			pNode.setID_Empleado(m_RS.getString("ID_Empleado"));
			pNode.setCompania_Sucursal(m_RS.getString("Compania_Sucursal"));
			pNode.setID_Departamento(m_RS.getString("ID_Departamento"));
			pNode.setNombre_Departamento(m_RS.getString("Nombre_Departamento"));
			pNode.setID_Turno(m_RS.getByte("ID_Turno"));
			pNode.setID_Turno_Comedor(m_RS.getByte("ID_Turno_Comedor"));
			pNode.setID_Categoria(m_RS.getByte("ID_Categoria"));
			pNode.setNombre(m_RS.getString("Nombre"));
			pNode.setApellido_Paterno(m_RS.getString("Apellido_Paterno"));
			pNode.setApellido_Materno(m_RS.getString("Apellido_Materno"));
			pNode.setFecha_de_Nacimiento(m_RS.getDate("Fecha_de_Nacimiento"));
			pNode.setFecha_de_Ingreso(m_RS.getDate("Fecha_de_Ingreso"));
			pNode.setClave_Cambio_Empresa(m_RS.getBoolean("Clave_Cambio_Empresa"));
			pNode.setFecha_Cambio_Empresa(m_RS.getDate("Fecha_Cambio_Empresa"));
			pNode.setZona_Economica(m_RS.getByte("Zona_Economica"));
			pNode.setRFC_Letras(m_RS.getString("RFC_Letras"));
			pNode.setRFC_Fecha(m_RS.getString("RFC_Fecha"));
			pNode.setRFC_Homoclave(m_RS.getString("RFC_Homoclave"));
			pNode.setRFC_Digito(m_RS.getString("RFC_Digito"));
			pNode.setCURP(m_RS.getString("CURP"));
			pNode.setCalculoSimplificado(m_RS.getBoolean("CalculoSimplificado"));
			pNode.setTipo_de_Nomina(m_RS.getByte("Tipo_de_Nomina"));
			pNode.setJornada(m_RS.getByte("Jornada"));
			pNode.setSindicalizado(m_RS.getBoolean("Sindicalizado"));
			pNode.setHoras_por_Jornada(m_RS.getFloat("Horas_por_Jornada"));
			pNode.setStatus(m_RS.getByte("Status"));
			pNode.setMotivo_Baja(m_RS.getString("Motivo_Baja"));
			pNode.setReparto_de_Utilidades(m_RS.getBoolean("Reparto_de_Utilidades"));
			pNode.setPremio_de_Puntualidad(m_RS.getBoolean("Premio_de_Puntualidad"));
			pNode.setCastigo_Impuntualidad(m_RS.getBoolean("Castigo_Impuntualidad"));
			pNode.setPuesto(m_RS.getString("Puesto"));
			pNode.setSalario_Nominal(m_RS.getFloat("Salario_Nominal"));
			pNode.setSalario_Diario(m_RS.getFloat("Salario_Diario"));
			pNode.setSalario_por_Hora(m_RS.getFloat("Salario_por_Hora"));
			pNode.setSalario_Integrado(m_RS.getFloat("Salario_Integrado"));
			pNode.setAplica_Horas_Extras(m_RS.getBoolean("Aplica_Horas_Extras"));
			pNode.setFecha_Vacaciones(m_RS.getDate("Fecha_Vacaciones"));
			pNode.setDias_Vacaciones(m_RS.getByte("Dias_Vacaciones"));
			pNode.setPrima_de_Vacaciones(m_RS.getFloat("Prima_de_Vacaciones"));
			pNode.setNum_Registro_IMSS(m_RS.getString("Num_Registro_IMSS"));
			pNode.setJefe_Inmediato(m_RS.getString("Jefe_Inmediato"));
			pNode.setCalle(m_RS.getString("Calle"));
			pNode.setNumero(m_RS.getString("Numero"));
			pNode.setColonia(m_RS.getString("Colonia"));
			pNode.setCodigo_Postal(m_RS.getString("Codigo_Postal"));
			pNode.setDelegacion(m_RS.getString("Delegacion"));
			pNode.setEstado_Civil(m_RS.getString("Estado_Civil"));
			pNode.setNombre_Esposo(m_RS.getString("Nombre_Esposo"));
			pNode.setNum_de_Hijos(m_RS.getByte("Num_de_Hijos"));
			pNode.setNombre_de_Hijos(m_RS.getString("Nombre_de_Hijos"));
			pNode.setNombre_Padre(m_RS.getString("Nombre_Padre"));
			pNode.setVive(m_RS.getBoolean("Vive"));
			pNode.setNombre_Madre(m_RS.getString("Nombre_Madre"));
			pNode.setViva(m_RS.getBoolean("Viva"));
			pNode.setEscolaridad(m_RS.getString("Escolaridad"));
			pNode.setTrabajo_Anterior_Grupo(m_RS.getString("Trabajo_Anterior_Grupo"));
			pNode.setUltimo_Trabajo(m_RS.getString("Ultimo_Trabajo"));
			pNode.setRecomendado_Por(m_RS.getString("Recomendado_Por"));
			pNode.setEn_Accidente_Avisar(m_RS.getString("En_Accidente_Avisar"));
			pNode.setFecha_para_Liquidaciones(m_RS.getDate("Fecha_para_Liquidaciones"));
			pNode.setObrEmp(m_RS.getBoolean("ObrEmp"));
			pNode.setFecha_Cambio_Obrero_Empleado(m_RS.getDate("Fecha_Cambio_Obrero_Empleado"));
			pNode.setCuenta_Bancaria(m_RS.getString("Cuenta_Bancaria"));
			pNode.setHistorial_Puestos(m_RS.getString("Historial_Puestos"));
			pNode.setRegistro_Infonavit(m_RS.getString("Registro_Infonavit"));
			pNode.setPrestamo_Infonavit(m_RS.getFloat("Prestamo_Infonavit"));
			pNode.setPorcentaje_Descuento(m_RS.getFloat("Porcentaje_Descuento"));
			pNode.setPrestamo_VSM(m_RS.getFloat("Prestamo_VSM"));
			pNode.setDescuento_VSM(m_RS.getFloat("Descuento_VSM"));
			pNode.setClave_Alta_Infonavit(m_RS.getBoolean("Clave_Alta_Infonavit"));
			pNode.setFecha_Alta_Infonavit(m_RS.getDate("Fecha_Alta_Infonavit"));
			pNode.setClave_Liquidacion_Infonavit(m_RS.getBoolean("Clave_Liquidacion_Infonavit"));
			pNode.setFecha_Liquidacion_Infonavit(m_RS.getDate("Fecha_Liquidacion_Infonavit"));
			pNode.setRegistro_Fonacot(m_RS.getString("Registro_Fonacot"));
			pNode.setNumero_de_Credito(m_RS.getString("Numero_de_Credito"));
			pNode.setPrestamo_Fonacot(m_RS.getFloat("Prestamo_Fonacot"));
			pNode.setAyuda_Vales_de_Despensa(m_RS.getBoolean("Ayuda_Vales_de_Despensa"));
			pNode.setImporte_Vales_de_Despensa(m_RS.getFloat("Importe_Vales_de_Despensa"));
			pNode.setID_XAction(m_RS.getString("ID_XAction"));
			pNode.setCompensacionAnual(m_RS.getBoolean("CompensacionAnual"));
			pNode.setSalario_Mixto(m_RS.getFloat("Salario_Mixto"));
			pNode.setCalculoMixto(m_RS.getBoolean("CalculoMixto"));
			pNode.setCompensacionAnualFija(m_RS.getFloat("CompensacionAnualFija"));
			pNode.setRegimen(m_RS.getInt("Regimen"));
			pNode.setID_SatBanco(m_RS.getString("ID_SatBanco"));
			pNode.setNoInt(m_RS.getString("NoInt"));
			pNode.setLocalidad(m_RS.getString("Localidad"));
			pNode.setEstado(m_RS.getString("Estado"));
			pNode.setPais(m_RS.getString("Pais"));
			pNode.setPCS(m_RS.getFloat("PCS"));
			
			m_Rows.addElement(pNode);

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}

	}

}
