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

import java.lang.InstantiationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;

abstract public class DecoratingMetaConstructor<T> extends MetaMethod {

	final private Constructor<T> constructor;
	
	public DecoratingMetaConstructor(Constructor<T> constructor) {
		this(constructor, constructor.getParameterTypes());
	}

	protected DecoratingMetaConstructor(Constructor<T> constructor, Class[] parameterTypes) {
		super(parameterTypes);
		this.constructor = constructor;
	}

	public Constructor<T> getConstructor() {
		return constructor;
	}
	
	public int getModifiers() {
		return constructor.getModifiers();
	}

	public String getName() {
		return "<init>";
	}

	public Class<T> getReturnType() {
		return constructor.getDeclaringClass();
	}

	public CachedClass getDeclaringClass() {
		return ReflectionCache.getCachedClass(constructor.getDeclaringClass());
	}

	public Object invoke(Object object, Object[] arguments) {
		Object[] transformedArgs = transformArgs(arguments);
		T instance = instantiate(transformedArgs);
		decorate(instance, arguments, transformedArgs);
		return instance;
	}
	
	protected T instantiate(Object[] arguments) {
		try {
			return constructor.newInstance(arguments);
		} catch (InstantiationException e) {
			throw new RuntimeException("Failed to invoke constructor " + constructor + " with args: " + arguments, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to invoke constructor " + constructor + " with args: " + arguments, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Failed to invoke constructor " + constructor + " with args: " + arguments, e);
		}
	}
	
	protected Object[] transformArgs(Object[] args) {
		return args;
	}
	
	abstract protected void decorate(T instance, Object[] givenArgs, Object[] transformedArgs);
	
}