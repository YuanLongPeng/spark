package spark.examples.session;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

public class SessionExample {
    private static final String SESSION_NAME = "email";

    public static void main(String[] args) {
    	
    	
        get("/", (req, res) -> {
        	System.out.print("get/ \n");
        	String email = req.session().attribute(SESSION_NAME);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            
            if(email != null) {
        		res.redirect("/main");
                return null;
            }

            return new ModelAndView(attributes, "signin.ftl");
        }, new FreeMarkerEngine());

        get("/logout", (req, res) -> {
   	    	req.session().attribute(SESSION_NAME, null);
    		res.redirect("/");
   	    	return true;
        });
        
        get("/main", (req, res) -> {
        	System.out.print("get/signin \n");
        	String email = req.session().attribute(SESSION_NAME);

            if(email == null) {
        		res.redirect("/");
                return null;
            }
            
        	List<String> diary = new ArrayList<String>();
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT DIARY_TITLE FROM DIARY D WHERE email=? AND DIARY_TITLE IS NOT NULL");
           		
       			pstmt.setString(1, email);
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    while (rset.next()) {
           	    	diary.add(rset.getString("DIARY_TITLE"));
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", diary);
            
            return new ModelAndView(attributes, "main.ftl");
        }, new FreeMarkerEngine());

        post("/signin", (req, res) -> {
        	System.out.print("post/signin \n");
        	String login_email =  req.queryParams("email");
        	System.out.print("login_email: "+login_email+"\n");
        	String login_password = req.queryParams("password");
        	System.out.print("login_password: "+login_password+"\n");

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT * FROM OWNER WHERE email=? AND password=?");
           		
       			pstmt.setString(1, login_email);
            	pstmt.setString(2, login_password);
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    while (rset.next()) {
           	    	success = true;
           	    	req.session().attribute(SESSION_NAME, login_email);
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	if(success) {
        		res.redirect("/main");
            } else {
            	res.redirect("/");
            }
        	
            return null;
        });

        post("/diary", (req, res) -> {
        	System.out.print("post/diary \n");
        	String email = req.session().attribute(SESSION_NAME);
        	String diary = req.queryParams("diary");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "INSERT INTO DIARY (EMAIL, DIARY_TITLE, LATESTIP) VALUES (?, ?, ?)");

       			pstmt.setString(1, email);
       			pstmt.setString(2, diary);
       			pstmt.setString(3, req.ip());
       			success = pstmt.execute();

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}

    		res.redirect("/main");
            return success;
        });
        
        get("/senctence", (req, res) -> {
        	System.out.print("get/senctence \n");
        	String email = req.session().attribute(SESSION_NAME);
        	String diaryId = req.queryParams("diaryId");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	List<String> sentences = new ArrayList<String>();
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT * FROM SENTENCE WHERE DIARYID=?");
           		
       			pstmt.setString(1, diaryId);
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    while (rset.next()) {
                	sentences.add(rset.getString("CONTENT"));
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", sentences);
            attributes.put("diaryId", diaryId);
            
            return new ModelAndView(attributes, "diary.ftl");
        }, new FreeMarkerEngine());
        
        
        post("/senctence", (req, res) -> {
        	System.out.print("post/senctence \n");
        	String email = req.session().attribute(SESSION_NAME);
        	String diaryId = req.queryParams("diaryId");
        	String sentence = req.queryParams("sentence");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "INSERT INTO SENTENCE (DIARYID, CONTENT, CREATE_IP) VALUES (?, ?, ?)");
           		
       			pstmt.setString(1, diaryId);
       			pstmt.setString(2, sentence);
       			pstmt.setString(3, req.ip());
       			success = pstmt.execute();

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });
        
        
        get("/theme", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	List<String> themes = new ArrayList<String>();
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT * FROM THEME");
           		
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    if (rset.next()) {
           	    	themes.add(rset.getString("TITLE"));
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);        		
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", themes);

            return new ModelAndView(attributes, "theme.ftl");
        }, new FreeMarkerEngine());
        

        post("/theme", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String title = req.queryParams("title");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "INSERT INTO THEME (TITLE) VALUES (?)");
           		
