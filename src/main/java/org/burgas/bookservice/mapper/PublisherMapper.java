package org.burgas.bookservice.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.publisher.PublisherFullResponse;
import org.burgas.bookservice.dto.publisher.PublisherRequest;
import org.burgas.bookservice.dto.publisher.PublisherShortResponse;
import org.burgas.bookservice.entity.publisher.Publisher;
import org.burgas.bookservice.repository.PublisherRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class PublisherMapper implements Mapper<PublisherRequest, Publisher, PublisherShortResponse, PublisherFullResponse> {

    @Getter
    private final PublisherRepository publisherRepository;

    private final ObjectFactory<BookMapper> bookMapperObjectFactory;

    private BookMapper getBookMapper() {
        return this.bookMapperObjectFactory.getObject();
    }

    @Override
    public Publisher toEntity(PublisherRequest publisherRequest) {
        UUID publisherId = this.getData(publisherRequest.getId(), UUID.randomUUID());
        return this.publisherRepository.findById(publisherId)
                .map(
                        publisher -> {
                            String name = this.getData(publisherRequest.getName(), publisher.getName());
                            String description = this.getData(publisherRequest.getDescription(), publisher.getDescription());
                            return Publisher.builder()
                                    .id(publisher.getId())
                                    .name(name)
                                    .description(description)
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            String name = this.getDataThrowable(publisherRequest.getName(), "Publisher name is null");
                            String description = this.getDataThrowable(publisherRequest.getDescription(), "Publisher description is null");
                            return Publisher.builder()
                                    .name(name)
                                    .description(description)
                                    .build();
                        }
                );
    }

    @Override
    public PublisherShortResponse toShortResponse(Publisher publisher) {
        return PublisherShortResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .description(publisher.getDescription())
                .build();
    }

    @Override
    public PublisherFullResponse toFullResponse(Publisher publisher) {
        return PublisherFullResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .description(publisher.getDescription())
                .books(
                        publisher.getBooks()
                                .stream()
                                .map(book -> this.getBookMapper().toBookWithoutPublisherResponse(book))
                                .toList()
                )
                .build();
    }
}
