package com.dev.cinema.aspect;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggerAspect {
    private Logger userLogger = Logger.getLogger(LoggerAspect.class.getName());

    @Before("controllerMethods() || daoMethods() || serviceMethods()")
    public void checkUserAuth(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        userLogger.info("Before " + joinPoint.toString() + ", args=[" + args + "]");
    }

    @After("controllerMethods() || daoMethods() || serviceMethods()")
    public void checkUserAuthClassLevel(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        userLogger.info("After " + joinPoint.toString() + ", args=[" + args + "]");
    }

    @Pointcut("within(com.dev.cinema.controllers.*))")
    public void controllerMethods() {
    }

    @Pointcut("within(com.dev.cinema.dao.impl.*))")
    public void daoMethods() {
    }

    @Pointcut("within(com.dev.cinema.service.impl.*))")
    public void serviceMethods() {
    }
}
