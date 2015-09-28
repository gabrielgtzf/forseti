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

import forseti.sets.JReportesBind3Set;
import forseti.sets.JReportesLevel1;
import java.awt.Color;
import java.awt.geom.*;

public class JFsiGrafBarras extends JFsiGraficas
{
	public JFsiGrafBarras()
	{
		
	}
	
	public JFsiGrafBarras(JReportesBind3Set cols, JReportesLevel1 rows, String Titulo,  boolean enCols, boolean linejes, byte tipo, int Hor, int Ver)
	{
		super(cols, rows, Titulo, enCols, tipo, Hor, Ver, Color.white, false, linejes);
  		
		dibPlanoXY(Color.black, Color.lightGray);
		
	}
	
	protected boolean verifParam()
	{
		if(m_Tipo == 2) // Si es porcentajes no pueden ser positivos y negativos
		{
			if(m_ValMinY < 0 && m_ValMaxY > 0)
			{
				m_Dibujar = false;
				return false;
			}
			return true;
		}
		return true;
	}


	public void dibGrafica()
	{
		if(!m_Dibujar)
			return;
		
		//Rectangle2D.Float bil = new Rectangle2D.Float(200,100,50,200);
		//m_g2d.fill(bil);
		//int altura = 0;
		
		// dibuja las barras normales
		if(m_Tipo == 0)
		{
			for(int c = 0; c < m_Cols.length; c++)
			{
				float iniX = (m_punto0_X + (c * m_nivelX) + obtIntNiv(2));
				float anc = ((m_nivelX - (obtIntNiv(2) * 2)) / m_Rows.length);
				
				for(int r = 0; r < m_Rows.length; r++)
				{
					m_g2d.setColor(obtColor(r));
					
					float val = m_Data[r][c];
					
					float x1 = iniX + (anc * r);
					float y1 = (val >= 0) ? (m_puntoY_Y + ((m_ValMaxY - val) * m_ptXuni)) : (m_puntoVal0Y);
					float alt = (val >= 0) ? (m_puntoVal0Y - y1) : (Math.abs(val) * m_ptXuni);
					Rectangle2D.Float b = new Rectangle2D.Float(x1,y1,anc,alt);
					m_g2d.fill(b);
					m_g2d.setColor(Color.black);
					m_g2d.draw(b);
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
				}
				
			}
		}
		else if(m_Tipo == 1) // Dibuja barras en pilas
		{
			for(int c = 0; c < m_Cols.length; c++)
			{
				float x1 = (m_punto0_X + (c * m_nivelX) + obtIntNiv(10));
				float anc = (m_nivelX - (obtIntNiv(10) * 2));
				float valp = 0.0F;
				float puntoYP = m_puntoVal0Y;
				float puntoYN = m_puntoVal0Y;
				
				for(int r = 0; r < m_Rows.length; r++)
				{
					m_g2d.setColor(obtColor(r));
					
					float val = m_Data[r][c];
					float y1;
					float alt;
					
					if(val >= 0)
					{
						valp += val;
						y1 = (m_puntoY_Y + ((m_ValMaxY - valp) * m_ptXuni));
						alt = (puntoYP - y1);
						puntoYP = y1;
						
					}
					else
					{
						y1 = puntoYN;
						alt = (Math.abs(val) * m_ptXuni);
						puntoYN = y1 + alt;
					}
					Rectangle2D.Float b = new Rectangle2D.Float(x1,y1,anc,alt);
					m_g2d.fill(b);
					m_g2d.setColor(Color.black);
					m_g2d.draw(b);
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
				}
				
			}
		}
		else if(m_Tipo == 2) // Dibuja barras en pilas por porcentaje
		{
			float dimY = m_punto0_Y - m_puntoY_Y;
			
			for(int c = 0; c < m_Cols.length; c++)
			{
				float x1 = (m_punto0_X + (c * m_nivelX) + obtIntNiv(10));
				float anc = (m_nivelX - (obtIntNiv(10) * 2));
				float vala = 0.0F;
				for(int r = 0; r < m_Rows.length; r++)
					vala += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
				
				//m_g2d.drawString(Float.toString(vala), 10, (10 + (m_AltoCadenaX*c)));

				float puntoYA = m_punto0_Y;
				
				for(int r = 0; r < m_Rows.length; r++)
				{
					m_g2d.setColor(obtColor(r));
										
					float val = (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					float y1 = (puntoYA - ((val * dimY) / vala));
					float alt = ((val * dimY) / vala);
					puntoYA = y1;
					
					Rectangle2D.Float b = new Rectangle2D.Float(x1,y1,anc,alt);
					m_g2d.fill(b);
					m_g2d.setColor(Color.black);
					m_g2d.draw(b);
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
				}
				
			}
		}
		//Rectangle2D.Float b = new Rectangle2D.Float(116F,509.36F,73F,43.64F);
		//m_g2d.fill(b);
		
		
	}
}
