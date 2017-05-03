package com.pipowersaver.efronbs.pipowersaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity implements IRequestCallback {

    public Set<String> connectedDevices = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context ctx = this;
        final IRequestCallback callbackObject = this;

        Button b = (Button) findViewById(R.id.newDeviceButton);
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(ctx);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceName = input.getText().toString();
                        try {
                            Requests.registerNewDevice(ctx, deviceName, callbackObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void deviceRegisterSuccess(final String deviceName) {

        if (connectedDevices.contains(deviceName)) {
            return;
        } else {
            connectedDevices.add(deviceName);
        }

        final Context ctx = this;
        final IRequestCallback callback = this;

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        // create new layout to hold toggle and label
        LinearLayout newLL = new LinearLayout(this);
        newLL.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams newLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //create new label
        EditText label = new EditText(this);
        label.setText(deviceName);
        label.setFocusable(false);
        label.setClickable(false);
        label.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f);
        newLL.addView(label, labelParams);

        // create new button
        ToggleButton newButton = new ToggleButton(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.3f);
        newLL.addView(newButton, buttonParams);

        // add the new linear layout to the container layout
        buttonLayout.addView(newLL, newLLParams);

        // set the
        newButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private String name = deviceName;
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToggleState state = isChecked ? ToggleState.ON : ToggleState.OFF;

                try {
                    Requests.toggleDeviceState(this.name, state, ctx, callback);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deviceRegisterFailed() {
        System.out.println("From activity: register failed, probably already exists");
    }
}
