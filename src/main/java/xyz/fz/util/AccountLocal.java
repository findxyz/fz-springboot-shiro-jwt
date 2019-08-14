package xyz.fz.util;

import xyz.fz.model.Account;
import xyz.fz.configuration.shiro.realm.JwtData;

public class AccountLocal {

    private static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public static AccountInfo getAccountInfo() {
        return (AccountInfo) threadLocal.get();
    }

    public static void setAccountInfo(AccountInfo accountInfo) {
        threadLocal.set(accountInfo);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static class AccountInfo {
        private JwtData jwtData;

        private Account account;

        public AccountInfo(JwtData jwtData, Account account) {
            this.jwtData = jwtData;
            this.account = account;
        }

        public JwtData getJwtData() {
            return jwtData;
        }

        public void setJwtData(JwtData jwtData) {
            this.jwtData = jwtData;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }
}
