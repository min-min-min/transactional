package com.chenjing.transactional;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


/**
 * @author Chenjing
 * @date 2020/9/17
 */
class MultiTransactionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition("com.chenjing.transactional.MultiTransactionalAspect",
                new RootBeanDefinition(MultiTransactionalAspect.class));
    }

}
