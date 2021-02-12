<?php
require 'config/configStock.php';
/**
 *Date: 25/07/2019
 *Description:
 *1) UpdateQuantity.php updates the component's quantity in the "stock" table of "Usuarios" database.
 *2) UpdateQuantity.php registers the change quantity in the "record" table of "Usuarios" database.
 *1) UpdateQuantity.php actualiza la cantidad del componente en la tabla "stock" de la base de datos "Usuarios".
 *2) UpdateQuantity.php registra la cantidad modificada en la tabla "record" de la base de datos "Usuarios".
 *Created by: Hernán Medina
 *Copyright 2019 ITeDA
 */

    /* MySQL Conexion*/
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
    // Chequea coneccion
    if($con == false){
        die("ERROR: No pudo conectarse con la DB. " . mysqli_connect_error());
    }
    
    $user = $_POST["user"];
    $description_act = $_POST["desc_act"];
    $stock_id = $_POST["stock_id"];

    // Ejecuta la actualizacion del registro    
    $statement = mysqli_prepare($con, "UPDATE stock SET description = ? WHERE stock_id = ? ");
    mysqli_stmt_bind_param($statement, "ss", $description_act, $stock_id);
    mysqli_stmt_execute($statement);
    $statement->close();

    //record
    $action = "Change Description";
    $description_prev = $_POST["desc_prev"];
    $qty_prev = $_POST["qty"];
    $qty_act = "No changes";
    $location_prev = $_POST["location"];
    $location_act = "No changes";
        
    $statement1 = mysqli_prepare($con, "INSERT INTO record(user, action, description_prev, description_act, quantity_prev, quantity_act, location_prev, location_act, stock_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        //ligar parámetros para marcadores
    mysqli_stmt_bind_param($statement1, "sssssssss", $user, $action, $description_prev, $description_act, $qty_prev, $qty_act, $location_prev, $location_act, $stock_id);
        //ejecutar ejecutar la consulta
    mysqli_stmt_execute($statement1);
    $statement1->close();
    $con->close();
    
    $response = array();
    $response["success"] = true;  
    echo json_encode($response);
   // mysqli_close($con);   
    $response1 = array();
    $response1["success"] = true;  
    echo json_encode($response1);

?>
