
package com.mycompany.comjava;

import com.mycompany.comjava.controller.GuiController;
import com.mycompany.comjava.gui.MainFrame;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class MainClass {
    public static void main (String[] agrs){
      
      int i;
        for (i = 0; i !=10; i++) {
            System.out.println(i);
        }
       new GuiController(new MainFrame());

    }
    
}
