<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<gui:page title="Login">
	<gui:header headline="x.gif" logoLink="/SteamParserOnline"
		logoLinkAlt="Startseite" logoLinkTarget="_blank">
	</gui:header>
	<gui:contentarea>
		<gui:workplace>

			<logic:present name="error" scope="request">
				<gui:informationarea>
					<gui:status><bean:message key="error.userlogin" /></gui:status>
				</gui:informationarea>
			</logic:present>

			<logic:notPresent name="error" scope="request">
				<gui:informationarea>
					<gui:status>Benutzername und Passwort eingeben</gui:status>
				</gui:informationarea>
				<gui:form action="/login.do" styleName="loginForm"
					startupFocusField="username" defaultSubmitButton="performLogin">
					<gui:label properties="username">Benutzername:</gui:label>
					<gui:text property="username" width="150" />
					<gui:label properties="password">Passwort:</gui:label>
					<gui:password property="password" width="150" />
					<gui:button submit="performLogin">Anmelden</gui:button>
				</gui:form>
			</logic:notPresent>

		</gui:workplace>
	</gui:contentarea>
</gui:page>
