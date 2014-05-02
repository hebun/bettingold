package model;

import java.io.Serializable;

public class DualMatch implements Serializable {

	public String homeTeam, awayTeam;
	public Integer ht2, draw2, at2, ht3, draw3, at3, deht, deat, dedraw;

	public Integer getDeht() {
		return deht;
	}

	public void setDeht(Integer deht) {
		this.deht = deht;
	}

	public Integer getDeat() {
		return deat;
	}

	public void setDeat(Integer deat) {
		this.deat = deat;
	}

	public Integer getDedraw() {
		return dedraw;
	}

	public void setDedraw(Integer dedraw) {
		this.dedraw = dedraw;
	}

	public String tarih;

	public DualMatch() {

	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Integer getHt2() {
		return ht2;
	}

	public void setHt2(Integer ht2) {
		this.ht2 = ht2;
	}

	public Integer getDraw2() {
		return draw2;
	}

	public void setDraw2(Integer draw2) {
		this.draw2 = draw2;
	}

	public Integer getAt2() {
		return at2;
	}

	public void setAt2(Integer at2) {
		this.at2 = at2;
	}

	public Integer getHt3() {
		return ht3;
	}

	public void setHt3(Integer ht3) {
		this.ht3 = ht3;
	}

	public Integer getDraw3() {
		return draw3;
	}

	public void setDraw3(Integer draw3) {
		this.draw3 = draw3;
	}

	public Integer getAt3() {
		return at3;
	}

	public void setAt3(Integer at3) {
		this.at3 = at3;
	}

	public String getTarih() {
		return tarih;
	}

	public void setTarih(String tarih) {
		this.tarih = tarih;
	}

	public String toString() {

		return homeTeam + " vs " + awayTeam + " ht3:" + ht3 + " deht:" + deht
				+ " at3:" + at3 + " deht:" + deat + " draw3:" + draw3
				+ " dedraw:" + dedraw;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 6954904804057438874L;
}
