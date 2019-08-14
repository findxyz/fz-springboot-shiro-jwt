package xyz.fz.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import xyz.fz.model.Account;
import xyz.fz.service.AccountService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockAccountServiceImpl implements AccountService, InitializingBean {

    private List<Account> mockAccounts = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/mock-accounts.json")) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            mockAccounts = objectMapper.readValue(
                    content,
                    new TypeReference<List<Account>>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String name, String password) {
        for (Account mockAccount : mockAccounts) {
            if (mockAccount.getName().equals(name) && mockAccount.getPassword().equals(password)) {
                return mockAccount;
            }
        }
        return null;
    }

    public Account getAccount(long id) {
        for (Account mockAccount : mockAccounts) {
            if (mockAccount.getId() == id) {
                return mockAccount;
            }
        }
        return null;
    }
}
