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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;

import java.text.SimpleDateFormat;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class GroupListAdapter extends CursorAdapter {

    LayoutInflater mInflater;


    public GroupListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.group_list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
        holder.txtAccount = (TextView) view.findViewById(R.id.txt_account);
        view.setTag(holder);

        // display group title
        int columnIndex = cursor.getColumnIndex(ContactsContract.Groups.TITLE);
        final String groupTitle = cursor.getString(columnIndex);
        holder.txtTitle.setText(groupTitle);

        // display account
        columnIndex = cursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME);
        holder.txtAccount.setText(cursor.getString(columnIndex));

        // get group id
        columnIndex = cursor.getColumnIndex(ContactsContract.Groups._ID);
        final int groupId = cursor.getInt(columnIndex);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, ContactList.class);
                intent.putExtra(FrontlineSMS.EXTRA_GROUP_ID, groupId);
                intent.putExtra(FrontlineSMS.EXTRA_GROUP_NAME, groupTitle);
                context.startActivity(intent);
            }
        });
    }


    static class ViewHolder {
        TextView txtTitle;
        TextView txtAccount;
    }

}
