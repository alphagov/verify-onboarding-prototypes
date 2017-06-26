<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.SuccessMatchView" -->
<!DOCTYPE html>
<html>

   <#include "common/head.ftl">

   <body>

      <#include "common/header.ftl">

      <main id="content">
         <h1 class="heading-large">Send a 'SUCCESS_MATCH' Response</h1>

         <form action="${sendSamlResponseUrl}" method="POST">
            <input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <div>
               Please enter your assertion consumer service URL: <br/>
               <input type="text" value="" name="assertionConsumerServiceUrl"/>
            </div>
            <div>
               Please enter PID: <br/><input type="text" value="pid" name="pid"/>
            </div>
            <div>
               <input type="radio" name="levelOfAssurance" value="LEVEL_1" checked>Level Of Assurance 1</input><br>
               <input type="radio" name="levelOfAssurance" value="LEVEL_2">Level Of Assurance 2</input><br>
            </div>

            <div>
               <input type="submit" id="continue-button" class="button" value="Send Saml Response to RP"/>
            </div>
         </form>
      </main>

      <#include "common/footer.ftl">

   </body>
</html>