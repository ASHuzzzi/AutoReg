package ru.lizzzi.autoreg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ru.lizzzi.autoreg.data.RegDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RegDbHelper mDbHelper = new RegDbHelper(this);
    private EditText mEditText;
    private TextView mTextView;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.editText);
        mTextView = findViewById(R.id.tv_this);
        mTextView2 = findViewById(R.id.tv_region);
        mTextView3 = findViewById(R.id.tv_othercod);
        mTextView4 = findViewById(R.id.tv_text_othercod);

        mTextView.setVisibility(View.INVISIBLE);
        mTextView2.setVisibility(View.INVISIBLE);
        mTextView3.setVisibility(View.INVISIBLE);
        mTextView4.setVisibility(View.INVISIBLE);

        mDbHelper = new RegDbHelper(this);

        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        mDbHelper.openDataBase();
    }

    @Override
    public void onClick(View view) {

        String wer = mEditText.getText().toString();

        if (mEditText.getText().length() == 0) {
            ShowToast(getResources().getString(R.string.toast_enterMessage));

        } else {
            try {
                int num = Integer.parseInt(wer);
                if (num > 0){

                    String stRegion = mDbHelper.getRegion(wer);
                    if (stRegion != null){
                        mTextView2.setText(stRegion);
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView2.setVisibility(View.VISIBLE);
                        String stCod = mDbHelper.getCod(stRegion);

                        if (stCod.length() > 1){
                            mTextView3.setText(stCod);
                            mTextView3.setVisibility(View.VISIBLE);
                            mTextView4.setVisibility(View.VISIBLE);
                        }

                        //убираем клавиатуру после нажатия на кнопку
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                    }else {
                        ShowToast(getResources().getString(R.string.toast_noRegion));

                    }
                }else {
                    ShowToast(getResources().getString(R.string.toast_errorSimbol));
                }

            } catch (NumberFormatException e) {
                ShowToast(getResources().getString(R.string.toast_errorSimbol));
            }
        }
    }

    @Override
    public void onStart() {

        super.onStart();
        if (mTextView2.length() > 1){
            mTextView.setVisibility(View.VISIBLE);
            mTextView2.setVisibility(View.VISIBLE);
            if (mTextView3.length() > 1){
                mTextView3.setVisibility(View.VISIBLE);
                mTextView4.setVisibility(View.VISIBLE);
            }
        }else {
            mEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }


    private void ShowToast (String Massage){

        mTextView2.setText("");
        mTextView3.setText("");

        mTextView.setVisibility(View.INVISIBLE);
        mTextView2.setVisibility(View.INVISIBLE);
        mTextView3.setVisibility(View.INVISIBLE);
        mTextView4.setVisibility(View.INVISIBLE);

        Toast toast = Toast.makeText(this, Massage,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

