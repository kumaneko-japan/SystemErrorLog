/*
 * 指定されたリソースが、システム内に存在しないことを通知するための例外クラスで、
 * 主に、ドメイン層のロジックで発生させる例外である。
 * RuntimeExceptionを継承しているため、デフォルトの動作として、トランザクションはロールバックされる。
 */
package syserrLog.commonException;

import syserrLog.message.Result;
import syserrLog.message.ResultMessage;

public class ResourceException extends NoticeException {

    public ResourceException(String message) {
        super(Result.error().add(ResultMessage.fromText(message)));
    }

    //メッセージを指定するためのコンストラクタ
    public ResourceException(Result messages) {
        super(messages);
    }

    //メッセージと例外を指定するためのコンストラクタ
    public ResourceException(Result messages, Throwable cause) {
        super(messages, cause);
    }

}
