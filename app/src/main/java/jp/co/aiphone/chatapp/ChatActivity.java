package jp.co.aiphone.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import jp.co.aiphone.chatapp.chatfunc.ChatFunc;
import jp.co.aiphone.chatapp.chatfunc.IChatListener;

/**
 *　チャット画面
 */
public class ChatActivity extends AppCompatActivity implements IChatListener {

    /* スレッドに投稿するメッセージの入力フィールド */
    private EditText m_textMessage;
    /* メッセージスレッド */
    private TextView m_textThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* メッセージ投稿ボタン */
        Button buttonPost;
        /* ログアウトボタン */
        Button buttonLogout;

        /* 各ウィジェットの初期化 */
        m_textMessage = findViewById(R.id.textMessage);
        m_textThread = findViewById(R.id.textThread);
        buttonPost = findViewById(R.id.buttonPost);
        buttonLogout = findViewById(R.id.buttonLogout);
        /* スクロールバーを動かせるようにする */
        m_textThread.setMovementMethod(new ScrollingMovementMethod());
        /* リスナーを追加 */
        ChatFunc.INSTANCE.setListener(this);
        /* 投稿ボタンがクリックされた時の処理 */
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ソフトキーボードを非表示にする
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(null != imm) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                String sMessage = m_textMessage.getText().toString();
                /* メッセージが未入力であれば何もせずに終了 */
                if(sMessage.equals("")) {
                    return;
                }
                /* チャットにメッセージを投稿 */
                ChatFunc.INSTANCE.post(sMessage);
                /* メッセージの入力フィールドを空にする */
                m_textMessage.setText("");
            }
        });
        /* ログアウトボタンがクリックされた時の処理 */
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* ログアウト処理 */
                ChatFunc.INSTANCE.logout();
                /* メイン画面に遷移 */
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void update(@NotNull final String sMessage) {
        /* スレッドにメッセージを表示 */
        m_textThread.append(sMessage);
        /* 画面を自動スクロール */
        final int scrollAmount = m_textThread.getLayout().getLineTop(m_textThread.getLineCount()) - m_textThread.getHeight();
        if (scrollAmount > 0) {
            m_textThread.scrollTo(0, scrollAmount);
        } else {
            m_textThread.scrollTo(0, 0);
        }
    }

    @Override
    public void notify(boolean isLoginSuccess) {
        /*　本クラスでは何もしない　*/
    }
}
