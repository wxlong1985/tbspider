package com.taobao.UI;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;



public class ListItemModel implements ListModel {
	private ListItemDataCollection collection;

	public ListItemModel(ListItemDataCollection collection) {
		this.collection = collection;
	}

	public Object getElementAt(int index) {
		return (collection.getLocations()[index]);
	}

	public int getSize() {
		return (collection.getLocations().length);
	}

	public void addListDataListener(ListDataListener l) {
	}

	public void removeListDataListener(ListDataListener l) {
	}
}
