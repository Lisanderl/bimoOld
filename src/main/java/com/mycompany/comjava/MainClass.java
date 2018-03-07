
package com.mycompany.comjava;

import com.mycompany.comjava.gui.MainFrame;


public class MainClass {
    public static void main (String[] agrs){
      
      int i;
        for (i = 0; i !=10; i++) {
            System.out.println(i);
        }
      
     MainFrame s = new MainFrame();
       Controller c = new Controller(s);
       
        
    }
    
}
