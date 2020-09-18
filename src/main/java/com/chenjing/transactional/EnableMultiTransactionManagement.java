package com.chenjing.transactional;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启多数据源事务管理
 *
 * @author Chenjing
 * @date 2020/9/17
 * @see MultiTransactional
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(MultiTransactionRegistrar.class)
@Documented
public @interface EnableMultiTransactionManagement {
}
