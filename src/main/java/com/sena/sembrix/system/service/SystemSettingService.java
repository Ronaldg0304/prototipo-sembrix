package com.sena.sembrix.system.service;

import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.repository.UserRepository;
import com.sena.sembrix.system.SystemSetting;
import com.sena.sembrix.system.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final SystemSettingRepository repository;
    private final UserRepository userRepository;


    @Transactional
    public List<SystemSetting> getAllSettings(UserEntity detachedUser) {
        log.info("Fetching settings for user email: {} with ID: {}", detachedUser.getEmail(), detachedUser.getId());
        UserEntity user = userRepository.getReferenceById(detachedUser.getId());
        ensureDefaults(user);
        return repository.findByUser_Id(user.getId());
    }

    private void ensureDefaults(UserEntity user) {
        createDefault(user, "app.name", "Sembrix", "TEXT", "Application Name", "The name displayed in the title bar and emails.");
        createDefault(user, "app.support_email", "support@sembrix.com", "TEXT", "Support Email", "Email address for user support inquiries.");
        createDefault(user, "app.maintenance", "false", "BOOLEAN", "Maintenance Mode", "Prevent non-admin users from logging in.");
        createDefault(user, "app.theme", "emerald", "COLOR", "Theme Color", "Primary color scheme for the application.");
        createDefault(user, "app.language", "es", "TEXT", "Application Language", "The current UI language (en or es).");
    }

    private void createDefault(UserEntity user, String key, String value, String type, String label, String description) {
        if (repository.findByUser_IdAndKey(user.getId(), key).isEmpty()) {
            log.info("Creating default setting {} for user ID {}", key, user.getId());
            repository.saveAndFlush(SystemSetting.builder()
                    .user(user)
                    .key(key)
                    .value(value)
                    .type(type)
                    .label(label)
                    .description(description)
                    .build());
        }
    }

    @Transactional
    public List<SystemSetting> updateSettings(UserEntity detachedUser, Map<String, String> updates) {
        log.info("Updating settings for user: {} ID: {}", detachedUser.getEmail(), detachedUser.getId());
        UserEntity user = userRepository.getReferenceById(detachedUser.getId());
        ensureDefaults(user);
        updates.forEach((key, value) -> {
            log.debug("Updating key {} to value {}", key, value);
            repository.findByUser_IdAndKey(user.getId(), key).ifPresentOrElse(setting -> {
                setting.setValue(value);
                repository.saveAndFlush(setting);
                log.info("Updated setting {} to {}", key, value);
            }, () -> log.warn("Setting {} not found for user ID {}", key, user.getId()));
        });
        return repository.findByUser_Id(user.getId());
    }
}
