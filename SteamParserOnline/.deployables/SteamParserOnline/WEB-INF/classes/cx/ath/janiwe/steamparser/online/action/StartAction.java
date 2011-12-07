/*
 * Created on 13.09.2005
 */
package cx.ath.janiwe.steamparser.online.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cx.ath.janiwe.steamparser.online.constants.Tables;
import cx.ath.janiwe.steamparser.online.table.MapTableModel;
import cx.ath.janiwe.steamparser.online.table.PlayerTableModel;
import cx.ath.janiwe.steamparser.online.table.SessionTableModel;
import cx.ath.janiwe.steamparser.online.table.WeaponTableModel;
import cx.ath.janiwe.steamparser.stats.GameStats;
import cx.ath.janiwe.steamparser.stats.MapStats;
import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.steamparser.stats.WeaponStats;
import de.dengot.steamparser.logic.QueryService;
import de.dengot.steamparser.online.chart.ChartConstants;
import de.dengot.steamparser.online.chart.ChartProducers;
import de.dengot.steamparser.online.chart.KillPieChartDatasetProducer;
import de.dengot.steamparser.online.chart.StackedPlayerStatsDatasetProducer;

public class StartAction extends Action
{

    // private int counter = 0;

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        HttpSession session = request.getSession();

        // System.out.println(++counter);
        // Player-Stats
        List<PlayerStats> playerStats = QueryService.getPlayerStats();
        PlayerTableModel pModel = new PlayerTableModel(playerStats);
        session.setAttribute(Tables.PLAYER_STATS_TABLE.getLabel(), pModel);

        // PlayerKillPieChartDatasetProducer killPieProducer = new
        // PlayerKillPieChartDatasetProducer(
        // ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER
        // .getId(), playerStats);

        KillPieChartDatasetProducer<PlayerStats> killPieProducer = new KillPieChartDatasetProducer<PlayerStats>(
                ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER.getId(),
                playerStats,
                ChartConstants.PLAYER_KILLS_CONSOLIDATE_MIN_PERCENTAGE);

        session.setAttribute(ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER
                .getId(), killPieProducer);

        StackedPlayerStatsDatasetProducer stackedPlayerStatsProd = new StackedPlayerStatsDatasetProducer(
                ChartProducers.STACKED_PLAYER_STATS_PRODUCER.getId(),
                playerStats, 8);
        session.setAttribute(ChartProducers.STACKED_PLAYER_STATS_PRODUCER
                .getId(), stackedPlayerStatsProd);

        // Session-Stats
        List<GameStats> gameStats = QueryService.getGameStats();
        SessionTableModel sModel = new SessionTableModel(gameStats);
        session.setAttribute(Tables.SESSION_STATS_TABLE.getLabel(), sModel);

        // Map-Stats
        List<MapStats> mapStats = QueryService.getMapStats();
        MapTableModel mModel = new MapTableModel(mapStats);
        session.setAttribute(Tables.MAP_STATS_TABLE.getLabel(), mModel);

        // Weapon-Stats
        List<WeaponStats> weaponStats = QueryService.getWeaponStats();
        WeaponTableModel wModel = new WeaponTableModel(weaponStats);
        session.setAttribute(Tables.WEAPON_STATS_TABLE.getLabel(), wModel);

        // WeaponKillPieChartDatasetProducer weaponPieProducer = new
        // WeaponKillPieChartDatasetProducer(
        // ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId(),
        // weaponStats);

        KillPieChartDatasetProducer<WeaponStats> weaponPieProducer = new KillPieChartDatasetProducer<WeaponStats>(
                ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId(),
                weaponStats,
                ChartConstants.WEAPON_KILLS_CONSOLIDATE_MIN_PERCENTAGE);

        session.setAttribute(ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER
                .getId(), weaponPieProducer);
        // return mapping.findForward("test");
        return mapping.findForward("stats");

    }

}
