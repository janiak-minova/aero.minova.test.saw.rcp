
package aero.minova.test.saw.rcp.parts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class BrowserPart {
	private Browser browser;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	public BrowserPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.BORDER);

		URL url;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL("platform:/plugin/aero.minova.test.saw.rcp/svg/friedhof-urnenfeld.svg");
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		browser.setText(sb.toString());
//		browser.setUrl("file:///Users/saak/Desktop/friedhof-urnenfeld.svg");
		browser.addLocationListener(new LocationListener() {

			@Override
			public void changing(LocationEvent event) {
				eventBroker.send(StatusBar.STATUSBAR, "Location changed to " + event.location);
				event.doit = false;
			}

			@Override
			public void changed(LocationEvent event) {
				eventBroker.send(StatusBar.STATUSBAR, "Location changed to " + event.location);
			}
		});
	}

}