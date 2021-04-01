package com.example.udemy.services;

import com.example.udemy.converter.DozerConverter;
import com.example.udemy.exceptions.ResourceNotFoundException;
import com.example.udemy.models.Person;
import com.example.udemy.repositories.PersonRepository;
import com.example.udemy.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    @Autowired
    PersonRepository repository;

    public PersonVO create(PersonVO person) {
        var entity = DozerConverter.parseObject(person, Person.class);
        return DozerConverter.parseObject(repository.save(entity), PersonVO.class);
    }

    public Page<PersonVO> findAll(Pageable pageable) {
        var page = repository.findAll(pageable);
        return page.map(person -> DozerConverter.parseObject(person, PersonVO.class));
    }

    public Page<PersonVO> findPersonByName(String name, Pageable pageable) {
        var page = repository.findPersonByName(name, pageable);
        return page.map(person -> DozerConverter.parseObject(person, PersonVO.class));
    }


    public void delete(Long id) {
        Person entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }

    public PersonVO findById(Long id) {
        var entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID"));

        return DozerConverter.parseObject(entity, PersonVO.class);
    }

    public PersonVO update(PersonVO person) {
        var entity = repository.findById(person.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID"));

        entity.setName(person.getName());
        var vo = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    @Transactional
    public void disablePerson(Long id) {
        var entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID"));

        repository.disablePerson(id);
    }
}
