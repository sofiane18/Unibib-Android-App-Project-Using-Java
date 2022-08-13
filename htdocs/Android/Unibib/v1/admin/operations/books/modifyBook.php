<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            $db->modifyBook($_POST['ref'],$_POST['title'],$_POST['author'],$_POST['description'],$_POST['edition'],$_POST['tags'],$_POST['shelf'],$_POST['side'],$_POST['row'],$_POST['col'],$_POST['quantity'],$_POST['image'],$_POST['pdf'],$_POST['releaseY']);
        }
        catch (exception $e) {
            $response['exception'] = $e;
        }

        if(!isset($e)){
            $response['error'] = false;
            $response['message'] = 'Updating of the book completed successfully';
        }




    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);
