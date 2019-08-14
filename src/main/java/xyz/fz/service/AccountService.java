package xyz.fz.service;

import xyz.fz.model.Account;

public interface AccountService {
    Account getAccount(String name, String password);

    Account getAccount(long id);
}
