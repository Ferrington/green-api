package com.techelevator.green.service;

import com.techelevator.green.payload.response.KeyValue;
import com.techelevator.green.payload.response.TableResponse;
import com.techelevator.green.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.hibernate.collection.spi.PersistentSortedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    StudentRepository studentRepository;

    public List<String> getTableNames() {
        return entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .map(EntityType::getName)
                .filter(t -> !t.equals("Role"))
                .toList();
    }

    public TableResponse getTable(String tableName, int pageNumber) {
        final int PAGE_SIZE = 25;

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

        SimpleJpaRepository<?, Long> repository = new SimpleJpaRepository<>(clazz, entityManager);
        Page<?> page = repository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));

        Optional<?> optObject = page.get().findAny();

        List<KeyValue<String, String>> fieldList = null;
        if (optObject.isPresent()) {
            Field[] fields = optObject.get().getClass().getDeclaredFields();
            fieldList = Arrays.stream(fields)
                            .map(f -> new KeyValue<String, String>(f.getName(), getFieldType(f)))
                            .collect(Collectors.toList());
        }

        return new TableResponse(page, fieldList);
    }

    private String getFieldType(Field f) {
        String str = f.getGenericType().getTypeName();
        int caretIndex = str.indexOf("<");
        if (caretIndex == -1) {
            return str.substring(str.lastIndexOf(".") + 1);
        }

        String firstBit = str.substring(0, caretIndex);
        String outerType = firstBit.substring(firstBit.lastIndexOf(".") + 1);

        String secondBit = str.substring(caretIndex);
        String innerType = secondBit.substring(secondBit.lastIndexOf(".") + 1, secondBit.length() - 1);

        return String.format("%s<%s>", outerType, innerType);
    }
}
