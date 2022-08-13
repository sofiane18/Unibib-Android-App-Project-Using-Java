<?php

    $response = array();

    if($_SERVER['REQUEST_METHOD']=='POST'){
        
        require_once '../../../../includes/DBOperations.php';
        $db = new DBOperations();
        $response = $db->getUserTagsListRecyclerView();



    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);