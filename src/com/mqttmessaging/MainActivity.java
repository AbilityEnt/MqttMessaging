package com.mqttmessaging;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mqttmessaging.Adapters.ConnectionsAdapter;
import com.mqttmessaging.Adapters.DatabaseAdapter;
import com.mqttmessaging.List.ConnectionList;
import com.mqttmessaging.Utilities.Utils;

public class MainActivity extends BaseActivity {
	DatabaseAdapter dataHelper;
	Button addConnection;
	TextView yourConnectionId, connectionHelp;
	EditText etDiagConId, etDiagConName;
	View connectionDiagView;
	Activity activity;
	Context context;
	ListView connectionListView;
	public ArrayAdapter<ConnectionList> listAdapter;
	List<ConnectionList> conList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataHelper = new DatabaseAdapter(this);
		activity = this;
		context = getApplicationContext();
		yourConnectionId = (TextView) findViewById(R.id.tv_your_connection_id);
		connectionHelp = (TextView) findViewById(R.id.tv_connection_help);
		yourConnectionId.setText("Your ID is: "
				+ Utils.getUniqueId(getContentResolver()));
		connectionListView = (ListView) findViewById(R.id.lv_connections);
		if (dataHelper.getConList() == null) {
			conList = new LinkedList<ConnectionList>();
			connectionHelp.setVisibility(View.VISIBLE);
		} else {
			conList = dataHelper.getConList();
		}
		listAdapter = new ConnectionsAdapter(activity, R.layout.rv_connections,
				conList);
		connectionListView.setAdapter(listAdapter);
		connectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						Message.class);
				intent.putExtra("conId", conList.get(position).getCid());
				intent.putExtra("conName", conList.get(position).getCname());
				startActivity(intent);
			}
		});
		connectionListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						final String cid = dataHelper.getConIdByName(conList
								.get(position).getCname());
						AlertDialog.Builder connectionDialogBuilder = new AlertDialog.Builder(
								activity);

						connectionDialogBuilder.setTitle("Delete Connection")
								.setMessage(
										"Are you sture you want to delete "
												+ conList.get(position)
														.getCname()
												+ " as a connection?");
						connectionDialogBuilder.setPositiveButton("Delete",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dataHelper.deleteConnection(cid);
										listAdapter.clear();
										listAdapter.notifyDataSetChanged();
										if (dataHelper.getConList() != null) {
											listAdapter.addAll(dataHelper
													.getConList());
										} else {
											conList = new LinkedList<ConnectionList>();
											connectionHelp
													.setVisibility(View.VISIBLE);
										}
									}
								});
						connectionDialogBuilder.setNegativeButton("Cancel",
								null);
						final AlertDialog dialog = connectionDialogBuilder
								.create();
						dialog.show();
						return true;
					}
				});
	}

	public void addConnection(View v) {
		connectionDiagView = activity.getLayoutInflater().inflate(
				R.layout.diag_new_connection, null);
		etDiagConId = (EditText) connectionDiagView
				.findViewById(R.id.et_diag_connection_id);
		etDiagConName = (EditText) connectionDiagView
				.findViewById(R.id.et_diag_connection_name);
		AlertDialog.Builder connectionDialogBuilder = new AlertDialog.Builder(
				activity);

		connectionDialogBuilder
				.setView(connectionDiagView)
				.setTitle("Add New Connection")
				.setMessage(
						"Please add in the connection id you are attepting to connect to, and set a name for that id.");
		connectionDialogBuilder.setPositiveButton("Add Connection",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		connectionDialogBuilder.setNegativeButton("Cancel", null);
		final AlertDialog dialog = connectionDialogBuilder.create();
		dialog.show();

		Button posButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		if (posButton != null)
			posButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String conId = etDiagConId.getText().toString();
					String conName = etDiagConName.getText().toString();
					if (conId.equals("") || conName.equals("")) {
						Toast.makeText(context, "Please fill in all fields",
								Toast.LENGTH_LONG).show();
					} else {
						dataHelper.addConnection(conName, conId);
						listAdapter.clear();
						listAdapter.notifyDataSetChanged();
						listAdapter.addAll(dataHelper.getConList());
						dialog.dismiss();
						Toast.makeText(context,
								"Connection added successfully.",
								Toast.LENGTH_LONG).show();
						connectionHelp.setVisibility(View.GONE);
					}
				}

			});
	}

}