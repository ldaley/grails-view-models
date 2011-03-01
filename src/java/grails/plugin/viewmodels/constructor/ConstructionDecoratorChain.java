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

public class ConstructionDecoratorChain<T> implements ConstructionDecorator<T> {

	final private ConstructionDecorator<T>[] decorators;
	
	public ConstructionDecoratorChain(ConstructionDecorator<T>... decorators) {
		this.decorators = decorators;
	}

	public Object[] transformArgs(Object[] args) {
		Object[] transformedArgs = args;
		for (ConstructionDecorator<T> decorator : decorators) {
			transformedArgs = decorator.transformArgs(transformedArgs);
		}
		return transformedArgs;
	}
	
	public void decorate(T instance, Object[] originalArgs, Object[] transformedArgs) {
		for (ConstructionDecorator<T> decorator : decorators) {
			decorator.decorate(instance, originalArgs, transformedArgs);
		}
	}
	
	public Class<?>[] getSignature(Constructor<T> constructor) {
		return decorators[0].getSignature(constructor);
	}
	
}