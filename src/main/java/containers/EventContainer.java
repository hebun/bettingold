package containers;

import java.util.List;

import entities.EventResult;

public class EventContainer extends Container {
	private List<EventResult> result;

	public List<EventResult> getResult() {
		return result;
	}

	public void setResult(List<EventResult> result) {
		this.result = result;
	}

}
