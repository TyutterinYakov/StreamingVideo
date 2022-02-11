package stream.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stream.model.VideoMetadata;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long>{

}
