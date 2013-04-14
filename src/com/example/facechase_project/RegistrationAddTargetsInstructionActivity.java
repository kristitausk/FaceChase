package com.example.facechase_project;

import com.example.facechase_project.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class RegistrationAddTargetsInstructionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_add_targets_instruction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration_add_targets_instruction,
				menu);
		return true;
	}
	
	public void ok(View view) {
		Intent intent = new Intent(this, AddTargetsActivity.class);
		startActivity(intent);
	}

}
