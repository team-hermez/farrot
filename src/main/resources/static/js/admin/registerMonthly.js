$(document).ready(function () {
    $.ajax({
        url: "/admin/member-monthly-register",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var signup_count = [];
            var signupDate = [];

            $.each(data, function () {
                signup_count.push(this["signup_count"]);
                signupDate.push(this["signupDate"]);
            });

            const ctx = document.getElementById('registerMonthlyChart').getContext('2d');

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: signupDate,
                    datasets: [{
                        label: '월별 가입자 수',
                        data: signup_count,
                        backgroundColor: 'rgba(0, 123, 255, 0.7)',
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 1,
                        hoverBackgroundColor: 'rgba(0, 123, 255, 0.9)',
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
                                    return tooltipItem.dataset.label + ': ' + tooltipItem.raw + '명';
                                }
                            }
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: '가입 월',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            ticks: {
                                autoSkip: false,
                                maxRotation: 0,
                                minRotation: 0,
                                padding: 10
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '가입자 수(명)',
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
