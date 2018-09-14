package com.mqttmessaging.Adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mqttmessaging.MainActivity;
import com.mqttmessaging.R;
import com.mqttmessaging.List.ConnectionList;

public class ConnectionsAdapter extends ArrayAdapter<ConnectionList> {

	private List<ConnectionList> cons;
	private Activity context;
	private int id;
	MainActivity mainHelper;

	public ConnectionsAdapter(Activity context, int resource,
			List<ConnectionList> objects) {
		super(context, resource, objects);
		this.context = context;
		this.id = resource;
		this.cons = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View root = convertView;
		if (root == null)
			root = context.getLayoutInflater().inflate(id, null);
		TextView tvName = (TextView) root.findViewById(R.id.tv_connection_name);
		// TextView tvDate = (TextView)
		// root.findViewById(R.id.tv_connection_date);

		ConnectionList con = cons.get(position);
		if (con != null) {
			tvName.setText("" + con.getCname());
			// tvDate.setText(con.getCdate());
		}

		return root;
	}

}
