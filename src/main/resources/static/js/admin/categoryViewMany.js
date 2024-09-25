$(document).ready(function () {
    $.ajax({
        url: "/admin/category-view-many",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var categoryCode = [];
            var totalViews = [];
            var backgroundColors = [
                'rgba(255, 99, 132, 0.5)',
                'rgba(54, 162, 235, 0.5)',
                'rgba(255, 206, 86, 0.5)',
                'rgba(75, 192, 192, 0.5)',
                'rgba(153, 102, 255, 0.5)',
                'rgba(255, 159, 64, 0.5)',
                'rgba(255, 0, 0, 0.5)',
                'rgba(0, 255, 0, 0.5)',
                'rgba(0, 0, 255, 0.5)',
                'rgba(255, 255, 0, 0.5)',
                'rgba(0, 255, 255, 0.5)',
                'rgba(255, 0, 255, 0.5)',
                'rgba(128, 0, 128, 0.5)',
                'rgba(128, 128, 0, 0.5)',
                'rgba(0, 128, 128, 0.5)',
                'rgba(192, 192, 192, 0.5)',
                'rgba(128, 128, 128, 0.5)',
                'rgba(0, 0, 0, 0.5)'
            ];

            $.each(data, function (index) {
                categoryCode.push(this["categoryCode"]);
                totalViews.push(this["totalViews"]);
            });

            const ctx = document.getElementById('categoryViewChart').getContext('2d');

            new Chart(ctx, {
                type: 'polarArea',
                data: {
                    labels: categoryCode,
                    datasets: [{
                        label: '이번 주 카테고리별 조회수',
                        data: totalViews,
                        backgroundColor: backgroundColors.slice(0, categoryCode.length),
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 1,
                    }]
                },
                options: {
                    plugins: {
                        legend: {
                            display: true,
                            position: 'right',
                            labels: {
                                font: {
                                    size: 14,
                                    weight: 'bold'
                                }
                            }
                        },
                    },
                    responsive: true,
                    scales: {
                        r: {
                            beginAtZero: true,
                            ticks: {
                                display: true,
                                color: 'rgba(0, 0, 0, 0.6)',
                            }
                        }
                    },
                    layout: {
                        padding: 0
                    }
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data:", error);
        }
    });
});
