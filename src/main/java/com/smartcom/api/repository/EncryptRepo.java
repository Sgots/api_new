package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.EncryptionKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EncryptRepo extends JpaRepository<EncryptionKeys, Integer> {
    @Query("UPDATE EncryptionKeys t set t.encryptionKey=:key, t.IV1=:iv1, t.IV2=:iv2  WHERE t.estate.estateid = :estateid")
    void findByEstateAndUpdate(@Param("estateid") Integer estateid,
                               @Param("key") byte[] key,
                               @Param("iv1") byte[] iv1,
                               @Param("iv2") byte[] iv2);

    EncryptionKeys findByEncryptID(Integer encrptid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM encryption_keys WHERE estate_id =?;\n")
    void deleteEncryption(Integer estateid);
}
