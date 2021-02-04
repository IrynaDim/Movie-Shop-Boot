package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.exception.DataProcessingException;
import com.dev.cinema.model.Order;
import com.dev.cinema.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = Logger.getLogger(OrderDaoImpl.class);
    private final SessionFactory sessionFactory;

    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order create(Order order) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            logger.info("Order was created" + order);
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Cant create order with description " + order
                            + " to the database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = cb.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            root.fetch("tickets", JoinType.LEFT);
            root.fetch("user");
            Predicate pr = cb.equal(root.get("user"), user);
            query.select(root).where(pr).distinct(true);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get orders for user: " + user, e);
        }
    }
}

