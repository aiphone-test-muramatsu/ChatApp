package jp.co.aiphone.chatapp

import android.os.Build
import android.widget.Button
import com.google.firebase.FirebaseApp
import jp.co.aiphone.chatapp.chatfunc.ChatFunc
import jp.co.aiphone.chatapp.chatfunc.IChatListener
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
// SDK=29はJava9が必要
// https://codechacha.com/ja/android-robolectric-unit-test/
// https://stackoverflow.com/questions/56808485/robolectric-and-android-sdk-29
@Config(sdk = [Build.VERSION_CODES.P])
class ChatFuncTestByRobolectric {

    // ロジックだけを使ってログインを確認する
    @Test
    fun loginTestByOnlyLogic_Sucess() {

        // Firebaseは初期化
        var context = RuntimeEnvironment.application
        FirebaseApp.initializeApp(context)

        // ログイン処理
        val sMail = "test@test.com"
        val sPass = "test12"
        val listener = object : IChatListener {
            override fun notify(isLoginSuccess: Boolean) {
                Assert.assertThat(isLoginSuccess, Is.`is`(true))
            }

            override fun update(sMessage: String){
                // 何もしない
            }
        }
        ChatFunc.setListener(listener)
        ChatFunc.login(sMail, sPass)

    }

    // ロジックだけを使ってログインを確認する
    @Test
    fun loginTestByOnlyLogic_Failure() {

        // Firebaseは初期化
        var context = RuntimeEnvironment.application
        FirebaseApp.initializeApp(context)

        // ログイン処理
        val sMail = "test@test.com"
        val sPass = "xxxxx"
        val listener = object : IChatListener {
            override fun notify(isLoginSuccess: Boolean) {
                Assert.assertThat(isLoginSuccess, Is.`is`(false))
            }

            override fun update(sMessage: String){
                // 何もしない
            }
        }
        ChatFunc.setListener(listener)
        ChatFunc.login(sMail, sPass)

    }

    // 画面を使ってログインを確認する
    @Ignore("今動かす必要なし")
    @Test
    fun loginTextByActivity() {
        val activity = Robolectric.setupActivity(MainActivity::class.java)
        val loginButton : Button = activity.buttonLogin
        // 起動確認
        Assert.assertThat(loginButton.text.toString(), Is.`is`("ログイン"))

        var context = RuntimeEnvironment.application
        FirebaseApp.initializeApp(context)

        // ログインを実施
        activity.textMail.setText("test@test.com")
        activity.textPass.setText("test12")
        loginButton.performClick()

        //Thread.sleep(7000)

        Assert.assertThat(activity.isDestroyed, Is.`is`(true))

    }
}