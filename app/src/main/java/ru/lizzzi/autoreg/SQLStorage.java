package ru.lizzzi.autoreg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Liza on 10.05.2017.
 */

public class SQLStorage extends SQLiteOpenHelper {

    // путь к базе данных вашего приложения
    @SuppressLint("SdCardPath")
    private String DATABASE_PATH = "/data/data/ru.lizzzi.autoreg/databases/";
    private static String DATABASE_NAME = "AutoReg.db";
    private SQLiteDatabase database;
    private final Context context;

    public SQLStorage(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public void checkDataBase() {
        database = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException ignored) {
        }
        if(database == null) {
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else {
            database.close();
        }
    }

    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException {
        //Открываем локальную БД как входящий поток
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);

        //Путь ко вновь созданной БД
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Открываем пустую базу данных как исходящий поток
        OutputStream outputStream = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        //закрываем потоки
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public synchronized void close() {
        if(database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getRegion(String codeOfRegion) {
        database = this.getReadableDatabase();
        String[] columns = { RegionAndCode.COLUMN_REGION };
        String selection = RegionAndCode.COLUMN_CODE + "=?";
        String[] selectionArgs = {codeOfRegion};
        Cursor cursor = database.query(
                RegionAndCode.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки

        String regionResult = "";
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            regionResult =  cursor.getString(cursor.getColumnIndex(RegionAndCode.COLUMN_REGION));
        }
        cursor.close();
        database.close();
        return regionResult;
    }

    public String getCode(String region) {
        database = this.getReadableDatabase();
        String[] columns = { RegionAndCode.COLUMN_CODE};
        String selection = RegionAndCode.COLUMN_REGION + "=?";
        String[] selectionArgs = new String[]{(region)};
        Cursor cursor = database.query(
                RegionAndCode.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки


        String codeResult = "";
        if (cursor.getCount() > 1 && cursor.moveToFirst()) {
            do {
                for (String columnNames : cursor.getColumnNames()) {
                    if (!cursor.getString(cursor.getColumnIndex(RegionAndCode.COLUMN_CODE)).equals(region)) {
                        codeResult = codeResult.concat(
                                cursor.getString(cursor.getColumnIndex(columnNames)) + " ");
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return codeResult;
    }

    public static final class RegionAndCode implements BaseColumns {
        final static String TABLE_NAME = "autoregg";
        final static String COLUMN_CODE = "cod";
        final static String COLUMN_REGION = "region";
    }
}
