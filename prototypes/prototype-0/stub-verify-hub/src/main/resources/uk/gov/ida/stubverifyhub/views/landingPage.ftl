<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.LandingPageView" -->
<html>
    <body>
    	<h1>You've sent a request.</h1>
    	<p>If this was the real Verify Hub service, you would see the following page:</p>
    	<div>
    		<img src="/assets/verify-landing-page.png" alt="Verify Hub Landing Page" height="100" width="100">
    	</div>
    	<form action="${generateSamlUrl}" method="POST">
        	<input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
        	<input type="hidden" name="relayState" value="${relayState}"/>
        	<input type="submit" id="continue-button" value="Continue"/>
        </form>
    </body>
</html>