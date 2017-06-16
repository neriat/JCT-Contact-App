package neria.tzidkani.jct_contact;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    //////////////////////////////////////////////////////////////
    //                     Data Structure                       //
    //////////////////////////////////////////////////////////////

    ArrayList<Contact> ContactList = new ArrayList<Contact>();
    ArrayList<Contact> ContactList_filter = new ArrayList<Contact>();
    Toast mToast;
    Boolean toclose=false;

    //////////////////////////////////////////////////////////////
    //                        ACTIVITY                          //
    //////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // kill-switch to expiration
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy");
        Date todayDate = new Date();
        Integer time = Integer.parseInt(currentDate.format(todayDate));
        if (time > 2017) {
            //Toast.makeText(this, ShowText.update, Toast.LENGTH_LONG).show();
            toclose=true;
        }

        // force close app
        if(toclose){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        // An indicator for initialization time
        long msTime = System.currentTimeMillis();

        //TODO: Delete
        //ShowShortToast(ShowText.author);

        // list data implemented via hard coded list (c# generated)
        // TODO: Check if HardCodedList is better, if not, enable this code
        ContactList = HardCodedList.getList();
/*
        try {
            ContactList = parse();
        } catch (IOException c) {
        } catch (XmlPullParserException c) {
        }
*/

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO delete if cause crashing
        final Button search = (Button) findViewById(R.id.SearchButton);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText searchInput = (EditText) findViewById(R.id.SearchString);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, ShowText.authorHEB, Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                //clear search text box
                searchInput.setText("", null);
            }
        });

        ButtonInitialization();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long msTime = System.currentTimeMillis();
                UpdateScrollViewByShowOrHide();
                //OLDsearchListenerFunction();
            }
        });
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    UpdateScrollViewByShowOrHide();
                    return true;
                }
                return false;
            }
        });

        /* HARD CODED TEST LIST
        ContactList.add(new Contact("aa","aa@aa"));
        ContactList.add(new Contact("bb","bb@bb"));
        ContactList.add(new Contact("cc","cc@cc"));
        ContactList.add(new Contact("dd","dd@dd"));
        ContactList.add(new Contact("ee","ae@ee"));
        ContactList.add(new Contact("ff","ff@ff"));
        ContactList.add(new Contact("gg","gg@gg"));
*/

        ShowShortToast(ShowText.initializationCalculationTime(System.currentTimeMillis() - msTime)); // Shows how long initialization actually occurred

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //////////////////////////////////////////////////////////////
    //                     Query Function                       //
    //////////////////////////////////////////////////////////////


    private void ButtonInitialization() {
        ScrollView scroll = (ScrollView) (findViewById(R.id.ScrollV));
        LinearLayout linear = (LinearLayout) (findViewById(R.id.LinearV));

        for (Contact c : ContactList) {
            Button t = new Button(this);
            t.setTransformationMethod(null);
            t.setText(c.toString());
            t.setVisibility(View.GONE);

            AssignActionToButton(t, c.name, c.email);
            linear.addView(t);
        }
    }

    private boolean containsAll (String[] list,String str){
    if (list.length==0) return false;
        for(String item:list)
            if (!str.contains(item))
                return false;
        return true;
    }

    // Function gets filtered contact list, and shows / hides it on ScrollView
    private void UpdateScrollViewByShowOrHide() {
        // force close app
        if(toclose){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        // TODO: Delete debug time calculation - PART 1 OF 2
        long msTime = System.currentTimeMillis();

        ScrollView scroll = (ScrollView) (findViewById(R.id.ScrollV));
        LinearLayout linear = (LinearLayout) (findViewById(R.id.LinearV));
        linear.setOrientation(LinearLayout.VERTICAL);
        String search_str = ((EditText) findViewById(R.id.SearchString)).getText().toString();
        String[] split_search_str = search_str.split("\\s+");
        int length = linear.getChildCount();
        View v;
        for (int i = 0; i < length; i++) {
            v = linear.getChildAt(i);
            String str = (String) ((Button) v).getText();
            if (containsAll(split_search_str,str))
                v.setVisibility(View.VISIBLE);
            else
                v.setVisibility(View.GONE);
        }
        // no need to commit changes manually
        // TODO: Delete debug time calculation - PART 2 OF 2
        ShowShortToast(ShowText.CalculationQueryTime(System.currentTimeMillis() - msTime));
        // Shows how long initialization actually occurred
    }

    //
    private void AssignActionToButton(final Button btn, final String name, final String email) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RadioButton option_cpy = (RadioButton) (findViewById(R.id.radio_copy));
                //RadioButton option_email = (RadioButton) (findViewById(R.id.radio_toemail));
                boolean checked_copyToClipboard = ((RadioButton) (findViewById(R.id.radio_copy))).isChecked();

                // case of copying to clipboard
                if (checked_copyToClipboard) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("E-mail", email);
                    clipboard.setPrimaryClip(clip);
                    ShowShortToast(ShowText.copied_lecturer(name));
                }
                // case of sending mail via default user's email application
                else {
                    String chooserTitle = "E-mail Title";
                    String title = "שליחת מייל למרצה " + name;

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    startActivity(Intent.createChooser(emailIntent, title));

                    Snackbar.make(view, ShowText.attempt_email, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }
            }
        });
    }

    //
    public void ShowShortToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }


    //////////////////////////////////////////////////////////////
    //                   NOT-USED Function                      //
    //////////////////////////////////////////////////////////////

    public void OLDsearchListenerFunction() {
        String str = ((EditText) findViewById(R.id.SearchString)).getText().toString();
        long msTime = System.currentTimeMillis();


        ContactList_filter = new ArrayList<Contact>();
        // Coping filtered contacts to ContactList_filter
        for (int i = 0; i < ContactList.size(); i++)
            if (ContactList.get(i).contains(str))
                ContactList_filter.add(ContactList.get(i));

        if (ContactList_filter.isEmpty())
            ShowShortToast(ShowText.no_result);
        else OLDUpdateScrollView();

        // TODO: Delete debug time calculation - PART 2 OF 2
        ShowShortToast(ShowText.CalculationQueryTime(System.currentTimeMillis() - msTime));
        // Shows how long initialization actually occurred

    }

    // Function gets filtered contact list, and show it on ScrollView
    private void OLDUpdateScrollView() {
        ScrollView scroll = (ScrollView) (findViewById(R.id.ScrollV));
        LinearLayout linear = (LinearLayout) (findViewById(R.id.LinearV));
        linear.setOrientation(LinearLayout.VERTICAL);
        // Clean the linear before filling it with new information
        linear.removeAllViews();

        for (int i = 0; i < ContactList_filter.size(); i++) {
            Button t = new Button(this);
            t.setTransformationMethod(null);
            t.setText(ContactList_filter.get(i).toString());
/*           Tags for later developments
            t.setTag(0,ContactList_filter.get(i).name);
            t.setTag(1,ContactList_filter.get(i).email); */

// This func is needed, android doesn't allow passing dynamic information, but via another function you can
            AssignActionToButton(t, ContactList_filter.get(i).name, ContactList_filter.get(i).email);
            linear.addView(t);
        }
        // no need to commit changes manually
    }

    //  Parse XML contact file to array list
    private ArrayList<Contact> parse() throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();

        AssetManager assetManager = getAssets();
        parser.setInput(getAssets().open("martzim.xml"), null);

        ArrayList<Contact> Contacts = null;
        int eventType = parser.getEventType();
        Contact con = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Contacts = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("record"))
                        con = new Contact();
                    else if (con != null) {
                        if (name.equals("name"))
                            con.name = parser.nextText();
                        else if (name.equals("email"))
                            con.email = parser.nextText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("record") && con != null)
                        Contacts.add(con);
            }
            eventType = parser.next();
        }

        return Contacts;

    }
}


