package app;

import static spark.Spark.get;
import static spark.Spark.post;

import spark.Request;
import spark.Response;


public class session {
    private static final String SESSION_NAME = "email";
    private Request request;
    private Response response;
    
    public void session(Request request, Response response) {
    	this.request = request;
    	this.response = response;

        
    }

}
