<#-- @ftlvariable name="" type="uk.gov.ida.stubverifyhub.views.LandingPageView" -->
<!DOCTYPE html>
<html>

	<#include "common/head.ftl">

	<body class="">

		<#include "common/header.ftl">

		<main id="content">
			<h1 class="heading-large">You've sent a request.</h1>
			<p>If this was the real Verify Hub service, you would see the following page:</p>
			<div>
				<img src="/assets/verify-landing-page.png" alt="Verify Hub Landing Page" height="241" width="482"/>
			</div>
			<form action="${chooseResponseUrl}" method="POST">
				<input type="hidden" name="SAMLRequest" value="${samlRequest}"/>
				<input type="hidden" name="relayState" value="${relayState}"/>
				<input type="submit" id="continue-button" class="button" value="Continue"/>
			</form>
		</main>

		<#include "common/footer.ftl">

	</body>
</html>