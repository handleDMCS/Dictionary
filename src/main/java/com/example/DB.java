package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class DB {
    static DB obj;
    private JedisPool pool;

    private DB() {
        this.pool = new JedisPool("localhost", 6900);
    }

    public static DB getInstance() {
        if(obj == null) {
            obj = new DB();
        }
        return obj;
    }
    
    public int insertJSON(String path, boolean replace) {
        try (FileReader reader = new FileReader(path)) {
            Gson gson = new Gson(); 
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            List<Word> words = new ArrayList<>();
            
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                String eng = entry.getKey();
                String vie = entry.getValue().getAsString();
                if(replace == true) words.add(new Word(eng, vie));
                else {
                    if(!existPhrase(eng)) words.add(new Word(eng, vie));
                }
            }

            batch_insert(words);
            return words.size();
        } catch (IOException e) {
            System.out.println("Unable to insert data");
            return 0;
        }
    }

    public void insert(String eng, String vie) {
        try (Jedis jedis = pool.getResource()) {
            jedis.zadd("Order", 0, eng);
            jedis.hset("Definitions", eng, vie);
        }
    }

    public void insert(Jedis jedis, Word word) {
        String eng = word.getWordTarget();
        String vie = word.getWordExplain();
        jedis.zadd("Order", 0, eng);
        jedis.hset("Definitions", eng, vie);
    }

    public void batch_insert(List<Word> words) {
        try (Jedis jedis = pool.getResource()) {
            for (Word word : words) {
                insert(jedis, word);
            }
        }
    }

    public void remove(String phrase) {
        try (Jedis jedis = pool.getResource()) {
            jedis.zrem("Order", phrase);
            jedis.hdel("Definitions", phrase);
        }
    }

    public List<Word> fetch(int first_id, int last_id) {
        if(first_id > last_id) {
            return null;
        }
        if(first_id < 0) {
            return null;
        }
        try (Jedis jedis = pool.getResource()) {
            List<String> words = jedis.zrange("Order", first_id, last_id);
            List<String> definitions = new ArrayList<>();
            for (String eng : words) {
                String vie = jedis.hget("Definitions", eng);
                definitions.add(vie);
            }
            List<Word> page = new ArrayList<>();
            for (int i = 0; i < words.size(); i++) {
                page.add(new Word(words.get(i), definitions.get(i)));
            }
            if(page.size() == 0) {
                return null;
            }
            return page;
        }
    }

    public List<Word> fetch() {
        try (Jedis jedis = pool.getResource()) {
            List<String> words = jedis.zrange("Order", 0, -1);
            List<String> definitions = new ArrayList<>();
            for (String eng : words) {
                String vie = jedis.hget("Definitions", eng);
                definitions.add(vie);
            }
            List<Word> page = new ArrayList<Word>();
            for (int i = 0; i < words.size(); i++) {
                page.add(new Word(words.get(i), definitions.get(i)));
            }
            if(page.size() == 0) {
                return null;
            }
            return page;
        }
    } 

    public List<Word> search(String keyword, String mode) {
        List<Word> data = fetch();
        try {
            switch (mode) {
                case "prefix":
                    return data.stream().filter(s -> s.getWordTarget().startsWith(keyword)).collect(Collectors.toList());
                case "substring":
                    return data.stream().filter(s -> s.getWordTarget().contains(keyword)).collect(Collectors.toList());
                default:
                    return null;
            }
        } catch(Exception e) {
            return null;
        }
    }

    String getDef(String phrase) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hget("Definitions", phrase);
        }
    }

    boolean existPhrase(String phrase) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists("Definitions", phrase);
        }
    }
}