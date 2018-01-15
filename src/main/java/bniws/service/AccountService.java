package bniws.service;

import bniws.dao.AccountRepository;
import bniws.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Widya on 12/27/2017.
 */
@Service
public class AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository dao;

    public Account getByMsisdn(String msisdn){
        return dao.findByNoHp(msisdn);
    }

    public void updateBalance(String msisdn, double balance){
        Account account = dao.findByNoHp(msisdn);
        if(account != null){
            logger.info("BNI ACCOUNT NOT NULL : "+account.getAccNo());
            account.setBalance(account.getBalance() + balance);
            dao.save(account);
        }
    }

    public Account getByAccNo(String accNo){
        return dao.findByAccNo(accNo);
    }

}
