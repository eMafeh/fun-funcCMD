<?php /** @noinspection SqlDialectInspection */
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/25
 * Time: 14:30
 */

function getCon()
{
    if (!$GLOBALS['con']) {
        $servername = "hdm128467490.my3w.com";
        $username = "hdm128467490";
        $password = "r5z3mxl10240M1";
        $dbname = "hdm128467490_db";
        $GLOBALS['con'] = mysqli_connect($servername, $username, $password, $dbname);
    }
    return $GLOBALS['con'];
}

function lastTime($tableName)
{
    $query = 'SELECT MAX(`modified`) AS `modified` FROM `' . $tableName . '`';
    $result = mysqli_query(getCon(), $query);
    $row = mysqli_fetch_array($result, MYSQL_ASSOC);
    return $row['modified'];
}

function queryListNotValid($tableName)
{
    $query = 'SELECT * FROM `' . $tableName . '` WHERE `valid`=1';
    $result = mysqli_query(getCon(), $query);
    $array = array();
    while ($rows = mysqli_fetch_array($result, MYSQL_ASSOC)) {
        array_push($array, $rows);
    }
    return $array;
}

function deleteGif($url)
{
    $query = 'update `gif` set `valid`=0,`modified`=now() where `url` = ? and `valid`=1';
    $stmt = getCon()->prepare($query);
    $stmt->bind_param('s', $url);
    $stmt->execute();
    return $stmt->affected_rows;
}

function login($ip)
{
    $query = 'insert into  `op_log` (`ip`,`type`)values (?,?)';
    $stmt = getCon()->prepare($query);
    $type = 'login';
    $stmt->bind_param('ss', $ip, $type);
    $stmt->execute();
    return $stmt->affected_rows;
}