function previewImage(event) {
    const input = event.target;
    const file = input.files[0];
    const imagePreview = document.getElementById('profileImagePreview');

    // 이미지 파일인지 확인
    const allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
    if (file && allowedExtensions.test(file.name)) {
        const reader = new FileReader();

        reader.onload = function(e) {
            imagePreview.src = e.target.result; // 선택한 이미지로 미리보기
        };

        reader.readAsDataURL(file);
    } else {
        alert('이미지 파일만 업로드할 수 있습니다.'); // 오류 메시지
        input.value = ''; // 잘못된 파일 선택 초기화
        imagePreview.src = ''; // 미리보기 초기화
    }
}
