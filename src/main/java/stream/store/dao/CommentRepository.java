package stream.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stream.store.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
