package org.example.dao;

import lombok.AllArgsConstructor;
import org.example.entity.Country;
import org.example.bd.SessionCreator;
import org.hibernate.query.Query;

import java.util.List;

@AllArgsConstructor
public class CountryDAO {

    private final SessionCreator sessionCreator;

    public List<Country> getAll() {
        Query<Country> query = sessionCreator.getSession().createQuery("select c from Country c join fetch c.languages", Country.class);
        return query.list();
    }
}
