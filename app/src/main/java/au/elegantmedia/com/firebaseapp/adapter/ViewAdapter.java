package au.elegantmedia.com.firebaseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import au.elegantmedia.com.firebaseapp.R;
import au.elegantmedia.com.firebaseapp.helper.UserDetails;

/**
 * Created by Nisala on 9/20/17.
 */

public class ViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<UserDetails> userDetails;

    public ViewAdapter(Context context, ArrayList<UserDetails> userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }

    @Override
    public int getCount() {
        return userDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return userDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserDetails userDetails = (UserDetails) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.details_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvAge = (TextView) convertView.findViewById(R.id.tv_age);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tv_email);
        // Populate the data into the template view using the data object
        tvName.setText(userDetails.getName());
        tvAge.setText(userDetails.getAge());
        tvEmail.setText(userDetails.getEmail());
        // Return the completed view to render on screen
        return convertView;
    }
}
