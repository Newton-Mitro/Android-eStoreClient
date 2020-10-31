package com.example.estoreclient.utils.dialogboxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.estoreclient.R;
import com.example.estoreclient.activities.MainActivity;

public class ExitDialog implements View.OnClickListener {
    private Activity activity;
    private Dialog exitDialog;
    private Button yesButton, cancelButton;
    private Intent intent;

    public ExitDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        exitDialog = new Dialog(activity);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.exit_layout);
        exitDialog.setCancelable(true);
        final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(exitDialog.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        exitDialog.show();
        exitDialog.getWindow().setAttributes(lp1);

        yesButton = exitDialog.findViewById(R.id.yes_button_id);
        cancelButton = exitDialog.findViewById(R.id.cancel_button_id);

        yesButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button_id:
                exitDialog.cancel();
                break;
            case R.id.yes_button_id:
                activity.finishAffinity();
                System.exit(0);
                break;
        }
    }
}
