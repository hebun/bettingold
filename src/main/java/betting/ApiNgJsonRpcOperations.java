package betting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import containers.EventContainer;
import containers.EventTypeResultContainer;
import containers.ListMarketBooksContainer;
import containers.ListMarketCatalogueContainer;
import containers.PlaceOrdersContainer;
import entities.EventResult;
import entities.EventTypeResult;
import entities.MarketBook;
import entities.MarketCatalogue;
import entities.MarketFilter;
import entities.PlaceExecutionReport;
import entities.PlaceInstruction;
import entities.PriceProjection;
import enums.ApiNgOperation;
import enums.MarketProjection;
import enums.MarketSort;
import enums.MatchProjection; 
import enums.OrderProjection;
import util.HttpUtil;
import util.JsonConverter;
import util.JsonrpcRequest;

public class ApiNgJsonRpcOperations extends ApiNgOperations {

	private static ApiNgJsonRpcOperations instance = null;
	private static FileHandler fileHTML;

	private ApiNgJsonRpcOperations() {
	}

	// assumes the current class is called logger
	private final static Logger LOGGER = Logger.getLogger(ApiNGDemo.class
			.getName());

	public static ApiNgJsonRpcOperations getInstance() {
		if (instance == null) {
			instance = new ApiNgJsonRpcOperations();
		}


		return instance;
	}

	public List<EventTypeResult> listEventTypes(MarketFilter filter,
			String appKey, String ssoId) throws APINGException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(
				ApiNgOperation.LISTEVENTTYPES.getOperationName(), params,
				appKey, ssoId);

		EventTypeResultContainer container = JsonConverter.convertFromJson(
				result, EventTypeResultContainer.class);
		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	public List<EventResult> listEvents(MarketFilter filter, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, new MarketFilter());
		params.put(LOCALE, locale);
		ApiNGDemo.LOGGER.info("making request");
		String result = getInstance().makeRequest(
				ApiNgOperation.LISTEVENTS.getOperationName(), params, appKey,
				ssoId);
		ApiNGDemo.LOGGER.info("made request");
		try {

			EventContainer container = JsonConverter.convertFromJson(result,
					EventContainer.class);

			return container.getResult();

		} catch (SecurityException e) {
			LOGGER.severe("logger:" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {

		}

	}

	public List<MarketBook> listMarketBook(List<String> marketIds,
			PriceProjection priceProjection, OrderProjection orderProjection,
			MatchProjection matchProjection, String currencyCode,
			String appKey, String ssoId) throws APINGException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, marketIds);
		params.put("priceProjection", priceProjection);
		String result = getInstance().makeRequest(
				ApiNgOperation.LISTMARKETBOOK.getOperationName(), params,
				appKey, ssoId);
		if (ApiNGDemo.isDebug())
			LOGGER.info("\nResponsex: " + result);

		ListMarketBooksContainer container = JsonConverter.convertFromJson(
				result, ListMarketBooksContainer.class);
		// System.out.println(container);
		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter,
			Set<MarketProjection> marketProjection, MarketSort sort,
			String maxResult, String appKey, String ssoId)
			throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put("marketProjection", marketProjection);
		String result = getInstance().makeRequest(
				ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params,
				appKey, ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		ListMarketCatalogueContainer container = JsonConverter.convertFromJson(
				result, ListMarketCatalogueContainer.class);

		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	public PlaceExecutionReport placeOrders(String marketId,
			List<PlaceInstruction> instructions, String customerRef,
			String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketId);
		params.put(INSTRUCTIONS, instructions);
		params.put(CUSTOMER_REF, customerRef);
		String result = getInstance().makeRequest(
				ApiNgOperation.PLACORDERS.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		PlaceOrdersContainer container = JsonConverter.convertFromJson(result,
				PlaceOrdersContainer.class);

		if (container.getError() != null)
			throw container.getError().getData().getAPINGException();

		return container.getResult();

	}

	protected String makeRequest(String operation, Map<String, Object> params,
			String appKey, String ssoToken) {
		String requestString;
		// Handling the JSON-RPC request
		JsonrpcRequest request = new JsonrpcRequest();
		request.setId("1");
		request.setMethod(ApiNGDemo.getProp().getProperty("SPORTS_APING_V1_0")
				+ operation);
		request.setParams(params);

		requestString = JsonConverter.convertToJson(request);
		if (ApiNGDemo.isDebug())
			System.out.println("\nRequest: " + requestString);

		// We need to pass the "sendPostRequest" method a string in util format:
		// requestString
		HttpUtil requester = new HttpUtil();
		return requester.sendPostRequestJsonRpc(requestString, operation,
				appKey, ssoToken);

	}

}
