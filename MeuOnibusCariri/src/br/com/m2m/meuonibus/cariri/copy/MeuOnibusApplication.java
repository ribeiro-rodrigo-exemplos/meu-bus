package br.com.m2m.meuonibus.cariri.copy;import android.content.Context;import br.com.hhw.startapp.StartAppApplication;public class MeuOnibusApplication extends StartAppApplication {	private static Context sContext;	     @Override    public void onCreate() {        super.onCreate();        sContext = getApplicationContext();    }     /**     * Returns the application context     *     * @return application context     */    public static Context getContext() {        return sContext;    }}