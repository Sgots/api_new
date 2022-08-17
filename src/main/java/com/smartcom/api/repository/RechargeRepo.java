/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.RechargeHistoryModel
 *  com.smartcom.api.repository.RechargeRepo
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.RechargeHistoryModel;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepo
        extends JpaRepository<RechargeHistoryModel, Integer> {
    public List<RechargeHistoryModel> findByRechargeToken(String var1);

    @Query(value = "from RechargeHistoryModel e where e.deviceID=:macaddress")
    public List<RechargeHistoryModel> findByDeviceID(@Param(value = "macaddress") String var1, Pageable var2);

    public List<RechargeHistoryModel> findByDeviceID(String var1);
}

