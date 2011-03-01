The “View Models” plugin provides some support for something like the Model-View-ViewModel pattern, referred to as MVVM (for some background see [this Wikipedia article](http://en.wikipedia.org/wiki/Model_View_ViewModel)).

## About MVVM

Since Grails is already founded on the MVC pattern, the idea of MVVM takes on a slightly different meaning to what it does to desktop developers. In the context of a Grails application, a “View Model” is an abstraction of the view oriented properties of an aspect of the application model.

Consider the following example. Your application lists one or more products on a screen. If the price of a product is less than $10, you want the product name to display in a different colour. Where does this characteristic belong? It most certainly does not belong on your `Product` domain class for obvious reasons. There are two feasible options; a taglib or a snippet of Groovy code in the view GSP. Using a taglib is a reasonable choice here, but taglibs are inconvenient to test and it doesn't seem right to create a tag to do a calculation (i.e. tags are inherently focussed on creating HTML representations of things). Using inline snippets of Groovy is very convenient, but difficult to test and prone to decay over time.

The MVVM pattern (as we are interpreting it) suggest that a specialised object should be created that implements the view level concerns of the product. Enter `ProductViewModel`.

    class ProductViewModel {
        Product product
        
        ProductViewModel(Product product) {
            this.product = product
        }
        
        String getNameColourName() {
            product.price < 10 ? "red" : "black"
        }
    }

No instead of passing your `Product` instance to the view, you pass an instance of `ProductViewModel`.

    class ProductController {
        
        def list = {
            [products: Products.findAll().collect { new ProductViewModel(it) }]
        }
        
    }

## Plugin Features

It's possible to roll your own view model type objects without any special plugins or magic. To do this you would likely create some classes in the `src/groovy` directory and manually instantiate them. While this would work, you don't get development time reloading and autowiring which you probably can't live without if you are like me. This plugin provides these two features.

All view model class MUST be in the `grails-app/viewModels` directory and MUST end in the suffix “ViewModel” (e.g. `ProductViewModel`).

### Autowiring

This plugin uses Groovy's MOP to augment existing constructors to provide dependency injection autowiring, and general Spring initialisation (e.g. calling `afterPropertiesSet()` on view models implementing [InitializingBean](http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/beans/factory/InitializingBean.html "InitializingBean (Spring Framework API 2.5)")). This includes supporting any custom constructors, not just no-arg constructors like Grails domain classes.

    class BookViewModel {
        def bookService
        def book
        
        BookViewModel(Book book) {
            this.book = book
        }
    }
    
    def bookViewModel = new BookViewModel(Book.get(1))
    assert bookViewModel.bookService != null

This also works with the default Groovy map constructor…

    class AuthorViewModel {
        def authorService
        def author
    }
    
    def authorViewModel = new AuthorViewModel(author: Author.get(1))
    assert authorViewModel.authorService != null

Autowiring occurs *after* the constructor has completed.

### Initialisation

After autowiring has occurred, the usual initialisation methods will be called on the view model instance via the [initializeBean](http://static.springsource.org/spring/docs/current/api/org/springframework/beans/factory/config/AutowireCapableBeanFactory.html#initializeBean(java.lang.Object, java.lang.String)) method.

### Reloading

View model classes can be hot reloaded during development time, like services and controllers.
    