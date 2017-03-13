package cf.castellon.turistorre.pruebas;

import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;

public class ActivityChatJava extends AppCompatActivity {
    @Bind(R.id.tv1Chat) TextView salidaTxt;
    @Bind(R.id.tv2Chat) TextView contador;
    @Bind(R.id.etChat) EditText texto;
    @Bind(R.id.btChat) Button boton;
    Socket sk;
    DataInputStream entrada;
    DataOutputStream salida;
    String msg;
    String ip ="192.168.43.91";
    int puerto =9831;
    final Handler myHandler = new Handler();
    static int val = 0;
    static int cont = 0;

    public DataInputStream getEntrada(){
        return entrada;
    }

    public void setEntrada(DataInputStream entrada) {
        this.entrada = entrada;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_java);
        ButterKnife.bind(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Log.v(this.getClass().getClass().getCanonicalName(),"Socket "+ip+" "+puerto);

        try {
            sk = new Socket(ip,puerto);
            Log.v(getClass().getCanonicalName(),"Se ha iniciado conexion.");
            Log("Conexion con servidor: "+ip+ " puerto: "+puerto);
            entrada = new DataInputStream(new BufferedInputStream(sk.getInputStream()));
            salida = new DataOutputStream(new BufferedOutputStream(sk.getOutputStream()));
        } catch (NetworkOnMainThreadException e) {
            Log.e("Error",e.getMessage());
        }

        catch (UnknownHostException e) {
            Log.e("Error",e.getMessage());
        }
        catch (IOException e) {
            Log.e("Error",e.getMessage());
        }

        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String recibe;
                    try {
                        recibe = getEntrada().readUTF();
                        if (recibe!=null){
                            ActivityChatJava.this.setMsg(recibe);
                            Log.v(getClass().getCanonicalName(),"Recibiendo datos.. "+recibe);

                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            actualizarMensajes();
                        }
                    });
                }
            }
        })).start();

    }

    private void actualizarMensajes() {
        this.Log(getMsg());
    }

    private void Log(String dato) {
        Log.v("Enviando a log valor: "+val,dato);
        salidaTxt.append(dato+"\n");
        val = val + 1;
    }

    @OnClick(R.id.btChat)
    public void onClick(View v) {
        Log.v(getClass().getCanonicalName(),"Iniciando Socket... ");
        Log("Enviando ..."+texto.getText().toString()+ "npi");
        ejecutaClienteSocket();
    }

    private void ejecutaClienteSocket() {
        try {
            Log.v(getClass().getCanonicalName(),"Enviando Datos..."+texto.getText().toString());
            salida.writeUTF(texto.getText().toString());
            salida.flush();

        } catch (Exception e){
            Log.e(getClass().getCanonicalName(),e.getMessage());
        }

    }


}

