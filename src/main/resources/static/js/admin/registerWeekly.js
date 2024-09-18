$(document).ready(function () {
    $.ajax({
        url: "/admin/member-register-weekly",
        type: "POST",
        dataType: "json",  // Correct dataType
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
                                text: '가입 수'
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