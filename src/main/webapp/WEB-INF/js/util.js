const EMPTY_FUNCTION = () => {
};

class Assert {
    static isTrue(b, msg) {
        if (!b) throw new Error(msg);
    }
}

class Collection {
    static getTarget(list, key, value) {
        for (const obj of list) if (obj[key] === value) return obj;
        return null;
    }
}

class Ajax {
    static get(method, param, backFn) {
        const xhr = new XMLHttpRequest();
        let message = "";
        if (typeof param === 'object')
            for (const key in param) if (param.hasOwnProperty(key)) message += `&${encodeURIComponent(key)}=${encodeURIComponent(param[key])}`;
        xhr.open("GET", `http://qianrui.fun/php/control.php?method=${method}${message}`, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                //&& (xhr.status === 200 || xhr.status === 304)
                if (typeof backFn === 'function') backFn(JSON.parse(xhr.responseText));
            }
        };
        xhr.send();
    }

    static post(method, param, backFn) {
        console.log(method, param, backFn);
        const xhr = new XMLHttpRequest();
        let message = "";
        if (typeof param === 'object')
            for (const key in param) if (param.hasOwnProperty(key)) message += `&${encodeURIComponent(key)}=${encodeURIComponent(param[key])}`;
        xhr.open("POST", "http://qianrui.fun/php/control.php", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                //&& (xhr.status === 200 || xhr.status === 304)
                console.log(xhr.responseText);
                if (typeof backFn === 'function') backFn(JSON.parse(xhr.responseText));
            }
        };
        xhr.send(`method=${method}${message}`);
    }
}

class TouchUtil {
    static listen(
        //目标element 累计像素
        target, unitLength,
        //左滑 左滑系数
        leftSlideFunc, leftCoefficient,
        //右滑 右滑系数
        rightSlideFn, rightCoefficient,
        //上滑 上滑系数
        topSlideFn, topCoefficient,
        //下滑 下滑系数
        bottomSlideFn, bottomCoefficient,
        //单指双击 间隔
        doubleClickFn, doubleClickMill,
        //双指同时单击
        clickAndClickFn,
        //单指按住 另一指单击
        holdAndClickFn
    ) {
        const clearFn = a => typeof a === "function" ? a : () => {
        };
        const clearNum = (a, b) => typeof a === "number" ? a : b;
        console.assert(target, "target must not be null");
        console.assert(unitLength, "unitLength must not be null");
        const lSlideFn = clearFn(leftSlideFunc);
        const lCoefficient = clearNum(leftCoefficient, 1);
        const rSlideFn = clearFn(rightSlideFn);
        const rCoefficient = clearNum(rightCoefficient, 1);
        const tSlideFn = clearFn(topSlideFn);
        const tCoefficient = clearNum(topCoefficient, 1);
        const bSlideFn = clearFn(bottomSlideFn);
        const bCoefficient = clearNum(bottomCoefficient, 1);
        const dClickFn = clearFn(doubleClickFn);
        const dClickMill = clearNum(doubleClickMill, 300);
        const clickAndClick = clearFn(clickAndClickFn);
        const holdAndClick = clearFn(holdAndClickFn);

        let xLast;
        let yLast;
        let first = null;
        let lastTime = 0;
        target.ontouchstart = a => {
            Touch.target = a.target;
            a.preventDefault();
            const touch = a.changedTouches[0];
            newLine(touch);
            //同时存在两个手指
            if (first !== null) {
                const time = a.timeStamp - lastTime;
                if (time < 20) {
                    clickAndClick();
                } else {
                    holdAndClick();
                }
                lastTime = 0;
            } else if (a.timeStamp - lastTime < dClickMill) {
                dClickFn();
                lastTime = 0;
            } else {
                first = touch.identifier;
                lastTime = a.timeStamp;
                xLast = touch.clientX;
                yLast = touch.clientY;
            }
        };

        target.ontouchmove = a => {
            Touch.target = a.target;
            a.preventDefault();
            const touch = a.changedTouches[0];
            newLine(touch);
            if (touch.identifier !== first) return;

            const dx = touch.clientX - xLast;
            const dy = touch.clientY - yLast;
            const m_x = Math.abs(dx);
            const m_y = Math.abs(dy);

            if (m_x >= unitLength) {
                const times = Math.floor(m_x / unitLength);
                if (dx > 0) {
                    xLast += times * unitLength;
                    for (let i = 0; i < times; i += rCoefficient)
                        rSlideFn();
                } else {
                    xLast -= times * unitLength;
                    for (let i = 0; i < times; i += lCoefficient)
                        lSlideFn();
                }
            } else if (m_y >= unitLength) {
                const times = Math.floor(m_y / unitLength);
                if (dy > 0) {
                    yLast += times * unitLength;
                    for (let i = 0; i < times; i += bCoefficient)
                        bSlideFn();
                } else {
                    yLast -= times * unitLength;
                    for (let i = 0; i < times; i += tCoefficient)
                        tSlideFn();
                }
            }
        };

        target.ontouchend = a => {
            const touch = a.changedTouches[0];
            if (touch.identifier === first) first = null;
        };
    };

}