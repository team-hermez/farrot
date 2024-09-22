function checkExPassword() {
    const exPassword = $('#exPassword').val();
    const checkPasswordButton = $('#checkExPasswordButton');

    if (!exPassword) {
        alert("현재 비밀번호를 입력해 주세요.");
        return;
    }

    $.ajax({
        url: "/member/check/expassword", // 서버의 URL에 맞게 수정
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ exPassword: exPassword }), // JSON 형태로 데이터 전송
        success: function(response) {
            if (response.isValid) {
                alert("현재 비밀번호가 확인되었습니다.");
                checkPasswordButton.text("확인 완료").css("background-color", "green");
            } else {
                alert("현재 비밀번호가 올바르지 않습니다.");
                checkPasswordButton.text("비밀번호 확인 실패").css("background-color", "red");
            }
        },
        error: function() {
            alert("오류가 발생했습니다. 다시 시도해 주세요.");
        }
    });
}