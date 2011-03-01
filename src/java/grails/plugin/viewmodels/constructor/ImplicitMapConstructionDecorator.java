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

import groovy.lang.GroovyObject;
import groovy.lang.MetaClassImpl;
import java.lang.reflect.Constructor;
import java.util.Map;

public class ImplicitMapConstructionDecorator<T extends GroovyObject> extends ConstructionDecoratorSupport<T> {

	static private final Object[] NO_ARGS = new Object[0];
	static private final Class[] SINGLE_MAP_ARG = { Map.class };
	
	public Object[] transformArgs(Object[] args) {
		return NO_ARGS;
	}
	
	public void decorate(T instance, Object[] givenArgs, Object[] transformedArgs) {
		MetaClassImpl metaClass = (MetaClassImpl)instance.getMetaClass();
		Map properties = (Map)givenArgs[0];
		metaClass.setProperties(instance, properties);
	}
	
	public Class<?>[] getSignature(Constructor<T> constructor) {
		return SINGLE_MAP_ARG;
	}
	
	ConstructionDecoratorChain<T> chainWith(ConstructionDecorator<T> decorator) {
		return new ConstructionDecoratorChain(this, decorator);
	}
	
}