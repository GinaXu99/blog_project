package com.springrestapi.blogrestapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springrestapi.blogrestapi.entity.Comment;
import com.springrestapi.blogrestapi.entity.Post;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CommentRepositoryTests {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	

	@Test
	@DisplayName("Test find comment by PostID")
	public void testfindCommentByPostId() {
		
		Post post = Post.builder()
				.title("post1")
				.description("post1 description")
				.contents("this is post1")
				.build();
		Comment comment1 = Comment.builder()
				.body("comment1")
				.post(post)
				.email("123@htomail.com")
				.name("c1")
				.build();
		Comment comment2 = Comment.builder()
				.body("comment1")
				.post(post)
				.email("123@htomail.com")
				.name("c1")
				.build();
		List<Comment> list = new ArrayList<>();
		list.add(comment2);
		list.add(comment1);
		
		postRepository.save(post);
		commentRepository.save(comment1);
		commentRepository.save(comment2);

		List<Comment> commentlists = commentRepository.findByPostId(post.getId());
		assertThat(commentlists).isNotNull();
		assertThat(commentlists.size()).isEqualTo(2);
		
		
		
	}
	
	

}
