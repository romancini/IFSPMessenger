package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments.ContatosFragment;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments.ListaConversasFragment;

public class PrincipalActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView drawerList;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ContatosFragment contatosFragment;
    private ListaConversasFragment listaConversasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listaConversasFragment = new ListaConversasFragment();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.lista_menu)));
        drawerList.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                null,
                R.string.abrir_drawer,
                R.string.fechar_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                listaConversasFragment).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        contatosFragment = new ContatosFragment();
        listaConversasFragment = new ListaConversasFragment();

        switch (position){
            // contatos
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        contatosFragment).commit();
                break;

            // conversas
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        listaConversasFragment).commit();
                break;

            default:
                break;
        }
        drawerLayout.closeDrawers();
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        else
            return super.onOptionsItemSelected(item);
    }
}
