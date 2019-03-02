<?php
/** @noinspection SqlDialectInspection */
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/25
 * Time: 14:30
 */


/**
 * @param string $url
 * @return int
 */
function insertGif($url)
{
    $query = 'insert into `gif`(url)values(?)';
    $PDO = getCon();
    $stmt = $PDO->prepare($query);
    $stmt->bindParam(1, $url);
    $stmt->execute();
    return $PDO->lastInsertId();
}

function markSmall($url)
{
    $sql = 'UPDATE gif set type=? where url=? and type!=?;';
    $stmt = getCon()->prepare($sql);
    $type = 'small';
    $stmt->bindParam(1, $type);
    $stmt->bindParam(2, $url);
    $stmt->bindParam(3, $type);
    $stmt->execute();
    return $stmt->rowCount();
}

/**
 * @param string $ip
 * @param $firstId
 */
function addOrUpdateVisit($ip, $firstId)
{
    $sql = 'insert into ipVisit (id,firstId) values (?,?) ON DUPLICATE KEY UPDATE times=times + 1, modified=now();';
    $stmt = getCon()->prepare($sql);
    $stmt->bindParam(1, $ip);
    $stmt->bindParam(2, $firstId);
    $stmt->execute();
}

function updateVisit($ip, $userId)
{
    $sql = 'UPDATE ipVisit set userId=? where id=?;';
    $stmt = getCon()->prepare($sql);
    $stmt->bindParam(1, $userId);
    $stmt->bindParam(2, $ip);
    $stmt->execute();
}

/**
 * @param UserType $user
 */
function insertUser($user)
{
    $stmt = getCon()->prepare('insert into user(id, homeImgId, gameImgId) values (?,?,?)');
    $stmt->bindParam(1, $user->id);
    $stmt->bindParam(2, $user->homeImgId);
    $stmt->bindParam(3, $user->gameImgId);
    $stmt->execute();
}

function insertOp($userId, $ip, $type)
{
    $stmt = getCon()->prepare('insert into op_log(userId,ip, type) values (?,?,?)');
    $stmt->bindParam(1, $userId);
    $stmt->bindParam(2, $ip);
    $stmt->bindParam(3, $type);
    $stmt->execute();
}

function addGrade($userId, $num)
{
    $stmt = getCon()->prepare('UPDATE user set grade=grade+? where id=?');
    $num = intval($num);
    $stmt->bindParam(1, $num, PDO::PARAM_INT);
    $stmt->bindParam(2, $userId);
    $stmt->execute();
}