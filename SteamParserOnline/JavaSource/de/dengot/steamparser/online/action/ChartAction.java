package de.dengot.steamparser.online.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.dengot.steamparser.logic.QueryService;
import de.dengot.steamparser.model.QueryablePlayer;
import de.dengot.steamparser.model.StatsType;
import de.dengot.steamparser.online.chart.AreaChartPostProcessor;
import de.dengot.steamparser.online.chart.ChartBean;
import de.dengot.steamparser.online.chart.PlayerStatsDatasetProducer;
import de.dengot.steamparser.online.forms.StatsTypeSelectionForm;

public class ChartAction extends Action
{
	
	public ChartAction()
	{
		super();
	}
	
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		
		// populate StatsType-Dropdownliste in Session
		List<StatsType> typeList = Arrays.asList(StatsType.values());
		Collections.sort(typeList, new Comparator<StatsType>()
				{
			
			public int compare(StatsType st1, StatsType st2)
			{
				return st1.getLabel().compareTo(st2.getLabel());
			}
			
				});
		session.setAttribute("statsTypeList", typeList);
		
		// get the Form of ChartsPage
		StatsTypeSelectionForm typeSelectionForm = (StatsTypeSelectionForm) form;
		
		List<QueryablePlayer> playerList = QueryService
		.getPlayerSessionStats();
		
		// String chosenFunction = request.getParameter("harpoon.submit");
		
		// Content Type of Stats
		StatsType typeToShow = typeSelectionForm.getTypeToShow();
		
		// DataProducer
		final String fragsProducerId = "playerSessionStatsProducer";
		PlayerStatsDatasetProducer fragsProducer = new PlayerStatsDatasetProducer(
				fragsProducerId, playerList, typeToShow);
		session.setAttribute(fragsProducerId, fragsProducer);
		
		// ChartInfos
		ChartBean statsChartBean = new ChartBean(fragsProducer,
				"Sessions Overview", "Session", typeToShow.getLabel());
		session.setAttribute("chartBean", statsChartBean);
		
		// Postprocessor
		final String fragsChartPostprocessorId = "playerSessionStatsPostprocessor";
		AreaChartPostProcessor fragsChartPostprocessor = new AreaChartPostProcessor();
		session.setAttribute(fragsChartPostprocessorId,
				fragsChartPostprocessor);
		
		
		return mapping.findForward("charts");
	}
}
