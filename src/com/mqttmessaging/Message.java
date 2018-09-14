package com.mqttmessaging;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mqttmessaging.R;

public class Message extends BaseActivity {

	Activity activity;
	Context context;
	String conId, conName;
	EditText etMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		Bundle extras = getIntent().getExtras();
		activity = this;
		context = getApplicationContext();
		
		etMessage = (EditText) findViewById(R.id.et_message);

		if (extras != null) {
			conId = extras.getString("conId");
			conName = extras.getString("conName");
			etMessage.setHint("Send message to " + conName);
		}
	}
	
	public void send(View v){
		String message = etMessage.getText().toString();
		if (message != "") {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

}
