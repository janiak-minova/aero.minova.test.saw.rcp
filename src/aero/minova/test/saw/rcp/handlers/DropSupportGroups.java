package aero.minova.test.saw.rcp.handlers;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Point;

import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;
import aero.minova.test.saw.rcp.model.Group;

public class DropSupportGroups implements DropTargetListener {

	private final NatTable natTable;

	private final Database db = Database.getInstance();

	public DropSupportGroups(NatTable natTable) {
		this.natTable = natTable;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
	}

	@Override
	public void dragLeave(DropTargetEvent event) {}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {}

	@Override
	public void dragOver(DropTargetEvent event) {}

	@Override
	public void drop(DropTargetEvent event) {
		if (event.data.getClass().equals(String[].class)) {
			String[] data = (String[]) event.data;
			for (String path : data) {
				String content;
				try {
					content = new String(Files.readAllBytes(Paths.get(path)));
					Contact c = VCardImportHandler.createContactFromString(content);
					Group g = db.getGroupByPosition(getRowPosition(event));
					g.addMember(c);
					this.natTable.refresh();
				} catch (Exception e) {}
			}
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {}

	private int getRowPosition(DropTargetEvent event) {
		Point pt = event.display.map(null, this.natTable, event.x, event.y);
		int position = this.natTable.getRowPositionByY(pt.y);
		return position;
	}

}
