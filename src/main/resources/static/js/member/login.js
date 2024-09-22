function openNaverLogin() {
    const url = '/oauth2/authorization/naver'; // Naver 로그인 URL
    const options = 'width=600,height=600,resizable=yes,scrollbars=yes,status=yes';

    const naverLoginWindow = window.open(url, 'Naver Login', options);

    // 부모 창에서 닫기
    const interval = setInterval(() => {
        if (naverLoginWindow.closed) {
            clearInterval(interval);
            // 이메일과 제공자를 쿼리 파라미터로 받기
            const email = new URLSearchParams(window.location.search).get('email');
            const provider = new URLSearchParams(window.location.search).get('provider');
            if (email && provider) {
                window.location.href = `/member/register?email=${email}&provider=${provider}`;
            }
        }
    }, 1000);
}
