$(function(){
    cropper = '';
    let $confirmBtn = $("#confirm-button");
    let $resetBtn = $("#reset-button");
    let $cutBtn = $("#cut-button");
    let $newItemImage = $("#new-item-image");
    let $currentItemImage = $("#current-item-image");
    let $resultImage = $("#cropped-new-item-image");
    let $itemImage = $("#itemImage");

    $newItemImage.hide();
    $cutBtn.hide();
    $resetBtn.hide();
    $confirmBtn.hide();

    $("#item-image-file").change(function(e) {
        if (e.target.files.length === 1) {
            const reader = new FileReader();
            reader.onload = e => {
                if (e.target.result) {
                    if (!e.target.result.startsWith("data:image")) {
                        alert("이미지 파일을 선택하세요.");
                        return;
                    }

                    let img = document.createElement("img");
                    img.id = 'new-item';
                    img.src = e.target.result;
                    img.setAttribute('width', '100%');

                    $newItemImage.html(img);
                    $newItemImage.show();
                    $currentItemImage.hide();

                    let $newImage = $(img);
                    $newImage.cropper({aspectRatio: 1});
                    cropper = $newImage.data('cropper');

                    $cutBtn.show();
                    $confirmBtn.hide();
                    $resetBtn.show();
                }
            };

            reader.readAsDataURL(e.target.files[0]);
        }
    });
    $resetBtn.click(function() {
        $currentItemImage.show();
        $newItemImage.hide();
        $resultImage.hide();
        $resetBtn.hide();
        $cutBtn.hide();
        $confirmBtn.hide();
        $itemImage.val('');
    });
    $cutBtn.click(function () {
        let dataUrl = cropper.getCroppedCanvas().toDataURL();

        if (dataUrl.length > 10 * 1024 * 1024) {
            alert("이미지 파일이 너무 큽니다. 10MB 보다 작은 파일을 사용하세요. 현재 이미지 사이즈 " + dataUrl.length);
            return;
        }

        let newImage = document.createElement("img");
        newImage.id = "cropped-new-item-image";
        newImage.src = dataUrl;
        newImage.width = 125;
        $resultImage.html(newImage);
        $resultImage.show();
        $confirmBtn.show();
        $confirmBtn.click(function () {
            $newItemImage.html(newImage);
            $cutBtn.hide();
            $confirmBtn.hide();
            $itemImage.val(dataUrl.replaceAll("data:image/png;base64,", ""));
        });
    });
});