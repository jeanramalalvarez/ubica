<?php
require "Slim/Slim.php";

include "NotORM/NotORM.php";

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

$pdo = new PDO("mysql:host=localhost;port=3306;dbname=1162218", "1162218","P4ssw0rd1zz");
//$pdo = new PDO("mysql:host=localhost;port=3306;dbname=ubica", "root","admin");//local_casa
//$pdo = new PDO("mysql:host=localhost;port=3306;dbname=ubica", "root","");//loca_trabajo
//$pdo = new PDO("mysql:host=localhost;port=3306;dbname=a7818608_ubicap", "a7818608_ubicap","a7818608_ubicap");//ubicaapp.comlu.com
//$pdo = new PDO("mysql:host=db4free.net;port=3306;dbname=ubica", "ubica","P4ssw0rd1zz"); //db4free
$db = new NotORM($pdo);

date_default_timezone_set('America/Lima');

$app->get('/seguimientos', function () use ($app, $db) {
	
	try {
		
		$seguimientos = array();
		foreach ($db->seguimiento as $seguimiento) {
			$seguimientos[]  = array(
				"idSegui" => $seguimiento["ID_SEGUI"],
				"deImei" => $seguimiento["DE_IMEI"],
				"nuLati" => $seguimiento["NU_LATI"],
				"nuLong" => $seguimiento["NU_LONG"],
				"deBate" => $seguimiento["DE_BATE"],
				"deDire" => $seguimiento["DE_DIRE"],
				"feSegui" => $seguimiento["FE_SEGUI"],
				"inRegiActi" => $seguimiento["IN_REGI_ACTI"],
				"feCrea" => $seguimiento["FE_CREA"],
				"coUsuaCrea" => $seguimiento["CO_USUA_CREA"],
				"ipUsuaCrea" => $seguimiento["IP_USUA_CREA"],
				"feModi" => $seguimiento["FE_MODI"],
				"coUsuaModi" => $seguimiento["CO_USUA_MODI"],
				"ipUsuaModi" => $seguimiento["IP_USUA_MODI"],
				"feMovl" => $seguimiento["FE_MOVL"]
			);
		}

		echo json_encode(array("status" => true, "seguimientos" => $seguimientos));
	
	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}

});

$app->get('/seguimientos/:imei', function ($imei) use ($app, $db) {
	
	try {
		
		$seguimientos = array();
		foreach ($db->seguimiento->where("DE_IMEI", $imei) as $seguimiento) {
			$seguimientos[]  = array(
				"idSegui" => $seguimiento["ID_SEGUI"],
				"deImei" => $seguimiento["DE_IMEI"],
				"nuLati" => $seguimiento["NU_LATI"],
				"nuLong" => $seguimiento["NU_LONG"],
				"deBate" => $seguimiento["DE_BATE"],
				"deDire" => $seguimiento["DE_DIRE"],
				"feSegui" => $seguimiento["FE_SEGUI"],
				"inRegiActi" => $seguimiento["IN_REGI_ACTI"],
				"feCrea" => $seguimiento["FE_CREA"],
				"coUsuaCrea" => $seguimiento["CO_USUA_CREA"],
				"ipUsuaCrea" => $seguimiento["IP_USUA_CREA"],
				"feModi" => $seguimiento["FE_MODI"],
				"coUsuaModi" => $seguimiento["CO_USUA_MODI"],
				"ipUsuaModi" => $seguimiento["IP_USUA_MODI"],
				"feMovl" => $seguimiento["FE_MOVL"]
			);
		}

		echo json_encode(array("status" => true, "seguimientos" => $seguimientos));
		
	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}

});

$app->get('/seguimiento/:id', function ($id) use ($app, $db) {
	
	try {
		
		$seguimiento = $db->seguimiento->where("ID_SEGUI", $id);
		
		if($seguimiento = $seguimiento->fetch()){
			$seguimiento = array(
				"idSegui" => $seguimiento["ID_SEGUI"],
				"deImei" => $seguimiento["DE_IMEI"],
				"nuLati" => $seguimiento["NU_LATI"],
				"nuLong" => $seguimiento["NU_LONG"],
				"deBate" => $seguimiento["DE_BATE"],
				"deDire" => $seguimiento["DE_DIRE"],
				"feSegui" => $seguimiento["FE_SEGUI"],
				"inRegiActi" => $seguimiento["IN_REGI_ACTI"],
				"feCrea" => $seguimiento["FE_CREA"],
				"coUsuaCrea" => $seguimiento["CO_USUA_CREA"],
				"ipUsuaCrea" => $seguimiento["IP_USUA_CREA"],
				"feModi" => $seguimiento["FE_MODI"],
				"coUsuaModi" => $seguimiento["CO_USUA_MODI"],
				"ipUsuaModi" => $seguimiento["IP_USUA_MODI"],
				"feMovl" => $seguimiento["FE_MOVL"]
			);
			echo json_encode(array("status" => true, "seguimiento" => $seguimiento));	
		}else{
			echo json_encode(array("status" => false, "message" => "ID $id no existe"));
		}
		
	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}

});

