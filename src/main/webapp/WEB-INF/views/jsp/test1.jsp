<HTML>
<HEAD>
<TITLE>Your Title Here</TITLE>
</HEAD>
<BODY  BGCOLOR="blue">

<article>
  <header>
   <div style="text-align:center">  <h1 style="
    font-size: 34pt;
    font-family: serif;
    font-style: italic;
    font-feature-settings: initial;
    color: rgba(169, 66, 66, 0.98);">Get the files or folders from One Micorsoft One Drive Cloud from the shared link</h1>
    
    </div>
  </header>
  
</article>
<div style="text-align:center"> 
<form name="myForm" action="path1" method="POST">
<p style="
    font-size: 22pt;
    font-family: monospace;
    font-style: inherit;
    font-feature-settings: initial;
    color: #0b83e9;
    width: 1545px;
    margin-top: 85px;">Enter the shared  One drive URL :</p> <input id="text1" type="text" >
<!-- <input id="param1" type="hidden" name="param1" value="Test"> -->
<input id="param2" type="hidden" name="param2" value="Test2">
<input type="button" style="font-size:13pt;color:white;background-color:green;border: 13px solid #336600;padding:3px;" Value="Download And Convert" onclick="submitform();">
</form>
</div>


<script type= text/javascript>

function submitform(){
	
	document.getElementById("param2").value = document.getElementById("text1").value;
	<!--get the token value from headerx-->
	location.parseHash = function(){
		   var hash = (this.hash ||'').replace(/^#/,'').split('&'),
		       parsed = {};

		   for(var i =0,el;i<hash.length; i++ ){
		        el=hash[i].split('=')
		        parsed[el[0]] = el[1];
		   }
		   return parsed;
		};

		/* var obj= location.parseHash();
		    obj.hash;  //fdg 
		    document.getElementById("param1").value = obj.access_token;   //value2 */
		    document.myForm.submit();
}


</script>

</BODY>
</HTML>