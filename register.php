<?php
$username = $_POST["username"];
$password = $_POST["password"];

#connecting
$dbname = "jsuhr2_rps";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);

$sql = "select * from users where username=\"" . addslashes($username) . "\"";
$result  = mysqli_query($cid, $sql);
$numRows = $result->num_rows;

if($numRows){
	echo "Failed";
} else{
	echo "Success";
	$sql = "insert into users (username, password, wins, losses, rock, paper, scissors)
				values (\"" . addslashes($username) . "\", \"" .
				addslashes($password) . "\", 0, 0, 0, 0, 0)";
	$result = mysqli_query($cid, $sql);
	if(!$result){
		echo "Failed to insert into users";
	}
}
?>
