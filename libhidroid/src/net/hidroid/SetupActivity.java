package net.hidroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends Activity {
	private EditText testInput = null;
	private EditText psm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);

		testInput = (EditText) findViewById(R.id.testInput);
		psm = (EditText) findViewById(R.id.psm);
		final Button doTest = (Button) findViewById(R.id.doTest);

		// Set up remote device choice dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.chooseBondedDevice));

		doTest.setOnClickListener(new OnTestRequestListener(builder));
	}

	private class OnTestRequestListener implements View.OnClickListener {
		AlertDialog.Builder builder;

		public OnTestRequestListener(Builder builder) {
			super();
			this.builder = builder;
		}

		private class ChosenDeviceListener implements
				DialogInterface.OnClickListener {
			Intent onClickIntent;
			BluetoothDevice[] devices;

			public ChosenDeviceListener(Intent onClickIntent,
					BluetoothDevice[] devices) {
				super();
				this.onClickIntent = onClickIntent;
				this.devices = devices;
			}

			public void onClick(DialogInterface dialogInterface, int item) {
				onClickIntent
						.putExtra(TestActivity.KEY_BLUETOOTH_REMOTE_DEVICE,
								devices[item]);
				startActivity(onClickIntent);
				return;
			}
		}

		public void onClick(View v) {
			// testOutput.setText(new L2capSocket().test());
			// intent.putExtra("blah", OnTestRequestListener.class);
			Intent intent = new Intent(getApplicationContext(),
					TestActivity.class);

			intent.putExtra(TestActivity.KEY_BLUETOOTH_PSM,
					Integer.parseInt(psm.getText().toString()));
			intent.putExtra(TestActivity.KEY_BLUETOOTH_WRITE_STRING, testInput
					.getText().toString());

			// Show remote device chooser dialog
			BluetoothDevice[] devices = new BluetoothDevice[1];
			devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices()
					.toArray(devices);
			final CharSequence[] items = new CharSequence[devices.length];
			for (int i = 0; i < devices.length; ++i) {
				items[i] = devices[i].getName();
			}
			builder.setItems(items, new ChosenDeviceListener(intent, devices));
			builder.create().show();
		}
	}
}