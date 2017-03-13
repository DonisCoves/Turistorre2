package cf.castellon.turistorre.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static cf.castellon.turistorre.utils.Utils.*;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.AdaptadorDrawerList;
import cf.castellon.turistorre.adaptadores.DatosDrawerList;
import cf.castellon.turistorre.fragments.AcercaFragment;
import cf.castellon.turistorre.fragments.AjustesFragment;
import cf.castellon.turistorre.fragments.FiestasFragment;
import cf.castellon.turistorre.fragments.GaleriaFragment;
import cf.castellon.turistorre.fragments.HomeFragment;
import cf.castellon.turistorre.fragments.LoginFragment;
import cf.castellon.turistorre.fragments.RaconsFragment;
import cf.castellon.turistorre.fragments.SelectorFragment;
import cf.castellon.turistorre.fragments.TrobamFragment;

/**
 * Clase donde juntaremos todos los fragments , será como el recipiente
 */


public class MainActivity extends Activity implements ListView.OnItemClickListener,SelectorFragment.OnListSeccionListener {
    /**
     * Es todo el objeto de la navegacion, que incluye el cajon "mDrawerList" y el frame donde
     * se incluirán los fragmentos(tambien se pueden meter activitys, pero con fragments el cambio es mas "armónico".
     */
    private DrawerLayout mDrawerLayout;
    /**
     * La lista que se ve a la izquierda el "drawer", "drawerlist" o el "cajón".
     */
    private ListView mDrawerList;
    /**
     * El fragmento que se insertará cuando pulsamos en la lista.
     */
    private Fragment frSeccion;


    /*necesario para mostrar los datos en la lista de la drawer: */
    /**
     * Clase que contiene la estructura de datos para la drawer list.
     */
    private DatosDrawerList datosDrawer;

    /**
     * Adaptador utilizado para la drawer list.
     */
    private AdaptadorDrawerList adaptadorDrawer;
    /**
     * Intent para saltat a una activity: en el perfil pej..
     */
    private Intent intent;
    /**
     * Para las interacciones entre el drawer y la action bar
     * Action Bar que tiene los eventos de cerrar y abrir el drawer
     * Esta Action Bar especifica se utiliza porque en el developers
     * dice que cuando la Action Bar está desplegada solo aparecerá
     * el icono y el título de la app, los items de acciones se desplazan
     * al desbordamiento.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Para trabajar con los fragmentos
     */

    private FragmentTransaction fragmentTransaction;

