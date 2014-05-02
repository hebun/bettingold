package betting;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "app", eager = true)
@ApplicationScoped
public class App implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7550798084561000895L;
	private static Logger log = ApiNGDemo.LOGGER;
	int callerCount = 0;
	public App() {
		log.info("app created");
	}

	public void updateBetfair() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				ApiNGDemo.fetchMarketBook();

			}
		});

		thread.start();

		System.out.println("cc:" + callerCount++);
	}

	private void out(String in) {
		System.out.println(in);
	}
}
