package com.monstertechno.hiddendairies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.monstertechno.hiddendairies.authentication.ForgotPassword;
import com.monstertechno.hiddendairies.authentication.LoginActivity;

public class FormActivity extends Activity implements OnClickListener {

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText reciep, sub, msg,name_user,userAdress,storyTittle,storyFull;
    String rec, subject, textMessage,User_name,Answer_You,UserAddress,StoryTittle,StoryFull;
    Spinner answer_you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        context = this;

        Button login = (Button) findViewById(R.id.btn_submit);
        name_user = (EditText) findViewById(R.id.name);
        answer_you = (Spinner) findViewById(R.id.answer);
        userAdress = (EditText) findViewById(R.id.address);
        storyTittle = (EditText) findViewById(R.id.story_title);
        storyFull = (EditText) findViewById(R.id.story);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        rec = "thakurajaa@gmail.com";

        User_name = name_user.getText().toString();
        Answer_You = answer_you.getSelectedItem().toString();
        UserAddress = userAdress.getText().toString();
        StoryTittle = storyTittle.getText().toString();
        StoryFull = storyFull.getText().toString();

        subject = "Story Sending from "+User_name;

        textMessage = "Hi my name is "+User_name+". I am from "+UserAddress+".\n"+Answer_You+" revel my name or my story's characters."+"\nThe story Title is "+StoryTittle+"\n"+StoryFull;

        if(!TextUtils.isEmpty(User_name) && !TextUtils.isEmpty(UserAddress) && !TextUtils.isEmpty(StoryTittle) && !TextUtils.isEmpty(StoryFull)) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("hiddendiries@gmail.com", "9775545770");
                }
            });

            pdialog = ProgressDialog.show(context, "", "Submitting your Story...", true);

            RetreiveFeedTask task = new RetreiveFeedTask();
            task.execute();
        }else{
            Toast.makeText(FormActivity.this,"First fill all the fields",Toast.LENGTH_SHORT).show();
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("hiddendiries@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();

            opensubmitbox();
        }
    }

    private void opensubmitbox() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout= inflater.inflate(R.layout.submit_dialog,null);
        AlertDialog.Builder show = new AlertDialog.Builder(this);
        show.setView(alertLayout);
        show.setCancelable(false);
        AlertDialog dialog = show.create();
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}
