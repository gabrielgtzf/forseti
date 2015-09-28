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

import static nl.captcha.Captcha.NAME;    

import java.io.IOException;    

import javax.servlet.ServletException;    
//import javax.servlet.http.HttpServlet;    
import javax.servlet.http.HttpServletRequest;    
import javax.servlet.http.HttpServletResponse;    
import javax.servlet.http.HttpSession;    

import nl.captcha.Captcha;    
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;    
import nl.captcha.servlet.StickyCaptchaServlet;    

public class JFsiCaptcha extends StickyCaptchaServlet 
{    
	private static final long serialVersionUID = 1L;    

    public JFsiCaptcha() 
    {
        super();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException 
    {    

        String _width = getServletConfig().getInitParameter("width");    
        String _height = getServletConfig().getInitParameter("height");    
        HttpSession session = request.getSession();    
        Captcha captcha;    
        if (session.getAttribute(NAME) == null) 
        {    
            captcha = new Captcha.Builder(Integer.parseInt(_width), Integer.parseInt(_height))    
            .addText()
            .addBackground(new GradiatedBackgroundProducer())
            .addNoise(new StraightLineNoiseProducer())
            .gimp(new FishEyeGimpyRenderer())
            .addBorder()
            .build();  
            session.setAttribute(NAME, captcha);    
            CaptchaServletUtil.writeImage(response, captcha.getImage());    
            return;    
        }    
        captcha = (Captcha) session.getAttribute(NAME);    
        CaptchaServletUtil.writeImage(response, captcha.getImage());    
    }    


    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException 
    {    

    }    

}