<?php
require 'config/configStock.php';
/**
 *Date: 25/07/2019
 Description: 
 DisplayJson.php selects the stock table complete and it sends the JSON to the JAVA files.
 DisplayJson.php selecciona la tabla stock completa y la envía por un JSON a los archivos jasva.

 *Created by: Hernán Medina
 *Copyright 2019 ITeDA
 */
class DisplayJson{
    function getAllJson(){

        $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);

        $json = array();
        $status="status";
        $message = "message";
        try{
            $sqlQuery = "SELECT * FROM stock";
            $result = mysqli_query($con,$sqlQuery);
            mysqli_fetch_all($result,MYSQLI_ASSOC);

            foreach($result as $data){
                array_push($json,
                    array( 'stock_id'    => $data['stock_id'],
                           'description' => $data['description'],
                           'm_number'    => $data['m_number'],
                           'location'    => $data['location'],
                           'dk_number'   => $data['dk_number'],
                           'quantity'    => $data['quantity']));
            }
        } catch (Exception $e){
            echo "Error while displaying json : " . $e->getMessage();
        }
        
        if($sqlQuery){
            echo json_encode(array("food"=>$json,$status=>1,$message=>"Success"));
        } else {
            echo json_encode(array("food"=>null,$status=>0, $message=>"Failed while displaying data description"));
        }
    }
}
$json = new DisplayJson();
$json->getAllJson();
?>
