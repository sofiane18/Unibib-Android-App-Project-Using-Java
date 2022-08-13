<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->userDemandBook($_POST['bookRef'],$_POST['userEmail']);
        }
        catch (exception $e) {
            $response['error'] = true;
            $response['message'] = $e;
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'The demanding of the boook completed successfully';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);
