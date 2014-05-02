package entities;

public class EventResult {

	private Event event;
	private Integer marketCount;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Integer getMarketCount() {
		return marketCount;
	}

	public void setMarketCount(Integer marketCount) {
		this.marketCount = marketCount;
	}

	public String getInsert() throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(event.getOpenDate());


		return "(2,'" + event.getId() + "','"
				+ event.getHome().replace('\'', ' ') + "','"
				+ event.getAway().replace('\'', ' ') + "','" + time + "',"
				+ marketCount + ")";
	}

	public static String startInsert() {
		String ret = "insert ignore into `match`(siteId,externId,homeTeam,awayTeam,tarih,marketCount) values ";
		return ret;
	}
}
