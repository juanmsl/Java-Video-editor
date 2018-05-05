# Java-Video-editor

------------------------------------------------------------------------------------------------------
    INSTALACIÓN
------------------------------------------------------------------------------------------------------

1) Descargar el JMF (Java Media Framework) de Oracle de la siguiente pagina:
   http://www.oracle.com/technetwork/java/javase/download-142937.html

2) Instalar la libreria JMF

3) Descargar el instalador para la libreria 'Xuggle' desde la pagina:
   http://www.compadre.org/osp/items/detail.cfm?ID=11606
   ó directamente descargando el instalador de windows
   http://www.compadre.org/osp/images/tracker/OSPXuggle-3.4.1012-windows-installer.exe

4) Instalar la libreria 'Xuggle' recien descargada

5) Tras finalizar la instalación, asegurese de que su computador tiene las siguientes variables
   globales, revisando en:
   Equipo > Click derecho > Propiedades > Configuración avanzada del sistema > Variables de entorno

  Las cuales deben ser:
    - [XUGGLE_HOME] [C:\Program Files (x86)\Xuggle]
    - [PATH] [******;C:\Program Files (x86)\Xuggle\bin] -> Agregar la ruta al path, junto a todo.

6) Asegurese de tener instalado el JDK de Oracle de x32bits, en caso de no tenerlo puede descargarlo
   desde la siguiente pagina:
   http://www.oracle.com/technetwork/es/java/javase/downloads/index.html

7) Cuando lo tenga instalado debe poder configurar el proyecto de la siguiente manera.

------------------------------------------------------------------------------------------------------
    CONFIGURACIÓN DEL PROYECTO
------------------------------------------------------------------------------------------------------

1) Cargar el proyecto en eclipse

2) En las propiedades del proyecto, debe configurar que el JDK respectivo sea el de x32bits, esto se
   configura en:
   Proyecto > Click derecho > Propiedades > Java Build Path > Libraries
   Se selecciona el elemento 'JRE System Library', y seguido del boton 'Edit' al costado derecho de
   la ventana. Y se selecciona el JDK de x32bits, en caso de no estar, se busca en el sistema de
   archivos y se agrega.

3) Estando en la misma ventana se deben agregar los .jars de la libreria de 'Xuggle', ubicados en la
   carpeta:
   C:\Program Files (x86)\Xuggle\share\java\jars

4) Estando en la misma ventana se deben agregar los .jars de la libreria de 'JMF', ubicados en la
   carpeta:
   C:\Program Files (x86)\JMF2.1.1e\lib

5) Teniendo configurados correctamente el JDK de x32bits, la libreria 'Xuggle', y la libreria 'JMF'
   podra probar el proyecto del 'Editor simple de video'

------------------------------------------------------------------------------------------------------
    USO DEL PROGRAMA
------------------------------------------------------------------------------------------------------

1) Cargue un video en la interfaz oprimiendo el boton 'Subir video' al costado derecho del programa,
   solo se admiten videos con formato '.mpg', una vez cargado el video, este desplegara un reproductor
   para el mismo.

2) Cargar una imagen en la interfaz oprimiendo el boton 'Subir imagen' al costado derecho del
   programa, solo se admiten imagenes con formato '.png' y '.jpg'

3) Cargar un archivo de audio si el usuario quiere, esto no es necesario, en la interfaz oprimiendo el
   boton 'Subir audio' al costado derecho del programa, solo se admiten archivos de audio con
   formato '.mp3'

4) Teniendo cargado el video y la imagen al menos, por medio del boton 'Cambiar' al costado derecho de
   interfaz, puede cambiar entre la vista de imagen y la vista de video. En la vista de video puede
   vizualizar el video, reproducirlo, y en caso de haber cargado un archivo de audio, el control para
   esta pista estara situado al norte de este panel. En la vista de imagen, tanto el reproductor del
   audio como del video desapareceran permitiendole al usuario manipular la imagen por medio de
   DRAG y de CLICKS, en caso de querer cambiar el tamaño de la imagen, con el DESPLAZAMIENTO DE LA
   LA RUEDA DEL MOUSE, puede hacerlo, en el momento de cambiar a la vista de video nuevamente, la posicion
   en la que se encuentre la imagen se guardara con el nanosegundo del video en el que esta detenido,
   para su posterior animación. En el momento de cambiar la vista a modo imagen, el video se detendra,
   y en caso de cambiar la vista a modo video, el video se reproducira automaticamente.

5) Cuando tenga las posiciones de la imagen ya definidas puede comenzar la exportación del video
   oprimiendo el boton 'Generar', al costado derecho la ventana. Se le preguntara la dirección en la
   que guardar el archivo de salida, y el respectivo nombre con su extención, los formatos validos
   para exportar el video, son '.mp4' y '.avi'

   ------------------------------------------------------------------------------------------------------
      NOTA: Las rutas del video a cargar, del video de salida, y del audio a cargar, no deben contener
            espacios, tildes, o caracteres especiales
   ------------------------------------------------------------------------------------------------------








                                                                 ----------------------------------------
                                                                          EDITOR DE VIDEO SIMPLE

                                                                         Materia: Desarrollo multimedial
                                                                     Universidad: Pontificia Javeriana
                                                                        Profesor: Luis Guillermo Torres
                                                                     Estudiantes: Juan Manuel Sánchez
                                                                           Fecha: 22/11/2016
                                                                 ----------------------------------------
