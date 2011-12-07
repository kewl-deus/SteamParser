<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<logic:notPresent name="user" scope="session">
	<meta http-equiv="refresh" content="0; URL=login.do">
</logic:notPresent>
<logic:present name="user" scope="session">
<gui:page title="Steam Loggers">
	<gui:header headline="x.gif" logoLink="/SteamParserOnline"
		logoLinkAlt="Startseite" logoLinkTarget="_blank">
	</gui:header>
	<gui:contentarea>
		<gui:workplace>

			<gui:form action="/manageLoggers.do" styleName="loggerManagementForm">
				<table>
					<tr>
						<td><gui:label properties="port">Port</gui:label></td>
						<td><gui:text property="port" width="70" /></td>
						<td><gui:radiobutton value="cs" property="gameType" width="80">CS: Source</gui:radiobutton>
						</td>
						<td><gui:radiobutton value="hl2dm" property="gameType" width="120">HL2: Deathmatch</gui:radiobutton>
						</td>
						<td><gui:label properties="notifyPattern">NotifyPattern</gui:label>
						</td>
						<td><gui:text property="notifyPattern" width="250" /></td>
						<td><gui:button submit="createLogger">Create Logger</gui:button></td>
					</tr>
				</table>

			</gui:form>

			<hr />

			<logic:present name="loggerTable">

				<p><b>Current SteamLogParsers:</b></p>
				<gui:table name="loggerTable" scope="session">
					<gui:definition>
						<gui:column name="port" width="70">
							UDP-Port
						</gui:column>
						<gui:column name="observers" width="250">
							Observers
						</gui:column>
						<gui:column name="notifyPattern" width="250">
							NotifyPattern
						</gui:column>
						<gui:column name="status" width="85">
							Status
						</gui:column>

						<gui:function name="activateLogger" link="manageLoggers.do"
							image="icons/function/table/anlegen.gif" alt="Aktivieren"
							columnWidth="24" />

						<gui:function name="deactivateLogger" link="manageLoggers.do"
							image="icons/menu/schriftstueck-abschliessen.gif"
							alt="Deaktivieren" columnWidth="24" />

						<gui:function name="deleteLogger" link="manageLoggers.do"
							image="icons/function/table/loeschen.gif" alt="L&ouml;schen"
							columnWidth="24" />
							
						
					</gui:definition>
					<gui:content>
						<gui:simplecell column="port" />
						<gui:simplecell column="observers" />
						<gui:simplecell column="notifyPattern" />
						<gui:simplecell column="status" />
					</gui:content>
				</gui:table>

			</logic:present>

		</gui:workplace>
	</gui:contentarea>
</gui:page>
</logic:present>