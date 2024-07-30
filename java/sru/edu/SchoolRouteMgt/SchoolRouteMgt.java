/*
 * Group1 - School Routing
 * Current Authors: Shane Smith, Pavels Avdejevs, Scott Hardy 
 * Previous Authors: Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Application File
 * Runs the main application for the spring boot project as well as checks to make sure that the 
 * user knows when the data is ready via a pop-up.
 * 
 * 
 */
package sru.edu.SchoolRouteMgt;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SchoolRouteMgt {
    public static void main(String[] args) {
    	//spring builder allows for the frames to work
    	SpringApplicationBuilder builder = new SpringApplicationBuilder(SchoolRouteMgt.class);
        builder.headless(false).run(args);
        
        //set system message to let user know when system has loaded all the data
        System.out.println("System is Done Loading");
    }
}