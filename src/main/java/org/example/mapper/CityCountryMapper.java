package org.example.mapper;

import org.example.entity.City;
import org.example.entity.Country;
import org.example.entity.CountryLanguage;
import org.example.redis.CityCountry;
import org.example.redis.Language;
import org.example.utils.DIContainer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CityCountryMapper {

    public  List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(this::mapCity).collect(Collectors.toList());
    }

    private  CityCountry mapCity(City city) {
        CityCountry res = DIContainer.get(CityCountry.class);
        res.setId(city.getId());
        res.setName(city.getName());
        res.setPopulation(city.getPopulation());
        res.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        res.setAlternativeCountryCode(country.getAlternativeCode());
        res.setContinent(country.getContinent());
        res.setCountryCode(country.getCode());
        res.setCountryName(country.getName());
        res.setCountryPopulation(country.getPopulation());
        res.setCountryRegion(country.getRegion());
        res.setCountrySurfaceArea(country.getSurfaceArea());

        Set<Language> languages = country.getLanguages().stream().map(CityCountryMapper::mapLanguage).collect(Collectors.toSet());
        res.setLanguages(languages);

        return res;
    }

    private static Language mapLanguage(CountryLanguage cl) {
        Language language = DIContainer.get(Language.class);
        language.setLanguage(cl.getLanguage());
        language.setIsOfficial(cl.getIsOfficial());
        language.setPercentage(cl.getPercentage());
        return language;
    }
}