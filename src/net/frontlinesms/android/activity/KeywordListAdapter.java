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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class KeywordListAdapter extends BaseListAdapter {

    public KeywordListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.rule_list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        final ViewHolder holder = new ViewHolder();
        holder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
        holder.txtDescription = (TextView) view.findViewById(R.id.txt_description);
        holder.chkBox = (CheckBox) view.findViewById(R.id.chk_box);
        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCheck(holder.chkBox, holder.id);
            }
        });
        view.setTag(holder);

        // keyword
        int columnIndex = cursor.getColumnIndex("keyword");
        final String keyword = cursor.getString(columnIndex);
        holder.txtTitle.setText(keyword);

        // description
        columnIndex = cursor.getColumnIndex("text");
        holder.txtDescription.setText(cursor.getString(columnIndex));

        // get keyword id
        columnIndex = cursor.getColumnIndex("_id");
        final int keywordId = cursor.getInt(columnIndex);
        holder.id = keywordId;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, ContactList.class);
                intent.putExtra(FrontlineSMS.EXTRA_KEYWORD_ID, keywordId);
                intent.putExtra(FrontlineSMS.EXTRA_KEYWORD_KEYWORD, keyword);
                context.startActivity(intent);
            }
        });

        holder.chkBox.setChecked(mSelectedItems.contains(holder.id));
    }


    static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        CheckBox chkBox;
        Integer id;
    }


}
