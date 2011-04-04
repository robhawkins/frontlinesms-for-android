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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private ArrayList<Event> mItems;
    LayoutInflater mInflater;

    public EventListAdapter(Context context, ArrayList<Event> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    /*
      * (non-Javadoc)
      */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_item, null);
            holder = new ViewHolder();
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtTimestamp = (TextView) convertView.findViewById(R.id.txtTimestamp);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            //holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDescription.setText( Html.fromHtml("<b>" + mItems.get(position).getDescription() + "<b>"));
        holder.txtTimestamp.setText( Html.fromHtml(df.format(mItems.get(position).getTimestamp())));
        //int iconId = convertView.getResources().getIdentifier("icon_photos_" + mItems.get(position).id, "drawable",convertView.getResources().getString(R.string.package_name));
        //holder.icon.setImageResource( iconId  );
        holder.id = mItems.get(position).getId();

        // convertView.setTag(mItems.get(position).id);
        //holder.txtDescription.setText( mItems.get(position).desciption );

        return convertView;
    }

    static class ViewHolder {
        TextView txtDescription;
        TextView txtTimestamp;
        ImageView icon;
        Integer id;
    }

}
