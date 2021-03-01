package aero.minova.test.saw.rcp.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Point;

import aero.minova.test.saw.rcp.model.Contact;
import aero.minova.test.saw.rcp.model.Database;
import aero.minova.test.saw.rcp.model.Group;

public class DragAndDropSupportGroups implements DragSourceListener, DropTargetListener {

	private final NatTable natTable;
	private final SelectionLayer selectionLayer;
	private Group draggedGroup;

	private final Database db = Database.getInstance();

	public DragAndDropSupportGroups(NatTable natTable, SelectionLayer selectionLayer) {
		this.natTable = natTable;
		this.selectionLayer = selectionLayer;
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
					Contact c = VCardImportHandler.createContactFromString(content).get(0);
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

	@Override
	public void dragStart(DragSourceEvent event) {
		if (this.selectionLayer.getSelectedRowCount() == 0) {
			event.doit = false;
		} else if (!this.natTable.getRegionLabelsByXY(event.x, event.y).hasLabel(GridRegion.BODY)) {
			event.doit = false;
		}

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		@SuppressWarnings("unchecked")
		List<Group> selection = ((RowSelectionModel<Group>) this.selectionLayer.getSelectionModel()).getSelectedRowObjects();
		List<String> paths = new ArrayList<String>();
		for (int i = 0; i < selection.size(); i++) {
			this.draggedGroup = selection.get(i);

			try {
				File file = File.createTempFile(draggedGroup.getName(), ".vcf");
				FileWriter myWriter = new FileWriter(file.getAbsolutePath());
				myWriter.write(VCardExportHandler.getVCardString(draggedGroup.getMembers()));
				myWriter.close();
				paths.add(file.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String[] array = new String[paths.size()];
		paths.toArray(array); // fill the array
		event.data = array;

	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		this.draggedGroup = null;

		this.natTable.refresh();
	}

}
