package bniws.dao;

import bniws.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Widya on 12/27/2017.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    Account findByNoHp(String msisdn);
    Account findByAccNo(String accNo);
}
