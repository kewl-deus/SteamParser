<%@taglib uri="gui-taglib.jar" prefix="gui"%>
<%@taglib uri="struts.jar" prefix="struts"%>
<%@taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/cewolf.tld" prefix="cewolf"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="de.dengot.steamparser.online.chart.ChartProducers"%>



<logic:present
	name="<%=ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER.getId() %>">
	<cewolf:chart id="playerKillSpreadingPie"
		title="Player's Kill Spreading (in %)" type="pie3d" showlegend="true">

		<cewolf:colorpaint color="#E9E9E9" />

		<cewolf:data>
			<cewolf:producer
				id="<%=ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER.getId() %>" />
		</cewolf:data>

	</cewolf:chart>


	<p>
	<div align="center">
		<cewolf:img chartid="playerKillSpreadingPie"
			renderer="cewolf" width="770" height="550">
			<cewolf:map
				tooltipgeneratorid="<%=ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER.getId() %>">
			</cewolf:map>
		</cewolf:img>
	</div>
	</p>
</logic:present>

<logic:present
	name="<%=ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId() %>">
	<cewolf:chart id="weaponKillSpreadingPie"
		title="Weapon's Kill Spreading (in %)" type="pie3d" showlegend="true">

		<cewolf:colorpaint color="#E9E9E9" />

		<cewolf:data>
			<cewolf:producer
				id="<%=ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId() %>" />
		</cewolf:data>

	</cewolf:chart>


	<p>
	<div align="center">
		<cewolf:img chartid="weaponKillSpreadingPie"
			renderer="cewolf" width="770" height="550">
			<cewolf:map
				tooltipgeneratorid="<%=ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId() %>">
			</cewolf:map>
		</cewolf:img>
	</div>
	</p>
</logic:present>


<logic:present name="<%=ChartProducers.STACKED_PLAYER_STATS_PRODUCER.getId() %>">
	<cewolf:chart id="stackedPlayerStatsChart" title="Kills and Headshots"
		type="stackedverticalbar3d" showlegend="true" xaxislabel="Players"
		yaxislabel="Kills">

		<cewolf:colorpaint color="#E9E9E9" />

		<cewolf:data>
			<cewolf:producer id="<%=ChartProducers.STACKED_PLAYER_STATS_PRODUCER.getId() %>" />
		</cewolf:data>

	</cewolf:chart>


	<p>
	<div align="center">
		<cewolf:img chartid="stackedPlayerStatsChart"
			renderer="cewolf" width="770" height="600">
			<cewolf:map
				tooltipgeneratorid="<%=ChartProducers.STACKED_PLAYER_STATS_PRODUCER.getId() %>">
			</cewolf:map>
		</cewolf:img>
	</div>
	</p>
</logic:present>
