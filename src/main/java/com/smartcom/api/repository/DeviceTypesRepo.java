/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.DeviceTypes
 *  com.smartcom.api.repository.DeviceTypesRepo
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.DeviceTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypesRepo
        extends JpaRepository<DeviceTypes, Integer> {
    public DeviceTypes findByTypeid(Integer var1);
}

