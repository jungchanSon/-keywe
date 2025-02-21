package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
