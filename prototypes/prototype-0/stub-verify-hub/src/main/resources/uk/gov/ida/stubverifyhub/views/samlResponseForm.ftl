<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.SamlResponseForm" -->
<html>
    <body>
        <form action="${assertionConsumerService}" method="POST">
            <input type="hidden" name="SAMLResponse" value="${samlResponse}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <input type="submit" id="continue-button" value="Send Saml Response to RP"/>
        </form>
    </body>
</html>
