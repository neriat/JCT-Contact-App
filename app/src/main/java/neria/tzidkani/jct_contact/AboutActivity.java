package neria.tzidkani.jct_contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton email = (FloatingActionButton) findViewById(R.id.email);
        FloatingActionButton GitHub = (FloatingActionButton) findViewById(R.id.github);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "neriat@gmail.com";
                String subject = "";
                String body = "";
                String chooserTitle = "E-mail Title";
                String title = "שליחת משוב";

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);

                startActivity(Intent.createChooser(emailIntent, title));

                Snackbar.make(view, ShowText.attempt_email, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        GitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ShowText.GitHub)));
                Snackbar.make(view, ShowText.attempts_browser, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
