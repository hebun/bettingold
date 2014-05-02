package betting;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import entities.EventResult;
import entities.EventTypeResult;
import entities.MarketBook;
import entities.MarketCatalogue;
import entities.MarketFilter;
import entities.PlaceExecutionReport;
import entities.PlaceInstruction;
import entities.PriceProjection;
import enums.MarketProjection;
import enums.MarketSort;
import enums.MatchProjection;
import enums.OrderProjection;

public abstract class ApiNgOperations {
	protected final String FILTER = "filter";
	protected final String LOCALE = "locale";
	protected final String SORT = "sort";
	protected final String MAX_RESULT = "maxResults";
	protected final String MARKET_IDS = "marketIds";
	protected final String MARKET_ID = "marketId";
	protected final String INSTRUCTIONS = "instructions";
	protected final String CUSTOMER_REF = "customerRef";
	protected final String locale = Locale.getDefault().toString();

	public abstract List<EventResult> listEvents(MarketFilter filter, String appKey, String ssoId)
			throws APINGException;

	public abstract List<EventTypeResult> listEventTypes(MarketFilter filter,
			String appKey, String ssoId) throws APINGException;

	public abstract List<MarketBook> listMarketBook(List<String> marketIds,
			PriceProjection priceProjection, OrderProjection orderProjection,
			MatchProjection matchProjection, String currencyCode,
			String appKey, String ssoId) throws APINGException;

	public abstract List<MarketCatalogue> listMarketCatalogue(
			MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId)
			throws APINGException;

	public abstract PlaceExecutionReport placeOrders(String marketId,
			List<PlaceInstruction> instructions, String customerRef,
			String appKey, String ssoId) throws APINGException;

	protected abstract String makeRequest(String operation,
			Map<String, Object> params, String appKey, String ssoToken)
			throws APINGException;

}
