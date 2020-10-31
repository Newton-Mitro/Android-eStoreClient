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

public class OrderSuccessDialog implements View.OnClickListener {
    private Activity activity;
    private Dialog orderSuccess;
    private Button homeButton, exitButton;
    private ImageButton cancelButton;
    private Intent intent;

    public OrderSuccessDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        orderSuccess = new Dialog(activity);
        orderSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        orderSuccess.setContentView(R.layout.order_success_layout);
        orderSuccess.setCancelable(true);
        final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(orderSuccess.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.MATCH_PARENT;
        orderSuccess.show();
        orderSuccess.getWindow().setAttributes(lp1);

        homeButton = orderSuccess.findViewById(R.id.home_button_id);
        exitButton = orderSuccess.findViewById(R.id.exit_button_id);
        cancelButton = orderSuccess.findViewById(R.id.cancel_success_dialog_id);

        homeButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_button_id:
                goToHome();
                break;
            case R.id.exit_button_id:
                activity.finishAffinity();
                System.exit(0);
                break;
            case R.id.cancel_success_dialog_id:
                goToHome();
                break;
        }
    }

    private void goToHome() {
        activity.finishAffinity();
        intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

}
