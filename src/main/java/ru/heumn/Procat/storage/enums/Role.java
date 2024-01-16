package ru.heumn.Procat.storage.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN_ROLE, SELLER_ROLE, MANAGER_ROLE;

    @Override
    public String getAuthority() {
        return name();
    }
}
