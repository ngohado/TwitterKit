package com.hado.twitterkit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "oPvIId5mxlpG8mwfdKjuEPRlC";
    private static final String TWITTER_SECRET = "OyKsYLDovLEELQ9nZehKSlid9xH24JhGFZlkRTzOu50ojpmD8A";
    TextView tvInformation;
    TwitterLoginButton btnLoginTwitter;
    Button btnShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTwitter();
        setContentView(R.layout.activity_main);
        initView();

        btnLoginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                getInfoUser(session);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = getImageResourceUri();
                TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this);
                try {
                    builder.text("Em a, chung ta khong nen quay lai voi nhau")
                            .url(new URL("https://www.youtube.com/watch?v=RowaBT-4UPM"))
                            .image(imageUri)
                            .show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String url = "http://twitter.com/share?text=Hado handsome&url=https://google.com&hashtags=depzai,totbung";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private Uri getLocalImageUri() {
        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hadohandsome.jpg";
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            return Uri.fromFile(imgFile);
        }
        return null;
    }

    //android.resource://[package-name]/[Resource-ID]
    private Uri getImageResourceUri() {
        return Uri.parse("android.resource://" + this.getPackageName() + "/" + R.mipmap.ic_launcher);
    }

    private void initTwitter() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
    }

    private void getInfoUser(TwitterSession session) {
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                tvInformation.setText(result.data.name);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    private void initView() {
        tvInformation = (TextView) findViewById(R.id.tv_information);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnLoginTwitter = (TwitterLoginButton) findViewById(R.id.btn_login_twitter);
        btnLoginTwitter.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        btnLoginTwitter.setText("");
        btnLoginTwitter.setBackgroundResource(R.drawable.icon_twiter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        btnLoginTwitter.onActivityResult(requestCode, resultCode, data);
    }
}


