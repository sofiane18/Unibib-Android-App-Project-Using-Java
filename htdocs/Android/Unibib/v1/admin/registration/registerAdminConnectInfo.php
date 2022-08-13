<?php 

     //Generating ID for User
    function generateRandomRef() {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = 'ADMIN_';

        for ($i = 0; $i < 10; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }

    $response = array();
    $ref;

    if($_SERVER['REQUEST_METHOD']=='POST'){
        require_once '../../../includes/DBOperations.php';
        $db = new DBOperations();
        try{
            if($db->createAdminConnectInfo($_POST['fName'],$_POST['lName'],$_POST['gender'],$_POST['birthDate'],$_POST['address'],
                                            $_POST['phone'],$_POST['email'],$_POST['password'],$_POST['invitationCode'])){
                $response['error'] = false;
                $response['message'] = 'Registration completed successfully';
                $refGen = true;


                while($refGen){
                    //Generating ID for User
                    $ref = generateRandomRef();

                    //Verify if there's one ref or not
                    $sql = 'SELECT ref FROM admins';
                    $stmt = $db->pdo->prepare($sql);
                    $stmt->execute();
                    $refs = $stmt->fetchall(PDO::FETCH_COLUMN);

                    if(!in_array($ref,$refs)){
                        $refGen=false;
                    }
                }

                $password = password_hash($_POST['password'],PASSWORD_DEFAULT);

                $sql = 'INSERT INTO admins (ref,firstName, lastName, gender, birthDate, address, email, phone, password) 
                    VALUES (:ref, :fname, :lname, :gender, :birthDate, :address, :email, :phone, :password)'; 
                
                //Prepare Statment
                $stmt = $db->pdo->prepare($sql);
                $stmt->execute(['ref' => $ref, 'fname' => $_POST['fName'],'lname' => $_POST['lName'],
                                'gender' =>$_POST['gender'], 'birthDate' => $_POST['birthDate'], 'address' => $_POST['address'],
                                'email' => $_POST['email'],'phone' =>$_POST['phone'], 'password' => $password]);
        
            }else{
                $response['error'] = true;
                foreach($db->err as $key => $value){
                    $response['message'][$key] = $value;
                }
            }
        }catch(Exception $e){
            $response['error'] = true;
            $response['message'] = $e;
        }


    }else{
        $response['error'] = true;
        $response['message'] = 'invalide request';
    } 

    echo json_encode($response);