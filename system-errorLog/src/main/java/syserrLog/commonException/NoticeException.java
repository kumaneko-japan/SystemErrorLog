/*
 * 結果メッセージを保持している例外であることを通知するための抽象例外クラスで、
 * 共通ライブラリでは、BusinessExceptionと、ResourceNotFoundExceptionが継承している。
 * RuntimeExceptionを継承しているため、デフォルトの動作としてはトランザクションはロールバックされる。
 * このクラスを継承すると、ResultMessagesLoggingInterceptorによって、WARNレベルのログが出力される。
 */
package syserrLog.commonException;

import lombok.Getter;
import syserrLog.message.Result;

@Getter
public abstract class NoticeException extends RuntimeException {

    private final Result result;

    //コンストラクタ(引数：1つ)
    protected NoticeException(Result messages) {
        this(messages, null);
    }

    //コンストラクタ(引数：2つ)
    public NoticeException(Result messages,Throwable cause) {
        super(cause);

        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }

        this.result = messages;
    }

    //メッセージを文字列形式で返す
    @Override
    public String getMessage() {
        return result.toString();
    }

}
