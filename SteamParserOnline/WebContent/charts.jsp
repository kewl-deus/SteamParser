<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/cewolf.tld" prefix="cewolf"%>
<%@ page import="de.dengot.steamparser.online.chart.ChartProducers"%>



<gui:page title="CS-S-Charts">
	<gui:header headline="x.gif" logoLink="/SteamParserOnline"
		logoLinkAlt="Startseite" logoLinkTarget="_blank">
	</gui:header>

	<gui:contentarea>
		<gui:workplace>

			<gui:form action="/chart.do" styleName="typeSelectionForm">
				<table>
					<tr>
						<td><gui:label properties="ordinalOfTypeToShow">StatsType</gui:label>
						</td>
						<td><gui:dropdownlist property="ordinalOfTypeToShow" width="200">
							<gui:options collection="statsTypeList" property="ordinal"
								labelProperty="label" />
						</gui:dropdownlist></td>
						<td><gui:button submit="applyType">Apply</gui:button></td>
					</tr>
				</table>

			</gui:form>


			<jsp:useBean id="chartBean"
				type="de.dengot.steamparser.online.chart.ChartBean" scope="session" />

			<cewolf:chart id="playerSessionStatsChart"
				title="<%= chartBean.getChartTitle() %>" type="area"
				showlegend="true" xaxislabel="<%= chartBean.getChartXAxisLabel() %>"
				yaxislabel="<%= chartBean.getChartYAxisLabel() %>">

				<cewolf:colorpaint color="#E9E9E9" />

				<cewolf:chartpostprocessor id="playerSessionStatsPostprocessor" />

				<cewolf:data>
					<cewolf:producer id="playerSessionStatsProducer" />
				</cewolf:data>

			</cewolf:chart>


			<p>
			<div align="center"><cewolf:img chartid="playerSessionStatsChart"
				renderer="cewolf" width="1000" height="700">
				<cewolf:map tooltipgeneratorid="playerSessionStatsProducer"></cewolf:map>
			</cewolf:img></div>
			</p>




			<cewolf:chart id="playerSessionStatsChart"
				title="<%= chartBean.getChartTitle() %>" type="horizontalbar3d"
				showlegend="true" xaxislabel="<%= chartBean.getChartXAxisLabel() %>"
				yaxislabel="<%= chartBean.getChartYAxisLabel() %>">

				<cewolf:colorpaint color="#E9E9E9" />

				<cewolf:data>
					<cewolf:producer id="playerSessionStatsProducer" />
				</cewolf:data>

			</cewolf:chart>


			<p>
			<div align="center"><cewolf:img chartid="playerSessionStatsChart"
				renderer="cewolf" width="1000" height="700">
			</cewolf:img></div>
			</p>


		</gui:workplace>
	</gui:contentarea>
</gui:page>
