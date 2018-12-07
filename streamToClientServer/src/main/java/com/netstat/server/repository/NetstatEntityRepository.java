package com.netstat.server.repository;

import com.netstat.server.domain.NetstatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetstatEntityRepository extends JpaRepository<NetstatEntity, Integer> {
}