    /**
     * En la creación de la actividad hacemos:
     * 1.-Creamos un acction Bar (ActionBarDrawerToggle) el cual manejará los eventos entre
     * la drawer y nuestra action bar normal . Estos eventos son:
     * onDrawerOpened y onDrawerClosed.- Se abre/cierra el "cajón" con lo que , segun la doc. oficial,
     * deberán desaparecer los iconos de la action bar y el título de la action bar no será el del
     * módulo en el que estemos sino el nombre de la app (Showpping en nuestor caso)
     * (todo esto en onDrawerOpened en el onDrawerClose tendremos que mostrar los botones y el titulo
     * del módulo seleccionado).
     * En estos métodos tendremos que llamar a invalidateOptionsMenu en donde le indicaremos a Android
     * que queremos cambiar la action Bar. invalidateOptionsMenu llama a onPrepareOptionsMenu  y
     * es aquí donde comprobamos si el drawer está abierto o cerrado(ya que tambien podemos
     * entrar a onPrepareOptionsMenu desde el método de cierre del drawer onDrawerClosed). Si el
     * cajón está abierto pues lo dicho hacemos invisibles los botones de la action y
     * mostramos el nombre de la app
     * <p/>
     * 2.- Anulamos los eventos del Drawer,es decir, el drawer ya no manejará cuando se abre/cierra el cajón
     * sino que lo hará el ActionBarDrawerToggle, ¿por qué? Sino tuvieramos Action Bar no pasaría nada,
     * pero al tenerla tenemos que hacer lo de mostrar/ocultar los iconos y el título de la action bar.
     * Para ello se utiliza esta clase ActionBarDrawerToggle que tiene los eventos de apertura/cierre del
     * cajón. Entonces la clase fuente que se encarga de recoger los eventos físicos(la DrawerLayout)
     * cojerá los eventos del ActionBarDrawerToggle.
     * <p/>
     * 3.- Habilitamos el icono de la actionBar para hacer que se abre/esconda  getActionBar().setHomeButtonEnabled(true)
     * <p/>
     * 4.- Habilitamos la navegación hacia arriba del icono de la actionBar getActionBar().setDisplayHomeAsUpEnabled(true)
     * <p/>
     * 5.- Montamos el listview mDrawerList. Con un clase con una estructura de los datos, un xml como recurso
     * que contendrá el diseño de una fila de la lista fila_drawer_list.xml y un adaptador para unirlo todo
     * y luego pasarselo a la listview para que muestre los datos
     * <p/>
     * 6.-Mostramos el fragment inicial
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                ActionBar actio = getActionBar();

         seccion = "Home";
        frSeccion = new HomeFragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
       if (getFragmentManager().findFragmentById(R.id.frSelector)!=null){ //tablet
           fragmentTransaction.add(R.id.flMain, frSeccion).commit();
       }else {
           fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
           prepareDrawer();
           mDrawerLayout.setDrawerListener(mDrawerToggle);
       }


    }

    private void prepareDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Explicado en 1.-

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_navigation_drawer, R.string.open, R.string.close) {
            /*    En estos métodos tendremos que llamar a invalidateOptionsMenu en donde le indicaremos a Android
            *    que queremos cambiar la action Bar. invalidateOptionsMenu llama a onPrepareOptionsMenu  */
            @Override
            public void onDrawerOpened(View drawerView) {
                //mostramos el nombre de la app
                invalidateOptionsMenu();
                getActionBar().setTitle(getTitle());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                getActionBar().setTitle(seccion);
            }
        };
        //Explicado en 3.-
        //Enable or disable the "home" button in the corner of the action bar.
        //(Note that this is the application home/up affordance on the action bar, not the systemwide home button.)
        //This defaults the application should call this method to enable interaction with the home/up affordance.
        //Habilitamos la imagen como botón
        getActionBar().setHomeButtonEnabled(true);

        //Explicado en 4.-
        //Set whether home should be displayed as an "up" affordance.
        //Set this to true if selecting "home" returns up by a single level in your
        //UI rather than back to the top level or front page.
        //Habilitamos navegacion hacia arriba
        getActionBar().setDisplayHomeAsUpEnabled(true);

        adaptadorDrawer = new AdaptadorDrawerList(this);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //iconosDrawer.recycle();  //como no se ejecuta el garbaje tenemos que hacerlo nosotros
        mDrawerList.setAdapter(adaptadorDrawer);
        mDrawerList.setOnItemClickListener(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen;
        /*comprobamos si el drawer está abierto o cerrado(ya que tambien podemos
        *    entrar a onPrepareOptionsMenu desde el método de cierre del drawer onDrawerClosed). Si el
        *    cajón está abierto pues lo dicho hacemos invisibles los botones de la action
        *
        */
        if (getFragmentManager().findFragmentById(R.id.frSelector)==null) { //movil
            //deberán desaparecer los iconos de la action bar
            isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            if (isDrawerOpen) {
                menu.findItem(R.id.action_settings).setVisible(false);
            } else { //tendremos que mostrar los botones y el titulo
                //del módulo seleccionado).
                menu.findItem(R.id.action_settings).setVisible(true);
           /* if (mDrawerList.isSelected()) {
                getActionBar().setTitle(mDrawerList.getSelectedItem().toString());

            }*/
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        seccion = ((TextView) view.findViewById(R.id.tvTitulo)).getText().toString();
        switch (seccion) {
            case "Home":
                frSeccion = new HomeFragment();
                seccion="TurisTorre";
                break;
            case "Racons":
                frSeccion = new RaconsFragment();
                break;
            case "Galeria":
                frSeccion = new GaleriaFragment();
                break;
            case "Trobam":
                frSeccion = new TrobamFragment();
                break;
            case "Login":
                frSeccion = new LoginFragment();
                break;
            case "Fiestas":
                frSeccion = new FiestasFragment();
                break;
            case "Ajustes":
                frSeccion = new AjustesFragment();
                break;
            case "Acerca_de":
                frSeccion = new AcercaFragment();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        getActionBar().setTitle(seccion);
        mDrawerLayout.closeDrawer(mDrawerList);
        //fragmentTransaction.remove(getFragmentManager().getFragment());
        fragmentTransaction.replace(R.id.content_frame, frSeccion).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //	Si el ikono de nuestra barra de accion se ha pulsado devolvemos true
        // para indicar que el evento se ha procesado y que se encargar� el ActionBarDrawerToggle de procesarlo
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String seccionStr) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        switch (seccionStr) {
            case "Home":
                frSeccion = new HomeFragment();
                break;
            case "Racons":
                frSeccion = new RaconsFragment();
                break;
            case "Galeria":
                frSeccion = new GaleriaFragment();
                break;
            case "Trobam":
                frSeccion = new TrobamFragment();
                break;
            case "Login":
                frSeccion = new LoginFragment();
                break;
            case "Fiestas":
                frSeccion = new FiestasFragment();
                break;
            case "Ajustes":
                frSeccion = new AjustesFragment();
                break;
            case "Acerca_de":
                frSeccion = new AcercaFragment();
                break;
        }
        fragmentTransaction.replace(R.id.flMain, frSeccion).commit();
    }
}
