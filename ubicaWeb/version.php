<?php

//PARA HABILITAR EL DEBUGIN EN SLIM
//http://docs.slimframework.com/errors/debug/
//http://docs.slimframework.com/configuration/settings/
/*
 $app = new \Slim\Slim(array(
 'debug' => true,
 'mode' => 'development'
 ));
 */
//$app->config('debug', true);

//AGREGAR ESTAS LINEAS EN EL ARCHIVO PHP.INI PARA HABILITAR EL DEBUG.
/*
 zend_extension = "C:\xampp\php\ext\php_xdebug.dll"
 xdebug.remote_enable = true
 */

// Imprime ejemplo 'Versi�n actual de PHP: 5.3.8'
echo 'Versi�n actual de PHP: ' . phpversion();

// Imprime ejemplo '2.0' o nada si la extensi�n no est� habilitada
echo phpversion('tidy');
?>