package cf.castellon.turistorre.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.DiaFiestaMeta;
import cf.castellon.turistorre.bean.Evento;
import cf.castellon.turistorre.bean.Fiestas;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.GenerarTerrat;
import cf.castellon.turistorre.fragments.Click.GaleriaPagina;
import cf.castellon.turistorre.fragments.Principal.BandoRecyclerView;
import cf.castellon.turistorre.fragments.Principal.RaconsViewPager;
import cf.castellon.turistorre.fragments.Principal.TerratsRecyclerView;
import static cf.castellon.turistorre.utils.Constantes.*;

/**
 * Utiles sin necesidad de iniciar la clase (objeto)
 */
public final class Utils {
    public static ProgressDialog mProgressDialog;
    public static String seccion;
    public static MiAplicacion mApplication;
    public static SharedPreferences prefs;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    /* Client used to interact with Google APIs. */
    public static GoogleApiClient mGoogleApiClient;
    //Lista para actualizar los logins(enabled,disabled)
    public static List<Boolean> estados = new ArrayList<Boolean>(6);
    //Cuando tengamos todos los datos de la imagen (las 2 url i el id usuario) la guardamos en la bbdd
    public static boolean datosImagenOk;
    public static boolean colorCambiado;
    public static String tokenFireBase;
    public static Usuario usuario;
    public static Map<String, HashSet> baseDatos = new HashMap<>();
    public static String portadaRC, usuarioUidRC;
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static Map<String,Map<String,Object>> referenciasFire;
    public static Bitmap bitMapStatic;
    public static Imagen imagenStatic;



    /**
     * GALERIAFRAGMENT
     */
    //Variable que utilizaremos para seguir con la app si tenemos todos los permisos
    public static int numPermisos;

    /********************************************
     * FIESTAS
     *****************************************/

    public static long tiempoInicialWeb, tiempoDatosRecogidos, tiempoEspera;

    /** METODOS FIESTAS **/

    /**
     * @return la lista de los titulos de las fiestas
     */
    //Extrae los uids de FireBase del nodo fiestas(el uidFiestas)
    public static List<String> getTitulosFiestas() {
        List<String> titulos ;
        HashSet<Fiestas> fiestas;

        titulos = new ArrayList<>();
        fiestas = (HashSet) baseDatos.get(Tablas.Fiestas.name());
        for (Fiestas fiesta : fiestas){
                titulos.add(fiesta.getTitulo());
        }
        return titulos;
    }

    public static Date formatearDia(String tituloDiaFiesta, String key) {
        int dia;
        String mes, anyo, diat;
        String diaFormateado;
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd MMMM yyyy");
        Date fecha = null;
        anyo = key.substring(key.length() - 4);
        StringTokenizer tokens = new StringTokenizer(tituloDiaFiesta, " ");
        tokens.nextToken(); //Domingo
        diat = tokens.nextToken();
        tokens.nextToken(); //de
        mes = tokens.nextToken(); //enero
        diaFormateado = String.format("%s %s %d", diat, mes, Integer.parseInt(anyo));  //1 enero 2017
        try {
            fecha = formatoDelTexto.parse(diaFormateado);
        } catch (ParseException ex) {

            ex.printStackTrace();
        }
        return fecha;
    }

