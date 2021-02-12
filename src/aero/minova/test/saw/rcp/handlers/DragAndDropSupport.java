package aero.minova.test.saw.rcp.handlers;

import java.text.SimpleDateFormat;
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

public class DragAndDropSupport implements DragSourceListener, DropTargetListener {

        private final NatTable natTable;
        private final SelectionLayer selectionLayer;
        private final List data;
        private final Database db = Database.getInstance();

        private Contact draggedContact;

        private static final String DATA_SEPARATOR = "|";

        public DragAndDropSupport(NatTable natTable, SelectionLayer selectionLayer, List data) {
            this.natTable = natTable;
            this.selectionLayer = selectionLayer;
            this.data = data;
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

            if (!selection.isEmpty()) {
                this.draggedContact = selection.get(0);
                event.data = this.draggedContact.getId() + "";
                
            }
        }

        @Override
        public void dragFinished(DragSourceEvent event) {
            //this.data.remove(this.draggedContact);
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
        public void dragLeave(DropTargetEvent event) {
        }

        @Override
        public void dragOperationChanged(DropTargetEvent event) {
        }

        @Override
        public void dragOver(DropTargetEvent event) {
        }

        @Override
        public void drop(DropTargetEvent event) {
 
            if (event.data.toString().length() > 0) {
                Contact c = db.getContactById(Long.parseLong(event.data.toString()));
                Group g = db.getGroupByPosition(getRowPosition(event));
                
                if (g != null) g.addMember(c);
                
                this.natTable.refresh();
            }
            
            System.out.println(getRowPosition(event));
        }

        @Override
        public void dropAccept(DropTargetEvent event) {
        }

        private int getRowPosition(DropTargetEvent event) {
            Point pt = event.display.map(null, this.natTable, event.x, event.y);
            int position = this.natTable.getRowPositionByY(pt.y);
            return position;
        }
    }