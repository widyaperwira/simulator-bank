package bniws.dao;

import bniws.domain.AccountTrx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Asus on 1/2/2018.
 */
@Repository
public interface AccountTrxRepository extends JpaRepository<AccountTrx, Long>{
    List<AccountTrx> findByAccSourceOrderByIdDesc(String accSource);

//    @Query(value = "SELECT t FROM AccountTrx t WHERE t.accSource = :accSource and t.accDestination = :accDest and t.amount = :amount order by t.createDate DESC")
//    List<AccountTrx> findByAccSourceAndAccDestinationAndAmountOrderByCreatedDateDesc(@Param("accSource")String accSource, @Param("accDest")String accDest, @Param("amount")double amount);

    List<AccountTrx> findByAccSourceAndAccDestinationAndAmountAndStatusOrderByCreateDateDesc(String accSource,
                         String accDest, double amount, String status);
    List<AccountTrx> findByAccSourceAndAccDestinationAndIdAndStatusOrderByCreateDateDesc(String accSource,
                        String accDest, int id, String status);
}
