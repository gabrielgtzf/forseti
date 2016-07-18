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


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import forseti.sets.*;

public abstract class JFsiGraficas 
{
	protected boolean m_Dibujar;
	//protected Frame m_f;
	protected BufferedImage m_img;
	protected Graphics2D m_g2d;
	//protected Image m_img;
	protected FontMetrics m_fm;
	
	protected String [] m_Cols;
	protected String [] m_Rows;
	protected float  [][] m_Data;
	protected String m_Titulo;
	protected String m_FmtVals;
	protected boolean m_enCols;
	protected boolean m_LinEjes;
	protected int m_Horizontal;
	protected int m_Vertical;
	protected float m_ValMinY;
	protected float m_ValMaxY;
	protected byte m_Tipo; // 0 Normal 1 pilas 2 porcentaje
	protected float m_puntoVal0Y;
	protected int m_AnchoCadenaCant;
	protected int m_MaxCadenaX;
	protected int m_AltoCadenaX;
	
	protected int m_MargenX;
	protected int m_MargenY;
	protected int m_MargenInt;
	protected float m_puntoY_X;
	protected float m_puntoY_Y;
	protected float m_puntoX_X;
	protected float m_puntoX_Y;
	protected float m_punto0_X;
	protected float m_punto0_Y;
	protected float m_ptXuni; // Son los pixeles representativos por unidad de cantidad
	protected float m_nivelX;
	protected float m_nivelY;
	protected boolean m_EjeXEn0;
	
	protected int m_AnchoTit;
	protected int m_AltoTit;
	protected int m_Cuadros;
	
	protected JFsiGraficas()
	{
	}
	
