package com.guy.inventory;

import android.app.Application;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class InventoryApp extends Application {

    public static BackendlessUser user;
    public static List<Sort> sorts;
    public static List<Client> clients;
    public static List<Buy> buys;
    public static List<Sale> sales;
    public static List<Export> exports;
    public static final String APPLICATION_ID = "Enter your Application ID here";
    public static final String API_KEY = "Enter your API key here";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY );
    }
}
