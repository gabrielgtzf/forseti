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
 * Contenedor de constantes habituales de QRCode.
 * @author Ginés Miguel Fernández Ordóñez
 */
public class QRCodeConstantes
{
	/**
	 * Constante para definir el grado de corrección L (bajo).
	 * Puede corregir hasta el 7% de los codewords del símbolo.
	 */
	public static final int ErrorCorrectL = 1;
	
	/**
	 * Constante para definir el grado de corrección M (medio).
	 * Puede corregir hasta el 15% de los codewords del símbolo.
	 */
	public static final int ErrorCorrectM = 2;
	
	/**
	 * Constante para definir el grado de corrección Q (Calidad).
	 * Puede corregir hasta el 25% de los codewords del símbolo.
	 */
	public static final int ErrorCorrectQ = 3;
	
	/**
	 * Constante para definir el grado de corrección H (Alto, máximo).
	 * Puede corregir hasta el 30% de los codewords del símbolo.
	 */
	public static final int ErrorCorrectH = 4;
	
	/**
	 * Constante para definir el modo de codificación con caracteres alfanuméricos.
	 * (0-9, A-Z y otros 9 caracteres: espacio, $, %, *, +, -, ., /, :)
	 */
	public static final int EncodeModeAlphanumeric = 1;
	
	/**
	 * Constante para definir el modo de codificación con caracteres numéricos. (0-9)
	 */
	public static final int EncodeModeNumeric = 2;
	
	/**
	 * Constante para definir el modo de codificación con bytes. (por defecto ISO/IEC 8859-1)
	 */
	public static final int EncodeModeByte = 3;
	
	/**
	 * Constante para definir el modo de codificación Kanji.
	 * Caracteres de la escritura japonesa, compactados en 13 bits.
	 */
	public static final int EncodeModeKanji = 4;
}