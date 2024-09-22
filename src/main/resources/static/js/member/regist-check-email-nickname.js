let isEmailChecked = false;
let isNicknameChecked = false;

function checkEmail() {
    const email = $('#email').val();
    if (!email) {
        alert("이메일을 입력해야 합니다.");
        return;
    }

    $.ajax({
        url: "/member/check/email",
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: email }), // JSON 형태로 데이터 전송
        success: function(response) {
            if (response) {
                $('#checkEmailButton').text("확인 완료").css("background-color", "green");
                isEmailChecked = true;
            } else {
                $('#checkEmailButton').text("중복 이메일").css("background-color", "red");
                isEmailChecked = false;
            }
            toggleSubmitButton();
        }
    });
}

function checkNickname() {
    const nickname = $('#nickname').val();
    if (!nickname) {
        alert("닉네임을 입력해야 합니다.");
        return;
    }

    $.ajax({
        url: "/member/check/nickname",
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ nickname: nickname }), // JSON 형태로 데이터 전송
        success: function(response) {
            if (response) {
                $('#checkNicknameButton').text("확인 완료").css("background-color", "green");
                isNicknameChecked = true;
            } else {
                $('#checkNicknameButton').text("중복 닉네임").css("background-color", "red");
                isNicknameChecked = false;
            }
            toggleSubmitButton();
        }
    });
}

function toggleSubmitButton() {
    if (isEmailChecked && isNicknameChecked) {
        $('#submitBtn').show(); // 제출 버튼 보이기
    } else {
        $('#submitBtn').hide(); // 제출 버튼 숨기기
    }
}
