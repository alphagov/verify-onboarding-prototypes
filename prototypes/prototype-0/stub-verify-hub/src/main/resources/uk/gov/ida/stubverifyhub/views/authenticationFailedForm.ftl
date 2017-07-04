<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.AuthenticationFailedView" -->
<!DOCTYPE html>
<html>

   <#include "common/head.ftl">

   <body>

      <#include "common/header.ftl">

      <main id="content">
         <h1 class="heading-large">Send a 'AUTHENTICATION_FAILED' Response</h1>

         <form action="${sendSamlResponseUrl}" method="POST">
            <input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <div class="form-group">
               <label class="form-label" for="assertion-consumer-service-url">Please enter your assertion consumer service URL:</label>
               <input class="form-control" id="assertion-consumer-service-url" type="text" value="" name="assertionConsumerServiceUrl"/>
            </div>
            <div class="form-group">
               <input type="submit" id="continue-button" class="button" value="Send Saml Response to RP"/>
            </div>
         </form>
      </main>

      <#include "common/footer.ftl">

   </body>
</html>
