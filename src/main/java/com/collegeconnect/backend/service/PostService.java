package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.CommentRequest;
import com.collegeconnect.backend.dto.CommentResponse;
import com.collegeconnect.backend.dto.PostRequest;
import com.collegeconnect.backend.dto.PostResponse;
import com.collegeconnect.backend.model.Comment;
import com.collegeconnect.backend.model.Post;
import com.collegeconnect.backend.model.User;
import com.collegeconnect.backend.repository.CommentRepository;
import com.collegeconnect.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        User currentUser = authService.getCurrentUserEntity();
        Post post = Post.builder()
                .user(currentUser)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();

        Post saved = postRepository.save(post);
        return mapToPostResponse(saved);
    }

    public List<PostResponse> getAllPosts(String category) {
        List<Post> posts;
        if (category != null && !category.trim().isEmpty()) {
            posts = postRepository.findByCategoryOrderByCreatedAtDesc(category);
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }
        return posts.stream().map(this::mapToPostResponse).collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return mapToPostResponse(post);
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        User currentUser = authService.getCurrentUserEntity();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        Comment comment = Comment.builder()
                .post(post)
                .user(currentUser)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);
        return mapToCommentResponse(saved);
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found with id: " + postId);
        }
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream().map(this::mapToCommentResponse).collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post p) {
        return PostResponse.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .username(p.getUser().getUsername())
                .title(p.getTitle())
                .content(p.getContent())
                .category(p.getCategory())
                .createdAt(p.getCreatedAt())
                .build();
    }

    private CommentResponse mapToCommentResponse(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .postId(c.getPost().getId())
                .userId(c.getUser().getId())
                .username(c.getUser().getUsername())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
