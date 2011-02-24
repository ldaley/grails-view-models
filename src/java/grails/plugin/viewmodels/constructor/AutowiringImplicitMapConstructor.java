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

import groovy.lang.GroovySystem;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClassImpl;
import java.lang.reflect.Constructor;

import java.util.Map;

import org.springframework.context.ApplicationContext;

public class AutowiringImplicitMapConstructor<T extends GroovyObject> extends AutowiringConstructor<T> {

	static private final Object[] NO_ARGS = new Object[0];
	static private final Class[] SINGLE_MAP_ARG = { Map.class };
	
	public AutowiringImplicitMapConstructor(Constructor<T> constructor, ApplicationContext applicationContext) {
		super(constructor, applicationContext, SINGLE_MAP_ARG);
		
		if (constructor.getParameterTypes().length > 0) {
			throw new IllegalArgumentException("constructor must be a no arg constructor, it is " + constructor);
		}
	}
	
	protected Object[] transformArgs(Object[] args) {
		return NO_ARGS;
	}
	
	protected void decorate(T instance, Object[] givenArgs, Object[] transformedArgs) {
		MetaClassImpl metaClass = (MetaClassImpl)instance.getMetaClass();
		
		Map properties = (Map)givenArgs[0];
		
		metaClass.setProperties(instance, properties);
		super.decorate(instance, givenArgs, transformedArgs);
	}
	
}