    /***
     * Recoger todos los uidsFiesta y para cada elemento del arrayList creado poner un uidDiaFiesta de la Fiesta
     * Luego tenemos que ver cual es la mas actual
     *
     * @return uidFiestaSeleccionada + reciente
     */
    public static String getUltimaFiesta() {
        Map<String, DiaFiestaMeta> diasFiestas;
        Iterator<String> itDiasFiestasMeta;
        SimpleDateFormat formatoDelTexto ;
        Map<String, Date> mapFiestaDia = new HashMap<>(); // "uidFiesta:tituloDiaFiestaFormateado"
        HashSet<Fiestas> fiestas;
        Date fechaMasReciente;
        String uidFiestaMasReciente;
        Iterator<String> itmapFiestaDia;

        formatoDelTexto = new SimpleDateFormat("dd MMMM yyyy");
        uidFiestaMasReciente ="";
        try {
            fechaMasReciente = formatoDelTexto.parse("15 Enero 1980");
        } catch (ParseException e) {
            fechaMasReciente = new Date();
            e.printStackTrace();
        }
        fiestas = (HashSet) baseDatos.get(Tablas.Fiestas.name());
        for (Fiestas fiesta : fiestas){
            diasFiestas = fiesta.getDiasFiestas();
            itDiasFiestasMeta = diasFiestas.keySet().iterator();
            while (itDiasFiestasMeta.hasNext()){
                String keyDia = itDiasFiestasMeta.next();
                DiaFiestaMeta diaFiestaMeta = diasFiestas.get(keyDia);
                mapFiestaDia.put(fiesta.getUidFiestas(), formatearDia(diaFiestaMeta.getTituloDiaFiesta(), fiesta.getUidFiestas()));
            }
        }

        itmapFiestaDia = mapFiestaDia.keySet().iterator();
        while (itmapFiestaDia.hasNext()) {
            String key = itmapFiestaDia.next();
            Date fechaI = mapFiestaDia.get(key);
            if (fechaI.after(fechaMasReciente)) {
                uidFiestaMasReciente = key;
                fechaMasReciente = fechaI;
            }
        }
        return uidFiestaMasReciente;
    }

    public static Usuario buscarUsuario(String uidUser) {
        Usuario usuario = null;
        boolean encontrado = false;
        HashSet<Usuario> usuarios;
        usuarios =  baseDatos.get(Tablas.Usuarios.name());
        Iterator<Usuario> iterator = usuarios.iterator();
        while (!encontrado) {
            usuario = iterator.next();
            if (usuario.getUidUser().equals(uidUser))
                encontrado = true;
        }
        return usuario;
    }

    public static Imagen buscarTerrat(String uidTerrat) {
        Imagen terrat = null;
        boolean encontrado = false;
        HashSet<Imagen> terrats;

        terrats =  baseDatos.get(Tablas.Terrats.name());
        Iterator<Imagen> iterator = terrats.iterator();
        while (!encontrado) {
            terrat = iterator.next();
            if (terrat.getUidImg().equals(uidTerrat))
                encontrado = true;
        }
        return terrat;
    }

    public static DiaFiesta buscarDiaFiesta(String uidDiaFiesta) {
        DiaFiesta diaFiesta = null;
        boolean encontrado = false;
        HashSet<DiaFiesta> diasFiesta;
        diasFiesta =  baseDatos.get(Tablas.DiasFiestas.name());
        Iterator<DiaFiesta> iterator = diasFiesta.iterator();
        while (!encontrado) {
            diaFiesta = iterator.next();
            if (diaFiesta.getUidDiaFiesta().equals(uidDiaFiesta))
                encontrado = true;
        }
        return diaFiesta;
    }

    public static Evento buscarEvento(DiaFiesta diaFiesta, String uidEvento) {
        Map<String, Evento> eventos;
        Evento evento;

        eventos = diaFiesta.getEventos();
        evento = eventos.get(uidEvento);
        return evento;
    }

