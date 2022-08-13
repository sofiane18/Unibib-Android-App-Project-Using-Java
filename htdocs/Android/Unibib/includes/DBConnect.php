<?php

    class DbConnect{
        
        private $pdo;

        function __construct()
        {
            
        }

        function connect(){
            include_once dirname(__FILE__).'/Constants.php';

            $dns = 'mysql:host='.DB_HOST.';dbname='.DB_NAME;

            $this->pdo = new PDO($dns,DB_USER,DB_PASSWORD);

            //If Theres Any Errors
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            return $this->pdo;
        }
    }