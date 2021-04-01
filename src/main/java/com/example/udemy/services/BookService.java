package com.example.udemy.services;

import com.example.udemy.converter.DozerConverter;
import com.example.udemy.exceptions.ResourceNotFoundException;
import com.example.udemy.models.Book;
import com.example.udemy.repositories.BookRepository;
import com.example.udemy.vo.BookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public List<BookVO> findAll() {
        var entities = repository.findAll();
        return DozerConverter.parseListObjects(entities, BookVO.class);
    }

    public BookVO findById(Long id) {
        var entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID")
        );

        return DozerConverter.parseObject(entity, BookVO.class);
    }

    public BookVO create(BookVO bookVO) {
        var entity = DozerConverter.parseObject(bookVO, Book.class);
        repository.save(entity);
        return DozerConverter.parseObject(entity, BookVO.class);
    }

    public void update(BookVO bookVO) {
        var entity = repository.findById(bookVO.getId()).orElseThrow(
            () -> new ResourceNotFoundException("No records found for this ID")
        );

        entity.setAuthor(bookVO.getAuthor());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setPrice(bookVO.getPrice());
        entity.setTitle(bookVO.getTitle());

        repository.save(entity);
    }

    public void delete(Long id) {
        var entity = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No records found for this ID")
        );

        repository.delete(entity);
    }

}