    public static void showProgressDialog(Context context, String mensaje) {
        if (mProgressDialog != null){
            if (!mProgressDialog.isShowing()) {// aqui entramos sino hay actividad
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(mensaje + " ...");
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        }
        else { //null
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.cargando));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static HashSet<Imagen> buscarImagenes(String uidUser) {
        HashSet<Imagen> imagenes;
        HashSet<Imagen> imagenesUser;

        imagenesUser = new HashSet<>();
        imagenes = (HashSet) baseDatos.get(Tablas.Imagenes.name());
        Iterator<Imagen> iterador = imagenes.iterator();
        while (iterador.hasNext()) {
            Imagen imagen = iterador.next();
            if (imagen.getUidUser().equalsIgnoreCase(uidUser))
                imagenesUser.add(imagen);
        }
        return imagenesUser;
    }

    /**
     * LOGINFRAGMENT
     */
    public static FirebaseUser mFirebaseUser;
    public static FirebaseAuth mAuth;/* Datos desde el usuario autenticado*/

    /**
     * Al reves porque el ultimo proveedor en regisrarse estara al final y para eliminarlo no nos causara problemas el avatar
     *
     * @param providerId
     * @return
     */
    public static boolean isProvider(String providerId) {
        for (int i = mFirebaseUser.getProviderData().size() - 1; i > 0; i--) {
            UserInfo profile = mFirebaseUser.getProviderData().get(i);
            if (profile.getProviderId().equals(providerId))
                return true;
        }
        return false;
    }

    //  **************                  BANDOFRAGMENT **************************

    public static byte[] escalarImagen(Uri pathUri, float escalado) {
        byte[] data;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathUri.getPath());

        } catch (OutOfMemoryError er) { //Si la imagen es muy grande
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(pathUri.getPath(), options);
        } catch (Exception e) {
            Log.e("sds", e.getMessage());
        }
        int width = bitmap.getWidth(); //2340
        int height = bitmap.getHeight(); //4160
        Matrix matrix = new Matrix();
        /*if (height > width)
            matrix.postRotate(90);*/
        matrix.postScale(escalado, escalado);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            data = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (Exception e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    public static Uri getOutputMediaFile() {
        File mediaFile;
        File mediaStorageDir;
        String timeStamp;

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Turisme");
        mediaStorageDir.mkdirs();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

    /**
     * GLOBAL
     */

    public static String obtenerFecha() {
        int dia, diaSemana, mes, anyo;
        String nombreMes, nombreDiaSemana, salida;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        dia = cal.get(Calendar.DAY_OF_MONTH);
        diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        mes = cal.get(Calendar.MONTH);
        anyo = cal.get(Calendar.YEAR);

        cal.set(Calendar.MONTH, mes);
        nombreMes = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));
        nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1); //primera letra en mayuscula

