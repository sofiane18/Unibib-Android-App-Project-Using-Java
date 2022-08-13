<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        $log;
        require_once '../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $log = $db->loginUser($_POST['email'],$_POST['password']);

        }
        catch (exception $e) {
            $response['exception'] = $e;
        }

        if(!isset($e)){
            if($log){
                $response['error'] = false;
                $response['message'] = 'Login successfully';
            }else{
                $response['error'] = true;
                $response['message'] = 'Wrong Informations';
            }
        }else{
            $response['error'] = true;
            $response['message'] = $e;
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);
