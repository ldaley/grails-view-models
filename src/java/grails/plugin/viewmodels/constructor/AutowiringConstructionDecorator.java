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

public class AutowiringConstructionDecorator<T> extends ConstructionDecoratorSupport<T> {

	final private Autowirer autowirer;
	
	public AutowiringConstructionDecorator(ApplicationContext applicationContext) {
		this(new Autowirer(applicationContext));
	}

	public AutowiringConstructionDecorator(Autowirer autowirer) {
		this.autowirer = autowirer;
	}
	
	public void decorate(T instance, Object[] givenArgs, Object[] transformedArgs) {
		autowirer.autowire(instance);
	}
	
}