        cal.set(Calendar.DAY_OF_WEEK, diaSemana);
        nombreDiaSemana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("es", "ES"));
        nombreDiaSemana = nombreDiaSemana.substring(0, 1).toUpperCase() + nombreDiaSemana.substring(1);

        salida = String.format("%s %d de %s del %d", nombreDiaSemana, dia, nombreMes, anyo);  //Domingo 27 de Noviembre del 2016
        return salida;
    }

    public static void cambiarColor(Context context, View view) {
        GradientDrawable gradient = ((GradientDrawable) view.getBackground());
        if (!colorCambiado)
            gradient.setColor(context.getResources().getColor(R.color.paper_succulents_rosa1));
        else
            gradient.setColor(context.getResources().getColor(R.color.paper_succulents_rosa2));
        colorCambiado = !colorCambiado;
    }

    public static void cambiarColorFondoTV(View view) {
        if (!colorCambiado)
            view.setBackgroundColor(view.getResources().getColor(R.color.spring_tones_verde));
        else
            view.setBackgroundColor(view.getResources().getColor(R.color.paper_succulents_gris3));
        colorCambiado = !colorCambiado;
    }

    /**
     * Como el server esta en heroku y tenenemos un dyno free lo tendremos que activar
     * sino no se envia la notifificacion
     * Esta opcion se quitará si algun dia pagamos para tener un dyno que no se duerma
     *
     * @param context
     */
    public static void activarServer(Context context) {
        RequestQueue requestQueue;
        StringRequest mStringRequest;

        requestQueue = Volley.newRequestQueue(context);
        mStringRequest = new StringRequest(Request.Method.GET, HEROKU_URL, null, null);
        requestQueue.add(mStringRequest);
    }

    public static void generarNotificacionBando(Context context, Imagen bando, String refBando) {
        FirebaseMessaging fm;
        Random random;

        random = new Random();
        fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(random.nextInt()))
                .addData("cabecera", context.getString(R.string.bando))
                .addData("titulo", bando.getTitulo())
                .addData("fecha", bando.getFecha())
                .addData("uidBando", refBando)
                .addData("action", ACTION_GPO_BANDO)
                .build());
    }

    public static void generarNotificacionAdminCambioGrupo(String grupo, Usuario user) {
        FirebaseMessaging fm;
        Random random;

        random = new Random();
        fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(random.nextInt()))
                .addData("grupoNuevo", grupo)
                .addData("grupoViejo", user.getGrupo())
                .addData("nombre", user.getNombre())
                .addData("uidUser", user.getUidUser())
                .addData("avatar", user.getAvatar())
                .addData("action", ACTION_CAMBIO_GRUPO)
                .build());
    }

    public static void generarNotificacionTerrat(String direccion, String uidTerrat) {
        FirebaseMessaging fm;
        Random random;

        random = new Random();
        fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(random.nextInt()))
                .addData("direccion", direccion)
                .addData("uidTerrat", uidTerrat)
                .addData("action", ACTION_GPO_TERRAT)
                .build());
    }

    public static void establecerPreferenciasIniciales(Context context) {
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        int notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
        numPermisos = prefs.getInt("numPermisos", 0);
        switch (notificacionesPrefs) {
            case TODAS:
                FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                break;
            case BANDOS:
                FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                break;
            case TERRATS:
                FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                break;
            case IMAGENES:
                FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                break;
            case NINGUNA:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Bando");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Terrat");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Imagenes");
                break;
        }
        activarServer(context);
    }

    public static List<String> eliminarGrupoActual(List<String> grupos, String grupoEliminar) {
        List<String> gpo = new ArrayList<>();
        if (!grupoEliminar.equals("administrador"))
            for (String grupo : grupos) {
                if (!grupoEliminar.equalsIgnoreCase(grupo))
                    gpo.add(grupo);
            }
        else
            gpo.add("Todos los permisos concedidos");
        return gpo;
    }

    public static void showError(Context context, String clase, String metodo, String descripcion) {
        Toast.makeText(context,"Clase: "+clase+"\nMétodo: "+metodo+"\nMotivo: "+descripcion,Toast.LENGTH_LONG).show();
        Log.e(TAG,"Clase: "+clase+"\nMétodo: "+metodo+"\nMotivo: "+descripcion);
    }

    public static void showToast(Context context, int descripcion) {
        Toast.makeText(context,context.getString(R.string.atencion) + " " + context.getString(descripcion),Toast.LENGTH_LONG).show();
    }

    public static void showWarning(Context context, int descripcion) {
        Toast.makeText(context,descripcion,Toast.LENGTH_LONG).show();
        Log.w(TAG,context.getString(descripcion));
    }

    public static void crearPaginasImagenes(Imagen imagenSeleccionada, HashSet<Imagen> imagenes, FragmentPageCarruselAdapter adaptador) {
        Bundle bund;
        Usuario usuario;
        GaleriaPagina pagina;

        bund = new Bundle();
        bund.putParcelable("imagen",imagenSeleccionada);
        usuario = buscarUsuario(imagenSeleccionada.getUidUser());
        bund.putParcelable("usuario",usuario);
        pagina = new GaleriaPagina();
        pagina.setArguments(bund);
        adaptador.addFragment(pagina);
        for (Imagen mImagen:imagenes){
            if (!mImagen.getUidImg().equals(imagenSeleccionada.getUidImg())) {
                bund = new Bundle();
                bund.putParcelable("imagen", mImagen);
                usuario = buscarUsuario(mImagen.getUidUser());
                bund.putParcelable("usuario", usuario);
                pagina = new GaleriaPagina();
                pagina.setArguments(bund);

                adaptador.addFragment(pagina);
            }
        }
    }

    public static void guardarFotoStorageFire(final Map<String, Object> tipoBean, final Context context,
                                              final FragmentManager fragmentManager,final @Nullable String titulo, final @Nullable String descripcion) {
        StorageReference mStorageRefPre;
        StorageReference mStorageRef;
        StorageReference rootStorage;
        final DatabaseReference rootDataBase;
        final Uri uri;
        final Activity activity;
        final String uidUser;

        uidUser = prefs.getString("uidUser","");

        activity = (Activity)context;
        rootStorage = (StorageReference) tipoBean.get("Storage");
        rootDataBase = (DatabaseReference) tipoBean.get("DataBase");
        uri = (Uri) tipoBean.get("fileUri");
        datosImagenOk=false;
        mStorageRef = rootStorage.child(mFirebaseUser.getDisplayName()+" - "+uidUser).child(uri.getLastPathSegment());
        String pathPre = uri.getLastPathSegment();
        pathPre = pathPre.substring(0,pathPre.length()-5);
        pathPre = pathPre.concat("_PRE.jpg");
        mStorageRefPre = rootStorage.child(mFirebaseUser.getDisplayName()+" - "+uidUser).child(pathPre);
        showProgressDialog(context,context.getString(R.string.dataUpload));
        mStorageRefPre.putBytes(escalarImagen(uri,1/8f)) //Pre
                .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (datosImagenOk) {
                            imagenStatic.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            guardarFotoBBDDFire(rootDataBase,imagenStatic,fragmentManager,context,activity);
                            datosImagenOk = false;
                       }
                        else{
                            imagenStatic = new Imagen();
                            imagenStatic.setUidUser(uidUser);
                            imagenStatic.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            imagenStatic.setUidImg(uri.getLastPathSegment().substring(0,uri.getLastPathSegment().length()-4));
                            imagenStatic.setTitulo("");
                            imagenStatic.setDescripcion("");
                            imagenStatic.setFecha(obtenerFecha());
                            if (titulo!=null)
                                imagenStatic.setTitulo(titulo);  // Si es terrat,raco o bando el nombre del terrat / raco
                            if (descripcion!=null)
                                imagenStatic.setDescripcion(descripcion);  // Si es raco o bando su descripcion
                            datosImagenOk =true;
                        }
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showError(context,getClass().getName(), exception.getStackTrace()[0].getMethodName(),exception.toString());
                        hideProgressDialog();
                    }
                });
        mStorageRef.putBytes(escalarImagen(uri,1))
                .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        hideProgressDialog();
                        if (datosImagenOk) {
                                imagenStatic.setUriStr(taskSnapshot.getDownloadUrl().toString());
                                guardarFotoBBDDFire(rootDataBase,imagenStatic,fragmentManager, context, activity);
                                datosImagenOk = false;
                        } else {
                            imagenStatic = new Imagen();
                            imagenStatic.setUidUser(uidUser);
                            imagenStatic.setUidImg(uri.getLastPathSegment().substring(0,uri.getLastPathSegment().length()-4));
                            if (taskSnapshot.getDownloadUrl()==null)
                                showError(context,getClass().getName(),"mStorageRef.onSuccess","taskSnapshot.getDownloadUrl()==null");
                            else {
                                imagenStatic.setUriStr(taskSnapshot.getDownloadUrl().toString());
                                imagenStatic.setTitulo("");
                                imagenStatic.setDescripcion("");
                                imagenStatic.setFecha(obtenerFecha());
                                if (titulo!=null)
                                    imagenStatic.setTitulo(titulo);  // Si es terrat,raco o bando el nombre del terrat / raco
                                if (descripcion!=null)
                                    imagenStatic.setDescripcion(descripcion);  // Si es raco o bando su descripcion
                                datosImagenOk = true;
                            }
                        }
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showError(context,getClass().getName(), exception.getStackTrace()[0].getMethodName(),exception.toString());
                        hideProgressDialog();
                    }
                });
    }

    public static void guardarFotoBBDDFire(final DatabaseReference rootRef, final Imagen imagen, final FragmentManager fragmentManager, final Context context, Activity activity) {
        final DatabaseReference dbRef;

        dbRef = rootRef.child(imagen.getUidImg());
        dbRef.setValue(imagen).addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String tabla;
                FragmentTransaction fragmentTransaction;
                hideProgressDialog();
                showToast(context,R.string.imgUploadOk);
                tabla= parsearKeyATabla(rootRef.getKey());
                switch (tabla) {
                    case "Terrats":
                        generarNotificacionTerrat(imagen.getTitulo(), imagen.getUidImg());
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new TerratsRecyclerView()).commit();
                        break;
                    case "Racons":
                        anyadirRaco(imagen);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new RaconsViewPager()).commit();
                        break;
                    case "Bandos":
                        generarNotificacionBando(context, imagen,dbRef.getKey());
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new BandoRecyclerView()).commit();
                        break;
                    case "DiasFiestas" :
