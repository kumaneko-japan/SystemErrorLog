/*
 * システムまたはアプリケーションの異常を検知したことを通知するための例外クラスで、
 * アプリケーション層、またはドメイン層のロジックで発生させる例外である。
 * RuntimeExceptionを継承しているため、デフォルトの動作としてはトランザクションはロールバックされる。
 */
package syserrLog.commonException;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException implements CodeRetention {

	/*
	 * SystemExceptionはファイル、ディレクトリ、マスターデータなど存在しなければならないものが
	 * 存在しないときにthrowされるException
	 */

	//例外コード
    private final String code;

    //表示されるメッセージおよび、例外の原因を指定できる
    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    //表示するメッセージを指定できる
    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }

    //例外の原因を指定できる
    public SystemException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

}
