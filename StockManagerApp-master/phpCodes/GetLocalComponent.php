 <?php
 /*

Date: 17/1/20

Description: 

GetComponent.php obtiene datos de la tabla "stock" de la base de datos "Usuarios" que sirven como api de dk pero sobre los componentes internos.

Created by: Hernán Medina

copyright 2019 ITeDA

*/
require 'config/configStock.php';

	$con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);

	$barcode = $_POST["barcode"];

	$statement = mysqli_prepare($con, "SELECT stock_id, location, description, quantity, m_number FROM stock WHERE dk_number = ?");
	mysqli_stmt_bind_param($statement, "s", $barcode);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $location, $description, $quantity, $m_number);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)){
		$response["success"] = true;
		$response["id"] = $id;
		$response["location"] = $location;
		$response["description"] = $description;
		$response["quantity"] = $quantity;
		$response["m_number"] = $m_number;
	}

	echo json_encode($response);
	
?> 