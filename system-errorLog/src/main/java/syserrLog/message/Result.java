package syserrLog.message;

import static syserrLog.message.StandardMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Getter;

@Getter
public class Result implements Serializable, Iterable<ResultMessage> {

	/*
	 * ①『ClassNotFoundException』
	 * アプリケーションが、クラスの文字列名を使用して次のメソッドでロードしようとしたが、
	 * 指定された名前のクラスの定義が見つからなかった場合にスローされる。
	 *
	 * ②『IOException』
	 * なんらかの入出力例外の発生を通知するシグナルを発生させる。
	 * 入出力処理の失敗、または割り込みの発生によって生成される例外の汎用クラス。
	 *
	 * ③『IllegalArgumentException』
	 * 不正な引数、または不適切な引数をメソッドに渡したことを示すためにスローされる。
	 */

	//メッセージタイプ
    private final ResultMessageType type;

    //メッセージ一覧
    private final List<ResultMessage> list = new ArrayList<ResultMessage>();

    //デフォルトの属性名
    public static final String DEFAULT_MESSAGES_ATTRIBUTE_NAME = StringUtils
            .uncapitalize(Result.class.getSimpleName());

    public Result(ResultMessageType type) {
        this(type, (ResultMessage[]) null);
    }

    public Result(ResultMessageType type, ResultMessage... messages) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null!");
        }
        this.type = type;
        if (messages != null) {
            addAll(messages);
        }
    }

    //ResultMessageを追加
    public Result add(ResultMessage message) {
        if (message != null) {
            this.list.add(message);
        } else {
            throw new IllegalArgumentException("message must not be null");
        }
        return this;
    }

    //ResultMessagesを作成および追加するためのコードを追加
    public Result add(String code) {
        if (code != null) {
            this.add(ResultMessage.fromCode(code));
        } else {
            throw new IllegalArgumentException("code must not be null");
        }
        return this;
    }

    //ResultMessagesを作成および追加するためのコードと引数を追加
    public Result add(String code, Object... args) {
        if (code != null) {
            this.add(ResultMessage.fromCode(code, args));
        } else {
            throw new IllegalArgumentException("code must not be null");
        }
        return this;
    }

    //すべてのメッセージを追加する
    public Result addAll(ResultMessage... messages) {
        if (messages != null) {
            for (ResultMessage message : messages) {
                add(message);
            }
        } else {
            throw new IllegalArgumentException("messages must not be null");
        }
        return this;
    }

    //すべてのメッセージを追加する
//    public CreateResult addAll(Collection<ResultMessage> messages) {
//        if (messages != null) {
//            for (ResultMessage message : messages) {
//                add(message);
//            }
//        } else {
//            throw new IllegalArgumentException("messages must not be null");
//        }
//        return this;
//    }

    //メッセージが空でないかを返す
    public boolean isNotEmpty() {
        return !list.isEmpty();
    }

    //成功メッセージのファクトリメソッド
    public static Result success() {
        return new Result(SUCCESS);
    }

    //情報メッセージのファクトリメソッド
    public static Result info() {
        return new Result(INFO);
    }

    //警告メッセージのファクトリメソッド
    @Deprecated
    public static Result warn() {
        return new Result(WARN);
    }

    //警告メッセージのファクトリメソッド
    public static Result warning() {
        return new Result(WARNING);
    }

    //エラーメッセージのファクトリメソッド
    public static Result error() {
        return new Result(ERROR);
    }

    //危険メッセージのファクトリメソッド
    public static Result danger() {
        return new Result(DANGER);
    }

    @Override
    public Iterator<ResultMessage> iterator() {
        return list.iterator();
    }

    //メッセージのタイプとメッセージのリストを出力
    @Override
    public String toString() {
        return "ResultMessages [type=" + type + ", list=" + list + "]";
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