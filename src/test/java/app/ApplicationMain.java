package app;

import static spark.Spark.*;

import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;


public class ApplicationMain {

    public static void main(String[] args) {
    	


        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            // The hello.ftl file is located in directory:
            // src/test/resources/spark/template/freemarker
            return new ModelAndView(attributes, "hello.ftl");
        }, new FreeMarkerEngine());
            
            get("/sentence", (req, res) -> "Hello World");
//            get("/theme", (req, res) -> "Hello World");
//            get("/user", (req, res) -> "Hello World");
//            get("/interest", (req, res) -> "Hello World");
    }  
    	
    	
    

}