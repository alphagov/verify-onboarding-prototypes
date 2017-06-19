export function createSamlForm (location: string, samlRequest: string) {
  return `
      <h1>Send SAML Authn request to hub</h1>
			<form method='post' action='${location}'>
				<input type='hidden' name='samlRequest' value='${samlRequest}'/>
				<input type='hidden' name='relayState' value=''/>
				<button>Submit</button>
			</form>
			<script>
				document.forms[0].submit()
			</script>
      `
}
