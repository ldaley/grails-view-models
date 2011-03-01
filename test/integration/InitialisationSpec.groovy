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

class InitialisationSpec extends IntegrationSpec {
	
	def "initialising bean method is called"() {
		when:
		def vm = new InitializingBeanViewModel()
		
		then:
		vm.p1 == 0
		vm.p2 == 1
	}

	def "initialising bean method is called after constructor"() {
		when:
		def vm = new InitializingBeanViewModel(p1: 1)
		
		then:
		vm.p1 == 1
		vm.p2 == 2
	}
	
}