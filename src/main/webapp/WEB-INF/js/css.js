//div size 和 font size 根据页面大小自适应
const maxWidth = window.innerWidth;
const maxHeight = window.innerHeight;
const buttonWidth = 0.6 * maxWidth;
const buttonHeight = 0.09 * maxWidth;
const fontSize = maxWidth * 0.08;


const xNum = 13;//border最短
const cellLength = maxWidth / xNum;
const minCellLength = cellLength * 0.6;
const yNum = Math.floor(maxHeight / cellLength);
const tableBorderWidth = maxHeight - yNum * cellLength;
const titleHeight = 1.1 * fontSize;

//对 button 的文字内容进行统一处理
{
    const $$button = document.getElementsByClassName("button");
    for (const button of $$button) {
        button.style.width = buttonWidth + "px";
        button.style.height = buttonHeight + "px";
        const strings = button.innerHTML.split('');
        button.innerHTML = `<span>${strings.join("</span><span>")}</span>`;
    }
}
//持有一些有 id 的对象
const $html = document.getElementsByTagName("html")[0];

const $barsOpen = document.getElementById("bars_open");
const $barsClose = document.getElementById("bars_close");
const $bars = document.getElementById("bars");
$bars.style.height = titleHeight + 'px';
$barsOpen.onclick = () => {
    $bars.style.top = '0';
    $barsOpen.style.display = 'none';
};
$barsClose.onclick = () => {
    $barsOpen.style.display = 'block';
    $bars.style.top = '-' + $bars.style.height;
};
$barsClose.click();

const $viewHome = document.getElementById("view_home");
const $viewGame = document.getElementById("view_game");
const $viewLogin = document.getElementById("view_login");
const $viewConfig = document.getElementById("view_config");
const $viewImage = document.getElementById("view_image");

const $autoTime = document.getElementById("loopTime");
const $starLine = document.getElementById("starLine");

const $showHomeImg = document.getElementById("show_home_img");
const $showGameImg = document.getElementById("show_game_img");

// const $homeImg = document.getElementById("home_img");
// $showHomeImg.onclick = () => $homeImg.click();
// $homeImg.onchange = function (...all) {
//     console.log(all);
// };

const $userInfo = document.getElementById("user_info");

const $table = document.getElementById("table");
const $allCount = document.getElementById("allCount");
const $stopMarker = document.getElementById("stopMarker");
const $canvas = document.getElementById("canvas");

const $minTable = document.getElementById("min-table");


const $console = document.getElementById("console");
console.log(fontSize);

$html.style.height = maxHeight + "px";
$html.style.width = maxWidth + "px";

$html.style.fontSize = fontSize + "px";

$userInfo.style.top = "0";
$userInfo.style.left = "0";
$userInfo.style.fontSize = fontSize / 2 + "px";

$table.style.borderColor = '#555555';
$table.style.borderStyle = 'solid';
$table.style.borderWidth = "0 0 " + tableBorderWidth + "px 0";

$minTable.style.top = fontSize * 8 / 3 + 'px';
$minTable.style.left = '0';
$minTable.style.opacity = '0.5';

$allCount.style.top = '0';
$allCount.style.left = '0';
$stopMarker.style.fontSize = fontSize * 2 + "px";
$stopMarker.style.top = 0.42 * maxHeight + "px";
$stopMarker.style.left = (0.5 * maxWidth - fontSize) + "px";

$canvas.style.top = "0";
$canvas.style.left = "0";

$console.style.display = 'none';
$console.style.fontSize = fontSize * 2 + "px";


const DoCss = {
    viewStack: [],
    $currentView: null,
    backView: () => {
        const $view = DoCss.viewStack.pop();
        if ($view) $view.style.top = maxHeight + 'px';
        if (DoCss.viewStack.length > 0)
            DoCss.viewStack[DoCss.viewStack.length - 1].style.top = '0';
    },
    showView: $view => {
        if (mainViews.includes($view))
            DoCss.__showMainView($view);
        else if (layoutViews.includes($view))
            DoCss.__showLayoutView($view);
        else
            throw new Error("id:" + $view + " is not a view class");
    },
    __showMainView: $view => {
        const $currentView = DoCss.$currentView;
        if ($currentView === $view) return;
        if ($currentView) DoCss.hiddenCurrentView($view, $currentView);
        $view.style.top = '0';
        DoCss.$currentView = $view;
    },
    __showLayoutView: $view => {
        if (DoCss.viewStack.length > 0) {
            const $currentView = DoCss.viewStack[DoCss.viewStack.length - 1];
            if ($currentView === $view) return;
            DoCss.hiddenCurrentView($view, $currentView);
        }
        const index = DoCss.viewStack.indexOf($view);
        if (index > -1) DoCss.viewStack.splice(index, 1);
        DoCss.viewStack.push($view);
        $view.style.top = '0';
    },
    hiddenCurrentView: ($view, $currentView) => {
        const temp = $view.style.height;
        $currentView.style.top = $view.style.top === temp ? '-' + temp : temp;
    }
};

//对含有 view 的顶级div进行统一处理
const mainViews = [];
const layoutViews = [];
{
    const $$views = document.getElementsByClassName("view");
    for (const $view of $$views) {
        Assert.isTrue($view.id, 'the view must have id');
        $view.style.height = $view.style.top = maxHeight + 'px';
        const classList = $view.classList;
        if (classList.contains('layout')) {
            const emptyDiv = document.createElement('div');
            emptyDiv.style.height = titleHeight + 'px';
            emptyDiv.style.width = maxWidth + 'px';
            emptyDiv.style.margin = '0';
            $view.insertBefore(emptyDiv, $view.firstElementChild);
            const closeBtn = document.createElement('div');
            closeBtn.classList.add('lClose', 'fa', 'fa-close');
            const style = closeBtn.style;
            style.top = titleHeight + 'px';
            style.width = fontSize + 'px';
            closeBtn.onclick = DoCss.backView;
            $view.appendChild(closeBtn);
            layoutViews.push($view);
            $view.style.zIndex = 1;
        } else {
            mainViews.push($view);
            $view.style.display = 'flex';
            $view.style.zIndex = 0;
        }
    }
}

DoCss.showView($viewHome);

let auto = false;
const startGame = start => {
    auto = start === true || start === false ? start : !auto;
    $stopMarker.style.display = auto ? "none" : "block";
};

$html.style.display = 'block';