package com.lgy.spring_upload.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lgy.spring_upload.DTO.AttachFileDTO;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Slf4j
public class UploadController {
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("uploadForm~!!!");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		log.info("uploadFormPost()");
		
		String uploadFolder = "C:\\temp\\upload";
		for (MultipartFile multipartFile : uploadFile) {
			log.info("===============================");
//			getOriginalFilename : 업로드 되는 파일 이름
			log.info("@# 업로드 되는 파일 이름 =>"+multipartFile.getOriginalFilename());
//			getSize : 업로드 되는 파일 크기
			log.info("@# 업로드 되는 파일 크기 =>"+multipartFile.getSize());
			
//			saveFile : 경로하고 파일이름
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
//				transferTo : saveFile 내용을 저장
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping("/uploadJquery")
	public void uploadJquery() {
		log.info("uploadJquery~!!!");
	}
	
//	@PostMapping("/uploadAjaxAction")
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public void uploadAjaxPost(MultipartFile[] uploadFile) {
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("uploadAjaxPost()");
		
		List<AttachFileDTO> list=new ArrayList<AttachFileDTO>();
		
//		기본 경로
		String uploadFolder = "C:\\temp\\upload";
		String uploadFolderPath = getFolder();
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("@# uploadPath =>"+uploadPath);
		
		log.info("@# exists =>"+uploadPath.exists());
//		날짜 폴더가 없으면
		if (uploadPath.exists() == false) {
			// make yyyy/MM/dd folder
			uploadPath.mkdirs();
		}
		
		for (MultipartFile multipartFile : uploadFile) {
			log.info("===============================");
//			getOriginalFilename : 업로드 되는 파일 이름
			log.info("@# 업로드 되는 파일 이름 =>"+multipartFile.getOriginalFilename());
//			getSize : 업로드 되는 파일 크기
			log.info("@# 업로드 되는 파일 크기 =>"+multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			UUID uuid = UUID.randomUUID();
			log.info("@# uuid =>"+uuid);
			
			AttachFileDTO attachFileDTO = new AttachFileDTO();
			attachFileDTO.setFileName(uploadFileName);
			attachFileDTO.setUuid(uuid.toString());
			attachFileDTO.setUploadPath(uploadFolderPath);
			
			uploadFileName = uuid+"_"+uploadFileName;
			log.info("@# uploadFileName =>"+uploadFileName);
			
//			saveFile : 경로하고 파일이름
//			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
//				transferTo : saveFile 내용을 저장
//				폴더에 저장
				multipartFile.transferTo(saveFile);
				
				log.info("@# checkImageType =>"+checkImageType(saveFile));
				
//				참이면 이미지 파일
				if (checkImageType(saveFile)) {
					attachFileDTO.setImage(true);
					
//					썸네일 파일은 s_ 를 앞에 추가
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));
//					썸네일 파일 형식을 100/100 크기로 생성
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				list.add(attachFileDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}//end of catch
		}//end of for
//		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
//	날짜별 폴더 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		
		log.info("@# str=>"+str);
		log.info("@# separator=>"+File.separator);
		
		return str.replace("-", File.separator);
	}
	
//	이미지 여부 체크
	private boolean checkImageType(File file) {
		log.info("@# checkImageType()");
		
		try {
//			이미지파일인지 체크하기 위한 타입(probeContentType)
			String contentType = Files.probeContentType(file.toPath());
			log.info("@# contentType=>"+contentType);
			
//			probeContentType 메소드 버그로 로직 추가
			if (contentType == null) {
				return false;
			}
			
//			startsWith : 파일종류 판단
			log.info("@# startsWith=>"+contentType.startsWith("image"));
			
			return contentType.startsWith("image");//참이면 이미지파일
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;//거짓이면 이미지파일이 아님
	}
	
//	이미지파일을 받아서 화면에 출력(byte 배열타입)
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		log.info("@# fileName=>"+fileName);
		
//		업로드 파일경로+이름
		File file = new File("C:\\temp\\upload\\"+fileName);
		log.info("@# file=>"+file);
		
		ResponseEntity<byte[]> result=null;
		
		try {
			HttpHeaders header = new HttpHeaders();
//			파일타입을 헤더에 추가
			header.add("Content-Type", Files.probeContentType(file.toPath()));
//			파일정보를 byte 배열로 복사+헤더정보+http상태 정상을 결과에 저장
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
//	모든 파일은 내부적으로 bit 값을 가짐(문서, 영상, 이미지, 소리)
//	APPLICATION_OCTET_STREAM_VALUE : 비트 스트림을 재조합하여 파일로 구성
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName) {
		log.info("@# fileName=>"+fileName);
		
//		파일을 리소스(자원)로 변경. 파일을 비트값으로 전환
		Resource resource = new FileSystemResource("C:\\temp\\upload\\"+fileName);
		log.info("@# resource=>"+resource);
		
//		리소스에서 파일명을 찾아서 변수에 저장
		String resourceName = resource.getFilename();
//		uuid 를 제외한 파일명
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		
		HttpHeaders header = new HttpHeaders();
		
		try {
//			헤더에 파일다운로드 정보 추가
			header.add("Content-Disposition", "attachment; filename="
						+new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		윈도우 다운로드시 필요한 정보(리소스, 헤더, 상태OK)
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@PostMapping(value = "/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {
		log.info("@# fileName=>"+fileName);
		File file;
		
		try {
//			file = new File("C:\\temp\\upload\\"+fileName);
//			URLDecoder.decode : 서버에 올라간 파일을 삭제하기 위해서 디코딩
			file = new File("C:\\temp\\upload\\"+URLDecoder.decode(fileName, "UTF-8"));
			log.info("@# file=>"+file);
			
//			폴더 파일 삭제
			file.delete();
			
//			이미지 파일이면 썸네일도 삭제
			if (type.equals("image")) {
//				getAbsolutePath : 절대경로(full path)
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("@# largeFileName=>"+largeFileName);
				
				file = new File(largeFileName);
//				썸네일 삭제
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
//			예외 오류 발생시 not found 처리
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
//		deleted : success 의 result 로 전송
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
	}
}

















