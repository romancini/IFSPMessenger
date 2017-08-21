package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.app.Activity;
import android.os.Bundle;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments.EntrarFragment;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EntrarFragment entrarFragment = new EntrarFragment();
        entrarFragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.content_frame, entrarFragment).commit();
    }
}
