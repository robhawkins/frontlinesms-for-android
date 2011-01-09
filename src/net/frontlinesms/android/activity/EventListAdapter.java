/**
 * This software is written by Meta Healthcare Systems Ltd. and subject
 * to a contract between Meta Healthcare Systems and its customer.
 * <p/>
 * This software stays property of Meta Healthcare Systems unless differing
 * arrangements between Meta Healthcare Systems and its customer apply.
 * <p/>
 * Meta Healthcare Systems Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * <p/>
 * Tel: +852 8199 9605
 * http://www.metahealthcare.com
 * mailto:info@metahealthcare.com
 * <p/>
 * (c)2010 Meta Healthcare Systems Ltd. All rights reserved.
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
import net.frontlinesms.android.data.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
      *
      * @see amandaharrington.widget.ArrayAdapter#getView(int, amandaharrington.view.View,
      * amandaharrington.view.ViewGroup)
      */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_row, null);
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
