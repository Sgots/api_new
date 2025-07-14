package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Estate;
import com.smartcom.api.model.Meterdataresponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.smartcom.api.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Devices, Integer> {
    @Query(nativeQuery = true, value = "SELECT * from devices where estate_id=? AND (device_type =4 || device_type = 5)")
    List<Devices> energyUsedToday(Integer estateid);

    // Devices findByMacaddress(String mac_address);
    @Query("from Devices e where (e.macaddress =:mac_address and e.delete_status=false)")
    Devices findByMacaddress(@Param("mac_address") String mac_address);

    Devices findByDeviceid(Integer device_id);

    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.delete_status = true WHERE t.deviceid = :deviceid")
    void findByDeviceIdDelete(Integer deviceid);
    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.active = :state WHERE t.macaddress = :mac")
    void findByMacAndSwitch(@Param("mac") String mac,
                            @Param("state") Boolean state);
    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.hasEvent = true WHERE t.macaddress = :mac")
    void findByDeviceIdEvent(String mac);

    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.enabled = :state WHERE t.macaddress = :mac")
    void findByDeviceIdOnline(String mac, Boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.hasEvent = false WHERE t.macaddress = :mac")
    void findByDeviceIdEventAndDel(String mac);

    @Transactional
    @Modifying
    @Query("UPDATE Devices  t set t.delete_status = true WHERE t.estate.estateid = :estateid")
    void findByEstateIdAndDelete(Integer estateid);

    @Query("from Devices e where e.estate.estateid =:estateid and e.delete_status = false and (e.deviceTypes.typeid='1' or e.deviceTypes.typeid='2' or e.deviceTypes.typeid='3' or e.deviceTypes.typeid='4') ORDER BY e.deviceid DESC")
    List<Devices> findAllByEstateId(@Param("estateid") Integer estateid);

    @Query("from Devices e where (e.deviceTypes.typeid='2' OR e.deviceTypes.typeid='3') and (e.estate.estateid =:estateid and e.delete_status=false)  ")
    Devices findAllByEstateIdAndDevice_type(@Param("estateid") Integer estateid);

    @Query("from Devices e where (e.deviceTypes.typeid='2' OR e.deviceTypes.typeid='1') and (e.estate.estateid =:estateid  and e.delete_status=false) ")
    Devices findAllByEstateIdAndDevice_type2(@Param("estateid") Integer estateid);

    @Query("from Devices e where e.estate.estateid =:estateid and e.deviceTypes.typeid='1' and e.delete_status=false")
    Devices findHub(@Param("estateid") Integer estateid);
    @Query(nativeQuery = true, value = "SELECT  *  FROM devices where estate_id=? and (device_type = 2 || device_type =3 )")
    Devices meterEnergy(Integer estateid);

    @Query("from Devices e where e.estate.estateid =:estateid and e.delete_status=false and e.deviceTypes.typeid='4' OR e.deviceTypes.typeid='3'")
    List<Devices> findAllSocketsByEstateIdAndDevice_type(@Param("estateid") Integer estateid);

    @Transactional
    @Modifying
    @Query("UPDATE Devices t set t.device_name =:device_name, t.credit=:credit, t.creditAction =:creditAction, t.power=:power, t.powerAction=:powerAction, t.energy=:energy, t.energyAction=:energyAction WHERE t.deviceid= :deviceid")
    void findByDeviceAndUpdate(@Param("deviceid") Integer deviceid,
                               @Param("device_name") String device_name,
                               @Param("credit") Integer credit,
                               @Param("creditAction") Integer creditAction,
                               @Param("power") Integer power,
                               @Param("powerAction") Integer powerAction,
                               @Param("energy") Integer energy,
                               @Param("energyAction") Integer energyAction);

    @Transactional
    @Modifying
    @Query("UPDATE Devices t set t.remainingCredit=:remaining_credit, t.remainingEnergy=:remaining_energy, t.currentPower=:current_power, t.energyUsed=:energy_used, t.costToday=:cost_today WHERE t.macaddress=:macaddress")
    void updateDeviceDataresponse(@Param("macaddress") String macaddress,
                               @Param("remaining_credit") Double remaining_credit,
                               @Param("remaining_energy") Double remaining_energy,
                               @Param("current_power") Double current_power,
                               @Param("energy_used") Double energy_used,
                               @Param("cost_today") Double cost_today);


    // User findByConfirmationToken(String confirmationToken);
    @Query("from Devices e where e.estate.estateid =:estateid and e.delete_status=false and e.deviceTypes.typeid='4'")
    List<Devices> findAllSockets(@Param("estateid") Integer estateid);

    @Query("from Devices e where e.estate.estateid =:estateid and e.delete_status=false and e.deviceTypes.typeid='5'")
    Devices findUnknown(@Param("estateid") Integer estateid);
}