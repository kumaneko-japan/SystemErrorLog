package syserrLog.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultMessage implements Serializable {

	/*
	 * ①『ClassNotFoundException』
	 * アプリケーションが、クラスの文字列名を使用して次のメソッドでロードしようとしたが、
	 * 指定された名前のクラスの定義が見つからなかった場合にスローされる。
	 *
	 * ②『IOException』
	 * なんらかの入出力例外の発生を通知するシグナルを発生させる。
	 * 入出力処理の失敗、または割り込みの発生によって生成される例外の汎用クラス。
	 */

	//空の配列
    private static final Object[] EMPTY_ARRAY = new Object[0];

    //メッセージコード
    private final String code;

    //メッセージ引数
    private final Object[] args;

    //メッセージテキスト
    private final String text;

    //コンストラクタ
    public ResultMessage(String code, Object[] args, String text) {
        this.code = code;
        this.args = args == null ? EMPTY_ARRAY : args;
        this.text = text;
    }

    //コンストラクタ・・・Nullチェック
    public static ResultMessage fromCode(String code, Object... args) {
        Assert.notNull(code, "code must not be null");
        return new ResultMessage(code, args, null);
    }

    //コンストラクタ・・・Nullチェック
    public static ResultMessage fromText(String text) {
        Assert.notNull(text, "text must not be null");
        return new ResultMessage(null, EMPTY_ARRAY, text);
    }

    //ハッシュコードを返す
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(args);
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    //引数がResultMessageの現在のインスタンスと等しいかチェックする
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResultMessage other = (ResultMessage) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (!Arrays.equals(args, other.args)) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    //コードとテキストを出力する
    @Override
    public String toString() {
        return "ResultMessage [code=" + code + ", args=" + Arrays.toString(args)
                + ", text=" + text + "]";
    }

    //シリアライズ及びデシリアライズプロセスの特別な処理
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    //シリアライズ及びデシリアライズプロセスの特別な処理
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
