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

class ReloadCapableConstructionDecorator<T> extends ConstructionDecoratorSupport<T> {
	
	private final ClassLoader classLoader;
	
	public ReloadCapableConstructionDecorator(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public Constructor<T> transformConstructor(Constructor<T> constructor) {
		try {
			Class<T> originalClassVersion = constructor.getDeclaringClass();
			Class<T> newestClassVersion = (Class<T>)classLoader.loadClass(originalClassVersion.getName());
			
			return newestClassVersion.getConstructor(constructor.getParameterTypes());
		} catch (NoSuchMethodException e) {
			return constructor;
		} catch (ClassNotFoundException e) {
			return constructor;
		}
	}
	
	ConstructionDecoratorChain<T> chainWith(ConstructionDecorator<T> decorator) {
		return new ConstructionDecoratorChain(decorator, this);
	}
	
}