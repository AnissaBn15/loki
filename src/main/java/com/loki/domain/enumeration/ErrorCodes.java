package com.loki.domain.enumeration;

public enum ErrorCodes {
        ARTICLE_NOT_FOUND(1000),
        ARTICLE_NOT_VALID(1001),
        ARTICLE_ALREADY_IN_USE(1002);
        private int code;

        ErrorCodes(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

}
