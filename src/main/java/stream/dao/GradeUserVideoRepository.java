package stream.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stream.model.GradeUserVideo;
import stream.model.User;
import stream.model.VideoMetadata;

@Repository
public interface GradeUserVideoRepository extends JpaRepository<GradeUserVideo, Long>{

	Optional<GradeUserVideo> findByUserAndVideo(User user, VideoMetadata video);

}
