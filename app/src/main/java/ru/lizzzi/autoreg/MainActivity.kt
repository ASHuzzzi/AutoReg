package ru.lizzzi.autoreg;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textThis;
    private TextView textRegion;
    private TextView textOtherCodeForRegion;
    private TextView textCode;

    private ViewModelMain viewModel;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(ViewModelMain.class);

        editText = findViewById(R.id.editText);

        initButtonSowRegion();

        textThis = findViewById(R.id.textThis);
        textThis.setVisibility(View.GONE);

        textRegion = findViewById(R.id.textRegion);
        textRegion.setVisibility(View.GONE);

        textOtherCodeForRegion = findViewById(R.id.textOtherCodeForRegion);
        textOtherCodeForRegion.setVisibility(View.GONE);

        textCode = findViewById(R.id.textCode);
        textCode.setVisibility(View.GONE);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initButtonSowRegion() {
        Button buttonShowRegion = findViewById(R.id.buttonShowRegion);
        buttonShowRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredCode = editText.getText().toString();
                if (enteredCode.length() == 0) {
                    showToast(getResources().getString(R.string.toastEnterCodeOfRegion));
                    return;
                }
                if (Integer.parseInt(enteredCode) > 0) {
                    getRegion(enteredCode);
                } else {
                    showToast(getResources().getString(R.string.toastErrorCode));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.checkStorage();
        if (viewModel.getDefaultCodeOfRegion().length() > 0) {
            textThis.setVisibility(View.VISIBLE);
            textRegion.setVisibility(View.VISIBLE);
            if (textOtherCodeForRegion.length() > 1) {
                textOtherCodeForRegion.setVisibility(View.VISIBLE);
                textCode.setVisibility(View.VISIBLE);
            }
        } else {
            editText.requestFocus();
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    private void getRegion(String codeEntered){
        String region = viewModel.getRegion(codeEntered);
        if (region.length() > 0) {
            textThis.setVisibility(View.VISIBLE);
            textRegion.setVisibility(View.VISIBLE);
            textRegion.setText(region);
            getOtherCodeOfRegion(region);

            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        } else {
            showToast(getResources().getString(R.string.toastNoSuchRegion));
        }
    }

    private void getOtherCodeOfRegion(String region) {
        String otherCodeOfRegion = viewModel.getOtherCodeOfRegion(region);
        if (otherCodeOfRegion.length() > 1) {
            textCode.setVisibility(View.VISIBLE);
            textOtherCodeForRegion.setVisibility(View.VISIBLE);
            textOtherCodeForRegion.setText(otherCodeOfRegion);
        }
    }

    private void showToast(String toastText) {
        textRegion.setText("");
        textOtherCodeForRegion.setText("");
        textThis.setVisibility(View.GONE);
        textRegion.setVisibility(View.GONE);
        textOtherCodeForRegion.setVisibility(View.GONE);
        textCode.setVisibility(View.GONE);
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

