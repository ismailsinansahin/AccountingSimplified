package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
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

        baseEntity.insertDateTime = LocalDateTime.now();
        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        if (authentication != null && !authentication.getPrincipal().equals("anonymous")) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            baseEntity.insertUserId = principal.getId();
            baseEntity.lastUpdateUserId = principal.getId();
        } else {
            baseEntity.insertUserId = -1L;
            baseEntity.lastUpdateUserId = -1L;
        }
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        if (authentication != null && !authentication.getPrincipal().equals("anonymous")) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            baseEntity.lastUpdateUserId = principal.getId();
        } else {
            baseEntity.insertUserId = -1L;
            baseEntity.lastUpdateUserId = -1L;
        }
    }
}