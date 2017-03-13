package cf.castellon.turistorre.utils;

import android.Manifest;
import android.app.ProgressDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import cf.castellon.turistorre.R;


/**
 * Constantes que utilizaremos en el proyecto
 */

public final class Constantes {
    public static String TITULO ="titulo";
    public static String DESCRICION ="descricion";
    public static String AVATAR ="avatar";
    public static String PORTADA ="portada";
    private String FIREBASE_URL = "https://turistorre.firebaseio.com/";
    public static  String HEROKU_URL = "https://servertorre.herokuapp.com";
    private String FIREBASE_CHILD = "test";
    public static final int RC_GOOGLE_LOGIN = 1;
    public static final String AVATAR_PRUEBAS = "https://scontent.xx.fbcdn.net/v/t1.0-1/s100x100/254604_2241436965131_8343350_n.jpg?oh=c680e565516e8eb5ed6a34dbe7c40439&oe=580F26EE";
    public static final int RC_SIGN_IN = 9001;
    public static final int NINGUNA = 0;
    public static final int TODAS = 1;
    public static final int BANDOS = 2;
    public static final int TERRATS = 3;
    public static final int IMAGENES = 4;
    public static final int CATALAN = 0;
    public static final int CASTELLANO = 1;
    public static final int INGLES = 2;
    public static final int CUENTAS = 2;
    public static final int PERMISO_CAMARA= 3;
    public static final int PERMISO_ESCRIBIR_SD = 4;
    public static final int PERMISO_SUBIR_IMAGENES = 5; //Es el de camara y el de escribirsd
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE = 101;
    public static final int CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE = 102;
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /******************************FIREBASE******************************/
    //Almacenamiento
    public static final StorageReference mStorageRootRef = FirebaseStorage.getInstance().getReference();
    public static final StorageReference mStorageImgRef = mStorageRootRef.child("Fotos");
    public static final StorageReference mStorageBandoRef = mStorageRootRef.child("Bandos");
    public static final StorageReference mStorageTerratsRef = mStorageRootRef.child("Terrats");
    public static final StorageReference mStorageFiestasRef = mStorageRootRef.child("Fiestas");
    //Bases de datos en tiempo real
    public static final DatabaseReference mDataBaseRootRef = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference mDataBaseKeysRef = mDataBaseRootRef.child("keys");
    public static final DatabaseReference mDataBaseImgRef = mDataBaseRootRef.child("Imagenes");
    public static final DatabaseReference mDataBaseTerratsRef = mDataBaseRootRef.child("Terrats");
    public static final DatabaseReference mDataBaseBandoRef = mDataBaseRootRef.child("Bandos");
    public static final DatabaseReference mDataBaseUsersRef = mDataBaseRootRef.child("Usuarios");
    public static final DatabaseReference mDataBaseFiestasRef = mDataBaseRootRef.child("Fiestas");
    public static final DatabaseReference mDataBaseDiasFiestaRef = mDataBaseRootRef.child("DiasFiestas");
    public static final DatabaseReference mDataBaseGruposRef = mDataBaseRootRef.child("Grupos");

    public static final String TAG = "GaleriaFragment";
    //    MENSAJERÍA EN LA NUBE
    public static final String SERVER_KEY = "AIzaSyDxLyatD56aKsqjxjd_Gq5T6siaAw-oT6Q";
    public static final String ID_REMITENTE = "351834780446";  //fcmProjectSenderId o projectId o SENDER_ID
    //Parametros AUTENTICACION para CCS
    public static final String LOGIN_CCS = ID_REMITENTE+"@gcm.googleapis.com";
    // GENERAL PROYECTO
    public static final String API_WEB = "AIzaSyC36nGbjdP5Z6k9bWOgPpRa3SHPl9tdltI"; //fcmServerKey o apiKey
    public static final String PROJECT_ID = "project-1031372115432573568";
    //Token es el registration_id
    //Para grupos necesitaremos el OAUTH_CLIENT_ID
    public static final String OAUTH_CLIENT_ID= "351834780446-aq8f9hf28ckdbf00dgnkde14mnn7ljo9.apps.googleusercontent.com";
    public static final String SCOPE = "audience:server:client_id:"+OAUTH_CLIENT_ID;
    //NOTIFICACIONES
    public static final String ACTION_GPO_BANDO = "BANDO";
    public static final String ACTION_GPO_TERRAT = "TERRAT";
    public static final String ACTION_MAIN = "android.intent.action.MAIN";

    public static final Integer [] AVATARES = {
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5
    };
    public static final Integer [] PORTADAS = {
            R.drawable.home1,
            R.drawable.home2,
            R.drawable.home3,
            R.drawable.home4,
            R.drawable.home5
    };

    public static enum Tipo_Proveedor{
        NATIVO_REGISTRAR,
        NATIVO_DESCONECTAR,
        NATIVO_INICIAL,
        DESCONECTAR_FACEBOOK,
        DESCONECTAR_GOOGLE,
        CONECTAR_GOOGLE,
    };
}
