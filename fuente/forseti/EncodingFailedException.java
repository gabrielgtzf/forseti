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
 * Excepción que indica fallo durante la codificación.
 * @author Ginés Fernández Ordóñez
 */
public class EncodingFailedException extends IllegalArgumentException
{
	/**
	 * ID serial versión.
	 */
	private static final long serialVersionUID = 2650012064677181630L;

	String message = null;

	public EncodingFailedException(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
}