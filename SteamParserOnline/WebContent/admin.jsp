<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<gui:page title="Admin-Page">
	<gui:header headline="x.gif" logoLink="/SteamParserOnline"
		logoLinkAlt="Startseite" logoLinkTarget="_blank">
	</gui:header>
	<gui:contentarea>
		<gui:workplace>
			<b><u>Log-Upload</u></b>
			<br>
			<br>
			<gui:form action="/admin" styleName="uploadForm"
				enctype="multipart/form-data">
				Zip-Datei: <input name="zipFile" type="file" size="50"
					maxLength="10000" accept="application/zip" />
				<br>
				<br>
				<gui:radiobutton value="cs" property="gameType" width="80">CS: Source</gui:radiobutton>
				<gui:radiobutton value="hl2dm" property="gameType" width="120">HL2: Deathmatch</gui:radiobutton>
				<br>
				<gui:button submit="uploadFile">Datei hochladen</gui:button>
				<gui:button submit="resetDB">Datenbank zurücksetzen</gui:button>
				<gui:button submit="insertDefault">Archiv parsen</gui:button>
			</gui:form>
			<logic:present name="parseLog">
				<br>
				<br>
				<b><u>Parser-Log</u></b>
				<br>
				<br>
				<span style="font-family:Courier New; font-size:120%"> <bean:write
					name="parseLog" filter="false" /> </span>
			</logic:present>

		</gui:workplace>
	</gui:contentarea>
</gui:page>
