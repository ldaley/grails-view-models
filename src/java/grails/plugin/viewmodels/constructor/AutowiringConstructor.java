/*
 * Copyright 2011 Luke Daley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.viewmodels.constructor;

import java.lang.reflect.Constructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

public class AutowiringConstructor<T> extends DecoratingMetaConstructor<T> {

	final public ApplicationContext applicationContext;
	
	public AutowiringConstructor(Constructor<T> constructor, ApplicationContext applicationContext) {
		this(constructor, applicationContext, constructor.getParameterTypes());
	}

	protected AutowiringConstructor(Constructor<T> constructor, ApplicationContext applicationContext, Class[] parameterTypes) {
		super(constructor, parameterTypes);
		this.applicationContext = applicationContext;
	}
	
	protected void decorate(T instance, Object[] givenArgs, Object[] transformedArgs) {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		beanFactory.autowireBeanProperties(instance, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		beanFactory.initializeBean(instance, instance.getClass().getName());
	
		AutowiredAnnotationBeanPostProcessor annotationProcessor = (AutowiredAnnotationBeanPostProcessor)beanFactory.createBean(
			AutowiredAnnotationBeanPostProcessor.class, 
			AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, 
			false
		);
		
		annotationProcessor.processInjection(instance);
	
		if (instance instanceof ApplicationContextAware) {
			((ApplicationContextAware)instance).setApplicationContext(applicationContext);
		}
	}
	
}