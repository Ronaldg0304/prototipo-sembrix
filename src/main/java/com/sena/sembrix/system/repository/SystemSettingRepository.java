package com.sena.sembrix.system.repository;

import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.system.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    List<SystemSetting> findByUser(UserEntity user);
    List<SystemSetting> findByUser_Id(Long userId);
    Optional<SystemSetting> findByUserAndKey(UserEntity user, String key);
    Optional<SystemSetting> findByUser_IdAndKey(Long userId, String key);
}
