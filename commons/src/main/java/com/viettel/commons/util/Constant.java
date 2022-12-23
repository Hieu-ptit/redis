package com.viettel.commons.util;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

public final class Constant {
    public static final String TOKEN_PRICE = "TOKEN_PRICE";
    public static final String GAME_SERVERS = "GAME_SERVERS";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String CONFIG = "CONFIG";
    public static final String UNBOXING_NODE_MAP = "UNBOXING_NODE_MAP";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_CLIENT_ID = "X-CLIENT-ID";
    public static final String X_CLIENT_SECRET = "X-CLIENT-SECRET";
    public static final int CASTLE_CHANNEL_ID = 1;
    public static final long BOX_REVISION = 1;
    public static final BigInteger WOFM_ID = BigInteger.ONE;
    public static final String X_AUTH_USER = "X-AUTH-USER";
    public static final String PREFIX_RESPONSE_CODE = "CASTLE-";
    public static final String X_GAME_ID = "X-GAME-ID";
    public static final String UPDATED = "UPDATED";
    public static final String UNBOXED = "UNBOXED";
    public static final String ID_TO_VALID_ACCOUNT = "ID_TO_VALID_ACCOUNT";
    public static final String ID_TO_PLAYER = "ID_TO_PLAYER";
    public static final String EMAIL_TO_OTP = "EMAIL_TO_OTP";
    public static final String FORGOT_PASSWORD_SUBJECT = "FORGOT_PASSWORD";
    public static final String TOKEN_REVOKED_MAP = "TOKEN_REVOKED";
    public static final int FAIR_CHANNEL_ID = 1;
    public static final int DEPOSIT_WORM_CHANNEL_ID = 2;
    public static final DateTimeFormatter DATE_TIME_FORMAT_TIMEZONE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String DEFAULT_SORT = "id:asc";
    public static final String PAYMENT_HISTORY_EXCEL_TEMPLATE = "Box_purchase_history.xlsx";
    public static final String MATCHING_HISTORY_EXCEL_TEMPLATE = "Item_purchase_history.xlsx";
    public static final String STAKING_HISTORY_EXCEL_TEMPLATE = "Staking_history.xlsx";
    public static final String REWARD_HISTORY_EXCEL_TEMPLATE = "Reward_receipt_history.xlsx";
    public static final String IMPORT_REWARD_EXCEL_TEMPLATE = "Import_reward.xlsx";
    public static final BigDecimal DECIMAL_AMOUNT_UNIT = BigDecimal.valueOf(1000_000_000_000_000_000L);
    public static final BigInteger INTEGER_AMOUNT_UNIT = BigInteger.valueOf(1000_000_000_000_000_000L);
    public static final int INT_INTEREST_RATE = 1000000;

    private Constant() {}

}
