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

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class JRepCellStyles
{
	Font fenc; 
	Font fnorm;
	CellStyle csEncCenMEnc;
    CellStyle csEncRigMEnc;
    CellStyle csEncLefMEnc;
    CellStyle csEncCenMAgr;
    CellStyle csEncRigMAgr;
    CellStyle csEncLefMAgr;
    CellStyle csEncCenMNor;
    CellStyle csEncRigMNor;
    CellStyle csEncLefMNor;
    CellStyle csNorCenMEnc;
    CellStyle csNorRigMEnc;
    CellStyle csNorLefMEnc;
    CellStyle csNorCenMAgr;
    CellStyle csNorRigMAgr;
    CellStyle csNorLefMAgr;
    CellStyle csNorCenMNor;
    CellStyle csNorRigMNor;
    CellStyle csNorLefMNor;
    
	public JRepCellStyles(Workbook wb)
	{
		fenc = wb.createFont();
        fenc.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	fenc.setColor(HSSFColor.WHITE.index);
    	
    	fnorm = wb.createFont();
        fnorm.setColor(HSSFColor.BLACK.index);
       
        csEncCenMEnc = wb.createCellStyle();
        csEncRigMEnc = wb.createCellStyle();
        csEncLefMEnc = wb.createCellStyle();
        csEncCenMAgr = wb.createCellStyle();
        csEncRigMAgr = wb.createCellStyle();
        csEncLefMAgr = wb.createCellStyle();
        csEncCenMNor = wb.createCellStyle();
        csEncRigMNor = wb.createCellStyle();
        csEncLefMNor = wb.createCellStyle();
        
        csNorCenMEnc = wb.createCellStyle();
        csNorRigMEnc = wb.createCellStyle();
        csNorLefMEnc = wb.createCellStyle();
        csNorCenMAgr = wb.createCellStyle();
        csNorRigMAgr = wb.createCellStyle();
        csNorLefMAgr = wb.createCellStyle();
        csNorCenMNor = wb.createCellStyle();
        csNorRigMNor = wb.createCellStyle();
        csNorLefMNor = wb.createCellStyle();
         
		csEncCenMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncCenMEnc.setFont(fenc);
	    csEncRigMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncRigMEnc.setFont(fenc);
	    csEncLefMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncLefMEnc.setFont(fenc);
	    csEncCenMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncCenMAgr.setFont(fenc);
	    csEncRigMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncRigMAgr.setFont(fenc);
	    csEncLefMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncLefMAgr.setFont(fenc);
	    csEncCenMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncCenMNor.setFont(fenc);
	    csEncRigMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncRigMNor.setFont(fenc);
	    csEncLefMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csEncLefMNor.setFont(fenc);
	    
	    csNorCenMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorCenMEnc.setFont(fnorm);
	    csNorRigMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorRigMEnc.setFont(fnorm);
	    csNorLefMEnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorLefMEnc.setFont(fnorm);
	    csNorCenMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorCenMAgr.setFont(fnorm);
	    csNorRigMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorRigMAgr.setFont(fnorm);
	    csNorLefMAgr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorLefMAgr.setFont(fnorm);
	    csNorCenMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorCenMNor.setFont(fnorm);
	    csNorRigMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorRigMNor.setFont(fnorm);
	    csNorLefMNor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    csNorLefMNor.setFont(fnorm);
	    
	    csEncCenMEnc.setAlignment(CellStyle.ALIGN_CENTER);
        csEncRigMEnc.setAlignment(CellStyle.ALIGN_RIGHT);
        csEncLefMEnc.setAlignment(CellStyle.ALIGN_LEFT);
        csEncCenMAgr.setAlignment(CellStyle.ALIGN_CENTER);
        csEncRigMAgr.setAlignment(CellStyle.ALIGN_RIGHT);
        csEncLefMAgr.setAlignment(CellStyle.ALIGN_LEFT);
        csEncCenMNor.setAlignment(CellStyle.ALIGN_CENTER);
        csEncRigMNor.setAlignment(CellStyle.ALIGN_RIGHT);
        csEncLefMNor.setAlignment(CellStyle.ALIGN_LEFT);
        
        csNorCenMEnc.setAlignment(CellStyle.ALIGN_CENTER);
        csNorRigMEnc.setAlignment(CellStyle.ALIGN_RIGHT);
        csNorLefMEnc.setAlignment(CellStyle.ALIGN_LEFT);
        csNorCenMAgr.setAlignment(CellStyle.ALIGN_CENTER);
        csNorRigMAgr.setAlignment(CellStyle.ALIGN_RIGHT);
        csNorLefMAgr.setAlignment(CellStyle.ALIGN_LEFT);
        csNorCenMNor.setAlignment(CellStyle.ALIGN_CENTER);
        csNorRigMNor.setAlignment(CellStyle.ALIGN_RIGHT);
        csNorLefMNor.setAlignment(CellStyle.ALIGN_LEFT);
        
        csEncCenMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csEncCenMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csEncCenMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncCenMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		
        csEncRigMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csEncRigMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csEncRigMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncRigMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		
        csEncLefMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csEncLefMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csEncLefMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncLefMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		            
		
		csEncCenMAgr = wb.createCellStyle();
        csEncCenMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncCenMAgr.setFillPattern(CellStyle.BIG_SPOTS);
		
        csEncRigMAgr = wb.createCellStyle();
        csEncRigMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncRigMAgr.setFillPattern(CellStyle.BIG_SPOTS);
		
		csEncLefMAgr = wb.createCellStyle();
		csEncLefMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csEncLefMAgr.setFillPattern(CellStyle.BIG_SPOTS);
		
        
		
		csNorCenMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csNorCenMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csNorCenMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorCenMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		
        csNorRigMEnc = wb.createCellStyle();
        csNorRigMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csNorRigMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csNorRigMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorRigMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		
		csNorLefMEnc.setBorderTop(CellStyle.BORDER_THIN);
		csNorLefMEnc.setBorderBottom(CellStyle.BORDER_THIN);
		csNorLefMEnc.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorLefMEnc.setFillPattern(CellStyle.BIG_SPOTS);
		
		csNorCenMAgr = wb.createCellStyle();
		csNorCenMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorCenMAgr.setFillPattern(CellStyle.BIG_SPOTS);
		
		csNorRigMAgr = wb.createCellStyle();
		csNorRigMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorRigMAgr.setFillPattern(CellStyle.BIG_SPOTS);
		
		csNorLefMAgr = wb.createCellStyle();
		csNorLefMAgr.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		csNorLefMAgr.setFillPattern(CellStyle.BIG_SPOTS);
	}
	
	public CellStyle getStyle(String alin, String tipocel, String font)
	{
		
        if(font.equals("fenc"))
        {
        	if(alin.equals("center"))
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csEncCenMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csEncCenMAgr;
                else
                	return csEncCenMNor;
                	
        	}
        	else if(alin.equals("right"))
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csEncRigMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csEncRigMAgr;
                else
                	return csEncRigMNor;
        	}
        	else
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csEncLefMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csEncLefMAgr;
                else
                	return csEncLefMNor;
        	}
        }
        else //if(font.equals("fnorm"))
        {
        	if(alin.equals("center"))
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csNorCenMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csNorCenMAgr;
                else
                	return csNorCenMNor;
                	
        	}
        	else if(alin.equals("right"))
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csNorRigMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csNorRigMAgr;
                else
                	return csNorRigMNor;
        	}
        	else
        	{
        		if(tipocel != null && tipocel.equals("encabezado"))
        			return csNorLefMEnc;
        		else if(tipocel != null && tipocel.equals("agregado"))
        			return csNorLefMAgr;
                else
                	return csNorLefMNor;
        	}
        }
        
	}
}