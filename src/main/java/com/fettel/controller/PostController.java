package com.fettel.controller;

import com.fettel.model.Category;
import com.fettel.model.Comment;
import com.fettel.model.Post;
import com.fettel.model.User;
import com.fettel.service.CategoryService;
import com.fettel.service.CommentService;
import com.fettel.service.PostService;
import com.fettel.service.UserService;
import com.fettel.util.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final UserService userService;

    public PostController(
            PostService postService,
            CategoryService categoryService,
            CommentService commentService,
            UserService userService
    ) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.userService = userService;
    }

    //===========GET -> POSTS LIST===========
    @GetMapping
    public String listPosts(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Post> posts = postService.findAll(page, 5, "createdAt", "asc");
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    //===========GET -> VIEW A POST===========
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        List<Comment> comments = commentService.findByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        // model.addAttribute("comment", new Comment());

        return "posts/details";
    }

    //===========GET -> POST CREATE FORM===========
    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("newPost", new Post());
        model.addAttribute("categories", categoryService.findAll()); // for displaying all categories
        return "posts/create";
    }

    //===========POST -> CREATE A POST===========
    @PostMapping("/create")
    public String createPost(
            @ModelAttribute("newPost") Post post,
            @RequestParam("imageFile") MultipartFile multipartFile,
            Principal principal,
            Model model
    ) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "uploads/images";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); // saving post image
            post.setImageUrl("/images/" + fileName);
        }

        post.setCategory(categoryService.findById(post.getCategory().getId()));

        post.setAuthor(userService.findByUsername(principal.getName())); // set user via principal

        postService.save(post); // saving the new post

        return "redirect:/posts";
    }

    //===========GET -> EDIT POST FORM===========
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.findAll()); // for displaying all categories
        return "posts/edit";
    }

    //===========POST -> EDIT A POST===========
    @PostMapping("/edit/{id}")
    public String updatePost(
            @PathVariable Long id,
            @ModelAttribute Post postData,
            @RequestParam("imageFile") MultipartFile multipartFile
    ) throws IOException {
        Post post = postService.findById(id);

        // deleting relative image
        String imagePath = "uploads" + post.getImageUrl();
        File imageFile = new File(imagePath);
        if(imageFile.exists()) imageFile.delete();

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "uploads/images";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); // saving new image
            post.setImageUrl("/images/" + fileName);
        }

        post.setTitle(postData.getTitle());
        post.setContent(postData.getContent());
        post.setCategory(postData.getCategory());
        postService.save(post);
        return "redirect:/posts/{id}";
    }

    //===========GET -> DELETE A POST===========
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {

        Post post = postService.findById(id);

        // deleting relative image
        String imagePath = "uploads" + post.getImageUrl();
        File imageFile = new File(imagePath);
        if(imageFile.exists()) imageFile.delete();

        postService.deleteById(id);
        return "redirect:/posts";
    }
}
