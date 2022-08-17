package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcom.api.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("from User e where (e.username = :username and e.enabled=true)")
    User findByUsername(@Param("username") String username);

    @Query("from User e where (e.email = :email and e.enabled=true)")
    User findByEmail(@Param("email") String email);

    /*  @Query("from Devices d where ?1 member of d.estate.users")
      List<Devices> findAllByMember(User user);*/
    @Query("from Devices  e where  e.estate.user.userid = :userid")
    Devices findAllByCurrentUsr(@Param("userid") Integer userid);

    User findByUserid(Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE User t set t.estatecount = t.estatecount + 1 WHERE t.userid = :userid")
    void findByUseridAndUpdate(Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE User t set t.enabled = false WHERE t.userid = :userid")
    void findByUseridAndDelete(Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE User t set t.estatecount = t.estatecount - 1 WHERE t.userid = :userid")
    void findByUseridAndDeleteEstate(Integer userid);

    @Transactional
    @Modifying
    @Query("UPDATE User t set t.firstName =:firstName, t.lastName =:lastName, t.email =:email WHERE t.userid = :userid")
    void findByUserUpdate(@Param("userid") Integer userid,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("email") String email);

    //User findByUseridAndUpdate(Integer userid);
    User findByConfirmationToken(String confirmationToken);
}