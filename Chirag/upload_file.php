
<?php
  
    $file_path = "profile_pic/";
    $tmp = $_FILES['uploaded_file']['tmp_name'];
    $filename=$_FILES['uploaded_file']['name'];
    $user = $_GET['user'];
    include "conn.php";
    $sql = "UPDATE `login` SET `img_name`='$filename' WHERE username='$user'";
    $result = $con->query($sql);
     
    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($tmp, $file_path) ){
        
        echo "success";
    } else{
        echo "fail";
    }
 ?>


