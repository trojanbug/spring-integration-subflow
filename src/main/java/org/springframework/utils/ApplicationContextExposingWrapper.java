package org.springframework.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Map;


public class ApplicationContextExposingWrapper implements ApplicationContext, ApplicationContextAware {
    @Override
    public String getId() {
        return wrappedContextReference.get().getId();
    }

    @Override
    public String getApplicationName() {
        return wrappedContextReference.get().getApplicationName();
    }

    @Override
    public String getDisplayName() {
        return wrappedContextReference.get().getDisplayName();
    }

    @Override
    public long getStartupDate() {
        return wrappedContextReference.get().getStartupDate();
    }

    @Override
    public ApplicationContext getParent() {
        return wrappedContextReference.get().getParent();
    }

    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return wrappedContextReference.get().getAutowireCapableBeanFactory();
    }

    @Override
    public Environment getEnvironment() {
        return wrappedContextReference.get().getEnvironment();
    }

    @Override
    public boolean containsBeanDefinition(String s) {
        return wrappedContextReference.get().containsBeanDefinition(s);
    }

    @Override
    public int getBeanDefinitionCount() {
        return wrappedContextReference.get().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return wrappedContextReference.get().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(ResolvableType resolvableType) {
        return wrappedContextReference.get().getBeanNamesForType(resolvableType);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> aClass) {
        return wrappedContextReference.get().getBeanNamesForType(aClass);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> aClass, boolean b, boolean b1) {
        return wrappedContextReference.get().getBeanNamesForType(aClass, b, b1);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
        return wrappedContextReference.get().getBeansOfType(aClass);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> aClass, boolean b, boolean b1) throws BeansException {
        return wrappedContextReference.get().getBeansOfType(aClass, b, b1);
    }

    @Override
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> aClass) {
        return wrappedContextReference.get().getBeanNamesForAnnotation(aClass);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
        return wrappedContextReference.get().getBeansWithAnnotation(aClass);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().findAnnotationOnBean(s, aClass);
    }

    @Override
    public Object getBean(String s) throws BeansException {
        return wrappedContextReference.get().getBean(s);
    }

    @Override
    public <T> T getBean(String s, Class<T> aClass) throws BeansException {
        return wrappedContextReference.get().getBean(s, aClass);
    }

    @Override
    public <T> T getBean(Class<T> aClass) throws BeansException {
        return wrappedContextReference.get().getBean(aClass);
    }

    @Override
    public Object getBean(String s, Object... objects) throws BeansException {
        return wrappedContextReference.get().getBean(s, objects);
    }

    @Override
    public <T> T getBean(Class<T> aClass, Object... objects) throws BeansException {
        return wrappedContextReference.get().getBean(aClass, objects);
    }

    @Override
    public boolean containsBean(String s) {
        return wrappedContextReference.get().containsBean(s);
    }

    @Override
    public boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().isSingleton(s);
    }

    @Override
    public boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().isPrototype(s);
    }

    @Override
    public boolean isTypeMatch(String s, ResolvableType resolvableType) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().isTypeMatch(s, resolvableType);
    }

    @Override
    public boolean isTypeMatch(String s, Class<?> aClass) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().isTypeMatch(s, aClass);
    }

    @Override
    public Class<?> getType(String s) throws NoSuchBeanDefinitionException {
        return wrappedContextReference.get().getType(s);
    }

    @Override
    public String[] getAliases(String s) {
        return wrappedContextReference.get().getAliases(s);
    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return wrappedContextReference.get().getParentBeanFactory();
    }

    @Override
    public boolean containsLocalBean(String s) {
        return wrappedContextReference.get().containsLocalBean(s);
    }

    @Override
    public String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return wrappedContextReference.get().getMessage(s, objects, s1, locale);
    }

    @Override
    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return wrappedContextReference.get().getMessage(s, objects, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
        return wrappedContextReference.get().getMessage(messageSourceResolvable, locale);
    }

    @Override
    public void publishEvent(ApplicationEvent applicationEvent) {
        wrappedContextReference.get().publishEvent(applicationEvent);
    }

    @Override
    public void publishEvent(Object o) {
        wrappedContextReference.get().publishEvent(o);
    }

    @Override
    public Resource[] getResources(String s) throws IOException {
        return wrappedContextReference.get().getResources(s);
    }

    @Override
    public Resource getResource(String s) {
        return wrappedContextReference.get().getResource(s);
    }

    @Override
    public ClassLoader getClassLoader() {
        return wrappedContextReference.get().getClassLoader();
    }

    private Reference<ApplicationContext> wrappedContextReference;


    public ApplicationContext getWrappedContext() {
        return wrappedContextReference.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        wrappedContextReference = new WeakReference<ApplicationContext>(applicationContext);
    }

}
