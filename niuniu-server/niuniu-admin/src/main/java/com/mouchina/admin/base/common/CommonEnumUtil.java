package com.mouchina.admin.base.common;

/**
 * Created by douzy on 15/10/21.
 */
public class CommonEnumUtil {
    /**
     * 退款
     */
    public enum Reimburse {
        /**
         * 初始
         */
        INIT {
            public byte getState() {
                return 1;
            }
        },
        /**
         * 退款成功
         */
        SUCCESS {
            public byte getState() {
                return 2;
            }
        },
        /**
         * 退款失败
         */
        FAIL {
            public byte getState() {
                return 3;
            }
        },
        /**
         * 退款中
         */
        ISREFOUND {
            public byte getState() {
                return 4;
            }
        },
        /**
         * 退款异常
         */
        ERROR {
            public byte getState() {
                return 5;
            }
        };

        public abstract byte getState();
    }
    /**
     * 提现
     */
    public enum Withdrawals {
        /**
         * 申请
         */
        INIT {
            public byte getState() {
                return 1;
            }
        },
        /**
         * 完成
         */
        SUCCESS {
            public byte getState() {
                return 2;
            }
        },
        /**
         * 失败
         */
        FAIL {
            public byte getState() {
                return 3;
            }
        },
        /**
         * 提现中
         */
        ISWITH {
            public byte getState() {
                return 4;
            }
        };

        public abstract byte getState();
    }
}