$app->post('/seguimiento', function () use ($app, $db) {

	try {
		
		$date = getDateString('Y-m-d H:i:s');

		$seguimiento = $app->request->post();

		$lat = null;
		$lng = null;
		
		if($seguimiento["latlng"] != null){
			$latlng = explode(',', $seguimiento["latlng"]);
			$lat = $latlng[0];
			$lng = $latlng[1];
		}else{
			$lat = $seguimiento["nuLati"];
			$lng = $seguimiento["nuLong"];
		}
		
		$lat = trim($lat);
		$lng = trim($lng);
		
		$direccion = null;
		$numero = null;
		$avenida = null;
		$distrito = null;
		$provincia = null;
		$departamento = null; 
		$pais = null;
		$resutaldo = true;
		//$result = getDireccion($lat, $lng, $direccion, $numero, $avenida, $distrito, $provincia, $departamento, $pais);
		
		$result = getDireccion($lat, $lng);
		
		if($result["estado"] == 'OK'){
			$direccion = $result["direccion"];
		}else{
			$direccion = $result["mensaje"];
		}
		
		$seguimiento = array(
				"DE_IMEI" => $seguimiento["deImei"],
				"NU_LATI" => $lat,
				"NU_LONG" => $lng,
				"DE_BATE" => $seguimiento["deBate"],
				"DE_DIRE" => $direccion,
				"FE_SEGUI" => $date,
				"FE_CREA" => $date,
				"CO_USUA_CREA" => 'APP',
				"IP_USUA_CREA" => get_client_ip(),
				"FE_MOVL" => $seguimiento["feMovl"]
			);
			
		$seguimiento = $db->seguimiento->insert($seguimiento);
		
		if($seguimiento != false){
			
			$seguimiento = $db->seguimiento->where("ID_SEGUI", $seguimiento["id"]);
			$seguimiento = $seguimiento->fetch();
			$seguimiento = array(
				"idSegui" => $seguimiento["ID_SEGUI"],
				"deImei" => $seguimiento["DE_IMEI"],
				"nuLati" => $seguimiento["NU_LATI"],
				"nuLong" => $seguimiento["NU_LONG"],
				"deBate" => $seguimiento["DE_BATE"],
				"deDire" => $seguimiento["DE_DIRE"],
				"feSegui" => $seguimiento["FE_SEGUI"],
				"inRegiActi" => $seguimiento["IN_REGI_ACTI"],
				"feCrea" => $seguimiento["FE_CREA"],
				"coUsuaCrea" => $seguimiento["CO_USUA_CREA"],
				"ipUsuaCrea" => $seguimiento["IP_USUA_CREA"],
				"feModi" => $seguimiento["FE_MODI"],
				"coUsuaModi" => $seguimiento["CO_USUA_MODI"],
				"ipUsuaModi" => $seguimiento["IP_USUA_MODI"],
				"feMovl" => $seguimiento["FE_MOVL"]
			);
			
			echo json_encode(array("status" => true,"message" => "Se registro", "seguimiento" => $seguimiento));
		}else{
			echo json_encode(array("status" => false, "message" => "No se pudo registrar"));
		}
	
	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}
	
});

$app->put('/seguimiento/:id', function ($id) use ($app, $db) {
	
	try {
		
		$seguimiento = $db->seguimiento->where("ID_SEGUI", $id);

		if ($seguimiento->fetch()) {
			$put = $app->request->put();
			$seguimiento = $seguimiento->update($put);
			echo json_encode(array("status" => true, "message" => "Se actualizo", "seguimiento" => $seguimiento));
		}else{
			echo json_encode(array("status" => false, "message" => "ID $id no existe"));
		}
	
	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}

});

$app->delete('/seguimiento/:id', function ($id) use ($app, $db) {
	
	try {
	
		$seguimiento = $db->seguimiento->where("ID_SEGUI", $id);

		if ($seguimiento->fetch()) {
			$seguimiento = $seguimiento->delete();
			echo json_encode(array("status" => true, "message" => "Se elimino", "seguimiento" => $seguimiento));
		}else{
			echo json_encode(array("status" => false, "message" => "ID $id no existe"));
		}

	} catch (Exception $e) {
		echo json_encode(array("status" => false, "message" => $e->getMessage()));
	}
	
});

function get_client_ip() {
    $ipaddress = '';
    if (getenv('HTTP_CLIENT_IP'))
        $ipaddress = getenv('HTTP_CLIENT_IP');
    else if(getenv('HTTP_X_FORWARDED_FOR'))
        $ipaddress = getenv('HTTP_X_FORWARDED_FOR');
    else if(getenv('HTTP_X_FORWARDED'))
        $ipaddress = getenv('HTTP_X_FORWARDED');
    else if(getenv('HTTP_FORWARDED_FOR'))
        $ipaddress = getenv('HTTP_FORWARDED_FOR');
    else if(getenv('HTTP_FORWARDED'))
       $ipaddress = getenv('HTTP_FORWARDED');
    else if(getenv('REMOTE_ADDR'))
        $ipaddress = getenv('REMOTE_ADDR');
    else
        $ipaddress = 'UNKNOWN';
    return $ipaddress;
}

