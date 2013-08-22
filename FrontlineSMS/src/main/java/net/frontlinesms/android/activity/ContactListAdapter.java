/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2010, Meta Healthcare Systems Ltd.
 *
 * This file is part of FrontlineSMS for Android.
 *
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.android.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class ContactListAdapter extends BaseListAdapter {

    public ContactListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.contact_list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        final ViewHolder holder = new ViewHolder();
        holder.txtDisplayName = (TextView) view.findViewById(R.id.txt_display_name);
        holder.txtMobile = (TextView) view.findViewById(R.id.txt_mobile);
        holder.chkBox = (CheckBox) view.findViewById(R.id.chk_box);
        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCheck(holder.chkBox, holder.id);
            }
        });
        view.setTag(holder);

        // display contact name
        int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        holder.txtDisplayName.setText(cursor.getString(columnIndex));

        columnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        holder.id = cursor.getInt(columnIndex);

        holder.chkBox.setChecked(mSelectedItems.contains(holder.id));

        // display mobile no
//        columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.ACCOUNT_NAME);
//        holder.txtMobile.setText(cursor.getString(columnIndex));

    }


    static class ViewHolder {
        TextView txtDisplayName;
        TextView txtMobile;
        CheckBox chkBox;
        Integer id;
    }

}
