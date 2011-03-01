class TheController {
	def index = { 
		NoConstructorViewModel vm = new NoConstructorViewModel()
		render(text: vm.arbitraryProperty)
	}
}