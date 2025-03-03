package org.example.dao;

import lombok.AllArgsConstructor;
import org.example.entity.City;
import org.example.bd.SessionCreator;
import org.hibernate.query.Query;

import java.util.List;

@AllArgsConstructor
public class CityDAO {

    private final SessionCreator sessionCreator;

    public List<City> getItems(int offset, int limit) {
        Query<City> query = sessionCreator.getSession().createQuery("select c from City c", City.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }

    public int getTotalCount() {
        Query<Long> query = sessionCreator.getSession().createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public City getById(Integer id) {
        Query<City> query = sessionCreator.getSession().createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }
}
