package de.dotwee.backgroundvideoservice.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import de.dotwee.backgroundvideoservice.library.RecordingService;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // listen on button clicks
        findViewById(R.id.buttonStart).setOnClickListener(this);
        findViewById(R.id.buttonStop).setOnClickListener(this);

        // check if service is running and restore layout if true
        notifyRecordingState();
    }

    @Override
    public void onClick(View v) {
        Intent recorderIntent = new Intent(this, RecordingService.class);

        switch (v.getId()) {

            // start service on button-start click
            case R.id.buttonStart:
                startService(recorderIntent);
                break;

            // stop service on button-stop click
            case R.id.buttonStop:
                stopService(recorderIntent);
                break;
        }

        notifyRecordingState();
    }

    /**
     * This method enabled/disabled buttons per service state
     * and updates the running-textview.
     */
    public void notifyRecordingState() {
        boolean isRunning = isServiceRunning();

        findViewById(R.id.buttonStart).setEnabled(!isRunning);
        findViewById(R.id.buttonStop).setEnabled(isRunning);

        // red color if true / green if false
        String color = isRunning ? "#E57373" : "#4CAF50";

        // use spanned for multi-color text
        Spanned message = Html.fromHtml("Running: <font color=\"" + color + "\">" + String.valueOf(isRunning) + "</font>");
        ((TextView) findViewById(R.id.textViewState)).setText(message);
    }

    /**
     * This method checks if the {@link RecordingService} is running
     * by looping through all running services.
     *
     * @return whether its running or not.
     */
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RecordingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
