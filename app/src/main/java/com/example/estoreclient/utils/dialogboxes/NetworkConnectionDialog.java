package com.example.estoreclient.utils.dialogboxes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.estoreclient.R;

public class NetworkConnectionDialog implements View.OnClickListener {
    private Context context;
    private Dialog networkConnectionDialog;
    private ImageButton dialog_close;
    private Button retry_button;

    public NetworkConnectionDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        networkConnectionDialog = new Dialog(context);
        networkConnectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        networkConnectionDialog.setContentView(R.layout.network_connection_failour_layout);
        networkConnectionDialog.setCancelable(true);
        final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(networkConnectionDialog.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.MATCH_PARENT;
        networkConnectionDialog.show();
        networkConnectionDialog.getWindow().setAttributes(lp1);

        dialog_close = networkConnectionDialog.findViewById(R.id.dialog_close_id);
        retry_button = networkConnectionDialog.findViewById(R.id.retry_button_id);

        dialog_close.setOnClickListener(this);
        retry_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_close_id:
                networkConnectionDialog.cancel();
                break;
            case R.id.retry_button_id:
                System.exit(0);
                break;

        }
    }

}
