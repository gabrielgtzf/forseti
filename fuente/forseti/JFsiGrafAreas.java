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

public class JFsiGrafAreas extends JFsiGraficas
{
	
	public JFsiGrafAreas()
	{
		
	}
	
	public JFsiGrafAreas(JReportesBind3Set cols, JReportesLevel1 rows, String Titulo,  boolean enCols, boolean linejes, byte tipo, int Hor, int Ver)
	{
		super(cols, rows, Titulo, enCols, tipo, Hor, Ver, Color.white, true, linejes);
  		
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
			//for(int r = 0; r < m_Rows.length; r++)
			for(int r = m_Rows.length - 1; r >= 0 ; r--)
			{
				GeneralPath p = new GeneralPath();
				
				m_g2d.setColor(obtColor(r));
				float iniY = 0.0F;
				
				for(int c = 0; c < m_Cols.length; c++)
				{
					float val = m_Data[r][c];
					
					float x1 = (m_punto0_X + (m_nivelX * c));
					float y1 = (val >= 0) ? (m_puntoY_Y + ((m_ValMaxY - val) * m_ptXuni)) : ((m_puntoVal0Y) + (Math.abs(val) * m_ptXuni));
					
					if(c == 0)
					{
						iniY = y1;
						p.moveTo(x1, y1);
					}
					else
						p.lineTo(x1, y1);
						
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
				}
				
				p.lineTo(m_puntoX_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, iniY);
				
				m_g2d.fill(p);
				m_g2d.setColor(Color.black);
				m_g2d.draw(p);

			}
		}
		else if(m_Tipo == 1) // Dibuja lineas en pilas
		{
			float valp[] = new float[m_Cols.length];
			float valn[] = new float[m_Cols.length];
			for(int c = 0; c < m_Cols.length; c++)
			{
				//Inicializa las variables a cero
				valp[c] = 0.0F;
				valn[c] = 0.0F;
			}
			for(int r = 0; r < m_Rows.length; r++)
			{
				for(int c = 0; c < m_Cols.length; c++)
				{
					if(m_Data[r][c] >= 0)
						valp[c] += m_Data[r][c];
					else
						valn[c] += m_Data[r][c];
					
				}
			}
			
			for(int r = m_Rows.length - 1; r >= 0 ; r--)
			{

				GeneralPath p = new GeneralPath();
				
				m_g2d.setColor(obtColor(r));
				
				float iniY = 0.0F;
				
				for(int c = 0; c < m_Cols.length; c++)
				{
					float val = m_Data[r][c];
					float x1 = (m_punto0_X + (m_nivelX * c));
					float y1; // = (val >= 0) ? (m_puntoY_Y + ((m_ValMaxY - val) * m_ptXuni)) : ((m_puntoVal0Y) + (Math.abs(val) * m_ptXuni));
				
					if(val >= 0)
					{
						y1 = (m_puntoY_Y + ((m_ValMaxY - valp[c]) * m_ptXuni));
						valp[c] -= val;
						
					}
					else
					{
						y1 = m_puntoVal0Y + (Math.abs(valn[c]) * m_ptXuni);
						valn[c] -= val;
						
					}
					
					if(c == 0)
					{
						iniY = y1;
						p.moveTo(x1, y1);
					}
					else
						p.lineTo(x1, y1);
						
					
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
				}
				p.lineTo(m_puntoX_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, iniY);
				
				m_g2d.fill(p);
				m_g2d.setColor(Color.black);
				m_g2d.draw(p);
			}					
			
			
			
			
		}
		else if(m_Tipo == 2) // Dibuja lineas en pilas por porcentaje
		{
			float dimY = m_punto0_Y - m_puntoY_Y;
			float vala[] = new float[m_Cols.length];
			float valp[] = new float[m_Cols.length];
			
			for(int c = 0; c < m_Cols.length; c++)
			{
				valp[c] = 0.0F;
				vala[c] = 0.0F;
				for(int r = 0; r < m_Rows.length; r++)
					vala[c] += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					
				//m_g2d.drawString(Float.toString(vala[c]) + ' ' + m_Cols[c], 300, (100 + (m_AltoCadenaX*c+1)));
			
			}
			for(int r = 0; r < m_Rows.length; r++)
			{
				for(int c = 0; c < m_Cols.length; c++)
				{
					if(m_Data[r][c] >= 0)
						valp[c] += m_Data[r][c];
					else
						valp[c] += Math.abs(m_Data[r][c]);
					
				}
			}
			
			for(int r = m_Rows.length - 1; r >= 0 ; r--)
			{
				GeneralPath p = new GeneralPath();
				
				m_g2d.setColor(obtColor(r));
				float iniY = 0.0F;
				for(int c = 0; c < m_Cols.length; c++)
				{
					float val = (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					
					float x1 = (m_punto0_X + (m_nivelX * c));
					float y1 = (m_punto0_Y - ((valp[c] * dimY) / vala[c]));
					
					valp[c] -= val;
					
					if(c == 0)
					{
						iniY = y1;
						p.moveTo(x1, y1);
					}
					else
						p.lineTo(x1, y1);
						
					
				}
				
				p.lineTo(m_puntoX_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, m_puntoVal0Y);
				p.lineTo(m_punto0_X, iniY);
				
				m_g2d.fill(p);
				m_g2d.setColor(Color.black);
				m_g2d.draw(p);
				
			}
		}
		//Rectangle2D.Float b = new Rectangle2D.Float(116F,509.36F,73F,43.64F);
		//m_g2d.fill(b);
		
		
	}
}
