package ru.lizzzi.autoreg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ru.lizzzi.autoreg.data.RegContract.RegCod;
import ru.lizzzi.autoreg.data.RegDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RegDbHelper mDbHelper = new RegDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mTextView = (TextView)findViewById(R.id.tv_this);
        TextView mTextView2 = (TextView)findViewById(R.id.tv_region);
        TextView mTextView3 = (TextView)findViewById(R.id.tv_othercod);
        TextView mTextView4 = (TextView)findViewById(R.id.tv_text_othercod);

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

        /*try {
            mDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }*/

        mDbHelper.openDataBase();

    }

    @Override
    public void onClick(View view) {
        EditText mEditText = (EditText) findViewById(R.id.editText);
        TextView mTextView = (TextView)findViewById(R.id.tv_this);
        TextView mTextView2 = (TextView)findViewById(R.id.tv_region);
        TextView mTextView3 = (TextView)findViewById(R.id.tv_othercod);
        TextView mTextView4 = (TextView)findViewById(R.id.tv_text_othercod);

        mTextView.setVisibility(View.INVISIBLE);
        mTextView2.setVisibility(View.INVISIBLE);
        mTextView3.setVisibility(View.INVISIBLE);
        mTextView4.setVisibility(View.INVISIBLE);
        mTextView4.setText(R.string.tv_text_othercod);

        String wer = String.valueOf(mEditText.getText());
        if (mEditText.getText().length() == 0) {
            ShowToast("Введите номер региона!");
        } else {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    RegCod._ID,
                    RegCod.COLUMN_COD,
                    RegCod.COLUMN_REGION
            };
            String selection = RegCod.COLUMN_COD + "=?";
            String[] selectionArgs = {(wer)};
            Cursor cursor = db.query(
                    RegCod.TABLE_NAME,
                    projection,
                    selection,             // столбцы для условия WHERE
                    selectionArgs,         // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                 // порядок сортировки

            cursor.moveToFirst();
            if (cursor.getCount() !=0 ){
                mTextView2.setText(cursor.getString(cursor.getColumnIndex(RegCod.COLUMN_REGION)));
                String OCod = cursor.getString(cursor.getColumnIndex(RegCod.COLUMN_REGION));

                String[] projection2 = {
                        RegCod.COLUMN_COD
                };
                selection = RegCod.COLUMN_REGION + "=?";
                selectionArgs = new String[]{(OCod)};
                cursor = db.query(
                        RegCod.TABLE_NAME,
                        projection2,
                        selection,             // столбцы для условия WHERE
                        selectionArgs,         // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                 // порядок сортировки

                mTextView.setVisibility(View.VISIBLE);
                mTextView2.setVisibility(View.VISIBLE);
                String str = "";
                if (cursor.getCount() > 1 ) {
                    if (cursor.moveToFirst()) {
                        do {
                            for (String cn : cursor.getColumnNames()) {
                                if (!cursor.getString(cursor.getColumnIndex(RegCod.COLUMN_COD)).equals(wer)){
                                    str = str.concat(cursor.getString(cursor.getColumnIndex(cn)) + "  ");
                                }
                            }

                        } while (cursor.moveToNext());
                    }
                    if (cursor.getCount() < 3){
                        mTextView4.setText("у этого региона есть также другой код:");
                    }
                    mTextView3.setVisibility(View.VISIBLE);
                    mTextView4.setVisibility(View.VISIBLE);
                }

                mTextView3.setText(str);

                cursor.close();
                db.close();

            }else {
                ShowToast("Такого региона не существует!");
            }

        }
    }

    void ShowToast (String Massage){
        Toast toast = Toast.makeText(this, Massage,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}

