<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.SamlResponseForm" -->
<html>
    <body>
    	<form action="${assertionConsumerService}" method="POST">
        	<input type="hidden" value="SAMLResponse" name="${samlResponse}"/>
        	<input type="hidden" value="RelayState" name="${relayState}"/>
        	<input type="submit" id="continue-button" value="Send Saml Response to RP"/>
        </form>
    </body>
</html>