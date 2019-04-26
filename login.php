<?php
$username = $_POST["username"];
$password = $_POST["password"];

#connecting
$dbname = "jsuhr2_rps";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);

$sql = "select * from users where username=\"" . $username . "\" and password=\"" . $password . "\";";
$result  = mysqli_query($cid, $sql);
$numRows = $result->num_rows;

$sql2 = "select * from users where username=\"" . $username . "\";";
$result2  = mysqli_query($cid, $sql2);
$numRows2 = $result2->num_rows;

if($numRows){
	echo "Success";
} else if ($numRows2){
	echo "Password";
} else {
	echo "Username";
}
?>
