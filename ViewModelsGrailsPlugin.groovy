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
import grails.plugin.viewmodels.*
import grails.plugin.viewmodels.constructor.*

import org.springframework.beans.BeanUtils
import org.springframework.context.ApplicationContextAware
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor

class ViewModelsGrailsPlugin {

	def title = "Grails View Models Plugin"
	def description = 'Grails support for the MVVM pattern: http://en.wikipedia.org/wiki/Model_View_ViewModel'
	def documentation = "http://grails.org/plugin/view-models"
	def author = "Luke Daley"
	def authorEmail = "ld@ldaley.com"
	
	def version = "0.1"
	def grailsVersion = "1.3 > *"
	def dependsOn = [:]

	def artefacts = [ViewModelArtefactHandler]
	def watchedResources = [
		"file:./grails-app/viewModels/**/*ViewModel.groovy",
		"file:../../plugins/*/viewModels/**/*ViewModel.groovy",
	]

	def pluginExcludes = []
	
	def doWithSpring = {
		for (viewModelClass in application.viewModelClasses) {
			"${viewModelClass.fullName}"(viewModelClass.clazz) { bean ->
				bean.singleton = false
				bean.autowire = true
			}
			"${viewModelClass.fullName}ViewModelClass"(MethodInvokingFactoryBean) { bean ->
				targetObject = ref("grailsApplication", true)
				targetMethod = "getArtefact"
				bean.lazyInit = true
				arguments = [ViewModelArtefactHandler.TYPE, viewModelClass.fullName]
			}
		}
	}
	
	def doWithDynamicMethods = { context ->
		def autowiringDecorator = new AutowiringConstructionDecorator(context)
		for (viewModelClass in application.viewModelClasses) {
			enhanceViewModelClass(viewModelClass, autowiringDecorator)
		}
	}

	def onChange = { event ->
		handleChange(application, event, ViewModelArtefactHandler.TYPE)
	}

	private static SINGLE_OBJ_ARRAY_SIGNATURE = [Object[]] as Class[]
	
	private static enhanceViewModelClass(viewModelClass, autowiringDecorator) {
		def constructors = viewModelClass.clazz.constructors
		
		for (constructor in constructors) {
			viewModelClass.metaClass.registerInstanceMethod(new DecoratingMetaConstructor(constructor, autowiringDecorator))
			
			if (constructors.size() == 1 && constructor.parameterTypes.size() == 0) {
				viewModelClass.metaClass.registerInstanceMethod(new DecoratingImplicitMapMetaConstructor(constructor, autowiringDecorator))
			}
		}
	}
	
	static private handleChange(application, event, type) {
		if (application.isArtefactOfType(type, event.source)) {
			def autowiringDecorator = new AutowiringConstructionDecorator(event.ctx)
			
			def oldClass = application.getArtefact(type, event.source.name)
			application.addArtefact(type, event.source)
			enhanceViewModelClass(event.source, autowiringDecorator)
			

			// Reload subclasses
			application.getArtefacts(type).each {
				if (it.clazz != event.source && oldClass.clazz.isAssignableFrom(it.clazz)) {
					def newClass = application.classLoader.reloadClass(it.clazz.name)
					application.addArtefact(type, newClass)
					enhanceViewModelClass(newClass, autowiringDecorator)
				}
			}
		}
	}

}
