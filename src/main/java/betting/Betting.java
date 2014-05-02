package betting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import model.DualMatch;
import model.Match;
import util.Jdbc;
import util.JdbcLong;
import util.LevenshteinDistance;

@ViewScoped
@ManagedBean
public class Betting implements Serializable {
	// multi threading

	private static final Logger log = ApiNGDemo.LOGGER;

	private static final long serialVersionUID = 1120356482214096067L;

	private List<Map<String, String>> matches;
	private List<Map<String, String>> tempoMatches;
	private String tableHeader;
	private int site;
	@ManagedProperty(value = "#{app}")
	App app;
	public Map parameters;
	private List<DualMatch> dualMatchs;

	public Betting() {

		setDbparameters();

		try {
			site = Integer.parseInt(getGET("site"));
		} catch (NumberFormatException e) {
			site = 3;
		}
		System.out.println("creating Betting");
		refresh();
	}

	public Betting(String testing) {
		site = 3;

	}

	private void setDbparameters() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext ctx = facesContext.getExternalContext();
		parameters = ctx.getInitParameterMap();

		Jdbc.DB_URL = parameters.get("jdbcurl").toString();

		Jdbc.USER = parameters.get("jdbcuser").toString();

		Jdbc.PASS = parameters.get("jdbcpassword").toString();
		JdbcLong.DB_URL = parameters.get("jdbcurl").toString();

		JdbcLong.USER = parameters.get("jdbcuser").toString();

		JdbcLong.PASS = parameters.get("jdbcpassword").toString();
	}

	public void refresh() {
		// matches = Jdbc
		// .select("select homeTeam,awayTeam,ht,draw,at,tarih from matchView where siteId=2 order by tarih");
		// tempoMatches = Jdbc
		// .select("select homeTeam,awayTeam,ht,draw,at,tarih from matchView where siteId=3 order by tarih");

		refreshDualMatchs();
		Jdbc.close("refresh");
	}

	private String getGET(String param) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		String string = context.getRequestParameterMap().get(param);
		return string;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	private void refreshDualMatchs() {
		int countLoop = 0;
		long start = System.currentTimeMillis();
		List<Match> list = null;

		list = Jdbc.select(
				"select * from duplicateAll where siteId=2 or siteId="
						+ this.site, Match.class);
		log.info("gettng dualmatchs for " + list.size());

		dualMatchs = new ArrayList<DualMatch>();
		for (final Match m : list) {

			if (m.getSiteId() == null || m.getSiteId() != 2) {

				continue;
			}
			if (m.otherId != 0) {

				continue;

			}

			for (final Match ma : list) {
				countLoop++;
				if (ma.otherId == null) {
					ma.otherId = 0;
					System.out.println("nullllll" + ma.getHomeTeam());
					System.out.print("+");
					continue;
				}
				if (ma != m && ma.otherId == 0) {

					if (LevenshteinDistance.similarity(m.getAwayTeam(),
							ma.getAwayTeam()) > 0.5
							&& LevenshteinDistance.similarity(m.getHomeTeam(),
									ma.getHomeTeam()) > 0.5) {
						m.otherId = ma.getId();
						ma.otherId = m.getId();

						try {
							boolean isHt = m.getDeht() < ma.getHt();
							boolean isAt = m.getDeat() < ma.getAt();
							boolean isDraw = m.getDedraw() < ma.getDraw();
							if (!(isHt || isAt || isDraw)) {
								continue;
							}
						} catch (Exception e) {
							continue;
						}

						DualMatch dualMatch = new DualMatch() {
							{
								homeTeam = m.getHomeTeam();
								awayTeam = ma.getAwayTeam();
								ht2 = m.getHt();
								ht3 = ma.getHt();
								at2 = m.getAt();
								at3 = ma.getAt();
								draw2 = m.getDraw();
								draw3 = ma.getDraw();
								tarih = m.getTarih();
								deht = m.getDeht();
								deat = m.getDeat();
								dedraw = m.getDedraw();

							}

						};

						// System.out.println(dualMatch.toString());
						dualMatchs.add(dualMatch);

					}
				}
			}

		}

		log.info("dualMatchs size:" + dualMatchs.size() + " time:"
				+ (System.currentTimeMillis() - start)+" counloop:"+countLoop);
		
	}

	public List<DualMatch> getDualMatchs() {
		return dualMatchs;
	}

	public void setDualMatchs(List<DualMatch> dualMatchs) {
		this.dualMatchs = dualMatchs;
	}

	public void fetchMarketBook(AjaxBehaviorEvent ajaxBehaviorEvent) {
		refresh();
		app.updateBetfair();

	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public List<Map<String, String>> getMatches() {

		return matches;
	}

	public void setMatches(List<Map<String, String>> matches) {
		this.matches = matches;
	}

	public List<Map<String, String>> getTempoMatches() {
		return tempoMatches;
	}

	public void setTempoMatches(List<Map<String, String>> tempoMatches) {
		this.tempoMatches = tempoMatches;
	}

	public String getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}
}
