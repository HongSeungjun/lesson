//package com.join.lesson.aop;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DistributedLockAop {
//    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
//    private final RedissonClient redissonClient;
//    private final AopForTransaction aopForTransaction;
//
//
//    /**
//     * @DistributedLock 어노테이션이 붙은 메소드를 대상으로 하는 어드바이스.
//     * 해당 메소드를 실행하기 전에 분산 잠금을 시도하고, 실행 후 잠금을 해제한다.
//     */
//    @Around("@annotation(distributedLock)")
//    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//
//        // 잠금 키 생성: 고정된 접두사 + 동적 EL 표현식 값
//        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
//        RLock lock = redissonClient.getLock(key);
//
//        try {
//            // 잠금 시도: 지정된 대기 시간 동안 잠금을 획득하려고 시도
//            if (!lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)) {
//                log.info("Failed to acquire lock.");
//                return null;
//            }
//            // 잠금 획득 성공 시, 대상 메소드 실행
//            return aopForTransaction.proceed(joinPoint);
//        } finally {
//            // 현재 스레드가 잠금을 보유하고 있는지 확인 후 잠금 해제
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//}