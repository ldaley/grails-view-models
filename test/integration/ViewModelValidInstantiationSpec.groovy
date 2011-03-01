/*
 * Copyright 2011 Luke Daley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import spock.lang.*
import grails.plugin.spock.*

class ViewModelValidInstantiationSpec extends IntegrationSpec {
	
	def "can instantiate view models with default constructor"() {
		when:
		def vm = new NoConstructorViewModel()
		
		then:
		vm != null
		vm.class.name == NoConstructorViewModel.name
	}
	
	def "instantiated view model is autowired"() {
		when:
		def vm = new NoConstructorViewModel()
		
		then:
		vm.exampleService != null
		vm.exampleService instanceof ExampleService
	}
	
	def "can instantiate with groovy map constructor"() {
		when:
		def vm = new NoConstructorViewModel(arbitraryProperty: 1)
		
		then:
		vm.arbitraryProperty == 1

		and:
		vm.exampleService != null
		vm.exampleService instanceof ExampleService
	}
	
	def "can instantiate object with one untyped arg"() {
		when:
		def vm = new OneUntypedArgConstructorViewModel(3)
		
		then:
		vm.arg == 3
		
		and:
		vm.exampleService != null
	}
	
	def "can instantiate object with one typed arg"() {
		when:
		def vm = new OneTypedArgConstructorViewModel(3)
		
		then:
		vm.arg == 3
		
		and:
		vm.exampleService != null
	}

	@FailsWith(
		value = org.spockframework.runtime.ConditionNotSatisfiedError, 
		reason = "Groovy bypasses the MOP layer for this kind of dispatch, so we don't get autowiring"
	)
	def "can instantiate object with implicit obj array"() {
		when:
		def vm = new ObjArrayConstructorViewModel(1,2,3)
		
		then:
		vm.args == [1,2,3] as Object[]
		
		and:
		vm.exampleService != null
	}

	def "can instantiate object with explicit obj array"() {
		when:
		def vm = new ObjArrayConstructorViewModel([1,2,3] as Object[])
		
		then:
		vm.args == [1,2,3] as Object[]
		
		and:
		vm.exampleService != null
	}
	
	def "can instantiate object with more than one arg"() {
		when:
		def vm = new MoreThanOneArgConstructorViewModel(1,2,3)
		
		then:
		vm.a1 == 1
		vm.a2 == 2
		vm.a3 == 3
		
		and:
		vm.exampleService != null
	}
	
}