function checkNickname() {
    const nickname = document.getElementById('nickname').value;
    // AJAX 요청 등으로 서버와 통신하여 중복 체크
    alert('닉네임 중복 체크: ' + nickname);
}