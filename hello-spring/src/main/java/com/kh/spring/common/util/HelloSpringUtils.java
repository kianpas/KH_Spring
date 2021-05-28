package com.kh.spring.common.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloSpringUtils {

	/**
	 * test.jpg 이라면
	 * @param originalFilename
	 * @return
	 */
	public static String getRenamedFilename(String originalFilename) {
		//확장자 추출
		int beginIndex = originalFilename.lastIndexOf("."); //4
		String ext = originalFilename.substring(beginIndex); //.jpg
		
		//년월일난수 format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");
		DecimalFormat df = new DecimalFormat("000"); //정수부 3자리
		
		return sdf.format(new Date())+ df.format(Math.random()*1000)+ext;
	}

	public static String getPageBar(int cPage, int limit, int totalContents, String url) {
		
		//페이지 바는 변경이 자주 일어나므로 효율적으로 스트링빌더로 하나의 주소를 사용
		StringBuilder pageBar = new StringBuilder();
		
		//게시글이 딱떨어지지 않더라도 페이지 이동이 일어나야함
		//55/10 -> 5.5 하면 5개의 페이지바밖에 안나올수 있음 6p까지는 나와야 모든 글 확인 가능하므로 ceil처리
		final int totalPage = (int)Math.ceil((double)totalContents/limit);
		
		//페이지 바의 크기
		final int pageBarSize = 5;
		url =(url.indexOf("?")>-1)?url+"&":url+"?";
		//1~5까지는 1 6~10은 6
		final int pageStart = (cPage -1)/pageBarSize * pageBarSize + 1;
		final int pageEnd = pageStart + pageBarSize -1;
		
		int pageNo = pageStart;
		
		//1. 이전영역
		if(pageNo == 1) {
			//이전버튼 비활성화
			pageBar.append("<li class='page-item disabled'><a class='page-link' href ='" + url + "cPage="+(pageNo-1)+"'/>prev</a></li>\n");
		}else {
			//이전버튼 활성화
			pageBar.append("<li class='page-item'><a class='page-link' href ='" + url + "cPage="+(pageNo-1)+"'/>prev</a></li>\n");
		}
		
		while(pageNo <= pageEnd && pageNo <= totalPage) {
			if(pageNo == cPage) {
				//현재 페이지 - 링크 비활성화
				pageBar.append("<li class='page-item active'><span class='page-link'>"+pageNo+"</span></li>");
			}else {
				//현재 페이지 - 링크 활성화
				pageBar.append("<li class='page-item'><a class='page-link' href='"+url+"cPage="+(pageNo)+"'/>"+pageNo+"</a></li>\n");
			}
			pageNo++;
		}
		
		if(pageNo>totalPage) {
			
		}else {
			pageBar.append("<li class='page-item'><a class='page-link' href='" + url + "cPage="+(pageNo)+"'/>next</a></li>\n");
		}
		
		return pageBar.toString();
	}

}
