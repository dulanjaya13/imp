/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.enhancement;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.jivesoftware.smackx.muc.HostedRoom;

/**
 *
 * @author Dulanjaya
 */
public class GroupCellRenderer implements Callback<ListView<HostedRoom>, ListCell<HostedRoom>> {

    @Override
    public ListCell<HostedRoom> call(ListView<HostedRoom> param) {
        ListCell<HostedRoom> cell = new ListCell<HostedRoom>() {

            @Override
            protected void updateItem(HostedRoom room, boolean bln) {
                super.updateItem(room, bln);
                setGraphic(null);
                setText(null);
                if (room != null) {
                    setText(room.getName());
                }
            }
        };
        return cell;
    }
}
