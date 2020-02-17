package jp.co.aiphone.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.aiphone.chatapp.chatfunc.ChatFunc;
import jp.co.aiphone.chatapp.chatfunc.IChatListener;

/**
 *　メイン画面(ログインおよび新規アカウント作成画面)
 */
public class MainActivity extends AppCompatActivity implements IChatListener {

    /* アプリケーションコンテキスト */
    private Context m_context;
    /* メールアドレスの入力フィールド */
    private EditText m_textEmail;
    /* パスワードの入力フィールド */
    private EditText m_textPass;
    /* 処理中を表示するダイアログ */
    private ProgressDialog m_progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_context = this;
        m_textEmail = findViewById(R.id.textMail);
        m_textPass = findViewById(R.id.textPass);
        m_progressDialog= new ProgressDialog(this);
        m_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_progressDialog.setMessage("処理中");
        m_progressDialog.setCancelable(true);

        /* リスナーを追加 */
        ChatFunc.INSTANCE.setListener(this);

        /*設定値の保存*/
        SharedPreferences prefer = getSharedPreferences("設定値" , MODE_PRIVATE);
        m_textEmail.setText(prefer.getString("mail" , ""));
        m_textPass.setText(prefer.getString("pass" , ""));

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sMail = m_textEmail.getText().toString();
                String sPass = m_textPass.getText().toString();
                if(sMail.equals("") || sPass.equals("")) {
                    /* エラー情報をトースト表示 */
                    Toast.makeText(MainActivity.this, "メールアドレスまたはパスワードを確認してください",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                /* ログイン処理 */
                ChatFunc.INSTANCE.login(sMail , sPass);
                m_progressDialog.show();
            }
        });

        Button buttonNew = findViewById(R.id.buttonNew);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sMail = m_textEmail.getText().toString();
                String sPass = m_textPass.getText().toString();
                if(sMail.equals("") || sPass.equals("")) {
                    /* エラー情報をトースト表示 */
                    Toast.makeText(MainActivity.this, "メールアドレスまたはパスワードを確認してください",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                /* アカウント作成処理 */
                ChatFunc.INSTANCE.createAcount(sMail , sPass);
                m_progressDialog.show();
            }
        });
    }

    @Override
    public void update(String sMessage) {
        /*　本クラスでは何もしない　*/
    }

    @Override
    public void notify(boolean isLoginSuccess) {
        m_progressDialog.dismiss();
        if(isLoginSuccess) {
            /* 設定値の保存 */
            SharedPreferences prefer = getSharedPreferences("設定値" , MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putString("mail", m_textEmail.getText().toString());
            editor.putString("pass", m_textPass.getText().toString());
            editor.apply();
            /* チャット画面に遷移 */
            Intent intent = new Intent(m_context , ChatActivity.class);
            startActivity(intent);
            /* 本画面を終了 */
            finish();
        }
        else {
            /* エラー情報をトースト表示 */
            Toast.makeText(MainActivity.this, "メールアドレスまたはパスワードを確認してください",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
