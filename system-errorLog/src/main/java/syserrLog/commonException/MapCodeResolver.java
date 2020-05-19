/*
 * ExceptionCodeResolverの実装クラスで、例外クラスの名前と、
 * 例外コードのマッピングを保持することで、例外コードの解決を実現。
 */
package syserrLog.commonException;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;

@Setter
public class MapCodeResolver implements ExceptionCodeResolver {
	/*
	 * 例外コードを解決するためのクラス
	 */

	//ロガー
    private static final Logger logger = LoggerFactory.getLogger(MapCodeResolver.class);

    //例外コードと例外クラスの間のマッピングルール
    private LinkedHashMap<String, String> exceptionMappings;

    //デフォルトの例外コード
    private String defaultExceptionCode;

    //例外コードを解決する
    @Override
    public String resolveExceptionCode(Exception ex) {

        if (ex == null) {
            logger.warn(
                    "target exception is null. return defaultExceptionCode.");
            return defaultExceptionCode;
        }

        if (ex instanceof CodeRetention) {
            String code = ((CodeRetention) ex).getCode();
            if (code != null) {
                return code;
            }
        }

        if (exceptionMappings == null || exceptionMappings.isEmpty()) {
            return defaultExceptionCode;
        }

        for (Entry<String, String> entry : exceptionMappings.entrySet()) {
            String targetException = entry.getKey();
            Class<?> exceptionClass = ex.getClass();
            while (exceptionClass != Object.class) {
                if (exceptionClass.getName().contains(targetException)) {
                    return entry.getValue();
                }
                exceptionClass = exceptionClass.getSuperclass();
            }
        }

        return defaultExceptionCode;
    }

}
