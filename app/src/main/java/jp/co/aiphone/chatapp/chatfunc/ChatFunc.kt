package jp.co.aiphone.chatapp.chatfunc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

/**
 * チャット機能クラス
 */
object ChatFunc {
    /* データベースのキー */
    private val KEY_CHAT = "message"
    /* IChatListenerのインスタンス */
    private var m_iChatListener: IChatListener? = null
    /* ログインに使用するメールアドレス */
    private var m_sMail: String? = null
    /* チャット番号 */
    private var m_iChatNo = 0
    /* 子要素が追加された時のリスナー */
    private var m_childListener: ChildEventListener? = null

    /**
     * アカウント作成処理。内部でFireabaseのAuthenticationサービスを使用している。
     * @param sMail アカウント作成に使用するメールアドレス
     * @param sPass アカウント作成に使用するパスワード
     */
    fun createAcount(sMail: String?, sPass: String?) {
        if (null == sMail) {
            /* アカウント作成失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
            return
        }
        if (null == sPass) {
            /* アカウント作成失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
            return
        }
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(sMail, sPass).addOnCompleteListener{
            if (it.isSuccessful) {
                /* アカウント作成成功結果を通知 */
                if (null != m_iChatListener) {
                    m_iChatListener!!.notify(true)
                }
                m_sMail = sMail
                return@addOnCompleteListener
            }
            /* アカウント作成失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
        }
    }

    /**
     * ログイン処理。内部でFireabaseのAuthenticationサービスを使用している。
     * @param sMail ログインに使用するメールアドレス
     * @param sPass ログインに使用するパスワード
     */
    fun login(sMail: String?, sPass: String?) {
        if (null == sMail) {
            /* ログイン失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
            return
        }
        if (null == sPass) {
            /* ログイン失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
            return
        }
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(sMail, sPass).addOnCompleteListener{
            if (it.isSuccessful) {
                /* ログイン成功結果を通知 */
                if (null != m_iChatListener) {
                    m_iChatListener!!.notify(true)
                }
                m_sMail = sMail
                return@addOnCompleteListener
            }
            /* ログイン失敗結果を通知 */
            if (null != m_iChatListener) {
                m_iChatListener!!.notify(false)
            }
        }
    }

    /**
     * ログアウト処理。内部でFireabaseのAuthenticationサービスを使用している。
     */
    fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        m_sMail = null
    }

    /**
     * メッセージを投稿する。内部でFireabaseのAuthenticationサービスおよびRealtime Databaseサービスを使用している。
     * @param sMessage 投稿するメッセージ
     */
    fun post(sMessage: String?) {
        if (null == m_sMail) {
            return
        }
        if (null == sMessage) {
            return
        }
        /* データベースを取得 */
        var myRef = FirebaseDatabase.getInstance().getReference(KEY_CHAT)
        /* データベースにテーブル追加 */
        myRef = myRef.child(m_iChatNo.toString())
        /* テーブルへ書き込み */
        myRef.setValue("$m_sMail:$sMessage\n")
    }

    /**
     * リスナーを設定する
     * @param iChatListener　設定するリスナー
     */
    fun setListener(iChatListener: IChatListener?) {
        if (null == iChatListener) {
            return
        }
        m_iChatListener = iChatListener
        if (null == m_sMail) {
            return
        }
        /* データベースを取得 */
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(KEY_CHAT)
        if (null != m_childListener) {
            /* 既存のリスナーを削除 */
            myRef.removeEventListener(m_childListener!!)
        }
        m_childListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (null != m_iChatListener) {
                    m_iChatListener!!.update(dataSnapshot.value as String)
                }
                m_iChatNo++
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        /* データベースにメッセージが追加された時に通知を行うためのリスナー設定 */
        myRef.addChildEventListener(m_childListener!!)
    }
}