//                            anyadirImagenDiaFiesta(imagen);
                        break;
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        FragmentTransaction fragmentTransaction;
                        if (exception.getMessage().equals("Firebase Database error: Permission denied"))
                            showToast(context, R.string.adminUpload);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new RaconsViewPager()).commit();
                        hideProgressDialog();
                    }
                });
    }

    public static void anyadirImagenDiaFiesta(Imagen imagen,String uidDiaFiesta, String uidEvento) {
        HashSet<DiaFiesta> diasFiestaHash;
        DiaFiesta diaFiesta;
        Evento evento;
        Map<String,Imagen> imagenes;

        diasFiestaHash = baseDatos.get(Tablas.DiasFiestas.name());
        diaFiesta = buscarDiaFiesta(uidDiaFiesta);
        evento = buscarEvento(diaFiesta,uidEvento);
        //Una vez guardada la imagen guardamos toda su estructura
        evento.addImagen(imagen);
        diaFiesta.addEvento(evento);
        diasFiestaHash.add(diaFiesta);
        baseDatos.put(Tablas.DiasFiestas.name(),diasFiestaHash);
    }

    private static String parsearKeyATabla(String key) {
        String tabla;
        if (key.equals("Terrats") || key.equals("Racons") || key.equals("Terrats") || key.equals("Bandos"))
            tabla=key;
        else
            tabla = "DiasFiestas";
        return tabla;
    }

    public static String obtenerPath (Uri uri,Context context) {
        int columnIndex;
        Cursor cursor;
        String strOut=null;

        String[] rutaColumna = {MediaStore.Images.Media.DATA};
        cursor = context.getContentResolver().query(uri, rutaColumna, null, null, null);
        if (cursor == null)
            showError(context, GenerarTerrat.class.getName(), "onActivityResult", "cursor == null");
        else {
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            strOut = cursor.getString(columnIndex);
            cursor.close();
        }
        return strOut;
    }

    public static Bitmap formatearBitmapfromPath (String path){
        Bitmap bitmap;
        int width, height;
        Matrix matrix;
        Bitmap rotatedBitmap;

        bitmap = BitmapFactory.decodeFile(path);
        width = bitmap.getWidth(); //2340
        height = bitmap.getHeight(); //4160
        matrix = new Matrix();
        if (height > width)
            matrix.postRotate(90);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return rotatedBitmap;
    }

    public static void establecerEstructurasIniciales(){
        Map<String,Object> referenciaImagenes;
        Map<String,Object> referenciaRacons;
        Map<String,Object> referenciaTerrats;
        Map<String,Object> referenciaBandos;
        Map<String,Object> referenciaFiestas;
        Map<String,Object> referenciaDiasFiestas;

        referenciasFire= new HashMap<>();
        referenciaDiasFiestas= new HashMap<>();
        referenciaImagenes= new HashMap<>();
        referenciaRacons = new HashMap<>();
        referenciaTerrats= new HashMap<>();
        referenciaBandos= new HashMap<>();
        referenciaFiestas= new HashMap<>();

        referenciaDiasFiestas.put("DataBase",mDataBaseDiasFiestaRef);
        referenciaDiasFiestas.put("Storage",mStorageDiasFiestasRef);
        referenciaDiasFiestas.put("fileUri",null);
        referenciaDiasFiestas.put("uidDiaFiesta",null);
        referenciaDiasFiestas.put("uidEvento",null);
        referenciaFiestas.put("DataBase",mDataBaseFiestasRef);
        referenciaFiestas.put("Storage",mStorageFiestasRef);
        referenciaFiestas.put("fileUri",null);
        referenciaImagenes.put("DataBase",mDataBaseImgRef);
        referenciaImagenes.put("Storage",mStorageImgRef);
        referenciaImagenes.put("fileUri",null);
        referenciaRacons.put("DataBase",mDataBaseRacoRef);
        referenciaRacons.put("Storage",mStorageRaconsRef);
        referenciaRacons.put("fileUri",null);
        referenciaTerrats.put("DataBase",mDataBaseTerratRef);
        referenciaTerrats.put("Storage",mStorageTerratsRef);
        referenciaTerrats.put("fileUri",null);
        referenciaBandos.put("DataBase",mDataBaseBandoRef);
        referenciaBandos.put("Storage",mStorageBandoRef);
        referenciaBandos.put("fileUri",null);

        referenciasFire.put(Tablas.DiasFiestas.name(),referenciaDiasFiestas);
        referenciasFire.put(Tablas.Fiestas.name(),referenciaFiestas);
        referenciasFire.put(Tablas.Imagenes.name(),referenciaImagenes);
        referenciasFire.put(Tablas.Racons.name(),referenciaRacons);
        referenciasFire.put(Tablas.Terrats.name(),referenciaTerrats);
        referenciasFire.put(Tablas.Bandos.name(),referenciaBandos);

    }

    /** Anyade una imagen a la base de datos local
     *
     * @param imagen La imagen a anyadir
     */
    public static void anyadirImagen(Imagen imagen){
        HashSet<Imagen> imagenes;

        imagenes = baseDatos.get(Tablas.Imagenes.name());
        imagenes.add(imagen);
        baseDatos.put(Tablas.Imagenes.name(),imagenes);
    }

    /** Anyade una imagen y su usuario a la base de datos local
     *
     * @param imagen La imagen a anyadir
     */
    public static void anyadirRaco(Imagen imagen){
        HashSet<Imagen> imagenes;
        HashSet<Usuario> usuarios;
        Usuario usuario;

        usuario = buscarUsuario(imagen.getUidUser());
        imagenes = baseDatos.get(Tablas.Racons.name());
        usuarios = baseDatos.get(Tablas.Usuarios.name());
        imagenes.add(imagen);
        usuarios.add(usuario);
        baseDatos.put(Tablas.Racons.name(),imagenes);
        baseDatos.put(Tablas.Usuarios.name(),usuarios);
    }

    public static Map<String,Object> parsearParametrosRFire(String tabla) {
        StorageReference storageReference;
        DatabaseReference databaseReference;
        Map<String,Object> referenciaFire;
        String [] params;
        String tablaOk,uidDiaFiesta,uidEvento;

        referenciaFire = new HashMap<>();
        establecerEstructurasIniciales(); //Limpiamos valores sobreescritos
        if (tabla !=null)
            if (!tabla.equals(Tablas.Bandos.name()) && !tabla.equals(Tablas.Imagenes.name()) && !tabla.equals(Tablas.Terrats.name())  && !tabla.equals(Tablas.Racons.name())) {
                ////Tabla = "DiasFiestas + uidDiaFiesta + uidEvento"
                params = tabla.split(" ");
                tablaOk = params[0];
                uidDiaFiesta = params[1];
                //uidEvento = "09:00 uidEvento1"
                uidEvento = params[2] + " " + params[3];
                referenciaFire = referenciasFire.get(tablaOk);
                storageReference = (StorageReference) referenciaFire.get("Storage");
                databaseReference = (DatabaseReference) referenciaFire.get("DataBase");
                databaseReference = databaseReference.child(uidDiaFiesta).child("eventos").child(uidEvento).child("imagenes");
                storageReference = storageReference.child(uidDiaFiesta).child(uidEvento);
                referenciaFire.put("Storage", storageReference);
                referenciaFire.put("DataBase", databaseReference);
                referenciasFire.put(tablaOk, referenciaFire);
            }
            else
                referenciaFire = referenciasFire.get(tabla);
            return referenciaFire;
    }

    public static String parsearTabla(String tabla) {
        String [] params;
        String tablaOk=null;
        if (tabla!=null)
            if (!tabla.equals(Tablas.Bandos) && !tabla.equals(Tablas.Imagenes)) {
                ////Tabla = "DiasFiestas + uidDiaFiesta + uidEvento"
                params = tabla.split(" ");
                tablaOk = params[0];
            }
            else
                tablaOk = tabla;
        return tablaOk;
    }

    public static String parserId(String userId) {
        userId = userId.replace("@","");
        userId = userId.replace(".","");
        return userId;
    }

    public static void loaders() {
        final HashSet<Usuario> usuarios;
        final HashSet<Imagen> terrats;
        final HashSet<Imagen> racons;

        usuarios = new HashSet<>();
        terrats = new HashSet<>();
        racons = new HashSet<>();
        mDataBaseUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = postSnapshot.getValue(Usuario.class);
                    usuarios.add(usuario);
                }
                baseDatos.put(Tablas.Usuarios.name(),usuarios);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDataBaseTerratRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagen terrat = postSnapshot.getValue(Imagen.class);
                    terrats.add(terrat);
                }
                baseDatos.put(Tablas.Terrats.name(),terrats);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDataBaseRacoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagen raco = postSnapshot.getValue(Imagen.class);
                    racons.add(raco);
                }
                baseDatos.put(Tablas.Racons.name(),racons);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}