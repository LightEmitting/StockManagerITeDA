 <?php
 /*

Date: 25/07/2019

Description: 
FindString.php gets certain data of the "stock" table of "Usuarios" database.

FindString.php obtiene ciertos datos de la tabla "stock" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/
require 'config/configStock.php';

// Create connection
$con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

//$query = "SELECT quantity, location FROM stock WHERE m_number = ?";
//$statement = $con->prepare($query);
$string1 = "%CMP%";
$string2 = "%HRR%";
$string3 = "%CSM%";
$string4 = "%INS%";

$statement1 = mysqli_prepare($con, "SELECT FORMAT((SELECT COUNT(*)*100/(SELECT COUNT(*) FROM stock) FROM stock where `location` like ?),1) ");
$statement1->bind_param( "s", $string1 );
$statement1->execute();
$statement1->bind_result($cmp);
$statement1->fetch();
$statement1->close();


$statement2 = mysqli_prepare($con, "SELECT FORMAT((SELECT COUNT(*)*100/(SELECT COUNT(*) FROM stock) FROM stock where `location` like ?),1) ");
$statement2->bind_param( "s", $string2 );
$statement2->execute();
$statement2->bind_result($hrr);
$statement2->fetch();
$statement2->close();


$statement3 = mysqli_prepare($con, "SELECT FORMAT((SELECT COUNT(*)*100/(SELECT COUNT(*) FROM stock) FROM stock where `location` like ?),1) ");
$statement3->bind_param( "s", $string3 );
$statement3->execute();
$statement3->bind_result($csm);
$statement3->fetch();
$statement3->close();

$statement4 = mysqli_prepare($con, "SELECT FORMAT((SELECT COUNT(*)*100/(SELECT COUNT(*) FROM stock) FROM stock where `location` like ?),1) ");
$statement4->bind_param( "s", $string4 );
$statement4->execute();
$statement4->bind_result($ins);
$statement4->fetch();

$aux = 100 - ($cmp + $hrr + $csm + $ins);
$error = number_format($aux, 1); 

$response = array();

if (is_null($cmp)) //no esta en la base de datos
	$response["success"] = "false";
else {
 	$response["cmp"] = $cmp;
 	$response["hrr"] = $hrr;
 	$response["csm"] = $csm;
 	$response["ins"] = $ins;
 	$response["err"] = $error;
	$response["success"] = "true";
}


$con->close();

echo json_encode($response);
?> 