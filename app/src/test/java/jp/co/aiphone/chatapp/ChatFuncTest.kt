package jp.co.aiphone.chatapp

import jp.co.aiphone.chatapp.chatfunc.ChatFunc
import jp.co.aiphone.chatapp.chatfunc.IChatListener
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class ChatFuncTest {

    private lateinit var listener : IChatListener

    @Test
    fun loginTestPassNullCheck() {
        // ログイン処理
        val sMail = "test@test.com"
        val sPass = null
        listener = object : IChatListener{
            override fun notify(isLoginSuccess: Boolean) {
                assertThat(isLoginSuccess,`is`(false))
            }

            override fun update(sMessage: String){
                // 何もしない
            }
        }
        ChatFunc.setListener(listener)
        ChatFunc.login(sMail, sPass)
    }
}