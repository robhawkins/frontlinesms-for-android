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
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.frontlinesms.android.R;
import net.frontlinesms.android.data.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class MessageListAdapter extends CursorAdapter {

    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    LayoutInflater mInflater;


    public MessageListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.message_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
        holder.txtMetaInfo = (TextView) view.findViewById(R.id.txt_message_info);
        view.setTag(holder);

        // display message content
        int columnIndex = cursor.getColumnIndex(MessageList.BODY_COLUMN);
//        holder.txtTitle.setText( Html.fromHtml("<b>" + cursor.getString(columnIndex) + "<b>"));
        holder.txtTitle.setText(cursor.getString(columnIndex));

        // display meta information like date, from/to and number
        columnIndex = cursor.getColumnIndex(MessageList.DATE_COLUMN);
        String s = df.format(cursor.getLong(columnIndex));
        columnIndex = cursor.getColumnIndex(MessageList.TYPE_COLUMN);
        s += cursor.getInt(columnIndex)==1?" from ":" to ";
        columnIndex = cursor.getColumnIndex(MessageList.NUMBER_COLUMN);
        s += cursor.getString(columnIndex);
        holder.txtMetaInfo.setText( Html.fromHtml(s));
    }


    static class ViewHolder {
        TextView txtTitle;
        TextView txtMetaInfo;
    }

}
