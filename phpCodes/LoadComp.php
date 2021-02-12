<?php
require 'config/configStock.php';
/*
Date: 25/07/2019

Description: 
1) LoadComp.php loads all the component/tool/consumable/etc data in the "stock" table of "Usuarios" database.
2) LoadComp.php records all new component/tool/consumable/etc in the "record" table of "Usuarios" database.

1) LoadComp.php carga toda la información del componente/herramienta/consumible/etc en la tabla "stock" de la base de datos "Usuarios".
2) LoadComp.php registra todo componente/herramienta/consumible/etc agregado en la tabla "record" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/

    //mysqli_connect parameters
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);

    $description_prev = "Does not exist";
    $quantity_prev = "Does not exist";
    $location_prev = "Does not exist";
    $description_act =  $_POST["description"];
    $quantity_act = $_POST["quantity"];
    $location_act = $_POST["Location"];
    $dk_number = $_POST["dk_number"];
    $m_number = $_POST["MFNumber"];

    $statement = mysqli_prepare($con, "INSERT INTO stock (description, quantity, location, dk_number, m_number) VALUES (?, ?, ?, ?, ?)");
        //ligar parámetros para marcadores
    mysqli_stmt_bind_param($statement, "sssss", $description_act, $quantity_act, $location_act, $dk_number, $m_number);
        //ejecutar ejecutar la consulta
    mysqli_stmt_execute($statement);
        
    //begin record
    $user = $_POST["user"];
    $action = "new";
    
    $stock_id = $statement -> insert_id;

    $statement = mysqli_prepare($con, "INSERT INTO record(user, action, description_prev, description_act, quantity_prev, quantity_act, location_prev, location_act, stock_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        //ligar parámetros para marcadores
    mysqli_stmt_bind_param($statement, "sssssssss", $user, $action, $description_prev, $description_act, $quantity_prev, $quantity_act, $location_prev, $location_act, $stock_id);

        //ejecutar la consulta
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    $statement->close();
    //end record
    $con->close();
    echo json_encode($response);
    
?>
