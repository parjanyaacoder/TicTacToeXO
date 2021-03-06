package com.s090.tictactoexo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class about extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Boolean b=intent.getBooleanExtra("Dark Mode", true);
        System.out.println("Bool "+b);
        themeUtils.onActivityCreateSetConditionTheme(this, b);
        setContentView(R.layout.activity_about);

        TextView title = (TextView) findViewById (R.id.title);
        String titleString = getString(R.string.app_name) + " " +getString(R.string.app_version);
        title.setText(titleString);
        TextView github =(TextView) findViewById(R.id.github);
        TextView mail_me = (TextView) findViewById(R.id.mail_me);
        TextView tweeter = (TextView) findViewById(R.id.tweet);
        tweeter.setMovementMethod(LinkMovementMethod.getInstance()); // to set links
        mail_me.setMovementMethod(LinkMovementMethod.getInstance());
        github.setMovementMethod(LinkMovementMethod.getInstance());
        LinearLayout about_info=(LinearLayout) findViewById(R.id.about_info);
        about_info.setTranslationY(1500f);
        about_info.setAlpha(0f);
        about_info.animate().alpha(1f).translationYBy(-1500f).setDuration(1200);
    }
}
