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
package fsi_admin;

public class JTimer implements Runnable
{

  private JTimerInterface  ti;
  private int             delay;
  private Thread          thread;

  private boolean doRun     = false;
  private boolean doSuspend = false;

  public JTimer(JTimerInterface _ti, int _delay)
  {
    ti     = _ti;
    delay  = _delay;
  }

  public void run()
  {
    try
    {
      while(doRun)
      {
        Thread.sleep(delay);
        ti.verificarPorTareas();

        if(doSuspend)
        {
          synchronized(this)
          {
            wait();
          }
        }
      }
    }
    catch(InterruptedException e)
    {
      System.out.println(e);
    }
  }

  public void startTimer()
  {
    if(thread == null)
    {
      thread = new Thread(this);
      doRun  = true;
      thread.start();
    }
  }
  
  public void stopTimer()
  {
    if(thread != null)
    {
      doRun   = false;
      thread  = null;
    }
  }
  
  public void suspendTimer()
  {
    if(thread != null && !doSuspend)
    {
      doSuspend = true;
    }
  }
  public void resumeTimer()
  {
    if(thread != null && doSuspend)
    {
      synchronized(this)
      {
        doSuspend = false;
        notifyAll();
      }
    }
  }
}