/*******************************************************************************
 * Copyright (c) 2014, 2020 Dirk Fauth and others. This program and the accompanying materials are made available under the terms of the Eclipse Public License
 * 2.0 which is available at https://www.eclipse.org/legal/epl-2.0/ SPDX-License-Identifier: EPL-2.0 Contributors: Dirk Fauth <dirk.fauth@googlemail.com> -
 * initial API and implementation Janos Binder <janos.binder@openchrom.net> - position is stored
 *******************************************************************************/

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

import aero.minova.test.saw.rcp.model.Contact;

public class DragAndDropSupportContacts implements DragSourceListener, DropTargetListener {

	private final NatTable natTable;
	private final SelectionLayer selectionLayer;

	private Contact draggedContact;

	public DragAndDropSupportContacts(NatTable natTable, SelectionLayer selectionLayer) {
		this.natTable = natTable;
		this.selectionLayer = selectionLayer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		if (this.selectionLayer.getSelectedRowCount() == 0) {
			event.doit = false;
		} else if (!this.natTable.getRegionLabelsByXY(event.x, event.y).hasLabel(GridRegion.BODY)) {
			event.doit = false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dragSetData(DragSourceEvent event) {
		// we know that we use the RowSelectionModel with single selection
		List<Contact> selection = ((RowSelectionModel<Contact>) this.selectionLayer.getSelectionModel()).getSelectedRowObjects();
		List<String> paths = new ArrayList<String>();
		for (int i = 0; i < selection.size(); i++) {
			this.draggedContact = selection.get(i);

			try {
				File file = File.createTempFile(draggedContact.getFirstName(), ".vcf");
				FileWriter myWriter = new FileWriter(file.getAbsolutePath());
				myWriter.write(VCardExportHandler.getVCardString(draggedContact));
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
		// this.data.remove(this.draggedContact);
		this.draggedContact = null;

		// clear selection
		this.selectionLayer.clear();

		this.natTable.refresh();
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

		// System.out.println(((String[]) event.data)[0]);

		if (event.data.getClass().equals(String[].class)) {
			String[] data = (String[]) event.data;
			for (String path : data) {
				if (path.contains(".vcf")) {
					try {
						String content = new String(Files.readAllBytes(Paths.get(path)));
						VCardImportHandler.createContactFromString(content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {}

}