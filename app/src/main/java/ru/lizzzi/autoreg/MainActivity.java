package ru.lizzzi.autoreg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.lizzzi.autoreg.data.RegDbHelper;

public class MainActivity extends AppCompatActivity {

    private RegDbHelper dbHelper = new RegDbHelper(this);
    private EditText editTextCode;
    private TextView textThisIs;
    private TextView textRegion;
    private TextView textResultOfOtherCod;
    private TextView textOtherCod;
    InputMethodManager methodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCode = findViewById(R.id.editText);
        textThisIs = findViewById(R.id.textThisIs);
        textRegion = findViewById(R.id.textRegion);
        textResultOfOtherCod = findViewById(R.id.textResultOfOtherCod);
        textOtherCod = findViewById(R.id.textOtherCod);
        initBiuttonSendResquest();

        dbHelper = new RegDbHelper(this);
        dbHelper.createDataBase();
        methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initBiuttonSendResquest() {
        Button buttonSendResquest = findViewById(R.id.buttonSendResquest);
        buttonSendResquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (methodManager != null) {
                    methodManager.hideSoftInputFromWindow(editTextCode.getWindowToken(), 0);
                }

                String codeEntered = editTextCode.getText().toString();
                if (codeEntered.matches("")) {
                    ShowToast(getResources().getString(R.string.toast_enterMessage));
                    return;
                }

                int code = Integer.parseInt(codeEntered);
                if (code > 0) {
                    String resultOfQuery = dbHelper.getRegion(codeEntered);
                    if (resultOfQuery != null) {
                        textRegion.setText(resultOfQuery);
                        textThisIs.setVisibility(View.VISIBLE);
                        textRegion.setVisibility(View.VISIBLE);
                        String otherRegionOnEnteredCode = dbHelper.getCode(resultOfQuery);
                        if (otherRegionOnEnteredCode.length() > 1) {
                            textResultOfOtherCod.setText(otherRegionOnEnteredCode);
                            textResultOfOtherCod.setVisibility(View.VISIBLE);
                            textOtherCod.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ShowToast(getResources().getString(R.string.toast_noRegion));
                    }
                } else {
                    ShowToast(getResources().getString(R.string.toast_errorSimbol));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (textRegion.length() > 1) {
            textThisIs.setVisibility(View.VISIBLE);
            textRegion.setVisibility(View.VISIBLE);
            if (textResultOfOtherCod.length() > 1) {
                textResultOfOtherCod.setVisibility(View.VISIBLE);
                textOtherCod.setVisibility(View.VISIBLE);
            }
        } else {
            editTextCode.requestFocus();
            if (methodManager != null) {
                methodManager.toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    private void ShowToast (String massage) {
        String emptyText = getResources().getString(R.string.empty_text);
        textRegion.setText(emptyText);
        textResultOfOtherCod.setText(emptyText);
        textThisIs.setVisibility(View.GONE);
        textRegion.setVisibility(View.GONE);
        textResultOfOtherCod.setVisibility(View.GONE);
        textOtherCod.setVisibility(View.GONE);

        Toast toast = Toast.makeText(this, massage, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

