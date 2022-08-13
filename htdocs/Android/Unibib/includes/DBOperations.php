<?php

    class DBOperations{
        public $pdo;

        //Error Handling
        public $err = [];
        //Success Get array
        public $succ = [];
        
        //Generating ID for User
        function generateRandomRef($s) {
            $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
            $charactersLength = strlen($characters);
            $randomString = $s;

            for ($i = 0; $i < 10; $i++) {
                $randomString .= $characters[rand(0, $charactersLength - 1)];
            }
            return $randomString;
        }
        
        function __construct(){
            
            require_once dirname(__FILE__).'/DBConnect.php';

            $db = new DbConnect();

            $this->pdo = $db->connect();

            date_default_timezone_set('Africa/Algiers');

        }


        function sanatizeData($data) {
            $data = trim($data);
            $data = stripslashes($data);
            $data = htmlspecialchars($data);
            return $data;
        }



        function createAdminPersonalInfo($fname,$lname,$gender){


            if(!empty($fname)){
                $fname = $this->sanatizeData($fname);
                $fname = ucfirst($fname);
                $fname_val = preg_match("/^[a-zA-Z]*$/",$fname);
                if(!$fname_val){
                    $this->err['fname'] = 'invalid';
                }else{
                    $this->succ['fname'] = $fname;
                }
            }else{
                $this->err['fname'] = 'empty';
            }

            if(!empty($lname)){
                $lname = $this->sanatizeData($lname);
                $lname = ucfirst($lname);
                $lname_val = preg_match("/^[a-zA-Z]*$/",$lname);
                if(!$lname_val){
                    $this->err['lname'] = 'invalid';
                }else{
                    $this->succ['lname'] = $lname;
                }
            }else{
                $this->err['lname'] = 'empty';
            }

            if(!empty($gender)){

                $this->succ['gender'] = $gender;

            }else{
                $this->err['lname'] = 'empty';
            }
            

            if(empty($this->err)){
                return true;    
            }else{
                return false;
            }

        }
        
        function createAdminAdditionalPersonalInfo($birthdate,$address,$phone){

            //date_naissance
            if(!empty($birthdate)){
               $this->succ['birthdate'] = $birthdate;
            }else{
                $this->err['birthdate'] = 'empty';
            }


            //Adresse
            if(!empty($address)){
                $address = $this->sanatizeData($address);
                $address = ucfirst($address);
                $this->succ['address'] = $address;
            
            }else{
                $this->err['address'] = 'empty';
            }

            //phone
            if(!empty($phone)){
                $phone_val = preg_match("/^[0-9\+]*$/",$phone);
                $phone_len = strlen($phone);
                if(!$phone_val || $phone_len<10){
                    $this->err['phone'] = 'invalid';
                }else{
                    //Verify if there's one phone or not
                    $sql = 'SELECT phone FROM admins';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute();
                    $phones = $stmt->fetchall(PDO::FETCH_COLUMN);
                    if(in_array($phone,$phones)){
                        $this->err['phone'] = 'already_existe';
                    }else{                   
                        $this->succ['phone'] = $phone;
                    }
                }
            }else{
                $this->err['phone'] = 'empty';
            }

            if(empty($this->err)){
                return true;    
            }

        }
        
        function createUserAdditionalPersonalInfo($birthdate,$address,$phone){

            //date_naissance
            if(!empty($birthdate)){
               $this->succ['birthdate'] = $birthdate;
            }else{
                $this->err['birthdate'] = 'empty';
            }


            //Adresse
            if(!empty($address)){
                $address = $this->sanatizeData($address);
                $address = ucfirst($address);
                $this->succ['address'] = $address;
            
            }else{
                $this->err['address'] = 'empty';
            }

            //phone
            if(!empty($phone)){
                $phone_val = preg_match("/^[0-9\+]*$/",$phone);
                $phone_len = strlen($phone);
                if(!$phone_val || $phone_len<10){
                    $this->err['phone'] = 'invalid';
                }else{
                    //Verify if there's one phone or not
                    $sql = 'SELECT phone FROM users';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute();
                    $phones = $stmt->fetchall(PDO::FETCH_COLUMN);
                    if(in_array($phone,$phones)){
                        $this->err['phone'] = 'already_existe';
                    }else{                   
                        $this->succ['phone'] = $phone;
                    }
                }
            }else{
                $this->err['phone'] = 'empty';
            }

            if(empty($this->err)){
                return true;    
            }

        }

        function createAdminConnectInfo($fname,$lname,$gender,$birthdate,$address,$phone,$email,$password,$invitationCode){

            foreach ($this->err as $i) {
                unset($this->err[$i]);
            }
            unset($this->err);
            $this->err = array();

            $this->createAdminPersonalInfo($fname,$lname,$gender);
            $this->createAdminAdditionalPersonalInfo($birthdate,$address,$phone);

            //email
            if(!empty($email)){
                $email = $this->sanatizeData($email);
                $email_val = filter_var($email,FILTER_VALIDATE_EMAIL);
                if(!$email_val){
                    $this->err['email'] = 'invalide';
                }else{
                    //Verify if there's one email or not
                    $sql = 'SELECT email FROM admins';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute();
                    $emails = $stmt->fetchall(PDO::FETCH_COLUMN);
                    if(in_array($email,$emails)){
                        $this->err['email'] = 'already_existe';
                    }else{
                        $this->succ['email'] = $email;
                    }
                }
            
                
            }else{
                $this->err['email'] = 'empty';

            }

            //Password
            if(!empty($password)){
                $password = $this->sanatizeData($password);
                $password_len = strlen($password);
                if($password_len<8){
                    $err['password'] = 'short';
                }
            }else{
                $err['password'] = 'empty';
            }

            //invitation COde
            if(!empty($invitationCode)){
 
                $invitationCode = $this->sanatizeData($invitationCode);

                //Verify the invitation code
                $sql = 'SELECT code FROM invitation';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $code = $stmt->fetchall(PDO::FETCH_COLUMN);
                if(in_array($invitationCode,$code)){
                    $this->succ['invitationCode'] = $invitationCode;
                }else{
                    $this->err['invitationCode'] = 'invalide';
                }
            
                
            }else{
                $this->err['invitationCode'] = 'empty';

            }

            if(empty($this->err)){
                return true;    
            }

        }
        
        function createUserConnectInfo($fname,$lname,$gender,$birthdate,$address,$phone,$email,$password){

            foreach ($this->err as $i) {
                unset($this->err[$i]);
            }
            unset($this->err);
            $this->err = array();

            $this->createAdminPersonalInfo($fname,$lname,$gender);
            $this->createUserAdditionalPersonalInfo($birthdate,$address,$phone);

            //email
            if(!empty($email)){
                $email = $this->sanatizeData($email);
                $email_val = filter_var($email,FILTER_VALIDATE_EMAIL);
                if(!$email_val){
                    $this->err['email'] = 'invalide';
                }else{
                    //Verify if there's one email or not
                    $sql = 'SELECT email FROM users';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute();
                    $emails = $stmt->fetchall(PDO::FETCH_COLUMN);
                    if(in_array($email,$emails)){
                        $this->err['email'] = 'already_existe';
                    }else{
                        $this->succ['email'] = $email;
                    }
                }
            
                
            }else{
                $this->err['email'] = 'empty';

            }

            //Password
            if(!empty($password)){
                $password = $this->sanatizeData($password);
                $password_len = strlen($password);
                if($password_len<8){
                    $err['password'] = 'short';
                }
            }else{
                $err['password'] = 'empty';
            }


            if(empty($this->err)){
                return true;    
            }

        }

        function loginAdmin($email,$password){
            $sql = 'SELECT email,password FROM admins WHERE email=:email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $logInfo = $stmt->fetchall(PDO::FETCH_ASSOC);
            $count = $stmt->rowCount();
            if($count == 1){
                foreach($logInfo as $admin){
                    return password_verify($password,$admin['password']);
                }                
            }
            return false;
        }

        
        function loginUser($email,$password){
            $sql = 'SELECT email,password FROM users WHERE email=:email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $logInfo = $stmt->fetchall(PDO::FETCH_ASSOC);
            $count = $stmt->rowCount();
            if($count == 1){
                foreach($logInfo as $admin){
                    return password_verify($password,$admin['password']);
                }                
            }
            return false;
        }



        function getAdminProfileInfo($email){
            $sql = 'SELECT * FROM admins WHERE email=:email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $adminInfo = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = 'SELECT * FROM invitation';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $invCode = $stmt->fetchall(PDO::FETCH_ASSOC);
            $admin = array();
            foreach($adminInfo as $admin){
                foreach($invCode as $key => $value){
                    foreach($value as $key => $value){
                        $admin[$key]=$value;
                    }
                }
            }    
            return [$admin];
        }
        
        function getUserProfileInfo($email){
            $sql = 'SELECT * FROM users WHERE email=:email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $userInfo = $stmt->fetchall(PDO::FETCH_ASSOC);

            return $userInfo;
        }


        function getAdminBooksListRecyclerView(){


            $sql = 'SELECT * FROM books';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $books = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $i=0;
            foreach($books as $book){
                
                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $books[$i]['shelfName'] = $bookShelf[0]['name'];
                $books[$i]['shelfSide'] = $bookPlace[0]['side'];
                $books[$i]['shelfRow'] = $bookPlace[0]['row'];
                $books[$i]['shelfCol'] = $bookPlace[0]['col'];

                $books[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }

            return $books;



        }


        function getAdminSearchBooks($searchBy,$searchKey){


            $sql = "SELECT * FROM books WHERE $searchBy LIKE '%$searchKey%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $books = $stmt->fetchall(PDO::FETCH_ASSOC);

            $i=0;
            foreach($books as $book){
                
                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $books[$i]['shelfName'] = $bookShelf[0]['name'];
                $books[$i]['shelfSide'] = $bookPlace[0]['side'];
                $books[$i]['shelfRow'] = $bookPlace[0]['row'];
                $books[$i]['shelfCol'] = $bookPlace[0]['col'];

                $books[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }

            return $books;



        }

        function getAdminSearchShelves($searchBy,$searchKey){


            $sql = "SELECT * FROM bookshelves WHERE $searchBy LIKE '%$searchKey%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $shelves = $stmt->fetchall(PDO::FETCH_ASSOC);

            $i=0;
            foreach($shelves as $shelf){
                $sql = 'SELECT * FROM bookplace WHERE bookShelfRef = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $shelf['ref']]);
                $bookPlaces = $stmt->fetchall(PDO::FETCH_ASSOC);
                $booksList = array();
                $k = 0;
                foreach($bookPlaces as $bookPlace){
                    $books = array();
                    $sql = 'SELECT * FROM books WHERE bookPlaceRef=:ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $bookPlace['ref']]);
                    $books = $stmt->fetchall(PDO::FETCH_ASSOC);

                    if(!empty($books)){
                        $j=0;
                        foreach($books as $book){
                            $books[$j]['shelfName'] = $shelf['name'];
                            $books[$j]['shelfSide'] = $bookPlace['side'];
                            $books[$j]['shelfRow'] = $bookPlace['row'];
                            $books[$j]['shelfCol'] = $bookPlace['col'];
                            $books[$j]['bookPlace'] = $shelf['name'].", ".$bookPlace['side'].", R".$bookPlace['row'].", C".$bookPlace['col'];
                            $j++;
                        }
                    
                        $booksList[$k] = $books;
                        $k++;
                    }
                }
                
                $shelves[$i]['books'] = $booksList;
                $i++;
            }


            return $shelves;



        }

        function getAdminSearchDemands($searchBy,$searchKey){


            $sql = "SELECT * FROM demands WHERE $searchBy LIKE '%$searchKey%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $demands = $stmt->fetchall(PDO::FETCH_ASSOC);

            
            $i=0;
            foreach($demands as $demand){
                
                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $demands[$i]['demandRef']= $demand['ref'];


                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                $demands[$i]['shelfName'] = $bookShelf[0]['name'];
                $demands[$i]['shelfSide'] = $bookPlace[0]['side'];
                $demands[$i]['shelfRow'] = $bookPlace[0]['row'];
                $demands[$i]['shelfCol'] = $bookPlace[0]['col'];

                $demands[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
                
            }
            return $demands;

        }

        function getUserSearchDemands($searchBy,$searchKey,$email){

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = "SELECT * FROM demands WHERE userRef = :userRef AND ( $searchBy LIKE '%$searchKey%')";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['userRef'=>$user[0]['ref']]);
            $demands = $stmt->fetchall(PDO::FETCH_ASSOC);

            
            $i=0;
            foreach($demands as $demand){
                
                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $demands[$i]['demandRef']= $demand['ref'];


                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                $demands[$i]['shelfName'] = $bookShelf[0]['name'];
                $demands[$i]['shelfSide'] = $bookPlace[0]['side'];
                $demands[$i]['shelfRow'] = $bookPlace[0]['row'];
                $demands[$i]['shelfCol'] = $bookPlace[0]['col'];

                $demands[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
                
            }
            return $demands;

        }

        function getAdminSearchBorrowings($searchBy,$searchKey){
            $borrowings = array();
            if($searchBy=='userRef' ||$searchBy=='bookRef' ){
                $sql = "SELECT * FROM demands WHERE $searchBy LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $demands = $stmt->fetchall(PDO::FETCH_ASSOC);

                foreach($demands as $demand){
                    
                    $sql = 'SELECT * FROM borrowings WHERE demandRef = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $demand['ref']]);
                    $borrowingsBuffer = $stmt->fetchall(PDO::FETCH_ASSOC);
                    if(!empty($borrowingsBuffer)){
                        $borrowings = array_merge($borrowings,$this->getBorrowingsSearchListRecyclerView($borrowingsBuffer));
                    } 
                }
            }else if($searchBy=='userName'){
                $sql = "SELECT * FROM users WHERE firstName LIKE '%$searchKey%' OR lastName LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $users = $stmt->fetchall(PDO::FETCH_ASSOC);
                $i=0;
                foreach($users as $user){
                    $sql = 'SELECT * FROM demands WHERE userRef = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $user['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    foreach($demands as $demand){  
                        $borrowingsBuffer = array();  
                        $sql = 'SELECT * FROM borrowings WHERE demandRef = :ref';
                        $stmt = $this->pdo->prepare($sql);
                        $stmt->execute(['ref' => $demand['ref']]);
                        $borrowingsBuffer = $stmt->fetchall(PDO::FETCH_ASSOC);
                        if(!empty($borrowingsBuffer)){
                            $borrowings = array_merge($borrowings,$this->getBorrowingsSearchListRecyclerView($borrowingsBuffer));
                        }                      
                    }
                    $i++;
                }    
            }else if($searchBy=='bookTitle'){
                $sql = "SELECT * FROM books WHERE title LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $books = $stmt->fetchall(PDO::FETCH_ASSOC);
                $i=0;
                foreach($books as $book){
                    $sql = 'SELECT * FROM demands WHERE bookRef = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $book['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    foreach($demands as $demand){  
                        $borrowingsBuffer = array();  
                        $sql = 'SELECT * FROM borrowings WHERE demandRef = :ref';
                        $stmt = $this->pdo->prepare($sql);
                        $stmt->execute(['ref' => $demand['ref']]);
                        $borrowingsBuffer = $stmt->fetchall(PDO::FETCH_ASSOC);
                        if(!empty($borrowingsBuffer)){
                            $borrowings = array_merge($borrowings,$this->getBorrowingsSearchListRecyclerView($borrowingsBuffer));
                        }                      
                    }
                } 
            }
            return $borrowings;        
        }

        function getUserSearchBorrowings($searchBy,$searchKey,$email){
            
            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $borrowings = array();
            if($searchBy=='bookRef' ){
                $sql = "SELECT * FROM demands WHERE userRef = :userRef AND $searchBy LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['userRef'=>$user[0]['ref']]);
                $demands = $stmt->fetchall(PDO::FETCH_ASSOC);

                foreach($demands as $demand){
                    
                    $sql = 'SELECT * FROM borrowings WHERE demandRef = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $demand['ref']]);
                    $borrowingsBuffer = $stmt->fetchall(PDO::FETCH_ASSOC);
                    if(!empty($borrowingsBuffer)){
                        $borrowings = array_merge($borrowings,$this->getBorrowingsSearchListRecyclerView($borrowingsBuffer));
                    } 
                }
               
            }else if($searchBy=='bookTitle'){
                $sql = "SELECT * FROM books WHERE title LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $books = $stmt->fetchall(PDO::FETCH_ASSOC);
                $i=0;
                foreach($books as $book){
                    $sql = 'SELECT * FROM demands WHERE bookRef = :ref AND userRef = :userRef';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $book['ref'],'userRef'=>$user[0]['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    foreach($demands as $demand){  
                        $borrowingsBuffer = array();  
                        $sql = 'SELECT * FROM borrowings WHERE demandRef = :ref';
                        $stmt = $this->pdo->prepare($sql);
                        $stmt->execute(['ref' => $demand['ref']]);
                        $borrowingsBuffer = $stmt->fetchall(PDO::FETCH_ASSOC);
                        if(!empty($borrowingsBuffer)){
                            $borrowings = array_merge($borrowings,$this->getBorrowingsSearchListRecyclerView($borrowingsBuffer));
                        }                      
                    }
                } 
            }
            return $borrowings;        
        }

        function getAdminSearchUsers($searchBy,$searchKey){
            $users = array();
            if($searchBy=='name'){
                $sql = "SELECT * FROM users WHERE firstName LIKE '%$searchKey%' OR lastName LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $users = $stmt->fetchall(PDO::FETCH_ASSOC);  
            }else if($searchBy=='ref'){
                $sql = "SELECT * FROM users WHERE ref LIKE '%$searchKey%'";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $users = $stmt->fetchall(PDO::FETCH_ASSOC);  
            }
            return $users;        
        }
        

        function verifyDemandsLate(){
            $sql = 'SELECT * FROM demands WHERE status = :approved';
            $stmt = $this->pdo->prepare($sql);
            $approved = "approved";
            $stmt->execute(['approved'=>$approved]);
            $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
            foreach($demands as $demand){ 
                if(time()-strtotime($demand['approveDate'])>259200){
                    $sql = 'UPDATE demands SET status = :canceled WHERE ref = :ref';
                    $canceled = "canceled";
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['canceled'=>$canceled,'ref'=>$demand['ref']]);
                }
            }
        }

        function getAdminDemandsListRecyclerView(){

            $this->verifyDemandsLate();
            $sql = 'SELECT * FROM demands WHERE status = :ready OR status = :approved ORDER BY demandDate DESC';
            $stmt = $this->pdo->prepare($sql);
            $ready = "ready";
            $approved = "approved";
            $stmt->execute(['ready'=>$ready,'approved'=>$approved]);
            $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $i=0;
            foreach($demands as $demand){
                
                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $demands[$i]['demandRef']= $demand['ref'];


                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }

                $demands[$i]['shelfName'] = $bookShelf[0]['name'];
                $demands[$i]['shelfSide'] = $bookPlace[0]['side'];
                $demands[$i]['shelfRow'] = $bookPlace[0]['row'];
                $demands[$i]['shelfCol'] = $bookPlace[0]['col'];

                $demands[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
                
            }
            return $demands;

        }

        function getUserDemandsListRecyclerView($email){

            $this->verifyDemandsLate();

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = 'SELECT * FROM demands WHERE userRef = :userRef AND (status = :ready OR status = :approved OR status = :excused OR status = :canceled) ORDER BY demandDate DESC';
            $stmt = $this->pdo->prepare($sql);
            $ready = "ready";
            $approved = "approved";
            $excused = "excused";
            $canceled = "canceled";
            $stmt->execute(['userRef'=>$user[0]['ref'],'ready'=>$ready,'approved'=>$approved,'excused'=>$excused,'canceled'=>$canceled]);
            $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $i=0;
            foreach($demands as $demand){
                
                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $demands[$i]['demandRef']= $demand['ref'];


                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $demands[$i][$key]=$value;
                    }
                }

                $demands[$i]['shelfName'] = $bookShelf[0]['name'];
                $demands[$i]['shelfSide'] = $bookPlace[0]['side'];
                $demands[$i]['shelfRow'] = $bookPlace[0]['row'];
                $demands[$i]['shelfCol'] = $bookPlace[0]['col'];

                $demands[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
                
            }
            return $demands;

        }

        function verifyBorrowingsLate(){
            $sql = 'SELECT * FROM borrowings WHERE retrieved = 0 AND late = 0';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $borrowings = $stmt->fetchall(PDO::FETCH_ASSOC);
            foreach($borrowings as $borrowing){ 
                if(time()-strtotime($borrowing['deliverDate'])>1296000){
                    $sql = 'UPDATE borrowings SET late = 1 WHERE ref = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref'=>$borrowing['ref']]);
                }
            }
        }

        function getAdminBorrowingsListRecyclerView(){
            $this->verifyBorrowingsLate();
            $sql = 'SELECT * FROM borrowings WHERE retrieved = 0 ORDER BY late DESC, deliverDate ASC';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $borrowings = $stmt->fetchall(PDO::FETCH_ASSOC);

            $i=0;
            foreach($borrowings as $borrowing){ 
                $sql = 'SELECT * FROM demands WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref'=>$borrowing['demandRef']]);
                $demand = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand[0]['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand[0]['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $borrowings[$i]['borrowingRef']= $borrowing['ref'];

                foreach($demand as $demandProp){
                    foreach($demandProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }

                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }
                
                $borrowings[$i]['shelfName'] = $bookShelf[0]['name'];
                $borrowings[$i]['shelfSide'] = $bookPlace[0]['side'];
                $borrowings[$i]['shelfRow'] = $bookPlace[0]['row'];
                $borrowings[$i]['shelfCol'] = $bookPlace[0]['col'];
                

                $borrowings[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }
            return $borrowings;
        }

        function getUserBorrowingsListRecyclerView($email){
            $this->verifyBorrowingsLate();

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);


            $sql = 'SELECT * FROM borrowings WHERE retrieved = 0 ORDER BY late DESC, deliverDate ASC';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $borrowings = $stmt->fetchall(PDO::FETCH_ASSOC);
            $borrowingsList = array();
            $i=0;
            foreach($borrowings as $borrowing){ 
                $sql = 'SELECT * FROM demands WHERE userRef = :userRef AND ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref'=>$borrowing['demandRef'],'userRef'=>$user[0]['ref']]);
                $demand = $stmt->fetchall(PDO::FETCH_ASSOC);

                if($stmt->rowCount()>0){
                    $borrowingsList[$i] = $borrowing;

                    $sql = 'SELECT * FROM users WHERE ref = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $demand[0]['userRef']]);
                    $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                    $sql = 'SELECT * FROM books WHERE ref = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $demand[0]['bookRef']]);
                    $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                    $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                    $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                    $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                    $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                    
                    $borrowingsList[$i]['borrowingRef']= $borrowing['ref'];

                    foreach($demand as $demandProp){
                        foreach($demandProp as $key => $value){
                            $borrowingsList[$i][$key]=$value;
                        }
                    }

                    foreach($book as $bookProp){
                        foreach($bookProp as $key => $value){
                            $borrowingsList[$i][$key]=$value;
                        }
                    }
                    
                    foreach($user as $userProp){
                        foreach($userProp as $key => $value){
                            $borrowingsList[$i][$key]=$value;
                        }
                    }
                    
                    $borrowingsList[$i]['shelfName'] = $bookShelf[0]['name'];
                    $borrowingsList[$i]['shelfSide'] = $bookPlace[0]['side'];
                    $borrowingsList[$i]['shelfRow'] = $bookPlace[0]['row'];
                    $borrowingsList[$i]['shelfCol'] = $bookPlace[0]['col'];
                    

                    $borrowingsList[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];
                    
                    $i++;
                }
            }
            return $borrowingsList;
        }

        function getBorrowingsSearchListRecyclerView($borrowings){
            
            $i=0;
            foreach($borrowings as $borrowing){ 
                $sql = 'SELECT * FROM demands WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref'=>$borrowing['demandRef']]);
                $demand = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM users WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand[0]['userRef']]);
                $user = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM books WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $demand[0]['bookRef']]);
                $book = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book[0]['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $borrowings[$i]['borrowingRef']= $borrowing['ref'];

                foreach($demand as $demandProp){
                    foreach($demandProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }

                foreach($book as $bookProp){
                    foreach($bookProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }
                
                foreach($user as $userProp){
                    foreach($userProp as $key => $value){
                        $borrowings[$i][$key]=$value;
                    }
                }

                $borrowings[$i]['shelfName'] = $bookShelf[0]['name'];
                $borrowings[$i]['shelfSide'] = $bookPlace[0]['side'];
                $borrowings[$i]['shelfRow'] = $bookPlace[0]['row'];
                $borrowings[$i]['shelfCol'] = $bookPlace[0]['col'];

                $borrowings[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }
            return $borrowings;
        }

        function getShelvesListRecyclerView(){
                $sql = "SELECT * FROM bookshelves";
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $shelves = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $i=0;
                foreach($shelves as $shelf){
                    $sql = 'SELECT * FROM bookplace WHERE bookShelfRef = :ref';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['ref' => $shelf['ref']]);
                    $bookPlaces = $stmt->fetchall(PDO::FETCH_ASSOC);
                    $booksList = array();
                    $k = 0;
                    foreach($bookPlaces as $bookPlace){
                        $books = array();
                        $sql = 'SELECT * FROM books WHERE bookPlaceRef=:ref';
                        $stmt = $this->pdo->prepare($sql);
                        $stmt->execute(['ref' => $bookPlace['ref']]);
                        $books = $stmt->fetchall(PDO::FETCH_ASSOC);

                        if(!empty($books)){
                            $j=0;
                            foreach($books as $book){
                                $books[$j]['shelfName'] = $shelf['name'];
                                $books[$j]['shelfSide'] = $bookPlace['side'];
                                $books[$j]['shelfRow'] = $bookPlace['row'];
                                $books[$j]['shelfCol'] = $bookPlace['col'];
                                $books[$j]['bookPlace'] = $shelf['name'].", ".$bookPlace['side'].", R".$bookPlace['row'].", C".$bookPlace['col'];
                                $j++;
                            }
                        
                            $booksList[$k] = $books;
                            $k++;
                        }
                    }
                    
                    $shelves[$i]['books'] = $booksList;
                    $i++;
                }
                return $shelves;
        }

        function getShelves(){

            $sql = "SELECT * FROM bookshelves";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $shelves = $stmt->fetchall(PDO::FETCH_ASSOC);

            return $shelves;
        }

        function getUserTagsListRecyclerView(){
            
            $sql = "SELECT tags FROM books";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $tags = $stmt->fetchall(PDO::FETCH_ASSOC);
            $tagsList = array();
            foreach($tags as $tag){
                foreach($tag as $key => $val){
                    $vals = explode(",",$val);
                    foreach($vals as $key => $value){
                        $vals[$key] = trim($value);
                    }
                    $tagsList = array_unique(array_merge($tagsList,$vals), SORT_REGULAR);
                }
            }
            return $tagsList;
        }

        function getUserSearchTags($searchKey){
            
            $sql = "SELECT tags FROM books WHERE tags LIKE '%$searchKey%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $tags = $stmt->fetchall(PDO::FETCH_ASSOC);
            $tagsList = array();
            foreach($tags as $tag){
                foreach($tag as $key => $val){
                    $vals = explode(",",$val);
                    foreach($vals as $key => $value){
                        $vals[$key] = trim($value);
                    }
                    $tagsList = array_unique(array_merge($tagsList,$vals), SORT_REGULAR);
                }
            }
            return $tagsList;
        }

        function getUserTagBooks($tag,$email){

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = "SELECT * FROM books WHERE tags LIKE '$tag' OR tags LIKE '$tag,%' OR tags LIKE '%, $tag' OR tags LIKE '%, $tag,%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $books = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $fullDemands = 0;

            $i=0;
            foreach($books as $book){
                $demandsCount = 0;
                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND (status = :ready OR status = :approved)';
                $stmt = $this->pdo->prepare($sql);
                $ready = "ready";
                $approved = "approved";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'ready'=>$ready,'approved'=>$approved]);
                $stmt->fetchall(PDO::FETCH_ASSOC);
                $demandsCount = $stmt->rowCount();

                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND status = :delivered';
                $stmt = $this->pdo->prepare($sql);
                $delivered = "delivered";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'delivered'=>$delivered]);
                $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                $borrowingsCount=0;

                if($stmt->rowCount()>0){
                    $sql = 'SELECT * FROM borrowings WHERE demandRef = :demandRef AND retrieved = 0';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['demandRef' => $demands[0]['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    $borrowingsCount = $stmt->rowCount();
                }

                $fullDemands += $demandsCount + $borrowingsCount;

                if($demandsCount>0 || $borrowingsCount>0){
                    $books[$i]['demandingIt'] = 1;
                }else{
                    $books[$i]['demandingIt'] = 0;
                }

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $books[$i]['shelfName'] = $bookShelf[0]['name'];
                $books[$i]['shelfSide'] = $bookPlace[0]['side'];
                $books[$i]['shelfRow'] = $bookPlace[0]['row'];
                $books[$i]['shelfCol'] = $bookPlace[0]['col'];

                $books[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }

            $j=0;
            foreach($books as $book){
                if($fullDemands>2){
                    $books[$j]['fullDemands'] = 1;
                }else{
                    $books[$j]['fullDemands'] = 0;
                }
                $j++;
            }

            return $books;
        }

        function getUserSearchBooks($searchBy,$searchKey,$email){

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = "SELECT * FROM books WHERE $searchBy LIKE '%$searchKey%'";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $books = $stmt->fetchall(PDO::FETCH_ASSOC);

            $fullDemands = 0;

            $i=0;
            foreach($books as $book){
                $demandsCount = 0;
                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND (status = :ready OR status = :approved)';
                $stmt = $this->pdo->prepare($sql);
                $ready = "ready";
                $approved = "approved";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'ready'=>$ready,'approved'=>$approved]);
                $stmt->fetchall(PDO::FETCH_ASSOC);
                $demandsCount = $stmt->rowCount();

                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND status = :delivered';
                $stmt = $this->pdo->prepare($sql);
                $delivered = "delivered";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'delivered'=>$delivered]);
                $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                $borrowingsCount=0;

                if($stmt->rowCount()>0){
                    $sql = 'SELECT * FROM borrowings WHERE demandRef = :demandRef AND retrieved = 0';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['demandRef' => $demands[0]['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    $borrowingsCount = $stmt->rowCount();
                }

                $fullDemands += $demandsCount + $borrowingsCount;

                if($demandsCount>0 || $borrowingsCount>0){
                    $books[$i]['demandingIt'] = 1;
                }else{
                    $books[$i]['demandingIt'] = 0;
                }

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $books[$i]['shelfName'] = $bookShelf[0]['name'];
                $books[$i]['shelfSide'] = $bookPlace[0]['side'];
                $books[$i]['shelfRow'] = $bookPlace[0]['row'];
                $books[$i]['shelfCol'] = $bookPlace[0]['col'];

                $books[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }

            $j=0;
            foreach($books as $book){
                if($fullDemands>2){
                    $books[$j]['fullDemands'] = 1;
                }else{
                    $books[$j]['fullDemands'] = 0;
                }
                $j++;
            }

            return $books;
        }

        function getUserBooks($email){

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $sql = "SELECT * FROM books";
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
            $books = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $fullDemands = 0;

            $i=0;
            foreach($books as $book){
                $demandsCount = 0;
                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND (status = :ready OR status = :approved)';
                $stmt = $this->pdo->prepare($sql);
                $ready = "ready";
                $approved = "approved";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'ready'=>$ready,'approved'=>$approved]);
                $stmt->fetchall(PDO::FETCH_ASSOC);
                $demandsCount = $stmt->rowCount();

                $sql = 'SELECT * FROM demands WHERE bookRef = :bookRef AND userRef = :userRef AND status = :delivered';
                $stmt = $this->pdo->prepare($sql);
                $delivered = "delivered";
                $stmt->execute(['bookRef' => $book['ref'],'userRef'=>$user[0]['ref'],'delivered'=>$delivered]);
                $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                $borrowingsCount=0;

                if($stmt->rowCount()>0){
                    $sql = 'SELECT * FROM borrowings WHERE demandRef = :demandRef AND retrieved = 0';
                    $stmt = $this->pdo->prepare($sql);
                    $stmt->execute(['demandRef' => $demands[0]['ref']]);
                    $demands = $stmt->fetchall(PDO::FETCH_ASSOC);
                    $borrowingsCount = $stmt->rowCount();
                }

                $fullDemands += $demandsCount + $borrowingsCount;

                if($demandsCount>0 || $borrowingsCount>0){
                    $books[$i]['demandingIt'] = 1;
                }else{
                    $books[$i]['demandingIt'] = 0;
                }

                $sql = 'SELECT * FROM bookplace WHERE ref = :ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $book['bookPlaceRef']]);
                $bookPlace = $stmt->fetchall(PDO::FETCH_ASSOC);

                $sql = 'SELECT * FROM bookshelves WHERE ref=:ref';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute(['ref' => $bookPlace[0]['bookShelfRef']]);
                $bookShelf = $stmt->fetchall(PDO::FETCH_ASSOC);
                
                $books[$i]['shelfName'] = $bookShelf[0]['name'];
                $books[$i]['shelfSide'] = $bookPlace[0]['side'];
                $books[$i]['shelfRow'] = $bookPlace[0]['row'];
                $books[$i]['shelfCol'] = $bookPlace[0]['col'];

                $books[$i]['bookPlace'] = $bookShelf[0]['name'].", ".$bookPlace[0]['side'].", R".$bookPlace[0]['row'].", C".$bookPlace[0]['col'];

                $i++;
            }

            $j=0;
            foreach($books as $book){
                if($fullDemands>2){
                    $books[$j]['fullDemands'] = 1;
                }else{
                    $books[$j]['fullDemands'] = 0;
                }
                $j++;
            }

            return $books;
        }

        

        function addShelf($name,$sides,$rows,$cols){
            $name = $this->sanatizeData($name);
            $name = ucfirst($name);

            (int) $sides = intval($sides);
            (int) $rows = intval($rows);
            (int) $cols = intval($cols);

            $refGen = true;
            while($refGen){
                //Generating ID for User
                $shelfRef = $this->generateRandomRef("SHELF_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM bookshelves';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($shelfRef,$refs)){
                    $refGen=false;
                }
            }

            $sql='INSERT INTO bookshelves(ref,name,sidesNum,rowNum,colNum) VALUES(:ref,:name,:sides,:rows,:cols)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$shelfRef,'name'=>$name,'rows'=>$rows,'cols'=>$cols,'sides'=>$sides]);

        }

        function modifyShelf($ref,$name,$sides,$rows,$cols){
            $name = $this->sanatizeData($name);
            $name = ucfirst($name);

            (int) $sides = intval($sides);
            (int) $rows = intval($rows);
            (int) $cols = intval($cols);

            $sql='UPDATE bookshelves SET name=:name, sidesNum=:sides, rowNum=:rows, colNum=:cols WHERE ref =:ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$ref,'name'=>$name,'rows'=>$rows,'cols'=>$cols,'sides'=>$sides]);
        }

        function addBorrowing($bookRef,$userRef){

            $refGen = true;
            while($refGen){
                //Generating ID for User
                $demandRef = $this->generateRandomRef("DEMAND_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM demands';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($demandRef,$refs)){
                    $refGen=false;
                }
            }

            $refGen = true;
            while($refGen){
                //Generating ID for User
                $borrowingRef = $this->generateRandomRef("BORRO_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM borrowings';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($borrowingRef,$refs)){
                    $refGen=false;
                }
            }

            $sql='INSERT INTO demands(ref,bookref,userRef,status) VALUES(:ref,:bookref,:userRef,:status)';
            $stmt = $this->pdo->prepare($sql);
            $status = "delivered";
            $stmt->execute(['ref'=>$demandRef,'bookref'=>$bookRef,'userRef'=>$userRef,'status'=>$status]);
            
            $sql='INSERT INTO borrowings(ref,demandRef) VALUES(:ref,:demandRef)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$borrowingRef,'demandRef'=>$demandRef]);
        }


        function userDemandBook($bookRef,$email){

            $sql = 'SELECT * FROM users WHERE email = :email';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['email' => $email]);
            $user = $stmt->fetchall(PDO::FETCH_ASSOC);

            $refGen = true;
            while($refGen){
                //Generating ID for User
                $demandRef = $this->generateRandomRef("DEMAND_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM demands';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($demandRef,$refs)){
                    $refGen=false;
                }
            }

            $sql='INSERT INTO demands(ref,bookref,userRef) VALUES(:ref,:bookref,:userRef)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$demandRef,'bookref'=>$bookRef,'userRef'=>$user[0]['ref']]);
            
        }
    
        function addBook($title,$author,$descr,$edition,$tags,$shelf,$side,$row,$col,$quantity,$image,$pdf,$releaseY){
        
    
            $title = $this->sanatizeData($title);
            $title = ucfirst($title);
            
            $author = $this->sanatizeData($author);
            $author = ucfirst($author);
            
            $descr = $this->sanatizeData($descr);
            $descr = ucfirst($descr);

            
            $tags = $this->sanatizeData($tags);
            if($tags != ""){
                $tags = strtolower($tags);
            }else{
                $tags = "other";
            }

            $edition = strtolower($edition);


            (int) $quantity = intval($quantity);

            (int) $releaseY = intval($releaseY);
            
            if($image!=""){
                $nameIMG = rand().'_'.time().'.jpeg';
                $targ_dir = '../../../../image/books';
                if(!file_exists($targ_dir)){
                    mkdir($targ_dir,0777,true);
                }         
                $targ_dir = $targ_dir.'/'.$nameIMG;
                file_put_contents($targ_dir,base64_decode($image));
                $image = 'http://192.168.1.106:80/Android/Unibib/image/books/'.$nameIMG;
            }else{
                $image = 'http://192.168.1.106:80/Android/Unibib/image/books/unibibLogoldpi.png';
            }



            $sql = 'SELECT ref FROM bookshelves where name=:shelf';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['shelf'=>$shelf]);
            $refs = $stmt->fetchall(PDO::FETCH_COLUMN);
            $shelfRef = $refs[0];

            $bookRef = '';
            $refGen = true;
            while($refGen){
                //Generating ID for User
                $bookRef = $this->generateRandomRef("BOOK_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM books';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($bookRef,$refs)){
                    $refGen=false;
                }
            }

            $PlaceRef = '';
            $refGen = true;
            while($refGen){
                //Generating ID for User
                $PlaceRef = $this->generateRandomRef("PLACE_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM bookplace';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($PlaceRef,$refs)){
                    $refGen=false;
                }
            }

            $sql='INSERT INTO bookplace(ref,bookShelfRef,row,col,side) VALUES(:ref,:shelf,:row,:col,:side)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$PlaceRef,'shelf'=>$shelfRef,'row'=>$row,'col'=>$col,'side'=>$side]);


            $sql='INSERT INTO books(ref,title,author,description,edition,tags,bookPlaceRef,quantity,image,releaseYear,pdf) 
                            VALUES(:ref,:title,:author,:description,:edition,:tags,:bookPlaceRef,:quantity,:image,:releaseYear,:pdf)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$bookRef,'title'=>$title,'author'=>$author,'description'=>$descr,'edition'=>$edition,'tags'=>$tags,
                            'bookPlaceRef'=>$PlaceRef,'quantity'=>$quantity,'image'=>$image,'releaseYear'=>$releaseY,'pdf'=>$pdf]);



        }

        function modifyBook($bookRef,$title,$author,$descr,$edition,$tags,$shelf,$side,$row,$col,$quantity,$image,$pdf,$releaseY){
        
    
            $title = $this->sanatizeData($title);
            $title = ucfirst($title);
            
            $author = $this->sanatizeData($author);
            $author = ucfirst($author);
            
            $descr = $this->sanatizeData($descr);
            $descr = ucfirst($descr);
            
            $tags = $this->sanatizeData($tags);
            if($tags != ""){
                $tags = strtolower($tags);
            }else{
                $tags = "other";
            }

            $edition = strtolower($edition);


            (int) $quantity = intval($quantity);

            (int) $releaseY = intval($releaseY);
            
            if($image!=""){
                if(!str_contains($image,"http")){
                    $nameIMG = rand().'_'.time().'.jpeg';
                    $targ_dir = '../../../../image/books';
                    if(!file_exists($targ_dir)){
                        mkdir($targ_dir,0777,true);
                    }         
                    $targ_dir = $targ_dir.'/'.$nameIMG;
                    file_put_contents($targ_dir,base64_decode($image));
                    $image = 'http://192.168.1.106:80/Android/Unibib/image/books/'.$nameIMG;
                }
            }else{
                $image = 'http://192.168.1.106:80/Android/Unibib/image/books/unibibLogoldpi.png';
            }


            $sql = 'SELECT ref FROM bookshelves where name=:shelf';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['shelf'=>$shelf]);
            $refs = $stmt->fetchall(PDO::FETCH_COLUMN);
            $shelfRef = $refs[0];

            $PlaceRef = '';
            $refGen = true;
            while($refGen){
                //Generating ID for User
                $PlaceRef = $this->generateRandomRef("PLACE_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM bookplace';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($PlaceRef,$refs)){
                    $refGen=false;
                }
            }
            
            $sql='INSERT INTO bookplace(ref,bookShelfRef,row,col,side) VALUES(:ref,:shelf,:row,:col,:side)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$PlaceRef,'shelf'=>$shelfRef,'row'=>$row,'col'=>$col,'side'=>$side]);


            $sql='UPDATE books SET title = :title,
                                    author=:author,
                                    description=:description,
                                    edition=:edition,
                                    tags=:tags,
                                    bookPlaceRef=:bookPlaceRef,
                                    quantity=:quantity,
                                    image=:image,
                                    releaseYear=:releaseYear,
                                    pdf=:pdf
                                    Where ref=:ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$bookRef,'title'=>$title,'author'=>$author,'description'=>$descr,'edition'=>$edition,'tags'=>$tags,
                            'bookPlaceRef'=>$PlaceRef,'quantity'=>$quantity,'image'=>$image,'releaseYear'=>$releaseY,'pdf'=>$pdf]);



        }

        function deleteBook($bookRef){
            $sql='SET FOREIGN_KEY_CHECKS=0;';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();

            $sql = 'SELECT bookPlaceRef FROM books where ref=:ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$bookRef]);
            $bookPlaceRefs = $stmt->fetchall(PDO::FETCH_COLUMN);
            $bookPlaceRef = $bookPlaceRefs[0];
          
            $sql='DELETE FROM bookplace WHERE ref=:ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$bookPlaceRef]);

            $sql='DELETE FROM books WHERE ref=:ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$bookRef]);

            $sql='SET FOREIGN_KEY_CHECKS=1;';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute();
        }


        function excuseDemand($ref){
            $sql='UPDATE demands SET status = :status WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $status = "excused";
            $stmt->execute(['ref'=>$ref,'status'=>$status]);
        }

        function approveDemand($ref){
            $sql='UPDATE demands SET status = :status WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $status = "approved";
            $stmt->execute(['ref'=>$ref,'status'=>$status]);
            
            $sql='UPDATE demands SET approveDate = :date WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $date = date('Y-m-d H:i:s',time());
            $stmt->execute(['ref'=>$ref,'date'=>$date]);

            $sql='SELECT * FROM demands WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$ref]);
            $demand = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $sql='SELECT * FROM books WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$demand[0]['bookRef']]);
            $book = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $val=intval($book[0]['quantity'])-1;
            $sql='UPDATE books SET quantity = :val WHERE ref=:bookRef';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['val'=>$val,'bookRef'=>$demand[0]['bookRef']]);

        }

        function retrieveBorrowing($ref){
            $sql='UPDATE borrowings SET retrieved = :retrieved WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $retrieved = 1;
            $stmt->execute(['ref'=>$ref,'retrieved'=>$retrieved]);
        }

        function cancelDemand($ref){
            $sql='UPDATE demands SET status = :status WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $status = "canceled";
            $stmt->execute(['ref'=>$ref,'status'=>$status]);

            $sql='SELECT * FROM demands WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$ref]);
            $demand = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $sql='SELECT * FROM books WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$demand[0]['bookRef']]);
            $book = $stmt->fetchall(PDO::FETCH_ASSOC);
            
            $val=intval($book[0]['quantity'])+1;
            $sql='UPDATE books SET quantity = :val WHERE ref=:bookRef';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['val'=>$val,'bookRef'=>$demand[0]['bookRef']]);
        }

        function deliveredDemand($ref){
            $sql='UPDATE demands SET status = :status WHERE ref = :ref';
            $stmt = $this->pdo->prepare($sql);
            $status = "delivered";
            $stmt->execute(['ref'=>$ref,'status'=>$status]);


            $refGen = true;
            while($refGen){
                //Generating ID for User
                $borroRef = $this->generateRandomRef("BORRO_");

                //Verify if there's one ref or not
                $sql = 'SELECT ref FROM borrowings';
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute();
                $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                if(!in_array($borroRef,$refs)){
                    $refGen=false;
                }
            }

            $sql='INSERT INTO borrowings(ref,demandRef) VALUES(:ref,:demandRef)';
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute(['ref'=>$borroRef,'demandRef'=>$ref]);
        }



    }