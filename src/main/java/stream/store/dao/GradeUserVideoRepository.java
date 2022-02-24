package stream.store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stream.store.entity.GradeUserVideo;
import stream.store.entity.User;
import stream.store.entity.VideoMetadata;

@Repository
public interface GradeUserVideoRepository extends JpaRepository<GradeUserVideo, Long>{

	Optional<GradeUserVideo> findByUserAndVideo(User user, VideoMetadata video);

}
