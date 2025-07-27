package com.fettel.controller;

import com.fettel.model.Comment;
import com.fettel.model.Post;
import com.fettel.model.User;
import com.fettel.service.CommentService;
import com.fettel.service.PostService;
import com.fettel.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public CommentController(
            CommentService commentService,
            PostService postService,
            UserService userService
    ) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    //===========POST -> ADD COMMENTS===========
    @PostMapping("/add/{postId}")
    public String addComment(
            @PathVariable Long postId,
            @ModelAttribute("content") String content,
            Principal principal
    ) {
        Comment comment = new Comment();

        Post post = postService.findById(postId);
        User user = userService.findByUsername(principal.getName());
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(user);

        commentService.save(comment);
        return "redirect:/posts/" + postId;
    }
}
