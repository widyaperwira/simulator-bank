package bniws.dto;

/**
 * Created by Widya on 12/27/2017.
 */
public class AccountDTO {

    //Account Source
    private int id;
    private String name;
    private String accNo;
    private String msisdn;
    private double balance;

    //Account Destination
    private String accNoDest;
    private String nameDest;
    private double amount;

    private int pinIndex1;
    private int pinIndex2;
    private String verifyPin;

    private String transactionNo;
    private String paymentStatus;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccNoDest() {
        return accNoDest;
    }

    public void setAccNoDest(String accNoDest) {
        this.accNoDest = accNoDest;
    }

    public int getPinIndex1() {
        return pinIndex1;
    }

    public void setPinIndex1(int pinIndex1) {
        this.pinIndex1 = pinIndex1;
    }

    public int getPinIndex2() {
        return pinIndex2;
    }

    public void setPinIndex2(int pinIndex2) {
        this.pinIndex2 = pinIndex2;
    }

    public String getVerifyPin() {
        return verifyPin;
    }

    public void setVerifyPin(String verifyPin) {
        this.verifyPin = verifyPin;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNameDest() {
        return nameDest;
    }

    public void setNameDest(String nameDest) {
        this.nameDest = nameDest;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
}
