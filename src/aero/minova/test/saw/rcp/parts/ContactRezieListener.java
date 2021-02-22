package aero.minova.test.saw.rcp.parts;

import java.util.Arrays;

import javax.inject.Inject;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Shell;

public class ContactRezieListener implements ControlListener {

	private SashForm sashForm;
	private int[] weights = new int[] {1, 1, 2};
	private static final int MIN_WIDTH_GROUPLIST = 300;
	private static final int MIN_WIDTH_CONTACTLIST = 300;
	private static final int MIN_WIDTH_CONTACTDETAIL = 50;

	public ContactRezieListener(SashForm sashForm) {
		this.sashForm = sashForm;
	}

	@Override
	public void controlMoved(ControlEvent e) {}

	@Override
	public void controlResized(ControlEvent e) {
//		System.out.println(e + "" + sashForm.getWeights().length + "[" //
//				+ sashForm.getWeights()[0] + "," //
//				+ sashForm.getWeights()[1] + "," //
//				+ sashForm.getWeights()[2] + "]" //
//				+ ", width = " + sashForm.getSashWidth() + "/" + sashForm.getSize().x);
		sashForm.setWeights(new int[] { 130, 120, 180 });
		
		
		int width = 0;
		for (int i: sashForm.getWeights()) width += i;

        if(width >= MIN_WIDTH_GROUPLIST + MIN_WIDTH_CONTACTLIST + MIN_WIDTH_CONTACTDETAIL)
        {
            weights[0] = 1000000 * MIN_WIDTH_GROUPLIST / width;
            weights[1] = 1000000 * MIN_WIDTH_CONTACTLIST / width;
            weights[2] = 1000000 - weights[0] - weights[1];
        }
        else
        {
            weights[0] = 1000000 * MIN_WIDTH_GROUPLIST / (MIN_WIDTH_GROUPLIST + MIN_WIDTH_CONTACTLIST + MIN_WIDTH_CONTACTDETAIL);
            weights[1] = 1000000 * MIN_WIDTH_CONTACTLIST / (MIN_WIDTH_GROUPLIST + MIN_WIDTH_CONTACTLIST + MIN_WIDTH_CONTACTDETAIL);
            weights[2] = 1000000 * MIN_WIDTH_CONTACTDETAIL / (MIN_WIDTH_GROUPLIST + MIN_WIDTH_CONTACTLIST + MIN_WIDTH_CONTACTDETAIL);

        }

        //System.out.println(width + " " + Arrays.toString(weights));

        //sashForm.setWeights(weights);
	}

}
