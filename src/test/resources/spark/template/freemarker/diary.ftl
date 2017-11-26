<!DOCTYPE html>
<html>
	<head>
		  <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
	</head>
    <body>
        <div class="col-sm-3 col-sm-offset-4 frame">
        <button id="speaking">Speak</button>        
            <ul>
            	<#list message as m>
            		<li style="width:100%;"><div class="msj-rta macro"><div class="text text-r"><p class="forSpeak">${m}</p><p><small> </small></p></div></li>
				</#list>
            </ul>
            <div>
                <div class="msj-rta macro" style="margin:auto">                        
                    <div class="text text-r" style="background:whitesmoke !important">
                        <input class="mytext" placeholder="Type a message"/>
                    </div> 
                </div>
            </div>
        </div>
    </body>
</html>


<script>
function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0'+minutes : minutes;
    var strTime = hours + ':' + minutes + ' ' + ampm;
    return strTime;
}            

//-- No use time. It is a javaScript effect.
function insertChat(who, text, time = 0){
    var control = "";
    var date = formatAMPM(new Date());
    
    control = '<li style="width:100%;">' +
                    '<div class="msj-rta macro">' +
                        '<div class="text text-r">' +
                            '<p class="forSpeak">'+text+'</p>' +
                            '<p><small>'+date+'</small></p>' +
                        '</div>' +                                
              '</li>';
    
    setTimeout(
        function(){
            $("ul").append(control);
        }, time
    );
    
	$.post( "/senctence", { sentence: text, diaryId: who } );
}

function resetChat(){
    $("ul").empty();
}

$(".mytext").on("keyup", function(e){
    if (e.which == 13){
        var text = $(this).val();
        if (text !== ""){
            insertChat('${diaryId}', text);
            $(this).val('');
        }
    }
});




    var text = document.querySelector(".mytext");
    var startBtn = document.querySelector("#recording");

    var recognition = new webkitSpeechRecognition();
    var utterance = window.speechSynthesis;
    // set params
    recognition.continuous = false;
    recognition.lang = 'zh-TW';
    recognition.interimResults = true;

	recognition.start();


    recognition.onresult = function(event) {
      var result = event.results[event.results.length - 1];
      text.value = (result[result.length - 1 ].transcript + '\n');
    }
	/*
    recognition.onsoundend = function() {
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          document.getElementById("Content").innerHTML += this.responseText;
          utterance.speak(this.responseText);
        }
      };
      xhttp.open("GET", "/convasation", true);
      xhttp.send();
    }*/
    // speech error handling
    recognition.onerror = function(event){
      console.log('error', event);
    }

    recognition.onend = function() {
      if (text.value.trim() != '') {
      	insertChat('${diaryId}', text.value);
      	text.value = '';
      }
      	  
      console.log("end");
      // auto restart
      recognition.start();
    }
	
	/*
    startBtn.addEventListener("click", function() {
      recognition.start();
      this.style.display = "none";
      document.querySelector("#content").style.display = "block";
    });
    */

    var speakingBtn = document.querySelector("#speaking");
    speakingBtn.addEventListener("click", function() {
      var speakText = '';
      $('.forSpeak').each(function() {
      	speakText += $(this).html();
      });
      var u = new SpeechSynthesisUtterance(speakText), voices = speechSynthesis.getVoices();
      u.voice = voices[44];
      speechSynthesis.speak(u);
    });
</script>

<style>
.mytext{
    border:0;padding:10px;background:whitesmoke;
}
.text{
    width:75%;display:flex;flex-direction:column;
}
.text > p:first-of-type{
    width:100%;margin-top:0;margin-bottom:auto;line-height: 13px;font-size: 12px;
}
.text > p:last-of-type{
    width:100%;text-align:right;color:silver;margin-bottom:-7px;margin-top:auto;
}
.text-l{
    float:left;padding-right:10px;
}        
.text-r{
    float:right;padding-left:10px;
}
.avatar{
    display:flex;
    justify-content:center;
    align-items:center;
    width:25%;
    float:left;
    padding-right:10px;
}
.macro{
    margin-top:5px;width:85%;border-radius:5px;padding:5px;display:flex;
}
.msj-rta{
    float:right;background:whitesmoke;
}
.msj{
    float:left;background:white;
}
.frame{
    background:#e0e0de;
    height:450px;
    overflow:hidden;
    padding:0;
}
.frame > div:last-of-type{
    position:absolute;bottom:5px;width:100%;display:flex;
}
ul {
    width:100%;
    list-style-type: none;
    padding:18px;
    position:absolute;
    bottom:32px;
    display:flex;
    flex-direction: column;

}
.msj:before{
    width: 0;
    height: 0;
    content:"";
    top:-5px;
    left:-14px;
    position:relative;
    border-style: solid;
    border-width: 0 13px 13px 0;
    border-color: transparent #ffffff transparent transparent;            
}
.msj-rta:after{
    width: 0;
    height: 0;
    content:"";
    top:-5px;
    left:14px;
    position:relative;
    border-style: solid;
    border-width: 13px 13px 0 0;
    border-color: whitesmoke transparent transparent transparent;           
}  
input:focus{
    outline: none;
}        
::-webkit-input-placeholder { /* Chrome/Opera/Safari */
    color: #d4d4d4;
}
::-moz-placeholder { /* Firefox 19+ */
    color: #d4d4d4;
}
:-ms-input-placeholder { /* IE 10+ */
    color: #d4d4d4;
}
:-moz-placeholder { /* Firefox 18- */
    color: #d4d4d4;
}   
</style>