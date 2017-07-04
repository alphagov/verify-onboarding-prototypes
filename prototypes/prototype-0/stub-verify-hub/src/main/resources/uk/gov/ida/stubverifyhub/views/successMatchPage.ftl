<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.SuccessMatchPageView" -->
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
            <div class="form-group">
               <label class="form-label" for="assertion-consumer-service-url">Please enter your assertion consumer service URL:</label>
               <input class="form-control" id="assertion-consumer-service-url" type="text" value="" name="assertionConsumerServiceUrl"/>
            </div>
            <div class="form-group">
               <label class="form-label" for="pid">Please enter PID:</label>
               <input class="form-control" id="pid" type="text" value="pid" name="pid"/>
            </div>
            <div class="form-group">
                <div class="multiple-choice">
                   <input id="level-of-assurance-1" type="radio" name="levelOfAssurance" value="LEVEL_1" checked></input>
                   <label for="level-of-assurance-1">Level Of Assurance 1</label>
                </div>
                <div class="multiple-choice">
                   <input id="level-of-assurance-2" type="radio" name="levelOfAssurance" value="LEVEL_2"></input>
                   <label for="level-of-assurance-2">Level Of Assurance 2</label>
                </div>
           </div>
            <div class="form-group">
               <input type="submit" id="continue-button" class="button" value="Send Saml Response to RP"/>
            </div>
         </form>
      </main>

      <#include "common/footer.ftl">

   </body>
</html>
