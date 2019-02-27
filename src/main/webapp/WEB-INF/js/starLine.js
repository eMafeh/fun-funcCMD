const context = $canvas.getContext('2d');
let cw, ch, canLineLength;
const stars = [];
const starSize = 70;

//当窗口大小改变时
~function setSize() {
    window.onresize = arguments.callee;
    cw = window.innerWidth;
    ch = window.innerHeight;
    canLineLength = ch / 8;
    $canvas.height = ch;
    $canvas.width = cw;
}();

class Star {
    init() {
        this.w = rand(0, cw);
        this.h = rand(0, ch);
        this.r = 1.5;
        this.speedX = rand(-1, 1);
        this.speedY = rand(-1, 1);
    }

    draw() {
        context.fillStyle = 'white';
        context.beginPath();
        context.arc(this.w, this.h, this.r, 0, Math.PI * 2);
        context.fill();
    }

    move() {
        this.w += this.speedX;
        this.h += this.speedY;
        if (this.w < 0 || this.w > cw) {
            this.speedX *= -1;
        }
        if (this.h < 0 || this.h > ch) {
            this.speedY *= -1;
        }
        this.draw();
    }
}

class Line {
    //星星之间的连线
    initStarLine() {
        this.colorStar = '#6699cc';
        this.colorStop = '#9966cc';
    }

    //鼠标与星星之间的连线
    initNewLine() {
        this.colorStar = '#6699cc';
        this.colorStop = '#ff6666';
    }

    drawLine(ow, oh, nw, nh) {
        const dx = ow - nw;
        const dy = oh - nh;
        const d = Math.sqrt(dx * dx + dy * dy);
        if (d < canLineLength) {
            const line = context.createLinearGradient(ow, oh, nw, nh);
            context.beginPath();
            context.moveTo(ow, oh);
            context.lineTo(nw, nh);
            line.addColorStop(0, this.colorStar);
            line.addColorStop(1, this.colorStop);
            context.StrokeWidth = 1;
            context.strokeStyle = line;
            context.stroke();
            context.restore();
        }
    }
}


//生成范围在min~max之间的随机数
function rand(min, max) {
    return Math.random() * (max - min) + min;
}

function create(num) {
    for (let i = 0; i < num; i++) {
        const star = new Star();
        star.init();
        star.draw();
        stars.push(star);
    }
}

create(starSize);
setTimeout(function () {
    context.clearRect(0, 0, cw, ch);
    for (const i of stars) {
        i.move();
        for (let j = 0; j < stars.length / 2; j++) {
            const line = new Line();
            line.initStarLine();
            line.drawLine(i.w, i.h, stars[j].w, stars[j].h);
        }
    }
    setTimeout(arguments.callee, 1000 / 24);
}, 1000 / 24);
const newLine = function (e) {
    e = e || window.event;
    const mw = e.clientX;
    const mh = e.clientY;
    for (const i of stars) {
        const line = new Line();
        line.initNewLine();
        line.drawLine(i.w, i.h, mw, mh);
    }
};