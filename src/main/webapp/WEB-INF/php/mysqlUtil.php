<?php
/** @noinspection SqlDialectInspection */
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/3/1
 * Time: 11:03
 */


function getCon()
{
    if (!$GLOBALS['con']) {
        $dbms = 'mysql';     //数据库类型
        $host = 'hdm128467490.my3w.com'; //数据库主机名
        $dbName = 'hdm128467490_db';    //使用的数据库
        $user = 'hdm128467490';      //数据库连接用户名
        $pass = 'r5z3mxl10240M1';          //对应的密码
        $dsn = "$dbms:host=$host;dbname=$dbName";
        $pdo = new PDO($dsn, $user, $pass, array(PDO::MYSQL_ATTR_INIT_COMMAND => "set names utf8"));//初始化一个PDO对象
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $GLOBALS['con'] = $pdo;
    }
    return $GLOBALS['con'];
}

/**
 * @param string $tableName
 * @param string $class
 * @return array
 */
function queryList($tableName, $class)
{
    $query = 'SELECT * FROM `' . $tableName . '` where `type`!=\'small\' LIMIT ?,?';
    $stmt = getCon()->prepare($query);
    $offset = intval($_REQUEST['offset']);
    $offset = $offset < 0 ? 0 : $offset;
    $limit = intval($_REQUEST['limit']);
    $limit = $limit <= 0 ? PHP_INT_MAX : $limit;
    $stmt->bindParam(1, $offset, PDO::PARAM_INT);
    $stmt->bindParam(2, $limit, PDO::PARAM_INT);
    $stmt->execute();
    $result = array();
    $num = $stmt->rowCount();
    for ($i = 0; $i < $num; $i++) {
        $object = $stmt->fetchObject($class);
        array_push($result, $object);
    }
    return $result;
}

/**
 * @param string $tableName
 * @param string $class
 * @param $id
 * @return mixed
 */
function getById($tableName, $class, $id)
{
    $stmt = getCon()->prepare('select * from `' . $tableName . '` where `id`=?');
    $stmt->bindParam(1, $id);
    $stmt->execute();
    return $stmt->fetchObject($class);
}

/**
 * @param string $tableName
 * @return mixed
 */
function lastTime($tableName)
{
    $query = 'SELECT MAX(`modified`) AS `modified` FROM `' . $tableName . '`';
    $stmt = getCon()->prepare($query);
    $stmt->execute();
    $var = $stmt->fetch();
    return $var[0];
}


/**
 * @return string
 */
function uuid()
{
    $stmt = getCon()->prepare('select uuid() as id;');
    $stmt->bindParam(1, $ip);
    $stmt->execute();
    $row = $stmt->fetch();
    return $row['id'];
}