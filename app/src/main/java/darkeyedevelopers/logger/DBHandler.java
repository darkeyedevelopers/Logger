package darkeyedevelopers.logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onk_r on 26-02-2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Accounts.db";
    private static final String TABLE_ACCOUNTS = "ACCOUNTS";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_PASS = "PASS";
    Cursor c;
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        setCursor();
    }

    public void setCursor() {
        SQLiteDatabase db = getReadableDatabase();
        c = db.rawQuery("SELECT * FROM "+TABLE_ACCOUNTS,null);
        c.moveToFirst();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY ," +
                COLUMN_PASS + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
    }

    //add data
    public void addAccount(Accounts New) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,New.getUsername());
        values.put(COLUMN_PASS,New.getPass());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ACCOUNTS,null,values);
        db.close();
    }

    //remove data
    public void removeAccount(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_ACCOUNTS+" WHERE "+COLUMN_USERNAME +" = \"" +username +"\";" );
    }


    public Accounts getNextAccount() {
        Accounts retrived = null;
        String user_name="",pwd="";
        if (c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_USERNAME))!=null && c.getString(c.getColumnIndex(COLUMN_PASS))!=null) {
                user_name = c.getString(c.getColumnIndex(COLUMN_USERNAME));
                pwd = c.getString(c.getColumnIndex(COLUMN_PASS));
            }
            retrived = new Accounts(user_name,pwd);
        }
        return  retrived;
    }
}
