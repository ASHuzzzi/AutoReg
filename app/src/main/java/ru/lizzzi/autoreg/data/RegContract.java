package ru.lizzzi.autoreg.data;

import android.provider.BaseColumns;



public class RegContract {
    private RegContract(){

    }

    public static final class RegCod implements BaseColumns {
        public final static String TABLE_NAME = "autoregg";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COD = "cod";
        public final static String COLUMN_REGION = "region";
    }
}
