document.addEventListener('DOMContentLoaded', () => {
    const nicknameField = document.getElementById('nickname');
    const exPasswordField = document.getElementById('exPassword');
    const newPasswordField = document.getElementById('newPassword');
    const confirmPassField = document.getElementById('confirmPass');
    const saveButton = document.querySelector('button[type="submit"]');

    function validateForm() {
        const isNicknameValid = nicknameField.value.trim() !== '';
        const isExPasswordValid = exPasswordField.value.trim() !== '';
        const isNewPasswordValid = newPasswordField.value.trim() !== '';
        const isConfirmPassValid = confirmPassField.value.trim() !== '';

        // 모든 필드가 유효할 경우 저장 버튼 활성화
        if (isNicknameValid && isExPasswordValid && isNewPasswordValid && isConfirmPassValid) {
            saveButton.disabled = false;
        } else {
            saveButton.disabled = true;
        }
    }

    // 입력 필드의 변화가 생길 때마다 유효성 검사
    nicknameField.addEventListener('input', validateForm);
    exPasswordField.addEventListener('input', validateForm);
    newPasswordField.addEventListener('input', validateForm);
    confirmPassField.addEventListener('input', validateForm);

    // 초기 상태 검사
    validateForm();
});
