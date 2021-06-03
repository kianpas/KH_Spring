<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="게시판" name="title" />
</jsp:include>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<style>
/*글쓰기버튼*/
input#btn-add {
	float: right;
	margin: 0 0 15px;
}

tr[data-no] {
	cursor: pointer;
}
</style>
<script>


function goBoardForm(){
	location.href = "${pageContext.request.contextPath}/board/boardForm.do";
}

$(()=>{
	$("tr[data-no]").click(e =>{
	//화살표함수안에서는 this는 e.target이 아니다
	//console.log(e.target); //클릭한 td태그 -> 부모tr로 이벤트 전파(bubbling)
	var $tr = $(e.target).parent();
	var no = $tr.data("no");
	location.href = "${pageContext.request.contextPath}/board/boardDetail.do?no="+no;
	})

$("#searchTitle").autocomplete({
	 source: function(request, response){
		//사용자 입력값 전달 ajax 요청 -> success함수안에서 response호출
   		$.ajax({
	    	url:"${pageContext.request.contextPath}/board/boardSearch.do",
	    	data : {
	    			search : request.term
	    			},
	    		//메소드는 success(data)로 사용가능
	    	success : function(data) {
	    				console.log(data);
					const {searchList} = data;
					
					 //배열
					const arr = searchList.map(({no, title})=>({
								label:title,
								value : title,
								no
						}));
						
					console.log(arr);
	    			/* 	
	    				var arr = data.searchList;
	    				
	    					arr = $.map(arr, function(board){
								console.log(board.title);
	    						return {	
	    							label:board.title, //노출텍스트
	    							value:board.no // 내무적 처리될값
	    						}
	    					})
	    					console.log(arr);
	    					*/
	    					//콜백함수 호출
	    					response(arr); 
    					 
	    			},
	    	error : function(xhr, status, err) {
	    				console.log(xhr, status, err);
	    	}

	    })
	  },
	    select:function(event, selected){
	    			//클릭했을 때 , 해당 게시그 상세 페이지로 이동
	    			const {item:{no}} = selected;
	    			console.log(event);
	    			console.log(selected.item.no);
	    			console.log(no);

	    			location.href="${pageContext.request.contextPath}/board/boardDetail.do?no="+no;
	    			
	   },
	    focus:function(event, focused){
	    	return false;

		    	}
			
	    });
})
</script>
<section id="board-container" class="container">
	<input type="search" placeholder="제목 검색..." id="searchTitle"
		class="form-control col-sm-3 d-inline" value="" /> <input
		type="button" value="글쓰기" id="btn-add" class="btn btn-outline-success"
		onclick="goBoardForm();" />
	<table id="tbl-board" class="table table-striped table-hover">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>첨부파일</th>
			<!-- 첨부파일 있을 경우, /resources/images/file.png 표시 width: 16px-->
			<th>조회수</th>
		</tr>
		<c:forEach items="${list}" var="b">
			<tr data-no="${b.no}">
				<td>${b.no}</td>
				<td>${b.title}</td>
				<td>${b.memberId}</td>
				<td><fmt:formatDate value="${b.regDate}" pattern="yy-MM-dd" /></td>
				<td><c:if test="${b.hasAttachment}">
						<img alt=""
							src="${pageContext.request.contextPath}/resources/images/file.png"
							width="16px" />
					</c:if></td>
				<!-- 첨부파일 있을 경우, /resources/images/file.png 표시 width: 16px-->
				<td>${b.readCount}</td>
			</tr>
		</c:forEach>
	</table>
	<nav>
		<ul class="pagination justify-content-center">${pageBar}
		</ul>
	</nav>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
