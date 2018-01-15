package bniws.service;

import bniws.dao.AccountTrxRepository;
import bniws.domain.AccountTrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Widya on 1/2/2018.
 */
@Service
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    AccountTrxRepository dao;

    public List<AccountTrx> findByAccSource(String accSource){
        return dao.findByAccSourceOrderByIdDesc(accSource);
    }

    public AccountTrx findLastTransactionPending(String accSource, String accDest, double amount){
        List<AccountTrx> trxList =
                dao.findByAccSourceAndAccDestinationAndAmountAndStatusOrderByCreateDateDesc(accSource, accDest, amount
                        , "PENDING");
        AccountTrx lastTrx = null;
        if(trxList != null && trxList.size() > 0){
            lastTrx = trxList.get(0);
        }
        return lastTrx;
    }

    public AccountTrx findLastTransactionPending(String accSource, String accDest, int transactionNo){
        List<AccountTrx> trxList =
                dao.findByAccSourceAndAccDestinationAndIdAndStatusOrderByCreateDateDesc(accSource, accDest
                        , transactionNo, "PENDING");
        AccountTrx lastTrx = null;
        if(trxList != null && trxList.size() > 0){
            lastTrx = trxList.get(0);
        }
        return lastTrx;
    }

    public AccountTrx updateStatus(String accSource, String accDest, double amount, int id, String status) throws Exception{
        AccountTrx result = findLastTransactionPending(accSource, accDest, id);
        logger.info("accSource : "+accSource);
        logger.info("accDest : "+accDest);
        logger.info("amount : "+amount);
        if(result != null){
            logger.info("RESULT TRX NOT NULL : "+result.getId());
            result.setStatus(status);
            dao.save(result);
        }
        return result;
    }

    public AccountTrx saveOrUpdate(AccountTrx accountTrx){
        return dao.save(accountTrx);
    }
}
