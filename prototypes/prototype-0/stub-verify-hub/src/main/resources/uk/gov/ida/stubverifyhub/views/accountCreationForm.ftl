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
                <div class="form-group">
                    <label class="form-label" for="assertion-consumer-service-url">Please enter your assertion consumer service URL:</label>
                    <input class="form-control" id="assertion-consumer-service-url" value="http://localhost:3200/verify/response" name="assertionConsumerServiceUrl"/>
                </div>
                <div class="form-group">
                    <label class="form-label" for="pid">Please enter PID:</label>
                    <input class="form-control" id="pid" value="new-user-pid" name="pid"/>
                </div>
                <div class="form-group">
                    <div class="multiple-choice">
                        <input id="level-of-assurance-1" type="radio" name="levelOfAssurance" value="LEVEL_1" checked/>
                        <label for="level-of-assurance-1">Level Of Assurance 1</label>
                    </div>
                    <div class="multiple-choice">
                        <input id="level-of-assurance-2" type="radio" name="levelOfAssurance" value="LEVEL_2"/>
                        <label for="level-of-assurance-2">Level Of Assurance 2</label>
                    </div>
                </div>

                <h4 class="heading-small">Set values for one or more of the following:</h4>

                <fieldset>
                    <div class="form-group">
                        <label class="form-label" for="first-name">First Name:</label>
                        <input class="form-control" id="first-name" name="firstName"/>
                        <input type="checkbox" id="first-name-verified" name="firstNameVerified" value="true"/>
                        <label for="first-name-verified">Verified?</label>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="middle-name">Middle Name:</label>
                        <input class="form-control" id="middle-name" name="middleName"/>
                        <input type="checkbox" id="middle-name-verified" name="middleNameVerified" value="true"/>
                        <label for="middle-name-verified">Verified?</label>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="surname">Surname:</label>
                        <input class="form-control" id="surname" name="surname"/>
                        <input type="checkbox" id="surname-verified" name="surnameVerified" value="true"/>
                        <label for="surname-verified">Verified?</label>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="date-of-birth">Date Of Birth:</label>
                        <input class="form-control" id="date-of-birth" name="dateOfBirth" type="date"/>
                        <input type="checkbox" id="date-of-birth-verified" name="dateOfBirthVerified" value="true"/>
                        <label for="date-of-birth-verified">Verified?</label>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="cycle3">Cycle3:</label>
                        <input class="form-control" id="cycle3" name="cycle3" />
                    </div>
                </fieldset>

                <h4 class="heading-small">Current Address</h4>

                <fieldset>
                    <div class="form-group">
                        <label class="form-label" for="address-line-1">Line 1:</label>
                        <input class="form-control" id="address-line-1" name="addressLine1"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="address-line-2">Line 2:</label>
                        <input class="form-control" id="address-line-2" name="addressLine2"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="address-line-3">Line 3:</label>
                        <input class="form-control" id="address-line-3" name="addressLine3"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="post-code">Post Code:</label>
                        <input class="form-control" id="post-code" name="postCode"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="international-post-code">International Post Code:</label>
                        <input class="form-control" id="international-post-code" name="internationalPostCode"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="uprn">UPRN:</label>
                        <input class="form-control" id="uprn" name="uprn"/>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="from-date">From Date:</label>
                        <input class="form-control" id="from-date" name="fromDate" type="date" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="to-date">To Date:</label>
                        <input class="form-control" id="to-date" name="toDate" type="date" />
                    </div>
                    <div class="form-group">
                        <div class="multiple-choice">
                            <input type="checkbox" id="address-verified" name="addressVerified" value="true"/>
                            <label for="address-verified">Address verified?</label>
                        </div>
                    </div>
                </fieldset>

                <input type="submit" id="continue-button" class="button" value="Send Saml Response to RP"/>
            </form>
        </main>

        <#include "common/footer.ftl">

    </body>
</html>
