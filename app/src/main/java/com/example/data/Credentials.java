package com.example.data;

import android.content.res.Resources;

import com.example.hadev.myfrontend.R;

import java.util.Properties;

/**
 * Created by Hadev on 8-3-2017.
 */
public class Credentials {

    private static Properties properties;

    private final String auth;
    private final String seed;

    public static Properties getProperties()
    {
        if (properties == null)
        {
            properties = new Properties();
            try {
                //properties.load(SimpleDBAdapter.class.getClassLoader().getResourceAsStream("simpledb.properties"));
                //properties.load(Resources.getSystem().openRawResource(R.raw.simpledb));

            } catch (Exception e) {
                //Log.e(TAG, "Cannot load auth properties", e);
                System.out.println("Cannot load auth properties" + e);
            }
        }
        return properties;
    }

    @SuppressWarnings("unused")
    private static String encrypt(String key, String secret, String seed) {
        return new Credentials(seed).encrypt(key.toCharArray(), secret.toCharArray());
    }

    public Credentials(String[] s, String seed) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            builder.append(s[i]);
        }
        this.seed = seed;
        this.auth = decrypt(builder.toString().toCharArray());
    }

    public Credentials(String auth, String seed) {
        this.seed = seed;
        this.auth = decrypt(auth.toCharArray());
    }

    public Credentials(String seed) {
        this.seed = seed;
        this.auth = "";
    }

    private String decrypt(char[] cs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cs.length; i+=2) {
            try {
                builder.append((char)(cs[i]-(i/2%(toint(seed)/2))));
            } catch (ArithmeticException e) {
            }
        }
        return builder.toString();
    }

    private int toint(String seed) {
        try {
            char[] c = seed.toCharArray();
            return (byte)c[2]-((byte)c[0]+(byte)c[1])/(byte)2;
        } catch (IndexOutOfBoundsException e) {
        }
        return -1;
    }

    public String getAuth() {
        try {
            return auth.substring(0, toint(seed));
        } catch (IndexOutOfBoundsException e) {
        }
        return null;
    }

    public String getAuth2() {
        try {
            return auth.substring(toint(seed));
        } catch (IndexOutOfBoundsException e) {
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(auth);
        builder.append(toint(seed));
        return builder.toString();
    }
}
