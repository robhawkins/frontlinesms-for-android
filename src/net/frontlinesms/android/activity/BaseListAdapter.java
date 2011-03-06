package net.frontlinesms.android.activity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CursorAdapter;

import java.util.Vector;

public abstract class BaseListAdapter extends CursorAdapter {

    protected LayoutInflater mInflater;
    protected Vector<Integer> mSelectedItems = new Vector<Integer>();

    public BaseListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mInflater = LayoutInflater.from(context);
        this.mSelectedItems.clear();
    }

    protected void toggleCheck(CheckBox chkBox, int id) {
        if (mSelectedItems.contains(id)) {
            mSelectedItems.removeElement(id);
        } else {
            mSelectedItems.add(id);
        }
        chkBox.setChecked(mSelectedItems.contains(id));
    }

    public Vector<Integer> getSelectedItems() {
        return mSelectedItems;
    }

}
