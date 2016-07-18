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
function redondear(cantidad, decimales) 
{
	var cantidad = parseFloat(cantidad);
	var decimales = parseFloat(decimales);
	decimales = (!decimales ? 2 : decimales);
	return Math.round(cantidad * Math.pow(10, decimales)) / Math.pow(10, decimales);
}   

function gestionarArchivos(modulo, objids, idsep)
{
	var medobjids;
	if(typeof objids == 'undefined')
		medobjids = '';
	else
		medobjids = objids;
	var finobjids = medobjids.replace(/\//g, "-");
	window.location.href = "/servlet/CEFAdmAWSS3Ctrl?ID_MODULO=" + modulo + "&OBJIDS=" + finobjids + "&IDSEP=" + idsep;
}

function gestionarArchivos2(modulo, preobjids, objids, idsep)
{
	var medobjids;
	if(typeof objids == 'undefined')
		medobjids = '';
	else
		medobjids = objids;
	var finobjids = medobjids.replace(/\//g, "-");
	window.location.href = "/servlet/CEFAdmAWSS3Ctrl?ID_MODULO=" + modulo + "&OBJIDS=" + preobjids + finobjids + "&IDSEP=" + idsep;
}

function gestionarArchivosCta(modulo, _Account, idsep)
{
	var _res;
		
	if(_Account == '')
	{
		_res = '';
	}
	else
	{
		//////////////////////////////////////////////////////
		var _cuenta;
		
		_cuenta = _Account.substring(0, 4);
		_res = _cuenta;

		_cuenta = _Account.substring(5 , 8);
		//alert('L2:%' + _cuenta);
		if(_cuenta != '')
		{
			_res = _res + _cuenta;
			_cuenta = _Account.substring(9, 12);
			//raise notice 'L3%', _cuenta;
			if(_cuenta != '')
			{
				_res = _res + _cuenta;
				_cuenta = _Account.substring(13, 16);
				//raise notice 'L4%', _cuenta;
				if(_cuenta != '')
				{
					_res = _res + _cuenta;
					_cuenta = _Account.substring(17, 20);
					//raise notice 'L5%', _cuenta;
					if(_cuenta != '')
					{
						_res = _res + _cuenta;
						_cuenta = _Account.substring(21 , 24);
						//raise notice 'L6%', _cuenta;
						if(_cuenta != '')
							_res = _res + _cuenta;
						else
							_res = _res + '000';
					}
					else
					{
						_res = _res + '000000';
					}
				}
				else
				{
					_res = _res + '000000000';
				}
			}
			else
			{
				_res = _res + '000000000000';
			}
		}
		else
		{
			_res = _res + '000000000000000';
		}

		//////////////////////////////////////////////////////
	}
	
	window.location.href = "/servlet/CEFAdmAWSS3Ctrl?ID_MODULO=" + modulo + "&OBJIDS=" + _res + "&IDSEP=" + idsep;
}

function establecerProcesoSVE(m_Objeto, m_Proceso)
{
	m_Objeto.value = m_Proceso;
}

function establecerProceso(m_Objeto, m_Proceso, ancho, alto)
{
	m_Objeto.value = m_Proceso;
	ventanaEmergente(ancho, alto, 0);
}

function establecerDobleProceso(m_Objeto, m_Proceso, m_Objeto2, m_Proceso2, ancho, alto)
{
	m_Objeto.value = m_Proceso;
	m_Objeto2.value = m_Proceso2;
	ventanaEmergente(ancho, alto, 0);
}

function establecerDobleProcesoSVE(m_Objeto, m_Proceso, m_Objeto2, m_Proceso2)
{
	m_Objeto.value = m_Proceso;
	m_Objeto2.value = m_Proceso2;
}

function ventanaEmergente(ancho, alto)
{
	parametrs = "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1,width=" + ancho + ",height=" + alto;
	ventana = window.open('', 'ventEm', parametrs);
	ventana.focus();
}

function abrirCatalogo(ruta, alto, ancho)
{
	parametrs = "toolbar=0,location=0,directories=0,status=1,menubar=1,scrollbars=1,resizable=1,width=" + ancho + ",height=" + alto;
	ventana = window.open(ruta, '', parametrs);
	ventana.focus();
}
function cerrarCatalogo()
{
	wCatalogo.close();
}

function esCadena(titulo, valor, minimo, maximo)
{
	if(valor == "" && minimo != 0 && maximo != 0) 
	{
		alert(titulo + " ???????");
		return false;
	}
	else if(valor.length < minimo)
	{
		alert(titulo + " < " + minimo);
		return false;
	}
	else if(valor.length > maximo)
	{
		alert(titulo + " > " + maximo);
		return false;
	}
	
	return true;
}

function esNumeroDecimal(titulo, valor, minimo, maximo, decim)
{
	//alert("No valido");
	//return false;
	indPunto = -1;
	indMenos = -1;
	numPuntos = 0;
	numMenos = 0;
	res = true;
	
	if(valor == "" || valor == "-" || valor == "."
		 || valor == "-." || valor == ".-") 
	{
		alert(titulo + " ??????");
		return false;
	}
		
	for(i=0;i<valor.length;i++)
	{
		if(valor.charAt(i) == ".") 
		{
			indPunto = i;
			numPuntos += 1;
		}
		
		if(valor.charAt(i) == "-") 
		{
			indMenos = i;
			numMenos += 1;
		}
		
		if( valor.charAt(i) != "0" && 
			valor.charAt(i) != "1" &&
			valor.charAt(i) != "2" &&
			valor.charAt(i) != "3" &&
			valor.charAt(i) != "4" &&
			valor.charAt(i) != "5" &&
			valor.charAt(i) != "6" &&
			valor.charAt(i) != "7" &&
			valor.charAt(i) != "8" &&
			valor.charAt(i) != "9" &&
			valor.charAt(i) != "." && 
			valor.charAt(i) != "-" )
		{
			alert(titulo + " *??????");
			res = false;
			break;
		}
	}
	
	if(numPuntos > 1)
	{
		alert(titulo + " ...??????");
		res = false;
	}
	else if(numPuntos == 1)
	{
		if(valor.length - 1 == indPunto)
		{
			alert(titulo + " *.??????");
			res = false;
		}
	}
	
	if(numMenos > 1)
	{
		alert(titulo + " --??????");
		res = false;
	}
	else if(numMenos == 1)
	{
		if(indMenos != 0)
		{
			alert(titulo + " ??????-");
			res = false;
		}
	}
	
	if(res == true)
	{
		if(minimo != null)
		{
			if(valor < minimo)
			{
				alert(titulo + " < " + minimo);
				res = false;
			}
		}
		
		if(maximo != null)
		{
			if(valor > maximo)
			{
				alert(titulo + " > " + maximo);
				res = false;
			}
		}
		
		if(decim != null)
		{
			if(indPunto != -1)
			{
			 	if((valor.length - indPunto) > (decim + 1))
				{
					alert(titulo + " *.**********??????");
					res = false;
				}
			}
		}
	}
	
	return res; 
}

function esNumeroEntero(titulo, valor, minimo, maximo)
{
	//alert("No valido");
	//return false;
	indMenos = -1;
	numMenos = 0;
	res = true;
	
	if(valor == "" || valor == "-") 
	{
		alert(titulo + " ??????");
		return false;
	}
		
	for(i=0; i<valor.length; i++)
	{
		if(valor.charAt(i) == "-") 
		{
			indMenos = i;
			numMenos += 1;
		}
		
		if( valor.charAt(i) != "0" && 
			valor.charAt(i) != "1" &&
			valor.charAt(i) != "2" &&
			valor.charAt(i) != "3" &&
			valor.charAt(i) != "4" &&
			valor.charAt(i) != "5" &&
			valor.charAt(i) != "6" &&
			valor.charAt(i) != "7" &&
			valor.charAt(i) != "8" &&
			valor.charAt(i) != "9" &&
			valor.charAt(i) != "-" )
		{
			alert(titulo + " *.*??????");
			res = false;
			break;
		}
	}
	
	if(numMenos > 1)
	{
		alert(titulo + " --??????");
		res = false;
	}
	else if(numMenos == 1)
	{
		if(indMenos != 0)
		{
			alert(titulo + " ??????-");
			res = false;
		}
	}
	
	if(res == true)
	{
		if(minimo != null)
		{
			if(valor < minimo)
			{
				alert(titulo + " < " + minimo);
				res = false;
			}
		}
		
		if(maximo != null)
		{
			if(valor > maximo)
			{
				alert(titulo + " > " + maximo);
				res = false;
			}
		}
		
	}
	
	return res; 
}

function verifCuenta(titulo, valor)
{
	res = true;
	for(i=0;i<valor.length;i++)
	{
		if( valor.charAt(i) != "0" && 
			valor.charAt(i) != "1" &&
			valor.charAt(i) != "2" &&
			valor.charAt(i) != "3" &&
			valor.charAt(i) != "4" &&
			valor.charAt(i) != "5" &&
			valor.charAt(i) != "6" &&
			valor.charAt(i) != "7" &&
			valor.charAt(i) != "8" &&
			valor.charAt(i) != "9" &&
			valor.charAt(i) != "-" &&
			valor.charAt(i) != " " )
		{
			alert(titulo + " ??????");
			res = false;
			break;
		}
	}	
	
	if( res == true )
	{
		for(j=0;j<valor.length;j++)
		{
			if(j<4 && valor.charAt(j) == "-")
			{
				alert(titulo + " ??????");
				res = false;
				break;
			}
			else if(j==4 && valor.charAt(j) != "-")
			{
				alert(titulo + " " + valor.charAt(j) + "??????");
				res = false;
				break;
			}
			else if(j>4 && j<8 && valor.charAt(j) == "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j==8 && valor.charAt(j) != "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j>8 && j<12 && valor.charAt(j) == "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j==12 && valor.charAt(j) != "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j>12 && j<16 && valor.charAt(j) == "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j==16 && valor.charAt(j) != "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j>16 && j<20 && valor.charAt(j) == "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j==20 && valor.charAt(j) != "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;
			}
			else if(j>20 && valor.charAt(j) == "-")
			{
				alert(titulo + " XXXX-XXX-XXX??????");
				res = false;
				break;	
			}
		}	
	}
	return res;
}


