package aero.minova.test.saw.rcp.parts;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

public class ContactRezieListener implements ControlListener {

	private SashForm sashForm;

	public ContactRezieListener(SashForm sashForm) {
		this.sashForm = sashForm;
	}

	@Override
	public void controlMoved(ControlEvent e) {}

	@Override
	public void controlResized(ControlEvent e) {
		System.out.println(e + "" + sashForm.getWeights().length + "[" //
				+ sashForm.getWeights()[0] + "," //
				+ sashForm.getWeights()[1] + "," //
				+ sashForm.getWeights()[2] + "]" //
				+ ", width = " + sashForm.getSashWidth() + "/" + sashForm.getSize().x);
//		sashForm.setWeights(new int[] { 200, 200, 0 });
	}

}
