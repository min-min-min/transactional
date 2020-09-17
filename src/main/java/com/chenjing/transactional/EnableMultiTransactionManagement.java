package com.chenjing.transactional;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * e.g
 *     @MultiTransactional(datasourceNames = {"coreDatasource", "userDatasource"})
 *     public void test() {
 *         chatListService.save();
 *         uUserBasicService.save();
 *     }
 * @author Chenjing
 * @date 2020/9/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(MultiTransactionRegistrar.class)
@Documented
public @interface EnableMultiTransactionManagement {
}
