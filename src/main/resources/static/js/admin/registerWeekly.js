$(document).ready(function () {
    $.ajax({
        url: "/admin/member-register-weekly",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var count = [];
            var SignupDate = [];

            $.each(data, function () {
                count.push(this["count"]);
                SignupDate.push(this["SignupDate"]);
            });

            const ctx = document.getElementById('mainChart').getContext('2d');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: SignupDate,
                    datasets: [{
                        label: '일주일 가입자 수',
                        data: count,
                        backgroundColor: 'rgba(0, 123, 255, 0.3)',
                        borderColor: 'rgba(0, 123, 255, 1)',
                        borderWidth: 2,
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
                            }
                        },
                        tooltip: {
                            enabled: true,
                            mode: 'index',
                            intersect: false,
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: '날짜',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            ticks: {
                                autoSkip: false,
                                maxRotation: 0,
                                minRotation: 0,
                                paddingLeft: 10
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '가입 수(명)',
                                font: {
                                    size: 16,
                                    weight: 'bold'
                                }
                            },
                            ticks: {
                                callback: function (value) {
                                    if (Number.isInteger(value)) {
                                        return value;
                                    }
                                    return null;
                                },
                                stepSize: 1
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
