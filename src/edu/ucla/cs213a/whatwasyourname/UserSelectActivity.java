package edu.ucla.cs213a.whatwasyourname;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class UserSelectActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_select, menu);
        return true;
    }
    
    public void onClickButton (View v)
    {
        int id = v.getId ();
        switch (id) {
         case R.id.userButton1 :
        	   MainActivity.subject1 = true;
               break;
          case R.id.userButton2 :
        	   MainActivity.subject1 = false;
               break;
        }
        finish();
    }
}
