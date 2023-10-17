package com.techelevator.green.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.green.payload.response.TableResponse;
import com.techelevator.green.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    EntityManager entityManager;

    public List<String> getTableNames() {
        return entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .map(EntityType::getName)
                .filter(t -> !t.equals("Role"))
                .toList();
    }

    public TableResponse getTable(String tableName, String pageNumber, String sortColumn, String sortDir) {
        SimpleJpaRepository<?, Long> repository = generateSimpleRepository(tableName);
        PageRequest pageRequest = generatePageRequest(pageNumber, sortColumn, sortDir);
        Page<?> page = repository.findAll(pageRequest);

        Optional<?> optObject = page.get().findAny();

        Map<String, String> fieldMap = new HashMap<>();
        List<String> fieldOrder = new ArrayList<>();
        if (optObject.isPresent()) {
            Field[] fields = optObject.get().getClass().getDeclaredFields();
            Arrays.stream(fields)
                    .filter(f ->
                        !f.getGenericType().getTypeName().contains("<")
                        && (!f.isAnnotationPresent(JsonProperty.class)
                            || !f.getAnnotation(JsonProperty.class).access().equals(JsonProperty.Access.WRITE_ONLY))
                    )
                    .forEach(f -> {
                        fieldOrder.add(f.getName());
                        fieldMap.put(f.getName(), getFieldType(f));
                    });

        }

        return new TableResponse(tableName, page, fieldOrder, fieldMap);
    }

    private PageRequest generatePageRequest(String pageNumber, String sortColumn, String sortDir) {
        final int PAGE_SIZE = 25;

        int page;
        try {
            page = Integer.parseInt(pageNumber);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number is not a number.");
        }

        Sort sort = Sort.by(sortColumn);
        if (sortDir.equals("desc")) {
            sort.descending();
        }

        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    private String getFieldType(Field f) {
        String str = f.getGenericType().getTypeName();
        return str.substring(str.lastIndexOf(".") + 1);
    }

    private SimpleJpaRepository<?, Long> generateSimpleRepository(String tableName) {
        Optional<EntityType<?>> entity = entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .filter(e -> e.getName().equals(tableName))
                .findAny();
        if (entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find table named: " + tableName);
        }

        EntityType<?> table = entity.get();
        Class<?> clazz = table.getJavaType();

        return new SimpleJpaRepository<>(clazz, entityManager);
    }
}
