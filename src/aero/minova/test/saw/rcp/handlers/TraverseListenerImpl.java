package aero.minova.test.saw.rcp.handlers;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;

public class TraverseListenerImpl implements TraverseListener {

	Logger logger;

	public TraverseListenerImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void keyTraversed(TraverseEvent e) {
		logger.info(
				"keyTraversed(detail=" + e.detail + ", stateMask=" + Integer.toHexString(e.stateMask) + ", keyCode= " + Integer.toHexString(e.keyCode) + ")");

//		if (!e.doit) return; // wir tun nichts, wenn ein anderer etwas getan hat

		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_NEXT:
			logger.info("SWT.TRAVERSE_ARROW_NEXT");
			break;
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			logger.info("SWT.TRAVERSE_ARROW_PREVIOUS");
			break;
		case SWT.TRAVERSE_ESCAPE:
			logger.info("SWT.TRAVERSE_ESCAPE");
			break;
		case SWT.TRAVERSE_MNEMONIC:
			logger.info("SWT.TRAVERSE_MNEMONIC");
			break;
		case SWT.TRAVERSE_NONE:
			logger.info("SWT.TRAVERSE_NONE");
			break;
		case SWT.TRAVERSE_PAGE_NEXT:
			logger.info("SWT.TRAVERSE_PAGE_NEXT");
			break;
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			logger.info("SWT.TRAVERSE_PAGE_PREVIOUS");
			break;
		case SWT.TRAVERSE_RETURN:
			logger.info("SWT.TRAVERSE_RETURN");
			break;
		case SWT.TRAVERSE_TAB_NEXT:
			logger.info("SWT.TRAVERSE_TAB_NEXT");
			break;
		case SWT.TRAVERSE_TAB_PREVIOUS:
			logger.info("SWT.TRAVERSE_TAB_PREVIOUS");
			break;
		default:
			logger.info("UNNOWN");
		}
	}

}
