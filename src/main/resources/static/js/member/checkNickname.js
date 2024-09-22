function checkNickname() {
    const nickname = $('#nickname').val();
    const currentNickname = $('#currentNickname').val(); // 현재 닉네임 가져오기

    if (!nickname) {
        alert("닉네임을 입력해야 합니다.");
        return;
    }

    // 현재 닉네임과 비교
    if (nickname === currentNickname) {
        alert("현재 사용 중인 닉네임입니다.");
        $('#checkNicknameButton').text("확인 완료").css("background-color", "green");
        isNicknameChecked = true;
        toggleSubmitButton();
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
