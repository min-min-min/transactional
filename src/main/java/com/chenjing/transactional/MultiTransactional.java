package com.chenjing.transactional;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多数据源事务注解
 * e.g.
 * @MultiTransactional(datasourceNames = {"coreDatasource", "userDatasource"})
 * public void test() {
 *     coreService.save();
 *     userService.save();
 * }
 * @author Chenjing
 * @date 2020/9/17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiTransactional {

    /**
     * 需要管理的DataSource bean name
     * 如果不指定，默认为所有的数据源的开启事务
     */
    @AliasFor(attribute = "value")
    String[] datasourceBeanNames() default {};

    /**
     * 需要管理的DataSource bean name
     */
    @AliasFor(attribute = "datasourceBeanNames")
    String[] value() default {};
}
