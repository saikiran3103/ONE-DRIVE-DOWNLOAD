<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>One Drive Access</title>
 
<spring:url value="/resources/core/css/hello.css" var="coreCss" />
<spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />
</head>
<body>

<article>
  <header>
  <div style="text-align:center">  <h1 style="
    font-size: 34pt;
    font-family: serif;
    font-style: italic;
    font-feature-settings: initial;
    color: rgba(169, 66, 66, 0.98);"
>Download the files from Microsoft One Drive cloud from the shared link</h1>
    </div>
    
  </header>
  
</article>
 
 
  <hr>
 
<div class="ms-Grid-col ms-u-mdPush1 ms-u-md9 ms-u-lgPush1 ms-u-lg6">
			<div>
			<div style="text-align:center">
				<p class="ms-font" style="
    font-size: 22pt;
    font-family: monospace;
    font-style: inherit;
    font-feature-settings: initial;
    color: #0b83e9;
    width: 1545px;
    margin-top: 85px;
    ">Use the button below to connect to Office 365.</p>
    </div>
				
				<form action="token" method="GET">

<div style="text-align:center" >
<input type="submit" style="font-size:13pt;color:white;background-color:green;border: 13px solid #336600;padding:3px;" value="Connect to Office 365"></div>
</form>
				
					
			</div>
 </div>
<spring:url value="/resources/core/css/hello.js" var="coreJs" />
<spring:url value="/resources/core/css/bootstrap.min.js" var="bootstrapJs" />
 
<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 
</body>
</html>