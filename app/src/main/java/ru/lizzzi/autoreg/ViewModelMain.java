package ru.lizzzi.autoreg;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class ViewModelMain extends AndroidViewModel {
    private SQLStorage sqlStorage;
    private String defaultCodeOfRegion = "";

    public ViewModelMain(@NonNull Application application) {
        super(application);
        sqlStorage = new SQLStorage(getApplication().getApplicationContext());
    }

    public void checkStorage() {
        sqlStorage.checkDataBase();
    }

    public String getDefaultCodeOfRegion() {
        return defaultCodeOfRegion;
    }

    public String getRegion(String codeOfRegion) {
        return defaultCodeOfRegion = sqlStorage.getRegion(codeOfRegion);
    }

    public  String getCodeOfRegion(String region) {
        return sqlStorage.getCod(region);
    }
}
