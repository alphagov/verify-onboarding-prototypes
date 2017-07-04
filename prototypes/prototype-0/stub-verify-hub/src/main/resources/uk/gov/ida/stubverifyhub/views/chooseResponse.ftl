<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.ChooseResponsePage" -->
<!DOCTYPE html>
<html>

   <#include "common/head.ftl">

   <body>

      <#include "common/header.ftl">

      <main id="content">
         <h1 class="heading-large">Select the type of Response</h1>
         <form action="${generateSamlResponseFormUrl}" method="POST">
            <input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <div class="form-group">
               <div class="multiple-choice">
                 <input id="success-match" type="radio" name="scenario" value="SUCCESS_MATCH" checked></input>
                 <label for="success-match">Success Match</label>
               </div>
               <div class="multiple-choice">
                 <input id="account-creation" type="radio" name="scenario" value="ACCOUNT_CREATION"></input>
                 <label for="account-creation">Account Creation</label>
               </div>
               <div class="multiple-choice">
                 <input id="authentication-failed" type="radio" name="scenario" value="AUTHENTICATION_FAILED"></input>
                 <label for="authentication-failed">Authentication Failed</label>
               </div>
               <div class="multiple-choice">
                 <input id="no-match" type="radio" name="scenario" value="NO_MATCH"></input>
                 <label for="no-match">No Match</label>
               </div>
               <div class="multiple-choice">
                 <input id="cancellation" type="radio" name="scenario" value="CANCELLATION"></input>
                 <label for="cancellation">Cancellation</label>
               </div>
            </div>
            <div>
               <input type="submit" id="continue-button" class="button" value="Continue"/>
            </div>
         </form>
      </main>

      <#include "common/footer.ftl">

   </body>
</html>
