<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.GenerateSamlResponseForm" -->
<html>
 <body>
    <h1>Send a Response</h1>
	<form action="${sendSamlResponseUrl}" method="POST">
		<input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
		<input type="hidden" name="RelayState" value="${relayState}"/>
		<div>
		<input type="text" value="" name="assertionConsumerServiceUrl"/>
		</div>
		<div>
		<label><input type="radio" name="LevelOfAssurance" value="LEVEL_1" checked> Level Of Assurance 1</label>
		</div>
		<div>
		<label><input type="radio" name="LevelOfAssurance" value="LEVEL_2"> Level Of Assurance 2</label>
		</div>
		<div>
		<input type="submit" id="continue-button" value="Send Saml Response to RP"/>
		</div>
	 </form>
 </body>
</html>
