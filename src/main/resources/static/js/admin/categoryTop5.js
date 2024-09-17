$(document).ready(function () {
    $.ajax({
        url: "/admin/category-sales",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var listHtml = '';

            $.each(data, function () {
                listHtml += '<li>' + this["categoryCode"] + ': ' + this["count"] + '</li>';
            });

            $('#topCategories ul').html(listHtml);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data:", error);
        }
    });
});