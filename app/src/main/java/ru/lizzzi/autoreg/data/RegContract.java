package ru.lizzzi.autoreg.data;

import android.provider.BaseColumns;

class RegContract {

    static final class RegCod implements BaseColumns {
        final static String TABLE_NAME = "autoregg";
        final static String _ID = BaseColumns._ID;
        final static String COLUMN_COD = "cod";
        final static String COLUMN_REGION = "region";
    }
}
