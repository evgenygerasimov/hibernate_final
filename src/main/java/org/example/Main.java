package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.AllArgsConstructor;

import org.example.entity.City;
import org.example.entity.Country;
import org.example.entity.CountryLanguage;
import org.example.mapper.CityCountryMapper;
import org.example.redis.CityCountry;
import org.example.dao.CityDAO;

import org.example.dao.CountryDAO;
import org.example.utils.DIContainer;
import org.example.bd.RedisConnectionCreator;
import org.example.bd.SessionCreator;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class Main {

    private final RedisConnectionCreator redisConnectionCreator = DIContainer.get(RedisConnectionCreator.class);
    private final SessionCreator sessionCreator = DIContainer.get(SessionCreator.class);
    private final CityDAO cityDAO = DIContainer.get(CityDAO.class);
    private final CountryDAO countryDAO = DIContainer.get(CountryDAO.class);
    private final CityCountryMapper cityCountryMapper = DIContainer.get(CityCountryMapper.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        Main main = new Main();

        List<City> allCities = main.fetchData(main);
        List<CityCountry> preparedData = main.cityCountryMapper.transformData(allCities);
        main.pushToRedis(preparedData);

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        main.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        main.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        main.sessionCreator.shutdown();
        main.redisConnectionCreator.shutdown();

    }

    private List<City> fetchData(Main main) {
        try (Session session = sessionCreator.getSession()) {

            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            List<Country> countries = countryDAO.getAll();
            int totalCount = main.cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(main.cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisConnectionCreator.getConnection()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisConnectionCreator.getConnection()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                if (value != null && !value.isEmpty()) {
                    try {
                        mapper.readValue(value, CityCountry.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void testMysqlData(List<Integer> ids) {
        try (Session session = sessionCreator.getSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}