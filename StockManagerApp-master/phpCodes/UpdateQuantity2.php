<?php
require 'config/configStock.php';
/**
 *Date: 25/07/2019
 *2) UpdateQuantity2.php updates certain data in the "dk_token" table of "Usuarios" database.
 *1) UpdateQuantity2.php actualiza ciertos datos  de la tabla "dk_token" de la base de datos "Usuarios".
 *Created by: Hernán Medina
 *Copyright 2019 ITeDA
 */
    /* MySQL Conexion*/
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
    // Chequea coneccion
    if($con === false){
        die("ERROR: No pudo conectarse con la DB. " . mysqli_connect_error());
    }
    
    $access_token_next = $_POST["access_token_next"];
    $refresh_token_next = $_POST["refresh_token_next"];
    $refresh_token_reg = $_POST["refresh_token_reg"];

    
    // Ejecuta la actualizacion del registro
    //$statement = "UPDATE dk_token SET access_token='$access_token_next', refresh_token='$refresh_token_next'  WHERE refresh_token='$refresh_token_reg'";
    
    $statement = mysqli_prepare($con, "UPDATE dk_token SET access_token= ?, refresh_token= ? WHERE refresh_token= ?");
    mysqli_stmt_bind_param($statement, "iis", $access_token_next, $refresh_token_next, $refresh_token_reg);
    mysqli_stmt_execute($statement);
    
    if(mysqli_query($con, $statement)){
        echo "Registro actualizado.";
    } else {
        echo "ERROR: No se ejecuto $statement. " . mysqli_error($con);
    }
    // Cierra la conexion
    mysqli_close($con);
?>
