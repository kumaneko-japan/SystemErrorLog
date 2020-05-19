package syserrLog.message;

import lombok.Getter;

@Getter
public enum StandardMessage implements ResultMessageType {

    SUCCESS("success"),

    INFO("info"),

    @Deprecated WARN("warn"),
    WARNING("warning"),

    ERROR("error"),

    DANGER("danger");

    private final String type;

    private StandardMessage(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
