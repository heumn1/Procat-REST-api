package ru.heumn.Procat.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserCRUDAspect {

    @Before("execution(* ru.heumn.Procat.controllers.UserController.*(..))")
    public void beforeUserControllerExecution(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        log.info("execution method: " + signature.getName());
    }

    @AfterThrowing(pointcut = "execution(* ru.heumn.Procat.controllers.UserController.*(..))",
    throwing = "exception")
    public void afterThrowingUserControllerExecution(JoinPoint joinPoint, Throwable exception) {
        log.error("Method: " + joinPoint.getSignature().getName() + " exception: " + exception.getMessage());
    }

    //TODO проверка на роль

}
