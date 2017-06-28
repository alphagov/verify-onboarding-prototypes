<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.AccountCreationView" -->
<!DOCTYPE html>
<html>

<#include "common/head.ftl">

   <body>

      <#include "common/header.ftl">

      <main id="content">
         <h1 class="heading-large">Send a 'ACCOUNT_CREATION' Response</h1>

         <form action="${sendSamlResponseUrl}" method="POST">
            <input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
            <input type="hidden" name="relayState" value="${relayState}"/>
            <div>
               Please enter your assertion consumer service URL: <br/>
               <input type="text" value="" name="assertionConsumerServiceUrl"/>
            </div><br>
            <div>
               Please enter PID: <br/><input type="text" value="pid" name="pid"/>
            </div><br>
            <div>
               <input type="radio" name="levelOfAssurance" value="LEVEL_1" checked>Level Of Assurance 1</input><br>
               <input type="radio" name="levelOfAssurance" value="LEVEL_2">Level Of Assurance 2</input><br>
            </div><br>
            <div>Set values for one or more of the following:</div><br>
            <div>First Name: <input name="firstName"/><input type="checkbox" name="firstNameVerified" value="false">Verified?</input></div>
            <div>Middle Name: <input name="middleName"/><input type="checkbox" name="middleNameVerified" value="false">Verified?</input></div>
            <div>Surname: <input name="surname"/><input type="checkbox" name="surnameVerified" value="false">Verified?</input></div>
            <div>Date Of Birth: <input name="dateOfBirth" type="date"/><input type="checkbox" name="dateOfBirthVerified" value="false">Verified?</input></div><br>
            <div>
               <div>Current Address <input type="checkbox" name="addressVerified" value="false">Verified?</input></div>
               <div>Line 1: <input name="addressLine1"/></div>
               <div>Line 2: <input name="addressLine2"/></div>
               <div>Line 3: <input name="addressLine3"/></div>
               <div>Post Code: <input name="postCode"/></div>
               <div>International Post Code: <input name="internationalPostCode"/></div>
               <div>UPRN: <input name="uprn"/></div>
               <div>From Date: <input name="fromDate" type="date"/></div>
               <div>To Date: <input name="toDate" type="date"/></div>
            </div>
            <br/>
            <div>Cycle3: <input name="cycle3"/></div><br>
            <div><input type="submit" id="continue-button" class="button" value="Send Saml Response to RP"/></div>
         </form>
      </main>

      <#include "common/footer.ftl">

   </body>
</html>
