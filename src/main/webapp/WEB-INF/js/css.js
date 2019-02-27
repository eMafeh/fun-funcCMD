const maxWidth = window.innerWidth;
const maxHeight = window.innerHeight;
const buttonWidth = 0.6 * maxWidth;
const buttonHeight = 0.09 * maxWidth;
const fontSize = maxWidth * 0.08;

const tableBorderWidth = maxHeight / 200;
const xNum = 14;
const cellLength = maxWidth / xNum;
const yNum = Math.floor((maxHeight - tableBorderWidth - buttonHeight) / cellLength);


const $$views = document.getElementsByClassName("view");
for (const view of $$views) view.classList.add("animate", "bounceIn");
const $$button = document.getElementsByClassName("button");
for (const button of $$button) {
    button.style.width = buttonWidth + "px";
    button.style.height = buttonHeight + "px";
    const strings = button.innerHTML.split('');
    button.innerHTML = `<span>${strings.join("</span><span>")}</span>`;
}

const $console = document.getElementById("console");
const $html = document.getElementsByTagName("html")[0];
const $homePage = document.getElementById("home_page");
const $gamePage = document.getElementById("game_page");
const $homeConfig = document.getElementById("home_config");


const $table = document.getElementById("table");
const $stopMarker = document.getElementById("stopMarker");
const $canvas = document.getElementById("canvas");

$html.style.height = maxHeight + "px";
$html.style.width = maxWidth + "px";

$html.style.fontSize = fontSize + "px";
$homePage.style.backgroundImage = 'url("http://img.zcool.cn/community/016e4c5860c8d3a8012060c82cd109.jpg")';
$gamePage.style.backgroundImage = 'url("http://img.zcool.cn/community/01b7855860c8eaa8012060c8779a60.jpg")';

$table.style.borderWidth = "0 0 " + tableBorderWidth + "px 0";
$stopMarker.style.fontSize = fontSize * 2 + "px";
$stopMarker.style.top = 0.42 * maxHeight + "px";
$stopMarker.style.left = (0.5 * maxWidth - fontSize) + "px";

$canvas.style.top = "0";
$canvas.style.left = "0";


const DoCss = {
    homeView: () => {
        $homePage.style.display = "block";
        $gamePage.style.display = "none";
    },
    gameView: () => {
        $gamePage.style.display = "block";
        $homePage.style.display = "none";
    },
    configHide: () => {
        $homeConfig.style.display = "none";
    },
    configShow: () => {
        $homeConfig.style.display = "block";
    }
};
let auto = false;
const startGame = start => {
    auto = start === true || start === false ? start : !auto;
    $stopMarker.style.display = auto ? "none" : "block";

};
DoCss.homeView();