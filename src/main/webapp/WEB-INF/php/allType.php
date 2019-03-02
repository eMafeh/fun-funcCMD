<?php

/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/28
 * Time: 10:46
 */

class AbstractType
{
    public $id;
    public $modified;
}

class GifType extends AbstractType
{
    public $name;
    public $url;
    public $type;
}

class IpVisitType extends AbstractType
{
    public $userId;
    public $firstId;
    public $times;
}

class UserType
{
    public $name;
    public $password;
    public $homeImgId = 1;
    public $gameImgId = 2;
    public $starLine = true;
    public $grade = 0;
}
