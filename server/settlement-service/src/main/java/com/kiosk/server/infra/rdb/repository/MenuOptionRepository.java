package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.MenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOptionEntity, Long> {
}
