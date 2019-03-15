class Layout {
    static alert(msg, callBack) {
        const fn = Layout.alert;
        Layout.alert = (msg, callBack) => console.log('too more', msg, callBack);
        callBack = typeof callBack === 'function' ? callBack : EMPTY_FUNCTION;
        $console.style.display = 'flex';
        $console.innerHTML = msg;
        $console.onclick = () => {
            Layout.alert = fn;
            $console.style.display = 'none';
            callBack();
        };
    };
}