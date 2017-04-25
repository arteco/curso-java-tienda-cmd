# PRÁCTICA - curso-java-tienda-cmd

## Enunciado

Creación de un programa Java por Consola que permita a los clientes realizar pedidos y al gestor gestionar su stock.

### Limitaciones del programa

El almacenamiento de la información será en memoria, en estructuras de objetos como listas y diccionarios, no será persistente, por lo que en cada ejecución se deberán dar de alta los productos.

Los usuarios podrán interactuar con la tienda mediante comandos por consola en formato texto. Para ello se deberá hacer uso de apache commons-cli. La ejecución del programa deberá terminar con la escritura del comando 'quit'.

### Perfiles de usuario:

1. Gestor: debe poder gestionar el producto disponible, reponer stock y revisar las compras realizadas.
1. Comprador: debe poder comprar usando un carrito de compra. 


### Requisitos Funcionales:

1. El gestor debe poder dar de alta, modificar, consultar y eliminar cualquier producto de su stock.
1. El gestor debe poder asignar un precio y cantidad a los productos de su stock
1. El comprador puede comprar cualquier producto disponible.
1. El comprador partirá siempre de un carrito de compra vacío, y podrá comprar cualquier producto disponible en stock.
1. Al añadir un producto al carrito, el stock disponible en la tienda se debe reducir
1. Los carritos de los clientes pueden cancelarse liberando el stock
1. Una vez el carrito esté confirmado, se imprimirá el resumen del carrito por consola y se liberarán los recursos.
1. El gestor debe poder ver un resumen de las compras realizadas.


## Metodología.

Se deberá utilizar una metodología orientada a desarrollo por Tests. 

### PASO 1 - Identificación y definición de INTERFACES:

1. Identificar las diferentes INTERFACES que intervienen: Shop, Customer, Manager, Item, Carry, ...
1. Todas las interfaces deben ser accesibles vía un único punto de entrada, también una interfaz: Environment. Por ejemplo: Environment env = new InMemmoryEnviroment(); Shop shop = env.getShop();
	
### PASO 2 - Enumeración de los test necesarios que validen los requisitos funcionales

1. Por cada requisito funcional deberá haber uno o más tests que den soporte y validen las operaciones solicitadas sobre el conjunto de interfaces definidas. Por ejemplo deberá haber un test destinado a validar que el gestor puede dar de alta un producto. Otro test para que el gestor pueda modificar un producto, etc... Y así sucesivamente para todos los requisitos directos e indirectos de los Requisitos Funcionales.
1. Este PASO 2, debe dar como resultado la identificación de los test, la identificación de todas las operaciones de cada una de las interfaces, y sobre todo, la condición de validación para cada test que permita validar el correcto funcionamiento de cada operación de las interfaces.

### PASO 3 - Implementación I de las interfaces

1. Para cada una de las interfaces se deberá crear una clase que implemente dicha interfaz y de cuerpo a las operaciones definidas en cada interfaz. Las operaciones deben trabajar con estructuras en memoria como listas y diccionarios de Java. No debe ser persistente, por lo que cada vez que se arranque la aplicación se deberá inicializar la tienda.
1. Tras la IMPLEMENTACIÓN en este punto, todos los test unitarios deberán ejecutarse con éxito.
1. Tras tener los test con ejecución exitosa de todas las operaciones, se deberá implementar el ejecutor de comandos por consola para interactuar con él de forma interactiva y por comandos de texto. Los comandos deben acceder a las interfaces vía el Enviroment (no deben usar las implementaciones concretas si no las interfaces), de tal manera que no se deba realizar ninguna modificación en la capa de ejecución de comandos para el siguiente PASO 4.


### PASO 4 - Implementación II de las interfaces.

1. Manteniendo las mismas interfaces, se creará una segunda implementación de (sólo) las entidades necesarias, manteniendo la primera, que permita realizar las mismas operaciones definidas en los requisitos funcionales, pero en vez de utilizar una estructura de datos en memoria como listas y diccionarios de Java, se usará una base de datos SQL empotrada en la aplicación: hsqldb. De tal manera que la implementación usando listas y diccionarios se deberán modificar para que utilice tablas SQL.
1. A la hora de arrancar el programa por consola se debe poder especificar si se desea utilizar la versión en memoria o la versión de hsqldb.