       			pstmt.setString(1, title);
       			success = pstmt.execute();

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });

        delete("/theme", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String title = req.params(":title");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			"DELETE FROM THEME WHERE title=?");
           		
       			pstmt.setString(1, title);
       			success = pstmt.execute();

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });
        
        
        get("/user", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String email_para = req.params(":email");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}

            Map<String, Object> attributes = new HashMap<>();
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT * FROM OWNER WHERE EMAIL=?");
            	
       			pstmt.setString(1, email_para);
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    if (rset.next()) {
                    attributes.put("message", rset.getString("CONTENT"));
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);        		
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	
            return new ModelAndView(attributes, "user.ftl");
        }, new FreeMarkerEngine());
        
        
        post("/user", (req, res) -> {
        	System.out.print("post/user \n");
        	String email = req.session().attribute(SESSION_NAME);
        	String email_para = req.queryParams("email")==null?"":req.queryParams("email").trim();
        	System.out.print("email_para: "+email_para+"\n");
        	String password = req.queryParams("password")==null?"":req.queryParams("password").trim();
        	System.out.print("password: "+password+"\n");
        	String birthday = req.queryParams("birthday")==null?"":req.queryParams("birthday").trim();
        	System.out.print("birthday: "+birthday+"\n");
        	String sex = req.queryParams("sex")==null?"":req.queryParams("sex").trim();
        	System.out.print("sex: "+sex+"\n");
        	
        	if(email != null) {
        		res.redirect("/");
                return null;
        	}

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
          			  "INSERT INTO OWNER (EMAIL, PASSWORD, BIRTHDAY, SEX) VALUES (?, ?, ?, ?)");
            	
       			pstmt.setString(1, email_para);
       			pstmt.setString(2, password);
       			pstmt.setDate(3, java.sql.Date.valueOf(birthday));
       			pstmt.setString(4, sex);

           	    
           	    if (pstmt.execute()) {
           	    	success = true;
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} catch(Exception notFound) {
        		System.out.print("Exception: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
        	res.redirect("/");
            return success;
        });

        put("/user", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String passport = req.params(":passport");
        	String birthday = req.params(":birthday");
        	String sex = req.params(":sex");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "UPDATE OWNER SET PASSWORD=?, BIRTHDAY=?, SEX=? WHERE EMAIL=?");
            	
       			pstmt.setString(1, passport);
       			pstmt.setString(2, birthday);
       			pstmt.setString(3, sex);
       			pstmt.setString(4, email);
           	    
           	    if (pstmt.execute()) {
           	    	success = true;
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);        		
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });
        
        
        get("/interest", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}

        	List<String> interests = new ArrayList<String>();
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "SELECT * FROM INTEREST WHERE EMAIL=?");
            	
       			pstmt.setString(1, email);
           	    ResultSet rset = pstmt.executeQuery();
           	    
           	    if (rset.next()) {
                    Map<String, Object> container = new HashMap<>();
           	    	interests.add(rset.getString("TITLE"));
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);        		
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", interests);
            
            return new ModelAndView(attributes, "interest.ftl");
        }, new FreeMarkerEngine());

        post("/interest", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String title = req.queryParams("title");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
          			  "INSERT INTO INTEREST (EMAIL, TITLE) VALUES (?, ?)");
            	
       			pstmt.setString(1, email);
       			pstmt.setString(2, title);
           	    
           	    if (pstmt.execute()) {
           	    	success = true;
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);        		
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });

        put("/interest", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	String title = req.params(":title");
        	String update_date = req.params(":update_date");
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}

        	boolean success = false;
        	Connection conn = null;
        	PreparedStatement pstmt = null;
        	try {
            	conn = new connectDB().getConnection();
            	
            	pstmt = conn.prepareStatement(
            			  "UPDATE INTEREST SET EMAIL=?, TITLE=?, UPDATE_DATE=?");
            	
       			pstmt.setString(1, email);
       			pstmt.setString(2, title);
       			pstmt.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
           	    
           	    if (pstmt.execute()) {
           	    	success = true;
           	    }

        	} catch(SQLException ex) {
        		System.out.print("SQLException: "+ex);
        	} catch(ClassNotFoundException notFound) {
        		System.out.print("ClassNotFoundException: "+notFound);
        	} finally {
            	pstmt.close();
           	    conn.close();
        	}
        	
            return success;
        });

        delete("/interest", (req, res) -> {
        	String email = req.session().attribute(SESSION_NAME);
        	
        	if(email == null) {
        		res.redirect("/");
                return null;
        	}
        	
        	
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "interest.ftl");
        }, new FreeMarkerEngine());
    	
    	/*
        get("/", (request, response) -> {
            String name = request.session().attribute(SESSION_NAME);
            if (name == null) {
                return "<html><body>What's your name?: <form action=\"/entry\" method=\"POST\"><input type=\"text\" name=\"name\"/><input type=\"submit\" value=\"go\"/></form></body></html>";
            } else {
                return String.format("<html><body>Hello, %s!</body></html>", name);
            }
        });

        post("/entry", (request, response) -> {
            String name = request.queryParams("name");
            if (name != null) {
                request.session().attribute(SESSION_NAME, name);
            }
            response.redirect("/");
            return null;
        });

        get("/clear", (request, response) -> {
            request.session().removeAttribute(SESSION_NAME);
            response.redirect("/");
            return null;
        });*/
    }
    

    public boolean isLogin(Request request, Response response) {
    	String email = request.session().attribute(SESSION_NAME);
    	
    	if(email == null) {
            response.redirect("/");
            return false;
    	} 
        return true;
    }
    
    public void login(Request request, Response response) {
        String email = request.queryParams("email");
        if (email != null) {
            request.session().attribute(SESSION_NAME, email);
        }
        response.redirect("/");
        //return null;
    }


    public void logout(Request request, Response response) {
        request.session().removeAttribute(SESSION_NAME);
        response.redirect("/");
    }

}
