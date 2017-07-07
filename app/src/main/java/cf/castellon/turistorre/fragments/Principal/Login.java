
package cf.castellon.turistorre.fragments.Principal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static cf.castellon.turistorre.utils.Utils.*;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.*;

import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.Permisos;
import cf.castellon.turistorre.utils.MiAplicacion;

@SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
public class Login extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private ProgressDialog mAuthProgressDialog;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    Editor editor;
    @BindView(R.id.rlGoogle)
    RelativeLayout layoutGoogle;
    @BindView(R.id.rlFacebook)
    RelativeLayout layoutFacebook;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.sign_conectar_f)
    LoginButton btnFacebook;
    private CallbackManager mFacebookCallbackManager;
    @BindView(R.id.btn_desconectar_g)
    Button signoutG;
    @BindView(R.id.sign_conectar_g)
    SignInButton mGoogleLoginButton;
    private Activity mActivity;
    private AccessTokenTracker accessTokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = getActivity();
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mApplication = (MiAplicacion) mActivity.getApplicationContext();
        //[2.- Facebook Callback, cuando cambie el acceso entramos]
        mFacebookCallbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                onFacebookAccessTokenChange(currentAccessToken);
            }  //ARRIBA: Cuando detectamos que nos hemos conectado recogemos ese evento y se lo pasamos a Firebase para que gestione el registro
        };
        mAuthProgressDialog = new ProgressDialog(mActivity);
        mAuthProgressDialog.setTitle(R.string.cargando);
        mAuthProgressDialog.setMessage(getString(R.string.antenticando));
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();
        //  Setup the Google API object to allow Google+ logins
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_web_id_str))
                .requestEmail()
                .build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                String email, rand;
                Editor et;
                Random random;
                String uidUser;

                mAuthProgressDialog.hide();
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    for (UserInfo profile : mFirebaseUser.getProviderData()){
                        email = profile.getEmail();
                        if (email == null) {
                            email = prefs.getString("emailNulo", "");
                            if (email.isEmpty()) {
                                random = new Random();
                                rand = Integer.toString(random.nextInt());
                                email = "mail" + rand;
                                et = prefs.edit();
                                et.putString("emailNulo", email);
                                et.apply();
                            }
                        }
                        uidUser = prefs.getString("uidUser","");
                        if (uidUser.isEmpty())
                            crearUsuarioBBDDFire(email, mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(), mFirebaseUser.getPhotoUrl().toString(), "multimedia");
                    }
                    setAuthenticatedUser(mFirebaseUser);
                } else {
                    updateUI(Tipo_Proveedor.NATIVO_INICIAL);
                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseUser != null) {
            mGoogleApiClient.disconnect();
        }
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
        mAuthProgressDialog.dismiss();
    }

    @Override
    public void onStart() {
        if (mFirebaseUser != null) //&& mFirebaseUser.getProviderId().equalsIgnoreCase("google.com"))
            mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_FACEBOOK) { //
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);
            }
        } else
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mAuthProgressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                            showError(getContext(), getClass().getName(), task.getException().getStackTrace()[0].getMethodName(), task.getException().getMessage());
                        mAuthProgressDialog.hide();
                    }
                });
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {  //Cuando valimos en Facebook, hacemos el registro en Firebase, cuando este en Fire saltamos a AuthResultHandler.onAuthenticated*//*
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuthProgressDialog.show();
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                                showError(getContext(), getClass().getName(), task.getException().getStackTrace()[0].getMethodName(), task.getException().getMessage());
                            mAuthProgressDialog.hide();
                        }
                    });
        } else
            logout("facebook.com");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.login_layout, container, false);
        ButterKnife.bind(this, view);
        btnFacebook.setReadPermissions("email");
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        if (mFirebaseUser != null) {
            if (isProvider("google.com"))
                updateUI(Tipo_Proveedor.DESCONECTAR_GOOGLE);
        } else
            updateUI(Tipo_Proveedor.NATIVO_INICIAL);
        return view;
    }

    private void setAuthenticatedUser(FirebaseUser user) {
        if (user != null)
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals("google.com"))
                    updateUI(Tipo_Proveedor.DESCONECTAR_GOOGLE);
                else if (profile.getProviderId().equals("facebook.com"))
                    updateUI(Tipo_Proveedor.DESCONECTAR_FACEBOOK);
            }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(mActivity)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick({R.id.btn_desconectar_g})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_desconectar_g:
                logout("google.com");
                break;
        }
    }

    private void logout(final String proveedor) {
        mDataBaseGruposRef.child("Multimedia").child(mFirebaseUser.getUid()).setValue(null);
        mFirebaseUser.delete();
        FirebaseAuth.getInstance().signOut();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        editor = prefs.edit();
        editor.putString("uidUser", "");
        editor.apply();
        usuario = null;
        switch (proveedor) {
            case "facebook.com":
                LoginManager.getInstance().logOut();
                updateUI(Tipo_Proveedor.CONECTAR_FACEBOOK);
                break;
            case "google.com":
                updateUI(Tipo_Proveedor.CONECTAR_GOOGLE);
                mGoogleApiClient.disconnect();
                break;
        }
    }

    private void updateUI(Tipo_Proveedor proveedor) {
        switch (proveedor) {
            case NATIVO_INICIAL:
                layoutGoogle.setVisibility(View.VISIBLE);
                layoutFacebook.setVisibility(View.VISIBLE);
                break;
            case DESCONECTAR_FACEBOOK:  //Ya estamos conectados
                layoutGoogle.setVisibility(View.GONE);
                break;
            case DESCONECTAR_GOOGLE:
                mGoogleLoginButton.setVisibility(View.GONE);
                signoutG.setVisibility(View.VISIBLE);
                layoutFacebook.setVisibility(View.GONE);
                break;
            case CONECTAR_GOOGLE:
                layoutFacebook.setVisibility(View.VISIBLE);
                mGoogleLoginButton.setVisibility(View.VISIBLE);
                signoutG.setVisibility(View.GONE);
                break;
            case CONECTAR_FACEBOOK:
                layoutGoogle.setVisibility(View.VISIBLE);
                break;
        }
        updateAvatar();
    }

    private void updateAvatar() {
        String url = "";
        if (mFirebaseUser != null) {
            for (UserInfo profile : mFirebaseUser.getProviderData())
                if (profile.getProviderId().equals("facebook.com") || profile.getProviderId().equals("google.com"))
                    url = profile.getPhotoUrl().toString();
            Glide.with(mActivity).load(url).into(ivAvatar);
        } else
            Glide.with(mActivity).load(R.drawable.escudo).into(ivAvatar);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        showError(getContext(), getClass().getName(), result.getClass().getName(), result.getErrorMessage());
    }

    private void crearUsuarioBBDDFire(final String userId, final String nombre, final String email, final String avatar, final String grupo) {
        final String uidUser;

        uidUser = parserId(userId);
        mAuthProgressDialog.show();//No me aparece el dialogo
        editor = prefs.edit();
        editor.putString("uidUser", uidUser);
        editor.apply();

        if (nombre.equalsIgnoreCase("TurisTorre Turistorre") || nombre.equalsIgnoreCase("TurisTorre Admin")) {
            usuario = new Usuario(nombre, avatar, email, uidUser, "administrador");
            FirebaseMessaging.getInstance().subscribeToTopic("administrador");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("userBandos", true);
            editor.apply();
        } else
            usuario = new Usuario(nombre, avatar, email, uidUser, grupo);
        mDataBaseUsersRef.child(uidUser).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuthProgressDialog.hide();
                loguearFabric(usuario);
                mDataBaseGruposRef.child(usuario.getGrupo()).child(uidUser).setValue(usuario);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mAuthProgressDialog.hide();
                showErrorDialog(e.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_login, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String uidUser;
        Usuario user;
        switch (item.getItemId()) {
            case (R.id.it_permisos):
                FragmentTransaction transaccion = getFragmentManager().beginTransaction();
                Permisos permisos = new Permisos();
                Bundle bundle = new Bundle();
                if (mFirebaseUser != null) {
                    uidUser = prefs.getString("uidUser","");
                    user = buscarUsuario(uidUser);
                    bundle.putString("GRUPO", user.getGrupo());
                }
                else
                    bundle.putString("GRUPO", "anonimo");
                permisos.setArguments(bundle);
                transaccion.replace(R.id.content_frame, permisos).commit();
                transaccion.addToBackStack(null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
