package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcom.api.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Estate t set t.delete_status = true WHERE t.estateid = :estateid")
    void findByEstateIdDelete(Integer estateid);

    @Transactional
    @Modifying
    @Query("UPDATE Estate t set t.delete_status = true WHERE t.user.userid = :userid")
    void findByUserIDDelete(Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE Estate t set t.savedevent = true WHERE t.estateid = :estateid")
    void findByEstateIdEvent(Integer estateid);

    @Transactional
    @Modifying
    @Query("UPDATE Estate t set t.savedevent = false WHERE t.estateid = :estateid")
    void findByEstateIdEventReverse(Integer estateid);

    @Query("from Estate e where (e.estateaddress =:estate_address and e.delete_status = false)")
    Estate findByEstateaddress(@Param("estate_address") String estate_address);

    @Query("from Estate e where (e.estateid =:estateid and e.delete_status = false)")
    Estate findByEstateid(@Param("estateid") Integer estateid);

    @Query("from Estate e where e.user.userid =:userid and e.delete_status = false")
    List<Estate> findAllByUserId(@Param("userid") Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE Estate t set t.estatename=:estatename, t.estateaddress =:estateaddress WHERE t.estateid = :estateid")
    void findByEstateAndUpdate(@Param("estateid") Integer estateid,
                               @Param("estatename") String estatename,
                               @Param("estateaddress") String estateaddress);

    @Modifying
    @Query(value = "insert into user_estates (estate_id,user_id) VALUES (:estate_id,:user_id)", nativeQuery = true)
    @Transactional
    void inviteUser(@Param("estate_id") Integer estate_id, @Param("user_id") Integer user_id);

    @Modifying
    @Query(value = "DELETE FROM `user_estates` WHERE `user_estates`.`estate_id` = ?1 AND `user_estates`.`user_id` = ?2", nativeQuery = true)
    @Transactional
    void removeUser(@Param("estate_id") Integer estate_id, @Param("user_id") Integer user_id);

    /*@Query("from Estate e where ?1 member of e.users")
    List<Estate> findAllByUsers(User user);*/
    @Query("from Estate e where e.user.userid =:userid and e.delete_status = false")
    Estate findByUserID(@Param("userid") Integer userid);
    // User findByConfirmationToken(String confirmationToken);
}