package tv.v1x1.common.scanners.config;

/**
 * @author Naomi
 */
public enum ConfigType {
    INTEGER, STRING, CREDENTIAL, TWITCH_OAUTH, MASTER_ENABLE, BOOLEAN, BOT_NAME, STRING_LIST, STRING_MAP, COMPLEX(true),
    COMPLEX_LIST(true), COMPLEX_STRING_MAP(true), PERMISSION, FILE, CHANNEL;

    private final boolean complex;

    ConfigType(final boolean complex) {
        this.complex = complex;
    }

    ConfigType() {
        this.complex = false;
    }

    public boolean isComplex() {
        return complex;
    }
}
