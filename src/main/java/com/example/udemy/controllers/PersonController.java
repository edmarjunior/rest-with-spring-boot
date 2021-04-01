package com.example.udemy.controllers;

import com.example.udemy.services.PersonService;
import com.example.udemy.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private PagedResourcesAssembler<PersonVO> assembler;

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "limit", defaultValue = "10") int limit,
          @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        var pagePerson = service.findAll(pageable);
        pagePerson.stream().forEach(p -> p.add(linkTo(PersonController.class).slash(p.getId()).withSelfRel()));
        return new ResponseEntity<>(assembler.toModel(pagePerson), HttpStatus.OK);
    }

    @GetMapping("/findPersonByName/{name}")
    public ResponseEntity<?> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        var pagePerson = service.findPersonByName(name, pageable);
        pagePerson.stream().forEach(p -> p.add(linkTo(PersonController.class).slash(p.getId()).withSelfRel()));
        return new ResponseEntity<>(assembler.toModel(pagePerson), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public PersonVO findById(@PathVariable("id") Long id) {
        var person = service.findById(id);
        person.add(linkTo(PersonController.class).slash(person.getId()).withSelfRel());
        return person;
    }

    @PostMapping()
    public PersonVO create(@RequestBody PersonVO person) {
        var personVo = service.create(person);
        personVo.add(linkTo(PersonController.class).slash(personVo.getId()).withSelfRel());
        return personVo;
    }

    @PutMapping("/{id}")
    public PersonVO update(@RequestBody PersonVO person) {
        var personVo = service.update(person);
        personVo.add(linkTo(PersonController.class).slash(personVo.getId()).withSelfRel());
        return personVo;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> disablePerson(@PathVariable("id") Long id) {
        service.disablePerson(id);
        return ResponseEntity.ok().build();
    }
}
