package com.fettel.service;

import com.fettel.model.Comment;
import com.fettel.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findByPostId(Long id) {
        return commentRepository.findByPostId(id);
    }
}
