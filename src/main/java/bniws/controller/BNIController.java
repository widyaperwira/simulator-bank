package bniws.controller;

import bniws.domain.Account;
import bniws.domain.AccountTrx;
import bniws.dto.AccountDTO;
import bniws.service.AccountService;
import bniws.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Widya on 12/27/2017.
 */
@RestController
@RequestMapping("/bniws")
public class BNIController {

    private final Logger logger = LoggerFactory.getLogger(BNIController.class);

    @Autowired
    AccountService service;

    @Autowired
    TransactionService trxService;

    @RequestMapping(value = "/checkAccount/{noHp}", method = RequestMethod.GET, produces = "application/json")
    public void checkAccountNumber(@PathVariable("noHp")String noHp ){
        logger.info("checkAccount");
        Account result = service.getByMsisdn(noHp);
        if(result!=null){
            Account temp = new Account();
            temp.setNoHp(noHp);
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.postForObject("http://staging.bayarind.id:8080/pay-gateway/pay-auth/auth/requestOtp/5mLS7h3bNpcxq9wuJz66v3dmJuC57uQW", result, Account.class);
            restTemplate.postForObject("http://localhost:8080/pay-gateway/pay-auth/auth/requestOtp/5mLS7h3bNpcxq9wuJz66v3dmJuC57uQW", temp, Account.class);
        }
    }

    @RequestMapping(value = "/checkSaldo", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> checkSaldo(@RequestBody AccountDTO dto){
        logger.info("checkSaldo");
        Account result = service.getByMsisdn(dto.getMsisdn());
        if(result!=null){
            return new ResponseEntity<Account>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<Account>(new Account(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> transfer(@RequestBody AccountDTO dto) {
        logger.info("transfer");
        Account accSource = service.getByMsisdn(dto.getMsisdn());
        Account accDest = service.getByAccNo(dto.getAccNoDest());

        if(accSource != null && accDest != null && accSource.getPin().equalsIgnoreCase(dto.getVerifyPin())){
            try{
                //save to table transaction status pending before confirmed by pin challenge
                AccountTrx accountTrx = new AccountTrx();
                accountTrx.setAccSource(accSource.getAccNo());
                accountTrx.setAccDestination(accDest.getAccNo());
                accountTrx.setAmount(dto.getAmount());
                accountTrx.setCreateDate(new Date());
                accountTrx.setModifiedDate(new Date());
                accountTrx.setName("TRANSFER");
                accountTrx.setStatus("PENDING");

                trxService.saveOrUpdate(accountTrx);

                //getId for transactionNo, and return it
                AccountTrx savedObj = null;
                List<AccountTrx> savedObjList = trxService.findByAccSource(accSource.getAccNo());
                if(savedObjList != null && savedObjList.size() > 0){
                    savedObj = savedObjList.get(0);
                }
                dto.setId(savedObj.getId());
            }catch(Exception e){
                e.printStackTrace();
            }

            //send confirmation page with random pin
            dto.setNameDest(accDest.getName());

            Random random = new Random();
            dto.setPinIndex1(random.nextInt(6));
            dto.setPinIndex2(random.nextInt(6));
            dto.setMessage("confirmation page, input pin challenge index number : "+dto.getPinIndex1()+" and "+dto.getPinIndex2());

            return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);

        } else {
            return new ResponseEntity<Account>(new Account(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/verifyPinChallenge", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> verifyPinChallenge(@RequestBody AccountDTO dto) {
        logger.info("verifyPinChallenge");
        Account accSource = service.getByMsisdn(dto.getMsisdn());
        Account accDest = service.getByAccNo(dto.getAccNoDest());
        if(accSource != null && accDest != null){
            AccountTrx lastTrx = trxService.findLastTransactionPending(accSource.getAccNo(), accDest.getAccNo()
                                                , dto.getId());
            if(lastTrx != null){
                try{
//                    String pin = accSource.getPin();
//                    String[] splitPin = pin.split("");
//                    String verifyPin = dto.getVerifyPin();
//                    String[] splitVerifyPin = verifyPin.split("");
//
//                    logger.info(splitPin[dto.getPinIndex1()]+" | "+splitVerifyPin[0]+" | "+splitPin[dto.getPinIndex2()]+" | "+splitVerifyPin[1]);
//
//                    if(splitPin[dto.getPinIndex1()].equalsIgnoreCase(splitVerifyPin[0])
//                            && splitPin[dto.getPinIndex2()].equalsIgnoreCase(splitVerifyPin[1])){
                        logger.info("PIN MATCHED!");
                        dto.setPaymentStatus("PAID");

                        //update balance source
                        service.updateBalance(accSource.getNoHp(), -dto.getAmount());

                        //update balance destination
                        service.updateBalance(accDest.getNoHp(), dto.getAmount());

                        //update status transaction
                        trxService.updateStatus(accSource.getAccNo(), accDest.getAccNo(), dto.getAmount(), dto.getId(), "PAID");

                        return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);
//                    } else {
//                        return new ResponseEntity<AccountDTO>(dto, HttpStatus.NOT_ACCEPTABLE);
//                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return new ResponseEntity<AccountDTO>(dto, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<AccountDTO>(new AccountDTO(), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<AccountDTO>(new AccountDTO(), HttpStatus.NOT_FOUND);
        }
    }

}
