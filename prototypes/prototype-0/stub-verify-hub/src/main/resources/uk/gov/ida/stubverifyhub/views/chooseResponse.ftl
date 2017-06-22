<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.ChooseResponsePage" -->
<html>
   <body>
      <h1>Select the type of Response</h1>

      <form action="${generateSamlResponseFormUrl}" method="POST">
         <input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
         <input type="hidden" name="relayState" value="${relayState}"/>
         <div>
            <input type="radio" name="responseType" value="SUCCESS_MATCH" checked>SUCCESS_MATCH</input><br/>
            <input type="radio" name="responseType" value="ACCOUNT_CREATION">ACCOUNT_CREATION</input><br/>
         </div>
         <div>
            <input type="submit" id="continue-button" value="Continue"/>
         </div>
      </form>
   </body>
</html>
