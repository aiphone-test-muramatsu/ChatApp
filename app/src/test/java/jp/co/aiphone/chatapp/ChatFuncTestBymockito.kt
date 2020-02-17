package jp.co.aiphone.chatapp

import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import jp.co.aiphone.chatapp.chatfunc.ChatFunc
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test
import java.io.IOException

// 参考
// https://qiita.com/tea_tree_tree/items/43c9b2c32f48324fbca6

class ChatFuncTestBymockito {

    // モッククラス生成
    // モックの元クラスはfinalにしてはいけない。Kotlinはデフォルトでfinalにするので
    // 下記のファイルを追加する
    // https://qiita.com/sudo5in5k/items/00ed3dbf9af309cf8aa9
    private val fireBaseMock = mock<ChatFunc> {
        //on { login("test@test.com","test12")}
        on { createAcount("test@test.com","test12") } doThrow RuntimeException("test")
    }

    @Test
    fun loginMockTest() {
        val sMail = "test@test.com"
        val sPass = "test12"
        // 何も起きない
        //fireBaseMock.login(sMail,sPass)


        var isThrow = false
        try {
            fireBaseMock.createAcount(sMail, sPass)
        } catch (e : RuntimeException) {
            isThrow = true
        }

        Assert.assertThat(isThrow, Is.`is`(true))

    }
}