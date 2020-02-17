package jp.co.aiphone.chatapp.chatfunc

/**
 * ChatFuncで使用するのリスナー
 */
interface IChatListener {
    /**
     * チャットのメッセージが更新された際に呼ばれる
     * @param sMessage　更新メッセージ
     */
    fun update(sMessage: String)

    /**
     * ログインに成功したかどうかを通知する
     * @param isLoginSuccess true：ログイン成功 false：ログイン失敗
     */
    fun notify(isLoginSuccess: Boolean)
}
