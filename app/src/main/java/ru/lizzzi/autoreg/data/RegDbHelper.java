package ru.lizzzi.autoreg.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
    private SQLiteDatabase myDataBase;
    private final Context mContext;

    public RegDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;

    }

    /**
     * Создает пустую базу данных и перезаписывает ее нашей собственной базой
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            //ничего не делать - база уже есть
        }else{
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
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //база еще не существует
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException{
        //Открываем локальную БД как входящий поток
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        //Путь ко вновь созданной БД
        String outFileName = DB_PATH + DB_NAME;
        //String outFileName = DB_PATH;

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //открываем БД
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getRegion(String stCod){

        myDataBase = this.getReadableDatabase();

        String stRegionResult = null;

        String[] columns = {
                RegContract.RegCod._ID,
                RegContract.RegCod.COLUMN_COD,
                RegContract.RegCod.COLUMN_REGION
        };
        String selection = RegContract.RegCod.COLUMN_COD + "=?";
        String[] selectionArgs = {(stCod)};
        Cursor cursor = myDataBase.query(
                RegContract.RegCod.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки

        cursor.moveToFirst();
        if (cursor.getCount() != 0 ){
            stRegionResult =  cursor.getString(cursor.getColumnIndex(RegContract.RegCod.COLUMN_REGION));
        }

        cursor.close();
        return stRegionResult;
    }

    public String getCod (String stRegion){
        myDataBase = this.getReadableDatabase();

        String[] columns = {
                RegContract.RegCod.COLUMN_COD
        };
        String selection = RegContract.RegCod.COLUMN_REGION + "=?";
        String[] selectionArgs = new String[]{(stRegion)};
        Cursor cursor = myDataBase.query(
                RegContract.RegCod.TABLE_NAME,
                columns,
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // порядок сортировки


        String stCodReult = "";
        if (cursor.getCount() > 1 ) {
            if (cursor.moveToFirst()) {
                do {
                    for (String cn : cursor.getColumnNames()) {
                        if (!cursor.getString(cursor.getColumnIndex(RegContract.RegCod.COLUMN_COD)).equals(stRegion)){
                            stCodReult = stCodReult.concat(cursor.getString(cursor.getColumnIndex(cn)) + "  ");
                        }
                    }

                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return stCodReult;
    }

}
