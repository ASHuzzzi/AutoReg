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
    private TextView textOtherCode;
    private TextView textOtherCode2;

    private ViewModelMain viewModel;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textThis = findViewById(R.id.textThis);
        textRegion = findViewById(R.id.textRegion);
        textOtherCode = findViewById(R.id.textOtherCode);
        textOtherCode2 = findViewById(R.id.textOtherCode2);
        Button buttonShowRegion = findViewById(R.id.buttonShowRegion);
        buttonShowRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeEntered = editText.getText().toString();
                if (codeEntered.length() == 0) {
                    showToast(getResources().getString(R.string.toastEnterCodeOfRegion));
                    return;
                }
                if (Integer.parseInt(codeEntered) > 0) {
                    String region = viewModel.getRegion(codeEntered);
                    if (region.length() > 0) {
                        textRegion.setText(region);
                        textThis.setVisibility(View.VISIBLE);
                        textRegion.setVisibility(View.VISIBLE);
                        String codeOfRegion = viewModel.getCodeOfRegion(region);
                        if (codeOfRegion.length() > 1) {
                            textOtherCode.setText(codeOfRegion);
                            textOtherCode.setVisibility(View.VISIBLE);
                            textOtherCode2.setVisibility(View.VISIBLE);
                        }

                        //убираем клавиатуру после нажатия на кнопку
                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    editText.getWindowToken(),
                                    0);
                        }
                    } else {
                        showToast(getResources().getString(R.string.toastNoSuchRegion));
                    }
                }else {
                    showToast(getResources().getString(R.string.toastErrorCode));
                }
            }
        });

        textThis.setVisibility(View.INVISIBLE);
        textRegion.setVisibility(View.INVISIBLE);
        textOtherCode.setVisibility(View.INVISIBLE);
        textOtherCode2.setVisibility(View.INVISIBLE);
        viewModel = ViewModelProviders.of(this).get(ViewModelMain.class);
        viewModel.checkStorage();
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (viewModel.getDefaultCodeOfRegion().length() > 0) {
            textThis.setVisibility(View.VISIBLE);
            textRegion.setVisibility(View.VISIBLE);
            if (textOtherCode.length() > 1) {
                textOtherCode.setVisibility(View.VISIBLE);
                textOtherCode2.setVisibility(View.VISIBLE);
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

    private void showToast(String Massage) {
        textRegion.setText("");
        textOtherCode.setText("");
        textThis.setVisibility(View.INVISIBLE);
        textRegion.setVisibility(View.INVISIBLE);
        textOtherCode.setVisibility(View.INVISIBLE);
        textOtherCode2.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(this, Massage,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

