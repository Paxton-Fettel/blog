package com.fettel.service;

import com.fettel.model.Category;
import com.fettel.model.Post;
import com.fettel.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Page<Post> findAll(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return postRepository.findAll(pageable);
    }

    public List<Post> findAllByCategory(Category category) {
        return postRepository.findAllByCategory(category);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
