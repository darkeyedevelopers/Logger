package darkeyedevelopers.logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    TextView tabText1;
    TextView tabText2;
    DBHandler dbHandler;
    FloatingActionButton cyberadd, jioadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        tabText1 = (TextView) (findViewById(R.id.textView1));
        tabText2 = (TextView) (findViewById(R.id.textView1));
        tabText1.setText("Click here to Login");
        tabText2.setText("Click here to Login");
        host.setup();
        cyberadd = (FloatingActionButton) (findViewById(R.id.cyberadd));
        jioadd = (FloatingActionButton) (findViewById(R.id.jioadd));
        dbHandler = new DBHandler(this);

        //Tab 1
        TabHost.TabSpec cyber = host.newTabSpec("Cyberroam");
        cyber.setContent(R.id.tab1);
        cyber.setIndicator("Cyberroam");
        host.addTab(cyber);
        RelativeLayout cyb = (RelativeLayout) (findViewById(R.id.tab1));

        cyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check from databases for account details
                verify();
            }
        });

        //Tab 2
        TabHost.TabSpec jio = host.newTabSpec("Jio");
        jio.setContent(R.id.tab2);
        jio.setIndicator("Jio");
        host.addTab(jio);
        RelativeLayout ji = (RelativeLayout) (findViewById(R.id.tab2));
        final TextView tabText2 = (TextView) (findViewById(R.id.textView2));

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
                opensettings();
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

    public void opensettings() {
        Intent intent = new Intent(this, AddAccounts.class);
        startActivity(intent);
    }

    public void verify() {
        String dbString = "";
        ArrayList <Accounts> a_list = dbHandler.getallAccounts();
        int i=0;
        while (i<a_list.size()) {
            if (getAccess(a_list.get(i).getUsername(), a_list.get(i).getPass())){
                return ;
            }
            i++;
        }
        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText("Failed to login all accounts are tried..");
        return ;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public boolean getAccess(final String user, final String pass) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        final String[] status = {""};
        String ip = "192.168.1.8";
        String port = "8090";
        String url = "http://" + ip + ":" + port + "/login.xml";
        // Request a string response from the provided URL.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = null;
                try {
                    dBuilder = dbFactory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document doc = null;
                try {
                    InputSource is = new InputSource(new StringReader(response));
                    doc = dBuilder.parse(is);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doc.getDocumentElement().normalize();
                status[0] = doc.getElementsByTagName("status").item(0).getTextContent();
                String contain = getCharacterDataFromElement((Element) doc.getElementsByTagName("message").item(0));

                tabText1.setText(contain + " \n as \n" + user);
                Snackbar.make(findViewById(android.R.id.content), contain + " as " + user, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String response = "Make sure you are connected to WiFi !";
                tabText1.setText(response);
                Snackbar.make(findViewById(android.R.id.content), response, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "191");
                params.put("username", user);
                params.put("password", pass);
                params.put("a", String.valueOf(new Date().getTime()));
                params.put("producttype", "0");
                return params;
            }
        };
        queue.add(stringRequest1);
        if (status[0] == "LOGIN")
            return false;
        else if (status[0] == "LIVE")
            return  true;
        return false;
    }
}
