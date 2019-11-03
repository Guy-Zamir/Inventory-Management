package com.guy.inventory;

import android.app.Application;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class InventoryApp extends Application {

    public static BackendlessUser user;
    public static List<Supplier> suppliers;
    public static List<Client> clients;
    public static List<Buy> buys;
    public static List<Sale> sales;
    public static final String APPLICATION_ID = "C9CFD411-6AA7-87D4-FF53-EF56C4CC4B00";
    public static final String API_KEY = "E12C2F9C-6B3B-E8B4-FFD5-FC3420C35E00";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY );
    }
}
