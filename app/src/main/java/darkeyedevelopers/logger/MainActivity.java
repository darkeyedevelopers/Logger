package darkeyedevelopers.logger;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.StringReader;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    TextView tabText1;
    TextView tabText2;
    DBHandler dbHandler;
    FloatingActionButton cyberadd,jioadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        tabText1 = (TextView)(findViewById(R.id.textView1));
        tabText2 = (TextView)(findViewById(R.id.textView1));
        tabText1.setText("Click here to Login");
        tabText2.setText("Click here to Login");
        host.setup();
        cyberadd = (FloatingActionButton)(findViewById(R.id.cyberadd));
        jioadd = (FloatingActionButton)(findViewById(R.id.jioadd));
        dbHandler = new DBHandler(this,null,null,1);

        //Tab 1
        TabHost.TabSpec cyber = host.newTabSpec("Cyberroam");
        cyber.setContent(R.id.tab1);
        cyber.setIndicator("Cyberroam");
        host.addTab(cyber);
        RelativeLayout cyb = (RelativeLayout)(findViewById(R.id.tab1));

        cyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });

        //Tab 2
        TabHost.TabSpec jio = host.newTabSpec("Jio");
        jio.setContent(R.id.tab2);
        jio.setIndicator("Jio");
        host.addTab(jio);
        RelativeLayout ji = (RelativeLayout)(findViewById(R.id.tab2));
        final TextView tabText2 = (TextView)(findViewById(R.id.textView2));

        ji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Add Accounts
        cyberadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        jioadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void verify(){
        String dbString = "";
        Accounts loc = dbHandler.getNextAccount();
        while(loc != null) {
            if(getAccess(loc.getUsername(),loc.getPass()))
                return;
            loc = dbHandler.getNextAccount();
        }
    }

    public boolean getAccess(final String user, final String pass) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String ip = "192.168.1.8";
        String port = "8090";
        String url ="http://"+ip+":"+port+"/login.xml";
        final boolean[] status = {false};
        // Request a string response from the provided URL.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String contain =  response.substring(response.indexOf("<message>"),response.indexOf("</message>"));
                if(contain.contains("Limit"))
                    status[0] = false;
                else if(contain.contains("logged"))
                    status[0] = true;
                tabText1.setText(contain);
                Snackbar.make(findViewById(android.R.id.content),contain, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String response = "Make sure you are connected to WiFi !";
                tabText1.setText(response);
                Snackbar.make(findViewById(android.R.id.content), response , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()  throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("mode","191");
                params.put("username",user);
                params.put("password", pass);
                params.put("a", String.valueOf(new Date().getTime()));
                params.put("producttype", "0");
                return params;
            }
        };
        queue.add(stringRequest1);
        return status[0];
    }
}
