/*
 * ExceptionLevelResolverの実装クラスで、例外コードの先頭1文字で、例外レベルを解決している。
 * 先頭の1文字目が、
 * 『i』の場合は、ExceptionLevel.INFO
 * 『w』の場合は、ExceptionLevel.WARN
 * 『e』の場合は、ExceptionLevel.ERROR
 * 上記以外の場合は、ExceptionLevel.ERROR レベルとして扱う。
 */
package syserrLog.commonException;

import lombok.Setter;

@Setter
public class FirstCharacterResolver implements ExceptionLevelResolver {

    private ExceptionCodeResolver exceptionCodeResolver;

//    public FirstCharacterResolver() {
//    }

    public FirstCharacterResolver(
            ExceptionCodeResolver exceptionCodeResolver) {
        setExceptionCodeResolver(exceptionCodeResolver);
    }

    @Override
    public ExceptionLevel resolveExceptionLevel(Exception ex) {
        String exceptionCode = resolveExceptionCode(ex);
        if (exceptionCode == null || exceptionCode.isEmpty()) {
            return ExceptionLevel.ERROR;
        }
        String exceptionCodePrefix = exceptionCode.substring(0, 1);
        if ("e".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.ERROR;
        }
        if ("w".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.WARN;
        }
        if ("i".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.INFO;
        }
        return ExceptionLevel.ERROR;
    }

    protected String resolveExceptionCode(Exception ex) {
        String exceptionCode = null;
        if (exceptionCodeResolver != null) {
            exceptionCode = exceptionCodeResolver.resolveExceptionCode(ex);
        }
        return exceptionCode;
    }

}
