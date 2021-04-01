package com.example.udemy.controllers;


import com.example.udemy.models.Book;
import com.example.udemy.services.BookService;
import com.example.udemy.vo.BookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;

    // @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping()
    public List<BookVO> findAll() {
        var books = service.findAll();
        books.stream().forEach(b -> b.add(linkTo(BookController.class).slash(b.getId()).withSelfRel()));
        return books;
    }

    // @CrossOrigin(origins = {"https://edmarcosta.site"})
    @GetMapping("/{id}")
    public BookVO findById(@PathVariable("id") Long id) {
        var book = service.findById(id);
        book.add(linkTo(BookController.class).slash(book.getId()).withSelfRel());
        return book;
    }

    @PostMapping()
    public BookVO create(@RequestBody BookVO bookVO) {
        var book = service.create(bookVO);
        book.add(linkTo(BookController.class).slash(book.getId()).withSelfRel());
        return book;
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody BookVO bookVO) {
        bookVO.setId(id);
        service.update(bookVO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
