package com.example.solobeast.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.solobeast.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MailContactAct extends AppCompatActivity {
    TextView emailTitleTv,emailBodyTv;
    FloatingActionButton submitMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_contact);
        init();

        submitMail.setOnClickListener(view -> {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"ofekalm100@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailTitleTv.getEditableText().toString()); //title
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBodyTv.getEditableText().toString()); // description
            startActivity(emailIntent);
        });
    }

    private void init(){
        submitMail = findViewById(R.id.fab_contact);
        emailTitleTv = findViewById(R.id.title_contact_et);
        emailBodyTv = findViewById(R.id.task_desc_et);
    }
}