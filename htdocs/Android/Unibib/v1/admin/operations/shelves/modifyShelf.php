<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->modifyShelf($_POST['ref'],$_POST['name'],$_POST['sides'],$_POST['rows'],$_POST['cols']);
        }
        catch (exception $e) {
            $response['error'] = true;
            $response['message'] = $e;
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'The addition of the shelf completed successfully';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);
