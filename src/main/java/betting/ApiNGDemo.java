package betting;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import betting.ApiNgJsonRpcOperations;
import entities.EventResult;
import entities.MarketBook;
import entities.MarketCatalogue;
import entities.MarketFilter;
import entities.PriceProjection;
import entities.PriceSize;
import entities.Runner;
import enums.MarketBettingType;
import enums.MarketProjection;
import enums.MarketSort;
import enums.PriceData;
import util.Jdbc;
import util.JdbcLong;
import util.LogFormatter;

public class ApiNGDemo {
	// market book. get prices
	private static Properties prop = new Properties();
	private static String applicationKey;
	private static String sessionToken;
	private static boolean debug;
	public static Logger LOGGER = Logger.getLogger(ApiNGDemo.class.getName());
	private static boolean started = false;
	static boolean fetchBookProcess = false;
	public static ApiNgJsonRpcOperations jsonOperations;

	public static void Main() {
		LOGGER = Logger.getLogger(ApiNGDemo.class.getName());
		jsonOperations = ApiNgJsonRpcOperations.getInstance();

		applicationKey = "5DWDmqno4izTvPPf";
		sessionToken = "OAnYfk7NsbMTDrmK/PBeP54AZJf8aAfqGwhPFoZp9kQ=";
		started = true;

		try {
			InputStream in = ApiNGDemo.class
					.getResourceAsStream("/apingdemo.properties");
			prop.load(in);
			in.close();
			LOGGER.setUseParentHandlers(false);
			debug = new Boolean(prop.getProperty("DEBUG"));

			ConsoleHandler consoleHandler = new ConsoleHandler() {
				{
					setOutputStream(System.out);
				}
			};

			consoleHandler.setFormatter(new LogFormatter());

			if (LOGGER.getHandlers().length == 0) {
				System.out.println("lengt 0");
				LOGGER.addHandler(consoleHandler);
			} else {
				System.out.println(LOGGER.getHandlers().length
						+ " adet handlers");
			}

		} catch (IOException e) {
			System.out.println("Error loading the properties file: "
					+ e.toString());
		}

	}

