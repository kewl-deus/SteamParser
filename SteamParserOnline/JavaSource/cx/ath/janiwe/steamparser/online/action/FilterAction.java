/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.action;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cx.ath.janiwe.steamparser.db.DBFilter;
import cx.ath.janiwe.steamparser.db.MapFilter;
import cx.ath.janiwe.steamparser.db.PlayerFilter;
import cx.ath.janiwe.steamparser.db.SessionFilter;
import cx.ath.janiwe.steamparser.db.WeaponFilter;
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

public class FilterAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        HttpSession session = request.getSession();

        // function-String darf nicht NULL sein!
        // TODO: function.equals("test") umdrehen --> "test".equals(function)
        // Achtung: durch "" + request... ist im Fall von null funtion = "null"
        // als String!
        String function = "" + request.getParameter("function");
        int node = Integer.parseInt(request.getParameter("node"));

        Collection<DBFilter> filters = (Collection<DBFilter>) session
                .getAttribute("filters");
        if (filters == null)
        {
            filters = new Vector<DBFilter>();
        }

        PlayerTableModel pModel = (PlayerTableModel) session
                .getAttribute(Tables.PLAYER_STATS_TABLE.getLabel());
        SessionTableModel sModel = (SessionTableModel) session
                .getAttribute(Tables.SESSION_STATS_TABLE.getLabel());
        MapTableModel mModel = (MapTableModel) session
                .getAttribute(Tables.MAP_STATS_TABLE.getLabel());
        WeaponTableModel wModel = (WeaponTableModel) session
                .getAttribute(Tables.WEAPON_STATS_TABLE.getLabel());

        for (Tables tab : Tables.values())
        {
            session.removeAttribute(tab.getLabel());
        }

        if (function.equals("addPlayerFilter"))
        {
            PlayerFilter f = pModel.getRow(node).getFilter();
            filters.remove(f);
            filters.add(f);
            session.setAttribute("playerFilter", f);
            populateSession(session, filters);
        }
        else if (function.equals("removePlayerFilter"))
        {
            PlayerFilter f = (PlayerFilter) session
                    .getAttribute("playerFilter");
            session.removeAttribute("playerFilter");
            filters.remove(f);
            populateSession(session, filters);
        }
        else if (function.equals("addSessionFilter"))
        {
            SessionFilter s = sModel.getRow(node).getFilter();
            filters.remove(s);
            filters.add(s);
            session.setAttribute("sessionFilter", s);
            populateSession(session, filters);
        }
        else if (function.equals("removeSessionFilter"))
        {
            SessionFilter s = (SessionFilter) session
                    .getAttribute("sessionFilter");
            session.removeAttribute("sessionFilter");
            filters.remove(s);
            populateSession(session, filters);
        }
        else if (function.equals("addMapFilter"))
        {
            MapFilter m = mModel.getRow(node).getFilter();
            filters.remove(m);
            filters.add(m);
            session.setAttribute("mapFilter", m);
            populateSession(session, filters);
        }
        else if (function.equals("removeMapFilter"))
        {
            MapFilter m = (MapFilter) session.getAttribute("mapFilter");
            session.removeAttribute("mapFilter");
            filters.remove(m);
            populateSession(session, filters);
        }
        else if (function.equals("addWeaponFilter"))
        {
            WeaponFilter w = wModel.getRow(node).getFilter();
            filters.remove(w);
            filters.add(w);
            session.setAttribute("weaponFilter", w);
            populateSession(session, filters);
        }
        else if (function.equals("removeWeaponFilter"))
        {
            WeaponFilter w = (WeaponFilter) session
                    .getAttribute("weaponFilter");
            session.removeAttribute("weaponFilter");
            filters.remove(w);
            populateSession(session, filters);
        }

        return mapping.findForward("stats");
    }

    public void populateSession(HttpSession session,
            Collection<DBFilter> filters)
    {
        DBFilter[] filterArray = filters.toArray(new DBFilter[filters.size()]);

        // Player-Stats
        List<PlayerStats> playerStats = QueryService
                .getPlayerStats(filterArray);
        if (playerStats.size() > 0)
        {
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

            session.setAttribute(
                    ChartProducers.PLAYER_KILL_SPREADING_PIE_PRODUCER.getId(),
                    killPieProducer);

            StackedPlayerStatsDatasetProducer stackedPlayerStatsProd = new StackedPlayerStatsDatasetProducer(
                    ChartProducers.STACKED_PLAYER_STATS_PRODUCER.getId(),
                    playerStats, 8);
            session.setAttribute(ChartProducers.STACKED_PLAYER_STATS_PRODUCER
                    .getId(), stackedPlayerStatsProd);
        }

        // Session-Stats
        List<GameStats> gameStats = QueryService.getGameStats(filterArray);
        if (gameStats.size() > 0)
        {
            SessionTableModel sModel = new SessionTableModel(gameStats);
            session.setAttribute(Tables.SESSION_STATS_TABLE.getLabel(), sModel);
        }

        // Map-Stats
        List<MapStats> mapStats = QueryService.getMapStats(filterArray);
        if (mapStats.size() > 0)
        {
            MapTableModel mModel = new MapTableModel(mapStats);
            session.setAttribute(Tables.MAP_STATS_TABLE.getLabel(), mModel);
        }

        // Weapon-Stats
        List<WeaponStats> weaponStats = QueryService
                .getWeaponStats(filterArray);
        if (weaponStats.size() > 0)
        {
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

            session.setAttribute(
                    ChartProducers.WEAPON_KILL_SPREADING_PIE_PRODUCER.getId(),
                    weaponPieProducer);
        }
        session.setAttribute("filters", filters);
    }
}
