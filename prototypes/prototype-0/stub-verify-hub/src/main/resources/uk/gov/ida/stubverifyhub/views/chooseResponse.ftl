<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.ChooseResponsePage" -->
<!DOCTYPE html>
<html>

   <#include "common/head.ftl">

   <body>

      <#include "common/header.ftl">

      <main id="content">
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
      </main>

      <#include "common/footer.ftl">

   </body>
</html>