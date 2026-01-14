package org.burgas.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.publisher.PublisherFullResponse;
import org.burgas.bookservice.dto.publisher.PublisherRequest;
import org.burgas.bookservice.dto.publisher.PublisherShortResponse;
import org.burgas.bookservice.entity.publisher.Publisher;
import org.burgas.bookservice.mapper.PublisherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class PublisherService implements CrudService<PublisherRequest, PublisherShortResponse, PublisherFullResponse> {

    private final PublisherMapper publisherMapper;

    @Override
    public List<PublisherShortResponse> findAll() {
        return this.publisherMapper.getPublisherRepository()
                .findAll()
                .stream()
                .map(this.publisherMapper::toShortResponse)
                .toList();
    }

    @Override
    public PublisherFullResponse findById(UUID id) {
        return this.publisherMapper.getPublisherRepository()
                .findById(id)
                .map(this.publisherMapper::toFullResponse)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void create(PublisherRequest publisherRequest) {
        Publisher publisher = this.publisherMapper.toEntity(publisherRequest);
        this.publisherMapper.getPublisherRepository().save(publisher);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void update(PublisherRequest publisherRequest) {
        Publisher publisher = this.publisherMapper.toEntity(publisherRequest);
        this.publisherMapper.getPublisherRepository().save(publisher);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void delete(UUID id) {
        Publisher publisher = this.publisherMapper.getPublisherRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        this.publisherMapper.getPublisherRepository().delete(publisher);
    }
}
