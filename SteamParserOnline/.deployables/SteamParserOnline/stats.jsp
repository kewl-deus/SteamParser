<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<gui:page title="CS-S-Stats">
	<gui:header headline="x.gif" logoLink="/SteamParserOnline"
		logoLinkAlt="Startseite" logoLinkTarget="_blank">
		<gui:pulldownmenu>
			<gui:menu name="GameSelect">
				<gui:menuoption link="show.do?gametype=cs">CS: Source</gui:menuoption>
				<gui:menuoption link="show.do?gametype=hl2dm">HL2: Deathmatch</gui:menuoption>
				<gui:menuoption link="show.do?gametype=ALL">Alle</gui:menuoption>
			</gui:menu>
		</gui:pulldownmenu>
	</gui:header>
	<gui:contentarea>
		<gui:workplace>
			<logic:present name="playerStatsTable">
				<p><b>Player-Stats:</b></p>
				<gui:table name="playerStatsTable" scope="session"
					controllerLink="playerController.do">
					<gui:definition>
						<gui:column name="rang" width="60">
							Rang
						</gui:column>
						<gui:column name="name" width="150">
							Name
						</gui:column>
						<gui:column name="kills" width="85">
							Kills
						</gui:column>
						<gui:column name="deaths" width="85">
							Deaths
						</gui:column>
						<gui:column name="headshots" width="90">
							Headshots
						</gui:column>
						<gui:column name="hsPercentImage" width="145">
							% Headshots
						</gui:column>
						<gui:column name="killsPerDeathAsString" width="90">
							Kills per Death
						</gui:column>
						<gui:column name="sessionsPlayed" width="85">
							Sessions Played
						</gui:column>
						<gui:column name="fph" width="85">
							Frags / Hour
						</gui:column>
						<logic:notPresent name="playerFilter">
							<gui:function name="addPlayerFilter" link="filter.do"
								image="icons/function/table/ansehen.gif" alt="Filter hinzufügen"
								columnWidth="24" />
						</logic:notPresent>
						<logic:present name="playerFilter">
							<gui:function name="removePlayerFilter" link="filter.do"
								image="icons/function/table/loeschen.gif" alt="Filter entfernen"
								columnWidth="24" />
						</logic:present>
					</gui:definition>
					<gui:content>
						<gui:simplecell column="rang" />
						<gui:simplecell column="name" />
						<gui:simplecell column="kills" />
						<gui:simplecell column="deaths" />
						<gui:simplecell column="headshots" />
						<gui:patterncell column="hsPercentImage">{1}&nbsp;{2}%</gui:patterncell>
						<gui:simplecell column="killsPerDeathAsString" />
						<gui:simplecell column="sessionsPlayed" />
						<gui:simplecell column="fph" />
					</gui:content>
				</gui:table>
				<hr>
			</logic:present>
			<logic:present name="sessionStatsTable">
				<p><b>Session-Stats:</b></p>
				<gui:table name="sessionStatsTable" scope="session"
					controllerLink="sessionController.do">
					<gui:definition>
						<gui:column name="duration" width="150">
							Zeit
						</gui:column>
						<gui:column name="involvedPlayers" width="150">
							Beteiligte Spieler
						</gui:column>
						<gui:column name="mapsPlayed" width="90">
							Gespielte Maps
						</gui:column>
						<gui:column name="kills" width="90">
							Kills
						</gui:column>
						<gui:column name="headshots" width="90">
							Headshots
						</gui:column>
						<gui:column name="hsPercentImage" width="229">
							% Headshots
						</gui:column>
						<gui:column name="damageGiven" width="80">
							Total Damage
						</gui:column>
						<logic:notPresent name="sessionFilter">
							<gui:function name="addSessionFilter" link="filter.do"
								image="icons/function/table/ansehen.gif" alt="Filter hinzufügen"
								columnWidth="24" />
						</logic:notPresent>
						<logic:present name="sessionFilter">
							<gui:function name="removeSessionFilter" link="filter.do"
								image="icons/function/table/loeschen.gif" alt="Filter entfernen"
								columnWidth="24" />
						</logic:present>
					</gui:definition>
					<gui:content>
						<gui:patterncell column="duration">{1}</gui:patterncell>
						<gui:simplecell column="involvedPlayers" />
						<gui:simplecell column="mapsPlayed" />
						<gui:simplecell column="kills" />
						<gui:simplecell column="headshots" />
						<gui:patterncell column="hsPercentImage">{1}&nbsp;{2}%</gui:patterncell>
						<gui:simplecell column="damageGiven" />
					</gui:content>
				</gui:table>
				<hr>
			</logic:present>
			<logic:present name="mapStatsTable">
				<p><b>Map-Stats:</b></p>
				<gui:table name="mapStatsTable" scope="session"
					controllerLink="mapController.do">
					<gui:definition>
						<gui:column name="rang" width="60">
							Rang
						</gui:column>
						<gui:column name="name" width="275">
							Name
						</gui:column>
						<gui:column name="kills" width="90">
							Kills
						</gui:column>
						<gui:column name="headshots" width="90">
							Headshots
						</gui:column>
						<gui:column name="hsPercentImage" width="286">
							% Headshots
						</gui:column>
						<gui:column name="damageGiven" width="80">
							Total Damage
						</gui:column>
						<logic:notPresent name="mapFilter">
							<gui:function name="addMapFilter" link="filter.do"
								image="icons/function/table/ansehen.gif" alt="Filter hinzufügen"
								columnWidth="24" />
						</logic:notPresent>
						<logic:present name="mapFilter">
							<gui:function name="removeMapFilter" link="filter.do"
								image="icons/function/table/loeschen.gif" alt="Filter entfernen"
								columnWidth="24" />
						</logic:present>
					</gui:definition>
					<gui:content>
						<gui:simplecell column="rang" />
						<gui:simplecell column="name" />
						<gui:simplecell column="kills" />
						<gui:simplecell column="headshots" />
						<gui:patterncell column="hsPercentImage">{1}&nbsp;{2}%</gui:patterncell>
						<gui:simplecell column="damageGiven" />
					</gui:content>
				</gui:table>
				<hr>
			</logic:present>
			<logic:present name="weaponStatsTable">
				<p><b>Weapon-Stats:</b></p>
				<gui:table name="weaponStatsTable" scope="session"
					controllerLink="weaponController.do">
					<gui:definition>
						<gui:column name="rang" width="60">
							Rang
						</gui:column>
						<gui:column name="weapon" width="120">
							Waffe
						</gui:column>
						<gui:column name="kills" width="80">
							Kills
						</gui:column>
						<gui:column name="percentKillsImage" width="167">
							Percentage of Kills
						</gui:column>
						<gui:column name="percentKills" width="55">
							%
						</gui:column>
						<gui:column name="headshots" width="90">
							Headshots
						</gui:column>
						<gui:column name="percentHeadshotsImage" width="166">
							% Headshots
						</gui:column>
						<gui:column name="percentHeadshots" width="55">
							%
						</gui:column>
						<gui:column name="damageGiven" width="80">
							Total Damage
						</gui:column>
						<logic:notPresent name="weaponFilter">
							<gui:function name="addWeaponFilter" link="filter.do"
								image="icons/function/table/ansehen.gif" alt="Filter hinzufügen"
								columnWidth="24" />
						</logic:notPresent>
						<logic:present name="weaponFilter">
							<gui:function name="removeWeaponFilter" link="filter.do"
								image="icons/function/table/loeschen.gif" alt="Filter entfernen"
								columnWidth="24" />
						</logic:present>
					</gui:definition>
					<gui:content>
						<gui:simplecell column="rang" />
						<gui:patterncell column="weapon">{1}</gui:patterncell>
						<gui:simplecell column="kills" />
						<gui:simplecell column="percentKillsImage" />
						<gui:simplecell column="percentKills" />
						<gui:simplecell column="headshots" />
						<gui:simplecell column="percentHeadshotsImage" />
						<gui:patterncell column="percentHeadshots">{1}</gui:patterncell>
						<gui:simplecell column="damageGiven" />
					</gui:content>
				</gui:table>
				<hr>
			</logic:present>

			<jsp:include page="charts2include.jsp" flush="true" />

		</gui:workplace>
	</gui:contentarea>
</gui:page>