function getDateString($format){
	//$dt = new DateTime();
	//$date = $dt->format('Y-m-d H:i:s');
	return date($format);
}

function getDireccion($lat, $lng){

	$resutaldo = array();
	$direccion = null;
	$numero = null;
	$avenida = null;
	$distrito = null;
	$provincia = null;
	$departamento = null; 
	$pais = null;
	
	try {
		
		$json = file_get_contents('http://maps.googleapis.com/maps/api/geocode/json?latlng='.$lat.','.$lng.'&sensor=true');
		
		$obj = json_decode($json);
		
		if($obj->status == 'OK'){
			
			foreach ($obj->results as $results) {
				if($results->types[0] == "street_address"){
					foreach ( $results->address_components as $direccion1) {
						if($numero == null && $direccion1->types[0] == "street_number"){
							$numero = $direccion1->long_name;
							continue;
						}
						if($avenida == null && $direccion1->types[0] == "route"){
							$avenida = $direccion1->long_name;
							continue;
						}
						if($distrito == null && $direccion1->types[0] == "locality"){
							$distrito = $direccion1->long_name;
							continue;
						}
						if($provincia == null && $direccion1->types[0] == "administrative_area_level_2"){
							$provincia = $direccion1->long_name;
							continue;
						}
						if($departamento == null && $direccion1->types[0] == "administrative_area_level_1"){
							$departamento = $direccion1->long_name;
							continue;
						}
						if($pais == null && $direccion1->types[0] == "country"){
							$pais = $direccion1->long_name;
							continue;
						}
					}
				}
			}
			
			foreach ($obj->results as $results) {
				if($results->types[0] == "locality"){
					foreach ( $results->address_components as $direccion1) {
						if($distrito == null && $direccion1->types[0] == "locality"){
							$distrito = $direccion1->long_name;
							continue;
						}
						if($provincia == null && $direccion1->types[0] == "administrative_area_level_2"){
							$provincia = $direccion1->long_name;
							continue;
						}
						if($departamento == null && $direccion1->types[0] == "administrative_area_level_1"){
							$departamento = $direccion1->long_name;
							continue;
						}
						if($pais == null && $direccion1->types[0] == "country"){
							$pais = $direccion1->long_name;
							continue;
						}
					}
				}
			}
			
			foreach ($obj->results as $results) {
				if($results->types[0] == "route"){
					foreach ($results->address_components as $direccion1) {
						if($avenida == null && $direccion1->types[0] == "route"){
							$avenida = $direccion1->long_name;
							continue;
						}
						if($provincia == null && $direccion1->types[0] == "administrative_area_level_2"){
							$provincia = $direccion1->long_name;
							continue;
						}
						if($departamento == null && $direccion1->types[0] == "administrative_area_level_1"){
							$departamento = $direccion1->long_name;
							continue;
						}
						if($pais == null && $direccion1->types[0] == "country"){
							$pais = $direccion1->long_name;
							continue;
						}
					}
				}
			}

			if($avenida != null){
				if($numero != null){
					$direccion = $avenida.' '.$numero.', '.$distrito.', '.$provincia.', '.$pais;
				}else{
					$direccion = $avenida.', '.$distrito.', '.$provincia.', '.$pais;
				}
				$resutaldo = array(
							 "estado" => 'OK',
							 "direccion" => $direccion,
							 "avenida" => $avenida,
							 "numero" => $numero,
							 "distrito" => $distrito,
							 "provincia" => $provincia,
							 "departamento" => $departamento,
							 "pais" => $pais);
			}else{
				foreach ( $obj->results as $results) {
					if($results->types[0] == "street_address"){
						$direccion = $results->formatted_address;
					}
				}
				
				if($direccion == null){
					foreach ( $obj->results as $results) {
						if($results->types[0] == "route"){
							$direccion = $results->formatted_address;
						}
					}
				}
				
				if($direccion == null){
					$resutaldo = array("estado" => 'ERROR', "mensaje" => '[ERROR]: No se pudo obtener la direccion desde la API de Google[1]');
				}else{
					$resutaldo = array("estado" => 'OK', "direccion" => $direccion);
				}
			}
		}else{
			$resutaldo = array("estado" => 'ERROR', "mensaje" => "[ERROR]: No se pudo obtener la direccion desde la API de Google[2]");
		}
	
	} catch (Exception $e) {
		$resutaldo = array("estado" => 'EXCEPTION', "mensaje" => "[ERROR]: ".$e->getMessage());
	}
	
	return $resutaldo;

}

$app->response()->header("Content-Type", "application/json");

$app->run();

?>