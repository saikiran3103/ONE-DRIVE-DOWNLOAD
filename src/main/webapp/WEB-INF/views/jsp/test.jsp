<HTML>
<HEAD>
<TITLE>Your Title Here</TITLE>
</HEAD>
<BODY BGCOLOR="FFFFFF">

<article>
  <header>
   <div style="text-align:center">  <h1>Get the files or folders from One Micorsoft One Drive Cloud from the shared link</h1>
    
    </div>
  </header>
  
</article>
<div style="text-align:center"> 
<form name="myForm" action="path" method="POST">
Enter the shared  One drive URL : <input id="text1" type="text" >
<input id="param1" type="hidden" name="param1" value="Test">
<input id="param2" type="hidden" name="param2" value="Test2">
<input type="button" Value="Download And Convert" onclick="submitform();">
</form>
</div>


<script type= text/javascript>

function submitform(){
	
	document.getElementById("param2").value = document.getElementById("text1").value;
	<!--get the token value from header-->
	location.parseHash = function(){
		   var hash = (this.hash ||'').replace(/^#/,'').split('&'),
		       parsed = {};

		   for(var i =0,el;i<hash.length; i++ ){
		        el=hash[i].split('=')
		        parsed[el[0]] = el[1];
		   }
		   return parsed;
		};

		var obj= location.parseHash();
		    obj.hash;  //fdg 
		    document.getElementById("param1").value = obj.access_token;   //value2x
		    document.myForm.submit();
}


</script>

</BODY>
</HTML>