	protected JFsiGraficas(JReportesBind3Set cols, JReportesLevel1 rows, String Titulo, boolean enCols, byte tipo, int Hor, int Ver, Color sup, 
				boolean EjeXEn0, boolean LinEjes)
	{
		m_Dibujar = true;
		m_img = new BufferedImage(Hor, Ver, BufferedImage.TYPE_INT_ARGB);
		m_g2d = (Graphics2D)m_img.getGraphics();	
		m_g2d.setColor(sup);
		m_g2d.fillRect(0, 0, m_img.getWidth(), m_img.getHeight());
		
		m_ValMinY = 0.0F;
		m_ValMaxY = 0.0F;
		m_AnchoCadenaCant = 0;
		m_MaxCadenaX = 0;
		m_AltoCadenaX = 0;
		m_AnchoTit = 0;
		m_AltoTit = 0;
		
		m_MargenX = 10;
		m_MargenY = 10;
		m_MargenInt = 5;
		m_puntoX_X = 0F;
		m_puntoX_Y = 0F;
		m_puntoY_X = 0F;
		m_puntoY_Y = 0F;
		m_punto0_X = 0F;
		m_punto0_Y = 0F;
		m_puntoVal0Y = 0F;
		m_ptXuni = 0F;
		m_nivelX = 0F;
		m_nivelY = 0F;
		m_Tipo = tipo;
		
		m_Cuadros = 10; //Pixeles de los cuadros
		m_Titulo = Titulo;
		m_enCols = enCols;
		m_Horizontal = Hor;
		m_Vertical = Ver;
		m_EjeXEn0 = EjeXEn0;
		m_LinEjes = LinEjes;
		
		if(m_enCols)
		{
			m_Cols = new String[(cols.getNumRows()-1)];
			m_Rows = new String[rows.getNumRows()];
			m_Data = new float[rows.getNumRows()][(cols.getNumRows()-1)];
			
			for(int i = 1; i < cols.getNumRows(); i++)
	  			m_Cols [i-1] = cols.getAbsRow(i).getColName();
	  	
	  		for(int i = 0; i < rows.getNumRows(); i++)
	  			m_Rows [i] = rows.getAbsRow(i).getSTS(cols.getAbsRow(0).getColName());
	  	        
	  		for(int i = 0; i < rows.getNumRows(); i++)
	        { 
	  			for(int j = 0; j < cols.getNumRows(); j++)
	    		{	
	    			if(j == 0)
	    				continue;
	    			
	    			m_Data [i][j-1] = Float.parseFloat(rows.getAbsRow(i).getSTS(cols.getAbsRow(j).getColName()));;
    			
	    		} 
	        }
		}
		else
		{
	  		m_Rows = new String[(cols.getNumRows()-1)];
			m_Cols = new String[rows.getNumRows()];
			m_Data = new float[(cols.getNumRows()-1)][rows.getNumRows()];
			
			for(int i = 1; i < cols.getNumRows(); i++)
	  			m_Rows [i-1] = cols.getAbsRow(i).getColName();
	  	
	  		for(int i = 0; i < rows.getNumRows(); i++)
	  			m_Cols [i] = rows.getAbsRow(i).getSTS(cols.getAbsRow(0).getColName());
	
	 		
	    	for(int j = 0; j < cols.getNumRows(); j++)
	    	{	
	    		if(j == 0)
	    			continue;
	    	
	  	   		for(int i = 0; i < rows.getNumRows(); i++)
	    	    { 		
	    			
	    			m_Data [j-1][i] = Float.parseFloat(rows.getAbsRow(i).getSTS(cols.getAbsRow(j).getColName()));
	    			
	    		} 
	        }
		}

		// Ahora que ya estan los valores en m_Data, Calcula los maximos y minimos segun el tipo
		
		for(int c = 0; c < m_Cols.length; c++)
		{
			float valn = 0.0F;
			float valp = 0.0F;
			
			for(int r = 0; r < m_Rows.length; r++)
			{
				float val = m_Data[r][c];
				
				if(m_Tipo == 0) // Es normal
				{
					if(val < m_ValMinY)
						m_ValMinY = val;
					if(val > m_ValMaxY)
						m_ValMaxY = val;
				}
				else if(m_Tipo == 1 || m_Tipo == 2) // Es en pilas, El valor maximo es la suma de los valores positivos y el minimo de los negativos
				{
					if(val >= 0)
						valp += val;
					else
						valn += val;
					
					if(valn < m_ValMinY)
						m_ValMinY = valn;
					if(valp > m_ValMaxY)
						m_ValMaxY = valp;
				}
						
			}
			
		}
		

		
    	if(m_ValMaxY <= 1)
  			m_FmtVals = ",|.|6|0";
  		else if(m_ValMaxY > 1 && m_ValMaxY <= 10)
  			m_FmtVals = ",|.|4|0";
  		else if(m_ValMaxY > 10 && m_ValMaxY <= 100)
  			m_FmtVals = ",|.|2|0";
  		else 
  			m_FmtVals = ",|.|1|0";

	}
	
	
	protected final void dibPlanoXY(Color plano, Color ejes)
	{
		
		Font titulo = new Font("Dialog", Font.BOLD, 18);
		m_fm = m_img.getGraphics().getFontMetrics(titulo);
		m_g2d.setFont(titulo);
		m_g2d.setPaint(Color.black);
		
		
		if(!verifParam())
		{
			String str = 	"No se puede dibujar la gráfica";
			int x = (m_Horizontal - m_fm.stringWidth(str)) / 2;
			int y = (m_Vertical / 2) - m_fm.getHeight();
			m_g2d.drawString(str, x, y);
			str = "porque esta variante no soporta";
			x = (m_Horizontal - m_fm.stringWidth(str)) / 2;
			y += m_fm.getHeight();
			m_g2d.drawString(str, x, y);
			str = "valores con diferentes signos";
			x = (m_Horizontal - m_fm.stringWidth(str)) / 2;
			y += m_fm.getHeight();
			m_g2d.drawString(str, x, y);
			return;
		}
				
		m_AnchoTit = m_fm.stringWidth(m_Titulo);
		m_AltoTit = m_fm.getHeight();
		int x = (m_Horizontal - m_AnchoTit) / 2;
		int y = (m_MargenY + m_AltoTit);
		m_g2d.drawString(m_Titulo, x, y);
		
		Font resto = new Font("monospaced", Font.PLAIN, 9);
		m_fm = m_img.getGraphics().getFontMetrics(resto);
		m_g2d.setFont(resto);
		m_AltoCadenaX = m_fm.getHeight();
		
		// Obtiene el maximo de las cadenas de x
		
		int max = (m_AltoCadenaX + m_MargenY) * m_Rows.length;
		int iniY = (m_Vertical / 2) - (max / 2);
		int iniX = m_MargenX + m_Cuadros + m_MargenInt;
		
		for(int i = 0; i < m_Rows.length; i++)
		{
			m_g2d.setColor(obtColor(i));
			Rectangle2D.Float r = new Rectangle2D.Float(m_MargenX, ((iniY + (i * (m_AltoCadenaX + m_MargenY)) - m_Cuadros)),
					m_Cuadros, m_Cuadros);
			m_g2d.fill(r);
			m_g2d.setColor(Color.black);
			m_g2d.draw(r);
			
			int val = m_fm.stringWidth(m_Rows[i]);
			m_g2d.drawString(m_Rows[i], iniX, (iniY + (i * (m_AltoCadenaX + m_MargenY))));
			if(val > m_MaxCadenaX)
				m_MaxCadenaX = val;
		}//////////////////////////////
		

		m_g2d.setColor(plano);
		
		m_AnchoCadenaCant = m_fm.stringWidth(Float.toString(m_ValMaxY)); 
		
		iniX += ( m_MaxCadenaX + m_MargenX + m_AnchoCadenaCant + m_MargenX + m_MargenInt);
		iniY = y + (m_MargenY * 4);
		
		m_puntoY_X = iniX;
		m_puntoY_Y = iniY;
		
		m_punto0_X = iniX;
		m_punto0_Y = m_Vertical - m_MargenY - ( m_AltoCadenaX * 2) - m_MargenY - m_MargenInt;
		
		// Define los puntos por unidad y el punto para el valor cero
		m_ptXuni = ((m_punto0_Y - m_puntoY_Y) / (m_ValMaxY - m_ValMinY));
		m_puntoVal0Y = (m_puntoY_Y + (m_ValMaxY * m_ptXuni));
		
		
		
		m_puntoX_X = m_Horizontal - m_MargenX;
		m_puntoX_Y = m_punto0_Y;
		
		
		// Ahora define los niveles de y y x
		// Primero los de y ( 10 Niveles )
		m_nivelY = ((m_punto0_Y - m_puntoY_Y) / 10F);
		
		GeneralPath p = new GeneralPath();
		GeneralPath ej = new GeneralPath();
		if(m_LinEjes)
		{
			m_g2d.setColor(ejes);
			
			for(float pt = m_puntoY_Y; pt <= m_punto0_Y; pt += m_nivelY)
			{
				ej.moveTo(m_puntoY_X, pt);
				ej.lineTo(m_puntoX_X, pt);
			}
			m_g2d.draw(ej);
			m_g2d.setColor(plano);
		}
		
		for(float pt = m_puntoY_Y; pt <= m_punto0_Y; pt += m_nivelY)
		{
			p.moveTo(m_puntoY_X, pt);
			p.lineTo(m_puntoY_X - m_MargenInt, pt);
		}
		
		if(!m_EjeXEn0)
			m_nivelX = ((m_puntoX_X - m_punto0_X) / (float)m_Cols.length);
		else
			m_nivelX = (m_Cols.length <= 1) ? (m_puntoX_X - m_punto0_X) : ((m_puntoX_X - m_punto0_X) / (float)(m_Cols.length-1));
		
		for(float pt = m_punto0_X; pt <= m_puntoX_X; pt += m_nivelX)
		{
			p.moveTo(pt, m_puntoX_Y);
			p.lineTo(pt, m_puntoX_Y + m_MargenInt);
		}
	
		m_g2d.draw(p);

		
		Line2D.Float lY = new Line2D.Float(m_puntoY_X, m_puntoY_Y, m_punto0_X, m_punto0_Y);
		m_g2d.draw(lY);
		
		Line2D.Float lx = new Line2D.Float(m_punto0_X, m_punto0_Y, m_puntoX_X, m_puntoX_Y);
		m_g2d.draw(lx);

		Line2D.Float l0 = new Line2D.Float(m_punto0_X, m_puntoVal0Y, m_puntoX_X, m_puntoVal0Y);
		m_g2d.draw(l0);

		// Las etiquetas de m_Cols y Cantidades
		float promcant = ((m_ValMaxY - m_ValMinY) / 10F);
		int derX = (int)(m_puntoY_X - m_MargenX);
		
		//m_g2d.drawString(m_FmtVals, 400, 300);
		
		for(int e = 0; e < 11; e++)
		{
			String val = (m_Tipo != 2) ? JUtil.FormatearRep( Float.toString((m_ValMaxY - (promcant * e))), m_FmtVals, "DECIMAL", null ) :
				JUtil.FormatearRep( Float.toString((100 - (10 * e))), m_FmtVals, "DECIMAL", null );
			int ancho = m_fm.stringWidth(val);
			x = (int)(derX - ancho);
			y = (int)((m_puntoY_Y + (m_AltoCadenaX/2)) + (m_nivelY * e));
			m_g2d.drawString(val,x,y);

			
		}
		
		y = (int)(m_punto0_Y + m_MargenInt + m_MargenY + m_AltoCadenaX);
		
		
		for(int e = 0; e < m_Cols.length; e++)
		{
			int i = e % 2;
			int ancho = m_fm.stringWidth(m_Cols[e]);
			
			if(!m_EjeXEn0)
				x = (int)(m_punto0_X + ((((e+1) * m_nivelX) - (m_nivelX / 2)) - (ancho / 2)));
			else
				x = (int)(m_punto0_X + ((e * m_nivelX) - ancho));
					
			m_g2d.drawString(m_Cols[e],x,((i==0) ? y : y+m_AltoCadenaX));
		}
		
		dibGrafica();
	}
	
