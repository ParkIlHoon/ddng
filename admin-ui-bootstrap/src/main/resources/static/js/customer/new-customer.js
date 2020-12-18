$(function(){
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 초기 데이터 세팅
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 고객 종류
    $.ajax({
        url : SERVER_URL + "/customer-api/customer/type",
        type : "GET",
    }).done((data, textStatus, jqXHR) => {
        $("#type-select").select2({
            placeholder: "종류를 선택해주세요.(필수)",
            allowClear: true,
            width : "100%",
            data : data
        });
    });

    $("#family-select").select2({
        minimumInputLength : 1,
        placeholder : "가족을 선택해주세요. 기존에 가족이 없는 경우 빈 칸으로 두세요.",
        width : "100%",
        allowClear : true,
        ajax: {
            url: SERVER_URL + "/customer-api/family",
            method: "GET",
            data : function (params) {return { keyword: params.term };},
            processResults: function (data) {
                return {
                    results: data.content
                };
            }
        }
    });

    cropper = '';
    let $confirmBtn = $("#confirm-button");
    let $resetBtn = $("#reset-button");
    let $cutBtn = $("#cut-button");
    let $newProfileImage = $("#new-profile-image");
    let $currentProfileImage = $("#current-profile-image");
    let $resultImage = $("#cropped-new-profile-image");
    let $profileImage = $("#profileImage");

    $newProfileImage.hide();
    $cutBtn.hide();
    $resetBtn.hide();
    $confirmBtn.hide();


    $("#submit-button").on("click", function(e){
        // $("#submit-button").attr("disabled", true);
        // $("#submit-button").text("");
        // $("#submit-button").append("<span class=\"spinner-border spinner-border-sm\" role=\"status\" aria-hidden=\"true\"></span>\n  저장중...");

        // var data = $("#new-customer-form").serializeObject();
        // $.ajax({
        //     dataType : "json",
        //     contentType : "application/json; charset=utf-8",
        //     method : "POST",
        //     url : SERVER_URL + "/customer-api/customer/",
        //     data : JSON.stringify(data)
        // }).always(function(data, status){
        //     location.href = "/new";
        // });
    });
    $("#profile-image-file").change(function(e) {
        if (e.target.files.length === 1) {
            const reader = new FileReader();
            reader.onload = e => {
                if (e.target.result) {
                    if (!e.target.result.startsWith("data:image")) {
                        alert("이미지 파일을 선택하세요.");
                        return;
                    }

                    let img = document.createElement("img");
                    img.id = 'new-profile';
                    img.src = e.target.result;
                    img.setAttribute('width', '100%');

                    $newProfileImage.html(img);
                    $newProfileImage.show();
                    $currentProfileImage.hide();

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
        $currentProfileImage.show();
        $newProfileImage.hide();
        $resultImage.hide();
        $resetBtn.hide();
        $cutBtn.hide();
        $confirmBtn.hide();
        $profileImage.val('');
    });
    $cutBtn.click(function () {
        let dataUrl = cropper.getCroppedCanvas().toDataURL();

        if (dataUrl.length > 10 * 1024 * 1024) {
            alert("이미지 파일이 너무 큽니다. 10MB 보다 작은 파일을 사용하세요. 현재 이미지 사이즈 " + dataUrl.length);
            return;
        }

        let newImage = document.createElement("img");
        newImage.id = "cropped-new-profile-image";
        newImage.src = dataUrl;
        newImage.width = 125;
        $resultImage.html(newImage);
        $resultImage.show();
        $confirmBtn.show();
        $confirmBtn.click(function () {
            $newProfileImage.html(newImage);
            $cutBtn.hide();
            $confirmBtn.hide();
            $profileImage.val(dataUrl.replaceAll("data:image/png;base64,", ""));
        });
    });
});