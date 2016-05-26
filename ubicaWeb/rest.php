<?php
require "Slim/Slim.php";

include "NotORM/NotORM.php";

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$pdo = new PDO("mysql:host=localhost;port=3306;dbname=rest", "root","");
$db = new NotORM($pdo);


$app->get('/personas', function () use ($app, $db) {
	
	$personas = array();
    foreach ($db->persona() as $persona) {
        $personas[]  = array(
            "idPersona" => $persona["ID_PERSONA"],
            "nombre" => $persona["NOMBRES"],
            "apellidos" => $persona["APELLIDOS"]
        );
    }

    echo json_encode(array("status" => true, "personas" => $personas));

});

$app->get('/persona/:id', function ($id) use ($app, $db) {
	
	$persona = $db->persona()->where("ID_PERSONA", $id);
	
	if($data = $persona->fetch()){
		$persona = array(
            "idPersona" => $data["ID_PERSONA"],
            "nombre" => $data["NOMBRES"],
            "apellidos" => $data["APELLIDOS"]
        );
		echo json_encode(array("status" => true, "persona" => $persona));	
	}else{
		echo json_encode(array("status" => false, "message" => "Persona ID $id no existe"));
	}

});

$app->post('/persona', function () use ($app, $db) {

	$persona = $app->request->post();
	$result = $db->persona()->insert($persona);
	
	if($data = $result->fetch()){
		$persona2 = array(
	            "idPersona" => $data["ID_PERSONA"],
	            "nombre" => $data["NOMBRES"],
	            "apellidos" => $data["APELLIDOS"]
	    );
	    echo json_encode(array("status" => true,"message" => "Se registro", "persona" => $persona2));
	}else{
		echo json_encode(array("status" => false, "message" => "No se pudo registrar"));
	}    

});

$app->put('/persona/:id', function ($id) use ($app, $db) {

	$persona = $db->persona()->where("ID_PERSONA", $id);

	if ($persona->fetch()) {
		$put = $app->request->put();
		$result = $persona->update($put);
		echo json_encode(array("status" => true, "message" => "Se actualizo", "result" => $result));
	}else{
		echo json_encode(array("status" => false, "message" => "Persona ID $id no existe"));
	}

});

$app->delete('/persona/:id', function ($id) use ($app, $db) {

	$persona = $db->persona()->where("ID_PERSONA", $id);

	if ($persona->fetch()) {
		$result = $persona->delete();
		echo json_encode(array("status" => true, "message" => "Se elimino", "result" => $result));
	}else{
		echo json_encode(array("status" => false, "message" => "Persona ID $id no existe"));
	}

});

$app->response()->header("Content-Type", "application/json");

 $app->run();

?>