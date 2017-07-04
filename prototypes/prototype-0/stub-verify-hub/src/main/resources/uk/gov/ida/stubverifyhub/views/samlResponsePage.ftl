<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.SamlResponsePageViev" -->
<html>
    <body>
        <form action="${assertionConsumerService}" method="POST">
        	<h1>Send SAML Response to RP</h1>
            <input type="hidden" name="SAMLResponse" value="${samlResponse}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <input type="submit" id="continue-button" value="Continue"/>
        </form>
        <script>
           var form = document.forms[0]
           form.setAttribute('style', 'display: none;')
           window.setTimeout(function () { form.removeAttribute('style') }, 5000)
           form.submit()
         </script>
    </body>
</html>