$(function () {
    now = new Date(), hour = now.getHours();
    if (hour < 6) {
        $("#updateTime").html("凌晨好！");
    } else if (hour < 9) {
        $("#updateTime").html("早上好！");
    } else if (hour < 12) {
        $("#updateTime").html("上午好！");
    } else if (hour < 14) {
        $("#updateTime").html("中午好！");
    } else if (hour < 17) {
        $("#updateTime").html("下午好！");
    } else if (hour < 19) {
        $("#updateTime").html("傍晚好！");
    } else if (hour < 22) {
        $("#updateTime").html("晚上好！");
    } else {
        $("#updateTime").html("夜里好！");
    }
});