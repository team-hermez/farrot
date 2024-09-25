$(document).ready(function () {
    $.ajax({
        url: "/admin/product-week-totalSales",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var week = [];
            var total_sales = [];

            $.each(data, function () {
                week.push(this["week"]);
                total_sales.push(this["total_sales"]);
            });

            const ctx = document.getElementById('weeklyTotalChart').getContext('2d');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: week,
                    datasets: [{
                        label: '주간 매출',
                        data: total_sales,
                        backgroundColor: 'rgba(0, 123, 255, 0.2)',
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 2,
                        fill: true,
                        pointRadius: 5,
                        pointHoverRadius: 7,
                    }]
                },
                options: {
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                }
                            },
                        },
                        tooltip: {
                            enabled: true,
                            mode: 'index',
                            intersect: false,
                        },
                    },
                    responsive: true,
                    scales: {
                        x: {
                            ticks: {
                                autoSkip: false,
                                maxRotation: 0,
                                minRotation: 0,
                                paddingLeft: 10,
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '매출 (원)',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            grid: {
                                color: 'rgba(200, 200, 200, 0.5)',
                            },
                        }
                    },
                    animation: {
                        tension: {
                            duration: 1000,
                            easing: 'easeInOutQuad',
                            from: 0.1,
                            to: 0.5,
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
