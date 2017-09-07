package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments.EntrarFragment;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.services.ContatoService;

public class LoginActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private Intent contatoServicoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contatoServicoIntent = new Intent(getApplicationContext(), ContatoService.class);
        startService(contatoServicoIntent);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_frame, new EntrarFragment());
        fragmentTransaction.commit();
    }
}
