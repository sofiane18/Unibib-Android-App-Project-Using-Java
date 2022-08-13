<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->deleteBook($_POST['ref']);
        }
        catch (exception $e) {
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'deleting the book completed successfully';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);