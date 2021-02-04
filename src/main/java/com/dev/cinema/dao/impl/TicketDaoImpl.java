package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.TicketDao;
import com.dev.cinema.exception.DataProcessingException;
import com.dev.cinema.model.Ticket;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl implements TicketDao {
    private static final Logger logger = Logger.getLogger(TicketDaoImpl.class);
    private final SessionFactory sessionFactory;

    public TicketDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Ticket add(Ticket ticket) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
            logger.info("Ticket was created" + ticket);
            return ticket;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Cant add ticket with details " + ticket.toString()
                            + " to the database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Ticket getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Ticket.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get ticket with id " + id, e);
        }
    }
}
