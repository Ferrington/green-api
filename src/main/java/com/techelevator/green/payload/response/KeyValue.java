package com.techelevator.green.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValue<K, V> {
    private K key;
    private V value;
}
