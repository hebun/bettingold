package entities;

import java.util.Date;

public class Event {

	private String id;
	private String name;
	private String countryCode;
	private String timezone;
	private String venue;
	private Date openDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String toString() {
		return "{" + "" + "id=" + getId() + "," + "name=" + getName() + ","
				+ "countryCode=" + getCountryCode() + "," + "timezone="
				+ getTimezone() + "," + "venue=" + getVenue() + ","
				+ "openDate=" + getOpenDate() + "," + "}";
	}

	public String getHome() {
		try {
			return getName().split(" v ")[0];
		} catch (Exception e) {
			return "xxx";
		}
	}

	public String getAway() throws Exception {
		try {
			return getName().split(" v ")[1];
		} catch (Exception e) {
			throw e;

		}
	}

	public String getInsert() throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(getOpenDate());
		return "insert ignore into `match`(siteId,externId,homeTeam,awayTeam,tarih) values(2,'"
				+ getId()
				+ "','"
				+ getHome()
				+ "','"
				+ getAway()
				+ "','"
				+ time + "')";
	}
}
