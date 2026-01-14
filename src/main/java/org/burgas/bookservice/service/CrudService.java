package org.burgas.bookservice.service;

import org.burgas.bookservice.dto.Request;
import org.burgas.bookservice.dto.Response;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface CrudService<R extends Request, S extends Response, F extends Response> {

    List<S> findAll();

    F findById(UUID id);

    void create(R r);

    void update(R r);

    void delete(UUID id);
}
