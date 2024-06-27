package com.socialnetwork.socialnetwork.infrastructure.repositories;

import com.socialnetwork.socialnetwork.infrastructure.entities.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long> {
}
