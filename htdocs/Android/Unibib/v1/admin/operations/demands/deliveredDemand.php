<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->deliveredDemand($_POST['ref']);
        }
        catch (exception $e) {
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'Setting the demand as delivered was successful';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);