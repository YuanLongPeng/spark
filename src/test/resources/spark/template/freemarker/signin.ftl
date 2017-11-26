<!DOCTYPE html>
<html>

<head>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
</head>

<body>

<!--Pulling Awesome Font -->
<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">

<div class="container">
    <div class="row">
	        <div class="col-md-offset-5 col-md-3">
	            <div class="form-login">
	            	<h4>Welcome back.</h4>
    				<form action="/signin" method="post">
		            	<input type="text" id="email" name="email" class="form-control input-sm chat-input" placeholder="username" />
		            	</br>
		            	<input type="password" id="password" name="password" class="form-control input-sm chat-input" placeholder="password" />
		            	</br>
			            <div class="wrapper">
				            <span class="group-btn">
				            	<input type="submit" class="btn btn-primary btn-md" value="Log in"> 
				                <a href="#" class="btn btn-primary btn-md" data-toggle="modal" data-target="#myModal">Sign up <i class="fa fa-sign-in"></i></a>
				            </span>
			            </div>
	    			</form>
	        	</div>
        	</div>
    </div>
</div>


<div class="container2">
  <!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Modal Header</h4>
        </div>
    		<form action="/user" method="post">
	        <div class="modal-body">
		            <input type="text" id="email" name="email" class="form-control input-sm chat-input" placeholder="email" />
		            <input type="password" id="password" name="password" class="form-control input-sm chat-input" placeholder="password" />
		            <input type="password" id="password_check" class="form-control input-sm chat-input" placeholder="password_check" />
		            <input type="text" id="birthday" name="birthday" class="form-control input-sm chat-input" placeholder="birthday" />
				    <label class="radio-inline">
				      <input type="radio" name="sex" value="male">male
				    </label>
				    <label class="radio-inline">
				      <input type="radio" name="sex" value="female">female
				    </label>
	        </div>
	        <div class="modal-footer">
			  <input type="submit" class="btn btn-primary btn-md" value="Sign up"> 
	          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
		</form>
      </div>
      
    </div>
  </div>
  
</div>




</body>
</html>




<style>   
   @import url(http://fonts.googleapis.com/css?family=Roboto:400);
body {
  background-color:#fff;
  -webkit-font-smoothing: antialiased;
  font: normal 14px Roboto,arial,sans-serif;
}

.container {
    padding: 25px;
    position: fixed;
}

.form-login {
    background-color: #EDEDED;
    padding-top: 10px;
    padding-bottom: 20px;
    padding-left: 20px;
    padding-right: 20px;
    border-radius: 15px;
    border-color:#d2d2d2;
    border-width: 5px;
    box-shadow:0 1px 0 #cfcfcf;
}

h4 { 
 border:0 solid #fff; 
 border-bottom-width:1px;
 padding-bottom:10px;
 text-align: center;
}

.form-control {
    border-radius: 10px;
}

.wrapper {
    text-align: center;
}

   </style>
