package ru.lizzzi.autoreg.data.service

import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import kotlin.Throws
import kotlin.jvm.Synchronized
import android.provider.BaseColumns
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Error
import java.lang.StringBuilder

/**
 * Created by Liza on 10.05.2017.
 */
class SQLStorage(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    object RegionAndCode : BaseColumns {
        const val TABLE_NAME = "autoregg"
        const val COLUMN_CODE = "cod"
        const val COLUMN_REGION = "region"
    }

    companion object {
        private const val DATABASE_NAME = "AutoReg.db"
    }

    // путь к базе данных вашего приложения
    private val dbPath by lazy {
        context.getDatabasePath(DATABASE_NAME).path
    }

    private var database: SQLiteDatabase? = null

    init {
        try {
            database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (ignored: SQLiteException) { }

        if (database == null) {
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.readableDatabase
            try {
                copyDataBase()
            } catch (ioException: IOException) {
                throw Error("Error copying database")
            }
        } else {
            database?.close()
        }
    }

    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     */
    @Throws(IOException::class)
    private fun copyDataBase() {
        //Открываем локальную БД как входящий поток
        val inputStream = context.assets.open(DATABASE_NAME)

        //Открываем пустую базу данных как исходящий поток
        val outputStream: OutputStream = FileOutputStream(dbPath)

        //перемещаем байты из входящего файла в исходящий
        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        //закрываем потоки
        with(outputStream) {
            this.flush()
            this.close()
        }

        inputStream.close()
    }

    @Synchronized
    override fun close() {
        database?.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) { }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }

    fun getRegionCode(codeOfRegion: String): String {
        database = this.readableDatabase
        val columns = arrayOf(RegionAndCode.COLUMN_REGION)
        val selection = RegionAndCode.COLUMN_CODE + "=?"
        val selectionArgs = arrayOf(codeOfRegion)
        val cursor = database?.query(
            RegionAndCode.TABLE_NAME,
            columns,
            selection,       // столбцы для условия WHERE
            selectionArgs,   // значения для условия WHERE
            null,   // Don't group the rows
            null,    // Don't filter by row groups
            null    // порядок сортировки
        )
        var regionResult = ""
        cursor?.let {
            if (cursor.count > 0 && cursor.moveToFirst()) {
                regionResult = cursor.getString(cursor.getColumnIndex(RegionAndCode.COLUMN_REGION))
            }
            cursor.close()
        }
        database?.close()
        return regionResult
    }

    fun getRegionCodes(codeOfRegion: String, region: String): String {
        database = this.readableDatabase
        val columns = arrayOf(RegionAndCode.COLUMN_CODE)
        val selection = RegionAndCode.COLUMN_REGION + "=?"
        val selectionArgs = arrayOf(region)
        val cursor = database?.query(
            RegionAndCode.TABLE_NAME,
            columns,
            selection,       // столбцы для условия WHERE
            selectionArgs,   // значения для условия WHERE
            null,   // Don't group the rows
            null,    // Don't filter by row groups
            null    // порядок сортировки
        )
        val stringBuilder = StringBuilder()
        cursor?.let {
            if (cursor.count > 1 && cursor.moveToFirst()) {
                do {
                    for (columnNames in cursor.columnNames) {
                        if (cursor.getInt(cursor.getColumnIndex(RegionAndCode.COLUMN_CODE)) != codeOfRegion.toInt()) {
                            stringBuilder
                                .append(cursor.getString(cursor.getColumnIndex(columnNames)))
                                .append(", ")
                        }
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        database?.close()
        if (stringBuilder.isNotEmpty() && stringBuilder.last().toString() == " ") {
            stringBuilder.delete(stringBuilder.lastIndex - 1, stringBuilder.lastIndex)
        }
        return stringBuilder.toString()
    }
}