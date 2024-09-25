$(document).ready(function () {
    $.ajax({
        url: "/admin/category-price-average",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var averagePrice = [];
            var categoryCode = [];
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

            $.each(data, function () {
                averagePrice.push(this["averagePrice"]);
                categoryCode.push(this["categoryCode"]);
            });

            const ctx = document.getElementById('categoryAverageChart').getContext('2d');

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: categoryCode,
                    datasets: [{
                        label: '카테고리별 평균 가격',
                        data: averagePrice,
                        backgroundColor: backgroundColors.slice(0, categoryCode.length),
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 1,
                        hoverBorderColor: 'rgba(0, 123, 255, 1)',
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
                            }
                        },
                        tooltip: {
                            enabled: true,
                            callbacks: {
                                label: function (tooltipItem) {
                                    return tooltipItem.dataset.label + ': ' + tooltipItem.raw + ' 원';
                                }
                            }
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: {
                            ticks: {
                                autoSkip: false,
                                maxTicksLimit: 10,
                                maxRotation: 45,
                                minRotation: 45,
                                padding: 10
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '평균 가격 (원)',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            grid: {
                                color: 'rgba(200, 200, 200, 0.5)',
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