	protected Color obtColor(int i)
	{
		int c = i % 11;
		Color col;
		
		switch(c)
		{
		case 0: col = Color.blue;
			break;
		case 1: col = Color.yellow;
			break;
		case 2: col = Color.cyan;
			break;
		case 3: col = Color.green;
			break;
		case 4: col = Color.gray;
			break;
		case 5: col = Color.magenta;
			break;
		case 6: col = Color.orange;
			break;
		case 7: col = Color.pink;
			break;
		case 8: col = Color.red;
			break;
		case 9: col = Color.darkGray;
			break;
		case 10: col = Color.white;
			break;
		default: col = Color.lightGray;
			break;
			
		}
		
		return col;
	}
	
	protected abstract void dibGrafica();
	protected abstract boolean verifParam();

	public Image getGrafica()
	{
		return m_img;
	}
	
	protected float obtIntNiv(int prc)
	{
		return (float)((m_nivelX * prc) / 100);
	}
	public Image getTitulo()
	{
		
		Frame f = new Frame();
		f.addNotify();
		Image imagenMensaje = f.createImage(m_Horizontal, m_Vertical);
		Graphics2D g2d = (Graphics2D)imagenMensaje.getGraphics();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String [] familias = env.getAvailableFontFamilyNames();
		g2d.setPaint(Color.black);
		
		int x = 2;
		int y = 5;
		
		for(int i = 0; i < familias.length; i++)
		{
			Font fuente = new Font(familias[i], Font.PLAIN, 26);
			FontMetrics fm = f.getFontMetrics(fuente);
			g2d.setFont(fuente);
						
			if(y >= m_Vertical)
			{
				y = 5;
				x += 300;
			}
			y += fm.getHeight() + 2;
			
			g2d.drawString(familias[i], x, y);
			
		}
		
		return(imagenMensaje);
		
	}

	public BufferedImage getBufferedImage()
	{
		return m_img;
	}
}