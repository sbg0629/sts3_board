<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<style>
		.uploadResult {
			width: 100%;
			background-color: gray;
		}
		.uploadResult ul {
			display: flex;
			flex-flow: row;
			justify-content: center;
			align-items: center;
		}
		
		.uploadResult ul li {
			list-style: none;
			padding: 10px;
		}
		
		.uploadResult ul li img {
			width: 20px;
		}
	</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
// 		확장자가 exe|sh|js 업로드 금지하기 위한 정규식
		var regex = new RegExp("(.*?)\.(exe|sh|js)$");
		var maxSize = 5242880;//5MB
		
		function checkExtension(fileName, fileSize) {
			if(fileSize > maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			return true;
		}
		
		var cloneObj = $(".uploadDiv").clone();
		console.log("@# cloneObj=>"+cloneObj);
		
		$("#uploadBtn").on("click", function() {
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			console.log(files);
			
			for(var i=0; i<files.length; i++){
// 				파일크기와 종류중에서 거짓이면 리턴
				if(!checkExtension(files[i].name, files[i].size)){
					return;
				}
				
//				파일 정보를 formData에 추가
				formData.append("uploadFile",files[i]);
			}
			
			$.ajax({
// 				컨트롤러단 호출
				 url:"uploadAjaxAction"
// 					processData : 기본은 key/value 를 Query String 으로 전송하는게 true
// 					(파일 업로드는 false)
				,processData: false
//				   contentType : 기본값 : "application / x-www-form-urlencoded; charset = UTF-8"
//				   첨부파일은 false : multipart/form-data로 전송됨
				,contentType: false
				,data: formData
				,type: "post"
				,success: function(result) {
					alert("Uploaded");
					console.log(result);
					
// 					파일정보들을 함수로 보냄
					showUploadedFile(result);
					
// 					파일 업로드 전 초기상태로 설정
					$(".uploadDiv").html(cloneObj.html());
				}//end of success
			});//end of ajax
		});//end of click function
		
		var uploadResult = $(".uploadResult ul");
		
		function showUploadedFile(uploadResultArr) {
			console.log("@# uploadResultArr=>"+uploadResultArr);
			var str="";
			
// 			업로드 파일 갯수만큼 반복
			$(uploadResultArr).each(function(i,obj) {
				console.log("@# obj.fileName=>"+obj.fileName);
				
// 				이미지파일이 아닌 경우
				if (!obj.image) {
// 					썸네일파일 경로+이름
					var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
// 					str += "<li><img src='./resources/img/attach.png'>"+obj.fileName+"</li>";
// 					str += "<li><a href='./download?fileName="+fileCallPath+"'>"
// 							  +"<img src='./resources/img/attach.png'>"+obj.fileName+"</li>";
// 					x 표시 추가
					str += "<li><a href='./download?fileName="+fileCallPath+"'>"
							  +"<img src='./resources/img/attach.png'>"+obj.fileName+"</a>"
							  +"<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"
							  +"</li>";
				}else{
// 					썸네일파일 경로+이름
					var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
// 					원본파일 경로+이름
					var originalPath = obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName;
					console.log("@# originalPath=>"+originalPath);
// 					백슬래시 -> 슬래시로 변경
					originalPath = originalPath.replace(new RegExp(/\\/g),"/");
					console.log("@# originalPath2=>"+originalPath);
					
// 					썸네일파일 경로 추가
// 					str += "<li><img src='./display?fileName="+fileCallPath+"'></li>";
// 					썸네일파일 경로 + 원본 이미지파일 정보
// 					str += "<li><a href=\"javascript:showImage(\'"+originalPath+"\')\"><img src='./display?fileName="
// 							+fileCallPath+"'></li>";
// 					x 표시 추가
					str += "<li><a href=\"javascript:showImage(\'"+originalPath+"\')\"><img src='./display?fileName="
							+fileCallPath+"'></a>"
							+"<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span>"
							+"</li>";
					
	// 				obj.fileName : 업로드파일 이름
// 					str += "<li>"+obj.fileName+"</li>";
				}
				
// 				obj.fileName : 업로드파일 이름
// 				str += "<li>"+obj.fileName+"</li>";
			});//end of each function
			console.log("@# str=>"+str);
			
// 			div class 에 파일 목록 추가
			uploadResult.append(str);
			
			$(".uploadResult").on("click","span",function(){
				var uploadResultItem = $(this).closest("li");
				console.log("@# uploadResultItem=>"+uploadResultItem);
				var targetFile = $(this).data("file");
				var targetType = $(this).data("type");
				
				$.ajax({
					 type:"post"
					,url:"deleteFile"
					,data:{fileName:targetFile, type:targetType}
					,dataType:"text"
					,success: function (result) {
						alert(result);
						uploadResultItem.remove();
					}
				});//end of $.ajax
				
// 				화면에서 삭제
// 				uploadResultItem.remove();
			});//end of span function
		}
		
		$(".bigPictureWrapper").on("click",function(){
			setTimeout(function () {
				$(".bigPictureWrapper").hide();
			},1000);
		});//end of bigPictureWrapper").on("click"
	});//end of ready function
	
// 	원본 이미지 출력
	function showImage(fileCallPath) {
// 		alert("@# fileCallPath=>"+fileCallPath);
		$(".bigPictureWrapper").css("display","flex").show();
		
		$(".bigPicture").html("<img src='./display?fileName="+fileCallPath+"'>")
						.animate({width:"100%",height:"100%"},1000);
	}
</script>
</head>
<body>
	<h1>Upload with Jquery</h1>
	
	<div class="bigPictureWrapper">
		<div class="bigPicture">
		</div>
	</div>
	
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	
	<div class="uploadResult">
		<ul>
		</ul>
	</div>
	
	<button id="uploadBtn">Upload</button>
</body>
</html>














