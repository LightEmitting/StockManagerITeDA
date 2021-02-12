<?php
require 'config/configStock.php';
/*

Date: 25/07/2019

Description:
Login.php gets all data of a specific user in the "user" table of "Usuarios" database.

Login.php obtiene toda la información de un determinado usuario en la tabla "user" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/
    //autenticacion
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);

    $user = $_POST["user"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "SELECT user_id, name, familyname, email FROM user WHERE user = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $user, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $name, $familyname, $email);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["name"] = $name;
        $response["familyname"] = $familyname;
        $response["user"] = $user;
        $response["password"] = $password;
        $response["email"] = $email;
        $response["userID"]= $userID;
    }

    echo json_encode($response);
?>
