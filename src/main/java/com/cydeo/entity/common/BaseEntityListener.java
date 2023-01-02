package com.cydeo.entity.common;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    public void onPrePersist(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")) {  // checks for valid user
            try {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                baseEntity.setInsertUserId(principal.getId());
                baseEntity.setLastUpdateUserId(principal.getId());
            } catch (Exception e){
                baseEntity.setInsertUserId(1L);
                baseEntity.setLastUpdateUserId(1L);
            }
        }
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")) {  // checks for valid user
            try {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                baseEntity.setLastUpdateUserId(principal.getId());
            } catch (Exception e){
                baseEntity.setLastUpdateUserId(1L);
            }
        }
    }
}
