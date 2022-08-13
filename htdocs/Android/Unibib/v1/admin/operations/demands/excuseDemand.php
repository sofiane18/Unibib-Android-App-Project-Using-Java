<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->excuseDemand($_POST['ref']);
        }
        catch (exception $e) {
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'Excusing for the demand was successful';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);