package com.sena.sembrix.system;

import com.sena.sembrix.identity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_system_settings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "setting_key"})
})
public class SystemSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @Column(name = "setting_key", nullable = false)
    private String key;

    @Column(name = "setting_value")
    private String value;

    @Column(name = "setting_type")
    private String type; // TEXT, BOOLEAN, COLOR, NUMBER

    @Column(name = "setting_label")
    private String label;

    @Column(name = "description")
    private String description;
}
