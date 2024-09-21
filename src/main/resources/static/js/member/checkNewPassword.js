function checkNewPassword() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPass = document.getElementById('confirmPass').value;
    const message = document.getElementById('passwordMatchMessage');

    // 비밀번호가 비어있는 경우
    if (!newPassword || !confirmPass) {
        message.textContent = '비밀번호를 입력해주세요.';
        message.style.color = 'red';
        return; // 함수 종료
    }

    if (newPassword !== confirmPass) {
        message.textContent = '비밀번호가 일치하지 않습니다.';
        message.style.color = 'red';
    } else {
        message.textContent = '비밀번호가 일치합니다.';
        message.style.color = 'green';
    }
}