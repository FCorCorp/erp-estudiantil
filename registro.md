# Registro de desarrollo



### Pantalla principal y datos precargados

09/05/2020

Lo primero que deberemos realizar sera la pantalla con los datos precargados del modulo.

- Se definieron los modelos y las clases.
- Falta definir como se accedera al elemento que se muestra en el JTree para cargar el resto de los datos para mostrar.

12/05/2020

- Se crearon y se muestran los datos precargados en la pantalla principal
- Falta refinar la vista con datos de los elementos seleccionados -> Algoritmo que obtenga datos seleccionados se lo puede sacar de la primera version.

16/05/2020

- Se realizo el algoritmo para obtener el elemento seleccionado en el Arbol de la vista principal (el camino mas concretamente) y se muestra el tipo de elemento junto con su codigo en la vista principal.
- Hay que agregar a la vista principal al seleccionar una linea, la opcion de inspeccionarla para realizar el CRUD de las maquinas.

30/05/2020

* Se creo un array de maquinas en el main para simular la base de datos de las maquinas, pero no se la utiliza, se lo deja para la segunda etapa.
* Se creo la clase maquina con sus detalles y relaciones.
* Se creo la vista de gestion de linea (VLinea) con su opcion para abrir y poner en el titulo de la misma el nombre de la linea que se inspecciona.
* Se agrego el boton de Inspeccionar con su propiedad de solo activarse cuando se seleccione una linea

La etapa del modulo finalizara cuando se presiona el boton "Inspeccionar" y se muestra la vista de la linea.



### Ventana de Inspeccion de linea