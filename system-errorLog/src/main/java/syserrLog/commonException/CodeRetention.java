/*
 * 例外コードを保持する役割があることを示すインターフェイスで、共通ライブラリでは、
 * SystemExceptionが実装している。
 * このクラスを実装した例外クラスを作成すると、共通ライブラリから提供している例外ハンドリング処理にて、
 * 例外で保持している例外コードでそのまま使われる。
 */
package syserrLog.commonException;

//例外コードを返す
public interface CodeRetention {
    String getCode();
}
