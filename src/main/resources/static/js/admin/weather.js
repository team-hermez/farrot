$(document).ready(function () {
    function convertTime() {
        var now = new Date();
        var month = now.getMonth() + 1;
        var date = now.getDate();
        return month + '월 ' + date + '일 현재날씨';
    }
    var currentTime = convertTime();
    $('#weatherApp .nowtime').text(currentTime); // 구체적인 선택자 사용
});

$.getJSON('https://api.openweathermap.org/data/2.5/weather?q=Seoul,kr&appid=46b55a9f61cc588200575a3dda8e3069&units=metric',
    function (WeatherResult) {
        $('.SeoulNowtemp').append(WeatherResult.main.temp + "°C");
        $('.SeoulLowtemp').append(WeatherResult.main.temp_min + "°C");
        $('.SeoulHightemp').append(WeatherResult.main.temp_max + "°C");

        var weathericonUrl = '<img src="http://openweathermap.org/img/wn/' + WeatherResult.weather[0].icon + '.png" alt="' + WeatherResult.weather[0].description + '"/>';
        $('.weather-icon').html(weathericonUrl);
    });