	/**
	 * 
	 */
	public static void fetchMarketBook() {
		if (false == started)
			Main();

		if (fetchBookProcess) {
			LOGGER.info("fetchbook in progress");
			return;
		}
		fetchBookProcess = true;
		LOGGER.info("fetchbook started");
		Tempobet.getWeekend();
		fetchEvents();
		fetchMarkets();

		// System.exit(0);
		JdbcLong.say = 0;

		try {
			MarketFilter filter = new MarketFilter();
			Set<String> eventTypeIds = new HashSet<String>();
			eventTypeIds.add("1");
			filter.setEventTypeIds(eventTypeIds);

			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.EVENT);

			JdbcLong.start("fetchMarketbokk");

			// marketIds.add("1.113291175");
			PriceProjection priceProjection = new PriceProjection();
			HashSet<PriceData> hashSet = new HashSet<PriceData>();
			hashSet.add(PriceData.EX_BEST_OFFERS);
			priceProjection.setPriceData(hashSet);

			List<Map<String, String>> list = JdbcLong
					.select("select m.externId as externId,m.eventId as eventId from `market` as m inner join `match` as ma on ma.externId=m.eventId"
							+ " where ma.tarih>now() and m.name like  '%Match Odds%'");

			List<String> marketIds = new ArrayList<String>();
			Hashtable<String, String> marketEvent = new Hashtable<String, String>();
			// marketIds.add("1.113308255");
			LOGGER.info("started to fetch marketbooks for " + list.size()
					+ " marketcatalogues");
			int k = 0;
			for (Map<String, String> h : list) {

				marketIds.add(h.get("externId"));
				marketEvent.put(h.get("externId"), h.get("eventId"));
				if (k++ % 20 == 0) {
					List<MarketBook> result = jsonOperations.listMarketBook(
							marketIds, priceProjection, null, null, null,
							applicationKey, sessionToken);

					for (MarketBook book : result) {
						int j = 0;
						String update = "update `match` set ";
						int topOran = 0;
						List<Runner> runners = book.getRunners();
						for (Runner r : runners) {

							if (r.getEx().getAvailableToBack().size() <= 0)
								continue;

							PriceSize ps = r.getEx().getAvailableToBack()
									.get(0);

							if (k == 4)
								System.out.println("r.tosting:" + r.toString());
							topOran += (ps.getPrice() * 100);
							if (j == 0) {

								update += " ht='" + (ps.getPrice() * 100)
										+ "',";
							}
							if (j == 1) {
								update += " at='" + (ps.getPrice() * 100)
										+ "',";
							}
							if (j == 2) {
								update += " draw='" + (ps.getPrice() * 100)
										+ "' where externId='"
										+ marketEvent.get(book.getMarketId())
										+ "'";
								if (topOran > 400) {
									JdbcLong.query(update);
								}
							}
							j++;
						}
						j = 0;
						update = "update `match` set ";
						for (Runner runner : runners) {
							List<PriceSize> availableToLay = runner.getEx()
									.getAvailableToLay();
							if (availableToLay.size() == 0)
								continue;
							PriceSize ps = availableToLay.get(0);

							topOran += (ps.getPrice() * 100);
							if (j == 0) {

								update += " deht='" + (ps.getPrice() * 100)
										+ "',";
							}
							if (j == 1) {
								update += " deat='" + (ps.getPrice() * 100)
										+ "',";
							}
							if (j == 2) {
								update += " dedraw='" + (ps.getPrice() * 100)
										+ "' where externId='"
										+ marketEvent.get(book.getMarketId())
										+ "'";
								if (topOran > 400) {
									JdbcLong.query(update);
								}
							}
							j++;
							// System.out.println(j + ":" +
							// priceSize.getPrice());
						}

					}
					System.out.print("*");
					marketIds.clear();
				}
			}

		} catch (APINGException e) {
			System.out.println("error blabla :");
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			JdbcLong.close("fetchmarketbook");
		}
		fetchBookProcess = false;
	}

	public static void fetchEvents() {
		if (false == started)
			Main();

		if (!checkCall("EVENTS")) {
			LOGGER.info("not fetching EVENTS");
			return;
		}
		ApiNGDemo.LOGGER.info("fetchin events");
		MarketFilter filter = new MarketFilter();
		Set<String> eventTypeIds = new HashSet<String>();
		eventTypeIds.add("1");
		filter.setEventTypeIds(eventTypeIds);
		try {
			List<EventResult> eventResults = jsonOperations.listEvents(filter,
					applicationKey, sessionToken);

			String minsert = EventResult.startInsert();
			int fails = 0;
			for (EventResult eventResult : eventResults) {
				try {
					minsert += eventResult.getInsert() + ",";

				} catch (Exception e) {
					fails++;
					// LOGGER.warning(e.getMessage());

				}
			}
			minsert = minsert.substring(0, minsert.length() - 1);

			JdbcLong.start("fetcEvents");
			LOGGER.info("inserting " + eventResults.size()
					+ " matchs total and " + fails + " fails for betfair");
			JdbcLong.query(minsert);
			JdbcLong.query("insert into betfairupdate(type,tarih) values('EVENTS',NOW())");
			JdbcLong.close("fetchevents");
		} catch (APINGException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
	}

	public static boolean checkCall(String string) {
		List<Map<String, String>> dataTable = JdbcLong
				.select("select * from betfairupdate where type='" + string
						+ "' and tarih >= DATE_SUB(NOW(),INTERVAL 1 HOUR)");

		return dataTable.size() == 0;

	}

	/**
	 * 
	 */
	public static void fetchMarkets() {
		if (false == started)
			Main();

		if (!checkCall("MARKETS")) {
			LOGGER.info("not fetching MARKETS");
			return;
		}
		try {
			MarketFilter filter = new MarketFilter();
			Set<String> eventTypeIds = new HashSet<String>();

			List<Map<String, String>> events = Jdbc
					.select("select * from `match` where "
							+ "((`match`.`awayTeam` <> 'xxx') "
							+ "and (`match`.`tarih` > now()) and "
							+ " (`match`.`siteId` = 2))");
			// LOGGER.info(String.valueOf(events.size()));
			// Set<String> eventIds = new HashSet<String>();
			// eventIds.add("27170459");
			eventTypeIds.add("1");
			filter.setEventTypeIds(eventTypeIds);
			Set<MarketBettingType> bettingTypes = new HashSet<MarketBettingType>();
			bettingTypes.add(MarketBettingType.ODDS);

			filter.setMarketBettingTypes(bettingTypes);
			// filter.setEventIds(eventIds);

			JdbcLong.start("fetchMarket");
			Set<String> eventIds = new HashSet<String>();
			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.EVENT);
			int k = 0;
			LOGGER.info("started to fetch markets for " + events.size()
					+ " matches");
			for (Map<String, String> row : events) {

				eventIds.add(row.get("externId"));
				if (k++ % 100 == 0) {

					filter.setEventIds(eventIds);
					List<MarketCatalogue> result = jsonOperations
							.listMarketCatalogue(filter, marketProjection,
									MarketSort.FIRST_TO_START, "1000",
									applicationKey, sessionToken);
					String minsert = MarketCatalogue.startInsert();
					for (MarketCatalogue catalogue : result) {
						minsert += catalogue.getInsert() + ",";
					}
					minsert = minsert.substring(0, minsert.length() - 1);
					JdbcLong.query(minsert);
					System.out.print("#");
					eventIds.clear();
				}
			}
		} catch (Exception e) {
			LOGGER.info("eror");
			e.printStackTrace();
		} catch (APINGException e) {
			e.printStackTrace();
		} finally {
			JdbcLong.query("insert into betfairupdate(type,tarih) values('MARKETS',NOW())");
			JdbcLong.close("fetchmarkets");

		}
	}

	public static Properties getProp() {
		return prop;
	}

	public static boolean isDebug() {
		return debug;
	}
}
