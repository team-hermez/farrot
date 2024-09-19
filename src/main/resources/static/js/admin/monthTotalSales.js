$(document).ready(function () {
    $.ajax({
        url: "/admin/product-month-totalSales",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var month = [];
            var total_sales = [];

            $.each(data, function () {
                month.push(this["month"]);
                total_sales.push(this["total_sales"])
            });

            const ctx = document.getElementById('monthTotalChart').getContext('2d');

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: month,
                    datasets: [{
                        label: '총매출',
                        data: total_sales,
                        backgroundColor: 'rgba(0, 123, 255, 0.5)',
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 1,
                        labels : {
                        },
                    }]

                },
                options: {
                    plugins: {
                        legend: {
                            display: false
                        },
                    },
                    responsive: true,
                    scales: {x: {
                            ticks: {
                                autoSkip: false,
                                maxRotation: 0,
                                minRotation: 0,
                                paddingLeft: 100
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '매출'
                            }
                        }
                    }
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data:", error);
        }
    });
});