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
import groovy.lang.GroovyObject;

public class DecoratingImplicitMapMetaConstructor<T extends GroovyObject> extends DecoratingMetaConstructor {
	
	private static final ImplicitMapConstructionDecorator DECORATOR_INSTANCE = new ImplicitMapConstructionDecorator();
	
	public DecoratingImplicitMapMetaConstructor(Constructor<T> constructor, ConstructionDecorator<T> decorator) {
		super(constructor, DECORATOR_INSTANCE.chainWith(decorator));
	}

}