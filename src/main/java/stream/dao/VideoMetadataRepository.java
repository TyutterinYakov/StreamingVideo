package stream.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import stream.model.StatusVideo;
import stream.model.User;
import stream.model.VideoMetadata;
import stream.model.VideoMetadataResponse;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long>{

	Set<VideoMetadata> findByUser(User user);

	Set<VideoMetadata> findByUserAndStatusIsNot(User user, StatusVideo delete);

	List<VideoMetadata> findAllByUserAndStatus(User user, StatusVideo public1);

	Collection<VideoMetadata> findAllByStatus(StatusVideo public1);

	@Query(nativeQuery=true, value="SELECT * FROM video_metadata WHERE video_id=?1 AND (status=?2 OR status=?3)")
	Optional<VideoMetadata> findByVideoIdAndStatus(Long id, String public1, String link);

	Optional<VideoMetadata> findByVideoIdAndUser(Long id, User user);

	Optional<VideoMetadata> findByUserAndVideoIdAndStatusIsNot(User user, Long videoId, StatusVideo delete);


}
