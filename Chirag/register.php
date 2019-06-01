<?php 
    
include "conn.php";

$response  = array();

if(isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['user']) && isset($_POST['mobile']) && isset($_POST['pass']))
{

    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $mobile = $_POST['mobile'];
    $pass = $_POST['pass'];
    $email = $_POST['user'];
    $status = (int) $_POST['status'];
    $name = $fname.' '.$lname;


    $sql ="INSERT INTO `charlie_db`.`login` (`id`,`username`, `password`, `status`, `name`, `mobile`) VALUES (NULL, '$email', '$pass', '$status', '$name', '$mobile')";

    

    $result = $con->query($sql);
    

    if ($status == 1) {
        $sql1 = "INSERT into garage_location (`username`,`status`) values ('$email',0)";
        $sql2 = "INSERT into garage_temp_location (`username`) values ('$email')"; 
        $con->query($sql1);
        $con->query($sql2);       
    }

    if($result){
        $response["status"] = 1;
    }
    else{
        $response["status"] = 0;
    }
    echo json_encode($response);
}
else{
    $response["status"] = 3;
    echo json_encode($response);
}


?>
