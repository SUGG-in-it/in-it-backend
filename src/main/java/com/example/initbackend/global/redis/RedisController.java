package com.example.initbackend.global.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate2;

    @GetMapping("/")
    public String test(){
        return "test";
    }

    @GetMapping("/redisTest")
    public ResponseEntity<?> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate2.opsForValue();
        SetOperations<String, String> sop = redisTemplate2.opsForSet();
//        HashSet<String > stringSet = new HashSet<>();
//        stringSet.add("a");
//        stringSet.add("b");
//        stringSet.add("c");
//        stringSet.add("a");
//        sop.add("1", String.valueOf(stringSet));
        sop.add("1", "yellow", "banana");
        sop.add("1", "ji");
        vop.set("yellow", "banana");
        vop.set("red", "apple");
        vop.set("green", "watermelon");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate2.opsForValue();
        String value = vop.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
