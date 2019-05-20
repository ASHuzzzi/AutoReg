package ru.lizzzi.autoreg.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Liza on 10.05.2017.
 */

public class RegDbHelper extends SQLiteOpenHelper {

    // путь к базе данных вашего приложения
    @SuppressLint("SdCardPath")
    private static String DB_PATH = "/data/data/ru.lizzzi.autoreg/databases/";
    private static String DB_NAME = "AutoReg.db";
    private SQLiteDatabase database;
    private final Context context;

    public RegDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    /**
     * Создает пустую базу данных и перезаписывает ее нашей собственной базой
     * */
    public void createDataBase() {
        if(!checkDataBase()){
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
     * @return true если существует, false если не существует
     */
    private boolean checkDataBase(){
        database = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e) {
            //база еще не существует
        }
        if (database != null) {
            database.close();
        }
        return database != null;
    }

    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException {
        //Открываем локальную БД как входящий поток
        InputStream inputStream = context.getAssets().open(DB_NAME);

        //Путь ко вновь созданной БД
        database = this.getReadableDatabase();
        String outFileName = database.getPath();
        database.close();

        //Открываем пустую базу данных как исходящий поток
        OutputStream outputStream = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer))>0) {
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
        String[] columns = {
                RegContract.RegCod._ID,
                RegContract.RegCod.COLUMN_COD,
                RegContract.RegCod.COLUMN_REGION
        };
        String selection = RegContract.RegCod.COLUMN_COD + "=?";
        String[] selectionArgs = {(codeOfRegion)};
        Cursor cursor = database.query(
                RegContract.RegCod.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки

        cursor.moveToFirst();
        String queryResult = null;
        if (cursor.getCount() != 0 ) {
            queryResult =  cursor.getString(cursor.getColumnIndex(RegContract.RegCod.COLUMN_REGION));
        }
        cursor.close();
        database.close();
        return queryResult;
    }

    public String getCode(String codeOfRegion) {
        database = this.getReadableDatabase();
        String[] columns = {
                RegContract.RegCod.COLUMN_COD
        };
        String selection = RegContract.RegCod.COLUMN_REGION + "=?";
        String[] selectionArgs = new String[]{(codeOfRegion)};
        Cursor cursor = database.query(
                RegContract.RegCod.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки

        String queryResult = "";
        if (cursor.getCount() > 1 ) {
            if (cursor.moveToFirst()) {
                do {
                    for (String cn : cursor.getColumnNames()) {
                        if (!cursor.getString(cursor.getColumnIndex(RegContract.RegCod.COLUMN_COD)).equals(codeOfRegion)){
                            queryResult = queryResult.concat(cursor.getString(cursor.getColumnIndex(cn)) + "  ");
                        }
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return queryResult;
    }
}
