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

public class JFsiGrafCirc extends JFsiGraficas
{
	public JFsiGrafCirc()
	{
		
	}
	
	public JFsiGrafCirc(JReportesBind3Set cols, JReportesLevel1 rows, String Titulo,  boolean enCols, byte tipo, int Hor, int Ver)
	{
		super(cols, rows, Titulo, enCols, tipo, Hor, Ver, Color.white, false, false);
  		
		dibPlanoXY(Color.white, Color.white);
		
	}
	
	protected boolean verifParam()
	{
		if(m_ValMinY < 0 && m_ValMaxY > 0)
		{
			m_Dibujar = false;
			return false;
		}
		
		return true;
		
	}


	public void dibGrafica()
	{
		if(!m_Dibujar)
			return;
		
		
		if(m_Tipo == 0)
		{
			float dimY = m_punto0_Y - m_puntoY_Y;
			float dimX = m_puntoX_X - m_punto0_X;
			float puntoX1 =  (dimX < dimY) ? m_punto0_X : m_punto0_X + ((dimX - dimY) / 2); 
			float puntoY1 =  (dimY < dimX) ? m_puntoY_Y : m_puntoY_Y + ((dimY - dimX) / 2); 
			float per = (dimX < dimY) ? dimX : dimY;
			float totval = 0.0F;
			float iniang = 90.0F;

			for(int r = 0; r < m_Rows.length; r++)
				for(int c = 0; c < m_Cols.length; c++)
					totval += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					
			for(int r = 0; r < m_Rows.length; r++)
			{
				m_g2d.setColor(obtColor(r));
				
				float vala = 0.0F;
				for(int c = 0; c < m_Cols.length; c++)
					vala += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
				
				
				float ang = ((vala * 360F) / totval);
				Arc2D.Float a = new Arc2D.Float(puntoX1,puntoY1,per,per,iniang,ang,Arc2D.PIE);
			
				
				m_g2d.fill(a);
				m_g2d.setColor(Color.black);
				m_g2d.draw(a);
				
				//m_g2d.drawString(Float.toString(totval) + "/" + Float.toString(vala) + "/" +
				//		Float.toString(iniang) + "/" + Float.toString(ang), 10, (10 + (m_AltoCadenaX*r)));
				
				iniang += ang;

			}
		}
		else if(m_Tipo == 1)
		{
			float dimY = m_punto0_Y - m_puntoY_Y;
			float dimX = m_puntoX_X - m_punto0_X;
			float puntoX1 =  (dimX < dimY) ? m_punto0_X : m_punto0_X + ((dimX - dimY) / 2); 
			float puntoY1 =  (dimY < dimX) ? m_puntoY_Y : m_puntoY_Y + ((dimY - dimX) / 2); 
			float per = (dimX < dimY) ? dimX : dimY;
			float niv = (per / 2) / m_Cols.length;
			
			for(int c = 0; c < m_Cols.length; c++)
			{
				float x1 = puntoX1 + ((niv * c) - (obtIntNiv(10) * c));
				float y1 = puntoY1 + ((niv * c) - (obtIntNiv(10) * c));
				float perc = per - (((niv * c) - (obtIntNiv(10) * c)) * 2);
				
				float iniang = 90.0F;
				float vala = 0.0F;
				for(int r = 0; r < m_Rows.length; r++)
					vala += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
				
				//m_g2d.drawString(Float.toString(vala), 10, (10 + (m_AltoCadenaX*c)));
				
				for(int r = 0; r < m_Rows.length; r++)
				{
					m_g2d.setColor(obtColor(r));
					
					float val = (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					float ang = ((val * 360F) / vala);
					Arc2D.Float a = new Arc2D.Float(x1,y1,perc,perc,iniang,ang,Arc2D.PIE);
				
					
					m_g2d.fill(a);
					m_g2d.setColor(Color.black);
					m_g2d.draw(a);
					
					
					//m_g2d.drawString(Float.toString(val) + ' ' + Float.toString(x1) + ' ' + Float.toString(y1) + 
					//		' ' + Float.toString(anc) + ' ' + Float.toString(alt), 300, (100 + (m_AltoCadenaX*altura++)));
					iniang += ang;
					
				}
				
			}
			
			// por ultimo, dibuja el circulo central
			float x1 = puntoX1 + ((niv * m_Cols.length) - (obtIntNiv(10) * m_Cols.length));
			float y1 = puntoY1 + ((niv * m_Cols.length) - (obtIntNiv(10) * m_Cols.length));
			float perc = per - (((niv * m_Cols.length) - (obtIntNiv(10) * m_Cols.length)) * 2);
			
			m_g2d.setColor(m_g2d.getBackground());
			Ellipse2D.Float e = new Ellipse2D.Float(x1,y1,perc,perc);
			m_g2d.fill(e);
			m_g2d.setColor(Color.black);
			m_g2d.draw(e);
		}
		else if(m_Tipo == 2)
		{
			float dimY = m_punto0_Y - m_puntoY_Y;
			float dimX = m_puntoX_X - m_punto0_X;
			float puntoX1 =  (dimX < dimY) ? m_punto0_X : m_punto0_X + ((dimX - dimY) / 2); 
			float puntoY1 =  (dimY < dimX) ? m_puntoY_Y : m_puntoY_Y + ((dimY - dimX) / 2); 
			float per = (dimX < dimY) ? dimX : dimY;
			float totval = 0.0F;
			float iniang = 90.0F;

			for(int r = 0; r < m_Rows.length; r++)
				for(int c = 0; c < m_Cols.length; c++)
					totval += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
					
			for(int r = 0; r < m_Rows.length; r++)
			{
				m_g2d.setColor(obtColor(r));
				
				float vala = 0.0F;
				for(int c = 0; c < m_Cols.length; c++)
					vala += (m_Data[r][c] >= 0) ? m_Data[r][c] : Math.abs(m_Data[r][c]);
				
				
				float ang = ((vala * 360F) / totval);
				Arc2D.Float a;
				
				if(r == 0)
					a = new Arc2D.Float(puntoX1 - obtIntNiv(5),puntoY1 - obtIntNiv(5),per,per,iniang,ang,Arc2D.PIE);
				else
					a = new Arc2D.Float(puntoX1,puntoY1,per,per,iniang,ang,Arc2D.PIE);
				
				m_g2d.fill(a);
				m_g2d.setColor(Color.black);
				m_g2d.draw(a);
				
				//m_g2d.drawString(Float.toString(totval) + "/" + Float.toString(vala) + "/" +
				//		Float.toString(iniang) + "/" + Float.toString(ang), 10, (10 + (m_AltoCadenaX*r)));
				
				iniang += ang;

			}
		}
		//Rectangle2D.Float b = new Rectangle2D.Float(116F,509.36F,73F,43.64F);
		//m_g2d.fill(b);
		
		
	}
}
